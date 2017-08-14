package com.strongit.doc.agencygroup;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.doc.bo.ToaGroupDet;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.GlobalBaseData;
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
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "groupDet.action", type = ServletActionRedirectResult.class) })
public class GroupDetAction extends BaseActionSupport {

	ToaGroupDet model = new ToaGroupDet(); // 定义MODEL对象.

	Page<ToaGroupDet> page = new Page<ToaGroupDet>(20, true); // 定义列表分页对象

	@Autowired
	GroupDetManager manager; // 注入服务类对象 detId
	private String detId;
	private String groupId;// 组ID
	private String groupName;// 组名
	private String orgCode;// 组ID
	private String orgName;// 组名
	private AgencyGroupManager groupManager;

	public GroupDetManager getManager() {
		return manager;
	}

	public void setManager(GroupDetManager manager) {
		this.manager = manager;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ToaGroupDet getModel() {
		return model;
	}

	public void setModel(ToaGroupDet model) {
		this.model = model;
	}

	@Autowired
	public GroupDetAction() {
	}

	/**
	 * 标签分页列表.
	 */
	@Override
	public String list() throws Exception {
		// if (groupId == null || "".equals(groupId)) {
		// List<ToaAddressGroup> group = groupManager.getGroupList();
		// if (group != null && group.size() > 0) {
		// ToaAddressGroup addrGroup = group.get(0);
		// groupId = addrGroup.getAddrGroupId();
		// groupName = addrGroup.getAddrGroupName();
		// } else {
		// groupName = "您的通讯录中还没有组,<a title='点击此处创建' href='#'
		// onclick='addGroup();'>点击此处创建组</a>";
		// }
		// } else {
		// setGroupName(groupManager.getGroupById(groupId).getAddrGroupName());
		// }
		if (groupId != null && !"".equals(groupId) && !"null".equals(groupId)) {
			setGroupName(groupManager.getGroupById(groupId)
					.getGroupAgencyName());
		} else {
			setGroupName("机构列表");
		}
		page = manager.getAgencyList(page, groupId, orgCode, orgName);

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		if (model.getGroupDetsId() != null && "".equals(model.getGroupDetsId())) {
			model.setGroupDetsId(null);
		}
		manager.save(model);
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

	public Page<ToaGroupDet> getPage() {
		return page;
	}

	public void setPage(Page<ToaGroupDet> page) {
		this.page = page;
	}

	@Override
	public String delete() throws Exception {
		if (detId != null) {
			String[] ids = detId.split(",");
			/*
			 * for(String id : ids){ manager.delete(id); }
			 */
			User user = manager.getUserService().getCurrentUser();
			if (ids != null) {
				for (String id : ids) {
					ToaGroupDet det = manager.getGroupDetById(id);
					if (det != null) {
						String orgNam1 = manager.getUserService().getOrgInfoByOrgId(
								det.getOrgId()).getOrgName();
						// OALogInfo info = new OALogInfo(
						// getText(GlobalBaseData.SENDDOC_DELETE_DOC,
						// new String[] {
						// user.getUserName(),
						// det.getToaGroupAgency()
						// .getGroupAgencyName(),
						// orgName }));
						OALogInfo info = new OALogInfo(user.getUserName()
								+ " 删除发文组-"
								+ det.getToaGroupAgency().getGroupAgencyName()
								+ " 中：" + orgNam1);
						manager.delete(id, info); 
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDetId() {
		return detId;
	}

	public void setDetId(String detId) {
		this.detId = detId;
	}

	@Autowired
	public void setGroupManager(AgencyGroupManager groupManager) {
		this.groupManager = groupManager;
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
