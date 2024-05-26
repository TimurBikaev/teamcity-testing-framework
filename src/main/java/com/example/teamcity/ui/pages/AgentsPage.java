package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.pages.favorites.FavoritesPage;

import static com.codeborne.selenide.Selenide.elements;

public class AgentsPage extends FavoritesPage {

    private static final String AGENTS = "/agents/overview";
    private ElementsCollection installAgentsButton = elements(Selectors.byHref("http://localhost:8111/installFullAgent.html"));
    private ElementsCollection unauthAgentsSidebar = elements(Selectors.byAgentSidebar("true"));
    private ElementsCollection agent = elements(Selectors.byHref("/agent/1"));


    //метод по открытию страницы
    public AgentsPage open() {
        Selenide.open(AGENTS);
        waitUntilFavoritePageIsLoaded(); //ожидание пока пропадет лоадер
        return this; //возвращаем текущий объект(страницу)
    }


}
