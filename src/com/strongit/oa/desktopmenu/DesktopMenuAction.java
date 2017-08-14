package com.strongit.oa.desktopmenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import com.strongit.oa.bo.ToaDesktopSectionsel;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.desktop.DesktopSectionselManager;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/desktopMenu.action") })
public class DesktopMenuAction extends BaseActionSupport {

	private DesktopSectionselManager sectionselManager;
	
	private List<ToaUumsBaseOperationPrivil> selected;
	
	private List<ToaUumsBaseOperationPrivil> maySelect;
	
	private BaseOptPrivilManager privilManager;
	
	
	private String idStr;
	
	private final static String sysCode="001";
	
	@Autowired
	public void setSectionselManager(DesktopSectionselManager sectionselManager) {
		this.sectionselManager = sectionselManager;
	}
	
	@Autowired
	public void setPrivilManager(BaseOptPrivilManager privilManager) {
		this.privilManager = privilManager;
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
		selected=new ArrayList<ToaUumsBaseOperationPrivil>();
		maySelect=new ArrayList<ToaUumsBaseOperationPrivil>();
		//将已经在桌面菜单中显示的内容存入
		List<ToaDesktopSectionsel> list=sectionselManager.deskMenuShow();
		for(int i=0;i<list.size();i++){
			ToaDesktopSectionsel sel=list.get(i);
			ToaUumsBaseOperationPrivil privil=privilManager.getPrivilInfoByPrivilSyscode(sel.getPrivilId());
			//增加处理空值 邓志城 2010年11月29日10:28:17
			if(privil.getPrivilId() != null){
				selected.add(privil);
			}else{
				logger.error("权限:"+sel.getPrivilId()+"不存在！");
				ToaDesktopSectionsel obj = sectionselManager.findByCode(sel.getPrivilId());
				if(obj != null) {
					sectionselManager.delObj(obj);
				}
				logger.error("权限:"+sel.getPrivilId()+"被删除！");
			}
			//end--------------------------------------
		}
		
		List<ToaUumsBaseOperationPrivil> auList=privilManager.getCurrentUserPrivilLst(true);//当前用户所存在权限的编码
		//将未出现在桌面菜单中的选项写入备选列表
		for(int i=0;i<auList.size();i++){
			ToaUumsBaseOperationPrivil privil=privilManager.getPrivilInfoByPrivilSyscode(auList.get(i).getPrivilSyscode());
			String des=privil.getPrivilDescription();
			if(des!=null&&des.startsWith("个人桌面使用")&&!selected.contains(privil)){		//描述项中以个人桌面使用开头并且不在已选项目中
				maySelect.add(privil);
			}
		}
		return SUCCESS;
	}
	
	public String edit() throws Exception{
		List<String> selectedIds=new ArrayList<String>();
		String[] ids=idStr.split(",");
		List<String> list=null;
		if(idStr!=null&&!"".equals(idStr)){		
			list=Arrays.asList(ids);			//前台传入的目前的顺序的list
		}else{
			list=new ArrayList<String>();		//前台传入值为空或者为null则不进行保存
		}
		List<ToaDesktopSectionsel> selectedList=sectionselManager.deskMenuShow();
		//将其中的id形成List
		for(int i=0;i<selectedList.size();i++){
			selectedIds.add(selectedList.get(i).getPrivilId());
		}
		
		for(int i=0;i<list.size();i++){
			String temp=list.get(i);
			if(selectedIds.contains(temp)){
				//就将其的order改变
				ToaDesktopSectionsel obj=sectionselManager.findByCode(temp);
				if(obj.getSectionselOrderby()!=i+1){
					obj.setSectionselOrderby(i+1);
					sectionselManager.saveObj(obj);
				}
			}else{
				//添加一个
				ToaDesktopSectionsel obj=new ToaDesktopSectionsel();
				obj.setPrivilId(temp);
				obj.setSectionselOrderby(i+1);
				sectionselManager.saveObj(obj);
			}
		}
		
		for(int i=0;i<selectedList.size();i++){
			if(list.contains(selectedIds.get(i))){
				
			}else{
				ToaDesktopSectionsel obj=sectionselManager.findByCode(selectedIds.get(i));
				sectionselManager.delObj(obj);
			}
		}
		return this.renderText("true");
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

	public List<ToaUumsBaseOperationPrivil> getSelected() {
		return selected;
	}

	
	/*public List<String> getAuthoritiesCodeList() throws Exception{
		UserDetails user=this.getUserDetails();
		GrantedAuthority[] au = user.getAuthorities();
		List<String> auList=new ArrayList<String>();
		for(int i=0;i<au.length;i++){
			if(au[i].getAuthority().startsWith(sysCode)&& privilManager.checkPrivilBySysCode(au[i].getAuthority())){
				auList.add(au[i].getAuthority());
			}
		}
		return auList;
	}*/

	public List<ToaUumsBaseOperationPrivil> getMaySelect() {
		return maySelect;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
}
