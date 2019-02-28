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
    public static final String PREF_KEY_NAME  = "PREFERENCE_KEY_NAME";
    public static final String PREF_KEY_SCORE = "PREFERENCE_KEY_SCORE";

    public static final String PREF_KEY_SCORE1 = "PREFERENCE_KEY_SCORE1";
    public static final String PREF_KEY_SCORE2 = "PREFERENCE_KEY_SCORE2";
    public static final String PREF_KEY_SCORE3 = "PREFERENCE_KEY_SCORE3";
    public static final String PREF_KEY_SCORE4 = "PREFERENCE_KEY_SCORE4";
    public static final String PREF_KEY_SCORE5 = "PREFERENCE_KEY_SCORE5";
    public static final String PREF_KEY_NAME1 = "PREFERENCE_KEY_NAME1";
    public static final String PREF_KEY_NAME2 = "PREFERENCE_KEY_NAME2";
    public static final String PREF_KEY_NAME3 = "PREFERENCE_KEY_NAME3";
    public static final String PREF_KEY_NAME4 = "PREFERENCE_KEY_NAME4";
    public static final String PREF_KEY_NAME5 = "PREFERENCE_KEY_NAME5";

    private static final String TAG = "MainActivity";

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mLeaderButton;
    private User mUser;
    private SharedPreferences mPreferences;
    private String mFirstName;

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
                mPreferences.edit().putString(PREF_KEY_NAME, mUser.getFirstname()).apply();
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
                startActivityForResult(leaderBoardIntent, LEADERBOARD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    //region checkNewUser()
    private void checkNewUser() {
        String name = mPreferences.getString(PREF_KEY_NAME, null);
        int score = mPreferences.getInt(PREF_KEY_SCORE, 21);
        //if a user name exists from prev game, greet user personally
        if(name != null){
            String welcome = getString(R.string.welcome_back_user);
            welcome = String.format(welcome, name, score);
            mGreetingText.setText(welcome);
            int i = name.length();
            mNameInput.setText(name);
            //place the cursor at end of name string
            //using i, i prevents from an actual selection of any text and
            //just places the cursor at end of string
            mNameInput.setSelection(i,i);
            mPlayButton.setEnabled(true);
        }
        //check for score,if score exists enable leader board btn
        if(score != 21){
            mLeaderButton.setEnabled(true);
        }
    }
    //endregion

    //region onActivity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //verify that the results returned are from the proper call
        if ((GAME_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
            //proper results being returned from Game Activity results
            //retrieve the score from the intent bundle
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, -1);
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();
            if (score != -1) {
                mLeaderButton.setEnabled(true);
            }
        }else {
            if ((LEADERBOARD_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
                    //proper results being returned from LeaderBoardActivity results
                    //retrieve the score from the intent bundle
                    System.out.println("returning from LeaderBoardActivity");
                    int score1 = data.getIntExtra(LeaderBoardActivity.BUNDLE_EXTRA_SCORE1, 0);
                    int score2 = data.getIntExtra(LeaderBoardActivity.BUNDLE_EXTRA_SCORE2, 0);
                    int score3 = data.getIntExtra(LeaderBoardActivity.BUNDLE_EXTRA_SCORE3, 0);
                    int score4 = data.getIntExtra(LeaderBoardActivity.BUNDLE_EXTRA_SCORE4, 0);
                    int score5 = data.getIntExtra(LeaderBoardActivity.BUNDLE_EXTRA_SCORE5, 0);
                    String name1 = data.getStringExtra(LeaderBoardActivity.BUNDLE_EXTRA_NAME1);
                    String name2 = data.getStringExtra(LeaderBoardActivity.BUNDLE_EXTRA_NAME2);
                    String name3 = data.getStringExtra(LeaderBoardActivity.BUNDLE_EXTRA_NAME3);
                    String name4 = data.getStringExtra(LeaderBoardActivity.BUNDLE_EXTRA_NAME4);
                    String name5 = data.getStringExtra(LeaderBoardActivity.BUNDLE_EXTRA_NAME5);
                    mPreferences.edit().putInt(PREF_KEY_SCORE1, score1).apply();
                    mPreferences.edit().putInt(PREF_KEY_SCORE2, score2).apply();
                    mPreferences.edit().putInt(PREF_KEY_SCORE3, score3).apply();
                    mPreferences.edit().putInt(PREF_KEY_SCORE4, score4).apply();
                    mPreferences.edit().putInt(PREF_KEY_SCORE5, score5).apply();
                    mPreferences.edit().putString(PREF_KEY_NAME1, name1).apply();
                    mPreferences.edit().putString(PREF_KEY_NAME2, name2).apply();
                    mPreferences.edit().putString(PREF_KEY_NAME3, name3).apply();
                    mPreferences.edit().putString(PREF_KEY_NAME4, name4).apply();
                    mPreferences.edit().putString(PREF_KEY_NAME5, name5).apply();
                }
            }
        String tryAgain = getString(R.string.try_again_txt);
        tryAgain = String.format(tryAgain, mPreferences.getString(PREF_KEY_NAME, ""));
        mGreetingText.setText(tryAgain);
    }
    //endregion

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