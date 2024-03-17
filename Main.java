package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.text.TableView;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    final static ObservableList<Customer> customerData = FXCollections.observableArrayList();
    final static ObservableList<Appointment> appoinments = FXCollections.observableArrayList();
    final static ObservableList<Appointment> appViews = FXCollections.observableArrayList();
    final static ObservableList<Report> reportList = FXCollections.observableArrayList();
    final static ObservableList<Contact> contactOne = FXCollections.observableArrayList();
    final static ObservableList<Contact> contactTwo = FXCollections.observableArrayList();
    final static ObservableList<Contact> contactThree = FXCollections.observableArrayList();
    final static ObservableList<String[]> con = FXCollections.observableArrayList();
    final static ObservableList<CustApp> custNApp = FXCollections.observableArrayList();
    final static ObservableList<String[]> div = FXCollections.observableArrayList();
    final static ObservableList<String> usa = FXCollections.observableArrayList();
    final static ObservableList<String> united = FXCollections.observableArrayList();
    final static ObservableList<String> canada = FXCollections.observableArrayList();
    final static ObservableList<User> userList = FXCollections.observableArrayList();
    final ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
    static ArrayList<String[]> divList = new ArrayList<String[]>();
    static String countryPicked = "";
    static String userIDNum = "";
    int customerID;

    @Override
    public void start(Stage stage) throws Exception {

        JDBC javaDB = new JDBC();
        javaDB.makeConnection();
        Connection conn = javaDB.getConnection();
        javaDB.makePreparedStatement("SELECT * FROM users", conn);
        PreparedStatement stmt = javaDB.getPreparedStatement();
        ResultSet result = stmt.executeQuery();
        ArrayList<String[]> all = new ArrayList<String[]>();

        /**
         * Obtaining login info
         */
        while (result.next()) {
            String[] letters = new String[2];
            String id = result.getString("User_Name");
            letters[0] = id;
            String password = result.getString("Password");
            letters[1] = password;
            all.add(letters);
        }

        javaDB.makePreparedStatement("SELECT * FROM customers", conn);
        PreparedStatement custStmt = javaDB.getPreparedStatement();
        ResultSet custResult = custStmt.executeQuery();

        /**
         * Obtaining customer info from SQL
         */
        while (custResult.next()) {
            int id = custResult.getInt("Customer_ID");
            String name = custResult.getString("Customer_Name");
            String address = custResult.getString("Address");
            String zip = custResult.getString("Postal_Code");
            String phone = custResult.getString("Phone");
            int divID = custResult.getInt("Division_ID");
            String createdDate = custResult.getString("Create_Date");
            String createdBy = custResult.getString("Created_By");
            String lastUpdate = custResult.getString("Last_Update");
            String updatedBy = custResult.getString("Last_Updated_By");

            Customer c = new Customer(id, name, address, zip, phone, divID);
            c.setCreateDate(createdDate);
            c.setCreatedBy(createdBy);
            c.setLastUpdate(lastUpdate);
            c.setLastUpdatedBy(updatedBy);
            c.setCountry(getCountryID(divID));
            c.setDivision(getDivisionName(divID));
            customerData.add(c);
        }

        javaDB.makePreparedStatement("SELECT * FROM appointments", conn);
        PreparedStatement appStmt = javaDB.getPreparedStatement();
        ResultSet appResult = appStmt.executeQuery();

        /**
         * Obtaining appointment info from SQL
         */
        while (appResult.next()) {
            int appID = appResult.getInt("Appointment_ID");
            String title = appResult.getString("Title");
            String descrip = appResult.getString("Description");
            String location = appResult.getString("Location");
            String type = appResult.getString("Type");
            String start = appResult.getString("Start");
            String end = appResult.getString("End");
            String createdDate = appResult.getString("Create_Date");
            String createdBy = appResult.getString("Created_By");
            String lastUpdate = appResult.getString("Last_Update");
            String updatedBy = appResult.getString("Last_Updated_By");
            int custID = appResult.getInt("Customer_ID");
            int userID = appResult.getInt("User_ID");
            int contactID = appResult.getInt("Contact_ID");

            Appointment a = new Appointment(appID, title, descrip, location, type, custID, userID);
            a.setCreateDate(createdDate);
            a.setCreatedBy(createdBy);
            a.setLastupated(lastUpdate);
            a.setUpdatedBy(updatedBy);
            a.setStart(estTtime(start));
            a.setEnd(estTtime(end));
            a.setContactID(contactID);
            a.setContact(getContactName(contactID));
            appoinments.add(a);
        }

        /**
         * Obtaining contact info from SQL
         */
        javaDB.makePreparedStatement("SELECT * FROM contacts", conn);
        PreparedStatement conStmt = javaDB.getPreparedStatement();
        ResultSet conResult = conStmt.executeQuery();

        while (conResult.next()) {
            String[] contacts = new String[3];
            String conId = conResult.getString("Contact_ID");
            contacts[0] = conId;
            String name = conResult.getString("Contact_Name");
            contacts[1] = name;
            String email = conResult.getString("Email");
            contacts[2] = email;
            con.add(contacts);
        }

        /**
         * Obtaining division info from SQL
         */
        javaDB.makePreparedStatement("SELECT * FROM first_level_divisions", conn);
        PreparedStatement divStmt = javaDB.getPreparedStatement();
        ResultSet divResult = divStmt.executeQuery();

        while (divResult.next()) {
            String[] division = new String[3];
            String divID = divResult.getString("Division_ID");
            division[0] = divID;
            String s = divResult.getString("Division");
            division[1] = s;
            String cID = divResult.getString("Country_ID");
            division[2] = cID;
            div.add(division);

            if (cID.equals("1")){
                usa.add(s);
            }
            else if (cID.equals("2")){
                united.add(s);
            }
            else{
                canada.add(s);
            }
        }

        conn.close();

        Locale language = Locale.getDefault();
        String lang = language.getDisplayLanguage();
        String f = "French";

        StackPane Loginlayout = new StackPane();
        Scene scene = new Scene(Loginlayout, 600, 600);
        StackPane insideLogin = new StackPane();
        Scene loginIn = new Scene(insideLogin, 1200, 600);
        StackPane addCustLayout = new StackPane();
        Scene custLayout = new Scene(addCustLayout, 600, 600);
        StackPane updateCustLayout = new StackPane();
        Scene updateLayout = new Scene(updateCustLayout, 600, 600);
        StackPane addAppLayout = new StackPane();
        Scene addAppointmentLayout = new Scene(addAppLayout, 600, 600);
        StackPane updateAppLayout = new StackPane();
        Scene updateLayoutApp = new Scene(updateAppLayout, 600, 600);
        StackPane reports = new StackPane();
        Scene reportsLayout = new Scene(reports, 1200, 700);

        Alert custALert = new Alert(Alert.AlertType.INFORMATION);
        Alert upcomingAppsALert = new Alert(Alert.AlertType.INFORMATION);
        Alert wrongLogin = new Alert(Alert.AlertType.INFORMATION);
        wrongLogin.setTitle("Error");
        wrongLogin.setHeaderText("Incorrect username and/or password");
        Alert weekendAlert = new Alert(Alert.AlertType.INFORMATION);
        weekendAlert.setTitle("Error");
        weekendAlert.setHeaderText("Can not schedule on a weekend");
        Alert noApps = new Alert(Alert.AlertType.INFORMATION);
        noApps.setHeaderText("There are not upcoming appointments");
        Alert delCust = new Alert(Alert.AlertType.CONFIRMATION);
        delCust.setTitle("Delecte Customer");
        delCust.setHeaderText("Are you sure you want to delete the Customer?");
        Alert overlapAlert = new Alert(Alert.AlertType.INFORMATION);
        overlapAlert.setTitle("Error");
        overlapAlert.setHeaderText("Can not overlap appointment");

        Label signIn = createLabel("Sign in", 10, -175);
        signIn.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label addCust = createLabel("Add a Customer", 0, -200);
        addCust.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label updateCust = createLabel("Update Customer", 0, -200);
        updateCust.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label cust = createLabel("Customer", -475, -250);
        cust.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label apps = createLabel("Appointments", -450, 25);
        apps.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label addApp = createLabel("Add Appointment", 0, -275);
        addApp.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label updateApp = createLabel("Update Appointment", 0, -275);
        updateApp.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
        Label appReports = createLabel("Number of Appointments \nby Month and Type", -425, -300);
        appReports.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Label earlyAppLabel = createLabel("Customer's Earliest \nAppointment", -455, 45);
        earlyAppLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Label anika = createLabel("Anika Costa", 15, -295);
        anika.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Label daniel = createLabel("Daniel Garcia", 25, -95);
        daniel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        Label li = createLabel("Li Lee", -15, 110);
        li.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        /**
         * Login layout
         */
        Label userName = createLabel("Username", -100, -50);
        Label pass = createLabel("Password", -100, 0);
        TextField userText = createTextfield(25, -50, 150);

        PasswordField passText = new PasswordField();
        passText.setTranslateX(25);
        passText.setMaxWidth(150.00);

        /**
         * Add customer layout
         */
        Label addCustName = createLabel("Name", -150, -70);
        Label addCustAddress = createLabel("Address", -150, -40);
        Label addCustZip = createLabel("Postal Code", -150, -10);
        Label addCustPhone = createLabel("Phone Number", -150, 20);
        Label country = createLabel("Country", -150, 50);
        Label division = createLabel("Division", -150, 80);
        TextField addNameText = createTextfield(0, -70, 150);
        TextField addAddressText = createTextfield(0, -40, 150);
        TextField addZipText = createTextfield(0, -10, 150);
        TextField addPhoneText = createTextfield(0, 20, 150);

        ArrayList<String> countries = new ArrayList<String>();
        countries.add("United States");
        countries.add("United Kingdom");
        countries.add("Canada");
        ComboBox countriesCombo = new ComboBox(FXCollections.observableList(countries));
        countriesCombo.setTranslateY(50);
        countriesCombo.setTranslateX(0);
        countriesCombo.setMaxWidth(150.00);

        ComboBox divisionCombo = new ComboBox(FXCollections.observableList(divList));
        divisionCombo.setTranslateY(80);
        divisionCombo.setTranslateX(0);
        divisionCombo.setMaxWidth(150.00);

        /**
         * Update customer layout
         */
        Label updateCustName = createLabel("Name", -150, -70);
        Label updateCustAddress = createLabel("Address", -150, -40);
        Label updateCustZip = createLabel("Postal Code", -150, -10);
        Label updateCustPhone = createLabel("Phone Number", -150, 20);
        Label updCountry = createLabel("Country", -150, 50);
        Label updDivision = createLabel("Division", -150, 80);
        TextField upNameText = createTextfield(0, -70, 150);
        TextField upAddressText = createTextfield(0, -40, 150);
        TextField upZipText = createTextfield(0, -10, 150);
        TextField upPhoneText = createTextfield(0, 20, 150);

        ComboBox updCountriesCombo = new ComboBox(FXCollections.observableList(countries));
        updCountriesCombo.setTranslateY(50);
        updCountriesCombo.setTranslateX(0);
        updCountriesCombo.setMaxWidth(150.00);

        ComboBox updDivisionCombo = new ComboBox(FXCollections.observableList(divList));
        updDivisionCombo.setTranslateY(80);
        updDivisionCombo.setTranslateX(0);
        updDivisionCombo.setMaxWidth(150.00);

        Label addAppTitle = createLabel("Title", -150, -170);
        Label addAppdescrip = createLabel("Description", -150, -140);
        Label addAppLocation = createLabel("Location", -150, -110);
        Label addAppType = createLabel("Type", -150, -80);
        Label addAppStart = createLabel("Start", -150, -50);
        Label addAppEnd = createLabel("End", -150, -20);
        Label addAppCon = createLabel("Contact", -150, 10);
        Label updAppTitle = createLabel("Title", -150, -170);
        Label updAppDesc = createLabel("Description", -150, -140);
        Label updAppLoc = createLabel("Location", -150, -110);
        Label updAppType = createLabel("Type", -150, -80);
        Label updAppStart = createLabel("Start", -150, -50);
        Label updAppEnd = createLabel("End", -150, -20);
        Label updAppDateCreated = createLabel("Created Date", -150, 10);
        Label updAppCreatedBy = createLabel("Created By", -150, 40);
        Label updAppDateUpdated= createLabel("Updated Date", -150, 70);
        Label updAppUpdatedBy = createLabel("Updated By", -150, 100);
        Label updAppCustId = createLabel("Customer ID", -150, 130);
        Label updAppUserId = createLabel("User ID", -150, 160);
        Label updAppContactId = createLabel("Contact ID", -150, 190);

        TextField addAppTitleText = createTextfield(0, -170, 150);
        TextField addAppDescripText = createTextfield(0, -140, 150);
        TextField addAppLocationText = createTextfield(0, -110, 150);
        TextField addAppTypeText = createTextfield(0, -80, 150);
        TextField updAppTitleText = createTextfield(0, -170, 150);
        TextField updAppDescText = createTextfield(0, -140, 150);
        TextField updAppLocText = createTextfield(0, -110, 150);
        TextField updAppTypeText = createTextfield(0, -80, 150);
        TextField updAppCreatedDateText = createTextfield(0, 10, 150);
        TextField updAppCreatedByText = createTextfield(0, 40, 150);
        TextField updAppLastUpdateText = createTextfield(0, 70, 150);
        TextField updAppUpdatedByText = createTextfield(0, 100, 150);
        TextField updAppCustIDText = createTextfield(0, 130, 150);
        TextField updAppUserIDText = createTextfield(0, 160, 150);
        TextField updAppContactIDText = createTextfield(0, 190, 150);

        DatePicker start = createDatePicker(-25, -50, 100);
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate startDate = start.getValue();
                boolean weekend = isWeekend(startDate);
                if (weekend){
                    weekendAlert.showAndWait();
                    start.getEditor().clear();
                }
            }
        });

        DatePicker end = createDatePicker(-25, -20, 100);
        end.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate endDate = end.getValue();
                boolean weekend = isWeekend(endDate);
                if (weekend){
                    weekendAlert.showAndWait();
                    end.getEditor().clear();
                }
            }
        });

        DatePicker updStart = createDatePicker(-25, -50, 100);
        updStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate updStartDate = updStart.getValue();
                boolean weekend = isWeekend(updStartDate);
                if (weekend){
                    weekendAlert.showAndWait();
                    updStart.getEditor().clear();
                }
            }
        });

        DatePicker updEnd = createDatePicker(-25, -20, 100);
        updEnd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDate updEndDate = updEnd.getValue();
                boolean weekend = isWeekend(updEndDate);
                if (weekend){
                    weekendAlert.showAndWait();
                    updEnd.getEditor().clear();
                }
            }
        });

        ArrayList<String> options = createTime();
        ComboBox time = new ComboBox(FXCollections.observableList(options));
        time.setTranslateY(-50);
        time.setTranslateX(85);
        time.setMaxWidth(100.00);

        ArrayList<String> optionsEnd = createTime();
        ComboBox timeEnd = new ComboBox(FXCollections.observableList(optionsEnd));
        timeEnd.setTranslateY(-20);
        timeEnd.setTranslateX(85);
        timeEnd.setMaxWidth(100.00);

        ArrayList<String> updOptions = createTime();
        ComboBox updStartTime = new ComboBox(FXCollections.observableList(updOptions));
        updStartTime.setTranslateY(-50);
        updStartTime.setTranslateX(85);
        updStartTime.setMaxWidth(100.00);

        ArrayList<String> updOptionsEnd = createTime();
        ComboBox updTimeEnd = new ComboBox(FXCollections.observableList(updOptionsEnd));
        updTimeEnd.setTranslateY(-20);
        updTimeEnd.setTranslateX(85);
        updTimeEnd.setMaxWidth(100.00);

        ComboBox selectContacts = new ComboBox(FXCollections.observableList(getContactNames()));
        selectContacts.setTranslateX(0);
        selectContacts.setTranslateY(10);
        selectContacts.setMaxWidth(150);

        ZoneId zone = ZoneId.systemDefault();
        String z = zone.getId();
        Label theZone = new Label();
        theZone.setText(z);
        theZone.setTranslateX(10);
        theZone.setTranslateY(-150);

        RadioButton allAppsRB = createdRadioButton("All Appointments", -200, 25);
        RadioButton monthAppRB = createdRadioButton("Current Month", 0, 25);
        RadioButton weekAppRB = createdRadioButton("Current Week", 200, 25);

        /**
         * TableView of Customers
         */
        TableView tableCustomers = new TableView();
        tableCustomers.setMaxSize(600.00, 200.00);
        tableCustomers.setTranslateX(-250);
        tableCustomers.setTranslateY(-125);
        TableColumn custID = new TableColumn("Customer_ID");
        custID.setCellValueFactory(new PropertyValueFactory<Customer,String>("id"));
        TableColumn custName = new TableColumn("Customer_Name");
        custName.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        TableColumn address = new TableColumn("Address");
        address.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
        TableColumn zip = new TableColumn("Postal Code");
        zip.setCellValueFactory(new PropertyValueFactory<Customer,String>("postalCode"));
        TableColumn phone = new TableColumn("Phone");
        phone.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        TableColumn divID = new TableColumn("Division_ID");
        divID.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("divisionID"));

        tableCustomers.getColumns().addAll(custID, custName, address, zip, phone, divID);
        tableCustomers.setItems(customerData);

        /**
         * TableView of Appointments
         */
        TableView tableAppoinments = new TableView();
        tableAppoinments.setMaxSize(800.00, 200.00);
        tableAppoinments.setTranslateX(-150);
        tableAppoinments.setTranslateY(150);
        TableColumn custAppID = new TableColumn("Appointment_ID");
        custAppID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("appID"));
        TableColumn appTitle = new TableColumn("Title");
        appTitle.setCellValueFactory(new PropertyValueFactory<Appointment,String>("title"));
        TableColumn appDescrip = new TableColumn("Description");
        appDescrip.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
        TableColumn appLocation = new TableColumn("Location");
        appLocation.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
        TableColumn appType = new TableColumn("Type");
        appType.setCellValueFactory(new PropertyValueFactory<Appointment,String>("type"));
        TableColumn appContact = new TableColumn("Contact");
        appContact.setCellValueFactory(new PropertyValueFactory<Appointment,String>("contact"));
        TableColumn appStart = new TableColumn("Start");
        appStart.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        TableColumn appEnd = new TableColumn("End");
        appEnd.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        TableColumn appCustID = new TableColumn("Customer_ID");
        appCustID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("custID"));
        TableColumn userID = new TableColumn("User_ID");
        userID.setCellValueFactory(new PropertyValueFactory<Appointment,Integer>("userID"));

        tableAppoinments.getColumns().addAll(custAppID, appTitle, appDescrip, appLocation, appType, appContact, appStart, appEnd, appCustID, userID);
        tableAppoinments.setItems(appoinments);

        String[] reportMonth = new String[appoinments.size()];
        String[] reportType = new String[appoinments.size()];
        for (int i = 0; i < appoinments.size(); i++){
            Appointment a = appoinments.get(i);
            String startApp = a.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(startApp, formatter);
            String month = dateTime.getMonth().toString();
            reportMonth[i] = month;
            reportType[i] = a.getType();
        }

        ArrayList<String[]> finalMonths = monthsInOrder(reportMonth, reportType);
        for (int i = 0; i < finalMonths.size(); i++){
            String[] stArr = finalMonths.get(i);
            reportMonth[i] = stArr[0];
            reportType[i] = stArr[1];
        }

        TableView reportsTable = new TableView();
        reportsTable.setMaxSize(290, 250);
        reportsTable.setTranslateX(-425);
        reportsTable.setTranslateY(-140);
        TableColumn month = new TableColumn("Month");
        month.setCellValueFactory(new PropertyValueFactory<Report, String>("month"));
        TableColumn type = new TableColumn("Type");
        type.setCellValueFactory(new PropertyValueFactory<Report, String>("type"));
        TableColumn count = new TableColumn("Number of Appointments");
        count.setMinWidth(150);
        count.setCellValueFactory(new PropertyValueFactory<Report, Integer>("count"));


        diffTypes(reportMonth, reportType);
        reportsTable.getColumns().addAll(month, type, count);
        reportsTable.setItems(reportList);

        TableView contact1 = new TableView();
        contact1.setMaxSize(600, 150);
        contact1.setTranslateX(250);
        contact1.setTranslateY(-200);
        TableColumn appID1 = new TableColumn("Appointment ID");
        appID1.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("appID"));
        appID1.setMinWidth(100);
        TableColumn title1 = new TableColumn("Title");
        title1.setCellValueFactory(new PropertyValueFactory<Contact, String>("title"));
        TableColumn type1 = new TableColumn("Type");
        type1.setCellValueFactory(new PropertyValueFactory<Contact, String>("type"));
        TableColumn desc1 = new TableColumn("Description");
        desc1.setCellValueFactory(new PropertyValueFactory<Contact, String>("description"));
        TableColumn start1 = new TableColumn("Start");
        start1.setCellValueFactory(new PropertyValueFactory<Contact, String>("start"));
        TableColumn end1 = new TableColumn("End");
        end1.setCellValueFactory(new PropertyValueFactory<Contact, String>("end"));
        TableColumn custID1 = new TableColumn("Customer ID");
        custID1.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("custID"));
        contact1.getColumns().addAll(appID1, title1, type1, desc1, start1, end1, custID1);
        getContact(contactOne, "Anika Costa");
        contact1.setItems(contactOne);

        TableView contact2 = new TableView();
        contact2.setMaxSize(600, 150);
        contact2.setTranslateX(250);
        contact2.setTranslateY(0);
        TableColumn appID2 = new TableColumn("Appointment ID");
        appID2.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("appID"));
        appID2.setMinWidth(100);
        TableColumn title2 = new TableColumn("Title");
        title2.setCellValueFactory(new PropertyValueFactory<Contact, String>("title"));
        TableColumn type2 = new TableColumn("Type");
        type2.setCellValueFactory(new PropertyValueFactory<Contact, String>("type"));
        TableColumn desc2 = new TableColumn("Description");
        desc2.setCellValueFactory(new PropertyValueFactory<Contact, String>("description"));
        TableColumn start2 = new TableColumn("Start");
        start2.setCellValueFactory(new PropertyValueFactory<Contact, String>("start"));
        TableColumn end2 = new TableColumn("End");
        end2.setCellValueFactory(new PropertyValueFactory<Contact, String>("end"));
        TableColumn custID2 = new TableColumn("Customer ID");
        custID2.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("custID"));
        contact2.getColumns().addAll(appID2, title2, type2, desc2, start2, end2, custID2);
        getContact(contactTwo, "Daniel Garcia");
        contact2.setItems(contactTwo);

        TableView contact3 = new TableView();
        contact3.setMaxSize(600, 150);
        contact3.setTranslateX(250);
        contact3.setTranslateY(200);
        TableColumn appID3 = new TableColumn("Appointment ID");
        appID3.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("appID"));
        appID3.setMinWidth(100);
        TableColumn title3 = new TableColumn("Title");
        title3.setCellValueFactory(new PropertyValueFactory<Contact, String>("title"));
        TableColumn type3 = new TableColumn("Type");
        type3.setCellValueFactory(new PropertyValueFactory<Contact, String>("type"));
        TableColumn desc3 = new TableColumn("Description");
        desc3.setCellValueFactory(new PropertyValueFactory<Contact, String>("description"));
        TableColumn start3 = new TableColumn("Start");
        start3.setCellValueFactory(new PropertyValueFactory<Contact, String>("start"));
        TableColumn end3 = new TableColumn("End");
        end3.setCellValueFactory(new PropertyValueFactory<Contact, String>("end"));
        TableColumn custID3 = new TableColumn("Customer ID");
        custID3.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("custID"));
        contact3.getColumns().addAll(appID3, title3, type3, desc3, start3, end3, custID3);
        getContact(contactThree, "Li Lee");
        contact3.setItems(contactThree);

        TableView custApp = new TableView();
        custApp.setMaxSize(290, 250);
        custApp.setTranslateX(-425);
        custApp.setTranslateY(200);
        TableColumn customerIDApp = new TableColumn("Customer ID");
        customerIDApp.setCellValueFactory(new PropertyValueFactory<CustApp, Integer>("id"));
        TableColumn earlyApp = new TableColumn("Appointment");
        earlyApp.setCellValueFactory(new PropertyValueFactory<CustApp, String>("date"));
        custApp.getColumns().addAll(customerIDApp, earlyApp);
        getEarlyApps();
        custApp.setItems(custNApp);


        tableCustomers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                allAppsRB.setSelected(false);
                monthAppRB.setSelected(false);
                weekAppRB.setSelected(false);
                appViews.clear();
                ObservableList<Customer> appCusts = tableCustomers.getSelectionModel().getSelectedItems();
                Customer appCust = appCusts.get(0);
                int customerid = appCust.getId();

                appoinments.forEach( (app) -> {
                    Appointment a = app;;
                    if (app.getCustID() == customerid){
                        appViews.add(app);
                    }
                });

            tableAppoinments.setItems(appViews);
            }
        });

        allAppsRB.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tableAppoinments.setItems(appoinments);
                allAppsRB.setSelected(true);
                monthAppRB.setSelected(false);
                weekAppRB.setSelected(false);
            }
        });

        monthAppRB.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                allAppsRB.setSelected(false);
                monthAppRB.setSelected(true);
                weekAppRB.setSelected(false);

                LocalDate currentDate = LocalDate.now();
                Month currentMonth = currentDate.getMonth();
                String currMonth = currentMonth.toString();
                ObservableList<Appointment> appMonthViews = FXCollections.observableArrayList();

                for (int i = 0; i < appoinments.size(); i++){
                    Appointment a = appoinments.get(i);
                    String appSt = a.getStart();
                    appSt = appSt.substring(0,10);
                    LocalDate stDate = LocalDate.parse(appSt);
                    Month stMonth = stDate.getMonth();
                    String sm = stMonth.toString();
                    if (currMonth.equals(sm)){
                        appMonthViews.add(a);
                    }
                }

                tableAppoinments.setItems(appMonthViews);
            }
        });

        weekAppRB.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                allAppsRB.setSelected(false);
                monthAppRB.setSelected(false);
                weekAppRB.setSelected(true);

                LocalDate currentWeek = LocalDate.now();
                int dayOfMonth = currentWeek.getDayOfMonth();
                String month = currentWeek.getMonth().toString();
                ObservableList<Appointment> appWeekViews = FXCollections.observableArrayList();
                dayOfWeek(month, dayOfMonth, appWeekViews);

                tableAppoinments.setItems(appWeekViews);

            }
        });

        Button signButton = createButton("Sign in", 15, 200);
        signButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String user = userText.getText();
                userIDNum = user;
                String password = passText.getText();
                boolean b = getIn(all, user, password);
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                User u;

                if (b == true) {
                    stage.setScene(loginIn);
                    stage.centerOnScreen();

                    u = new User(user, formatter.format(date), true);

                    Appointment a = appSoonLogin();
                    if (a == null) {
                        noApps.showAndWait();
                    }
                    else {
                        upcomingAppsALert.setHeaderText("Upcoming appointment for Appointment ID-" + a.getAppID() + " on " + a.getStart());
                        upcomingAppsALert.showAndWait();
                    }

                    es.scheduleAtFixedRate(new Runnable() {
                            public void run() {
                                Appointment a = appIn15();
                                if (a != null) {
                                    upcomingAppsALert.showAndWait();
                                }
                            }
                            }, 0, 60, TimeUnit.SECONDS);
                }

                else{
                    wrongLogin.showAndWait();
                    passText.clear();

                    u = new User(user, formatter.format(date), true);
                }

                userList.add(u);

                File fileScan = new File("login_activity.txt");
                try {
                    Scanner sc = new Scanner(fileScan);
                    while (sc.hasNextLine() == true) {
                        String line = sc.nextLine();
                        String[] letters = line.split(", ");
                        String s = letters[0];
                        String s2 = letters[1];
                        String s3 = letters[2];

                        User userOld = new User(s, s2, Boolean.parseBoolean(s3.substring(17)));
                        userList.add(userOld);
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                File file = new File("login_activity.txt");
                try {

                    FileWriter writer = new FileWriter(file);
                    for (int i = 0; i < userList.size(); i++){
                        User userLogs = userList.get(i);
                        writer.write(userLogs.getUser() + ", " + userLogs.getDatetime() + ", " + "Login Succesful: " + userLogs.isIn() + "\n");
                    }

                    writer.close();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                userText.clear();
                passText.clear();

            }
        });

        Button addCustButton = createButton("Add Customer", 300, -200);
        addCustButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(custLayout);
                stage.centerOnScreen();

            }
        });

        divisionCombo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String country = countriesCombo.getSelectionModel().getSelectedItem().toString();
                if (country.equals("United States")){
                    divisionCombo.setItems(usa);
                }
                else if (country.equals("United Kingdom")){
                    divisionCombo.setItems(united);
                }
                else{
                    divisionCombo.setItems(canada);
                }

            }
        });

        Button addCustAddButton = createButton("Add", 200, 250);
        addCustAddButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                customerID = findMaxCust() + 1;
                String name = addNameText.getText();
                String address = addAddressText.getText();
                String zip = addZipText.getText();
                String phone = addPhoneText.getText();

                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
                String dateCreated = dateNow.format(formatDate);

                String createdBy = userIDNum;
                String lastUpdate = dateCreated;
                String updatedBy = userIDNum;

                Object country = countriesCombo.getValue();
                countryPicked = country.toString();

                int divID = getDivisionID(divisionCombo.getValue().toString());

                JDBC javaDB2 = new JDBC();
                javaDB2.makeConnection();
                Connection conn2 = javaDB2.getConnection();

                try {
                    Statement addCustStmt = conn2.createStatement();
                    addCustStmt.executeUpdate("INSERT INTO customers VALUES ( '" + customerID + "','" + name + "','" + address + "','" + zip + "','"
                            + phone + "','" + dateCreated + "','" + createdBy + "','" + dateCreated + "','" + updatedBy + "','" + divID + "')");

                    Customer c = new Customer(customerID, name, address, zip, phone, divID);
                    c.setCreateDate(dateCreated);
                    c.setCreatedBy(createdBy);
                    c.setLastUpdate(lastUpdate);
                    c.setLastUpdatedBy(updatedBy);

                    customerData.add(c);
                    tableCustomers.setItems(customerData);

                    conn2.close();
                }
                catch (SQLException throwables) {
                    System.out.println("cant insert");
                }

                addNameText.clear();
                addAddressText.clear();
                addZipText.clear();
                addPhoneText.clear();
                countriesCombo.getEditor().clear();
                divisionCombo.getEditor().clear();
                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });

        Button addCustCancelButton = createButton("Cancel", 250, 250);
        addCustCancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addNameText.clear();
                addAddressText.clear();
                addZipText.clear();
                addPhoneText.clear();
                countriesCombo.getEditor().clear();
                divisionCombo.getEditor().clear();

                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });

        Button updateCustButton = createButton("Update Customer", 300, -150);
        updateCustButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<Customer> cust = tableCustomers.getSelectionModel().getSelectedItems();
                Customer c = cust.get(0);

                String id = String.valueOf(c.getId());
                upNameText.setText(c.getName());
                upAddressText.setText(c.getAddress());;
                upZipText.setText(c.getPostalCode());
                upPhoneText.setText(c.getPhone());
                updCountriesCombo.setValue(c.getCountry());
                updDivisionCombo.setValue(c.getDivision());
                
                stage.setScene(updateLayout);
                stage.centerOnScreen();
            }
        });

        Button updateCustupButton = createButton("Update", 175, 250);
        updateCustupButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<Customer> cust = tableCustomers.getSelectionModel().getSelectedItems();
                Customer c = cust.get(0);

                c.setName(upNameText.getText());
                c.setAddress(upAddressText.getText());
                c.setPostalCode(upZipText.getText());
                c.setPhone(upPhoneText.getText());

                Object country = updCountriesCombo.getValue();
                countryPicked = country.toString();
                c.setCountry(countryPicked);

                Object d = updDivisionCombo.getValue();
                String di = d.toString();
                c.setDivision(di);

                c.setDivisionID(getDivisionID(di));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestamp = dateFormat.format(new java.util.Date());
                c.setLastUpdate(timestamp);

                JDBC javaDB3 = new JDBC();
                javaDB3.makeConnection();
                Connection conn3 = javaDB3.getConnection();
                Statement updateCustStmt = null;
                try {
                    updateCustStmt = conn3.createStatement();
                    updateCustStmt.executeUpdate("UPDATE customers SET Customer_Name='" + upNameText.getText() + "', Address='" + upAddressText.getText()
                            + "', Postal_Code='" + upZipText.getText() + "', Phone='" + upPhoneText.getText() + "', Create_Date='" + c.getCreateDate()
                            + "', Created_By='" + c.getCreatedBy() + "', Last_Update='" + timestamp + "', Last_Updated_By='" + c.getLastUpdatedBy()
                            + "', Division_ID='" + c.getDivisionID() + "' WHERE Customer_ID='" + c.getId() + "'");

                    for (int i = 0; i < customerData.size(); i++){
                        Customer custo = customerData.get(i);
                        if (custo.getId() == c.getId()){
                            customerData.set(i, c);
                        }
                    }

                conn3.close();

                }
                catch (SQLException throwables) {
                    System.out.println("cant update");
                }

                upNameText.clear();
                upAddressText.clear();
                upZipText.clear();
                upPhoneText.clear();
                updCountriesCombo.getEditor().clear();
                updDivisionCombo.getEditor().clear();

                stage.setScene(loginIn);
                stage.centerOnScreen();

            }
        });

        Button updateCustCancelButton = createButton("Cancel", 250, 250);
        updateCustCancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });

        Button deleteCustButton = createButton("Delete Customer",  300, -100);
        deleteCustButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Optional<ButtonType> option = delCust.showAndWait();
                if (option.get() == ButtonType.OK) {
                    custALert.setHeaderText("Customer and related appointments deleted");
                    custALert.showAndWait();

                    ObservableList<Customer> cust = tableCustomers.getSelectionModel().getSelectedItems();
                    Customer c = cust.get(0);
                    int id = c.getId();

                    for (int i = 0; i < appoinments.size(); i++) {
                        Appointment app = appoinments.get(i);
                        System.out.println(id);
                        System.out.println(app.getCustID());
                        if (app.getCustID() == id) {
                            appoinments.remove(i);
                        }
                    }

                    JDBC javaDelApp = new JDBC();
                    javaDelApp.makeConnection();
                    Connection connDelApp = javaDelApp.getConnection();
                    Statement deleteAppStmt = null;

                    try {
                        deleteAppStmt = connDelApp.createStatement();
                        deleteAppStmt.executeUpdate("DELETE FROM Appointments WHERE Customer_ID='" + id + "'");
                        tableAppoinments.setItems(appoinments);
                        connDelApp.close();
                    } catch (SQLException throwables) {
                        System.out.println("cant delete appointment");
                    }

                    JDBC javaDB4 = new JDBC();
                    javaDB4.makeConnection();
                    Connection conn4 = javaDB4.getConnection();
                    Statement deleteCustStmt = null;

                    try {
                        deleteCustStmt = conn4.createStatement();
                        deleteCustStmt.executeUpdate("DELETE FROM Customers WHERE Customer_ID='" + id + "'");
                        tableCustomers.getItems().removeAll(tableCustomers.getSelectionModel().getSelectedItem());
                        conn4.close();
                    } catch (SQLException throwables) {
                        System.out.println("cant delete customer");
                    }
                }

            }

        });

        Button addAppButton = createButton("Add Apppointment", 350, 75);
        addAppButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(addAppointmentLayout);
                stage.centerOnScreen();
            }
        });

        Button addAppAddButton = createButton("Add", 200, 250);
        addAppAddButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<Customer> cust = tableCustomers.getSelectionModel().getSelectedItems();
                Customer c = cust.get(0);
                int custID = c.getId();
                String n = c.getName();

                int appID = findMaxApp() + 1;
                String title = addAppTitleText.getText();
                String desc = addAppDescripText.getText();
                String loc = addAppLocationText.getText();
                String type = addAppTypeText.getText();
                Object con = selectContacts.getValue();
                String contact = con.toString();

                LocalDate date = start.getValue();
                DateTimeFormatter formatDateStart = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String startDateCreated = date.format(formatDateStart);
                Object s = time.getValue();
                String ss = s.toString();
                String sTime = getTime(ss);
                String startDateTime = startDateCreated + " " + sTime;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateTime = null;
                try {
                    dateTime = sdf.parse(startDateTime);
                } catch (ParseException e) {
                    System.out.println("cant do  start date");
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String utc = formatter.format(dateTime);

                LocalDate dateTwo = end.getValue();
                DateTimeFormatter formatDateEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String endDateCreated = dateTwo.format(formatDateEnd);
                Object sEnd = timeEnd.getValue();
                String ssEnd = sEnd.toString();
                String sTimeEnd = getTime(ssEnd);
                String endDateTime = endDateCreated + " " + sTimeEnd;

                SimpleDateFormat edf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateTimeEnd = null;
                try {
                    dateTimeEnd = edf.parse(endDateTime);
                } catch (ParseException e) {
                    System.out.println("cant do end date");
                }

                SimpleDateFormat formatterEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatterEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
                String utcEnd = formatterEnd.format(dateTimeEnd);

                LocalDateTime dateNow = LocalDateTime.now();
                DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateCreated = dateNow.format(formatDate);

                String createdBy = userIDNum;
                String lastUpdate = dateCreated;
                String updatedBy = userIDNum;
                int userID = getUserID();
                int contactID = getContactID(contact);

                boolean overlap = isAppOverlapping(dateTime, dateTimeEnd);
                if (overlap){
                    overlapAlert.showAndWait();
                }
                else{
                    JDBC addAppJavaDB = new JDBC();
                    addAppJavaDB.makeConnection();
                    Connection addAppconn = addAppJavaDB.getConnection();

                    try {
                        Statement addAppCustStmt = addAppconn.createStatement();
                        addAppCustStmt.executeUpdate("INSERT INTO appointments VALUES ( '" + appID + "','" + title + "','" + desc + "','" + loc + "','"
                                + type + "','" + utc + "','" + utcEnd + "','" + dateCreated + "','" + createdBy + "','" + lastUpdate + "','" +
                                updatedBy + "','" + custID + "','" + userID + "','" + contactID + "')");

                        Appointment a = new Appointment(appID, title, desc, loc, type, custID, userID);
                        a.setCreateDate(dateCreated);
                        a.setCreatedBy(createdBy);
                        a.setLastupated(lastUpdate);
                        a.setUpdatedBy(updatedBy);
                        a.setContact(contact);
                        a.setStart(startDateTime);
                        a.setEnd(endDateTime);
                        appoinments.add(a);
                        tableAppoinments.setItems(appoinments);

                        addAppconn.close();
                    }
                    catch (SQLException throwables) {
                        System.out.println("cant add app");
                    }

                    addAppTitleText.clear();
                    addAppDescripText.clear();
                    addAppLocationText.clear();
                    addAppTypeText.clear();
                    start.getEditor().clear();
                    time.setValue(null);
                    end.getEditor().clear();
                    timeEnd.setValue(null);
                    selectContacts.setValue(null);

                    stage.setScene(loginIn);
                    stage.centerOnScreen();
                }
            };
        });

        Button addAppCancelButton = createButton("Cancel", 250, 250);
        addAppCancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                addAppTitleText.clear();
                addAppDescripText.clear();
                addAppLocationText.clear();
                addAppTypeText.clear();
                start.getEditor().clear();
                time.setValue(null);
                end.getEditor().clear();
                timeEnd.setValue(null);

                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });

        Button updateAppButton = createButton("Update Appointment", 350, 125);
        updateAppButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<Appointment> app = tableAppoinments.getSelectionModel().getSelectedItems();
                Appointment a = app.get(0);

                String title = a.getTitle();
                updAppTitleText.setText(title);
                String desc = a.getDescription();
                updAppDescText.setText(desc);
                String loc = a.getLocation();
                updAppLocText.setText(loc);
                String type = a.getType();
                updAppTypeText.setText(type);

                String startTime = a.getStart();
                String[] arr = startTime.split(" ");
                String date = arr[0];
                LocalDate startDate = LocalDate.parse(date);
                updStart.setValue(startDate);

                String time = arr[1];
                String sub = time.substring(0,2);
                if (Integer.parseInt(sub) < 12){
                    time = time.substring(0,time.length() - 3);
                    time = time + " AM";
                    updStartTime.setValue(time);
                }
                else{
                    time = time.substring(0,time.length() - 3);
                    time = time + " PM";
                    updStartTime.setValue(time);
                }

                String endTime = a.getEnd();
                String[] arrEnd = endTime.split(" ");
                String dateEnd = arrEnd[0];
                LocalDate endDate = LocalDate.parse(dateEnd);
                updEnd.setValue(endDate);

                String timeEnd = arrEnd[1];
                String subEnd = timeEnd.substring(0,2);
                if (Integer.parseInt(subEnd) < 12){
                    timeEnd = timeEnd.substring(0,timeEnd.length() - 3);
                    timeEnd = timeEnd + " AM";
                    updTimeEnd.setValue(timeEnd);
                }
                else{
                    timeEnd = timeEnd.substring(0,timeEnd.length() - 3);
                    timeEnd = timeEnd + " PM";
                    updTimeEnd.setValue(timeEnd);
                }

                String dateCreated = a.getCreateDate();
                updAppCreatedDateText.setText(dateCreated);
                String createdBy = a.getCreatedBy();
                updAppCreatedByText.setText(createdBy);
                String lastUpdate = a.getLastupated();
                updAppLastUpdateText.setText(lastUpdate);
                String updatedBy = a.getUpdatedBy();
                updAppUpdatedByText.setText(updatedBy);
                String cusID = String.valueOf(a.getCustID());
                updAppCustIDText.setText(cusID);
                String userID = String.valueOf(a.getUserID());
                updAppUserIDText.setText(userID);
                String contactID = String.valueOf(a.getContactID());
                updAppContactIDText.setText(contactID);

                stage.setScene(updateLayoutApp);
                stage.centerOnScreen();
            }
        });

        Button updAppUpdButton = createButton("Update", 190, 250);
        updAppUpdButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ObservableList<Appointment> apps = tableAppoinments.getSelectionModel().getSelectedItems();
                Appointment a = apps.get(0);

                a.setTitle(updAppTitleText.getText());
                a.setDescription(updAppDescText.getText());
                a.setLocation(updAppLocText.getText());
                a.setType(updAppTypeText.getText());

                LocalDate date = updStart.getValue();
                DateTimeFormatter formatDateStart = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String startDateCreated = date.format(formatDateStart);
                Object s = updStartTime.getValue();
                String ss = s.toString();
                String sTime = getTime(ss);
                a.setStart(startDateCreated + " " + sTime);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateTime = null;
                try {
                    //dateTime = sdf.parse(startDateCreated + " " + sTime);
                    dateTime = sdf.parse(a.getStart());
                } catch (ParseException e) {
                    System.out.println("cant do  start date");
                }

                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String utc = sdf.format(dateTime);

                LocalDate dateTwo = updEnd.getValue();
                DateTimeFormatter formatDateEnd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String endDateCreated = dateTwo.format(formatDateEnd);
                Object sEnd = updTimeEnd.getValue();
                String ssEnd = sEnd.toString();
                String sTimeEnd = getTime(ssEnd);
                a.setEnd(endDateCreated + " " + sTimeEnd);

                SimpleDateFormat edf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateTimeEnd = null;
                try {
                    dateTimeEnd = edf.parse(endDateCreated + " " + sTimeEnd);
                } catch (ParseException e) {
                    System.out.println("cant do end date");
                }

                SimpleDateFormat formatterEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatterEnd.setTimeZone(TimeZone.getTimeZone("UTC"));
                String utcEnd = formatterEnd.format(dateTimeEnd);

                a.setCreateDate(updAppCreatedDateText.getText());
                a.setCreatedBy(updAppCreatedByText.getText());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timestamp = dateFormat.format(new java.util.Date());
                a.setLastupated(timestamp);

                a.setLastupated(updAppUpdatedByText.getText());
                a.setCustID(Integer.parseInt(updAppCustIDText.getText()));
                a.setUserID(Integer.parseInt(updAppUserIDText.getText()));
                a.setContactID(2);

                JDBC javaUpdApp = new JDBC();
                javaUpdApp.makeConnection();
                Connection connUpdApp = javaUpdApp.getConnection();
                Statement updateAppStmt = null;
                try {
                    updateAppStmt = connUpdApp.createStatement();
                    updateAppStmt.executeUpdate("UPDATE appointments SET Title='" + updAppTitleText.getText() +  "', Description='" + updAppDescText.getText()
                            + "', Location='" + updAppLocText.getText() + "', Type='" + updAppTypeText.getText() + "', Start='" + utc +
                            "', End='" + utcEnd + "', Create_Date='" + updAppCreatedDateText.getText() + "', Created_By='" + updAppCreatedByText.getText() +
                            "', Last_Update='" + timestamp + "', Last_Updated_By='" + updAppUpdatedByText.getText() +
                            "', Customer_ID='" + updAppCustIDText.getText() + "', User_ID='" + updAppUserIDText.getText() +
                            "', Contact_ID='" + updAppContactIDText.getText() + "' WHERE Appointment_ID='" + a.getAppID() + "'");

                    for (int i = 0; i < appoinments.size(); i++){
                        Appointment app = appoinments.get(i);
                        if (app.getAppID() == a.getAppID()){
                            appoinments.set(i, a);
                        }
                    }

                    connUpdApp.close();

                } catch (SQLException throwables) {
                    System.out.println("cant update appointment");
                }

                updAppTitleText.clear();
                updAppDescText.clear();
                updAppLocText.clear();
                updAppTypeText.clear();
                updStart.getEditor().clear();
                updStartTime.setValue(null);
                updEnd.getEditor().clear();
                updTimeEnd.setValue(null);
                updAppCreatedDateText.clear();
                updAppCreatedByText.clear();
                updAppLastUpdateText.clear();
                updAppUpdatedByText.clear();
                updAppCustIDText.clear();
                updAppUserIDText.clear();
                updAppContactIDText.clear();

                stage.setScene(loginIn);
                stage.centerOnScreen();

            }
        });

        Button updAppCancelButton = createButton("Cancel", 250, 250);
        updAppCancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                updAppTitleText.clear();
                updAppDescText.clear();
                updAppLocText.clear();
                updAppTypeText.clear();
                updStart.getEditor().clear();
                updStartTime.setValue(null);
                updEnd.getEditor().clear();
                updTimeEnd.setValue(null);
                updAppCreatedDateText.clear();
                updAppCreatedByText.clear();
                updAppLastUpdateText.clear();
                updAppUpdatedByText.clear();
                updAppCustIDText.clear();
                updAppUserIDText.clear();
                updAppContactIDText.clear();

                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });

        Button deleteAppButton = createButton("Delete Appointment", 350, 175);
        deleteAppButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                ObservableList<Appointment> apps = tableAppoinments.getSelectionModel().getSelectedItems();
                Appointment a = apps.get(0);
                int appID = a.getAppID();

                JDBC delApp = new JDBC();
                delApp.makeConnection();
                Connection delAppConn = delApp.getConnection();
                Statement deleteApp = null;
                try {
                    deleteApp = delAppConn.createStatement();
                    deleteApp.executeUpdate("DELETE FROM appointments WHERE Appointment_ID='" + appID + "'");
                    tableAppoinments.getItems().removeAll(tableAppoinments.getSelectionModel().getSelectedItem());

                    delAppConn.close();
                }
                catch (SQLException throwables) {
                    System.out.println("cant delete appointment");
                }

                Alert delAppAlert = new Alert(Alert.AlertType.INFORMATION);
                delAppAlert.setTitle("Important");
                delAppAlert.setContentText("Appointment " + appID + " - " + a.getType() + " is now deleted");
                delAppAlert.setHeaderText(null);

                delAppAlert.showAndWait();
            }
        });

        Button logOutButton = createButton("Log Out", 500, 250);
        logOutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(scene);
                stage.centerOnScreen();
                es.shutdown();
            }
        });

        Button reportsButton = createButton("Reports", 400, 250);
        reportsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(reportsLayout);
                stage.centerOnScreen();
            }
        });

        Button mainMenuButton = createButton("Main Menu", 550, 325);
        mainMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setScene(loginIn);
                stage.centerOnScreen();
            }
        });


        if (lang == f){
            wrongLogin.setTitle("Erreur");
            wrongLogin.setContentText("Mauvais nom d'utilisateur ou mot de passe");
            wrongLogin.setHeaderText(null);
            signIn.setText("S'identifier");
            userName.setText("Nom d'utilisateur");
            pass.setText("Mot de passe");
            signButton.setText("S'identifier");

        }

        Loginlayout.getChildren().addAll(userName, pass, userText, passText, signIn, signButton, theZone);
        stage.setScene(scene);
        stage.show();

        insideLogin.getChildren().addAll(tableCustomers, addCustButton, updateCustButton, deleteCustButton, tableAppoinments, cust, apps, addAppButton,
                updateAppButton, deleteAppButton, logOutButton, allAppsRB, monthAppRB, weekAppRB, reportsButton);

        addCustLayout.getChildren().addAll(addCust, addCustName, addCustAddress, addCustZip, addCustPhone, addNameText, addAddressText,
                addZipText, addPhoneText, addCustAddButton, addCustCancelButton, countriesCombo, country, division, divisionCombo);

        updateCustLayout.getChildren().addAll(updateCust, updateCustName, updateCustAddress, updateCustZip, updateCustPhone, upNameText, upAddressText,
                upZipText, upPhoneText, updateCustupButton, updateCustCancelButton, updCountry, updDivision, updCountriesCombo, updDivisionCombo);

        addAppLayout.getChildren().addAll(addAppTitle, addAppdescrip, addAppLocation, addAppType, addAppStart, addAppEnd, addApp, addAppTitleText,
                addAppDescripText, addAppLocationText, addAppTypeText, start, time, end, timeEnd, addAppAddButton, addAppCancelButton, selectContacts,
                addAppCon);

        updateAppLayout.getChildren().addAll(updateApp, updAppContactId, updAppCustId, updAppTitle, updAppDesc, updAppCreatedBy, updAppLoc,
                updAppUserId, updAppType, updAppEnd, updAppStart, updAppDateCreated, updAppDateUpdated, updAppUpdatedBy, updAppContactIDText,
                updAppTitleText, updAppCustIDText, updAppDescText, updAppCreatedByText, updAppLocText, updAppCreatedDateText, updAppTypeText, updAppLastUpdateText,
                updStart, updStartTime, updEnd, updTimeEnd, updAppUpdatedByText, updAppUserIDText, updAppUpdButton, updAppCancelButton);

        reports.getChildren().addAll(reportsTable, mainMenuButton, appReports, contact1, contact2, contact3, anika, daniel, li, custApp, earlyAppLabel);
    }


    public static void main(String[] args) {
        launch();
    }

    public static int getUserID() {
            if ("test".equals(userIDNum)) {
                return 1;
            }
        return 2;
    }

    public static boolean getIn(ArrayList<String[]> list, String user, String pass) {
        for (int i = 0; i < list.size(); i++) {
            String[] logs = list.get(i);
            String zero = logs[0];
            String one = logs[1];
            if (zero.equals(user) && one.equals(pass)) {
                return true;
            }
        }
        return false;
    }

    public static int findMaxCust(){
        int max = 0;
        for (int i = 0; i < customerData.size(); i++){
            Customer c = customerData.get(i);
            int x = c.getId();
            if (x > max) {
                max = x;
            }
        }
        return max;
    }

    public static int findMaxApp(){
        int max = 0;
        for (int i = 0; i < appoinments.size(); i++){
            Appointment a = appoinments.get(i);
            int x = a.getAppID();
            if (x > max) {
                max = x;
            }
        }
        return max;
    }

    public static String getTime(String s){
        if (s.contains("12")){
            String pm = s.replace(" PM", ":00");
            return pm;
        }
        if (s.contains("PM")) {
            String hour = String.valueOf(s.charAt(0));
            int military = Integer.valueOf(hour) + 12;
            String convert = String.valueOf(military);
            String newTime = s.substring(1, s.length() - 3);
            String newS = convert + newTime + ":00";
            return newS;
        }
        else {
            String am = s.replace(" AM", ":00");
            String sub = s.substring(0,2);
            if (sub.contains(":")){
                am = "0" + am;
            }
            return am;
        }
    }

    public static Label createLabel (String l, int x, int y){
        Label label = new Label();
        label.setText(l);
        label.setTranslateX(x);
        label.setTranslateY(y);
        return label;
    }

    public static TextField createTextfield (int x, int y, int w){
        TextField text = new TextField();
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.setMaxWidth(w);
        return text;
    }

    public static Button createButton (String s, int x, int y){
        Button b = new Button();
        b.setText(s);
        b.setTranslateX(x);
        b.setTranslateY(y);
        return b;
    }

    public static String getDivisionName(int d){
        for (int i = 0; i < divList.size(); i++) {
            String[] div = divList.get(i);
            if (div[0].equals(String.valueOf(d))){
                return div[1];

            }
        }
        return "";
    }

    public static int getDivisionID(String s){
        for (int i = 0; i < div.size(); i++) {
            String[] arr = div.get(i);
            if (arr[1].equals(s)){
                return Integer.parseInt(arr[0]);
            }
        }
        return 0;
    }

    public static String getCountryID(int d){
        for (int i = 0; i < divList.size(); i++) {
            String[] div = divList.get(i);
            if (div[0].equals(String.valueOf(d))){
                if (div[2].equals("1")){
                    return "United States";
                }
                if (div[2].equals("2")){
                    return "United Kingdom";
                }
            }
        }
        return "Canada";
    }

    public static int getContactID(String contact){
        if (contact.equals("Anika Costa")){
            return 1;
        }
        else if (contact.equals("Daniel Garcia")){
            return 2;
        }
        else if (contact == "Li Lee"){
            return  3;
        }
        return 0;
    }

    public static String getContactName(int ID){
        if (ID == 1){
            return "Anika Costa";
        }
        else if (ID == 2){
            return "Daniel Garcia";
        }
        if (ID == 3){
            return "Li Lee";
        }
        return null;
    }


    public static RadioButton createdRadioButton (String s, int x, int y){
        RadioButton rb = new RadioButton();
        rb.setText(s);
        rb.setTranslateX(x);
        rb.setTranslateY(y);
        return rb;
    }

    public static void dayOfWeek (String currentMonth, int value, ObservableList<Appointment> appWeekViews){
        for (int i = 0; i < appoinments.size(); i++) {
            Appointment a = appoinments.get(i);
            String appSt = a.getStart();
            appSt = appSt.substring(0, 10);
            LocalDate stDate = LocalDate.parse(appSt);
            DayOfWeek day = stDate.getDayOfWeek();
            String d = day.toString();
            int stWeek = stDate.getDayOfMonth();
            String month = stDate.getMonth().toString();

            if (currentMonth.equals(month) && d.equals("MONDAY")){
                for (int j = 0; j < 5; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("TUESDAY")){
                for (int j = -1; j < 4; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("WEDNESDAY")){
                for (int j = -2; j < 3; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("THURSDAY")){
                for (int j = -3; j < 2; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("FRIDAY")){
                for (int j = -4; j < 1; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("SATURDAY")){
                for (int j = - 5; j < 1; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
            else if (currentMonth.equals(month) && d.equals("SUNDAY")){
                for (int j = 0; j < 6; j++){
                    if (value + j == stWeek){
                        appWeekViews.add(a);
                    }
                }
            }
        }
    }

    public static boolean isWeekend (LocalDate d){
        DayOfWeek day = d.getDayOfWeek();
        String date = day.toString();
        if (date.equals("SATURDAY") || date.equals("SUNDAY")){
            return true;
        }
        return false;
    }

    public static boolean isAppOverlapping (Date start, Date end){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < appoinments.size(); i++){
            Appointment a = appoinments.get(i);
            String startTime = a.getStart();
            String endTime = a.getEnd();
            Date dateStart = null;
            Date dateEnd = null;
            try {
                dateStart = sdf.parse(startTime);
                dateEnd = sdf.parse(endTime);
            }
            catch (ParseException e) {
                System.out.println("cant");
            }
            if (start.equals(dateStart)){
                return true;
            }
            if (start.after(dateStart) && start.before(dateEnd)){
                return true;
            }
            if (end.after(dateStart) && end.before(dateEnd)){
                return true;
            }
        }
        return false;
    }

    public static Appointment appSoonLogin(){
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime addMin = LocalDateTime.now().plusMinutes(15);
        String currentDateTime = current.format(formatt2);
        String minAdded = addMin.format(formatt2);

        for (int i = 0; i < appoinments.size(); i++){
            Appointment a = appoinments.get(i);
            String startApp = a.getStart();
            Date appStart = null;
            Date now = null;
            Date added = null;
            try {
                appStart = formatt.parse(startApp);
                now = formatt.parse(currentDateTime);
                added = formatt.parse(minAdded);
            }
            catch (ParseException e) {
                System.out.println("problem");
            }

            if (appStart.after(now) && appStart.before(added)){
                return a;
            }
            if (appStart.equals(now)){
                return a;
            }
        }
        return null;
    }

    public static Appointment appIn15(){
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatt2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime addMin = LocalDateTime.now().plusMinutes(15);
        String minAdded = addMin.format(formatt2);

        for (int i = 0; i < appoinments.size(); i++) {
            Appointment a = appoinments.get(i);
            String startApp = a.getStart();
            Date appStart = null;
            Date added = null;
            try {
                appStart = formatt.parse(startApp);
                added = formatt.parse(minAdded);
            } catch (ParseException e) {
                System.out.println("problem");
            }

            if (added.equals(appStart)) {
                return a;
            }
        }
        return null;
    }

    public static String estTtime (String utc){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(utc, formatter);
        dateTime = dateTime.minusHours(4);
        String est = dateTime.format(formatter);
        return est;
    }

    public static void diffTypes (String[] month, String[] type){
        for (int i = 0; i < month.length; i++){
            int count = 1;
            for (int j = i + 1; j < month.length; j++){
                if (month[i].equals("")) {
                    break;
                }
                if (month[i].equals(month[j]) && type[i].equals(type[j])) {
                        count = count + 1;
                        month[j] = "";
                }
            }
            if (month[i] != ""){
                Report r = new Report(month[i], type[i], count);
                reportList.add(r);
            }
        }
    }

    public static ArrayList<String[]> monthsInOrder (String[] month, String[] type){
        ArrayList<String[]> together = new ArrayList<String[]>();
        ArrayList<String[]> order = new ArrayList<String[]>();
        for (int i = 0; i < month.length; i++){
            String[] arr = new String[2];
            arr[0] = month[i];
            arr[1] = type[i];
            together.add(arr);
        }

        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("JANUARY")){
                order.add(list);
            }
        }

        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("FEBRUARY")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("MARCH")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("APRIL")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("MAY")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("JUNE")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("JULY")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("AUGUST")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("SEPTEMBER")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("OCTOBER")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("NOVEMBER")){
                order.add(list);
            }
        }
        for (int i = 0; i < together.size(); i++){
            String[] list = together.get(i);
            if (list[0].equals("DECEMBER")){
                order.add(list);
            }
        }
        return order;
    }

    public static void getContact (ObservableList o, String s){
        appoinments.forEach( (app) -> {
            Appointment a = app;
            if (a.getContact().equals(s)){
                Contact c = new Contact(a.getAppID(), a.getTitle(), a.getType(), a.getDescription(), a.getStart(), a.getEnd(), a.getCustID());
                o.add(c);
            }
        });
    }

    public static ArrayList<String> getDivisionList(String s){
        ArrayList<String> divs = new ArrayList<String>();
        if (s.equals("united States")){
            for (int i = 0; i < div.size(); i++){
                String[] arr = div.get(i);
                if (arr[2].equals("1")){
                    divs.add(arr[1]);
                }
            }
        }
        if (s.equals("united Kingdom")){
            for (int i = 0; i < div.size(); i++){
                String[] arr = div.get(i);
                if (arr[2].equals("2")){
                    divs.add(arr[1]);
                }
            }
        }
        if (s.equals("Canada")){
            for (int i = 0; i < div.size(); i++){
                String[] arr = div.get(i);
                if (arr[2].equals("3")){
                    divs.add(arr[1]);
                }
            }
        }
        return divs;
    }

    /**
     * method that obtains the earliest appointment for each customer
     */
    public static void getEarlyApps(){
        int[] num = new int[appoinments.size()];
        String[] time = new String[appoinments.size()];
        for (int i =0; i < appoinments.size(); i++){
            Appointment a = appoinments.get(i);
            num[i] = a.getCustID();
            time[i] = a.getStart();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int id2 = 0;
        for (int i = 0; i < appoinments.size(); i++){
            int id = num[i];
            String app = time[i];
            boolean isSame = false;
            String earlyApp = app;
            LocalDate one = LocalDate.parse(app, formatter);
            for (int j = i + 1; j < appoinments.size(); j++){
                id2 = num[j];
                String app2 = time[j];
                LocalDate two = LocalDate.parse(app2, formatter);
                if (id == id2 && id2 != 0){
                    isSame = true;
                    if (one.isBefore(two)){
                        earlyApp = app;
                        num[j] = 0;
                    }
                    else {
                        earlyApp = app2;
                        num[j] = 0;
                    }
                }
            }
            if (isSame == false && id != 0){
                CustApp c = new CustApp(id, earlyApp);
                custNApp.add(c);
            }
            else if (id != 0){
                CustApp c = new CustApp(id, earlyApp);
                custNApp.add(c);
            }
        }
    }

    /**
     * method that returns an Arraylist of names from the contact list
     */
    public static ArrayList<String> getContactNames(){
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < con.size(); i++){
            String[] contact = con.get(i);
            String name = contact[1];
            names.add(name);
        }
        return names;
    }

    /**
     * method that creates a datepicker
     */
    public static DatePicker createDatePicker(int x, int y, int w){
        DatePicker  d= new DatePicker();
        d.setTranslateX(x);
        d.setTranslateY(y);
        d.setMaxWidth(w);
        return d;
    }

    /**
     * method that returns an Arraylist of the time for the time combobox in the appointments
     */
    public static ArrayList<String> createTime(){
        ArrayList<String> list= new ArrayList<String>();
        String[] minutes = {"00", "15", "30", "45"};
        for (int i = 8; i < 12; i ++){
            for (int j = 0; j < minutes.length; j++){
                String s = i + ":" + minutes[j] + " AM";
                list.add(s);
            }
        }
        list.add("12:00 PM");
        list.add("12:15 PM");
        list.add("12:30 PM");
        list.add("12:45 PM");
        for (int i = 1; i < 10; i ++){
            for (int j = 0; j < minutes.length; j++){
                String s = i + ":" + minutes[j] + " PM";
                list.add(s);
            }
        }
        list.add("10:00 PM");
        return list;
    }

}


