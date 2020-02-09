package client;
import assess.ExamServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    private Client() {}
    public static void main(String[] args) {
        int var1 = 20345;

        System.out.println("RMIRegistry port = " + var1);
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(var1);

            // Looking up the registry for the remote object
            ExamServer stub = (ExamServer) registry.lookup("ExamServer");

            stub.login(16342861, "luke");

            // System.out.println("Remote method invoked");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}