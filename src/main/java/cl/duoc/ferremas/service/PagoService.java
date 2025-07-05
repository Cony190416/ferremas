package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.OrdenCompra;
import cl.duoc.ferremas.model.OrdenItem;
import cl.duoc.ferremas.model.Producto;
import cl.duoc.ferremas.repository.OrdenCompraRepository;
import cl.duoc.ferremas.repository.OrdenItemRepository;
import cl.duoc.ferremas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service // Marca esta clase como un componente de servicio de Spring (gestión lógica de negocio)
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private final WebClient webClient; // Cliente HTTP reactivo para hacer llamadas a servicios externos
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenItemRepository ordenItemRepository;
    private final ProductoRepository productoRepository;

    // Constantes para configuración del entorno de integración de Transbank (no usar en producción)
    private static final String BASE_URL = "https://webpay3gint.transbank.cl/rswebpaytransaction/api/webpay/v1.0";
    private static final String COMMERCE_CODE = "597055555532"; // Código de comercio de prueba
    private static final String API_KEY = "579B532A7440BB0C9079DED94D31EA1615BACEB56610332264630D42D0A36B1C"; // Llave secreta de prueba
    private static final String RETURN_URL = "http://localhost:8080/api/pago/confirmar"; // URL donde Transbank redirige tras el pago

    
    public PagoService(OrdenCompraRepository ordenCompraRepository, 
                      OrdenItemRepository ordenItemRepository,
                      ProductoRepository productoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.ordenItemRepository = ordenItemRepository;
        this.productoRepository = productoRepository;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    /**
     * Inicia una transacción WebPay con items del carrito.
     * @param carritoItems Lista de items del carrito
     * @param divisa Divisa de la transacción
     * @param tasaCambio Tasa de cambio aplicada
     * @param clienteId ID del cliente (opcional)
     * @return Mono con respuesta que incluye token, URL y full_url para redirigir
     */
    public Mono<Map<String, Object>> iniciarTransaccionConCarrito(List<Map<String, Object>> carritoItems, 
                                                                  String divisa, 
                                                                  Double tasaCambio, 
                                                                  Long clienteId) {
        log.info("Iniciando pago para carrito con {} items", carritoItems.size());
        
        try {
            // Calcular monto total
            Double montoTotal = carritoItems.stream()
                    .mapToDouble(item -> {
                        // Manejar tanto Integer como Double para precio
                        Object precioObj = item.get("precio");
                        Double precio;
                        if (precioObj instanceof Integer) {
                            precio = ((Integer) precioObj).doubleValue();
                        } else {
                            precio = (Double) precioObj;
                        }
                        
                        // Manejar tanto Integer como Double para cantidad
                        Object cantidadObj = item.get("cantidad");
                        Integer cantidad;
                        if (cantidadObj instanceof Double) {
                            cantidad = ((Double) cantidadObj).intValue();
                        } else {
                            cantidad = (Integer) cantidadObj;
                        }
                        
                        return precio * cantidad;
                    })
                    .sum();

            log.info("Total calculado: ${}", montoTotal);

            // Generar identificadores únicos
            String buyOrder = UUID.randomUUID().toString().substring(0, 26);
            String sessionId = UUID.randomUUID().toString().substring(0, 26);

            // Crear orden de compra
            OrdenCompra ordenCompra = new OrdenCompra();
            ordenCompra.setBuyOrder(buyOrder);
            ordenCompra.setSessionId(sessionId);
            ordenCompra.setMontoTotal(montoTotal);
            ordenCompra.setDivisa(divisa);
            ordenCompra.setTasaCambio(tasaCambio);

            // Guardar orden de compra
            OrdenCompra ordenCompraFinal = ordenCompraRepository.save(ordenCompra);
            log.info("Orden creada con ID: {}", ordenCompraFinal.getId());

            // Crear items de la orden
            for (Map<String, Object> item : carritoItems) {
                String codigoProducto = (String) item.get("codigo");
                Optional<Producto> productoOpt = productoRepository.findByCodigo(codigoProducto);
                
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    OrdenItem ordenItem = new OrdenItem();
                    ordenItem.setOrdenCompra(ordenCompraFinal);
                    ordenItem.setProducto(producto);
                    
                    // Manejar tanto Integer como Double para cantidad
                    Object cantidadObj = item.get("cantidad");
                    Integer cantidad;
                    if (cantidadObj instanceof Double) {
                        cantidad = ((Double) cantidadObj).intValue();
                    } else {
                        cantidad = (Integer) cantidadObj;
                    }
                    ordenItem.setCantidad(cantidad);
                    
                    // Manejar tanto Integer como Double para precio
                    Object precioObj = item.get("precio");
                    Double precio;
                    if (precioObj instanceof Integer) {
                        precio = ((Integer) precioObj).doubleValue();
                    } else {
                        precio = (Double) precioObj;
                    }
                    ordenItem.setPrecioUnitario(precio);
                    ordenItem.setDivisa(divisa);
                    
                    // Guardar item
                    ordenItemRepository.save(ordenItem);
                }
            }

            log.info("Items de orden guardados correctamente");
            
            // Hacer llamada real a WebPay para obtener token válido
            return iniciarTransaccion(montoTotal)
                    .map(webpayResponse -> {
                        String token = (String) webpayResponse.get("token");
                        String fullUrl = (String) webpayResponse.get("full_url");
                        
                        log.info("Token real de WebPay obtenido: {}", token);
                        log.info("URL completa de WebPay: {}", fullUrl);
                        
                        // Crear respuesta combinando datos de la orden y WebPay
                        Map<String, Object> respuesta = new HashMap<>();
                        respuesta.put("success", true);
                        respuesta.put("orden_id", ordenCompraFinal.getId());
                        respuesta.put("total", montoTotal);
                        respuesta.put("full_url", fullUrl);
                        respuesta.put("token", token);
                        respuesta.put("url", webpayResponse.get("url"));
                        respuesta.put("mensaje", "Pago iniciado correctamente. Redirigiendo a WebPay...");
                        
                        log.info("Respuesta de pago con token real generada exitosamente");
                        return respuesta;
                    })
                    .onErrorResume(error -> {
                        log.error("Error al obtener token de WebPay: ", error);
                        Map<String, Object> respuesta = new HashMap<>();
                        respuesta.put("success", false);
                        respuesta.put("error", "Error al conectar con WebPay: " + error.getMessage());
                        return Mono.just(respuesta);
                    });
            
        } catch (Exception e) {
            log.error("Error al procesar el pago: ", e);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", false);
            respuesta.put("error", "Error interno del servidor: " + e.getMessage());
            return Mono.just(respuesta);
        }
    }

    /**
     * Inicia una transacción WebPay simple (método original).
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
     * Confirma una transacción WebPay usando el token recibido.
     * @param token Token de la transacción
     * @return Mono con respuesta de confirmación
     */
    public Mono<Map<String, Object>> confirmarTransaccion(String token) {
        return webClient.put() // Se usa PUT como indica la documentación de WebPay
                .uri("/transactions/{token}", token) // URI con el token recibido
                .header("Tbk-Api-Key-Id", COMMERCE_CODE)  // Header con ID del comercio
                .header("Tbk-Api-Key-Secret", API_KEY)    // Header con clave secreta
                .retrieve()                               // Ejecuta la petición
                .bodyToMono(new ParameterizedTypeReference<>() {}); // Mapea la respuesta como Map
    }

    /**
     * Obtiene todas las órdenes de compra para el panel de admin.
     * @return Lista con todas las órdenes de compra
     */
    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        return ordenCompraRepository.findAll();
    }
}
