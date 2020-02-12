package vista;

import controller.ManagerDao;

/**
 *
 * @author Eric Ribas;
 */
public class Vista {
    
    public static void main(String[] args) throws Exception{
        ManagerDao mD = new ManagerDao();
        mD.index();
        String get = mD.get();
        System.out.println(get);
        mD.update();
        get = mD.get();
        System.out.println(get);
        mD.close();
    }
}
