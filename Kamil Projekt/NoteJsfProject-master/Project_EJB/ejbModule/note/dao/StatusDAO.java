package note.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import note.entities.Status;

//DAO - Data Access Object for Status entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.
@Named
@Stateless
public class StatusDAO {
	private final static String UNIT_NAME = "note-PU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Status status) {
		em.persist(status);
		// em.flush();
	}

	public Status merge(Status status) {
		return em.merge(status);
	}

	public void remove(Status status) {
		em.remove(em.merge(status));
	}

	public Status find(Object id) {
		return em.find(Status.class, id);
	}

	public List<Status> getFullList() {
		List<Status> list = null;

		Query query = em.createQuery("select p from Status p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Status> getList(Map<String, Object> searchParams) {
		List<Status> list = null;

		// 1. Build query string with parameters
		String select = "select p ";
		String from = "from Status p ";
		String where = "";
		String orderby = "order by p.surname asc, p.name";

		// search for surname
		String surname = (String) searchParams.get("surname");
		if (surname != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.surname like :surname ";
		}
		
		// ... other parameters ... 

		// 2. Create query object
		Query query = em.createQuery(select + from + where + orderby);

		// 3. Set configured parameters
		if (surname != null) {
			query.setParameter("surname", surname+"%");
		}

		// ... other parameters ... 

		// 4. Execute query and retrieve list of Status objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public Status getStatus(String status) {
		List<Status> list = null;

		Query query = em.createQuery("select r from Status r where r.status = :status ");
		
		if (status != null) {
			query.setParameter("status", status);
		}
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list.get(0);
	}

}
