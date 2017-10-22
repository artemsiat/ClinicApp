package ru.clinic.application.dao.entity.settings;

import javafx.beans.value.ObservableValue; /**
 * Product clinicApp
 * Created by artem_000 on 10/17/2017.
 */
public class Setting {
    private long id;
    private String group;
    private String code;
    private String name;
    private String defaultValue;
    private String value;
    private String type;
    private String hint;
    private boolean editable;

    public Setting() {
    }

    public Setting(String group, String code, String name, String defaultValue, String value, String type, String hint, boolean editable) {
        this.group = group;
        this.code = code;
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = value;
        this.type = type;
        this.hint = hint;
        this.editable = editable;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public void onChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Value Change from " + oldValue + " to " +  newValue);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
