package engine;

import assess.Assessment;
import assess.ExamServer;
import errors.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ExamEngine implements ExamServer {

    // Constructor is required
    public ExamEngine() {
        super();
    }

    // Implement the methods defined in the assess.ExamServer interface...
    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password) throws
            UnauthorizedAccess, RemoteException {

        System.out.println("Trying to login");
	// TBD: You need to implement this method!
	// For the moment method just returns an empty or null value to allow it to compile

	return 0;	
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // For the moment method just returns an empty or null value to allow it to compile

        return null;
    }

    // Return an assess.Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
        // For the moment method just returns an empty or null value to allow it to compile

        return null;
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed) throws
            UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // TBD: You need to implement this method!
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