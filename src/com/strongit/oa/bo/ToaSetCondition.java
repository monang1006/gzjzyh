package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SET_CONDITION"
 *     
*/
@Entity
@Table(name="T_OA_SET_CONDITION")
public class ToaSetCondition implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id
	@GeneratedValue
    private String conditionId;

    /** nullable persistent field */
    private String targetTable;

    /** nullable persistent field */
    private String generateConsql;

    /** nullable persistent field */
    private String generateDesc;

    /** nullable persistent field */
    private String orgId;

    /** nullable persistent field */
    private String orgName;

    /** full constructor */
    public ToaSetCondition(String conditionId, String targetTable, String generateConsql, String generateDesc, String orgId, String orgName) {
        this.conditionId = conditionId;
        this.targetTable = targetTable;
        this.generateConsql = generateConsql;
        this.generateDesc = generateDesc;
        this.orgId = orgId;
        this.orgName = orgName;
    }

    /** default constructor */
    public ToaSetCondition() {
    }

    /** minimal constructor */
    public ToaSetCondition(String conditionId) {
        this.conditionId = conditionId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CONDITION_ID"
     *         
     */
    @Id
	@Column(name="CONDITION_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getConditionId() {
        return this.conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    /** 
     *            @hibernate.property
     *             column="TARGET_TABLE"
     *             length="64"
     *         
     */
    @Column(name="TARGET_TABLE", length=64)
    public String getTargetTable() {
        return this.targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    /** 
     *            @hibernate.property
     *             column="GENERATE_CONSQL"
     *             length="2000"
     *         
     */
    @Column(name="GENERATE_CONSQL", length=2000)
    public String getGenerateConsql() {
        return this.generateConsql;
    }

    public void setGenerateConsql(String generateConsql) {
        this.generateConsql = generateConsql;
    }

    /** 
     *            @hibernate.property
     *             column="GENERATE_DESC"
     *             length="2000"
     *         
     */
    @Column(name="GENERATE_DESC", length=2000)
    public String getGenerateDesc() {
        return this.generateDesc;
    }

    public void setGenerateDesc(String generateDesc) {
        this.generateDesc = generateDesc;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
    @Column(name="ORG_ID", length=32)
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NAME"
     *             length="100"
     *         
     */
    @Column(name="ORG_NAME", length=100)
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("conditionId", getConditionId())
            .toString();
    }

}
