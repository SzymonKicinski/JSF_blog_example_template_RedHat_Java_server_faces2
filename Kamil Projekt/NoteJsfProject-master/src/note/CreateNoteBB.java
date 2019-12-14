package note;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import common.Common;
import common.NavigationController;
import note.dao.NoteDAO;
import note.dao.StatusDAO;
import note.dao.UserDAO;
import note.entities.Note;
import note.entities.Status;
import note.entities.User;
import security.NoteUser;

@ManagedBean
public class CreateNoteBB {
	
	private String title;
	private String description;
	private int assigneId;
	
	@EJB
	UserDAO userDAO;
	@EJB
	StatusDAO statusDAO;
	@EJB
	NoteDAO noteDAO;
	
	private static final Logger log = Logger.getLogger(CreateNoteBB.class);
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getAssigneId() {
		return assigneId;
	}
	public void setAssigneId(int assigneId) {
		this.assigneId = assigneId;
	}
	public String createNote(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		
		if(!validate()) {
			return null;
		}
		
		Note note = new Note();
		note.setAssignee(userDAO.find(assigneId));
		note.setDescription(description);
		note.setReaded((byte) 0);		
		note.setReporter(getUser());
		note.setStatus(statusDAO.getStatus("Created"));
		note.setTitle(title);	
		
		noteDAO.create(note);
	
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("mainPage", "successAdd"), "null"));		
		
		return null;
	}
	
	private boolean validate() {
		boolean result = true;
		FacesContext ctx = FacesContext.getCurrentInstance();
		 
		if (title == null || title.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("mainPage", "emptyNoteTitle"), "null"));
		}

		if (description == null || description.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("mainPage", "emptyNoteDesc"), "null"));
		}

		if (ctx.getMessageList().isEmpty()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	public User getUser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return (User) session.getAttribute("userOrg");
	}
	
	public Status getStatus() {
		List<Status> statusesList = statusDAO.getFullList();
		return statusesList.stream().filter(status -> status.getStatus().equals("Created")).findFirst().get();		
	}
	
	public List<User> usersAsMap(){
		List<User> fullList = userDAO.getFullList();
		return fullList;
	}
	
	

}
