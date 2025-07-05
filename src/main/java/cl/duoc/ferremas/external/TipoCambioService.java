package cl.duoc.ferremas.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service // Servicio de Spring para inyección y uso en otros componentes
public class TipoCambioService {

    // URL pública para obtener el tipo de cambio del dólar desde mindicador.cl
    private final String url = "https://mindicador.cl/api/dolar";

    // Cliente HTTP para hacer peticiones REST
    private final RestTemplate restTemplate = new RestTemplate();

    // Mapeador JSON para parsear la respuesta
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene el valor actual del dólar en pesos chilenos consultando la API externa.
     * @return valor del dólar como Double, o null en caso de error.
     */
    public Double obtenerValorDolar() {
        try {
            // Hace la petición GET y recibe la respuesta como String JSON
            String response = restTemplate.getForObject(url, String.class);

            // Convierte el String JSON a objeto JsonNode para navegar en la estructura
            JsonNode root = objectMapper.readTree(response);

            // Accede a la primera entrada de la serie y obtiene el valor del dólar
            return root.path("serie").get(0).path("valor").asDouble();

        } catch (Exception e) {
            e.printStackTrace(); // Imprime error para debug
            return null;  // Devuelve null si algo falla, podrías mejorar lanzando excepción
        }
    }

    /**
     * Convierte un monto dado en dólares a pesos chilenos usando el valor actual.
     * @param montoEnDolares monto en USD
     * @return equivalente en pesos chilenos, o null si falla la consulta del tipo de cambio
     */
    public Double convertirAPesos(Double montoEnDolares) {
        Double valorDolar = obtenerValorDolar();
        if (valorDolar == null) return null;
        return montoEnDolares * valorDolar;
    }

    /**
     * Convierte un monto dado en pesos chilenos a dólares usando el valor actual.
     * @param montoEnPesos monto en CLP
     * @return equivalente en dólares, o null si falla la consulta del tipo de cambio
     */
    public Double convertirADolares(Double montoEnPesos) {
        Double valorDolar = obtenerValorDolar();
        if (valorDolar == null) return null;
        return montoEnPesos / valorDolar;
    }
}