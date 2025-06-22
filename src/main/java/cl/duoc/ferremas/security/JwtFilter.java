package cl.duoc.ferremas.security;

// import cl.duoc.ferremas.service.UsuarioService;
// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;

// import java.io.IOException;

// @Component
public class JwtFilter /* implements Filter */ {

    // @Autowired
    // private JwtUtil jwtUtil;

    // @Autowired
    // private UsuarioService usuarioService;

    // @Override
    // public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    //         throws IOException, ServletException {
    //
    //     HttpServletRequest httpRequest = (HttpServletRequest) request;
    //     String path = httpRequest.getServletPath();
    //
    //     // Ignorar el filtro para rutas públicas de login y registro
    //     if (path.equals("/auth/login") || path.equals("/auth/registro")) {
    //         chain.doFilter(request, response);
    //         return; // Salir para no validar token en estas rutas
    //     }
    //
    //     String authHeader = httpRequest.getHeader("Authorization");
    //
    //     if (authHeader != null && authHeader.startsWith("Bearer ")) {
    //         String token = authHeader.substring(7);
    //         String email = jwtUtil.extraerEmail(token);
    //
    //         if (email != null && jwtUtil.validarToken(token)) {
    //             var usuario = usuarioService.loadUserByUsername(email);
    //             var auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
    //             SecurityContextHolder.getContext().setAuthentication(auth);
    //         }
    //     }
    //
    //     chain.doFilter(request, response);
    // }
}
