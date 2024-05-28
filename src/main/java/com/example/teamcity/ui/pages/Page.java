package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import com.example.teamcity.ui.elements.PageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.element;

//Родительский класс всех PafeObjects, здесь храним общие элементы (кнопки submit, лоадеры загрузки и т.д)
public abstract class Page {
    private static SelenideElement submitButton = element(Selectors.byType("submit")); //все кнопки подтвержения (LogIn, Proceed etc)
    private static SelenideElement savingWaitingMarker = element(Selectors.byId("saving")); //лоадер
    private static SelenideElement pageWaitingMarker = element(Selectors.byDataTest("ring-loader")); //другой лоадер

    //метод Сабмита с зашитым в него ожиданием
    public static void submit() {
        submitButton.shouldBe(Condition.enabled, Duration.ofMinutes(10));
        submitButton.click();
        waitUntilDataIsSaved(); //добавили ожидание после клика
    }

    //добавим ожидание по лоадеру (который отловили через throttling страницы)
    public static void waitUntilPageIsLoaded() {
        pageWaitingMarker.shouldNotBe(Condition.visible, Duration.ofMinutes(5));
    }

    //лоадер сохранения данных (который отловили через throttling страницы)
    public static void waitUntilDataIsSaved() {
        //ждем пока не пропадет
        savingWaitingMarker.shouldNotBe(Condition.visible, Duration.ofSeconds(30));
    }


//    private List<PageElement> generatePageElements(ElementsCollection collection) {
//        //создаем список для накопления элементов
//        var elements = new ArrayList<ProjectElement>();
//
//        //пойдем по всей коллекции и для каждого веб-элемента создавать
//        collection.forEach(webElement -> {
//                    var pageElement = new ProjectElement(webElement);
//                    elements.add(pageElement);
//                }
//        );
//        return elements;
//    }

    //Из веб-элементов в пэйдж-элементы ElementsCollection >> List<ProjectElement
    //Параметризация с дженериком

    public  <T extends PageElement> List<T> generatePageElements(
            ElementsCollection collection,
            Function<SelenideElement, T> creator) {
        var elements = new ArrayList<T>();
        collection.forEach(webElement -> elements.add(creator.apply(webElement)));
        return elements;
    }
}
