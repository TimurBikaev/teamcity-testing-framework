package com.example.teamcity.ui;


import com.codeborne.selenide.Condition;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.example.teamcity.api.generators.RandomData.getString;
import static com.example.teamcity.ui.pages.Page.submit;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject.checkErrorEmptyID;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject.checkErrorEmptyName;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject.checkErrorPrivateUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject.checkErrorUrl;
import static com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject.projectIdInput;
import static com.example.teamcity.ui.pages.pagesForAdmin.EditProject.checkSuccessCreateProject;

public class Test_CreateNewProject extends BaseUiTest {

    @Test
    public void authorizedUserShouldBeAbleCreateNewProjectByUrl() throws InterruptedException {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        var urlPubRepository = "https://github.com/TimurBikaev/teamcity-testing-framework"; //урл репозитория для создения через Реп

        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(urlPubRepository) //переходим по урлу создания
                .setupProjectByUrl(testData.getProject().getName(), testData.getBuildType().getName()); //заполняем поля Имя и БилдТайп

        new ProjectsPage().open()
                .getSubprojects() // забираем значения subprojects
                .stream().reduce((first, second) -> second).get() //забираем последний созданный элемент
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));// проверяем появление имени проекта

        // Проверка апишкой наличия созданного проекта (по-хорошему -- вынести в метод, передавать имя проекта для проверки)
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getName())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //Создание нового проекта Manual
    @Test
    public void authorizedUserShouldBeAbleCreateNewProjectByManually() {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        //генерим имя проекта
        String projectName = TestDataGenerator.generateStringOfLengthRange(1, 80);
        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().openCreateProjectByManually(testData.getProject().getParentProject().getLocator())
                .setupProjectByManual(projectName); //переходим по урлу создания и заполняем имя

        //Проверяем сообщение об успехе на след.странице
        checkSuccessCreateProject(projectName);
        //у сообщения ограниченная вместимость по символам, поэтому полностью может не уместиться!

        // Проверка апишкой наличия созданного проекта
        uncheckedWithSuperUser.getProjectRequest()
                .get(projectName)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }


    //НЕГАТИВ: НЕКОРРЕКТНЫЙ УРЛ
    @Test
    public void authorizedUserCreateNewProjectUrlUncorrectUrlRepos() {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        var url = getString(); //рандомный урл репозитория для создения через Реп

        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url); //заполняем урл репозитория

        //проверяем ошибку заполнения урла
        checkErrorUrl();
    }

    //НЕГАТИВ: УРЛ ЗАКРЫТОГО РЕПОЗИТОРИЯ БЕЗ КРЕДОВ
    @Test
    public void authorizedUserCreateNewProjectUrlPrivateReposWithoutCred() {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        var urlPrivateRepository = "https://github.com/TimurBikaev/privateRepository"; //урл репозитория для создения через Реп

        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(urlPrivateRepository); //заполняем урл репозитория

        //проверяем ошибку private урла
        checkErrorPrivateUrl();
    }

    //НЕГАТИВ - МАНУАЛ - пустое имя
    @Test
    public void authorizedUserCreateNewProjectManualEmptyName() {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных

        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().openCreateProjectByManually(testData.getProject().getParentProject().getLocator())
                .setupProjectByManualOnlyName(""); //переходим по урлу создания и заполняем имя

        submit();

        //Проверяем хинт об ошибке
        checkErrorEmptyName();
    }

    //НЕГАТИВ - МАНУАЛ - пустой id
    @Test
    public void authorizedUserCreateNewProjectManuallyEmptyID() {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        //генерим имя проекта
        String projectName = TestDataGenerator.generateStringOfLengthRange(1, 225);
        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().openCreateProjectByManually(testData.getProject().getParentProject().getLocator())
                .setupProjectByManualOnlyName(projectName); //переходим по урлу создания и заполняем имя

        //очистка инпута ID
        projectIdInput.clear();

        submit();

        //Проверяем хинт об ошибке
        checkErrorEmptyID();
    }
}

