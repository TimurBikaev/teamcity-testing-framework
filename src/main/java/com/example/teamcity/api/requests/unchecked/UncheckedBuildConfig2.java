package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.Crudinterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

//extends Request чтобы наследовать конструктор UncheckedBuildConfig,
//а implements Crudinterface чтобы все методы КРУДа сами обязательно проставлялись
public class UncheckedBuildConfig2 extends Request implements Crudinterface {
    //private static final String BUILD_CONFIG_ENDPOINT = "/app/rest/buildTypes";
    private static final String BUILD_CONFIG_CHECK_ENDPOINT = "/buildConfiguration/";

    public UncheckedBuildConfig2(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return null;
    }

    @Override
    public Response get(String projectId) {
        return given().spec(spec).get(BUILD_CONFIG_CHECK_ENDPOINT);
    }

    @Override
    public Object update(Object object) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return null;
    }
}


