package com.strongit.oa.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.remind.RemindManager;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.ToaInfoDelegation;
import com.strongit.workflow.po.UserInfo;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程委托action
 * @author 胡丽丽
 * @date 2010-02-23  下午17：16
 *
 */
public   class ProcessAction extends BaseActionSupport {

	private Page page=  new Page(FlexTableTag.MAX_ROWS, true);
	private String deId;
	private String type;
	private String dhDeleProcess;
	private String deStartDate;
	private String deEndDate;
	private ToaInfoDelegation model=new ToaInfoDelegation();;
	private IProcessDefinitionService manager;
	private UupUtil uupBroker;
	private UserInfo userInfo;
	private String message;//提示信息内容
	private String userIds;//要提醒的用户ID
	private String usermessage;//用户自定义提醒信息
	protected @Autowired RemindManager remindService;//提醒服务类
	protected String remindType;//提醒方式：一般在前台页面用户勾选提醒方式，通过自动节点实现。
//	protected abstract String getModuleType();//抛出让子类实现:判断调用消息的模块
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public void setModel(ToaInfoDelegation model)
	{
		this.model = model;
	}

	public void setPage(Page page)
	{
		this.page = page;
	}

	public UupUtil getUupBroker() {
		return uupBroker;
	}

	@Autowired
	public void setUupBroker(UupUtil uupBroker) {
		this.uupBroker = uupBroker;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public Page getPage()
	{
		return page;
	}

	public ToaInfoDelegation getModel()
	{
		return model;
	}

	@Autowired
	public void setManager(IProcessDefinitionService aManager)
	{
		manager = aManager;
	}
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 保存流程委托
	 * @author 胡丽丽
	 * @date 2010-02-23 下午 17:17
	 */
	@Override
	public String save() throws Exception {
		try {
			if (new Long(0).equals(model.getDeId())) {
				model.setDeId(null);
			}
			UserInfo userInfo = (UserInfo) uupBroker.getCurrentUserInfo();
			/**
			 * 将businessId设置成String
			 */
			model.setDeUserId(userInfo.getUserId());
			model.setDeIsStart("0");
			model.setDeIsOuttime("0");
			
			String[] processes = this.dhDeleProcess.split("\\|");
			
			manager.addDelegationDetail(model, processes);	
			//提醒用户ID列表
			List<String> userlist=new ArrayList<String>();
			userlist.add(userIds);
			if(usermessage==null||"".equals(usermessage)){
				usermessage="您有一个委派任务";
			}
			//将提醒(方式|内容)存储在session中
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", usermessage);
			cxt.getSession().put("moduleType", "");//调用消息模块的类型
			remindService.sendRemind(userlist,null);
			//addActionMessage("信息保存成功");
			StringBuffer str=new StringBuffer();
			str.append("<script> alert('信息保存成功');window.dialogArguments.document.location.reload();window.close();</script>");
			return renderHtml(str.toString());
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getDeId()
	{
		return deId;
	}
	
	public void setDeId(String deId)
	{
		this.deId = deId;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getDhDeleProcess()
	{
		return dhDeleProcess;
	}
	
	public void setDhDeleProcess(String dhDeleProcess)
	{
		this.dhDeleProcess = dhDeleProcess;
	}
	
	public String getDeEndDate()
	{
		return deEndDate;
	}
	
	public void setDeEndDate(String deEndDate)
	{
		this.deEndDate = deEndDate;
	}
	
	public String getDeStartDate()
	{
		return deStartDate;
	}
	
	public void setDeStartDate(String deStartDate)
	{
		this.deStartDate = deStartDate;
	}

	public String getUsermessage() {
		return usermessage;
	}

	public void setUsermessage(String usermessage) {
		this.usermessage = usermessage;
	}

	

}
