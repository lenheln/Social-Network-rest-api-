package com.example.social_network.dto;

import com.example.social_network.utils.Genders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;

/**
 * Dto для отображения пользователя в каком-либо списке
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserByListDto {

    private Long id;

    private String fio;

    private Genders gender;

    private Integer age;

    public static class UserByListDtoBuilder{
        Integer age;

        public UserByListDtoBuilder age(LocalDate dateOfBirth){
            this.age = (dateOfBirth == null)
                    ? null
                    : Period.between(dateOfBirth, LocalDate.now()).getYears();
            return this;
        }
    }
}