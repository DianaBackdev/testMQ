package com.example.MQAdminTemplate.repository;


import com.example.MQAdminTemplate.domain.TemporaryAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryAdminRepository extends JpaRepository<TemporaryAdmin, Long> {
    TemporaryAdmin findByConfirmationCode(String confirmationCode);
}