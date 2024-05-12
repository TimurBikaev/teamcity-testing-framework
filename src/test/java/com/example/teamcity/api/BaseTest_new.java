package com.example.teamcity.api;

// В этом пакете содержится основной функционал для выполнения тестов в системе

import com.example.teamcity.api.models.AuthSettings;
import com.example.teamcity.api.models.Module;
import com.example.teamcity.api.requests.checked.CheckedAuthSettings;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.teamcity.api.models.Module.defaultModule;
import static com.example.teamcity.api.models.Module.httpBasic;
import static com.example.teamcity.api.models.Module.ldapModule;
import static com.example.teamcity.api.models.Module.tokenAuthModule;

public class BaseTest_new {
    protected SoftAssertions softy; // Переменная для мягких проверок, доступная в этом классе и его наследниках
    private RequestSpecification spec; // Спецификация запроса

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
    }

    @AfterMethod
    public void afterTest() {
        softy.assertAll(); // Проверка всех собранных ошибок после выполнения теста
    }

    // Метод для вывода сообщения на консоль
    public static void message(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }
}
