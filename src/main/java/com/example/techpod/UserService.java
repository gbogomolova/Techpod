package com.example.techpod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Попытка найти пользователя с именем: " + username); // Лог начала поиска

        return userRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("Пользователь найден: " + user.getUsername()); // Лог, если пользователь найден
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole().replace("ROLE_", ""))
                            .build();
                })
                .orElseThrow(() -> {
                    System.out.println("Пользователь с именем " + username + " не найден."); // Лог, если пользователь не найден
                    return new UsernameNotFoundException("Пользователь не найден: " + username);
                });
    }


    public List<User> getAllUsers() {
            List<User> users = userRepository.findAll(); // Получаем список пользователей
            System.out.println("Метод отработал"); // Лог вывода списка
            return users;

    }

    public void changeUserRole(Long id, String newRole) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь отсутствует"));
        user.setRole(newRole);
        userRepository.save(user);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findByUsername(String username) {
        System.out.println("Поиск пользователя в базе данных: " + username);
        return userRepository.findByUsername(username).orElse(null);
    }

}