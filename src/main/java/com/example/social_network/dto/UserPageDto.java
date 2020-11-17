package com.example.social_network.dto;

import com.example.social_network.utils.Genders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/*
    Dto для отображения на странице пользователя
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {

    private Long id;

    private String fio;

    private Integer age;

    private Genders gender;

    private String interests;

    private CityDto city;

    private List<UserByListDto> friends = new ArrayList<>();

    public static class UserPageDtoBuilder{
        private Integer age;

        public UserPageDtoBuilder age(LocalDate dateOfBirth){
            this.age = (dateOfBirth == null)
                    ? null
                    : Period.between(dateOfBirth, LocalDate.now()).getYears();
            return this;
        }
    }
}

