package com.fanikiosoftware.topquiz.controller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanikiosoftware.topquiz.R;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.System.out;
/**
 * Created by Angela Rosas on 2/12/2019.
 */
public class LeaderBoardActivity extends AppCompatActivity {

    public static final String PREF_KEY_NAME = "PREFERENCE_KEY_NAME";
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

    public final static String BUNDLE_EXTRA_SCORE1 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE1");
    public final static String BUNDLE_EXTRA_SCORE2 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE2");
    public final static String BUNDLE_EXTRA_SCORE3 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE3");
    public final static String BUNDLE_EXTRA_SCORE4 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE4");
    public final static String BUNDLE_EXTRA_SCORE5 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_SCORE5");
    public final static String BUNDLE_EXTRA_NAME1 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_NAME1");
    public final static String BUNDLE_EXTRA_NAME2 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_NAME2");
    public final static String BUNDLE_EXTRA_NAME3 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_NAME3");
    public final static String BUNDLE_EXTRA_NAME4 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_NAME4");
    public final static String BUNDLE_EXTRA_NAME5 =
            LeaderBoardActivity.class.getCanonicalName().concat("BUNDLE_EXTRA_NAME5");

    int currentScore, score1, score2, score3, score4, score5;
    String currentName, name1, name2, name3, name4, name5;
    private Button mRankByNameBtn;
    private Button mRankByScoreBtn;
    private TextView tv_leader_board;
    private SharedPreferences mPreferences;
    String[][] mLeaderArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        System.out.println("LeaderBoardActivity::onCreate()");
        //get current score and user name
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //set up buttons & text views
        mRankByNameBtn = findViewById(R.id.btn_rank_by_name);
        mRankByScoreBtn = findViewById(R.id.btn_rank_by_score);
        tv_leader_board = findViewById(R.id.tv_leader_board);
        mRankByNameBtn.setEnabled(false);
        setUpListeners();
        getSavedScores();
        //show leaders ranked by score(desc) as default view
        rankByScore();
        showLeadersByScore();
    }

    private void getSavedScores(){
        currentScore = mPreferences.getInt(PREF_KEY_SCORE, -1);
        currentName = mPreferences.getString(PREF_KEY_NAME, "");
        score1 = mPreferences.getInt(PREF_KEY_SCORE1, -1);
        score2 = mPreferences.getInt(PREF_KEY_SCORE2, -1);
        score3 = mPreferences.getInt(PREF_KEY_SCORE3, -1);
        score4 = mPreferences.getInt(PREF_KEY_SCORE4, -1);
        score5 = mPreferences.getInt(PREF_KEY_SCORE5, -1);
        name1 = mPreferences.getString(PREF_KEY_NAME1, "");
        name2 = mPreferences.getString(PREF_KEY_NAME2, "");
        name3 = mPreferences.getString(PREF_KEY_NAME3, "");
        name4 = mPreferences.getString(PREF_KEY_NAME4, "");
        name5 = mPreferences.getString(PREF_KEY_NAME5, "");
    }

    //set up onclick listeners
    private void setUpListeners() {
        mRankByNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LeaderBoardActivity::Setting listener onClick()-mRankByName");
                rankByName();
            }
        });
        mRankByScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LeaderBoardActivity::Setting listener onClick()-mRankByScore");
                showLeadersByScore();
            }
        });
//        mHomeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("LeaderBoardActivity::Setting listener onClick()-mHome");
//                Intent intent = new Intent();
//                //add variable mScore to intent to pass back
//                intent.putExtra(BUNDLE_EXTRA_SCORE1, score1);
//                intent.putExtra(BUNDLE_EXTRA_SCORE2, score2);
//                intent.putExtra(BUNDLE_EXTRA_SCORE3, score3);
//                intent.putExtra(BUNDLE_EXTRA_SCORE4, score4);
//                intent.putExtra(BUNDLE_EXTRA_SCORE5, score5);
//                intent.putExtra(BUNDLE_EXTRA_NAME1, name1);
//                intent.putExtra(BUNDLE_EXTRA_NAME2, name2);
//                intent.putExtra(BUNDLE_EXTRA_NAME3, name3);
//                intent.putExtra(BUNDLE_EXTRA_NAME4, name4);
//                intent.putExtra(BUNDLE_EXTRA_NAME5, name5);
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });
    }

    //********start methods********//
    private void rankByName() {
        //Sort the array by players' names (column2)- class below
        Arrays.sort(mLeaderArray, new ColumnComparator(1));

        //show leader board w/names and scores in desc order by name
        String leadersByName = getString(R.string.tv_show_leaderboard);
        leadersByName = String.format(leadersByName, mLeaderArray[0][0], mLeaderArray[0][1],
                mLeaderArray[1][0], mLeaderArray[1][1],
                mLeaderArray[2][0], mLeaderArray[2][1],
                mLeaderArray[3][0], mLeaderArray[3][1],
                mLeaderArray[4][0], mLeaderArray[4][1]);
        tv_leader_board.setText(leadersByName);
        mRankByScoreBtn.setEnabled(true);
    }

    //default view shows leader board ranked by descending scores
    private void rankByScore() {
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
    }
    private void showLeadersByScore(){
        //show leader board by descending scores
        String scoresDescending = getString(R.string.tv_show_leaderboard);
        String scores[] = new String[] {
                String.valueOf(score1),
                String.valueOf(score2),
                String.valueOf(score3),
                String.valueOf(score4),
                String.valueOf(score5)};
        //set empty scores to "" so they do not print
        for(int s = 0; s < scores.length; s++){
            if(scores[s] == "-1"){
                scores[s] = "";
            }
        }
        scoresDescending = String.format(scoresDescending,
                scores[0], name1,
                scores[1], name2, 
                scores[2], name3,
                scores[3], name4,
                scores[4], name5);
        tv_leader_board.setText(scoresDescending);
        //now that order is determined, set 2d array w/names and scores & enable sort by name
        mRankByNameBtn.setEnabled(true);
        mLeaderArray = new String[][]{
                {scores[0], name1},
                {scores[1], name2},
                {scores[2], name3},
                {scores[3], name4},
                {scores[4], name5}};
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //todo
        out.println("LeaderBoardActivity::onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        out.println("LeaderBoardActivity::onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        out.println("LeaderBoardActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        out.println("LeaderBoardActivity::onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        out.println("LeaderBoardActivity::onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        out.println("LeaderBoardActivity::onResume()");
    }
    //sorting class for name name sort
    public class ColumnComparator implements Comparator {

        int columnToSort;

        ColumnComparator(int columnToSort) {
            this.columnToSort = columnToSort;
        }

        //overriding compare method
        public int compare(Object o1, Object o2) {
            String[] row1 = (String[]) o1;
            String[] row2 = (String[]) o2;
            //compare the columns to sort
            return row1[columnToSort].compareTo(row2[columnToSort]);
        }
    }
}