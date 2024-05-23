package com.example.teamcity.ui.elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

//класс абстрактный, т.к. мы не можем создать такую сущность
public abstract class PageElement {
    private final SelenideElement element;

    public PageElement(SelenideElement element) {
        this.element = element;
    }

    //поиск элемента внутри элемента
    public SelenideElement findElement(By by) {
        return element.find(by);
    }

    //поиск элемента внутри элемента
    public SelenideElement findElement(String value) {
        return element.find(value);
    }

    //поиск элементов внутри элемента
    public ElementsCollection findElements(By by) {
        return element.findAll(by);
    }

}
