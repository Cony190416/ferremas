package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    /**
     * Busca productos cuyo nombre contenga una cadena dada, sin distinguir entre mayúsculas y minúsculas.
     * @param nombre Parte del nombre a buscar
     * @return Lista de productos coincidentes
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca todos los productos que pertenezcan a una categoría específica.
     * @param categoria Nombre de la categoría
     * @return Lista de productos en la categoría
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Busca productos con stock menor al valor proporcionado.
     * @param stock Límite inferior de stock
     * @return Lista de productos con bajo stock
     */
    List<Producto> findByStockLessThan(int stock);
}
