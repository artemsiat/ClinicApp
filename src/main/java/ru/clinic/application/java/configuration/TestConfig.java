package ru.clinic.application.java.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by Artem Siatchinov on 1/7/2017.
 */

@Configuration
@Component
@ComponentScan(basePackages = {"ru.clinic.application.test"})
public class TestConfig {

}
