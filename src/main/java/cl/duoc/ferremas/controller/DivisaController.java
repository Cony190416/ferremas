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

    // GET /api/divisa/convertir-generico?amount=X&desde=CLP&hacia=USD - Convierte entre divisas
    @GetMapping("/convertir-generico")
    public Map<String, Object> convertirGenerico(
            @RequestParam Double amount,
            @RequestParam String desde,
            @RequestParam String hacia) {
        
        Map<String, Object> response = new HashMap<>();
        Double valorDolar = tipoCambioService.obtenerValorDolar();
        
        if (valorDolar == null) {
            response.put("error", "No se pudo obtener el valor del dólar");
            return response;
        }
        
        Double resultado = 0.0;
        
        if (desde.equals("CLP") && hacia.equals("USD")) {
            resultado = tipoCambioService.convertirADolares(amount);
        } else if (desde.equals("USD") && hacia.equals("CLP")) {
            resultado = tipoCambioService.convertirAPesos(amount);
        } else if (desde.equals("CLP") && hacia.equals("EUR")) {
            // Conversión CLP -> USD -> EUR (aproximada)
            Double enUsd = tipoCambioService.convertirADolares(amount);
            resultado = enUsd * 0.85; // Tasa aproximada USD a EUR
        } else if (desde.equals("EUR") && hacia.equals("CLP")) {
            // Conversión EUR -> USD -> CLP (aproximada)
            Double enUsd = amount / 0.85; // Tasa aproximada EUR a USD
            resultado = tipoCambioService.convertirAPesos(enUsd);
        } else {
            response.put("error", "Conversión no soportada");
            return response;
        }
        
        response.put("amount_original", amount);
        response.put("valor_convertido", resultado);
        response.put("origin_currency", desde);
        response.put("target_currency", hacia);
        response.put("current_dollar_value", valorDolar);
        
        return response;
    }
}
