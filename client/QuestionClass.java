package client;

import assess.Question;

import java.util.Arrays;

public class QuestionClass implements Question {

    private int qNumber;
    private String qDetail;
    private String[] options;

    public QuestionClass(int qNumber, String qDetail, String[] options){
        this.qNumber = qNumber;
        this.qDetail = qDetail;
        this.options = options;
    }

    @Override
    public int getQuestionNumber() {
        return this.qNumber;
    }

    @Override
    public String getQuestionDetail() {
        return this.qDetail;
    }

    @Override
    public String[] getAnswerOptions() {
        return this.options;
    }

    @Override
    public String toString() {
        return Arrays.toString(options);
    }
}