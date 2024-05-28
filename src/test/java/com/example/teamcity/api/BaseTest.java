package com.example.teamcity.api;

// В этом пакете содержится основной функционал для выполнения тестов в системе

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected SoftAssertions softy; // Переменная для мягких проверок, доступная в этом классе и его наследниках



    public TestDataStorage testDataStorage; // Объект для хранения тестовых данных

    // Объект для выполнения проверенных запросов от имени суперпользователя
    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().superUserSpec());

    // Объект для выполнения непроверенных запросов от имени суперпользователя
    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().superUserSpec());





    @BeforeMethod
    public void beforeTest() {
        softy = new SoftAssertions(); // Инициализация объекта для мягких проверок перед каждым тестом
        testDataStorage = TestDataStorage.getStorage();
    }

    @AfterMethod
    public void afterTest() {
        softy.assertAll(); // Проверка всех собранных ошибок после выполнения теста
        testDataStorage.delete();
    }

    // Метод для вывода сообщения на консоль
    public static void message(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
}
