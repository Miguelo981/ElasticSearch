/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controller.ManagerDao;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.enums.Evento;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 *
 * @author Eric Ribas;
 */
public class DAOInterfaceImpl implements DAOInterface {

    private static DAOInterfaceImpl daoInterfaceImpl = null;
    private static ManagerDao managerDao;

    private DAOInterfaceImpl() {
        managerDao = ManagerDao.getManagerDao();
    }

    public static DAOInterfaceImpl getInstance() {
        if (daoInterfaceImpl == null) {
            daoInterfaceImpl = new DAOInterfaceImpl();
        }
        return daoInterfaceImpl;
    }

    @Override
    public void insertEmpleado(Empleado e) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", e.getUsuario());
        jsonMap.put("name", e.getNombre());
        jsonMap.put("pass", e.getPassword());
        jsonMap.put("surname", e.getApellidos());
        jsonMap.put("phone", e.getTelefono());
        jsonMap.put("dni", e.getDni());
        int id = getID();
        IndexRequest indexRequest = new IndexRequest("users").id(String.valueOf(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
        if (managerDao.index(indexRequest)) {
            System.out.println("Usuario " + e.getUsuario() + " creado con exito!");
        }
    }

    public int getID() {
        int id = 0;
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            searchRequest.source(sourceBuilder);
            id = managerDao.getTryEmpleado(searchRequest);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return id + 1;
    }

    @Override
    public Empleado loginEmpleado(String user, String pass) {
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("user", user));
            sourceBuilder.query(QueryBuilders.termQuery("pass", pass)); //Funciona?
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("users");
            searchRequest.source(sourceBuilder);
            Empleado e = managerDao.getEmpleado(searchRequest);
            return e;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public void updateEmpleado(Empleado e) {
        managerDao.update(e);
    }

    @Override
    public void removeEmpleado(Empleado e) {
        managerDao.delete(e);
    }

    @Override
    public Incidencia getIncidenciaById(int id) {
        return managerDao.getIncidentByID(id);
    }

    @Override
    public List<Incidencia> selectAllIncidencias() {
        return managerDao.getAllIncidents();
    }

    @Override
    public void insertIncidencia(Incidencia i) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("date", i.getFecha());
        jsonMap.put("origin", i.getOrgien());
        jsonMap.put("destination", i.getDestino());
        jsonMap.put("detail", i.getDetalle());
        jsonMap.put("type", i.getTipo().name());
        int id = 0;
        IndexRequest indexRequest = new IndexRequest("incidents").id(Integer.toString(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
        if (managerDao.index(indexRequest)) {
            System.out.println("Incidencia de tipo " + i.getTipo().name() + " creada con exito!");
        }
    }

    @Override
    public List<Incidencia> getIncidenciaByDestino(Empleado e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Incidencia> getIncidenciaByOrigen(Empleado e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertarEvento(Evento e) {
        HashMap<String, Object> jsonMap = new HashMap<>();
        /**
         * jsonMap.put("type", i.getFecha()); jsonMap.put("date",
         * i.getOrgien()); jsonMap.put("employe", i.getDestino());
         */
        int id = 0;
        IndexRequest indexRequest = new IndexRequest("events").id(Integer.toString(id)).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
        if (managerDao.index(indexRequest)) {
            System.out.println("Evento de tipo " + e.name() + " creado con exito!");
        }
    }

    @Override
    public Evento getUltimoInicioSesion(Empleado e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<RankingTO> getRankingEmpleados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        managerDao.close();
    }
}
