package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_DESKTOP_WHOLE"
 * 
 */
@Entity
@Table(name = "T_OA_DESKTOP_WHOLE", catalog = "", schema = "")
public class ToaDesktopWhole implements Serializable, Cloneable {

	/** identifier field */
	private String desktopId;

	/** nullable persistent field */
	private int desktopColumn;

	/** nullable persistent field */
	private int desktopLeft;

	/** nullable persistent field */
	private int desktopCenter;

	/** nullable persistent field */
	private int desktopRight;

	/** nullable persistent field */
	private String desktopLayout;

	/** nullable persistent field */
	private String desktopIsdefault;

	/** nullable persistent field */
	private Date desktopCtime;

	/** persistent field */
	private String userId;

	private String isMoren;// 是否可编辑 IS_MOREN

	private String portalId;// 门户ID PORTAL_ID

	private String rest1;// 备用字段

	private String rest2;// 备用字段

	/** nullable persistent field */
	private String desktopRole;

	/** persistent field */
	private Set toaDesktopSections;

	/** full constructor */
	public ToaDesktopWhole(String desktopId, int desktopColumn,
			int desktopLeft, int desktopCenter, int desktopRight,
			String desktopLayout, String desktopIsdefault, Date desktopCtime,
			String userId, Set toaDesktopSections) {
		this.desktopId = desktopId;
		this.desktopColumn = desktopColumn;
		this.desktopLeft = desktopLeft;
		this.desktopCenter = desktopCenter;
		this.desktopRight = desktopRight;
		this.desktopLayout = desktopLayout;
		this.desktopIsdefault = desktopIsdefault;
		this.desktopCtime = desktopCtime;
		this.userId = userId;
		this.toaDesktopSections = toaDesktopSections;
	}

	/** default constructor */
	public ToaDesktopWhole() {
	}

	/** minimal constructor */
	public ToaDesktopWhole(String desktopId, String userId,
			Set toaDesktopSections) {
		this.desktopId = desktopId;
		this.userId = userId;
		this.toaDesktopSections = toaDesktopSections;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="DESKTOP_ID"
	 * 
	 */
	@Id
	@Column(name = "DESKTOP_ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getDesktopId() {
		return this.desktopId;
	}

	public void setDesktopId(String desktopId) {
		this.desktopId = desktopId;
	}

	/**
	 * @hibernate.property column="DESKTOP_COLUMN" length="22"
	 * 
	 */
	@Column(name = "DESKTOP_COLUMN", nullable = true)
	public int getDesktopColumn() {
		return this.desktopColumn;
	}

	public void setDesktopColumn(int desktopColumn) {
		this.desktopColumn = desktopColumn;
	}

	/**
	 * @hibernate.property column="DESKTOP_LEFT" length="22"
	 * 
	 */
	@Column(name = "DESKTOP_LEFT", nullable = true)
	public int getDesktopLeft() {
		return this.desktopLeft;
	}

	public void setDesktopLeft(int desktopLeft) {
		this.desktopLeft = desktopLeft;
	}

	/**
	 * @hibernate.property column="DESKTOP_CENTER" length="22"
	 * 
	 */
	@Column(name = "DESKTOP_CENTER", nullable = true)
	public int getDesktopCenter() {
		return this.desktopCenter;
	}

	public void setDesktopCenter(int desktopCenter) {
		this.desktopCenter = desktopCenter;
	}

	/**
	 * @hibernate.property column="DESKTOP_RIGHT" length="22"
	 * 
	 */
	@Column(name = "DESKTOP_RIGHT", nullable = true)
	public int getDesktopRight() {
		return this.desktopRight;
	}

	public void setDesktopRight(int desktopRight) {
		this.desktopRight = desktopRight;
	}

	/**
	 * @hibernate.property column="DESKTOP_LAYOUT" length="50"
	 * 
	 */
	@Column(name = "DESKTOP_LAYOUT", nullable = true)
	public String getDesktopLayout() {
		return this.desktopLayout;
	}

	public void setDesktopLayout(String desktopLayout) {
		this.desktopLayout = desktopLayout;
	}

	/**
	 * @hibernate.property column="DESKTOP_ISDEFAULT" length="1"
	 * 
	 */
	@Column(name = "DESKTOP_ISDEFAULT", nullable = true)
	public String getDesktopIsdefault() {
		return desktopIsdefault;
	}

	public void setDesktopIsdefault(String desktopIsdefault) {
		this.desktopIsdefault = desktopIsdefault;
	}

	/**
	 * @hibernate.property column="DESKTOP_CTIME" length="7"
	 * 
	 */
	@Column(name = "DESKTOP_CTIME", nullable = true)
	public Date getDesktopCtime() {
		return desktopCtime;
	}

	public void setDesktopCtime(Date desktopCtime) {
		this.desktopCtime = desktopCtime;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="DESKTOP_ID"
	 * @hibernate.collection-one-to-many class="com.strongit.oa.bo.ToaDesktopSection"
	 * 
	 */
	@OneToMany(mappedBy = "toaDesktopWhole", targetEntity = com.strongit.oa.bo.ToaDesktopSection.class, cascade = CascadeType.ALL)
	public Set getToaDesktopSections() {
		return this.toaDesktopSections;
	}

	public void setToaDesktopSections(Set toaDesktopSections) {
		this.toaDesktopSections = toaDesktopSections;
	}

	public String toString() {
		return new ToStringBuilder(this).append("desktopId", getDesktopId())
				.toString();
	}

	@Column(name = "DESKTOP_ROLE", nullable = true)
	public String getDesktopRole() {
		return desktopRole;
	}

	public void setDesktopRole(String desktopRole) {
		this.desktopRole = desktopRole;
	}

	@Column(name = "USER_ID", nullable = true)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Column(name = "IS_MOREN", nullable = true)
	public String getIsMoren() {
		return isMoren;
	}

	public void setIsMoren(String isMoren) {
		this.isMoren = isMoren;
	}

	@Column(name = "PORTAL_ID", nullable = true)
	public String getPortalId() {
		return portalId;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	@Column(name = "REST1", nullable = true)
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", nullable = true)
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}
}
