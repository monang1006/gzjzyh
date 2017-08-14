package com.strongit.tag.web.menu;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.tag.web.util.ResponseUtils;

/**
 * <p>Title: TreeTag.java</p>
 * <p>Description: 通用树形结构标签</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-10-28 16:35:16
 * @version  1.1(添加属性:rootcheck)
 */

public class MenuTag extends TagSupport{

	private static final long serialVersionUID = 6982808609998526604L;
	
	private String id;                     // 创建不同的树
	private String dealclass;
	private List data;						//前台传入数据
	private List menus;
	private String root;
	

	private String menuTagStyle;	//菜单样式 style.css
	private String menuStyle;		//菜单样式 winxp.css
	
	ImenuNode dealNode;

	public void setData(List data) {
		this.data = data;
	}

	public void setDealclass(String dealclass) {
		this.dealclass = dealclass;
	}


	public int  doStartTag(){
		Class cls = null;
		try {
			dealNode=(ImenuNode)cls.forName(dealclass).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		StringBuffer s=new StringBuffer();
		System.out.println(getRootPath());
		
		if(null==menuTagStyle||"".equals(menuTagStyle)||"null".equals(menuTagStyle)){
			s.append("<link href='"+getRootPath()+"/common/menustoolbar/menu/style.css?11212' rel='stylesheet' type='text/css' />");
		}else{
			s.append("<link href='"+getRootPath()+"/"+menuTagStyle+"' rel='stylesheet' type='text/css' />");
		}
		//s.append("<link href='"+menuTagStyle+"' rel='stylesheet' type='text/css' />");
		s.append("<SCRIPT src='"+getRootPath()+"/common/menustoolbar/menu/poslib.js' type=text/javascript></SCRIPT>");
		s.append("<SCRIPT src='"+getRootPath()+"/common/menustoolbar/menu/scrollbutton.js' type=text/javascript></SCRIPT>");
		s.append("<SCRIPT src='"+getRootPath()+"/common/menustoolbar/menu/menu4.js' type=text/javascript></SCRIPT>");
		s.append("<script>");
		
		if(null==menuStyle||"".equals(menuStyle)||"null".equals(menuStyle)){
			s.append("Menu.prototype.cssFile = '"+getRootPath()+"/common/menustoolbar/menu/winxp.css';");
		}else{
			s.append("Menu.prototype.cssFile = '"+getRootPath()+"/"+menuStyle+"';");
		}
		s.append("Menu.prototype.mouseHoverDisabled = false;");
		s.append("Menu.prototype.showTimeout = 20;");
		s.append("Menu.prototype.closeTimeout = 20;");
		s.append("var tmp;");
	
		s.append("var mb = new MenuBar;");
		
		int menuCount=0;
		for(int j=0;j<menus.size();j++){
			ToaUumsBaseOperationPrivil mo=new ToaUumsBaseOperationPrivil();
			mo=(ToaUumsBaseOperationPrivil)menus.get(j);
			s.append("var menus_"+mo.getPrivilSyscode()+" = new Menu();");
			
			
			for(int i=0;i<data.size();i++){
				Object obj=data.get(i);
				dealNode.setObject(obj);
				if(ismenusone(mo.getPrivilSyscode(),dealNode.getTagNodeId())){
			
				if(dealNode.getTagParentId()==null||"".equals(dealNode.getTagParentId())||"0".equals(dealNode.getTagParentId())){
					//s.append("var menu_"+dealNode.getTagNodeId()+" = new Menu();");
					//s.append("menu_"+dealNode.getTagNodeId()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '/StringOA/common/js/menu/menu/file.png' ) );");
				}else{
					//s.append("var menu_"+dealNode.getTagParentId()+" = new Menu();");
					//s.append("menu_"+dealNode.getTagNodeId()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '/StringOA/common/js/menu/menu/file.png' ) );");
				}
			//	System.out.println(root+"/images"+dealNode.getNodeImg());
				if(dealNode.getTagNodeId().length()==8){
					if(ismenus(dealNode.getTagNodeId(),"one")){
						s.append("var menus_"+dealNode.getTagNodeId()+" = new Menu();");
						if(dealNode.getNodeImg()!=null){
					    s.append("menus_"+mo.getPrivilSyscode()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '"+root+"/images"+dealNode.getNodeImg()+"',menus_"+dealNode.getTagNodeId()+") );");
						}else{
							s.append("menus_"+mo.getPrivilSyscode()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '"+root+"/images/folder.gif',menus_"+dealNode.getTagNodeId()+") );");
						}
					    s.append(submenusItems(dealNode.getTagNodeId(),"menus_"+dealNode.getTagNodeId()));
					}else{
						if(dealNode.getNodeImg()!=null){
					    s.append("menus_"+mo.getPrivilSyscode()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images/"+dealNode.getNodeImg()+"') );");
						}else{
							s.append("menus_"+mo.getPrivilSyscode()+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images/ico2.gif') );");
						}
					}
					
					menuCount +=1;
				}
				
				s.append("\n");
				}
				dealNode.setObject(null);
			}
		s.append("mb.add(tmp = new MenuButton( '<span>"+mo.getPrivilName()+"</span>', menus_"+mo.getPrivilSyscode()+" ) );");
		
//			if(menuCount<13){
//				for(int mc=menuCount;mc<14;mc++){
//					s.append("menus_"+mo.getPrivilSyscode()+".add(tmp = new MenuItem('','javascript:;', '') );");
//				}
//			}
//			menuCount=0;
		}
		s.append("mb.write();");
		s.append("</script>");
		ResponseUtils.write(pageContext, s.toString());
		return EVAL_PAGE;
		
	}
	
	public void release(){
		
		super.release();	
		
	}
	private String submenusItemsTwo(String nodeid,String menuname){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<data.size();i++){
			Object obj=data.get(i);
			dealNode.setObject(obj);
			if(dealNode.getTagNodeId().length()==16){
				if(dealNode.getTagNodeId().substring(0, 12).equals(nodeid)){
					if(dealNode.getNodeImg()!=null){
						s.append(""+menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images"+dealNode.getNodeImg()+"') );");
					}else{
						s.append(""+menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images/ico2.gif') );");
					}
					
				}
			}
		    dealNode.setObject(null);
		}
		return s.toString();
	}
	private String submenusItems(String nodeid,String menuname){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<data.size();i++){
			Object obj=data.get(i);
			dealNode.setObject(obj);
			if(dealNode.getTagNodeId().length()==12){
				if(dealNode.getTagNodeId().substring(0, 8).equals(nodeid)){
				if(ismenusTwo(dealNode.getTagNodeId())){
					 s.append("var menus_"+dealNode.getTagNodeId()+" = new Menu();");
					 if(dealNode.getNodeImg()!=null){
					 s.append(menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '"+root+"/images"+dealNode.getNodeImg()+"',menus_"+dealNode.getTagNodeId()+") );");
					 }else{
						 s.append(menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"', '', '"+root+"/images/folder.gif',menus_"+dealNode.getTagNodeId()+") );");
					 }
					 s.append(submenusItemsTwo(dealNode.getTagNodeId(),"menus_"+dealNode.getTagNodeId()));
				}else{
						if(dealNode.getNodeImg()!=null){
						s.append(""+menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images"+dealNode.getNodeImg()+"') );");
						}else{
							s.append(""+menuname+".add(tmp = new MenuItem('"+dealNode.getTagNodeName()+"',\""+dealNode.getClick()+"\", '"+root+"/images/ico2.gif') );");
						}
					//System.out.println(dealNode.getNodeImg());
				}}
			}
		    dealNode.setObject(null);
		}
		return s.toString();
	}
	private boolean ismenusone(String parentnodeid,String nodeid){
		if(nodeid!=null && nodeid.substring(0, 4).equals(parentnodeid))
		{
			return true;
		}else{
			return false;
		}
	}
	private boolean ismenusTwo(String nodeid){
		for(int i=0;i<data.size();i++){
			ToaUumsBaseOperationPrivil m=(ToaUumsBaseOperationPrivil)data.get(i);
			//dealNode.setObject(obj);
			
			
	
				if(m.getPrivilSyscode().length()==16){
					if(m.getPrivilSyscode().substring(0, 12).equals(nodeid))
					return true;
				}
		}
		return false;
	}
	private boolean ismenus(String nodeid,String child){
		for(int i=0;i<data.size();i++){
			ToaUumsBaseOperationPrivil m=(ToaUumsBaseOperationPrivil)data.get(i);
			//dealNode.setObject(obj);
			
			if(child.equals("one")){
			if(m.getPrivilSyscode()!=null && m.getPrivilSyscode().length()==12){
				if(m.getPrivilSyscode().substring(0, 8).equals(nodeid))
				return true;
			}}
			/*else if(child.equals("two")){
				if(m.getPrivilSyscode().length()==16){
					if(m.getPrivilSyscode().substring(0, 12).equals(nodeid))
					return true;
				}
			}*/
		}
		return false;
	}
	
	
	public String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}





	public List getMenus() {
		return menus;
	}





	public void setMenus(List menus) {
		this.menus = menus;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getMenuTagStyle() {
		return menuTagStyle;
	}

	public void setMenuTagStyle(String menuTagStyle) {
		this.menuTagStyle = menuTagStyle;
	}


	private String getFrameRoot() {
		String frameroot= (String) pageContext.getSession().getAttribute("frameroot"); 
		if(frameroot==null||frameroot.equals("")||frameroot.equals("null")){
			frameroot= "/frame/theme_gray";
		}
		return	getRootPath() + frameroot;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle;
	}
}
