package com.strongit.doc.agencygroup;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.doc.bo.ToaGroupAgency;
import com.strongit.doc.bo.ToaGroupDet;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 机构分组管理Action.
 * 
 * @author
 * @company
 * @date
 * @version 2.0.2.3
 * @comment
 * @email
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "agencyGroup.action", type = ServletActionRedirectResult.class) })
public class AgencyGroupAction extends BaseActionSupport {

	ToaGroupAgency model = new ToaGroupAgency(); // 定义MODEL对象.
	ToaGroupDet modelDets = new ToaGroupDet();

	Page<ToaGroupDet> page = new Page<ToaGroupDet>(20, true); // 定义列表分页对象

	@Autowired
	AgencyGroupManager manager; // 注入服务类对象
	private String groupId;// 组ID
	private String groupName;// 组名
	private String orgs;// 导入机构。格式：【org1,org2,...】
	private String orgCode;// 组ID
	private String orgName;// 组名

	@Autowired
	public AgencyGroupAction() {
	}

	/**
	 * 标签分页列表.
	 */
	@Override
	public String list() throws Exception {
		String strGroup = manager.getAgencyGroupList();
		getRequest().setAttribute("group", strGroup);

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (groupId != null) {
			model = manager.getGroupById(groupId);
		}
	}

	/**
	 * 将选择的机构导入到组中
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String doImport() throws Exception {
		try {
			manager.importPublic2Groups(groupId, orgs);
			return renderText(SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return renderText(ERROR);
		}
	}

	/**
	 * author:luosy description: 查找重复的用户名 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkUserName() throws Exception {
		try {
			String ret = manager.checkOrgName(groupId, orgs);
			return renderText(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return renderText(ERROR);
		}
	}

	/**
	 * 显示要导入的机构列表
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String importOrgList() throws Exception {
		// if("1".equals(orgId)){//取当前用户所在机构人员
		// TUumsBaseOrg baseOrg =
		// manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
		// orgId = baseOrg.getOrgId();
		// orgName = baseOrg.getOrgName();
		// }
		// if(orgId == null || "".equals(orgId)){
		// orgName = "用户列表";
		// }
		;
		page = manager.getOrgs(page,orgCode,orgName);
		return "import";
	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		OALogInfo info;
		User user = manager.getUserService().getCurrentUser();
		if (model.getGroupAgencyId() != null
				&& "".equals(model.getGroupAgencyId())) {
			model.setGroupAgencyId(null);
			info = new OALogInfo(user.getUserName() + " 修改发文组名称为："
					+ model.getGroupAgencyName());

		} else {
			info = new OALogInfo(user.getUserName() + " 新增发文组："
					+ model.getGroupAgencyName());
		}
		manager.save(model, info);
		return RELOAD;
	}

	/**
	 * 保存记录.
	 * 
	 * @author:
	 * @date:
	 * @return
	 */
	public void doSave() {
		String ret = "0";
		try {
			manager.save(model); // 保存对象
		} catch (Exception e) {
			logger.error("保存出错。", e);
			ret = "-1";
		}
		this.renderText(ret);
	}

	public AgencyGroupManager getManager() {
		return manager;
	}

	public void setManager(AgencyGroupManager manager) {
		this.manager = manager;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setModel(ToaGroupAgency model) {
		this.model = model;
	}

	public String getOrgs() {
		return orgs;
	}

	public void setOrgs(String orgs) {
		this.orgs = orgs;
	}

	public ToaGroupAgency getModel() {
		return model;
	}

	public Page<ToaGroupDet> getPage() {
		return page;
	}

	public void setPage(Page<ToaGroupDet> page) {
		this.page = page;
	}

	@Override
	public String delete() throws Exception {
		try {
			ToaGroupAgency ag = manager.getGroupById(groupId);
			OALogInfo info = new OALogInfo(manager.getUserService()
					.getCurrentUser().getUserName()
					+ " 删除发文组：" + ag.getGroupAgencyName());
			manager.deleteGroup(groupId, info);
			renderText("删除成功！");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return "add";
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
