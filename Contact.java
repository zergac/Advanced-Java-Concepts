package sample;

public class Contact {

    private int appID;
    private String title;
    private String type;
    private String description;
    private String start;
    private String end;
    private int custID;

    public Contact(int appID, String title, String type, String description, String start, String end, int custID){
        this.appID = appID;
        this.title = title;
        this.type = type;
        this. description = description;
        this.start = start;
        this.end = end;
        this.custID = custID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public String getType() {
        return type;
    }

    public int getCustID() {
        return custID;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public int getAppID() {
        return appID;
    }


}
