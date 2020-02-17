package engine;

import assess.Session;

import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;

public class SessionClass implements Session {

    private int token;
    private int studentid;
    private LocalTime expiry;
    private int exirationLength;

    public SessionClass(int studentid){
        String id = Integer.toString(studentid);
        this.token = id.hashCode();
        this.exirationLength = 30;
        setExpiry();
        this.studentid = studentid;
    }

    public void setExpiry(){
        this.expiry = LocalTime.now().plus(exirationLength,MINUTES);
        System.out.println("Expiry time: " + this.expiry);
    }

    public LocalTime getExpiry() {
        return expiry;
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
