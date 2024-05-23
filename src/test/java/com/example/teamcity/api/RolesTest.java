package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

// Класс для тестирования ролей
public class RolesTest extends BaseTest {

    /*
     * Этот тест проверяет, что неавторизованный пользователь не имеет права создавать проекты.
     * Он отправляет запрос на создание проекта без авторизации и проверяет, что сервер возвращает статус ответа 401 (несанкционированный доступ).
     * Затем проверяется, что авторизованный пользователь не может найти созданный проект.
     */
    @Test
    public void unauthorizedUser_ShouldNotHaveRightToCreateProject() {
        var testData = testDataStorage.addTestData(); // Добавляем тестовые данные

        // Создание проекта неавторизованным пользователем
        new UncheckedRequests(Specifications.getSpec().unauthSpec())//создаем реквест через спеку, неавториз.юзером,
                .getProjectRequest()  //реквест для проекта
                .create(testData.getProject())// создаем проект
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED); // Проверяем статус ответа

        // Проверка отсутствия созданного проекта при попытке доступа Авторизованным пользователем
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId()) //запрашиваем проект по id
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND) //проверяем статус
                //и тело ответа
                .body(Matchers.containsString(String.format("No project found by locator 'count:1,id:%s'", testData.getProject().getId())));
    }

    /*
     * Этот тест проверяет, что системный администратор имеет право создавать проекты.
     * Он создает пользователя с ролью SYSTEM_ADMIN, а затем создает проект с этим пользователем.
     * После этого проверяется, что созданный проект действительно существует.
     */
    @Test
    public void systemAdmin_ShouldHaveRightsToCreateProject() {
        var testData = testDataStorage.addTestData(); // Добавляем тестовые данные

        // Устанавливаем роль системного администратора для пользователя
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        // Создаем пользователя
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        // Создаем проект от имени пользователя с ролью SYSTEM_ADMIN
        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser())).create(testData.getProject());

        // Проверяем, что проект был успешно создан
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    /* Этот тест проверяет, что администратор проекта имеет право создавать конфигурации сборки в своем проекте.
     Он создает проект и пользователя с ролью PROJECT_ADMIN,
      а затем создает конфигурацию сборки с этим пользователем.
      После этого проверяется, что созданная конфигурация сборки действительно существует.
     */
    @Test
    public void projectAdmin_ShouldHaveRightsToCreateBuildConfigToHisProject() {
        var testData = testDataStorage.addTestData(); // Добавляем тестовые данные

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Устанавливаем роль администратора проекта для пользователя
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        // Создаем пользователя с ролью администратора проекта
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        // Создаем конфигурацию сборки для проекта с ролью администратора
        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());

        // Проверяем, что конфигурация сборки была успешно создана
        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }

    /*
    Этот тест проверяет, что администратор проекта не имеет права создавать конфигурации сборки в чужих проектах.
     Он создает два проекта и двух пользователей с ролью PROJECT_ADMIN для каждого проекта.
     Затем первый администратор пытается создать конфигурацию сборки для проекта второго администратора и проверяет,
      что сервер возвращает статус ответа 403 (запрещено).
     */
    @Test
    public void projectAdmin_ShouldNotHaveRightToCreateBuildConfigToAnotherProject() {
        var firstTestData = testDataStorage.addTestData(); // Добавляем тестовые данные для первого проекта
        var secondTestData = testDataStorage.addTestData(); // Добавляем тестовые данные для второго проекта


        // Создаем проекты
        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        // Устанавливаем роль администратора проекта для пользователей
        firstTestData.getUser().setRoles(TestDataGenerator
                .generateRoles(Role.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));
        // Создаем пользователей с ролью администратора проекта
        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        //второй юзер secondTestData.getUser() для второго проекта secondTestData.getProject()
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));
        // Создаем пользователей с ролью администратора проекта
        checkedWithSuperUser.getUserRequest().create(secondTestData.getUser());

        // Пытаемся создать конфигурацию сборки для проекта второго администратора с использованием авторизации первого администратора
        new UncheckedBuildConfig(Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType()) // Создаем конфигурацию сборки
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN); // Проверяем статус ответа
    }
}
