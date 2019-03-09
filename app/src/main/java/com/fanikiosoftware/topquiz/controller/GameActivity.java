package com.fanikiosoftware.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fanikiosoftware.topquiz.R;
import com.fanikiosoftware.topquiz.model.Question;
import com.fanikiosoftware.topquiz.model.QuestionBank;

import java.util.Arrays;

import static java.lang.System.out;

public class GameActivity extends AppCompatActivity implements OnClickListener {

    public static final String BUNDLE_STATE_SCORE =
            GameActivity.class.getCanonicalName().concat("BUNDLE_STATE_SCORE");
    public static final String BUNDLE_STATE_QUESTIONS =
            GameActivity.class.getCanonicalName().concat("BUNDLE_STATE_QUESTIONS");
    public static final String PREF_KEY_SCORE = "PREFERENCE_KEY_SCORE";

    private TextView mQuestionTextView;
    private Button mAnswerBtn1;
    private Button mAnswerBtn2;
    private Button mAnswerBtn3;
    private Button mAnswerBtn4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;
    private int mScore;
    private int mNumQuestions;
    private boolean mEnableTouchEvents;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        out.println("GameActivity::onCreate()");
        mQuestionBank = this.generateQuestions(); //returns QuestionBank obj -> List of questions
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //has data been persisted? if so, get data (score and questions)
        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTIONS);
        } else {
            //no saved data on record
            mScore = 0;
            mNumQuestions = 4;
        }
        mEnableTouchEvents = true;

        //set up the widgets
        mQuestionTextView = findViewById(R.id.activity_game_question_text);
        mAnswerBtn1 = findViewById(R.id.activity_game_answer1_btn);
        mAnswerBtn2 = findViewById(R.id.activity_game_answer2_btn);
        mAnswerBtn3 = findViewById(R.id.activity_game_answer3_btn);
        mAnswerBtn4 = findViewById(R.id.activity_game_answer4_btn);

        //set up tags for onclick method identification
        mAnswerBtn1.setTag(0);
        mAnswerBtn2.setTag(1);
        mAnswerBtn3.setTag(2);
        mAnswerBtn4.setTag(3);

        //set up onclick listeners for all btns
        mAnswerBtn1.setOnClickListener(this);
        mAnswerBtn2.setOnClickListener(this);
        mAnswerBtn3.setOnClickListener(this);
        mAnswerBtn4.setOnClickListener(this);

        //retrieve a question
        mCurrentQuestion = mQuestionBank.getQuestion();
        //send current question to method to display question and answer choices
        this.displayQuestion(mCurrentQuestion);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTIONS, mNumQuestions);
        super.onSaveInstanceState(outState);
    }

    //displays the question and it's choicelist
    private void displayQuestion(final Question question) {
        //set question into text view widget
        mQuestionTextView.setText(question.getQuestion());
        //set index 0-3 of choice list into each answer button
        mAnswerBtn1.setText(question.getChoiceList().get(0));
        mAnswerBtn2.setText(question.getChoiceList().get(1));
        mAnswerBtn3.setText(question.getChoiceList().get(2));
        mAnswerBtn4.setText(question.getChoiceList().get(3));
    }

    //onClickListener implementation for answer/choice list btns
    @Override
    public void onClick(View v) {
        //which button called onClick()
        int userAnswer = (int) v.getTag();
        if (userAnswer == mCurrentQuestion.getAnswerIndex()) {
            //user answer is correct
            Toast.makeText(GameActivity.this, "Good job! That's correct",
                    Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
            // incorrect answer
            Toast.makeText(GameActivity.this, "Oops! Wrong answer!",
                    Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                // If this is the last question,game over
                // else, display the next question.
                if (--mNumQuestions == 0) {
                    // End the game
                    endGame();
                } else {
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); // LENGTH_SHORT is usually 2 second long so delay touch until completed
    }

    //provide the questions and choice list for each question
    public QuestionBank generateQuestions() {
        Question question1 = new Question("T'Challa is the king of Wakanda. " +
                "What was the previous king, T'Challa's father, called?",
                Arrays.asList("Azzuir the Wise", "S'Yan", "T'Chaka", "T'Challa the First"),
                2);

        Question question2 = new Question("Wakanda is so technologically advanced" +
                " because of vibranium. BUT, how did it get all of the precious metal?",
                Arrays.asList("It hit Wakanda in the form of an asteroid",
                        "It was created by an ancient form of magic",
                        "It was made by a mountain growing around an Infinity Stone",
                        "It was sent back in time by a future Black Panther"), 0);

        Question question3 = new Question("Where was Erik Killmonger - or N'Jadaka - " +
                "brought up as a child?",
                Arrays.asList("Paris", "California", "Wakanda", "Singapore"), 1);

        Question question4 = new Question("What is the name of the elite all-women" +
                " bodyguard squad commanded by Okoye?", Arrays.asList("Panther Squad",
                "The Dora Milaje", "Silver Arrows", "Wusa Crew"), 1);

        Question question5 = new Question("What advice does Okoye give to T'Challa " +
                "before he jumps out of the Royal Talon Flyer at the start of the movie?",
                Arrays.asList("Don't freeze", "Don't get seen", "Make Wakanda proud",
                        "Fighty fighty punch punch"), 0);

        Question question6 = new Question("What name does Shuri give to her new," +
                " silent type of footwear that she gives to T'Challa?",
                Arrays.asList("Hush Panthers", "Contraverse Soles", "Sneakers",
                        "Panther Pads"), 2);

        Question question7 = new Question("What is the name of the legendary first" +
                " Black Panther, who united the 5 tribes of Wakanda?",
                Arrays.asList("Bashenga", "T'Challa", "Keith", "Zuri"), 1);

        Question question8 = new Question("What organization does Everett" +
                " Ross, T'Challa's reluctant ally, work for during the movie?",
                Arrays.asList("Joint Counter Fighting Task Force", "SHIELD",
                        "The CIA", "S.W.O.R.D."), 2);

        Question question9 = new Question("Who is the other superhero, who first " +
                "appeared in Captain America: The First Avenger, " +
                "who appears in the post-credits sequence of Black Panther?",
                Arrays.asList("Captain America", "Dum Dum Dugan", "Lady Sif",
                        "Bucky Barnes"), 3);

        Question question10 = new Question("What is Shuri's relationship with T'Challa?",
                Arrays.asList("She's his older sister", "She's his aunt",
                        "She's his childhood friend", "She's his younger sister"),3);

        Question question11 = new Question("What is the name of the special type of" +
                " beads worn by Wakandans, that can do everything from make calls to healing" +
                " people?", Arrays.asList("Cull Obsidian", "Jaguar Habit", "Kimoyo Beads",
                        "Spin Balls"), 2);

        Question question12 = new Question("What animal does W'Kabi ride into battle at" +
                " the end of the movie?",
                Arrays.asList("A hippo", "An ostrich", "A rhino", "A little puppy"), 2);

        Question question13 = new Question("Ulysses Klaue is one of the film's bad" +
                " guys. Which other Marvel movie did he appear in?",
                Arrays.asList("Captain America: The Winter Soldier",
                        "Marvel's Agents of SHIELD", "Avengers: Age of Ultron",
                        "Big Poppa Chuckles: The Fourth Avenger"), 2);

        Question question14 = new Question("Vibranium is the source of Wakanda's " +
                "technology and it's also what Captain America's shield is made out of! " +
                "What makes it special?",Arrays.asList("It can shoot electricity",
                "It's super magnetic", "It's super sparkly", "It absorbs all the energy from hits"),
                3);

        Question question15 = new Question("In the final big battle during Black Panther," +
                " what weapon does Shuri use?",
                Arrays.asList("An energy staff", "Vibranium gauntlets",
                        "She pilots a Royal Talon Flyer", "Big sword"), 1);
        return new QuestionBank(Arrays.asList(question1, question2, question3, question4,
                question5, question6, question7, question8, question9, question10, question11,
                question12, question13, question14, question15));
    }

    //endGame implementation ,save score to shared pref and display final score to user
    private void endGame() {
        new AlertDialog.Builder(this)
                .setTitle("Well done")
                .setMessage("Your score: " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //store user score in Shared Preferences
                        mPreferences.edit().putInt(GameActivity.PREF_KEY_SCORE, mScore).apply();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("GameActivity::onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("GameActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("GameActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("GameActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("GameActivity::onDestroy()");
    }
}