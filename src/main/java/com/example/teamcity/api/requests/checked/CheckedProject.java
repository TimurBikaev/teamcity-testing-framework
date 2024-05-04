package com.example.teamcity.api.requests.checked;

//Здесь ассертим значения на нужные коды ответа, вызывая всю логику из непроверяемых

import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

// Класс для выполнения проверяемых операций с проектами
public class CheckedProject extends Request implements Crudinterface {

    // Конструктор класса, принимающий спецификацию запроса
    public CheckedProject(RequestSpecification spec) {
        super(spec);
    }

    // Метод для создания проекта
    @Override
    public Project create(Object obj) {
        System.out.println("");
        System.out.println("****************************************************************");
        System.out.println("СОЗДАНИЕ ПРОЕКТА");
        // Вызов метода создания проекта из непроверенного запроса
        return new UncheckedProject(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK) // Проверка успешного создания проекта
                .extract().as(Project.class); // Извлечение созданного проекта
    }

    // Метод для получения проекта по идентификатору
    @Override
    public Project get(String id) {
        // Вызов метода получения проекта из непроверенного запроса
        return new UncheckedProject(spec).get(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK) // Проверка успешного получения проекта
                .extract().as(Project.class); // Извлечение полученного проекта и преобразование в объект
    }

    // Метод для обновления проекта
    @Override
    public Object update(Object object) {
        return null; // Пока не реализовано
    }

    // Метод для удаления проекта по идентификатору
    @Override
    public String delete(String id) {
        // Вызов метода удаления проекта из непроверенного запроса
        return new UncheckedProject(spec)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK) // Проверка успешного удаления проекта
                .extract().asString(); // Извлечение результата удаления
    }
}
