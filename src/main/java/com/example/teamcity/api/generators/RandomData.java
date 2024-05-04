package com.example.teamcity.api.generators;

//Дата провайдер

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {

    private static final int LENGTH = 5; // Длина случайных строк

    // Метод для генерации случайной строки
    public static String getString() {
        return "test_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного имени пользователя
    public static String getStringUsername() {
        return "username_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного пароля
    public static String getStringPassword() {
        return "password_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного имени проекта
    public static String getStringProjectName() {
        return "project_name_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного имени типа сборки
    public static String getStringBuildTypeName() {
        return "build_type_name_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного идентификатора проекта
    public static String getStringProjectId() {
        return "project_id_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    // Метод для генерации случайного идентификатора типа сборки
    public static String getStringBuildTypeId() {
        return "build_type_id_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }
}