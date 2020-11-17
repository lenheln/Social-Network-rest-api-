package com.example.social_network.service;

import com.example.social_network.domain.User;
import com.example.social_network.dto.UserByListDto;
import com.example.social_network.dto.UserEditDto;
import com.example.social_network.dto.UserPageDto;
import com.example.social_network.dto.UserRegisterDto;
import com.example.social_network.repository.UserRepository;
import com.example.social_network.service.filters.FriendFilter;
import com.example.social_network.service.filters.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервисный слой для работы с сущностью User (пользователь)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CityService cityService;

    /**
     * Создание учетной записи пользователя. Сохраняет пользователя в базе данных
     *
     * @param userDto
     * @return id пользователя
     */
    public Long save(UserRegisterDto userDto) throws Exception {
        User user = converterUserRegisterDtoToUser(userDto);
        user = userRepository.save(user);
        return user.getId();
    }

    /**
     * Получение данных пользователя по его id
     *
     * @param id
     * @return страницу пользователя
     */
    @Transactional(readOnly = true)
    public UserPageDto getUser(Long id) {
        User user = userRepository.findById(id).get();
        return convertToUserPageDto(user);
    }

    /**
     * Обновление полей пользователя
     *
     * @param userDto
     * @param id
     * @return id пользователя с обновленными полями
     */

    public void updateUser(UserEditDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setDateOfBirth(userDto.getDateOfBirth());
        user.setGender(userDto.getGender());
        user.setInterests(userDto.getInterests());
        user.setCity(userDto.getCity());
        userRepository.save(user);
    }

    /**
     * Удаляет страницу пользователя (пользователя из базы данных) с указанным id
     *
     * @param id
     */
    public void delete(Long id){
        userRepository.deleteById(id);
    }

    /**
     * Получение списка всех друзей пользователя
     *
     * @param id пользователя
     * @return сет друзей
     */
    @Transactional(readOnly = true)
    public Page<UserByListDto> getFriends(Long id, FriendFilter filter, Pageable pageable){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        filter.setUser(user);
        return userRepository
                .findAll(filter.toSpecification(), pageable)
                .map(this::convertToUserByListDto);
    }

    /**
     * Добавление друга пользователю с userId
     *
     * @param userId идентификатор пользователя
     * @param friendId идентификатор друга
     */
    public void addFriend(Long userId, Long friendId) {
        userRepository.addFriend(userId, friendId);
    }

    /**
     * Удаление друга из списка друзей
     *
     * @param userId идентификатор пользователя, который совершает действие
     * @param friendId идентификатор другя
     */
    public void deleteFriend(Long userId, Long friendId) {
        userRepository.deleteFriend(userId, friendId);
    }

    /**
     * Поиск пользователей по фильтрам
     *
     * @param filter набор условий
     * @param pageable настройки пагинации
     * @return список юзеров в виде {@UserByListDto}
     */
    @Transactional(readOnly = true)
    public Page<UserByListDto> findAll(UserFilter filter, Pageable pageable) {
        return userRepository
                .findAll(filter.toSpecification(), pageable)
                .map(this::convertToUserByListDto);
    }

    /**
     * Конвертирует сущность DTO {@UserRegisterDto } в сущность {@User}
     *
     * @param userDto
     * @return User
     */
    public User converterUserRegisterDtoToUser(UserRegisterDto userDto) throws Exception {
        return User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .build();
    }

    /**
     * Конвертирует сущность {@link User} в сущность DTO {@link UserPageDto}
     * для отображения на странице пользователя
     *
     * @param user
     * @return UserPageDto
     */
    public UserPageDto convertToUserPageDto(User user) {

        return UserPageDto.builder()
                .id(user.getId())
                .fio(String.format("%s %s", user.getName(), user.getSurname()))
                .gender(user.getGender())
                .age(user.getDateOfBirth())
                .interests(user.getInterests())
                .city(cityService.convertToCityDto(user.getCity()))
                .friends(Stream.concat(
                        user.getMyFriends().stream(),
                        user.getFriendsOfMine().stream()
                )
                        .collect(Collectors.toList())
                        .stream()
                        .map(this::convertToUserByListDto)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Конвертирует сущность {@link User} в сущность DTO {@link UserByListDto}
     * для отображения в списке друзей
     *
     * @param user
     * @return UserByListDto
     */
    public UserByListDto convertToUserByListDto(User user){
        return UserByListDto.builder()
                .id(user.getId())
                .fio(String.format("%s %s", user.getName(), user.getSurname()))
                .gender(user.getGender())
                .age(user.getDateOfBirth())
                .build();
    }
}