package com.example.techpod;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    // Получение всех пользователей
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("Метод из АПИ контроллера был вызван");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Эндпоинт для регистрации нового пользователя
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Проверяем, существует ли пользователь
            if (userService.userExists(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с таким именем уже существует.");
            }

            // Сохраняем пользователя
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка регистрации. Попробуйте позже.");
        }
    }

    @GetMapping("/role")
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("ROLE_USER"); // Возвращаем роль по умолчанию
        }
        return "ROLE_USER"; // Возвращаем роль по умолчанию, если пользователь не аутентифицирован
    }

    // Изменение роли пользователя
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUserRole(@PathVariable Long id, @RequestParam String newRole) {
        try {
            userService.changeUserRole(id, newRole);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
