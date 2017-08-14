/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息项管理ACTION
 */
package com.strongit.oa.infotable.infoitem;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageFormManager;
import com.strongit.oa.bo.ToaSysmanagePmanager;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.infoManager.IInfoManagerService;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.infotable.infotype.InfoTypeManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "infoItem.action", type = ServletActionRedirectResult.class) })
public class InfoItemAction extends BaseActionSupport {
	/** 分页对象*/
	private Page<ToaSysmanageProperty> page = new Page<ToaSysmanageProperty>(FlexTableTag.MAX_ROWS,
			true);

	/** 信息项编号*/
	private String infoItemCode;
	/** 信息集构建信息*/
	private String infoSetState;
	/** 排序号*/
	private String infoItemOrderby;
	
	public String getInfoSetState() {
		return infoSetState;
	}

	public void setInfoSetState(String infoSetState) {
		this.infoSetState = infoSetState;
	}

	/** 信息项列表*/
	private List infoitemlist;

	/** 信息项状态情况*/
	private HashMap<String, String> statemap = new HashMap<String, String>();

	/** 信息项类型情况*/
	private HashMap<String, String> datetypemap = new HashMap<String, String>();

	/** 信息项必填情况*/
	private HashMap<String, String> flagmap = new HashMap<String, String>();

	/** 信息项读写属性情况*/
	private HashMap<String, String> prosetmap = new HashMap<String, String>();

	/** 信息项系统状态情况*/
	private HashMap<String, String> sysmap = new HashMap<String, String>();

	/** 信息集编号*/
	private String infoSetCode;

	/** 信息集名称*/
	private String infoSetName;

	/** 字典类列表*/
	private List dictList;

	/** 信息项分类列表*/
	private List proTypeList;

	/** 信息项对象*/
	private ToaSysmanageProperty model = new ToaSysmanageProperty();

	/** 信息项Manager*/
	private InfoItemManager manager;
	
	private IInfoManagerService infoManagerService;

	/** 字典类Manager*/
	private DictTypeManager dictmanage;

	/** 信息项分类Manager*/
	private InfoTypeManager typemanage;
	
	/** 是*/
	private static final String YES = "1";

	/** 否*/
	private static final String NO = "0";
	
	private static final String KEY = "15";
	
	private String usedInfoItem;									//信息项是否在表单中应用
	/** 信息集Manager*/
	private InfoSetManager Smanager;
	/**
	 * @roseuid 49479C710128
	 */
	public InfoItemAction() {
		statemap.put(NO, "未构建");
		statemap.put(YES, "已构建");

		sysmap.put("", "普通信息项");
		sysmap.put("0", "普通信息项");
		sysmap.put("1", "系统信息项");

		datetypemap.put("0", "字符串");
		datetypemap.put("1", "代码");
		datetypemap.put("2", "数值");
		datetypemap.put("3", "年");
		datetypemap.put("4", "年月");
		datetypemap.put("5", "年月日");
		datetypemap.put("6", "年月日时间");
		datetypemap.put("10", "文件");
		datetypemap.put("11", "图片");
		datetypemap.put("12", "电话号码");
		datetypemap.put("13", "文本");
		datetypemap.put("14", "备注");
		datetypemap.put("20", "大字段");
		datetypemap.put(KEY, "主外键");

		flagmap.put(NO, "否");
		flagmap.put(YES, "是");

		prosetmap.put("0", "可读写");
		prosetmap.put("1", "不可读");
		prosetmap.put("2", "只读");
	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<ToaSysmanageProperty> getPage() {
		return page;
	}

	/**
	 * Sets the value of the infoItemCode property.
	 * 
	 * @param aInfoItemCode
	 *            the new value of the infoItemCode property
	 */
	public void setInfoItemCode(java.lang.String aInfoItemCode) {
		infoItemCode = aInfoItemCode;
	}

	/**
	 * Access method for the infoitemlist property.
	 * 
	 * @return the current value of the infoitemlist property
	 */
	public java.util.List getInfoitemlist() {
		return infoitemlist;
	}

	/**
	 * Access method for the statemap property.
	 * 
	 * @return the current value of the statemap property
	 */
	public java.util.HashMap getStatemap() {
		return statemap;
	}

	/**
	 * Access method for the datetypemap property.
	 * 
	 * @return the current value of the datetypemap property
	 */
	public java.util.HashMap getDatetypemap() {
		return datetypemap;
	}

	/**
	 * Access method for the flagmap property.
	 * 
	 * @return the current value of the flagmap property
	 */
	public java.util.HashMap getFlagmap() {
		return flagmap;
	}

	/**
	 * Access method for the prosetmap property.
	 * 
	 * @return the current value of the prosetmap property
	 */
	public java.util.HashMap getProsetmap() {
		return prosetmap;
	}

	/**
	 * Access method for the sysmap property.
	 * 
	 * @return the current value of the sysmap property
	 */
	public java.util.HashMap getSysmap() {
		return sysmap;
	}

	/**
	 * Access method for the infoSetCode property.
	 * 
	 * @return the current value of the infoSetCode property
	 */
	public java.lang.String getInfoSetCode() {
		return infoSetCode;
	}

	/**
	 * Sets the value of the infoSetCode property.
	 * 
	 * @param aInfoSetCode
	 *            the new value of the infoSetCode property
	 */
	public void setInfoSetCode(java.lang.String aInfoSetCode) {
		infoSetCode = aInfoSetCode;
	}

	/**
	 * Access method for the infoSetName property.
	 * 
	 * @return the current value of the infoSetName property
	 */
	public java.lang.String getInfoSetName() {
		return infoSetName;
	}

	/**
	 * Access method for the dictList property.
	 * 
	 * @return the current value of the dictList property
	 */
	public java.util.List getDictList() {
		return dictList;
	}

	/**
	 * Access method for the proTypeList property.
	 * 
	 * @return the current value of the proTypeList property
	 */
	public java.util.List getProTypeList() {
		return proTypeList;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaSysmanageProperty getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setSmanager(InfoSetManager aManager) {
		Smanager = aManager;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(InfoItemManager aManager) {
		manager = aManager;
	}
	/**
	 * 构建信息项
	 * 
	 * @return java.lang.String
	 * @roseuid 4946537D0096
	 */
	public String create() throws Exception{
		addActionMessage(manager.createdInfoItem(infoItemCode, infoSetCode));
		return "reloads";
	}

	/**
	 * 撤销构建信息项
	 * 
	 * @return java.lang.String
	 * @roseuid 494653940029
	 */
	public String destroy() throws Exception{
		addActionMessage(manager.destroyInfoItem(infoItemCode, infoSetCode));
		return "reloads";
	}

	/**
	 * 初始化信息项输入框
	 * 
	 * @return java.lang.String
	 * @roseuid 494653F9028C
	 */
	@Override
	public String input() throws Exception{
		prepareModel();
		long maxSort=0;
		if (YES.equals(model.getInfoItemIsSystem())) {
			addActionMessage(model.getInfoItemSeconddisplay() + "("
					+ model.getInfoItemField() + ")" + "是系统信息项，不可修改！");
			return "reloads";
		} 
//		else if (YES.equals(model.getInfoItemState())) {
//			addActionMessage(model.getInfoItemSeconddisplay() + "("
//					+ model.getInfoItemField() + ")" + "已构建，请先撤销信息项构建！");
//			return "reloads";
//		}
		if(model!=null&&model.getInfoItemCode()!=null&&!model.getInfoItemCode().equals("")){
			String infoItemId=model.getToaSysmanageStructure().getInfoSetValue()+"$"+model.getInfoItemField();
			int count=infoManagerService.isHasInfoFormUsed(infoItemId);
			if(count>0){
				usedInfoItem="1";
			}else {
				usedInfoItem="0";
			}
		}
		else if (KEY.equals(model.getInfoItemDatatype())) {
			addActionMessage(model.getInfoItemSeconddisplay() + "("
					+ model.getInfoItemField() + ")" + "是主外键，不可修改！");
			return "reloads";
		}
		////添加序号自动生成的功能
		if(model.getInfoItemCode()==null||"".equals(model.getInfoItemCode())){
			//获取最大排序值
			maxSort=manager.maxNo();
			model.setInfoItemOrderby(maxSort);
		}
		infoSetName = manager.getInfoSetName(infoSetCode);
		dictList = dictmanage.getAllDictTypes();
		proTypeList = typemanage.getAllTypes(infoSetCode);
		return INPUT;
	}

	/**
	 * 获取信息项分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 49479C710177
	 */
	@Override
	public String list() throws Exception{
		page = manager.getAllItems(infoSetCode, page, model);
		return SUCCESS;
	}

	/**
	 * 保存信息项
	 * 
	 * @return java.lang.String
	 * @roseuid 49479C7101A5
	 */
	@Override
	public String save() throws Exception{
		if ("".equals(model.getInfoItemCode())){
			model.setInfoItemCode(null);
		}
		if(!(model.getInfoItemState()!=null&&!model.getInfoItemState().equals("")&&model.getInfoItemState().equals("1"))){
			
			model.setInfoItemState(NO);
		}
		String dataType=model.getInfoItemDatatype();
		if("0".equals(dataType)||"1".equals(dataType)||"2".equals(dataType)||"3".equals(dataType)||"4".equals(dataType)||"5".equals(dataType)||"6".equals(dataType))
			model.setSystemIdentify("1");
		else{
			model.setSystemIdentify("");
		}
		if(manager.stutsinfoItemField(model)){
			addActionMessage("保存失败，信息项值不能重复！");
			return "reloads";
		}
		manager.saveInfoItem(model);
		addActionMessage("保存信息项成功");
		return "reloads";
	}

	/**
	 * 批量删除信息项
	 * 
	 * @return java.lang.String
	 * @roseuid 49479C7101E4
	 */
	@Override
	public String delete() throws Exception{

		addActionMessage(manager.delInfoItem(infoItemCode));
		return "reloads";
	}

	public String IsHasDelete() throws Exception{
		boolean boo=false;
		String[] str = infoItemCode.split(",");
		String itemName="";
		for(int i=0;i<str.length;i++){
			
			model = manager.getInfoItem(str[i]);
			String infoItemId=model.getToaSysmanageStructure().getInfoSetValue()+"$"+model.getInfoItemField();
			int count=infoManagerService.isHasInfoFormUsed(infoItemId);
		
			if(count>0){
				boo=true;
				itemName=itemName+","+model.getInfoItemSeconddisplay(); 
			}
		}
		if(boo){
			itemName=itemName.substring(1);
			return renderHtml( itemName);					//信息项已经使用
		}else{
			return renderHtml( "0");					
		}
	}
	
	/**
	 * 初始化信息项对象
	 * 
	 * @roseuid 49479C710213
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (infoItemCode != null) {
			model = manager.getInfoItem(infoItemCode);
			
		} else {
			model = new ToaSysmanageProperty();
			model.setInfoItemFlag(NO);
			model.setInfoItemProset(NO);
			model.setIsQuery(NO);
			model.setIsView(NO);
		}
		ToaSysmanageStructure infoSet = Smanager.getInfoSet(infoSetCode);
		infoSetState=infoSet.getInfoSetState();
		
	}
	/**
	 * 判断排序号是否重复
	 * author  taoji
	 * @return
	 * @date 2014-3-21 下午08:58:33
	 */
	public String check(){
		String temp = manager.check(infoSetCode,infoItemOrderby);
		if(temp.equals("true")){
			return renderText("true");
		}else{
			return renderText("false");
		}
	}
	@Autowired
	public void setDictmanage(DictTypeManager dictmanage) {
		this.dictmanage = dictmanage;
	}

	@Autowired
	public void setTypemanage(InfoTypeManager typemanage) {
		this.typemanage = typemanage;
	}
	@Autowired
	public void setInfoManagerService(IInfoManagerService infoManagerService) {
		this.infoManagerService = infoManagerService;
	}

	public String getUsedInfoItem() {
		return usedInfoItem;
	}

	public void setUsedInfoItem(String usedInfoItem) {
		this.usedInfoItem = usedInfoItem;
	}

	public String getInfoItemOrderby() {
		return infoItemOrderby;
	}

	public void setInfoItemOrderby(String infoItemOrderby) {
		this.infoItemOrderby = infoItemOrderby;
	}
}
