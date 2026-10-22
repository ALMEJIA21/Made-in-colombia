import java.util.List;


public final class ReporteCronograma {

    private static final String[] ENCABEZADOS = {"Hora inicio", "Hora fin", "Plato", "Tarea", "Tipo"};
    private static final int ANCHO_ETIQUETA = 24;

    
    private ReporteCronograma() {
    }


    public static String formatearHora(int minutos) {
        if (minutos < 0) {
            return "<00:00";
        }
        return ConversorTiempo.minutosAHora(minutos);
    }

 
    public static String formatear(Cronograma cronograma, ValidadorHorario validador) {
        List<PasoCronograma> pasos = cronograma.getPasos();
        StringBuilder texto = new StringBuilder();
        texto.append("=== CRONOGRAMA (servicio a las ")
                .append(formatearHora(validador.getHoraServicioMin())).append(") ===\n\n");
        texto.append(formatearEstado(validador)).append("\n\n");
        texto.append(formatearTabla(pasos)).append("\n");
        texto.append("Pasos: ").append(pasos.size())
                .append(" | Inicio mas temprano: ").append(formatearHora(validador.getInicioMasTempranoMin()))
                .append(" | Servicio: ").append(formatearHora(validador.getHoraServicioMin()));
        return texto.toString();
    }


        /** Tabla con el resultado de una consulta (filtro por plato o por tipo). */
    public static String formatearConsulta(String titulo, List<PasoCronograma> pasos) {
        if (pasos.isEmpty()) {
            return "=== " + titulo + " ===\nNo hay tareas que coincidan con la consulta.";
        }
        return "=== " + titulo + " ===\n" + formatearTabla(pasos) + "\nTareas encontradas: " + pasos.size();
    }

    /** Reporte agrupado: una linea por plato con sus tareas, inicio, fin y minutos activos. */
    public static String formatearResumenPorPlato(Cronograma cronograma, List<Plato> platos) {
        StringBuilder texto = new StringBuilder("=== RESUMEN POR PLATO ===\n");
        for (Plato plato : platos) {
            List<PasoCronograma> pasosPlato = cronograma.filtrarPorPlato(plato.getNombre());
            int minutosActivos = 0;
            for (PasoCronograma paso : pasosPlato) {
                if (paso.getTarea().esActiva()) {
                    minutosActivos += paso.getTarea().getDuracionMinutos();
                }
            }
            texto.append(String.format("%-20s Tareas: %d | Empieza: %s | Termina: %s | Minutos activos: %d%n",
                    plato.getNombre(),
                    pasosPlato.size(),
                    formatearHora(pasosPlato.get(0).getInicioMin()),
                    formatearHora(pasosPlato.get(pasosPlato.size() - 1).getFinMin()),
                    minutosActivos));
        }
        return texto.toString().trim();
    }
    
    private static String formatearEstado(ValidadorHorario validador) {
        if (!validador.hayConflicto()) {
            String inicio = formatearHora(validador.getInicioMasTempranoMin());
            if (validador.getMinutosSobrantes() == 0) {
                return "Tiempo justo: debe empezar a las " + inicio + ", que es la hora actual.";
            }
            return "Tiempo suficiente: debe empezar a las " + inicio + " y sobran "
                    + minutos(validador.getMinutosSobrantes()) + ".";
        }

        StringBuilder bloque = new StringBuilder();
        bloque.append("*** ADVERTENCIA: HORARIO IMPOSIBLE ***\n");
        bloque.append(linea("Hora de servicio:", formatearHora(validador.getHoraServicioMin()))).append("\n");
        bloque.append(linea("Hora actual ingresada:", formatearHora(validador.getHoraActualMin()))).append("\n");
        bloque.append(linea("Inicio necesario:", formatearInicioNecesario(validador.getInicioMasTempranoMin()))).append("\n");
        bloque.append(linea("Retraso estimado:", minutos(validador.getRetrasoMinutos())));
        if (validador.horaActualPosteriorAlServicio()) {
            bloque.append("\nLa hora actual es posterior a la hora de servicio.");
        }
        return bloque.toString();
    }

    private static String formatearInicioNecesario(int inicioMin) {
        if (inicioMin < 0) {
            return "<00:00 (" + minutos(-inicioMin) + " antes de la medianoche)";
        }
        return formatearHora(inicioMin);
    }

    private static String linea(String etiqueta, String valor) {
        return String.format("%-" + ANCHO_ETIQUETA + "s%s", etiqueta, valor);
    }

    private static String minutos(int cantidad) {
        return cantidad + (cantidad == 1 ? " minuto" : " minutos");
    }

  
    private static String formatearTabla(List<PasoCronograma> pasos) {
        String[][] filas = new String[pasos.size()][];
        int[] anchos = new int[ENCABEZADOS.length];
        for (int c = 0; c < ENCABEZADOS.length; c++) {
            anchos[c] = ENCABEZADOS[c].length();
        }

        for (int f = 0; f < pasos.size(); f++) {
            PasoCronograma paso = pasos.get(f);
            filas[f] = new String[] {
                    formatearHora(paso.getInicioMin()),
                    formatearHora(paso.getFinMin()),
                    paso.getNombrePlato(),
                    paso.getTarea().getNombre(),
                    paso.getTarea().getTipo().getEtiqueta()
            };
            for (int c = 0; c < filas[f].length; c++) {
                anchos[c] = Math.max(anchos[c], filas[f][c].length());
            }
        }

        String separador = armarSeparador(anchos);
        StringBuilder tabla = new StringBuilder();
        tabla.append(separador).append("\n");
        tabla.append(armarFila(ENCABEZADOS, anchos)).append("\n");
        tabla.append(separador).append("\n");
        for (String[] fila : filas) {
            tabla.append(armarFila(fila, anchos)).append("\n");
        }
        tabla.append(separador);
        return tabla.toString();
    }

    private static String armarSeparador(int[] anchos) {
        StringBuilder separador = new StringBuilder("+");
        for (int ancho : anchos) {
            separador.append("-".repeat(ancho + 2)).append("+");
        }
        return separador.toString();
    }

    private static String armarFila(String[] celdas, int[] anchos) {
        StringBuilder fila = new StringBuilder("|");
        for (int c = 0; c < celdas.length; c++) {
            fila.append(" ").append(String.format("%-" + anchos[c] + "s", celdas[c])).append(" |");
        }
        return fila.toString();
    }
}