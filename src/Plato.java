import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Plato {

    private final String nombre;
    private final List<Tarea> tareas = new ArrayList<>();

    public Plato(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del plato no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public void agregarTarea(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
        tareas.add(tarea);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tarea> getTareas() {
        return Collections.unmodifiableList(tareas);
    }


    public int getDuracionTotal() {
        int total = 0;
        for (Tarea t : tareas) {
            total += t.getDuracionMinutos();
        }
        return total;
    }

    @Override
    public String toString() {
        return nombre + " (" + tareas.size() + " tareas, " + getDuracionTotal() + " min)";
    }
}