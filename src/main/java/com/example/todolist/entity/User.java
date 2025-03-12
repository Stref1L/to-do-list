package com.example.todolist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 26)
    @Column(nullable = false, name = "first_name", length = 26)
    private String firstName;

    @Size(min = 2, max = 26)
    @Column(nullable = false, name = "last_name", length = 26)
    private String lastName;

    @Size(min = 2, max = 26)
    @Column(nullable = false, unique = true, length = 26)
    private String username;

    @Size(min = 5)
    @Column(nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;
}
