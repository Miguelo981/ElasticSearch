/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controller.BBDDManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.enums.Tipo;
import modelo.enums.TipoEvento;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 *
 * @author Eric Ribas;
 */
public class DAOInterfaceImpl implements DAOInterface {

    private static DAOInterfaceImpl daoInterfaceImpl = null;
    private static BBDDManager managerDao;

    private DAOInterfaceImpl() {
        managerDao = BBDDManager.getManagerDao();
    }

    public static DAOInterfaceImpl getInstance() {
        if (daoInterfaceImpl == null) {
            daoInterfaceImpl = new DAOInterfaceImpl();
        }
        return daoInterfaceImpl;
    }

    /**
     * Función para insertar un empleado. Por parametro se pasa un objeto de
     * tipo Empelado. En primer lugar, se reailiza la petición, se comprueba que
     * no exista un usuario igual y finalmente se llama al managerDAO. Se hace
     * el mapeo del objeto. Finalmente, si todo ha ido correcto, significara que
     * el empleado ha sido insertado correctamente a la BBDD.
     *
     * @param e
     */
    @Override
    public void insertEmpleado(Empleado e) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("user", e.getUsuario()));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        searchRequest.source(sourceBuilder);
        if (!new GetIndexRequest("users").local()) {
            new GetIndexRequest("users").local();
        }
        if (!managerDao.checkUserExists(searchRequest)) {
            HashMap<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("user", e.getUsuario());
            jsonMap.put("name", e.getNombre());
            jsonMap.put("pass", e.getPassword());
            jsonMap.put("surname", e.getApellidos());
            jsonMap.put("phone", e.getTelefono());
            jsonMap.put("dni", e.getDni());
            int id;
            id = getEmployeeID();
            IndexRequest indexRequest = new IndexRequest("users").id(String.valueOf(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
            if (managerDao.index(indexRequest)) {
                System.out.println("User " + e.getUsuario() + " created successfully");
            }
        } else {
            System.out.println("User already exists.");
        }

    }

    /**
     * Función para comprobar que no existan dos usuarios iguales. Se pasa por
     * parametro un objeto de tipo Empleado.
     *
     * @param e
     * @return
     */
    public boolean checkUser(Empleado e) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("user", e.getUsuario()));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        searchRequest.source(sourceBuilder);
        return managerDao.checkUserExists(searchRequest);
    }

    /**
     * Función para obtener la id de los empleados.
     *
     * @return
     */
    public int getEmployeeID() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        return managerDao.getID(searchRequest);
    }

    public int getIDEvento() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("events");
        searchRequest.source(sourceBuilder);
        return managerDao.getID(searchRequest);
    }

    /**
     * Función para hacer el login, por parametro se pasa tanto el usuario como
     * la contraseña, ambos de tipo String. Se realiza la petición, cuyo índice
     * es 'users'. Recogemos el resultado de la petición y comprobamos que
     * concida tanto el usuario como la password. Si es así, significa que los
     * credenciales proporiciondos son correctos y devolveremos un objeto de
     * tipo Empleado.
     *
     * @param user
     * @param pass
     * @return
     */
    @Override
    public Empleado loginEmpleado(String user, String pass) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("user", user));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            searchRequest.source(sourceBuilder);
            Empleado e = managerDao.getEmpleado(searchRequest);
            if (e.getPassword().equals(pass)) {
                return e;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Función para actualizar los datos del empleado, se pasa por parametro el
     * empleado. Hacemos un mapeado de los datos y llamamos la función del
     * 'updateEmpleado' del managerDAO.
     *
     * @param e
     */
    @Override
    public void updateEmpleado(Empleado e) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", e.getUsuario());
        jsonMap.put("name", e.getNombre());
        jsonMap.put("pass", e.getPassword());
        jsonMap.put("surname", e.getApellidos());
        jsonMap.put("phone", e.getTelefono());
        jsonMap.put("dni", e.getDni());

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        managerDao.updateEmpleado(searchRequest, jsonMap);
    }

    /**
     * Función para eliminar un empleado, se pasa por parametro un objeto de
     * tipo empleado. Se realiza una petición cuyo índice es 'users'.
     * Seguidamente se llama la función 'delete' de managerDAO. Pasamos tanto la
     * petición como el nombre de usuario del empleado.
     *
     * @param e
     */
    @Override
    public void removeEmpleado(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        managerDao.delete(searchRequest, e.getUsuario());
    }

    /**
     * Función para devolver una incidencia. Pasamos por parametro la id.
     * Finalmente, devolvemos el resultado tras llamar la función
     * 'getIncidentByID' del managerDAO.
     *
     * @param id
     * @return
     */
    @Override
    public Incidencia getIncidenciaById(int id) {
        return managerDao.getIncidentByID(id);
    }

    /**
     * Función para devolver el listado de incidencias. Devolvemos el resultado
     * tras llamar la función 'selectAllIncidencias' del managerDAO.
     *
     * @return
     */
    @Override
    public List<Incidencia> selectAllIncidencias() {
        return managerDao.getAllIncidents();
    }

    /**
     * Función para devolver la ID de las incidencias. Devolvemos el resultado
     * tras llamar la función 'getID' del managerDAO, le pasamos por parametro
     * la petición a la BBDD.
     *
     * @return
     */
    public int getIncidentID() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getID(searchRequest);
    }

    /**
     * Función para insertar una incidencia. Por parametro se pasa un objeto de
     * tipo Incidencia. En primer lugar, se reailiza la petición, se comprueba
     * que exista el usuario y finalmente se llama al managerDAO. Se hace el
     * mapeo del objeto. Si la incidencia es de tipo 'I', entonces insertaremos
     * un evento con la fecha actual. Finalmente, si todo ha ido correctamente,
     * significara que la incidencia ha sido insertada satisfacoriamente a la
     * BBDD.
     *
     * @param i
     */
    @Override
    public void insertIncidencia(Incidencia i) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("user", i.getDestino()));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        searchRequest.source(sourceBuilder);
        if (managerDao.checkUserExists(searchRequest)) {
            if (!i.getOrigen().equals(i.getDestino())) {
                HashMap<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("date", i.getFecha());
                jsonMap.put("origin", i.getOrigen());
                jsonMap.put("destination", i.getDestino());
                jsonMap.put("detail", i.getDetalle());
                jsonMap.put("type", i.getTipo().name());
                int id;
                id = getIncidentID();
                IndexRequest indexRequest = new IndexRequest("incidents").id(Integer.toString(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
                if (managerDao.index(indexRequest)) {
                    System.out.println("Incidencia de tipo " + i.getTipo().name() + " creada con exito!");
                    if (i.getTipo().equals(Tipo.URGENTE)) {
                        insertarEvento(new Evento(TipoEvento.U, LocalDate.now(), i.getOrigen()));
                    }
                }
            } else {
                System.out.println("No puede ser el mismo usuario de origen ");
            }
        } else {
            System.out.println("Usuario de destino no existe");
        }
    }

    /**
     * Función para devolver el listado de incidencias por destino. Pasamos por
     * parametro el empleado. Seguidamente hacemos la petición. Finalmente,
     * devolvemos el resultado tras llamar a la función
     * 'getIncidentsByDestination' de managerDAO, la cúal hemos pasado la
     * petición y el nombre de usuario del empleado.
     *
     * @param e
     * @return
     */
    @Override
    public List<Incidencia> getIncidenciaByDestino(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getIncidentsByDestination(searchRequest, e.getUsuario());
    }

    /**
     * Función para devolver el listado de incidencias por origen. Pasamos por
     * parametro el empleado. Seguidamente hacemos la petición. Finalmente,
     * devolvemos el resultado tras llamar a la función 'getIncidentsByOrigin'
     * de managerDAO, la cúal hemos pasado la petición y el nombre de usuario
     * del empleado.
     *
     * @param e
     * @return
     */
    @Override
    public List<Incidencia> getIncidenciaByOrigen(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getIncidentsByOrigin(searchRequest, e.getUsuario());
    }

    /**
     * Función para insertar un eveneto. Por parametro se pasa un objeto de tipo
     * Evento. Se reailiza la petición, seguidamente se llama al managerDAO. Se
     * hace el mapeo del objeto. Finalmente, si todo ha ido correctamente,
     * significara que el evento ha sido insertada satisfacoriamente a la BBDD.
     *
     * @param e
     */
    @Override
    public void insertarEvento(Evento e) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("type", e.getTipo().name());
        jsonMap.put("date", e.getFecha());
        jsonMap.put("user", e.getUsuario());
        IndexRequest indexRequest = new IndexRequest("events")
                .id(String.valueOf(getIDEvento()))
                .source(jsonMap)
                .opType(DocWriteRequest.OpType.CREATE);
        if (managerDao.index(indexRequest)) {
            System.out.println("Evento de tipo " + e.getTipo() + " creado con exito!");
        } else {
            System.out.println("Error al crear el evento!");
        }
    }

    /**
     * Función para devolver el último inicio de sesisión según el emepleado.
     * Pasamos por parametro un objecto de tipo empleado. Finalmente llamamos a
     * la función 'getLastSessión' del managerDAO y devolvemos un objeto de tipo
     * evento.
     *
     * @param e
     * @return
     */
    @Override
    public Evento getUltimoInicioSesion(Empleado e) {
        Evento evento = managerDao.getLastSession(e);
        return evento;
    }

    @Override
    public List<RankingTO> getRankingEmpleados() {
        List<RankingTO> rankingEmpleados = new ArrayList<>();
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            rankingEmpleados = managerDao.getRanking(searchRequest);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        Collections.sort(rankingEmpleados);
        return rankingEmpleados;
    }

    /**
     * Función para cerrar la conexión con la BBDD.
     */
    @Override
    public void close() {
        managerDao.close();
    }

    /**
     * Función para devolver el listado de empleados. Hacemos la petición cuyo
     * índice es 'users'. Seguidamente, llamamos a la función 'findEmpleados' de
     * managerDAO y pasamos la petición. Finalmente, devolvemos el listado de
     * empleados.
     *
     * @return
     */
    @Override
    public List<Empleado> findEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            empleados = managerDao.findEmpleados(searchRequest);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return empleados;
    }
}
