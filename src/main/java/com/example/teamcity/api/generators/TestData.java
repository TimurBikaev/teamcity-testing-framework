// Генерируемые тестовые данные
package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedUser;
import com.example.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

@Builder // Ломбок: создает паттерн Builder для удобного создания экземпляров класса
@Data // Ломбок: генерирует геттеры, сеттеры, методы equals() и hashCode(), а также метод toString()
public class TestData {
    private User user; // Пользовательские данные
    private NewProjectDescription project; // Данные о новом проекте
    private BuildType buildType; // Данные о типе сборки

    // Метод для удаления сгенерированных данных
    public void delete() {
        var spec = Specifications.getSpec().authSpec(user); // Получаем спецификацию для авториз. пользователя
        new UncheckedProject(spec).delete(project.getId()); // Удаляем проект непроверяемым методом, т.к. нам всё равно на код *это попытка удаления)
        new UncheckedUser(spec).delete(user.getUsername()); // Удаляем пользователя
    }
}
