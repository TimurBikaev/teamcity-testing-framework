package com.example.teamcity.api.requests;

import io.restassured.specification.RequestSpecification;

// Базовый класс для выполнения запросов
public class Request {
    protected RequestSpecification spec; // Спецификация запроса

    // Конструктор класса, принимающий спецификацию запроса
    public Request(RequestSpecification spec) {
        this.spec = spec; // Инициализация спецификации запроса
    }
}
