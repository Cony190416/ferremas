package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.MensajeCliente;
import cl.duoc.ferremas.repository.MensajeClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajeClienteService {

    private final MensajeClienteRepository mensajeClienteRepository;

    public MensajeClienteService(MensajeClienteRepository mensajeClienteRepository) {
        this.mensajeClienteRepository = mensajeClienteRepository;
    }

    public MensajeCliente guardarMensaje(MensajeCliente mensaje) {
        return mensajeClienteRepository.save(mensaje);
    }

    public List<MensajeCliente> obtenerMensajes() {
        return mensajeClienteRepository.findAll();
    }
}
