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

    /**
     * Retorna todos los productos disponibles en la base de datos.
     */
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su código.
     *
     * @param codigo Código único del producto
     * @return Optional con el producto encontrado o vacío si no existe
     */
    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    /**
     * Busca productos que contengan una coincidencia parcial en su nombre (sin distinción de mayúsculas).
     *
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca productos que pertenezcan a una categoría específica.
     *
     * @param categoria Categoría del producto
     * @return Lista de productos de esa categoría
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    /**
     * Busca productos cuyo stock sea menor al valor especificado.
     *
     * @param cantidad Límite de stock inferior
     * @return Lista de productos con stock bajo
     */
    public List<Producto> buscarPorStockMenorA(int cantidad) {
        return productoRepository.findByStockLessThan(cantidad);
    }

    /**
     * Guarda o actualiza un producto en la base de datos.
     *
     * @param producto Producto a guardar o actualizar
     * @return El producto guardado
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto por su código.
     *
     * @param codigo Código del producto a eliminar
     */
    public void eliminarProducto(String codigo) {
        productoRepository.deleteById(codigo);
    }

    /**
     * Actualiza el stock de un producto.
     *
     * @param codigo Código del producto
     * @param nuevoStock Nuevo valor de stock
     * @return Optional con el producto actualizado o vacío si no se encontró
     */
    public Optional<Producto> actualizarStock(String codigo, int nuevoStock) {
        Optional<Producto> productoOpt = productoRepository.findById(codigo);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }
        return productoOpt;
    }
}
