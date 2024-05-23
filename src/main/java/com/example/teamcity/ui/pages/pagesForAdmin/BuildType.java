package com.example.teamcity.ui.pages.pagesForAdmin;
//В этом пакете будем накапливать все странички, требующие определенные права.

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

//страница редактирования проекта
public class BuildType extends Page {

    private static SelenideElement successMessage = element(Selectors.byId("unprocessed_objectsCreated"));

    //Проверяем сообщение об успехе на след.странице
    public static void checkSuccessCreateBuildConfig(String buildName, String urlRepository) {
        String fullUrlRepository = urlRepository + "#refs/heads/master";
        String expectedMessage = String.format(
                "New build configuration \"%s\" and VCS root \"%s\" have been successfully created.",
                buildName, fullUrlRepository
        );
        successMessage.shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text(expectedMessage), Duration.ofSeconds(10));
        //New build configuration "Build" and VCS root "https://github.com/TimurBikaev/teamcity-testing-framework#refs/heads/master" have been successfully created.
    }



}
