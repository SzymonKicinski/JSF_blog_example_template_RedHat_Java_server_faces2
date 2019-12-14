package security;

import note.entities.Role;

public class NoteUser {
	
	private int userid;
	private byte isblocked;
	private String name;
	private String password;
	private String surname;
	private Role roleBean;
	private String remoteAdr;
	private String remoteHost;
	private String remotePort;
	
	public NoteUser(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public byte getIsblocked() {
		return isblocked;
	}

	public void setIsblocked(byte isblocked) {
		this.isblocked = isblocked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Role getRoleBean() {
		return roleBean;
	}

	public void setRoleBean(Role roleBean) {
		this.roleBean = roleBean;
	}

	public String getRemoteAdr() {
		return remoteAdr;
	}

	public void setRemoteAdr(String remoteAdr) {
		this.remoteAdr = remoteAdr;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}	
		
}
