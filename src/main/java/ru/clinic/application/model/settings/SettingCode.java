package ru.clinic.application.model.settings;

/**
 * Product clinicApp
 * Created by artem_000 on 10/28/2017.
 */
public enum SettingCode {


    MAIL_HOST("mail.host", "mail host"),
    MAIL_PORT("mail.port", "mail port"),
    MAIL_USERNAME("mail.username", "mail username"),
    MAIL_PASSWORD("mail.password", "mail password"),
    MAIL_PROPERTIES_MAIL_SMTP_AUTH("mail.properties.mail.smtp.auth", "mail smtp auth"),

    BACKUP_RECIPIENTS("backup.recipients", "backup recipients"),
    BACKUP_DATABASE_DIR("database.backup.dir", "database backup dir"),
    BACKUP_DATABASE_FREQUENCY_MINUTES("database.backup.frequency.minutes", "database.backup.frequency.minutes"),
    BACKUP_DATABASE_KEEP_DURATION("backup.db.keep.duration", "back up database max keep duration"),
    BACKUP_DATABASE_ON_EXIT("backup.database.on.exit", "Создавать резервную копию базы данных перед выходом"),
    BACKUP_LOGS_KEEP_DURATION("backup.logs.keep.duration", "Максимальная давность логирования"),

    MAX_PATIENT_LOAD_COUNT("max.patient.load.count", "Максимальная выгрузка пациентов"),
    COMPANY_NAME("company.name", "Наименование компании"),
    ;

    private String code;
    private String name;

    SettingCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
