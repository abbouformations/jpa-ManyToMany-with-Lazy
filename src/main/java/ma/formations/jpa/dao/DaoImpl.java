package ma.formations.jpa.dao;

import ma.formations.jpa.dtos.PersonneDto;
import ma.formations.jpa.model.Personne;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

public class DaoImpl implements IDao {
    private static final Logger log = LogManager.getLogger(DaoImpl.class);
    private EntityManager session;
    private EntityTransaction tx;

    @Override
    public <T> void save(T t) {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            tx = session.getTransaction();
            tx.begin();
            session.merge(t);
            tx.commit();
            log.info( "{} ajouté avec succés",t);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("erreur dans save()", e);
            if (tx != null)
                tx.rollback();

        } finally {
            if (session != null)
            session.close();

        }
    }

    @Override
    public List<PersonneDto> getPersonnesByActivite(String description) {
        List<Personne> result = null;
        ModelMapper modelMapper=new ModelMapper();
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            Query q=session.createQuery("select p from Personne p join p.activites a where a.description =:description");
            q.setParameter("description", description);
            result=q.getResultList();
            log.info("getPersonnesByActivite({}) OK",description);
            if (result == null || result.isEmpty())
                return null;
            return result.stream().map(bo->modelMapper.map(bo, PersonneDto.class)).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("erreur dans getPersonnesByActivite()", e);
            return null;
        } finally {
            session.close();
            if (session != null)
                session.close();
        }
    }

    @Override
    public void remove(String firstname,String lastname) {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            tx = session.getTransaction();
            tx.begin();
            Query query = session.createQuery("select p from Personne p where p.firstname=:firstname and p.lastname=:lastname");
            query.setParameter("firstname",firstname);
            query.setParameter("lastname",lastname);
            List personnesFound = query.getResultList();
            if (personnesFound == null || personnesFound.isEmpty())
                return;
            personnesFound.forEach(p->session.remove(p));
            tx.commit();
            log.info("Personne [{},{}] supprimée avec succés",firstname,lastname);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("erreur dans remove()", e);
            if (tx != null)
                tx.rollback();
        } finally {
            if (session != null)
                session.close();

        }
    }
}
