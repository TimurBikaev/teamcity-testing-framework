package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Аннотации для Ломбока чтобы иметь доступ к указанным в классе полям
@Builder // Генерация билдера. Аннотация реализует паттерн проектирования Билдер. Позволяет удобно наполнять дата-класс
@Data // Генерация геттеров, сеттеров, equals, hashCode и toString
@NoArgsConstructor // Генерация конструктора без аргументов
@AllArgsConstructor // Генерация конструктора с аргументами
public class User_new { //класс хранения (дата-класс) данных о юзере, содержит поля:
    //название полей надо сверить с документацией, правильно ли в апи названы эти параметры Юзера
    private String username; // Имя пользователя
    private String password; // Пароль
    private String email; // Электронная почта
    private Roles roles; // Роли пользователя

    //вот пример геттера для такого класса, и чтобы их не создавать - добавляем аннотации Ломбок
//и они автоматом существуют
//    public String getUsername() {
//        return username;
//    }
    //создавать можно в этом же классе на ПКМ-Generate-Getters
}




