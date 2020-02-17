package engine;

import assess.*;
import client.AssessmentClass;
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
        int token = 0;

        System.out.println("Trying to login");

        for(Student student: students){
            if(student.getStudentid() == studentid) {
                realPass = student.getPassword();
            }
        }
        System.out.println("Real Password: " + realPass);

        if(realPass.equals(password)){
            Session session = new SessionClass(studentid);
            sessions.add(session);
            token = session.getToken();
        }else{
            throw new UnauthorizedAccess("Unable to login");
        }

	return token;
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // For the moment method just returns an empty or null value to allow it to compile

        // check token is valid
        // return list of assessment information available for that student

        Boolean valid = false;

        for(Session session: sessions){
            if(session.getStudentid() == studentid && session.getToken() == token){
                System.out.println("Session is valid");
                valid = true;
            }
        }

        if(valid == false){
            throw new UnauthorizedAccess("No session matches your credentials");
        }else{
            List<String> summaries = new ArrayList<>();

            for(Assessment assessment: assessments){
                if(assessment.getAssociatedID() == studentid)
                    summaries.add(assessment.getInformation());
            }
            return summaries;
        }
    }

    // Return an assess.Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // For the moment method just returns an empty or null value to allow it to compile

        // check token is valid
        // return assessment object
        // ensure assessment available to student

        Boolean valid = false;

        for(Session session: sessions){
            if(session.getStudentid() == studentid && session.getToken() == token){
                System.out.println("Session is valid");
                valid = true;
            }
        }

        if(valid == false){
            throw new UnauthorizedAccess("No session matches your credentials");
        }else{

            for(Assessment assessment: assessments){
                AssessmentClass assessment1 = (AssessmentClass) assessment;
                if(assessment1.getAssociatedID() == studentid && assessment1.getCourseCode().equals(courseCode))
                    return assessment;
            }
            throw new NoMatchingAssessment("Could not find matching assessment");
        }
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // check token is valid
        // check submission date has not passed
        // add assessment to list of assessments ready for correction


        Boolean valid = false;

        for(Session session: sessions){
            if(session.getStudentid() == studentid && session.getToken() == token){
                System.out.println("Session is valid");
                valid = true;
            }
        }

        if(valid == false){
            throw new UnauthorizedAccess("No session matches your credentials");
        }else{
            availableForCorrection.add(completed);
            System.out.println("Assessment has been submitted for correction");
        }

    }

    public void initialiseServer(){

        Student luke = new StudentClass(1, "mypass");
        students.add(luke);
        Student declan = new StudentClass(2, "decspass");
        students.add(declan);

        ArrayList<Question > questions = new ArrayList<>();
        String [] o1 = {"Liverpool", "Manchester City", "Chelsea"};
    	String [] o2 = {"Oakland Raiders", "Dallas Cowboys", "Kansas City Chiefs"};
    	String [] o3 = {"Miami Heat", "Toronto Raptors", "Houston Rockets"};

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

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub =
                (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.createRegistry(20345);
            //Registry registry = LocateRegistry.getRegistry(20345);
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}