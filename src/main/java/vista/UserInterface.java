/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

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
            if (e.getUsuario().equals("admin") && e.getPassword().equals("admin")) {
                switch (InputAsker.askInt(menuAdmin())) {
                    case 1:
                        insertarIncidencia();
                        break;
                    case 2:
                        incidenciaByID();
                        break;
                    case 3:
                        registEmpleado();
                    case 0:
                        response = true;
                        break;
                }
            } else {
                switch (InputAsker.askInt(menu())) {
                    case 1:
                        insertarIncidencia();
                        break;
                    case 2:
                        incidenciaByID();
                        break;
                    case 3:
                        getAllIncidencias();
                    case 4:
                        getRankingEmpleados();
                    case 0:
                        response = true;
                        break;
                }
            }
        } while (!response);
    }    
    
    private String menu() {
        return "1.- Report incident.\n2.- Get incident by ID.\n0.- Exit";
    }
    
    private String menuAdmin() {
        return "1.- Report incident.\n2.- Get incident by ID.\n3.- Register \n0.- Exit";
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
    
    private void registEmpleado() {
        Empleado e = new Empleado();
        String user = InputAsker.askString("Username: ");
        e.setUsuario(user);
        e.setNombre(InputAsker.askString("Name: "));
        String pass = "", pass2 = "";
        do {
            pass = InputAsker.askString("Insert password: (8 digits maximum)", 8);
            pass2 = InputAsker.askString("Confirm password: ", 8);
            if (!pass.equals(pass2)) {
                System.out.println("Passwords does not match.");
            }
        } while (!pass.equals(pass2));
        e.setPassword(pass);
        e.setApellidos(InputAsker.askString("Surname: "));
        e.setTelefono(InputAsker.askString("Phone number: ", 8));
        e.setDni(InputAsker.askDNI("DNI: "));
        daoInterfaceImpl.insertEmpleado(e);
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

    private void getAllIncidencias() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getRankingEmpleados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
