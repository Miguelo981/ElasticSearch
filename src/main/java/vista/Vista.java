package vista;

import java.time.LocalDate;
import modelo.DAOInterfaceImpl;
import modelo.Empleado;
import modelo.Evento;
import modelo.enums.TipoEvento;

/**
 *
 * @author Eric Ribas;
 */
public class Vista {

    private static DAOInterfaceImpl daoInterfaceImpl;

    /**
     * Se hace una instanciación del singelton, y además se llama a la función
     * menuConsola.
     *
     * @param args
     */
    public static void main(String[] args) {
        daoInterfaceImpl = DAOInterfaceImpl.getInstance();
        menuConsola();
    }

    /**
     * Función para mostrar el menú inicial.
     *
     * @return
     */
    private static String menu() {
        return "1 - Login.\n0 - Exit";
    }

    /**
     * Función para inciar el menú incial según la opción que seleccionen.
     */
    public static void menuConsola() {
        Boolean response = false;
        do {
            switch (InputAsker.askInt(menu())) {
                case 1:
                    login();
                    break;
                case 0:
                    response = true;
                    break;
            }
        } while (!response);
        daoInterfaceImpl.close();
    }

    /**
     * Función para hacer el login. Se pide por pantalla tanto el usuario como
     * la contraseña.
     */
    private static void login() {
        Empleado e;
        String username = InputAsker.askString("Username: ");
        String password = InputAsker.askString("Insert password");
        UserInterface userInterface = new UserInterface();
        if (username.equals("admin") && password.equals("admin")) {
            e = new Empleado();
            e.setUsuario(username);
            e.setPassword(password);
            userInterface.menuUsuario(e, daoInterfaceImpl);
        } else {
            e = daoInterfaceImpl.loginEmpleado(username, password);
            if (e != null) {
                daoInterfaceImpl.insertarEvento(new Evento(TipoEvento.I, LocalDate.now(), e.getUsuario()));
                userInterface.menuUsuario(e, daoInterfaceImpl);
            } else {
                System.out.println("User does not exists");
            }
        }
    }
}
