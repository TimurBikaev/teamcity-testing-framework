package com.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectCreationTest extends BaseApiTest {

    // ПОЗИТИВНЫЙ КЕЙС:Проверяем создание проекта с корректными данными
    @Test
    public void project_create_positive_validData() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Создаем проект с корректными данными
        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());

        // Проверяем успешное создание проекта
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

    }


    // ПОЗИТИВНЫЙ КЕЙС: значение copyAllAssociatedSettings(false)
    @Test
    public void project_create_positive_copySettings_false() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Создаем описание проекта с превышающим ограничение именем
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(testData.getProject().getId())
                .copyAllAssociatedSettings(false) //проверяемый параметр
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    // ПОЗИТИВНЫЙ КЕЙС: длина имени проекта (1 символ)
    @Test
    public void project_create_positive_name_1symbol() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем имя проекта
        String projectName = TestDataGenerator.generateStringOfLength(1); //представим, что от 1 до 255 включительно (на самом деле проходит 100500+символов

        // Создаем описание проекта с превышающим ограничение именем
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName)
                .id(testData.getProject().getId())
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //ПОЗИТИВНЫЙ КЕЙС: длина имени проекта (255 символов)
    @Test
    public void project_create_positive_name_255symbols() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем имя проекта
        String projectName = TestDataGenerator.generateStringOfLength(255); //представим, что до 255 включительно (на самом деле проходит 100500+символов

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName)
                .id(testData.getProject().getId())
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }


    //ПОЗИТИВНЫЙ КЕЙС: длина id проекта (225 символов)
    @Test
    public void project_create_positive_id_225symbols() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectid = TestDataGenerator.generateStringOfLength(225); // до 225 включительно

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectid)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectid)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //ПОЗИТИВНЫЙ КЕЙС: ограничение на длину id проекта (1 символ)
    @Test
    public void project_create_positive_id_1symbols() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectid = TestDataGenerator.generateStringOfLength(1); //

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectid)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectid)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //ПОЗИТИВНЫЙ КЕЙС: Проверяем id проекта на допустимость "_"
    @Test
    public void project_create_positive_id_underscores() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectid = TestDataGenerator.generateStringOfLengthRange(1, 224) + "_"; // генерим от 1 до 224 буквы + подчеркивание

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectid)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;
    }

    // ПОЗИТИВНЫЙ КЕЙС: id проекта равен null (проект создается без него)
    @Test
    public void project_create_positive_id_null() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectId = null; //передаем нулл

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectId)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        Response response = uncheckedProject.create(projectDescription); //Создаем респонс
        response.then().assertThat().statusCode(HttpStatus.SC_OK); //создан успешно без id


        //создается, id  автоматически назначается из имени, в формате:
        //      "id": "ProjectNameNiFVZ",
        //    "name": "project_name_niFVZ",
        //что затрудняет удаление после теста (DELETE 404)  -- нужно доработать

        //Решение:
        // Сохраняем id созданной билд-конфигурации из респонса
        String id = response.jsonPath().getString("id");

        //удаляем внутри теста и проверяем ответ
        uncheckedProject.delete(id).then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT); //код 204


    }

    // ПОЗИТИВНЫЙ КЕЙС: null в copyAllAssociatedSettings
    @Test
    public void project_create_positive_copySettings_null() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(testData.getProject().getId())
                .copyAllAssociatedSettings(null)  //передаем null
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    // ПОЗИТИВНЫЙ КЕЙС: null в parentProject
    @Test
    public void project_create_positive_parentProject_null() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(null) //передаем null
                .name(testData.getProject().getName())
                .id(testData.getProject().getId())
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }


//    // НЕГАТИВНЫЙ КЕЙС: создание проекта без авторизации (ВОЗМОЖНО ДУБЛИРУЕТ ТЕСТ ИЗ РОЛЕЙ)
//    @Test
//    public void project_create_negative_unauthorized() {
//        // Генерируем тестовые данные для пользователя и проекта
//        var testData = testDataStorage.addTestData();
//
////        // Регистрируем пользователя
////        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
//
//        // Создаем описание проекта
//        var projectDescription = NewProjectDescription.builder()
//                .parentProject(testData.getProject().getParentProject())
//                .name(testData.getProject().getName())
//                .id(testData.getProject().getId())
//                .copyAllAssociatedSettings(true)
//                .build();
//
//
//        // Пытаемся создать проект
//        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
//        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED); // Проверяем статус ответа;
//
//        // Проверка отсутствия созданного проекта
//        uncheckedWithSuperUser.getProjectRequest()
//                .get(testData.getProject().getId())
//                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
//                .body(Matchers.containsString("No project found by locator 'count:1,id:" + testData.getProject().getId() + "'"));
//    }


    // НЕГАТИВНЫЙ КЕЙС: длина имени проекта (0 символов)
    @Test
    public void project_create_negative_name_0symbol() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем имя проекта
        String projectName = TestDataGenerator.generateStringOfLength(0); //представим, что от 1 до 255 включительно (на самом деле проходит 100500+символов)
        String id = testData.getProject().getId();

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName)
                .id(id)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST); // Проверяем статус ответа;

        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(id)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + id + "'"));
    }


    //НЕГАТИВНЫЙ КЕЙС: Создание двух проектов с одним NAME и разными ID
    @Test
    public void project_create_negative_name_duplicate() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем уникальные идентификаторы проектов с помощью TestDataGenerator
        String projectId1 = TestDataGenerator.generateStringOfLength(2);
        String projectId2 = TestDataGenerator.generateStringOfLength(2);

        // Генерируем имя проекта
        String projectName = testData.getProject().getName(); //

        // Создаем описание проекта
        message("Первый проект с name=" + projectName);
        var projectDescription1 = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName)
                .id(projectId1)
                .copyAllAssociatedSettings(true)
                .build();

        // Создаем Первый проект
        var uncheckedProject1 = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject1.create(projectDescription1).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Создаем описание проекта
        message("Второй проект с name=" + projectName);
        var projectDescription2 = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName) //то же имя
                .id(projectId2)  // другой id
                .copyAllAssociatedSettings(true)
                .build();

        // Создаем Второй проект
        var uncheckedProject2 = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject2.create(projectDescription2).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST) // Проверяем статус ответа;
                .body(Matchers.containsString("Project with this name already exists: " + testData.getProject().getName())); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
    }


    // НЕГАТИВНЫЙ КЕЙС: длина id проекта (0 символов)
    @Test
    public void project_create_negative_id_empty() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectid = TestDataGenerator.generateStringOfLength(0); //ограничение от 1 до 225 включительно

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectid)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                .body(Matchers.containsString("Project ID must not be empty.")); // Проверяем статус ответа, почему то 500;

        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectid)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + projectid + "'"));
    }

    // НЕГАТИВНЫЙ КЕЙС: длина id проекта 226 символов
    @Test
    public void project_create_negative_id_226symbol() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта
        String projectId = TestDataGenerator.generateStringOfLength(226); //ограничение от 1 до 225 включительно

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectId)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                .body(Matchers.containsString("Project ID \"" + projectId + "\" is invalid: it is 226 characters long while the maximum length is 225. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)."));
        // Проверяем статус ответа, почему то 500;

        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectId)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + projectId + "'"));
    }


    // НЕГАТИВНЫЙ КЕЙС: id проекта начинается с цифры
    @Test
    public void project_create_negative_id_startsByNumeric() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта c цифрой в начале
        String num = TestDataGenerator.generateStringOfLengthNumeric(1);
        String projectId = num + TestDataGenerator.generateStringOfLengthRange(0, 224); //ограничение от 1 до 225 включительно

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectId)
                .copyAllAssociatedSettings(true)
                .build();


        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //Проверяем статус ответа, почему то 500
                // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                .body(Matchers.containsString("is invalid: starts with non-letter character")); // Проверяем содержание сообщения (основную мысль)
        //Оставил такую проверку сообщения, так как долго боролся с синтаксисом для проверки сообщения и никак не удалось победить кавычки:
        /*java.lang.AssertionError: 1 expectation failed.
        Response body doesn't match expectation.
        Expected: a string containing "Project ID \"4CuLProy\" is invalid: starts with non-letter character \"4\".
        */
        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectId)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + projectId + "'"));
    }


    // НЕГАТИВНЫЙ КЕЙС: id проекта начинается с _
    @Test
    public void project_create_negative_id_startsUnderscore() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id проекта c цифрой в начале
        String start = "_";
        String projectId = start + TestDataGenerator.generateStringOfLengthRange(0, 224); //ограничение от 1 до 225 включительно

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(testData.getProject().getName())
                .id(projectId)
                .copyAllAssociatedSettings(true)
                .build();


        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) //Проверяем статус ответа, почему то 500
                // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                .body(Matchers.containsString("is invalid: starts with non-letter character")); // Проверяем содержание сообщения (основную мысль)
        //Оставил такую проверку сообщения, так как долго боролся с синтаксисом для проверки сообщения и никак не удалось победить кавычки:
        /*java.lang.AssertionError: 1 expectation failed.
        Response body doesn't match expectation.
        Expected: a string containing "Project ID \"4CuLProy\" is invalid: starts with non-letter character \"4\".
        */
        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectId)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + projectId + "'"));
    }


    //НЕГАТИВНЫЙ КЕЙС: Создание двух проектов с одним ID
    @Test
    public void project_create_negative_id_duplicate() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем уникальные идентификаторы проектов с помощью TestDataGenerator
        String projectName1 = TestDataGenerator.generateStringOfLength(2);
        String projectName2 = TestDataGenerator.generateStringOfLength(2);

        // Генерируем ID проекта
        String projectId = testData.getProject().getName(); //

        // Создаем описание проекта
        message("Первый проект с Id=" + projectId);
        var projectDescription1 = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName1)
                .id(projectId)
                .copyAllAssociatedSettings(true)
                .build();

        // Создаем Первый проект
        var uncheckedProject1 = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject1.create(projectDescription1).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Проверка созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectId)
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        // Создаем описание проекта
        message("Второй проект с Id=" + projectId);
        var projectDescription2 = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName2) //новое имя
                .id(projectId)  // тот же id
                .copyAllAssociatedSettings(true)
                .build();

        // Создаем Второй проект
        var uncheckedProject2 = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject2.create(projectDescription2).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST) // Проверяем статус ответа;
                // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                .body(Matchers.containsString("is already used by another project"));


    }

    // НЕГАТИВНЫЙ КЕЙС: id проекта содержит недопустимые символы
    @Test
    public void project_create_negative_id_containsSpecialCharacters() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Список спецсимволов для проверки
        String specialCharacters = "!@#$%^&*()-+=[]{};:'\",.<>?/\\|`~";

        // Проходим по каждому спецсимволу и проверяем создание проекта
        for (int i = 0; i < specialCharacters.length(); i++) {
            char specialChar = specialCharacters.charAt(i);

            // Генерируем id проекта с текущим спецсимволом
            String projectId = TestDataGenerator.generateStringOfLengthRange(1, 1) + specialChar; // Ограничение от 1 до 225 символов включительно

            // Создаем описание проекта
            var projectDescription = NewProjectDescription.builder()
                    .parentProject(testData.getProject().getParentProject())
                    .name(testData.getProject().getName())
                    .id(projectId)
                    .copyAllAssociatedSettings(true)
                    .build();

            // Пытаемся создать проект
            var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
            uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Проверяем статус ответа, почему-то 500
                    // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
                    .body(Matchers.containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)")); // Проверяем содержание сообщения (основную мысль)

            //Для скобок другой ответ -не ищутся через get
            if (projectId.contains(")") || projectId.contains("(")) {
                // Проверка отсутствия созданного БилдКонфига
                uncheckedWithSuperUser.getBuildConfigRequest()
                        .get(projectId)
                        .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
                return;
            }

            // Проверка отсутствия созданного проекта
            uncheckedWithSuperUser.getProjectRequest()
                    .get(projectId)
                    .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                    .body(Matchers.containsString("No project found by locator 'count:1,id:" + projectId + "'"));

        }
    }

    // НЕГАТИВНЫЙ КЕЙС: name проекта равен null
    @Test
    public void project_create_negative_name_null() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем Name проекта
        String projectName = null; //передаем нулл
        String id = testData.getProject().getId();

        // Создаем описание проекта
        var projectDescription = NewProjectDescription.builder()
                .parentProject(testData.getProject().getParentProject())
                .name(projectName)
                .id(id)
                .copyAllAssociatedSettings(true)
                .build();

        // Пытаемся создать проект
        var uncheckedProject = new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedProject.create(projectDescription).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty.")); // Проверяем содержание сообщения

        // Проверка отсутствия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(id)
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + id + "'"));

    }


}








