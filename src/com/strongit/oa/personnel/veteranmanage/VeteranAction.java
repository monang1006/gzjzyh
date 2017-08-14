package com.strongit.oa.personnel.veteranmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBaseVeteran;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaVeteranRegard;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.personnel.util.Veteran;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings( { "serial", "unchecked" })
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "veteranmanage/veteran.action", type = ServletActionRedirectResult.class) })
public class VeteranAction extends BaseActionSupport {
	private Page<ToaBaseVeteran> page = new Page<ToaBaseVeteran>(FlexTableTag.MAX_ROWS, true);

	private String personId;

	private ToaBaseVeteran model = new ToaBaseVeteran();
	
	private Page<ToaVeteranRegard> regardPage = new Page<ToaVeteranRegard>(FlexTableTag.MAX_ROWS, true);

	private String regardId;

	private ToaVeteranRegard regard = new ToaVeteranRegard();

	/** 字典项列表 */
	private List<ToaSysmanageDictitem> levelList;

	private List<ToaSysmanageDictitem> healthList;
	
	private List<ToaSysmanageDictitem> saxList;
	
	private List<ToaSysmanageDictitem> nationList;

	private Map veternMap = new HashMap();

	private Map healthMap = new HashMap();
	private Map saxMap = new HashMap();

	/** 字典接口 */
	private IDictService dictService;

	private Veteranmanager veterManager;

	private PersonOrgManager personorgManager;

	private List<ToaBaseOrg> orgList;

	/** 编码规则 */
	private String codeType;

	/** 未删除 */
	private static final String NOTDEL = "0";

	/** 查询字段 */
	private String personName;

	private String personPset;

	private String personSax;

	private String personLevel;

	private String healthState;
	/**查询慰问信息字段*/
	private String verePersons;

	private String vereTopic;

	private Date vereTime;

	
	public VeteranAction() {

	}

	public Veteranmanager getVeterManager() {
		return veterManager;
	}

	@Autowired
	public void setVeterManager(Veteranmanager veterManager) {
		this.veterManager = veterManager;
	}

	public IDictService getDictService() {
		return dictService;
	}

	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	@Override
	public String delete() throws Exception {
		if (personId != null) {
			model = veterManager.getOneVeteran(personId);
			model.setPersonIsdel("1");
			veterManager.saveToaBaseVeteran(model);
		} 
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/veteranmanage/veteran.action" + "';"
				+ "</script>");
	}
	
	public String deleteRegard() throws Exception{
		if (regardId != null) {
			veterManager.deleteRegards(regardId);
		} 
		
		return renderHtml("<script> window.parent.veteranPersonRegards.location='"
					+ getRequest().getContextPath()
					+ "/personnel/veteranmanage/veteran!regardList.action?personId="+personId+"';</script>");

	}

	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		levelList = dictService.getItemsByDictValue("DAIYU");// 获取待遇级别列表
		healthList = dictService.getItemsByDictValue("HEALTH");// 获取健康状况列表
		saxList=dictService.getItemsByDictValue("SEX");//获取性别列表
		/** 将两个字典项装载进MAP */
		if (levelList != null && healthList != null) {
			this.addMap(levelList, healthList,saxList);
		}
		page = veterManager.queryBaseVeteran(page, personName, personPset,
				personSax, personLevel, healthState);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		levelList = dictService.getItemsByDictValue("DAIYU");// 获取待遇级别列表
		healthList = dictService.getItemsByDictValue("HEALTH");// 获取健康状况列表
		saxList=dictService.getItemsByDictValue("SEX");//获取性别列表
		nationList=dictService.getItemsByDictValue("AE");//获取民族列表
		if (personId != null) {
			model = veterManager.getOneVeteran(personId);
		} else {
			model = new ToaBaseVeteran();
		}
	}
	
	protected void prepareRegard() throws Exception {
		if (regardId != null) {
			regard = veterManager.getOneVeteranRegard(regardId);
		} else {
			regard = new ToaVeteranRegard();
		}
		if (personId != null) {
			model = veterManager.getOneVeteran(personId);
		}
	}

	@Override
	public String save(){

		if ("".equals(model.getPersonId())) {
			model.setPersonId(null);
	}
		try{
			model.setPersonIsdel("0");
		veterManager.saveToaBaseVeteran(model);
		}catch(Exception ex){
			ex.printStackTrace();
			}
		return renderHtml("<script> window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/personnel/veteranmanage/veteran.action';window.close(); </script>");

	}
	
	public String saveRegard()throws Exception {

		if ("".equals(regard.getVereId())) {
			regard.setVereId(null);
			
	
	  }
	veterManager.saveToaVeteranRegard(regard);
	personId=regard.getToaBaseVeteran().getPersonId();
    return renderHtml("<script> window.dialogArguments.parent.veteranPersonRegards.location='"
					+ getRequest().getContextPath()
					+ "/personnel/veteranmanage/veteran!regardList.action?personId="+personId+"';window.close(); </script>");

	}
	
	public String regardList() throws Exception {
		if(personId!=null)
		{
		  regardPage = veterManager.queryVeteranRegard(regardPage, personId, verePersons, vereTopic, vereTime);
		}
		return "regardList";
	}


	/**
	 * 机构树
	 * 
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception {

		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		orgList = personorgManager.getOrgsByIsdel(NOTDEL);

		return "tree";
	}

	public String addRegard() throws Exception {
		prepareRegard();
		return "addRegard";
	}

	public String view() throws Exception {
		prepareModel();
		return "view";
	}
	
	public String viewPlay() throws Exception{
		prepareModel();
		return "viewPlay";
	}

	public String rview() throws Exception {
		System.out.println("viewr");
		return "rview";
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<ToaBaseVeteran> getPage() {
		return page;
	}

	public void setPage(Page<ToaBaseVeteran> page) {
		this.page = page;
	}

	public void setModel(ToaBaseVeteran model) {
		this.model = model;
	}

	public List<ToaSysmanageDictitem> getHealthList() {
		return healthList;
	}

	public void setHealthList(List<ToaSysmanageDictitem> healthList) {
		this.healthList = healthList;
	}

	public String getHealthState() {
		return healthState;
	}

	public void setHealthState(String healthState) {
		this.healthState = healthState;
	}

	public List<ToaSysmanageDictitem> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ToaSysmanageDictitem> levelList) {
		this.levelList = levelList;
	}

	public String getPersonLevel() {
		return personLevel;
	}

	public void setPersonLevel(String personLevel) {
		this.personLevel = personLevel;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonPset() {
		return personPset;
	}

	public void setPersonPset(String personPset) {
		this.personPset = personPset;
	}

	public String getPersonSax() {
		return personSax;
	}

	public void setPersonSax(String personSax) {
		this.personSax = personSax;
	}

	public Map getHealthMap() {
		return healthMap;
	}

	public void setHealthMap(Map healthMap) {
		this.healthMap = healthMap;
	}

	public Map getVeternMap() {
		return veternMap;
	}

	public void setVeternMap(Map veternMap) {
		this.veternMap = veternMap;
	}

	/**
	 * 给MAP装载字典项数据
	 * 
	 * @author 蒋国斌
	 * @date 2009-9-21 下午02:41:13
	 * @param leveList
	 * @param healList
	 */
	public void addMap(List leveList, List healList,List saxList) {
		for (Iterator it = leveList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem item = (ToaSysmanageDictitem) it.next();
			veternMap.put(item.getDictItemCode(), item.getDictItemName());
		}
		for (Iterator it = healList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem ite = (ToaSysmanageDictitem) it.next();
			healthMap.put(ite.getDictItemCode(), ite.getDictItemName());
		}
		for (Iterator it = saxList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem ite = (ToaSysmanageDictitem) it.next();
			saxMap.put(ite.getDictItemCode(), ite.getDictItemName());
		}
	}


	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public List<ToaBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<ToaBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public PersonOrgManager getPersonorgManager() {
		return personorgManager;
	}

	@Autowired
	public void setPersonorgManager(PersonOrgManager personorgManager) {
		this.personorgManager = personorgManager;
	}

	public ToaVeteranRegard getRegard() {
		return regard;
	}

	public void setRegard(ToaVeteranRegard regard) {
		this.regard = regard;
	}

	public String getRegardId() {
		return regardId;
	}

	public void setRegardId(String regardId) {
		this.regardId = regardId;
	}

	public Page<ToaVeteranRegard> getRegardPage() {
		return regardPage;
	}

	public String getVerePersons() {
		return verePersons;
	}

	public void setVerePersons(String verePersons) {
		this.verePersons = verePersons;
	}

	public Date getVereTime() {
		return vereTime;
	}

	public void setVereTime(Date vereTime) {
		this.vereTime = vereTime;
	}

	public String getVereTopic() {
		return vereTopic;
	}

	public void setVereTopic(String vereTopic) {
		this.vereTopic = vereTopic;
	}

	public List<ToaSysmanageDictitem> getSaxList() {
		return saxList;
	}

	public void setSaxList(List<ToaSysmanageDictitem> saxList) {
		this.saxList = saxList;
	}

	public List<ToaSysmanageDictitem> getNationList() {
		return nationList;
	}

	public void setNationList(List<ToaSysmanageDictitem> nationList) {
		this.nationList = nationList;
	}

	public Map getSaxMap() {
		return saxMap;
	}

	public void setSaxMap(Map saxMap) {
		this.saxMap = saxMap;
	}

}
