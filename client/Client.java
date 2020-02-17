package client;
import assess.Assessment;
import assess.ExamServer;
import assess.Question;
import errors.InvalidOptionNumber;
import errors.InvalidQuestionNumber;
import errors.NoMatchingAssessment;
import errors.UnauthorizedAccess;

import java.util.List;
import java.util.Scanner;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Client() {}

    public static void getCommands(){
        System.out.println("\n ---- Available commands ---- ");
        System.out.println("To get all available Assessments =  all ");
        System.out.println("To download an Assessment =  get ");
        System.out.println("To edit an Assessment =  edit ");
        System.out.println("To submit an Assessment =  submit ");
        System.out.println("To print out commands =  help ");
        System.out.println("To end the session =  end ");
        System.out.println("----------------------- ");
    }

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
                getCommands();
            }

            Assessment assessment = null;

            while(authToken != 0){
                System.out.print("\n Please input a command: ");
                String command = scanner.next();

                if(command.equals("all")){
                    try {
                        System.out.println("\n" + stub.getAvailableSummary(authToken,studentId) + "\n");
                    }catch (UnauthorizedAccess e){
                        System.out.print("\n Unable to get Assessments!");
                        System.out.print("\n Error: " + e.getMessage());
                    }

                }else if(command.equals("get")) {

                    System.out.print("\n Please enter Course Code: ");
                    String courseCode = scanner.next();
                    System.out.print("\n Downloading Assessment..... ");
                    try {

                        assessment = stub.getAssessment(authToken, studentId, courseCode);
                        System.out.print("\n Assessment Downloaded! \n");

                        }catch(NoMatchingAssessment e){System.out.print("\n" + e);
                        }catch (UnauthorizedAccess e){
                        System.out.print("\n Unable to get Assessment!");
                        System.out.print("\n Error: " + e.getMessage());
                    }

                }else if(command.equals("edit")){

                    System.out.print("\n You are now in edit mode ");
                    System.out.print("\n ---- Edit mode Commands ---- ");
                    System.out.print("\n View list of all questions and answers options =  view ");
                    System.out.print("\n Return one question only with answer options =  viewone ");
                    System.out.print("\n To select an answer =  select ");
                    System.out.print("\n To view a selected answer =  check ");
                    System.out.print("\n To exit edit mode =  exit ");
                    System.out.print("\n ---------------------- ");

                    while(true){
                        if(assessment == null){
                            System.out.print("\n\n Exiting edit mode as you have not downloaded an Assignment yet! \n\n");
                            break;
                        }

                        System.out.print("\n Please input a command to edit assessment: ");
                        String editCommand = scanner.next();

                        List<Question> questions = assessment.getQuestions();

                        System.out.print("\n" + assessment.getInformation());

                        if(editCommand.equals("view")){

                        for (Question question: questions) {
                            System.out.print("\n Question " + question.getQuestionNumber() + ": " + question.getQuestionDetail());
                            System.out.print("\n The Options are: " + question.toString());
                        }

                        }else if(editCommand.equals("viewone")){
                            System.out.print("\n\n Please select a question number: ");
                            int response = scanner.nextInt();
                            for (Question question: questions) {
                                if(response == question.getQuestionNumber()) {
                                    System.out.print("\n Question " + question.getQuestionNumber() + ": " + question.getQuestionDetail());
                                    System.out.print("\n The Options are: " + question.toString());
                                }
                            }

                        }else if(editCommand.equals("select")){
                            System.out.print("\n\n What question do you want to answer? ");
                            int q = scanner.nextInt();
                            for (Question question: questions) {
                                if(q == question.getQuestionNumber()) {
                                    System.out.print("\n Question " + question.getQuestionNumber() + ": " + question.getQuestionDetail());
                                    System.out.print("\n The Options are: " + question.toString());
                                    System.out.print("\n Select an option: ");
                                    int o = scanner.nextInt();
                                    try {
                                        assessment.selectAnswer(question.getQuestionNumber(), o);
                                    }catch(InvalidOptionNumber e){
                                        System.out.println("Error: " + e.getMessage());
                                    }catch(InvalidQuestionNumber e){
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                }
                            }


                        }else if(editCommand.equals("check")){

                            System.out.print("\n\n What answer do you want to check? ");
                            int response = scanner.nextInt();
                            for (Question question: questions) {
                                if(response == question.getQuestionNumber()) {
                                    System.out.print("\n Question " + question.getQuestionNumber() + ": " + question.getQuestionDetail());
                                    System.out.print("\n The Options are: " + question.toString());
                                    System.out.print("\n You Selected option: " + assessment.getSelectedAnswer(response));

                                }
                            }

                        }else if(editCommand.equals("exit")) {

                            System.out.println("\n Finished editing");
                            break;

                        }else{
                            System.out.println("\n Invalid Command!");
                        }


                    }

                }else if(command.equals("submit")) {

                    System.out.print("\n Are you sure you want to submit your assessment(Y/N)? ");
                    String response = scanner.next();

                    if(response.equals("Y")){

                        if(assessment != null) {

                            try {
                                stub.submitAssessment(authToken, studentId, assessment);
                                System.out.print("\n Assessment Submitted! \n");
                            }catch (UnauthorizedAccess e){
                                System.out.print("\n Unable to submit Assessment!");
                                System.out.print("\n Error: " + e.getMessage());
                            }
                        }else{
                            System.out.print("\n You have not completed an assessment yet\n ");
                        }

                    }else if(response.equals("N")){

                    }else{
                        System.out.print("\n Invalid command \n ");
                    }


                }else if(command.equals("help")) {

                    getCommands();

                } else if(command.equals("end")) {

                    System.out.println("\n Goodbye");
                    break;

                }else{
                    System.out.println("\n Invalid Command!");
                }
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}