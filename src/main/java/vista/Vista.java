package vista;

import java.time.LocalDate;
import modelo.DAOInterfaceImpl;
import modelo.Empleado;
import modelo.Evento;
import modelo.Incidencia;
import modelo.enums.Tipo;
import modelo.enums.TipoEvento;

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
        return "1 - Login.\n2 - Register.\n0 - Exit";
    }
    
    public static void menuConsola() {
        Boolean response = false;
        do {
            switch (InputAsker.askInt(menu())) {
                case 1:
                    login();
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
                case 0:
                    response = true;
                    break;
            }
        } while (!response);
        daoInterfaceImpl.close();
    }

    public static void cleanEmpleados() {
        daoInterfaceImpl.getEmployeeID();
    }

    public static void tryCaso() {
        System.out.println(daoInterfaceImpl.getEmployeeID());
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
    
    //TODO INSERT EVENTO
    private static void login() {
        Empleado e = daoInterfaceImpl.loginEmpleado(InputAsker.askString("Username: "), InputAsker.askString("Insert password"));
        if(e!=null){
            daoInterfaceImpl.insertarEvento(new Evento(TipoEvento.I, LocalDate.now(), e.getUsuario()));
            UserInterface userInterface = new UserInterface();
            userInterface.MenuUsuario(e, daoInterfaceImpl);
        }
    }
}
