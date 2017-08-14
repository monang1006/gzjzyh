package com.strongit.oa.msgaccount;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMessageAccount;
import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.msgaccount.util.TelNumUtil;
import com.strongit.oa.sms.SmsConfManager;
import com.strongit.oa.sms.SmsManager;
import com.strongit.oa.smsplatform.util.PropertiesUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.utils.StringUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/msgAccount.action") })
public class MsgAccountAction extends BaseActionSupport{

	private MsgAccountManager msgManager;
	
	private SmsConfManager smsConfManager;
	
	private List<ToaSmsCommConf> sendList; 
	
	private Page<ToaMessageAccount> page=new Page<ToaMessageAccount>(FlexTableTag.MAX_ROWS,true);
	
	private ToaMessageAccount message;
	
	private SmsManager smsManager;
	
	private String nowTel;						//对应当前选中的短信猫SM卡号码
	
	private String mobile;						//移动发送短信需要发送内容
	
	private String tel;							//电信查询短信需要发送内容
	
	private String union;						//联通查询短信需要发送内容
	
	private String con;							//需要发送的内容
	
	private String ids;							//传递页面与后台类之间的ID
	
	private String simNumber;					//SIM卡号码
	
	private String operNumber;					//运营商号码
	
	private String simContent;					//短信内容
	
	private Date beginDate;						//起始时间
	
	private Date endDate;						//截至时间
	
	@Autowired
	public void setMsgManager(MsgAccountManager msgManager) {
		this.msgManager = msgManager;
	}

	@Autowired
	public void setSmsConfManager(SmsConfManager smsConfManager) {
		this.smsConfManager = smsConfManager;
	}
	
	@Autowired
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(msgManager.deleteObj(ids)){
			return renderText("true");
		}else{
			return renderText("false");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		page=msgManager.getMessageAccount(page, this.simNumber, this.operNumber, this.simContent, this.beginDate, this.endDate);
		if(StringUtil.isNotEmpty(simNumber)){
		     this.simNumber = URLDecoder.decode(simNumber,"UTF-8");
		}
		if(StringUtil.isNotEmpty(operNumber)){
		    this.operNumber = URLDecoder.decode(operNumber,"UTF-8");
		}
		if(StringUtil.isNotEmpty(simContent)){
		    this.simContent =URLDecoder.decode(simContent,"UTF-8");
		}
		return SUCCESS;
	}
	
	public String info() throws Exception {
		message=msgManager.getObject(ids);
		return "info";
	}
	
	public String sendMsg() throws Exception{
		String oper=TelNumUtil.getMobileOper(nowTel);
		if(oper==null){
			return renderText("no");
		}else{
			String[] temp=getArray(PropertiesUtil.getProperty(oper));
			if(temp!=null){
				String operNumber=temp[0];
				try{
					smsManager.sendSingleSmsBySimNum(operNumber, con, nowTel, new OALogInfo("手机短信-余额查询-『发送』：("+operNumber+")"+con));
					return renderText("success");
				}catch(Exception e){
					return renderText("error");
				}
			}else{
				return renderText("error");
			}
		}
	}

	public String send() throws Exception {
		String[] arr=getArray(PropertiesUtil.getProperty(PropertiesUtil.mobile));
		if(arr!=null){
			mobile=arr[1];
			arr=null;
		}
		arr=getArray(PropertiesUtil.getProperty(PropertiesUtil.telecom));
		if(arr!=null){
			tel=arr[1];
			arr=null;
		}
		arr=getArray(PropertiesUtil.getProperty(PropertiesUtil.unicom));
		if(arr!=null){
			union=arr[1];
		}
		sendList=smsConfManager.getSmsConfList();
		return "send";
	}
	
	public String[] getArray(String info){
		if(info!=null){
			if(info.indexOf(",")!=0){
				return info.split(",");
			}else{
				return null;
			}
		}else{
			return null;
		}
	}	
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ToaSmsCommConf> getSendList() {
		return sendList;
	}

	public void setSendList(List<ToaSmsCommConf> sendList) {
		this.sendList = sendList;
	}

	public void setNowTel(String nowTel) {
		this.nowTel = nowTel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUnion() {
		return union;
	}

	public void setUnion(String union) {
		this.union = union;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public Page<ToaMessageAccount> getPage() {
		return page;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public ToaMessageAccount getMessage() {
		return message;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if(endDate==null){
			
		}else{
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
		}
		this.endDate = endDate;
	}

	public String getOperNumber() {
		return operNumber;
	}

	public void setOperNumber(String operNumber) {
		this.operNumber = operNumber;
	}

	public String getSimContent() {
		return simContent;
	}

	public void setSimContent(String simContent) {
		this.simContent = simContent;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

}
