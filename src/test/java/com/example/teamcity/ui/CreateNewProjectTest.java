package com.example.teamcity.ui;


import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import com.example.teamcity.ui.pages.pagesForAdmin.CreateNewProject;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest {

    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() throws InterruptedException {
        //Берем хранилище, добавляем туда ТестДату
        var testData = testDataStorage.addTestData(); // Создание тестовых данных
        var url = "https://github.com/TimurBikaev/teamcity-testing-framework"; //урл репозитория для создения через Реп

        //логинимся
        loginAsUser(testData.getUser());

        //далее создаем проект (доступно только админам)
        new CreateNewProject().open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url) //переходим по урлу создания
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName()); //заполняем поля Имя и БилдТайп


        new ProjectsPage().open()
                .getSubprojects() // забираем значения subprojects
                .stream().reduce((first, second) -> second).get() //забираем последний созданный элемент
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));// проверяем появление имени проекта
    }
}

