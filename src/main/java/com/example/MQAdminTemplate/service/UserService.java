package com.example.MQAdminTemplate.service;

import com.example.MQAdminTemplate.domain.User;
import com.example.MQAdminTemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public User createUser(User user) {

        // Проверка на существующий email
        if (userRepository.findUserByEmailUser(user.getEmailUser()) != null) {
            return null; // Email уже существует
        }
        // Шифруем пароль перед сохранением пользователя
        user.setPasswordUser(passwordEncoder.encode(user.getPasswordUser()));
        // Устанавливаем значения по умолчанию, такие как дата создания аккаунта
        user.setAccountCreationDateUser(LocalDateTime.now());
        user.setAccountStatusUser(true);
        // Чтение изображения из файла
        try {
            Path imagePath = Paths.get("C:\\Users\\DELL\\Projects\\MQ_Backend\\MQ_Backend\\src\\main\\resources\\assets\\images\\img.png");
            byte[] imageBytes = Files.readAllBytes(imagePath);
            user.setProfilePictureUser(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userRepository.save(user);
    }

    // Метод для получения всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdDesc();
    }

    // Метод для получения пользователя по ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + " не найден"));
    }


}
