/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import modelo.enums.TipoEvento;
import java.time.LocalDate;

/**
 *
 * @author alu2018262
 */
public class Evento {

    /**
     * Para el tipo de hemos utilizado un enumerado, un LocalDate para la fecha
     * y un string para el nombre de usuario.
     */
    private TipoEvento tipo;
    private LocalDate fecha;
    private String usuario;

    public Evento(TipoEvento tipo, LocalDate fecha, String usuario) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Función sobrescrita para deolver el resultado de eventos por pantalla.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Historial -> " + "tipo=" + tipo + ", fecha=" + fecha + ", usuario=" + usuario;
    }
}
