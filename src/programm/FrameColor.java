package programm;

import javafx.scene.paint.Color;

/**
 * Created by Artem Siatchinov on 7/24/2016.
 */
public class FrameColor {

    private static Color COLOR_SUCESS = Color.GREEN;
    private static Color COLOR_ERROR = Color.BLUE;

    private static Color NOT_WORKING_DAY_COLOR = Color.TEAL;
    private static Color IS_WORKING_DAY_COLOR = Color.GREEN;

    private static String HEX_PAST_WORKING = "#6dc066";
    private static String HEX_PAST_NON_WORKING = "#81d8d0";
    private static String HEX_FUTURE_WORKING = "#008000";
    private static String HEX_FUTURE_NON_WORKING = "#237ace";

    public static Color getColorSucess() {
        return COLOR_SUCESS;
    }

    public static Color getColorError() {
        return COLOR_ERROR;
    }

    public static Color getIsWorkingDayColor() {
        return IS_WORKING_DAY_COLOR;
    }

    public static Color getNotWorkingDayColor() {
        return NOT_WORKING_DAY_COLOR;
    }

    public static String getHexPastWorking() {
        return HEX_PAST_WORKING;
    }

    public static String getHexPastNonWorking() {
        return HEX_PAST_NON_WORKING;
    }

    public static String getHexFutureWorking() {
        return HEX_FUTURE_WORKING;
    }

    public static String getHexFutureNonWorking() {
        return HEX_FUTURE_NON_WORKING;
    }
}
