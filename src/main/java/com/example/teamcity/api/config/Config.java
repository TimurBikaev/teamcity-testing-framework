package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Config config;

    //название файла
    private final static String CONFIG_PROPERTIES = "config.properties";

    //переменная для хранения
    private Properties properties;

    //тут тоже синглтон

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES); //загружаем
    }

    private static Config getConfig() {
        if(config == null) {
            config = new Config();
        }
        return config;
    }

    //Выгружаем из файла properties в самом начале, на вызове конструктора

    public void loadProperties (String filename) {
        try(InputStream stream = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if(stream == null) {
                System.err.println("Config file " + filename + " not found");
            }
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("Error during file reading " + filename);
            throw new RuntimeException(e);
        }
    }

    //метод для доступа к пропертям по имени
    public static String getProperty (String key) {
        //предварительно вызвав конфиг
        return getConfig().properties.getProperty(key);
    }


}
