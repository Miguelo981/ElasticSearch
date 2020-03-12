/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.time.LocalDate;
import java.util.List;
import modelo.DAOInterfaceImpl;
import modelo.Empleado;
import modelo.Incidencia;
import modelo.enums.Tipo;

/**
 *
 * @author alu2018240
 */
public class UserInterface {

    private DAOInterfaceImpl daoInterfaceImpl;

    public UserInterface() {
    }

    public void menuUsuario(Empleado e, DAOInterfaceImpl impl) {
        Boolean response = false;
        daoInterfaceImpl = impl;
        do {
            if (e.getUsuario().equals("admin") && e.getPassword().equals("admin")) {
                switch (InputAsker.askInt(menuAdmin())) {
                    case 1:
                        registEmpleado();
                        break;
                    case 2:
                        updateEmpleado();
                        break;
                    case 3:
                        deleteEmpleado();
                        break;
                    case 4:
                        getRankingEmpleados();
                        break;
                        
                    case 0:
                        response = true;
                        break;
                }
            } else {
                switch (InputAsker.askInt(menu())) {
                    case 1:
                        insertarIncidencia(e);
                        break;
                    case 2:
                        incidenciaByID();
                        break;
                    case 3:
                        getAllIncidencias();
                        break;
                    case 4:
                        getIncidentByOrigen(e);
                        break;
                    case 5:
                        getIncidentByDestino(e);
                        break;
                    case 0:
                        response = true;
                        break;
                }
            }
        } while (!response);
    }

    private String menu() {
        return "1 - Report incident.\n2 - Get incident by ID.\n3 - Show all incidents.\n4 - Get incident by origin.\n5 - Get incident by destination\n0.- Exit";
    }

    private String menuAdmin() {
        return "1 - Register.\n2 - Get Ranking Employee\n0.- Exit";
    }

    private void insertarIncidencia(Empleado e) {
        Incidencia i = new Incidencia();
        i.setFecha(LocalDate.now());
        i.setOrigen(e.getUsuario());
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

    private void updateEmpleado() {
        System.out.println("Update");
        List<Empleado> empleados = daoInterfaceImpl.findEmpleados();
        if (!empleados.isEmpty()) {
            for (Empleado e : empleados) {
                System.out.println(e.toString());
            }
            String user = InputAsker.askString("Select user (username)");
            Empleado e = null;
            for (Empleado empleado : empleados) {
                if (empleado.getUsuario().equalsIgnoreCase(user)) {
                    e = empleado;
                }
            }
            if (e == null) {
                System.out.println("No user exists with that username");
            } else {
                e.setNombre(InputAsker.askString("Name ("+e.getNombre()+"): "));
                e.setApellidos(InputAsker.askString("Surname ("+e.getApellidos()+"): "));
                e.setTelefono(InputAsker.askString("Phone number ("+e.getTelefono()+"): ", 8));
                e.setDni(InputAsker.askDNI("DNI ("+e.getDni()+"): "));
                daoInterfaceImpl.updateEmpleado(e);
            }
        } else {
            System.out.println("No users registered");
        }
    }

    private void deleteEmpleado() {
        Empleado e = new Empleado();
        //Falta pasar por parametro el empleado conectado
        daoInterfaceImpl.removeEmpleado(e);
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
        for (Incidencia i : daoInterfaceImpl.selectAllIncidencias()) {
            System.out.println(i);
        }
    }

    private void getRankingEmpleados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void getIncidentByOrigen(Empleado e) {
        for (Incidencia i : daoInterfaceImpl.getIncidenciaByOrigen(e)) {
            System.out.println(i);
        }
    }

    private void getIncidentByDestino(Empleado e) {
        for (Incidencia i : daoInterfaceImpl.getIncidenciaByDestino(e)) {
            System.out.println(i);
        }
    }
}
