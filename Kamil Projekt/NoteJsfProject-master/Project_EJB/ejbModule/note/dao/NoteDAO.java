package note.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import note.entities.Note;

//DAO - Data Access Object for Note entity
//Designed to serve as an interface between higher layers of application and data.
//Implemented as stateless Enterprise Java bean - server side code that can be invoked even remotely.
@Named
@Stateless
public class NoteDAO {
	private final static String UNIT_NAME = "note-PU";
	Logger log = Logger.getLogger(NoteDAO.class);
	// Dependency injection (no setter method is needed)
	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Note note) {
		em.persist(note);
		// em.flush();
	}

	public Note merge(Note note) {
		return em.merge(note);
	}

	public void remove(Note note) {
		em.remove(em.merge(note));
	}

	public Note find(Object id) {
		return em.find(Note.class, id);
	}

	public List<Note> getFullList() {
		List<Note> list = null;

		Query query = em.createQuery("select p from Note p");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Note> getList(Map<String, Object> searchParams) {
		List<Note> list = null;
		
		String select = "select p ";
		String from = "from Note p ";
		String where = "";
		String orderby = "order by p.title asc";		
		
		String status = (String) searchParams.get("status");		
		if (status != null) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.status.statusid = :status ";
		}
		
		String search = (String) searchParams.get("search");		
		if (search != null && !search.equals("")) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.title like :search ";
		}
		
		String reporterIdFilter= (String) searchParams.get("reporterIdFilter") == null ? "-1" : (String) searchParams.get("reporterIdFilter");
		if (!reporterIdFilter.equals("-1")) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.reporter.userid = :reporter ";
		}

		String assigneeIdFilter= (String) searchParams.get("assigneeIdFilter") == null ? "-1" : (String) searchParams.get("assigneeIdFilter");
		if (!assigneeIdFilter.equals("-1")) {
			if (where.isEmpty()) {
				where = "where ";
			} else {
				where += "and ";
			}
			where += "p.assignee.userid = :assignee ";
		}

		Query query = em.createQuery(select + from + where + orderby);
		
		if (!reporterIdFilter.equals("-1")) {
			query.setParameter("reporter", Integer.parseInt(reporterIdFilter));
		}
		if (status != null) {
			query.setParameter("status", Integer.parseInt(status));
		}		
		if (search != null && !search.equals("")) {
			query.setParameter("search", "%"+search+"%");
		}
		if (!assigneeIdFilter.equals("-1")) {
			query.setParameter("assignee", Integer.parseInt(assigneeIdFilter));
		}
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public Note getNote(String note) {
		List<Note> list = null;

		Query query = em.createQuery("select r from Note r where r.note = :note ");
		
		if (note != null) {
			query.setParameter("note", note);
		}
		
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list.get(0);
	}

}
