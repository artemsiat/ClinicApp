package programm.texts;

/**
 * Created by Artem Siatchinov on 8/11/2016.
 */
public class FrameAppointmentText {

    private static String CHOOSE_LENGTH_TEXT = "Выберите продолжительность";
    private static String CHOOSE_DOCTOR_TEXT = "Выберите врача";
    private static String CHOOSE_PATIENT_TEXT = "Выберите пациента";

    private static String CHOSEN_PATIENT_TEXT = "Выбранный пациент";
    private static String NO_PATIENT_CHOSEN_TEXT = "Выберите пациента";

    private static String NO_DOCTOR_CHOSEN_TEXT = "Выберите врача";

    private static String APP_DATE_PICKER_LABEL = "День приёма";
    private static String APP_START_TIME_LABEL = "Начало приёма";
    private static String APP_LENGTH_LABEL = "Продолжительность";

    private static String HOURS_COMBO_BOX_TEXT = "часы";
    private static String MINUTES_COMBO_BOX_TEXT = "минуты";

    private static String _15_MINUTES = "15 минут";
    private static String _30_MINUTES = "30 минут";
    private static String _45_MINUTES = "45 минут";
    private static String _60_MINUTES = "1 час";
    private static String _1_15_MINUTES = "1 час 15 минут";
    private static String _1_30_MINUTES = "1 час 30 минут";
    private static String _1_45_MINUTES = "1 час 45 минут";
    private static String _120_MINUTES = "2 часа";

    //Length Options

    public static String getChooseLengthText() {
        return CHOOSE_LENGTH_TEXT;
    }

    public static String getHoursComboBoxText() {
        return HOURS_COMBO_BOX_TEXT;
    }

    public static String getMinutesComboBoxText() {
        return MINUTES_COMBO_BOX_TEXT;
    }

    public static String get_15Minutes() {
        return _15_MINUTES;
    }

    public static String get_30Minutes() {
        return _30_MINUTES;
    }

    public static String get_45Minutes() {
        return _45_MINUTES;
    }

    public static String get_60Minutes() {
        return _60_MINUTES;
    }

    public static String get_115Minutes() {
        return _1_15_MINUTES;
    }

    public static String get_130Minutes() {
        return _1_30_MINUTES;
    }

    public static String get_145Minutes() {
        return _1_45_MINUTES;
    }

    public static String get_120Minutes() {
        return _120_MINUTES;
    }

    public static String getChooseDoctorText() {
        return CHOOSE_DOCTOR_TEXT;
    }

    public static String getChoosePatientText() {
        return CHOOSE_PATIENT_TEXT;
    }

    public static String getChosenPatientText() {
        return CHOSEN_PATIENT_TEXT;
    }

    public static String getNoPatientChosenText() {
        return NO_PATIENT_CHOSEN_TEXT;
    }

    public static String getNoDoctorChosenText() {
        return NO_DOCTOR_CHOSEN_TEXT;
    }

    public static String getAppDatePickerLabel() {
        return APP_DATE_PICKER_LABEL;
    }

    public static String getAppLengthLabel() {
        return APP_LENGTH_LABEL;
    }

    public static String getAppStartTimeLabel() {
        return APP_START_TIME_LABEL;
    }
}
