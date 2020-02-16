package engine;

import assess.Session;

public class SessionClass implements Session {

    private int token;
    private int studentid;

    public SessionClass( int studentid){
        String id = Integer.toString(studentid);
        this.token = id.hashCode();
        this.studentid = studentid;
    }

    @Override
    public int getToken() {
        return token;
    }


    @Override
    public int getStudentid() {
        return studentid;
    }

    @Override
    public void setStudentid(int studentid) {

    }
}
