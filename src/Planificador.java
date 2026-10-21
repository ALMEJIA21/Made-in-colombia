import java.util.ArrayList;
import java.util.List;

public final class Planificador {

    private Planificador() {
    }

    public static List<PasoCronograma> calcularInverso(List<Plato> platos, int horaServicioMin) {
        if (platos == null || platos.isEmpty()) {
            throw new IllegalArgumentException("Se necesita al menos un plato.");
        }
        if (horaServicioMin < 0 || horaServicioMin >= ConversorTiempo.MINUTOS_POR_DIA) {
            throw new IllegalArgumentException("La hora de servicio debe estar dentro del dia.");
        }

        List<PasoCronograma> pasos = new ArrayList<>();
        for (Plato plato : platos) {
            pasos.addAll(calcularPlato(plato, horaServicioMin));
        }
        return pasos;
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
