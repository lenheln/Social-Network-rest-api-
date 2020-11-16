package com.example.social_network.domain;

import lombok.Data;

import javax.persistence.*;

/**
 *  Сущность регион
 */
@Entity
@Table(name = "regions")
@Data
public class Region {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;
}
