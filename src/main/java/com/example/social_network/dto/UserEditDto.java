package com.example.social_network.dto;

import com.example.social_network.domain.City;
import com.example.social_network.utils.Genders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Dto для сущности User (пользователь) для обновления страницы
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    @Length(min = 1 , max = 45)
    private String name;

    @Length(min = 1,  max = 45)
    private String surname;

    private LocalDate dateOfBDay;

    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Length(max = 512)
    private String interests;

    private City city;

}