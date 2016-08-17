package programm.texts;

/**
 * Created by Artem Siatchinov on 7/17/2016.
 */
public class ConnectionText {

    private static String OPEN_CONNECTION_ERROR = "Ошибка: открытие соединения с базой данных. ";
    private static String OPEN_CONNECTION_SUCCESS = "Успешно: открытие соединения с базой данных. ";

    private static String CLOSE_CONNECTION_ERROR = "Ошибка: закрытие соединения с базой данных. ";
    private static String CLOSE_CONNECTION_SUCCESS = "Успешно: закрытие соединения с базой данных. ";

    private static String CREATE_TABLE_ERROR = "Ошибка: создание новой таблицы. ";
    private static String CREATE_TABLE_SUCCESS = "Успешно: создание новой таблицы. ";

    private static String DROP_TABLE_ERROR = "Ошибка: удаление таблицы. ";
    private static String DROP_TABLE_SUCCESS = "Успешно: удаление таблицы. ";

    private static String LOAD_TABLE_ERROR = "Ошибка: загрузка таблицы. ";
    private static String LOAD_TABLE_SUCCESS = "Успешно: загрузка таблицы. ";

    private static String ADD_OBJECT_ERROR = "Успешно: добавление нового объекта в базу данных. ";
    private static String ADD_OBJECT_SUCCESS = "Ошибка: добавление нового объекта в базу данных. ";

    private static String CLOSE_STATEMENT_ERROR = "Ошибка: закрытие стэйтмента. ";
    private static String CLOSE_STATEMENT_SUCCESS = "Успешно: закрытие стэйтмента. ";

    private static String CHECK_TABLE_ERROR = "Ошибка: проверка базы данных таблицы. ";
    private static String CHECK_TABLE_SUCCESS = "Успешно: проверка таблицы. ";

    private static String CLOSE_RESULTS_SET_ERROR = "Ошибка: закрытие result set. ";
    private static String CLOSE_RESULTS_SET_SUCCESS = "Успешно: закрытие result set. ";

    private static String CHECK_RESULT_SET_SUCCESS = "Успешно: открытие result set. ";

    private static String EXECUTE_STATEMENT_SUCCESS = "Успешно: statement execution. ";
    private static String EXECUTE_STATEMENT_ERROR = "Ошибка: statement execution. ";


    //Getters
    public static String getOpenConnectionError() {
        return OPEN_CONNECTION_ERROR;
    }

    public static String getCloseConnectionError() {
        return CLOSE_CONNECTION_ERROR;
    }

    public static String getCreateTableError() {
        return CREATE_TABLE_ERROR;
    }

    public static String getCreateTableSuccess() {
        return CREATE_TABLE_SUCCESS;
    }

    public static String getDropTableError() {
        return DROP_TABLE_ERROR;
    }

    public static String getDropTableSuccess() {
        return DROP_TABLE_SUCCESS;
    }

    public static String getCloseStatementError() {
        return CLOSE_STATEMENT_ERROR;
    }

    public static String getCheckTableError() {
        return CHECK_TABLE_ERROR;
    }

    public static String getCloseResultSetError() {
        return CLOSE_RESULTS_SET_ERROR;
    }

    public static String getCheckTableSuccess() {
        return CHECK_TABLE_SUCCESS;
    }

    public static String getOpenConnectionSuccess() {
        return OPEN_CONNECTION_SUCCESS;
    }

    public static String getCheckResultSetSuccess() {
        return CHECK_RESULT_SET_SUCCESS;
    }

    public static String getCloseResultsSetError() {
        return CLOSE_RESULTS_SET_ERROR;
    }

    public static String getCloseStatementSuccess() {
        return CLOSE_STATEMENT_SUCCESS;
    }

    public static String getCloseConnectionSuccess() {
        return CLOSE_CONNECTION_SUCCESS;
    }

    public static String getCloseResultsSetSuccess() {
        return CLOSE_RESULTS_SET_SUCCESS;
    }

    public static String getAddObjectError() {
        return ADD_OBJECT_ERROR;
    }

    public static String getAddObjectSuccess() {
        return ADD_OBJECT_SUCCESS;
    }

    public static String getLoadTableError() {
        return LOAD_TABLE_ERROR;
    }

    public static String getLoadTableSuccess() {
        return LOAD_TABLE_SUCCESS;
    }

    public static String getExecuteStatementError() {
        return EXECUTE_STATEMENT_ERROR;
    }

    public static String getExecuteStatementSuccess() {
        return EXECUTE_STATEMENT_SUCCESS;
    }
}
