package assess;

import errors.*;

import java.util.ArrayList;

public interface Student{

    public String getPassword();

    public void setPassword(String password);

    public ArrayList<Assessment> getAssess();

    public void setAssess(Assessment ass);

    public int getStudentid();

    public void setStudentid(int studentid);

}
