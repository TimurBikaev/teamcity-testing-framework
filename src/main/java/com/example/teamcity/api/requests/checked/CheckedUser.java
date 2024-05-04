package com.example.teamcity.api.requests.checked;// Пакет для выполнения проверенных операций с пользователями

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedUser;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

// Класс для выполнения проверенных операций с пользователями
public class CheckedUser extends Request implements Crudinterface {

    // Конструктор класса, принимающий спецификацию запроса
    public CheckedUser(RequestSpecification spec) {
        super(spec);
    }

    // Метод для создания пользователя
    @Override
    public User create(Object obj) {
        System.out.println("");
        System.out.println("****************************************************************");
        System.out.println("СОЗДАНИЕ ЮЗЕРА");
        // Вызов метода создания пользователя из непроверенного запроса
        return new UncheckedUser(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK) // Проверка успешного создания пользователя
                .extract().as(User.class); // Извлечение созданного пользователя
    }

    // Метод для получения пользователя по идентификатору
    @Override
    public Object get(String id) {
        return null; // Пока не реализовано
    }

    // Метод для обновления пользователя
    @Override
    public Object update(Object object) {
        return null; // Пока не реализовано
    }

    // Метод для удаления пользователя по идентификатору
    @Override
    public String delete(String id) {
        // Вызов метода удаления пользователя из непроверенного запроса
        return new UncheckedUser(spec).delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT) // Проверка успешного удаления пользователя
                .extract().asString(); // Извлечение результата удаления
    }
}
