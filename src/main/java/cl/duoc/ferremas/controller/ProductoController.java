// Controlador REST para gestionar productos y sus precios
package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.model.Precio;
import cl.duoc.ferremas.service.ProductoService;
import cl.duoc.ferremas.service.PrecioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase manejará peticiones REST
@RequestMapping("/api/productos") // Ruta base para todas las operaciones relacionadas a productos
@CrossOrigin(origins = "*") // Permite el acceso desde cualquier origen (ideal para frontend separado)
public class ProductoController {

    // Inyección de dependencias de servicios
    private final ProductoService productoService;
    private final PrecioService precioService;

    // Constructor que inyecta los servicios necesarios
    public ProductoController(ProductoService productoService, PrecioService precioService) {
        this.productoService = productoService;
        this.precioService = precioService;
    }

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // Buscar un producto por su código
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigo) {
        Optional<Producto> productoOpt = productoService.obtenerPorCodigo(codigo);
        if (productoOpt.isPresent()) {
            return ResponseEntity.ok(productoOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
    }

    // Buscar productos que coincidan con un nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // Buscar productos por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    // Buscar productos cuyo stock es menor al límite indicado
    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> buscarPorStockBajo(@RequestParam int limite) {
        return ResponseEntity.ok(productoService.buscarPorStockMenorA(limite));
    }

    // Obtener historial de precios de un producto específico
    @GetMapping("/{codigo}/precios")
    public ResponseEntity<?> obtenerHistorialPrecios(@PathVariable String codigo) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        List<Precio> precios = precioService.obtenerPreciosPorCodigoProducto(codigo);
        return ResponseEntity.ok(precios);
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            // Verifica si ya existe un producto con ese código
            if (productoService.obtenerPorCodigo(producto.getCodigo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un producto con el código '" + producto.getCodigo() + "'");
            }
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear producto: " + e.getMessage());
        }
    }

    // Actualizar un producto existente
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }

        // Se asegura que el código no cambie
        producto.setCodigo(codigo);
        Producto actualizado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar un producto por su código
    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigo) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        productoService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Actualizar solo el stock de un producto (PATCH = actualización parcial)
    @PatchMapping("/{codigo}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable String codigo, @RequestBody int nuevoStock) {
        Optional<Producto> productoActualizado = productoService.actualizarStock(codigo, nuevoStock);
        if (productoActualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        return ResponseEntity.ok(productoActualizado.get());
    }

    // Agregar un nuevo precio al historial de un producto
    @PostMapping("/{codigo}/precios")
    public ResponseEntity<?> agregarPrecio(@PathVariable String codigo, @RequestBody Precio precio) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }

        // Asocia el precio al producto antes de guardarlo
        precio.setProducto(producto.get());
        Precio nuevoPrecio = precioService.guardarPrecio(precio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrecio);
    }
}
