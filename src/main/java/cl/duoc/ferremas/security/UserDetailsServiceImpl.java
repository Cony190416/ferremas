package cl.duoc.ferremas.security;

import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario con email: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("❌ Usuario no encontrado con email: " + email));

        System.out.println("✅ Usuario encontrado: " + usuario.getEmail() + " con rol " + usuario.getRol());

        return new UsuarioPrincipal(usuario);
    }
}

