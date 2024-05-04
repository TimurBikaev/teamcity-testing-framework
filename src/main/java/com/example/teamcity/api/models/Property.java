package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private String name; // Название свойства
    private Boolean value; // Значение свойства

    // Метод для создания карты свойств LDAP
    public static Map<String, List<Property>> ldapProperty() {
        return new HashMap<>() {{
            put("property", singletonList(new Property("allowCreatingNewUsersByLogin", true))); // Добавляем свойство LDAP
        }};
    }

    // Метод для создания карты свойств по умолчанию
    public static Map<String, List<Property>> defaultProperty() {
        return new HashMap<>() {{
            put("property", // Устанавливаем ключ "property"
                    Arrays.asList( // Задаем список свойств
                            new Property("usersCanResetOwnPasswords", true),
                            new Property("usersCanChangeOwnPasswords", true),
                            new Property("usersCanChangeOwnPasswords", false))
            );
        }};
    }
}



