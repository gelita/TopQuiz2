package com.fanikiosoftware.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fanikiosoftware.topquiz.R;
import com.fanikiosoftware.topquiz.model.User;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_ACTIVITY_REQUEST_CODE = 22;
    private static final int LEADERBOARD_ACTIVITY_REQUEST_CODE = 33;
    public static final String PREF_KEY_CURRENT_NAME = "PREFERENCE_KEY_CURRENT_NAME";
    public static final String PREF_KEY_NAME1 = "PREFERENCE_KEY_NAME1";
    public static final String PREF_KEY_NAME2 = "PREFERENCE_KEY_NAME2";
    public static final String PREF_KEY_NAME3 = "PREFERENCE_KEY_NAME3";
    public static final String PREF_KEY_NAME4 = "PREFERENCE_KEY_NAME4";
    public static final String PREF_KEY_NAME5 = "PREFERENCE_KEY_NAME5";
    public static final String PREF_KEY_SCORE = "PREFERENCE_KEY_SCORE";
    public static final String PREF_KEY_SCORE1 = "PREFERENCE_KEY_SCORE1";
    public static final String PREF_KEY_SCORE2 = "PREFERENCE_KEY_SCORE2";
    public static final String PREF_KEY_SCORE3 = "PREFERENCE_KEY_SCORE3";
    public static final String PREF_KEY_SCORE4 = "PREFERENCE_KEY_SCORE4";
    public static final String PREF_KEY_SCORE5 = "PREFERENCE_KEY_SCORE5";

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mLeaderButton;
    private User mUser;
    private SharedPreferences mPreferences;
    private String mFirstName;
    int currentScore, score1, score2, score3, score4, score5;
    String currentName, name1, name2, name3, name4, name5;
    String[] mLeaderArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameInput = findViewById(R.id.activity_main_name_input);
        mGreetingText = findViewById(R.id.activity_main_greeting_txt);
        mPlayButton = findViewById(R.id.activity_main_play_btn);
        mLeaderButton = findViewById(R.id.activity_main_leader_board_btn);
        //the buttons will stay disabled until the user enters input
        mPlayButton.setEnabled(false);
        mLeaderButton.setEnabled(false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkNewUser();
        mUser = new User();
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do something
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //true when user enters at least 1 letter in editText field
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //do something
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName = mNameInput.getText().toString().trim();
                mUser.setFirstname(mFirstName);
                mPreferences.edit().putString(PREF_KEY_CURRENT_NAME, mUser.getFirstname()).apply();
                Intent gameActivityIntent = new Intent(MainActivity.this,
                        GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });

        mLeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leaderBoardIntent = new Intent(MainActivity.this,
                        LeaderBoardActivity.class);
                leaderBoardIntent.putExtra("sorted scores", mLeaderArray);
                startActivity(leaderBoardIntent);
            }
        });
    }

    private void checkNewUser() {
        String name = mPreferences.getString(PREF_KEY_CURRENT_NAME, null);
        int score = mPreferences.getInt(PREF_KEY_SCORE, -1);
        //if a user name exists from prev game, greet user personally
        if (name != null) {
            String welcome = getString(R.string.welcome_back_user);
            welcome = String.format(welcome, name, score);
            mGreetingText.setText(welcome);
            int i = name.length();
            mNameInput.setText(name);
            //place the cursor at end of name string
            //using i, i prevents from an actual selection of any text and
            //just places the cursor at end of string
            mNameInput.setSelection(i, i);
            mPlayButton.setEnabled(true);
        }
        //check for score,if score exists enable leader board btn
        if (score > -1 && name != null) {
            mLeaderButton.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //verify that the results returned are from the proper call
        if ((GAME_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
            //check for score from SharedPref and if score exists, allow Leader board
            if (mPreferences.getInt(PREF_KEY_SCORE, -1) > -1) {
                //get stored scores and then sort
                getSavedScores();
                sortScores();
                saveLeaderBoard();
                mLeaderButton.setEnabled(true);
            }
        } else {
            if ((LEADERBOARD_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
                //do something?
            }
        }
        String tryAgain = getString(R.string.try_again_txt);
        tryAgain = String.format(tryAgain, mPreferences.getString(PREF_KEY_CURRENT_NAME, ""));
        mGreetingText.setText(tryAgain);
    }

    private void getSavedScores() {
        score1 = mPreferences.getInt(PREF_KEY_SCORE1, -1);
        score2 = mPreferences.getInt(PREF_KEY_SCORE2, -1);
        score3 = mPreferences.getInt(PREF_KEY_SCORE3, -1);
        score4 = mPreferences.getInt(PREF_KEY_SCORE4, -1);
        score5 = mPreferences.getInt(PREF_KEY_SCORE5, -1);
        name1 = mPreferences.getString(PREF_KEY_NAME1, null);
        name2 = mPreferences.getString(PREF_KEY_NAME2, null);
        name3 = mPreferences.getString(PREF_KEY_NAME3, null);
        name4 = mPreferences.getString(PREF_KEY_NAME4, null);
        name5 = mPreferences.getString(PREF_KEY_NAME5, null);
    }

    private void sortScores() {
        currentScore = mPreferences.getInt(PREF_KEY_SCORE, -1);
        currentName = mPreferences.getString(PREF_KEY_CURRENT_NAME,null);
        if (currentScore > score5) {
            score5 = currentScore;
            name5 = currentName;
        }
        if (currentScore > score4) {
            score5 = score4;
            name5 = name4;
            score4 = currentScore;
            name4 = currentName;
        }
        if (currentScore > score3) {
            score4 = score3;
            name4 = name3;
            score3 = currentScore;
            name3 = currentName;
        }
        if (currentScore > score2) {
            score3 = score2;
            name3 = name2;
            score2 = currentScore;
            name2 = currentName;
        }
        if (currentScore > score1) {
            score2 = score1;
            name2 = name1;
            score1 = currentScore;
            name1 = currentName;
        }
        mLeaderArray = new String[]{
                String.valueOf(score1), name1,
                String.valueOf(score2), name2,
                String.valueOf(score3), name3,
                String.valueOf(score4), name4,
                String.valueOf(score5), name5
        };
    }

    private void saveLeaderBoard() {
        mPreferences.edit().putString(PREF_KEY_NAME1, name1).apply();
        mPreferences.edit().putString(PREF_KEY_NAME2, name2).apply();
        mPreferences.edit().putString(PREF_KEY_NAME3, name3).apply();
        mPreferences.edit().putString(PREF_KEY_NAME4, name4).apply();
        mPreferences.edit().putString(PREF_KEY_NAME5, name5).apply();
        mPreferences.edit().putInt(PREF_KEY_SCORE1, score1).apply();
        mPreferences.edit().putInt(PREF_KEY_SCORE2, score2).apply();
        mPreferences.edit().putInt(PREF_KEY_SCORE3, score3).apply();
        mPreferences.edit().putInt(PREF_KEY_SCORE4, score4).apply();
        mPreferences.edit().putInt(PREF_KEY_SCORE5, score5).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("MainActivity::onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("MainActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("MainActivity::onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("MainActivity::onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("MainActivity::onResume()");
    }
}