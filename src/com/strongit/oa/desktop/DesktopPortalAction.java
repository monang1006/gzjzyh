package com.strongit.oa.desktop;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaDesktopWhole;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 门户管理
 * 
 * @author zouhr
 * 
 */

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "desktopPortal.action", type = ServletActionRedirectResult.class) })
public class DesktopPortalAction extends BaseActionSupport {

	private Page<ToaDesktopPortal> page = new Page(FlexTableTag.MAX_ROWS, true);

	private ToaDesktopPortal model = new ToaDesktopPortal();

	private DesktopPortalManager manager;

	private DesktopWholeManager wholeManager;

	private String portalId;

	private String portalName;

	private Map editMap = new HashMap();

	public DesktopPortalAction() {

		editMap.put("0", "<font color='#38d7ff'>不可编辑</font>");
		editMap.put("1", "<font color='#90036'>可编辑</font>");

	}

	/**
	 * 门户删除操作
	 */
	@Override
	public String delete() throws Exception {
		String msg = "";
		StringBuffer renderHtml = new StringBuffer("");
		try {
			String[] ids = portalId.split(",");

			for (int i = 0; i < ids.length; ++i) {
				// String id =
				// this.manager.getPortal(ids[i]).getPortalDesktopId();
				List<ToaDesktopWhole> hols = wholeManager
						.getDesktopWholeListById(ids[i]);
				for (Iterator<ToaDesktopWhole> it = hols.iterator(); it
						.hasNext();) {
					ToaDesktopWhole hole = it.next();
					this.wholeManager.deleteDesktop(hole.getDesktopId());
				}
				manager.deleteByPortalId(ids[i]);

			}
			this.manager.delPortal(this.portalId);
		} catch (Exception e) {
			msg = "门户信息删除失败！";
		}
		renderHtml.append("<script>");
		if (msg != "") {
			renderHtml.append("alert('删除成功');");
		}
		renderHtml
				.append(
						"top.frames('perspective_top').location.reload(); window.location='")
				.append(getRequest().getContextPath()).append(
						"/desktop/desktopPortal.action'; </script>");

		return renderHtml(renderHtml.toString());
	}

	public String portalBackup() throws Exception {
		String msg = "";
		StringBuffer renderHtml = new StringBuffer("");
		List<ToaDesktopWhole> holes = wholeManager
				.getDesktopWholeListNomoren(portalId);
	 if(holes!=null && holes.size()!=0){
		for (Iterator<ToaDesktopWhole> it = holes.iterator(); it.hasNext();) {
			ToaDesktopWhole hole = it.next();
			this.wholeManager.deleteDesktop(hole.getDesktopId());
		}
	 }
		renderHtml.append("<script>");
		if (msg != "") {
			renderHtml.append("alert('还原恢复成功');");
		}
		renderHtml
		.append(
				"window.location='")
		.append(getRequest().getContextPath()).append(
				"/desktop/desktopPortal.action'; </script>");

         return renderHtml(renderHtml.toString());
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		prepareModel();
		return "input";
	}

	/**
	 * 展现门户信息
	 */
	@Override
	public String list() throws Exception {
		this.page = this.manager.getPortalPages(page, portalName);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (null != this.portalId) {
			this.model = this.manager.getPortal(this.portalId);
		} else {
			this.model = new ToaDesktopPortal();
		}

	}

	/**
	 * 保存门户操作
	 */
	@Override
	public String save() throws Exception {

		if (model.getPortalId() != null && !"".equals(model.getPortalId())) {
		   if(model.getIsEdit().equals("0")){
			   List<ToaDesktopWhole> holes = wholeManager
				.getWholesNomorenAndUser(model.getPortalId());
		if(holes!=null && holes.size()!=0){
		    for(Iterator<ToaDesktopWhole> it = holes.iterator(); it.hasNext();) {
			ToaDesktopWhole hole = it.next();
			this.wholeManager.deleteDesktop(hole.getDesktopId());
		 }
		}
		   }
		    model.setSetTime(new Date());
			this.manager.updatePortal(model);
		} else {
			// ToaDesktopWhole whole =
			// wholeManager.getDesktopWholeByPortal(null);
			// whole.setDesktopIsdefault("2");//类型
			// model.setPortalDesktopId(whole.getDesktopId());

			this.manager.addPortal(model);
			ToaDesktopWhole whole = wholeManager.getDesktopWholeByPortalId(
					null, model.getPortalId());

		}

		return "init";
	}

	/*
	 * 
	 * Description: param: @author 彭小青 @date Sep 26, 2009 11:31:28 AM
	 */
	public String isExistName() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		boolean flag = true;
		try {
			flag = manager.isExistName(portalId, portalName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag == true)
			renderText("0");
		else
			renderText("1");
		return null;
	}


	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getPortalId() {
		return portalId;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}

	public Page getPage() {
		return page;
	}

	@Autowired
	public void setManager(DesktopPortalManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setWholeManager(DesktopWholeManager wholeManager) {
		this.wholeManager = wholeManager;
	}

	public Map getEditMap() {
		return editMap;
	}

	public void setEditMap(Map editMap) {
		this.editMap = editMap;
	}
}
