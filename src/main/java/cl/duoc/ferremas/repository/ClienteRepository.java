package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    
    Cliente findByEmail(String email); // ✅ Agregado para login
}
