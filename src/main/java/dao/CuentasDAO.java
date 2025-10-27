package dao;

import modelo.Cliente;
import modelo.Cuenta;

import java.sql.*;
import java.util.*;

public class CuentasDAO implements CrudDAO<Cuenta> {

    private Connection con;

    public CuentasDAO(Connection connection) {
        con = connection;
    }

    @Override
    public List<Cuenta> listar() throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        CrudDAO<Cliente> clienteDAO = new ClienteDAO(con);

        String sql = "SELECT c.id, " +
                "c.iban, " +
                "c.saldo, " +
                "c.interes, " +
                "c.id_cliente, " +
                "c.fecha " +
                "FROM cuentas c ";
        try (Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setId(rs.getLong("id"));
                cuenta.setIban(rs.getString("iban"));
                cuenta.setSaldo(rs.getFloat("saldo"));
                cuenta.setInteres(rs.getFloat("interes"));
                cuenta.setFecha(rs.getDate("fecha").toLocalDate());

                Cliente cliente = clienteDAO.obtener(rs.getLong("id_cliente"));
                cuenta.setCliente(cliente);

                cuentas.add(cuenta);
            }
        }
        return cuentas;
    }

    @Override
    public Cuenta obtener(long id) throws SQLException {
        Cuenta cuenta = null;
        CrudDAO<Cliente> clienteDAO = new ClienteDAO(con);

        String sql = "SELECT c.id, " +
                "c.iban, " +
                "c.saldo, " +
                "c.interes, " +
                "c.id_cliente, " +
                "c.fecha " +
                "FROM cuentas c " +
                "WHERE c.id = ?";
        
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                cuenta = new Cuenta();
                cuenta.setId(rs.getLong("id"));
                cuenta.setIban(rs.getString("iban"));
                cuenta.setSaldo(rs.getFloat("saldo"));
                cuenta.setInteres(rs.getFloat("interes"));
                cuenta.setFecha(rs.getDate("fecha").toLocalDate());

                Cliente cliente = clienteDAO.obtener(rs.getLong("id_cliente"));
                cuenta.setCliente(cliente);
            }
        }
        return cuenta;
    }

    @Override
    public void insertar(Cuenta obj) throws SQLException {
        String sql = "INSERT INTO cuentas ( iban, saldo, interes, id_cliente, fecha ) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, obj.getIban());
            pst.setFloat(2, obj.getSaldo());
            pst.setFloat(3, obj.getInteres());
            pst.setLong(4, obj.getCliente().getId());
            pst.setObject(5, obj.getFecha());

            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) obj.setId(rs.getLong(1));
        }
    }

    @Override
    public void actualizar(Cuenta obj) throws SQLException {
        String sql = "UPDATE cuentas SET " +
                "iban = ? " +
                ", saldo = ? " +
                ", interes = ? " +
                ", id_cliente = ?" +
                ", fecha = ? " +
                "WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, obj.getIban());
            pst.setFloat(2, obj.getSaldo());
            pst.setFloat(3, obj.getInteres());
            pst.setLong(4, obj.getCliente().getId());
            pst.setObject(5, obj.getFecha());
            pst.setLong(6, obj.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void eliminar(Cuenta obj) throws SQLException {
        String sql = "DELETE FROM cuentas WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setLong(1, obj.getId());

            pst.executeUpdate();
        }
    }

    public Map<String, Double> saldoMedioPorCliente() {
        Map<String, Double> saldoMedio = null;
        String sql = """
                SELECT
                    cl.nombre AS nombre_cliente,
                    AVG(c.saldo) AS saldo_medio
                FROM clientes cl
                JOIN cuentas c ON cl.id = c.id_cliente
                GROUP BY cl.nombre
                UNION ALL
                SELECT
                    'TOTAL' AS nombre_cliente,
                    SUM(saldo_medio) AS saldo_medio
                FROM (
                    SELECT AVG(c.saldo) AS saldo_medio
                    FROM clientes cl
                    JOIN cuentas c ON cl.id = c.id_cliente
                    GROUP BY cl.nombre
                ) AS sub
                ORDER BY CASE WHEN nombre_cliente = 'TOTAL' THEN 1 ELSE 0 END, nombre_cliente;
                """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            saldoMedio = new LinkedHashMap<>();

            while (rs.next()) {
                String nombreCliente = rs.getString("nombre_cliente");
                Double saldoPromedio = rs.getDouble("saldo_medio");
                saldoMedio.put(nombreCliente, saldoPromedio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saldoMedio;
    }

}
