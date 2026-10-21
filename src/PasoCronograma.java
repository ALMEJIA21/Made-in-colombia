public class PasoCronograma {

    private final String nombrePlato;
    private final Tarea tarea;
    private final int inicioMin;
    private final int finMin;

    public PasoCronograma(String nombrePlato, Tarea tarea, int inicioMin, int finMin) {
        if (nombrePlato == null || nombrePlato.isBlank()) {
            throw new IllegalArgumentException("El nombre del plato no puede estar vacio.");
        }
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
        if (finMin - inicioMin != tarea.getDuracionMinutos()) {
            throw new IllegalArgumentException("El intervalo no coincide con la duracion de la tarea.");
        }
        this.nombrePlato = nombrePlato;
        this.tarea = tarea;
        this.inicioMin = inicioMin;
        this.finMin = finMin;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public int getInicioMin() {
        return inicioMin;
    }

    public int getFinMin() {
        return finMin;
    }

    @Override
    public String toString() {
        return nombrePlato + " | " + tarea + " | " + inicioMin + " -> " + finMin;
    }
}
