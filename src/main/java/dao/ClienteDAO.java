package dao;

import modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements CrudDAO<Cliente> {

    private Connection con;

    public ClienteDAO(Connection connection) {
        con = connection;
    }

    @Override
    public void insertar(Cliente obj) throws SQLException {
        String sql = "INSERT INTO clientes( dni, nombre ) VALUES (?, ?)";
        try (PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, obj.getDni());
            pst.setString(2, obj.getNombre());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) obj.setId(rs.getLong(1));
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.id, " +
                "c.dni, " +
                "c.nombre " +
                "FROM clientes c " +
                "ORDER BY c.id";
        try (var st = con.createStatement()) {
            var rs = st.executeQuery(sql);

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombre(rs.getString("nombre"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public Cliente obtener(long id) throws SQLException {
        Cliente cliente = null;

        String sql = "SELECT c.id, " +
                "c.dni, " +
                "c.nombre " +
                "FROM clientes c " +
                "WHERE c.id = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String dni = rs.getString("dni");
                String nombre = rs.getString("nombre");
                cliente = new Cliente(id, dni, nombre);
            }
        }
        return cliente;
    }

    @Override
    public void eliminar(Cliente obj) throws SQLException {
        throw new SQLException("No implementado");
    }

    @Override
    public void actualizar(Cliente obj) throws SQLException {
        throw new SQLException("No implementado");
    }

}
