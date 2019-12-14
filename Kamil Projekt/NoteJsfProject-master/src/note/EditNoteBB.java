package note;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import common.Common;

import note.dao.NoteDAO;
import note.dao.StatusDAO;
import note.dao.UserDAO;
import note.entities.Note;
import note.entities.Status;
import note.entities.User;


@ManagedBean
public class EditNoteBB {
	
	private String title;
	private String description;
	private int assigneId;
	private int statusId;
	
	@EJB
	UserDAO userDAO;
	@EJB
	StatusDAO statusDAO;
	@EJB
	NoteDAO noteDAO;
	
		private Note note= null;

		@PostConstruct
		public void postConstruct() {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(true);
			note = (Note) session.getAttribute("note");

			if (note != null && (Integer)note.getNoteid() != null) {
				setTitle(note.getTitle());
				setDescription(note.getDescription());
				setAssigneId(note.getAssignee().getUserid());				
			}
		}
	
	private static final Logger log = Logger.getLogger(EditNoteBB.class);
	
		
	
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}


	public int getStatusId() {
		return statusId;
	}


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
	
	public List<Status> statusAsList(){
		List<Status> fullList = statusDAO.getFullList();
		return fullList;
	}
	
	public String saveData() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		
		if(!validate()) {
			return null;
		}
		note = (Note) session.getAttribute("note");
	
		if (note != null) {
			session.removeAttribute("note");
		}
		
		note.setDescription(description);
		note.setTitle(title);
		note.setAssignee(userDAO.find(assigneId));
		note.setStatus(statusDAO.find(statusId));
		
		noteDAO.merge(note);		
		
		return "dashboardNote?faces-redirect=true";
	}
	
	

}
