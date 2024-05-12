// Генератор тестовых данных
package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

public class TestDataGenerator_new {

    // Метод для генерации тестовых данных
    public static TestData generate() {
        // Генерация пользователя
        var user = User.builder()
                .username(RandomData.getStringUsername()) // Генерация имени пользователя
                .password(RandomData.getStringPassword()) // Генерация пароля
                .email(RandomData.getString() + "@gmail.com") // Генерация email
                .roles(Roles.builder()
                        .role(Arrays.asList(Role.builder()
                                .roleId("SYSTEM_ADMIN") //по умолчанию передаем роль Админа (заменить на тип enum)
                                .scope("g") //скоуп групп
                                .build())) // Генерация роли пользователя
                        .build())
                .build();

        // Генерация описания нового проекта
        var project = NewProjectDescription
                .builder()
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build()) // Генерация родительского проекта
                .name(RandomData.getStringProjectName()) // Генерация имени проекта
                .id(RandomData.getStringProjectId()) // Генерация идентификатора проекта
                .copyAllAssociatedSettings(true) // Указание копирования всех связанных настроек
                .build();

        // Генерация типа сборки
        var buildType = BuildType.builder()
                .id(RandomData.getStringBuildTypeId()) // Генерация идентификатора типа сборки
                .name(RandomData.getStringBuildTypeName()) // Генерация имени типа сборки
                .project(project) // Привязка к проекту
                .build();

        // Возврат сгенерированных данных
        return TestData.builder()
                .user(user)
                .project(project)
                .buildType(buildType)
                .build();
    }

    // Метод для генерации ролей
    public static Roles generateRoles(com.example.teamcity.api.enums.Role role, String scope) {
        return Roles.builder().role
                (Arrays.asList(Role.builder().roleId(role.getText())
                        .scope(scope).build())).build();
    }

    // Генерация случайной строки заданной длины
    public static String generateStringOfLength(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    // Генерация случайной строки заданной длины
    public static String generateStringOfLengthRange(int minLengthIncl, int maxLengthIncl) {
        return RandomStringUtils.randomAlphabetic(minLengthIncl, maxLengthIncl);
    }

    // Генерация случайной строки заданной длины
    public static String generateStringOfLengthNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }
}
