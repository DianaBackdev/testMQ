package com.example.MQAdminTemplate.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "temporary_admin")
public class TemporaryAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temporary_admin_id_seq")
    @SequenceGenerator(name = "temporary_admin_id_seq", sequenceName = "temporary_admin_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "image")
    private byte[] image;
}
