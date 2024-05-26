package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.favorites.FavoritesPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.elements;

public class AgentPage extends FavoritesPage {

//    private static final String AGENTS = "/agent/1";
    private ElementsCollection authAgentButton = elements(Selectors.byAgentSidebar("true"));

    public AgentPage connectAgent() {
        authAgentButton.first().shouldBe(visible).click();
        submit();
        return this; // возвращаем новую страницу (AgentDetailsPage)
    }

}
