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

    public boolean checkUser(Empleado e) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("user", e.getUsuario()));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        searchRequest.source(sourceBuilder);
        return managerDao.checkUserExists(searchRequest);
    }

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

    @Override
    public void removeEmpleado(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("users");
        managerDao.delete(searchRequest, e.getUsuario());
    }

    @Override
    public Incidencia getIncidenciaById(int id) {
        return managerDao.getIncidentByID(id);
    }

    @Override
    public List<Incidencia> selectAllIncidencias() {
        return managerDao.getAllIncidents();
    }

    public int getIncidentID() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getID(searchRequest);
    }

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

    @Override
    public List<Incidencia> getIncidenciaByDestino(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getIncidentsByDestination(searchRequest, e.getUsuario());
    }

    @Override
    public List<Incidencia> getIncidenciaByOrigen(Empleado e) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("incidents");
        return managerDao.getIncidentsByOrigin(searchRequest, e.getUsuario());
    }

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

    @Override
    public void close() {
        managerDao.close();
    }

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
