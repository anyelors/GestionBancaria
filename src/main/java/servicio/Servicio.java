package servicio;

import modelo.Cliente;
import modelo.Cuenta;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Servicio {

    List<Cliente> listarClientes() throws SQLException;
    void altaCliente( Cliente objCliente) throws SQLException;
    void altaCuenta(Cuenta objCuenta) throws SQLException;
    void actualizarCuenta( Cuenta objCuenta ) throws SQLException;
    List<Cuenta> listarCuentas() throws SQLException;
    void transferencia( Cuenta origen, Cuenta destino, float saldo) throws SQLException;
    void bajaCuenta( Cuenta cuenta ) throws SQLException;
    Map<String, Double> saldoMedioCliente() throws SQLException;

}
