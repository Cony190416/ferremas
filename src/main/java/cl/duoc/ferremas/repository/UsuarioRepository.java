package cl.duoc.ferremas.repository;

import cl.duoc.ferremas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   
    Optional<Usuario> findByEmail(String email);

     //método para login manual:
    Optional<Usuario> findByEmailAndContrasena(String email, String contrasena);
}
