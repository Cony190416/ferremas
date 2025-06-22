package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.external.TipoCambioService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // Controlador REST que devuelve datos JSON automáticamente
@RequestMapping("/api/divisa") // Ruta base para este controlador
public class DivisaController {

    private final TipoCambioService tipoCambioService;

    // Constructor para inyectar el servicio que maneja el tipo de cambio
    public DivisaController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    // GET /api/divisa/dolar - Retorna valor actual del dólar en CLP y fuente
    @GetMapping("/dolar")
    public Map<String, Object> getDollarValue() {
        Double dollarValue = tipoCambioService.obtenerValorDolar(); // Obtiene valor actual del dólar

        Map<String, Object> response = new HashMap<>();
        response.put("currency", "USD"); // Moneda
        response.put("value_in_clp", dollarValue); // Valor en pesos chilenos
        response.put("source", "https://mindicador.cl"); // Fuente del dato

        return response; // Devuelve JSON con datos
    }

    // GET /api/divisa/convertir?amount=X - Convierte monto de CLP a USD
    @GetMapping("/convertir")
    public Map<String, Object> convertClpToUsd(@RequestParam Double amount) {
        Double result = tipoCambioService.convertirADolares(amount); // Convierte CLP a USD
        Double dollarValue = tipoCambioService.obtenerValorDolar(); // Valor dólar actual

        Map<String, Object> response = new HashMap<>();
        response.put("amount_in_clp", amount); // Monto original en CLP
        response.put("current_dollar_value", dollarValue); // Valor dólar actual
        response.put("converted_amount_usd", result); // Monto convertido a USD
        response.put("origin_currency", "CLP"); // Moneda origen
        response.put("target_currency", "USD"); // Moneda destino

        return response; // Devuelve JSON con resultado
    }

    // GET /api/divisa/convertir-inverso?amount=X - Convierte monto de USD a CLP
    @GetMapping("/convertir-inverso")
    public Map<String, Object> convertUsdToClp(@RequestParam Double amount) {
        Double result = tipoCambioService.convertirAPesos(amount); // Convierte USD a CLP
        Double dollarValue = tipoCambioService.obtenerValorDolar(); // Valor dólar actual

        Map<String, Object> response = new HashMap<>();
        response.put("amount_in_usd", amount); // Monto original en USD
        response.put("current_dollar_value", dollarValue); // Valor dólar actual
        response.put("converted_amount_clp", result); // Monto convertido a CLP
        response.put("origin_currency", "USD"); // Moneda origen
        response.put("target_currency", "CLP"); // Moneda destino

        return response; // Devuelve JSON con resultado
    }
}
