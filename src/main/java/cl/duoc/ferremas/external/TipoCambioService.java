package cl.duoc.ferremas.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TipoCambioService {

    private final String url = "https://mindicador.cl/api/dolar";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Double obtenerValorDolar() {
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("serie").get(0).path("valor").asDouble();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Puedes lanzar excepción o manejar error según prefieras
        }
    }

    public Double convertirAPesos(Double montoEnDolares) {
        Double valorDolar = obtenerValorDolar();
        if (valorDolar == null) return null;
        return montoEnDolares * valorDolar;
    }

    public Double convertirADolares(Double montoEnPesos) {
        Double valorDolar = obtenerValorDolar();
        if (valorDolar == null) return null;
        return montoEnPesos / valorDolar;
    }
}

