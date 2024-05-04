package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage; // Импорт класса для хранения тестовых данных
import com.example.teamcity.api.requests.CheckedRequests; // Импорт класса для проверенных запросов
import com.example.teamcity.api.requests.UncheckedRequests; // Импорт класса для непроверенных запросов
import com.example.teamcity.api.spec.Specifications; // Импорт класса для спецификаций запросов
import org.testng.annotations.AfterMethod; // Импорт аннотации для метода, выполняемого после теста
import org.testng.annotations.BeforeMethod; // Импорт аннотации для метода, выполняемого перед тестом

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
