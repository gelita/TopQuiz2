package com.fanikiosoftware.topquiz.controller;

import android.content.Intent;
import android.os.Bundle;
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

    private Button mRankByNameBtn;
    private Button mRankByScoreBtn;
    private TextView tv_leader_board;
    String[] mLeaderArray;
    Intent intent;
    String[][] mSortByNameArray;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        //set up buttons & text views
        mRankByNameBtn = findViewById(R.id.btn_rank_by_name);
        mRankByScoreBtn = findViewById(R.id.btn_rank_by_score);
        tv_leader_board = findViewById(R.id.tv_leader_board);
        mRankByNameBtn.setEnabled(false);
        intent = getIntent();
        mLeaderArray = intent.getStringArrayExtra("sorted scores");
        setUpListeners();
        //show leaders ranked by score(desc) as default view
        showLeadersByScore();
    }

    //set up onclick listeners
    private void setUpListeners() {
        mRankByNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeadersByName();
            }
        });
        mRankByScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLeadersByScore();
            }
        });
    }

    //default view - leader board sorted by Score(descending)
    private void showLeadersByScore() {
        //set empty scores to "" so they do not print
        for(int i = 0; i < mLeaderArray.length; i=i+2){
            if(mLeaderArray[i].equals("-1")){
                mLeaderArray[i] = "";
                mLeaderArray[i+1]= "";
            }
        }
        String scoresDescending = getString(R.string.tv_show_leaderboard);
        scoresDescending = String.format(scoresDescending,
                String.valueOf(mLeaderArray[0]), mLeaderArray[1],
                String.valueOf(mLeaderArray[2]), mLeaderArray[3],
                String.valueOf(mLeaderArray[4]), mLeaderArray[5],
                String.valueOf(mLeaderArray[6]), mLeaderArray[7],
                String.valueOf(mLeaderArray[8]), mLeaderArray[9]);

        tv_leader_board.setText(scoresDescending);
        mRankByNameBtn.setEnabled(true);
    }

    private void showLeadersByName() {
        mSortByNameArray = new String[][]{{mLeaderArray[0], mLeaderArray[1]},
                {mLeaderArray[2], mLeaderArray[3]},
                {mLeaderArray[4], mLeaderArray[5]},
                {mLeaderArray[6], mLeaderArray[7]},
                {mLeaderArray[8], mLeaderArray[9]}};
        // Sort the array by players' names (column2)- class below
        Arrays.sort(mSortByNameArray, new ColumnComparator(1));
        //show leader board w/names and scores in desc order by name
        String leadersByName = getString(R.string.tv_show_leaderboard);
        leadersByName = String.format(leadersByName, mSortByNameArray[0][0], mSortByNameArray[0][1],
                mSortByNameArray[1][0], mSortByNameArray[1][1],
                mSortByNameArray[2][0], mSortByNameArray[2][1],
                mSortByNameArray[3][0], mSortByNameArray[3][1],
                mSortByNameArray[4][0], mSortByNameArray[4][1]);
        tv_leader_board.setText(leadersByName);
        mRankByScoreBtn.setEnabled(true);
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