package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MEETING_TODO"
 *   待办工作表  
*/
@Entity
@Table(name = "T_OA_MEETING_TODO", catalog = "", schema = "")
public class ToaMeetingTodo implements Serializable {

    /** identifier field */
    private String todoId;

    /** nullable persistent field */
    private String todoTitle;

    /** nullable persistent field */
    private String todoUserid;

    /** nullable persistent field */
    private String todoUsername;

    /** nullable persistent field */
    private String todoState;

    /** persistent field */
    private com.strongit.oa.bo.ToaMeetingSummary meetingSummary;

    /** full constructor */
    public ToaMeetingTodo(String meetingTodoId, String meetingTodoTitle, String meetingTodoUserid, String meetingTodoUsername, String meetingTodoState, com.strongit.oa.bo.ToaMeetingSummary toaMeetingSummary) {
        this.todoId = meetingTodoId;
        this.todoTitle = meetingTodoTitle;
        this.todoUserid = meetingTodoUserid;
        this.todoUsername = meetingTodoUsername;
        this.todoState = meetingTodoState;
        this.meetingSummary = toaMeetingSummary;
    }

    /** default constructor */
    public ToaMeetingTodo() {
    }

    /** minimal constructor */
    public ToaMeetingTodo(String meetingTodoId, com.strongit.oa.bo.ToaMeetingSummary toaMeetingSummary) {
        this.todoId = meetingTodoId;
        this.meetingSummary = toaMeetingSummary;
    }
    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MEETING_TODO_ID"
     *         
     */
    @Id
	@Column(name = "MEETING_TODO_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getTodoId() {
		return todoId;
	}

	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}
	@Column(name = "MEETING_TODO_STATE")
	public String getTodoState() {
		return todoState;
	}

	public void setTodoState(String todoState) {
		this.todoState = todoState;
	}
	@Column(name = "MEETING_TODO_TITLE")
	public String getTodoTitle() {
		return todoTitle;
	}

	public void setTodoTitle(String todoTitle) {
		this.todoTitle = todoTitle;
	}
	@Column(name = "MEETING_TODO_USERID")
	public String getTodoUserid() {
		return todoUserid;
	}

	public void setTodoUserid(String todoUserid) {
		this.todoUserid = todoUserid;
	}
	@Column(name = "MEETING_TODO_USERNAME")
	public String getTodoUsername() {
		return todoUsername;
	}

	public void setTodoUsername(String todoUsername) {
		this.todoUsername = todoUsername;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEETING_SUMMARY_ID")
	public com.strongit.oa.bo.ToaMeetingSummary getMeetingSummary() {
		return meetingSummary;
	}

	public void setMeetingSummary(
			com.strongit.oa.bo.ToaMeetingSummary meetingSummary) {
		this.meetingSummary = meetingSummary;
	}

    
}
