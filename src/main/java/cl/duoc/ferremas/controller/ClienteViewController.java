package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteViewController {

    @GetMapping("/home")
    public String home() {
        return "cliente/home";
    }

    @GetMapping("/registro")
    public String registro() {
        return "cliente/registro";
    }
}
