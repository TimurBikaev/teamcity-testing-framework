package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.AuthSettings;
import com.example.teamcity.api.requests.Crudinterface;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;


// Реализация интерфейса CRUD для работы с настройками аутентификации (AuthSettings)
public class CheckedAuthSettings implements Crudinterface {

    private static final String AUTH_SETTINGS_ENDPOINT = "/app/rest/server/authSettings"; // Конечная точка для настроек аутентификации
    private final RequestSpecification spec; // Спецификация запроса

    // Конструктор класса, принимающий спецификацию запроса
    public CheckedAuthSettings(RequestSpecification spec) {
        this.spec = spec; // Инициализация спецификации
    }

    // Метод для создания объекта AuthSettings
    @Override
    public Object create(Object obj) {
        // Необходимо реализовать создание AuthSettings
        return null;
    }

    // Метод для получения объекта AuthSettings по его идентификатору
    @Override
    public Object get(String id) {
        // Необходимо реализовать получение AuthSettings
        return null;
    }

    // Метод для обновления объекта AuthSettings
    @Override
    public Object update(Object object) {
        // Приведение объекта к типу AuthSettings
        AuthSettings authSettings = (AuthSettings) object;

        // Выполнение PUT-запроса для обновления настроек аутентификации
        given()
                .spec(spec)
                .body(authSettings)
                .put(AUTH_SETTINGS_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.SC_OK); // Проверка успешного выполнения запроса

        return authSettings; // Возвращаем обновленные настройки аутентификации
    }

    // Метод для удаления объекта AuthSettings по его идентификатору
    @Override
    public Object delete(String id) {
        // Необходимо реализовать удаление AuthSettings
        return null;
    }
}