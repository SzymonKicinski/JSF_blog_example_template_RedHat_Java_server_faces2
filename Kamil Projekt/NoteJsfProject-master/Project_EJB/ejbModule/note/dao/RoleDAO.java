package note.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import note.entities.Role;

//DAO - Data Access Object for Role entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.
@Named
@Stateless
public class RoleDAO {
	private final static String UNIT_NAME = "note-PU";

	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Role role) {
		em.persist(role);
		// em.flush();
	}

	public Role merge(Role role) {
		return em.merge(role);
	}

	public void remove(Role role) {
		em.remove(em.merge(role));
	}

	public Role find(Object id) {
		return em.find(Role.class, id);
	}

	public List<Role> getFullList() {
		List<Role> list = null;

		Query query = em.createQuery("select p from Role p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Role> getList(Map<String, Object> searchParams) {
		List<Role> list = null;

		// 1. Build query string with parameters
		String select = "select p ";
		String from = "from Role p ";
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

		// 4. Execute query and retrieve list of Role objects
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public Role getRole(String role) {
		List<Role> list = null;

		Query query = em.createQuery("select r from Role r where r.role = :role ");
		
		if (role != null) {
			query.setParameter("role", role);
		}
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list.get(0);
	}

}
