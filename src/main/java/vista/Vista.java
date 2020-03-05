package vista;

import controller.ManagerDao;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Empleado;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;

/**
 *
 * @author Eric Ribas;
 */
public class Vista {

    private static ManagerDao managerDao;

    public static void main(String[] args) {
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
        managerDao = ManagerDao.getManagerDao();
        do {
            switch (InputAsker.askInt(menu())) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    response = true;
                    break;
            }
            /*response = hotelManager.managerMenu(answer);
                    if (!response.equals("false") && !response.equals("true")) {
                        System.out.println(Color.messageMaker(Color.getColorByMessage(response.split(" ")[0]), response));
                    }*/
        } while (!response);
    }

    private static void register() {
        HashMap<String, Object> jsonMap = new HashMap<>();
        String user = InputAsker.askString("User name: ");
        jsonMap.put("user", user);
        jsonMap.put("name", InputAsker.askString("Name: "));
        String pass = "", pass2 = "";
        do {
            pass = InputAsker.askString("Insert password: (8 digits maximum)", 8);
            pass2 = InputAsker.askString("Confirm password: ", 8);
            if (!pass.equals(pass2)) {
                System.out.println("Passwords does not match.");
            }
        } while (!pass.equals(pass2));
        jsonMap.put("pass", pass);
        jsonMap.put("surname", InputAsker.askString("Surname: "));
        jsonMap.put("phone", InputAsker.askString("Phone number: ", 8));
        jsonMap.put("dni", InputAsker.askDNI("DNI: "));
        int id = 0;
        IndexRequest indexRequest = new IndexRequest("userss").id(Integer.toString(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
        if (managerDao.index(indexRequest)) {
            System.out.println("Usuario " + user + " creado con exito!");
        }

    }

    private static void login() {
        try {
            managerDao.getEmpleado();
            Empleado empleado = managerDao.getEmpleado(new GetRequest("userss", "0"));
            System.out.print(empleado.toString());
        } catch (Exception ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
