package ru.clinic.application.java.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 12/31/2016.
 */

@Configuration
@Component
@ComponentScan(basePackages = {"ru.clinic.application.java.service", "ru.clinic.application.java.fx.controllers", "ru.clinic.application.java.fx.frames", "ru.clinic.application.java.dao",
        "ru.clinic.application.java.service.dataBaseModel"})
public class AppConfig {

    public AppConfig() {
    }

}
