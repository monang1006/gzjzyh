package com.strongit.oa.docdis;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDocDis;
import com.strongit.oa.bo.ToaDocafterflow;
import com.strongit.oa.bo.ToaSetCondition;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.docafterflow.DocafterflowManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * <p>Title: DocDisAction.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 公文分发Action
 * @date 	 2009-11-11 21:03:38
 * @version  1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/docdis/docDis.action") })
public class DocDisAction extends BaseActionSupport{

	private static final long serialVersionUID = -4687254208903653386L;
	
	private Page<ToaDocDis> page = new Page<ToaDocDis>(FlexTableTag.MAX_ROWS, true);
	
	private Page<ToaDocafterflow> fpage = new Page<ToaDocafterflow>(6,true);
	 
	private DocDisManager docDisManager;
	
	private DocafterflowManager afterflowManager;
	
	@Autowired
	private IUserService userService;					//用户
	
	private SendDocManager theSendDocManager;	//发文
	
	private String notReadOver="";					//文本信息，用ID加上","的形式来写的内容
	
	private String notDis="";						//文本信息，没有分发操作
	
	@Autowired
	private IEFormService eformService;
	
	@Autowired
	public void setDocDisManager(DocDisManager docDisManager) {
		this.docDisManager = docDisManager;
	}
	
	@Autowired
	public void setAfterflowManager(DocafterflowManager afterflowManager) {
		this.afterflowManager = afterflowManager;
	}
	
	/**
	 *  对应查询所需要条件属性
	 */
	private String state;								//外部分发状态  -1：全部   0：未分发  1：已分发
	
	private String innerState;							//内部分发状态  -1：全部   0：未分发  1：已分发
	
	private String title;								//文章标题
	
	private Date   startDate;							//查询范围中的开始时间
	
	private Date   endDate;								//查询范围中的结束时间
	
	private String signState;							//查询外部签收是否完成状态 
	
	private int hasreply;								//已经签收数量
	
	private int unreply;								//尚未签收数量
	
	private String unreplyOrg;							//生成对应的未签收组织机构名称
	
	private String unReplyUser;							//生成对应的未签收的用户
	
	private String ids;									//前台树形结构传入选中id;
	
	private String names;								//前台树形结构传入选中节点名称;
	
	private List<TUumsBaseOrg> list;					//生成树形结构所需数据内容
	
	private List<OrgAndUser>   userList;				//生成部门人员数
	
	private String docId;								//前台传入对应文档的ID主键值
	
	private ToaDocDis docDis;							//传入前台的文档对象
	
	private ToaSetCondition condition;					//查询条件bo
	
	private	ToaDocafterflow afterflow;					//前台的分发到某个地市的数据对象
	
	private String TANGER_OCX_Username;					//Office控件当前用户
	
	private File formData; 							 //表单数据

	private String type;
	
	private String distributeType;
	
	private String showType;

	@Override
	public String delete() throws Exception {
		String msg=docDisManager.deleteDocs(docId);
		StringBuffer sb = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/workservice.js'>")
			.append("</SCRIPT>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append("/common/js/commontab/service.js'>")
			.append("</SCRIPT>")
			.append("<script>");
			if(!"".equals(msg)){
				sb.append(" alert(\"以下公文已分发，不能删除 ：")
				.append(msg)
				.append("\");");
			}
			sb.append(" window.location=\"")
			.append(getRequest().getContextPath())
			.append("/docdis/docDis.action")
			.append("\"; </script>");
		return renderHtml(sb.toString());
	}
	
	public String notRealDelete() throws Exception{
		return this.renderHtml(docDisManager.notRealDelDocs(docId));
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		TANGER_OCX_Username = docDisManager.getCurrentUser().getUserLoginname();
		User users=userService.getCurrentUser();
		TUumsBaseOrg org =userService.getSupOrgByUserIdByHa(users.getUserId());
		String orgCode = org.getOrgSyscode();
		getRequest().setAttribute("orgCode", orgCode);
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		page = docDisManager.getPageList(page, title, state,innerState, startDate, endDate,signState);
		List<ToaDocDis> list= page.getResult();
		if(list != null){
			for(int i=0;i<list.size();i++){
				ToaDocDis tempDocDis = list.get(i);
				List<ToaDocafterflow> tempList = new ArrayList<ToaDocafterflow>(tempDocDis.getToaDocafterflows());
				if("0".equals(tempDocDis.getSenddocDic())){//未外部分发
					notDis=notDis+tempDocDis.getSenddocId()+",";
				}else{
					for(int j=0;j<tempList.size();j++){
						ToaDocafterflow tempObj = tempList.get(j);
						if((tempObj.getGetType()==null||"0".equals(tempObj.getGetType()))&&"0".equals(tempObj.getGetOrnot())){
							notReadOver=notReadOver+tempDocDis.getSenddocId()+",";
							break;
						}
					}
				}
			}
		}
		return SUCCESS;
	}
	/**
	 * @author  于宏洲
	 * @date    2009-11-11 17:10:11
	 * @des     跳转到公文查看页面
	 * @return  String
	 */
	public String view() throws Exception{
		prepareModel();
		TANGER_OCX_Username = docDisManager.getCurrentUser().getUserLoginname();
		User users=userService.getCurrentUser();
		TUumsBaseOrg org =userService.getSupOrgByUserIdByHa(users.getUserId());
		String orgCode = org.getOrgSyscode();
		getRequest().setAttribute("orgCode", orgCode);
		return "view";
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-11 17:22:10
	 * @des     进行Ajax调用的Action接口，判断当前文档是否被分发(0:都未分发;1：已外部分发；2：已内部分发;3:都被分发)
	 * @return  String
	 */
	public String charge() throws Exception{
		return renderText(docDisManager.chargeSendOrNot(docId));
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-11 21:03:11
	 * @des     进行统计Action跳转接口
	 * @return  String
	 */
	public String gotoCount() throws Exception{
		prepareModel();
		TANGER_OCX_Username = docDisManager.getCurrentUser().getUserLoginname();
		User users=userService.getCurrentUser();
		TUumsBaseOrg org =userService.getSupOrgByUserIdByHa(users.getUserId());
		String orgCode = org.getOrgSyscode();
		getRequest().setAttribute("orgCode", orgCode);
		List<ToaDocafterflow> hasreplyList = afterflowManager.getObjByReadOrNot(docId, true);
		List<ToaDocafterflow> unreplyList = afterflowManager.getObjByReadOrNot(docId, false);
		hasreply = hasreplyList.size();
		unreply	= unreplyList.size();
		//以下是为了读取相关的日期信息
		unreplyOrg=(unreplyOrg==null?"":unreplyOrg);
		for(int i=0;i<unreplyList.size();i++){
			if(i!=unreplyList.size()-1){
				unreplyOrg = unreplyOrg + unreplyList.get(i).getDeptname() + ",";
			}else{
				unreplyOrg = unreplyOrg + unreplyList.get(i).getDeptname();
			}
		}
		if(hasreply!=0){
			afterflow = hasreplyList.get(0);
		}else if(unreply!=0){
			afterflow = unreplyList.get(0);
		}
		docDis = docDisManager.getDocDisById(docId);
		return "count";
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 11:48:55
	 * @des 	统计列表入口    
	 * @return  String
	 */
	public String viewflow() throws Exception{
		fpage = afterflowManager.getPageListByDocId(fpage, docId);
		return "viewflow";
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 15:36:03
	 * @des     进行分发前台树形结构展现入口
	 * @return  String
	 */
	public String orgTree() throws Exception{
		if(distributeType!=null&&"orgAndUserTree".equals(distributeType)){	//展现部门人员树
			TUumsBaseOrg nowOrg=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());	//根据用户Id获取到该用户所在分级授权机构对象
			if(showType!=null&&"users".equals(showType)){
//				condition=docDisManager.getConditionByOrgId(nowOrg.getOrgId());
//				if(condition==null||"T_UUMS_BASE_POST".equals(condition.getTargetTable())){//没有设置条件或者设置了岗位的条件
//					userList=docDisManager.getPostAndUserListByCon(condition,nowOrg,user.getCurrentUser().getUserId());
//				}else if("T_UUMS_BASE_GROUP".equals(condition.getTargetTable())){
//					userList=docDisManager.getGroupAndUserListByCon(condition,nowOrg,user.getCurrentUser().getUserId());
//				}
			}else{
				List<TUumsBaseOrg> orgList=userService.getSelfAndChildOrgsByOrgsyscodeAndType(nowOrg.getOrgSyscode(), "0", "0");	//获取组织结构下的所有部门列表
				userList=docDisManager.getOrgAndUserList(orgList,nowOrg);
			}
			getRequest().setAttribute("userList", userList);
		}else{								//展现机构树
			list = userService.getSelfAndChildOrgsByOrgsyscodeAndType(null,"1","0");
//			list = user.getAllDeparments();

			getRequest().setAttribute("list", list);
		}	
		return "orgtree";
	}
	
	
	/*
	 * 
	 * Description:公文分发
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 21, 2009 10:43:34 AM
	 */
	public String beginDocDis() throws Exception{
		boolean flag=false;
		if(distributeType!=null&&"orgAndUserTree".equals(distributeType)){	//内部分发
			flag=afterflowManager.realGetDocOfInner(docId, ids,names);
		}else{	//外部分发
			flag=afterflowManager.realGetDoc(docId,ids,names);
		}
		if(flag){
			return renderText("true");
		}else{
			return renderText("false");
		}
	}

	@Override
	protected void prepareModel() throws Exception {
		if (docId != null) {
			docDis = docDisManager.getDocDisById(docId);
		} else {
			docDis = new ToaDocDis(); 
		}
	}
	
	/*
	 * 
	 * Description:保存表单数据
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 19, 2009 4:44:52 PM
	 */
	public String eFormDateSave()throws Exception{
		if(null!=formData && !"".equals(formData)){
			try{
				VoFormDataBean bean = eformService.saveFormData(formData);
				docId = bean.getBusinessId();
				bean.deleteFile();
				renderText(docId);
			}catch(Exception e){
				e.printStackTrace();
				renderText("error");
			}
		}
		return null;
	}
	
	/*
	 * 
	 * Description:内部分发统计
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 21, 2009 2:54:03 PM
	 */
	public String gotoCountOfInner() throws Exception{
		List<ToaDocafterflow> hasreplyList = afterflowManager.getInnerDistrByReadOrNot(docId, true);
		List<ToaDocafterflow> unreplyList = afterflowManager.getInnerDistrByReadOrNot(docId, false);
		hasreply = hasreplyList.size();
		unreply	= unreplyList.size();
		unReplyUser=(unReplyUser==null?"":unReplyUser);
		for(int i=0;i<unreplyList.size();i++){
			if(i!=unreplyList.size()-1){
				unReplyUser = unReplyUser + unreplyList.get(i).getUserName() + ",";
			}else{
				unReplyUser = unReplyUser + unreplyList.get(i).getUserName();
			}
		}
		if(hasreply!=0){
			afterflow = hasreplyList.get(0);
		}else if(unreply!=0){
			afterflow = unreplyList.get(0);
		}
		docDis = docDisManager.getDocDisById(docId);
		return "innercount";
	}
	
	/*
	 * 
	 * Description:内部分发统计列表入口
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 21, 2009 3:14:23 PM
	 */
	public String viewInnerflow() throws Exception{
		fpage = afterflowManager.getInnerPageListByDocId(fpage, docId);
		return "viewinnerflow";
	}
	

	@Override
	public String save() throws Exception {
		
		return null;
	}

	public Object getModel() {
		
		return null;
	}
	
	public Page<ToaDocDis> getPage() {
		return page;
	}

	public void setPage(Page<ToaDocDis> page) {
		this.page = page;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public int getHasreply() {
		return hasreply;
	}

	public void setHasreply(int hasreply) {
		this.hasreply = hasreply;
	}

	public int getUnreply() {
		return unreply;
	}

	public void setUnreply(int unreply) {
		this.unreply = unreply;
	}

	public void setDocDis(ToaDocDis docDis) {
		this.docDis = docDis;
	}

	public ToaDocDis getDocDis() {
		return docDis;
	}

	public ToaDocafterflow getAfterflow() {
		return afterflow;
	}

	public void setAfterflow(ToaDocafterflow afterflow) {
		this.afterflow = afterflow;
	}

	public Page<ToaDocafterflow> getFpage() {
		return fpage;
	}

	public void setFpage(Page<ToaDocafterflow> fpage) {
		this.fpage = fpage;
	}

	public String getUnreplyOrg() {
		return unreplyOrg;
	}

	public void setUnreplyOrg(String unreplyOrg) {
		this.unreplyOrg = unreplyOrg;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getInnerState() {
		return innerState;
	}

	public void setInnerState(String innerState) {
		this.innerState = innerState;
	}

	public String getTANGER_OCX_Username() {
		return TANGER_OCX_Username;
	}

	public void setTANGER_OCX_Username(String username) {
		TANGER_OCX_Username = username;
	}

	public void setFormData(File formData) {
		this.formData = formData;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDistributeType() {
		return distributeType;
	}

	public void setDistributeType(String distributeType) {
		this.distributeType = distributeType;
	}

	public List<OrgAndUser> getUserList() {
		return userList;
	}

	public String getUnReplyUser() {
		return unReplyUser;
	}

	@Autowired
	public void setTheSendDocManager(SendDocManager theSendDocManager) {
		this.theSendDocManager = theSendDocManager;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public ToaSetCondition getCondition() {
		return condition;
	}

	public void setCondition(ToaSetCondition condition) {
		this.condition = condition;
	}

	public String getNotReadOver() {
		return notReadOver;
	}

	public void setNotReadOver(String notReadOver) {
		this.notReadOver = notReadOver;
	}

	public String getNotDis() {
		return notDis;
	}

	public void setNotDis(String notDis) {
		this.notDis = notDis;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}
}
