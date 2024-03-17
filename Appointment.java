package sample;

public class Appointment {

    private int appID;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastupated;
    private String updatedBy;
    private int custID;
    private int userID;
    private int contactID;
    private String contact;

    public Appointment( int appID, String title, String description, String location, String type, int custID, int userID){
        this.appID = appID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.custID = custID;
        this.userID = userID;
    }

    public void setAppID(int id){
        appID = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String l){
        location = l;
    }

    public void setType(String t){
        type = t;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastupated(String lastupated) {
        this.lastupated = lastupated;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContactID(int contactID){
        this.contactID = contactID;
    }

    public void setContact (String c){
        contact = c;
    }

    public int getAppID() {
        return appID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastupated() {
        return lastupated;
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

    public String getStart() {
        return start;
    }

    public String getType() {
        return type;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public int getContactID() {
        return contactID;
    }

    public int getCustID() {
        return custID;
    }

    public int getUserID() {
        return userID;
    }

    public String getContact(){
        return contact;
    }




}

