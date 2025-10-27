import helpers.ConsoleHelper;
import modelo.Cliente;
import modelo.Cuenta;
import servicio.Servicio;
import servicio.ServicioImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class App {

    public static void altaCliente() {
        try {
            Servicio servicio = new ServicioImpl();
            String dni = ConsoleHelper.pedirCadena("DNI del cliente:");
            String nombre = ConsoleHelper.pedirCadena("Nombre del cliente:");
            servicio.altaCliente(new Cliente(dni, nombre));
            System.out.println("Cliente dado de alta");
        } catch (SQLException e) {
            System.out.println("Error creando cliente: " + e.getMessage());
        }
    }

    public static void altaCuenta() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cliente> clientes = servicio.listarClientes();
            Cliente cliente = null;
            do {
                System.out.println("Listado de clientes:");
                clientes.forEach(System.out::println);
                String vDni = ConsoleHelper.pedirCadena("Indica DNI de cliente. ");
                cliente = clientes.stream()
                        .filter(c -> c.getDni().equals(vDni))
                        .findFirst()
                        .orElse(null);
                if (cliente == null) System.out.println("Cliente no existe. Vuelve a intentarlo.");
            } while (cliente == null);
            String iban = ConsoleHelper.pedirCadena("IBAN de nueva cuenta");
            servicio.altaCuenta(new Cuenta(iban, cliente));
            System.out.println("Cuenta registrada.");
        } catch (SQLException e) {
            System.out.println("Error creando cuenta: " + e.getMessage());
        }
    }

    public static void listarClientes() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cliente> clientes = servicio.listarClientes();
            if (!clientes.isEmpty())
                clientes.forEach(System.out::println);
            else
                System.out.println("No hay clientes registrados");
        } catch (SQLException ex) {
            System.out.println("Error obteniendo listado de batallas. " + ex.getMessage());
        }
    }

    public static void listarCuentas() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cuenta> cuentas = servicio.listarCuentas();
            if (!cuentas.isEmpty())
                cuentas.forEach(System.out::println);
            else
                System.out.println("No hay cuentas registradas");
        } catch (SQLException ex) {
            System.out.println("Error obteniendo listado de batallas. " + ex.getMessage());
        }
    }

    public static void operacionCuentas() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cuenta> cuentas = servicio.listarCuentas();
            Cuenta cuenta = null;
            do {
                System.out.println("Listado de cuentas:");
                cuentas.forEach(System.out::println);
                String vIban = ConsoleHelper.pedirCadena("Indica el IBAN: ");
                cuenta = cuentas.stream()
                        .filter(c -> c.getIban().equals(vIban))
                        .findFirst()
                        .orElse(null);
                if (cuenta == null) System.out.println("Cliente no existe. Vuelve a intentarlo.");
            } while (cuenta == null);

            int opt = ConsoleHelper.pedirEntero("1.- INGRESO / 0.- RETIRO: ", 0, 1);
            double saldo = ConsoleHelper.pedirDecimal("SALDO: ");
            if (opt == 1){
                cuenta.setSaldo((float) (cuenta.getSaldo() + saldo));
            } else {
                cuenta.setSaldo((float) (cuenta.getSaldo() - saldo));
            }

            servicio.actualizarCuenta(cuenta);
            System.out.println("Cuenta actualizada: ");
            System.out.println(cuenta.toString());
        } catch (SQLException e) {
            System.out.println("Error creando cuenta: " + e.getMessage());
        }
    }

    public static void transferencias() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cuenta> cuentas = servicio.listarCuentas();
            Cuenta cuentaOrigen = null;
            Cuenta cuentaDestino = null;

            System.out.println("Listado de cuentas:");
            cuentas.forEach(System.out::println);

            do {
                String vIbanOrigen = ConsoleHelper.pedirCadena("IBAN cuenta de origen: ");
                cuentaOrigen = cuentas.stream()
                        .filter(c -> c.getIban().equals(vIbanOrigen))
                        .findFirst()
                        .orElse(null);
                if (cuentaOrigen == null) System.out.println("IBAN no existe. Vuelve a intentarlo.");
            } while (cuentaOrigen == null);

            do {
                String vIbanDestino = ConsoleHelper.pedirCadena("IBAN cuenta de destino: ");
                cuentaDestino = cuentas.stream()
                        .filter(c -> c.getIban().equals(vIbanDestino))
                        .findFirst()
                        .orElse(null);
                if (cuentaDestino == null) System.out.println("IBAN no existe. Vuelve a intentarlo.");
            } while (cuentaDestino == null || cuentaOrigen.getIban() == cuentaDestino.getIban());

            double saldo = ConsoleHelper.pedirDecimal("Indica cantidad transferencia: ");

            servicio.transferencia(cuentaOrigen, cuentaDestino, (float) saldo);
            System.out.println("Transferencia completada: ");
            System.out.println("Cuenta Origen: ");
            System.out.println(cuentaOrigen);
            System.out.println("Cuenta Destino: ");
            System.out.println(cuentaDestino);

        } catch (SQLException e) {
            System.out.println("Error creando cuenta: " + e.getMessage());
        }
    }

    public static void saldosByCliente() {
        try {
            Servicio servicio = new ServicioImpl();
            Map<String, Double> saldoMedio = servicio.saldoMedioCliente();
            System.out.println("SALDO MEDIO POR CLIENTES:");
            saldoMedio.forEach((k, v) -> System.out.printf("    %-20s - %.2f€%n", k, v));
        } catch (SQLException e) {
            System.out.println("Error obteniendo saldos medios por cliente: " + e.getMessage());
        }
    }

    public static void menu() {
        do {
            System.out.println("\n=== GESTIÓN DE CUENTAS ===");
            System.out.println("1. ALTA CLIENTE");
            System.out.println("2. ALTA CUENTA");
            System.out.println("3. LISTAR CUENTAS");
            System.out.println("4. LISTAR CLIENTES");
            System.out.println("5. OPERACIÓN CUENTAS");
            System.out.println("6. TRANSFERENCIA");
            System.out.println("7. MEDIA SALDO POR CLIENTE");
            System.out.println("0. Salir");
            int op = ConsoleHelper.pedirEntero("Selecciona opción: ", 0, 7);
            switch (op) {
                case 1: {
                    altaCliente();
                    menu();
                }
                ;
                break;
                case 2: {
                    altaCuenta();
                    menu();
                }
                ;
                break;
                case 3: {
                    listarCuentas();
                    menu();
                }
                ;
                break;
                case 4: {
                    listarClientes();
                    menu();
                }
                ;
                break;
                case 5: {
                    operacionCuentas();
                    menu();
                }
                ;
                break;
                case 6: {
                    transferencias();
                    menu();
                }
                ;
                break;
                case 7: {
                    saldosByCliente();
                    menu();
                }
                ;
                case 0:
                    System.out.println("FIN GESTIÓN DE CUENTAS");
                    System.exit(0);
            }
        } while (true);
    }

    public static void main(String[] args) {
        menu();
    }
}
