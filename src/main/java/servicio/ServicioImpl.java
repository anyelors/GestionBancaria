package servicio;

import dao.ClienteDAO;
import dao.CrudDAO;
import dao.CuentasDAO;
import datasourse.Database;
import modelo.Cliente;
import modelo.Cuenta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServicioImpl implements Servicio {
    @Override
    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cliente> dao = new ClienteDAO(conn);
            clientes = dao.listar();
        }
        return clientes;
    }

    @Override
    public void altaCliente(Cliente objCliente) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cliente> dao = new ClienteDAO(conn);
            dao.insertar(objCliente);
        }
    }

    @Override
    public void altaCuenta(Cuenta objCuenta) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cuenta> dao = new CuentasDAO(conn);
            dao.insertar(objCuenta);
        }
    }

    @Override
    public void actualizarCuenta(Cuenta objCuenta) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cuenta> dao = new CuentasDAO(conn);
            dao.actualizar(objCuenta);
        }
    }

    @Override
    public List<Cuenta> listarCuentas() throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cuenta> dao = new CuentasDAO(conn);
            cuentas = dao.listar();
        }
        return cuentas;
    }

    @Override
    public void transferencia(Cuenta origen, Cuenta destino, float saldo) throws SQLException {

        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cuenta> dao = new CuentasDAO(conn);
            try {
                conn.setAutoCommit(false);
                origen.retirar(saldo);
                dao.actualizar(origen);
                destino.ingresar(saldo);
                dao.actualizar(destino);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    @Override
    public void bajaCuenta(Cuenta cuenta) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            CrudDAO<Cuenta> dao = new CuentasDAO(conn);
            dao.eliminar(cuenta);
        }
    }

    @Override
    public Map<String, Double> saldoMedioCliente() throws SQLException {
        Map<String, Double> saldoMedio = null;
        try (Connection conn = Database.getConnection()) {
            CuentasDAO dao = new CuentasDAO(conn);
            //saldoMedio = dao.saldoMedioPorCliente();
            List<Cuenta> cuentas = dao.listar();
            saldoMedio = cuentas.stream()
                    .collect(Collectors.groupingBy(c -> c.getCliente().getNombre(),
                            Collectors.averagingDouble(c -> c.getSaldo())));
        }
        return saldoMedio;
    }
}
