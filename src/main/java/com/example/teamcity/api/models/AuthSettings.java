package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

// Модель для хранения настроек авторизации
@Builder // Аннотация Lombok для создания паттерна Builder
@Data // Аннотация Lombok для генерации геттеров, сеттеров, методов equals, hashCode и toString
@AllArgsConstructor // Аннотация Lombok для генерации конструктора с аргументами
@NoArgsConstructor // Аннотация Lombok для генерации конструктора по умолчанию
public class AuthSettings {
    private Boolean perProjectPermissions; // Разрешения на уровне проекта
    private Map<String, List<Module>> modules; // Модули с настройками
}
