import java.util.ArrayList;
import java.util.List;

public final class Planificador {

    private Planificador() {
    }

    public static List<PasoCronograma> calcularInverso(List<Plato> platos, int horaServicioMin) {
        validarEntrada(platos, horaServicioMin);

        List<PasoCronograma> pasos = new ArrayList<>();
        for (Plato plato : platos) {
            pasos.addAll(calcularPlato(plato, horaServicioMin));
        }
        return pasos;
    }

    public static List<PasoCronograma> calcularCronograma(List<Plato> platos, int horaServicioMin) {
        validarEntrada(platos, horaServicioMin);

        int cantidad = platos.size();
        int[] siguiente = new int[cantidad];
        int[] limite = new int[cantidad];
        List<List<PasoCronograma>> pasosPorPlato = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            siguiente[i] = platos.get(i).getTareas().size() - 1;
            limite[i] = horaServicioMin;
            pasosPorPlato.add(new ArrayList<>());
        }

        List<int[]> ocupadosActivos = new ArrayList<>();
        int elegido = elegirPlato(siguiente, limite);
        while (elegido != -1) {
            Plato plato = platos.get(elegido);
            Tarea tarea = plato.getTareas().get(siguiente[elegido]);
            int duracion = tarea.getDuracionMinutos();

            int fin = limite[elegido];
            if (tarea.esActiva()) {
                fin = ubicarActiva(fin, duracion, ocupadosActivos);
                ocupadosActivos.add(new int[] {fin - duracion, fin});
            }
            int inicio = fin - duracion;

            pasosPorPlato.get(elegido).add(0, new PasoCronograma(plato.getNombre(), tarea, inicio, fin));
            limite[elegido] = inicio;
            siguiente[elegido]--;
            elegido = elegirPlato(siguiente, limite);
        }

        List<PasoCronograma> pasos = new ArrayList<>();
        for (List<PasoCronograma> pasosPlato : pasosPorPlato) {
            pasos.addAll(pasosPlato);
        }
        return pasos;
    }

    private static int elegirPlato(int[] siguiente, int[] limite) {
        int elegido = -1;
        for (int i = 0; i < siguiente.length; i++) {
            if (siguiente[i] >= 0 && (elegido == -1 || limite[i] > limite[elegido])) {
                elegido = i;
            }
        }
        return elegido;
    }

    private static int ubicarActiva(int fin, int duracion, List<int[]> ocupadosActivos) {
        boolean movido = true;
        while (movido) {
            movido = false;
            int inicio = fin - duracion;
            int nuevoFin = fin;
            for (int[] ocupado : ocupadosActivos) {
                boolean seSolapan = inicio < ocupado[1] && fin > ocupado[0];
                if (seSolapan && ocupado[0] < nuevoFin) {
                    nuevoFin = ocupado[0];
                    movido = true;
                }
            }
            fin = nuevoFin;
        }
        return fin;
    }

    private static void validarEntrada(List<Plato> platos, int horaServicioMin) {
        if (platos == null || platos.isEmpty()) {
            throw new IllegalArgumentException("Se necesita al menos un plato.");
        }
        if (horaServicioMin < 0 || horaServicioMin >= ConversorTiempo.MINUTOS_POR_DIA) {
            throw new IllegalArgumentException("La hora de servicio debe estar dentro del dia.");
        }
    }

    private static List<PasoCronograma> calcularPlato(Plato plato, int horaServicioMin) {
        List<Tarea> tareas = plato.getTareas();
        List<PasoCronograma> pasosPlato = new ArrayList<>();

        int fin = horaServicioMin;
        for (int i = tareas.size() - 1; i >= 0; i--) {
            Tarea tarea = tareas.get(i);
            int inicio = fin - tarea.getDuracionMinutos();
            pasosPlato.add(0, new PasoCronograma(plato.getNombre(), tarea, inicio, fin));
            fin = inicio;
        }
        return pasosPlato;
    }
}