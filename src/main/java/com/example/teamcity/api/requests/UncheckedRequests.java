package com.example.teamcity.api.requests;

import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedUser;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

// Класс для доступа к непроверенным запросам
@Getter
public class UncheckedRequests {
    private UncheckedUser userRequest; // Запросы для пользователей
    private UncheckedProject projectRequest; // Запросы для проектов
    private UncheckedBuildConfig buildConfigRequest; // Запросы для конфигураций сборки

    // Конструктор класса, принимающий спецификацию запроса
    public UncheckedRequests(RequestSpecification spec) {
        // Инициализация объектов запросов для пользователей, проектов и конфигураций сборки
        this.userRequest = new UncheckedUser(spec); // Создание запросов для пользователей
        this.buildConfigRequest = new UncheckedBuildConfig(spec); // Создание запросов для конфигураций сборки
        this.projectRequest = new UncheckedProject(spec); // Создание запросов для проектов
    }
}
