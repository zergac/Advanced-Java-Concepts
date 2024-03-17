package sample;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;
    private String country;
    private String division;


    Customer(int id, String name, String address, String zip, String phone, int divID){
        this.id = id;
        this.name = name;
        this.address = address;
        postalCode = zip;
        this.phone = phone;
        divisionID = divID;
    }


    public int getId(){

        return id;
    }

    public String getName(){

        return name;
    }

    public String getAddress(){

        return address;
    }

    public String getPostalCode(){

        return postalCode;
    }

    public String getPhone(){

        return phone;
    }

    public int getDivisionID(){

        return divisionID;
    }

    public String getCreateDate(){
        return  createDate;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public String getLastUpdate(){
        return lastUpdate;
    }

    public String getLastUpdatedBy(){
        return lastUpdatedBy;
    }

    public String getCountry(){
        return country;
    }

    public String getDivision(){
        return division;
    }

    public void setCreateDate (String date){
        createDate = date;
    }

    public void setCreatedBy(String s){
        createdBy = s;
    }

    public void setLastUpdate(String update){
        lastUpdate = update;
    }

    public void setLastUpdatedBy(String upBy){
        lastUpdatedBy = upBy;
    }

    public void setName(String n){
        name = n;
    }

    public void setAddress(String a){
        address = a;
    }

    public void setPostalCode(String zip){
        postalCode = zip;
    }

    public void setPhone(String p){
        phone = p;
    }

    public void setDivisionID(int divID){
        divisionID = divID;
    }

    public void setCountry(String s){
        country = s;
    }

    public void setDivision(String d){
        division = d;
    }







}
