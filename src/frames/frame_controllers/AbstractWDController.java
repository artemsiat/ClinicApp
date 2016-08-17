package frames.frame_controllers;

import data_base.DBSecondLayer;
import frames.FrameModel;
import instances.Doctor;
import instances.WorkingDay;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import programm.FrameColor;
import programm.Programm;
import programm.helper_classes.FormatDate;
import programm.helper_classes.WorkingDayFormat;
import programm.programm_settings.WorkingDaySettings;
import programm.texts.FrameButtonText;
import programm.texts.FrameErrorTexts;
import programm.texts.FrameLabelText;
import programm.texts.FrameTableText;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public abstract class AbstractWDController extends AbstractPersonController{

    Programm PROGRAMM;
    DBSecondLayer PERSON_DATA_BASE;
    DBSecondLayer WD_DATA_BASE;

    //Table
    private ObservableList<WorkingDay> workingDayObservableList;

    private WorkingDay selectedWorkingDay = null;

    //Stage

    @FXML private AnchorPane ANCHOR_PANE;

    //Working Day Table

    @FXML private TableView<WorkingDay> WD_TABLE;
    @FXML private TableColumn<WorkingDay, String> WD_DAY_COLUMN;
    @FXML private TableColumn<WorkingDay, String> WD_TIME_COLUMN;
    @FXML private TableColumn<WorkingDay, String> WD_BREAK_COLUMN;
    @FXML private TableColumn<WorkingDay, String> WD_NO_APP_COLUMN;

    //Working day Table Buttons

    @FXML private Label TABLE_WD_IS_CREATED_LABEL;
    @FXML private Label TABLE_WD_STATUS_LABEL;
    @FXML private Button CHECK_WD_TABLE_BUTTON;
    @FXML private Button CREATE_WD_TABLE_BUTTON1;
    @FXML private Button DROP_WD_TABLE_BUTTON1;


    //Working Day Date Picker

    @FXML private Label WD_LABEL;
    @FXML private DatePicker WD_PICKER;
    @FXML private Button PREV_WD_BUTTON;
    @FXML private Label WD_DATE_PICKER_INFO;
    @FXML private Button NEXT_WD_BUTTON;


    // Working day Working day Time picker (Slider)

    @FXML private Label WD_TIME_LABEL;
    @FXML private Label START_WD_TIME_LABEL;
    @FXML private Label END_WD_TIME_LABEL;
    @FXML private Slider START_WD_TIME_SLIDER;
    @FXML private Label START_WD_TIME_INFO;
    @FXML private Label END_WD_TIME_INFO;
    @FXML private Slider END_WD_TIME_SLIDER;

    // Working day Working day Lunch Time picker (Slider)

    @FXML private Slider START_LUNCH_SLIDER;
    @FXML private Slider END_LUNCH_SLIDER;
    @FXML private Label WD_LUNCH_TIME_LABEL;
    @FXML private Label START_LUNCH_TIME_LABEL;
    @FXML private Label END_LUNCH_TIME_LABEL;
    @FXML private Label START_LUNCH_TIME_INFO;
    @FXML private Label END_LUNCH_TIME_INFO;

    //Working day Buttons

    @FXML private Button ADD_WD_BUTTON;
    @FXML private Button UPDATE_WD_BUTTON;
    @FXML private Button DELETE_WD_BUTTON;

    //Check to show passed working days

    @FXML private CheckBox WD_PASSED_CHECK;

    //Constructor

    public AbstractWDController(Programm PROGRAMM, FrameModel frameModel, DBSecondLayer personDataBase, DBSecondLayer wdDataBase) {
        super(PROGRAMM, frameModel, personDataBase);

        this.PROGRAMM = PROGRAMM;
        this.PERSON_DATA_BASE = personDataBase;
        this.WD_DATA_BASE = wdDataBase;

    }

    //Initialization
    protected void initializeWorkingDayController(){

        //Table
        initWorkingDayTable();
        setTableColumnLabels();
        setTableLabelButtons();
        setTableCreatedStatus();
        createWDTableListener();

        //Working Day
        setWorkingDayButtons();
        setWorkingDayLabels();

        //Sliders
        createSliderListeners();
        setSlidersMaxValue();
        setSliderInitialValue();


        //Calendar
        createWdCalendarListener();

        //Check box listener
        setWDCheckBoxListener();

        deselectDatePicker();

        //Todo tests

        //Set the background
        //ANCHOR_PANE.setStyle("-fx-background-color: #FFFFE2;");

    }


    //Person Selected

    /**
     * Abstract method that is being called from Abstract Person Controller
     * When a person(doctor) is selected
     */
    protected void personSelected(){

        //Set the date Working day List
        setWorkingDaysList(((Doctor)SELECTED_PERSON).getWorkingDays());

        //Advice date picker starting from now
        adviceDatePickerDate(LocalDate.now());

        //Working day fields
        setSliderInitialValue();

        setDatePickerFactory();
    }

    private void setWorkingDaysList(ObservableList<WorkingDay> workingDays) {
        workingDayObservableList = workingDays;
        FXCollections.sort(workingDayObservableList);
        WD_TABLE.setItems(workingDayObservableList);
    }


    //Person deselected

    /**
     * Abstract method that is being called from Abstract Person Controller
     * When a person(doctor) is deselected
     */
    protected void personDeselected(){

        //Clear list
        WD_TABLE.getSelectionModel().select(null);
        WD_TABLE.setItems(null);
        workingDayObservableList = null;

        WD_PICKER.setValue(null);

        setDatePickerFactory();
    }


    //Helper methods

    //Date
    /**
     * Private Method that is called either when the person is selected or a new Working day is added
     * to change the working day to the next day that is not a working day
     * Input value is the day from which the search should be started for new day
     */
    private void adviceDatePickerDate(LocalDate startDate){

        LocalDate nextAvailableDate = WorkingDayFormat.findNextAvailableDate(workingDayObservableList, LocalDate.now());
        WD_PICKER.setValue(nextAvailableDate);
    }



    //Table

    /**
     * Sets the selected working day to be the working day that was chosen by the table click
     */
    private void selectWorkingDay() {
        selectedWorkingDay = workingDayObservableList.get(WD_TABLE.getSelectionModel().getSelectedIndex());
        setSelectedWDFields();
    }

    private void deselectWorkingDay() {

        selectedWorkingDay = null;

        deselectDatePicker();
        setSliderInitialValue();
    }


    /**
     * Method is called when ever the working day is selected from the table
     * This method sets the Working Day fields
     * to the values of the working day selected
     */
    private void setSelectedWDFields() {

        //SET WD date picker
        WD_PICKER.setValue(selectedWorkingDay.getDate());

        //Set start and end time of WD
        START_WD_TIME_SLIDER.setValue(WorkingDayFormat.convertToSliderValue(selectedWorkingDay.getStartTime()));
        END_WD_TIME_SLIDER.setValue(WorkingDayFormat.convertToSliderValue(selectedWorkingDay.getEndTime()));

        //Set start and end of lunch time
        START_LUNCH_SLIDER.setValue(WorkingDayFormat.convertToSliderValue(selectedWorkingDay.getLunchStart()));
        END_LUNCH_SLIDER.setValue(WorkingDayFormat.convertToSliderValue(selectedWorkingDay.getLunchEnd()));

        //Set start and end of lunch time
    }




    //Table Methods

    private void setTableCreatedStatus() {
        setTableCreatedStatus(WD_DATA_BASE.isTableCreated());
    }
    /**
     * Sets the table is created status label
     */
    private void setTableCreatedStatus(boolean status) {
        if (status){
            TABLE_WD_STATUS_LABEL.setText(FrameLabelText.getTableCreatedStatusPos());
            TABLE_WD_STATUS_LABEL.setTextFill(FrameColor.getColorSucess());
        }
        else {
            TABLE_WD_STATUS_LABEL.setText(FrameLabelText.getTableCreatedStatusNeg());
            TABLE_WD_STATUS_LABEL.setTextFill(FrameColor.getColorError());
        }
    }


    //Slider methods

    private void setLunchSliderLabels(){

        int startValue = (int)START_LUNCH_SLIDER.getValue();
        int endValue = (int)END_LUNCH_SLIDER.getValue();

        // if slider's values are the same
        if (startValue == endValue){
            START_LUNCH_TIME_INFO.setText(FrameLabelText.getNoLunchLabel());
            END_LUNCH_TIME_INFO.setText(FrameLabelText.getNoLunchLabel());
        }
        else{
            START_LUNCH_TIME_INFO.setText(WorkingDayFormat.getStartSliderLabel(startValue));
            END_LUNCH_TIME_INFO.setText(WorkingDayFormat.getEndSliderLabel(endValue));
        }
    }

    private void setDayTimeSliderLabels(){
        int startValue = (int)START_WD_TIME_SLIDER.getValue();
        int endValue = (int)END_WD_TIME_SLIDER.getValue();

        START_WD_TIME_INFO.setText(WorkingDayFormat.getStartSliderLabel(startValue));
        END_WD_TIME_INFO.setText(WorkingDayFormat.getEndSliderLabel(endValue));
    }




    //Date Picker
    private boolean dateIsWorkingDate( LocalDate item) {

        if (workingDayObservableList != null){

            for (WorkingDay workingDay : workingDayObservableList){
                if (workingDay.getDate().equals(item))
                    return true;
            }

        }
        return false;
    }
    private void adviceCalendarDate() {

        //Creating Day Cell Factory
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override public DateCell call(DatePicker param) {

                return new DateCell(){

                    @Override public void updateItem(LocalDate item, boolean empty) {

                        super.updateItem(item, empty);

                        setTooltip(new Tooltip("Не рабочий день"));

                        //if date is a working day set it to color
                        if (dateIsWorkingDate(item)){

                            setDisable(true);
                            setStyle("-fx-background-color: #C2DFFF;");
                        }

                        // else if date is before today ,set date to color and disable for picking
                        else if (item.isBefore(LocalDate.now())){

                            setDisable(true);
                            setStyle("-fx-background-color: #EEE8AA;");//Yellow - EEE8AA, Red - ffc0cb, blue - 0000FF, green - 008000;
                        }

                        // else set the color to available date
                        else {
                            setStyle("-fx-background-color: #ADFF2F;");
                        }
                    }
                };
            }
        };




        WD_PICKER.setDayCellFactory(dayCellFactory);

        //Sets the next available day
        //LocalDate dateToSet = nextAvailableDate(day);
        //WD_PICKER.setValue(dateToSet);

    }
    /**
     * Sets Date picker value to null
     * and info label to no date chosen label
     *
     * called when frame starts or when a person is deselected
     * or from date picker listener when the date is not a working date
     */
    private void deselectDatePicker(){

        WD_PICKER.setValue(null);

        //Setting Date Picker Info Label
        WD_DATE_PICKER_INFO.setTextFill(FrameColor.getColorError());
        WD_DATE_PICKER_INFO.setText(FrameErrorTexts.getNoDobChosen());
    }


    //Frame Buttons
    //Working Day Buttons
    @FXML void addWDBtnAction(ActionEvent event) {

        //Check that doctor is selected
        if (SELECTED_PERSON == null){
            System.out.println("Person is not selected addWDBtnAction()");
            return;
        }
        //Check that Day is selected
        if (WD_PICKER.getValue() == null){
            System.out.println("Date is not set addWDBtnAction()");
            return;
        }

        //Check that the chosen date is not a working day
        if (dateIsWorkingDate(WD_PICKER.getValue())){
            System.out.println("Date is already a working day addWDBtnAction()");
            return;
        }

        //if date is before today
        if (WD_PICKER.getValue().isBefore(LocalDate.now())){
            System.out.println("The chosen date is before today ");
            return;
        }

        WorkingDay workingDay = createNewWorkingDay();

        WD_DATA_BASE.addObject(workingDay);
        FXCollections.sort(workingDayObservableList);

        //advice next date
        LocalDate newDate = WorkingDayFormat.findNextAvailableDate(workingDayObservableList, workingDay.getDate());
        WD_PICKER.setValue(newDate);

    }
    @FXML void deleteWdBtnAction(ActionEvent event) {

        if (selectedWorkingDay == null){
            return; //Working day is not selected
        }
        WD_DATA_BASE.completeRemoveObject(selectedWorkingDay);



    }
    @FXML void updateWdBtnAction(ActionEvent event) {

        //Check that doctor is selected
        if (SELECTED_PERSON == null){
            System.out.println("Person is not selected updateWdBtnAction()");
            return;
        }

        //Check that working day is selected in the table
        if (WD_TABLE.getSelectionModel().getSelectedItem() == null){
            System.out.println("Working day is not selected updateWdBtnAction()");
            return;
        }
        //Check that Day is selected
        if (WD_PICKER.getValue() == null){
            System.out.println("Date is not set updateWdBtnAction()");
            return;
        }

        WorkingDay workingDay = createNewWorkingDay();
        if (WD_DATA_BASE.updateObject(selectedWorkingDay, workingDay)){
            System.out.println("WD updated updateWdBtnAction()");
        }
        deselectWorkingDay();
        WD_TABLE.refresh();


    }

    //WD Buttons helper methods
    private WorkingDay createNewWorkingDay() {
        //Create new Working day
        WorkingDay workingDay = new WorkingDay();


        workingDay.setCreator(PROGRAMM.getCurrentAdmin().getID());
        workingDay.setDoctorID(SELECTED_PERSON.getID());
        workingDay.setwhencreated(LocalDateTime.now());
        workingDay.setDeleted(false);
        workingDay.setDate(WD_PICKER.getValue());
        workingDay.setStartTime(WorkingDayFormat.convertSliderValue(START_WD_TIME_SLIDER.getValue()));
        workingDay.setEndTime(WorkingDayFormat.convertSliderValue(END_WD_TIME_SLIDER.getValue()));
        workingDay.setLunchStart(WorkingDayFormat.convertSliderValue(START_LUNCH_SLIDER.getValue()));
        workingDay.setLunchEnd(WorkingDayFormat.convertSliderValue(END_LUNCH_SLIDER.getValue()));

        return workingDay;
    }

    //Table Buttons
    @FXML void checkWdTableBtnAction(ActionEvent event) {
        boolean status = WD_DATA_BASE.checkTable();
        LOG_INFO = WD_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(WD_DATA_BASE.isTableCreated());
        displayLog();
    }
    @FXML void createWdTableBtnAction(ActionEvent event) {

        boolean status = WD_DATA_BASE.createTable();
        LOG_INFO = WD_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(WD_DATA_BASE.isTableCreated());
        displayLog();
    }
    @FXML void dropWdTableBtnAction(ActionEvent event) {

        boolean status = WD_DATA_BASE.dropTable();
        LOG_INFO = WD_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(WD_DATA_BASE.isTableCreated());
        displayLog();
    }

    //Date Picker buttons
    @FXML void nextDateBtnAction(ActionEvent event) {
        //Sets the date picker to next date
        //If date picker's value is null ( and doctor is selected) , then set the date picker to today
        // if doctor is not selected then nothing happens
        if (SELECTED_PERSON == null){
            return;
        }
        //If date picker's value is null ( and doctor is selected) , then set the date picker to today
        if (WD_PICKER.getValue() == null){
            WD_PICKER.setValue(LocalDate.now());
            return;
        }
        //Else set the date picker value to the next date
        LocalDate dateSelected = WD_PICKER.getValue();
        WD_PICKER.setValue(dateSelected.plusDays(1));
    }
    @FXML void prevDateBtnAction(ActionEvent event) {
        //Sets the date picker to prev date
        // if doctor is not selected then nothing happens
        if (SELECTED_PERSON == null){
            return;
        }
        if (WD_PICKER.getValue() == null){
            WD_PICKER.setValue(LocalDate.now());
            return;
        }
        //Else set the date picker value to the previous date
        LocalDate dateSelected = WD_PICKER.getValue();
        WD_PICKER.setValue(dateSelected.minusDays(1));



    }


    //Setting up Frame
    //Table
    private void initWorkingDayTable() {

        WD_DAY_COLUMN.setCellValueFactory(new PropertyValueFactory<WorkingDay, String>("workingDayProperty"));
        WD_TIME_COLUMN.setCellValueFactory(new PropertyValueFactory<WorkingDay, String>("workingHoursProperty"));
        WD_BREAK_COLUMN.setCellValueFactory(new PropertyValueFactory<WorkingDay, String >("lunchProperty"));
        WD_NO_APP_COLUMN.setCellValueFactory(new PropertyValueFactory<WorkingDay, String>("noOfAppointments"));

    }
    private void setTableColumnLabels(){
        WD_DAY_COLUMN.setText(FrameTableText.getDateColumnText());
        WD_TIME_COLUMN.setText(FrameTableText.getHoursColumnText());
        WD_BREAK_COLUMN.setText(FrameTableText.getBreaksColumnText());
        WD_NO_APP_COLUMN.setText(FrameTableText.getNoAppointmentsColumnText());
    }
    private void setTableLabelButtons(){
        TABLE_WD_IS_CREATED_LABEL.setText(FrameLabelText.getTableStatusLabel());

        CHECK_WD_TABLE_BUTTON.setText(FrameButtonText.getTableUpdateButton());
        CREATE_WD_TABLE_BUTTON1.setText(FrameButtonText.getTableCreateButton());
        DROP_WD_TABLE_BUTTON1.setText(FrameButtonText.getTableDropButton());
    }
    private void createWDTableListener() {

        //Add change working day under question????
        WD_TABLE.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                deselectWorkingDay();
            }
            else {
                selectWorkingDay();
            }
        });
    }

    //Working Day
    private void setWorkingDayButtons() {
        ADD_WD_BUTTON.setText(FrameButtonText.getAddButtonText());
        UPDATE_WD_BUTTON.setText(FrameButtonText.getUpdateButtonText());
        DELETE_WD_BUTTON.setText(FrameButtonText.getRemoveButtonText());

        NEXT_WD_BUTTON.setText(FrameButtonText.getNextButtonText());
        PREV_WD_BUTTON.setText(FrameButtonText.getPreviousButtonText());
    }
    private void setWorkingDayLabels() {
        WD_LABEL.setText(FrameLabelText.getWdDatePickerLabel());

        WD_TIME_LABEL.setText(FrameLabelText.getWdTimeLabel());
        START_WD_TIME_LABEL.setText(FrameLabelText.getWdStartTimeLabel());
        END_WD_TIME_LABEL.setText(FrameLabelText.getWdEndTimeLabel());


        WD_LUNCH_TIME_LABEL.setText(FrameLabelText.getWdLunchLabel());
        START_LUNCH_TIME_LABEL.setText(FrameLabelText.getWdStartLunchLabel());
        END_LUNCH_TIME_LABEL.setText(FrameLabelText.getWdEndLunchLabel());

        WD_PASSED_CHECK.setText(FrameLabelText.getWdShowPreviousWdCheck());

    }
    private void setDatePickerLabel() {
        //If person is not selected , return
        if (SELECTED_PERSON == null){
            WD_DATE_PICKER_INFO.setTextFill(FrameColor.getColorError());
            WD_DATE_PICKER_INFO.setText(FrameErrorTexts.getNoDoctorChosenWdPicker());
            return;
        }

        LocalDate datePickerValue = WD_PICKER.getValue();
        //If value of date picker is null, set label
        if (datePickerValue ==  null){
            WD_DATE_PICKER_INFO.setTextFill(FrameColor.getColorError());
            WD_DATE_PICKER_INFO.setText(FrameErrorTexts.getNoDobChosen());
            return;
        }

        //if date is older then the specified time
        if (datePickerValue.isBefore(LocalDate.now().minusMonths(WorkingDaySettings.getMonthsFromToday()))){
            WD_DATE_PICKER_INFO.setTextFill(FrameColor.getColorError());
            WD_DATE_PICKER_INFO.setText(FrameErrorTexts.getChosenDateTooOld());
            return;
        }

        //If date is before today
        if (datePickerValue.isBefore(LocalDate.now())){

            //Set checkBox to true
            WD_PASSED_CHECK.setSelected(true);
            String formatedDate = FormatDate.convertDateToMyFormat(datePickerValue);

            //Check if date is past working date
            if (dateIsWorkingDate(WD_PICKER.getValue())){
                WD_DATE_PICKER_INFO.setTextFill(FrameColor.getIsWorkingDayColor());
                WD_DATE_PICKER_INFO.setText(formatedDate + (" Рабочий день."));
            }
            else{
                WD_DATE_PICKER_INFO.setTextFill(FrameColor.getNotWorkingDayColor());
                WD_DATE_PICKER_INFO.setText(formatedDate + (" Не рабочий день."));
            }

        }
        //if date is today or after today
        else{
            //Set checkbox to false
            WD_PASSED_CHECK.setSelected(false);
            String formatedDate = FormatDate.convertDateToMyFormat(datePickerValue);
            //Check if date is working date

            if (dateIsWorkingDate(WD_PICKER.getValue())){
                WD_DATE_PICKER_INFO.setTextFill(FrameColor.getIsWorkingDayColor());
                WD_DATE_PICKER_INFO.setText(formatedDate + (" Рабочий день."));
            }
            else{
                WD_DATE_PICKER_INFO.setTextFill(FrameColor.getNotWorkingDayColor());
                WD_DATE_PICKER_INFO.setText(formatedDate + (" Не рабочий день."));
            }
        }
    }

    //Slider
    private void setSlidersMaxValue(){
        START_WD_TIME_SLIDER.setMax(WorkingDayFormat.getSliderMaxValue());
        END_WD_TIME_SLIDER.setMax(WorkingDayFormat.getSliderMaxValue());

        START_LUNCH_SLIDER.setMax(WorkingDayFormat.getSliderMaxValue());
        END_LUNCH_SLIDER.setMax(WorkingDayFormat.getSliderMaxValue());
    }
    private void setSliderInitialValue(){

        END_WD_TIME_SLIDER.setValue(WorkingDaySettings.getDayEndTime());
        START_WD_TIME_SLIDER.setValue(WorkingDaySettings.getDayStartTime());

        START_LUNCH_SLIDER.setValue(WorkingDaySettings.getLunchStartTime());
        END_LUNCH_SLIDER.setValue(WorkingDaySettings.getLunchEndTime());

    }
    private void createSliderListeners(){

        //Start time of working day slider
        START_WD_TIME_SLIDER.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                //if start slider move beyond the end slider value
                if (newValue.doubleValue() > END_WD_TIME_SLIDER.getValue()){
                    //set the value of end slider to the same value as start slider
                    END_WD_TIME_SLIDER.setValue(newValue.doubleValue());
                }
                //if slider moves after the start of lunch time
                if (newValue.doubleValue() > START_LUNCH_SLIDER.getValue()){
                    //change the start lunch slider
                    START_LUNCH_SLIDER.setValue(newValue.doubleValue());
                }

                setDayTimeSliderLabels();
            }
        });

        //End time of working day slider

        END_WD_TIME_SLIDER.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                //Make sure the End slider does not slide before start slider
                if (newValue.doubleValue() < START_WD_TIME_SLIDER.getValue()){
                    //set the value of start slider to the same value as end slider
                    START_WD_TIME_SLIDER.setValue(newValue.doubleValue());
                }

                // if slider value is smaller then the end lunch slider value
                if (newValue.doubleValue() < END_LUNCH_SLIDER.getValue()){
                    //move the end Lunch slider
                    END_LUNCH_SLIDER.setValue(newValue.doubleValue());
                }

                //Todo if end slider slide before end of lunch time , then move the lunch time

                setDayTimeSliderLabels();
            }
        });


        //Start of lunch time slider

        START_LUNCH_SLIDER.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                //Make sure the lunch does not start after it ends
                if (newValue.doubleValue() > END_LUNCH_SLIDER.getValue()){

                    END_LUNCH_SLIDER.setValue(newValue.doubleValue());

                }

                // Make sure the lunch time is in bounds of working day
                //if lunch starts before the working day starts
                if (newValue.doubleValue() < START_WD_TIME_SLIDER.getValue()){
                    //change the value of the start of the working day
                    START_WD_TIME_SLIDER.setValue(newValue.doubleValue());
                }


                setLunchSliderLabels();
            }
        });


        //End of lunch time slider

        END_LUNCH_SLIDER.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                //Make sure the lunch does not end before it starts
                if (newValue.doubleValue() < START_LUNCH_SLIDER.getValue()){

                    START_LUNCH_SLIDER.setValue(newValue.doubleValue());

                }
                // Make sure the lunch time is in bounds of working day
                //if lunch ends after the end of the working day
                if (newValue.doubleValue() > END_WD_TIME_SLIDER.getValue()){
                    //change the end of the day value
                    END_WD_TIME_SLIDER.setValue(newValue.doubleValue());
                }

                setLunchSliderLabels();

            }
        });


    }

    //Date Picker Listener
    private void createWdCalendarListener(){
        WD_PICKER.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                setDatePickerLabel();

            }
        });
    }
    private void setDatePickerFactory(){

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

                        //If working day is null return
                        if (workingDayObservableList == null) {
                            return;}


                        LocalDate today = LocalDate.now();

                        //Working day
                        if (((Doctor)SELECTED_PERSON).isWorkingDay(item) != null){
                            setTooltip(new Tooltip("Рабочий день"));
                            //if date is before today
                            if (item.isBefore(today)){
                                setStyle("-fx-background-color: " + FrameColor.getHexPastWorking() + ";");
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
                            } else {
                                setStyle("-fx-background-color: " + FrameColor.getHexFutureNonWorking() + ";");
                            }
                        }
                    }
                };
            }
        };
        WD_PICKER.setDayCellFactory(dayCellFactory);
    }
    @FXML void wdDatePickerAction(MouseEvent event) {
        setDatePickerFactory();
    }

    //Check Box listener
    private void setWDCheckBoxListener() {
        WD_PASSED_CHECK.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == true){
                ObservableList<WorkingDay> list = ((Doctor)SELECTED_PERSON).getWorkingDaysPast();
                setWorkingDaysList(list);
            }
            if (newValue ==  false){
                ObservableList<WorkingDay> list = ((Doctor)SELECTED_PERSON).getWorkingDays();
                setWorkingDaysList(list);
            }
        }
        );
    }


    //Abstract methods
    protected abstract ObservableList getWorkingDays();
    protected abstract ObservableList getWorkingDaysPast();

}
