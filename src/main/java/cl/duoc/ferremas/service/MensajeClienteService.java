package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.MensajeCliente;
import cl.duoc.ferremas.repository.MensajeClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Marca esta clase como un componente de servicio en Spring (para lógica de negocio)
public class MensajeClienteService {

    // Inyección del repositorio que gestiona la entidad MensajeCliente
    private final MensajeClienteRepository mensajeClienteRepository;

    // Constructor para inyectar el repositorio vía dependencia
    public MensajeClienteService(MensajeClienteRepository mensajeClienteRepository) {
        this.mensajeClienteRepository = mensajeClienteRepository;
    }

    /**
     * Guarda un mensaje de cliente en la base de datos.
     * @param mensaje Objeto MensajeCliente a guardar
     * @return El mensaje guardado, con ID generado si aplica
     */
    public MensajeCliente guardarMensaje(MensajeCliente mensaje) {
        return mensajeClienteRepository.save(mensaje);
    }

    /**
     * Obtiene todos los mensajes de clientes almacenados en la base de datos.
     * @return Lista de objetos MensajeCliente
     */
    public List<MensajeCliente> obtenerMensajes() {
        return mensajeClienteRepository.findAll();
    }
}
