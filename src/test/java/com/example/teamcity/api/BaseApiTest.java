package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

// Базовый класс для API-тестов, наследующий базовый класс для всех тестов
public class BaseApiTest extends BaseTest{

    public TestDataStorage testDataStorage; // Объект для хранения тестовых данных

    // Объект для выполнения проверенных запросов от имени суперпользователя
    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().superUserSpec());

    // Объект для выполнения непроверенных запросов от имени суперпользователя
    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().superUserSpec());


    // Метод для настройки теста перед выполнением
    @BeforeMethod
    public void setupTest() {

        testDataStorage = TestDataStorage.getStorage(); // Инициализация объекта для хранения тестовых данных
    }

    // Метод для очистки данных после выполнения теста
    @AfterMethod
    public void cleanTest() {
        testDataStorage.delete(); // Очистка данных
    }


}
