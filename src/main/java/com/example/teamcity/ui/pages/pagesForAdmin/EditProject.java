package com.example.teamcity.ui.pages.pagesForAdmin;
//В этом пакете будем накапливать все странички, требующие определенные права.

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.element;

//страница редактирования проекта
public class EditProject extends Page {
    private static SelenideElement successMessage = element(Selectors.byId("message_projectCreated"));
    private SelenideElement createBuildConfigButton = element(Selectors.byClass("icon_before icon16 addNew"));

    //Проверяем сообщение об успехе на след.странице
    public static void checkSuccessCreateProject(String projectName) {
        String expectedMessage = String.format("Project \"%s\" has been successfully created. You can now create a build configuration.", projectName);
        successMessage.shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text(expectedMessage), Duration.ofSeconds(10));
    }


    //Для создания билд-конфига
    //Открываем урл редактирования проекта, передав в него проект
    public static EditProject open(String projectId) {
        String url = "/admin/editProjectObjectMenu.html?projectId=" + projectId;
        System.out.println("url " + url);
        Selenide.open("/admin/editProject.html?projectId=" + projectId);
        waitUntilPageIsLoaded();
        return new EditProject();
    }
}
