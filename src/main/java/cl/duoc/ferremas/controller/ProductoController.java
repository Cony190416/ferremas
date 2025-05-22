package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.model.Precio;
import cl.duoc.ferremas.service.ProductoService;
import cl.duoc.ferremas.service.PrecioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final PrecioService precioService;

    public ProductoController(ProductoService productoService, PrecioService precioService) {
        this.productoService = productoService;
        this.precioService = precioService;
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{codigo}")
    public Optional<Producto> obtenerPorCodigo(@PathVariable String codigo) {
        return productoService.obtenerPorCodigo(codigo);
    }

    @GetMapping("/buscar-nombre")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/categoria")
    public List<Producto> buscarPorCategoria(@RequestParam String nombre) {
        return productoService.buscarPorCategoria(nombre);
    }

    @GetMapping("/stock-bajo")
    public List<Producto> buscarPorStock(@RequestParam int limite) {
        return productoService.buscarPorStockMenorA(limite);
    }

    @GetMapping("/precios/{codigo}")
    public List<Precio> historialDePrecios(@PathVariable String codigo) {
        return precioService.obtenerPreciosPorCodigoProducto(codigo);
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }
}
