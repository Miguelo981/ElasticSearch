package modelo;

/**
 *
 * @author alu2018240
 */
public class RankingTO implements Comparable<RankingTO>{
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

    @Override
    public int compareTo(RankingTO t) {
        if (numIncidencias>t.numIncidencias){
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return empleado + ", num incidencias urgentes=" + numIncidencias;
    }
}
