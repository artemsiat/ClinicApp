package ru.clinic.application.fx.controllers;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.common.alerts.AlertType;
import ru.clinic.application.common.alerts.AppAllerts;
import ru.clinic.application.common.alerts.ConfirmationDialog;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.frames.FrameDbTables;
import ru.clinic.application.model.settings.SettingGroup;
import ru.clinic.application.model.settings.SettingValueType;
import ru.clinic.application.service.setting.SettingsService;

import java.util.List;
import java.util.Map;

/**
 * Product clinicApp
 * Created by artem_000 on 10/15/2017.
 */
@Component
public class ControllerSettings extends ControllerClass {

    private final static Logger LOGGER = LogManager.getLogger(ControllerSettings.class);

    @Autowired
    private FrameDbTables frameDbTables;

    @Autowired
    private SettingsService settingsService;

    @FXML
    private ScrollPane scrollPane;

    private Button saveButton;

    @Override
    public void startController() {
        LOGGER.debug("Starting Settings Controller");
    }

    @Override
    public void stopController() {
        LOGGER.debug("Stopping Settings Controller");
    }

    @Override
    public void postStart() {
        LOGGER.debug("Post start Settings Controller");
        Map<String, List<Setting>> settingsMap = settingsService.getSettingsMap();
        addSettingsMap(settingsMap);
    }

    private void addSettingsMap(Map<String, List<Setting>> settingsMap) {
        Pane pane = new Pane();
        VBox allSettings = createSettings(settingsMap);
        pane.getChildren().add(allSettings);
        scrollPane.setContent(pane);
    }

    private VBox createSettings(Map<String, List<Setting>> settingsMap) {
        VBox allSettings = new VBox();
        allSettings.setSpacing(40);

        settingsMap.forEach((key, value) -> {
            GridPane gridPane = createSettingGroup(value);

            VBox vBox = new VBox();
            vBox.getChildren().add(getGroupLabel(key));
            vBox.getChildren().add(gridPane);
            vBox.setSpacing(15);
            allSettings.getChildren().add(vBox);
        });
        HBox buttons = createButtons();

        allSettings.getChildren().add(buttons);

        allSettings.setPadding(new Insets(25, 25, 35, 25));

        return allSettings;
    }

    private HBox createButtons() {
        HBox buttons = new HBox();
        buttons.setSpacing(10);

        Button dbTables = new Button("База данных");
        dbTables.setOnAction(event -> {
            frameDbTables.start();
            settingsService.getDbManager();
        });

        saveButton = new Button("Сохранить изменения");
        saveButton.setDisable(true);
        saveButton.setOnAction(this::saveButtonClicked);

        buttons.getChildren().add(saveButton);
        buttons.getChildren().add(dbTables);
        return buttons;
    }

    private void saveButtonClicked(ActionEvent event) {
        LOGGER.debug("SaveButton clicked");
        //Todo alert user
        if (ConfirmationDialog.UPDATE_SETTINGS.confirm()) {
            Map<Setting, List<String>> validationResult = settingsService.validateNewValues();
            if (validationResult.isEmpty()) {
                settingsService.updateSettings();
                postStart();// Redraw frame
            } else {
                LOGGER.debug("VALIDATION ERROR ");

                String validationMsg = settingsService.generateValidationErrorMsg(validationResult);
                LOGGER.debug("validationMessage \n" + validationMsg);
                AppAllerts.informationAlert(AlertType.VALIDATION_ERROR, validationMsg);
            }
        }
    }

    private void textFieldValueChanged(ObservableValue<? extends String> observable, String oldValue, String newValue, Setting setting) {
        setting.setNewValue(newValue);

        String initialValue = setting.getValue();
        LOGGER.debug("Setting Text field value changed. initial value [{}]. changed from [{}] to [{}]", initialValue, oldValue, newValue);

        if (StringUtils.equals(StringUtils.trim(initialValue), StringUtils.trim(newValue))) {
            saveButton.setDisable(true);
        } else {
            saveButton.setDisable(false);
        }
    }

    private void checkBoxValueChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue, Setting setting) {
        setting.setNewValue(newValue ? "true" : "false");

        boolean initialValue = StringUtils.equalsIgnoreCase("true", StringUtils.trim(setting.getValue()));

        LOGGER.debug("Setting boolean value changed. initial value [{}]. changed from [{}] to [{}]", initialValue, oldValue, newValue);
        saveButton.setDisable(newValue == initialValue);
    }

    private GridPane createSettingGroup(List<Setting> groupSettings) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        for (int index = 0; index < groupSettings.size(); index++) {
            Setting setting = groupSettings.get(index);


            gridPane.add(getLable(setting.getName()), 0, index);

            if (setting.getType() == SettingValueType.BOOLEAN) {
                CheckBox checkBox = createValueCheckBox(setting);
                gridPane.add(checkBox, 1, index);
            } else {
                TextField settingValue = createValueTextField(setting);
                gridPane.add(settingValue, 1, index);
            }

            gridPane.add(getLable(setting.getHint()), 2, index);
        }
        return gridPane;
    }

    private CheckBox createValueCheckBox(Setting setting) {
        CheckBox checkBox = new CheckBox();
        if (StringUtils.equalsIgnoreCase("true", StringUtils.trim(setting.getValue()))) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            checkBoxValueChanged(observable, oldValue, newValue, setting);
        });
        return checkBox;
    }

    private TextField createValueTextField(Setting setting) {
        TextField settingValue = new TextField();
        settingValue.setStyle("-fx-font-size: 10pt;");
        settingValue.setMinWidth(270);

        settingValue.setText(setting.getValue());

        if (setting.isEditable()) {
            settingValue.textProperty().addListener(setting::onChange);
        } else {
            settingValue.setEditable(setting.isEditable());
        }
        settingValue.textProperty().addListener((observable, oldValue, newValue) -> {
            textFieldValueChanged(observable, oldValue, newValue, setting);
        });

        return settingValue;
    }

    private Label getLable(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 10pt;");
        return label;
    }

    private Label getGroupLabel(String groupCode) {
        Label label = new Label(SettingGroup.getByCode(groupCode).getName());
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 12pt;");
        return label;
    }
}
