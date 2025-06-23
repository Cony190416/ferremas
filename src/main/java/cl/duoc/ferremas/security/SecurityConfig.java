package cl.duoc.ferremas.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configuración CSRF: ignorar en estas rutas solamente
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    new AntPathRequestMatcher("/productos/**"),
                    new AntPathRequestMatcher("/api/productos/**"),
                    new AntPathRequestMatcher("/divisa/**"),
                    new AntPathRequestMatcher("/pago/**"),
                    new AntPathRequestMatcher("/api/pago/**"),
                    new AntPathRequestMatcher("/api/contacto/**")
                )
            )
            // Configuración de autorización (debe ir fuera de .csrf)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/index.html",
                    "/css/**", "/js/**", "/img/**",
                    "/login", "/logout",
                    "/cliente/registro", "/api/clientes/registrar", "api/cliente/registro", "api/clientes/login", "/api/clientes/login",

                    // Rutas divisa públicas
                    "/divisa/convertir",
                    "/divisa/convertir-inverso",
                    "/divisa/dolar",

                    // Productos públicos (todas las rutas)
                    "/productos/**",
                    "/api/productos/**",

                    // Rutas pago públicas
                    "/pago/pago-exitoso",
                    "/pago/pago-fallido",

                    // APIs públicas divisa y pago
                    "/api/divisa/**",
                    "/api/pago/**"
                ).permitAll()
                // Roles para otras rutas
                .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/bodeguero/**").hasRole("BODEGUERO")
                .requestMatchers("/contador/**").hasRole("CONTADOR")
                .requestMatchers("/vendedor/**").hasRole("VENDEDOR")
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            // Login con formulario personalizado
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("contrasena")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
                        String rol = usuarioPrincipal.getUsuario().getRol().name();
                        String email = usuarioPrincipal.getUsername();

                        System.out.println("✅ Login exitoso");
                        System.out.println("🔐 Usuario: " + email);
                        System.out.println("🔑 Rol: " + rol);

                        switch (rol) {
                            case "ADMINISTRADOR" -> response.sendRedirect("/admin/home");
                            case "BODEGUERO"     -> response.sendRedirect("/bodeguero/home");
                            case "CONTADOR"      -> response.sendRedirect("/contador/home");
                            case "VENDEDOR"      -> response.sendRedirect("/vendedor/home");
                            default              -> response.sendRedirect("/");
                        }
                    }
                })
                .permitAll()
            )
            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Servicio de detalles de usuario
            .userDetailsService(userDetailsServiceImpl);

        return http.build();
    }
}
