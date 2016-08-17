package programm.texts;

/**
 * Created by Artem Siatchinov on 8/5/2016.
 */
public class FrameTableText {

    //Person Table
    private static String FULL_NAME_COLUMN_TEXT = "Ф.И.О.";
    private static String PHONE_COLUMN_TEXT = "Телефон";
    private static String DOB_COLUMN_TEXT = "Дата рождения";
    private static String E_MAIL_COLUMN_TEXT = "эл. почта";

    //Working Day Table
    private static String DATE_COLUMN_TEXT = "Дата";
    private static String HOURS_COLUMN_TEXT = "Рабочие часы";
    private static String BREAKS_COLUMN_TEXT = "Перерыв";
    private static String NO_APPOINTMENTS_COLUMN_TEXT = "кол-во записей";

    public static String getFullNameColumnText() {
        return FULL_NAME_COLUMN_TEXT;
    }

    public static String getPhoneColumnText() {
        return PHONE_COLUMN_TEXT;
    }

    public static String getDobColumnText() {
        return DOB_COLUMN_TEXT;
    }

    public static String geteMailColumnText() {
        return E_MAIL_COLUMN_TEXT;
    }

    public static String getDateColumnText() {
        return DATE_COLUMN_TEXT;
    }

    public static String getHoursColumnText() {
        return HOURS_COLUMN_TEXT;
    }

    public static String getBreaksColumnText() {
        return BREAKS_COLUMN_TEXT;
    }

    public static String getNoAppointmentsColumnText() {
        return NO_APPOINTMENTS_COLUMN_TEXT;
    }
}
