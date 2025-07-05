package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.OrdenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
    
    // Buscar items por orden de compra
    List<OrdenItem> findByOrdenCompraId(Long ordenCompraId);
    
    // Buscar items por producto
    List<OrdenItem> findByProductoCodigo(String codigoProducto);
    
    // Contar items por orden de compra
    long countByOrdenCompraId(Long ordenCompraId);
} 