package com.example.MQAdminTemplate.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id_seq")
    @SequenceGenerator(name = "messages_id_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "id_users")
    private int userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "telegram_nick")
    private String telegramNick;
}
