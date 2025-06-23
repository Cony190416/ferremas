package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/home")
    public String mostrarDashboard() {
        return "admin/home"; // debe existir templates/admin/home.html
    }
}
