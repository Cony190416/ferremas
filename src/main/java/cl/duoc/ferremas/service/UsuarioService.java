package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String email, String contrasena) {
        return usuarioRepository.findByEmailAndContrasena(email, contrasena).orElse(null);
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return Lista con todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario
     * @return Optional con el usuario o vacío si no existe
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Cambia el estado activo/inactivo de un usuario.
     * @param id ID del usuario
     * @return Usuario con el estado actualizado
     * @throws RuntimeException si el usuario no existe
     */
    public Usuario toggleEstadoUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(!usuario.isActivo());
            return usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    /**
     * Guarda un usuario nuevo o actualiza uno existente.
     * @param usuario Usuario a guardar
     * @return Usuario guardado
     */
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar
     */
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
