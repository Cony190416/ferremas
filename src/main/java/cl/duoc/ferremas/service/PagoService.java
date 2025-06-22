package cl.duoc.ferremas.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service // Marca esta clase como un componente de servicio de Spring (gestión lógica de negocio)
public class PagoService {

    private final WebClient webClient; // Cliente HTTP reactivo para hacer llamadas a servicios externos

    // Constantes para configuración del entorno de integración de Transbank (no usar en producción)
    private static final String BASE_URL = "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.0";
    private static final String COMMERCE_CODE = "597055555532"; // Código de comercio de prueba
    private static final String API_KEY = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C"; // Llave secreta de prueba
    private static final String RETURN_URL = "http://localhost:8080/api/pago/confirmar"; // URL donde Transbank redirige tras el pago

    // Constructor que configura el WebClient con la URL base
    public PagoService() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Inicia una transacción WebPay.
     * @param monto Monto de la compra
     * @return Mono con respuesta que incluye token, URL y full_url para redirigir
     */
    public Mono<Map<String, Object>> iniciarTransaccion(Double monto) {
        // Genera identificadores únicos para orden de compra y sesión
        String buyOrder = UUID.randomUUID().toString().substring(0, 26);
        String sessionId = UUID.randomUUID().toString().substring(0, 26);

        // Prepara los datos a enviar a Transbank
        Map<String, Object> payload = new HashMap<>();
        payload.put("buy_order", buyOrder);
        payload.put("session_id", sessionId);
        payload.put("amount", monto);
        payload.put("return_url", RETURN_URL);

        // Realiza la solicitud POST al endpoint /transactions con headers requeridos
        return webClient.post()
                .uri("/transactions")
                .header("Tbk-Api-Key-Id", COMMERCE_CODE) // Header obligatorio para identificar el comercio
                .header("Tbk-Api-Key-Secret", API_KEY)   // Header obligatorio con API Key secreta
                .bodyValue(payload)                      // Cuerpo de la petición con datos de la transacción
                .retrieve()                              // Envía la petición y espera respuesta
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}) // Mapea la respuesta a un Map
                .map(response -> {
                    // Extrae el token y la URL para construir la URL final que se usará para redirigir al cliente
                    String token = (String) response.get("token");
                    String url = (String) response.get("url");
                    response.put("full_url", url + "?token_ws=" + token); // Se agrega token a la URL
                    return response;
                });
    }

    /**
     * Confirma una transacción después del redireccionamiento del cliente
     * @param token Token entregado por WebPay
     * @return Mono con la respuesta de confirmación
     */
    public Mono<Map<String, Object>> confirmarTransaccion(String token) {
        return webClient.put() // Se usa PUT como indica la documentación de WebPay
                .uri("/transactions/{token}", token) // URI con el token recibido
                .header("Tbk-Api-Key-Id", COMMERCE_CODE)  // Header con ID del comercio
                .header("Tbk-Api-Key-Secret", API_KEY)    // Header con clave secreta
                .retrieve()                               // Ejecuta la petición
                .bodyToMono(new ParameterizedTypeReference<>() {}); // Mapea la respuesta como Map
    }
}
