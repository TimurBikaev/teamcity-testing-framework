package com.example.teamcity.api;

//Первый апи-тест, настедуется от базового апи-класса,
// а тот наследуется от общего базового класса

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest{
    @Test //чтобы метод стал исполняемым для TestNG
    public void buildConfigurationTest(){

        //cоздаем юзера с помощью билдера по спецификации
        var user =  User.builder()
                .username("admin")
                .password("admin")
                .build();

        //используем RestAssured
        //Запрашиваем токен под нашими кредами и сохраням в переменную
        var token = RestAssured
                .given()
                .spec(Specifications.getSpec().authSpec(user))
                .get("/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                        .extract().asString();

        System.out.println(token);



    }
}
