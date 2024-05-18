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
public class CreateNewBuildConfig extends Page {
    private static SelenideElement urlInput = element(Selectors.byId("url"));    //инпут урл репозитория
    private static String errorUrlEmptyText = "URL must not be empty";
    private static String errorUrlPrivateText = "Anonymous authentication has failed. The repository is either private or does not exist";
    private static SelenideElement buildConfigNameInput = element(Selectors.byId("buildTypeName")); //инпут Name
    private SelenideElement buildBranchInput = element(Selectors.byId("branch"));
    public static SelenideElement buildCongIdInput = element(Selectors.byId("buildTypeExternalId"));
    private static SelenideElement ManuallyButton = element(Selectors.byHref("#createManually"));

    private static SelenideElement buildNameError = element(Selectors.byId("error_buildTypeName"));
    private static String errorNameEmptyText = "Name must not be empty";
    private static SelenideElement buildIDError = element(Selectors.byId("error_buildTypeExternalId"));
    private static String errorIDEmptyText = "The ID field must not be empty.";

    private static SelenideElement buildUrlEmptyError = element(Selectors.byId("error_url"));



    //Открываем урл создания БилдКонфига ерез URL
    public static CreateNewBuildConfig openCreateBuildConfigPage(String projectId) {
        //                              /admin/createObjectMenu.html?projectId=_Root&showMode=createProjectMenu&cameFromUrl=http%3A%2F%2Flocalhost%3A8111%2Ffavorite%2Fprojects#createFromUrl
        Selenide.open("/admin/createObjectMenu.html?projectId=" +  projectId + "&showMode=createBuildTypeMenu#createFromUrl");
                 //http://localhost:8111/admin/createObjectMenu.html?projectId=projectIdwZrlU&showMode=createBuildTypeMenu#createFromUrl
        waitUntilPageIsLoaded();
        return new CreateNewBuildConfig();
    }

    //переключение в Мануал
    public static void  switchToManually() {
        ManuallyButton.shouldBe(enabled, Duration.ofSeconds(10));
        ManuallyButton.click();
    }

    //Открываем урл создания БилдКонфига через Manually
    public static CreateNewBuildConfig openCreateBuildConfigPage_ByManual(String projectId) {
        Selenide.open("/admin/createObjectMenu.html?projectId=" +  projectId + "&showMode=createBuildTypeMenu#createManually");
        waitUntilPageIsLoaded();
        return new CreateNewBuildConfig();
    }

    //Общий метод создания БК через UI - вводим урл и подтверждаем
    public static CreateNewBuildConfig createBuildConfigByUrl(String url) {
        urlInput.shouldBe(enabled, Duration.ofSeconds(10));
        urlInput.sendKeys(url);
        submit();
        return new CreateNewBuildConfig();
    }

    //Метод создания БК через Manually - вводим buildConfigNameInput и подтверждаем
    public static CreateNewBuildConfig createBuildConfigByManually(String name) {
        buildConfigNameInput.shouldBe(enabled, Duration.ofSeconds(10));
        buildConfigNameInput.sendKeys(name);
        // Проверяем, что значение в buildCongIdInput сгенерировалось и не пустое
        buildCongIdInput.shouldBe(Condition.attribute("generated"), Duration.ofSeconds(10));
        buildCongIdInput.shouldNotBe(Condition.empty);
        submit();
        return new CreateNewBuildConfig();
    }

    //Метод создания БК через Manually - вводим buildConfigNameInput и подтверждаем
    public static CreateNewBuildConfig createBuildConfigByManually_name(String name) {
        buildConfigNameInput.shouldBe(enabled, Duration.ofSeconds(10));
        buildConfigNameInput.sendKeys(name);
        // Проверяем, что значение в buildCongIdInput сгенерировалось и не пустое
//        buildCongIdInput.shouldBe(Condition.attribute("generated"), Duration.ofSeconds(10));
//        buildCongIdInput.shouldNotBe(Condition.empty);

        return new CreateNewBuildConfig();
    }

    //Заполнение полей BuildConfig
    public void setupBuildConfig(String projectName, String buildBranch) {
        buildConfigNameInput.shouldBe(visible, Duration.ofSeconds(10));
        buildConfigNameInput.clear();
        buildConfigNameInput.sendKeys(projectName);

        buildBranchInput.shouldBe(visible, Duration.ofSeconds(10));
        buildBranchInput.clear();
        buildBranchInput.sendKeys(buildBranch);
        submit();
    }

    public static void checkErrorEmptyUrl() {
        buildUrlEmptyError.shouldBe(visible, Duration.ofSeconds(10));
        buildUrlEmptyError.shouldHave(text(errorUrlEmptyText));
    }

    public static void checkErrorPrivateRepUrl() {
        buildUrlEmptyError.shouldBe(visible, Duration.ofSeconds(10));
        buildUrlEmptyError.shouldHave(text(errorUrlPrivateText));
    }

    public static void checkErrorEmptyBuildID() {
        buildIDError.shouldBe(visible, Duration.ofSeconds(10));
        buildIDError.shouldHave(text(errorIDEmptyText));
    }

    public static void checkErrorEmptyBuildName() {
        buildNameError.shouldBe(visible, Duration.ofSeconds(10));
        buildNameError.shouldHave(text(errorNameEmptyText));
    }


}
