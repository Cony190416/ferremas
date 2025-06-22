package cl.duoc.ferremas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity  // Indica que esta clase es una entidad de JPA (tabla en la base de datos)
public class MensajeCliente {

    @Id  // Clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Valor autogenerado por la base de datos
    private Long id;

    private String nombre;  // Nombre del cliente que envía el mensaje

    private String correo;  // Correo electrónico del cliente

    @Column(length = 1000)  // Columna para el mensaje con un largo máximo de 1000 caracteres
    private String mensaje;

    private LocalDateTime fecha = LocalDateTime.now();  // Fecha y hora en que se creó el mensaje, inicializada al momento actual

    // Getters y setters para acceso y modificación de los atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
