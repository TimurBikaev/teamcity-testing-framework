package com.example.teamcity.api.enums;

public enum Role {
    // Перечисление ролей пользователей в системе

    SYSTEM_ADMIN("SYSTEM_ADMIN"), // Роль системного администратора
    PROJECT_ADMIN("PROJECT_ADMIN"), // Роль администратора проекта
    PROJECT_DEVELOPER("PROJECT_DEVELOPER"), // Роль разработчика проекта
    PROJECT_VIEWER("PROJECT_VIEWER"), // Роль наблюдателя проекта
    AGENT_MANAGER("AGENT_MANAGER"); // Роль менеджера агента

    private String text; // Текстовое представление роли

    Role(String text) {
        // Конструктор с параметром текстового представления роли
        this.text = text;
    }

    public String getText() {
        // Метод для получения текстового представления роли
        return text;
    }
}

