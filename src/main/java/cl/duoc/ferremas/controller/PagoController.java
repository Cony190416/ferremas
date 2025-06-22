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
import java.util.Map;

@Controller
@RequestMapping("/api/pago") // Ruta base para todas las operaciones de pago
public class PagoController {

    @Autowired
    private PagoService pagoService; // Inyección del servicio que maneja la lógica de pagos

    // Inicia una transacción de pago con el monto recibido
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
        return "pago-exitoso";  // Mapea a archivo HTML en templates
    }

    // Devuelve la vista para mostrar cuando el pago fue fallido
    @GetMapping("/pago-fallido")
    public String mostrarVistaPagoFallido() {
        return "pago-fallido";  // Mapea a archivo HTML en templates
    }
}
