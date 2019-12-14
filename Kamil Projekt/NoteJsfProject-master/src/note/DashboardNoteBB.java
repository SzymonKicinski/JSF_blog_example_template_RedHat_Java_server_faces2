package note;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import note.dao.NoteDAO;
import note.dao.StatusDAO;
import note.dao.UserDAO;
import note.entities.Note;
import note.entities.Status;
import note.entities.User;

@ManagedBean
public class DashboardNoteBB {
	
	@EJB
	NoteDAO noteDAO;
	@EJB
	StatusDAO statusDAO;
	@EJB
	UserDAO userDAO;
	
	Logger log = Logger.getLogger(DashboardNoteBB.class);
	
	private int assigneIdFilter = -1;
	private int reporterIdFilter = -1;
	private String search = null;	
	private Status status;
		
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getAssigneIdFilter() {
		return assigneIdFilter;
	}
	
	public void setAssigneIdFilter(int assigneIdFilter) {
		this.assigneIdFilter = assigneIdFilter;
	}

	public int getReporterIdFilter() {
		return reporterIdFilter;
	}

	public void setReporterIdFilter(int reporterIdFilter) {
		this.reporterIdFilter = reporterIdFilter;
	}

	public List<Note> getList(String status){
		List<Note> noteList = null;
		Map<String,Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("status", Integer.toString(statusDAO.getStatus(status).getStatusid()));	
		
		if (new Integer(assigneIdFilter) != -1){
			searchParams.put("assigneeIdFilter", Integer.toString(assigneIdFilter));
		}
		
		if (new Integer(reporterIdFilter) != -1){
			searchParams.put("reporterIdFilter", Integer.toString(reporterIdFilter));
		}
		
		if (search != null && !search.equals("")){
			searchParams.put("search", search);
		}
		
		noteList = noteDAO.getList(searchParams);		
				
		return noteList;		
	}
	
	public List<User> usersAsList(){
		List<User> fullList = userDAO.getFullList();
		return fullList;
	}
	
	public List<Status> statusAsList(){
		List<Status> fullList = statusDAO.getFullList();
		return fullList;
	}
	
	public void filter() {
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("templatemo_wrapper:created");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("templatemo_wrapper:in_progress");
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("templatemo_wrapper:done");
	}
	
	public String editNote(Note note) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("note", note);
		return "noteEdit?faces-redirect=true";
	}
	
	public String clearFilter() {
		assigneIdFilter = -1;
		reporterIdFilter = -1;
		search = null;
		filter();
		return null;
	}	
	
	public String deleteNote(Note note) {
		noteDAO.remove(note);
		return null;
	}
}
