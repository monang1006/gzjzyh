package com.strongit.oa.wap;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.address.AddressGroupManager;
import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.address.util.UserBean;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.wap.util.Data;
import com.strongit.oa.wap.util.Form;
import com.strongit.oa.wap.util.Item;
import com.strongit.oa.wap.util.Row;
import com.strongit.oa.wap.util.Status;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      组织机构Action
 */
@ParentPackage("default")
public class AddressOrgAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2269536603864795631L;
	private AddressOrgManager manager;
	private String orgId;
	private String orgName;
	private String userName;//根据用户姓名搜索
	private String userId;	//根据用户ID查看用户详细信息
	private UserBean model = new UserBean();
	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(FlexTableTag.MAX_ROWS,true);
	private Page<TUumsBaseUser> userpage = new Page<TUumsBaseUser>(8,true);
	private Page<TUumsBaseUser> userpage2 = new Page<TUumsBaseUser>(8,true);
	private int currentPage;
	private String chooseType;//按部门、岗位、用户组类型展示人员
	private final static String DEPARTMENT = "bm";//部门
	private final static String POST = "gw";//岗位
	private final static String GROUP = "yhz";//用户组
	private String currentUserId;//获取当前用户ID
	private String type;//标示组织机构树的展示性质【导入到个人通讯录...】
	
	@Autowired public AddressGroupManager groupManager;	//wap开发添加
	@Autowired private IUserService userService;	//wap开发添加
	private String forward;								//wap开发添加
	private String groupId;								//wap开发添加
	private String moduleCode;							//wap开发添加
	private String searchOrgId;							//wap开发添加
	private int searchCurrentPage;						//wap开发添加
	private String searchUserName;						//wap开发新增
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	/**
	 * 获取组织机构列表,以树形式展现
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		List<Organization> lst = manager.getAllDeparments();
		List newList = new ArrayList();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(Iterator<Organization> it=lst.iterator();it.hasNext();){
			TempPo po = new TempPo();
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			po.setId(orgSysCode);
			po.setCodeId(org.getOrgId());
			po.setParentId(manager.findFatherCode(orgSysCode, config_orgCode, null));
			po.setName(org.getOrgName());
			po.setType(type);//这里暂时用于表示树用于导入系统人员
			newList.add(po);
			
		}
		
		getRequest().setAttribute("orgList", newList);
		
		return "tree";
	}

	/**
	 * author:dengzc
	 * description:选择接收人
	 * modifyer:邓志城
	 * description:转到选择人员页面，增加了按岗位、用户组选择人员功能。
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String tree()throws Exception{
//		return "chooseperson";
		return "selectperson";
	}

	/**
	 * @author:hecj
	 * @description:获取一级机构
	 * 
	 */
	public String getLevelOrg()throws Exception{
		/*List<Organization> lst = manager.getAllDeparments();
		List newList = new ArrayList();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		List tempList=new ArrayList();
		for(Iterator<Organization> it=lst.iterator();it.hasNext();){
			TempPo po = new TempPo();
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			
			if(org.getOrgParentId()==null||"".equals(org.getOrgParentId())||"0".equals(org.getOrgParentId())){
				po.setId(orgSysCode);
				po.setCodeId(org.getOrgId());
				po.setParentId(manager.findFatherCode(orgSysCode, config_orgCode, null));
				po.setName(org.getOrgName());
				po.setType(type);//这里暂时用于表示树用于导入系统人员
				newList.add(po);
				tempList.add(po);
			}
		}
		List orgList=new ArrayList();
		
		if(newList.size()>0){
			TempPo po=(TempPo)newList.get(0);
			orgList=manager.childOrg(po.getCodeId(),lst,config_orgCode);
			for(int i=0;i<orgList.size();i++){
				Organization org = (Organization)orgList.get(i);
				TempPo newpo = new TempPo();
				newpo.setId(org.getOrgSyscode());
				newpo.setCodeId(org.getOrgId());
				newpo.setParentId(manager.findFatherCode(org.getOrgSyscode(), config_orgCode, null));
				newpo.setName(org.getOrgName());
				newpo.setType(type);//这里暂时用于表示树用于导入系统人员
				tempList.add(newpo);
			}
		}*/
		
		List<Organization> lst = manager.getAllDeparments();
		List newList = new ArrayList();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(Iterator<Organization> it=lst.iterator();it.hasNext();){
			TempPo po = new TempPo();
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			po.setId(orgSysCode);
			po.setCodeId(org.getOrgId());
			po.setParentId(manager.findFatherCode(orgSysCode, config_orgCode, null));
			po.setName(org.getOrgName());
			po.setType(type);//这里暂时用于表示树用于导入系统人员
			newList.add(po);
			
		}
		List<TempPo> orgList = new ArrayList<TempPo>();
		for(int i=0;i<newList.size();i++){
			TempPo po=(TempPo)newList.get(i);
			if("".equals(po.getParentId())||po.getParentId()==null||"0".equals(po.getParentId())){
				orgList.add(po);
			}else{
				String parentId= manager.findFatherCode(po.getParentId(), config_orgCode, null);
				if("0".equals(parentId)||"".equals(parentId)){
					po.setName("----"+po.getName());
					orgList.add(po);
				}else{
					po.setName("--------"+po.getName());
					orgList.add(po);
				}
			}
		}
		/*Collections.sort(orgList,new Comparator<TempPo>(){
			public int compare(TempPo node1, TempPo node2) {
				String n1 = (String) node1.getId();
				String n2 = (String) node2.getId();
				
				return n1.compareTo(n2);
			}
		});*/
		getRequest().setAttribute("orgList", orgList);
		
		return "tree";
	}
	
	/**
	 * @author:hecj
	 * @description:获取一级机构以及它的分组机构
	 * 
	 */
	public String getOrgWithSub(String orgId)throws Exception{
		List<TempPo> orgList = new ArrayList<TempPo>();
		try{
			/*String parentOrgId="";
			List<Organization> lst = manager.getAllDeparments();
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			if(orgId!=null&&!"".equals(orgId)&&!"null".equals(orgId)){
				parentOrgId=orgId;
			}else{
				if(lst!=null&&lst.size()>0){
					for(Organization org:lst){
						if(org.getOrgParentId()==null||"".equals(org.getOrgParentId())||"0".equals(org.getOrgParentId())){
							parentOrgId=org.getOrgId();
						}
					}
				}
			}
			if(!"".equals(parentOrgId)){
				lst= manager.childOrg(parentOrgId, lst, config_orgCode);
				Organization parentOrg=manager.getOrgById(parentOrgId);
				if(parentOrg!=null){
					TempPo Po = new TempPo();
					String orgSysCode = parentOrg.getOrgSyscode();
					Po.setId(orgSysCode);
					Po.setCodeId(parentOrg.getOrgId());
					Po.setParentId(parentOrg.getOrgParentId());
					Po.setName(parentOrg.getOrgName());
					Po.setType(type);//这里暂时用于表示树用于导入系统人员
					orgList.add(Po);
				}
				if(lst!=null&&lst.size()>0){
					for(Organization org:lst){
						TempPo subPo = new TempPo();
						String orgSysCode = org.getOrgSyscode();
						subPo.setId(orgSysCode);
						subPo.setCodeId(org.getOrgId());
						subPo.setParentId(parentOrg.getOrgId());
						subPo.setName(org.getOrgName());
						subPo.setType(type);//这里暂时用于表示树用于导入系统人员
						orgList.add(subPo);
					}
				}
			}
			getRequest().setAttribute("orgList", orgList);
			*/
			List<Organization> lst = manager.getAllDeparments();
			List newList = new ArrayList();
			List<Organization> newList2 = new ArrayList<Organization>(lst);
			Collections.sort(newList2, new Comparator<Organization>(){

				public int compare(Organization o1, Organization o2) {
					String code1 = o1.getOrgSyscode();
					String code2 = o2.getOrgSyscode();
					if(code1 != null && code2 != null) {
						return code1.length() - code2.length();
					}
					return 0;
				}
				
			});
			String root = null;
			if(!newList2.isEmpty()) {
				root = newList2.get(0).getOrgSyscode();
			}
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			for(int i=0;i<lst.size();i++){
				TempPo po = new TempPo();
				Organization org = lst.get(i); 
				String orgSysCode = org.getOrgSyscode();
				po.setId(orgSysCode);
				po.setCodeId(org.getOrgId());
				String parentId = manager.findFatherCode(orgSysCode, config_orgCode, null);
				/*if(i == 0){//机构列表根据编码排序，第一个一定是根节点
					if(!"0".equals(parentId)){
						parentId = "0";
					}
				}*/
				if(org.getOrgSyscode().equals(root)) {
					if(!"0".equals(parentId)){
						parentId = "0";
					}
				}
				po.setParentId(parentId);
				po.setName(org.getOrgName());
				po.setType(type);//这里暂时用于表示树用于导入系统人员
				newList.add(po);
				
			}
			
			getRequest().setAttribute("orgList", newList);
		}catch(Exception e){
			e.getMessage();
		}
		return "tree";
	}
	
	/**
	 * author:dengzc
	 * description:按类型展示人员
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String choosetree()throws Exception{
		List<TempPo> lst = new ArrayList<TempPo>();
		if(DEPARTMENT.equals(chooseType)){//按部门搜索
			lst = manager.getUserTreeByDept();
		}else if(POST.equals(chooseType)){//按岗位搜索
			lst = manager.getUserTreeByPost();
		}else if(GROUP.equals(chooseType)){//按组搜索
			lst = manager.getUserTreeByUserGroup();
		}
		getRequest().setAttribute("orgList",lst);
		return "choosetree";
	}
	
	/**
	 * author:dengzc
	 * description:获取系统用户，用同步用户到IM软件
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String systemUserTree()throws Exception{
		return "synuser";
	}
	
	/**
	 * author:dengzc
	 * description:获取组织机构树
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String systree()throws Exception{
		currentUserId = manager.getCurrentUser().getUserId();
		List<Organization> lst = manager.getAllDeparments();
		List<Organization> newLst = new ArrayList<Organization>();//用于保存根节点的组织机构（如果父编码为0的就是跟节点）
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		String[] hasChild = new String[lst.size()];
		for(int j=0;j<lst.size();j++){
			Organization org = lst.get(j);
			String fatherCode = manager.findFatherCode(org.getOrgSyscode(), config_orgCode, null);
			if("0".equals(fatherCode)){
				newLst.add(org);
			}
		}
		for(int i=0;i<newLst.size();i++){
			Organization org = newLst.get(i);
			List<User> userList = manager.getUsersByOrgID(org.getOrgId());
			int count = userList.size();
			if(count>0 || isHasChild(org.getOrgId())){ 
				hasChild[i] = "1";//组织机构下有人员,或部门
			}else{
				hasChild[i] = "0";
			}
		}
		getRequest().setAttribute("orgList", newLst);
		getRequest().setAttribute("hasChild", hasChild);
		return "systree";
	}

	/**
	 * author:dengzc
	 * description:判断组织机构下是否还有部门
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	private boolean isHasChild(String orgId)throws Exception{
		Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
		String orgSysCode = org.getOrgSyscode();
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		List<Organization> lst = manager.getAllDeparments();
		for(int i=0;i<lst.size();i++){
			Organization thisOrg = lst.get(i);
			String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
			if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
				return true;
			}
		}
		return false;
	}
	
	/**
	 * author:dengzc
	 * description:异步加载系统通讯录下的人员
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String synajaxtree()throws Exception{
		try {
			StringBuffer str=new StringBuffer("");
			List<User> userList = manager.getUsersByOrgID(orgId);//需要获取此部门下的所有人员
			Organization org = manager.getOrgById(orgId);//需要获取此部门下的所有子部门
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			String orgSysCode = org.getOrgSyscode();
			List<Organization> lst = manager.getAllDeparments();
			//将人员挂接到此树下
			for(int j=0;j<userList.size();j++){
				User user = userList.get(j);
				str.append("<li id="+user.getUserId()+"><span>"+user.getUserName()+"</span><label style='display:none;'>person</label></li>\n");			
			}
			
			for(int i=0;i<lst.size();i++){
				Organization thisOrg = lst.get(i);
				String fatherCode = manager.findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
				if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
					//如果当前部门下无人员
					List<User> userLst = manager.getUsersByOrgID(thisOrg.getOrgId());//需要获取此部门下的所有人员
					if(userLst.size()==0 && !isHasChild(thisOrg.getOrgId())){ //无子节点
						str.append("<li id="+thisOrg.getOrgId()+" leafChange='folder-org-leaf'><span>"+thisOrg.getOrgName()+"</span><label style='display:none;'>org</label></li>\n");
					}else{//此部门下还存在人员或子部门
						str.append("<li id="+thisOrg.getOrgId()+" ><span>"+thisOrg.getOrgName()+"</span><label style='display:none;'>org</label>\n");
						str.append("<ul class=ajax>\n");
						str.append("<li id="+thisOrg.getOrgId()+i+">{url:"+getRequest().getContextPath()+"/address/addressOrg!synajaxtree.action?orgId="+thisOrg.getOrgId()+"}</li>\n");
						str.append("</ul>\n");
						str.append("</li>\n");					
					}
				}
			}
			renderHtml(str.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * author:dengzc
	 * description:获取组织机构下的人员列表
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String orguserlist()throws Exception{
		currentUserId = manager.getCurrentUser().getUserId();
		if(orgId == null || "".equals(orgId)){
			TUumsBaseOrg baseOrg =  manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
			orgId = baseOrg.getOrgId();
			orgName = baseOrg.getOrgName();
		}
		page = manager.getUsersByOrgId(page, orgId, userName,"");
		return "userlist";
	}
	public String androidallperson(){
		currentPage=currentPage==0?1:currentPage;
		userpage.setPageNo(currentPage);
		userpage= manager.getUsersByOrgId(userpage, null, null,"");
		if(userpage==null||userpage.getResult().size()==0){
			currentPage=userpage.getTotalPages();
			userpage.setPageNo(currentPage);
			userpage= manager.getUsersByOrgId(userpage, null, null,"");
		}
		StringBuffer html = new StringBuffer("");
		for(int i=0;i<userpage.getResult().size();i++){
		
			TUumsBaseUser u=userpage.getResult().get(i);
			Organization org=manager.getOrgById(u.getOrgId());
			html.append("<data>"+u.getUserId()+","+u.getUserName()+","+org.getOrgName()+"</data>");
		}
	//	List<User> list=manager.getAllUserInfo();
		/*for(int i=0;i<list.size();i++){
			User u=new User();
			u=list.get(i);
			Organization org=manager.getOrgById(u.getOrgId());
			html.append("<data>"+u.getUserId()+","+u.getUserName()+","+org.getOrgName()+"</data>");
		}*/
		getRequest().setAttribute("allperson", html.toString());
		return "androidallperson";
	}
	
	public String androidalldeparments(){
		HttpServletRequest request = getRequest();
		Data data = new Data();
		try{
			List<Organization> orglist=manager.getAllDeparments();
			List<Item> lst=new ArrayList<Item>();
			Form form=new Form();
			for(int i=0;i<orglist.size();i++){
				Organization org=orglist.get(i);
				Item item=new Item();
				item.setType(org.getOrgName());
				item.setValue(org.getOrgId());
				lst.add(item);
			}
			form.setItems(lst);
			data.setForm(form);
			Status statu = new Status("0","success");
			data.setStatus(statu);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}catch(Exception ex){
			String msg = ex.getMessage();
		    Status status = new Status("1",msg);
		    data.setStatus(status);
		    request.setAttribute("result", Data.GenerateJsonFormData(data));
		}
		return "androidallperson";
	}
	public String androidsearch()throws Exception{
		HttpServletRequest request = getRequest();
		Data data = new Data();
		try{
			currentPage=currentPage==0?1:currentPage;
			userpage.setPageNo(currentPage);
			userpage= manager.getUsersByOrgId(userpage, orgId, null,"");
			if(userpage==null||userpage.getResult().size()==0){
				currentPage=userpage.getTotalPages();
				userpage.setPageNo(currentPage);
				userpage= manager.getUsersByOrgId(userpage, orgId, null,"");
			}
			List<TUumsBaseUser> userlist=userpage.getResult();
			List<Item> lst=new ArrayList<Item>();
			Form form=new Form();
			List<Row> rows=new ArrayList<Row>();
			for(TUumsBaseUser user:userlist){
				Row row=new Row();
				List<Item> items = new ArrayList<Item>();
				Item item=new Item();
				item.setType(user.getUserName());
				item.setValue(user.getUserId());
				items.add(item);
				row.setItems(items);
				rows.add(row);
			}
			
			data.setRows(rows);
			Status status=new Status("0","success");
			com.strongit.oa.wap.util.Page upage=new com.strongit.oa.wap.util.Page();
			upage.setTotalPages(userpage.getTotalPages());
			upage.setTotalCount(userpage.getTotalCount());
			data.setStatus(status);
			data.setPage(upage);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}catch(Exception ex){
			String err=ex.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidallperson";
	}
	
	public String androidsearch2()throws Exception{
		HttpServletRequest request = getRequest();
		Data data = new Data();
		try{
			currentPage=currentPage==0?1:currentPage;
			userpage2.setPageNo(currentPage);
			userpage2= manager.getUsersByOrgId(userpage2, orgId, null,"");
			if(userpage2==null||userpage2.getResult().size()==0){
				currentPage=userpage2.getTotalPages();
				userpage2.setPageNo(currentPage);
				userpage2= manager.getUsersByOrgId(userpage2, orgId, null,"");
			}
			List<TUumsBaseUser> userlist=userpage2.getResult();
			List<Item> lst=new ArrayList<Item>();
			Form form=new Form();
			List<Row> rows=new ArrayList<Row>();
			for(TUumsBaseUser user:userlist){
				Row row=new Row();
				List<Item> items = new ArrayList<Item>();
				Item item=new Item();
				item.setType(user.getUserName());
				item.setValue(user.getUserId());
				items.add(item);
				row.setItems(items);
				rows.add(row);
			}
			
			data.setRows(rows);
			Status status=new Status("0","success");
			com.strongit.oa.wap.util.Page userpage=new com.strongit.oa.wap.util.Page();
			userpage.setTotalPages(userpage2.getTotalPages());
			userpage.setTotalCount(userpage2.getTotalCount());
			data.setStatus(status);
			data.setPage(userpage);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}catch(Exception ex){
			String err=ex.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidallperson";
	}
	
	public String androidlikesearch()throws Exception{
		HttpServletRequest request = getRequest();
		Data data = new Data();
		try{
			List<User> lstUser=manager.getAllUsers(userName,"public");
			List<Item> lst=new ArrayList<Item>();
			Form form=new Form();
			for(User user:lstUser){
				Item item=new Item();
				item.setType(user.getUserName());
				item.setValue(user.getUserId());
				lst.add(item);
			}
			form.setItems(lst);
			data.setForm(form);
			Status statu = new Status("0","success");
			data.setStatus(statu);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}catch(Exception ex){
			String msg = ex.getMessage();
		    Status status = new Status("1",msg);
		    data.setStatus(status);
		    request.setAttribute("result", Data.GenerateJsonFormData(data));
		}
		return "androidallperson";
	}
	/**
	 * author:dengzc
	 * description:显示对应组织机构下的人员列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String choosePersonByOrgId()throws Exception{
		try {
			StringBuffer sb = new StringBuffer("");
			if(orgId == null){
				List<Organization> list = manager.getAllDeparments();
				if(list.size()>0){
					orgId = list.get(0).getOrgId();
				}else{
					return null;
				}
			}
			List<User> lstUser = manager.getUsersByOrgID(orgId);
			for(Iterator<User> it=lstUser.iterator();it.hasNext();){
				User user = it.next();
				sb.append("<tr class=\"tr1\">");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;\" align=\"center\">");
				sb.append(user.getUserName());
				sb.append("</td>");
				sb.append("<td class=\"td1\" style=\"text-indent: 0px;\" align=\"center\">");
				sb.append(user.getUserEmail()==null?"":user.getUserEmail());
				sb.append("</td>");
				sb.append("</tr>");
			}
			renderHtml(sb.toString());
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * author:dengzc
	 * description:搜索个人通讯录和系统通讯录
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String searchUser()throws Exception{
		String searchType = getRequest().getParameter("searchType");
		String userName = URLDecoder.decode(getRequest().getParameter("userName"), "utf-8"); 
		List<User> lstUser = manager.getAllUsers(userName,searchType);
		StringBuffer sb = new StringBuffer("<table class=\"table1\" width=\"190\" border=\"0\" cellspacing=\"0\" cellpadding=\"00\">");
		for(int i=0;i<lstUser.size();i++){
			User user = lstUser.get(i);
			String name = user.getUserName();
			if(null!=name && !"".equals(name)){
				sb.append("<tr class=\"biao_bg1\"><td>")
					.append(name);
				String path = getRequest().getContextPath();
				String frameroot= (String) getRequest().getSession().getAttribute("frameroot"); 
				if(frameroot==null||frameroot.equals("")||frameroot.equals("null")){
					frameroot= "/frame/theme_gray";
				}
				frameroot=path+frameroot;
				sb.append("</td>");
				if("public".equals(searchType)){//公共通讯录
					sb.append("<td width=\"135\" align=\"right\">");
					sb.append("<img src=\""+frameroot+"/images/tijiao.gif\" title=\"发送邮件\" width=\"15\" style=\"cursor: hand;\" onclick=\"javascript:sendmail('"+user.getUserId()+"','public'"+")\" height=\"15\"></img>")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/sms.gif\" title=\"发送消息\" onclick=\"javascript:sendmsg('"+user.getUserId()+"'"+")\" width=\"15\" height=\"15\" />")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/mobile_sms.gif\" title=\"发送手机短信\" onclick=\"javascript:sendphone('"+user.getUserId()+"'"+")\" width=\"15\" height=\"17\" />")
							.append("&nbsp;&nbsp;")
							.append("<img style=\"cursor: hand;\" src=\""+path+"/oa/image/address/rtx.bmp\" title=\"发送即时消息\" onclick=\"javascript:sendIM('"+user.getUserId()+"','"+manager.getCurrentUser().getUserId()+"'"+")\" width=\"15\" height=\"15\" />");
				}else{
					sb.append("<td width=\"55\" align=\"right\">");
					sb.append("<img style=\"cursor: hand;\" src=\""+frameroot+"/images/tijiao.gif\" title=\"发送邮件\" onclick=\"javascript:sendmail('"+user.getUserId()+"','personal'"+")\" width=\"15\" height=\"15\" />")
					.append("&nbsp;&nbsp;")
					.append("<img style=\"cursor: hand;\" src=\""+frameroot+"/images/chakan.gif\" width=\"15\" title=\"查看属性\" onclick=\"javascript:prop('"+user.getUserId()+"'"+")\" height=\"15\" />");
				}
				
				sb.append("</td></tr>");
			}
			if(null!=user.getUserTel() && !"null".equals(user.getUserTel()) && !"".equals(user.getUserTel())){
				sb.append("<tr class=\"td1\"><td  colspan=\"2\"><span class=\"wz\">");
				sb.append("电话："+user.getUserTel()).append("</span></td></tr>"); 
			}
			if(null!=user.getUserEmail() && !"null".equals(user.getUserEmail()) && !"".equals(user.getUserEmail())){
				sb.append("<tr class=\"td1\"><td  colspan=\"2\"><span class=\"wz\">");
				sb.append("邮件："+user.getUserEmail()).append("</span></td></tr>"); 
			}
			sb.append("<tr class=\"td1\"><td  colspan=\"2\"></td></tr>");
		}
		if(lstUser.size()==0){
			sb.append("<tr class=\"biao_bg1\"><td><span class=\"wz\">没有找到相关记录</span></td></tr>");
		}
		sb.append("</table>");
		if(logger.isInfoEnabled()){
			logger.info(sb.toString());
		}
		ActionContext.getContext().put("searchResult", sb.toString());
		return "searchresult";
	}

	/**
	 * author:dengzc
	 * description:系统通讯录中查看用户详细信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String userDetail() throws Exception{
		if(userId == null || "".equals(userId)){
			throw new Exception("用户ID不存在！");
		}
		model = manager.getUserInfo(userId);
		TUumsBaseUser u=manager.getUserInfoByUserId(userId);
		model.setMobile(u.getRest2());
		model.setTel(u.getUserTel());
		return "userdetail";
	}

	/**
	 * author:hecj
	 * description:安卓手机中系统通讯录中查看用户详细信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String androidUserDetail() throws Exception{
		HttpServletRequest request = getRequest();
		Data data = new Data();
		
		try{
			Form form=new Form();
			List<Item> lst=new ArrayList<Item>();
			Item item=new Item();
			if(userId == null || "".equals(userId)){
				throw new Exception("用户ID不存在！");
			}
			model = manager.getUserInfo(userId);
			TUumsBaseUser user = userService.getUserInfoByUserId(userId);
			/*String strLoginName=model.getUserLoginName();
			
			if(strLoginName==null){//登录名
				strLoginName="";
			}
			item.setType("登录名:");
			item.setValue(strLoginName);
			lst.add(item);*/
			
			String strUserName=model.getUserName();
			if(strUserName==null){//用户名
				strUserName="";
			}
			item=new Item();
			item.setType("用户名:");
			item.setValue(strUserName);
			lst.add(item);
			
			String strSex=model.getSex();
			if(strSex==null){//性别
				strSex="";
			}
			item=new Item();
			item.setType("性别:");
			item.setValue(strSex);
			lst.add(item);
			
			String strEmail=user.getUserEmail();
			if(strEmail==null){//邮件
				strEmail="";
			}
			item=new Item();
			item.setType("邮件:");
			item.setValue(strEmail);
			lst.add(item);
			
			String strTel=user.getUserTel();
			if(strTel==null){//电话
				strTel="";
			}
			item=new Item();
			item.setType("电话:");
			item.setValue(strTel);
			lst.add(item);
			
			String strMobile=user.getRest2();
			if(strMobile==null){//手机
				strMobile="";
			}
			item=new Item();
			item.setType("手机:");
			item.setValue(strMobile);
			lst.add(item);
			
			form.setItems(lst);
			data.setForm(form);
			Status statu = new Status("0","success");
			data.setStatus(statu);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}catch(Exception ex){
			String msg = ex.getMessage();
		    Status status = new Status("1",msg);
		    data.setStatus(status);
		    request.setAttribute("result", Data.GenerateJsonFormData(data));
		}
		
		return "androidallperson";
	}
	
	/**
	 * 显示个人通讯录要导入的公共通讯录列表
	 * @author:邓志城
	 * @date:2009-7-20 上午08:43:48
	 * @return
	 * @throws Exception
	 */
	public String importOrgUserList()throws Exception{
		if("1".equals(orgId)){//取当前用户所在机构人员
			TUumsBaseOrg baseOrg =  manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
			orgId = baseOrg.getOrgId();
			orgName = baseOrg.getOrgName();
		}
		if(orgId == null || "".equals(orgId)){
			orgName = "用户列表";
		}
		page = manager.getUsersByOrgId(page, orgId, userName,"");
		return "importlist";
	}
	
	
	/**
	 * 得到带复选框的树机构(大集中项目移植)
	 * @author:邓志城
	 * @date:2009-12-21 上午10:52:00
	 * @return
	 * @throws Exception
	 */
	public String showDictOrgTreeWithCheckbox() throws Exception  {
		String param = getRequest().getParameter("type");
		List<TempPo> list = manager.getOrgListFromDict(param);
		if(!list.isEmpty()){
			TempPo firstPo = list.get(0);
			getRequest().setAttribute("root", firstPo.getName());
		}else{
			getRequest().setAttribute("root", "未找到记录");
		}
		getRequest().setAttribute("orgList",list);
		return "choosedictorg";
	}
	
	/**
	 * 选择人员时的全选功能。
	 * @author:郑志斌
	 * @date:2010-6-2 下午16:47:53
	 * @param type "1":公文通讯录；其他：系统通讯录
	 * @return
	 * @throws Exception
	 */
	public String getAllUsersForWorkflow() throws Exception {
		String ret = "";
		try{
			orgId = getRequest().getParameter("orgId");
			type = getRequest().getParameter("type");
			ret = manager.getAllUserInfoForWorkflow(type,orgId);
		}catch(Exception e){
			logger.error("工作流选择人员全选时出现错误，异常信息：" + e);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	/************************以下是wap版手机开发**************************/
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 22, 2010 4:49:16 PM
	 */
	public String getOrguserlist()throws Exception{
		page.setPageSize(100);
		//this.getLevelOrg();
		getOrgWithSub(orgId);
		currentPage=currentPage==0?1:currentPage;
		page.setPageNo(currentPage);
		currentUserId = manager.getCurrentUser().getUserId();
		if(orgId == null || "".equals(orgId)){
			TUumsBaseOrg baseOrg =  manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
			orgId = baseOrg.getOrgId();
			orgName = baseOrg.getOrgName();
		}
		page = manager.getUsersByOrgId(page, orgId, userName,"");
		return "tree";
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 22, 2010 4:56:32 PM
	 */
	public String getOrgList()throws Exception{
		list();
		return "selectPer";
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 22, 2010 4:49:03 PM
	 */
	public String toChooseUsers()throws Exception{
		page.setPageSize(10);
		this.getLevelOrg();
		if("import".equals(chooseType)){	//如果是导入人员时选择
			if(groupId==null||"".equals(groupId)){		//如果groupId为空
				getResponse().sendRedirect(getRequest().getContextPath()+"/address/addressGroup!getAddressList.action?message=fail");
				return null;
			}
			List<ToaAddressGroup> list = groupManager.getGroupList();
			HttpServletRequest request=this.getRequest();
			request.setAttribute("list", list);
		}
		searchCurrentPage=searchCurrentPage==0?1:searchCurrentPage;
		page.setPageNo(searchCurrentPage);
		if(searchOrgId == null || "".equals(searchOrgId)){
			TUumsBaseOrg baseOrg =  manager.getUserDepartmentByUserId();//获取当前用户所在的组织机构
			searchOrgId = baseOrg.getOrgId();
		}
		page = manager.getUsersByOrgId(page, searchOrgId, searchUserName,"");
		return "selectPer";
	}
	
	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return model;
	}

	@Autowired
	public void setManager(AddressOrgManager manager) {
		this.manager = manager;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Page<TUumsBaseUser> getPage() {
		return page;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName)throws Exception {
		orgName = URLDecoder.decode(orgName, "utf-8");
		this.orgName = orgName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getChooseType() {
		return chooseType;
	}

	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Page<TUumsBaseUser> getUserpage() {
		return userpage;
	}

	public void setPage(Page<TUumsBaseUser> page) {
		this.page = page;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Page<TUumsBaseUser> getUserpage2() {
		return userpage2;
	}


	public String getSearchOrgId() {
		return searchOrgId;
	}

	public void setSearchOrgId(String searchOrgId) {
		this.searchOrgId = searchOrgId;
	}

	public int getSearchCurrentPage() {
		return searchCurrentPage;
	}

	public void setSearchCurrentPage(int searchCurrentPage) {
		this.searchCurrentPage = searchCurrentPage;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getSearchUserName() {
		return searchUserName;
	}

	public void setSearchUserName(String searchUserName) {
		this.searchUserName = searchUserName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

}
