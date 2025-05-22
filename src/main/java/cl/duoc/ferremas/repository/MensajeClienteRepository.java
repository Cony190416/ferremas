package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.MensajeCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeClienteRepository extends JpaRepository<MensajeCliente, Long> {
}
