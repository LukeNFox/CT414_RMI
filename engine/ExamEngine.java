package engine;

import assess.*;
import client.QuestionClass;
import errors.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;

public class ExamEngine implements ExamServer {

    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();
    private ArrayList<Assessment> assessments = new ArrayList<>();
    private ArrayList<Assessment> availableForCorrection = new ArrayList<>();

    // Constructor is required
    public ExamEngine() {
        super();
        initialiseServer();
    }

    // Implement the methods defined in the assess.ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws
            UnauthorizedAccess, RemoteException {

        //check student id and password match a student
        // if so this will return an Auth token
        //else throws unauthorised exception

        String realPass = null;
        Boolean existingSession = false;
        Session thisSession = null;
        int token = 0;

        System.out.println("Trying to login");
        System.out.println("Checking Password");
        for (Student student : students) {
            if (student.getStudentid() == studentid) {
                realPass = student.getPassword();
            }
        }
        if (realPass.equals(password)) {

            for(Session session: sessions){
                if(session.getStudentid() == studentid){
                    existingSession = true;
                    thisSession = session;
                }
            }

            if(existingSession){
                System.out.println("Session already Exists");
                token = thisSession.getToken();
            }else{
                System.out.println("Creating Session");
                thisSession = new SessionClass(studentid);
                sessions.add(thisSession);
                token = thisSession.getToken();
            }

        } else {
            System.out.println("Incorrect Password");
            throw new UnauthorizedAccess("Unable to login - Incorrect Password");
        }
        return token;
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // check token is valid
        // return list of assessment information available for that student

        Boolean valid = checkSession(studentid, token);
        List<String> summaries = new ArrayList<>();

        System.out.println("Gathering assessment details");
        if (valid) {
            for (Assessment assessment : assessments) {
                if (assessment.getAssociatedID() == studentid)
                    summaries.add(assessment.getInformation());
            }
        }

        if(summaries.size() > 0){
            return summaries;
        }else{
            System.out.println("No assessments match this id");
            throw new NoMatchingAssessment("Could not find any assessments matching your id");
        }
    }

    // Return an assess.Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // check token is valid
        // return assessment object
        // ensure assessment available to student

        Boolean valid = checkSession(studentid, token);

        System.out.println("Finding assessment matching course code");
        if(valid) {
            for (Assessment assessment : assessments) {
                AssessmentClass assessment1 = (AssessmentClass) assessment;
                if (assessment1.getAssociatedID() == studentid && assessment1.getCourseCode().equals(courseCode))
                    return assessment;
            }
            System.out.println("No assessments match course code");
            throw new NoMatchingAssessment("Could not find matching assessment");
        }else{
            System.out.println("Session details are invalid");
            throw new UnauthorizedAccess("No session matches your credentials");
        }
    }


    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // check token is valid
        // check submission date has not passed
        // add assessment to list of assessments ready for correction


        Boolean valid = checkSession(studentid,token);

        if(valid){
            if(availableForCorrection.isEmpty()){
                availableForCorrection.add(completed);
            }else {
                boolean exists = false;
                AssessmentClass assessment1 = (AssessmentClass) completed;
                for (Assessment assessment : availableForCorrection) {
                    AssessmentClass assessment2 = (AssessmentClass) assessment;

                    if (assessment1.getAssociatedID() == assessment2.getAssociatedID() && assessment1.getCourseCode().equals(assessment2.getCourseCode())) {
                        int index = availableForCorrection.indexOf(assessment);
                        System.out.println("index: " + index);
                        System.out.println("Assessment already exists");
                        availableForCorrection.set(index, completed);
                        exists = true;
                    }
                }
                if(exists == false) {
                    System.out.println("Assessment does not exist");
                    availableForCorrection.add(completed);
                }
            }
            System.out.println("Assessment has been submitted for correction");
            System.out.println("Assessment submitted" + completed);
            System.out.println("Assessment ready for correction" + availableForCorrection);
        }else{
            System.out.println("Session details are invalid");
            throw new UnauthorizedAccess("No session matches your credentials");
        }

    }

    public void initialiseServer(){

        Student luke = new StudentClass(1, "mypass");
        students.add(luke);
        Student declan = new StudentClass(2, "decspass");
        students.add(declan);

        ArrayList<Question > questions = new ArrayList<>();
        String [] o1 = {"1. Liverpool", "2. Manchester City", "3. Chelsea"};
        String [] o2 = {"1. Oakland Raiders", "2. Dallas Cowboys", "3. Kansas City Chiefs"};
        String [] o3 = {"1. Miami Heat", "2. Toronto Raptors", "3. Houston Rockets"};

        Question q1 = new QuestionClass(1, "Who won the Premier League in 2019", o1);
        Question q2 = new QuestionClass(2, "Who won the Superbowl in 2020", o2);
        Question q3 = new QuestionClass(3, "Who won the NBA Finals in 2019", o3);

        questions.add(q1);
        questions.add(q2);
        questions.add(q3);


        LocalDate closingDate = LocalDate.parse("2020-02-17");
        String title = "CT414";
        for(Student student: students) {
            assessments.add(new AssessmentClass(closingDate, title, student.getStudentid(), questions));
        }
    }

    public boolean checkSession(int studentid, int token) throws UnauthorizedAccess{
        for(Session session: sessions){
            if(session.getStudentid() == studentid && session.getToken() == token){
                System.out.println("Session details are valid for this request");
                return true;
            }
        }
        throw new UnauthorizedAccess("No session matches your credentials");
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
//            Registry registry = LocateRegistry.createRegistry(20345);
            Registry registry = LocateRegistry.getRegistry(20345);
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}