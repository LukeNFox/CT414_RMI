package client;

import assess.Assessment;
import assess.Question;
import errors.*;

import java.util.Date;
import java.util.List;

public class AssessmentClass implements Assessment {

    @Override
    public String getInformation() {
        return null;
    }

    @Override
    public Date getClosingDate() {
        return null;
    }

    @Override
    public List<Question> getQuestions() {
        return null;
    }

    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
        return null;
    }

    @Override
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {

    }

    @Override
    public int getSelectedAnswer(int questionNumber) {
        return 0;
    }

    @Override
    public int getAssociatedID() {
        return 0;
    }

}