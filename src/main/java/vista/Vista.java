package vista;

import controller.ManagerDao;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Eric Ribas;
 */
public class Vista {
    private static ManagerDao managerDao;
    
    public static void main(String[] args) {
        menuConsola();
       // mD.index();
        String get = mD.get();
        System.out.println(get);
        mD.updatePosts();
        get = mD.get();
        System.out.println(get);
        mD.close();
    }

    public static void menuConsola() {
            Boolean response = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            managerDao = ManagerDao.getManagerDao();
            do {
                try {
                    String answer = br.readLine();
                    /*response = hotelManager.managerMenu(answer);
                    if (!response.equals("false") && !response.equals("true")) {
                        System.out.println(Color.messageMaker(Color.getColorByMessage(response.split(" ")[0]), response));
                    }*/
                } catch (IOException | NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                }
            } while (!response);
        }
}
