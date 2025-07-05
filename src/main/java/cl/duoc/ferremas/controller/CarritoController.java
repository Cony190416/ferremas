package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Carrito;
import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.repository.ProductoRepository;
import cl.duoc.ferremas.repository.PrecioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "*")
public class CarritoController {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private PrecioRepository precioRepository;

    private Carrito getCarrito(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    @GetMapping("/json")
    public Carrito getCarritoJson(HttpSession session) {
        return getCarrito(session);
    }

    @PostMapping("/json/agregar")
    public ResponseEntity<?> agregarProductoJson(@RequestParam String codigo, @RequestParam int cantidad, HttpSession session) {
        Producto producto = productoRepository.findById(codigo).orElse(null);
        if (producto != null) {
            producto.setPrecios(precioRepository.findByProductoCodigo(codigo));
            getCarrito(session).agregarProducto(producto, cantidad);
            Map<String, String> ok = new HashMap<>();
            ok.put("msg", "Producto agregado con éxito");
            return ResponseEntity.ok(ok);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/json/quitar")
    public ResponseEntity<?> quitarProductoJson(@RequestParam String codigo, HttpSession session) {
        getCarrito(session).quitarProducto(codigo);
        Map<String, String> ok = new HashMap<>();
        ok.put("msg", "Producto eliminado con éxito");
        return ResponseEntity.ok(ok);
    }

    @PostMapping("/json/modificar")
    public Carrito modificarCantidadJson(@RequestParam String codigo, @RequestParam int cantidad, HttpSession session) {
        getCarrito(session).modificarCantidad(codigo, cantidad);
        return getCarrito(session);
    }

    @PostMapping("/json/vaciar")
    public ResponseEntity<?> vaciarCarritoJson(HttpSession session) {
        getCarrito(session).vaciar();
        Map<String, String> ok = new HashMap<>();
        ok.put("msg", "Carrito vaciado");
        return ResponseEntity.ok(ok);
    }
} 