package assess;

import errors.*;

import java.io.Serializable;
import java.util.ArrayList;

public interface Student extends Serializable {

    public String getPassword();

    public void setPassword(String password);

    public ArrayList<Assessment> getAssess();

    public void setAssess(Assessment ass);

    public int getStudentid();

    public void setStudentid(int studentid);

}
