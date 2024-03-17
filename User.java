package sample;
import java.util.Date;

public class User {

    private String user;
    private String datetime;
    private boolean isIn;


    public User(String user, String datetime, boolean isIn){
        this.user = user;
        this.datetime = datetime;
        this.isIn = isIn;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public String getUser() {
        return user;
    }

    public String getDatetime() {
        return datetime;
    }

    public boolean isIn() {
        return isIn;
    }


}
