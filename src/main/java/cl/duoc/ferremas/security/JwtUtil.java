package cl.duoc.ferremas.security;

// import io.jsonwebtoken.*;
// import org.springframework.stereotype.Component;

// import java.util.Date;

// @Component
public class JwtUtil {

    // private final String SECRET_KEY = "clave-secreta-jwt";
    // private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    // public String generarToken(String email) {
    //     return Jwts.builder()
    //             .setSubject(email)
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
    //             .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
    //             .compact();
    // }

    // public String extraerEmail(String token) {
    //     return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    // }

    // public boolean validarToken(String token) {
    //     try {
    //         Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }
}
