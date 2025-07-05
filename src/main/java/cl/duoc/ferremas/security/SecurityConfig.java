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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Recursos estáticos públicos
                .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/img/**").permitAll()
                // Login y registro públicos
                .requestMatchers("/login", "/logout", "/cliente/registro", "/api/clientes/registrar").permitAll()
                // APIs públicas - asegurar que estén antes de las rutas protegidas
                .requestMatchers("/api/productos/**").permitAll()
                .requestMatchers("/api/clientes/login").permitAll()
                .requestMatchers("/api/divisa/**").permitAll()
                .requestMatchers("/api/pago/**").permitAll()
                // Páginas de divisa públicas
                .requestMatchers("/divisa/**").permitAll()
                // Páginas de pago públicas
                .requestMatchers("/pago/**").permitAll()
                // Carrito público
                .requestMatchers("/carrito/**").permitAll()
                // Rutas específicas por rol - usar roles exactos de la BD
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/bodeguero/**").hasAuthority("BODEGUERO")
                .requestMatchers("/vendedor/**").hasAuthority("VENDEDOR")
                .requestMatchers("/contador/**").hasAuthority("CONTADOR")
                .requestMatchers("/cliente/**").hasAuthority("CLIENTE")
                // Todas las demás peticiones requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String targetUrl = getTargetUrlByRole(authentication);
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }
        };
    }

    // Método privado con nombre diferente para evitar conflicto
    private String getTargetUrlByRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            // Los roles en la base de datos son: ADMINISTRADOR, BODEGUERO, VENDEDOR, CONTADOR, CLIENTE
            if (role.equals("ADMINISTRADOR")) {
                return "/admin/home";
            } else if (role.equals("BODEGUERO")) {
                return "/bodeguero/home";
            } else if (role.equals("VENDEDOR")) {
                return "/vendedor/home";
            } else if (role.equals("CONTADOR")) {
                return "/contador/home";
            } else if (role.equals("CLIENTE")) {
                return "/cliente/home";
            }
        }
        return "/login?error";
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
