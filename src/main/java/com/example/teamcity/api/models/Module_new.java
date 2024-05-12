package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

// Модель для представления модулей настроек
@Builder // Аннотация Lombok для создания паттерна Builder
@Data // Аннотация Lombok для генерации геттеров, сеттеров, методов equals, hashCode и toString
@AllArgsConstructor // Аннотация Lombok для генерации конструктора с аргументами
@NoArgsConstructor // Аннотация Lombok для генерации конструктора по умолчанию
public class Module_new {
    private String name; // Название модуля
    private Map<String, List<Property>> properties; // Настройки модуля

    // Метод для создания модуля с настройками по умолчанию
    public static Module_new defaultModule() {
        return Module_new.builder()
                .name("Default") // Устанавливаем название по умолчанию
                .properties(Property.defaultProperty()) // Устанавливаем свойства по умолчанию
                .build();
    }

    // Метод для создания модуля HTTP Basic
    public static Module_new httpBasic() {
        return Module_new.builder().name("HTTP-Basic").build(); // Устанавливаем название модуля HTTP-Basic
    }

    // Метод для создания модуля LDAP
    public static Module_new ldapModule() {
        return Module_new.builder()
                .name("LDAP") // Устанавливаем название модуля LDAP
                .properties(Property.ldapProperty()) // Устанавливаем свойства LDAP
                .build();
    }

    // Метод для создания модуля аутентификации по токену
    public static Module_new tokenAuthModule() {
        return Module_new.builder().name("Token-Auth").build(); // Устанавливаем название модуля аутентификации по токену
    }
}
