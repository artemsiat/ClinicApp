package programm.helper_classes;

import instances.WorkingDay;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Artem Siatchinov on 8/4/2016.
 */
public class WorkingDayFormat {

    //Start time 8 00
    //End time 22 00

    //Set start time 9 00
    //Set end time 21 00

    private static double SLIDER_MAX_VALUE = 56;

    //Working hours and Lunch hours
    private static int DAY_START_TIME = 4; // 4 = one hour
    private static int DAY_END_TIME = 52; // 52 = 21 00
    private static int LUNCH_START_TIME = 16; // 16 = 12 00
    private static int LUNCH_END_TIME = 16; // 16 = 12 00



    //Get label for time

    public static String getStartSliderLabel(int value){
        int hours, minutes;

        String answer;

        hours = (value / 4 ) + 8;//56/4 = часы
        minutes = value % 4 * 15;
        if(minutes == 0){
            answer = ("с  " + hours+ ":00");
        }
        else {
            answer = ("с  " + hours + ":" + minutes);
        }
        return answer;
    }

    public static String getEndSliderLabel(int value){
        int hours, minutes;

        String answer;

        hours = (value / 4 ) + 8;//56/4 = часы
        minutes = value % 4 * 15;
        if(minutes == 0){
            answer = ("по " + hours+ ":00");
        }
        else {
            answer = ("по " + hours + ":" + minutes);
        }
        return answer;
    }

    public static String getHoursProperty(int startTime, int endTime){
        int startMinutes = startTime%100;
        int endMinutes = endTime%100;
        String startHours , endHours;
        if (startMinutes == 0){
            startHours = "с " + startTime/100 + ":00";
        }
        else{
            startHours = "с " + startTime/100 + ":" + startTime%100;
        }
        if (endMinutes == 0){
            endHours = " по " + endTime/100 + ":00";
        }
        else {
            endHours = " по " + endTime/100 + ":" + endTime%100;
        }


        return startHours + endHours;
    }

    // get Maximum slider value

    public static double getSliderMaxValue() {
        return SLIDER_MAX_VALUE;
    }

    /**
     * Converts value from the slider to the int representation of time
     * 3.8 = 4 = 1 hour = 900
     */
    public static int convertSliderValue(double value){

        // 0 is 8 00, each 4 points is one hour , one point is 15 min
        int hours = (int)value / 4 + 8;
        int minutes = ((int)value % 4) * 15;
        int returnValue = hours * 100 + minutes;

        return returnValue;
    }

    /**
     * Converts Time to Time picker
     */
    public static double convertToSliderValue(int time){

            int hours = ((time / 100) - 8) *4;
            int minutes = time % 100 / 15;

            return hours+minutes;
        }

    public static LocalDate findNextAvailableDate(ObservableList<WorkingDay> workingDayObservableList, LocalDate startDate) {

        //Create array of all wd that are after start date
        ArrayList<WorkingDay> tempWDList = new ArrayList<>();

        for (WorkingDay workingDay : workingDayObservableList){
            if (workingDay.getDate().isBefore(startDate)){
                continue;
            }
            tempWDList.add(workingDay);
        }

        LocalDate availableDate = startDate;

        for (int index = 0 ; index < 100 ; index ++){
            boolean dateTaken = false;
            for (WorkingDay workingDay : tempWDList){
                if (workingDay.getDate().equals(availableDate)){
                    dateTaken = true;
                    break;
                }
            }
            if (dateTaken){
                availableDate = availableDate.plusDays(1);
                continue;
            }
            break;
        }

        return availableDate;
    }

    /*Input is Hours from 0 to 23, Minutes from 0 to 59
    * Output is time 900, 1100 or 1145*/
    public static int getTimeFromHourAndMinutes(int startHour, int startMinutes){
        int startTime = startHour * 100 + startMinutes;
        return startTime;

    }

    /*Input is Start time 900 , 1115, 1745
    * and Length in minutes 15 minutes, 30, 45, 60 etc.
    * out put is new time with added minutes 915, 1145, 1900*/
    public static int getEndTimeFromLength(int startTime, int lengthMinutes){
        int plusHours = lengthMinutes / 60;
        int plusMinutes = lengthMinutes % 60;
        int startMinutes = startTime % 100;
        int startHours = startTime / 100;

        if (startMinutes + plusMinutes >= 60){
            plusHours += 1;
            plusMinutes = (startMinutes + plusMinutes) % 60;
        }
        int endTime = ((startHours + plusHours)*100) + plusMinutes;
        System.err.println("Start time " + startTime + " . End time " + endTime + ". WorkingDayFormat.getEndTimeFromLength()");

        return endTime;
    }

}
