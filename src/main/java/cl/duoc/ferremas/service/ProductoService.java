package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un componente de servicio para que Spring la gestione como parte de su contenedor
public class ProductoService {

    // Repositorio que se encarga de interactuar con la base de datos
    private final ProductoRepository productoRepository;

    // Constructor que permite la inyección del repositorio
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los productos registrados en la base de datos.
     * @return Lista con todos los productos
     */
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su código único.
     * @param codigo Código del producto
     * @return Optional que puede contener el producto o estar vacío si no existe
     */
    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    /**
     * Busca productos cuyo nombre contenga una parte de texto, sin importar mayúsculas o minúsculas.
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos cuyo nombre contiene la cadena buscada
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca todos los productos pertenecientes a una categoría específica.
     * @param categoria Nombre de la categoría
     * @return Lista de productos que pertenecen a esa categoría
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    /**
     * Devuelve productos cuyo stock actual sea menor que un valor dado.
     * @param cantidad Límite inferior de stock
     * @return Lista de productos con stock bajo
     */
    public List<Producto> buscarPorStockMenorA(int cantidad) {
        return productoRepository.findByStockLessThan(cantidad);
    }

    /**
     * Guarda un producto nuevo o actualiza uno existente (por su código).
     * @param producto Producto a guardar
     * @return Producto guardado o actualizado
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto de la base de datos por su código.
     * @param codigo Código del producto a eliminar
     */
    public void eliminarProducto(String codigo) {
        productoRepository.deleteById(codigo);
    }

    /**
     * Actualiza únicamente el stock de un producto específico.
     * @param codigo Código del producto a actualizar
     * @param nuevoStock Nuevo valor de stock
     * @return Optional con el producto actualizado, o vacío si no fue encontrado
     */
    public Optional<Producto> actualizarStock(String codigo, int nuevoStock) {
        Optional<Producto> productoOpt = productoRepository.findById(codigo);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get(); // Obtiene el producto
            producto.setStock(nuevoStock);         // Actualiza su stock
            productoRepository.save(producto);     // Guarda los cambios
        }
        return productoOpt;
    }

    /**
     * Actualiza un producto completo por su código.
     * @param codigo Código del producto a actualizar
     * @param productoActualizado Producto con los nuevos datos
     * @return Producto actualizado
     * @throws RuntimeException si el producto no existe
     */
    public Producto actualizarProducto(String codigo, Producto productoActualizado) {
        Optional<Producto> productoOpt = productoRepository.findById(codigo);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            
            // Actualizar solo los campos permitidos
            producto.setNombre(productoActualizado.getNombre());
            producto.setMarca(productoActualizado.getMarca());
            producto.setCategoria(productoActualizado.getCategoria());
            producto.setStock(productoActualizado.getStock());
            
            return productoRepository.save(producto);
        } else {
            throw new RuntimeException("Producto no encontrado con código: " + codigo);
        }
    }
}
