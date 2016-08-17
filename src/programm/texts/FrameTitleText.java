package programm.texts;

/**
 * Created by Artem Siatchinov on 7/24/2016.
 */
public class FrameTitleText {


    private static String FRAME_TITLE = "\"ООО\" Классическая гомеопатия";
    private static String ADMIN_FRAME_TITLE = FRAME_TITLE;
    private static String DOCTOR_FRAME_TITLE = FRAME_TITLE;
    private static String PATIENT_FRAME_TITLE = FRAME_TITLE;

    public static String getFrameTitle() {
        return FRAME_TITLE;
    }

    public static String getAdminFrameTitle() {
        return ADMIN_FRAME_TITLE;
    }

    public static String getDoctorFrameTitle() {
        return DOCTOR_FRAME_TITLE;
    }

    public static String getPatientFrameTitle() {
        return PATIENT_FRAME_TITLE;
    }
}
