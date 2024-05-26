package com.example.teamcity.api;

import com.example.teamcity.api.models.AuthSettings;
import com.example.teamcity.api.models.Module;
import com.example.teamcity.api.requests.checked.CheckedAuthSettings;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.teamcity.api.models.Module.defaultModule;
import static com.example.teamcity.api.models.Module.httpBasic;
import static com.example.teamcity.api.models.Module.ldapModule;
import static com.example.teamcity.api.models.Module.tokenAuthModule;

public class BaseApiTest extends BaseTest{

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
}
