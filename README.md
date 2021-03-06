Задача: 
Написать RestApi Социальная Сеть
 
Нефункциональные требования:
• Spring Boot 2.3.4
• Gradle
• Java 11
• Spring Web
• Spring Data JPA
• PostgreSQL
• Flyway
• Lombok
• JUnit 5 
• Mockito
• Spring Boot Test
• Postman (manual testing)

Цель: 
• Создать REST сервис с помощью которого можно будет просматривать и создавать анкеты в социальной сети. 
• Функциональные требования:  - Страница регистрации, где указывается следующая информация:   1) Имя   2) Фамилия   3) Возраст   4) Пол   5) Интересы   6) Город  - Страницы с анкетой
• Есть возможность регистрации, создавать персональные страницы, возможность подружиться, список друзей.
• Отсутствуют SQL-инъекции.
• Должны быть написаны Unit-тесты для уровня Service, Controller. 
• Должны быть написаны Integration-тесты для Controller. 
• Для того чтобы вручную проверить, что сервис работает, воспользуйтесь Postman утилитой.

Результат:
Реализованы следующие функции:
- CRUD операции с персональной страницей пользователя
- Добавление и удаление пользователя в / из друзей
- Получение страниц всех пользователей
- Поиск по пользователям по вхождению строки в имя / фамилию, по диапозону для возраста, городу, полу 
- Получение списка друзей пользователя
