package note.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the note database table.
 * 
 */
@Entity
@NamedQuery(name="Note.findAll", query="SELECT n FROM Note n")
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int noteid;

	private String description;

	private byte readed;

	private String title;

	//bi-directional many-to-one association to Status
	@ManyToOne
	private Status status;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="users_assigneeId")
	private User assignee;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="users_reporterId")
	private User reporter;

	public Note() {
	}

	public int getNoteid() {
		return this.noteid;
	}

	public void setNoteid(int noteid) {
		this.noteid = noteid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getReaded() {
		return this.readed;
	}

	public void setReaded(byte readed) {
		this.readed = readed;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getAssignee() {
		return this.assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public User getReporter() {
		return this.reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

}