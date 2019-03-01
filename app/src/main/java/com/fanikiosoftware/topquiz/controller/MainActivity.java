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
        System.out.println("score at oncreate(): " + mPreferences.getInt(PREF_KEY_SCORE,-47));
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
                startActivityForResult(gameActivityIntent,GAME_ACTIVITY_REQUEST_CODE);
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
        int score = mPreferences.getInt(PREF_KEY_SCORE, -1);
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
        if(score > -1 && name != null){
            mLeaderButton.setEnabled(true);
        }
    }
    //endregion

    //region onActivity Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int score = mPreferences.getInt(PREF_KEY_SCORE,-1);
        //verify that the results returned are from the proper call
        if ((GAME_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
          //check for score from SharedPref and if score exists, allow Leaderboard
           if( score > -1){
               System.out.println("***returning from GameActivity" + score );
                //todo sort scores here
                mLeaderButton.setEnabled(true);









            }
        }else {
            if ((LEADERBOARD_ACTIVITY_REQUEST_CODE == requestCode) && (RESULT_OK == resultCode)) {
                    System.out.println("returning from LeaderBoardActivity");
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
        out.println("MainActivity::onDestroy()" + mPreferences.getInt(PREF_KEY_SCORE,-5));
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