package cl.duoc.ferremas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity  // Marca esta clase como entidad JPA (tabla en base de datos)
public class Producto {

    @Id  // Clave primaria, aquí es un String (ejemplo: "FER-12345")
    private String codigo; // Código único del producto

    private String nombre;  // Nombre del producto
    private String marca;   // Marca del producto
    private String categoria; // Categoría del producto
    private int stock;      // Cantidad disponible en inventario

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)  
    // Relación uno a muchos: un producto puede tener varios precios
    @JsonManagedReference  // Marca el lado "padre" para evitar referencias cíclicas en JSON
    private List<Precio> precios;  // Lista de precios asociados a este producto

    // Getters y setters para acceder y modificar los atributos

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<Precio> getPrecios() {
        return precios;
    }

    public void setPrecios(List<Precio> precios) {
        this.precios = precios;
    }
}
