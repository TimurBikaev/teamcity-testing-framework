package com.example.teamcity.ui.pages.favorites;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.ProjectElement;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.elements;

//Страница всех проектов
public class ProjectsPage extends FavoritesPage {
    private static final String FAVORITE_PROJECTS = "/favorite/projects";
    private ElementsCollection subprojects = elements(Selectors.byClass("Subproject__container--Px"));


   // private SelenideElement subproject = element(Selectors.byClass("Subproject__container--Px"));

    private void waitForSubprojectElement() throws InterruptedException {
     //  subproject.shouldBe(Condition.visible, Duration.ofSeconds(10));
        subprojects.shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(10));
        subprojects.forEach(element -> element.shouldBe(visible, Duration.ofSeconds(10)));
        System.out.println("Waiting for subproject is done");
        //Thread.sleep(2000);
    }

    //метод по открытию страницы
    public ProjectsPage open() {
        Selenide.open(FAVORITE_PROJECTS);
        waitUntilFavoritePageIsLoaded(); //ожидание пока пропадет лоадер
        return this; //возвращаем текущий объект(страницу)
    }

    //метод для возвращения в виде списка
    public List<ProjectElement> getSubprojects() throws InterruptedException {
        waitForSubprojectElement();
        return generatePageElements(subprojects, ProjectElement::new); //генерим из спарсенной коллекции
    }


}
