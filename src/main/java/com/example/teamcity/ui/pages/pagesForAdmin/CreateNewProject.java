package com.example.teamcity.ui.pages.pagesForAdmin;
//В этом пакете будем накапливать все странички, требующие определенные права.

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.element;


//Общий метод для создания проекта с заполнением полей
public class CreateNewProject extends Page {
    private SelenideElement urlInput = element(Selectors.byId("url"));    //передаем урл
    private SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));

    //Открываем урл создания проекта, передав в него род.проект
    public CreateNewProject open(String parentProjectId) {
        //                              /admin/createObjectMenu.html?projectId=_Root&showMode=createProjectMenu&cameFromUrl=http%3A%2F%2Flocalhost%3A8111%2Ffavorite%2Fprojects#createFromUrl
        Selenide.open("/admin/createObjectMenu.html?projectId=" +  parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }

    //Общий метод создания проекта через UI - вводим урл и подтверждаем
    public CreateNewProject createProjectByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    //Заполнение полей проекта
    public void setupProject(String projectName, String buildTypeName) {
        projectNameInput.shouldBe(visible, Duration.ofSeconds(10));
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);

        buildTypeNameInput.shouldBe(visible, Duration.ofSeconds(10));
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }

}
