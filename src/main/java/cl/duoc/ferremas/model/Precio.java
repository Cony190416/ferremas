package cl.duoc.ferremas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;

@Entity  // Marca esta clase como entidad JPA (tabla en base de datos)
public class Precio {

    @Id  // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID
    private Long id;

    private LocalDateTime fecha;  // Fecha en la que se registró el precio

    private int valor;  // Valor del precio en ese momento

    @ManyToOne  // Relación muchos a uno: muchos precios pueden pertenecer a un producto
    @JoinColumn(name = "producto_codigo")  // Nombre de la columna en la tabla para la clave foránea
    @JsonBackReference  // Evita referencia cíclica al serializar JSON (hace que este lado no se serialice)
    private Producto producto;

    // Constructores
    public Precio() {
        // Constructor por defecto requerido por JPA
    }

    // Getters y setters para acceder y modificar los atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
