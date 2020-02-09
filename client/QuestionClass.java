package client;

import assess.Question;

public class QuestionClass implements Question {

    public QuestionClass(){}

    @Override
    public int getQuestionNumber() {
        return 0;
    }

    @Override
    public String getQuestionDetail() {
        return null;
    }

    @Override
    public String[] getAnswerOptions() {
        return new String[0];
    }
}
