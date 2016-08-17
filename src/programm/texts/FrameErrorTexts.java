package programm.texts;

import programm.programm_settings.WorkingDaySettings;

/**
 * Created by Artem Siatchinov on 8/3/2016.
 */
public class FrameErrorTexts {

    //Admin Frame

    private static String PASSWORD_MISMATCH_ERROR = "пароли не совпадают";
    private static String NO_USER_NAME_ERROR = "логин не заполнин";
    private static String NO_DOB_CHOSEN = "Дата не выбрана";
    private static String USER_NAME_TAKEN_ERROR = "такой логин используется";

    //Doctor Frame
    //Working Day
    private static String NO_DOCTOR_CHOSEN_WD_PICKER = "Доктор не выбран";
    private static String CHOSEN_DATE_TOO_OLD = "Выбранный день превышает " + WorkingDaySettings.getMonthsFromToday() + " месяца давности";


    public static String getPasswordMismatchError() {
        return PASSWORD_MISMATCH_ERROR;
    }

    public static String getNoUserNameError() {
        return NO_USER_NAME_ERROR;
    }

    public static String getNoDobChosen() {
        return NO_DOB_CHOSEN;
    }

    public static String getUserNameTakenError() {
        return USER_NAME_TAKEN_ERROR;
    }

    public static String getNoDoctorChosenWdPicker() {
        return NO_DOCTOR_CHOSEN_WD_PICKER;
    }

    public static String getChosenDateTooOld() {
        return CHOSEN_DATE_TOO_OLD;
    }
}
