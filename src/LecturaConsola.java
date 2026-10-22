import java.util.Scanner;


public final class LecturaConsola {

    private static final Scanner SCANNER = new Scanner(System.in);

   
    private LecturaConsola() {
    }

    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!SCANNER.hasNextLine()) {
            
            System.out.println("\nEntrada finalizada. Hasta pronto.");
            System.exit(0);
        }
        return SCANNER.nextLine().trim();
    }

    public static String leerTexto(String mensaje) {
        while (true) {
            String valor = leerLinea(mensaje);
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("  Error: el dato no puede estar vacio.");
        }
    }


    public static int leerEnteroPositivo(String mensaje) {
        while (true) {
            try {
                int numero = Integer.parseInt(leerLinea(mensaje));
                if (numero > 0) {
                    return numero;
                }
                System.out.println("  Error: el numero debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Error: ingrese un numero entero valido (solo digitos, sin letras).");
            }
        }
    }


    public static int leerOpcion(String mensaje, int minimo, int maximo) {
        while (true) {
            try {
                int opcion = Integer.parseInt(leerLinea(mensaje));
                if (opcion >= minimo && opcion <= maximo) {
                    return opcion;
                }
            } catch (NumberFormatException e) {
             
            }
            System.out.println("  Error: elija una opcion entre " + minimo + " y " + maximo + ".");
        }
    }

   
    public static int leerHora(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje);
            if (ConversorTiempo.esHoraValida(texto)) {
                return ConversorTiempo.horaAMinutos(texto);
            }
            System.out.println("  Error: formato de hora invalido. Use HH:mm de 24 horas (ej. 13:30).");
        }
    }
}