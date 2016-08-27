package instances;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programm.helper_classes.FormatDate;
import programm.helper_classes.WorkingDayFormat;
import programm.texts.FrameLabelText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class WorkingDay extends DateInstance implements Comparable<WorkingDay>{

    private int doctorID;
    private int lunchStart;
    private int lunchEnd;

    private ObservableList<Appointment> appointmentObservableList;

    //Properties Day , Working hours, break , no of appointments
    private SimpleStringProperty workingDayProperty;
    private SimpleStringProperty workingHoursProperty;
    private SimpleStringProperty lunchProperty;
    private SimpleStringProperty noOfAppointments;

    public WorkingDay(){

        //Initialize properties
        workingDayProperty = new SimpleStringProperty();
        workingHoursProperty = new SimpleStringProperty();
        lunchProperty = new SimpleStringProperty();
        noOfAppointments = new SimpleStringProperty();

        appointmentObservableList = FXCollections.observableArrayList();

    }

    @Override
    public String toString() {
        return "Рабочий день: " + super.toString() +
                ". Doctor ID: " + doctorID +
                ". Lunch Start Time: " + lunchStart +
                ". Lunch End Time: " + lunchEnd;
    }

    @Override
    public void generateProperties() {
        setWorkingDayProperty(FormatDate.convertDateToMyFormat(super.getDate()));
        setWorkingHoursProperty(WorkingDayFormat.getHoursProperty(super.getStartTime(),super.getEndTime()));
        if (lunchEnd == lunchStart){
            setLunchProperty(FrameLabelText.getNoLunchLabel());
        }
        else {
            setLunchProperty(WorkingDayFormat.getHoursProperty(lunchStart,lunchEnd));
        }
        //todo to implement
        setNoOfAppointments("not implemented");
    }

    @Override
    public int compareTo(WorkingDay otherWD) {
        if (this.getDate().isBefore(otherWD.getDate())){
            return -1;
        }
        else {
            return 1;
        }
    }




    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(int lunchStart) {
        this.lunchStart = lunchStart;
    }

    public int getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(int lunchEnd) {
        this.lunchEnd = lunchEnd;
    }


    //Properties


    private void setWorkingDayProperty(String workingDayProperty) {
        this.workingDayProperty.set(workingDayProperty);
    }

    private void setWorkingHoursProperty(String workingHoursProperty) {
        this.workingHoursProperty.set(workingHoursProperty);
    }

    private void setLunchProperty(String lunchProperty) {
        this.lunchProperty.set(lunchProperty);
    }

    private void setNoOfAppointments(String noOfAppointments) {
        this.noOfAppointments.set(noOfAppointments);
    }

    public String getWorkingDayProperty() {
        return workingDayProperty.get();
    }

    public String getWorkingHoursProperty() {
        return workingHoursProperty.get();
    }

    public String getLunchProperty() {
        return lunchProperty.get();
    }

    public String getNoOfAppointments() {
        return noOfAppointments.get();
    }


    public void setAppointments(ObservableList<Appointment> appointments) {
        appointmentObservableList.clear();
        appointmentObservableList.setAll(appointments);
    }

    public ObservableList<Appointment> getAppointmentObservableList() {
        return appointmentObservableList;
    }

    public HashMap<Integer, ArrayList<Integer>> getAvailableTime() {

        HashMap<Integer, ArrayList<Integer>> dayTimeMap = createTimeMap(getStartTime(), getEndTime());
        //Create Map lunch and add it to the list
        if (lunchStart != lunchEnd){
            processTimeMap(dayTimeMap, createTimeMap(lunchStart, lunchEnd));
        }
        /*and the same thing for each appointment*/
        for (Appointment appointment : appointmentObservableList){
            //update the working hours and minutes map (remove lunch and appointment time)
            processTimeMap(dayTimeMap, createTimeMap(appointment.getStartTime(), appointment.getEndTime()));
        }

        return dayTimeMap;
    }

    private HashMap<Integer, ArrayList<Integer>> createTimeMap(int startTime, int endTime) {

        int intervalStart = startTime/100;
        int intervalEnd = endTime/100;

        //HashMap of available times , first Integer is hour , second is minutes
        //{9=[0, 15, 30, 45], 10=[0, 15, 30, 45], 11=[0, 15, 30, 45], 12=[0, 15, 30, 45]}
        HashMap<Integer, ArrayList<Integer>> timeMap = new HashMap<>();
        for (int hour = intervalStart ; hour <= intervalEnd ; hour ++){
            timeMap.put(hour, new ArrayList<>(Arrays.asList(0,15,30,45)));
        }
        //Remove start time and end time minutes
        //Minutes of start hour
        int dayStartMinutes = startTime%100;
        int dayEndMinutes = endTime%100;

        if (dayStartMinutes == 15){
            timeMap.put(intervalStart, new ArrayList<>(Arrays.asList(15,30,45)));
        }
        else if (dayStartMinutes == 30){
            timeMap.put(intervalStart, new ArrayList<>(Arrays.asList(30,45)));
        }
        else if (dayStartMinutes == 45){
            timeMap.put(intervalStart, new ArrayList<>(Collections.singletonList(45)));
        }


        if (dayEndMinutes == 0 ){
            timeMap.put(intervalEnd, new ArrayList<>(Collections.emptyList()));
        }
        else if (dayEndMinutes == 15 ){
            timeMap.put(intervalEnd, new ArrayList<>(Collections.singletonList(0)));
        }
        else if (dayEndMinutes == 30 ){
            timeMap.put(intervalEnd, new ArrayList<>(Arrays.asList(0,15)));
        }
        else if (dayEndMinutes == 45 ){
            timeMap.put(intervalEnd, new ArrayList<>(Arrays.asList(0,15,30)));
        }

        return timeMap;
    }

    private void processTimeMap(HashMap<Integer, ArrayList<Integer>> dayTimeMap, HashMap<Integer, ArrayList<Integer>> appointmentsTimeMapList) {

        /*Getting map of available minutes for each available hour and the same map for appointment*/
        /*For each entry in appointment map*/
        for (HashMap.Entry<Integer, ArrayList<Integer>> entry : appointmentsTimeMapList.entrySet()){

            /*Appointment variables*/
            Integer key = entry.getKey();
            ArrayList<Integer> values = entry.getValue();
            /*New list for day Map*/
            ArrayList<Integer> newValues = new ArrayList<>();
            for (Integer minutes : dayTimeMap.get(key)){
                if (values.contains(minutes)){
                    continue;
                }
                newValues.add(minutes);
            }
            dayTimeMap.put(key, newValues);
        }
    }

    public ArrayList<Integer> getAppointmentLengths(int startHour, int startMinutes){
        ArrayList<Integer> lengths = new ArrayList<>();




        return lengths;
    }


}
