package engine;

import assess.Assessment;
import assess.Question;
import errors.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssessmentClass implements Assessment {

    private ArrayList<Question> questions = new ArrayList<>();
    private int[] answers;
    private LocalDate closingDate;
    private String information;
    private String courseCode;
    private int studentid;

    public AssessmentClass(LocalDate closingDate, String title, int studentid, ArrayList<Question> questions){
        this.closingDate = closingDate;
        this.studentid = studentid;
        this.questions = questions;
        this.courseCode = title;
        answers = new int[questions.size()];
        this.information = ("\n ----- Course code: " + title + "\n Closing Date: " + closingDate.toString() + "\n Number of Questions: " + questions.size());
    }

    @Override
    public String getInformation() {
        return information;
    }

    @Override
    public LocalDate getClosingDate() {
        return closingDate;
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
        for(Question question: questions){
            if(questionNumber == question.getQuestionNumber()){
                return question;
            }
        }
        throw new InvalidQuestionNumber();
    }

    @Override
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {
        for (Question question: questions) {
            if(questionNumber == question.getQuestionNumber()) {
                String[] options = question.getAnswerOptions();
                if(optionNumber > 0 && optionNumber <= options.length) {
                    answers[questionNumber - 1] = optionNumber;
                    return;
                }else {
                    throw new InvalidOptionNumber();
                }
            }
        }
        throw new InvalidQuestionNumber();
    }

    @Override
    public int getSelectedAnswer(int questionNumber) {
        for (Question question: questions) {
            if(questionNumber == question.getQuestionNumber()) {
               return answers[questionNumber - 1];
            }
        }
        return 0;
    }
    @Override
    public int getAssociatedID() {
        return studentid;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

}