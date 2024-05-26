package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.example.teamcity.api.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.LoginPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.BeforeSuite;

public class BaseUiTest extends BaseTest {
    // Метод, выполняемый перед всеми тестами
    @BeforeSuite
    public void setupUiTest() {

        // Установка базового URL для тестирования с использованием параметра "host" из конфигурации
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        // Установка удаленного веб-драйвера для выполнения тестов
        Configuration.remote = Config.getProperty("remote");

        //Настройки для Селеноида
        Configuration.reportsFolder = "target/surefire-reports"; //папка для репортов
        Configuration.downloadsFolder = "target/downloads"; //папка для загрузок

        //Настройки браузера
        BrowserSettings.setup(Config.getProperty("browser")); //название берем из конфига

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

    }

    public void loginAsUser(User user) {
        //Генерируем юзера
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(user); // Создание пользователя
        //логинимся методом, передавая туда юзера
        new LoginPage().open().login(user);
    }

    public void loginAsExistUser(User user) {
        //логинимся методом, передавая туда юзера
        new LoginPage().login(user);
    }

}
