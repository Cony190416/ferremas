package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.MensajeCliente;
import cl.duoc.ferremas.service.MensajeClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "*") // Para permitir llamadas desde frontend
public class MensajeClienteController {

    private final MensajeClienteService mensajeClienteService;

    public MensajeClienteController(MensajeClienteService mensajeClienteService) {
        this.mensajeClienteService = mensajeClienteService;
    }

    // POST /api/contacto - Enviar mensaje de contacto
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody MensajeCliente mensaje) {
        try {
            // Validaciones básicas
            if (mensaje.getNombre() == null || mensaje.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre es obligatorio");
            }
            
            if (mensaje.getCorreo() == null || mensaje.getCorreo().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El correo es obligatorio");
            }
            
            if (mensaje.getMensaje() == null || mensaje.getMensaje().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El mensaje es obligatorio");
            }
            
            // Validar formato de correo básico
            if (!mensaje.getCorreo().contains("@")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El formato del correo no es válido");
            }
            
            MensajeCliente nuevoMensaje = mensajeClienteService.guardarMensaje(mensaje);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMensaje);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar mensaje: " + e.getMessage());
        }
    }

    // GET /api/contacto - Obtener todos los mensajes (para administradores)
    @GetMapping
    public ResponseEntity<List<MensajeCliente>> obtenerMensajes() {
        List<MensajeCliente> mensajes = mensajeClienteService.obtenerMensajes();
        return ResponseEntity.ok(mensajes);
    }

    // GET /api/contacto/{id} - Obtener mensaje específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMensajePorId(@PathVariable Long id) {
        try {
            // Nota: necesitarás agregar este método en el service
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body("Funcionalidad pendiente de implementar");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener mensaje: " + e.getMessage());
        }
    }
}
