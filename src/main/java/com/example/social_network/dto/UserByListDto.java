package com.example.social_network.dto;

import com.example.social_network.utils.Genders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Dto для отображения пользователя в каком-либо списке
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserByListDto {

    private String fio;

    private Genders gender;

    private Integer age;
}