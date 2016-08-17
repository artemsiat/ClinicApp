package frames.frame_controllers;

import data_base.DBSecondLayer;
import frames.FrameModel;
import instances.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import programm.FrameColor;
import programm.Programm;
import programm.helper_classes.FormatDate;
import programm.texts.FrameButtonText;
import programm.texts.FrameErrorTexts;
import programm.texts.FrameLabelText;
import programm.texts.FrameTableText;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Artem Siatchinov on 7/15/2016.
 */
public abstract class AbstractPersonController {

    private Programm PROGRAMM;
    private FrameModel FRAME_MODEL;

    private DBSecondLayer PEOPLE_DATA_BASE;
    private ObservableList<Person> PERSON_LIST;
    Person SELECTED_PERSON;

    //Table

    @FXML private TableView<Person> TABLE_PERSON;
    @FXML private TableColumn<Person, String> FULL_NAME_COL;
    @FXML private TableColumn<Person, String> PHONE_COL;
    @FXML private TableColumn<Person, String> DOB_COL;
    @FXML private TableColumn<Person, String> EMAIL_COL;

    //Person Labels

    @FXML private Label LAST_NAME_LABEL;
    @FXML private Label FIRST_NAME_LABEL;
    @FXML private Label MIDDLE_NAME_LABEL;
    @FXML private Label DOB_LABEL;
    @FXML private Label PHONE_LABEL;
    @FXML private Label EMAIL_LABEL;

    //Person Info Labels

    @FXML private Label DOB_INFO_LABEL;
    @FXML private Label LAST_NAME_INFO_LABEL;

    //Person Fields

    @FXML private TextField LAST_NAME_FIELD;
    @FXML private TextField FIRST_NAME_FIELD;
    @FXML private TextField MIDDLE_NAME_FIELD;
    @FXML private DatePicker DOB_DATE_PICKER;
    @FXML private TextField PHONE_FIELD;
    @FXML private TextField EMAIL_FIELD;

    //Person Buttons

    @FXML private Button ADD_PERSON_BUTTON;
    @FXML private Button UPDATE_PERSON_BUTTON;
    @FXML private Button DELETE_PERSON_BUTTON;
    @FXML private Button RESTORE_PERSON_BUTTON;
    @FXML private Button CLEAR_FIELDS_BUTTON;

    //Create Drop Table Labels and buttons

    @FXML private Label TABLE_IS_CREATED_LABEL;
    @FXML private Label TABLE_CREATE_STATUS_LABEL;
    @FXML private Button CREATE_TABLE_BUTTON;
    @FXML private Button DROP_TABLE_BUTTON;
    @FXML private Button CHECK_TABLE_BUTTON;

    //Restore Check Box

    @FXML private CheckBox SHOW_REMOVED_CHECKBOX;

    //Frame Buttons
    @FXML private Button EXIT_BUTTON;

    //Text Area Log Info
    @FXML private TextArea LOG_TEXT_AREA;

    protected String LOG_INFO = "";


    //Constructor

    public AbstractPersonController(Programm PROGRAMM, FrameModel frameModel, DBSecondLayer persomDataBase){
        this.PROGRAMM = PROGRAMM;
        this.FRAME_MODEL = frameModel;
        this.PEOPLE_DATA_BASE = persomDataBase;

        SELECTED_PERSON = null;
    }


    //Initialize

    public void initializePersonController(){

        this.PERSON_LIST = getPersonList();

        createPersonTable();

        setPersonButtonsText();
        setTableLabelsButtons();
        setTableColumnLabels();
        setPersonLabelText();

        //Set the status of the tableIsCreated
        setTableCreatedStatus();

        setCheckBoxListener();
        setDobListener();
        setPersonTableListener();

        clearFieldsInfo();
    }




    //Table

    @FXML void personTableClicked(MouseEvent event) {

        //Todo not implemented
    }
    /**
     * Sets the table is created status label
     *
     */
    private void setTableCreatedStatus(boolean status) {
        if (status){
            TABLE_CREATE_STATUS_LABEL.setText(FrameLabelText.getTableCreatedStatusPos());
            TABLE_CREATE_STATUS_LABEL.setTextFill(FrameColor.getColorSucess());
        }
        else {
            TABLE_CREATE_STATUS_LABEL.setText(FrameLabelText.getTableCreatedStatusNeg());
            TABLE_CREATE_STATUS_LABEL.setTextFill(FrameColor.getColorError());
        }
    }

    private void setTableCreatedStatus() {
        setTableCreatedStatus(PEOPLE_DATA_BASE.isTableCreated());
    }

    protected void createPersonTable() {

        FULL_NAME_COL.setCellValueFactory(new PropertyValueFactory<Person, String>("FULL_NAME_PROPERTY"));
        PHONE_COL.setCellValueFactory(new PropertyValueFactory<Person, String>("PHONE_NUMBER_PROPERTY"));
        DOB_COL.setCellValueFactory(new PropertyValueFactory<Person, String >("DATE_OF_BIRTH_PROPERTY"));
        EMAIL_COL.setCellValueFactory(new PropertyValueFactory<Person, String>("EMAIL_PROPERTY"));

        TABLE_PERSON.setItems(PERSON_LIST);
    }

    private void setTableColumnLabels(){

        FULL_NAME_COL.setText(FrameTableText.getFullNameColumnText());
        PHONE_COL.setText(FrameTableText.getPhoneColumnText());
        DOB_COL.setText(FrameTableText.getDobColumnText());
        EMAIL_COL.setText(FrameTableText.geteMailColumnText());

    }


    private void setPersonTableListener() {

        TABLE_PERSON.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                deselectPerson();
            }
            else {
                selectPerson();
            }
        });
    }

    //Buttons Table

    @FXML void createTableBtnAction(ActionEvent event) {

        boolean status = PEOPLE_DATA_BASE.createTable();
        LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(PEOPLE_DATA_BASE.isTableCreated());
        displayLog();

    }

    @FXML void dropTableBtnAction(ActionEvent event) {
        boolean status = PEOPLE_DATA_BASE.dropTable();
        LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(PEOPLE_DATA_BASE.isTableCreated());
        displayLog();
    }

    /**
     * Calls the DataBaseModel to check if table is created
     * All DataBase Instances inherit from DataBaseModel
     */
    @FXML void checkTableBtnAction(ActionEvent event) {
        boolean status = PEOPLE_DATA_BASE.checkTable();
        LOG_INFO = PEOPLE_DATA_BASE.getStatus() + "\n" + "\n" + LOG_INFO;

        setTableCreatedStatus(PEOPLE_DATA_BASE.isTableCreated());
        displayLog();
    }



    //Buttons Frame

    @FXML void exitBtnAction(ActionEvent event) {
        FRAME_MODEL.stop();
    }



    //Buttons Person

    @FXML void addBtnAction(ActionEvent event) {

        clearFieldsInfo();

        if (!checkInputFields()){
            return;
        }

        // Create new person instance and add it to the data base
        Person newPerson = createPerson();
        if (PEOPLE_DATA_BASE.addObject(newPerson)){
            TABLE_PERSON.getSelectionModel().select(null);
        }

    }

    /**
     * Marks the delete column as true for this row
     * if the object is already marked as true then the object will be removed completely
     */
    @FXML void removeBtnAction(ActionEvent event) {

        //If object is already in deleted list then complete delete
        clearFieldsInfo();
        if (SELECTED_PERSON !=null && SELECTED_PERSON.isDeleted()){
            if (PEOPLE_DATA_BASE.completeRemoveObject(SELECTED_PERSON)){
                TABLE_PERSON.getSelectionModel().select(null);
                return;
            }
        }


        if (SELECTED_PERSON != null){
            if (PEOPLE_DATA_BASE.removeObject(SELECTED_PERSON)){
                TABLE_PERSON.getSelectionModel().select(null);
            }
        }
    }

    @FXML void updateBtnAction(ActionEvent event) {

        //Will not update a person who is marked as deleted
        if (SELECTED_PERSON != null && SELECTED_PERSON.isDeleted()){
            return;
        }

        clearFieldsInfo();

        if (SELECTED_PERSON != null){
            if (PEOPLE_DATA_BASE.updateObject(SELECTED_PERSON, createPerson())){
                //deselect Selected person
                TABLE_PERSON.getSelectionModel().select(null);
            }
        }

    }

    @FXML void restoreBtnAction(ActionEvent event) {
        if (SELECTED_PERSON != null){
            if (PEOPLE_DATA_BASE.restoreObject(SELECTED_PERSON)){
                TABLE_PERSON.getSelectionModel().select(null);
            }
        }
    }

    @FXML void clearFieldsBtnAcion(ActionEvent event) {
        TABLE_PERSON.getSelectionModel().select(null);

        deselectPerson();
    }



    private Person createPerson() {

        Person person = (Person)PEOPLE_DATA_BASE.getNewDataBaseInstance();

        person.setFIRST_NAME(FIRST_NAME_FIELD.getText());
        person.setMIDDLE_NAME(MIDDLE_NAME_FIELD.getText());
        person.setLAST_NAME(LAST_NAME_FIELD.getText());

        person.setDOB(DOB_DATE_PICKER.getValue());
        person.setPHONE(PHONE_FIELD.getText());
        person.setEMAIL(EMAIL_FIELD.getText());

        person.setCreator(PROGRAMM.getCurrentAdmin().getID());

        LocalDateTime created = LocalDateTime.now();
        person.setwhencreated(created);
        person.setDeleted(false);

        createNewObject(person);

        return person;
    }

    private boolean checkInputFields(){
        return checkObjectInputFields();
    }

    protected void displayLog() {
        LOG_TEXT_AREA.setText(LOG_INFO);
    }

    private void deselectPerson() {

        SELECTED_PERSON = null;
        clearPersonFields();
        clearFieldsInfo();

        personDeselected();

    }

    private void clearFieldsInfo(){
        LAST_NAME_INFO_LABEL.setText("");
        DOB_INFO_LABEL.setText("");

        clearObjectFieldsInfo();
    }

    private void selectPerson(){
        SELECTED_PERSON = PERSON_LIST.get(TABLE_PERSON.getSelectionModel().getSelectedIndex());
        setSelectedPersonFields();
        personSelected();
    }

    protected void selectPerson(Person person){
        SELECTED_PERSON = person;

        //Todo find person in table and selected it

        setSelectedPersonFields();
        personSelected();
    }

    /**
     * Sets all the person input fields
     * calls the abstract method (Set Object Fields)
     */
    private void setSelectedPersonFields(){

        LAST_NAME_FIELD.setText(SELECTED_PERSON.getLAST_NAME());
        FIRST_NAME_FIELD.setText(SELECTED_PERSON.getFIRST_NAME());
        MIDDLE_NAME_FIELD.setText(SELECTED_PERSON.getMIDDLE_NAME());
        DOB_DATE_PICKER.setValue(SELECTED_PERSON.getDOB());
        PHONE_FIELD.setText(SELECTED_PERSON.getPHONE());
        EMAIL_FIELD.setText(SELECTED_PERSON.getEMAIL());

        setObjectFields(SELECTED_PERSON);

    }

    /**
     * Clears all the person input fields
     * calls the abstract method (Clear Object Fields)
     */
    private  void clearPersonFields(){
        LAST_NAME_FIELD.setText("");
        FIRST_NAME_FIELD.setText("");
        MIDDLE_NAME_FIELD.setText("");
        DOB_DATE_PICKER.setValue(null);
        PHONE_FIELD.setText("");
        EMAIL_FIELD.setText("");

        clearObjectFields();

    }




    //Prepares the frame for display

    /**
     * Sets the followding labels text
     * First name
     * Last name
     * Middle name
     *
     * Date of birth
     * Phone number
     * e-mail
     */
    private void setPersonLabelText() {

        LAST_NAME_LABEL.setText(FrameLabelText.getLastNameLabel());
        FIRST_NAME_LABEL.setText(FrameLabelText.getFirstNameLabel());
        MIDDLE_NAME_LABEL.setText(FrameLabelText.getMiddleNameLabel());

        DOB_LABEL.setText(FrameLabelText.getDobLabel());
        PHONE_LABEL.setText(FrameLabelText.getPhoneLabel());
        EMAIL_LABEL.setText(FrameLabelText.getEmailLabel());

    }

    /**
     * Set buttons text for the following buttons:
     *
     * Add instance
     * Remove instance
     * Update instance
     *
     */
    private void setPersonButtonsText(){

        ADD_PERSON_BUTTON.setText(FrameButtonText.getAddButtonText());
        UPDATE_PERSON_BUTTON.setText(FrameButtonText.getUpdateButtonText());
        DELETE_PERSON_BUTTON.setText(FrameButtonText.getRemoveButtonText());
        RESTORE_PERSON_BUTTON.setText(FrameButtonText.getRestoreButtonText());
        CLEAR_FIELDS_BUTTON.setText(FrameButtonText.getClearFieldsButtonText());

        EXIT_BUTTON.setText(FrameButtonText.getExitFrameButtonText());
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
    private void setTableLabelsButtons(){
        CREATE_TABLE_BUTTON.setText(FrameButtonText.getTableCreateButton());
        DROP_TABLE_BUTTON.setText(FrameButtonText.getTableDropButton());
        CHECK_TABLE_BUTTON.setText(FrameButtonText.getTableUpdateButton());

        TABLE_IS_CREATED_LABEL.setText(FrameLabelText.getTableStatusLabel());

    }

    //Listeners
    private void setDobListener() {
        DOB_DATE_PICKER.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (newValue == null){
                    DOB_INFO_LABEL.setTextFill(FrameColor.getColorError());
                    DOB_INFO_LABEL.setText(FrameErrorTexts.getNoDobChosen());
                }
                else {
                    DOB_INFO_LABEL.setTextFill(FrameColor.getColorSucess());
                    DOB_INFO_LABEL.setText(FormatDate.formatWorkingDay(FormatDate.dateToWorkingDay(newValue)));
                }
            }
        });
    }

    private void setCheckBoxListener(){
        SHOW_REMOVED_CHECKBOX.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){

                    PERSON_LIST = getRemovedPersonList();
                    TABLE_PERSON.getSelectionModel().select(null);
                }
                else {
                    PERSON_LIST = getPersonList();
                    TABLE_PERSON.getSelectionModel().select(null);
                }
                TABLE_PERSON.setItems(PERSON_LIST);
            }
        });
    }


    //Getters TextFields

    public TextField getLAST_NAME_FIELD() {
        return LAST_NAME_FIELD;
    }

    public TextField getFIRST_NAME_FIELD() {
        return FIRST_NAME_FIELD;
    }

    public TextField getMIDDLE_NAME_FIELD() {
        return MIDDLE_NAME_FIELD;
    }

    public DatePicker getDOB_DATE_PICKER() {
        return DOB_DATE_PICKER;
    }

    public TextField getPHONE_FIELD() {
        return PHONE_FIELD;
    }

    public TextField getEMAIL_FIELD() {
        return EMAIL_FIELD;
    }


    //Abstract methods

    protected abstract void clearObjectFields();
    protected abstract void clearObjectFieldsInfo();
    protected abstract void setObjectFields(Person person);

    protected abstract void personDeselected();
    protected abstract void personSelected();

    protected abstract ObservableList<Person> getPersonList();
    protected abstract ObservableList<Person> getRemovedPersonList();

    //Buttons
    //Add button
    protected abstract void createNewObject(Person person);
    protected abstract boolean checkObjectInputFields();

}
