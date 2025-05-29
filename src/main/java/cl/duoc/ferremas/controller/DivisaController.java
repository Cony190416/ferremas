package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.external.TipoCambioService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/divisa")
public class DivisaController {

    private final TipoCambioService tipoCambioService;

    public DivisaController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    @GetMapping("/dolar")
    public Map<String, Object> getDollarValue() {
        Double dollarValue = tipoCambioService.obtenerValorDolar();

        Map<String, Object> response = new HashMap<>();
        response.put("currency", "USD");
        response.put("value_in_clp", dollarValue);
        response.put("source", "https://mindicador.cl");

        return response;
    }

    @GetMapping("/convertir")
    public Map<String, Object> convertClpToUsd(@RequestParam Double amount) {
        Double result = tipoCambioService.convertirADolares(amount);
        Double dollarValue = tipoCambioService.obtenerValorDolar();

        Map<String, Object> response = new HashMap<>();
        response.put("amount_in_clp", amount);
        response.put("current_dollar_value", dollarValue);
        response.put("converted_amount_usd", result);
        response.put("origin_currency", "CLP");
        response.put("target_currency", "USD");

        return response;
    }

    @GetMapping("/convertir-inverso")
    public Map<String, Object> convertUsdToClp(@RequestParam Double amount) {
        Double result = tipoCambioService.convertirAPesos(amount);
        Double dollarValue = tipoCambioService.obtenerValorDolar();

        Map<String, Object> response = new HashMap<>();
        response.put("amount_in_usd", amount);
        response.put("current_dollar_value", dollarValue);
        response.put("converted_amount_clp", result);
        response.put("origin_currency", "USD");
        response.put("target_currency", "CLP");

        return response;
    }
}
