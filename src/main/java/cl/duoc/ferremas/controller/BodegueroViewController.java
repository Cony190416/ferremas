package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/bodeguero")
public class BodegueroViewController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/home")
    public String mostrarInventario() {
        return "bodeguero/home"; // debe existir templates/bodega/home.html
    }

    // API para obtener todos los productos (para el panel bodeguero)
    @GetMapping("/api/productos")
    @ResponseBody
    public ResponseEntity<List<Producto>> obtenerProductos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
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
}
