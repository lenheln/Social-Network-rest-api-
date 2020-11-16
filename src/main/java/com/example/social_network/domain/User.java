package com.example.social_network.domain;

import com.example.social_network.utils.Genders;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.*;

/**
 * Сущность User (пользователь)
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(min = 1, max = 45)
    @Column(name = "name")
    private String name;

    @NotNull
    @Length(min = 1, max = 45)
    @Column(name = "surname")
    private String surname;

    @Column(name = "dateofbday")
    private LocalDate dateOfBDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Genders gender;

    @Length(max = 512)
    @Column(name = "interests")
    private String interests;

    @ManyToOne
    @JoinColumn(name="cityid")
    private City city;

    @ManyToMany(cascade={CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name="friendship",
            joinColumns={@JoinColumn(name="iduser")},
            inverseJoinColumns={@JoinColumn(name="idfriend")})
    private List<User> myFriends = new ArrayList<>();

    @ManyToMany(cascade={CascadeType.PERSIST}, mappedBy = "myFriends", fetch = FetchType.LAZY)
    private List<User> friendsOfMine = new ArrayList<>();

    @Override
    public String toString(){
        return String.format("Name: %s, Surname: %s, Age: %d, Gender: %s, Interests: %s, City: %s \n",
                name, surname, Period.between(LocalDate.now(), dateOfBDay).getYears(), gender, interests, city);
    }
}
