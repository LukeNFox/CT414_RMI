package engine;

import assess.Assessment;
import assess.ExamServer;
import assess.Session;
import assess.Student;
import errors.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ExamEngine implements ExamServer {

    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Session> sessions = new ArrayList<>();

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

        return null;
    }

    // Return an assess.Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // For the moment method just returns an empty or null value to allow it to compile

        // check token is valid
        // return assessment object
        // ensure assessment available to student
        // set student id in assessment object


        return null;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // check token is valid
        // check submission date has not passed
        // add assessment to list of assessments ready for correction

    }

    public void initialiseServer(){

        Student luke = new StudentClass(1, "mypass");
        students.add(luke);
        Student declan = new StudentClass(2, "decspass");
        students.add(declan);

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
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}