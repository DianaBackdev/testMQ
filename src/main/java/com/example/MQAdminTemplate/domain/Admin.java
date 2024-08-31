package com.example.MQAdminTemplate.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admins_id_seq")
    @SequenceGenerator(name = "admins_id_seq", sequenceName = "admins_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "email")
    private String emailAdmin;

    @Column(name = "first_name")
    private String firstNameAdmin;

    @Column(name = "last_name")
    private String lastNameAdmin;

    @Column(name = "password")
    private String passwordAdmin;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "reset_code_pass")
    private String resetCodePass;

    @Column(name = "role")
    private String role = "ADMIN";

    @Column(name = "image")
    private byte[] image;

}
