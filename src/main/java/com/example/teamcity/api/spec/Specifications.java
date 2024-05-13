package com.example.teamcity.api.spec;
// Импорт необходимых классов и пакетов для работы с Rest-Assured и конфигурацией

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static com.example.teamcity.api.config.Config.getProperty;

// Класс для создания спецификаций запросов с использованием Rest-Assured
public class Specifications {

        /*
        Преимущества использования этой спецификации:

    Стандартизация запросов: Все запросы в проекте будут иметь одинаковые базовые параметры (URI, тип контента, логирование).
    Повторное использование: Спецификации можно повторно использовать в разных тестах, что упрощает код и повышает его читаемость.
    Удобство: Предоставлены методы для создания спецификаций для разных типов пользователей, что упрощает работу с авторизацией.
    Гибкость: Можно легко модифицировать код спецификации для добавления новых функций или изменения поведения.
     */

    // Приватный конструктор, чтобы запретить создание экземпляров Specifications класса извне
    private Specifications() {
    }

    private static Specifications spec; // Приватная переменная для хранения единственного экземпляра Specifications

    //Как забрать единственный созданный экземпляр?
    // Метод для получения единственного экземпляра Specifications (реализация паттерна Singleton)
    public static Specifications getSpec() {
        if (spec == null) { //если спека еще не существует, то создаем новый экз.
            spec = new Specifications();
        }
        return spec; //возвращаем спек в любом случае
    }

    // Приватный метод для создания экземпляра RequestSpecBuilder с базовыми параметрами
    /*Создание экземпляра RequestSpecBuilder с базовыми параметрами для запросов Rest-Assured.
    Определение общих настроек, которые будут применяться ко всем запросам в проекте. */
    private RequestSpecBuilder reqBuilder() {
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri("http://" + getProperty("host")); // Установка базового URI из конфигурации
        //Эти фильтры будут записывать информацию о запросах и ответах в консоль или файл журнала, что может быть полезно для отладки.
        requestBuilder.addFilter(new RequestLoggingFilter()); // Добавление фильтра для логирования запросов
        requestBuilder.addFilter(new ResponseLoggingFilter()); // Добавление фильтра для логирования ответов
        //Это сообщает серверу, что клиент ожидает JSON-ответы.
        requestBuilder.setContentType(ContentType.JSON); // Установка типа контента на JSON
        requestBuilder.setAccept(ContentType.JSON); // Установка заголовка Accept на JSON
        return requestBuilder;
        /*Возвращает построенный RequestSpecBuilder, который можно использовать
         для создания спецификаций запросов для разных типов пользователей
         (неавторизованные, авторизованные, суперпользователь). */

        /* Почему так сделано:
    Стандартизация запросов:
    Все запросы в проекте будут иметь одинаковые базовые параметры (URI, тип контента, логирование).
    Это делает код более читаемым, единообразным и упрощает его сопровождение.
    Повторное использование:
    reqBuilder() можно использовать повторно в разных методах класса Specifications.
    Это позволяет избежать дублирования кода и повышает его читаемость.
    Гибкость:
    В reqBuilder() можно легко добавить или изменить параметры, чтобы соответствовать вашим конкретным потребностям.
    Например, вы можете изменить тип контента на XML или добавить пользовательские заголовки.
    Удобство:
    reqBuilder() упрощает создание спецификаций запросов, предоставляя все необходимые настройки в одном месте. */
    }

    // Метод для создания спецификации запросов для неавторизованного пользователя
    public RequestSpecification unauthSpec() {
        var requestBuilder = reqBuilder(); // Создание экземпляра RequestSpecBuilder
        return requestBuilder.build(); // Возвращение построенной спецификации
    }

    // Метод для создания спецификации запросов для авторизованного пользователя (которого уже созданного надо передать)
    public RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder(); // Создание экземпляра RequestSpecBuilder
        // Формирование базового URI с учетом имени пользователя и пароля из объекта User (так как установили его модели Геттеры)
        requestBuilder.setBaseUri("http://" + user.getUsername() + ":" + user.getPassword() + "@" + getProperty("host"));
        return requestBuilder.build(); // Возвращение построенной спецификации
    }

    // Метод для создания спецификации запросов от имени суперпользователя
    public RequestSpecification superUserSpec() {
        var requestBuilder = reqBuilder(); // Создание экземпляра RequestSpecBuilder
        // Формирование базового URI с учетом токена суперпользователя из конфигурации (токен берем из логов запуска контейнера Сервера ТимСити)
        requestBuilder.setBaseUri("http://:" + Config.getProperty("superUserToken") + "@" + Config.getProperty("host"));
        //хост и порт храним в config.properties в который ходим через .Config
        return requestBuilder.build(); // Возвращение построенной спецификации
    }
}





