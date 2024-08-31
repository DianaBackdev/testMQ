package com.example.MQAdminTemplate.repository;

import com.example.MQAdminTemplate.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Admin findAdminByEmailAdmin(String emailAdmin);
}
