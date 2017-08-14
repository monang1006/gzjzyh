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
 *         table="T_OA_AFFICHE_ATTACH"
 *     
*/
@Entity
@Table(name="T_OA_AFFICHE_ATTACH")
public class ToaAfficheAttach implements Serializable {

    /** identifier field */
    private String afficheAttachId;

    /** nullable persistent field */
    private String attachId;

    /** persistent field */
    private com.strongit.oa.bo.ToaAffiche toaAffiche;

    /** full constructor */
    public ToaAfficheAttach(String afficheAttachId, String attachId, com.strongit.oa.bo.ToaAffiche toaAffiche) {
        this.afficheAttachId = afficheAttachId;
        this.attachId = attachId;
        this.toaAffiche = toaAffiche;
    }

    /** default constructor */
    public ToaAfficheAttach() {
    }

    /** minimal constructor */
    public ToaAfficheAttach(String afficheAttachId, com.strongit.oa.bo.ToaAffiche toaAffiche) {
        this.afficheAttachId = afficheAttachId;
        this.toaAffiche = toaAffiche;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="AFFICHE_ATTACH_ID"
     *         
     */
    @Id
	@Column(name="AFFICHE_ATTACH_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getAfficheAttachId() {
        return this.afficheAttachId;
    }

    public void setAfficheAttachId(String afficheAttachId) {
        this.afficheAttachId = afficheAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_ID"
     *             length="32"
     *         
     */
    @Column(name="ATTACH_ID",nullable=true)
    public String getAttachId() {
        return this.attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="AFFICHE_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="AFFICHE_ID")
    public com.strongit.oa.bo.ToaAffiche getToaAffiche() {
        return this.toaAffiche;
    }

    public void setToaAffiche(com.strongit.oa.bo.ToaAffiche toaAffiche) {
        this.toaAffiche = toaAffiche;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("afficheAttachId", getAfficheAttachId())
            .toString();
    }

}
