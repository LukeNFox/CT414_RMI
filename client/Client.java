package client;
import assess.ExamServer;
import errors.UnauthorizedAccess;

import java.util.Scanner;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Client() {}
    public static void main(String[] args) {
        int var1 = 20345;
        int authToken = 0;

        System.out.println("RMIRegistry port = " + var1);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(var1);

            // Looking up the registry for the remote object
            ExamServer stub = (ExamServer) registry.lookup("ExamServer");

            // create a scanner so we can read the command-line input
            Scanner scanner = new Scanner(System.in);

            //  Welcome user
            System.out.print("\n \n ----- Welcome to Exam Center! ----- \n");

            // Request login details
            System.out.print("\n Lets Login ");
            System.out.print("\n Please enter your Student ID: ");
            int studentId = scanner.nextInt();
            System.out.print(" Please enter your password: ");
            String password = scanner.next();

            try {
                authToken = stub.login(studentId, password);
            }catch(UnauthorizedAccess e){
                System.out.println(" Incorrect Password!");
                authToken = 0;
            }catch (NullPointerException e){
                System.out.println(" User does not exist!");
                authToken = 0;
            }

            if(authToken != 0){
                System.out.println("\n You are Logged in");
                System.out.println("\n ---- Available commands ---- ");
                System.out.println("To get all available Assessments =  all ");
                System.out.println("To end the session =  end ");
                System.out.println("----------------------- ");
            }

            while(authToken != 0){
                System.out.print("\n Please input a command: ");
                String command = scanner.next();

                if(command.equals("all")){
                    System.out.println("\n" + stub.getAvailableSummary(authToken,studentId) + "\n");
                }else if(command.equals("end")) {
                    System.out.println("\n Goodbye");
                    break;
                }else{
                    System.out.println("\n Invalid Command!");
                }
            }

            // System.out.println("Remote method invoked");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}