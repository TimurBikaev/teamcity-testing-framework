package com.example.teamcity.ui;


import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.example.teamcity.ui.pages.Page.submit;
import static com.example.teamcity.ui.pages.pagesForAdmin.BuildType.checkSuccessCreateBuildConfig;
import static com.example.teamcity.ui.pages.pagesForAdmin.BuildTypeVcsRoots.checkSuccessCreateBuildConfig_VCS;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.buildCongIdInput;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyBuildID;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyBuildName;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorEmptyUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.checkErrorPrivateRepUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewBuildConfig.switchToManually;

public class Test_CreateNewBuildConfig extends BaseUiTest {
    String url = "https://github.com/TimurBikaev/teamcity-testing-framework"; //урл репозитория для создания через URL


    //Создание BuildConfig by Url
    @Test
    public void authorizedUser_ShouldBeAble_Create_NewBuildConfig_ByUrl() throws InterruptedException {

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

        //создание через урл репозитория
        CreateNewBuildConfig.createBuildConfigByUrl(url);

        //Proceed
        submit();

        //Проверяем сообщение об успехе на след.странице
        checkSuccessCreateBuildConfig("Build", url);
    }

    //Создание BuildConfig by Manually
    @Test
    public void authorizedUser_ShouldBeAble_Create_NewBuildConfig_ByManual() throws InterruptedException {

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

        //Проверяем сообщение об успехе на след.странице
        checkSuccessCreateBuildConfig_VCS();
    }

    //НЕГАТИВ:  BuildConfig by Manually с пустым именем
    @Test
    public void authorizedUser_Create_NewBuildConfig_ByManual_emptyName() {

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
        CreateNewBuildConfig.createBuildConfigByManually_name("");

        submit();

        //Проверяем сообщения об ошибке
        checkErrorEmptyBuildName();
        checkErrorEmptyBuildID();
    }

    //НЕГАТИВ:  BuildConfig by Manually с пустым ID
    @Test
    public void authorizedUser_Create_NewBuildConfig_ByManual_emptyID() {

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
        CreateNewBuildConfig.createBuildConfigByManually_name(buldConfigName);

        //удаляем ID
        buildCongIdInput.shouldBe(visible, Duration.ofSeconds(10));
        buildCongIdInput.clear();

        submit();

        //Проверяем сообщение об ошибке
        checkErrorEmptyBuildID();
    }

    //НЕГАТИВ Создание BuildConfig by Url с пустым Url
    @Test
    public void authorizedUser_ShouldBeAble_Create_NewBuildConfig_ByEmptyUrl() throws InterruptedException {

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
    public void authorizedUser_ShouldBeAble_Create_NewBuildConfig_ByPrivateUrl() throws InterruptedException {
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

