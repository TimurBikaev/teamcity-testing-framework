package com.example.teamcity.api.requests.checked;

//GET для получения токена, живущий вместе с юзером

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;

// Класс для выполнения GET-запроса для получения токена аутентификации
public class AuthRequest_new {

    private User user; // Пользователь, для которого выполняется запрос

    // Конструктор класса, принимающий пользователя в качестве аргумента
    public AuthRequest_new(User user) {
        this.user = user; // Устанавливаем переданного пользователя в поле класса
    }

    // Метод для получения CSRF-токена
    public String getCsrfToken() {
        // Выполняем GET-запрос для получения CSRF-токена
        return RestAssured
                .given() // Начинаем настройку запроса
                .spec(Specifications.getSpec().authSpec(user)) // Устанавливаем спецификацию запроса с авторизацией
                .get("/authenticationTest.html?csrf") // Выполняем GET-запрос к странице для получения CSRF-токена
                .then().assertThat().statusCode(HttpStatus.SC_OK) // Проверяем, что код ответа равен 200
                .extract().asString(); // Извлекаем строковое значение токена из ответа
    }
}
