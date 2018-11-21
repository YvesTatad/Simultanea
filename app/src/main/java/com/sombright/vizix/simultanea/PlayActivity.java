package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
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

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, PlayersViewAdapter.OnClickPlayerListener, OpenTriviaDatabase.Listener {

    private int currentLevel = 0;
    private int amountOfCurrentLevelMobs = 0;
    private CountDownTimer mQuestionLifeSpanCounter;
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
    private ImageView localPlayerThumb;
    private ImageView leftAnimation, rightAnimation;
    private Button buttonQuestion, buttonAnswers, buttonBattle;
    private Button buttonHeal, buttonAttack, buttonDefend;
    private Handler handler = new Handler();
    private MediaPlayer mMusic;
    private Animation fadeOutAnimation;
    // Some variables we need to keep for the zoom-out animation
    private ImageView mOtherPlayerThumb;
    private Player mOtherPlayer;
    private boolean mWinBattle;
    // Translation and scale factors
    class ZoomAnimationData {
        ImageView thumb;
        ImageView big;
        PointF startScale = new PointF();
        PointF startTranslation = new PointF();
    }
    class AttackAnimationData {
        Player attacker, victim;
        boolean kill;
        ZoomAnimationData left = new ZoomAnimationData();
        ZoomAnimationData right = new ZoomAnimationData();
    }
    private AttackAnimationData mAttackAnimationData = null;
    private int mShortAnimationDuration = 500;
    private PreferencesProxy mPrefs;

    private QuizPool quizPool = null;
    private OpenTriviaDatabase opentdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_play);

        // These are the portions of the layout that we will modify during the game
        questionText = findViewById(R.id.questionTextView);
        answersLayout = findViewById(R.id.answersLayout);
        battleOptionsLayout = findViewById(R.id.battleOptionsLayout);
        otherPlayersLayout = findViewById(R.id.otherPlayersLayout);
        textViewLocalPlayer = findViewById(R.id.textViewLocalPlayer);
        leftAnimation = findViewById(R.id.leftAnimation);
        rightAnimation = findViewById(R.id.rightAnimation);

        buttonQuestion = findViewById(R.id.buttonQuestion);
        buttonAnswers = findViewById(R.id.buttonAnswers);
        buttonBattle = findViewById(R.id.buttonBattle);

        buttonHeal = findViewById(R.id.buttonHeal);
        buttonAttack = findViewById(R.id.buttonAttack);
        buttonDefend = findViewById(R.id.buttonDefend);

        localPlayerThumb = findViewById(R.id.imageViewLocalPlayer);
        localPlayerHealth = findViewById(R.id.localPlayerHealth);
        localPlayerHealth.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

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
                    characterName = mob.getName(PlayActivity.this);
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
            updatePlayerInfo(player);
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
        pickQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mMusic.start();

        //startDiscovering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mMusic.pause();

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
        // The previous question is over, clear-out any pending answers
        handler.removeCallbacksAndMessages(null);

        me.setAnswered(false);
        mPlayersViewAdapter.setAnswered(false);
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

        // Fake receiving updates from taskmaster as other players answered
//        for (int i = 0; i < mPlayersViewAdapter.getCount(); i++) {
//            final Player player = mPlayersViewAdapter.getItem(i);
//            if (player == null) {
//                continue;
//            }
//            // Simulate other players answering within 0-10 seconds
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // TODO: vary the likeliness that the player answered correctly based on the character
//
//                    boolean correct = random.nextBoolean();
//                    // Player sends their answer to tasks master...
//                    player.setAnswered(true);
//
//                    Log.d(TAG, "Player " + player.getName() + " answered " + (correct ? "right":"wrong"));
//
//                    if (correct || mPlayersViewAdapter.hasEveryoneAnswered()) {
//                        if (mQuestionLifeSpanCounter != null) {
//                            mQuestionLifeSpanCounter.cancel();
//                        }
//                    }
//
//                    // Taskmaster adjusts player info
//                    if (correct) {
//                        player.setPoints(player.getPoints()+1);
//                        updatePlayerInfo(player);
//                    }
//                    // Taskmaster picks another question
//                    if (correct || (me.hasAnswered() && mPlayersViewAdapter.hasEveryoneAnswered())) {
//                        pickQuestion();
//                    }
//                }
//            }, 3000 + random.nextInt(10)*1000);
//        }
        showQuestionAlt();
        questionLifeSpan();
    }

    private void questionLifeSpan() {

       mQuestionLifeSpanCounter = new CountDownTimer(10000, 1000) { // 1000 = 1 sec
            private Integer countNum = 0;

            @Override
            public void onTick(long millisUntilFinished) {
            countNum = countNum+1;
                Log.d("QuestionLifeSpanCounter", countNum.toString());
            }

            @Override
            public void onFinish() {
                pickQuestion();
                countNum = 0;
            }
        };
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

    public void showQuestionAlt() {
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
        updatePlayerInfo(me);
        int cooldown = me.getCharacter().getRecovery() * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (me.getHealth() == 0) {
                    return;
                }
                me.setCombatMode(Player.COMBAT_MODE_NONE);
                updatePlayerInfo(me);
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
        updatePlayerInfo(me);
        int cooldown = me.getCharacter().getRecovery() * 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (me.getHealth() == 0) {
                    return;
                }
                me.setCombatMode(Player.COMBAT_MODE_NONE);
                updatePlayerInfo(me);
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
        updatePlayerInfo(me);
    }

    private void animateAttack(Player attacker, Player victim, boolean defending, boolean killed) {
        final AttackAnimationData data = new AttackAnimationData();

        data.attacker = attacker;
        data.victim = victim;
        data.kill = killed;

        if (attacker == me) {
            data.left.thumb = localPlayerThumb;
        } else {
            int position = mPlayersViewAdapter.getPosition(attacker);
            LinearLayout item = (LinearLayout) otherPlayersLayout.getChildAt(position);
            data.left.thumb = (ImageView) item.getChildAt(0);
        }
        data.left.big = leftAnimation;

        if (victim == me) {
            data.right.thumb = localPlayerThumb;
        } else {
            int position = mPlayersViewAdapter.getPosition(victim);
            LinearLayout item = (LinearLayout) otherPlayersLayout.getChildAt(position);
            data.right.thumb = (ImageView) item.getChildAt(0);
        }
        data.right.big = rightAnimation;

        zoomImageFromThumb(data.left, null);
        zoomImageFromThumb(data.right, new Runnable() {
            @Override
            public void run() {
                doCharacterAnimation(data);
            }
        });
    }

    /**
     * Zoom-in animation:
     * - hide the small thumbnail
     * - calculate the offset and scaling required to shrink the big animation image exactly over the thumbnail
     * - animate from the thumbnail position to the full-size animation position.
     */
    private void zoomImageFromThumb(ZoomAnimationData data, Runnable endAction) {
        // Load the big zoom-in images (same as the small thumbnail images for now).
        data.big.setImageDrawable(data.thumb.getDrawable());

        // Calculate the starting and ending bounds for the zoomed-in image.
        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the zoomed-in image.
        data.thumb.getGlobalVisibleRect(startBounds);
        data.big.getGlobalVisibleRect(finalBounds);
        // Calculate the transformations needed to make the big animation fit exactly on top of the thumbnail:
        data.startTranslation.x = startBounds.left - finalBounds.left;
        data.startTranslation.y = startBounds.top - finalBounds.top;
        data.startScale.x = (float) startBounds.width() / finalBounds.width();
        data.startScale.y = (float) startBounds.height() / finalBounds.height();

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnails.
        data.thumb.setAlpha(0f);

        // Apply translation and scale to cover the thumbnail
        data.big.setTranslationX(data.startTranslation.x);
        data.big.setTranslationY(data.startTranslation.y);
        data.big.setScaleX(data.startScale.x);
        data.big.setScaleY(data.startScale.y);
        data.big.setVisibility(View.VISIBLE);
        data.big.bringToFront();

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        data.big.setPivotX(0f);
        data.big.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties.
        ViewPropertyAnimator anim = data.big.animate();
        anim.translationX(0f);
        anim.translationY(0f);
        anim.scaleX(1f);
        anim.scaleY(1f);
        anim.setDuration(mShortAnimationDuration);
        anim.setInterpolator(new DecelerateInterpolator());
        if (endAction != null) {
            // Schedule the next step at the end of the zoom-in animation
            anim.withEndAction(endAction);
        }
        anim.start();
    }

    /**
     * Battle animations
     * The attacker and the victim images are now fully zoomed-in.
     * Time to play the attack and the hurt/death animations.
     */
    private void doCharacterAnimation(final AttackAnimationData data) {
        // Victim: select between hurt and death animations
        int resId;
        if (data.kill) {
            resId = data.victim.getCharacter().getDeathAnimationId();
        } else {
            resId = data.victim.getCharacter().getImageResourceHurt();
        }
        data.right.big.setImageResource(resId);
        AnimationDrawable anim = (AnimationDrawable) data.right.big.getDrawable();
        anim.start();
        // Select attack animation
        data.left.big.setImageResource(data.attacker.getCharacter().getImageResourceAttack());
        anim = (AnimationDrawable) data.left.big.getDrawable();
        anim.start();
        // Let the hurt/death animation run for a certain time before zooming out
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomImageBackToThumb(data.left, data.attacker.getCharacter(), false);
                zoomImageBackToThumb(data.right, data.victim.getCharacter(), data.kill);
            }
            // Death animation takes a bit longer
        }, data.kill ? 2000: 1000);
    }

    /**
     * Zoom-out animation:
     * - Shrink the animation images back to the thumbnail position
     */
    private void zoomImageBackToThumb(final ZoomAnimationData data, Character character, boolean dead) {
        // Stop the hurt or death animation
        AnimationDrawable animationDrawable = (AnimationDrawable) data.big.getDrawable();
        animationDrawable.stop();
        // Zoom-out with the dead image or normal image
        if (dead)
            data.big.setImageResource(character.getDeadImageId());
        else
            data.big.setImageDrawable(data.thumb.getDrawable());

        // Animate the four positioning/sizing properties in parallel,
        // back to their original values.
        ViewPropertyAnimator anim = data.big.animate();
        anim.translationX(data.startTranslation.x);
        anim.translationY(data.startTranslation.y);
        anim.scaleX(data.startScale.x);
        anim.scaleY(data.startScale.y);
        anim.setDuration(mShortAnimationDuration);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.withEndAction(new Runnable() {
            @Override
            public void run() {
                // Re-enable thumb view with the same image as the zoom-out animation
                data.thumb.setImageDrawable(data.big.getDrawable());
                data.thumb.setAlpha(1f);
                data.big.setVisibility(View.INVISIBLE);
                data.big.setTranslationX(0f);
                data.big.setTranslationY(0f);
                data.big.setScaleX(1f);
                data.big.setScaleY(1f);
            }
        });
        anim.start();
    }

    private void finishGame() {
        mMusic.stop();
        finish();
    }

    @Override
    public void onClick(View v) {
        // Check if it was an answer button
        QuizPool.Answer answer = (QuizPool.Answer) v.getTag(R.id.id_answer);
        if (answer != null) {
            // First thing: prevent user from clicking other answers while we handle this one.
            recursiveSetEnabled(false, answersLayout);

            if (answer.correct || mPlayersViewAdapter.hasEveryoneAnswered()) {
                if (mQuestionLifeSpanCounter != null) {
                    mQuestionLifeSpanCounter.cancel();
                }
            }

            if (answer.correct) {
                Log.d(TAG, "Correct!");
                questionText.setText(R.string.answer_correct);
                v.setBackgroundColor(ColorUtils.setAlphaComponent(Color.GREEN, 150));
            } else {
                Log.d(TAG, "Incorrect!");
                questionText.setText(R.string.answer_incorrect);
                v.setBackgroundColor(ColorUtils.setAlphaComponent(Color.RED, 150));
            }
            me.setAnswered(true);
            if (answer.correct) {
                // Simulate task master sending us updated points
                me.setPoints(me.getPoints()+1);
                updatePlayerInfo(me);
            }
            if (mPlayersViewAdapter.hasEveryoneAnswered()) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickQuestion();
                    }
                }, 500);
            }
            return;
        }

        Log.e(TAG, "Unhandled click on " + v);
    }

    private void updatePlayerInfo(Player player) {
        Log.d(TAG, "updatePlayerInfo");

        if (player == me) {
            updateLocalPlayerUi();
            if (me.getPoints() != 0) {
                buttonAttack.setEnabled(true);
            }
        } else {
            mPlayersViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickPlayer(Player victim) {
        Log.v(TAG, "onClickPlayer: " + victim.getName());
        // Check if it was a player button
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
        updatePlayerInfo(victim);
        updatePlayerInfo(me);
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
