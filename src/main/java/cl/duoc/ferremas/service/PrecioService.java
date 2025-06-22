package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Precio;
import cl.duoc.ferremas.repository.PrecioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Indica que esta clase es un "Service" de Spring (capa de lógica de negocio)
public class PrecioService {

    // Inyección del repositorio que permite acceder a la base de datos
    private final PrecioRepository precioRepository;

    // Constructor para inyectar el repositorio mediante dependencia
    public PrecioService(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    /**
     * Obtiene el historial de precios de un producto a partir de su código.
     * @param codigoProducto Código del producto (por ejemplo: "FER-12345")
     * @return Lista con los precios históricos asociados a ese producto
     */
    public List<Precio> obtenerPreciosPorCodigoProducto(String codigoProducto) {
        return precioRepository.findByProductoCodigo(codigoProducto); // Usa el método custom del repositorio
    }

    /**
     * Guarda un nuevo precio en la base de datos.
     * @param precio Objeto Precio a guardar
     * @return Precio guardado con ID autogenerado
     */
    public Precio guardarPrecio(Precio precio) {
        return precioRepository.save(precio); // Inserta o actualiza el precio en la base de datos
    }
}
