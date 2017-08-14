package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_BASE"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_BASE")
public class ToaSysmanageBase implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String baseId;

    /** nullable persistent field */
    private String baseWindowsTitle;

    /** nullable persistent field */
    private String baseTitle;

    /** nullable persistent field */
    private String baseTitleFont;

    /** nullable persistent field */
    private String baseLogoPic;

    /** nullable persistent field */
    private Integer baseLogoWidth;

    /** nullable persistent field */
    private String baseStatusbar;

    /** nullable persistent field */
    private Integer baseLogoHeight;

    /** nullable persistent field */
    private Integer baseStatusbarTime;

    /** nullable persistent field */
    private String baseInterfaceThemes;

    /** nullable persistent field */
    private String baseDefaultStartMenu;

    /** nullable persistent field */
    private String baseMenuIcon;

    /** nullable persistent field */
    private String baseIsShow;

    /** nullable persistent field */
    private String baseCity;

    /** nullable persistent field */
    private String baseLoginPic;

    /** nullable persistent field */
    private String baseUserPic;

    /** nullable persistent field */
    private Integer baseFolderSize;

    /** nullable persistent field */
    private Integer baseEmailSize;

    /** nullable persistent field */
    private String baseMobileState;
    
    /** վ����Ϣ ���˿ռ��С*/
    private String baseMsgSize;
    
    private String userId;
    
    
    //////////////////////////////////////////
    private String leftbg;				//右顶部图片存放路径
    
	private Integer fastMenuFontSize;	//顶部字体大小
	
	private String PicPath ; 			//背景图片路径存放字段

    /** nullable persistent field */
    private String baseLoginThemes;		//BY 刘皙 2012年4月23日15:15:25
    
    
    
    
    
    
    

    /** full constructor */
    public ToaSysmanageBase(String baseId, String baseWindowsTitle, String baseTitle, String baseTitleFont, String baseLogoPic, Integer baseLogoWidth, String baseStatusbar, Integer baseLogoHeight, Integer baseStatusbarTime, String baseInterfaceThemes, String baseDefaultStartMenu, String baseMenuIcon, String baseIsShow, String baseCity, String baseLoginPic, String baseUserPic, Integer baseFolderSize, Integer baseEmailSize, String baseMobileState, String baseMsgSize, Integer fastMenuFontSize, String baseLoginThemes ) {
        this.baseId = baseId;
        this.baseWindowsTitle = baseWindowsTitle;
        this.baseTitle = baseTitle;
        this.baseTitleFont = baseTitleFont;
        this.baseLogoPic = baseLogoPic;
        this.baseLogoWidth = baseLogoWidth;
        this.baseStatusbar = baseStatusbar;
        this.baseLogoHeight = baseLogoHeight;
        this.baseStatusbarTime = baseStatusbarTime;
        this.baseInterfaceThemes = baseInterfaceThemes;
        this.baseDefaultStartMenu = baseDefaultStartMenu;
        this.baseMenuIcon = baseMenuIcon;
        this.baseIsShow = baseIsShow;
        this.baseCity = baseCity;
        this.baseLoginPic = baseLoginPic;
        this.baseUserPic = baseUserPic;
        this.baseFolderSize = baseFolderSize;
        this.baseEmailSize = baseEmailSize;
        this.baseMobileState = baseMobileState;
        this.baseMsgSize = baseMsgSize;
        this.fastMenuFontSize = fastMenuFontSize;
        this.baseLoginThemes = baseLoginThemes;
    }

    /** default constructor */
    public ToaSysmanageBase() {
    }

    /** minimal constructor */
    public ToaSysmanageBase(String baseId) {
        this.baseId = baseId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="BASE_ID"
     *         
     */
    @Id
	@Column(name="BASE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getBaseId() {
        return this.baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_WINDOWS_TITLE"
     *             length="100"
     *         
     */
    @Column(name="BASE_WINDOWS_TITLE",nullable=true)
    public String getBaseWindowsTitle() {
        return this.baseWindowsTitle;
    }

    public void setBaseWindowsTitle(String baseWindowsTitle) {
        this.baseWindowsTitle = baseWindowsTitle;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_TITLE"
     *             length="100"
     *         
     */
    @Column(name="BASE_TITLE",nullable=true)
    public String getBaseTitle() {
        return this.baseTitle;
    }

    public void setBaseTitle(String baseTitle) {
        this.baseTitle = baseTitle;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_TITLE_FONT"
     *             length="50"
     *         
     */
    @Column(name="BASE_TITLE_FONT",nullable=true)
    public String getBaseTitleFont() {
        return this.baseTitleFont;
    }

    public void setBaseTitleFont(String baseTitleFont) {
        this.baseTitleFont = baseTitleFont;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_LOGO_PIC"
     *             length="100"
     *         
     */
    @Column(name="BASE_LOGO_PIC",nullable=true)
    public String getBaseLogoPic() {
        return this.baseLogoPic;
    }

    public void setBaseLogoPic(String baseLogoPic) {
        this.baseLogoPic = baseLogoPic;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_LOGO_WIDTH"
     *             length="5"
     *         
     */
    @Column(name="BASE_LOGO_WIDTH",nullable=true)
    public Integer getBaseLogoWidth() {
        return this.baseLogoWidth;
    }

    public void setBaseLogoWidth(Integer baseLogoWidth) {
        this.baseLogoWidth = baseLogoWidth;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_STATUSBAR"
     *         
     */
    @Column(name="BASE_STATUSBAR",nullable=true)
    public String getBaseStatusbar() {
        return this.baseStatusbar;
    }

    public void setBaseStatusbar(String baseStatusbar) {
        this.baseStatusbar = baseStatusbar;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_LOGO_HEIGHT"
     *             length="5"
     *         
     */
    @Column(name="BASE_LOGO_HEIGHT",nullable=true)
    public Integer getBaseLogoHeight() {
        return this.baseLogoHeight;
    }

    public void setBaseLogoHeight(Integer baseLogoHeight) {
        this.baseLogoHeight = baseLogoHeight;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_STATUSBAR_TIME"
     *             length="5"
     *         
     */
    @Column(name="BASE_STATUSBAR_TIME",nullable=true)
    public Integer getBaseStatusbarTime() {
        return this.baseStatusbarTime;
    }

    public void setBaseStatusbarTime(Integer baseStatusbarTime) {
        this.baseStatusbarTime = baseStatusbarTime;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_INTERFACE_THEMES"
     *             length="50"
     *         
     */
    @Column(name="BASE_INTERFACE_THEMES",nullable=true)
    public String getBaseInterfaceThemes() {
        return this.baseInterfaceThemes;
    }

    public void setBaseInterfaceThemes(String baseInterfaceThemes) {
        this.baseInterfaceThemes = baseInterfaceThemes;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_LOGIN_THEMES"
     *             length="32"
     *         
     */
    @Column(name="BASE_LOGIN_THEMES",nullable=true)
    public String getBaseLoginThemes() {
        return this.baseLoginThemes;
    }

    public void setBaseLoginThemes(String baseLoginThemes) {
        this.baseLoginThemes = baseLoginThemes;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_DEFAULT_START_MENU"
     *             length="32"
     *         
     */
    @Column(name="BASE_DEFAULT_START_MENU",nullable=true)
    public String getBaseDefaultStartMenu() {
    	if(this.baseDefaultStartMenu==null){
    		return "nav_0001";
    	}else{
    		return this.baseDefaultStartMenu;
    	}
    }

    public void setBaseDefaultStartMenu(String baseDefaultStartMenu) {
        this.baseDefaultStartMenu = baseDefaultStartMenu;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_MENU_ICON"
     *             length="32"
     *         
     */
    @Column(name="BASE_MENU_ICON",nullable=true)
    public String getBaseMenuIcon() {
        return this.baseMenuIcon;
    }

    public void setBaseMenuIcon(String baseMenuIcon) {
        this.baseMenuIcon = baseMenuIcon;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_IS_SHOW"
     *             length="10"
     *         
     */
    @Column(name="BASE_IS_SHOW",nullable=true)
    public String getBaseIsShow() {
        return this.baseIsShow;
    }

    public void setBaseIsShow(String baseIsShow) {
        this.baseIsShow = baseIsShow;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_CITY"
     *             length="16"
     *         
     */
    @Column(name="BASE_CITY",nullable=true)
    public String getBaseCity() {
        return this.baseCity;
    }

    public void setBaseCity(String baseCity) {
        this.baseCity = baseCity;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_LOGIN_PIC"
     *             length="100"
     *         
     */
    @Column(name="BASE_LOGIN_PIC",nullable=true)
    public String getBaseLoginPic() {
        return this.baseLoginPic;
    }

    public void setBaseLoginPic(String baseLoginPic) {
        this.baseLoginPic = baseLoginPic;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_USER_PIC"
     *             length="100"
     *         
     */
    @Column(name="BASE_USER_PIC",nullable=true)
    public String getBaseUserPic() {
        return this.baseUserPic;
    }

    public void setBaseUserPic(String baseUserPic) {
        this.baseUserPic = baseUserPic;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_FOLDER_SIZE"
     *             length="5"
     *         
     */
    @Column(name="BASE_FOLDER_SIZE",nullable=true)
    public Integer getBaseFolderSize() {
        return this.baseFolderSize;
    }

    public void setBaseFolderSize(Integer baseFolderSize) {
        this.baseFolderSize = baseFolderSize;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_EMAIL_SIZE"
     *             length="5"
     *         
     */
    @Column(name="BASE_EMAIL_SIZE",nullable=true)
    public Integer getBaseEmailSize() {
        return this.baseEmailSize;
    }

    public void setBaseEmailSize(Integer baseEmailSize) {
        this.baseEmailSize = baseEmailSize;
    }

    /** 
     *            @hibernate.property
     *             column="BASE_MOBILE_STATE"
     *             length="1"
     *         
     */
    @Column(name="BASE_MOBILE_STATE",nullable=true)
    public String getBaseMobileState() {
        return this.baseMobileState;
    }

    public void setBaseMobileState(String baseMobileState) {
        this.baseMobileState = baseMobileState;
    }

    @Column(name="USERID",nullable=true)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("baseId", getBaseId())
            .toString();
    }

    /** 
     *            @hibernate.property
     *             column="BASE_MSG_SIZE"
     *             length="5"
     *         
     */
    @Column(name="BASE_MSG_SIZE",nullable=true)    
	public String getBaseMsgSize() {
		return baseMsgSize;
	}

	public void setBaseMsgSize(String baseMsgSize) {
		this.baseMsgSize = baseMsgSize;
	}
	
	
	@Column(name="OA_LEFTBG_PATH",nullable=true)
	public String getLeftbg() {
		return leftbg;
	}

	public void setLeftbg(String leftbg) {
		this.leftbg = leftbg;
	}
	
	
	@Column(name="FASTMENU_FONT_SIZE", nullable=true)
	public Integer getFastMenuFontSize() {
		return fastMenuFontSize;
	}

	public void setFastMenuFontSize(Integer fastMenuFontSize) {
		this.fastMenuFontSize = fastMenuFontSize;
	}
	
	
	@Column(name="PICPATH", nullable=true)
	public String getPicPath() {
		return PicPath;
	}

	public void setPicPath(String picPath) {
		PicPath = picPath;
	}
	
	
	
}