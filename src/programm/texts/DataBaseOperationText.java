package programm.texts;

/**
 * Created by Artem Siatchinov on 7/25/2016.
 */
public class DataBaseOperationText {

    private static String CHECK_TABLE_OPERATION = "Операция: проверка статуса таблицы. ";
    private static String CREATE_TABLE_OPERATION = "Операция: создание новой таблицы. ";
    private static String DROP_TABLE_OPERATION = "Операция: удаление существующей таблицы. ";

    private static String ADD_OBJECT_OPERATION = "Операция: добавление нового объекта в базу данных. ";
    private static String LOAD_OBJECTS_OPERATION = "Операция: загрузка базы данных. ";
    private static String REMOVE_OBJECT_OPERATION = "Операция: удаление объекта (Ни что не удаляется из БД, просто ставиться пометка что бы данный объект на загружался в список не удаленных объектов). ";
    private static String UPDATE_OBJECT_OPERATION = "Операция: обнавление объекта. ";
    private static String RESTORE_OBJECT_OPERATION = "Операция: востановление объекта. ";
    private static String COMPLETE_OBJECT_REMOVEL = "Операция: окончательное удление объекта. ";

    public static String getCheckTableOperation() {
        return CHECK_TABLE_OPERATION;
    }

    public static String getCreateTableOperation() {
        return CREATE_TABLE_OPERATION;
    }

    public static String getDropTableOperation() {
        return DROP_TABLE_OPERATION;
    }

    public static String getAddObjectOperation() {
        return ADD_OBJECT_OPERATION;
    }

    public static String getLoadObjectsOperation() {
        return LOAD_OBJECTS_OPERATION;
    }

    public static String getRemoveObjectOperation() {
        return REMOVE_OBJECT_OPERATION;
    }

    public static String getUpdateObjectOperation() {
        return UPDATE_OBJECT_OPERATION;
    }

    public static String getRestoreObjectOperation() {
        return RESTORE_OBJECT_OPERATION;
    }

    public static String getCompleteObjectRemovel() {
        return COMPLETE_OBJECT_REMOVEL;
    }
}
