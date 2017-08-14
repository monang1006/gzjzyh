/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: zhangli
 * Version: V1.0
 * Description： 案卷管理ACTION
 */
package com.strongit.oa.archive.archivefolder;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefile.ArchiveFileAppendManager;
import com.strongit.oa.archive.sort.ArchiveSortManager;
import com.strongit.oa.archive.tempfile.AnnexManager;
import com.strongit.oa.archive.tempfile.TempFileManager;
import com.strongit.oa.archive.tempfile.TempFileType;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "archive.action", type = ServletActionRedirectResult.class),
    @Result(name = ArchiveFolderAction.ARCHIVE_SORT_EXCEL, value = "stream", type = StreamResult.class, params = {
        "contentType", "application/vnd.ms-excel", "inputName", "stream",
        "contentDisposition", "attachment;filename=\"archiveFolder.xls\"",
        "bufferSize", "1024" })  })

public class ArchiveFolderAction extends BaseActionSupport {
	/** 分页对象*/
	private Page<ToaArchiveFolder> page = new Page<ToaArchiveFolder>(FlexTableTag.MAX_ROWS, true);
	//文件页面对象
	private Page<ToaArchiveTempfile> tempFilePage=new Page<ToaArchiveTempfile>(FlexTableTag.MAX_ROWS, true);

	public Page<ToaArchiveTempfile> getTempFilePage() {
		return tempFilePage;
	}
	private String year1; 
    private String month1; 
    private String orgId1;
    private String tempfileDeadline;
    private String fileFolder;
    private String fileTitle;
    private String fileNo;
    private String disLogo1;
    private String groupType;

	/** 档案类目编号*/
	private String archiveSortId;
	private String folderLimitId;
	
	/** 档案类目年度*/
	private String folderDate1;
	/** 档案类目名称*/
	private String archiveSortName;
	//开始年度
	private Date folderFromDate1;
	//结束年度
	private Date folderToDate1;
	/** 档案案卷编号*/
	private String folderId;
	/** 案卷年度*/
	private String folderDateY;

	/** 档案文件编号串*/
	private String fileIds;

	/** 档案案卷列表*/
	private List folderList;

	/** 保管期限字典列表*/
	private List limitdictList;

	/** 字典列表*/
	private List dictList;

	/** 机构列表*/
	private List orgList;

	/** 用户列表*/
	private List userList;

	/** 档案类目列表*/
	private List sortList;

	/** 档案文件列表*/
	private List fileList;
	private List<ToaArchiveTempfile> tempfileList;

	/** 档案案卷对象*/
	private ToaArchiveFolder model = new ToaArchiveFolder();

	/** 档案文件对象*/
	private ToaArchiveFile filemodel = new ToaArchiveFile();

	/** 年内文件对象*/
	private ToaArchiveTempfile tempfilemodel = new ToaArchiveTempfile();

	/** 档案案卷Manager*/
	private ArchiveFolderManager manager;

	/** 档案类目Manager*/
	private ArchiveSortManager sortmanager;
	/** 文件Manager*/
    private TempFileManager tempfilemanager;

	/** 字典Service接口*/
	private IDictService dictservice;

	/** 跳转参数*/
	private String forward; 

	/** 字典类值*/
	private String dictValue = null;

	/** 字典类名称*/
	private String dictName = null;

	/** 机构编号*/
	private String orgId = null;

	/** 对象编号*/
	private String objId = null;

	/** 对象名称*/
	private String objName = null;

	/** 已选择对象值*/
	private String hascheckedvalues = null;

	/** 案卷状态Map*/
	private Map<String, String> statemap = new HashMap<String, String>();

	/** 对应模块类型*/
	private String moduletype = null;

	/** 未归档*/
	private static final String NO = "0";

	/** 已归档*/
	private static final String YES = "1";

	/** 审核中*/
	private static final String AUDITING = "2";

	/** 驳回*/
	private static final String BACK = "3";

	/** 销毁审核*/
	private static final String DESTR_AUDIT = "4";

	//销毁驳回
	private static final String DESTR_BACK="5";

	//用户接口 
	private IUserService userService;
	//文件搜索标示
	private String searchType;
	//部门名称
	private String fileDepartmentName;
	//查看文件时存放颁奖单位的名字
	private String fileAwardsOrgLevel;
	//附件名称
	 private String fileFileName;	
	//档案文件附件ID
	private String fileAppedId;
	//资料中心文件组卷后，返回，显示相对应“节点”下文件列表
	private String treeType;
	//资料中心文件组卷后，返回，显示相对应“节点”下文件列表
	private String treeValue;
	// 标识是否是处领导用户
	private String depLogo; 
	 // 该归档文件的流程实例ID
	private String instanceId;
	private String folderDepartmentName;
	//案卷编号
	private String folderNo;
	//案卷名称
	private String folderName;
	//创建日期
	private String folderDate;
	//归档状态
	private String folderAuditing;
	//文件id
	private String tempfileId;
	//件号
	private String tempPieceNo;
	//件号1
    private String tempfPieceNo;
	/**
     * 导出Excel数据流.
     */
    private InputStream stream; 
    public static final String ARCHIVE_SORT_EXCEL = "archiveSortExcel";
		/**
		 * @author zzbsteven 2010/4/22 14:14
		 * 档案管理——”归档确认“中设置申请归档和直接归档
		 * {‘0’：‘申请归档’，‘1’：‘直接归档’}
		 */
	private String archiveIsEnable ;

	@Autowired ArchiveFileAppendManager appendManager;
	
	@Autowired AnnexManager annexManger;
	
	/** 
	 * @author zzbsteven 2010/04/23 9:34
	 * 系统全局设置，获取是否直接归档 
	 * */
    @Autowired SystemsetManager systemsetManager;  
  //表单内容
	private String eformContent;
	//流程ID
	private String workflow;
	//电子表单ID
	private String appendFormId;
	
	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getAppendFormId() {
		return appendFormId;
	}

	public void setAppendFormId(String appendFormId) {
		this.appendFormId = appendFormId;
	}

	public String getFileDepartmentName() {
		return fileDepartmentName;
	}

	public void setFileDepartmentName(String fileDepartmentName) {
		this.fileDepartmentName = fileDepartmentName;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @roseuid 494F585200BB
	 */
	public ArchiveFolderAction() {
		statemap.put(null, "未归档");
		statemap.put(NO, "未归档");
		statemap.put(YES, "已归档");
		statemap.put(AUDITING, "审核中");
		statemap.put(BACK, "驳回");
		statemap.put(DESTR_AUDIT, "销毁审核");
		statemap.put(DESTR_BACK, "销毁驳回");
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<ToaArchiveFolder> getPage() {
		return page;
	}

	/**
	 * Sets the value of the archiveSortId property.
	 * 
	 * @param aArchiveSortId
	 *            the new value of the archiveSortId property
	 */
	public void setArchiveSortId(java.lang.String aArchiveSortId) {
		archiveSortId = aArchiveSortId;
	}

	public String getArchiveSortId() {
		return archiveSortId;
	}

	/**
	 * Sets the value of the folderId property.
	 * 
	 * @param aFolderId
	 *            the new value of the folderId property
	 */
	public void setFolderId(java.lang.String aFolderId) {
		folderId = aFolderId;
	}

	public String getFolderId() {
		return folderId;
	}

	/**
	 * Access method for the folderList property.
	 * 
	 * @return the current value of the folderList property
	 */
	public List getFolderList() {
		return folderList;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ArchiveFolderManager aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String tree() {
		folderList=manager.getAllFolerBySelect(model);
		return "tree";
	}


	/**
	 * 获取机构树
	 * 
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String orgtree() throws Exception{
		orgList = manager.getOrgList(moduletype);
		return "orgtree";
	}

	/**
	 * 获取用户树
	 * 
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String usertree() throws Exception{

		userList = manager.getUserList(orgId);
		return "usertree";
	}
	/**
	 * 获取字典树
	 * 
	 * @return java.lang.String
	 * @roseuid 494F58830109
	 */
	public String dictree() throws Exception{
		dictName = dictservice.getDictNameByValue(dictValue);
		dictList = dictservice.getItemsByDictValue(dictValue);
		return "dictree";
	}

	/**
	 * 申请归档
	 * 
	 * @return java.lang.String
	 * @roseuid 49504F0D036B
	 */
	public String appigeonhole() throws Exception{
		addActionMessage(manager.appigeonhole(folderId));
		return "reloads";
	}

	/**
	 * 根据部门ID获取部门名称
	 * @return
	 */
	private String getDepartment(String orgId) throws Exception{
		Organization org=userService.getDepartmentByOrgId(orgId);;
		if(org!=null){
			return org.getOrgName();
		}else{
			return null;
		}	
	}
	/**
	 * 初始化审核归档申请
	 * 
	 * @return java.lang.String
	 * @roseuid 49504FE80000
	 */
	public String initaudit() throws Exception{
		model = manager.getFolder(folderId);
		if (YES.equals(model.getFolderAuditing())) {
			//addActionMessage("该案卷已归档，无需再归档！");
			StringBuffer returnhtml = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append(
					"/common/frame/perspective_content/actions_container/js/workservice.js'>")
					.append("</SCRIPT>")
					.append("<SCRIPT src='")
					.append(getRequest().getContextPath())
					.append(
					"/common/frame/perspective_content/actions_container/js/service.js'>")
					.append("</SCRIPT>").append("<script>").append("alert('")
					.append("该案卷已归档，无需再归档！").append(
					"');window.close();</script>");
			return renderHtml(returnhtml.toString());
		} else if (BACK.equals(model.getFolderAuditing())) {
			//addActionMessage("该案卷归档申请未通过，请重新提交归档申请！");
			StringBuffer returnhtml = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append(
					"/common/frame/perspective_content/actions_container/js/workservice.js'>")
					.append("</SCRIPT>")
					.append("<SCRIPT src='")
					.append(getRequest().getContextPath())
					.append(
					"/common/frame/perspective_content/actions_container/js/service.js'>")
					.append("</SCRIPT>").append("<script>").append("alert('")
					.append("该案卷归档申请未通过，请重新提交归档申请！").append(
					"');window.close();</script>");
			return renderHtml(returnhtml.toString());
		} else if (!AUDITING.equals(model.getFolderAuditing())) {
			//	addActionMessage("该案卷未提交归档申请，请先提交归档申请！");
			StringBuffer returnhtml = new StringBuffer(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append(
					"/common/frame/perspective_content/actions_container/js/workservice.js'>")
					.append("</SCRIPT>")
					.append("<SCRIPT src='")
					.append(getRequest().getContextPath())
					.append(
					"/common/frame/perspective_content/actions_container/js/service.js'>")
					.append("</SCRIPT>").append("<script>").append("alert('")
					.append("该案卷未提交归档申请，请先提交归档申请！").append(
					"');window.close();</script>");
			return renderHtml(returnhtml.toString());
		} else {
			return "initaudit";
		}
	}

	/**
	 * 审核归档申请
	 * 
	 * @return java.lang.String
	 * @roseuid 49504FE80000
	 */
	public String audit() throws Exception{
		addActionMessage(manager.auditpigeonhole(folderId, model
				.getFolderAuditing(), model.getFolderArchiveNo(), model
				.getFolderAuditingContent(),new OALogInfo("审核归档申请")));
		return "temp";
	}
    
	/**
	 * 初始化移动案卷
	 * 
	 * @return java.lang.String
	 * @roseuid 4950501B002E
	 */
	public String initremove() throws Exception{
		sortList = sortmanager.getAllArchiveSort();
		return "initremove";
	}

	/**
	 * 移动案卷
	 * 
	 * @return java.lang.String
	 * @roseuid 4950501B002E
	 */
	public String remove() throws Exception{
		prepareModel();
		ToaArchiveSort sort = sortmanager.getArchiveSort(archiveSortId);
		addActionMessage(manager.removefoder(folderId, sort,new OALogInfo("移动案卷")));
		return "temp";
	}

	/**
	 * 查看案卷列表
	 * 
	 * @return java.lang.String
	 * @roseuid 495072D30203
	 */
	@Override
	public String list() throws Exception{
	    SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd");
		limitdictList = dictservice.getItemsByDictValue("BGQX");
		if ("rujuan".equals(moduletype)) {//入卷
//			model.setFolderDepartment(orgId);
			model.setFolderAuditing(NO);
		} else if("rujuanpige".equals(moduletype)){//入卷并归档
//			model.setFolderDepartment(orgId);
			model.setFolderAuditing(YES);
		}
		else if ("searchborrowFile".equals(moduletype)) {// 如果为借阅文件，则只能查看已归档的案卷
			if (model.getFolderAuditing() == null)
				model.setFolderAuditing(YES);
		}
		if(folderDateY!=null&&!"".equals(folderDateY)){
		    
		    model.setFolderDate(st.parse(folderDateY+"-01-01"));
		}
		archiveIsEnable=systemsetManager.getSystemset().getArchiveIsEnable();//获取系统设置是否要审核的值
		page = manager.getAllFolderfile(page, archiveSortId,folderDateY, model);
		if (forward != null && forward.equals("rujuan"))
			return "rujuan";
		else if(forward != null && forward.equals("selected"))
		    return "selected";
		    else
			return SUCCESS;
	}
	/**
	 * 获取未归档文件附件数量
	 * @author 胡丽丽
	 * @date 2010-01-25
	 */
	public void getappendsize(){
		if(fileList!=null){
			Map<String,Integer> map = annexManger.getCount();
			for(Object temp:fileList){
				ToaArchiveTempfile tf=(ToaArchiveTempfile)temp;
				tf.setAppendsize(map.get(tf.getTempfileId())==null?0:map.get(tf.getTempfileId()));
				//tf.setAppendsize(tf.getToaArchiveTfileAppends().size());
			}
		}
	}
	/**
     * 导出为excel
     * @author xush
     * @date 5/29/2013 11:28 AM
     */
	public String importExcel(){
	    try {
	        ToaArchiveFolder model=new ToaArchiveFolder();
	        SimpleDateFormat st = new SimpleDateFormat(
            "yyyy");
	        //保管期限
	        if(folderLimitId!=null&&!"".equals(folderLimitId)){
	            model.setFolderLimitId(folderLimitId);
	        }
	        //部门名称
	        if(folderDepartmentName!=null&&!"".equals(folderDepartmentName)){
                model.setFolderDepartmentName(folderDepartmentName);
            }
	        //文号
	        if(folderNo!=null&&!"".equals(folderNo)){
                model.setFolderNo(folderNo);
            }
	        //案卷名称
	        if(folderName!=null&&!"".equals(folderName)){
                model.setFolderName(folderName);
            }
	        //创建日期
	        if(folderDate!=null&&!"".equals(folderDate)){
                model.setFolderDate(st.parse(folderDate));
            }
	        //状态
	        if(folderAuditing!=null&&!"".equals(folderAuditing)){
                model.setFolderAuditing(folderAuditing);
            }
	        
	   //List<ToaArchiveFolder> list=manager.getAllFolder(archiveSortId);
	   //if(archiveSortId!=null&&"".equals(anObject)){
	        
	         // 导出Excel数据流.
	        stream = manager.searchExcel(model,archiveSortId,statemap);
	   //}else{
	       //stream = manager.searchExcel(null,null);
	   //}
	    } catch (Exception e) {
            e.printStackTrace();
        }
        return ARCHIVE_SORT_EXCEL;
    }
	/**
	 * 获取已归档文件附件数量
	 * @author 胡丽丽
	 * @date 2010-01-25
	 */
	public void getfileappendsize(){
		if(fileList!=null){
			Map<String,Integer> map = appendManager.getCount();
			for(Object temp:fileList){
				ToaArchiveFile tf=(ToaArchiveFile)temp;
				tf.setAppendsize(map.get(tf.getFileId())==null?0:map.get(tf.getFileId()));
			}
		}
	}
	/**
	 * 保存案卷
	 * 
	 * @return java.lang.String
	 * @roseuid 495072D30213
	 */
	@Override
	public String save() throws Exception{
		if ("".equals(model.getFolderId()))
		{
			model.setFolderId(null);
		}
//		if(model.getFolderAuditing()==null||"".equals(model.getFolderAuditing())){
		//初始化卷（盒）状态为"0"未归档
			model.setFolderAuditing("0");
//		}
			
		User user=userService.getCurrentUser();
		TUumsBaseOrg baseOrg= userService.getSupOrgByUserIdByHa(user.getUserId());
		if(baseOrg!=null){
			model.setFolderOrgcode(baseOrg.getSupOrgCode());									//添加机构CODE
			model.setOrgId(baseOrg.getOrgId());													//添加机构ID
		}
		
		if(model.getFolderDepartment()!=null){
			Organization org = manager.getDepartmentByOrgId(model.getFolderDepartment());
			if(model.getFolderDepartmentName()!=org.getOrgName())
			{
				model.setFolderDepartmentName(org.getOrgName());// 待组织机构完善后补充		
			}
		}
		    SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd");
            if(folderFromDate1!=null&&!"".equals(folderFromDate1)){
               model.setFolderFromDate(st.format(folderFromDate1));
             }
            if(folderToDate1!=null&&!"".equals(folderToDate1)){
              model.setFolderToDate(st.format(folderToDate1));
            }
        
		addActionMessage(manager.saveFolder(model,new OALogInfo("保存案卷")));
		return "reloads";
	}

	/**
	 * 删除案卷
	 * 
	 * @return java.lang.String
	 * @roseuid 495072D30232
	 */
	@Override
	public String delete() throws Exception{
		addActionMessage(manager.delFolder(folderId,new OALogInfo("根据ID删除案卷")));
		return "reloads";
	}

	/**
	 * 判断编号是否存在
	 * @author 胡丽丽
	 * @date 2010-01-07
	 * @return
	 */
	public String isHasthesameNo(){
		List list=manager.getFolderByNo(model.getFolderNo(), new OALogInfo("根据卷（盒）编号获取卷（盒）表单"));
		ToaArchiveFolder folderModel=new ToaArchiveFolder();
		boolean isBooleanB=false;

		if(list!=null&&list.size()>=1 ){
			for(int i=0;i<list.size();i++){
				folderModel=(ToaArchiveFolder)list.get(i);
				if(folderId!=null&&!folderId.equals("")&&folderModel.getFolderId().equals(folderId)){
					isBooleanB=true;
					break;
				}				
			}
			if(isBooleanB){
				return renderHtml("0");
			}
			else {
				return renderHtml("1");
			}
		}
		else{
			return renderHtml("0");
		}
	}
	/**

     * 通过id获取案卷号
     * @author xush
     * @date 6/26/2013 3:03 PM
     * @return
     */
    public String findFloderNo(){
        String[] str = folderId.split(",");
        
        StringBuffer sb = new StringBuffer();
        //遍历str数组
        for (int i = 0; i < str.length; i++) {
            //当前id得到的ToaArchiveFolder对象
            ToaArchiveFolder foder = manager.getFolder(str[i]);
            //得到案卷号追加到StringBuffer中
            if(i!=str.length-1){ 
                           sb.append(foder.getFolderNo()).append(",");
                              }else{
                           sb.append(foder.getFolderNo());  
                       }
           
         }
        //传入jsp页面的值
        return renderText(sb.toString());
    }
    
	/**

	 * 根据年度 
	 * 判断编号是否存在
	 * @author 
	 * @date 2013-06-20
	 * @return
	 */
	public String isHasthesameNoandDate()throws Exception{
			List list=manager.getFolderByNoandDate(model.getFolderNo(),folderDate1,new OALogInfo("根据卷（盒）编号获取卷（盒）列表"));
				ToaArchiveFolder folderModel=new ToaArchiveFolder();
				boolean isBooleanB=false;
		
				if(list!=null&&list.size()>=1 ){
					for(int i=0;i<list.size();i++){
						folderModel=(ToaArchiveFolder)list.get(i);
						if(folderId!=null&&!folderId.equals("")&&folderModel.getFolderId().equals(folderId)){
							isBooleanB=true;
							break;
						}				
					}
					if(isBooleanB){
						return renderHtml("0");
					}
					else {
						return renderHtml("1");
					}
				}
				else{
					return renderHtml("0");
				}
	}
	/**

	 * 初始化案卷对象
	 * 
	 * @roseuid 495072D30251
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (folderId != null) {
			model = manager.getFolder(folderId);
			if(model.getFolderDepartment()!=null){
				Organization org = manager.getDepartmentByOrgId(model.getFolderDepartment());
				model.setFolderDepartmentName(org.getOrgName());// 待组织机构完善后补充
			}
		} else {
			model = new ToaArchiveFolder();
			User user = manager.getCurrentUser();
			if (user != null) {
				Organization org = manager.getUserDepartmentByUserId(user.getUserId());
				if(model.getFolderDepartment()==null||model.getFolderDepartment()==""){
					model.setFolderDepartment(org.getOrgId());
					model.setFolderDepartmentName(org.getOrgName());
//					model.setFolderOrgcode(org.getSupOrgCode());
				}
				model.setFolderCreaterId(user.getUserId());
				model.setFolderCreaterName(user.getUserName());
				
			}
			List numList=dictservice.getItemsByDictValue("SSQZ");//全宗号
			if(numList!=null){
				for(int i=0;i<numList.size();i++){
					ToaSysmanageDictitem dic=(ToaSysmanageDictitem)numList.get(i);
					String dictName=dic.getDictItemName();
					if("1.059".equals(dictName))
					{
						model.setFolderOrgId(dic.getDictItemCode());
						model.setFolderOrgName(dic.getDictItemName());						
					}					
				}
			}
			model.setFolderDate(new Date());
		}
	}

	/**
	 * 初始化输入框
	 */
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if (archiveSortId != null)
		{
			ToaArchiveSort archiveSort=null;
			try {
				archiveSort = sortmanager.getArchiveSort(archiveSortId);
			} catch (Exception e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			if(archiveSort!=null)
				archiveSortName =archiveSort.getSortName();			
		}
		if (forward != null && forward.equals("viewFile")) {
			if (YES.equals(model.getFolderAuditing())||DESTR_AUDIT.equals(model.getFolderAuditing())||DESTR_BACK.equals(model.getFolderAuditing())) {
				fileList = manager.getArchiveFileByFolderId(folderId, filemodel);
				getfileappendsize();
				return "archiveFile";
			} else {
				fileList = manager.getFileByFolderId(folderId, tempfilemodel);
				getappendsize();
				return forward;
			}
			
		} else if ("searchborrowFile".equals(forward)
				|| "archiveFile".equals(forward)) {
			fileList = manager.getArchiveFileByFolderId(folderId, filemodel);
			getfileappendsize();
			return forward;
		} else if ("pige".equals(moduletype) || "manage".equals(moduletype)) {
			if (YES.equals(model.getFolderAuditing())) {
				addActionMessage("该案卷已归档，不能编辑！");
				return "reloads";
			} else if (AUDITING.equals(model.getFolderAuditing())) {
				addActionMessage("该案卷已提交归档，不能编辑！");
				return "reloads";
			}
		}
		limitdictList = dictservice.getItemsByValue("BGQX");
//		if (archiveSortId != null)
//			archiveSortName = sortmanager.getArchiveSort(archiveSortId)
//			.getSortName();
		if (forward == null || "".equals(forward) || "null".equals(forward)){
		    SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd");
		    if(model.getFolderId()!=null&&!"".equals(model.getFolderId())){
		        if(model.getFolderFromDate()!=null&&!"".equals(model.getFolderFromDate())){
		           folderFromDate1=st.parse(model.getFolderFromDate());
		         }
		        if(model.getFolderToDate()!=null&&!"".equals(model.getFolderToDate())){
		          folderToDate1=st.parse(model.getFolderToDate());
		        }
		    }
			forward = INPUT;
		}
		return forward;
	}

	/**
	 * author:zhangli 
	 * description:查看案卷文件 
	 * @return
	 */
	public String viewFile() throws Exception{
		filemodel = manager.getToaArchiveFile(fileIds);
		if(filemodel.getFileDepartment()!=null&&!"".equals(filemodel.getFileDepartment()))//判断部门是否为空
		{
			fileDepartmentName=getDepartment(filemodel.getFileDepartment());

		}
		eformContent="";
		fileFileName = "";
		Iterator<ToaArchiveFileAppend> appendlist=filemodel.getToaArchiveFileAppends().iterator();
		try {
			if(filemodel.getFileDocType()!=null&&!TempFileType.TEMPFILE.equals(filemodel.getFileDocType())){
				if(appendlist.hasNext()){
					ToaArchiveFileAppend app=appendlist.next();
					if(app.getAppendContent()!=null&&!app.getAppendContent().equals("")){
						   byte[] bufData = app.getAppendContent();
						   fileFileName = new String(bufData,"utf-8");	//电子表单信息
						   
					   }else {
						   fileFileName="附件内容字段为空";
					   }
					workflow=filemodel.getWorkflow();
					instanceId = workflow.split(";")[0];
					fileAppedId=app.getAppendId();			//附件ID
					appendFormId=filemodel.getFileFormId();
				}
				return "vieweform";
			}else{
//				if(appendlist.hasNext()){
//					ToaArchiveFileAppend app=appendlist.next();
//					   fileFileName+="<div id="+app.getAppendId()+" style=\"display: \"><a href=\"javascript:delAttach('"+app.getAppendId()+"')\">[删除]</a>" +
//						"<a href=\"javascript:download('"+app.getAppendId()+"')\">"+app.getAppendName()+"</a>&nbsp;</div>";
//						
//				   }
				if(filemodel.getFileAwardsOrgId()!=null&&!filemodel.getFileAwardsOrgId().equals("")){
					fileAwardsOrgLevel=dictservice.getDictItemName(filemodel.getFileAwardsOrgId())+"-"+filemodel.getFileAwardsOrg();
				}
				else {
					fileAwardsOrgLevel=filemodel.getFileAwardsOrg();
				}
				return "viewArchivefile";
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	    * 
	    * @author zhengzb
	    * @desc 全文检索中，通过档案文件所属附件ID，来查看档案文件。
	    * 2010-5-13 上午11:27:37 
	    * @param appendId   附件ID
	    * @return
	    */
	   public String searchViewAppend() throws  Exception{
		   
		   StringBuffer returnhtml = new StringBuffer();

		   
		   if(fileAppedId!=null&&!fileAppedId.equals("")){
			   ToaArchiveFileAppend fileAppend=manager.getArchiveFileAppendByAppendId(fileAppedId, new OALogInfo("根据档案文件附件id获取附件"));
			   if(fileAppend!=null){
				   
				   String fileId=fileAppend.getToaArchiveFile().getFileId();
				   String fileTitle=fileAppend.getToaArchiveFile().getFileTitle();
				   returnhtml.append(fileId+","+fileTitle);
				   return renderHtml(returnhtml.toString());
			   }
			   else{
				   return renderHtml("当前附件不存在");
			   }
			   
		   }
		   else{
			   return renderHtml("当前附件不存在");
		   }
	   }

	   /**
	    * 
	    * @author zhengzb
	    * @desc 全文检索中，通过档案文件ID，来查看档案文件。
	    * 2010-6-24 下午06:30:14 
	    * @return
	    * @throws Exception
	    */
	   public String searchViewFile() throws  Exception{
		   StringBuffer returnhtml = new StringBuffer();
		   if(fileIds!=null&&!fileIds.equals("")){
			   filemodel = manager.getToaArchiveFile(fileIds);
			   if(filemodel!=null){
				   returnhtml.append(fileIds+","+filemodel.getFileTitle());
			   }else{
				   returnhtml.append("当前附件不存在");
			   }
			  
		   }else{
			   returnhtml.append("当前附件不存在");
		   }
		   return renderHtml(returnhtml.toString());
	   }
	   
	/**
	 * 作者：郑志斌
	 * 修改时间： 2010-04-02 11:47
	 * 修改内容： model.setFolderAuditing("0");  给审核字段添加“0”
	 * 作者：彭小青 
	 * 修改时间：2008-12-26下午01:23:25 
	 * 说明：
	 * @param
	 * @return
	 */
	public String gropFile() throws Exception{
		String[] fileId = fileIds.split(",");
		if ("".equals(model.getFolderId()))
			model.setFolderId(null);
		model.setFolderAuditing("0");
		if(model.getFolderDepartment()!=null){
			Organization org = manager.getDepartmentByOrgId(model.getFolderDepartment());
			if(model.getFolderDepartmentName()!=org.getOrgName())
			{
				model.setFolderDepartmentName(org.getOrgName());	
			}
		}
		String msg="资料中心文件组卷（盒）失败！";
		String message=manager.saveFolder(model,new OALogInfo("保存案卷"));//先判断“案卷编号”是否存在，在保存
		manager.gropFile(model, fileId,new OALogInfo("文件组卷"));
		msg="资料中心文件组卷（盒）成功！";
		//	 addActionMessage(msg);	
		StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/workservice.js'>")
				.append("</SCRIPT>")
				.append("<SCRIPT src='")
				.append(getRequest().getContextPath())
				.append(
				"/common/js/commontab/service.js'>")
				.append("</SCRIPT>")
				.append("<script>")
				.append("alert('")
				.append(msg)
				.append("');")
				.append(" location='")
				.append(getRequest().getContextPath())
//				.append("/archive/tempfile/tempFile.action")
				.append("/archive/tempfile/tempFile.action?depLogo=" + depLogo +"&treeType="+treeType+"&treeValue="+treeValue)
				.append("';</script>");
		return renderHtml(returnhtml.toString()); 
	}
	
	/**
	    * 判断案卷归档是否申请归档还是直接归档
	    * @author zzbsteven 2010/04/23 9:25
	    * @return "0" :申请归档，"1"：直接归档
	    * @throws Exception
	    */
	   public String archiveIsEnable()throws Exception{
		   ToaSystemset systemset=new ToaSystemset();
		   systemset=systemsetManager.getSystemset();
		   return renderHtml(systemset.getArchiveIsEnable());//返回系统设置的”是否直接归档值（”0“，”1“）“
	   }
	   
	  /** 
	   * @author zzbsteven  2010/04/23 9:25
	   * @param 直接归档案卷
	   * */
	   public String auditArchive() throws Exception{
		    addActionMessage(manager.auditArchiveFolder(folderId, new OALogInfo("直接归档")));
			return "reloads";
		}

	public ToaArchiveFolder getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getArchiveSortName() {
		return archiveSortName;
	}

	public List getLimitdictList() {
		return limitdictList;
	}

	@Autowired
	public void setDictservice(IDictService dictservice) {
		this.dictservice = dictservice;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public List getDictList() {
		return dictList;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	@Autowired
	public void setSortmanager(ArchiveSortManager sortmanager) {
		this.sortmanager = sortmanager;
	}

	public String getDictName() {
		return dictName;
	}

	public Map getStatemap() {
		return statemap;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getModuletype() {
		return moduletype;
	}

	public void setModuletype(String moduletype) {
		this.moduletype = moduletype;
	}

	public List getSortList() {
		return sortList;
	}

	public String getFileIds() {
		return fileIds;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

	public List getFileList() {
		return fileList;
	}

	public ToaArchiveFile getFilemodel() {
		return filemodel;
	}

	public void setFilemodel(ToaArchiveFile filemodel) {
		this.filemodel = filemodel;
	}

	public ToaArchiveTempfile getTempfilemodel() {
		return tempfilemodel;
	}

	public void setTempfilemodel(ToaArchiveTempfile tempfilemodel) {
		this.tempfilemodel = tempfilemodel;
	}

	public List getOrgList() {
		return orgList;
	}

	public List getUserList() {
		return userList;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getHascheckedvalues() {
		return hascheckedvalues;
	}

	public void setHascheckedvalues(String hascheckedvalues) {
		this.hascheckedvalues = hascheckedvalues;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getEformContent() {
		return eformContent;
	}

	public void setEformContent(String eformContent) {
		this.eformContent = eformContent;
	}


	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileAwardsOrgLevel() {
		return fileAwardsOrgLevel;
	}

	public void setFileAwardsOrgLevel(String fileAwardsOrgLevel) {
		this.fileAwardsOrgLevel = fileAwardsOrgLevel;
	}

	public String getArchiveIsEnable() {
		return archiveIsEnable;
	}

	public void setArchiveIsEnable(String archiveIsEnable) {
		this.archiveIsEnable = archiveIsEnable;
	}

	public String getFileAppedId() {
		return fileAppedId;
	}

	public void setFileAppedId(String fileAppedId) {
		this.fileAppedId = fileAppedId;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getTreeValue() {
		return treeValue;
	}

	public void setTreeValue(String treeValue) {
		this.treeValue = treeValue;
	}
	
	public String getDepLogo() {
		return depLogo;
	}

	public void setDepLogo(String depLogo) {
		this.depLogo = depLogo;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	 public InputStream getStream() {
	        return stream;
	    }

    public String getFolderLimitId() {
        return folderLimitId;
    }

    public void setFolderLimitId(String folderLimitId) {
        this.folderLimitId = folderLimitId;
    }

    public String getFolderDepartmentName() {
        return folderDepartmentName;
    }

    public void setFolderDepartmentName(String folderDepartmentName) {
        this.folderDepartmentName = folderDepartmentName;
    }

    public String getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderDate() {
        return folderDate;
    }

    public void setFolderDate(String folderDate) {
        this.folderDate = folderDate;
    }

    public String getFolderAuditing() {
        return folderAuditing;
    }

    public void setFolderAuditing(String folderAuditing) {
        this.folderAuditing = folderAuditing;
    }

	public String getFolderDate1() {
		return folderDate1;
	}

	public void setFolderDate1(String folderDate1) {
		this.folderDate1 = folderDate1;
	}
    
	
	/**
	 * 根据获取的卷(盒)号
	 * 判断卷(盒)是否归档
	 * @author 
	 * @date 2013-06-27
	 * @return 
	 */
	public String getfolderAuditing()throws Exception{
		  String[] str = folderId.split(",");	
				ToaArchiveFolder folderModel=new ToaArchiveFolder();
				boolean isBooleanB=false;
				  for (int i = 0; i < str.length; i++) {
			        folderModel = manager.getFolder(str[i]);
						if(!folderModel.getFolderAuditing().equals("0")){
							isBooleanB=true;
							break;
						}				
					}
					if(isBooleanB){
						return renderHtml("0");
					}
					else {
						return renderHtml("1");
					}
				}
	/**
	 * 根据获取的卷(盒)号
	 * 获取各个卷(盒)的文件列表
	 * @author 
	 * @date 2013-06-27
	 * @return 
	 */
	public String getArchiveFile()throws Exception{
		 
		tempfileList= manager.getFilesByFolderIds(folderId,tempfilemodel);
		getRequest().setAttribute("folderId", folderId);
		 return "createPieceNo";
	
}
	/**
     *  生成件号
     * 
     * @author xush
     * @date 2013-06-27
     * @return 
     */
    public void createPieceNo()throws Exception{
        List<ToaArchiveTempfile> tempFileList=new ArrayList<ToaArchiveTempfile>();
       
          tempFileList= manager.createPieceNo(folderId,tempfilemodel);
          if(tempFileList!=null&&tempFileList.size()>0){
              manager.createPieceNoTrue(tempFileList);
          }
        }
       
    /**
     * 校验在此page页面下是否有相同的件号
     * 
     * @author xush
     * @date 7/3/2013 11:41 AM
     * @return 
     */
    public String changePieceNo()throws Exception{
       String msg="0";
       List<ToaArchiveTempfile> tempFileList=new ArrayList<ToaArchiveTempfile>();
       tempFileList=manager.getSamePieceNo(folderId,tempfileId,tempPieceNo);
         // manager.getTempfileById(tempfileId,tempPieceNo);
       if(tempPieceNo!=null&&!"".equals(tempPieceNo)){
           if(tempFileList!=null&&tempFileList.size()>0){
             msg="1";
                 }
           }
       return renderText(msg); 
        }
    /**
     *  编辑并保存件号
     * 
     * @author xush
     * @date 7/8/2013 11:28 AM
     * @return 
     */
    public void savePieceNo()throws Exception{
        List<ToaArchiveTempfile> tempFileList=new ArrayList<ToaArchiveTempfile>();
        tempFileList = manager.createPieceNo(folderId,null);
        manager.saveTempfPieceNo(tempFileList,tempfPieceNo);  
        }
	/**
	 * 根据获取的卷(盒)号,判断是否案卷的文件已生成件号
	 * @return 已生成件号 返回  1，未生成件号 返回 0
	 * @date 6/28/2013 11:24 AM
	 * 
	 * */
	public String archiveIsCreatePieceNo()throws Exception{
		//根据卷(盒)号，获取案卷的文件列表
		fileList= manager.createPieceNo(folderId,tempfilemodel);
		boolean isBooleanB=false;
        if(fileList!=null && fileList.size()>0){
			for(int i=0;i<fileList.size();i++){
				ToaArchiveTempfile tempfile=(ToaArchiveTempfile)fileList.get(i);
				//文件的件号是否为空
				if(tempfile.getTempfilePieceNo()==null){
					isBooleanB=true;
					break;
				}
			}
        }
		if(isBooleanB){
			return renderHtml("0");
		}
		else {
			return renderHtml("1");
		}
	}

    public String getTempfileId() {
        return tempfileId;
    }

    public void setTempfileId(String tempfileId) {
        this.tempfileId = tempfileId;
    }

    public String getTempPieceNo() {
        return tempPieceNo;
    }

    public void setTempPieceNo(String tempPieceNo) {
        this.tempPieceNo = tempPieceNo;
    }

    public void setTempfilemanager(TempFileManager tempfilemanager) {
        this.tempfilemanager = tempfilemanager;
    }

    public String getTempfPieceNo() {
        return tempfPieceNo;
    }

    public void setTempfPieceNo(String tempfPieceNo) {
        this.tempfPieceNo = tempfPieceNo;
    }

    public String getFolderDateY() {
        return folderDateY;
    }

    public void setFolderDateY(String folderDateY) {
        this.folderDateY = folderDateY;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getMonth1() {
        return month1;
    }

    public void setMonth1(String month1) {
        this.month1 = month1;
    }

    public String getOrgId1() {
        return orgId1;
    }

    public void setOrgId1(String orgId1) {
        this.orgId1 = orgId1;
    }

    public String getFileFolder() {
        return fileFolder;
    }

    public void setFileFolder(String fileFolder) {
        this.fileFolder = fileFolder;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getDisLogo1() {
        return disLogo1;
    }

    public void setDisLogo1(String disLogo1) {
        this.disLogo1 = disLogo1;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Date getFolderFromDate1() {
        return folderFromDate1;
    }

    public void setFolderFromDate1(Date folderFromDate1) {
        this.folderFromDate1 = folderFromDate1;
    }

    public Date getFolderToDate1() {
        return folderToDate1;
    }

    public void setFolderToDate1(Date folderToDate1) {
        this.folderToDate1 = folderToDate1;
    }

    public List<ToaArchiveTempfile> getTempfileList() {
        return tempfileList;
    }
	
  }
