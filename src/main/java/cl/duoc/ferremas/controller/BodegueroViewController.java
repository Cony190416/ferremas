package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bodeguero")
public class BodegueroViewController {

    @GetMapping("/home")
    public String mostrarInventario() {
        return "bodeguero/home"; // debe existir templates/bodega/home.html
    }
}
