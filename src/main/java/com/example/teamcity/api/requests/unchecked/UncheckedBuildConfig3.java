package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

//extends Request чтобы наследовать конструктор UncheckedBuildConfig,
//а implements Crudinterface чтобы все методы КРУДа сами обязательно проставлялись
public class UncheckedBuildConfig3 extends Request implements Crudinterface {
    //private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes";
    private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes/";

    public UncheckedBuildConfig3(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return null;
    }

    @Override
    public Response get(String name) {
        return given().spec(spec).get(BUILD_CONFIG_ENDPOINT + name); //ГЕТ для доступа по имени
    }

    @Override
    public Object update(Object object) {
        return null;
    }

    @Override
    public Response delete(String name) {
        return given().spec(spec).get(BUILD_CONFIG_ENDPOINT + name); //ГЕТ для доступа по имени;
    }

}


