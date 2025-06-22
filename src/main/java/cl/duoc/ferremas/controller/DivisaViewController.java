package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.external.TipoCambioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Indica que esta clase es un controlador de vistas (no REST)
public class DivisaViewController {

    // Inyección del servicio que obtiene y convierte el tipo de cambio
    private final TipoCambioService tipoCambioService;

    // Constructor para inyectar el servicio
    public DivisaViewController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    // Nombre de la tienda que se mostrará en las vistas
    private final String nombreTienda = "FERREMAS";

    // GET /divisa/dolar - Muestra el valor actual del dólar en la vista
    @GetMapping("/divisa/dolar")
    public String mostrarValorDolar(Model model) {
        Double valorDolar = tipoCambioService.obtenerValorDolar(); // Llama al servicio para obtener el valor actual
        model.addAttribute("valorDolar", valorDolar); // Lo pasa a la vista como atributo
        model.addAttribute("nombreTienda", nombreTienda); // Agrega nombre de la tienda para mostrar en la vista
        return "divisa/dolar"; // Renderiza la vista ubicada en templates/divisa/dolar.html
    }

    // GET /divisa/convertir?amount=X - Convierte de CLP a USD
    @GetMapping("/divisa/convertir")
    public String mostrarConvertirClpToUsd(@RequestParam(required = false) Double amount, Model model) {
        if (amount != null) { // Si se ingresó un monto, realiza la conversión
            Double resultado = tipoCambioService.convertirADolares(amount);
            model.addAttribute("montoOriginal", amount); // Monto original ingresado
            model.addAttribute("resultadoConversion", String.format("%.2f", resultado)); // Resultado formateado a 2 decimales
        }
        model.addAttribute("nombreTienda", nombreTienda);
        return "divisa/convertir"; // Renderiza la vista templates/divisa/convertir.html
    }

    // GET /divisa/convertir-inverso?amount=X - Convierte de USD a CLP
    @GetMapping("/divisa/convertir-inverso")
    public String mostrarConvertirUsdToClp(@RequestParam(required = false) Double amount, Model model) {
        if (amount != null) { // Si se ingresó un monto, realiza la conversión
            Double resultado = tipoCambioService.convertirAPesos(amount);
            model.addAttribute("montoOriginal", amount); // Monto original ingresado
            model.addAttribute("resultadoConversion", String.format("%.0f", resultado)); // Resultado sin decimales
        }
        model.addAttribute("nombreTienda", nombreTienda);
        return "divisa/convertir-inverso"; // Renderiza la vista templates/divisa/convertir-inverso.html
    }
}
