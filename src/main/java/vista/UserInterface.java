/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import controller.ManagerDao;
import java.time.LocalDate;
import modelo.DAOInterfaceImpl;
import modelo.Empleado;
import modelo.Incidencia;
import modelo.enums.Tipo;
import static vista.Vista.cleanEmpleados;
import static vista.Vista.tryCaso;

/**
 *
 * @author alu2018240
 */
public class UserInterface {
    private DAOInterfaceImpl daoInterfaceImpl;

    public UserInterface() {
    }
    
    public void MenuUsuario(Empleado e, DAOInterfaceImpl impl){
        Boolean response = false;
        daoInterfaceImpl = impl;
        do {
            switch (InputAsker.askInt(menu())) {
                case 1:
                    insertarIncidencia();
                    break;
                case 2:
                    incidenciaByID();
                    break;
                case 0:
                    response = true;
                    break;
            }
        } while (!response);
    }    
    
    private String menu() {
        return "1.- Report incident.\n2.- Get incident by ID.\n0.- Exit";
    }
    
    private void insertarIncidencia() {
        Incidencia i = new Incidencia();
        i.setFecha(LocalDate.now());
        i.setOrgien(InputAsker.askString("Origin: "));
        i.setDestino(InputAsker.askString("Destination: "));
        i.setDetalle(InputAsker.askString("Detail: "));
        i.setTipo(getTipo(InputAsker.askString("Type: ")));
        daoInterfaceImpl.insertIncidencia(i);
    }

    private void incidenciaByID() {
        int id = InputAsker.askInt("ID: ");
        System.out.println(daoInterfaceImpl.getIncidenciaById(id).toString());
    }
    
    //Moverlo a otro sitio
    public Tipo getTipo(String tipo) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return Tipo.URGENTE;
            case "NORMAL":
                return Tipo.NORMAL;
        }
        return null;
    }
}
