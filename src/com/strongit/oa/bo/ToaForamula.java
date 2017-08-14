package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
 *        @hibernate.class
 *         table="T_OA_FORAMULA"
 *     
*/

@Entity
@Table(name="T_OA_FORAMULA")
public class ToaForamula implements Serializable {

    /** identifier field */
    private String foramulaId;

    /** nullable persistent field */
    private String foramulaDec;

    /** nullable persistent field */
    private Date foramulaDate;
    
    private String foramulaDefault;

    /** persistent field */
    private Set toaPagemodels;
    
    private String isCreatePage;
    
    private String createTime;

    /** full constructor */
    public ToaForamula(String foramulaId, String foramulaDec, Date foramulaDate, Set toaPagemodels) {
        this.foramulaId = foramulaId;
        this.foramulaDec = foramulaDec;
        this.foramulaDate = foramulaDate;
        this.toaPagemodels = toaPagemodels;
    }

    /** default constructor */
    public ToaForamula() {
    }

    /** minimal constructor */
    public ToaForamula(String foramulaId, Set toaPagemodels) {
        this.foramulaId = foramulaId;
        this.toaPagemodels = toaPagemodels;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FORAMULA_ID"
     *         
     */
	@Id
	@Column(name="FORAMULA_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getForamulaId() {
        return this.foramulaId;
    }

    public void setForamulaId(String foramulaId) {
        this.foramulaId = foramulaId;
    }

    /** 
     *            @hibernate.property
     *             column="FORAMULA_DEC"
     *             length="100"
     *         
     */
    @Column(name="FORAMULA_DEC",nullable=true)
    public String getForamulaDec() {
        return this.foramulaDec;
    }

    public void setForamulaDec(String foramulaDec) {
        this.foramulaDec = foramulaDec;
    }

    /** 
     *            @hibernate.property
     *             column="FORAMULA_DATE"
     *             length="7"
     *         
     */
    @Column(name="FORAMULA_DATE",nullable=true)
    public Date getForamulaDate() {
        return this.foramulaDate;
    }

    public void setForamulaDate(Date foramulaDate) {
        this.foramulaDate = foramulaDate;
    }
    
    @Column(name="FORAMULA_DEFAULT",nullable=true)
    public String getForamulaDefault(){
    	return this.foramulaDefault;
    }
    
    public void setForamulaDefault(String foramulaDefault){
    	this.foramulaDefault = foramulaDefault;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FORAMULA_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.mine.bo.ToaPagemodel"
     *         
     */
    @OneToMany(mappedBy="toaForamula",targetEntity=com.strongit.oa.bo.ToaPagemodel.class,cascade=CascadeType.ALL)
    public Set getToaPagemodels() {
        return this.toaPagemodels;
    }

    public void setToaPagemodels(Set toaPagemodels) {
        this.toaPagemodels = toaPagemodels;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("foramulaId", getForamulaId())
            .toString();
    }

	public String getIsCreatePage() {
		return isCreatePage;
	}

	public void setIsCreatePage(String isCreatePage) {
		this.isCreatePage = isCreatePage;
	}

	@Column(name="createDate",nullable=true)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
