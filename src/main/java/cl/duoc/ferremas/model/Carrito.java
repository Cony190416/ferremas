package cl.duoc.ferremas.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Carrito {
    private List<CarritoItem> items = new ArrayList<>();

    public List<CarritoItem> getItems() {
        return items;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        for (CarritoItem item : items) {
            if (item.getProducto().getCodigo().equals(producto.getCodigo())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new CarritoItem(producto, cantidad));
    }

    public void quitarProducto(String productoCodigo) {
        Iterator<CarritoItem> it = items.iterator();
        while (it.hasNext()) {
            CarritoItem item = it.next();
            if (item.getProducto().getCodigo().equals(productoCodigo)) {
                it.remove();
                return;
            }
        }
    }

    public void modificarCantidad(String productoCodigo, int cantidad) {
        for (CarritoItem item : items) {
            if (item.getProducto().getCodigo().equals(productoCodigo)) {
                item.setCantidad(cantidad);
                return;
            }
        }
    }

    public void vaciar() {
        items.clear();
    }

    public double getTotal() {
        double total = 0;
        for (CarritoItem item : items) {
            total += obtenerPrecioActual(item.getProducto()) * item.getCantidad();
        }
        return total;
    }

    private double obtenerPrecioActual(Producto producto) {
        if (producto.getPrecios() == null || producto.getPrecios().isEmpty()) {
            return 0;
        }
        // Suponiendo que el último precio es el más actual
        return producto.getPrecios().get(producto.getPrecios().size() - 1).getValor();
    }
} 