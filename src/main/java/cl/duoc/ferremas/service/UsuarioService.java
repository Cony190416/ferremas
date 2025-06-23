package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(String email, String contrasena) {
        return usuarioRepository.findByEmailAndContrasena(email, contrasena).orElse(null);
    }
}
