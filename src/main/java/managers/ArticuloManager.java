package managers;

import org.example.Articulo;
import org.example.Cliente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticuloManager {
    EntityManagerFactory emf = null;
    EntityManager em = null;

    public ArticuloManager(boolean anularShowSQL) {
        Map<String, Object> properties = new HashMap<>();
        if(anularShowSQL){
            // Desactivar el show_sql (si está activado en el persistence.xml o configuración por defecto)
            properties.put("hibernate.show_sql", "false");
        }else{
            properties.put("hibernate.show_sql", "true");
        }
        emf = Persistence.createEntityManagerFactory("example-unit", properties);
        em = emf.createEntityManager();

    }

    public List<Articulo> getArticulosXDenominacion(String denominacion){
        String jpql = "FROM Articulo WHERE denominacion LIKE :denominacion ORDER BY denominacion";
        Query query = em.createQuery(jpql);
        query.setParameter("denominacion",'%'+denominacion+'%');
        List<Articulo> articulos = query.getResultList();
        return articulos;
    }
}
