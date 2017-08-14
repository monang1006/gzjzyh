package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_DESKTOP_SECTION"
 *     
*/
@Entity
@Table(name="T_OA_DESKTOP_SECTION",catalog="",schema="")
public class ToaDesktopSection implements Serializable {

    /** identifier field */
    private String sectionId;

    /** nullable persistent field */
    private String sectionType;

    /** nullable persistent field */
    private String sectionName;

    /** nullable persistent field */
    private String sectionCreater;

    /** nullable persistent field */
    private String sectionDate;

    /** nullable persistent field */
    private String sectionColor;

    /** nullable persistent field */
    private int sectionLine;

    /** nullable persistent field */
    private int sectionWidth;

    /** nullable persistent field */
    private int sectionRow;

    /** nullable persistent field */
    private int sectionCol;
    
    private String sectionImg;
    
    private String sectionurl;
    
    private String showType;
    
    /** nullable persistent field */
    private int sectionFontSize;
    
    /** nullable persistent field */
    private String isClose ;// 是否可关闭 IS_CLOSE 

    /** persistent field */
    private com.strongit.oa.bo.ToaDesktopWhole toaDesktopWhole;

    /** full constructor */
    public ToaDesktopSection(String sectionId, String sectionType, String sectionName, String sectionCreater, 
    		String sectionDate, String sectionColor,String isClose,int sectionFontSize,int sectionLine, int sectionWidth, int sectionRow, 
    		int sectionCol, com.strongit.oa.bo.ToaDesktopWhole toaDesktopWhole) {
        this.sectionId = sectionId;
        this.sectionType = sectionType;
        this.sectionName = sectionName;
        this.sectionCreater = sectionCreater;
        this.sectionDate = sectionDate;
        this.sectionColor = sectionColor;
        this.isClose = isClose;
        this.sectionFontSize = sectionFontSize;
        this.sectionLine = sectionLine;
        this.sectionWidth = sectionWidth;
        this.sectionRow = sectionRow;
        this.sectionCol = sectionCol;
        this.toaDesktopWhole = toaDesktopWhole;
    }

    /** default constructor */
    public ToaDesktopSection() {
    }

    /** minimal constructor */
    public ToaDesktopSection(String sectionId, com.strongit.oa.bo.ToaDesktopWhole toaDesktopWhole) {
        this.sectionId = sectionId;
        this.toaDesktopWhole = toaDesktopWhole;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SECTION_ID"
     *         
     */
	@Id
	@Column(name="SECTION_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getSectionId() {
        return this.sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_TYPE"
     *             length="32"
     *         
     */
    @Column(name="SECTION_TYPE",nullable=true)
    public String getSectionType() {
        return this.sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_NAME"
     *             length="50"
     *         
     */
    @Column(name="SECTION_NAME",nullable=true)
    public String getSectionName() {
        return this.sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_CREATER"
     *             length="1"
     *         
     */
    @Column(name="SECTION_CREATER",nullable=true)
    public String getSectionCreater() {
        return this.sectionCreater;
    }

    public void setSectionCreater(String sectionCreater) {
        this.sectionCreater = sectionCreater;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_DATE"
     *             length="1"
     *         
     */
    @Column(name="SECTION_DATE",nullable=true)
    public String getSectionDate() {
        return this.sectionDate;
    }

    public void setSectionDate(String sectionDate) {
        this.sectionDate = sectionDate;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_COLOR"
     *             length="20"
     *         
     */
    @Column(name="SECTION_COLOR",nullable=true)
    public String getSectionColor() {
        return this.sectionColor;
    }

    public void setSectionColor(String sectionColor) {
        this.sectionColor = sectionColor;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_LINE"
     *             length="22"
     *         
     */
    @Column(name="SECTION_LINE",nullable=true)
    public int getSectionLine() {
        return this.sectionLine;
    }

    public void setSectionLine(int sectionLine) {
        this.sectionLine = sectionLine;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_WIDTH"
     *             length="22"
     *         
     */
    @Column(name="SECTION_WIDTH",nullable=true)
    public int getSectionWidth() {
        return this.sectionWidth;
    }

    public void setSectionWidth(int sectionWidth) {
        this.sectionWidth = sectionWidth;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_ROW"
     *             length="22"
     *         
     */
    @Column(name="SECTION_ROW",nullable=true)
    public int getSectionRow() {
        return this.sectionRow;
    }

    public void setSectionRow(int sectionRow) {
        this.sectionRow = sectionRow;
    }

    /** 
     *            @hibernate.property
     *             column="SECTION_COL"
     *             length="22"
     *         
     */
    @Column(name="SECTION_COL",nullable=true)
    public int getSectionCol() {
        return this.sectionCol;
    }

    public void setSectionCol(int sectionCol) {
        this.sectionCol = sectionCol;
    }
    
    /** 
     *            @hibernate.property
     *             column="SECTION_URL"
     *             length="500"
     *         
     */
    @Column(name="SECTION_URL",nullable=true)
	public String getSectionurl() {
		return sectionurl;
	}

	public void setSectionurl(String sectionurl) {
		this.sectionurl = sectionurl;
	}

	/** 
     *            @hibernate.property
     *             column="SECTION_IMG"
     *             length="200"
     *         
     */
    @Column(name="SECTION_IMG",nullable=true)
	public String getSectionImg() {
		return sectionImg;
	}
    
    @Column(name="showType",nullable=true)
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}    

	public void setSectionImg(String sectionImg) {
		this.sectionImg = sectionImg;
	}

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DESKTOP_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DESKTOP_ID", nullable=false)
    public com.strongit.oa.bo.ToaDesktopWhole getToaDesktopWhole() {
        return this.toaDesktopWhole;
    }

    public void setToaDesktopWhole(com.strongit.oa.bo.ToaDesktopWhole toaDesktopWhole) {
        this.toaDesktopWhole = toaDesktopWhole;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sectionId", getSectionId())
            .toString();
    }

    /**字体大小*/
    @Column(name="sectionFontSize",nullable=true)
	public int getSectionFontSize() {
		return sectionFontSize;
	}
	public void setSectionFontSize(int sectionFontSize) {
		this.sectionFontSize = sectionFontSize;
	}
	/**是否可关闭*/
    @Column(name="IS_CLOSE",nullable=true)
	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}


	

}
