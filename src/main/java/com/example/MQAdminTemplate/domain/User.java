package com.example.MQAdminTemplate.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "first_name")
    private String firstNameUser;

    @Column(name = "last_name")
    private String lastNameUser;

    @Column(name = "email")
    private String emailUser;

    @Column(name = "phone_number")
    private String phoneNumberUser;

    @Column(name = "password")
    private String passwordUser;

    @Column(name = "profile_picture")
    private byte[] profilePictureUser;

    @Column(name = "account_creation_date")
    private LocalDateTime accountCreationDateUser;

    @Column(name = "account_deletion_date")
    private LocalDateTime accountDeletionDateUser;

    @Column(name = "account_status")
    private boolean accountStatusUser;

    @Column(name = "city")
    private String cityUser;

    @Column(name = "role")
    private String role = "USER";

}
