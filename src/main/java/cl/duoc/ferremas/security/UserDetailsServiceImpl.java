package cl.duoc.ferremas.security;

import cl.duoc.ferremas.model.Cliente;
import cl.duoc.ferremas.model.Usuario;
import cl.duoc.ferremas.repository.ClienteRepository;
import cl.duoc.ferremas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario con email: " + email);

        // Primero buscar en la tabla usuario (colaboradores)
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        
        if (usuario != null) {
            System.out.println("✅ Usuario colaborador encontrado: " + usuario.getEmail() + " con rol " + usuario.getRol());
            return new UsuarioPrincipal(usuario);
        }
        
        // Si no se encuentra en usuario, buscar en la tabla cliente
        Cliente cliente = clienteRepository.findByEmail(email);
        
        if (cliente != null) {
            System.out.println("✅ Cliente encontrado: " + cliente.getEmail() + " (sin rol específico)");
            // Crear un UsuarioPrincipal con rol CLIENTE para el cliente
            return new UsuarioPrincipal(cliente);
        }
        
        // Si no se encuentra en ninguna tabla
        throw new UsernameNotFoundException("❌ Usuario no encontrado con email: " + email);
    }
}

