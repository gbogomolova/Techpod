package com.example.techpod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Изменено на @Controller для обработки шаблонов
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    // Регистрация
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "registration";
        }

        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "registration";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userService.saveUser(user); // Передаем два аргумента

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Авторизация
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        System.out.println("Попытка входа с именем пользователя: " + username); // Лог запроса входа

        User user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("Ошибка: Пользователь с именем " + username + " не найден."); // Лог, если пользователь не найден
            model.addAttribute("error", "Пользователь с таким именем не зарегистрирован");
            return "login";
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Ошибка: Неверный пароль для пользователя " + username); // Лог, если пароль неверный
            model.addAttribute("error", "Неверное имя пользователя или пароль");
            return "login";
        }

        System.out.println("Успешный вход пользователя: " + username); // Лог успешного входа
        return "redirect:/index";
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUsers(Model model) {
        System.out.println("Метод из контроллера был вызван");
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manage_users";
    }

    // Управление доступом для администартора
    @PostMapping("/change-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@PathVariable Long id, @RequestParam String newRole) {
        userService.changeUserRole(id, newRole);
        return "redirect:/admin/users";
    }
}
