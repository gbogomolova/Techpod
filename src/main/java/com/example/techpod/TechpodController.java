package com.example.techpod;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;



@Controller
public class TechpodController {

    @Autowired
    private TechpodService techpodService;

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "registration";
    }

    @GetMapping("/index")
    public String showLandingPage() {
        System.out.println("Пользователь: вошел в систему.");
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editTroublePage(@PathVariable("id") Long id, Model model) {
        Trouble trouble = techpodService.get(id);
        if (trouble == null) {
            return "redirect:/index";
        }
        model.addAttribute("trouble", trouble);
        return "edit_trouble";
    }

    @GetMapping("/manage_users")
    public String showManageUsers() {
        return "manage_users";
    }

    @GetMapping("/histogram")
    public String showHistogram() {
        return "histogram";
    }

    @GetMapping("/new_trouble")
    public String showNewTroubleForm() {
        return "new_trouble";
    }
}
