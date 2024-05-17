package com.example.teamcity.ui.elements;


import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;

//по аналогии с PageObject описываем тут не страницу, а отдельный элемент
@Getter
public class ProjectElement extends PageElement {

    //храним как поле класса значение

    private final SelenideElement header;
   // private final SelenideElement icon;

    //конструктор, принимающий на вход сам веб-элемент и сохраняющий в переменные элементы
    public ProjectElement(SelenideElement element) {
        super(element);
        // Ищем хэдер у элемента и ждем, пока он станет видимым
      //  this.header = findElement(Selectors.byDataTest("subproject")).shouldBe(visible, Duration.ofSeconds(10));

        // Ищем иконку у элемента и ждем, пока она станет видимой
      //  this.icon = findElement("svg").shouldBe(visible, Duration.ofSeconds(10));
        //ищем хэдер у элемента
        this.header = findElement(Selectors.byDataTest("subproject")).shouldBe(visible, Duration.ofSeconds(10));
      // this.header = findElement(Selectors.byDataTest("subproject")); ////*[@data-test="subproject"]
//        this.icon = findElement("svg");
    }
}
