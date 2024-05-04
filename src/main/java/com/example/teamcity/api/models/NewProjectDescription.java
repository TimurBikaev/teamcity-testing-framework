package com.example.teamcity.api.models;

import lombok.AllArgsConstructor; // Импорт аннотации Lombok для генерации конструктора с аргументами
import lombok.Builder; // Импорт аннотации Lombok для генерации билдера
import lombok.Data; // Импорт аннотации Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
import lombok.NoArgsConstructor; // Импорт аннотации Lombok для генерации конструктора без аргументов

@Builder // Аннотация Lombok для генерации билдера
@Data // Аннотация Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
@AllArgsConstructor // Аннотация Lombok для генерации конструктора с аргументами
@NoArgsConstructor // Аннотация Lombok для генерации конструктора без аргументов
public class NewProjectDescription {
    private Project parentProject; // Родительский проект
    private String name; // Имя проекта
    private String id; // Идентификатор проекта
    private Boolean copyAllAssociatedSettings; // Флаг копирования всех связанных настроек
}

