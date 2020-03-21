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
import modelo.Evento;
import modelo.Incidencia;
import modelo.RankingTO;
import modelo.enums.Tipo;
import org.elasticsearch.client.indices.GetIndexRequest;

/**
 *
 * @author alu2018240
 */
public class UserInterface {

    private DAOInterfaceImpl daoInterface;

    public UserInterface() {
    }

    /**
     * Función para devolver todas las opciones del usuario logueado. Según el
     * tipo de usuario se le mostrar unas opciones u otras.
     *
     * @param e
     * @param dao
     */
    public void menuUsuario(Empleado e, DAOInterfaceImpl dao) {
        Boolean response = false;
        daoInterface = dao;
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
                    case 5:
                        getLastSession();
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

    /**
     * Función para mostrar por pantalla el menú de un usuario convencional
     * (empleado).
     *
     * @return
     */
    private String menu() {
        return "1 - Report incident.\n2 - Get incident by ID.\n3 - Show all incidents.\n4 - Get incident by origin.\n5 - Get incident by destination\n0.- Exit";
    }

    /**
     * Función para mostrar por pantalla el menú de un usuario con permisos
     * (administrador)
     *
     * @return
     */
    private String menuAdmin() {
        return "1 - Register.\n2 - Update Emloyee\n3 - Delete Employee\n4 - Get Ranking Employee\n5 - Get last Session from user\n0.- Exit";
    }

    /**
     * Función para insertar incidencia, se pasa por parametro un empleado. Se
     * le pide al usuario que introduzca el destinatario, el detalle y el tipo
     * de incidencia.
     *
     * @param e
     */
    private void insertarIncidencia(Empleado e) {
        Incidencia i = new Incidencia();
        i.setFecha(LocalDate.now());
        i.setOrigen(e.getUsuario());
        i.setDestino(InputAsker.askString("Destination: "));
        i.setDetalle(InputAsker.askString("Detail: "));
        i.setTipo(getTipo(InputAsker.askString("Type: ")));
        daoInterface.insertIncidencia(i);
    }

    /**
     * Función para ver una incidencia por ID. Se le pide al usuario que
     * introduzca la ID, de cuya incidencia quiere.
     */
    private void incidenciaByID() {
        int id = InputAsker.askInt("ID: ");
        System.out.println(daoInterface.getIncidenciaById(id).toString());
    }

    /**
     * Función para registrar un empleado, Se pide al usuario que escriba el
     * usuario, el nombre, la contraseña (mínimo 8 digitos), los apellidos, el
     * teléfono y el DNI (8 numeros y 1 letra).
     */
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
        daoInterface.insertEmpleado(e);
    }

    /**
     * Función para actualizar un empleado, se le pide al usuario que escriba el
     * nombre de usuario, en caso de exista, se le pidara que estableza los
     * nuevos datos para el usuario.
     */
    private void updateEmpleado() {
        System.out.println("Update");
        List<Empleado> empleados = daoInterface.findEmpleados();
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
                e.setNombre(InputAsker.askString("Name (" + e.getNombre() + "): "));
                e.setApellidos(InputAsker.askString("Surname (" + e.getApellidos() + "): "));
                e.setTelefono(InputAsker.askString("Phone number (" + e.getTelefono() + "): ", 8));
                e.setDni(InputAsker.askDNI("DNI (" + e.getDni() + "): "));
                daoInterface.updateEmpleado(e);
            }
        } else {
            System.out.println("No users registered");
        }
    }

    /**
     * Función para eliminar un empleado, se le pide al usuario que escriba el
     * nombre de usuario que quiere eliminar, en caso de no haya ningún usuario
     * registrado con ese nombre, se le mostrar un mensaje de error. Además, si
     * no empleados registrados se le mostrar por pantalla.
     */
    private void deleteEmpleado() {
        System.out.println("Delete");
        List<Empleado> empleados = daoInterface.findEmpleados();
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
                daoInterface.removeEmpleado(e);
            }
        } else {
            System.out.println("No users registered");
        }
    }

    /**
     * Función para devolver el tipo de incidencia pasando por parametro el tipo
     * en formato cadena.
     *
     * @param tipo
     * @return
     */
    public Tipo getTipo(String tipo) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return Tipo.URGENTE;
            case "NORMAL":
                return Tipo.NORMAL;
        }
        return null;
    }

    /**
     * Función para mostrar todas las incidencias registradas/almacenadas en la
     * BBDD.
     */
    private void getAllIncidencias() {
        for (Incidencia i : daoInterface.selectAllIncidencias()) {
            System.out.println(i);
        }
    }

    /**
     * Función para mostrar el ranking de empleados.
     */
    private void getRankingEmpleados() {
        List<RankingTO> rankingEmpleados = daoInterface.getRankingEmpleados();
        if (rankingEmpleados.isEmpty()) {
            System.out.println("No employees");
        }
        for (RankingTO to : rankingEmpleados) {
            System.out.println(to);
        }
    }

    /**
     * Función para mostrar las incidencias por origen.
     *
     * @param e
     */
    private void getIncidentByOrigen(Empleado e) {
        for (Incidencia i : daoInterface.getIncidenciaByOrigen(e)) {
            System.out.println(i);
        }
    }

    private void getIncidentByDestino(Empleado e) {
        for (Incidencia i : daoInterface.getIncidenciaByDestino(e)) {
            System.out.println(i);
        }
    }

    /**
     * Función para mostrar el último inició de sesión, se le pide por pantalla
     * que seleccione un empleado.
     */
    private void getLastSession() {
        Evento evento = null;
        System.out.println("Get last session");
        List<Empleado> empleados = daoInterface.findEmpleados();
        if (!empleados.isEmpty()) {
            for (Empleado e : empleados) {
                System.out.println(e.toString());
            }
            String user = InputAsker.askString("Select user (username)");
            Empleado e = null;
            for (Empleado empleado : empleados) {
                if (empleado.getUsuario().equals(user)) {
                    e = empleado;
                }
            }
            if (e == null) {
                System.out.println("No user exists with that username");
            } else {
                evento = daoInterface.getUltimoInicioSesion(e);
            }
            if (evento == null) {
                System.out.println("No events form that user");
            } else {
                System.out.println(evento);
            }
        } else {
            System.out.println("No users registered");
        }
    }
}
