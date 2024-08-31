package com.example.MQAdminTemplate.service;

import com.example.MQAdminTemplate.domain.Admin;
import com.example.MQAdminTemplate.domain.TemporaryAdmin;
import com.example.MQAdminTemplate.repository.AdminRepository;
import com.example.MQAdminTemplate.repository.TemporaryAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final TemporaryAdminRepository temporaryAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private static final String FIXED_CONFIRMATION_EMAIL = "diankorenko@gmail.com";

    @Autowired
    public AdminService(AdminRepository adminRepository, TemporaryAdminRepository temporaryAdminRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.adminRepository = adminRepository;
        this.temporaryAdminRepository = temporaryAdminRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public String registerAdmin(Admin admin) {

        if (adminRepository.findAdminByEmailAdmin(admin.getEmailAdmin()) != null) {
            return "email_exists";
        }

        String confirmationCode = generateCode();

        TemporaryAdmin temporaryAdmin = new TemporaryAdmin();
        temporaryAdmin.setFirstName(admin.getFirstNameAdmin());
        temporaryAdmin.setLastName(admin.getLastNameAdmin());
        temporaryAdmin.setEmail(admin.getEmailAdmin());
        temporaryAdmin.setPassword(passwordEncoder.encode(admin.getPasswordAdmin()));
        temporaryAdmin.setConfirmationCode(confirmationCode);
        temporaryAdmin.setCreatedDate(LocalDateTime.now());
        // Чтение изображения из файла
        try {
            Path imagePath = Paths.get("C:\\Users\\DELL\\Projects\\MQ_Backend\\MQ_Backend\\src\\main\\resources\\assets\\images\\img.png");
            byte[] imageBytes = Files.readAllBytes(imagePath);
            System.out.println("зашли в часть загрузки фото" + Arrays.toString(imageBytes));
            temporaryAdmin.setImage(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        temporaryAdminRepository.save(temporaryAdmin);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(FIXED_CONFIRMATION_EMAIL);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("noreply@yourdomain.com");
        mailMessage.setText("Your confirmation code is: " + confirmationCode);

        mailSender.send(mailMessage);

        return "success";
    }

    public boolean confirmAdmin(String code) {
        TemporaryAdmin temporaryAdmin = temporaryAdminRepository.findByConfirmationCode(code);
        if (temporaryAdmin != null) {
            Admin admin = new Admin();
            admin.setFirstNameAdmin(temporaryAdmin.getFirstName());
            admin.setLastNameAdmin(temporaryAdmin.getLastName());
            admin.setEmailAdmin(temporaryAdmin.getEmail());
            admin.setPasswordAdmin(temporaryAdmin.getPassword());
            admin.setEnabled(true);
            admin.setImage(temporaryAdmin.getImage()); // Добавляем изображение
            adminRepository.save(admin);
            temporaryAdminRepository.delete(temporaryAdmin);
            return true;
        }
        return false;
    }

    public String sendResetCode(String email) {

        if (adminRepository.findAdminByEmailAdmin(email) == null) {
            return "email_not_found";
        }

        String resetCode = generateCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(FIXED_CONFIRMATION_EMAIL);
        message.setSubject("Password Reset Code");
        message.setText("Your password reset code is: " + resetCode);
        mailSender.send(message);

        Admin admin = adminRepository.findAdminByEmailAdmin(email);
        if (admin != null) {
            admin.setResetCodePass(resetCode);
            adminRepository.save(admin);
        }
        return "success";
    }

    public boolean verifyResetCode(String email, String code) {
        Admin admin = adminRepository.findAdminByEmailAdmin(email);
        return admin != null && admin.getResetCodePass().equals(code);
    }

    public void resetPassword(String email, String password) {
        Admin admin = adminRepository.findAdminByEmailAdmin(email);
        if (admin != null) {
            String encodedPassword = passwordEncoder.encode(password);
            admin.setPasswordAdmin(encodedPassword);
            admin.setResetCodePass(null);
            adminRepository.save(admin);
        }
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }







    public boolean checkPassword(Admin admin, String password) {
        return passwordEncoder.matches(password, admin.getPasswordAdmin());
    }

    public boolean validatePassword(String password) {
        // Реализуйте логику проверки пароля, если требуется
        return password.length() >= 3;
    }

    public boolean changePassword(Admin admin, String currentPassword, String newPassword) {
        // Проверяем корректность нового пароля и совпадение текущего пароля
        if (!validatePassword(newPassword) || !checkPassword(admin, currentPassword)) {
            return false; // Некорректный новый пароль или текущий пароль не совпадает
        }

        try {
            // Шифруем новый пароль перед сохранением
            String encryptedNewPassword = passwordEncoder.encode(newPassword);
            admin.setPasswordAdmin(encryptedNewPassword);
            adminRepository.save(admin);
            return true; // Пароль успешно обновлен
        } catch (Exception e) {
            e.printStackTrace(); // Вывести информацию об ошибке в консоль
            return false; // Ошибка при обновлении пароля
        }
    }

    public Admin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            return adminRepository.findAdminByEmailAdmin(email);
        }
        return null;
    }


    public Admin updateAdmin(Admin updatedAdmin) {
        Admin currentAdmin = getCurrentAdmin();

        if (currentAdmin == null) {
            // Администратор не авторизован
            return null;
        }
        // Проверяем, изменились ли данные
        if (!currentAdmin.getFirstNameAdmin().equals(updatedAdmin.getFirstNameAdmin())) {
            currentAdmin.setFirstNameAdmin(updatedAdmin.getFirstNameAdmin());
        }
        if (!currentAdmin.getLastNameAdmin().equals(updatedAdmin.getLastNameAdmin())) {
            currentAdmin.setLastNameAdmin(updatedAdmin.getLastNameAdmin());
        }
        if (!currentAdmin.getEmailAdmin().equals(updatedAdmin.getEmailAdmin())) {
            currentAdmin.setEmailAdmin(updatedAdmin.getEmailAdmin());
        }
        if (!currentAdmin.getRole().equals(updatedAdmin.getRole())) {
            currentAdmin.setRole(updatedAdmin.getRole());
        }
        if (!Arrays.equals(currentAdmin.getImage(), updatedAdmin.getImage())) {
            currentAdmin.setImage(updatedAdmin.getImage());
        }
        // Сохранение обновленных данных
        return adminRepository.save(currentAdmin);
    }


}
