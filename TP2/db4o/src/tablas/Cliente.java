package tablas;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nombre;
    private List<Factura> facturas = new ArrayList<>();

    public Cliente(){}

    public Cliente(int id, String nombre) {
        this.nombre = nombre;
        this. id = id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void agregarFactura(Factura factura) {
        this.facturas.add(factura);
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nombre='" + nombre + '\'' + ", facturas=" + facturas + '}';
    }
}
