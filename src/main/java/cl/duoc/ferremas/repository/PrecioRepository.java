package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  // Indica que esta interfaz es un componente de repositorio en Spring
public interface PrecioRepository extends JpaRepository<Precio, Long> {
    
    // Método personalizado para obtener todos los precios asociados a un producto
    // según su código. Spring genera automáticamente la consulta gracias a la convención
    // del nombre del método (findBy + atributo anidado).
    List<Precio> findByProductoCodigo(String codigoProducto);
    
    // Ejemplo: si "codigoProducto" es "FER-12345", devolverá todos los precios registrados
    // para ese producto específico.
}
