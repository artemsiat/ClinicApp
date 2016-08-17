package programm.programm_settings;

/**
 * Created by Artem Siatchinov on 8/6/2016.
 */
public class WorkingDaySettings {

    private static boolean IS_LOAD_ALL_WD = false; // tells the Working day  data base if it should load all the dates
    private static int MONTHS_FROM_TODAY = 2;

    //Start and end time of the sliders(working day and lunch) in Working day frame
    private static int DAY_START_TIME = 4; // 4 = one hour
    private static int DAY_END_TIME = 52; // 52 = 21 00
    private static int LUNCH_START_TIME = 16; // 16 = 12 00
    private static int LUNCH_END_TIME = 16; // 16 = 12 00

    public static boolean isLoadAllWd() {
        return IS_LOAD_ALL_WD;
    }

    public static int getMonthsFromToday() {
        return MONTHS_FROM_TODAY;
    }


    public static int getDayStartTime() {
        return DAY_START_TIME;
    }

    public static int getDayEndTime() {
        return DAY_END_TIME;
    }

    public static int getLunchStartTime() {
        return LUNCH_START_TIME;
    }

    public static int getLunchEndTime() {
        return LUNCH_END_TIME;
    }
}
