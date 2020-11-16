package com.example.social_network.service.Specification;
import com.example.social_network.domain.City;
import com.example.social_network.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.time.LocalDate;

/**
 * Базовый класс спецификаций
 */
public class BaseSpecification {

    /**
     * Поиск по вхождению
     *
     * @param column колонка таблицы сущности T
     * @param value значение для поиска
     * @return спецификация
     */
    public static <T> Specification<T> like(final String column, final String value) {
        return StringUtils.isEmpty(column) || StringUtils.isEmpty(value) || value.equals(null)
                ? null
                : (root, query, cb) ->
                cb.like(
                        cb.lower(root.get(column)),
                        cb.lower(cb.literal("%"+ value + "%"))
                );
    }

    /**
     * Поиск по эквивалентности полю
     *
     * @param column колонка таблицы сущности T
     * @param value значение для сравнения
     * @return спецификация
     */
    public static <T> Specification<T> equal(final String column, final String value) {
        return StringUtils.isEmpty(column) || StringUtils.isEmpty(value) || value.equals(null)
                ? null
                : (root, query, cb) ->
                cb.equal(
                        cb.lower(root.get(column)),
                        cb.lower(cb.literal(value))
                );
    }

    /**
     * Поиск по эквивалентности полю
     *
     * @param joinAttribute поле сущности, по которому будет join
     * @param column колонка таблицы сущности T
     * @param value значение для сравнения
     * @return спецификация
     */
    public static <T> Specification<T> like(final String joinAttribute, final String column, final String value) {
        return StringUtils.isEmpty(column) || ObjectUtils.isEmpty(value)
                ? null
                : (root, query, cb) ->
                cb.like(
                    cb.lower(root.join(joinAttribute, JoinType.LEFT).get(column)),
                    cb.lower(cb.literal("%"+ value + "%"))
                );
    }

    /**
     * Поиск по эквивалентности полю
     *
     * @param column колонка таблицы сущности T
     * @param value Enum для сравнения равенства
     * @return спецификация
     */
    public static <T> Specification<T> equal(final String column, final Enum value) {
        return StringUtils.isEmpty(column) || ObjectUtils.isEmpty(value)
                ? null
                : (root, query, cb) ->
                cb.equal(root.get(column), value);
    }

    /**
     * Поиск сущностей, у которых значение поля больше заданного
     *
     * @param column колонка таблицы сущности T
     * @param min значение для сравнения
     * @return спецификация
     */
    public  static <T> Specification<T> gt(final String column, final Integer min) {
        return StringUtils.isEmpty(column) || min == null
                ? null
                : (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("dateOfBDay"), LocalDate.now().minusYears(min));    }

    /**
     * Поиск сущностей, у которых значение поля больше меньше
     *
     * @param column колонка таблицы сущности T
     * @param max значение для сравнения
     * @return спецификация
     */
    public static <T> Specification <T> lt(final String column, final Integer max) {
        return StringUtils.isEmpty(column) || max == null
                ? null
                : (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("dateOfBDay"), LocalDate.now().minusYears(max));
    }

    /**
     * Поиск сущностей, у которых в друзьях есть данный пользователь
     *
     * @param user пользователь
     * @return спецификация
     */
    public static Specification<User> isFriend(User user){
        return (root, query , cb) -> {
            Predicate userPr = cb.isMember(user, root.get("friendsOfMine"));
            Predicate friendPr = cb.isMember(user, root.get("myFriends"));
            return cb.or(userPr,friendPr);
        };
    }
}
