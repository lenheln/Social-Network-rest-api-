package com.example.social_network.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 *  Сущность город
 */

@Entity
@Table(name = "cities")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "regionid")
    private Region region;
}