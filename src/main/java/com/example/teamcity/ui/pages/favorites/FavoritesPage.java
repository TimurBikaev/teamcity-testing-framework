package com.example.teamcity.ui.pages.favorites;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.Page;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.element;

public class FavoritesPage extends Page {
     private SelenideElement footer = element(Selectors.byClass("Footer__copyright--Pt"));
    // private SelenideElement projectsLoaderSpinner = element(Selectors.byDataTest("ring-loader-inline"));
    private SelenideElement header = element(Selectors.byDataTest("overview-header"));

    private SelenideElement projectsLoaderSpinner = element(Selectors.byDataTest("ring-loader-inline"));

    private void waitForProjectListLoad() {
        projectsLoaderSpinner.shouldNotBe(Condition.visible, Duration.ofSeconds(30));
    }

    //Ожидание загрузки страницы Favorite
    public void waitUntilFavoritePageIsLoaded() {
        waitUntilPageIsLoaded();
        header.shouldBe(Condition.visible, Duration.ofSeconds(10));
        footer.shouldBe(Condition.visible, Duration.ofSeconds(10));
        waitForProjectListLoad();
    }
}

