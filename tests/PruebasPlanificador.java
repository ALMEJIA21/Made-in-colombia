import java.util.ArrayList;
import java.util.List;

public class PruebasPlanificador {

    private static int total = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        int servicio = ConversorTiempo.horaAMinutos("13:00");

        Plato arroz = new Plato("Arroz con coco");
        arroz.agregarTarea(new Tarea("Lavar y sofreir", 10, Tarea.Tipo.ACTIVA));
        arroz.agregarTarea(new Tarea("Cocinar arroz", 30, Tarea.Tipo.PASIVA));
        Plato pollo = new Plato("Pollo sudado");
        pollo.agregarTarea(new Tarea("Picar verduras", 15, Tarea.Tipo.ACTIVA));
        pollo.agregarTarea(new Tarea("Cocer pollo", 40, Tarea.Tipo.PASIVA));

        List<Plato> menu = new ArrayList<>();
        menu.add(arroz);
        menu.add(pollo);
        List<PasoCronograma> pasos = Planificador.calcularInverso(menu, servicio);

        verificar("CT-P01 escenario de referencia: 4 pasos", pasos.size() == 4);
        verificar("CT-P02a Lavar y sofreir 12:20-12:30", paso(pasos, 0, "12:20", "12:30"));
        verificar("CT-P02b Cocinar arroz 12:30-13:00", paso(pasos, 1, "12:30", "13:00"));
        verificar("CT-P02c Picar verduras 12:05-12:20", paso(pasos, 2, "12:05", "12:20"));
        verificar("CT-P02d Cocer pollo 12:20-13:00", paso(pasos, 3, "12:20", "13:00"));

        boolean convergen = pasos.get(1).getFinMin() == servicio && pasos.get(3).getFinMin() == servicio;
        verificar("CT-P03 la ultima tarea de cada plato termina a la hora de servicio", convergen);

        boolean continuo = pasos.get(0).getFinMin() == pasos.get(1).getInicioMin()
                && pasos.get(2).getFinMin() == pasos.get(3).getInicioMin();
        verificar("CT-P04 las tareas de un plato se encadenan en orden", continuo);

        Plato solo = new Plato("Jugo");
        solo.agregarTarea(new Tarea("Licuar", 5, Tarea.Tipo.ACTIVA));
        List<Plato> unico = new ArrayList<>();
        unico.add(solo);
        List<PasoCronograma> pasosUnico = Planificador.calcularInverso(unico, servicio);
        verificar("CT-P05 plato con una tarea: 12:55-13:00",
                pasosUnico.size() == 1 && paso(pasosUnico, 0, "12:55", "13:00"));

        List<PasoCronograma> temprano = Planificador.calcularInverso(unico, ConversorTiempo.horaAMinutos("00:03"));
        verificar("CT-P06 inicio antes de medianoche queda negativo (-2)", temprano.get(0).getInicioMin() == -2);

        verificarExcepcion("CT-P07a lista vacia lanza excepcion", () -> Planificador.calcularInverso(new ArrayList<>(), servicio));
        verificarExcepcion("CT-P07b lista nula lanza excepcion", () -> Planificador.calcularInverso(null, servicio));
        verificarExcepcion("CT-P07c hora de servicio fuera del dia lanza excepcion", () -> Planificador.calcularInverso(menu, 1440));

        System.out.println();
        System.out.println("Total: " + total + " | Satisfactorias: " + (total - fallidas) + " | Fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static boolean paso(List<PasoCronograma> pasos, int indice, String inicio, String fin) {
        PasoCronograma p = pasos.get(indice);
        return p.getInicioMin() == ConversorTiempo.horaAMinutos(inicio) && p.getFinMin() == ConversorTiempo.horaAMinutos(fin);
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) {
            fallidas++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLO] ") + descripcion);
    }

    private static void verificarExcepcion(String descripcion, Runnable accion) {
        boolean lanzo = false;
        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        verificar(descripcion, lanzo);
    }
}
