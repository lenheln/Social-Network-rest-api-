package com.example.social_network.service;

import com.example.social_network.domain.City;
import com.example.social_network.domain.Region;
import com.example.social_network.domain.User;
import com.example.social_network.dto.CityDto;
import com.example.social_network.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

/**
 * Сервисный слой для работы с сущностью City
 */
@Service
@AllArgsConstructor
@Transactional
public class CityService {

    private final CityRepository cityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Поиск городов по вхождению строки в название города
     *
     * @param name поисковая строка
     * @param pageable настройки пагинации
     * @return страница с городами
     */
    public Page<CityDto> findCityByName(String name, Pageable pageable) {
        return cityRepository.findByNameContainsIgnoreCase(name, pageable)
                             .map(this::convertToCityDto);
    }

    /**
     * Конвертирует сущность City в Dto для отображения в списке городов
     *
     * @param city
     * @return dto
     */
    public CityDto convertToCityDto(City city){
        if(city == null) {return null;}
        CityDto cityDto = new CityDto();
        cityDto.setName(city.getName());
        Region region = city.getRegion();
        if (region != null) {
            cityDto.setRegionName(region.getName());
        }
        return cityDto;
    }
}
