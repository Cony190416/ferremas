package cl.duoc.ferremas.security;

import cl.duoc.ferremas.model.Cliente;
import cl.duoc.ferremas.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioPrincipal implements UserDetails {

    private final Usuario usuario;
    private final Cliente cliente;
    private final boolean esCliente;

    public UsuarioPrincipal(Usuario usuario) {
        this.usuario = usuario;
        this.cliente = null;
        this.esCliente = false;
    }

    public UsuarioPrincipal(Cliente cliente) {
        this.usuario = null;
        this.cliente = cliente;
        this.esCliente = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (esCliente) {
            return Collections.singletonList(new SimpleGrantedAuthority("CLIENTE"));
        } else {
            return Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().name()));
        }
    }

    @Override
    public String getPassword() {
        if (esCliente) {
            return cliente.getContrasena();
        } else {
            return usuario.getContrasena();
        }
    }

    @Override
    public String getUsername() {
        if (esCliente) {
            return cliente.getEmail();
        } else {
            return usuario.getEmail();
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean esCliente() {
        return esCliente;
    }
}
