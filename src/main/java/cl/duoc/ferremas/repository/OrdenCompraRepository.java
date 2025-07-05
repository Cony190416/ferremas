package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    
    // Buscar por buy_order (único)
    Optional<OrdenCompra> findByBuyOrder(String buyOrder);
    
    // Buscar por token
    Optional<OrdenCompra> findByTokenWs(String tokenWs);
    
    // Buscar por cliente
    List<OrdenCompra> findByClienteIdOrderByFechaCreacionDesc(Long clienteId);
    
    // Buscar por estado
    List<OrdenCompra> findByEstadoOrderByFechaCreacionDesc(OrdenCompra.EstadoOrden estado);
    
    // Buscar órdenes entre fechas
    @Query("SELECT o FROM OrdenCompra o WHERE o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY o.fechaCreacion DESC")
    List<OrdenCompra> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                @Param("fechaFin") LocalDateTime fechaFin);
    
    // Buscar órdenes autorizadas entre fechas (para contador)
    @Query("SELECT o FROM OrdenCompra o WHERE o.estado = 'AUTORIZADO' AND o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY o.fechaCreacion DESC")
    List<OrdenCompra> findAutorizadasByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                           @Param("fechaFin") LocalDateTime fechaFin);
    
    // Contar órdenes por estado
    long countByEstado(OrdenCompra.EstadoOrden estado);
    
    // Sumar montos de órdenes autorizadas entre fechas
    @Query("SELECT SUM(o.montoTotal) FROM OrdenCompra o WHERE o.estado = 'AUTORIZADO' AND o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    Double sumMontoTotalAutorizadasByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                         @Param("fechaFin") LocalDateTime fechaFin);
} 