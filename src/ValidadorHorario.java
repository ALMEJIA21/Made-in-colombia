
public class ValidadorHorario {

    private final int horaServicioMin;
    private final int horaActualMin;
    private final int inicioMasTempranoMin;

    
    public ValidadorHorario(Cronograma cronograma, int horaServicioMin, int horaActualMin) {
        if (cronograma == null || cronograma.getPasos().isEmpty()) {
            throw new IllegalArgumentException("El cronograma no puede ser nulo ni estar vacio.");
        }
        validarHora(horaServicioMin, "de servicio");
        validarHora(horaActualMin, "actual");
        this.horaServicioMin = horaServicioMin;
        this.horaActualMin = horaActualMin;
       
        this.inicioMasTempranoMin = cronograma.getPasos().get(0).getInicioMin();
    }

    private static void validarHora(int minutos, String nombre) {
        if (minutos < 0 || minutos >= ConversorTiempo.MINUTOS_POR_DIA) {
            throw new IllegalArgumentException("La hora " + nombre + " debe estar dentro del dia.");
        }
    }

  
    public boolean hayConflicto() {
        return inicioMasTempranoMin < horaActualMin;
    }

    
    public int getRetrasoMinutos() {
        return hayConflicto() ? horaActualMin - inicioMasTempranoMin : 0;
    }

   
    public int getMinutosSobrantes() {
        return hayConflicto() ? 0 : inicioMasTempranoMin - horaActualMin;
    }

 
    public boolean horaActualPosteriorAlServicio() {
        return horaActualMin > horaServicioMin;
    }

    public int getHoraServicioMin() {
        return horaServicioMin;
    }

    public int getHoraActualMin() {
        return horaActualMin;
    }

    public int getInicioMasTempranoMin() {
        return inicioMasTempranoMin;
    }
}