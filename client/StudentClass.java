package client;

import java.util.ArrayList;

import assess.Assessment;
import assess.Student;

public class StudentClass implements Student {

    private int studentid;
    private String password;
    private ArrayList<Assessment> assess = new ArrayList<>();

    public StudentClass (int studentid, String password) {
        this.studentid = studentid;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Assessment> getAssess() {
        return assess;
    }

    public void setAssess(Assessment ass) {
        this.assess.add(ass);
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }
}