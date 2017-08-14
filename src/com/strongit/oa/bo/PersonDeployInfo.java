package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PERSON_DEPLOYINFO", catalog = "", schema = "")
public class PersonDeployInfo implements Serializable{
	
	     private String deployinfoId;

	    /** nullable persistent field */
	    private String personId;
	    
	    private com.strongit.oa.bo.ToaPersonDeploy deployInfo;

	    /** nullable persistent field */
	    private String oldCompany;

	    /** nullable persistent field */
	    private String oldPset;

	    /** nullable persistent field */
	    private String newCommany;

	    /** nullable persistent field */
	    private Date exchangeTime;

	    /** nullable persistent field */
	    private String oldLevel;

	    /** nullable persistent field */
	    private String newLevel;
	    
	    private String newPset;

	    /** nullable persistent field */
	    private String isRetire;

	    /** nullable persistent field */
	    private String exchangeWhy;
	    
	    private String newInfos;
	    private String oldInfos;
	    
	    private String personName;
	    private Date lastTime;

	    @Id
		@Column(name = "DEPLOYINFOID", nullable = false)
		@GeneratedValue(generator = "hibernate-uuid")
		@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
		public String getDeployinfoId() {
			return deployinfoId;
		}

		public void setDeployinfoId(String deployinfoId) {
			this.deployinfoId = deployinfoId;
		}
		 @Column(name = "EXCHANGE_TIME")
		public Date getExchangeTime() {
			return exchangeTime;
		}

		public void setExchangeTime(Date exchangeTime) {
			this.exchangeTime = exchangeTime;
		}
		 @Column(name = "EXCHANGE_WHY")
		public String getExchangeWhy() {
			return exchangeWhy;
		}

		public void setExchangeWhy(String exchangeWhy) {
			this.exchangeWhy = exchangeWhy;
		}
		 @Column(name = "IS_RETIRE")
		public String getIsRetire() {
			return isRetire;
		}

		public void setIsRetire(String isRetire) {
			this.isRetire = isRetire;
		}
		 @Column(name = "NEW_COMMANY")
		public String getNewCommany() {
			return newCommany;
		}

		public void setNewCommany(String newCommany) {
			this.newCommany = newCommany;
		}
		 @Column(name = "NEW_LEVEL")
		public String getNewLevel() {
			return newLevel;
		}

		public void setNewLevel(String newLevel) {
			this.newLevel = newLevel;
		}
		 @Column(name = "NEW_PSET")
		public String getNewPset() {
			return newPset;
		}

		public void setNewPset(String newPset) {
			this.newPset = newPset;
		}
		 @Column(name = "OLD_COMPANY")
		public String getOldCompany() {
			return oldCompany;
		}

		public void setOldCompany(String oldCompany) {
			this.oldCompany = oldCompany;
		}
		 @Column(name = "OLD_LEVEL")
		public String getOldLevel() {
			return oldLevel;
		}

		public void setOldLevel(String oldLevel) {
			this.oldLevel = oldLevel;
		}
		 @Column(name = "OLD_PSET")
		public String getOldPset() {
			return oldPset;
		}

		public void setOldPset(String oldPset) {
			this.oldPset = oldPset;
		}
		 @Column(name = "PERSONID")
		public String getPersonId() {
			return personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId;
		}
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "DEPLOY_TYPEID")
		public com.strongit.oa.bo.ToaPersonDeploy getDeployInfo() {
			return deployInfo;
		}

		public void setDeployInfo(com.strongit.oa.bo.ToaPersonDeploy deployInfo) {
			this.deployInfo = deployInfo;
		}
		 @Column(name = "NEW_INFOS")
		public String getNewInfos() {
			return newInfos;
		}
		 
		public void setNewInfos(String newInfos) {
			this.newInfos = newInfos;
		}
		 @Column(name = " OLD_INFOS")
		public String getOldInfos() {
			return oldInfos;
		}

		public void setOldInfos(String oldInfos) {
			this.oldInfos = oldInfos;
		}

		@Transient
		public String getPersonName() {
			return personName;
		}

		public void setPersonName(String personName) {
			this.personName = personName;
		}

		@Transient
		public Date getLastTime() {
			return lastTime;
		}

		public void setLastTime(Date lastTime) {
			this.lastTime = lastTime;
		}
}
