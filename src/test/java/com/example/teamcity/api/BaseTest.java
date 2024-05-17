package com.example.teamcity.api;

// В этом пакете содержится основной функционал для выполнения тестов в системе

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.AuthSettings; // Импортируем модель AuthSettings
import com.example.teamcity.api.models.Module; // Импортируем модель Module
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedAuthSettings; // Импортируем класс CheckedAuthSettings для проверки настроек аутентификации
import com.example.teamcity.api.spec.Specifications; // Импортируем класс Specifications для спецификаций запросов
import io.restassured.specification.RequestSpecification; // Импортируем класс RequestSpecification для создания спецификации запроса
import org.assertj.core.api.SoftAssertions; // Импортируем класс SoftAssertions для мягких проверок
import org.testng.annotations.AfterMethod; // Импортируем аннотацию AfterMethod для выполнения метода после каждого теста
import org.testng.annotations.BeforeMethod; // Импортируем аннотацию BeforeMethod для выполнения метода перед каждым тестом
import org.testng.annotations.BeforeSuite; // Импортируем аннотацию BeforeSuite для выполнения метода перед всеми тестами в пакете

import java.util.Arrays; // Импортируем класс Arrays для работы с массивами
import java.util.HashMap; // Импортируем класс HashMap для работы с хэш-картой
import java.util.List; // Импортируем класс List для работы со списками
import java.util.Map; // Импортируем класс Map для работы с отображениями

import static com.example.teamcity.api.models.Module.defaultModule; // Импортируем метод defaultModule из класса Module
import static com.example.teamcity.api.models.Module.httpBasic; // Импортируем метод httpBasic из класса Module
import static com.example.teamcity.api.models.Module.ldapModule; // Импортируем метод ldapModule из класса Module
import static com.example.teamcity.api.models.Module.tokenAuthModule; // Импортируем метод tokenAuthModule из класса Module

public class BaseTest {
    protected SoftAssertions softy; // Переменная для мягких проверок, доступная в этом классе и его наследниках
    private RequestSpecification spec; // Спецификация запроса


    public TestDataStorage testDataStorage; // Объект для хранения тестовых данных

    // Объект для выполнения проверенных запросов от имени суперпользователя
    public CheckedRequests checkedWithSuperUser
            = new CheckedRequests(Specifications.getSpec().superUserSpec());

    // Объект для выполнения непроверенных запросов от имени суперпользователя
    public UncheckedRequests uncheckedWithSuperUser
            = new UncheckedRequests(Specifications.getSpec().superUserSpec());


//    // Метод для настройки теста перед выполнением
//    @BeforeMethod
//    public void setupTest() {
//        testDataStorage = TestDataStorage.getStorage(); // Инициализация объекта для хранения тестовых данных
//    }
//
//    // Метод для очистки данных после выполнения теста
//    @AfterMethod
//    public void cleanTest() {
//        testDataStorage.delete(); // Очистка данных
//    }



    @BeforeSuite
    public void setup() {
        message("Включение \"perProjectPermissions\": true");
        // Создание спецификации запроса для администратора
        spec = Specifications.getSpec().superUserSpec();

        // Настройка модулей для аутентификации
        Map<String, List<Module>> modules = new HashMap<>() {
            {
                put("module", Arrays.asList(
                        httpBasic(), defaultModule(), tokenAuthModule(), ldapModule())
                );
            }
        };

        // Создание объекта AuthSettings с указанными настройками аутентификации
        AuthSettings authSettings = new AuthSettings(true, modules);

        // Обновление настроек аутентификации с помощью CheckedAuthSettings
        new CheckedAuthSettings(spec).update(authSettings);
    }


    @BeforeMethod
    public void beforeTest() {
        softy = new SoftAssertions(); // Инициализация объекта для мягких проверок перед каждым тестом
        testDataStorage = TestDataStorage.getStorage();
    }

    @AfterMethod
    public void afterTest() {
        softy.assertAll(); // Проверка всех собранных ошибок после выполнения теста
      //  testDataStorage.delete();
    }

    // Метод для вывода сообщения на консоль
    public static void message(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
}
