//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\address\\AddressAction.java

package com.strongit.oa.address;

import java.io.File;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;


import com.opensymphony.xwork2.ActionContext;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.strongit.oa.address.util.CSVUtil;
import com.strongit.oa.address.util.StringUtil;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaAddressMail;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      个人通讯录Action
 */
@ParentPackage("default")
public class AddressAction extends BaseActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5931092668488897263L;
	private String id;//个人通讯录中的记录ID.多条记录ID以逗号隔开
	private String groupId;//组ID
	private String groupName;//组名
	public ToaAddress model = new ToaAddress();//联系人对象
    private String typewei;
    private IUserService user;
	public String getTypewei() {
		return typewei;
	}

	public void setTypewei(String typewei) {
		this.typewei = typewei;
	}

	private ToaAddress oaAddress;//联系人对象
	private AddressManager manager;
	private AddressGroupManager groupManager;
	@Autowired AddressOrgManager addressOrgManager;
	private Page<ToaAddress> page = new Page<ToaAddress>(FlexTableTag.MAX_ROWS, true);
	private File upload;//导入导出用到
	private String userId;//在个人桌面发送邮件时验证用户是否配了默认邮箱
	private String type; //通过此标示来决策去个人通讯录还是去系统通讯录中所选用户的Email
	
	private String parentId; //添加选择人员树，的父节点
	
	Page<Object[]> pageUser = new Page<Object[]>(FlexTableTag.MAX_ROWS,true);//OA选择人员列表对象

	/**
	 * 空的构造方法
	 */
	public AddressAction() {}

	/**
	 * @param aManager AddressManager 注入的manager对象
	 */
	@Autowired
	public void setManager(AddressManager aManager) {
		manager = aManager;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	/**
	 * @author 邓志城
	 * description:获取联系人列表 如果组ID为null,则加载第一个组的联系人
	 */
	public String list() throws Exception {
		/*if (groupId == null || "".equals(groupId)) { 
			List<ToaAddressGroup> group = groupManager.getGroupList();
			if (group != null && group.size() > 0) {
				ToaAddressGroup addrGroup = group.get(0);
				groupId = addrGroup.getAddrGroupId();
				groupName = addrGroup.getAddrGroupName();
			} else {
				groupName = "您的通讯录中还没有组,<a title='点击此处创建' href='#' onclick='addGroup();'>点击此处创建组</a>";
			}
		} else {
			setGroupName(groupManager.getGroupById(groupId).getAddrGroupName());
		}*/
		if(groupId != null && !"".equals(groupId)){
			model.setGroupName(groupManager.getGroupById(groupId).getAddrGroupName());
		}else{
			model.setGroupName("用户列表");
		}
		if(model.getName()!=null && !"".equals(model.getName())){
			model.setName(model.getName().trim());
		}
		if(model.getTel1()!=null && !"".equals(model.getTel1())){
			model.setTel1(model.getTel1().trim());
		}
		if(model.getMobile1()!=null && !"".equals(model.getMobile1())){
			model.setMobile1(model.getMobile1().trim());
		}
		page = manager.getAddressList(page, groupId,model);
		return SUCCESS;
	}

	/**
	 * @author 郑志斌
	 * description:获取联系人列表 如果组ID为null,则加载第一个组的联系人
	 */
	public String showlist() throws Exception {
		String userId = user.getCurrentUser().getUserId();
		if(type == null || "".equals(type) || type.equals("docAddress")){//个人通讯录
			
			pageUser = manager.getAddressUserList(pageUser, groupId, model.getName(),userId,typewei);
			
		}else if(type.equals("orgAddress")){//系统通讯录
			pageUser = manager.getSystemAddressList(pageUser, groupId, model.getName(),userId,typewei);
		}else if(type.equals("userGroup")){//用户组
			pageUser = manager.getGroupAddressList(pageUser, groupId, model.getName(),userId,typewei);
		}
		/**
		 * 已办事宜  我的在办文件  ---反馈   标识 typewei=feedBack
		 * 反馈人员--过滤掉自己 
		 * **/
		if(typewei!=null && !"".equals(typewei) && "feedBack".equals(typewei)){ 
			List<Object[]> result=pageUser.getResult();
			for(int i=0;i<result.size();i++){
				Object[] objs=result.get(i);
				if(userId.equals(objs[0])){
					result.remove(i);
					i--;
				}
			}
			pageUser.setResult(result);
		}
		return "showlist";
	}

	/**
	 * 全选所有人员
	 * @author:邓志城
	 * @date:2010-6-29 下午07:48:01
	 * @return
	 * @throws Exception
	 */
	public String doSelectAllUser() throws Exception {
		String users = "";
		List<Object[]> userObjList = null;
		JSONArray userArray = new JSONArray();//组装用户姓名和用户id字符串
		try{
			String userName = model.getName();
			userName = URLDecoder.decode(userName, "utf-8");
			if(type == null || "".equals(type) || type.equals("docAddress")){//个人通讯录
				userObjList = manager.getAddressList(groupId, userName);
			}else if(type.equals("orgAddress")){//系统通讯录
				userObjList = manager.getSystemAddressList(groupId, userName);
			}else if(type.equals("userGroup")){//用户组通讯录
				userObjList = manager.getGroupAddressList(new Page<Object[]>(), groupId, userName,null,null).getResult();
			}
			if(userObjList != null && !userObjList.isEmpty()){
				for(Object[] objs : userObjList){
					JSONObject objUser = new JSONObject();
					objUser.put("userId", objs[0]);
					objUser.put("userName", objs[1]);
					userArray.add(objUser);
				}
			}
			users = userArray.toString();
			logger.info(users);
		}catch(Exception e){
			logger.error("全选通讯录人员出错。", e);
			users = "-1";
		}
		return this.renderText(users);
	}
	
	/**
	 * 转到选择人员树形，人员列表显示界面
	 * @author:邓志城
	 * @date:2010-6-29 下午05:26:27
	 * @return
	 * @throws Exception
	 */
	public String selectperson() throws Exception{
		
		return "treeandpersonal";
	}
	
	/**
	 * 通讯录人员列表
	 * 默认展现第一个组下的人员列表
	 * 判断依据为groupId是否存在。
	 * @author:邓志城
	 * @date:2009-12-9 上午10:20:05
	 * @return
	 * @throws Exception
	 */
	public String userlist() throws Exception {
		page = manager.getAddressList(page, groupId,model);
		return "userlist";
	}
	
	/**
	 * 保存通讯录信息
	 */
	public String save()throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		model.setToaAddressGroup(groupManager.getGroupById(groupId));
		manager.addAddress(model);
		String email = getRequest().getParameter("email");
		String defaultEmail = getRequest().getParameter("default");//获取设置的默认邮箱地址
		if (null != email && !"".equals(email)) {
			String[] emails = email.split(",");
			for (int i = 0; i < emails.length; i++) {
				ToaAddressMail mail = new ToaAddressMail();
				mail.setMail(emails[i]);
				if (null != defaultEmail && !"".equals(defaultEmail)) {
					if (emails[i].equals(defaultEmail)) {
						mail.setIsDefault("1");
					}
				}
				mail.setToaAddress(model);
				manager.addAddressMail(mail);
			}

		}
		addActionMessage("添加成功!");
		return "add";
	}

	/**
	 * @return java.lang.String
	 * @roseuid 495041D1035B
	 */
	public String delete()throws Exception {
		try {
			manager.deleteAddress(id);
			renderText("删除成功!");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * author:dengzc 
	 * description:从通讯录中导出 
	 * modifyer: description:
	 * 
	 * @return
	 */
	public String export()throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		HttpServletRequest request   = getRequest();
		String column = request.getParameter("headerColumn");
		String[] header = column.split(",");
		String exportExt = request.getParameter("exportExt");
		if(null == column || "".equals(column)){
			addActionMessage("未选择列字段!导出失败!");
			return "export";
		}
		// 获取所有联系人信息
		/*if(groupId == null || "".equals(groupId)){ 
			throw new SystemException("组不存在,导出数据失败!");
		}*/
		List<ToaAddress> listAddress = manager.getUserAddress(groupId);
		CSVUtil.exportCSV(header,getResponse(),listAddress,exportExt,groupName);
		return null;
	}

	/**
	 * author:dengzc
	 * description:转到导出数据页面
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String initExport() throws Exception {
//		if(groupName!=null && !"".equals(groupName)){
//			groupName = URLDecoder.decode(groupName, "utf-8");
//			model.setGroupName(groupName);
//		}
		if(groupId != null && !"".equals(groupId)){
			model.setGroupName(groupManager.getGroupById(groupId).getAddrGroupName());
		}else{
			model.setGroupName("用户列表");
		}
		return "export";
	}
	
	/**
	 * author:dengzc
	 * description:读取文件内容
	 * modifyer:
	 * description:
	 * @return
	 */
	public String importFile()throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		String read = "";
		read = CSVUtil.importCSV(upload);
		LogPrintStackUtil.printInfo(logger, "导入的文件内容是:"+read);
		LogPrintStackUtil.printInfo(logger, "文件编码为:"+System.getProperty( "file.encoding"));
		ActionContext.getContext().put("allContent", read);
		return "import";
	}

	/**
	 * author:dengzc
	 * description:导入数据到通讯录中
	 * modifyer:
	 * description:
	 * @return
	 */
	public String doImportFile()throws Exception{
		try {
			getRequest().setAttribute("backlocation", "javascript:cancel()");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String param = getRequest().getParameter("params");//获取所有需要导入的字段
			String[] params = param.split("Γ");//文件格式如:姓名=dengzc,aa%MSN=,,,
			String message = "";
			String email = "";
			String[] headers = new String[params.length];//字段名
			Map<String, String> map = new HashMap<String, String>();
			int columnCount = 0;
			for(int i=0;i<params.length;i++){
				String[] header_value = params[i].split("=");
				String header = header_value[0];
				headers[i] = header;
				String value = "";
				if(header_value.length != 1){
					value = header_value[1];
				}
				map.put(header, value);
				String[] values = StringUtil.getSplitArray(value, ',');
				columnCount = values.length;
			}	
			ToaAddressGroup group =  groupManager.getGroupById(groupId);
			for(int j=0;j<columnCount;j++){
				ToaAddress address = new ToaAddress();
				for(int k=0;k<headers.length;k++){
					String value = map.get(headers[k]);
					String[] values = StringUtil.getSplitArray(value, ',');
					if("姓名".equals(headers[k])){
						address.setName(values[j]); 
					}else if ("职务".equals(headers[k])) {
						address.setNickname(values[j]);
					} else if ("电子邮件".equals(headers[k])) {
						//address.setDefaultEmail(values[j]);
						email = values[j];
					} else if ("手机1".equals(headers[k])) {
						address.setMobile1(values[j]);
					} else if ("手机2".equals(headers[k])) {
						address.setMobile2(values[j]);
					} else if ("性别".equals(headers[k])) {
						if ("男".equals(values[j])) {
							address.setSex("1");
						} else {
							address.setSex("0"); 
						}
					} else if ("生日".equals(headers[k])) {
						if(null!=values[j] && !"".equals(values[j])){ 
							try {
								address.setBirthday(sdf.parse(values[j]));
							} catch (ParseException e) {
								e.printStackTrace();
								message = "您输入的日期格式不对!";
							}
						}
					} else if ("QQ".equals(headers[k])) {
						address.setQq(values[j]);
					} else if ("MSN".equals(headers[k])) {
						address.setMsn(values[j]);
					} else if ("主页".equals(headers[k])) {
						address.setHomepage(values[j]);
					} else if ("爱好".equals(headers[k])) {
						address.setLikeThing(values[j]);
					} else if ("国家".equals(headers[k])) {
						address.setCountry(values[j]);
					} else if ("家庭电话1".equals(headers[k])) {
						address.setTel1(values[j]);
					} else if ("家庭电话2".equals(headers[k])) {
						address.setTel2(values[j]);
					} else if ("省".equals(headers[k])) {
						address.setProvince(values[j]);
					} else if ("城市".equals(headers[k])) {
						address.setCity(values[j]);
					} else if ("传真".equals(headers[k])) {
						address.setFax(values[j]);
					} else if ("家庭地址".equals(headers[k])) {
						address.setAddress(values[j]);
					} else if ("公司".equals(headers[k])) {
						address.setCompany(values[j]);
					} else if ("职位".equals(headers[k])) {
						address.setPosition(values[j]);
					} else if ("部门".equals(headers[k])) {
						address.setDepartment(values[j]);
					} else if ("公司电话1".equals(headers[k])) {
						address.setCoTel1(values[j]);
					} else if ("公司电话2".equals(headers[k])) {
						address.setCoTel2(values[j]);
					} else if ("邮编".equals(headers[k])) {
						address.setZipcode(values[j]);
					} else if ("附注信息".equals(headers[k])) {
						address.setAddressRemark(values[j]);
					}
				}
				address.setToaAddressGroup(group);
				manager.addAddress(address);
				if(!"".equals(email)){
					ToaAddressMail mail = new ToaAddressMail();
					mail.setMail(email);
					mail.setIsDefault("1");
					mail.setToaAddress(address);
					manager.addAddressMail(mail);
				}
			}

			message = "成功导入联系人至联系组【"+group.getAddrGroupName()+"】!";
			renderText(message);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * @roseuid 495041D1037A
	 */
	protected void prepareModel()throws Exception {
		if (null != id) {
			oaAddress = manager.getAddressById(id);
		}
	}

	@Override
	public String input() throws Exception {
		return "add";
	}

	/**
	 * author:dengzc description:转到编辑联系人页面,传递所选择联系人的所有信息. modifyer: description:
	 * 
	 * @return
	 */
	public String initEdit()throws Exception {
		prepareModel();
		return "edit";
	}

	/**
	 * author:dengzc description: 修改通讯录信息.这里比较麻烦的是通讯录表和联系人邮件表有关联。
	 * 更新联系人信息时,如果邮件信息有改动,需要同步更新. 最开始的做法是,先获取某联系人的原邮件信息.再获取前台页面传回
	 * 的邮件信息.但这种做法太过复杂.因此考虑采用先删除原邮件信息再 添加新的邮件信息的方式。
	 *  modifyer: description:
	 * 
	 * @return
	 */
	public String edit()throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		ToaAddress address = manager.getAddressById(model.getAddrId());
		model.setToaAddressGroup(address.getToaAddressGroup());
		model.setToaAddressMails(address.getToaAddressMails());
		model.setUserId(address.getUserId());
		manager.editAddress(model);
		// 先删除邮件信息
		if(model.getToaAddressMails() != null && model.getToaAddressMails().size()>0){
			manager.deleteMail(model.getToaAddressMails());			
		}
		// 添加新的邮件信息
		String email = getRequest().getParameter("email");
		String defaultEmail = getRequest().getParameter("default");
		if (null != email && !"".equals(email)) {
			String[] emails = email.split(",");
			for (int i = 0; i < emails.length; i++) {
				ToaAddressMail mail = new ToaAddressMail();
				mail.setMail(emails[i]);
				if (null != defaultEmail && !"".equals(defaultEmail)) {
					if (emails[i].equals(defaultEmail)) {
						mail.setIsDefault("1");//默认邮箱地址标示为“1”
					}
				}
				mail.setToaAddress(model);
				manager.addAddressMail(mail);
			}

		}
		addActionMessage("修改成功!");
		return "edit";
	}

	/**
	 * author:dengzc
	 * description:获取用户默认邮箱
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String getUserDefaultEmail()throws Exception{
		String ret = "";
		try{
			ret = manager.getUserDefaultEmail("")==null?"":manager.getUserDefaultEmail("");
		}catch(Exception e){
			LogPrintStackUtil.printErrorStack(logger, e);
			ret = "error";
		}
		renderText(ret);
		return null;
	}

	/**
	 * author:dengzc
	 * description:根据用户ID获取用户Email
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String getUserEmail() throws Exception {
		String ret = "";
		try{
			ret = manager.getUserEmail(userId, type);
		}catch(Exception e){
			LogPrintStackUtil.printErrorStack(logger, e);
			ret = "error";
		}
		renderText(ret);
		return null;
	}
	
	public Object getModel() {
		return model;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Page<ToaAddress> getPage() {
		return page;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Autowired
	public void setGroupManager(AddressGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public ToaAddress getOaAddress() {
		return oaAddress;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Page<Object[]> getPageUser() {
		return pageUser;
	}

	public void setPageUser(Page<Object[]> pageUser) {
		this.pageUser = pageUser;
	}

	public String getType() {
		return type;
	}

}
