package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.external.TipoCambioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DivisaViewController {

    private final TipoCambioService tipoCambioService;

    public DivisaViewController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    private final String nombreTienda = "FERREMAS";

    @GetMapping("/divisa/dolar")
    public String mostrarValorDolar(Model model) {
        Double valorDolar = tipoCambioService.obtenerValorDolar();
        model.addAttribute("valorDolar", valorDolar);
        model.addAttribute("nombreTienda", nombreTienda);
        return "divisa/dolar";
    }

    @GetMapping("/divisa/convertir")
    public String mostrarConvertirClpToUsd(@RequestParam(required = false) Double amount, Model model) {
        if (amount != null) {
            Double resultado = tipoCambioService.convertirADolares(amount);
            model.addAttribute("montoOriginal", amount);
            model.addAttribute("resultadoConversion", String.format("%.2f", resultado));
        }
        model.addAttribute("nombreTienda", nombreTienda);
        return "divisa/convertir";
    }

    @GetMapping("/divisa/convertir-inverso")
    public String mostrarConvertirUsdToClp(@RequestParam(required = false) Double amount, Model model) {
        if (amount != null) {
            Double resultado = tipoCambioService.convertirAPesos(amount);
            model.addAttribute("montoOriginal", amount);
            model.addAttribute("resultadoConversion", String.format("%.0f", resultado));
        }
        model.addAttribute("nombreTienda", nombreTienda);
        return "divisa/convertir-inverso";
    }
}
