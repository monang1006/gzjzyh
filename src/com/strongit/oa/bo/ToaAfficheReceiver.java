package com.strongit.oa.bo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.strongmvc.orm.hibernate.Page;

/** 
 *        @hibernate.class
 *         table="T_OA_AFFICHE_RECEIVER"
 *     
*/
@Entity
@Table(name="T_OA_AFFICHE_RECEIVER")
public class ToaAfficheReceiver implements Serializable {

    /** identifier field */
    private String id;
    
    /** nullable persistent field */
    private String afficheReceiver;
    
    /** nullable persistent field */
    private String afficheReceiverId;
    
    private com.strongit.oa.bo.ToaAffiche toaAffiche;
    
    /** nullable persistent field */
    public final static String NOTIFY_DRAFT = "0";    //未发布_草稿
    public final static String NOTIFY_SENDED = "1";		//发布_公告栏
    public final static String NOTIFY_OVERDUE = "2";	//过期_
    
    /** full constructor */
    public ToaAfficheReceiver(String id, String afficheReceiver,String afficheReceiverId,com.strongit.oa.bo.ToaAffiche toaAffiche) {
       this.id = id;
       this.afficheReceiver = afficheReceiver;
       this.afficheReceiverId = afficheReceiverId;
       this.toaAffiche = toaAffiche;
    }

    /** default constructor */
    public ToaAfficheReceiver() {
    }

    /** minimal constructor */
    public ToaAfficheReceiver(String afficheReceiverId, com.strongit.oa.bo.ToaAffiche toaAffiche) {
        this.afficheReceiverId = afficheReceiverId;
        this.toaAffiche = toaAffiche;
    }
    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ID"
     */
    @Id
	@Column(name="ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_RECEIVER"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="AFFICHE_RECEIVER", columnDefinition="CLOB", nullable=true)    
    public String getAfficheReceiver() {
        return this.afficheReceiver;
    }

    public void setAfficheReceiver(String afficheReceiver) {
        this.afficheReceiver = afficheReceiver;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_RECEIVERID"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="AFFICHE_RECEIVERID", columnDefinition="CLOB", nullable=true)    
    public String getAfficheReceiverId() {
        return this.afficheReceiverId;
    }

    public void setAfficheReceiverId(String afficheReceiverId) {
        this.afficheReceiverId = afficheReceiverId;
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
            .append("afficheReceiver", getAfficheReceiver())
            .toString();
    }

	/*public Page<ToaAffiche> getListToaAffiche(String userId,
			Page<ToaAffiche> page) {
		// TODO Auto-generated method stub
		return null;
	}*/
    

}
