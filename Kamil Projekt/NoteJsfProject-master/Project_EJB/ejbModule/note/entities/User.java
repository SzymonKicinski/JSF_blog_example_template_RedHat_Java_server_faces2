package note.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userid;

	private byte isblocked;

	private String name;

	private String password;

	private String surname;

	//bi-directional many-to-one association to Note
	@OneToMany(mappedBy="assignee")
	private List<Note> notes1;

	//bi-directional many-to-one association to Note
	@OneToMany(mappedBy="reporter")
	private List<Note> notes2;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="roles_roleid")
	private Role role;

	public User() {
	}

	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public byte getIsblocked() {
		return this.isblocked;
	}

	public void setIsblocked(byte isblocked) {
		this.isblocked = isblocked;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Note> getNotes1() {
		return this.notes1;
	}

	public void setNotes1(List<Note> notes1) {
		this.notes1 = notes1;
	}

	public Note addNotes1(Note notes1) {
		getNotes1().add(notes1);
		notes1.setAssignee(this);

		return notes1;
	}

	public Note removeNotes1(Note notes1) {
		getNotes1().remove(notes1);
		notes1.setAssignee(null);

		return notes1;
	}

	public List<Note> getNotes2() {
		return this.notes2;
	}

	public void setNotes2(List<Note> notes2) {
		this.notes2 = notes2;
	}

	public Note addNotes2(Note notes2) {
		getNotes2().add(notes2);
		notes2.setReporter(this);

		return notes2;
	}

	public Note removeNotes2(Note notes2) {
		getNotes2().remove(notes2);
		notes2.setReporter(null);

		return notes2;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}