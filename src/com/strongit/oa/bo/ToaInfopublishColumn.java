package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INFOPUBLISH_COLUMN"
 *     
*/
@Entity
@Table(name = "T_OA_INFOPUBLISH_COLUMN")
public class ToaInfopublishColumn implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String clumnId;

    /** nullable persistent field */
    private String clumnParent;

    /** nullable persistent field */
    private String clumnDir;

    /** persistent field */
    private String clumnName;

    /** nullable persistent field */
    private Date clumnCreatetime;

    /** nullable persistent field */
    private String clumnMemo;

    /** nullable persistent field */
    private String clumnTips;

    /** nullable persistent field */
    private String clumnIsnewopen;

    /** nullable persistent field */
    private String clumnIsuserss;

    /** nullable persistent field */
    private String clumnIsarticlenewwin;

    /** nullable persistent field */
    private String clumnIsarticle;

    /** nullable persistent field */
    private String clumnIsprivate;

    /** nullable persistent field */
    private String clumnCreateduser;

    /** nullable persistent field */
    private Date clumnCreatedtime;

    /** nullable persistent field */
    private String clumnLastmodifieduser;

    /** nullable persistent field */
    private Date clumnLastmodifiedtime;

    /** nullable persistent field */
    private String clumnIsgenhtml;

    /** nullable persistent field */
    private Date clumnCreattime;

    /** nullable persistent field */
    private String clumnCreatuser;

    /** nullable persistent field */
    private String clumnIsgenrss;

    /** nullable persistent field */
    private Date clumnCreatrsstime;

    /** nullable persistent field */
    private String clumnPath;

    /** nullable persistent field */
    private String clumnCreatrssuser;

    /** nullable persistent field */
    private String clumnHits;
    
    /** nullable persistent field */
    private String processName;
    
    //变量用于保存分组统计数
    private int articleNum;
    
    /** persistent field */
    private Set<ToaInfopublishColumnPrivil> toaInfopublishColumnPrivils;

    /** persistent field */
    private Set<ToaInfopublishColumnArticl> toaInfopublishColumnArticls;

    /** full constructor */
    public ToaInfopublishColumn(String clumnId, String clumnParent, String clumnDir, String clumnName, Date clumnCreatetime, String clumnMemo, String clumnTips, String clumnIsnewopen, String clumnIsuserss, String clumnIsarticlenewwin, String clumnIsarticle, String clumnIsprivate, String clumnCreateduser, Date clumnCreatedtime, String clumnLastmodifieduser, Date clumnLastmodifiedtime, String clumnIsgenhtml, Date clumnCreattime, String clumnCreatuser, String clumnIsgenrss, Date clumnCreatrsstime, String clumnPath, String clumnCreatrssuser, String clumnHits,String processName, Set toaInfopublishColumnPrivils, Set toaInfopublishColumnArticls) {
        this.clumnId = clumnId;
        this.clumnParent = clumnParent;
        this.clumnDir = clumnDir;
        this.clumnName = clumnName;
        this.clumnCreatetime = clumnCreatetime;
        this.clumnMemo = clumnMemo;
        this.clumnTips = clumnTips;
        this.clumnIsnewopen = clumnIsnewopen;
        this.clumnIsuserss = clumnIsuserss;
        this.clumnIsarticlenewwin = clumnIsarticlenewwin;
        this.clumnIsarticle = clumnIsarticle;
        this.clumnIsprivate = clumnIsprivate;
        this.clumnCreateduser = clumnCreateduser;
        this.clumnCreatedtime = clumnCreatedtime;
        this.clumnLastmodifieduser = clumnLastmodifieduser;
        this.clumnLastmodifiedtime = clumnLastmodifiedtime;
        this.clumnIsgenhtml = clumnIsgenhtml;
        this.clumnCreattime = clumnCreattime;
        this.clumnCreatuser = clumnCreatuser;
        this.clumnIsgenrss = clumnIsgenrss;
        this.clumnCreatrsstime = clumnCreatrsstime;
        this.clumnPath = clumnPath;
        this.clumnCreatrssuser = clumnCreatrssuser;
        this.clumnHits = clumnHits;
        this.processName = processName;
        this.toaInfopublishColumnPrivils = toaInfopublishColumnPrivils;
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
    }

    /** default constructor */
    public ToaInfopublishColumn() {
    }

    /** minimal constructor */
    public ToaInfopublishColumn(String clumnId, String clumnName, Set toaInfopublishColumnPrivils, Set toaInfopublishColumnArticls) {
        this.clumnId = clumnId;
        this.clumnName = clumnName;
        this.toaInfopublishColumnPrivils = toaInfopublishColumnPrivils;
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CLUMN_ID"
     *         
     */
    @Id
    @Column(name = "CLUMN_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getClumnId() {
        return this.clumnId;
    }

    public void setClumnId(String clumnId) {
        this.clumnId = clumnId;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_PARENT"
     *             length="32"
     *         
     */
    @Column(name = "CLUMN_PARENT", nullable = true)
    public String getClumnParent() {
        return this.clumnParent;
    }

    public void setClumnParent(String clumnParent) {
        this.clumnParent = clumnParent;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_DIR"
     *             length="100"
     *         
     */
    @Column(name = "CLUMN_DIR", nullable = true)
    public String getClumnDir() {
        return this.clumnDir;
    }

    public void setClumnDir(String clumnDir) {
        this.clumnDir = clumnDir;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_NAME"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name = "CLUMN_NAME", nullable = false)
    public String getClumnName() {
        return this.clumnName;
    }

    public void setClumnName(String clumnName) {
        this.clumnName = clumnName;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATETIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_CREATETIME", nullable = true)
    public Date getClumnCreatetime() {
        return this.clumnCreatetime;
    }

    public void setClumnCreatetime(Date clumnCreatetime) {
        this.clumnCreatetime = clumnCreatetime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_MEMO"
     *             length="150"
     *         
     */
    @Column(name = "CLUMN_MEMO", nullable = true)
    public String getClumnMemo() {
        return this.clumnMemo;
    }

    public void setClumnMemo(String clumnMemo) {
        this.clumnMemo = clumnMemo;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_TIPS"
     *             length="150"
     *         
     */
    @Column(name = "CLUMN_TIPS", nullable = true)
    public String getClumnTips() {
        return this.clumnTips;
    }

    public void setClumnTips(String clumnTips) {
        this.clumnTips = clumnTips;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISNEWOPEN"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISNEWOPEN", nullable = true)
    public String getClumnIsnewopen() {
        return this.clumnIsnewopen;
    }

    public void setClumnIsnewopen(String clumnIsnewopen) {
        this.clumnIsnewopen = clumnIsnewopen;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISUSERSS"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISUSERSS", nullable = true)
    public String getClumnIsuserss() {
        return this.clumnIsuserss;
    }

    public void setClumnIsuserss(String clumnIsuserss) {
        this.clumnIsuserss = clumnIsuserss;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISARTICLENEWWIN"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISARTICLENEWWIN", nullable = true)
    public String getClumnIsarticlenewwin() {
        return this.clumnIsarticlenewwin;
    }

    public void setClumnIsarticlenewwin(String clumnIsarticlenewwin) {
        this.clumnIsarticlenewwin = clumnIsarticlenewwin;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISARTICLE"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISARTICLE", nullable = true)
    public String getClumnIsarticle() {
        return this.clumnIsarticle;
    }

    public void setClumnIsarticle(String clumnIsarticle) {
        this.clumnIsarticle = clumnIsarticle;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISPRIVATE"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISPRIVATE", nullable = true)
    public String getClumnIsprivate() {
        return this.clumnIsprivate;
    }

    public void setClumnIsprivate(String clumnIsprivate) {
        this.clumnIsprivate = clumnIsprivate;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATEDUSER"
     *             length="20"
     *         
     */
    @Column(name = "CLUMN_CREATEDUSER", nullable = true)
    public String getClumnCreateduser() {
        return this.clumnCreateduser;
    }

    public void setClumnCreateduser(String clumnCreateduser) {
        this.clumnCreateduser = clumnCreateduser;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATEDTIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_CREATEDTIME", nullable = true)
    public Date getClumnCreatedtime() {
        return this.clumnCreatedtime;
    }

    public void setClumnCreatedtime(Date clumnCreatedtime) {
        this.clumnCreatedtime = clumnCreatedtime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_LASTMODIFIEDUSER"
     *             length="20"
     *         
     */
    @Column(name = "CLUMN_LASTMODIFIEDUSER", nullable = true)
    public String getClumnLastmodifieduser() {
        return this.clumnLastmodifieduser;
    }

    public void setClumnLastmodifieduser(String clumnLastmodifieduser) {
        this.clumnLastmodifieduser = clumnLastmodifieduser;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_LASTMODIFIEDTIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_LASTMODIFIEDTIME", nullable = true)
    public Date getClumnLastmodifiedtime() {
        return this.clumnLastmodifiedtime;
    }

    public void setClumnLastmodifiedtime(Date clumnLastmodifiedtime) {
        this.clumnLastmodifiedtime = clumnLastmodifiedtime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISGENHTML"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISGENHTML", nullable = true)
    public String getClumnIsgenhtml() {
        return this.clumnIsgenhtml;
    }

    public void setClumnIsgenhtml(String clumnIsgenhtml) {
        this.clumnIsgenhtml = clumnIsgenhtml;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATTIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_CREATTIME", nullable = true)
    public Date getClumnCreattime() {
        return this.clumnCreattime;
    }

    public void setClumnCreattime(Date clumnCreattime) {
        this.clumnCreattime = clumnCreattime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATUSER"
     *             length="25"
     *         
     */
    @Column(name = "CLUMN_CREATUSER", nullable = true)
    public String getClumnCreatuser() {
        return this.clumnCreatuser;
    }

    public void setClumnCreatuser(String clumnCreatuser) {
        this.clumnCreatuser = clumnCreatuser;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_ISGENRSS"
     *             length="1"
     *         
     */
    @Column(name = "CLUMN_ISGENRSS", nullable = true)
    public String getClumnIsgenrss() {
        return this.clumnIsgenrss;
    }

    public void setClumnIsgenrss(String clumnIsgenrss) {
        this.clumnIsgenrss = clumnIsgenrss;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATRSSTIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_CREATRSSTIME", nullable = true)
    public Date getClumnCreatrsstime() {
        return this.clumnCreatrsstime;
    }

    public void setClumnCreatrsstime(Date clumnCreatrsstime) {
        this.clumnCreatrsstime = clumnCreatrsstime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_PATH"
     *             length="100"
     *         
     */
    @Column(name = "CLUMN_PATH", nullable = true)
    public String getClumnPath() {
        return this.clumnPath;
    }

    public void setClumnPath(String clumnPath) {
        this.clumnPath = clumnPath;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATRSSUSER"
     *             length="25"
     *         
     */
    @Column(name = "CLUMN_CREATRSSUSER", nullable = true)
    public String getClumnCreatrssuser() {
        return this.clumnCreatrssuser;
    }

    public void setClumnCreatrssuser(String clumnCreatrssuser) {
        this.clumnCreatrssuser = clumnCreatrssuser;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_HITS"
     *             length="10"
     *         
     */
    @Column(name = "CLUMN_HITS", nullable = true)
    public String getClumnHits() {
        return this.clumnHits;
    }

    public void setClumnHits(String clumnHits) {
        this.clumnHits = clumnHits;
    }
    
    /** 
     *            @hibernate.property
     *             column="PROCESS_NAME"
     *             length="50"
     *         
     */
    @Column(name = "PROCESS_NAME", nullable = true)
    public String getProcessName() {
        return this.processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CLUMN_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInfopublishColumnPrivil"
     *         
     */
    @OneToMany(mappedBy = "toaInfopublishColumn", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<ToaInfopublishColumnPrivil> getToaInfopublishColumnPrivils() {
        return this.toaInfopublishColumnPrivils;
    }

    public void setToaInfopublishColumnPrivils(Set<ToaInfopublishColumnPrivil> toaInfopublishColumnPrivils) {
        this.toaInfopublishColumnPrivils = toaInfopublishColumnPrivils;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CLUMN_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInfopublishColumnArticl"
     *         
     */
    @OneToMany(mappedBy = "toaInfopublishColumn", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<ToaInfopublishColumnArticl> getToaInfopublishColumnArticls() {
        return this.toaInfopublishColumnArticls;
    }

    public void setToaInfopublishColumnArticls(Set<ToaInfopublishColumnArticl> toaInfopublishColumnArticls) {
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("clumnId", getClumnId())
            .toString();
    }
    @Transient
	public int getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
}
