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

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite llamadas desde cualquier origen
public class ProductoController {

    private final ProductoService productoService;
    private final PrecioService precioService;

    public ProductoController(ProductoService productoService, PrecioService precioService) {
        this.productoService = productoService;
        this.precioService = precioService;
    }

    // Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // Buscar producto por código
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

    // Buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // Buscar productos por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    // Buscar productos con stock bajo
    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> buscarPorStockBajo(@RequestParam int limite) {
        return ResponseEntity.ok(productoService.buscarPorStockMenorA(limite));
    }

    // Obtener historial de precios de un producto
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

    // Crear nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
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

    // Actualizar producto existente
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }

        producto.setCodigo(codigo); // Asegura que el código no cambie
        Producto actualizado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar producto por código
    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigo) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        productoService.eliminarProducto(codigo);
        return ResponseEntity.noContent().build();
    }

    // Actualizar solo el stock de un producto
    @PatchMapping("/{codigo}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable String codigo, @RequestBody int nuevoStock) {
        Optional<Producto> productoActualizado = productoService.actualizarStock(codigo, nuevoStock);
        if (productoActualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        return ResponseEntity.ok(productoActualizado.get());
    }

    // Agregar nuevo precio a un producto
    @PostMapping("/{codigo}/precios")
    public ResponseEntity<?> agregarPrecio(@PathVariable String codigo, @RequestBody Precio precio) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }

        precio.setProducto(producto.get());
        Precio nuevoPrecio = precioService.guardarPrecio(precio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrecio);
    }
}
