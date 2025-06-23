package cl.duoc.ferremas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendedor")
public class VendedorViewController {

    @GetMapping("/home")
    public String mostrarPedidos() {
        return "vendedor/home"; // debe existir templates/vendedor/home.html
    }
}

