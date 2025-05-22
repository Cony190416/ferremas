package cl.duoc.ferremas.controller;

import cl.duoc.ferremas.model.MensajeCliente;
import cl.duoc.ferremas.service.MensajeClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacto")
public class MensajeClienteController {

    private final MensajeClienteService mensajeClienteService;

    public MensajeClienteController(MensajeClienteService mensajeClienteService) {
        this.mensajeClienteService = mensajeClienteService;
    }

    @PostMapping
    public MensajeCliente guardarMensaje(@RequestBody MensajeCliente mensaje) {
        return mensajeClienteService.guardarMensaje(mensaje);
    }

    @GetMapping
    public List<MensajeCliente> obtenerMensajes() {
        return mensajeClienteService.obtenerMensajes();
    }
}
