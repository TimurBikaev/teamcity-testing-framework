package com.example.teamcity.ui.pages.pagesForAdmin;
//В этом пакете будем накапливать все странички, требующие определенные права.

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

//страница редактирования проекта
public class BuildTypeVcsRoots extends Page {

    private static SelenideElement successMessage = element(Selectors.byId("unprocessed_buildTypeCreated"));

    //Проверяем сообщение об успехе на след.странице
    public static void checkSuccessCreateBuildConfig_VCS() {
        String expectedMessage = "Build configuration successfully created. You can now configure VCS roots.";
        successMessage.shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text(expectedMessage), Duration.ofSeconds(10));
    }



}
