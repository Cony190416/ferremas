package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.MensajeCliente;
import cl.duoc.ferremas.service.MensajeClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Define un controlador REST (devuelve JSON por defecto)
@RequestMapping("/api/contacto") // Ruta base para las peticiones relacionadas a mensajes de contacto
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen (útil para el frontend)
public class MensajeClienteController {

    // Inyección por constructor del servicio que gestiona los mensajes
    private final MensajeClienteService mensajeClienteService;

    public MensajeClienteController(MensajeClienteService mensajeClienteService) {
        this.mensajeClienteService = mensajeClienteService;
    }

    // POST /api/contacto - Permite enviar un mensaje de contacto
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody MensajeCliente mensaje) {
        try {
            // Validación: nombre no puede estar vacío
            if (mensaje.getNombre() == null || mensaje.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre es obligatorio");
            }

            // Validación: correo no puede estar vacío
            if (mensaje.getCorreo() == null || mensaje.getCorreo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El correo es obligatorio");
            }

            // Validación: mensaje no puede estar vacío
            if (mensaje.getMensaje() == null || mensaje.getMensaje().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El mensaje es obligatorio");
            }

            // Validación simple del formato del correo
            if (!mensaje.getCorreo().contains("@")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El formato del correo no es válido");
            }

            // Guarda el mensaje si todo está correcto
            MensajeCliente nuevoMensaje = mensajeClienteService.guardarMensaje(mensaje);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMensaje); // 201 Created

        } catch (Exception e) {
            // Error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar mensaje: " + e.getMessage());
        }
    }

    // GET /api/contacto - Devuelve todos los mensajes (uso típico: administrador)
    @GetMapping
    public ResponseEntity<List<MensajeCliente>> obtenerMensajes() {
        List<MensajeCliente> mensajes = mensajeClienteService.obtenerMensajes();
        return ResponseEntity.ok(mensajes); // 200 OK con lista de mensajes
    }

    // GET /api/contacto/{id} - Buscar un mensaje por su ID (aún no implementado)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMensajePorId(@PathVariable Long id) {
        try {
            // Aquí falta implementar el método en el servicio que obtenga el mensaje por ID
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body("Funcionalidad pendiente de implementar");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener mensaje: " + e.getMessage());
        }
    }
}
