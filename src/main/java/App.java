import helpers.ConsoleHelper;
import modelo.Cliente;
import modelo.Cuenta;
import servicio.Servicio;
import servicio.ServicioImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class App {

    public static Cliente validClienteExistente (String dni) throws SQLException {
        Servicio servicio = new ServicioImpl();
        List<Cliente> clientes = servicio.listarClientes();
        Cliente cliente;
        cliente = clientes.stream()
                .filter(c -> c.getDni().equals(dni))
                .findFirst()
                .orElse(null);

        return cliente;
    }

    public static Cuenta validCuentaExistente (String iban) throws SQLException {
        Servicio servicio = new ServicioImpl();
        List<Cuenta> cuentas = servicio.listarCuentas();
        Cuenta cuenta;
        cuenta = cuentas.stream()
                .filter(c -> c.getIban().equals(iban))
                .findFirst()
                .orElse(null);

        return cuenta;
    }

    public static void altaCliente() {
        try {
            Servicio servicio = new ServicioImpl();
            String vDni = ConsoleHelper.pedirCadena("DNI del cliente:");
            Cliente cliente = validClienteExistente(vDni);
            if (cliente != null) {
                System.out.println("Cliente Ya Existe.");
                return;
            }
            String vNombre = ConsoleHelper.pedirCadena("Nombre del cliente:");
            servicio.altaCliente(new Cliente(vDni, vNombre));
            System.out.println("Cliente dado de alta");
        } catch (SQLException e) {
            System.out.println("Error creando cliente: " + e.getMessage());
        }
    }

    public static void altaCuenta() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cliente> clientes = servicio.listarClientes();
            Cliente cliente;
            Cuenta cuenta;
            do {
                System.out.println("Listado de clientes:");
                clientes.forEach(System.out::println);
                String vDni = ConsoleHelper.pedirCadena("Indica DNI de cliente. ");
                cliente = validClienteExistente(vDni);
                if (cliente == null) System.out.println("Cliente no existe. Vuelve a intentarlo.");
            } while (cliente == null);
            String vIban = ConsoleHelper.pedirCadena("IBAN de nueva cuenta");
            cuenta = validCuentaExistente(vIban);
            if (cuenta != null) {
                System.out.println("IBAN ya existe.");
                return;
            }

            servicio.altaCuenta(new Cuenta(vIban, cliente));
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
            Cuenta cuenta;
            do {
                System.out.println("Listado de cuentas:");
                cuentas.forEach(System.out::println);
                String vIban = ConsoleHelper.pedirCadena("Indica el IBAN: ");
                cuenta = validCuentaExistente(vIban);
                if (cuenta == null) System.out.println("Cuenta no existe. Vuelve a intentarlo.");
            } while (cuenta == null);

            int opt = ConsoleHelper.pedirEntero("1.- INGRESO / 0.- RETIRO: ", 0, 1);
            double saldo = ConsoleHelper.pedirDecimal("SALDO: ");

            if (opt == 0 && cuenta.getSaldo() < saldo) {
                System.out.println("No se puede realizar retiro. Saldo insuficiente en la Cuenta");
                return;
            }

            if (opt == 1) {
                cuenta.ingresar((float) saldo);
            } else {
                cuenta.retirar((float) saldo);
            }

            servicio.actualizarCuenta(cuenta);
            System.out.println("Cuenta actualizada: ");
            System.out.println(cuenta);
        } catch (SQLException e) {
            System.out.println("Error creando cuenta: " + e.getMessage());
        }
    }

    public static void transferencias() {
        try {
            Servicio servicio = new ServicioImpl();
            List<Cuenta> cuentas = servicio.listarCuentas();
            Cuenta cuentaOrigen;
            Cuenta cuentaDestino;

            System.out.println("Listado de cuentas:");
            cuentas.forEach(System.out::println);

            do {
                String vIbanOrigen = ConsoleHelper.pedirCadena("IBAN cuenta de origen: ");
                cuentaOrigen = validCuentaExistente(vIbanOrigen);
                if (cuentaOrigen == null) System.out.println("IBAN no existe. Vuelve a intentarlo.");
            } while (cuentaOrigen == null);

            do {
                String vIbanDestino = ConsoleHelper.pedirCadena("IBAN cuenta de destino: ");
                cuentaDestino = validCuentaExistente(vIbanDestino);
                if (cuentaDestino == null || cuentaOrigen.getIban().equals(cuentaDestino.getIban()))
                    System.out.println("IBAN no existe o igual al IBAN Origen. Vuelve a intentarlo.");
            } while (cuentaDestino == null || cuentaOrigen.getIban().equals(cuentaDestino.getIban()));

            double saldo = ConsoleHelper.pedirDecimal("Indica cantidad transferencia: ");

            if (cuentaOrigen.getSaldo() < saldo) {
                System.out.println("No se puede realizar Transferencia. Saldo insuficiente en la Cuenta Origen");
                return;
            }

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
                break;
                case 2: {
                    altaCuenta();
                    menu();
                }
                break;
                case 3: {
                    listarCuentas();
                    menu();
                }
                break;
                case 4: {
                    listarClientes();
                    menu();
                }
                break;
                case 5: {
                    operacionCuentas();
                    menu();
                }
                break;
                case 6: {
                    transferencias();
                    menu();
                }
                break;
                case 7: {
                    saldosByCliente();
                    menu();
                }
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
