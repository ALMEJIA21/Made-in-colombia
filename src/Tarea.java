
public class Tarea {

 
    public enum Tipo {
        ACTIVA("Activa"),   
        PASIVA("Pasiva");   

        private final String etiqueta;

        Tipo(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String getEtiqueta() {
            return etiqueta;
        }
    }

    private final String nombre;
    private final int duracionMinutos;
    private final Tipo tipo;

    public Tarea(String nombre, int duracionMinutos, Tipo tipo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede estar vacio.");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duracion debe ser mayor que 0.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de tarea es obligatorio.");
        }
        this.nombre = nombre.trim();
        this.duracionMinutos = duracionMinutos;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public boolean esActiva() {
        return tipo == Tipo.ACTIVA;
    }

    @Override
    public String toString() {
        return nombre + " (" + duracionMinutos + " min, " + tipo.getEtiqueta() + ")";
    }
}