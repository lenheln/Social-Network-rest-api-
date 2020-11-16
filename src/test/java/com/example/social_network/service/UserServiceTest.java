package com.example.social_network.service;

import com.example.social_network.domain.City;
import com.example.social_network.domain.User;
import com.example.social_network.dto.*;
import com.example.social_network.repository.UserRepository;
import com.example.social_network.service.filters.FriendFilter;
import com.example.social_network.service.filters.UserFilter;
import com.example.social_network.utils.Genders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CityService cityService;


    private UserService userService;
    private User user;
    private City city;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserService(userRepository, cityService);

        city =  City.builder()
                .id(34L)
                .name("г Москва")
                .build();

        user = User.builder()
                .id(1L)
                .name("Name")
                .surname("Surname")
                .dateOfBDay(LocalDate.of(1987, 7, 21))
                .city(city)
                .gender(Genders.F)
                .interests("Nothing")
                .myFriends(new ArrayList<>())
                .friendsOfMine(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Создание учетной записи пользователя. Сохраняет пользователя в базе данных")
    void save_ReturnId() throws Exception {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Assertions.assertEquals(user.getId(), userService.save(new UserRegisterDto()));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Получение данных пользователя по его id")
    void getUser_ReturnUserPageDto() {
        UserPageDto userDto = userService.convertToUserPageDto(user);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        Assertions.assertEquals(userDto, userService.getUser(1L));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Обновление полей пользователя")
    void updateUser_UserEditDto_ReturnDtoWithEditedFields() throws Exception {

        City newCity = City.builder().id(31L).name("г Санкт-Петербург").build();
        UserEditDto userEditDto = UserEditDto.builder()
                .name("New name")
                .surname("New surname")
                .dateOfBDay(LocalDate.of(1993, 3, 3))
                .city(newCity)
                .gender(Genders.M)
                .interests("New interests")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        userService.updateUser(userEditDto, 1L);

        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("New name", user.getName());
        Assertions.assertEquals("New surname", user.getSurname());
        Assertions.assertEquals(LocalDate.of(1993,3,3), user.getDateOfBDay());
        Assertions.assertEquals(Genders.M, user.getGender());
        Assertions.assertEquals(newCity, user.getCity());
        Assertions.assertEquals("New interests", user.getInterests());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Удаляет страницу пользователя (пользователя из базы данных) с указанным id")
    void delete_Ok() {
        Mockito.doNothing().when(userRepository).deleteById(Mockito.anyLong());
        userService.delete(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Получение списка всех друзей пользователя")
    void getFriends_ReturnPageOfUserFriends() {
        User user = User.builder()
                .name("Name")
                .surname("Surname")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(user));

        User friend = User.builder()
                .name("Friend name")
                .surname("Friend surname")
                .build();

        List<User> friendList = List.of(friend);
        Page friendsPage = new PageImpl(friendList);
        Mockito.when(userRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(friendsPage);

        UserByListDto friendDto = UserByListDto.builder()
                .fio("Friend name Friend surname")
                .build();

        Mockito.when(userService.convertToUserByListDto(Mockito.any(User.class))).thenReturn(friendDto);

        List<UserByListDto> friendsDtoList = new ArrayList<>();
        friendList.forEach((f) -> friendsDtoList.add(userService.convertToUserByListDto(f)));
        Page friendsDtoPage = new PageImpl(friendsDtoList);

        FriendFilter filter = new FriendFilter(user);
        Pageable pageable = Pageable.unpaged();
        Assertions.assertEquals(friendsDtoPage, userService.getFriends(1L, filter, pageable));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).findAll(
                Mockito.any(Specification.class),
                Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Добавление друга пользователю с userId")
    void addFriend_Ok() {
        Mockito.when(userRepository.addFriend(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
        userService.addFriend(1L, 2L);

        Mockito.verify(userRepository, Mockito.times(1)).addFriend(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Удаление друга из списка друзей")
    void deleteFriend_Ok() {
        Mockito.when(userRepository.deleteFriend(Mockito.anyLong(), Mockito.anyLong())).thenReturn(1);
        userService.deleteFriend(1L, 2L);

        Mockito.verify(userRepository, Mockito.times(1)).deleteFriend(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Поиск пользователей по фильтрам")
    void findAll_ReturnUsersList() {
        List<User> users = new ArrayList<>();
        users.add(user);
        Page usersPage = new PageImpl<>(users);

        Mockito.when(userRepository.findAll(
                Mockito.any(Specification.class),
                Mockito.any(Pageable.class)
        )).thenReturn(usersPage);

        List<UserByListDto> usersDto = new ArrayList<>();
        usersDto.add(userService.convertToUserByListDto(user));
        Page usersDtoPage = new PageImpl<>(usersDto);

        Pageable pageable =  Pageable.unpaged();
        Assertions.assertEquals(usersDtoPage, userService.findAll(new UserFilter(), pageable));

        Mockito.verify(userRepository, Mockito.times(1)).findAll(
                Mockito.any(Specification.class),
                Mockito.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Конвертирует сущность DTO {@UserRegisterDto } в сущность {@User}")
    void converterUserRegisterDtoToUser() throws Exception {

        City city = City.builder()
                        .id(34L)
                        .name("г Москва")
                        .build();

        UserRegisterDto userDto = UserRegisterDto.builder()
                .name("Name")
                .surname("Surname")
                .dateOfBDay(LocalDate.of(1987, 7, 21))
                .city(city)
                .gender(Genders.F)
                .build();

        User user = userService.converterUserRegisterDtoToUser(userDto);

        Assertions.assertEquals("Name", user.getName());
        Assertions.assertEquals("Surname", user.getSurname());
        Assertions.assertEquals(LocalDate.of(1987, 7, 21), user.getDateOfBDay());
        Assertions.assertEquals(city, user.getCity());
        Assertions.assertEquals(Genders.F, user.getGender());
    }

    @Test
    @DisplayName("Конвертирует сущность User в сущность UserPageDto")
    void convertToUserPageDto() {
        CityOnUserPageDto cityDto = CityOnUserPageDto.builder()
                .name("г Москва")
                .build();
        Mockito.when(cityService.convertToCityOnPageDto(Mockito.any(City.class))).thenReturn(cityDto);

        UserPageDto userDto = userService.convertToUserPageDto(user);
        Assertions.assertEquals("Name Surname", userDto.getFio());
        Assertions.assertEquals(
                Period.between(
                        user.getDateOfBDay(),
                        LocalDate.now()
                ).getYears(),
                userDto.getAge()
        );
        Assertions.assertEquals(cityDto, userDto.getCity());
        Assertions.assertEquals(Genders.F, userDto.getGender());
        Assertions.assertEquals("Nothing", userDto.getInterests());
    }

    @Test
    @DisplayName("Конвертирует сущность User в сущность UserByListDto")
    void convertToUserByListDto() {
        UserByListDto userDto = userService.convertToUserByListDto(user);

        Assertions.assertEquals("Name Surname", userDto.getFio());
        Assertions.assertEquals(
                Period.between(
                        user.getDateOfBDay(),
                        LocalDate.now()
                ).getYears(),
                userDto.getAge()
        );
        Assertions.assertEquals(Genders.F, userDto.getGender());
    }
}