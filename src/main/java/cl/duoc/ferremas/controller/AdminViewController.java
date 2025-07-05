package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.model.OrdenCompra;
import cl.duoc.ferremas.service.ProductoService;
import cl.duoc.ferremas.service.UsuarioService;
import cl.duoc.ferremas.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PagoService pagoService;

    // Página principal del admin
    @GetMapping("/home")
    public String home() {
        return "admin/home";
    }

    // API para obtener todos los productos (para el panel admin)
    @GetMapping("/api/productos")
    @ResponseBody
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // API para obtener todos los usuarios
    @GetMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    // API para obtener ventas/órdenes
    @GetMapping("/api/ventas")
    @ResponseBody
    public ResponseEntity<List<OrdenCompra>> obtenerVentas() {
        return ResponseEntity.ok(pagoService.obtenerTodasLasOrdenes());
    }

    // API para actualizar producto
    @PutMapping("/api/productos/{codigo}")
    @ResponseBody
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizarProducto(codigo, producto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // API para eliminar producto
    @DeleteMapping("/api/productos/{codigo}")
    @ResponseBody
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigo) {
        try {
            productoService.eliminarProducto(codigo);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Producto eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // API para toggle de estado de usuario
    @PutMapping("/api/usuarios/{id}/toggle")
    @ResponseBody
    public ResponseEntity<?> toggleUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.toggleEstadoUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // API para obtener estadísticas del dashboard
    @GetMapping("/api/estadisticas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Producto> productos = productoService.obtenerTodos();
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            List<OrdenCompra> ventas = pagoService.obtenerTodasLasOrdenes();
            
            // Calcular ventas de hoy
            double ventasHoy = ventas.stream()
                .filter(v -> v.getFechaCreacion().toLocalDate().equals(java.time.LocalDate.now()))
                .mapToDouble(OrdenCompra::getMontoTotal)
                .sum();
            
            stats.put("totalProductos", productos.size());
            stats.put("totalUsuarios", usuarios.size());
            stats.put("totalVentas", ventas.size());
            stats.put("ventasHoy", ventasHoy);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            stats.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(stats);
        }
    }
}
