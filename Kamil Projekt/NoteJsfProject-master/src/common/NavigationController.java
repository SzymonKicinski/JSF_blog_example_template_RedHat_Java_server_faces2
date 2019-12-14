package common;

import java.io.Serializable;
import org.jboss.logging.Logger;

import note.entities.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "navigationController", eager = true)
@RequestScoped

public class NavigationController implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(NavigationController.class);
	   @ManagedProperty(value = "#{param.pageId}")
	   private String pageId;

	public String showPage() {
	      if(pageId == null) {
	         return "note.xhtml";
	      }
	      
	      if(pageId.equals("home")) {
	         return "note.xhtml";
	      }
	      if(pageId.equals("addNote")) {
	         return "addNote.xhtml";
	      }
	      if(pageId.equals("dashboardNote")){
	         return "dashboardNote.xhtml";
	      }
	      if(pageId.equals("adminConsole")){
	    	FacesContext ctx = FacesContext.getCurrentInstance();
	  		HttpSession session = (HttpSession) ctx.getExternalContext()
	  				.getSession(true);
	  		User user = (User) session.getAttribute("userOrg");
	  		if(!user.getRole().getRole().equals("admin")) {
	  			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						Common.getKeyFromBundle("mainPage", "notPermitted"), "null"));
	  			return null;
	  		}
		         return "/pages/console/administrationConsoleHome.xhtml";
		  }
	    	 return "note.xhtml";
	      
	}
	

	   
	   public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
}