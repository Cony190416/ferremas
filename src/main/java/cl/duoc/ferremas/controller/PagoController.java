package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/pago") // Ruta base para todas las operaciones de pago
public class PagoController {

    @Autowired
    private PagoService pagoService; // Inyección del servicio que maneja la lógica de pagos

    // Endpoint de prueba simple
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Test endpoint funcionando");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    // Inicia una transacción de pago con el carrito completo
    @PostMapping("/iniciar-carrito")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> iniciarPagoConCarrito(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("🔍 Recibida petición de pago con carrito");
            System.out.println("📦 Request data: " + request);
            
            // Validar que existan los campos requeridos
            if (!request.containsKey("carrito")) {
                System.out.println("❌ Error: Campo 'carrito' es requerido");
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Campo 'carrito' es requerido");
                return Mono.just(ResponseEntity.badRequest().body(error));
            }
            
            if (!request.containsKey("divisa")) {
                System.out.println("❌ Error: Campo 'divisa' es requerido");
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Campo 'divisa' es requerido");
                return Mono.just(ResponseEntity.badRequest().body(error));
            }
            
            if (!request.containsKey("tasaCambio")) {
                System.out.println("❌ Error: Campo 'tasaCambio' es requerido");
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Campo 'tasaCambio' es requerido");
                return Mono.just(ResponseEntity.badRequest().body(error));
            }
            
            // Extraer datos del request
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> carritoItems = (List<Map<String, Object>>) request.get("carrito");
            String divisa = (String) request.get("divisa");
            Double tasaCambio = ((Number) request.get("tasaCambio")).doubleValue();
            Long clienteId = request.get("clienteId") != null ? ((Number) request.get("clienteId")).longValue() : null;
            
            System.out.println("✅ Datos extraídos correctamente:");
            System.out.println("   - Carrito items: " + carritoItems.size());
            System.out.println("   - Divisa: " + divisa);
            System.out.println("   - Tasa cambio: " + tasaCambio);
            System.out.println("   - Cliente ID: " + clienteId);
            
            // Llamar al servicio de pago
            System.out.println("🚀 Llamando al servicio de pago...");
            return pagoService.iniciarTransaccionConCarrito(carritoItems, divisa, tasaCambio, clienteId)
                    .map(response -> {
                        System.out.println("✅ Respuesta del servicio: " + response);
                        return ResponseEntity.ok(response);
                    })
                    .onErrorResume(error -> {
                        System.err.println("❌ Error en el servicio de pago: " + error.getMessage());
                        error.printStackTrace();
                        Map<String, Object> errorResponse = new HashMap<>();
                        errorResponse.put("error", "Error interno del servidor: " + error.getMessage());
                        return Mono.just(ResponseEntity.status(500).body(errorResponse));
                    });
                    
        } catch (Exception e) {
            System.err.println("❌ Error en el controlador: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error interno del servidor: " + e.getMessage());
            return Mono.just(ResponseEntity.status(500).body(error));
        }
    }

    // Inicia una transacción de pago con el monto recibido (método original)
    @PostMapping("/iniciar")
    @ResponseBody // Retorna un JSON como respuesta
    public Mono<ResponseEntity<Map<String, Object>>> iniciarPago(@RequestParam("monto") Double monto) {
        return pagoService.iniciarTransaccion(monto)
                .map(ResponseEntity::ok) // Si es exitoso, responde con los datos de la transacción
                .onErrorResume(e -> { // En caso de error, devuelve un mensaje con el error
                    e.printStackTrace();
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Error al iniciar transacción: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(error));
                });
    }

    // Confirma la transacción con el token entregado por Webpay (puede ser GET o POST)
    @RequestMapping(value = "/confirmar", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<ResponseEntity<Object>> confirmarPago(@RequestParam("token_ws") String token) {
        return pagoService.confirmarTransaccion(token)
                .map(response -> {
                    // Redirige según el estado de la transacción
                    if ("AUTHORIZED".equals(response.get("status"))) {
                        return ResponseEntity.status(HttpStatus.FOUND)
                                .header(HttpHeaders.LOCATION, "/api/pago/pago-exitoso")
                                .build();
                    } else {
                        return ResponseEntity.status(HttpStatus.FOUND)
                                .header(HttpHeaders.LOCATION, "/api/pago/pago-fallido")
                                .build();
                    }
                })
                .onErrorResume(e -> {
                    // Si hay error al confirmar, redirige a vista de fallo
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                            .header(HttpHeaders.LOCATION, "/api/pago/pago-fallido")
                            .build());
                });
    }

    // Devuelve la vista para mostrar cuando el pago fue exitoso
    @GetMapping("/pago-exitoso")
    public String mostrarVistaPagoExitoso() {
        return "pago/pago-exitoso";  // Mapea a archivo HTML en templates
    }

    // Devuelve la vista para mostrar cuando el pago fue fallido
    @GetMapping("/pago-fallido")
    public String mostrarVistaPagoFallido() {
        return "pago/pago-fallido";  // Mapea a archivo HTML en templates
    }
}
