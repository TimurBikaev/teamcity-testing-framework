package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder // Аннотация Lombok для генерации билдера
@Data // Аннотация Lombok для генерации геттеров, сеттеров, equals, hashCode и toString
@NoArgsConstructor // Аннотация Lombok для генерации конструктора без аргументов
@AllArgsConstructor // Аннотация Lombok для генерации конструктора с аргументами
public class Role_new {
    private String roleId; // Идентификатор роли
    private String scope; // Область роли
    private String href; // Ссылка на роль
}

