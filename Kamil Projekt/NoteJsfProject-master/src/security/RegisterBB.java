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

import note.dao.RoleDAO;
import note.dao.UserDAO;
import note.entities.User;

@ManagedBean
public class RegisterBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String pass;
	private String pass2;
	private static final Logger log = Logger.getLogger(RegisterBB.class);
	
	@EJB
	UserDAO userDAO;
	@EJB
	RoleDAO roleDAO;

	public String getPass2() {
		return pass2;
	}

	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}

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
		} else {		
			if (!pass.equals(pass2)) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						Common.getKeyFromBundle("loginPage", "diffPass"), "null"));
			}	
		}

		if (ctx.getMessageList().isEmpty()) {
			result = true;
		} else {
			result = false;
		}
		return result;

	}

	public String doRegister() throws NoSuchAlgorithmException {
		FacesContext ctx = FacesContext.getCurrentInstance();		
		NoteUser user = null;

		if (!validateData()) {
			return PAGE_STAY_AT_THE_SAME;
		}
		
		user = getUserFromDatabase(login, pass);
		
		if (user != null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Common.getKeyFromBundle("loginPage", "userExists"), null));
			return PAGE_STAY_AT_THE_SAME;
		}		
		
		User userToSave = prepareUser();
		userDAO.create(userToSave);
		
		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				Common.getKeyFromBundle("loginPage", "registerSuccess"), null));
		return PAGE_STAY_AT_THE_SAME;
	}
	
	private User prepareUser() throws NoSuchAlgorithmException {
		User userToSave = new User();
		userToSave.setIsblocked((byte)0);
		userToSave.setName(login);
		userToSave.setPassword(getMd5(pass));
		userToSave.setRole(roleDAO.getRole("user"));
		return userToSave;
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

	public NoteUser getUser() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return (NoteUser) session.getAttribute("user");
	}
	
	private NoteUser getUserFromDatabase(String login, String pass) {
		NoteUser u = null;
		
		List<User> fullList = userDAO.getFullList();
		
		for(User user : fullList) {			
			if(user.getName().equals(login)){
				u = new NoteUser(login,pass);
				u.setIsblocked(user.getIsblocked());
				u.setUserid(user.getUserid());
				u.setSurname(user.getSurname());
				u.setRoleBean(user.getRole());
			}
		}
		return u;
	}

}
