package programm.error_texts;

/**
 * Created by Artem Siatchinov on 7/25/2016.
 */
public class DataBaseErrorText {
    private static String TABLE_NOT_CREATED_ERROR = "Ошибка: таблица не создана. ";
    private static String TABLE_ALREADY_CREATED_ERROR = "Ошибка: таблица уже создана. ";

    public static String getTableNotCreatedError() {
        return TABLE_NOT_CREATED_ERROR;
    }

    public static String getTableAlreadyCreatedError() {
        return TABLE_ALREADY_CREATED_ERROR;
    }
}
