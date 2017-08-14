package com.strongit.oa.viewmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaForamula;
import com.strongit.oa.bo.ToaPagemodel;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.viewmodel.util.DateUtil;
import com.strongit.oa.viewmodel.util.NoHidenFileFilter;
import com.strongmvc.webapp.action.BaseActionSupport;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * <p>Title: ViewModelPageAction.java</p>
 * <p>Description: 页面模板管理Action</p>
 * <p>Strongit Ltd. (C) copyright 2010</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-04
 * @version  1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/viewModelPage.action") })
public class ViewModelPageAction extends BaseActionSupport{

	private static final long serialVersionUID = 2203201888075751149L;
	
	private static final Logger log  = Logger.getLogger(ViewModelPageAction.class); 
	
	private ToaPagemodel model = new ToaPagemodel();
	
	private String id;														//待操作页面模型ID
	
	private String modelId;													//模型ID
	
	private String parentId;												//父级页面模板ID,如果为0证明没有顶级页面模板
	
	private String parentName;												//父级页面模板名称
	
	private String modelName;												//模型名称
	
	private String nowNode;													//动态树形的节点
	
	private String dbobj;													//原有附件对象名称
	
	private String valName;													//父级页面变量名称
	
	private File[] file;													//文件对象
	
	@Autowired private ViewModelManager viewModelManager;					//模型Manager类
	
	@Autowired private ViewModelPageManager viewModelPageManager;
	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(viewModelPageManager.chargeLeaf(id, modelId)){					//如果是叶子节点
			ToaPagemodel obj = viewModelPageManager.getObjById(id);
			String oapath = PathUtil.getRootPath();
			String filePath = oapath + "WEB-INF/ftls/"+obj.getPagemodelName()+".ftl";
			com.strongit.oa.viewmodel.util.FileOper.delFile(filePath);
			viewModelPageManager.delObj(obj);
			return renderText("true");
		}else{																//如果不是叶子节点，禁止删除
			return renderText("has");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if("0".equals(parentId)){
			
		}else{
			ToaPagemodel parentObj = viewModelPageManager.getObjById(parentId);
			if(parentObj!=null)
				parentName = parentObj.getPagemodelDes();
		}
		model.setPagemodelPagetype("0");
		return "input";
	}
	
	public String edit() throws Exception{
		model = viewModelPageManager.getObjById(id);
		parentId = model.getPagemodelParentid();
		parentName = model.getToaForamula().getForamulaDec();
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return SUCCESS;
	}
	
	/**
	 * @author:于宏洲
	 * @des   :页面模板树形展现方式展现方法
	 * @return
	 * @throws Exception
	 * @date  :2010-06-05
	 */
	public String tree() throws Exception{
		if(modelId!=null){
			ToaForamula obj = viewModelManager.getObjById(modelId);
			if(obj!=null){
				modelName = obj.getForamulaDec();
			}
			List<ToaPagemodel> list = viewModelPageManager.getModelRootPage(modelId);
			if(list!=null&&list.size()!=0){
				String[] charge = new String[list.size()];
				for(int i=0;i<list.size();i++){
					if(viewModelPageManager.chargeLeaf(list.get(i).getPagemodelId(),modelId)){
						charge[i]="0";
					}else{
						charge[i]="1";
					}
				}
				getRequest().setAttribute("list", list);
				getRequest().setAttribute("charge", charge);
			}
			
		}
		return "tree";
	}
	
	/**
	 * @author:于宏洲
	 * @des   :获取服务器端路径的
	 * @return
	 * @date  :2010-06-06
	 */
	
	public String getServerPathTree()throws Exception{
		String filePath = PathUtil.getRootPath();
		String rootPath = filePath + "WEB-INF"+File.separator+"jsp";
		File file = new File(rootPath);
		if(file.isDirectory()){
			List<File> list = java.util.Arrays.asList(file.listFiles(new NoHidenFileFilter()));
			if(list!=null&&list.size()!=0){
				String[] charge = new String[list.size()];
				for(int i=0;i<list.size();i++){
					System.out.print(list.get(i).getPath());
					if(isLeafOrNot(list.get(i).getPath())){
						charge[i]="0";
					}else{
						charge[i]="1";
					}
				}
				this.getRequest().setAttribute("list", list);
				this.getRequest().setAttribute("charge", charge);
				this.getRequest().setAttribute("realPath", rootPath);
			}
		}
		return "stree";
	}
	
	/**
	 * @author:于宏洲
	 * @des   :加载儿子节点方法
	 * @return
	 * @date  :2010-06-08
	 */
	public String getRootChild() throws Exception{
		
		String realPath =  PathUtil.getRootPath()+"WEB-INF"+File.separator+"jsp";
		try {
			StringBuffer str=new StringBuffer("");
			File file = new File(nowNode);
			if(file.exists()){
				List<File> list = java.util.Arrays.asList(file.listFiles(new NoHidenFileFilter()));
				for(int i=0;i<list.size();i++){
					String nowPath = list.get(i).getPath();
					String tempId = nowPath.substring(realPath.length()+1, nowPath.length());
					if(isLeafOrNot(list.get(i).getPath())){
						str.append("<li id="+tempId+"  leafChange='folder-share-leaf'><span>"+list.get(i).getName()+"</span><label style='display:none;'>person</label></li>\n");
					}else{
						str.append("<li id=" + tempId + ">")
						   .append("<span>"+list.get(i).getName()+"</span>")
						   .append("<ul class=ajax>")
						   .append("<li id="+tempId+1 +">{url:"+getRequest().getContextPath()+"/viewmodel/viewModelPage!getRootChild.action?nowNode="+list.get(i).getPath()+"}</li>")
						   .append("</ul>")
						   .append("</li>");
					}
				}
				renderHtml(str.toString());
			}
		} catch (Exception e) {
			log.error(e);
			renderText(e.toString());
		}
		return null;
		
	}
	
	public String getPageRootChild() throws Exception{
		StringBuffer str = new StringBuffer();
		List<ToaPagemodel> list = viewModelPageManager.getPageListByFather(parentId, modelId);
		for(int i=0;i<list.size();i++){
			if(viewModelPageManager.chargeLeaf(list.get(i).getPagemodelId(), modelId)){
				str.append("<li id="+list.get(i).getPagemodelId()+"><span>"+list.get(i).getPagemodelDes()+"</span><label style='display:none;'>person</label></li>\n");
			}else{
				str.append("<li id=" + list.get(i).getPagemodelId() + ">")
				   .append("<span>"+list.get(i).getPagemodelDes()+"</span>")
				   .append("<ul class=ajax>")
				   .append("<li id=ajax"+list.get(i).getPagemodelId() +">{url:"+getRequest().getContextPath()+"/viewmodel/viewModelPage!getPageRootChild.action?parentId="+list.get(i).getPagemodelId()+"&modelId="+list.get(i).getToaForamula().getForamulaId()+"}</li>")
				   .append("</ul>")
				   .append("</li>");
			}
		}
		return renderHtml(str.toString());
	}
	
	
	/**
	 * @author:于宏洲
	 * @des   :判断当前的文件是否为树形结构的根节点
	 * @param path
	 * @return
	 * @date  :2010-06-06
	 */
	private boolean isLeafOrNot(String path){
		File file = new File(path);
		if(file.exists()){
			List<File> list = java.util.Arrays.asList(file.listFiles(new NoHidenFileFilter()));
			if(list == null || list.size()==0){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public String  chargeRoot() throws Exception{
		if(modelId!=null){
			if(viewModelPageManager.chargeLeaf("0", modelId)){
				return renderText("true");
			}else{
				return renderText("false");
			}
		}else{
			return renderText("false");
		}
		
	}
	
	/**
	 * @author:于宏洲
	 * @des   :上传模板文件方法
	 * @return
	 * @date  :2010-06-09
	 */
	private String uplaodFile(){
		String filePath = PathUtil.getRootPath();
		String name = DateUtil.getUtilTime();
		String uploadsPath = filePath + "WEB-INF/ftls/" + name +".ftl";
		System.out.println(uploadsPath);
		try{
			File newFile = new File(uploadsPath);
			newFile.createNewFile();
			System.out.println(file[0].isFile());
			copyFile(file[0],newFile);
			return name;
		}catch(Exception e){
			log.error(e);
			return null;
		}
	}
	
	/**
	 * @author:于宏洲
	 * @des   :文件拷贝方法
	 * @param f1
	 * @param f2
	 * @throws Exception
	 * @date  :2010-06-09
	 */
	private void copyFile(File f1,File f2) throws Exception{
		  int length=2097152;
		  FileInputStream in;
		  try{
			  in=new FileInputStream(f1);
		  
		  FileOutputStream out=new FileOutputStream(f2);
		  byte[] buffer=new byte[length];
		  while (true) {
			int ins = in.read(buffer);
			if (ins == -1) {
				in.close();
				out.flush();
				out.close();
				break;
			} else
				out.write(buffer, 0, ins);
		}
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	}	
	
	public String chargeVal() throws Exception{
		
		if(viewModelPageManager.chargeValSame(modelId, parentId, valName.trim(),id)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public String createJSP() throws Exception{
		List<ToaPagemodel> list = viewModelPageManager.getPageModelList(modelId);
		ToaForamula foramula=viewModelManager.getObjById(modelId);
		int sucNumber = 0;
		int failNumber = 0;
		for(int i=0;i<list.size();i++){
			Map<String,String> valMap = new HashMap<String,String>();
			valMap.put("jsppath", foramula.getCreateTime());
			ToaPagemodel temp = list.get(i);
			List<ToaPagemodel> sonList = findSon(temp.getPagemodelId(),list);
			if(sonList.size()==0){								//当前页面为根节点，既不需要进行相关的页面变量配置，可以直接生成页面
				if(createJSPPage(temp,valMap)){
					sucNumber = sucNumber+1;
				}else{
					failNumber = failNumber+1;
				}
			}else{
				for(ToaPagemodel obj:sonList){
					String val="<%=path%>/";
					if(obj.getPagemodelVal()!=null&&obj.getPagemodelVal().endsWith("toPage")){				//不需要通过Action直接跳转的页面
						String path = obj.getPagemodelSavepath();
						val = val + obj.getPagemodelVal()+ "=" + path;
						if(obj.getPagemodelActionName()==null||"null".equals(obj.getPagemodelActionName())||"".equals(obj.getPagemodelActionName().trim())){
							val = val + "/" + obj.getPagemodelName()+".jsp";
						}else{
							val = val + "/" + obj.getPagemodelActionName() + "-" +obj.getPagemodelName()+".jsp";
						}
						
					}else{
						String urlVal = obj.getPagemodelVal();
						if(urlVal.indexOf("&")==-1){														//当URL中本身就不存在变量的时候
							val = val +urlVal + "?pageModelId="+obj.getPagemodelId();
						}else{
							val = val + urlVal + "&pageModelId="+obj.getPagemodelId();						//当URL中本身存在变量的时候
						}
					}
					valMap.put(obj.getPagemodelValname(),val);
				}
				if(createJSPPage(temp,valMap)){
					sucNumber = sucNumber+1;
				}else{
					failNumber = failNumber+1;
				}
			}
		}
		if(failNumber>0){			//pengxq于20110117添加
			foramula.setIsCreatePage("0");
			viewModelManager.saveForamula(foramula);
		}else{
			foramula.setIsCreatePage("1");
			viewModelManager.saveForamula(foramula);
			viewModelManager.createRootPath(foramula, "1","3", "2");
		}
		JSONObject jsobj = new JSONObject();
		jsobj.put("sucn", sucNumber);
		jsobj.put("faln", failNumber);
		return renderText(jsobj.toString());
	}
	
	/**
	 * @author:于宏洲
	 * @des   :在对应的List内存数据中寻找对应节点的子节点
	 * @param parentId
	 * @param list
	 * @return
	 * @date  :2010-06-21
	 */
	private List<ToaPagemodel> findSon(String parentId,List<ToaPagemodel> list){
		List<ToaPagemodel> sonList = new ArrayList<ToaPagemodel>();
		for(int i=0;i<list.size();i++){
			ToaPagemodel son = list.get(i);
			if(parentId.equals(son.getPagemodelParentid())){
				sonList.add(son);
			}
		}
		return sonList;
	}
	
	/**
	 * @author:于宏洲
	 * @des   :根据上传的模板生成相应的JSP页面
	 * @param temp
	 * @param map
	 * @return
	 * @date  :2010-06-21
	 */
	private boolean createJSPPage(ToaPagemodel temp,Map<String,String> map){
		
		String path=PathUtil.getRootPath()+"WEB-INF/ftls";
		
		String abPath = temp.getPagemodelSavepath();
		
		String jspPath = PathUtil.getRootPath()+"WEB-INF"+File.separator+"jsp"+File.separator+abPath;
		
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("utf-8");
		try {
			cfg.setDirectoryForTemplateLoading(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("freemarker模板路径错误："+temp.getPagemodelSavepath());
			return false;
		}
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		try{
			Template template = cfg.getTemplate(temp.getPagemodelName()+".ftl");
			template.setEncoding("utf-8");
			Writer out = null;
			if("".equals(temp.getPagemodelActionName())||temp.getPagemodelActionName()==null||"null".equals(temp.getPagemodelActionName())){//前置页面名称为空则直接生成页面
				out = new OutputStreamWriter(new FileOutputStream(jspPath+"/"+temp.getPagemodelName()+".jsp"),"utf-8");   
			}else{										//前置页面名称 不为空则生成页面要进行前置页面+ "-" +页面名称
				out = new OutputStreamWriter(new FileOutputStream(jspPath+"/"+temp.getPagemodelActionName()+"-"+temp.getPagemodelName()+".jsp"),"utf-8");   
			}
			try{
				template.process(map, out);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(e);
			}
			out.flush();
			out.close();
		}catch(Exception e){
			log.error("freemarker生成页面失败，应该生成页面的名称为:"+jspPath+"/"+temp.getPagemodelName()+".jsp");
			return false;
		}
		return true;
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		model = new ToaPagemodel();
		ToaForamula parentObj = viewModelManager.getObjById(modelId);
		model.setToaForamula(parentObj);
		if("".equals(model.getPagemodelId())){
			model.setPagemodelId(null);
		}
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		ToaForamula parentObj = viewModelManager.getObjById(modelId);
		String path = model.getPagemodelSavepath();
		if(path.indexOf("\\")!=-1){														//如果路径为Linux不识别的路径，进行修改成linux可以读取的路径
			path = path.replaceAll("\\\\", "/");
			model.setPagemodelSavepath(path);
		}
		if("".equals(model.getPagemodelId())){
			model.setPagemodelId(null);
		}
		if(file==null&&dbobj!=null&&!"".equals(dbobj)&&!"null".equals(dbobj)){ 			//修改的时候没有修改附件信息
			model.setPagemodelName(dbobj);
			if(viewModelPageManager.saveObj(model)){
				return this.renderHtml("<script>alert('保存成功');window.parent.location.reload();</script>");
			}else{
				return this.renderHtml("<script>alert('信息修改失败');window.parent.location.reload();</script>");
			}
		}else{
			if(file[0].length()!=0){
				if(viewModelPageManager.saveObj(model)){
					if("null".equals(dbobj)){					//判断当前为修改将原有的附件删除
						String delFilePath = PathUtil.getRootPath();
						delFilePath = delFilePath + "WEB-INF/ftls/" + model.getPagemodelName() +".ftl";
						com.strongit.oa.viewmodel.util.FileOper.delFile(delFilePath);
					}
					String name=uplaodFile();
					if(name!=null){
						model.setPagemodelName(name);
						viewModelPageManager.saveObj(model);
						return this.renderHtml("<script>alert('保存成功');window.parent.location.reload();</script>");
					}else{
						return this.renderHtml("<script>alert('文件上传失败');window.parent.location.reload();</script>");
					}
					
				}else{
					return this.renderHtml("<script>alert('模板信息、文件上传均失败，请重新操作');window.parent.location.reload();</script>");
				}
			}else{
				return this.renderHtml("<script>alert('上传文件为0K请重新操作');window.parent.location.reload();</script>");
			}
		}
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public String getNowNode() {
		return nowNode;
	}

	public void setNowNode(String nowNode) {
		this.nowNode = nowNode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setModel(ToaPagemodel model) {
		this.model = model;
	}

	public ToaPagemodel getModel() {
		return model;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDbobj() {
		return dbobj;
	}

	public void setDbobj(String dbobj) {
		this.dbobj = dbobj;
	}

	public void setValName(String valName) {
		this.valName = valName;
	}

}
