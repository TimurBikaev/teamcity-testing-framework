package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedUser extends Request implements Crudinterface {

    private final static String USER_ENDPOINT = "/app/rest/users";

    // Конструктор класса, принимающий спецификацию запроса
    public UncheckedUser(RequestSpecification spec) {
        super(spec);
    } //переиспользование конструктора-родителя Request
    //поэтому super


    @Override
    public Response create(Object obj) {
        return given()
                .spec(spec)
                .body(obj)
                .post(USER_ENDPOINT);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(Object object) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given().spec(spec).delete(USER_ENDPOINT + "/username:" + id);
    }

}
