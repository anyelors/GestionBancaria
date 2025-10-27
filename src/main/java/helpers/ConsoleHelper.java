package helpers;

import java.util.Scanner;

public class ConsoleHelper {

    /**
     * Solicita al usuario un valor entero por consola
     * @param mensaje
     * @return valor entero
     */
    public static int pedirEntero( String mensaje ) {
        int valor = 0;
        boolean ok = false;
        do {
            try {
                Scanner scr = new Scanner(System.in);
                System.out.println(mensaje);
                valor = Integer.parseInt(scr.nextLine());
                ok = true;
            } catch (NumberFormatException ex) {
                System.out.println("Error. Valor no valido");
            }
        } while(!ok);
        return valor;
    }

    /**
     * Solicita al usuario un valor decimal por consola
     * @param mensaje
     * @return valor decimal
     */
    public static double pedirDecimal( String mensaje ) {
        double valor = 0.0;
        boolean ok = false;
        do {
            try {
                Scanner scr = new Scanner(System.in);
                System.out.println(mensaje);
                valor = Double.parseDouble(scr.nextLine());
                ok = true;
            } catch (NumberFormatException ex) {
                System.out.println("Error. Valor no valido");
            }
        } while(!ok);
        return valor;
    }

    /**
     * Solicita al usuario un valor decimal en un determinado rango
     * @param mensaje
     * @param valorMinimo
     * @param valorMaximo
     * @return valor decimal
     */
    public static double pedirDecimal( String mensaje, double valorMinimo, double valorMaximo) {
        double valor;
        boolean ok;
        do {
            valor = pedirDecimal(mensaje);
            ok = ( valor >= valorMinimo && valor <= valorMaximo );
            if ( !ok ) {
                System.out.println("Error. El valor no está en el rango requerido");
            }
        } while ( !ok);
        return valor;
    }

    /**
     * Solicita al usuario un valor entero en un determinado rango
     * @param mensaje
     * @param valorMinimo
     * @param valorMaximo
     * @return
     */
    public static int pedirEntero( String mensaje, int valorMinimo, int valorMaximo ) {
        int valor;
        boolean ok;
        do {
            valor = pedirEntero(mensaje);
            ok = ( valor >= valorMinimo && valor <= valorMaximo );
            if ( !ok ) {
                System.out.println("Error. El valor no está en el rango requerido");
            }
        } while ( !ok);
        return valor;
    }

    /**
     * Solicita al usuario una cadena validando que no sea vacia o con espacios en blanco
     * @param mensaje
     * @return
     */
    public static String pedirCadena( String mensaje ) {
        Scanner scr = new Scanner(System.in);
        String cadena;
        do {
            System.out.println(mensaje);
            cadena = scr.nextLine();
            if ( !cadena.isBlank() ) {
                return cadena;
            } else {
                System.out.println("La cadena no puede ser vacía");
            }
        } while( true);
    }

    /**
     * Solicita al usuario una conformacion indicando "S" o "N"
     * @param mensaje
     * @return TRUE si indico "S" FALSE si indico NO.
     */
    public static boolean pedirConfirmacion( String mensaje ) {
        String respuesta;
        do {
           respuesta = pedirCadena(mensaje + " (s/n)?");
           if ( respuesta.trim().equalsIgnoreCase("S")) return true;
           if ( respuesta.trim().equalsIgnoreCase("N")) return false;
        } while ( true );
    }
}
