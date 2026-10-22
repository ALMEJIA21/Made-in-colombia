import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Main {

    private static final int MINIMO_PLATOS = 2;

    private static int horaServicioMin = -1;  
    private static final List<Plato> platos = new ArrayList<>();
   
    private static final Set<String> nombresPlatos = new HashSet<>();

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== MADE IN COLOMBIA ===");
        System.out.println("Orquestador de tiempos culinarios\n");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1) Registrar menu (hora de servicio, platos y tareas)");
            System.out.println("2) Calcular y mostrar cronograma");
            System.out.println("3) Salir");
            int opcion = leerOpcion("Elija una opcion: ", 1, 3);

            switch (opcion) {
                case 1 -> registrarMenu();
                case 2 -> calcularCronograma();
                case 3 -> {
                    System.out.println("Hasta pronto.");
                    salir = true;
                }
            }
        }
    }


    private static void registrarMenu() {
        platos.clear();
        nombresPlatos.clear();

        System.out.println("\n--- REGISTRO DE MENU ---");
        horaServicioMin = leerHora("Hora de servicio para todo el menu (HH:mm): ");

        boolean otroPlato = true;
        while (otroPlato) {
            String nombrePlato = leerNombrePlatoNuevo();
            Plato plato = new Plato(nombrePlato);
            nombresPlatos.add(nombrePlato.toLowerCase());

            registrarTareas(plato);
            platos.add(plato);
            System.out.println("Plato registrado: " + plato);

            if (platos.size() < MINIMO_PLATOS) {
                System.out.println("Faltan platos: el menu necesita al menos " + MINIMO_PLATOS + ".");
            } else {
                otroPlato = leerOpcion("Agregar otro plato? (1 = Si, 2 = No): ", 1, 2) == 1;
            }
        }

        mostrarResumen();
    }


    private static void registrarTareas(Plato plato) {
        System.out.println("Registre las tareas de \"" + plato.getNombre() + "\" en el orden en que se ejecutan.");

        int opcionContinuar;
        do {
            String nombreTarea = leerTexto("  Tarea #" + (plato.getTareas().size() + 1) + " - nombre: ");
            int duracion = leerEnteroPositivo("  Duracion en minutos: ");
            int tipoOpcion = leerOpcion("  Tipo (1 = Activa, 2 = Pasiva): ", 1, 2);
            Tarea.Tipo tipo = (tipoOpcion == 1) ? Tarea.Tipo.ACTIVA : Tarea.Tipo.PASIVA;

            plato.agregarTarea(new Tarea(nombreTarea, duracion, tipo));
            System.out.println("  Tarea agregada. Total del plato: " + plato.getDuracionTotal() + " min.");

          
            opcionContinuar = leerOpcion("  Agregar otra tarea? (1 = Si, 2 = Terminar plato): ", 1, 2);
        } while (opcionContinuar == 1);
    }


    private static String leerNombrePlatoNuevo() {
        while (true) {
            String nombre = leerTexto("\nNombre del plato #" + (platos.size() + 1) + ": ");
            if (nombresPlatos.contains(nombre.toLowerCase())) {
                System.out.println("  Error: ya registro un plato llamado \"" + nombre + "\". Use otro nombre.");
            } else {
                return nombre;
            }
        }
    }

    private static void mostrarResumen() {
        System.out.println("\n--- RESUMEN DEL MENU ---");
        System.out.println("Hora de servicio: " + formatearHora(horaServicioMin));
        for (Plato plato : platos) {
            System.out.println("* " + plato);
            List<Tarea> tareas = plato.getTareas();
            for (int i = 0; i < tareas.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + tareas.get(i));
            }
        }
    }

       private static void calcularCronograma() {
        if (platos.size() < MINIMO_PLATOS || horaServicioMin < 0) {
            System.out.println("\nPrimero registre un menu con al menos " + MINIMO_PLATOS + " platos (opcion 1).");
            return;
        }

        int horaActualMin = LecturaConsola.leerHora("Hora actual o de inicio disponible (HH:mm): ");
        Cronograma cronograma = new Cronograma(Planificador.calcularCronograma(platos, horaServicioMin));
        ValidadorHorario validador = new ValidadorHorario(cronograma, horaServicioMin, horaActualMin);
        System.out.println("\n" + ReporteCronograma.formatear(cronograma, validador));
    }
        

    private static void mostrarCronograma(List<PasoCronograma> pasos) {
        System.out.println("\n--- CRONOGRAMA (servicio a las " + formatearHora(horaServicioMin) + ") ---");
        System.out.printf("%-12s%-12s%-20s%-25s%-8s%n", "Hora inicio", "Hora fin", "Plato", "Tarea", "Tipo");
        for (PasoCronograma paso : pasos) {
            System.out.printf("%-12s%-12s%-20s%-25s%-8s%n",
                    formatearHora(paso.getInicioMin()),
                    formatearHora(paso.getFinMin()),
                    paso.getNombrePlato(),
                    paso.getTarea().getNombre(),
                    paso.getTarea().getTipo().getEtiqueta());
        }
    }

    private static String formatearHora(int minutosDesdeMedianoche) {
        if (minutosDesdeMedianoche < 0) {
            return "<00:00";
        }
        return ConversorTiempo.minutosAHora(minutosDesdeMedianoche);
    }

    
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nEntrada finalizada. Hasta pronto.");
            System.exit(0);
        }
        return SCANNER.nextLine().trim();
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            String valor = leerLinea(mensaje);
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("  Error: el dato no puede estar vacio.");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            try {
                int numero = Integer.parseInt(leerLinea(mensaje));
                if (numero > 0) {
                    return numero;
                }
                System.out.println("  Error: el numero debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Error: ingrese un numero entero valido (sin letras).");
            }
        }
    }

    private static int leerOpcion(String mensaje, int minimo, int maximo) {
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


    private static int leerHora(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje);
            if (ConversorTiempo.esHoraValida(texto)) {
                return ConversorTiempo.horaAMinutos(texto);
            }
            System.out.println("  Error: formato de hora invalido. Use HH:mm de 24 horas (ej. 13:30).");
        }
    }
}