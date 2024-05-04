package com.example.teamcity.api.generators;

import java.util.ArrayList;
import java.util.List;

// Класс для хранения и управления тестовыми данными
public class TestDataStorage {
    private static TestDataStorage testDataStorage;
    private List<TestData> testDataList; // Список тестовых данных для хранения

    // Приватный конструктор для создания экземпляра класса TestDataStorage
    private TestDataStorage() {
        this.testDataList = new ArrayList<>(); // Создание нового списка при инициализации
    }

    // Метод для получения единственного экземпляра класса TestDataStorage (синглтон)
    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage(); // Создание экземпляра, если он не существует
        }
        return testDataStorage;
    }

    // Метод для добавления новых тестовых данных с автоматической генерацией
    public TestData addTestData() {
        var testData = TestDataGenerator.generate(); // Генерация новых тестовых данных
        addTestData(testData); // Добавление в хранилище
        return testData; // Возвращение сгенерированных данных
    }

    // Метод для добавления переданных тестовых данных в хранилище
    public TestData addTestData(TestData testData) {
        getStorage().testDataList.add(testData); // Добавление в список хранилища
        return testData; // Возвращение добавленных данных
    }

    // Метод для удаления всех тестовых данных из хранилища
    public void delete() {
        testDataList.forEach(TestData::delete); // Итерация по списку и удаление каждого элемента
    }
}
