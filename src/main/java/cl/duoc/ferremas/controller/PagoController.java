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
@RequestMapping("/api/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // POST para iniciar transacción - REST API que responde JSON
    @PostMapping("/iniciar")
    @ResponseBody
    public Mono<ResponseEntity<Map<String, Object>>> iniciarPago(@RequestParam("monto") Double monto) {
        return pagoService.iniciarTransaccion(monto)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Error al iniciar transacción: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(error));
                });
    }

    // Acepta GET y POST desde Webpay para confirmar transacción
    @RequestMapping(value = "/confirmar", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<ResponseEntity<Object>> confirmarPago(@RequestParam("token_ws") String token) {
        return pagoService.confirmarTransaccion(token)
                .map(response -> {
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
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                            .header(HttpHeaders.LOCATION, "/api/pago/pago-fallido")
                            .build());
                });
    }

    // Vista de pago exitoso
    @GetMapping("/pago-exitoso")
    public String mostrarVistaPagoExitoso() {
        return "pago-exitoso";  // templates/pago-exitoso.html
    }

    // Vista de pago fallido
    @GetMapping("/pago-fallido")
    public String mostrarVistaPagoFallido() {
        return "pago-fallido";  // templates/pago-fallido.html
    }
}
