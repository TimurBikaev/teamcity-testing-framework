package com.example.teamcity.api;

//Первый апи-тест, настедуется от базового апи-класса,
// а тот наследуется от общего базового класса

import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {

    // Тест-кейс для проверки создания конфигурации сборки
    @Test
    public void buildConfigurationTest() {

        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем проверяемого пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект от имени пользователя, передавая описание проекта
        var project = new CheckedProject(Specifications.getSpec() // Получаем спецификацию запроса
                .authSpec(testData.getUser()))  // Указываем пользователя для авторизации
                .create(testData.getProject()); // Создаем проект от имени этого пользователя

        // Проверяем, что идентификатор созданного проекта совпадает с идентификатором из тестовых данных
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

    }
    //создаем описание проекта
//        var projectDescription = NewProjectDescription
//                .builder()
//                .parentProject(Project.builder()
//                        .locator("_Root")
//                        .build())
//                .name("name1")
//                .id("id1")
//                .copyAllAssociatedSettings(true)
//                .build();


    //используем RestAssured
    //Запрашиваем токен под нашими кредами и сохраням в переменную
    //   var token = new AuthRequest(user).getCsrfToken(); //поменяли РестАшшуред на метод получения токена для конкретного юзера

//        RestAssured
//                .given()
//                .spec(Specifications.getSpec().authSpec(user))
//                .get("/authenticationTest.html?csrf")
//                .then().assertThat().statusCode(HttpStatus.SC_OK)
//                .extract().asString();

//        System.out.println(token);


    //ПОЗИТИВНЫЙ КЕЙС создания Билд Конфига с валидными данными
    @Test
    public void createBuildConfig_validData() {
        var testData = testDataStorage.addTestData(); // Добавляем тестовые данные для  проекта

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Создаем БилдКОнфиг
        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType()) // Создаем конфигурацию сборки
                .then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа

    }

    // ПОЗИТИВНЫЙ ТЕСТ: создание Билд Конфига c ID 1 символ
    @Test
    public void createBuildConfig_Id_1() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем имя проекта
        String id = TestDataGenerator.generateStringOfLength(1);

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;
    }

    // ПОЗИТИВНЫЙ ТЕСТ: создание Билд Конфига c ID 225 символов
    @Test
    public void createBuildConfig_Id_225() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем имя проекта
        String id = TestDataGenerator.generateStringOfLength(225);

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;
    }

    // ПОЗИТИВНЫЙ ТЕСТ: создание Билд Конфига c name 1 символ
    @Test
    public void createBuildConfig_name_1() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем имя проекта
        String name = TestDataGenerator.generateStringOfLength(1);

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(testData.getBuildType().getId())
                .name(name)
                .project(testData.getBuildType().getProject())
                .build();

        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;
    }

    // ПОЗИТИВНЫЙ ТЕСТ: создание Билд Конфига c name 255 символ (УСЛОВНО)
    @Test
    public void createBuildConfig_name_255() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем имя
        String name = TestDataGenerator.generateStringOfLength(255);

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(testData.getBuildType().getId())
                .name(name)
                .project(testData.getBuildType().getProject())
                .build();

        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;
    }



    //НЕГАТИВНЫЙ КЕЙС создания Билд Конфига без авторизации
    @Test
    public void createBuildConfig_unauthorized() {
        var testData = testDataStorage.addTestData(); // Добавляем тестовые данные для  проекта

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());


        // Создаем БилдКОнфиг
        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType()) // Создаем конфигурацию сборки
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED); // Проверяем статус ответа

    }


    // НЕГАТИВНЫЙ ТЕСТ: создание Билд Конфига под несуществующий проект
    @Test
    public void createBuildConfig_unExistProject() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем проверяемого пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его


        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(testData.getBuildType().getId())
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND) // Проверяем статус ответа;
                .body(Matchers.containsString("No project found by locator ")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
        //Project cannot be found by external id 'project_id_QosCi'.
    }

    // НЕГАТИВНЫЙ ТЕСТ: создание Билд Конфига c пустым ID
    @Test
    public void createBuildConfig_emptyId() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем
        String id = "";

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Проверяем статус ответа;
                .body(Matchers.containsString("Build configuration or template ID must not be empty.")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

    }

    // НЕГАТИВНЫЙ ТЕСТ: создание Билд Конфига c пустым Name
    @Test
    public void createBuildConfig_emptyName() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем
        String name = "";

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(testData.getBuildType().getId())
                .name(name)
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST) // Проверяем статус ответа;
                .body(Matchers.containsString("When creating a build type, non empty name should be provided.")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

    }

    // НЕГАТИВНЫЙ ТЕСТ: создание Билд Конфига c ID 226 символов
    @Test
    public void createBuildConfig_Id_226() {
        var testData = testDataStorage.addTestData(); // Получаем тестовые данные и сохраняем их

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Генерируем
        String id = TestDataGenerator.generateStringOfLength(226);

        // Создаем Билд Конфиг от имени пользователя, передавая описание
        var buildConfigDescription = BuildType.builder()
                .id(id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Проверяем статус ответа;
                .body(Matchers.containsString("is invalid: it is 226 characters long while the maximum length is 225. ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

    }

    // НЕГАТИВНЫЙ КЕЙС: id содержит недопустимые символы
    @Test
    public void createBuildConfig_containsSpecialCharacters() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его

        // Список спецсимволов для проверки
        String specialCharacters = "!@#$%^&*()-+=[]{};:'\",.<>?/\\|`~";

        // Проходим по каждому спецсимволу и проверяем создание
        for (int i = 0; i < specialCharacters.length(); i++) {
            char specialChar = specialCharacters.charAt(i);

            // Генерируем id с текущим спецсимволом
            String id = TestDataGenerator.generateStringOfLengthRange(1, 1) + specialChar; // Ограничение от 1 до 225 символов включительно

            // Создаем описание
            var buildConfigDescription = BuildType.builder()
                    .id(id)
                    .name(testData.getBuildType().getName())
                    .project(testData.getBuildType().getProject())
                    .build();

            // Пытаемся создать
            var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
            uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Проверяем статус ответа;
                    .body(Matchers.containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение
        }
    }

    //ПОЗИТИВНЫЙ КЕЙС: Проверяем id  на допустимость "_"
    @Test
    public void createBuildConfig_underscores() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        new CheckedUser(Specifications.getSpec() // Создаем пользователя от имени суперпользователя
                .superUserSpec()).create(testData.getUser()); // и регистрируем его
        // Генерируем id
        String id = TestDataGenerator.generateStringOfLengthRange(1, 224) + "_"; // генерим от 1 до 224 буквы + подчеркивание

        // Создаем описание
        var buildConfigDescription = BuildType.builder()
                .id(id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();

        // Пытаемся создать
        var uncheckedBuildConfig = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

    }

    //НЕГАТИВНЫЙ КЕЙС: Создание двух БилдКонфигов с одним NAME и разными ID
    @Test
    public void createBuildConfig_DuplicateName() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем уникальные идентификаторы проектов с помощью TestDataGenerator
        String Id1 = TestDataGenerator.generateStringOfLength(2);
        String Id2 = TestDataGenerator.generateStringOfLength(2);

        // Генерируем имя
        String name = testData.getProject().getName(); //

        // Создаем описание проекта
        message("Первый БилдКонфиг с name=" + name);
        var buildConfigDescription1 = BuildType.builder()
                .id(Id1)
                .name(name)
                .project(testData.getBuildType().getProject())
                .build();

        // Создаем Первый проект
        var uncheckedBuildConfig1 = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig1.create(buildConfigDescription1).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Создаем описание проекта
        message("Второй проект с name=" + name);
        var buildConfigDescription2 = BuildType.builder()
                .id(Id2)
                .name(name)
                .project(testData.getBuildType().getProject())
                .build();

        // Создаем Второй проект
        var uncheckedBuildConfig2 = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig2.create(buildConfigDescription2).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST) // Проверяем статус ответа;
                .body(Matchers.containsString("already exists in project")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

    }

    //НЕГАТИВНЫЙ КЕЙС: Создание двух БилдКонфигов с одним ID
    @Test
    public void createBuildConfig_DuplicateID() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем уникальные идентификаторы проектов с помощью TestDataGenerator
        String name1 = TestDataGenerator.generateStringOfLength(2);
        String name2 = TestDataGenerator.generateStringOfLength(2);

        // Генерируем имя
        String id = testData.getProject().getId(); //

        // Создаем описание проекта
        message("Первый БилдКонфиг с id=" + id);
        var buildConfigDescription1 = BuildType.builder()
                .id(id)
                .name(name1)
                .project(testData.getBuildType().getProject())
                .build();

        // Создаем Первый проект
        var uncheckedBuildConfig1 = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig1.create(buildConfigDescription1).then().assertThat().statusCode(HttpStatus.SC_OK); // Проверяем статус ответа;

        // Создаем описание
        message("Второй проект с id=" + id);
        var buildConfigDescription2 = BuildType.builder()
                .id(id)
                .name(name2)
                .project(testData.getBuildType().getProject())
                .build();

        // Создаем Второй проект
        var uncheckedBuildConfig2 = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig2.create(buildConfigDescription2).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST) // Проверяем статус ответа;
                .body(Matchers.containsString("is already used by another configuration or template")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

    }

    // НЕГАТИВНЫЙ КЕЙС: id  начинается с цифры
    @Test
    public void createBuildConfig_IDstartsByNonLetter() {
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());

        // Генерируем id  c цифрой в начале
        String num = TestDataGenerator.generateStringOfLengthNumeric(1);
        String Id = num + TestDataGenerator.generateStringOfLengthRange(0, 224); //ограничение от 1 до 225 включительно

        // Создаем описание
        var buildConfigDescription = BuildType.builder()
                .id(Id)
                .name(testData.getBuildType().getName())
                .project(testData.getBuildType().getProject())
                .build();


        // Пытаемся создать проект
        var uncheckedBuildConfig2 = new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()));
        uncheckedBuildConfig2.create(buildConfigDescription).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Проверяем статус ответа;
                .body(Matchers.containsString("is invalid: starts with non-letter character")); // Проверяем, содержит ли текст сообщения об ошибке ожидаемое сообщение

        //Оставил такую проверку сообщения, так как долго боролся с синтаксисом для проверки сообщения и никак не удалось победить кавычки:
        /*java.lang.AssertionError: 1 expectation failed.
        Response body doesn't match expectation.
        Expected: a string containing "Project ID \"4CuLProy\" is invalid: starts with non-letter character \"4\".
        */

    }

}




