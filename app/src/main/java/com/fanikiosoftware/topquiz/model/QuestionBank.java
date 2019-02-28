package com.fanikiosoftware.topquiz.model;

import java.util.Collections;
import java.util.List;

/**
 *
 * Created by Angela Rosas on 1/29/2019.
 */

public class QuestionBank {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {
        mQuestionList = questionList;
        //shuffle the question list
        Collections.shuffle(mQuestionList);
        mNextQuestionIndex = 0;
    }

    public Question getQuestion() {
        // Loop through each question on the list
        //if at the end of the list - start back at the  beginning
        if (mNextQuestionIndex == mQuestionList.size()) {
            mNextQuestionIndex = 0;
        }
        // else return index and increment AFTER returning the index
        return mQuestionList.get(mNextQuestionIndex++);
    }
}
