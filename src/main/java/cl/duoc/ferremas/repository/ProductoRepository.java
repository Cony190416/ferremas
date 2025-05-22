package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    // Buscar por nombre completo o parcial (ignora mayúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por categoría
    List<Producto> findByCategoria(String categoria);

    // Buscar por stock menor a cierto valor
    List<Producto> findByStockLessThan(int stock);
}
