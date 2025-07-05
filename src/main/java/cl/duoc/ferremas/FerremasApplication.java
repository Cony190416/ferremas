package cl.duoc.ferremas; // Define el paquete al que pertenece la clase

// Importa las clases necesarias de Spring Boot
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotación que indica que esta clase es la principal de una aplicación Spring Boot
// Incluye automáticamente: @Configuration, @EnableAutoConfiguration y @ComponentScan
@SpringBootApplication
public class FerremasApplication {

    // Método principal que sirve como punto de entrada para iniciar la aplicación
    public static void main(String[] args) {
        // Lanza la aplicación Spring Boot utilizando esta clase como configuración base
        SpringApplication.run(FerremasApplication.class, args);
    }
}
