package com.example.social_network.controller;

import com.example.social_network.domain.City;
import com.example.social_network.domain.User;
import com.example.social_network.dto.UserEditDto;
import com.example.social_network.dto.UserRegisterDto;
import com.example.social_network.utils.Genders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  Интеграционные тесты для контроллера
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Создание учетной записи пользователя. Возвращает статус 201")
    public void createPage_ReturnOk() throws Exception {
        UserRegisterDto userDto = UserRegisterDto.builder()
                .name("Name")
                .surname("Surname")
                .dateOfBDay(LocalDate.of(1990, 3, 3))
                .gender(Genders.M)
                .city(City.builder()
                        .id(34L)
                        .name("г Москва")
                        .build())
                .build();

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User with id = 4 created"));
    }

    @Test
    @DisplayName("Создание учетной записи пользователя.Сохраняет пользователя в базе данных")
    void createPage_InValidName_ThrowException() throws Exception {
        UserRegisterDto userDto = UserRegisterDto.builder()
                .name("")
                .surname("surname")
                .build();
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", Is.is("length must be between 1 and 45")))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Получение пользователей с применением фильтра по всем полям")
    public void getUsers_WithFilter_ReturnUser() throws Exception {
        mockMvc.perform(get("/users").contentType("application/json")
                .param("page", "0")
                .param("size", "3")
                .param("name", "Маша")
                .param("surname", "Иванова")
                .param("age", "33")
                .param("gender", "F")
                .param("city.id", "1")
                .param("interests", "books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fio", Is.is("Маша Иванова")))
                .andExpect(jsonPath("$.content[0].age", Is.is(33)))
                .andExpect(jsonPath("$.content[0].gender", Is.is("F")));
    }

    @Test
    @DisplayName("Получение пользователей без фильтра. Пагинация default")
    public void getUsers_ReturnUsersList() throws Exception {
         mockMvc.perform(get("/users").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fio", Is.is("Маша Иванова")))
                .andExpect(jsonPath("$.content[0].age", Is.is(33)))
                .andExpect(jsonPath("$.content[0].gender", Is.is("F")))

                .andExpect(jsonPath("$.content[1].fio", Is.is("Пётр Петров")))
                .andExpect(jsonPath("$.content[1].age", Is.is(65)))
                .andExpect(jsonPath("$.content[1].gender", Is.is("M")))

                .andExpect(jsonPath("$.content[2].fio", Is.is("Олег Олегов")))
                .andExpect(jsonPath("$.content[2].age", Is.is(40)))
                .andExpect(jsonPath("$.content[2].gender", Is.is("M")));
    }

    @Test
    @DisplayName("Получение страницы пользователя по его id")
    public void getPage_id1_PageМаша() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio", Is.is("Маша Иванова")))
                .andExpect(jsonPath("$.age", Is.is(33)))
                .andExpect(jsonPath("$.gender", Is.is("F")))
                .andExpect(jsonPath("$.interests", Is.is("books")))
                .andExpect(jsonPath("$.city.name", Is.is("г Барнаул")));
    }

    @Test
    @DisplayName("Обновление полей на странице пользователя")
    public void updatePage_isOk() throws Exception {
        UserEditDto userDto = UserEditDto.builder()
                .name("New name")
                .surname("New surname")
                .dateOfBDay(LocalDate.of(1950, 5,5))
                .interests("Space")
                .gender(Genders.M)
                .city(City.builder().id(31L).name("г Санкт-Петербург").build())
                .build();

        mockMvc.perform(patch("/users/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery query =  cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root).where(cb.equal(root.get("id"),1));
        User updatedUser = (User) entityManager.createQuery(query).getSingleResult();

        Assertions.assertEquals(updatedUser.getName(), "New name");
        Assertions.assertEquals(updatedUser.getSurname(), "New surname");
        Assertions.assertEquals(updatedUser.getDateOfBDay(), LocalDate.of(1950, 5,5));
        Assertions.assertEquals(updatedUser.getInterests(), "Space");
        Assertions.assertEquals(updatedUser.getCity(), City.builder().id(31L).name("г Санкт-Петербург").build());
        Assertions.assertEquals(updatedUser.getGender(), Genders.M);
    }

    @Test
    @DisplayName("Обновление полей на странице пользователя")
    void updatePage_InValidAge_ThrowException() throws Exception {
        UserEditDto userDto = UserEditDto.builder()
                .name("")
                .build();
        mockMvc.perform(patch("/users/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("length must be between 1 and 45")))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @DisplayName("Удаление страницы пользователя с указанным id")
    public void deletePage_isOk() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery query =  cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(root.get("id"));
        List<Long> idList = entityManager.createQuery(query).getResultList();

        Assertions.assertFalse(idList.contains(1L));
        Assertions.assertTrue(idList.contains(2L));
        Assertions.assertTrue(idList.contains(3L));

        entityManager.close();
    }

    @Test
    @DisplayName("Добавление друга пользователю с userId")
    public void addFriend_isOk() throws Exception {
        mockMvc.perform(put("/users/2/add").param("friendId", "3"))
                .andExpect(status().isOk());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery query =  cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root.get("friendsOfMine")).where(cb.equal(root.get("id"),3));
        User user2 = (User) entityManager.createQuery(query).getSingleResult();
        Assertions.assertEquals(user2.getId(), 2);

        query.select(root.get("myFriends")).where(cb.equal(root.get("id"), 2));
        User user3 = (User) entityManager.createQuery(query).getSingleResult();
        Assertions.assertEquals(user3.getId(),3);

        entityManager.close();

    }

    @Test
    @DisplayName("Удаление друга из списка друзей")
    public void deleteFriend_isOk() throws Exception {
        mockMvc.perform(put("/users/1/friends/delete").param("friendId", "2"))
                .andExpect(status().isOk());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery query =  cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root.get("friendsOfMine")).where(cb.equal(root.get("id"),2));
        Assertions.assertThrows(NoResultException.class,
                () -> entityManager.createQuery(query).getSingleResult());

        query.select(root.get("myFriends")).where(cb.equal(root.get("id"),1));
        Assertions.assertThrows(NoResultException.class,
                () -> entityManager.createQuery(query).getSingleResult());

        entityManager.close();
    }

    @Test
    @DisplayName("Получение списка друзей пользователя с помощью фильтра")
    public void getFriends_FriendList() throws Exception {
        mockMvc.perform(get("/users/1/friends").contentType("application_json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fio", Is.is("Пётр Петров")))
                .andExpect(jsonPath("$.content[0].age", Is.is(65)))
                .andExpect(jsonPath("$.content[0].gender", Is.is("M")))

                .andExpect(jsonPath("$.content[1].fio", Is.is("Олег Олегов")))
                .andExpect(jsonPath("$.content[1].age", Is.is(40)))
                .andExpect(jsonPath("$.content[1].gender", Is.is("M")));
    }
}
