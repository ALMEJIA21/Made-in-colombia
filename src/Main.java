import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {

    private static final int MINIMO_PLATOS = 2;

    private static int horaServicioMin = -1;
   
    private static final List<Plato> platos = new ArrayList<>();
  
    private static final Map<String, Plato> platosPorNombre = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("=== MADE IN COLOMBIA ===");
        System.out.println("Orquestador de tiempos culinarios\n");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1) Registrar menu (hora de servicio, platos y tareas)");
            System.out.println("2) Calcular y mostrar cronograma");
            System.out.println("3) Consultar cronograma (por plato, por tipo o resumen)");
            System.out.println("4) Salir");
            int opcion = LecturaConsola.leerOpcion("Elija una opcion: ", 1, 4);

            switch (opcion) {
                case 1 -> registrarMenu();
                case 2 -> calcularCronograma();
                case 3 -> consultarCronograma();
                case 4 -> {
                    System.out.println("Hasta pronto.");
                    salir = true;
                }
            }
        }
    }


    private static void registrarMenu() {
        platos.clear();
        platosPorNombre.clear();

        System.out.println("\n--- REGISTRO DE MENU ---");
        horaServicioMin = LecturaConsola.leerHora("Hora de servicio para todo el menu (HH:mm): ");

        boolean otroPlato = true;
        while (otroPlato) {
            String nombrePlato = leerNombrePlatoNuevo();
            Plato plato = new Plato(nombrePlato);

            registrarTareas(plato);
            platos.add(plato);
            platosPorNombre.put(plato.getNombre().toLowerCase(), plato);
            System.out.println("Plato registrado: " + plato);

            if (platos.size() < MINIMO_PLATOS) {
                System.out.println("Faltan platos: el menu necesita al menos " + MINIMO_PLATOS + ".");
            } else {
                otroPlato = LecturaConsola.leerOpcion("Agregar otro plato? (1 = Si, 2 = No): ", 1, 2) == 1;
            }
        }

        mostrarResumen();
    }


    private static void registrarTareas(Plato plato) {
        System.out.println("Registre las tareas de \"" + plato.getNombre() + "\" en el orden en que se ejecutan.");

        int opcionContinuar;
        do {
            String nombreTarea = LecturaConsola.leerTexto("  Tarea #" + (plato.getTareas().size() + 1) + " - nombre: ");
            int duracion = LecturaConsola.leerEnteroPositivo("  Duracion en minutos: ");
            int tipoOpcion = LecturaConsola.leerOpcion("  Tipo (1 = Activa, 2 = Pasiva): ", 1, 2);
            Tarea.Tipo tipo = (tipoOpcion == 1) ? Tarea.Tipo.ACTIVA : Tarea.Tipo.PASIVA;

            plato.agregarTarea(new Tarea(nombreTarea, duracion, tipo));
            System.out.println("  Tarea agregada. Total del plato: " + plato.getDuracionTotal() + " min.");

            opcionContinuar = LecturaConsola.leerOpcion("  Agregar otra tarea? (1 = Si, 2 = Terminar plato): ", 1, 2);
        } while (opcionContinuar == 1);
    }


    private static String leerNombrePlatoNuevo() {
        while (true) {
            String nombre = LecturaConsola.leerTexto("\nNombre del plato #" + (platos.size() + 1) + ": ");
            if (platosPorNombre.containsKey(nombre.toLowerCase())) {
                System.out.println("  Error: ya registro un plato llamado \"" + nombre + "\". Use otro nombre.");
            } else {
                return nombre;
            }
        }
    }

    private static void mostrarResumen() {
        System.out.println("\n--- RESUMEN DEL MENU ---");
        System.out.println("Hora de servicio: " + ReporteCronograma.formatearHora(horaServicioMin));
        for (Plato plato : platos) {
            System.out.println("* " + plato);
            List<Tarea> tareas = plato.getTareas();
            for (int i = 0; i < tareas.size(); i++) {
                System.out.println("    " + (i + 1) + ". " + tareas.get(i));
            }
        }
    }

    private static boolean hayMenuRegistrado() {
        if (platos.size() < MINIMO_PLATOS || horaServicioMin < 0) {
            System.out.println("\nPrimero registre un menu con al menos " + MINIMO_PLATOS + " platos (opcion 1).");
            return false;
        }
        return true;
    }

    private static Cronograma armarCronograma() {
        return new Cronograma(Planificador.calcularCronograma(platos, horaServicioMin));
    }

    private static void calcularCronograma() {
        if (!hayMenuRegistrado()) {
            return;
        }

        int horaActualMin = LecturaConsola.leerHora("Hora actual o de inicio disponible (HH:mm): ");
        Cronograma cronograma = armarCronograma();
        ValidadorHorario validador = new ValidadorHorario(cronograma, horaServicioMin, horaActualMin);
        System.out.println("\n" + ReporteCronograma.formatear(cronograma, validador));
    }

    private static void consultarCronograma() {
        if (!hayMenuRegistrado()) {
            return;
        }

        Cronograma cronograma = armarCronograma();
        System.out.println("\n--- CONSULTAR CRONOGRAMA ---");
        System.out.println("1) Tareas de un plato");
        System.out.println("2) Tareas por tipo (Activa o Pasiva)");
        System.out.println("3) Resumen agrupado por plato");
        int opcion = LecturaConsola.leerOpcion("Elija una consulta: ", 1, 3);

        switch (opcion) {
            case 1 -> {
                Plato plato = buscarPlato();
                System.out.println("\n" + ReporteCronograma.formatearConsulta(
                        "Tareas de \"" + plato.getNombre() + "\"",
                        cronograma.filtrarPorPlato(plato.getNombre())));
            }
            case 2 -> {
                int tipoOpcion = LecturaConsola.leerOpcion("Tipo (1 = Activa, 2 = Pasiva): ", 1, 2);
                Tarea.Tipo tipo = (tipoOpcion == 1) ? Tarea.Tipo.ACTIVA : Tarea.Tipo.PASIVA;
                System.out.println("\n" + ReporteCronograma.formatearConsulta(
                        "Tareas de tipo " + tipo.getEtiqueta(),
                        cronograma.filtrarPorTipo(tipo)));
            }
            case 3 -> System.out.println("\n" + ReporteCronograma.formatearResumenPorPlato(cronograma, platos));
        }
    }


    private static Plato buscarPlato() {
        while (true) {
            String nombre = LecturaConsola.leerTexto("Nombre del plato a consultar: ");
            Plato plato = platosPorNombre.get(nombre.toLowerCase());
            if (plato != null) {
                return plato;
            }
            System.out.println("  Error: no hay un plato llamado \"" + nombre + "\". Platos registrados:");
            for (Plato registrado : platos) {
                System.out.println("    - " + registrado.getNombre());
            }
        }
    }
}
