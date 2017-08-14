package com.strongit.oa.infopub.column;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.hibernate.connection.UserSuppliedConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.TempPo;
import com.strongmvc.webapp.action.BaseActionSupport;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-11 上午09:25:38
 * Autour: lanlc
 * Version: V1.0
 * Description：栏目管理
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "column.action", type = ServletActionRedirectResult.class) })
public class ColumnAction extends BaseActionSupport {

	private String clumnId; // 栏目ID
	private String pclumnId; // 父栏目ID
	private String clumnViewName; // 栏目名称
	private ToaInfopublishColumn model = new ToaInfopublishColumn();
	private ColumnManager manager;
	private List columnList; // 栏目list集
	private IUserService userService;
	private String[] hasChild; // 子类别
	private String userId;// 权限用户
	private String userName;// 用户名称
	private String parentClumnName; // 父栏目名称
	private List<String> processList; // 流程信息集
	String processTypeId = "1"; // 栏目流程类型为1
	private String adminUser;// 栏目管理用户
	private String isadmin;
	private String adminUserName;// 管理栏目的用户名称

	private String chooseType;// 按部门、岗位、用户组类型展示人员

	private final static String DEPARTMENT = "bm";// 部门
	private final static String POST = "gw";// 岗位
	private final static String GROUP = "yhz";// 用户组
	private AddressOrgManager personmanager;

	public String getChooseType() {
		return chooseType;
	}

	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * author:lanlc description:栏目树父类目录展现 modifyer:
	 * 
	 * @return
	 */
	public String tree() throws Exception {
		
		columnList = manager.getMyUpdateColumn();
		if (columnList.isEmpty()) {
			model = new ToaInfopublishColumn();
			model.setClumnDir("Column"); // 栏目目录
			model.setClumnName("默认栏目"); // 栏目名称
			model.setClumnTips("默认栏目"); // 栏目提示
			model.setClumnParent("0"); // 父栏目
			model.setClumnCreatetime(new Date()); // 创建日期
			model.setClumnCreatuser(userService.getCurrentUser().getUserName()); // 创建人
			model.setClumnIsnewopen("1"); // 是否新窗口
			model.setClumnIsprivate("1"); // 不加权
			manager.saveColumn(model);
			columnList = manager.getParentColumn();
		}
		Collections.sort(columnList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
				ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
				return  p1.getClumnId().compareTo(p2.getClumnId());
			}
		});
		ToaInfopublishColumn column = null;
		List<ToaInfopublishColumn> subColumnList;
		hasChild = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			column = (ToaInfopublishColumn) columnList.get(i);
			subColumnList = manager.getSubColumn(column.getClumnId());
			if (subColumnList.size() > 0) {
				hasChild[i] = "1";
			} else {
				hasChild[i] = "0";
			}
		}
		return "tree";
	}

	/**
	 * description:选择接收人 description:转到选择人员页面，增加了按岗位、用户组选择人员功能。
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String persontree() throws Exception {
		return "chooseperson";
	}

	/**
	 * author:dengzc description:按类型展示人员 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String choosetree() throws Exception {
		List<TempPo> lst = new ArrayList<TempPo>();
		if (DEPARTMENT.equals(chooseType)) {
			lst = personmanager.getUserTreeByDept();
		} else if (POST.equals(chooseType)) {
			lst = personmanager.getUserTreeByPost();
		} else if (GROUP.equals(chooseType)) {

		}
		getRequest().setAttribute("orgList", lst);
		return "choosetreeperson";
	}

	/**
	 * author:lanlc description:异步加载树结构 modifyer:
	 * 
	 * @return
	 */
	public String syntree() throws Exception {
		StringBuffer str = new StringBuffer();
		List<ToaInfopublishColumn> folds = manager.getSubColumn(clumnId);
		for (int i = 0; i < folds.size(); i++) {
			ToaInfopublishColumn fold = folds.get(i);
			List<ToaInfopublishColumn> subColumnList = manager
					.getSubColumn(fold.getClumnId());
			if (!subColumnList.isEmpty()) {
				str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + ">");
				str.append("<span>" + fold.getClumnName() + "</span>");
				str.append("<ul class=ajax>");
				str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + i + ">{url:"
						+ getRequest().getContextPath()
						+ "/infopub/column/column!syntree.action?clumnId="
						+ fold.getClumnId() + "}</li>");
				str.append("</ul>");
				str.append("</li>");
			} else {
				str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + ">");
				str.append("<span>" + fold.getClumnName());
				str.append("</span>");
				str.append("</li>");
			}
		}
		renderHtml(str.toString());
		return null;
	}

	/**
	 * author:lanlc description: 栏目合并、移动初始化数 modifyer:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String chooseTree() throws Exception {
		columnList = manager.getParentColumn();
		Collections.sort(columnList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
				ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
				System.out.println(p1.getClumnCreatetime().getTime()-p2.getClumnCreatetime().getTime());
				return p1.getClumnId().compareTo(p2.getClumnId());
			}
		});
		ToaInfopublishColumn column = null;
		List<ToaInfopublishColumn> subColumnList;
		hasChild = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			column = (ToaInfopublishColumn) columnList.get(i);
			subColumnList = manager.getSubColumn(column.getClumnId());
			if (subColumnList.size() > 0) {
				hasChild[i] = "1";
			} else {
				hasChild[i] = "0";
			}
		}
		return "choosetree";
	}

	/**
	 * author:lanlc description:合并栏目 modifyer:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String margeColumn() throws Exception {
		if (clumnId != null && pclumnId != null) {
			model = manager.getColumn(pclumnId);
			ToaInfopublishColumn col = new ToaInfopublishColumn();
			col = manager.getColumn(clumnId);
			if(col.getClumnParent().equals("0")){
				model.setClumnParent("0");
			}
			String parentClumnIds = manager.getparentColumnIds(pclumnId,"");
			if(parentClumnIds.indexOf(clumnId)>-1){
				this.renderText("不能将当前栏目合并到其子栏目下！");
				return null;
			}
			
			
			if (manager.margeColumn(clumnId, model,new OALogInfo("判断是否成功合并栏目"))) {// 栏目合并成功
				manager.delColumn(clumnId,new OALogInfo("删除指定栏目"));
			}
			this.renderText("margetrue");
		} else {
			this.renderText("合并栏目不成功");
		}
		return null;
	}

	/**
	 * author:lanlc description:移动栏目 modifyer:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String moveColumn() throws Exception {
		if (clumnId != null && pclumnId != null) {
			prepareModel();
			String parentClumnIds = manager.getparentColumnIds(pclumnId,"");
			if(parentClumnIds.indexOf(model.getClumnId())>-1){
				this.renderText("不能将当前栏目移动到其子栏目下！");
				return null;
			}
			model.setClumnParent(pclumnId);
			manager.saveColumn(model,new OALogInfo("移动栏目"));
			this.renderText("movetrue");
		} else {
			this.renderText("移动栏目不成功！");
		}
		return null;
	}

	/**
	 * author:lanlc description:设置栏目为顶级栏目 modifyer:
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	public String setParentColumn() throws Exception {
		if (clumnId != null) {
			prepareModel();
			if (model.getClumnParent() != "0"
					&& !"0".equals(model.getClumnParent())) {
				model.setClumnParent("0");
				manager.saveColumn(model,new OALogInfo("设置栏目置顶"));
				return renderText("settrue");
			} else {
				return renderText("setfalse");
			}
		}
		return null;
	}

	/**
	 * author:lanlc description: 异步删除栏目 modifyer:
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String delete() throws Exception {
		if (!"".equals(clumnId) && clumnId != null) {
			List<ToaInfopublishColumn> folds = manager.getSubColumn(clumnId);
			boolean iscolumnArticl = manager.columnArticl(clumnId);
			if (folds.size() > 0 || iscolumnArticl) { // 栏目下存在子栏目或栏目下有文章
				this.renderText("delfalse");
			} else {
				manager.delColumn(clumnId,new OALogInfo("删除栏目"));
				// addActionMessage("删除信息成功");
			}

		}
		return null;
	}

	
	/**
	 * author:lanlc description:初始化添加栏目 modifyer:
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String input() throws Exception {
		String userid = userService.getCurrentUser().getUserId();
		getRequest().setAttribute("userid",userid);
		if (!"".equals(clumnId) && clumnId != null) {
		User user= userService.getCurrentUser();
		String privateuserid = user.getUserId();
		String viewPrivate =	manager.getViewPrivate(clumnId,privateuserid);//查看用户是拥有修改还是查看权限
		getRequest().setAttribute("viewPrivate", viewPrivate);
			clumnViewName = manager.getClumnName(clumnId);
			// 查找流程类型对应的流程
			List<Object[]> pList = manager
					.getProcessOwnedByProcessType(processTypeId);
			processList = new ArrayList<String>();
			if (pList.size() > 0) {
				for (int i = 0, n = pList.size(); i < n; i++) {
					Object[] process = pList.get(i);
					String proName = (String) process[0]; // 流程名称
					processList.add(proName);
				}
			}
			if (!"0".equals(model.getClumnParent())) {
				parentClumnName = manager.getClumnName(model.getClumnParent());
			}
			if ("1".equals(model.getClumnIsprivate())) { // 不加权，则将原来的加权删除
				manager.delColumnPrivil(model.getClumnId());
			} else {
				StringBuffer userNameInfo = new StringBuffer("");
				String id = manager.getColumnPrivil(clumnId, "0"); // 用户ID字符串，以,分隔
				if (id.length() > 0) {
					userId = id.substring(0, id.length() - 1);
					if (userId.length() > 0) {
						String[] ids = userId.split(",");
						for (int i = 0, n = ids.length; i < n; i++) {
							userNameInfo.append(manager.getUserName(ids[i]))
									.append(",");
						}
					}
					userName = userNameInfo.substring(0,
							userNameInfo.length() - 1);
				}
				// 有修改权限的用户
				StringBuffer nameinfo = new StringBuffer();
				String adminid = manager.getColumnPrivil(clumnId, "1");
				if (adminid.length() > 0) {
					adminUser = adminid.substring(0, adminid.length() - 1);
					if (adminUser.length() > 0) {
						String[] adminids = adminid.split(",");
						for (int i = 0, n = adminids.length; i < n; i++) {
							nameinfo.append(manager.getUserName(adminids[i]))
									.append(",");
						}
					}
					adminUserName = nameinfo
							.substring(0, nameinfo.length() - 1);
				}
			}
		}
		return "input";
	}

	/**
	 * author:lanlc description:新建子栏目或顶级栏目 modifyer:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String newSubColumn() throws Exception {
		String userid = userService.getCurrentUser().getUserId();
		getRequest().setAttribute("userid",userid);
		if (!"".equals(clumnId) && clumnId != null) {
			clumnViewName = manager.getClumnName(clumnId);
		}
		// 查找流程类型对应的流程
		List<Object[]> pList = manager
				.getProcessOwnedByProcessType(processTypeId);
		processList = new ArrayList<String>();
		if (pList.size() > 0) {
			for (int i = 0, n = pList.size(); i < n; i++) {
				Object[] process = pList.get(i);
				String proName = (String) process[0]; // 流程名称
				processList.add(proName);
			}
		}
		return "subcolumn";
	}

	/**
	 * author:lanlc description: modifyer:
	 */
	@Override
	public String list() throws Exception {
		return null;

	}

	/**
	 * author:lanlc description: modifyer:
	 * @throws Exception
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (clumnId != null) {
			model = manager.getColumn(clumnId);
			System.out.println("***********" + model.getClumnIsprivate());
			model.setClumnLastmodifiedtime(new Date());
			model.setClumnLastmodifieduser(userService.getCurrentUser()
					.getUserName());
		} else {
			model = new ToaInfopublishColumn();
			model.setClumnCreatetime(new Date());
			model.setClumnCreateduser(userService.getCurrentUser()
					.getUserName());
		}
	}

	/**
	 * author:lanlc description:保存栏目 modifyer:
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String save() throws Exception {
		if ("".equals(model.getClumnId()) || model.getClumnId() == null) {
			model.setClumnId(null);
			model.setClumnCreatetime(new Date());
			model.setClumnCreateduser(userService.getCurrentUser()
					.getUserName());
		} else {
			model.setClumnLastmodifiedtime(new Date());
			model.setClumnLastmodifieduser(userService.getCurrentUser()
					.getUserName());
		}
		if ("".equals(model.getClumnParent()) || model.getClumnParent() == null) {
			model.setClumnParent("0"); // 栏目父节点为0
		}
		if ("".equals(model.getClumnIsprivate())
				|| model.getClumnIsprivate() == null || userId == null) {
			model.setClumnIsprivate("1"); // 未设置权限则设置为1
		}
		manager.saveColumn(model,new OALogInfo("保存栏目"));
		if (model.getClumnIsprivate().equals("0")) {
			if (null != model.getClumnId() && !"".equals(model.getClumnId())) {
				manager.delColumnPrivil(model.getClumnId(),new OALogInfo("删除栏目权限"));
			}
			
			if (userId != null && !"".equals(userId)) { // 添加的用户ID则保存
				manager.saveColumnPrivil(model, userId, "0",new OALogInfo("保存栏目权限用户"));
			}
			
			if (adminUser != null && !adminUser.equals("")) {// 栏目管理权限
				manager.saveColumnPrivil(model, adminUser, "1",new OALogInfo("保存栏目权限用户"));
				String nowUserId = userService.getCurrentUser().getUserId();// 当前用户ID
				String[] userid = adminUser.split(",");
				for (int i = 0; i < userid.length; i++) {
					if (userid[i].equals(nowUserId) || userid[i] == nowUserId) {// 判断管理权限用户是否有当前用户
						return "temp";
					}
				}
				manager.saveColumnPrivil(model, userService.getCurrentUser()
						.getUserId(), "1",new OALogInfo("保存栏目权限用户"));

			} else {
				manager.saveColumnPrivil(model, userService.getCurrentUser()
						.getUserId(), "1",new OALogInfo("保存栏目权限用户"));
			}

		}
		return "temp";
	}

	@Autowired
	public void setManager(ColumnManager manager) {
		this.manager = manager;
	}

	public String getClumnId() {
		return clumnId;
	}

	public void setClumnId(String clumnId) {
		this.clumnId = clumnId;
	}

	public ToaInfopublishColumn getModel() {
		return model;
	}

	public void setModel(ToaInfopublishColumn model) {
		this.model = model;
	}

	public List getColumnList() {
		return columnList;
	}

	public void setColumnList(List columnList) {
		this.columnList = columnList;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String[] getHasChild() {
		return hasChild;
	}

	public void setHasChild(String[] hasChild) {
		this.hasChild = hasChild;
	}

	public String getClumnViewName() {
		return clumnViewName;
	}

	public void setClumnViewName(String clumnViewName) {
		this.clumnViewName = clumnViewName;
	}

	public String getPclumnId() {
		return pclumnId;
	}

	public void setPclumnId(String pclumnId) {
		this.pclumnId = pclumnId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getParentClumnName() {
		return parentClumnName;
	}

	public void setParentClumnName(String parentClumnName) {
		this.parentClumnName = parentClumnName;
	}

	public List<String> getProcessList() {
		return processList;
	}

	public void setProcessList(List<String> processList) {
		this.processList = processList;
	}

	public String getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(String isadmin) {
		this.isadmin = isadmin;
	}

	@Autowired
	public void setPersonmanager(AddressOrgManager personmanager) {
		this.personmanager = personmanager;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

}
