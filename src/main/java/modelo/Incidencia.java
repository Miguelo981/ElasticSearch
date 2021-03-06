/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.enums.Tipo;
import java.time.LocalDate;

/**
 *
 * @author alu2018262
 */
public class Incidencia {

    /**
     * Para la fecha hemos utilizado el LocalDate, para el origen, destino y
     * detalla un String. Y por último para el tipo de incidencias hemos
     * utilizado un enumero.
     */
    private LocalDate fecha;
    private String orgien, destino, detalle;
    private Tipo tipo;

    public Incidencia() {

    }

    public Incidencia(LocalDate fecha, String orgien, String destino, String detalle, Tipo tipo) {
        this.fecha = fecha;
        this.orgien = orgien;
        this.destino = destino;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return orgien;
    }

    public void setOrigen(String orgien) {
        this.orgien = orgien;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Función toString sobreescrita para devolver el resultado por pantalla de
     * las incidencias.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Incidencia -> " + "fecha=" + fecha + ", orgien=" + orgien + ", destino=" + destino + ", detalle=" + detalle + ", tipo=" + tipo;
    }
}
