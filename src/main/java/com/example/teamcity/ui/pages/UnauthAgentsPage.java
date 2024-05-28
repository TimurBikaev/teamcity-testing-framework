package com.example.teamcity.ui.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.favorites.FavoritesPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.elements;

public class UnauthAgentsPage extends FavoritesPage {

    private static final String UNAUTH_AGENTS = "/agents/unauthorized";
    private ElementsCollection unauthAgentsSidebar = elements(Selectors.byAgentSidebar("true"));
    private ElementsCollection agent = elements(Selectors.byHref("/agent/1"));

    // метод по открытию страницы
    public UnauthAgentsPage open() {
        Selenide.open(UNAUTH_AGENTS);
        waitUntilFavoritePageIsLoaded(); // ожидание пока пропадет лоадер

        // Ожидание появления хотя бы одного элемента в коллекциях на странице
        unauthAgentsSidebar.shouldBe(CollectionCondition.sizeGreaterThan(0)).first().shouldBe(visible);
        agent.shouldBe(CollectionCondition.sizeGreaterThan(0)).first().shouldBe(visible);

        return this; // возвращаем текущий объект (страницу)
    }



}
