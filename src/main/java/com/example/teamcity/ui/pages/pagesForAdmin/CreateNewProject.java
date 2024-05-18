package com.example.teamcity.ui.pages.pagesForAdmin;
//В этом пакете будем накапливать все странички, требующие определенные права.

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.element;


//Общий метод для создания проекта с заполнением полей
public class CreateNewProject extends Page {
    private SelenideElement urlInput = element(Selectors.byId("url"));    //передаем урл
    private static SelenideElement errorUrl = element(Selectors.byId("error_url"));    //хинт об ошибочном урле]
    private static String errorUrlText = "Cannot create a project using the specified URL. The URL is not recognized.";
    private static String errorPrivateUrlText = "Anonymous authentication has failed. The repository is either private or does not exist";

    private SelenideElement projectNameInput = element(Selectors.byId("projectName"));
    private SelenideElement buildTypeNameInput = element(Selectors.byId("buildTypeName"));

    //Мануал
    private SelenideElement manualNameInput = element(Selectors.byId("name"));
    private static SelenideElement errorName = element(Selectors.byId("errorName")); //хинт об ошибочном урле]
    private static String errorNameEmptyText = "Project name is empty";

    private static SelenideElement manualCreateButton = element(Selectors.byHref("#createManually"));
    public static SelenideElement projectIdInput = element(Selectors.byId("externalId"));
    public static SelenideElement errorIDEmpty = element(Selectors.byId("errorExternalId"));
    private static String errorIDEmptyText = "Project ID must not be empty.";

    //Открываем урл создания проекта, передав в него род.проект
    public CreateNewProject open(String parentProjectId) {
        //                              /admin/createObjectMenu.html?projectId=_Root&showMode=createProjectMenu&cameFromUrl=http%3A%2F%2Flocalhost%3A8111%2Ffavorite%2Fprojects#createFromUrl
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        return this;
    }

    //Открываем урл создания проекта, передав в него род.проект
    public CreateNewProject openCreateProject_ByManually(String parentProjectId) {
        //                              /admin/createObjectMenu.html?projectId=_Root&showMode=createProjectMenu&cameFromUrl=http%3A%2F%2Flocalhost%3A8111%2Ffavorite%2Fprojects#createFromUrl
        Selenide.open("/admin/createObjectMenu.html?projectId=" + parentProjectId + "&showMode=createProjectMenu");
        waitUntilPageIsLoaded();
        switchToManually();
        return this;
    }

    //переключение в Мануал
    public static void switchToManually() {
        manualCreateButton.shouldBe(enabled, Duration.ofSeconds(10));
        manualCreateButton.click();
    }

    //Общий метод создания проекта через UI - вводим урл и подтверждаем
    public CreateNewProject createProjectByUrl(String url) {
        urlInput.sendKeys(url);
        submit();
        return this;
    }

    //Общий метод создания проекта через Manual - вводим name (id формируется автомат.) и подтверждаем
    public CreateNewProject setupProjectByManual(String name) {
        manualNameInput.sendKeys(name);
        // Проверяем, что значение в projectIdInput сгенерировалось и не пустое
        projectIdInput.shouldBe(Condition.attribute("generated"), Duration.ofSeconds(10));
        projectIdInput.shouldNotBe(Condition.empty);
        submit();
        return this;
    }

    public CreateNewProject setupProjectByManual_onlyName(String name) {
        manualNameInput.sendKeys(name);
        return this;
    }

//    //Проверка сообщения об успехе
//    public void Check() {
//        checkSuccessCreateProject();
//    }


    //Заполнение полей проекта
    public void setupProject_ByUrl(String projectName, String buildTypeName) {
        projectNameInput.shouldBe(visible, Duration.ofSeconds(10));
        projectNameInput.clear();
        projectNameInput.sendKeys(projectName);

        buildTypeNameInput.shouldBe(visible, Duration.ofSeconds(10));
        buildTypeNameInput.clear();
        buildTypeNameInput.sendKeys(buildTypeName);
        submit();
    }

    public static void checkErrorUrl() {
        errorUrl.shouldBe(visible, Duration.ofSeconds(10));
        errorUrl.shouldHave(text(errorUrlText));
    }

    public static void checkErrorPrivateUrl() {
        errorUrl.shouldBe(visible, Duration.ofSeconds(10));
        errorUrl.shouldHave(text(errorPrivateUrlText));
    }

    public static void checkErrorEmptyName() {
        errorName.shouldBe(visible, Duration.ofSeconds(10));
        errorName.shouldHave(text(errorNameEmptyText));
    }

    public static void checkErrorEmptyID() {
        errorIDEmpty.shouldBe(visible, Duration.ofSeconds(10));
        errorIDEmpty.shouldHave(text(errorIDEmptyText));
    }

}
