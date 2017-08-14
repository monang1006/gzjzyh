/*
 * Copyright : Jiang Xi Strong Co.. Ltd.
 * All right reserved.
 * JDK 1.5.0_14;Struts：2.1.2;Spring 2.5.6;Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息表管理通用接口ACTION
 */
package com.strongit.oa.infotable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class InfoTableAction extends BaseActionSupport {
	private Page<List> page = new Page<List>(FlexTableTag.MAX_ROWS, true);

	private List datalist;

	private String tableDesc;

	private String parentablename;

	private String pkey;

	private String struct;

	private String nowdate;

	private String tableName;

	private String keyid;

	private String functable;

	private String funcPro;

	private String funcid;

	private String otherPro;

	private String querycondition;

	private String fpro;

	private String fid;

	private String infoItems;

	private List dataRowTitle;

	private IInfoTableService manager;

	private DictItemManager dictmanager;

	@Autowired
	public void setDictmanager(DictItemManager dictmanager) {
		this.dictmanager = dictmanager;
	}

	/**
	 * @roseuid 494A31D10157
	 */
	public InfoTableAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page<List> getPage() {
		return page;
	}

	/**
	 * Access method for the datalist property.
	 * 
	 * @return the current value of the datalist property
	 */
	public java.util.List getDatalist() {
		return datalist;
	}

	/**
	 * Access method for the tableDesc property.
	 * 
	 * @return the current value of the tableDesc property
	 */
	public java.lang.String getTableDesc() {
		return tableDesc;
	}

	/**
	 * Access method for the parentablename property.
	 * 
	 * @return the current value of the parentablename property
	 */
	public java.lang.String getParentablename() {
		return parentablename;
	}

	/**
	 * Access method for the pkey property.
	 * 
	 * @return the current value of the pkey property
	 */
	public java.lang.String getPkey() {
		return pkey;
	}

	/**
	 * Sets the value of the pkey property.
	 * 
	 * @param aPkey
	 *            the new value of the pkey property
	 */
	public void setPkey(java.lang.String aPkey) {
		pkey = aPkey;
	}

	/**
	 * Access method for the struct property.
	 * 
	 * @return the current value of the struct property
	 */
	public java.lang.String getStruct() {
		return struct;
	}

	/**
	 * Sets the value of the struct property.
	 * 
	 * @param aStruct
	 *            the new value of the struct property
	 */
	public void setStruct(java.lang.String aStruct) {
		struct = aStruct;
	}

	/**
	 * Access method for the nowdate property.
	 * 
	 * @return the current value of the nowdate property
	 */
	public java.lang.String getNowdate() {
		return nowdate;
	}

	/**
	 * Sets the value of the nowdate property.
	 * 
	 * @param aNowdate
	 *            the new value of the nowdate property
	 */
	public void setNowdate(java.lang.String aNowdate) {
		nowdate = aNowdate;
	}

	/**
	 * Access method for the tableName property.
	 * 
	 * @return the current value of the tableName property
	 */
	public java.lang.String getTableName() {
		return tableName;
	}

	/**
	 * Sets the value of the tableName property.
	 * 
	 * @param aTableName
	 *            the new value of the tableName property
	 */
	public void setTableName(java.lang.String aTableName) {
		tableName = aTableName;
	}

	/**
	 * Access method for the keyid property.
	 * 
	 * @return the current value of the keyid property
	 */
	public java.lang.String getKeyid() {
		return keyid;
	}

	/**
	 * Sets the value of the keyid property.
	 * 
	 * @param aKeyid
	 *            the new value of the keyid property
	 */
	public void setKeyid(java.lang.String aKeyid) {
		keyid = aKeyid;
	}

	/**
	 * Access method for the funcPro property.
	 * 
	 * @return the current value of the funcPro property
	 */
	public java.lang.String getFuncPro() {
		return funcPro;
	}

	/**
	 * Sets the value of the funcPro property.
	 * 
	 * @param aFuncPro
	 *            the new value of the funcPro property
	 */
	public void setFuncPro(java.lang.String aFuncPro) {
		funcPro = aFuncPro;
	}

	/**
	 * Access method for the funcid property.
	 * 
	 * @return the current value of the funcid property
	 */
	public java.lang.String getFuncid() {
		return funcid;
	}

	/**
	 * Sets the value of the funcid property.
	 * 
	 * @param aFuncid
	 *            the new value of the funcid property
	 */
	public void setFuncid(java.lang.String aFuncid) {
		funcid = aFuncid;
	}

	/**
	 * Access method for the otherPro property.
	 * 
	 * @return the current value of the otherPro property
	 */
	public java.lang.String getOtherPro() {
		return otherPro;
	}

	/**
	 * Sets the value of the otherPro property.
	 * 
	 * @param aOtherPro
	 *            the new value of the otherPro property
	 */
	public void setOtherPro(java.lang.String aOtherPro) {
		otherPro = aOtherPro;
	}

	/**
	 * Access method for the querycondition property.
	 * 
	 * @return the current value of the querycondition property
	 */
	public java.lang.String getQuerycondition() {
		return querycondition;
	}

	/**
	 * Sets the value of the querycondition property.
	 * 
	 * @param aQuerycondition
	 *            the new value of the querycondition property
	 */
	public void setQuerycondition(java.lang.String aQuerycondition) {
		querycondition = aQuerycondition;
	}

	/**
	 * Access method for the fpro property.
	 * 
	 * @return the current value of the fpro property
	 */
	public java.lang.String getFpro() {
		return fpro;
	}

	/**
	 * Sets the value of the fpro property.
	 * 
	 * @param aFpro
	 *            the new value of the fpro property
	 */
	public void setFpro(java.lang.String aFpro) {
		fpro = aFpro;
	}

	/**
	 * Access method for the fid property.
	 * 
	 * @return the current value of the fid property
	 */
	public java.lang.String getFid() {
		return fid;
	}

	/**
	 * Sets the value of the fid property.
	 * 
	 * @param aFid
	 *            the new value of the fid property
	 */
	public void setFid(java.lang.String aFid) {
		fid = aFid;
	}

	/**
	 * Access method for the infoItems property.
	 * 
	 * @return the current value of the infoItems property
	 */
	public java.lang.String getInfoItems() {
		return infoItems;
	}

	/**
	 * Sets the value of the infoItems property.
	 * 
	 * @param aInfoItems
	 *            the new value of the infoItems property
	 */
	public void setInfoItems(java.lang.String aInfoItems) {
		infoItems = aInfoItems;
	}

	/**
	 * Access method for the dataRowTitle property.
	 * 
	 * @return the current value of the dataRowTitle property
	 */
	public java.util.List getDataRowTitle() {
		return dataRowTitle;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IInfoTableService aManager) {
		manager = aManager;
	}

	/**
	 * 通过字典名和字典类编号查找具体字典项对象
	 * 
	 * @roseuid 494A2DC40157
	 */
	public void findictitem() throws Exception{
		String inputname = getRequest().getParameter("inputname");
		String dataid = getRequest().getParameter("dataid");
		dictmanager.getDictItem(inputname, dataid);
	}

	/**
	 * @roseuid 494A2E1D029F
	 */
	public void findictothervalue() {

	}

	/**
	 * author:zhangli description:初始化获取信息表对应信息集编号、主键属性、表名描述等信息 modifyer:
	 * description:
	 * 
	 * @return
	 */
	public String content() throws Exception{
		ToaSysmanageStructure infoset = manager.getInfoSetByValue(tableName);
		struct = infoset.getInfoSetCode();
		pkey = infoset.getInfoSetPkey();
		tableDesc = infoset.getInfoSetName();
		return "content";
	}

	/**
	 * author:zhangli description:通过信息集获取树节点列表 modifyer: description:
	 * 
	 * @return
	 */
	public String tree() throws Exception{
		datalist = manager.getTreeByStruct(struct, otherPro, fpro,
				querycondition, functable, pkey);
		ToaSysmanageStructure infoset = manager.getInfoSet(struct);
		tableDesc = infoset.getInfoSetName();
		return "tree";
	}

	/**
	 * 获取信息分页列表
	 * 
	 * @return java.lang.String
	 * @roseuid 494A31D101A5
	 */
	@Override
	public String list() throws Exception{
		ToaSysmanageStructure infoset = new ToaSysmanageStructure();
		if (struct == null)		//信息集对象为空
			infoset = manager.getInfoSetByValue(tableName);
		else
			infoset = manager.getInfoSet(struct);
		struct = infoset.getInfoSetCode();	//获取信息集编码
		if(infoItems!=null&&!"".equals(infoItems)){//选择列不为空
			 dataRowTitle=manager.getTableTitle(struct,infoItems);	
		}else{
			dataRowTitle = manager.getTableTitle(struct);
		}

		pkey = infoset.getInfoSetPkey();		//主键
		tableDesc = infoset.getInfoSetName();	//信息集描述
		String parentid = infoset.getInfoSetParentid();		//父节点ID
		if (parentid != null && !parentid.equals("") 	//如果存在父信息集
				&& !parentid.equals("null") &&!parentid.equals("0")) {
			ToaSysmanageStructure parentinfoset = manager.getInfoSet(infoset
					.getInfoSetParentid());	//获取父信息集对象
			parentablename = parentinfoset.getInfoSetName();//获取父信息集名称
			fpro = parentinfoset.getInfoSetPkey();	//获取父信息集的主键
		}

		StringBuffer sql = new StringBuffer(" 1=1 ");
		if (fpro != null && !fpro.equals("") && !fpro.equals("null"))
			sql.append(" and ").append(fpro).append("='").append(fid).append(
					"'");
		if (funcPro != null && !funcPro.equals("null") && !funcPro.equals(""))
			sql.append(" and ").append(funcPro).append("='").append(funcid)
					.append("' ");
		if (querycondition != null && !querycondition.equals("null")
				&& !querycondition.equals(""))
			sql.append(" and").append(querycondition);

		page = manager.getTablePage(dataRowTitle, struct, sql.toString(), page);
		return SUCCESS;
	}
	
	/**
	 * 获取信息项列表
	 * 
	 * @return java.lang.String
	 * @roseuid 49479C710177
	 */
	public String rowlist() throws Exception{
		dataRowTitle = manager.getSelectItems(struct);
		String forward = getRequest().getParameter("forward");
		return forward;
	}

	/**
	 * 保存信息
	 * 
	 * @return java.lang.String
	 * @roseuid 494A31D101D4
	 */
	@Override
	public String save() throws Exception{
		keyid = getRequest().getParameter(pkey);
		if (keyid != null && !keyid.equals("") && !keyid.equals("null"))
			addActionMessage(manager.editTable(getRequest(), struct));
		else
			addActionMessage(manager.addTable(getRequest(), struct));
		return "temp";
	}

	/**
	 * 删除信息
	 * 
	 * @return java.lang.String
	 * @roseuid 494A31D10213
	 */
	@Override
	public String delete() throws Exception{
		addActionMessage(manager.delTable(struct, keyid));
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
				.append(getActionMessages().toString()).append("');</script>");
		return renderHtml(returnhtml.toString());
	}

	/**
	 * 初始化信息
	 * 
	 * @roseuid 494A31D10251
	 */
	@Override
	protected void prepareModel() throws Exception{
		if (keyid != null && !keyid.equals("") && !keyid.equals("null"))
			datalist = manager.initTableEdit(struct, keyid);
		else
			datalist = manager.initTableAdd(struct);
	}

	/**
	 * 初始化信息输入框
	 */
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		prepareModel();
		return INPUT;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFunctable() {
		return functable;
	}

	public void setFunctable(String functable) {
		this.functable = functable;
	}
}
