package com.strongit.oa.personnel.structure;


import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaStructure;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.infopub.statistic.ArticleStatistic;
import com.strongit.oa.personnel.structure.statistic.DrawBar;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 机构编制action
 * @author 胡丽丽
 * createDate:2009-10-19
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/personnel/structure/personStructure.action", type = ServletActionRedirectResult.class) })
public class PersonStructureAction extends BaseActionSupport {

	private Page<ToaStructure> page=new Page<ToaStructure>(FlexTableTag.MAX_ROWS,true);
	/** 编制统计PAGE*/
	private Page<StructureStatistic> statpage=new Page<StructureStatistic>(FlexTableTag.MIDDLE_ROWS,true);
	/** 编制manager*/
	private PersonStructureManager manager;
	/** 编制BO*/
	private ToaStructure model=new ToaStructure();
	/** 编号ID*/
	private String structureId;
	/** 机构manager*/
	private PersonOrgManager baseorgManager;
	/** 机构BO*/
	private ToaBaseOrg org=new ToaBaseOrg();
	/** 机构ID*/
	private String orgId;
	/** 编制列表*/
	private List<ToaStructure> structureList;
	/** 返回值*/
	private String audittype;
	private Map<String,String> stateMap=new HashMap<String, String>();//编制状态
	private Map<String,String> strucTypeMap=new HashMap<String, String>();//编制类型
	private Map<String,Integer> sumPersonMap=new HashMap<String, Integer>();//编制人员统计
	private Map<String,String> natureMap=new HashMap<String, String>();//机构性质map
	private String isgoogle;//是否搜索
	/** 编制类型*/
	private String strucType;
	
	 /** 字典接口*/
    private IDictService dictService;
	
    /** 编制类型list*/
    private List<ToaSysmanageDictitem> dictlist;
    
    private List<ToaSysmanageDictitem> orgDictList;
    
    private PersonManager personManager;
	private static final String STATE1="0" ;//编制状态：正常
	private static final String STATE2="1" ;//编制状态：满编
	private static final String STATE3="2" ;//编制状态：超编
	
	private static final String YES="1";
	private static final String NO="0";
	/** 柱形图名称*/
	private String temp1;
	
	
	PersonStructureAction(){
		stateMap.put("0","正常");
		stateMap.put("1","满编");
		stateMap.put("2","超编");
	}
	public List<ToaStructure> getStructureList() {
		return structureList;
	}

	public void setStructureList(List<ToaStructure> structureList) {
		this.structureList = structureList;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getStructureId() {
		return structureId;
	}

	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}

	@Autowired
	public void setManager(PersonStructureManager manager) {
		this.manager = manager;
	}

	/**
	 * 把编制类型放入MAP中
	 * @author 胡丽丽
	 * @date:2009-10-28
	 */
	private void getDictitem(){
		for(ToaSysmanageDictitem dict:dictlist){
			strucTypeMap.put(dict.getDictItemCode(), dict.getDictItemName());
		}
	}
	/**
	 * 删除编制
	 * @author hull
	 * @date:2009-10-28
	 */
	@Override
	public String delete() throws Exception {
		String message="";//消息
		List<ToaBasePerson> list=personManager.getPersonByStruc( structureId);
		if(list==null||list.size()==0){//是否存在人员
		manager.delete(structureId);
		}else{
			message="给编制下存在人员，请先调转人员！";
		}
		StringBuffer str=new StringBuffer();
		if(!"".equals(message)){
		if(orgId!=null&&!"".equals(orgId)){//判断机构是否为空
			
			str.append("<script>alert('"+message+"');window.location='")
			   .append(getRequest().getContextPath())
			   .append("/personnel/structure/personStructure.action?orgId="+orgId+"&audittype=structure';</script>");
			return renderHtml(str.toString());
		}
		str.append("<script>alert('"+message+"');window.location='")
		   .append(getRequest().getContextPath())
		   .append("/personnel/structure/personStructure.action';</script>");
		return renderHtml(str.toString());
		}else{
			if(orgId!=null&&!"".equals(orgId)){
				
				str.append("<script>window.location='")
				   .append(getRequest().getContextPath())
				   .append("/personnel/structure/personStructure.action?orgId="+orgId+"&audittype=structure';</script>");
				return renderHtml(str.toString());
			}
			str.append("<script>window.location='")
			   .append(getRequest().getContextPath())
			   .append("/personnel/structure/personStructure.action';</script>");
			return renderHtml(str.toString());
		}
	}
	
	/**
	 * 获取编制状态
	 * @author hull
	 * @date:2009-10-28
	 * @return
	 * @throws Exception
	 */
	public String structureState()throws Exception{
		ToaStructure structure=manager.getStructureByID(structureId);
		return renderHtml(structure.getStrucStatus());
	}

	/**
	 * 初始化编辑页面
	 * @author hull
	 * @date :2009-10-29
	 */
	@Override
	public String input() throws Exception {
		
		dictlist=dictService.getItemsByDictValue("BIANZHI");//获取编制
		prepareModel();
		if(model.getStrucType()!=null){
		for(ToaSysmanageDictitem dic:dictlist){
			if(model.getStrucType().equals(dic.getDictItemCode())){
				model.setStrucTypeName(dic.getDictItemName());
			}
		}
		}
		if(orgId!=null&&!"".equals(orgId)){//判断机构ID是否为空
		org=baseorgManager.getOrgByID(orgId);
		}else{
			org=new ToaBaseOrg();
		}
		return INPUT;
	}
	/**
	 * 获取机构列表
	 * @author hull
	 * @date:2009-10-29
	 */
	@Override
	public String list() throws Exception {
		dictlist=dictService.getItemsByDictValue("BIANZHI");//编制类型
		ToaSysmanageDictitem dic=new ToaSysmanageDictitem();
		dic.setDictItemName("全部类型");
		dictlist.add(0, dic);
		this.getDictitem();
		if("structure".equals(audittype)){//判断是否是某机构下列表
			this.nativelist();
			org=baseorgManager.getOrgByID(orgId);
			org.setOrgNatureName(natureMap.get(org.getOrgNature()));
			if(isgoogle!=null&&!"".equals(isgoogle)){//判断是否是搜索
				model.setToaBaseOrg(org);
				page=manager.search(page, model);
			}else{
				page=manager.getStructureByOrg(page, orgId);
			}
			List<ToaStructure> strcList=page.getResult();
			if(strcList!=null){//判断是否查询出数据
				for(ToaStructure struc:strcList){
					struc.setStrucTypeName(strucTypeMap.get(struc.getStrucType()));
				}
			}
			return "structure";
		}else{
			if(isgoogle!=null&&!"".equals(isgoogle)){//是否搜索
				model.setToaBaseOrg(org);
				page=manager.search(page,model);
			}else{
				page =manager.getAllStructure(page);
			}
			List<ToaStructure> strcList=page.getResult();
			if(strcList!=null){//是否查询出数据
				for(ToaStructure struc:strcList){
					struc.setStrucTypeName(strucTypeMap.get(struc.getStrucType()));
				}
			}

		}
		return SUCCESS;
	}

	/**
	 * 查询该机构下是否存在这个编制
	 * @author 胡丽丽
	 * @return
	 * @throws Exception
	 */
	public String strucType()throws Exception{
		int strucSum=manager.getStructureByOrgType(orgId, strucType).size();
		if(strucSum>0&&(structureId==null||"".equals(structureId))){
			return renderHtml(YES);
		}else{
			return renderHtml(NO);
		}
		
	}
	/**
	 * 初始化BO
	 * @author hull
	 * @date:2009-10-29
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(structureId==null||"".equals(structureId)){
			model=new ToaStructure();
		}else{
			model=manager.getStructureByID(structureId);
			orgId=model.getToaBaseOrg().getOrgid();
		}
	}

	/**
	 * 保存编制
	 * @author hull
	 * @date:2009-10-29
	 */
	@Override
	public String save() throws Exception {
		dictlist=dictService.getItemsByDictValue("BIANZHI");//编制类型
    	this.getDictitem();
		if("".equals(model.getStrucId())){//编制ID是否等于空字符串
			model.setStrucId(null);
			model.setStrucStatus(STATE1);
		}else if(model.getStrucId()==null){//编制ID是否为空
			model.setStrucStatus(STATE1);
		}else{
			this.updateStruc();
		}
		model.setToaBaseOrg(org);
		model.setStrucTypeName(strucTypeMap.get(model.getStrucType()));
		Date date=new Date();
		model.setStrucEdittime(date);
		
		manager.save(model);
		return renderHtml("<script> window.dialogArguments.document.location.reload();window.close();</script>");
	}

	/**
	 * 修改编制状态
	 * @author hull
	 */
    private void updateStruc(){
    	structureId=model.getStrucId();
    	int strNumber=Integer.parseInt(model.getStrucNumber());//获取编制状态，并转换成整型
    	int count=personManager.getCountByStrucId(structureId);//统计该编制下人员数量
    	if(count==strNumber){//满编
    		model.setStrucStatus(STATE2);
    	}else if(count>strNumber){//超编
    		model.setStrucStatus(STATE3);
    	}else{
    		model.setStrucStatus(STATE1);
    	}
    }
    /**
     * 根据机构统计人员情况
     * @author 胡丽丽
     * @date:2009-10-29
     * @return
     * @throws Exception
     */
    public String statistic()throws Exception {
    	try {
    		if(isgoogle!=null&&"search".equals(isgoogle)&&org!=null&&org.getOrgName()!=null&&!"".equals(org.getOrgName())){//判断机构名称是否为空
    			statpage=manager.getStatisticByOrg(statpage,org.getOrgid());
    		}else{
    			if(orgId!=null&&!"".equals(orgId)){
    			 org=baseorgManager.getOrgByID(orgId);
    			}else{
    				org=new ToaBaseOrg();
    			}
			     statpage=manager.getStatisticByOrg(statpage,org.getOrgSyscode(),"");
    		}
		} catch (RuntimeException e) {
		
			e.printStackTrace();
		}
		brawBar(statpage.getResult(),"编制情况统计柱形图","机构名称","人员数量统计");
    	return "statistic";
    }
    /**
     * 统计机构下编制详细情况
     * @author hull
     * @date 2009-10-14 14:50:00
     * @return
     * @throws Exception
     */
    public String statisticInfo() throws Exception{
    	try {
			this.nativelist();
			org=baseorgManager.getOrgByID(orgId);
			org.setOrgNatureName(natureMap.get(org.getOrgNature()));
			if(isgoogle!=null&&!"".equals(isgoogle)){//是否搜索
				page=manager.getStructureByOrg(page, orgId,model.getStrucType());
			}else{
			page=manager.getStructureByOrg(page, orgId);
			}
			dictlist=dictService.getItemsByDictValue("BIANZHI");//编制类型
			this.getDictitem();
			ToaSysmanageDictitem dic=new ToaSysmanageDictitem();
			dic.setDictItemName("全部类型");
			dictlist.add(0, dic);
			int outsum=0;
			structureList=new ArrayList<ToaStructure>();
			if(page.getResult()!=null){//是否查询出数据
				structureList=page.getResult();
				this.getSumPerson();
				for(ToaStructure struct:structureList){
					struct.setStrucTypeName(strucTypeMap.get(struct.getStrucType()));
					if(sumPersonMap.get(struct.getStrucId())!=null){
					struct.setRealityPerson(sumPersonMap.get(struct.getStrucId()));
					int properSum=Integer.parseInt(struct.getStrucNumber());
					if(sumPersonMap.get(struct.getStrucId())>properSum){
						struct.setOutPerson(sumPersonMap.get(struct.getStrucId())-properSum);
					}
					}
				}
			}
			brawBarInfo(structureList,"编制情况统计柱形图","编制名称","人员数量统计");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return "statisticinfo";
    }
    /**
     * 获取机构下每个编制里人员数量
     * @author hull
     */
    private void getSumPerson(){
    	List<Object> list=personManager.getSumPersonByStructure(orgId);
    	for(Object obj:list){
    		Object[] objs=(Object[])obj;
    		sumPersonMap.put(objs[0].toString(),Integer.parseInt(objs[1].toString()));
    	}
    }
    /**
     * 获取机构性质
     * @author hull
     */
    private void nativelist(){
    	orgDictList=dictService.getItemsByDictValue("JIGOU");//编制类型
    	for(ToaSysmanageDictitem dictitem:orgDictList){
    		natureMap.put(dictitem.getDictItemCode(), dictitem.getDictItemName());
    	}
    }
    
	/**
	 * 画柱形图
	 */
	public void brawBar(List<StructureStatistic> list,String title,String xtitle,String ytitle){
		getResponse().setContentType("image/jpeg");
		
		 DrawBar brawbar = DrawBar.getInstance();//柱形图
	     StructureStatistic stat=null;

		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         for(int i=0;i<list.size();i++){
        	 stat=list.get(i);
        	 for(int j=0;j<2;j++){
        		 if(j==0){//是否统计应有编内人数
        			 dataset.addValue(stat.getProperPerson(), "应有编内人数", stat.getToabaseorg().getOrgName());
        		 }else if(j==1){//是否统计实际人数
        			 dataset.addValue(stat.getRealityPerson(), "实际编内人数", stat.getToabaseorg().getOrgName());
        		 }
        	 }
         }
    	 brawbar.setDataset(dataset);
		 brawbar.init();
	     brawbar.setTitle(title);
	     brawbar.setYTitle(ytitle);
	     brawbar.setXTitle(xtitle);
	     brawbar.setBgColor("white");
	     brawbar.setIsV(true);
	     brawbar.setWidth(650);
	     brawbar.setMargin(0.1);
	     brawbar.setHeight(370);
	     brawbar.setXFontSize(10);
	     brawbar.setYFontSize(10);
	   
	     folder();//清理文件夹
	     // 4.保存图片
			temp1=String.valueOf((Math.random()*10));
		  brawbar.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp1+".jpg");
  
	}
    
	/**
	 * 画柱形图
	 */
	public void brawBarInfo(List<ToaStructure> list,String title,String xtitle,String ytitle){
		getResponse().setContentType("image/jpeg");
		
		 DrawBar brawbar = DrawBar.getInstance();//柱形图
	     ToaStructure stat=null;
		 double[][] data = new double[3][list.size()];
		
		 for(int j=0;j<3;j++){
		 for(int i=0;i<list.size();i++){
			 stat=list.get(i);
			 if(j==0){//是否统计应有编内人数
				 data[j][i]=Integer.parseInt(stat.getStrucNumber());
			 }else if(j==1){//是否统计实际人数
				 data[j][i]=stat.getRealityPerson();
			 }else{//是否统计超编人数
				 data[j][i]=stat.getOutPerson();
			 }
		 }
		 }
		 
		 String[] rowKeys = {"应有编内人数","实际人数","超编人数"};
		 String[] columnKeys = new String[list.size()];
		 for(int i=0;i<list.size();i++){
			 columnKeys[i]=list.get(i).getStrucTypeName();
		 }
		 CategoryDataset dateset= DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data); 
		 brawbar.setDataset(dateset);
		 brawbar.init();
	     brawbar.setTitle(title);
	     brawbar.setYTitle(ytitle);
	     brawbar.setXTitle(xtitle);
	     brawbar.setBgColor("white");
	     brawbar.setIsV(true);
	     brawbar.setWidth(650);
	     brawbar.setMargin(0.1);
	     brawbar.setHeight(370);
	     brawbar.setXFontSize(10);
	     brawbar.setYFontSize(10);
	   
	     folder();//清理文件夹
	     // 4.保存图片
			temp1=String.valueOf((Math.random()*10));
		  brawbar.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp1+".jpg");
  
	}
    
    /***
	 * 判断文件夹里文件的数量
	 */
	private void folder(){
		
		String path = this.getRequest().getRealPath("/")+"/img/";
		int fileCount = 0;
		File d = new File(path);
		File list[] = d.listFiles();
		for(int i = 0; i < list.length; i++){
		    if(list[i].isFile()){
		        fileCount++;
		    }
		}
		if(fileCount>=10){
			delAllFile(this.getRequest().getRealPath("/")+"/img/");
		}

	}
	
    /***
     * 清楚文件夹里的文件
     * @param path
     * @return
     */
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	            
	             flag = true;
	          }
	       }
	       return flag;
	     }
	public ToaStructure getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public ToaBaseOrg getOrg() {
		return org;
	}

	public void setOrg(ToaBaseOrg org) {
		this.org = org;
	}

	@Autowired
	public void setBaseorgManager(PersonOrgManager baseorgManager) {
		this.baseorgManager = baseorgManager;
	}

	public Page<ToaStructure> getPage() {
		return page;
	}

	public void setPage(Page<ToaStructure> page) {
		this.page = page;
	}

	public String getAudittype() {
		return audittype;
	}

	public void setAudittype(String audittype) {
		this.audittype = audittype;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}
	public IDictService getDictService() {
		return dictService;
	}
	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}
	public List<ToaSysmanageDictitem> getDictlist() {
		return dictlist;
	}
	public void setDictlist(List<ToaSysmanageDictitem> dictlist) {
		this.dictlist = dictlist;
	}
	public Map<String, String> getStrucTypeMap() {
		return strucTypeMap;
	}
	public void setStrucTypeMap(Map<String, String> strucTypeMap) {
		this.strucTypeMap = strucTypeMap;
	}
	public String getStrucType() {
		return strucType;
	}
	public void setStrucType(String strucType) {
		this.strucType = strucType;
	}
	@Autowired
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	public String getIsgoogle() {
		return isgoogle;
	}
	public void setIsgoogle(String isgoogle) {
		this.isgoogle = isgoogle;
	}
	public Page<StructureStatistic> getStatpage() {
		return statpage;
	}
	public void setStatpage(Page<StructureStatistic> statpage) {
		this.statpage = statpage;
	}
	public Map<String, Integer> getSumPersonMap() {
		return sumPersonMap;
	}
	public void setSumPersonMap(Map<String, Integer> sumPersonMap) {
		this.sumPersonMap = sumPersonMap;
	}
	public Map<String, String> getNatureMap() {
		return natureMap;
	}
	public void setNatureMap(Map<String, String> natureMap) {
		this.natureMap = natureMap;
	}
	public List<ToaSysmanageDictitem> getOrgDictList() {
		return orgDictList;
	}
	public void setOrgDictList(List<ToaSysmanageDictitem> orgDictList) {
		this.orgDictList = orgDictList;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

}
