package cl.duoc.ferremas.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "cliente123"; // Cambia por la que tú necesites
        String hashedPassword = encoder.encode(password);

        System.out.println("¿Coincide admin123 con hash? " + encoder.matches("admin123", "$2a$10$L2A9gSuXOccF3h4iDIWQnePUc5PBJId7eB4i3JXUu/gC0wfW9Cz/C"));
        System.out.println("Contraseña original: " + password);
        System.out.println("Contraseña encriptada: " + hashedPassword);
    }
}


//admin123
//bodeguero123
//contador123
//vendedor123
//cliente123