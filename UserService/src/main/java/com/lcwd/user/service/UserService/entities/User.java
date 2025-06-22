package com.lcwd.user.service.UserService.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "ID")
    private String userId;

    @Column(name="Name", length = 25)
    private String name;

    @Column(name = "Email")
    private String email;

    @Column(name = "About")
    private String about;

    // Not want to store in table as it will be coming from rating microservice
    @Transient
    private List<Rating> ratings;


}
