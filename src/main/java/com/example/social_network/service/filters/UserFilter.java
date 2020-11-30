package com.example.social_network.service.filters;

import com.example.social_network.domain.User;
import com.example.social_network.service.Specification.BaseSpecification;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

/**
 * Получает всех пользователей, кроме самого пользователя и его друзей
 */
@Setter
public class UserFilter extends BaseFilter {

    /**
     * Пользователь от лица которого производится поиск
     */
    private User user;

    /**
     * Спецификация
     * @return
     */
    @Override
    public Specification<User> toSpecification() {
        return BaseSpecification.notFriend(user).and(super.toSpecification());
    }
}
