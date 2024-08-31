package com.example.MQAdminTemplate.controller;


import com.example.MQAdminTemplate.domain.Admin;
import com.example.MQAdminTemplate.domain.User;
import com.example.MQAdminTemplate.repository.AdminRepository;
import com.example.MQAdminTemplate.service.AdminService;
import com.example.MQAdminTemplate.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, AdminRepository adminRepository, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String home(Model model) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        model.addAttribute("currentAdmin", currentAdmin);
        return "index-general";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword, Model model) {

        Admin admin = adminService.getCurrentAdmin();
        model.addAttribute("currentAdmin", admin);

        // Проверяем, определен ли пользователь
        if (admin == null) {
            model.addAttribute("error", "Пользователь не определен.");
            return "index-general";
        }

        // Проверяем соответствие текущего пароля
        if (!adminService.checkPassword(admin, oldPassword)) {
            model.addAttribute("error", "Текущий пароль не совпадает.");
            return "index-general";
        }

        // Проверяем валидность нового пароля
        if (!adminService.validatePassword(newPassword)) {
            model.addAttribute("error", "Некорректный новый пароль.");
            return "index-general";
        }

        // Если все проверки пройдены успешно, меняем пароль
        if (adminService.changePassword(admin, oldPassword, newPassword)) {
            model.addAttribute("message", "Пароль успешно обновлен.");
        } else {
            model.addAttribute("error", "Ошибка при обновлении пароля. Повторите попытку.");
        }

        return "index-general";
    }

    @PostMapping("/updateAdminData")
    public String updateAdminData(@RequestParam("firstNameAdmin") String firstName,
                                  @RequestParam("lastNameAdmin") String lastName,
                                  @RequestParam("emailAdmin") String email,
                                  @RequestParam("role") String role,
                                  @RequestParam("image") MultipartFile imageFile,
                                  Model model) {
        Admin currentAdmin = adminService.getCurrentAdmin();

        if (currentAdmin == null) {
            // Администратор не авторизован
            return "redirect:/login"; // Перенаправляем на страницу входа, если пользователь не авторизован
        }

        try {
            // Обновление данных администратора
            currentAdmin.setFirstNameAdmin(firstName);
            currentAdmin.setLastNameAdmin(lastName);
            currentAdmin.setEmailAdmin(email);
            currentAdmin.setRole(role);

            // Обработка загруженного файла
            if (!imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                currentAdmin.setImage(imageBytes);
            }

            // Сохранение обновленных данных
            Admin updatedAdmin = adminService.updateAdmin(currentAdmin);
            model.addAttribute("currentAdmin", updatedAdmin);

            model.addAttribute("messageUpdateAdminData", "Данные успешно обновлены.");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorUpdateAdminData", "Ошибка при загрузке изображения.");
        }

        return "index-general"; // Замените на путь к странице аккаунта
    }


    @GetMapping("/admin/image")
    @ResponseBody
    public ResponseEntity<byte[]> getAdminImage() {
        Admin admin = adminService.getCurrentAdmin();

        if (admin == null || admin.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(admin.getImage());
    }

    @GetMapping("/ecommerce-customer-adding")
    public String showCreateUserForm(Model model) {
        return "ecommerce-customer-adding"; // Замените на путь к вашей HTML-форме
    }

    @PostMapping("/ecommerce-customer-adding")
    public String createUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<User> users = userService.getAllUsers();
            redirectAttributes.addFlashAttribute("listAllUsers", users);
            redirectAttributes.addFlashAttribute("error", "Ошибка создания пользователя.");
            return "redirect:/admin/listAllUsers"; // Возврат к форме, если есть ошибки
        }

        // Попытка создать пользователя
        User createdUser = userService.createUser(user);
        if (createdUser == null) {
            // Email уже существует
            List<User> users = userService.getAllUsers();
            redirectAttributes.addFlashAttribute("listAllUsers", users);
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует.");
            return "redirect:/admin/listAllUsers"; // Возврат к форме при дублировании email
        }

        // Если пользователь успешно создан
        List<User> users = userService.getAllUsers();
        redirectAttributes.addFlashAttribute("listAllUsers", users);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно создан.");
        return "redirect:/admin/listAllUsers"; // Перенаправление на страницу со списком пользователей
    }




















    @GetMapping("/listAllUsers")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        // Вывод в консоль только id и cityUser для каждого пользователя
        users.forEach(user -> System.out.println("ID: " + user.getId() + ", Город: " + user.getCityUser()));

        model.addAttribute("listAllUsersSort", users);
        return "ecommerce-customers"; // Название HTML-шаблона для отображения списка пользователей
    }

    @GetMapping("/ecommerce-customers-allInfo/{id}")
    public String getUserInfoById(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id); // Вызов метода сервиса
        model.addAttribute("oneUserInfo", user); // Добавляем информацию о пользователе в модель
        return "ecommerce-customers-allInfo"; // Название HTML-шаблона для отображения информации о пользователе
    }





}
