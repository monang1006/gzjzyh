package com.strongit.oa.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSON_PRIVIL"
 *     
*/
@Entity
@Table(name="T_OA_PERSON_PRIVIL")
public class ToaPersonPrivil implements Serializable {

	@Id 
	@GeneratedValue
    private String id;
	
	private String userId;
	
    private byte[] personIds;

  
    public ToaPersonPrivil(String id, String userId) {
		super();
		this.id = id;
		this.userId = userId;
	}

	public ToaPersonPrivil() {
		super();
	}

	public ToaPersonPrivil(String id, String userId, byte[] personIds) {
		super();
		this.id = id;
		this.userId = userId;
		this.personIds = personIds;
	}

    @Id
	@Column(name="ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="PERSON_IDS",nullable=true)
	@Lob
	public byte[] getPersonIds() {
		return personIds;
	}

	public void setPersonIds(byte[] personIds) {
		this.personIds = personIds;
	}

	@Column(name="USER_ID",nullable=false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
