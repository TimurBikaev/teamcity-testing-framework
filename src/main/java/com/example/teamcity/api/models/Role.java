package com.example.teamcity.api.models;

import lombok.AllArgsConstructor; // Импорт аннотации Lombok для генерации конструктора с аргументами
import lombok.Builder; // Импорт аннотации Lombok для генерации билдера
import lombok.Data; // Импорт аннотации Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
import lombok.NoArgsConstructor; // Импорт аннотации Lombok для генерации конструктора без аргументов

@Builder // Аннотация Lombok для генерации билдера
@Data // Аннотация Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
@NoArgsConstructor // Аннотация Lombok для генерации конструктора без аргументов
@AllArgsConstructor // Аннотация Lombok для генерации конструктора с аргументами
public class Role {
    private String roleId; // Идентификатор роли
    private String scope; // Область роли
    private String href; // Ссылка на роль
}

