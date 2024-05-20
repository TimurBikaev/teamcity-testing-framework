package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

    //extends Request чтобы наследовать конструктор UncheckedBuildConfig,
    //а implements Crudinterface чтобы все методы КРУДа сами обязательно проставлялись
public class UncheckedBuildConfig extends Request implements Crudinterface {
    private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes";

    public UncheckedBuildConfig(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return given().spec(spec).body(obj).post(BUILD_CONFIG_ENDPOINT);
    }

    @Override
    public Response get(String id) {
        return given().spec(spec).get(BUILD_CONFIG_ENDPOINT + "/id:" + id);//ГЕТ для доступа по ID
    }

    @Override
    public Object update(Object object) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given().spec(spec).delete(BUILD_CONFIG_ENDPOINT + "/id:" + id);
    }
}


