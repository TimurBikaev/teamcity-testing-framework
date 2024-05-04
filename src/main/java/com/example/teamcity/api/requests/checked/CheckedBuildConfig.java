package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

// Класс для выполнения проверяемых операций с конфигурациями сборки
public class CheckedBuildConfig extends Request implements Crudinterface {

    // Конструктор класса, принимающий спецификацию запроса
    public CheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    // Метод для создания конфигурации сборки
    @Override
    public BuildType create(Object obj) {
        System.out.println("");
        System.out.println("****************************************************************");
        System.out.println("СОЗДАНИЕ BuildType");
        // Вызов метода создания конфигурации сборки из непроверенного запроса
        return new UncheckedBuildConfig(spec).create(obj)
                // Проверка успешного создания конфигурации сборки
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class); // Извлечение созданной конфигурации сборки
    }

    // Метод для получения объекта по идентификатору
    @Override
    public Object get(String id) {
        return null; // Пока не реализовано
    }

    // Метод для обновления объекта
    @Override
    public Object update(Object object) {
        return null; // Пока не реализовано
    }

    // Метод для удаления объекта по идентификатору
    @Override
    public String delete(String id) {
        // Вызов метода удаления конфигурации сборки из непроверенного запроса
        return new UncheckedBuildConfig(spec).delete(id)
                // Проверка успешного удаления конфигурации сборки
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString(); // Извлечение результата удаления
    }
}
