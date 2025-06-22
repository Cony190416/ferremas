package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Marca esta interfaz como un repositorio de Spring (componente de acceso a datos)
public interface ProductoRepository extends JpaRepository<Producto, String> {
    // Esta interfaz extiende JpaRepository y gestiona entidades Producto identificadas por una clave primaria de tipo String (el campo "codigo")

    /**
     * Busca productos cuyo nombre contenga una cadena dada, ignorando mayúsculas y minúsculas.
     * Utiliza la convención de nombres de Spring Data JPA para generar automáticamente la consulta.
     * Ejemplo: findByNombreContainingIgnoreCase("martillo") → devuelve todos los productos que contengan "martillo" en el nombre.
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca todos los productos que pertenezcan a una categoría específica.
     * Ejemplo: findByCategoria("Herramientas") → devuelve todos los productos en esa categoría.
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Busca productos con stock inferior al valor dado.
     * Ejemplo: findByStockLessThan(10) → devuelve todos los productos cuyo stock sea menor a 10.
     */
    List<Producto> findByStockLessThan(int stock);
}
