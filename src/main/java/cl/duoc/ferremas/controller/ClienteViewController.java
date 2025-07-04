package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteViewController {

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "cliente/registro";  // Thymeleaf busca templates/cliente/registro.html
    }
}
