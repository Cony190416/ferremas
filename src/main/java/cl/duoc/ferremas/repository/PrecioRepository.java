package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {

    // Buscar historial de precios por código de producto
    List<Precio> findByProductoCodigo(String codigoProducto);
}
