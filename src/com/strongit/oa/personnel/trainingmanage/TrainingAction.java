package com.strongit.oa.personnel.trainingmanage;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;



import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaTrainColumn;
import com.strongit.oa.bo.ToaTrainInfo;
import com.strongit.oa.bo.ToaTrainRecord;
import com.strongit.oa.bo.ToaTraininfoAttach;
import com.strongit.oa.dict.IDictService;

import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.util.Train;
import com.strongit.oa.personnel.util.Training;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
@SuppressWarnings({ "serial", "unchecked" })
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "trainingmanage/training.action", type = ServletActionRedirectResult.class) })
public class TrainingAction extends BaseActionSupport {
	
	private Page<ToaTrainInfo> page = new Page<ToaTrainInfo>(FlexTableTag.MAX_ROWS, true);
	private ToaTrainInfo model =new ToaTrainInfo();
	private String trainingId;
	
	private String persons;
	private String personsName;
	
	private String sName;
	private String orgid;
	private String sorgName;
	
	
	private String clumnId; // 栏目ID
	private String pclumnId; // 父栏目ID
	private List columnList; // 栏目list集
	private ToaTrainColumn column = new ToaTrainColumn();
	
	private String[] hasChild; // 子类别
	
	private String clumnViewName; // 栏目名称
	private String parentClumnName; // 父栏目名称
	/** 字典接口 */
	private IDictService dictService;
	
	private Trainingmanager trainManager;
	
	/** 人员manager*/
	private PersonManager personManager;
	
	/** 字典项列表 */
	private List<ToaSysmanageDictitem> trainTypeList;//培训类别LIST

	private Map traintypeMap = new HashMap();//培训类别MAP
	/** 查询字段*/
	private String trainTopic;
	private String trainCommpany;
	private String trainType;
	private Date sDate;
	private Date eDate;
	
/** 与附件有关*/
	private String attachId;
	private ToaTraininfoAttach attach=new ToaTraininfoAttach();//附件对象
	private List<ToaTraininfoAttach> attachList;//附件LIST
//	附件
	private String attachFiles;
//	删除记录ids
	private String delAttachIds ;
	
	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;
	

	
public String tree() throws Exception {
		
		columnList = trainManager.getParentColumn();
		if (columnList.isEmpty()) {
			column = new ToaTrainColumn();
			column.setClumnDir("Column"); // 栏目目录
			column.setClumnName("默认栏目"); // 栏目名称
		
			column.setClumnParent("0"); // 父栏目
			column.setClumnCreatetime(new Date()); // 创建日期
			
			trainManager.saveColumn(column);
			columnList = trainManager.getParentColumn();
		}
		ToaTrainColumn columns = null;
		List<ToaTrainColumn> subColumnList;
		hasChild = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			columns = (ToaTrainColumn) columnList.get(i);
			subColumnList = trainManager.getSubColumn(columns.getClumnId());
			if (subColumnList.size() > 0) {
				hasChild[i] = "1";
			} else {
				hasChild[i] = "0";
			}
		}
		return "tree";
	}


public String cloumnTree() throws Exception {
	
	columnList = trainManager.getParentColumn();
	ToaTrainColumn columns = null;
	List<ToaTrainColumn> subColumnList;
	hasChild = new String[columnList.size()];
	for (int i = 0; i < columnList.size(); i++) {
		columns = (ToaTrainColumn) columnList.get(i);
		subColumnList = trainManager.getSubColumn(columns.getClumnId());
		if (subColumnList.size() > 0) {
			hasChild[i] = "1";
		} else {
			hasChild[i] = "0";
		}
	}
	return "cloumnTree";
}




public String syntree() throws Exception {
	StringBuffer str = new StringBuffer();
	List<ToaTrainColumn> folds = trainManager.getSubColumn(clumnId);
	for (int i = 0; i < folds.size(); i++) {
		ToaTrainColumn fold = folds.get(i);
		List<ToaTrainColumn> subColumnList = trainManager
				.getSubColumn(fold.getClumnId());
		if (!subColumnList.isEmpty()) {
			str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + ">");
			str.append("<span>" + fold.getClumnName() + "</span>");
			str.append("<ul class=ajax>");
			str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + i + ">{url:"
					+ getRequest().getContextPath()
					+ "/personnel/trainingmanage/training!syntree.action?clumnId="
					+ fold.getClumnId() + "}</li>");
			str.append("</ul>");
			str.append("</li>");
		} else {
			str.append("<li  name="+fold.getClumnName()+"  id=" + fold.getClumnId() + ">");
			str.append("<span>" + fold.getClumnName());
			str.append("</span>");
			str.append("</li>");
		}
	}
	renderHtml(str.toString());
	return null;
}


public String column() throws Exception {

	if (!"".equals(clumnId) && clumnId != null) {
		clumnViewName = trainManager.getClumnName(clumnId);
		column =trainManager.getColumn(clumnId);
		// 查找流程类型对应的流程
		if (!"0".equals(column.getClumnParent())) {
			parentClumnName = trainManager.getClumnName(column.getClumnParent());
		}
	}
	return "column";
}

public String newSubColumn() throws Exception {
	if (!"".equals(clumnId) && clumnId != null) {
		clumnViewName = trainManager.getClumnName(clumnId);
	}
	return "subcolumn";
}

public String deleteCloumn() throws Exception {
	if (!"".equals(clumnId) && clumnId != null) {
		List<ToaTrainColumn> folds = trainManager.getSubColumn(clumnId);
		boolean iscolumnArticl = trainManager.columnArticl(clumnId);
		if (folds.size() > 0 || iscolumnArticl) { // 栏目下存在子栏目或栏目下有培训
			this.renderText("delfalse");
		} else {
			trainManager.delColumn(clumnId,new OALogInfo("删除栏目"));
			// addActionMessage("删除信息成功");
		}

	}
	return null;
}

public String saveCloumn() throws Exception {
	if ("".equals(column.getClumnId()) || column.getClumnId() == null) {
		column.setClumnId(null);
		column.setClumnCreatetime(new Date());	
	} 
	if ("".equals(column.getClumnParent()) || column.getClumnParent() == null) {
		column.setClumnParent("0"); // 栏目父节点为0
	}
	trainManager.saveColumn(column,new OALogInfo("保存栏目"));

	return "temp";
}

	@Override
	public String delete() throws Exception {
		if (trainingId != null) {
			trainManager.deleteTrainInfo(trainingId);
		} 
		
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/trainingmanage/training.action"+
				"';"+
				"</script>");
	}
	public String pdelete() throws Exception {
		HttpServletRequest request= ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		List<Train> l=(List<Train>) session.getAttribute("list");
		if(l!=null){
			for(int i=0;i<l.size();i++){
				if(l.get(i).getTrainId().equals(getTrainingId())){
					l.remove(i);
				}
			}
			session.setAttribute("list", l);
		}
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/personnel/training!person.action"+
				"';"+
				"</script>");
	}
	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}
	/**
	 * 给MAP装载字典项数据
	 * 
	 * @author 蒋国斌
	 * @date 2009-9-21 下午02:41:13
	 * @param leveList
	 * @param healList
	 */
	public void addMap(List trainTypeList) {
		for (Iterator it = trainTypeList.iterator(); it.hasNext();) {
			ToaSysmanageDictitem item = (ToaSysmanageDictitem) it.next();
			traintypeMap.put(item.getDictItemCode(), item.getDictItemName());
		}
	}

	@Override
	public String list() throws Exception {
		trainTypeList = dictService.getItemsByDictValue("PEIXUN");// 获取培训分类列表
		/** 将两个字典项装载进MAP */
		if (trainTypeList != null) {
			this.addMap(trainTypeList);
		}
		page =trainManager.queryTrainInfoPage(page, trainTopic, trainCommpany, trainType, sDate, eDate,clumnId);

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		trainTypeList = dictService.getItemsByDictValue("PEIXUN");// 获取培训分类列表
	
		if (trainingId != null) {
			model = trainManager.getOneTrainInfo(trainingId);
			clumnId=model.getToaTrainColumn().getClumnId();
			attachList=trainManager.getTrainAttaches(trainingId);
			if(attachList!=null){
				Iterator it=attachList.iterator();
				attachFiles="";
				while (it.hasNext()) {
					ToaTraininfoAttach att = (ToaTraininfoAttach) it.next();
					
					attachFiles+="<div id="+att.getAttachId()+" style=\"display: \"><a href=\"javascript:delAttach('"+att.getAttachId()+"')\">[删除]<a>" +
							"<a href=\"javascript:download('"+att.getAttachId()+"')\">"+att.getAttachName()+"</a>&nbsp;</div>";
				}
			}
			List ls=trainManager.getTrainRecordsbyT(trainingId);
			StringBuffer xx=new StringBuffer();
			StringBuffer ps=new StringBuffer();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				ToaTrainRecord rd=(ToaTrainRecord)it.next();
				ToaBasePerson pt=rd.getToaBasePerson();
				ps.append(pt.getPersonid());
				ps.append(",");
				xx.append(rd.getPersonName());
				xx.append(",");
			}
			persons=ps.toString();
			personsName=xx.toString();
			
		} else {
			model = new ToaTrainInfo();
			clumnId=this.clumnId;
		}
	}
	
	@Override
	public String save() throws Exception {
		if( delAttachIds!=null&&!"".equals(delAttachIds)) {
			trainManager.deleteAttaches(delAttachIds);
		}
		if ("".equals(model.getTrainId())) {
			model.setTrainId(null);
	  }
		if(clumnId!=null && !clumnId.equals("")){
			model.setToaTrainColumn(trainManager.getColumn(clumnId));
		}
		trainManager.saveToaTrainInfo(model);
		this.trainingId=model.getTrainId();
		ToaTrainInfo train=trainManager.getOneTrainInfo(trainingId);
		if(file!=null){
		for(int i=0;i<file.length;i++){
		
			ToaTraininfoAttach attach=new ToaTraininfoAttach();
			attach.setTraininfo(train);
			
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file[i]);
				byte[] buf = new byte[(int)file[i].length()];
				fis.read(buf);
				attach.setAttachCon(buf);
				attach.setAttachName(fileName[i]);
				//String ext = fileName[i].substring(fileName[i].lastIndexOf(".")+1);
				
			} catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error("上传失败！"+e);
					}
					throw e;
				}finally{
					try {
						fis.close();
					} catch (IOException e) {
						if(logger.isErrorEnabled()){
							logger.error("文件关闭失败！"+e);
						}
						throw e;
					}
				}
				trainManager.saveTrainattach(attach);
		}
			}
		
		if(!persons.equals("")&&!personsName.equals("")){
			trainManager.deleteTrainRecords(trainingId);
			String ids[]=persons.split(",");
			for(int i=0;i<ids.length;i++){
				ToaTrainRecord recor=new ToaTrainRecord();
				ToaBasePerson per=personManager.getPersonByID(ids[i]);
				recor.setRecordId(null);
				recor.setPersonName(per.getPersonName());
				recor.setToaBasePerson(per);
				recor.setToaTrainInfo(train);
				trainManager.saveToaTrainRecord(recor);
			}
		}
		
		return renderHtml("<script> window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/personnel/trainingmanage/training.action';window.close(); </script>");

	}
	
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaTraininfoAttach file =trainManager.getTrainAttache(attachId);
		response.reset();
		response.setContentType("application/x-download");         //windows
		OutputStream output = null;
		try{
			response.addHeader("Content-Disposition", "attachment;filename=" +
			         new String(file.getAttachName().getBytes("gb2312"),"iso8859-1"));
		    output = response.getOutputStream();
		    output.write(file.getAttachCon());
		    output.flush();
		} catch(Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage(),e);
			}
		} finally {		 	    
		    if(output != null){
		      try {
				output.close();
			} catch (IOException e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}
		      output = null;
		    }
		}
		return null;
	}

	
	protected void prepareViewModel() throws Exception {
		trainTypeList = dictService.getItemsByDictValue("PEIXUN");// 获取培训分类列表
	
		if (trainingId != null) {
			model = trainManager.getOneTrainInfo(trainingId);
			attachList=trainManager.getTrainAttaches(trainingId);
			if(attachList!=null){
				Iterator it=attachList.iterator();
				attachFiles="";
				while (it.hasNext()) {
					ToaTraininfoAttach att = (ToaTraininfoAttach) it.next();
					
					attachFiles+="<div id="+att.getAttachId()+" style=\"display: \">" +
							"<a href=\"javascript:download('"+att.getAttachId()+"')\">"+att.getAttachName()+"</a>&nbsp;</div>";
				}
			}
			List ls=trainManager.getTrainRecordsbyT(trainingId);
			StringBuffer xx=new StringBuffer();
			StringBuffer ps=new StringBuffer();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				ToaTrainRecord rd=(ToaTrainRecord)it.next();
				ToaBasePerson pt=rd.getToaBasePerson();
				ps.append(pt.getPersonid());
				ps.append(",");
				xx.append(rd.getPersonName());
				xx.append(",");
			}
			persons=ps.toString();
			personsName=xx.toString();
		}
	}
	public String view() throws Exception{
		prepareViewModel();
		return "view";
	}
	public String pedit() throws Exception{
		prepareModel();
		return "pedit";
	}
	public String pview() throws Exception{
		prepareModel();
		System.out.println("pview...");
		return "pview";
	}
	public String person() throws Exception{
		trainTypeList = dictService.getItemsByDictValue("PEIXUN");// 获取培训分类列表
		/** 将两个字典项装载进MAP */
		if (trainTypeList != null) {
			this.addMap(trainTypeList);
		}
		page =trainManager.queryTrainInfoPageByperson(page,sName);
		return "person";
	}
	
	
	public String personOrg() throws Exception{
		trainTypeList = dictService.getItemsByDictValue("PEIXUN");// 获取培训分类列表
		/** 将两个字典项装载进MAP */
		if (trainTypeList != null) {
			this.addMap(trainTypeList);
		}
		page =trainManager.queryTrainInfoPageByOrg(page, orgid);
		return "personOrg";
	}
	public String add() throws Exception{
		HttpServletRequest request= ServletActionContext.getRequest();
		List list=new ArrayList();
		list.add("测试一");
		list.add("人力资源");
		list.add("研发部");
		
		request.setAttribute("FMList", list);
		List userList=new ArrayList();
		userList.add("研发部");
		userList.add("研发部");
		request.setAttribute("userFMList", userList);
		return "add";
	}
	public String draw() throws Exception{
	/**
		HttpServletRequest request= ServletActionContext.getRequest();
		request.setAttribute("mylist", getList());
//		list=articlesManager.getColumnArticleCount(beginTime,endTime);
//		getRequest().setAttribute("beginTime", beginTime);
//		getRequest().setAttribute("endTime",endTime);
		for(Training as:list){//循环信息列表过滤过长的标题
			if(as.getName().length()>=20){
				as.setName(as.getName().substring(0,10)+"...");
			}
		}
		//清理文件夹
		   folder();
		// 1.生成绘图类对象
		
		     brawBar("培训统计柱形图","人员","培训时间");
		
			 brawPie();
			 **/
		return "draw";
	}
	/***
	 * 饼形图
	 */
	public void brawPie(){
		/**
		// 清除缓存
		getResponse().setHeader("cache-control", "no-cache");
		getResponse().setHeader("cache-control", "no-store");
		getResponse().setDateHeader("expires", 0);
		getResponse().setHeader("pragma", "no-cache");
		
		getResponse().setContentType("image/jpeg");
		 DrawPie brawpie=new DrawPie();//饼形图

			for(Training as:list){
				if(as.getName()==null)
				{
					as.setName("");
				}
				brawpie.addData(as.getName(), Double.valueOf(as.getValue()));
			}
			brawpie.init();
			brawpie.setBgcolor(Color.white);
			brawpie.setTitle("培训统计饼图");
			brawpie.setWidth(400);
			brawpie.setHeight(250);
			brawpie.setLabelFontSize(10);
			brawpie.setFactor(0.2);
			//4.保存图片
			String url=getRequest().getContextPath();
			temp=String.valueOf((Math.random()*10));
			brawpie.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp+".jpg");
			**/

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
	/**
	 * 画柱形图
	 */
	public void brawBar(String title,String xtitle,String ytitle){
		/**
		getResponse().setContentType("image/jpeg");
		
		 DrawBar brawbar = DrawBar.getInstance();//柱形图
	      
	     for(Training as:list){
	    	 if(as.getName()==null)
				{
					as.setName("");
				}
		     brawbar.addData(as.getName(),Integer.valueOf(as.getValue()),"培训记录");
	      }
	     brawbar.init();
	     brawbar.setTitle(title);
	     brawbar.setYTitle(ytitle);
	     brawbar.setXTitle(xtitle);
	     brawbar.setBgColor("white");
	     brawbar.setIsV(true);
	     brawbar.setWidth(400);
	     brawbar.setMargin(0.1);
	     brawbar.setHeight(300);
	     brawbar.setXFontSize(10);
	     brawbar.setYFontSize(10);
	     // 4.保存图片
	   
	     String url=getRequest().getContextPath();
			temp1=String.valueOf((Math.random()*10));
			while(true){
				if(temp1.equals(temp)){
					temp1=String.valueOf((Math.random()*10));
				}
				else{
					break;
				}
			}
	     brawbar.saveAbs(this.getRequest().getRealPath("/")+"/img/"+temp1+".jpg");
//	     try {
//		//	brawbar.writeWebChartAsJPEG(getResponse().getOutputStream());
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//		}
 * **/

	}

	

	public void setList(List<Training> list) {
		if(list!=null){
			list.clear();
		}
		for(int i=0;i<10;i++){
			list.add(new Training("张"+i,"1"+i));
		}
	}

	public Page<ToaTrainInfo> getPage() {
		return page;
	}

	public void setPage(Page<ToaTrainInfo> page) {
		this.page = page;
	}

	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}

	public void setModel(ToaTrainInfo model) {
		this.model = model;
	}

	public Object getModel() {
		return model;
	}
	public List<ToaSysmanageDictitem> getTrainTypeList() {
		return trainTypeList;
	}
	public void setTrainTypeList(List<ToaSysmanageDictitem> trainTypeList) {
		this.trainTypeList = trainTypeList;
	}
	public Map getTraintypeMap() {
		return traintypeMap;
	}
	public void setTraintypeMap(Map traintypeMap) {
		this.traintypeMap = traintypeMap;
	}
	public IDictService getDictService() {
		return dictService;
	}
	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}
	public Trainingmanager getTrainManager() {
		return trainManager;
	}
	@Autowired
	public void setTrainManager(Trainingmanager trainManager) {
		this.trainManager = trainManager;
	}
	public Date getEDate() {
		return eDate;
	}
	public void setEDate(Date date) {
		eDate = date;
	}
	public Date getSDate() {
		return sDate;
	}
	public void setSDate(Date date) {
		sDate = date;
	}
	public String getTrainCommpany() {
		return trainCommpany;
	}
	public void setTrainCommpany(String trainCommpany) {
		this.trainCommpany = trainCommpany;
	}
	public String getTrainTopic() {
		return trainTopic;
	}
	public void setTrainTopic(String trainTopic) {
		this.trainTopic = trainTopic;
	}
	public String getTrainType() {
		return trainType;
	}
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}
	public ToaTraininfoAttach getAttach() {
		return attach;
	}
	public void setAttach(ToaTraininfoAttach attach) {
		this.attach = attach;
	}
	public String getAttachFiles() {
		return attachFiles;
	}
	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	public List<ToaTraininfoAttach> getAttachList() {
		return attachList;
	}
	public void setAttachList(List<ToaTraininfoAttach> attachList) {
		this.attachList = attachList;
	}
	public String getDelAttachIds() {
		return delAttachIds;
	}
	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}
	public String getPersons() {
		return persons;
	}
	public void setPersons(String persons) {
		this.persons = persons;
	}
	public String getPersonsName() {
		return personsName;
	}
	public void setPersonsName(String personsName) {
		this.personsName = personsName;
	}
	public PersonManager getPersonManager() {
		return personManager;
	}
	@Autowired
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
	public String getSName() {
		return sName;
	}
	public void setSName(String name) {
		sName = name;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getSorgName() {
		return sorgName;
	}
	public void setSorgName(String sorgName) {
		this.sorgName = sorgName;
	}
	public List getColumnList() {
		return columnList;
	}
	public void setColumnList(List columnList) {
		this.columnList = columnList;
	}
	public ToaTrainColumn getColumn() {
		return column;
	}
	public void setColumn(ToaTrainColumn column) {
		this.column = column;
	}
	public String[] getHasChild() {
		return hasChild;
	}
	public void setHasChild(String[] hasChild) {
		this.hasChild = hasChild;
	}


	public String getClumnId() {
		return clumnId;
	}


	public void setClumnId(String clumnId) {
		this.clumnId = clumnId;
	}


	public String getPclumnId() {
		return pclumnId;
	}


	public void setPclumnId(String pclumnId) {
		this.pclumnId = pclumnId;
	}


	public String getClumnViewName() {
		return clumnViewName;
	}


	public void setClumnViewName(String clumnViewName) {
		this.clumnViewName = clumnViewName;
	}


	public String getParentClumnName() {
		return parentClumnName;
	}


	public void setParentClumnName(String parentClumnName) {
		this.parentClumnName = parentClumnName;
	}
	
}
