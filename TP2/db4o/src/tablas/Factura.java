package tablas;

public class Factura {
    private int nro;
    private int importe;

    public Factura(){}

    public Factura(int nro, int importe) {
        this.nro = nro;
        this.importe = importe;
    }

    public int getNro() {
        return nro;
    }

    public int getImporte() {
        return importe;
    }

    public void setImporte(int importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "Factura{" + "nro=" + nro + ", importe=" + importe + '}';
    }
}
