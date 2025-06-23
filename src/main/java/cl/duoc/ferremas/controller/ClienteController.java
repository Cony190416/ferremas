package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Cliente;
import cl.duoc.ferremas.repository.ClienteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Registro de clientes
    @PostMapping("/registrar")
    public String registrarCliente(@ModelAttribute Cliente cliente) {
        // Validar si el email ya existe para evitar duplicados (opcional pero recomendable)
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            // Podrías agregar un parámetro para mostrar error en el formulario de registro
            return "redirect:/cliente/registro?error=emailExiste";
        }

        String passwordEncriptada = passwordEncoder.encode(cliente.getContrasena());
        cliente.setContrasena(passwordEncriptada);
        clienteRepository.save(cliente);
        return "redirect:/login?success=registro";
    }

    // Login manual de clientes
    @PostMapping("/login")
    public String loginCliente(@ModelAttribute Cliente cliente, HttpServletRequest request) {
        Cliente clienteDB = clienteRepository.findByEmail(cliente.getEmail());

        if (clienteDB != null && passwordEncoder.matches(cliente.getContrasena(), clienteDB.getContrasena())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("clienteLogueado", clienteDB);
            return "redirect:/index.html";  // redirige a página pública o principal
        }

        // Redirigir con parámetro para mostrar error en login
        return "redirect:/login?error=cliente";
    }

    // Logout manual de clientes
    @GetMapping("/logout")
    public String logoutCliente(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/index.html";
    }
}
