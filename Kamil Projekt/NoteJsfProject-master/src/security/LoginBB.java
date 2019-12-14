package security;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import common.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import note.CreateNoteBB;
import note.dao.RoleDAO;
import note.dao.UserDAO;
import note.entities.User;


@ManagedBean
public class LoginBB {
	private static final String PAGE_MAIN = "/pages/note/note";
	private static final String PAGE_LOGIN = "/pages/login";
	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final Logger log = Logger.getLogger(LoginBB.class);

	private String login;
	private String pass;
	
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public boolean validateData() {
		boolean result = true;
		FacesContext ctx = FacesContext.getCurrentInstance();
		 
		if (login == null || login.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("loginPage", "emptyLogin"), "null"));
		}

		if (pass == null || pass.length() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("loginPage", "emptyPass"), "null"));
		}

		if (ctx.getMessageList().isEmpty()) {
			result = true;
		} else {
			result = false;
		}
		return result;

	}

	public String doLogin() throws NoSuchAlgorithmException {		
		FacesContext ctx = FacesContext.getCurrentInstance();
		NoteUser user = null;

		if (!validateData()) {
			return PAGE_STAY_AT_THE_SAME;
		}
		
		user = getUserFromDatabase(login, pass);
		
		if (user == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("loginPage", "badCredentials"), null));
			return PAGE_STAY_AT_THE_SAME;
		}
		
		if(user.getIsblocked() == 1) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("loginPage", "ban"), null));
			return PAGE_STAY_AT_THE_SAME;
		}

		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		session.setAttribute("user", user);		
 
		return PAGE_MAIN;
	}

	public NoteUser getUser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return (NoteUser) session.getAttribute("user");
	}
	
	public String doLogout(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}
	
	private NoteUser getUserFromDatabase(String login, String pass) throws NoSuchAlgorithmException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) ctx.getExternalContext()
				.getSession(true);
		NoteUser u = null;
		
		List<User> fullList = userDAO.getFullList();
		
		for(User user : fullList) {
			if(user.getName().equals(login) && user.getPassword().equals(getMd5(pass))){
				u = new NoteUser(login,pass);
				u.setIsblocked(user.getIsblocked());
				u.setUserid(user.getUserid());
				u.setSurname(user.getSurname());
				u.setRoleBean(user.getRole());
				session.setAttribute("userOrg", user);
			}
		}
		return u;
	}
	
	private String getMd5(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}

}
