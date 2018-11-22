package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.MobCharacters.MobPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sombright.vizix.simultanea.MainActivity.TAG;

public class PlayActivitySingle extends AppCompatActivity implements View.OnClickListener, PlayersViewAdapter.OnClickPlayerListener, OpenTriviaDatabase.Listener {

    private int currentLevel = 0;
    private int amountOfCurrentLevelMobs = 0;
    private CountDownTimer mQuestionLifeSpanCounter = new CountDownTimer(10000, 1000) { // 1000 = 1 sec

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(TAG, "mQuestionLifeSpanCounter " + millisUntilFinished/1000);
            int progress = (int) (millisUntilFinished/100);
            counter.setProgress(progress);
        }

        @Override
        public void onFinish() {
            pickQuestion();
        }
    };
    private final static int MESSAGE_DURATION_MS = 1500;
    private final static int LONG_MESSAGE_DURATION_MS = 3000;
    TextView questionText;
    private Random random = new Random();
    private LinearLayout answersLayout;
    private LinearLayout battleOptionsLayout;
    private Player me;
    private PlayersViewAdapter mPlayersViewAdapter;
    private GridView otherPlayersLayout;
    private TextView textViewLocalPlayer;
    private ProgressBar localPlayerHealth;
    private ProgressBar counter;
    private ImageView localPlayerThumb;
    private Button buttonQuestion, buttonAnswers, buttonBattle;
    private Button buttonHeal, buttonAttack, buttonDefend;
    private Handler handler = new Handler();
    private MediaPlayer mMusic;
    private Animation fadeOutAnimation;
    private PreferencesProxy mPrefs;

    private QuizPool quizPool = null;
    private OpenTriviaDatabase opentdb = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Code for hiding the status bar from
         * https://developer.android.com/training/system-ui/status.html
         */
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }

        mPrefs = new PreferencesProxy(this);

        // Retrieve a copy of the settings (cannot change during the game)
        String name = mPrefs.getMultiPlayerAlias();

        me = new Player(this);
        me.setName(name);
        me.setCharacter(mPrefs.getCharacter());
        Log.d(TAG, "Playing as character " + me.getCharacter());

        // Apply the initial layout (we will modify below)
        setContentView(R.layout.activity_play_single);

        // These are the portions of the layout that we will modify during the game
        questionText = findViewById(R.id.questionTextView);
        answersLayout = findViewById(R.id.answersLayout);
        battleOptionsLayout = findViewById(R.id.battleOptionsLayout);
        otherPlayersLayout = findViewById(R.id.otherPlayersLayout);
        textViewLocalPlayer = findViewById(R.id.textViewLocalPlayer);

        buttonQuestion = findViewById(R.id.buttonQuestion);
        buttonAnswers = findViewById(R.id.buttonAnswers);
        buttonBattle = findViewById(R.id.buttonBattle);

        buttonHeal = findViewById(R.id.buttonHeal);
        buttonAttack = findViewById(R.id.buttonAttack);
        buttonDefend = findViewById(R.id.buttonDefend);

        localPlayerThumb = findViewById(R.id.imageViewLocalPlayer);
        localPlayerHealth = findViewById(R.id.localPlayerHealth);
        localPlayerHealth.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        counter = findViewById(R.id.counter);
        counter.getProgressDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);


        buttonQuestion.setEnabled(false);
        buttonAnswers.setEnabled(true);
        buttonBattle.setEnabled(true);

        updateLocalPlayerUi();

        // Prepare an animation object for when we loose a life
        fadeOutAnimation = new AlphaAnimation(1, 0);
        fadeOutAnimation.setDuration(MESSAGE_DURATION_MS / 6);
        fadeOutAnimation.setInterpolator(new LinearInterpolator());
        fadeOutAnimation.setRepeatCount(Animation.INFINITE);
        fadeOutAnimation.setRepeatMode(Animation.REVERSE);
        // Remove the fake content we put in the initial layout (for designing)
        mPlayersViewAdapter = new PlayersViewAdapter(this, R.layout.players_example_item, this);
        otherPlayersLayout.setAdapter(mPlayersViewAdapter);

        questionText.setText(R.string.waiting_for_master);
        answersLayout.removeAllViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // Background music
        mMusic = MediaPlayer.create(this, R.raw.fight2);
        mMusic.setLooping(true);

        //TODO -Yves: Replace this with a Switch Case instead of If/Else statements.
        //TODO -Yves: For future reference, amountOfCurrentLevelMobs should count the amount of objects automatically in a Level Class and use that as its value.
        if (currentLevel == 0){
            amountOfCurrentLevelMobs = 3;
        }
        if (currentLevel == 1){
            amountOfCurrentLevelMobs = 2;
        }
        if (currentLevel == 2){
            amountOfCurrentLevelMobs = 2;
        }
        if (currentLevel == 3){
            amountOfCurrentLevelMobs = 2;
        }

        for (int i = 0; i < amountOfCurrentLevelMobs; i++) {
            // Find a unique name
            String characterName = null, playerName = null;
            int num = 0; // Add a number when the name already exists
            Player player = null;
            do {
                num++;
                for (MobModel mob: MobPool.mobList) {
                    // Skip unplayable characters
                    if (!mob.isPlayable()) {
                        continue;
                    }
                    characterName = mob.getName(PlayActivitySingle.this);
                    playerName = characterName;
                    if (num > 1) {
                        playerName += " " + num;
                    }
                    player = mPlayersViewAdapter.getPlayerByName(playerName);
                    if (player == null) {
                        break;
                    }
                }
            } while (player != null);
            player = new Player(this);
            player.setName(playerName);
            if(currentLevel == 0){
                player.setMob(characterName);
            }

            if(currentLevel == 1){
                player.setMobsLevelOne(characterName);
            }
            if(currentLevel == 2){
                player.setMobsLevelTwo(characterName);
            }
            if(currentLevel == 3){
                player.setMobsLevelThree(characterName);
            }
            mPlayersViewAdapter.add(player);
        }

        if (mPrefs.shouldUseOpenTriviaDatabase()) {
            opentdb = new OpenTriviaDatabase(this);
            opentdb.setQuestionAttributes(OpenTriviaDatabase.CATEGORY_ANY,
                    OpenTriviaDatabase.DIFFICULTY_ANY,
                    OpenTriviaDatabase.TYPE_ANY);
            opentdb.setListener(this);
        } else {
            quizPool = new QuizPool(this);
        }
        Log.d(TAG, "calling pickQuestion from onStart");
        pickQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mMusic.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mMusic.pause();
        mQuestionLifeSpanCounter.cancel();

        // Update the player profile if needed
        if (me.getPoints() > mPrefs.getHighScore()) {
            mPrefs.setHighScore(me.getPoints());
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        mMusic.stop();
        mMusic.release();
        super.onStop();
    }

    private void updateLocalPlayerUi() {
        Log.d(TAG, "updateLocalPlayerUi");
        // Set our character image
        AnimationDrawable animationDrawable = me.getAnimation();
        if (localPlayerThumb.getDrawable() != animationDrawable) {
            localPlayerThumb.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }
        if (!textViewLocalPlayer.getText().equals(me.getName())) {
            textViewLocalPlayer.setText(me.getName());
        }
        if (localPlayerHealth.getProgress() != me.getHealth()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                localPlayerHealth.setProgress(me.getHealth(), true);
            } else {
                localPlayerHealth.setProgress(me.getHealth());
            }
        }
        if (me.getPoints() == 0) {
            //TODO disable attack?
            buttonAttack.setEnabled(false);
            buttonHeal.setEnabled(false);
        } else if (me.getPoints() >= 1) {
            buttonAttack.setEnabled(true);
        }
        else if (me.getPoints() >= 2) {
            buttonHeal.setEnabled(true);
        }
    }

    // Go through a sub-tree of views and call setEnabled on every leaf (views)
    private void recursiveSetEnabled(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled(enable, (ViewGroup) child);
            }
        }
    }

    private void pickQuestion() {
        Log.d(TAG, "pickQuestion");

        QuizPool.Entry entry;
        if (opentdb != null) {
            OpenTriviaDatabase.Question question = opentdb.getQuestion();
            if (question == null) {
                // We'll come back later in onQuestionsAvailable callback
                waitingForQuestion = true;
                return;
            }
            // Convert to QuizPool.Question
            int type;
            switch (question.type) {
                case OpenTriviaDatabase.TYPE_MULTIPLE_CHOICE:
                    type = QuizPool.TYPE_MULTIPLE_CHOICE;
                    break;
                case OpenTriviaDatabase.TYPE_TRUE_OR_FALSE:
                    type = QuizPool.TYPE_TRUE_FALSE;
                    break;
                default:
                    Log.e(TAG, "Unknown question type " + question.type);
                    type = QuizPool.TYPE_MULTIPLE_CHOICE;
                    break;
            }
            List<QuizPool.Answer> answers = new ArrayList<>();
            for (String answer: question.incorrect_answers)
                answers.add(new QuizPool.Answer(answer, false));
            answers.add(random.nextInt(answers.size()),
                    new QuizPool.Answer(question.correct_answer, true));
            entry = new QuizPool.Entry(question.question, type, answers);
        } else {
            entry = quizPool.getQuestion();
        }
        questionText.setText(entry.question);

        // We clear-out the old buttons, and create new ones for the current question
        answersLayout.removeAllViews();
        for (QuizPool.Answer answer : entry.answers) {
            Button button = new Button(this);
            button.setText(answer.text);
            button.setTag(R.id.id_answer, answer);
            button.setOnClickListener(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            button.setLayoutParams(lp);
            answersLayout.addView(button);
        }

        showQuestion(null);
        mQuestionLifeSpanCounter.start();
    }

    /**
     * Functions to change the content of the bottom-right box
     */
    public void showQuestion(View view) {
        Log.d(TAG, "showQuestion");
        answersLayout.setVisibility(View.INVISIBLE);
        battleOptionsLayout.setVisibility(View.INVISIBLE);
        questionText.setVisibility(View.VISIBLE);
        questionText.bringToFront();
        buttonQuestion.setEnabled(false);
        buttonAnswers.setEnabled(true);
        buttonBattle.setEnabled(true);
        abortCombatMode();
    }

    public void abortCombatMode() {
        if (me.getCombatMode() != Player.COMBAT_MODE_ATTACK) {
            mPlayersViewAdapter.setClickable(false);
            if (me.getPoints() >= 1) {
                buttonAttack.setEnabled(true);
            }
            if (me.getPoints() >= 2) {
                buttonHeal.setEnabled(true);
            }
            buttonDefend.setEnabled(true);
//            buttonHeal.setEnabled(true);
            me.setCombatMode(Player.COMBAT_MODE_NONE);
        }
    }
    public void showAnswers(View view) {
        Log.d(TAG, "showAnswers");
        questionText.setVisibility(View.INVISIBLE);
        battleOptionsLayout.setVisibility(View.INVISIBLE);
        answersLayout.setVisibility(View.VISIBLE);
        answersLayout.bringToFront();
        buttonQuestion.setEnabled(true);
        buttonAnswers.setEnabled(false);
        buttonBattle.setEnabled(true);
//        abortCombatMode();
    }

    public void showBattleOptions(View view) {
        Log.d(TAG, "showBattleOptions");
        questionText.setVisibility(View.INVISIBLE);
        answersLayout.setVisibility(View.INVISIBLE);
        battleOptionsLayout.setVisibility(View.VISIBLE);
        battleOptionsLayout.bringToFront();
        buttonQuestion.setEnabled(true);
        buttonAnswers.setEnabled(true);
        buttonBattle.setEnabled(false);
    }


    public void buttonHealClicked(View view) {
        buttonHeal.setEnabled(false);
//        buttonAttack.setEnabled(false);
//        buttonDefend.setEnabled(false);
        me.setCombatMode(Player.COMBAT_MODE_HEAL);
        int cooldown = me.getCharacter().getRecovery() * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (me.getHealth() == 0) {
                    return;
                }
                me.setCombatMode(Player.COMBAT_MODE_NONE);
                if (me.getPoints() >= 2) {
                    buttonHeal.setEnabled(true);
                }
                else{
                    buttonHeal.setEnabled(false);
                }
//                buttonHeal.setEnabled(true);
                if (me.getPoints() >= 1) {
                    buttonAttack.setEnabled(true);
                }
                buttonDefend.setEnabled(true);
            }
        }, cooldown);
    }

    public void buttonDefendClicked(View view) {
//        buttonHeal.setEnabled(false);
//        buttonAttack.setEnabled(false);
        buttonDefend.setEnabled(false);
        me.setCombatMode(Player.COMBAT_MODE_DEFEND);
        int cooldown = me.getCharacter().getRecovery() * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (me.getHealth() == 0) {
                    return;
                }
                me.setCombatMode(Player.COMBAT_MODE_NONE);
//                buttonHeal.setEnabled(true);
                if (me.getPoints() >= 1) {
                    buttonAttack.setEnabled(true);
                }
                if (me.getPoints() >= 2) {
                    buttonHeal.setEnabled(true);
                }
                else{
                    buttonHeal.setEnabled(false);
                }
                buttonDefend.setEnabled(true);
            }
        }, cooldown);
    }

    public void buttonAttackClicked(View view) {

        buttonHeal.setEnabled(false);
        buttonAttack.setEnabled(false);
        buttonDefend.setEnabled(false);
        me.setCombatMode(Player.COMBAT_MODE_ATTACK);
        mPlayersViewAdapter.setClickable(true);
    }

    private void animateAttack(Player attacker, Player victim, boolean defending, boolean killed) {
        // Animate local player
        if (attacker == me) {
            me.setAnimation(Player.ANIMATION_ATTACK);
        } else if (me.getHealth() <= 0) {
            me.setAnimation(Player.ANIMATION_DYING);
        } else {
            me.setAnimation(Player.ANIMATION_HURT);
        }
        updateLocalPlayerUi();

        // Animate other characters
        for (int i = 0; i < mPlayersViewAdapter.getCount(); i++) {
            Player player = mPlayersViewAdapter.getItem(i);

            // This will never happen, but the check is required to make Android Studio happy
            if (player == null) continue;

            if (attacker == me && player != victim) continue;

            if (attacker != me) {
                // Prevent a zombie apocalypse
                if (player.getHealth() <= 0) {
                    continue;
                }
                player.setAnimation(Player.ANIMATION_ATTACK);
            } else if (player.getHealth() <= 0) {
                player.setAnimation(Player.ANIMATION_DYING);
            } else {
                player.setAnimation(Player.ANIMATION_HURT);
            }
        }
        mPlayersViewAdapter.notifyDataSetChanged();

        // Let the animation run for a bit and then do the following...
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                me.setAnimation(Player.ANIMATION_NORMAL);
                updateLocalPlayerUi();

                for (int i = 0; i < mPlayersViewAdapter.getCount(); i++) {
                    Player player = mPlayersViewAdapter.getItem(i);

                    // This will never happen, but the check is required to make Android Studio happy
                    if (player == null) continue;

                    player.setAnimation(Player.ANIMATION_NORMAL);
                }
                mPlayersViewAdapter.notifyDataSetChanged();
//        buttonHeal.setEnabled(true);
                if (me.getPoints() >= 1) {
                    buttonAttack.setEnabled(true);
                }
                if (me.getPoints() >= 2) {
                    buttonHeal.setEnabled(true);
                }
                else{
                    buttonHeal.setEnabled(false);
                }
                buttonDefend.setEnabled(true);

                pickQuestion();
            }
            // Death animation takes a bit longer
        }, killed ? 3000: 2000);
    }

    private void finishGame() {
        mMusic.stop();
        finish();
    }

    /**
     * This function is called when the player clicks one of the answers
     * @param v: the button (view) that was clicked
     */
    @Override
    public void onClick(View v) {
        // Verify that v is really one of the answer button:
        QuizPool.Answer answer = (QuizPool.Answer) v.getTag(R.id.id_answer);
        if (answer == null) {
            Log.e(TAG, "Unhandled click on " + v);
            return;
        }

        // Abort timer
        Log.e(TAG, "Aborting timer");
        mQuestionLifeSpanCounter.cancel();

        // Prevent user from clicking other answers while we handle this one.
        recursiveSetEnabled(false, answersLayout);

        if (answer.correct) {
            Log.d(TAG, "Correct!");
            v.setBackgroundColor(ColorUtils.setAlphaComponent(Color.GREEN, 150));
        } else {
            Log.d(TAG, "Incorrect!");
            v.setBackgroundColor(ColorUtils.setAlphaComponent(Color.RED, 150));
        }
        if (answer.correct) {
            // Simulate task master sending us updated points
            me.setPoints(me.getPoints()+1);
        }
        Log.d(TAG, "calling pickQuestion from onClick");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Picking a new question after answering");
                pickQuestion();
            }
        }, 500);
    }

    @Override
    public void onClickPlayer(Player victim) {
        Log.v(TAG, "onClickPlayer: " + victim.getName());

        mQuestionLifeSpanCounter.cancel();

        mPlayersViewAdapter.setClickable(false);
        boolean defending = victim.getCombatMode() == Player.COMBAT_MODE_DEFEND;
        int damage = me.getCharacter().getAttack();
        if (defending) {
            damage -= victim.getCharacter().getDefense();
        }
        if (damage < 0) {
            damage = 0;
        }
        int victimHealth = victim.getHealth() - damage;
        boolean killed = false;
        if (victimHealth <= 0) {
            victim.setHealth(0);
            killed = true;
        } else {
            victim.setHealth(victimHealth);
        }
        // Attacking is not free...
        me.setPoints(me.getPoints()-1);
        animateAttack(me, victim, defending, killed);
    }


    // OpenTriviaDatabase callbacks (for single player mode

    @Override
    public void onCategoriesChanged(List<String> categories) {
        // Do nothing
    }
    private boolean waitingForQuestion = false;
    @Override
    public void onQuestionsAvailable(boolean available) {
        if (available && waitingForQuestion) {
            waitingForQuestion = false;
            Log.d(TAG, "calling pickQuestion from onQuestionsAvailable");
            pickQuestion();
        }
    }

    //Prompts the user to click Back button twice to confirm going back from the current activity.
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Current Match Data will be lost.\nPress BACK again to confirm exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
