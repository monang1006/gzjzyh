package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_SYSTEMSET")
public class ToaSystemset implements Serializable  {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	  @GeneratedValue	
	private String systemsetId;
	/*
	 * 公文打印控制
	 * {0:'启用总份数打印控制' , 1:'启用每人份数打印控制'}
	 */
	private String gwcontrol;
	/*
	 * 电子签章控制
	 * {0:'软航电子签章' , 1:'金格电子签章'}
	 */
	//private String signcontrol;
	
	/*
	 * 初始密码登录后是否强制修改密码
	 * {'0':'否' , '1':'是' }
	 */
	private String uppass;
	
	/*
	 * 密码长度
	 * 最小多少位
	 */
	private int passMin;
	
	/*
	 * 密码长度
	 * 最大多少位
	 */
	private int passMax;
	
	/*
	 * 密码必须同时包含字母和数字
	 * {'0':'否' , '1':'是' }
	 */
	private String passSet;
	
	/*
	 * 登录错误次数控制
	 * {'0':'否' , '1':'是' }
	 */
	private String loginnum;
	
	/*
	 * 允许IE记忆用户名和密码
	 * {'0':'否' , '1':'是' }
	 */
	private String ieuserps;
	
	/*
	 * 是否启用USB用户KEY
	 * {'0':'否' , '1':'是' ,'2','两者都用'}
	 */
	private String usbkey;
	
	private String rtxIsEnable;		//rtx是否启用	
	
	private String rtxIsDefault;	//rtx是否默认登录
	
	private Integer funcMenuFontSize;	//功能菜单字体大小
	
	private Integer manaMenuFontSize;	//管理菜单字体大小
	
	private Integer fastMenuFontSize;	//快捷菜单字体大小
	
	private String suggestionStyle;  //审批意见输入模式 "0":"笔形图标"， "1":"快捷办理"

	private String suggestionShowName;	//流程审批意见是否显示节点名  0:不显示；1：显示
	
	private Integer refreshTime;       //桌面自动刷新时间间隔
	
	private String isUseCASign;  	//是否启用ca认证 “0”：“不启用”，“1”：“启用”
	
    private byte[] ipadbg;//ipad登陆界面背景图
    
    private Date  ipadBgUpdate;


    private String tempFilePath;
    
    /** 张磊 2012-03-14 */
    private String isBirthReminder ;//是否启用生日提醒功能  “0”：“不启用”，“1”：“启用”
    
    private Date reminderDate ; //发送提醒时间
    
    private String reminderText ;//配置的短信模板
    
    /**
     * add by hecj 2014-1-11
     * 用于向ios推送消息的设置，保存的格式是xml
     */
    private String pushSetting;



	/**
	 * @author zzbsteven 2010/4/22 14:14
	 * 档案管理——”归档确认“中设置申请归档和直接归档
	 * {‘0’：‘申请归档’，‘1’：‘直接归档’}
	 */
	private String archiveIsEnable ;
	
	/**
	 * 表单中显示领导所审批意见时间格式
	 */
	private String showDateFormat;
	/**
	 * 是否启用新的office控件 0:否  1：是
	 */
	private String officeNew;
	
	public ToaSystemset() {
	}

	public ToaSystemset(String systemsetId, String gwcontrol, String uppass,
			int passMin, int passMax, String passSet, String loginnum,
			String ieuserps, String usbkey,String rtxIsEnable,String rtxIsDefault,Integer funcMenuFontSize,
			Integer manaMenuFontSize,Integer fastMenuFontSize,String archiveIsEnable,String suggestionStyle,
			String suggestionShowName, Integer refreshTime) {
		this.systemsetId = systemsetId;
		this.gwcontrol = gwcontrol;
		//this.signcontrol = signcontrol;
		this.uppass = uppass;
		this.passMin = passMin;
		this.passMax = passMax;
		this.passSet = passSet;
		this.loginnum = loginnum;
		this.ieuserps = ieuserps;
		this.usbkey = usbkey;
		this.rtxIsEnable=rtxIsEnable;
		this.rtxIsDefault=rtxIsDefault;
		this.funcMenuFontSize=funcMenuFontSize;
		this.manaMenuFontSize=manaMenuFontSize;
		this.fastMenuFontSize=fastMenuFontSize;
		this.archiveIsEnable=archiveIsEnable;
		this.suggestionStyle=suggestionStyle;
		this.suggestionShowName = suggestionShowName;
		this.refreshTime = refreshTime;
	}

	@Id
    @Column(name="SYSTEMSET_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getSystemsetId() {
		return systemsetId;
	}

	public void setSystemsetId(String systemsetId) {
		this.systemsetId = systemsetId;
	}

	@Column(name="GWCONTROL", nullable=false)
	public String getGwcontrol() {
		return gwcontrol;
	}

	public void setGwcontrol(String gwcontrol) {
		this.gwcontrol = gwcontrol;
	}
	
	@Column(name="UPPASS", nullable=false)
	public String getUppass() {
		return uppass;
	}

	public void setUppass(String uppass) {
		this.uppass = uppass;
	}
	
	@Column(name="PASS_MIN", nullable=false)
	public int getPassMin() {
		return passMin;
	}

	public void setPassMin(int passMin) {
		this.passMin = passMin;
	}
	
	@Column(name="PASS_MAX", nullable=false)
	public int getPassMax() {
		return passMax;
	}

	public void setPassMax(int passMax) {
		this.passMax = passMax;
	}
	
	@Column(name="PASS_SET", nullable=false)
	public String getPassSet() {
		return passSet;
	}

	public void setPassSet(String passSet) {
		this.passSet = passSet;
	}
	
	@Column(name="LOGIN_NUM", nullable=false)
	public String getLoginnum() {
		return loginnum;
	}

	public void setLoginnum(String loginnum) {
		this.loginnum = loginnum;
	}

	@Column(name="IEUSERPS", nullable=false)
	public String getIeuserps() {
		return ieuserps;
	}

	public void setIeuserps(String ieuserps) {
		this.ieuserps = ieuserps;
	}
	
	@Column(name="USBKEY", nullable=false)
	public String getUsbkey() {
		return usbkey;
	}

	public void setUsbkey(String usbkey) {
		this.usbkey = usbkey;
	}
	
	public String toString() {
	    return new ToStringBuilder(this).append("systemsetId", getSystemsetId()).toString();
	  }

	@Column(name="FASTMENU_FONT_SIZE", nullable=true)
	public Integer getFastMenuFontSize() {
		return fastMenuFontSize;
	}

	public void setFastMenuFontSize(Integer fastMenuFontSize) {
		this.fastMenuFontSize = fastMenuFontSize;
	}

	@Column(name="FUNCMENU_FONT_SIZE", nullable=true)
	public Integer getFuncMenuFontSize() {
		return funcMenuFontSize;
	}

	public void setFuncMenuFontSize(Integer funcMenuFontSize) {
		this.funcMenuFontSize = funcMenuFontSize;
	}

	@Column(name="MANAMENU_FONT_SIZE", nullable=true)
	public Integer getManaMenuFontSize() {
		return manaMenuFontSize;
	}

	public void setManaMenuFontSize(Integer manaMenuFontSize) {
		this.manaMenuFontSize = manaMenuFontSize;
	}

	@Column(name="RTX_ISDEFAULT", nullable=false)
	public String getRtxIsDefault() {
		return rtxIsDefault;
	}

	public void setRtxIsDefault(String rtxIsDefault) {
		this.rtxIsDefault = rtxIsDefault;
	}

	@Column(name="RTX_ISENABLE", nullable=false)
	public String getRtxIsEnable() {
		return rtxIsEnable;
	}

	public void setRtxIsEnable(String rtxIsEnable) {
		this.rtxIsEnable = rtxIsEnable;
	}
	@Column(name="ARCHIVE_ISENABLE", nullable=false) 
	public String getArchiveIsEnable() {
		return archiveIsEnable;
	}
	public void setArchiveIsEnable(String archiveIsEnable) {
		this.archiveIsEnable = archiveIsEnable;
	}
	
	@Column(name="SUGGESTION_STYLE", nullable=true) 
	public String getSuggestionStyle() {
		return suggestionStyle;
	}

	public void setSuggestionStyle(String suggestionStyle) {
		this.suggestionStyle = suggestionStyle;
	}


	@Column(name="SUGGESTION_SHOWNAME", nullable=true) 
	public String getSuggestionShowName() {
		return suggestionShowName;
	}

	/**
	 * @author:luosy
	 * @description: 0:不显示；1：显示
	 * @date : 2011-6-9
	 * @modifyer:
	 * @description:
	 * @param suggestionShowName
	 */
	public void setSuggestionShowName(String suggestionShowName) {
		this.suggestionShowName = suggestionShowName;
	}

	@Column(name="SHOWDATE_FORMAT", nullable=true) 
	public String getShowDateFormat() {
		return showDateFormat;
	}

	public void setShowDateFormat(String showDateFormat) {
		this.showDateFormat = showDateFormat;
	}
	@Column(name="REFRESH_TIME", nullable=true)
	public Integer getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}
	
	@Column(name = "IPADBG",columnDefinition = "BLOB")
    @Lob
	public byte[] getIpadbg() {
		return ipadbg;
	}

	public void setIpadbg(byte[] ipadbg) {
		this.ipadbg = ipadbg;
	}
	
	@Column(name="ISUSE_CASIGN", nullable=true)
	public String getIsUseCASign() {
		return isUseCASign;
	}

	public void setIsUseCASign(String isUseCASign) {
		this.isUseCASign = isUseCASign;
	}
	
	@Transient
	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}
	
	@Column(name="IPADBGUPDATE", nullable=true)
	public Date getIpadBgUpdate() {
		return ipadBgUpdate;
	}

	public void setIpadBgUpdate(Date ipadBgUpdate) {
		this.ipadBgUpdate = ipadBgUpdate;
	}

	/**
	 * @author 张磊
	 * @description: 0:不启用；1：启用
	 * @date: 2012-03-14
	 * @description: 是否启用生日提醒，以及配置短信模板
	 */
	@Column(name="IS_BIRTH_REMINDER",nullable=true)
	public String getIsBirthReminder() {
		return isBirthReminder;
	}

	public void setIsBirthReminder(String isBirthReminder) {
		this.isBirthReminder = isBirthReminder;
	}

	@Column(name="REMINDER_DATE",nullable=true)
	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}
	@Column(name="REMINDER_TEXT",nullable=true)
	public String getReminderText() {
		return reminderText;
	}

	public void setReminderText(String reminderText) {
		this.reminderText = reminderText;
	}

	@Column(name="IOS_PUSH_SETTTING")
	public String getPushSetting() {
		return pushSetting;
	}

	@Column(name="OFFICE_NEW")
	public String getOfficeNew() {
		return officeNew;
	}

	public void setOfficeNew(String officeNew) {
		this.officeNew = officeNew;
	}

	public void setPushSetting(String pushSetting) {
		this.pushSetting = pushSetting;
	}
}
