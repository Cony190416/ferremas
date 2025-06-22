package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.MensajeCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Indica que esta interfaz es un componente de repositorio de Spring
public interface MensajeClienteRepository extends JpaRepository<MensajeCliente, Long> {
    // Extiende JpaRepository para proporcionar métodos CRUD automáticamente
    // JpaRepository<T, ID> — T es la entidad, ID es el tipo de su clave primaria (Long)
    // No es necesario definir ningún método adicional si solo necesitas operaciones básicas
}
