/**
 * Pruebas de ConversorTiempo (T-04 / Issue #4), sin librerias externas.
 *
 * Compilar: javac -d out src/*.java tests/PruebasConversorTiempo.java
 * Ejecutar: java -cp out PruebasConversorTiempo
 */
public class PruebasConversorTiempo {

    private static int total = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        // CT-C01: conversion correcta de horas tipicas
        verificar("CT-C01a 13:00 -> 780", ConversorTiempo.horaAMinutos("13:00") == 780);
        verificar("CT-C01b 00:00 -> 0", ConversorTiempo.horaAMinutos("00:00") == 0);
        verificar("CT-C01c 23:59 -> 1439", ConversorTiempo.horaAMinutos("23:59") == 1439);
        verificar("CT-C01d 9:05 (una cifra) -> 545", ConversorTiempo.horaAMinutos("9:05") == 545);

        // CT-C02: conversion inversa
        verificar("CT-C02a 780 -> 13:00", ConversorTiempo.minutosAHora(780).equals("13:00"));
        verificar("CT-C02b 0 -> 00:00", ConversorTiempo.minutosAHora(0).equals("00:00"));
        verificar("CT-C02c 545 -> 09:05", ConversorTiempo.minutosAHora(545).equals("09:05"));

        // CT-C03: ida y vuelta conserva el valor en los 1440 minutos del dia
        boolean idaVuelta = true;
        for (int m = 0; m < ConversorTiempo.MINUTOS_POR_DIA; m++) {
            if (ConversorTiempo.horaAMinutos(ConversorTiempo.minutosAHora(m)) != m) {
                idaVuelta = false;
            }
        }
        verificar("CT-C03 ida y vuelta de 0 a 1439", idaVuelta);

        // CT-C04: formatos invalidos rechazados
        String[] invalidas = {"25:70", "24:00", "12:60", "abc", "", "  ", "12", "12:5", "1:2:3", "-1:30", "12.30"};
        for (String h : invalidas) {
            verificar("CT-C04 esHoraValida(\"" + h + "\") = false", !ConversorTiempo.esHoraValida(h));
        }
        verificar("CT-C04 esHoraValida(null) = false", !ConversorTiempo.esHoraValida(null));

        // CT-C05: horaAMinutos lanza excepcion con entrada invalida
        verificarExcepcion("CT-C05a horaAMinutos(\"25:70\") lanza excepcion", () -> ConversorTiempo.horaAMinutos("25:70"));
        verificarExcepcion("CT-C05b horaAMinutos(null) lanza excepcion", () -> ConversorTiempo.horaAMinutos(null));

        // CT-C06: minutosAHora lanza excepcion fuera del dia
        verificarExcepcion("CT-C06a minutosAHora(-1) lanza excepcion", () -> ConversorTiempo.minutosAHora(-1));
        verificarExcepcion("CT-C06b minutosAHora(1440) lanza excepcion", () -> ConversorTiempo.minutosAHora(1440));

        System.out.println();
        System.out.println("Total: " + total + " | Satisfactorias: " + (total - fallidas) + " | Fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) {
            fallidas++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLO] ") + descripcion);
    }

    private static void verificarExcepcion(String descripcion, Runnable accion) {
        boolean lanzo = false;
        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar(descripcion, lanzo);
    }
}
