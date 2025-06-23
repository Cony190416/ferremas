package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Esto indica a Thymeleaf que renderice templates/login.html
    }
}
