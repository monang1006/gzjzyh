package com.strongit.oa.desktop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import com.strongit.oa.bo.ToaDesktopSectionsel;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.desktop.util.TempPo;
import com.strongit.oa.infopub.column.IColumnService;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 可选桌面模块菜单管理类
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/desktopSectionsel.action") })
public class DesktopSectionselAction extends BaseActionSupport{

	private DesktopSectionselManager sectionselManager;
	
	private IColumnService infoPubColumn;
	
	private final static String sysCode="001";				//OA系统的权限编码为001
	
	private List<TempPo> objList;
	
	private BaseOptPrivilManager privilManager;
	
	@Autowired
	public void setSectionselManager(DesktopSectionselManager sectionselManager) {
		this.sectionselManager = sectionselManager;
	}

	@Autowired
	public void setInfoPubColumn(IColumnService infoPubColumn) {
		this.infoPubColumn = infoPubColumn;
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
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:26:09 PM
	 * @desc: 生成对桌面选择模块的列表
	 * @return
	 * @throws Exception List<String>
	 */
	public String list() throws Exception {
		// TODO Auto-generated method stub
		HttpServletRequest request=this.getRequest();
		String root=request.getContextPath();										//获得当前工程的根路径
		List<ToaDesktopSectionsel> list=sectionselManager.findAll();				//在栏目表中所存在的权限编码
		List<String> auList=privilManager.getAllOptPrivilSysCode();					//当前用户所存在权限的编码
		StringBuffer html=new StringBuffer();
		objList=new ArrayList<TempPo>();
		html.append("<html>")
			.append("<head><title>桌面选择</title></head>")
			.append("<body>");
		for(int i=0;i<list.size();i++){												//在当前用户权限下获得用户可选择的桌面列表
			if(auList.contains("001-"+list.get(i).getPrivilId())){
				ToaUumsBaseOperationPrivil privil=privilManager.getPrivilInfoByPrivilSyscode(list.get(i).getPrivilId());
				html.append("<div align=\"center\" class=\"panelcon\">\n");
				html.append("    <img src=\""+root+"/oa/image/desktop/menu/1/icon/none.gif\" align=\"absmiddle\" class=\"panelicon\">"+"<span style='width:100px'>"+privil.getPrivilName()+"</span>"+"<img src=\""+root+"/oa/image/desktop/index_i/add2.gif\" align=\"absmiddle\" class=\"paneladdimg\" onclick=\"addBlock('"+privil.getPrivilSyscode()+"','"+privil.getPrivilName()+"');\">\n");
				html.append("</div>\n");
			}
		}
		List<ToaInfopublishColumn> colList=infoPubColumn.getMyColumn();			   //获得信息发布模块栏目表中的权限过滤后的栏目列表
		for(int i=0;i<colList.size();i++){
			ToaInfopublishColumn col=colList.get(i);
			html.append("<div align=\"center\" class=\"panelcon\">\n");
			html.append("    <img src=\""+root+"/oa/image/desktop/menu/1/icon/none.gif\" align=\"absmiddle\" class=\"panelicon\">"+"<span style='width:100px'>"+col.getClumnName()+"</span>"+"<img src=\""+root+"/oa/image/desktop/index_i/add2.gif\" align=\"absmiddle\" class=\"paneladdimg\" onclick=\"addBlock('pub-"+col.getClumnId()+"','"+col.getClumnName()+"');\">\n");
			html.append("</div>\n");
		}
		html.append("</body>")
			.append("</html>");
		return this.renderText(html.toString());
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
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:26:09 PM
	 * @desc: 获得当前用户的权限列表
	 * @return
	 * @throws Exception List<String>
	 */
	public List<String> getAuthoritiesCodeList() throws Exception{
		UserDetails user=this.getUserDetails();
		GrantedAuthority[] au = user.getAuthorities();
		List<String> auList=new ArrayList<String>();
		for(int i=0;i<au.length;i++){
			if(au[i].getAuthority().startsWith(sysCode)){
				auList.add(au[i].getAuthority());
			}
		}
		return auList;
	}

}
