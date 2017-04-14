package ru.clinic.application.java.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;
import ru.clinic.application.java.dao.entity.Appointment;

import java.time.LocalDate;

/**
 * Created by Artem Siatchinov on 1/20/2017.
 */
@Component
public class AppointmentService {

    public ObservableList<Appointment> getAppsByWd(LocalDate workingDay){
        //Todo check that there are no appointments for that day
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //appointments.add(new Appointment());
        return appointments;
    }

/*    public HashMap<Integer, ArrayList<Integer>> getAvailableTime() {

        HashMap<Integer, ArrayList<Integer>> dayTimeMap = createTimeMap(getStartTime(), getEndTime());
        //Create Map lunch and add it to the list
        if (lunchStart != lunchEnd){
            processTimeMap(dayTimeMap, createTimeMap(lunchStart, lunchEnd));
        }
        *//*and the same thing for each appointment*//*
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

        *//*Getting map of available minutes for each available hour and the same map for appointment*//*
        *//*For each entry in appointment map*//*
        for (HashMap.Entry<Integer, ArrayList<Integer>> entry : appointmentsTimeMapList.entrySet()){

            *//*Appointment variables*//*
            Integer key = entry.getKey();
            ArrayList<Integer> values = entry.getValue();
            *//*New list for day Map*//*
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
        HashMap<Integer, ArrayList<Integer>> timeMap = getAvailableTime();

        int length = 0;
        lengths.add(length);

        while(isLengthValid(timeMap, startHour, startMinutes)){
            if (startMinutes == 45){ startMinutes = 0; startHour +=1; }
            else {startMinutes += 15;}
            length += 15;
            lengths.add(length);
        }

        //System.err.println("Time map: " + timeMap);
        //System.err.println("Available lengths: " + lengths);
        return lengths;
    }
    private boolean isLengthValid(HashMap<Integer, ArrayList<Integer>> timeMap, int startHour, int startMinutes){

        if (startMinutes == 45){ startMinutes = 0; startHour +=1; }
        else {startMinutes += 15;}

        ArrayList<Integer> minutes = timeMap.get(startHour);

        if (minutes == null){return false;}
        if (minutes.contains(startMinutes)){return true;}
        else if (minutes.isEmpty() && startMinutes == 0){return true;}

        return false;
    }*/
}
