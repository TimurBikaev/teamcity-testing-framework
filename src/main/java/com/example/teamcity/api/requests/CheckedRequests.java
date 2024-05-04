package com.example.teamcity.api.requests;

import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

// Класс для выполнения проверенных запросов
@Getter
public class CheckedRequests {
    private CheckedUser userRequest; // Объект для выполнения проверенных операций с пользователями
    private CheckedProject projectRequest; // Объект для выполнения проверенных операций с проектами
    private CheckedBuildConfig buildConfigRequest; // Объект для выполнения проверенных операций с конфигурациями сборки

    // Конструктор класса, принимающий спецификацию запроса
    public CheckedRequests(RequestSpecification spec) {
        this.userRequest = new CheckedUser(spec); // Создание объекта для выполнения операций с пользователями
        this.buildConfigRequest = new CheckedBuildConfig(spec); // Создание объекта для выполнения операций с конфигурациями сборки
        this.projectRequest  = new CheckedProject(spec); // Создание объекта для выполнения операций с проектами
    }
}
