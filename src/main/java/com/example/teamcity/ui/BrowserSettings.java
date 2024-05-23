package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

//Здесь будем настраивать браузеры для запуска тестов
public class BrowserSettings {

    public static void setup(String browser) {
        Configuration.browser = browser;
        switch (browser) {

            case "firefox":
                setFirefoxOptions();
                break;

            case "chrome":
                setChromeOptions();
                break;
        }
    }

    //выносим отдельно настройки

    private static void setFirefoxOptions() {
        //передаем настройки для firefox
        Configuration.browserCapabilities = new FirefoxOptions();
        Configuration.browserCapabilities.setCapability("selenoid:options", getSelenoidOptions());
    }

    private static void setChromeOptions() {
        Configuration.browserCapabilities = new ChromeOptions();
        Configuration.browserCapabilities.setCapability("selenoid:options", getSelenoidOptions());
    }

    private static Map<String, Boolean> getSelenoidOptions() {
        //передаем настройки для Selenoide
        Map<String, Boolean> options = new HashMap<>(); //создаем мапу
        options.put("enableVNC", true); //добавляем в нее параметр для UI
        options.put("enableLog", true); //параметр для логирования
        return options;
    }

}
