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
@CrossOrigin(origins = "*") // Para permitir llamadas desde frontend
public class ProductoController {

    private final ProductoService productoService;
    private final PrecioService precioService;

    public ProductoController(ProductoService productoService, PrecioService precioService) {
        this.productoService = productoService;
        this.precioService = precioService;
    }

    // GET /api/productos - Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/{codigo} - Buscar producto por código
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerPorCodigo(@PathVariable String codigo) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Producto con código " + codigo + " no encontrado");
    }

    // GET /api/productos/buscar?nombre=xxx - Buscar por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/categoria/{categoria} - Buscar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/stock?limite=xxx - Buscar productos con stock bajo
    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> buscarPorStockBajo(@RequestParam int limite) {
        List<Producto> productos = productoService.buscarPorStockMenorA(limite);
        return ResponseEntity.ok(productos);
    }

    // GET /api/productos/{codigo}/precios - Historial de precios de un producto
    @GetMapping("/{codigo}/precios")
    public ResponseEntity<?> obtenerHistorialPrecios(@PathVariable String codigo) {
        // Verificar que el producto existe
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (!producto.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código " + codigo + " no encontrado");
        }
        
        List<Precio> precios = precioService.obtenerPreciosPorCodigoProducto(codigo);
        return ResponseEntity.ok(precios);
    }

    // POST /api/productos - Crear nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            // Validar que el código no exista
            if (productoService.obtenerPorCodigo(producto.getCodigo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Ya existe un producto con el código: " + producto.getCodigo());
            }
            
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear producto: " + e.getMessage());
        }
    }

    // PUT /api/productos/{codigo} - Actualizar producto existente
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigo, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.obtenerPorCodigo(codigo);
        if (!productoExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código " + codigo + " no encontrado");
        }
        
        producto.setCodigo(codigo); // Asegurar que el código coincida
        Producto productoActualizado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(productoActualizado);
    }

    // POST /api/productos/{codigo}/precios - Agregar nuevo precio a un producto
    @PostMapping("/{codigo}/precios")
    public ResponseEntity<?> agregarPrecio(@PathVariable String codigo, @RequestBody Precio precio) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (!producto.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con código " + codigo + " no encontrado");
        }
        
        precio.setProducto(producto.get());
        Precio nuevoPrecio = precioService.guardarPrecio(precio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrecio);
    }
}