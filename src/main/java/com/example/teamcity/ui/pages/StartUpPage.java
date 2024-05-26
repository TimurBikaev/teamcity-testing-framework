package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

@Getter
public class StartUpPage extends Page {
    private SelenideElement proceedButton = element(Selectors.byId("proceedButton"));
    private SelenideElement acceptLicenseInput = element(Selectors.byId("accept"));
    private SelenideElement continueButton = element(Selectors.byName("Continue"));
    private SelenideElement createAccountHeader = element(Selectors.byId("header"));

    public StartUpPage open() {
        Selenide.open("/");
        return this;
    }

    //проходим шаги после установки - принять-принять-соглашение с лицензией и т.д.
    public StartUpPage setupTeamCityServer() {
        waitUntilPageIsLoaded();
        proceedButton.shouldBe(Condition.visible, Duration.ofSeconds(10));
        proceedButton.click();
        waitUntilPageIsLoaded();
        proceedButton.shouldBe(Condition.visible, Duration.ofSeconds(10));
        proceedButton.click();

        waitUntilPageIsLoaded();
        acceptLicenseInput.shouldBe(Condition.enabled, Duration.ofMinutes(5));
        acceptLicenseInput.scrollTo();
        acceptLicenseInput.click();
        continueButton.click();
        return this;
    }
}
