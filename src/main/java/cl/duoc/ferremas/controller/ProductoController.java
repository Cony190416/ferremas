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
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;
    private final PrecioService precioService;

    public ProductoController(ProductoService productoService, PrecioService precioService) {
        this.productoService = productoService;
        this.precioService = precioService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

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

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> buscarPorStockBajo(@RequestParam int limite) {
        return ResponseEntity.ok(productoService.buscarPorStockMenorA(limite));
    }

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

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (productoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }

        producto.setCodigo(codigo);
        Producto actualizado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(actualizado);
    }

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

    @PatchMapping("/{codigo}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable String codigo, @RequestBody int nuevoStock) {
        Optional<Producto> productoActualizado = productoService.actualizarStock(codigo, nuevoStock);
        if (productoActualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código '" + codigo + "' no encontrado");
        }
        return ResponseEntity.ok(productoActualizado.get());
    }

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
