
public final class ConversorTiempo {

    public static final int MINUTOS_POR_HORA = 60;
    public static final int MINUTOS_POR_DIA = 24 * MINUTOS_POR_HORA;

    // Clase de utilidades: solo metodos estaticos, no se instancia.
    private ConversorTiempo() {
    }

    /** Indica si el texto tiene formato HH:mm valido (00:00 a 23:59). */
    public static boolean esHoraValida(String texto) {
        if (texto == null) {
            return false;
        }
        String limpio = texto.trim();
        if (!limpio.matches("\\d{1,2}:\\d{2}")) {
            return false;
        }
        String[] partes = limpio.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        return horas <= 23 && minutos <= 59;
    }

    /**
     * Convierte "HH:mm" a minutos desde la medianoche.
     *
     * @throws IllegalArgumentException si el texto no es una hora valida
     */
    public static int horaAMinutos(String texto) {
        if (!esHoraValida(texto)) {
            throw new IllegalArgumentException(
                    "Hora invalida: \"" + texto + "\". Use HH:mm de 24 horas (ej. 13:30).");
        }
        String[] partes = texto.trim().split(":");
        return Integer.parseInt(partes[0]) * MINUTOS_POR_HORA + Integer.parseInt(partes[1]);
    }

    /**
     * Convierte minutos desde la medianoche a "HH:mm".
     *
     * @throws IllegalArgumentException si los minutos estan fuera del dia (0 a 1439)
     */
    public static String minutosAHora(int minutos) {
        if (minutos < 0 || minutos >= MINUTOS_POR_DIA) {
            throw new IllegalArgumentException(
                    "Minutos fuera del dia: " + minutos + ". Debe estar entre 0 y " + (MINUTOS_POR_DIA - 1) + ".");
        }
        return String.format("%02d:%02d", minutos / MINUTOS_POR_HORA, minutos % MINUTOS_POR_HORA);
    }
}
