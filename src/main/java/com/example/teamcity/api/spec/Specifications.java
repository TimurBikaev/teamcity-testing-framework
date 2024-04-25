package com.example.teamcity.api.spec;

//Здесь Рест-Ашуред спецификации

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    private static Specifications spec;

    //Создаем синглтон - паттерн проектирования (что-то в проекте в 1 экземпляре) с приватным конструктором

    //Конструкия гарантирует, что будет спецификация в одном экз.
    private Specifications() {
    } //сделали и закрыли конструктор, больше нельзя создать новый экз
    //метод чтобы отдавать единственный созданный экземпляр
    public static Specifications getSpec() {
        if (spec == null) {
            spec = new Specifications();
        }
        return spec;
    }


    private RequestSpecBuilder reqBuilder() {
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }

    //спецификация неавторизованного юзера в виде метода, возвращающего спецификацию
    public RequestSpecification unauthSpec() {
        var requestBuilder = reqBuilder();
        return requestBuilder.build();
    }

    //спецификация Авторизованного юзера в виде метода, возвращающего спецификацию
    public RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://" + user.getUsername() + ":" + user.getPassword() + "@" + Config.getProperty("host"));

        return requestBuilder.build(); //метод возвращает построенную спецификацию
    }
}
