package ru.clinic.application.fx.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clinic.application.dao.entity.settings.Setting;
import ru.clinic.application.fx.ControllerClass;
import ru.clinic.application.fx.frames.FrameDbTables;
import ru.clinic.application.model.settings.SettingGroup;
import ru.clinic.application.service.setting.SettingsService;

import java.util.ArrayList;
import java.util.HashMap;
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

        settingsMap.forEach((key, value)->{
            GridPane gridPane = createSettingGroup(value);

            VBox vBox = new VBox();
            vBox.getChildren().add(getGroupLabel(key));
            vBox.getChildren().add(gridPane);
            vBox.setSpacing(15);
            allSettings.getChildren().add(vBox);
        });
        HBox buttons = createButtons();

        allSettings.getChildren().add(buttons);
        allSettings.setLayoutX(25);
        allSettings.setLayoutY(25);
        return allSettings;
    }

    private HBox createButtons() {
        HBox buttons = new HBox();

        Button dbTables = new Button("База данных");
        dbTables.setOnAction(event -> {frameDbTables.start(); settingsService.getDbManager();});

        buttons.getChildren().add(new Button("save"));
        buttons.getChildren().add(dbTables);
        return buttons;
    }

    private GridPane createSettingGroup(List<Setting> groupSettings) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        for (int index = 0; index < groupSettings.size(); index++) {
            Setting setting = groupSettings.get(index);
            Label settingName = new Label(setting.getName());
            Label settingHint = new Label(setting.getHint());
            TextField settingValue = new TextField();
            settingValue.setMinWidth(270);
            if (StringUtils.isBlank(setting.getValue())) {
                settingValue.setText(setting.getDefaultValue());
            } else {
                settingValue.setText(setting.getValue());
            }
            System.out.println(setting.getCode() + "  " + setting.isEditable());
            if (setting.isEditable()){
                settingValue.textProperty().addListener(setting::onChange);
            }else {
                settingValue.setEditable(setting.isEditable());
            }
            gridPane.add(settingName, 0, index);
            gridPane.add(settingValue, 1, index);
            gridPane.add(settingHint, 2, index);
        }
        return gridPane;
    }

    private Label getGroupLabel(String groupCode) {
        Label label = new Label(SettingGroup.getByCode(groupCode).getName());
        label.setStyle("-fx-font-weight: bold");
        return label;
    }
}
