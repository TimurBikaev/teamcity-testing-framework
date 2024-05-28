package com.example.teamcity.ui;


import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.example.teamcity.api.generators.RandomData.getStringBuildTypeName;
import static com.example.teamcity.ui.pages.Page.submit;
import static com.example.teamcity.ui.pages.pagesForAdmin.BuildType.checkSuccessCreateBuildConfig;
import static com.example.teamcity.ui.pages.pagesForAdmin.BuildTypeVcsRoots.checkSuccessCreateBuildConfig_VCS;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.buildCongIdInput;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyBuildID;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyBuildName;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorPrivateRepUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.createBuildConfigInputOnlyName;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.switchToManually;

public class CreateNewBuildConfigTest extends BaseUiTest {
    String url = "https://github.com/TimurBikaev/teamcity-testing-framework"; //урл открытого репозитория для создания через URL


    //Создание BuildConfig by Url
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfigByUrl() {

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();
        String buildName = getStringBuildTypeName();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());
        // Проверяем успешное создание проекта
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        //Сохраняем ID проекта для дальнейшего запроса билда
        String projectId = testData.getProject().getId();

        message("Id проекта: " + testData.getProject().getId());
        message("http://localhost:8111/buildConfiguration/" + testData.getProject().getId() + "_Build" + buildName + "?mode=builds");

        //Переход в создание БК для проекта
        CreateNewBuildConfig.openCreateBuildConfigPage(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //создание через урл репозитория
        CreateNewBuildConfig.createBuildConfigByUrl(url);

        //Заполняем имя
        createBuildConfigInputOnlyName(buildName);

        //Proceed
        submit();

        //Проверяем сообщение об успехе на след.странице
        checkSuccessCreateBuildConfig(buildName, url);

        /*Как напрямую дергать БилдКОнфиг не зная его ID я не придумал и время поджимало,
        поэтому на UI нашел УРЛ по которому можно перейти в билд после создания, зная ID проекта и Имя Билда.
        Дергаем этот составной урл и проверяем код 200.
        Нужно дергать урл http://localhost:8111/buildConfiguration/projectIdmsVCd_BuildTypeNamedvoFK?mode=builds
        Пример составления: "http://localhost:8111/buildConfiguration/" + testData.getProject().getId() + "_Build" + buildName + "?mode=builds"
        И создал еще один метод получения билд-конфига.
        */

        // Проверка созданного БилдКонфига -- хотел переопределить get но столкнулся пока с тем что 1 аргумент только принимает
        uncheckedWithSuperUser.getBuildConfigRequestByName()
                .get(projectId + "_Build" + buildName + "?mode=builds")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //ВТОРОЙ СПОСОБ ПРОВЕРКИ ЧЕРЕЗ АПИ Создание BuildConfig by Url
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfigByUrl2() {

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();
        String buildName = getStringBuildTypeName();

        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());
        // Проверяем успешное создание проекта
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        //Сохраняем ID проекта для дальнейшего запроса билда
        String projectId = testData.getProject().getId();

        message("Id проекта: " + testData.getProject().getId());
        message("http://localhost:8111/buildConfiguration/" + testData.getProject().getId() + "_Build" + buildName + "?mode=builds");

        //Переход в создание БК для проекта
        CreateNewBuildConfig.openCreateBuildConfigPage(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //создание через урл репозитория
        CreateNewBuildConfig.createBuildConfigByUrl(url);

        //Заполняем имя
        createBuildConfigInputOnlyName(buildName);

        //Proceed
        submit();

        //Проверяем сообщение об успехе на след.странице
        checkSuccessCreateBuildConfig(buildName, url);

        // Проверка созданного БилдКонфига -- хотел переопределить get но столкнулся пока с тем что 1 аргумент только принимает
        uncheckedWithSuperUser.getBuildConfigRequestByNameBuild()
                .get(buildName)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //Создание BuildConfig by Manually
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfigByManual() throws InterruptedException {

        //генерим имя БК
        String buldConfigName = TestDataGenerator.generateStringOfLengthRange(1, 2);

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());

        //Переход в создание БК для проекта способом Manually
        CreateNewBuildConfig.openCreateBuildConfigPage_ByManual(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //Переход в создание БК для проекта способом Manually
        switchToManually();

        //создание через Manually
        CreateNewBuildConfig.createBuildConfigByManually(buldConfigName);

        //Сохраняем сгенерированный ID
        String buildID = buildCongIdInput.getAttribute("generated");

        submit();

        //Проверяем сообщение об успехе на след.странице (без названия БК)
        checkSuccessCreateBuildConfig_VCS();

        // Проверка созданного БилдКонфига по сгенерир.ID (здесь было просто)
        uncheckedWithSuperUser.getBuildConfigRequest()
                .get(buildID)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //НЕГАТИВ:  BuildConfig by Manually с пустым именем
    @Test
    public void authorizedUserCreateNewBuildConfigByManualemptyName() {

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());

        //Переход в создание БК для проекта способом Manually
        CreateNewBuildConfig.openCreateBuildConfigPage_ByManual(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //Переход в создание БК для проекта способом Manually
        switchToManually();

        //создание через Manually с заполнением только имени
        createBuildConfigInputOnlyName("");

        submit();

        //Проверяем сообщения об ошибке
        checkErrorEmptyBuildName();
        checkErrorEmptyBuildID();
    }

    //НЕГАТИВ:  BuildConfig by Manually с пустым ID
    @Test
    public void authorizedUserCreateNewBuildConfigByManualEmptyID() {

        //генерим имя БК
        String buldConfigName = TestDataGenerator.generateStringOfLengthRange(1, 10);

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());


        //Переход в создание БК для проекта способом Manually
        CreateNewBuildConfig.openCreateBuildConfigPage_ByManual(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //Переход в создание БК для проекта способом Manually
        switchToManually();

        //создание через Manually
        createBuildConfigInputOnlyName(buldConfigName);

        //удаляем ID
        buildCongIdInput.shouldBe(visible, Duration.ofSeconds(10));
        buildCongIdInput.clear();

        submit();

        //Проверяем сообщение об ошибке
        checkErrorEmptyBuildID();
    }

    //НЕГАТИВ Создание BuildConfig by Url с пустым Url
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfigByEmptyUrl() throws InterruptedException {

        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());

        //Переход в создание БК для проекта
        CreateNewBuildConfig.openCreateBuildConfigPage(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //создание через урл рандомного репозитория
        CreateNewBuildConfig.createBuildConfigByUrl("");

        //проверяем ошибку заполнения урла
        checkErrorEmptyUrl();

    }

    //НЕГАТИВ Создание BuildConfig by Url с Приватным репоз. без кредов
    @Test
    public void authorizedUserShouldBeAbleCreateNewBuildConfigByPrivateUrl() throws InterruptedException {
        var urlPrivateRepository = "https://github.com/TimurBikaev/privateRepository"; //урл репозитория для создения через Реп
        // Генерируем тестовые данные для пользователя и проекта
        var testData = testDataStorage.addTestData();

        // Создаем проект
        // Регистрируем пользователя
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(testData.getUser());
        // Создаем проект с корректными данными
        var project = new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getProject());
        // Проверяем успешное создание проекта
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());

        //Переход в создание БК для проекта
        CreateNewBuildConfig.openCreateBuildConfigPage(testData.getProject().getId());

        //логинимся
        loginAsExistUser(testData.getUser());

        //создание через урл рандомного репозитория
        CreateNewBuildConfig.createBuildConfigByUrl(urlPrivateRepository);

        //проверяем ошибку заполнения урла
        checkErrorPrivateRepUrl();
    }
}

