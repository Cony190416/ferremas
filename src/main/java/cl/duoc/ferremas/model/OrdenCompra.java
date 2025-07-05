package cl.duoc.ferremas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "buy_order", unique = true, nullable = false)
    private String buyOrder;
    
    @Column(name = "session_id", nullable = false)
    private String sessionId;
    
    @Column(name = "token_ws")
    private String tokenWs;
    
    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;
    
    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenItem> items;
    
    @Column(name = "divisa", nullable = false)
    private String divisa;
    
    @Column(name = "tasa_cambio")
    private Double tasaCambio;
    
    // Enums
    public enum EstadoOrden {
        PENDIENTE,
        AUTORIZADO,
        RECHAZADO,
        ANULADO
    }
    
    // Constructores
    public OrdenCompra() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBuyOrder() {
        return buyOrder;
    }
    
    public void setBuyOrder(String buyOrder) {
        this.buyOrder = buyOrder;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getTokenWs() {
        return tokenWs;
    }
    
    public void setTokenWs(String tokenWs) {
        this.tokenWs = tokenWs;
    }
    
    public Double getMontoTotal() {
        return montoTotal;
    }
    
    public void setMontoTotal(Double montoTotal) {
        this.montoTotal = montoTotal;
    }
    
    public EstadoOrden getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<OrdenItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrdenItem> items) {
        this.items = items;
    }
    
    public String getDivisa() {
        return divisa;
    }
    
    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }
    
    public Double getTasaCambio() {
        return tasaCambio;
    }
    
    public void setTasaCambio(Double tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
} 