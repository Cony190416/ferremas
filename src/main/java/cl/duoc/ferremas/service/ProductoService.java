package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<Producto> buscarPorStockMenorA(int cantidad) {
        return productoRepository.findByStockLessThan(cantidad);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }
}
