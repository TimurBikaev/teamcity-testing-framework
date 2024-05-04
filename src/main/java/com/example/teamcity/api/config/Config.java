package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс Config предоставляет доступ к настройкам, загруженным из файла config.properties.
 * Этот класс распарсит нам значения из файла config.properties
 */
public class Config {

    // Название файла с настройками
    private final static String CONFIG_PROPERTIES = "config.properties";

    // Объект для хранения загруженных настроек из stream
    private Properties properties;

    // По аналогии со Specifications: Синглтон для доступа к единственному экземпляру Config
    //должен быть только один экземпляр
    private static Config config;

    // Приватный конструктор для создания экземпляра Config и загрузки настроек из файла
    private Config() {
        properties = new Properties(); //создаем свойства
        loadProperties(CONFIG_PROPERTIES); //и затем явно вызываем из файла-константы
    }

    // Получение единственного экземпляра Config
    private static Config getConfig() {
        if(config == null) { //если объект не существует
            config = new Config(); //создаем новый экземпляр
        }
        return config; //и возвращаем в любом случае - существующий или только созданный
    }

    // Выгрузка свойств из файла и сохранение в объекте properties
    //Вызываем в самом начале проекта, когда обращаемся к конфигу
    public void loadProperties(String filename) {
        //дергаем загрузчик текущего класса и по названию файла забираем что внутри
        try(InputStream stream = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if(stream == null) { //если стрима нет, распечатываем ошибку
                System.err.println("Config file " + filename + " not found");
            }
            properties.load(stream); //а если все норм, загружаем в переменную
        } catch (IOException e) {
            System.err.println("Error during file reading " + filename);
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение значения свойства из загруженных настроек по его имени.
     *
     * @param key Имя свойства
     * @return Значение свойства
     */
    public static String getProperty(String key) {
        // Получение экземпляра Config и возврат значения свойства по ключу
        return getConfig().properties.getProperty(key); //перед вызовом properties вызываем getConfig(), иначе упадем
    }
}
