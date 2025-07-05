package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Cliente;
import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.repository.ClienteRepository;
import cl.duoc.ferremas.repository.UsuarioRepository;
import cl.duoc.ferremas.security.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registro de clientes
    @PostMapping("/registrar")
    public String registrarCliente(@ModelAttribute Cliente cliente) {
        // Validar si el email ya existe para evitar duplicados
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            return "redirect:/cliente/registro?error=emailExiste";
        }

        String passwordEncriptada = passwordEncoder.encode(cliente.getContrasena());
        cliente.setContrasena(passwordEncriptada);
        clienteRepository.save(cliente);
        return "redirect:/login?success=registro";
    }

    // --- Endpoints REST para la vista del cliente ---
    
    // Verificar si el usuario logueado es un cliente
    @GetMapping("/check-cliente")
    @ResponseBody
    public ResponseEntity<?> verificarCliente(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No hay usuario logueado");
        }
        
        if (!(authentication.getPrincipal() instanceof UsuarioPrincipal)) {
            return ResponseEntity.status(401).body("Tipo de usuario no válido");
        }
        
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        if (usuarioPrincipal.esCliente()) {
            return ResponseEntity.ok("Es cliente");
        } else {
            // Si no es cliente, verificar si es un colaborador
            String rol = usuarioPrincipal.getUsuario().getRol().name();
            if (rol.equals("ADMINISTRADOR") || rol.equals("BODEGUERO") || rol.equals("CONTADOR") || rol.equals("VENDEDOR")) {
                return ResponseEntity.status(403).body("Es colaborador, no cliente");
            } else {
                // Si no tiene rol específico, se considera cliente
                return ResponseEntity.ok("Es cliente");
            }
        }
    }
    
    // Obtener datos del cliente logueado
    @GetMapping("/perfil")
    @ResponseBody
    public ResponseEntity<?> obtenerPerfil(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No hay usuario logueado");
        }
        
        if (!(authentication.getPrincipal() instanceof UsuarioPrincipal)) {
            return ResponseEntity.status(401).body("Tipo de usuario no válido");
        }
        
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        if (!usuarioPrincipal.esCliente()) {
            // Si no es cliente, verificar si es un colaborador
            String rol = usuarioPrincipal.getUsuario().getRol().name();
            if (rol.equals("ADMINISTRADOR") || rol.equals("BODEGUERO") || rol.equals("CONTADOR") || rol.equals("VENDEDOR")) {
                return ResponseEntity.status(403).body("Acceso denegado - Solo para clientes");
            }
            // Si no tiene rol específico, se considera cliente y continúa
        }
        
        Cliente cliente = usuarioPrincipal.getCliente();
        
        // No enviar la contraseña por seguridad
        Map<String, Object> perfil = new HashMap<>();
        perfil.put("id", cliente.getId());
        perfil.put("nombre", cliente.getNombre());
        perfil.put("email", cliente.getEmail());
        perfil.put("direccion", cliente.getDireccion());
        
        return ResponseEntity.ok(perfil);
    }

    // Obtener estadísticas del cliente
    @GetMapping("/estadisticas")
    @ResponseBody
    public ResponseEntity<?> obtenerEstadisticas(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No hay usuario logueado");
        }
        
        if (!(authentication.getPrincipal() instanceof UsuarioPrincipal)) {
            return ResponseEntity.status(401).body("Tipo de usuario no válido");
        }
        
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        if (!usuarioPrincipal.esCliente()) {
            // Si no es cliente, verificar si es un colaborador
            String rol = usuarioPrincipal.getUsuario().getRol().name();
            if (rol.equals("ADMINISTRADOR") || rol.equals("BODEGUERO") || rol.equals("CONTADOR") || rol.equals("VENDEDOR")) {
                return ResponseEntity.status(403).body("Acceso denegado - Solo para clientes");
            }
            // Si no tiene rol específico, se considera cliente y continúa
        }
        
        // Por ahora simulamos estadísticas (después se conectarán con pedidos reales)
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPedidos", 5);
        stats.put("pedidosEntregados", 3);
        stats.put("totalGastado", 156000);
        
        return ResponseEntity.ok(stats);
    }

    // Obtener pedidos activos del cliente
    @GetMapping("/pedidos")
    @ResponseBody
    public ResponseEntity<?> obtenerPedidos(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No hay usuario logueado");
        }
        
        if (!(authentication.getPrincipal() instanceof UsuarioPrincipal)) {
            return ResponseEntity.status(401).body("Tipo de usuario no válido");
        }
        
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        if (!usuarioPrincipal.esCliente()) {
            // Si no es cliente, verificar si es un colaborador
            String rol = usuarioPrincipal.getUsuario().getRol().name();
            if (rol.equals("ADMINISTRADOR") || rol.equals("BODEGUERO") || rol.equals("CONTADOR") || rol.equals("VENDEDOR")) {
                return ResponseEntity.status(403).body("Acceso denegado - Solo para clientes");
            }
            // Si no tiene rol específico, se considera cliente y continúa
        }
        
        // Por ahora simulamos pedidos (después se conectarán con pedidos reales)
        Map<String, Object>[] pedidos = new Map[2];
        
        Map<String, Object> pedido1 = new HashMap<>();
        pedido1.put("id", "P001");
        pedido1.put("fecha", "2025-01-15");
        pedido1.put("productos", "Martillo, Destornillador");
        pedido1.put("total", 45000);
        pedido1.put("estado", "preparando");
        pedidos[0] = pedido1;
        
        Map<String, Object> pedido2 = new HashMap<>();
        pedido2.put("id", "P002");
        pedido2.put("fecha", "2025-01-14");
        pedido2.put("productos", "Cemento, Arena");
        pedido2.put("total", 32500);
        pedido2.put("estado", "pendiente");
        pedidos[1] = pedido2;
        
        return ResponseEntity.ok(pedidos);
    }

    // Obtener historial de compras del cliente
    @GetMapping("/historial")
    @ResponseBody
    public ResponseEntity<?> obtenerHistorial(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("No hay usuario logueado");
        }
        
        if (!(authentication.getPrincipal() instanceof UsuarioPrincipal)) {
            return ResponseEntity.status(401).body("Tipo de usuario no válido");
        }
        
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        if (!usuarioPrincipal.esCliente()) {
            // Si no es cliente, verificar si es un colaborador
            String rol = usuarioPrincipal.getUsuario().getRol().name();
            if (rol.equals("ADMINISTRADOR") || rol.equals("BODEGUERO") || rol.equals("CONTADOR") || rol.equals("VENDEDOR")) {
                return ResponseEntity.status(403).body("Acceso denegado - Solo para clientes");
            }
            // Si no tiene rol específico, se considera cliente y continúa
        }
        
        // Por ahora simulamos historial (después se conectarán con pedidos reales)
        Map<String, Object>[] historial = new Map[2];
        
        Map<String, Object> compra1 = new HashMap<>();
        compra1.put("id", "P003");
        compra1.put("fecha", "2025-01-10");
        compra1.put("productos", "Lentes, Guantes");
        compra1.put("total", 18990);
        compra1.put("estado", "entregado");
        historial[0] = compra1;
        
        Map<String, Object> compra2 = new HashMap<>();
        compra2.put("id", "P004");
        compra2.put("fecha", "2025-01-05");
        compra2.put("productos", "Taladro, Brocas");
        compra2.put("total", 78000);
        compra2.put("estado", "entregado");
        historial[1] = compra2;
        
        return ResponseEntity.ok(historial);
    }

    // Endpoint temporal para crear cliente de prueba
    @PostMapping("/crear-prueba")
    @ResponseBody
    public ResponseEntity<?> crearClientePrueba() {
        try {
            // Verificar si ya existe un cliente de prueba
            Cliente clienteExistente = clienteRepository.findByEmail("cliente@prueba.com");
            if (clienteExistente != null) {
                return ResponseEntity.ok("Cliente de prueba ya existe: cliente@prueba.com / 123456");
            }
            
            // Crear cliente de prueba
            Cliente cliente = new Cliente();
            cliente.setNombre("Cliente Prueba");
            cliente.setEmail("cliente@prueba.com");
            cliente.setContrasena(passwordEncoder.encode("123456"));
            cliente.setDireccion("Av. Prueba 123, Santiago");
            
            clienteRepository.save(cliente);
            return ResponseEntity.ok("Cliente de prueba creado: cliente@prueba.com / 123456");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear cliente de prueba: " + e.getMessage());
        }
    }

    // Endpoint para actualizar contraseñas de usuarios existentes
    @PostMapping("/actualizar-contrasenas")
    @ResponseBody
    public ResponseEntity<?> actualizarContrasenas() {
        try {
            // Obtener todos los clientes
            List<Cliente> clientes = clienteRepository.findAll();
            int actualizados = 0;
            
            for (Cliente cliente : clientes) {
                // Actualizar contraseña con el nuevo encoder
                cliente.setContrasena(passwordEncoder.encode("123456"));
                clienteRepository.save(cliente);
                actualizados++;
            }
            
            return ResponseEntity.ok("Se actualizaron " + actualizados + " contraseñas a '123456'");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar contraseñas: " + e.getMessage());
        }
    }

    // Endpoint temporal para listar usuarios
    @GetMapping("/listar-usuarios")
    @ResponseBody
    public ResponseEntity<?> listarUsuarios() {
        try {
            Map<String, Object> resultado = new HashMap<>();
            
            // Listar clientes
            List<Cliente> clientes = clienteRepository.findAll();
            resultado.put("clientes", clientes);
            
            // Listar usuarios de la tabla usuario
            List<Usuario> usuariosBD = usuarioRepository.findAll();
            resultado.put("usuarios", usuariosBD);
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al listar usuarios: " + e.getMessage());
        }
    }
}
