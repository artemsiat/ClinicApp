package instances;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class Doctor extends Person{

    private ObservableList<WorkingDay> workingDays;
    private ObservableList<WorkingDay> workingDaysPast;

    public Doctor()
    {

        workingDays = FXCollections.observableArrayList();
        workingDaysPast = FXCollections.observableArrayList();

    }

    @Override public String toString()
    {
        return super.toStringMethod();
    }


    public void addWorkingDay(WorkingDay workingDay)
    {
        workingDays.add(workingDay);
    }
    public void addWorkingDayPast(WorkingDay workingDay)
    {
        workingDaysPast.add(workingDay);
    }

    public void removeWorkingDay(WorkingDay workingDay)
    {

        //If the object is not located in future working days then remove it from past working days
        if (!workingDays.remove(workingDay)) {
            if(!workingDaysPast.remove(workingDay)){
                //Problem couldn't find the working day in any of the lists
                System.out.println("ERROR - DID NOT REMOVE THE SPECIFIED WORKING DAY Doctor.removeWorkingDay() " + toString());
            }
        }
    }


    //Getters

    public ObservableList<WorkingDay> getWorkingDays()
    {
        return workingDays;
    }

    public ObservableList<WorkingDay> getWorkingDaysPast()
    {
        return workingDaysPast;
    }

    public void resetWorkingDays() {
        workingDays.clear();
        workingDaysPast.clear();
    }

    /**
     * Checks if input date is a working date for this Doctor
     * If Working day is found then working day is returned
     * else null will be returned
     */
    public WorkingDay isWorkingDay(LocalDate item) {

        if (item == null){
            return null;
        }

        for (WorkingDay workingDay : workingDays){
            if (workingDay.getDate().equals(item)){
                return workingDay;
            }
        }
        for (WorkingDay workingDay : workingDaysPast){
            if (workingDay.getDate().equals(item)){
                return workingDay;
            }
        }
        return null;
    }

    public WorkingDayHours generateWorkingDayHours(){

        //getAppoitments();
        return null;
    }

    public WorkingDay getWorkingDayByID(int id){

        Optional<WorkingDay> wd = workingDays.stream().filter(workingDay -> workingDay.getID() == id).findFirst();
        if (!wd.isPresent()){
            wd= workingDaysPast.stream().findAny().filter(workingDay -> workingDay.getID() == id);
        }
        if (wd.isPresent()){
            return wd.get();
        }
        return null;
    }

}
