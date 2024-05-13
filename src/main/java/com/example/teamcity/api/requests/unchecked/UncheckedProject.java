package com.example.teamcity.api.requests.unchecked;

//Для проверки негативных сценариев, где приходит код ошибки вместо кода 2хх

import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

//extends Request чтобы наследовать конструктор UncheckedBuildConfig,
//имплементируем интерфейс КРУД для ОБЯЗАТЕЛЬНОГО переопределения методов внутри этого интерфейса
public class UncheckedProject extends Request implements Crudinterface {

    private static final String PROJECT_ENDPOINT = "/app/rest/projects";

    public UncheckedProject(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) { //устанавливаем метод и эндпоинт (из константы) для данной операции
        return given()
                .spec(spec)
                .body(obj)
                .post(PROJECT_ENDPOINT);
    }

    @Override
    public Response get(String id) {
        return given().spec(spec).get(PROJECT_ENDPOINT + "/id:" + id);
    }

    @Override
    public Object update(Object object) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given()
                .spec(spec) //авторизация;
                .delete(PROJECT_ENDPOINT + "/id:" + id);
    }
}
