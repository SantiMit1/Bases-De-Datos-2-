import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import tablas.Cliente;
import tablas.Factura;
import java.util.Scanner;

public class Punto2 {
    private static final Scanner sc = new Scanner(System.in);
    private static ObjectContainer db;

    public static void main(String[] args) {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "punto2.db4o");

        try {
            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = Integer.parseInt(sc.nextLine());
                switch (opcion) {
                    case 1 -> agregarCliente();
                    case 2 -> agregarFactura();
                    case 3 -> listarClientes();
                    case 4 -> buscarClientePorId();
                    case 5 -> salir = true;
                    default -> System.out.println("Opción inválida.");
                }
            }
        } finally {
            db.close();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ ===");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Agregar Factura a Cliente");
        System.out.println("3. Listar todos los Clientes");
        System.out.println("4. Buscar Cliente por ID");
        System.out.println("5. Salir");
        System.out.print("Ingrese opción: ");
    }

    private static void agregarCliente() {
        System.out.print("ID Cliente: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre: ");
        String descr = sc.nextLine();

        Cliente c = new Cliente(id, descr);
        db.store(c);
        System.out.println("Cliente agregado correctamente.");
    }

    private static void agregarFactura() {
        System.out.print("ID Cliente: ");
        int id = Integer.parseInt(sc.nextLine());
        Cliente ejemplo = new Cliente(id, null);
        ObjectSet<Cliente> clientes = db.queryByExample(ejemplo);

        if (clientes.isEmpty()) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        Cliente c = clientes.getFirst();
        System.out.print("Número de Factura: ");
        int nro = Integer.parseInt(sc.nextLine());
        System.out.print("Importe: ");
        int importe = Integer.parseInt(sc.nextLine());

        Factura f = new Factura(nro, importe);
        c.agregarFactura(f);
        db.store(c); // actualizar cliente
        System.out.println("Factura agregada correctamente.");
    }

    private static void listarClientes() {
        System.out.println("\n=== Todos los clientes ===");
        for (Object obj : db.queryByExample(Cliente.class)) {
            System.out.println(obj);
        }
    }

    private static void buscarClientePorId() {
        System.out.print("ID Cliente: ");
        int id = Integer.parseInt(sc.nextLine());
        Cliente example = new Cliente(id, null);
        ObjectSet<Cliente> clientes = db.queryByExample(example);

        if (clientes.isEmpty()) {
            System.out.println("Cliente no encontrado.");
        } else {
            System.out.println(clientes.getFirst());
        }
    }
}