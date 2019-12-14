package commonConsole;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.context.RequestContext;

import note.dao.NoteDAO;
import note.dao.RoleDAO;
import note.dao.StatusDAO;
import note.dao.UserDAO;
import note.entities.Note;
import note.entities.Role;
import note.entities.Status;
import note.entities.User;

@ManagedBean
public class NoteManagementBB {
	
	@EJB
	NoteDAO noteDAO;
	@EJB
	StatusDAO statusDAO;
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;
	
	private int role;	

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	Logger log = Logger.getLogger(NoteManagementBB.class);
	
	public List<User> usersAsList(){
		List<User> fullList = userDAO.getFullList();
		return fullList;
	}
	
	public List<Note> notesAsList(){
		List<Note> fullList = noteDAO.getFullList();
		return fullList;
	}
	
	public List<Status> statusAsList(){
		List<Status> fullList = statusDAO.getFullList();
		return fullList;
	}
	
	public List<Role> roleAsList(){
		List<Role> fullList = roleDAO.getFullList();
		return fullList;
	}
	
	public String editNote(Note note) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("note", note);
		log.info(session.getAttribute("note"));
		return "/pages/console/noteEditConsole.xhtml";
	}
		
}
