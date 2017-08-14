package com.strongit.oa.personnel.baseperson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaPersonDeploy;
import com.strongit.oa.bo.ToaStructure;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.dataprivil.PostDataPrivilManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.infotable.IInfoTableService;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.personnel.deploymanage.Deploymanager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.personnel.structure.PersonStructureManager;
import com.strongit.oa.util.FieldValue;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * ��������ACTION
 * 
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/personnel/baseperson/person.action", type = ServletActionRedirectResult.class) })
public class PersonAction extends BaseActionSupport<ToaBasePerson> {

	private Page<ToaBasePerson> page = new Page<ToaBasePerson>(FlexTableTag.MAX_ROWS, true);
	
	/**信息集信息项*/
	private Page<List> infopage = new Page<List>(FlexTableTag.MAX_ROWS, true);
	
	private Page<Object[]> holidaypage = new Page<Object[]>(FlexTableTag.MAX_ROWS, true);

	private Map<String, String> strucTypeMap = new HashMap<String, String>();
	
	/**性别map*/
	private Map<String, String>  sexMap = new HashMap<String, String> ();

	/**人员类别map*/
	private Map<String, String>  personKindMap = new HashMap<String, String> (); 
	
	/**人员编制map*/
	private Map<String, String>  bianzhiMap = new HashMap<String, String> (); 
	
	/**人员编制map*/
	private Map<String, String> deployTypeMap=new HashMap<String, String> (); 
	
	/**判断*/
	private Map<String, String>  judge = new HashMap<String, String> ();

	/** 人员BO */
	private ToaBasePerson model = new ToaBasePerson();
	
	/** 人员调配历史记录bo */
	private PersonDeployInfo deployinfo=new PersonDeployInfo();
	
	/** 机构BO */
	private ToaBaseOrg org = new ToaBaseOrg();	
	
	/** 机构ID */
	private String orgId;
	
	/** 人员ID */
	private String personId;
	
	/** 编制ID */
	private String strucId;
	
	/**信息集编码 */
	private String infoSetCode;
	
	/**信息集值*/
	private String infoSetValue;
	
	/**主键值*/
	private String keyid;  //用于添加、修改人员及人员子集信息
	
	/**外键值*/
	private String fkeyid; //用于添加、修改人员及人员子集信息
	
	/**信息集主键 */
	private String pkey;

	/** 人员照片 */
	private String photo;

	/** 照片文件 */
	private File file;
	
	private String fileName;
	
	/**读写状态，暂时没用*/
	private String readonly;
	
	/**调配类别ID */
	private String deployId;
	
	/**信息项值*/
	private String objId;       //用户展现字典树
	
	/**信息项值+name*/
	private String  objName;     //用户展现字典树
	
	/**字典类ID*/
	private String dictCode;	//用户展现字典树
	
	private String hascheckedvalues=null;
	
	/**字典类名称*/
	private String dictName;   //用户展现字典树
	
	/**树形JS代码*/
	private String treeScript;	//用户展现字典树
	
	/**人员类别编码*/
	private String persontype; 
	
	/**人员类别名*/
	private String typename;
	
	/**跳转变量*/
    private String forward;
    
    /**查看标识*/
    private String disLogo;
    
	/**存放消息字符串*/
	private String msg;
	
	/**人员身份*/
	private String personstatus;
	
	private String condition;
	
	/**人员信息集编码*/
	private final static String personstructcode = "40288239230c361b01230c7a60f10015";
	
	private final static String PERSON_TABLE_NAME="T_OA_BASE_PERSON";
	
	private final static String PERSON_PKEY="PERSONID";
	
	private final static String personkey=PERSON_TABLE_NAME+"."+PERSON_PKEY;			//人员信息集主键
	
	private final static String persontable=PERSON_TABLE_NAME+" "+PERSON_TABLE_NAME;	//人员信息集
	
	/**在职人员*/
	private final static String ZZRY="4028822723fece180123fed551ae0008";
	
	/**退休人员*/
	private final static String TXRY="ff8080812015288501201792ab090061";
	
	private final static String ZERO="0";
	
	private final static String ONE="1";
	
	private final static String TWO="2";
	
	private final static String FOUR="4";
	
	/**人员调配时默认展现的信息*/
	private String defaultshow="PERSONID,PERSON_NAME,PERSON_SAX,";
		
	/** 信息集manager*/
	private InfoSetManager infoManager;
	
	/**信息项manager*/
	private InfoItemManager itemManager;

	/** 人员MANAGER */
	private PersonManager manager;

	/** 机构MANAGER */
	private PersonOrgManager personOrgManager;

	/** 机构编制manager*/
	private PersonStructureManager strucManager;
	
	/** 字典接口 */
	private IDictService dictService;
	
	/**信息集服务接口*/
	private IInfoTableService infoTable;
	
	/**字典类接口*/
	private DictTypeManager dicttypemanager;
	
	/**调配类别manager*/
	private Deploymanager deploymanager;
	
	@Autowired private PostDataPrivilManager privilManager;
	
	/** 信息集列表*/
	private List<ToaSysmanageStructure> tableList;
	
	/** 信息项列表*/
	private List<ToaSysmanageProperty> columnList;

	/** 待遇级别列表 */
	private List<ToaSysmanageDictitem> dictItemList;

	/** 机构编制列表 */
	private List<ToaStructure> strucList;
	
	/**调配类别列表 */
	private List<ToaPersonDeploy> deployTypeList;
	
	/**调配历史记录列表 */
	private List<PersonDeployInfo> deployInfoList;
	
	/**组织机构列表*/
	private List<TempPo> orglist=new ArrayList<TempPo>();
	
	/**休假情况统计明细*/
	private List<Object[]> detailList=new ArrayList<Object[]>();
	
	/**查询语句中的from子句内容*/
	private String tables;			
	
	public PersonAction() {
		   
	}
	
	/**
	 * 删除人员基本信息
	 */
	@Override
	public String delete() throws Exception {
		String[] ids=personId.split(",");
		manager.delete(ids);
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/baseperson/person.action?orgId=" + orgId
				+ "';</script>");
	}

	/**
	 * 填充编制map
	 */
	private void getDictitem() {
		List<ToaSysmanageDictitem> dictlist = new ArrayList<ToaSysmanageDictitem>();
		dictlist = dictService.getItemsByDictValue("BIANZHI");// 获取编制
		for (ToaSysmanageDictitem dict : dictlist) {
			strucTypeMap.put(dict.getDictItemCode(), dict.getDictItemName());
		}
	}

	/**
	 * 添加页面初始化
	 */
	@Override
	public String input() throws Exception {
		// 判断机构ID是否为空，不会空就获取该机构下的所有编制
		if (orgId != null && !"".equals(orgId)) {

			strucList = strucManager.getStructureByOrg(orgId);
			org = personOrgManager.getOrgByID(orgId);
			getDictitem();
			for (ToaStructure stru : strucList) {
				stru.setStrucTypeName(strucTypeMap.get(stru.getStrucType()));
			}
		}
		// 判断人员照片是否为空, 为空就是用默认照片*/
		if (model.getPersonPhoto() == null || "".equals(model.getPersonPhoto())) {
			photo = "/personPhoto/no.jpg";
		} else {

			photo = new String(model.getPersonPhoto());
		}
		ToaSysmanageStructure tabl=infoManager.getToaStructByValue(PERSON_TABLE_NAME);
		tableList=infoManager.getChildCreatedInfoSet(tabl.getInfoSetCode());
		
		return "view";
	}

	@Override
	public String list() throws Exception {
		dictItemList = dictService.getItemsByDictValue("SEX");
		try {
			for(Iterator it = dictItemList.iterator(); it.hasNext();) {
				ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
				sexMap.put(toas.getDictItemCode(),toas.getDictItemShortdesc());
			}
			dictItemList = dictService.getItemsByDictValue("PERSONKIND");
			for(Iterator it = dictItemList.iterator(); it.hasNext();) {
				ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
				personKindMap.put(toas.getDictItemCode(),toas.getDictItemShortdesc());
			}
			if(orgId!=null&&!"".equals(orgId)){
				strucList=strucManager.getStructureByOrg(orgId);
				for(Iterator it = strucList.iterator(); it.hasNext();) {
					ToaStructure toas = (ToaStructure) it.next();
					bianzhiMap.put(toas.getStrucId(),toas.getStrucTypeName());
				}
			}
			if (strucId != null && !"".equals(strucId)) {// 根据编制查询人员列表
				page = manager.getPersonByStruc(page, strucId);
				return "structure";
			}else{	// 根据查询条件查询人员列表
				page = manager.getPersonByOrg(page, model,orgId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	
	@Override
	protected void prepareModel() throws Exception {
		/*if (personId != null && !"".equals(personId)) {
			model = manager.getPersonByID(personId);
		} else {
			model = new ToaBasePerson();
		}
		if (orgId != null && !"".equals(orgId)) {
			org = personOrgManager.getOrgByID(orgId);
		} else {
			org = new ToaBaseOrg();
		}*/
	}

	@Override
	public String save() throws Exception {
		try {
			if (file != null && !"".equals(file)) {// 导读图片是否为空
				photo = getRequest().getParameter("photo");
				FileInputStream fis = new FileInputStream(file);
				byte[] buf = new byte[(int) file.length()];
				SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"yyyyMMddHHmmssms");
				String newFileName = sdf.format(new Date());
				String ext = photo
						.substring(photo.length() - 3, photo.length());
				String src = "/personPhoto/" + newFileName + "." + ext;
				FileOutputStream fos = new FileOutputStream(getRequest()
						.getSession().getServletContext().getRealPath("/")
						+ src);
				fis.read(buf);
				fos.write(buf);
				fos.flush();

				model.setPersonPhoto(src.getBytes());// 获取文件
				fos.close();
			}
		} catch (RuntimeException e) {

			e.printStackTrace();
		}
		model.setBaseOrg(org);
		if ("".equals(model.getPersonid())) {
			model.setPersonid(null);
		}
		manager.save(model);
		if(model.getPersonStructId()!=null&&!"".equals(model.getPersonStructId())){
			this.updateStruc();
		}
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/baseperson/person.action?orgId=" + orgId
				+ "';</script>");
	}
	/**
	 * 修改编制状态
	 */
    private void updateStruc(){
    	strucId=model.getPersonStructId();
    	ToaStructure structure=strucManager.getStructureByID(strucId);
    	int strNumber=Integer.parseInt(structure.getStrucNumber());
    	int count=manager.getCountByStrucId(strucId);
    	if(count==strNumber){
    		structure.setStrucStatus("1");
    	}else if(count>strNumber){
    		structure.setStrucStatus("2");
    	}
    	strucManager.save(structure);
    }
    
    /*
     * 
     * Description:视图录入，获取人员及人员子集信息集列表
     * param: 
     * @author 	 彭小青
     * @date 	 Sep 27, 2009 10:05:11 AM
     */
    public String initViewAddTool()throws Exception{
    	if(forward!=null&&forward.equals("others")){	//人事信息查询模块
    		tableList=privilManager.getUserInfoStru();	//查找人员有权限的子集
    	}else{	
    		tableList=infoManager.getChildCreatedInfoSet2(PERSON_TABLE_NAME,true);
    	}
    	HttpServletRequest request=getRequest();			//获取session
    	request.setAttribute("tableList",tableList);		//保存到session范围内
 	    if(forward!=null&&(forward.equals("initviewedit")||forward.equals("viewinfo"))){	//编辑子集的跳转
 	    	return "initviewedit";
 	    }else if(forward!=null&&forward.equals("others")){	
 	    	return "others";
 	    }else{			//添加的跳转
 	    	return "initviewadd";
 	    }
    }
    
    /*
     * 
     * Description:视图录入，获取信息集的信息项列表
     * param: 
     * @author 	 彭小青
     * @date 	 Sep 27, 2009 10:05:14 AM
     */
    public String initViewAddPerson()throws Exception{
    	ToaSysmanageStructure struct=null;
    	if(infoSetCode==null||infoSetCode.equals("")){//信息集编码没值
    		struct=infoManager.getToaStructByValue(infoSetValue);
    		infoSetCode=struct.getInfoSetCode();
    	}else{										 //信息集编码有值
    		struct=infoManager.getInfoSet(infoSetCode);
    		infoSetValue=struct.getInfoSetValue();
    	}	
    	pkey=struct.getInfoSetPkey();					//信息集的主键
    	persontype=ZZRY;								//人员类别默认值（字典项编码）
    	typename=dictService.getDictItemName(persontype);//人员类别默认值（字典项名）
    	List<ToaSysmanageProperty> proList = itemManager.getAllCreatedItems(infoSetCode);
    	HttpServletRequest request=getRequest();	//获取session
 	    request.setAttribute("proList",proList);	//保存到session范围内
    	return "viewadd";
    }
    
    /*
     * 
     * Description:视图录入，保存录入的信息
     * param: 
     * @author 	 彭小青
     * @date 	 Sep 27, 2009 10:05:17 AM
     */
    public String viewAddPerson()throws Exception{
    	ToaSysmanageStructure struct=infoManager.getInfoSet(infoSetCode);
    	HttpServletRequest request=getRequest();
    	String flag="true";
    	try{
    		keyid=manager.addPersonInfo(request,struct);	//增加人员或人员子集记录,keyid为人员或人员子集的主键值
    		if (PERSON_TABLE_NAME.equals(struct.getInfoSetValue())&&file!=null&&!"".equals(file)) {//图片不为空
    			manager.savePhoto(file, keyid);
    		}
    	}catch(Exception e){
    		flag="false";
    		e.printStackTrace();
    	}
    	if(struct!=null&&struct.getInfoSetValue().equals(PERSON_TABLE_NAME)){	//添加人员信息
    		personId=keyid;
    		fkeyid=orgId;
    		if(flag.equals("false")){
    			msg="人员信息添加失败!";
    		}
    	}else{	//添加子集信息
    		fkeyid=personId;
    		if(flag.equals("false")){
    			msg="人员子集信息添加失败!";
    		}
    	}
    	if(forward!=null&&forward.equals("addRelationInfo")){	//编辑子集信息时，添加子集信息的跳转
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
    		.append("<script>");
    		if(msg!=null&&!"".equals(msg)){
    			returnhtml.append("alert('").append(msg).append("');");
    		}
    		returnhtml.append("window.close();window.dialogArguments.submitForm();").append("</script>"); 
    		return renderHtml(returnhtml.toString());
    	}else{	//视图录入时，添加人员信息或子集信息时的跳转
    		return "temps";
    	}	
    }
    
    /*
     * 
     * Description:展现字典树或编制树、机构树
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 7, 2009 8:31:34 PM
     */
    public String infoItemTree()throws Exception{
    	if(objId!=null&&objId.equals("STRUC_ID")){	//编制字典项
    		HttpServletRequest request=getRequest();
    		dictName="人员编制";
    		strucList=strucManager.getStructureByOrg(orgId);	
    	}else if(objId!=null&&objId.equals("ORG_ID")){//人员调配，选择所属机构
    		dictName="所属机构";
    		List<ToaBaseOrg> list=personOrgManager.getOrgsByIsdel("0");//获取未删除的组织机构
    		List<String> syscodeList = new ArrayList<String>();//存放组织机构编号的list
    		for(int i=0;i<list.size();i++){
    			ToaBaseOrg org = list.get(i);
				syscodeList.add(org.getOrgSyscode());
			}
    		for(int i=0;i<list.size();i++){//循环组织机构列表
				TempPo po = new TempPo();
				po.setType("org");
				ToaBaseOrg org = list.get(i);
				String orgSysCode = org.getOrgSyscode();
				ToaBaseOrg porg = personOrgManager.getParentOrgByOrgSyscode(orgSysCode);//根据组织机构编码获取父组织机构
				String porgsyscode = porg.getOrgSyscode();
				if(porg==null||orgSysCode.equals(porgsyscode)||!syscodeList.contains(porgsyscode))//没有找到父组织机构，则设置父编码为空
						po.setParentId(null);
				else
					po.setParentId(porg.getOrgid());
				po.setId(org.getOrgid());
				po.setCodeId(orgSysCode);
				po.setName(org.getOrgName());
				orglist.add(po);
			}
    	}else{	//其它字典项
    		ToaSysmanageDict dict=dicttypemanager.getDictType(dictCode);
    		dictName=dict.getDictName();
        	dictItemList=dictService.getItemsByDictValue(dict.getDictValue());
    	}
		return "dictree";
    }
    
    /*
     * 
     * Description:编辑人员或人员子集信息
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 7, 2009 8:31:30 PM
     */
    public String initEditPerson()throws Exception{
    	ToaSysmanageStructure struct=infoManager.getInfoSet(infoSetCode);
    	List<ToaSysmanageProperty> proList = itemManager.getAllCreatedItems(infoSetCode);
    	HttpServletRequest request=getRequest();	//获取session
		List<FieldValue> propertyList =manager.getSingleTableValue(request,proList,struct.getInfoSetValue(),struct.getInfoSetPkey(),keyid);		
 	    request.setAttribute("propertyList",propertyList);	//保存到session范围内
 	    //编辑人员、编辑子集功能中（查看人员信息，查看子集、编辑子集信息）的跳转
 	    if(forward!=null&&(forward.equals("viewedit")||forward.equals("view")||forward.equals("viewInfo")||forward.equals("editRelationInfo")||forward.equals("viewRelationInfo"))){
 	    	return "viewedit";
 	    }else{	//视图录入完后的跳转
 	    	return "viewaddedit";
 	    }
		
    }
    
    /*
     * 
     * Description:保存编辑后的人员或人员子集信息
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 7, 2009 8:31:26 PM
     */
    public String editPerson()throws Exception{
    	HttpServletRequest request=getRequest();
    	try{
    		manager.editPersonInfo(request);	//编辑人员或人员子集记录
    		if (personstructcode.equals(infoSetCode)&&file!=null&&!"".equals(file)) {//图片不为空
    			manager.savePhoto(file, keyid);
    		}
		}catch(Exception e){
			msg="视图编辑人员失败!";
		}
		addActionMessage(msg);
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
		.append("<script>");
		if(msg!=null&&!"".equals(msg)){
			returnhtml.append("alert('").append(msg).append("');");
		}
		if(forward!=null&&(forward.equals("viewedit")||forward.equals("editRelationInfo")||forward.equals("saveAndClose"))){	//保存编辑的人员信息或人员子集信息的跳转
			returnhtml.append("window.close();window.dialogArguments.submitForm();").append("</script>");    	
 	    }else if(forward!=null&&forward.equals("saveAndClose")){	//视图录入时,保存并关闭的跳转
 	    	returnhtml.append("window.parent.close();window.parent.dialogArguments.submitForm();").append("</script>");    
 	    }else if(forward!=null&&forward.equals("saveAndAdd")){		//视图录入时,保存并添加的跳转
    		if(infoSetCode=="40288239230c361b01230c7a60f10015"){	//如果为人员信息集
    			returnhtml.append("window.parent.addnewPerson();");
		    }else{
		    	returnhtml.append(" window.parent.addnewInfo();");	
		    } 
    		returnhtml.append("window.parent.dialogArguments.submitForm();").append("</script>");    	
    	}else{ 	//视图录入时，保存编辑信息的跳转
			returnhtml.append(" location='")
					.append(getRequest().getContextPath())
					.append("/personnel/baseperson/person!initEditPerson.action?infoSetCode=")
					.append(infoSetCode)
					.append("&keyid=")
					.append(keyid)
					.append("&personId=")
					.append(personId)
					.append("&orgId=")
					.append(orgId)
					.append("';</script>");
 	    	
 	   }
	   return renderHtml(returnhtml.toString());
    } 
    
    /*
     * 
     * Description:编辑人员时，查看人员照片
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 7, 2009 8:31:22 PM
     */
    public String viewPersonPhoto()throws Exception{
    	HttpServletResponse response = this.getResponse();
    	manager.setContentToHttpResponse(response, personId);
    	return null;
    }
    
    /*
     * 
     * Description:判断是否超编
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 8, 2009 5:53:35 PM
     */
    public String isOutOfQuata()throws Exception{
    	String isfull=manager.isOutOfNum(orgId, personstatus,personId);
    	String status=manager.isOutOfQuata(orgId, strucId, personId);
    	if(isfull!=null&&isfull.equals(FOUR)){
    		renderText(status);
    	}else{
    		renderText(isfull);
    	}
		return null;
    }

    /*
     * 
     * Description:判断职数是否超编
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 29, 2009 3:41:23 PM
     */
    public String isOutOfNum()throws Exception{
        renderText(manager.isOutOfNum(orgId, personstatus,personId));
		return null;
    }
    
    /*
     * 
     * Description:获取人员子集列表信息
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 10, 2009 9:39:34 AM
     */
    public String viewRelationInfos()throws Exception{
    	columnList=itemManager.getCreatedItemsByCode(infoSetCode);
    	if(columnList!=null&&columnList.size()>0){
    		ToaSysmanageStructure struct=infoManager.getInfoSet(infoSetCode);
    		pkey=struct.getInfoSetPkey();
    		infoSetValue=struct.getInfoSetValue();
    		objName=struct.getInfoSetName();
    		infopage = infoTable.getTablePage(columnList, infoSetCode, PERSON_PKEY+"='"+personId+"' and (ISDEL is null or ISDEL='"+ZERO+"') ", infopage);
    	}
    	return "infolist";
    } 
    
   /*
    * 
    * Description:删除数据库表的信息记录
    * param: 
    * @author 	 彭小青
    * @date 	 Oct 12, 2009 9:14:51 AM
    */
    public String deleteRelationInfo()throws Exception{
    	 ToaSysmanageStructure struct=infoManager.getInfoSet(infoSetCode);
    	 msg=manager.deleteRelationInfo(struct, keyid);	//删除信息
    	 addActionMessage(msg);
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
 		.append("<script>");
 		if(msg!=null&&!"".equals(msg)){
 			returnhtml.append("alert('").append(msg).append("');");
 		}
 		returnhtml.append(" location='")
		.append(getRequest().getContextPath())
		.append("/personnel/baseperson/person!viewRelationInfos.action?infoSetCode=")
		.append(infoSetCode)
		.append("&pro=PERSONID")
		.append("&personId=")
		.append(personId)
		.append("&orgId=")
		.append(orgId)
		.append("';</script>");	
 		return renderHtml(returnhtml.toString());
   }
   
    /*
     * Description:初始化人员调配界面
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 13, 2009 9:57:40 AM
     */
    public String viewChangeInfo()throws Exception{
    	deployTypeList=deploymanager.getDeployes();//获取调配类别列表
    	ToaPersonDeploy deploytype=null;
    	HttpServletRequest request=this.getRequest();
    	if(deployId!=null&&!"".equals(deployId)){	//调配类别ID不为空
    		deploytype=deploymanager.getOnePersonDeploy(deployId);
    	}else{
    		deploytype=deployTypeList.get(0);
    		deployId=deploytype.getPdepId();
    	}
    	String fileds=defaultshow;	//重新组装要调配的字段
    	if(deploytype.getPdepEditcode()!=null){
    		fileds+=deploytype.getPdepEditcode();
    	}
    	List<FieldValue> propertyList=manager.getSelectFiledValue(request, fileds, personId);	
 	    request.setAttribute("propertyList",propertyList);	//保存到session范围内
    	return "viewchangeinfo";
    }
    
    /*
     * 
     * Description:是否有调配类别信息
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 13, 2009 4:02:40 PM
     */
    public String isExistDeployType()throws Exception{
    	deployTypeList=deploymanager.getDeployes();
    	model=manager.getPersonByID(personId);
    	if(model.getPersonPersonKind()!=null&&model.getPersonPersonKind().equals(TXRY)){
			renderText(ZERO);
    	}else if(readonly!=null&&readonly.equals("true")&&(deployTypeList==null||deployTypeList.size()==0)){
    		renderText(ONE);
    	}else{
    		renderText(TWO);
    	}
		return null;
    }
    
    /*
     * 
     * Description:调配人员
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 21, 2009 3:06:10 PM
     */
    public String changePersonInfo()throws Exception{
    	HttpServletRequest request=getRequest();
    	msg=manager.ChangePersonInfo(request,deployinfo);	//编辑人员或人员子集记录
		addActionMessage(msg);
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
		.append("<script>");
		if(msg!=null&&!"".equals(msg)){
			returnhtml.append("alert('").append(msg).append("');");
		}
		returnhtml.append("window.close();window.dialogArguments.submitForm();").append("</script>"); 
		/*returnhtml.append(" location='")
		.append(getRequest().getContextPath())
		.append("/personnel/baseperson/person!viewChangeInfo.action?infoSetCode=")
		.append(infoSetCode)
		.append("&personId=")
		.append(personId)
		.append("&keyid=")
		.append(keyid)
		.append("&orgId=")
		.append(orgId)
		.append("&deployId=")
		.append(deployId)
		.append("';</script>");	 */ 	
		return renderHtml(returnhtml.toString());
    }
    
    /*
     * 
     * Description:查看人员调配信息列表
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 20, 2009 9:05:09 AM
     */
    public String viewDeloyInfo()throws Exception{
    	deployTypeList=deploymanager.getDeployes();	//调配类别列表
    	for(Iterator it = deployTypeList.iterator(); it.hasNext();) {//循环列表，填充map
    		ToaPersonDeploy toas = (ToaPersonDeploy) it.next();
			deployTypeMap.put(toas.getPdepId(),toas.getPdepName());
		}
    	deployInfoList=deploymanager.getPerDeployList(deployinfo,personId);	//根据人员id、查询条件获取人员的调配历史记录列表
    	ToaBasePerson person=null;
    	for(Iterator it = deployInfoList.iterator(); it.hasNext();){
    		PersonDeployInfo deployinfo=(PersonDeployInfo) it.next();
    		person=manager.getPersonByID(deployinfo.getPersonId());
    		if(person!=null)
    			deployinfo.setPersonName(person.getPersonName());
    	}
    	return "deploylist";
    }
    
    /*
     * 
     * Description:查看调配记录的详细信息
     * param: 
     * @author 	 彭小青
     * @date 	 Oct 20, 2009 10:33:29 AM
     */
    public String viewDeployDetails()throws Exception{
    	deployinfo=deploymanager.getDeployInfo(keyid);
    	return "deploydetail";
    }
    
    public String deleteDeployInfo()throws Exception{
    	String msg="";
    	try{
    		deploymanager.deleteDeployInfo(keyid);
    	}catch(Exception e){
    		msg="删除调配记录失败！";
    		e.printStackTrace();
    	}
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
  		.append("<script>");
  		if(msg!=null&&!"".equals(msg)){
  			returnhtml.append("alert('").append(msg).append("');");
  		}
  		returnhtml.append(" location='")
		.append(getRequest().getContextPath())
		.append("/personnel/baseperson/person!viewDeloyInfo.action?orgId=")
		.append(orgId)
		.append("&personId=")
		.append(personId)
		.append("';</script>");		
  		return renderHtml(returnhtml.toString());
    }
    
    /*
     * 
     * Description:人事信息查询模块按当前人员权限来展现信息集与信息项
     * param: 
     * @author 	    彭小青
     * @date 	    Apr 2, 2010 2:39:07 PM
     */
    public String getPersonInfoList()throws Exception{
    	try{
    		if(tables==null||"".equals(tables)){//当没有设置查询条件时
    			tables=persontable;
    		}		
    		columnList=privilManager.getUserInfoPros(personstructcode);		//获取当前用户的拥有权限的信息项列表
    		String query=manager.buildCondition(columnList,orgId,condition);//根据当前用户的信息项权限列表和人事人员访问权限构造查询条件
    		StringBuffer con=new StringBuffer("");
    		if(orgId==null||"".equals(orgId)||"null".equals(orgId)){		 //pengxq于20110106添加
    			con.append(query);
				tables+=" ,T_OA_BASE_ORG o";
				infopage = infoTable.getTablePage(columnList,tables,personkey,con.toString(),infopage);
			}else{
				if(tables.indexOf(",")!=-1){		//当多表查询人员信息时
	    			con.append(personkey)
	    				.append(" in (select ")
	    				.append(personkey)
	    				.append(" from ")
	    				.append(tables)
	    				.append(" where ")
	    				.append(query)
	    				.append(")");
	    		}else{
					con.append(query);
	    		}
				infopage = infoTable.getTablePage(columnList,persontable,personkey,con.toString(),infopage);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		infopage=new Page<List>(FlexTableTag.MAX_ROWS, true);
    		infopage.setTotalCount(0);
    		infopage.setTotalCount(0);
    	}		
    	return "query";
    }
    
    /*
     * 
     * Description:人事信息查询模块查看相应人员有权限的子集信息
     * param: 
     * @author 	    彭小青
     * @date 	    Apr 7, 2010 10:04:16 AM
     */
    public String viewPersonOtherInfo()throws Exception{
    	columnList=privilManager.getUserInfoPros(infoSetCode);
    	if(columnList!=null&&columnList.size()>0){
    		ToaSysmanageStructure struct=infoManager.getInfoSet(infoSetCode);
    		pkey=struct.getInfoSetPkey();
    		infoSetValue=struct.getInfoSetValue();
    		objName=struct.getInfoSetName();
    		String con=PERSON_PKEY+"='"+personId+"' and (ISDEL is null or ISDEL='"+ZERO+"')";
    		infopage = infoTable.getTablePage(columnList, infoSetCode,con,infopage);
    	}
    	return "otherinfo";
    }
    
    /*
     * 
     * Description:休假情况统计
     * param: 
     * @author 	    彭小青
     * @date 	    May 6, 2010 6:48:21 PM
     */
    public String getHolidayStatistic()throws Exception{
    	try{
    		columnList=privilManager.getUserInfoPros(personstructcode);		//获取当前用户的拥有权限的信息项列表
    		condition=URLDecoder.decode(condition, "utf-8");
    		holidaypage=manager.getHolidayStatistic(holidaypage,columnList,tables,condition,orgId,objName,forward);
    	}catch(Exception e){
    		e.printStackTrace();
    		infopage=new Page<List>(FlexTableTag.MAX_ROWS, true);
    		infopage.setTotalCount(0);
    		infopage.setTotalCount(0);
    	}
    	return "statistic";
    }
    
    /*
     * 
     * Description:查看休假情况明细
     * param: 
     * @author 	    彭小青
     * @date 	    May 6, 2010 11:00:06 PM
     */
    public String getHolidayDetails()throws Exception{
    	dictItemList = dictService.getItemsByDictValue("DRUGE");
    	for(Iterator it = dictItemList.iterator(); it.hasNext();) {
    		ToaSysmanageDictitem toas = (ToaSysmanageDictitem) it.next();
    		judge.put(toas.getDictItemCode(),toas.getDictItemShortdesc());
    	}
    	condition=URLDecoder.decode(condition, "utf-8");
    	detailList=manager.getHolidayStatisticDetail(tables,condition,personId);
    	return "details";
    }
    
    /*
     * 
     * Description:满足任职年限统计
     * param: 
     * @author 	    彭小青
     * @date 	    May 7, 2010 4:45:44 PM
     */
    public String getDutyStatistic()throws Exception{
    	dictItemList = dictService.getItemsByDictValue("ZW");
		columnList=privilManager.getUserInfoPros(personstructcode);		//获取当前用户的拥有权限的信息项列表
		condition=URLDecoder.decode(condition, "utf-8");
		String con=manager.buildCondition(columnList,null,condition);	//根据当前用户的信息项权限列表和人事人员访问权限构造查询条件
		if(columnList!=null&&columnList.size()>0&&dictItemList!=null&&dictItemList.size()>0)
			detailList=manager.getDutyStatistic(tables, con.toString(),dictItemList);
    	return "duty";
    }
    
    
    /*
     * 
     * Description:满足任职年限统计明细
     * param: 
     * @author 	    彭小青
     * @date 	    May 7, 2010 10:03:30 PM
     */
    public String getDutyDetails()throws Exception{
    	dictName=dictService.getDictItemName(dictCode);
    	columnList=privilManager.getUserInfoPros(personstructcode);		//获取当前用户的拥有权限的信息项列表
		condition=URLDecoder.decode(condition, "utf-8");
		String con=manager.buildCondition(columnList,null,condition);	//根据当前用户的信息项权限列表和人事人员访问权限构造查询条件
		holidaypage=manager.getDutyStatisticDetail(holidaypage,tables,con,dictCode);
    	return "dutydetail";
    }
    
	public String getStrucId() {
		return strucId;
	}

	public void setStrucId(String strucId) {
		this.strucId = strucId;
	}

	public ToaBasePerson getModel() {
		return model;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Page<ToaBasePerson> getPage() {
		return page;
	}

	public void setPage(Page<ToaBasePerson> page) {
		this.page = page;
	}

	public ToaBaseOrg getOrg() {
		return org;
	}

	public void setOrg(ToaBaseOrg org) {
		this.org = org;
	}

	public String getPhoto() {
		return photo;
	}

	public void sePhoto(String photo) {
		this.photo = photo;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	@Autowired
	public void setManager(PersonManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}
	
	@Autowired
	public void setStrucManager(PersonStructureManager strucManager) {
		this.strucManager = strucManager;
	}
	
	@Autowired
	public void setPersonOrgManager(PersonOrgManager personOrgManager) {
		this.personOrgManager = personOrgManager;
	}
	
	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public List<ToaStructure> getStrucList() {
		return strucList;
	}

	public void setStrucList(List<ToaStructure> strucList) {
		this.strucList = strucList;
	}

	public Map<String, String> getStrucTypeMap() {
		return strucTypeMap;
	}

	public void setStrucTypeMap(Map<String, String> strucTypeMap) {
		this.strucTypeMap = strucTypeMap;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public List<ToaSysmanageStructure> getTableList() {
		return tableList;
	}

	public void setTableList(List<ToaSysmanageStructure> tableList) {
		this.tableList = tableList;
	}

	public List<ToaSysmanageProperty> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ToaSysmanageProperty> columnList) {
		this.columnList = columnList;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public String getInfoSetCode() {
		return infoSetCode;
	}

	public void setInfoSetCode(String infoSetCode) {
		this.infoSetCode = infoSetCode;
	}

	public String getInfoSetValue() {
		return infoSetValue;
	}

	public void setInfoSetValue(String infoSetValue) {
		this.infoSetValue = infoSetValue;
	}

	public String getFkeyid() {
		return fkeyid;
	}

	public void setFkeyid(String fkeyid) {
		this.fkeyid = fkeyid;
	}

	public String getKeyid() {
		return keyid;
	}

	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	@Autowired
	public void setDicttypemanager(DictTypeManager dicttypemanager) {
		this.dicttypemanager = dicttypemanager;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getTreeScript() {
		return treeScript;
	}

	public void setTreeScript(String treeScript) {
		this.treeScript = treeScript;
	}

	public String getDictName() {
		return dictName;
	}

	public List<ToaSysmanageDictitem> getDictItemList() {
		return dictItemList;
	}

	public void setDictItemList(List<ToaSysmanageDictitem> dictItemList) {
		this.dictItemList = dictItemList;
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

	public String getHascheckedvalues() {
		return hascheckedvalues;
	}

	public void setHascheckedvalues(String hascheckedvalues) {
		this.hascheckedvalues = hascheckedvalues;
	}

	public String getMsg() {
		return msg;
	}

	public String getPersontype() {
		return persontype;
	}

	public String getTypename() {
		return typename;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getForward() {
		return forward;
	}
	
	@Autowired
	public void setInfoTable(IInfoTableService infoTable) {
		this.infoTable = infoTable;
	}

	public Page<List> getInfopage() {
		return infopage;
	}

	public void setInfopage(Page<List> infopage) {
		this.infopage = infopage;
	}

	@Autowired
	public void setDeploymanager(Deploymanager deploymanager) {
		this.deploymanager = deploymanager;
	}

	public List<ToaPersonDeploy> getDeployTypeList() {
		return deployTypeList;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getDefaultshow() {
		return defaultshow;
	}

	public void setDefaultshow(String defaultshow) {
		this.defaultshow = defaultshow;
	}

	public void setOrglist(List<TempPo> orglist) {
		this.orglist = orglist;
	}

	public List<TempPo> getOrglist() {
		return orglist;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public Map<String, String> getPersonKindMap() {
		return personKindMap;
	}
	
	public Map<String, String> getSexMap() {
		return sexMap;
	}

	public Map<String, String> getBianzhiMap() {
		return bianzhiMap;
	}

	public Map<String, String> getDeployTypeMap() {
		return deployTypeMap;
	}

	public List<PersonDeployInfo> getDeployInfoList() {
		return deployInfoList;
	}

	public void setModel(ToaBasePerson model) {
		this.model = model;
	}

	public PersonDeployInfo getDeployinfo() {
		return deployinfo;
	}

	public void setDeployinfo(PersonDeployInfo deployinfo) {
		this.deployinfo = deployinfo;
	}

	public void setPersonstatus(String personstatus) {
		this.personstatus = personstatus;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTables() {
		return tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}

	public Page<Object[]> getHolidaypage() {
		return holidaypage;
	}

	public void setHolidaypage(Page<Object[]> holidaypage) {
		this.holidaypage = holidaypage;
	}

	public List<Object[]> getDetailList() {
		return detailList;
	}

	public Map<String, String> getJudge() {
		return judge;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
