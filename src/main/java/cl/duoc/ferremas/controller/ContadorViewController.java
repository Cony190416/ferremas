package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contador")
public class ContadorViewController {

    @GetMapping("/home")
    public String mostrarHome() {
        return "contador/home";
    }

    @GetMapping("/reportes")
    public String mostrarReportes() {
        return "contador/home"; // debe existir templates/contador/home.html
    }
}
