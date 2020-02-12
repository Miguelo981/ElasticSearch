/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import enums.Evento;
import java.time.LocalDate;

/**
 *
 * @author alu2018262
 */
public class Historial {
 
    private Evento tipo;
    private LocalDate fecha;
    private Empleado usuario;

    public Historial(Evento tipo, LocalDate fecha, Empleado usuario) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public Evento getTipo() {
        return tipo;
    }

    public void setTipo(Evento tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Empleado getUsuario() {
        return usuario;
    }

    public void setUsuario(Empleado usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Historial ->" + "tipo=" + tipo + ", fecha=" + fecha + ", usuario=" + usuario;
    }
}
