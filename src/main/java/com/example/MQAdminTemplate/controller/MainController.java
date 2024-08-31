package com.example.MQAdminTemplate.controller;

import com.example.MQAdminTemplate.domain.Admin;
import com.example.MQAdminTemplate.security.CustomUserDetailService;
import com.example.MQAdminTemplate.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    AdminService adminService;
    CustomUserDetailService customUserDetailService;

    @Autowired
    public MainController(AdminService adminService, CustomUserDetailService customUserDetailService) {
        this.adminService = adminService;
        this.customUserDetailService = customUserDetailService;
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/test-front")
    public String testFront() {
        return "landing-page-C";
    }

    @GetMapping("/registry")
    public String registry()  {
        return "auth-register";
    }
    @PostMapping("/registry")
    public String registryAdmin(@ModelAttribute Admin admin, Model model) {
        String result = adminService.registerAdmin(admin);
        if ("email_exists".equals(result)) {
            model.addAttribute("error", "Данная почта уже существует!");
            return "auth-register"; // Имя HTML-шаблона страницы регистрации
        }
        return "auth-lock-screen";
    }

    @GetMapping("/auth-lock-screen")
    public String authLockScreen()  {
        return "auth-lock-screen";
    }

    @GetMapping("/confirm-account")
    public String confirmAccount()  {
        return "auth-recoverqw-lock-screen";
    }
    @PostMapping("/confirm-account")
    public String confirmAccountAdmin(@RequestParam("code") String code, Model model) {
        if (adminService.confirmAdmin(code)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Данный код недействителен, повторите попытку!");
            return "auth-lock-screen";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword()  {
        return "auth-recoverqw";
    }
    @PostMapping("/send-reset-code")
    public String sendResetCode(@RequestParam("email") String email, HttpSession session, Model model) {
        String result = adminService.sendResetCode(email);
        if ("email_not_found".equals(result)) {
            model.addAttribute("error", "Данной почты не существует!");
            return "auth-recoverqw"; // Имя HTML-шаблона страницы регистрации
        }
        session.setAttribute("email", email); // Сохранение email в сессии
        return "redirect:/enter-reset-code";
    }

    @GetMapping("/enter-reset-code")
    public String enterResetCode()  {
        return "auth-recoverqw-lock-screen";
    }
    @PostMapping("/verify-reset-code")
    public String verifyResetCode(@RequestParam("code") String code, HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null || !adminService.verifyResetCode(email, code)) {
            model.addAttribute("error", "Данный код недействителен, повторите попытку!");
            return "auth-recoverqw-lock-screen";
        }
        session.setAttribute("codeVerified", true); // Помечаем, что код подтвержден
        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword()  {
        return "auth-recoverqw-lock-screen-new-pass";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("password") String password, HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        Boolean codeVerified = (Boolean) session.getAttribute("codeVerified");

        if (email == null || codeVerified == null || !codeVerified) {
            model.addAttribute("error", "Что-то пошло не так!");

            return "auth-recoverqw-lock-screen-new-pass";
        }

        adminService.resetPassword(email, password);
        session.removeAttribute("email");
        session.removeAttribute("codeVerified");

        return "redirect:/login?resetSuccess";
    }

}








