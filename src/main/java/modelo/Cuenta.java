package modelo;

import java.time.LocalDate;

public class Cuenta {

    private Long id;
    private String iban;
    private Float saldo;
    private Float interes;
    private Cliente cliente;
    private LocalDate fecha;

    public Cuenta() {
    }

    public Cuenta(String iban, Cliente cliente) {
        this.iban = iban;
        this.saldo = 0.0f;
        this.interes = 0.5f;
        this.cliente = cliente;
        this.fecha = LocalDate.now();
    }

    public Cuenta(Long id, String iban, Float saldo, Float interes, Cliente cliente, LocalDate fecha) {
        this.id = id;
        this.iban = iban;
        this.saldo = saldo;
        this.interes = interes;
        this.cliente = cliente;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }

    public Float getInteres() {
        return interes;
    }

    public void setInteres(Float interes) {
        this.interes = interes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Iban = " + iban + ", Saldo = " + saldo + ", Interes = " + interes +
                ", Cliente = " + cliente.getDni() + " - " + cliente.getNombre() +
                ", Fecha = " + fecha;
    }
}
