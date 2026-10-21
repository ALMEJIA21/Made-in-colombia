import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Cronograma {

    
    private static final Comparator<PasoCronograma> ORDEN_CRONOLOGICO =
            Comparator.comparingInt(PasoCronograma::getInicioMin)
                    .thenComparingInt(PasoCronograma::getFinMin)
                    .thenComparing(PasoCronograma::getNombrePlato, String.CASE_INSENSITIVE_ORDER);

    private final List<PasoCronograma> pasos;

    /**
     * @param pasosCalculados pasos en cualquier orden (por ejemplo, la salida de
     *                        Planificador.calcularInverso). Puede estar vacia.
     * @throws IllegalArgumentException si la lista es nula o contiene1 un elemento nulo
     */
    public Cronograma(List<PasoCronograma> pasosCalculados) {
        if (pasosCalculados == null) {
            throw new IllegalArgumentException("La lista de pasos no puede ser nula.");
        }
        List<PasoCronograma> copia = new ArrayList<>();
        for (int i = 0; i < pasosCalculados.size(); i++) {
            PasoCronograma paso = pasosCalculados.get(i);
            if (paso == null) {
                throw new IllegalArgumentException("La lista de pasos contiene un elemento nulo (posicion " + i + ").");
            }
            copia.add(paso);
        }
        copia.sort(ORDEN_CRONOLOGICO);
        this.pasos = Collections.unmodifiableList(copia);
    }

   
    public List<PasoCronograma> getPasos() {
        return pasos;
    }

    /**
     * Pasos de un tipo (activa o pasiva), conservando el orden cronologico.
     *
     * @throws IllegalArgumentException si el tipo es nulo
     */
    public List<PasoCronograma> filtrarPorTipo(Tarea.Tipo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de tarea no puede ser nulo.");
        }
        List<PasoCronograma> resultado = new ArrayList<>();
        for (PasoCronograma paso : pasos) {
            if (paso.getTarea().getTipo() == tipo) {
                resultado.add(paso);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    /**
     * Pasos de un plato, conservando el orden cronologico. No distingue
     * mayusculas (igual que Main al rechazar platos repetidos). Si el plato no
     * existe en el cronograma, devuelve una lista vacia.
     *
     * @throws IllegalArgumentException si el nombre es nulo o esta vacio
     */
    public List<PasoCronograma> filtrarPorPlato(String nombrePlato) {
        if (nombrePlato == null || nombrePlato.isBlank()) {
            throw new IllegalArgumentException("El nombre del plato no puede estar vacio.");
        }
        String buscado = nombrePlato.trim();
        List<PasoCronograma> resultado = new ArrayList<>();
        for (PasoCronograma paso : pasos) {
            if (paso.getNombrePlato().equalsIgnoreCase(buscado)) {
                resultado.add(paso);
            }
        }
        return Collections.unmodifiableList(resultado);
    }
}