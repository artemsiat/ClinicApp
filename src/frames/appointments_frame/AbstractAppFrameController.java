package frames.appointments_frame;

import data_base.Appointments;
import data_base.DataBase;
import data_base.Doctors;
import instances.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import programm.FrameColor;
import programm.Programm;
import programm.helper_classes.FormatDate;
import programm.texts.DatePickerText;
import programm.texts.FrameAppointmentText;
import programm.texts.FrameButtonText;
import programm.texts.FrameLabelText;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Artem Siatchinov on 8/15/2016.
 */
public class AbstractAppFrameController {

    private Programm programm;
    private DataBase dataBase;
    private Appointments appointments;
    private Doctors doctors;

    private AppointmentFrame appointmentFrame;

    private Patient selectedPatient;
    private ObservableList<Appointment> patientAppointmentsList;
    private ObservableList<Appointment> doctorAppointmentsList;

    private Doctor selectedDoctor;
    private WorkingDay selectedWorkingDay;
    private Integer selectedAppHour;
    private Integer selectedAppMinutes;
    private Integer selectedAppLength;

    //Patient Appointments Table
    @FXML private TableView<Appointment> patientAppsTable;

    @FXML private TableColumn<Appointment, String> doctorAppTableColl;
    @FXML private TableColumn<Appointment, String> patientAppTableColl;
    @FXML private TableColumn<Appointment, String> dayAppTableColl;
    @FXML private TableColumn<Appointment, String> timeAppTableColl;
    @FXML private TableColumn<Appointment, String> lengthAppTableColl;

    //Patient
    @FXML private Label patientLabelInfo;

    //Doctor
    @FXML private ComboBox<String> chooseDoctorComboBox;
    @FXML private Label doctorLabelInfo;

    //Appointment
    @FXML private DatePicker appDatePicker;
    @FXML private Label datePickerLabel;
    @FXML private Label datePickerLabelInfo;

    @FXML private ComboBox<Integer> appHoursComboBox;
    @FXML private ComboBox<Integer> appMinutesComboBox;
    @FXML private Label startTimeLabel;
    @FXML private Label startTimeLabelInfo;

    @FXML private Label appLengthLabel;
    @FXML private Label appLengthLabelInfo;


    //Buttons
    @FXML private Button createAppBtn;
    @FXML private Button removeAppBtn;
    @FXML private Button updateAppBtn;
    @FXML private Button exitFrameBtn;

    @FXML private Button choosePatientBtn;

    //Create Drop Table Labels and buttons

    @FXML private Label tableCreatedLabel;
    @FXML private Label tableCreatedInfo;
    @FXML private Button createTableButton;
    @FXML private Button dropTableButton;
    @FXML private Button checkTableButton;

    public AbstractAppFrameController(Programm programm, AppointmentFrame appointmentFrame) {

        this.programm = programm;
        this.appointmentFrame = appointmentFrame;

        this.dataBase = programm.getDATA_BASE();
        this.appointments = programm.getDATA_BASE().getAppointments();
        this.doctors = dataBase.getDoctors();

        patientAppointmentsList = null;
        doctorAppointmentsList = null;
    }

    //Initialization
    protected void initializeAppController(){
        patientSelected(programm.getSelectedPatient());
        setCreateDropTableButtonsLabels();

        setWorkingDayPickerListener();
        setHoursComboBoxListener();

        initAppLabels();

        setDoctorComboBox();
        selectedDoctor = null;
        selectedAppHour = null;
        selectedAppMinutes = null;
        doctorSelected();

        initPatientAppsTable();
    }

    //Tables
    private void initPatientAppsTable() {
        doctorAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorProperty"));
        patientAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patientProperty"));
        dayAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("dayProperty"));
        timeAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("timeProperty"));
        lengthAppTableColl.setCellValueFactory(new PropertyValueFactory<Appointment, String>("lengthPropety"));

        patientAppsTable.setItems(patientAppointmentsList);
    }


    //Doctor
    private void setDoctorComboBox() {

        chooseDoctorComboBox.setPromptText(FrameAppointmentText.getChooseDoctorText());
        chooseDoctorComboBox.getItems().add(" ");

        ObservableList<Person> doctorsList = doctors.getDoctors();

        for (Person person : doctorsList){
            chooseDoctorComboBox.getItems().add(person.getFULL_NAME_PROPERTY());
        }


        //create combo Box listener
        chooseDoctorComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null){
                    return;
                }
                //No Doctor Selected
                if (newValue.equals(" ")){
                    selectedDoctor = null;
                    doctorSelected();
                    return;
                }

                //Doctor Selected
                ObservableList<Person> doctorsList =  doctors.getDoctors();
                for (Person person : doctorsList){

                    //if the doctor match the combo box choice
                    if (chooseDoctorComboBox.getValue().equals(person.getFULL_NAME_PROPERTY())){

                        selectedDoctor = (Doctor)person;
                        doctorSelected();
                    }
                }
            }
        });
    }

    private void doctorSelected(){

        System.out.println("Doctor selected: " + selectedDoctor);

        if (selectedDoctor ==null){
            doctorLabelInfo.setTextFill(FrameColor.getColorError());
            doctorLabelInfo.setText(FrameAppointmentText.getNoDoctorChosenText());

            //Date picker info
            datePickerLabelInfo.setTextFill(FrameColor.getColorError());
            datePickerLabelInfo.setText(DatePickerText.getNoDoctorPicked());

            //Make sure the date picker is disabled
            appDatePicker.setValue(null);
            appDatePicker.setDisable(true);

            return;
        }

        doctorLabelInfo.setTextFill(FrameColor.getColorSucess());
        doctorLabelInfo.setText(selectedDoctor.getFULL_NAME_PROPERTY() + ". Тел: " + selectedDoctor.getPHONE());

        //create date picker factory and enable date picker

        appDatePicker.setDisable(false);
        appDatePicker.setValue(null);
        setWorkingDayPickerFactory();
        setWorkingDayPickerInfo();
    }

    //Working Hours

    /**
     * Initializes Hours ComboBox
     * Called when working day selected
     */
    private void setHoursComboBox() {

        appHoursComboBox.setDisable(false);
        appHoursComboBox.setPromptText(FrameAppointmentText.getHoursComboBoxText());

        //Obtain list

        appHoursComboBox.getItems().add(new Integer(9));

    }

    private void appHoursSelected(){
        System.out.println("Hour selected: " + selectedAppHour);
        if (selectedAppHour == null){

        }
    }

    private void setHoursComboBoxListener() {

        appHoursComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue == null){
                    selectedAppHour = null;
                    appHoursSelected();
                    return;
                }
                selectedAppHour = newValue;
                appHoursSelected();
            }
        });
    }

    //Working Day

    /**
     * 1.Doctor was deselected
     * 2.New Working day Selected is null
     * 3.New Working day is not null - proceed
     */
    private void workingDaySelected(){

        System.out.println("Working day selected: " + selectedWorkingDay);

/*        if (selectedDoctor == null){
            return;
        }*/


        if (selectedWorkingDay == null){
            setWorkingDayPickerInfo();
            //Check that hours combo box is deselected
            appHoursComboBox.setValue(null);
            appHoursComboBox.getItems().clear();
            return;
        }


        if (selectedWorkingDay != null){
            //load list of appointments

            setWorkingDayPickerInfo();
            setHoursComboBox();

            //load appointments for that day
            if (dataBase.getAppointments().loadWorkingDayAppointments(selectedWorkingDay)){

            }

            return;
        }


    }


    private void setWorkingDayPickerInfo() {

        LocalDate datePickerValue = appDatePicker.getValue();

        if (datePickerValue == null){
            selectedWorkingDay = null;
            datePickerLabelInfo.setTextFill(FrameColor.getColorError());
            datePickerLabelInfo.setText(DatePickerText.getNoDatePicked());
            return;
        }

        String formatedDate = FormatDate.convertDateToMyFormat(datePickerValue);
        selectedWorkingDay = selectedDoctor.isWorkingDay(datePickerValue);

        if (selectedWorkingDay == null){
            datePickerLabelInfo.setTextFill(FrameColor.getNotWorkingDayColor());
            datePickerLabelInfo.setText(formatedDate + (" Не рабочий день."));

            return;
        }
        else {
            datePickerLabelInfo.setText(formatedDate + (" Рабочий день."));
            datePickerLabelInfo.setTextFill(FrameColor.getIsWorkingDayColor());

            System.out.println("Start time : " + selectedWorkingDay.getStartTime() + " End time : " + selectedWorkingDay.getEndTime());


            //Todo populate appointments table

            //TODO set up hours combo box
        }
    }

    private void setWorkingDayPickerListener(){

        appDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if (selectedDoctor == null){
                    selectedWorkingDay = null;
                    workingDaySelected();
                    return;
                }

                selectedWorkingDay = selectedDoctor.isWorkingDay(newValue);
                workingDaySelected();

            }
        });

    }

    private void setWorkingDayPickerFactory(){

        /**
         * If the working day list is null then return
         * Cells with working days green
         * non working blue
         * passed working light green
         * passed non working light blue and disabled
         *
         */

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override public DateCell call(DatePicker param) {

                return new DateCell(){

                    @Override public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);

                        if (selectedDoctor == null){
                            return;
                        }

                        LocalDate today = LocalDate.now();


                        //Working day
                        if (selectedDoctor.isWorkingDay(item) != null){
                            setTooltip(new Tooltip("Рабочий день"));
                            //if date is before today
                            if (item.isBefore(today)){
                                setStyle("-fx-background-color: " + FrameColor.getHexPastWorking() + ";");
                                setDisable(true);
                            }
                            else{
                                setStyle("-fx-background-color: " + FrameColor.getHexFutureWorking() + ";");
                            }

                        }

                        //Non Working day
                        else {
                            setTooltip(new Tooltip("Не рабочий день"));
                            //if date is before today
                            if (item.isBefore(today)) {
                                setStyle("-fx-background-color: " + FrameColor.getHexPastNonWorking() + ";");
                                setDisable(true);
                            } else {
                                setStyle("-fx-background-color: " + FrameColor.getHexFutureNonWorking() + ";");
                                setDisable(true);
                            }
                        }
                    }
                };
            }
        };

        appDatePicker.setDayCellFactory(dayCellFactory);

    }

    //Buttons
    @FXML void createAppBtnAction(ActionEvent event) {
        appointments.addObject(createAppointment());
    }



    @FXML void removeAppBtnAction(ActionEvent event) {

    }

    @FXML void updateAppBtnAction(ActionEvent event) {

    }

    @FXML void exitFrameBtnAction(ActionEvent event) {

    }

    //Patient

    public void patientSelected(Patient patient) {

        this.selectedPatient = patient;

        if (selectedPatient == null){
            patientLabelInfo.setTextFill(FrameColor.getColorError());
            patientLabelInfo.setText(FrameAppointmentText.getNoPatientChosenText());

            patientAppointmentsList = null;
            return;
        }
        patientLabelInfo.setTextFill(FrameColor.getColorSucess());
        patientLabelInfo.setText(selectedPatient.getFULL_NAME_PROPERTY());

        //Get Appointments list
        patientAppointmentsList = selectedPatient.getAppointmentsList();
        patientAppsTable.setItems(patientAppointmentsList);
    }


    //Helper
    private DataBaseInstance createAppointment() {

        if (selectedDoctor == null || selectedPatient == null || selectedWorkingDay == null){
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setCreator(programm.getCurrentAdmin().getID());
        appointment.setDoctorId(selectedDoctor.getID());
        appointment.setWokringDayId(selectedWorkingDay.getID());
        appointment.setPatientId(selectedPatient.getID());
        appointment.setDate(selectedWorkingDay.getDate());
        appointment.setStartTime(900);
        appointment.setEndTime(1015);
        appointment.setwhencreated(LocalDateTime.now());

        return appointment;
    }

    //Initialization
    protected void intiButtons() {

        createAppBtn.setText(FrameButtonText.getCreateButtonText());
        removeAppBtn.setText(FrameButtonText.getRemoveButtonText());
        updateAppBtn.setText(FrameButtonText.getUpdateButtonText());
        exitFrameBtn.setText(FrameButtonText.getExitFrameButtonText());

        choosePatientBtn.setText(FrameAppointmentText.getChoosePatientText());
    }
    private void initAppLabels(){
        datePickerLabel.setText(FrameAppointmentText.getAppDatePickerLabel());
        startTimeLabel.setText(FrameAppointmentText.getAppStartTimeLabel());
        appLengthLabel.setText(FrameAppointmentText.getAppLengthLabel());

        startTimeLabelInfo.setText("");
        appLengthLabelInfo.setText("");

    }


    //Buttons Table

    @FXML void createTableBtnAction(ActionEvent event) {

        boolean status = appointments.createTable();
        //LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(appointments.isTableCreated());
        //displayLog();

    }

    @FXML void dropTableBtnAction(ActionEvent event) {
        boolean status = appointments.dropTable();
        //LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(appointments.isTableCreated());
        //displayLog();
    }

    @FXML void checkTableBtnAction(ActionEvent event) {
        boolean status = appointments.checkTable();
        //LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(appointments.isTableCreated());
        //displayLog();
    }
    /**
     * Sets the table is created status label
     *
     */
    private void setTableCreatedStatus(boolean status) {
        if (status){
            tableCreatedInfo.setText(FrameLabelText.getTableCreatedStatusPos());
            tableCreatedInfo.setTextFill(FrameColor.getColorSucess());
        }
        else {
            tableCreatedInfo.setText(FrameLabelText.getTableCreatedStatusNeg());
            tableCreatedInfo.setTextFill(FrameColor.getColorError());
        }
    }
    /**
     * Sets the label and buttons text for the following:
     *
     * Create table button
     * Drop table button
     * Check if table is created button
     *
     * Label (is table created)
     */
    private void setCreateDropTableButtonsLabels(){
        createTableButton.setText(FrameButtonText.getTableCreateButton());
        dropTableButton.setText(FrameButtonText.getTableDropButton());
        checkTableButton.setText(FrameButtonText.getTableUpdateButton());

        tableCreatedLabel.setText(FrameLabelText.getTableStatusLabel());

        setTableCreatedStatus(appointments.isTableCreated());

    }
}
