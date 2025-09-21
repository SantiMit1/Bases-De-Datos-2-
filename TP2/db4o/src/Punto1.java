import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import tablas.Cliente;
import tablas.Factura;

import java.util.List;

public class Punto1 {
    public static void main(String[] args) {
        punto1();
    }

    private static void punto1() {
        ObjectContainer db = Db4oEmbedded.openFile("punto1.db4o");

        //Persistir datos
        Cliente cliente = new Cliente(1, "Juan");
        Factura factura = new Factura(1, 1000);

        Cliente cliente2 = new Cliente(2, "Guillermo");
        Factura factura2 = new Factura(2, 2000);

        cliente.agregarFactura(factura);
        cliente2.agregarFactura(factura2);
        db.store(cliente);
        db.store(cliente2);
        db.commit();

        //Leer datos
        Query query = db.query();
        query.constrain(Cliente.class);
        List<Cliente> clientes = query.execute();
        for (Cliente c : clientes) {
            System.out.println(c.toString());
        }

        db.close();
    }
}
