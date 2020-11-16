package com.example.social_network.controller;

import com.example.social_network.domain.User;
import com.example.social_network.dto.UserByListDto;
import com.example.social_network.dto.UserEditDto;
import com.example.social_network.dto.UserPageDto;
import com.example.social_network.dto.UserRegisterDto;
import com.example.social_network.service.UserService;
import com.example.social_network.service.filters.FriendFilter;
import com.example.social_network.service.filters.UserFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Юнит-тесты контроллера
 */

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userController = new UserController(userService);
    }

    @Test
    @DisplayName("Создание учетной записи пользователя. Сохраняет пользователя в базе данных")
    void createPage_Ok() throws Exception {

        Mockito.when(userService.save(Mockito.any(UserRegisterDto.class))).thenReturn(1L);
        ResponseEntity answer =  userController.createPage(new UserRegisterDto());

        Assertions.assertEquals(201, answer.getStatusCodeValue());
        Assertions.assertEquals("User with id = 1 created", answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).save(Mockito.any(UserRegisterDto.class));
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Поиск пользователей с помощью фильтра")
    void getUsers_Ok() {
        List<UserByListDto> listDtos = new ArrayList<>();
        listDtos.add( new UserByListDto());
        Page<UserByListDto> dtoPage = new PageImpl<UserByListDto>(listDtos);

        Mockito.when(userService.findAll(
                Mockito.any(UserFilter.class),
                Mockito.any(Pageable.class)
        )).thenReturn(dtoPage);

        Pageable pageable =  Pageable.unpaged();
        ResponseEntity answer = userController.getUsers(new UserFilter(), pageable);

        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals(dtoPage, answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).findAll(
                Mockito.any(UserFilter.class),
                Mockito.any(Pageable.class)
        );
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Получение страницы пользователя по его id")
    void getPage_Ok() {
        UserPageDto userDto = UserPageDto.builder()
                .fio("Test test")
                .build();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(userDto);
        ResponseEntity answer = userController.getPage(1L);

        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals(userDto, answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).getUser(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Обновление полей на странице пользователя")
    void updatePage_Ok() throws Exception {

        Mockito.doNothing().when(userService).updateUser(
                Mockito.any(UserEditDto.class),
                Mockito.anyLong());
        ResponseEntity answer = userController.updatePage(new UserEditDto(), 1L);
        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals("User with id = 1 updated", answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).updateUser(Mockito.any(UserEditDto.class), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Удаление страницы пользователя с указанным id")
    void deletePage_Ok() {
        Mockito.doNothing().when(userService).delete(Mockito.anyLong());
        ResponseEntity answer = userController.deletePage(1L);
        Assertions.assertEquals(200, answer.getStatusCodeValue());

        Mockito.verify(userService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Добавление друга пользователю с userId")
    void addFriend_Ok() {
        Mockito.doNothing().when(userService).addFriend(Mockito.anyLong(), Mockito.anyLong());
        ResponseEntity answer = userController.addFriend(1L, 2L);
        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals("User with id = 2 added as a friend", answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).addFriend(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Получение списка друзей пользователя с помощью фильтра")
    void getFriends_Ok() {

        List<UserByListDto> users = List.of(new UserByListDto());
        Page<UserByListDto> page = new PageImpl<>(users);

        Mockito.when(userService.getFriends(
                Mockito.anyLong(),
                Mockito.any(FriendFilter.class),
                Mockito.any(Pageable.class)
        )).thenReturn(page);
        Pageable pageable =  Pageable.unpaged();
        ResponseEntity answer = userController.getFriends(1L, new FriendFilter(new User()), pageable);

        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals(page, answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).getFriends(
                Mockito.anyLong(),
                Mockito.any(FriendFilter.class),
                Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    @DisplayName("Удаление друга из списка друзей")
    void deleteFriend_Ok() {
        Mockito.doNothing().when(userService).deleteFriend(Mockito.anyLong(), Mockito.anyLong());
        ResponseEntity answer = userController.deleteFriend(1L, 2L);
        Assertions.assertEquals(200, answer.getStatusCodeValue());
        Assertions.assertEquals("User with id = 2 deleted from friends", answer.getBody());

        Mockito.verify(userService, Mockito.times(1)).deleteFriend(Mockito.anyLong(),Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userService);
    }
}