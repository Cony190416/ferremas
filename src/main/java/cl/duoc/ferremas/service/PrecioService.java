package cl.duoc.ferremas.service;

import cl.duoc.ferremas.model.Precio;
import cl.duoc.ferremas.repository.PrecioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioService {

    private final PrecioRepository precioRepository;

    public PrecioService(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    public List<Precio> obtenerPreciosPorCodigoProducto(String codigoProducto) {
        return precioRepository.findByProductoCodigo(codigoProducto);
    }

    public Precio guardarPrecio(Precio precio) {
        return precioRepository.save(precio);
    }
}
