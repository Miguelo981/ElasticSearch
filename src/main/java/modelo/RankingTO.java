package modelo;

/**
 * Se implementa a la clase el comparable, permitira compara los objetos según
 * sus atributos.
 *
 * @author alu2018240
 */
public class RankingTO implements Comparable<RankingTO> {

    /**
     * Atributo empleado de tipo Empleado y un int para el número de
     * incidencias.
     */
    private Empleado empleado;
    private int numIncidencias;

    public RankingTO(Empleado empleado, int numIncidencias) {
        this.empleado = empleado;
        this.numIncidencias = numIncidencias;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getNumIncidencias() {
        return numIncidencias;
    }

    public void setNumIncidencias(int numIncidencias) {
        this.numIncidencias = numIncidencias;
    }

    /**
     * Función compareTo sobreescrita para comprarar objetos de tipo RankingTO
     * según el número de incidencias.
     *
     * @param t
     * @return
     */
    @Override
    public int compareTo(RankingTO t) {
        if (numIncidencias > t.numIncidencias) {
            return -1;
        }
        return 1;
    }

    /**
     * Función toString sobreescrita para devolver el resultado del número de
     * incidencias.
     *
     * @return
     */
    @Override
    public String toString() {
        return empleado + ", num incidencias urgentes=" + numIncidencias;
    }
}
