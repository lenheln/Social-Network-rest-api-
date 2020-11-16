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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    private String fio;

    private Integer age;

    private Genders gender;

    private String interests;

    private CityOnUserPageDto city;

    private List<UserByListDto> friends = new ArrayList<>();
}