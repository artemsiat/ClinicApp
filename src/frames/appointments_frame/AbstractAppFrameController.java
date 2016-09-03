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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Artem Siatchinov on 8/15/2016.
 */
public class AbstractAppFrameController extends DoctorAppointmentTable{

    private Programm programm;
    private DataBase dataBase;
    private Appointments appointments;
    private Doctors doctors;

    private AppointmentFrame appointmentFrame;

    private Patient selectedPatient;


    private Doctor selectedDoctor;
    private WorkingDay selectedWorkingDay;

    HashMap<Integer, ArrayList<Integer>> hoursAndMinutes;
    private Integer selectedAppHour;
    private Integer selectedAppMinutes;
    private Integer selectedAppLength;

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
    @FXML private ComboBox<Integer> appLengthComboBox;
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

    }

    //Initialization
    protected void initializeAppController(){
        initializePatientAppController();
        initializeDoctorAppController();
        initButtons();

        patientSelected(programm.getSelectedPatient());
        setCreateDropTableButtonsLabels();

        setWorkingDayPickerListener();
        setHoursComboBoxListener();
        setMinutesComboBoxListener();
        setLengthComboBoxListener();

        initAppLabels();

        setDoctorComboBox();

        this.hoursAndMinutes = null;
        this.selectedDoctor = null;
        this.selectedAppHour = null;
        this.selectedAppMinutes = null;

        doctorSelected();
    }


    /*Selectors*/
    public void patientSelected(Patient patient) {

        this.selectedPatient = patient;

        if (selectedPatient == null){
            patientLabelInfo.setTextFill(FrameColor.getColorError());
            patientLabelInfo.setText(FrameAppointmentText.getNoPatientChosenText());

            patientAppsTable.setItems(null);
            return;
        }
        patientLabelInfo.setTextFill(FrameColor.getColorSucess());
        patientLabelInfo.setText(selectedPatient.getFULL_NAME_PROPERTY());

        //Get Appointments list

        patientAppsTable.setItems(selectedPatient.getAppointmentsList());
    }
    /**
     * 1.Doctor was deselected
     * 2.New Working day Selected is null
     * 3.New Working day is not null - proceed
     */
    private void workingDaySelected(){


        if (selectedWorkingDay == null){


            setWorkingDayPickerInfo();
            //Check that hours combo box is deselected
            appHoursComboBox.setValue(null);
            appHoursComboBox.getItems().clear();

            doctorAppsTable.setItems(null);
            return;
        }


        if (selectedWorkingDay != null){
            //load list of appointments

            setWorkingDayPickerInfo();

            setHoursComboBox();

            //load appointments for that day
            if (dataBase.getAppointments().loadWorkingDayAppointments(selectedWorkingDay)){
                doctorAppsTable.setItems(selectedWorkingDay.getAppointmentObservableList());
            }

            return;
        }
    }

    private void appHoursSelected(){
        //Todo
        //When hours selected
        if (selectedAppHour == null){
            setMinutesComboBox();
        }
        if (selectedAppHour != null){
            setMinutesComboBox();
        }
    }
    private void appMinutesSelected() {
        /*Set startTimeLabelInfo displaying starting time in positive color*/
        if (selectedAppMinutes != null){
            setLengthComboBox();
            startTimeLabelInfo.setTextFill(FrameColor.getColorSucess());
            if (selectedAppMinutes == 0 ){
                startTimeLabelInfo.setText("в " + selectedAppHour + ":00");
            }
            else {
                startTimeLabelInfo.setText("в " + selectedAppHour + ":" + selectedAppMinutes);
            }
        }
        if (selectedAppMinutes == null){
            startTimeLabelInfo.setTextFill(FrameColor.getColorError());
            startTimeLabelInfo.setText("");
            setLengthComboBox();
        }
    }

    private void appLengthSelected() {
        if (selectedAppLength != null){

            /*Todo Place inside method*/
            int plusHours = selectedAppLength / 60 ;
            int plusMinutes = selectedAppLength % 60;
            System.err.println("Adding hours: " + plusHours);
            int startHour = selectedAppHour + plusHours;
            int startMinutes = selectedAppMinutes + plusMinutes;
            if (startMinutes >= 60){
                int plusHour = startMinutes/60;
                startMinutes = startMinutes%60;
                startHour += plusHour;
            }

            appLengthLabelInfo.setTextFill(FrameColor.getColorSucess());
            if (startMinutes == 0){
                appLengthLabelInfo.setText(selectedAppLength + " минут. до " + startHour + ":00");
            }
            else {
                appLengthLabelInfo.setText(selectedAppLength + " минут. до " + startHour + ":" + startMinutes);
            }
        }
        else {
            appLengthLabelInfo.setTextFill(FrameColor.getColorError());
            appLengthLabelInfo.setText("");
        }
    }


    private void doctorSelected(){


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


    /*Setters*/
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

    private void setHoursComboBox() {

        if (selectedWorkingDay == null){
            /*Disable combo box*/
            appHoursComboBox.setDisable(true);
            /*Clear items in combo box*/
            appHoursComboBox.getItems().clear();
            /*Set map of available time*/
            hoursAndMinutes = null;
        }
        else {
            /*Enable combo box*/
            appHoursComboBox.setDisable(false);
            /*Set map of available time*/
            hoursAndMinutes = selectedWorkingDay.getAvailableTime();
            /*Assign hours to the combo box*/
            for (HashMap.Entry<Integer, ArrayList<Integer>> entry : hoursAndMinutes.entrySet()){
                Integer key = entry.getKey();
                ArrayList<Integer> values = entry.getValue();
                if (values.isEmpty()){
                    continue;
                }
                appHoursComboBox.getItems().add(key);
            }
        }
    }

    private void setMinutesComboBox() {

        appMinutesComboBox.getItems().clear();
        if (selectedAppHour != null){
            System.out.println(hoursAndMinutes.get(selectedAppHour));
            ArrayList<Integer> minutes = hoursAndMinutes.get(selectedAppHour);

            for (Integer minute : minutes){
                appMinutesComboBox.getItems().add(minute);
            }
        }
    }
    private void setLengthComboBox() {

        appLengthComboBox.getItems().clear();
        if (selectedAppMinutes != null){
            System.err.println("Start time: " + selectedAppHour+ "  :  " + selectedAppMinutes);
            ArrayList<Integer> lengths = selectedWorkingDay.getAppointmentLengths(selectedAppHour, selectedAppMinutes);

            lengths.stream().forEach(length -> {appLengthComboBox.getItems().add(length);});
        }
    }


    /*Info labels setters*/
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



            //Todo populate appointments table

            //TODO set up hours combo box
        }
    }



    /*Listeners*/
    private void setHoursComboBoxListener() {
        appHoursComboBox.setPromptText(FrameAppointmentText.getHoursComboBoxText());
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

    private void setMinutesComboBoxListener() {
        appMinutesComboBox.setPromptText(FrameAppointmentText.getMinutesComboBoxText());
        appMinutesComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue == null){
                    selectedAppMinutes = null;
                    appMinutesSelected();
                }
                else {
                    selectedAppMinutes = newValue;
                    appMinutesSelected();
                }
            }
        });
    }

    private void setLengthComboBoxListener() {
        appLengthComboBox.setPromptText(FrameAppointmentText.getLengthComboBoxText());
        appLengthComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue == null){
                    selectedAppLength = null;
                    appLengthSelected();
                }
                else {
                    selectedAppLength = newValue;
                    appLengthSelected();
                }
            }
        });
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

    /*Buttons*/
    @FXML void createAppBtnAction(ActionEvent event) {

        System.err.println("Selected Patient : " + selectedPatient + " . Selected App Length " + selectedAppLength);
        if (selectedPatient != null && selectedAppLength != null) {
            System.err.println("New apointment, start time: " + selectedAppHour + " : " + selectedAppMinutes + " . Length: " + selectedAppLength);
            //appointments.addObject(createAppointment());
        }
    }



    @FXML void removeAppBtnAction(ActionEvent event) {

    }

    @FXML void updateAppBtnAction(ActionEvent event) {

    }

    @FXML void exitFrameBtnAction(ActionEvent event) {

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
    protected void initButtons() {

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
