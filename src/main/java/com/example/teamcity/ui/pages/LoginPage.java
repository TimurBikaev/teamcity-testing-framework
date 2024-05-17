package com.example.teamcity.ui.pages;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.Selectors;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.element;


@Getter //для доступа к элементам
public class LoginPage extends Page{


    //private - потому что элементы должны хранить состояние и не меняться
    private static final String LOGIN_PAGE_URL = "/login.html";
    private SelenideElement usernameInput = element(Selectors.byId("username"));
    private SelenideElement passwordInput = element(Selectors.byId("password"));

    //метод открытия страницы, возвращающий саму страницу
    public LoginPage open() {
        // Открываем страницу аутентификации
        Selenide.open(LOGIN_PAGE_URL); // Относительный путь от хоста
        return this; //вернуть текущий экз.класса
    }

    public void login (User user) {

    // Заполняем поля данными из генерации. Логинимся созданным ранее юзером
        usernameInput.sendKeys(user.getUsername());
        passwordInput.sendKeys(user.getPassword());
       submit(); // Нажимаем на кнопку "Log in"

    }
}
