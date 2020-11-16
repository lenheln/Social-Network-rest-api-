package com.example.social_network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Dto для сущности City для отображения в списке городов при поиске города
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CityOnUserPageDto {
    private String name;
    private String regionName;
}
