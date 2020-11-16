package com.example.social_network.controller;

import com.example.social_network.dto.CityOnUserPageDto;
import com.example.social_network.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Контроллер для работы с сущностью City
 */
@RestController
@RequestMapping("/cities")
@AllArgsConstructor
@Slf4j
@Api(description = "Контроллер для работы с сущностью City")
public class CityController {

    private final CityService cityService;

    @GetMapping
    @ApiOperation("Получение списка городов")
    public Page<CityOnUserPageDto> getCities(@RequestParam(required = false) String name,
                                             @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {
        return cityService.findCityByName(name, pageable);
    }
}
