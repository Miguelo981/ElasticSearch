package vista;

import java.time.LocalDate;
import modelo.DAOInterfaceImpl;
import modelo.Empleado;
import modelo.Incidencia;
import modelo.enums.Tipo;

/**
 *
 * @author Eric Ribas;
 */
public class Vista {

    private static DAOInterfaceImpl daoInterfaceImpl;

    public static void main(String[] args) {
        daoInterfaceImpl = DAOInterfaceImpl.getInstance();
        menuConsola();
        // mD.index();
        /*String get = mD.get();
        System.out.println(get);
        mD.updatePosts();
        get = mD.get();
        System.out.println(get);
        mD.close();*/
    }

    private static String menu() {
        return "1.- Login.\n2.- Register.\n0.- Exit";
    }
    
    public static void menuConsola() {
        Boolean response = false;
        do {
            switch (InputAsker.askInt(menu())) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    tryCaso();
                    break;
                case 4:
                    cleanEmpleados();
                    break;
                case 5:
                    updateEmpleado();
                    break;
                case 6:
                    deleteEmpleado();
                    break;
                case 7:
                    insertarIncidencia();
                    break;
                case 8:
                    incidenciaByID();
                    break;
                case 0:
                    response = true;
                    break;
            }
        } while (!response);
        daoInterfaceImpl.close();
    }

    public static void cleanEmpleados() {
        daoInterfaceImpl.getID();
    }

    public static void tryCaso() {
        System.out.println(daoInterfaceImpl.getID());
    }

    private static void register() {
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

    private static void insertarIncidencia() {
        Incidencia i = new Incidencia();
        i.setFecha(LocalDate.now());
        i.setOrgien(InputAsker.askString("Origin: "));
        i.setDestino(InputAsker.askString("Destination: "));
        i.setDetalle(InputAsker.askString("Detail: "));
        i.setTipo(getPlatoType(InputAsker.askString("Type: ")));
        daoInterfaceImpl.insertIncidencia(i);
    }

    private static void incidenciaByID() {
        int id = InputAsker.askInt("ID: ");
        System.out.println(daoInterfaceImpl.getIncidenciaById(id).toString());
    }

    //Moverlo a otro sitio
    public static Tipo getPlatoType(String tipo) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return Tipo.URGENTE;
            case "NORMAL":
                return Tipo.NORMAL;
        }
        return null;
    }

    private static void updateEmpleado() {
        Empleado e = new Empleado();
        System.out.println("============================\n     UPDATE YOUR INFO     \n============================");
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
        System.out.println("============================");
        //Falta pasar por parametro el empleado conectado
        daoInterfaceImpl.updateEmpleado(e);
    }

    private static void deleteEmpleado() {
        Empleado e = new Empleado();
        //Falta pasar por parametro el empleado conectado
        daoInterfaceImpl.removeEmpleado(e);
    }

    private static void login() {
        if (daoInterfaceImpl.loginEmpleado(InputAsker.askString("Username: "), InputAsker.askString("Insert password: ")) != null) {
            
        }
    }
}
