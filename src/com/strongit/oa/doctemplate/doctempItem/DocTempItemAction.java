/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板action类
 */
package com.strongit.oa.doctemplate.doctempItem;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaDoctemplate;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.constants.Constants;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.doctemplate.eform.TemplateEFormManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docTempItem.action", type = ServletActionRedirectResult.class) })
public class DocTempItemAction extends BaseActionSupport {
	private static final long serialVersionUID = 1L;
	private Page<ToaDoctemplate> page= new Page<ToaDoctemplate>(FlexTableTag.MAX_ROWS,true);
	List<ToaDoctemplate> list=new ArrayList<ToaDoctemplate>();
	private String doctemplateId; 		//模板ID
	private String docgroupId; 			//模板类别ID
	private String doctempTypeName; 	//模板类别名称
	private String docgroupType;
	private File wordDoc; 				//文档
	private ToaDoctemplate model = new ToaDoctemplate();
	private ToaDoctemplateGroup groupModel=new ToaDoctemplateGroup();
	private DocTempItemManager manager;
	private DocTempTypeManager typemanager;
	private File[] file;				//上传文档模版
	private String fileName;
	private String path;
	private String type;				//模板类型
	private final static String POSITON="/oa/image/doctemplate/";
	private String templateImagename="";
	
	@Autowired IEFormService eform;	//注入电子表单服务接口
	@Autowired TemplateEFormManager templateEFormManager;//模板-表单服务类
	
	public void setFileFileName(String fileName) {
		this.fileName = fileName;
	}
	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	/**
	 * 得到启动表单树形结构数据.
	 * @author:邓志城
	 * @date:2010-11-18 下午04:08:22
	 * @return
	 * 	List<TempPo>
	 */
	public String eformTree() {
		try {
			List<EForm> eformList = eform.getFormTemplateList(Constants.SF);
			List formList = templateEFormManager.findEForm(doctemplateId);
			StringBuilder strForms = new StringBuilder(",");
			if(formList != null && !formList.isEmpty()) {
				for(Object formObj : formList) {
					strForms.append(String.valueOf(formObj)).append(","); 
				}
			}
			List<TempPo> typeList = new LinkedList<TempPo>();
			for(EForm eform : eformList) {
				TempPo bean = new TempPo();
				bean.setId(String.valueOf(eform.getId()));
				bean.setName(eform.getTitle());
				bean.setParentId("0");
				bean.setType(Constants.SF);
				typeList.add(bean);
			}
			getRequest().setAttribute("typeList", typeList);
			getRequest().setAttribute("forms", strForms);//已挂接的表单
		} catch (Exception e) {
			logger.error("读取模板数据异常。", e);
		}
		return "eformtree";
	}


	/**
	 * 选择打印模板
	 */
	public String printformDocList() {
		try {
			list = templateEFormManager.findTemplatesbyId(doctemplateId);
			List docList = new ArrayList();
			if ((this.list != null) && (this.list.size() > 0)) {
				for (int i = 0; i < this.list.size(); ++i) {
					ToaDoctemplate doctemplate = (ToaDoctemplate) this.list
							.get(i);
					if ((doctemplate.getLogo() != null)
							&& (!(doctemplate.getLogo().equals("")))) {
						String imageName = doctemplate.getDoctemplateId()
								.toString() + "." + doctemplate.getLogo();
						String result = IsHasDoctempItemImage(imageName);
						if (result.equals("true"))
							doctemplate.setIsHasImage("1");
						else
							doctemplate.setIsHasImage("0");
					} else {
						doctemplate.setIsHasImage("0");
					}
					docList.add(doctemplate);
				}
				this.list = docList;
			}
			this.page.setResult(this.list);
			this.doctempTypeName = this.typemanager
					.doctempTypeName(this.docgroupId);

			HttpServletRequest request = getRequest();
			this.path = request.getContextPath() + "/oa/image/doctemplate/";
		} catch (Exception e) {
			this.logger.error("读取模板数据异常。", e);
		}
		return "printformDocList";
	}
	
	/**
	 * 保存模板与表单关联关系
	 * @author:邓志城
	 * @date:2010-11-18 下午04:41:02
	 * @return
	 */
	public String doSelectEForm() {
		String ret = "0";
		try{
			String formId = getRequest().getParameter("formId");
			templateEFormManager.update(doctemplateId, formId);
		}catch(Exception ex) {
			logger.error("保存表单和模板关系时出错。", ex);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	
	/**
	 * author:lanlc
	 * description:删除公文模板
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String delete() throws Exception {
		try{
			model=manager.getDoctemplate(doctemplateId);	//查找到对象
			if(model!=null){	//对象不为空
				if(model.getLogo()!=null&&!"".equals(model.getLogo())){		//该模板有图标则删除服务器上的图标
					HttpServletRequest request=this.getRequest();
					String POSITON="oa"+File.separator +"image"+File.separator +"doctemplate"+File.separator ;
					path=request.getSession().getServletContext().getRealPath("/")+POSITON+doctemplateId+"."+model.getLogo();
					File exsitFile=new File(path);
					exsitFile.delete();
				}
				manager.delDoctemplate(model);
			}
		}catch(ServiceException e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return "reloads";
	}
	
	/**
	 * author:lanlc
	 * description:添加或编辑公文模板
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String input() throws Exception {
		doctempTypeName = typemanager.doctempTypeName(docgroupId);
		ToaDoctemplateGroup group=typemanager.getTemplateType(docgroupId);
		type=group.getDocgroupType();
		if(type!=null&&type.equals("1")){
			return "txtinput";
		}else{
			
			return "input";
		}
	}
	
	/**
	 * author:lanlc
	 * description:编辑时打开文档，文档流直接写入HttpServletResponse请求
	 * modifyer:
	 * @return
	 * @throws Exception
	 */
	public String opendoc() throws Exception{
		HttpServletResponse response = this.getResponse();
		if(doctemplateId!=null){
			manager.setContentToHttpResponse(response, doctemplateId);
		}
		return null;
	}
	
	public String opentxt() throws Exception{
		ToaDoctemplate template =null;
		if(doctemplateId!=null){
			template=manager.getDoctemplate(doctemplateId);
		}
		if(template.getDoctemplateTxtContent()!=null){
			String content = HtmlUtils.htmlUnescape(template.getDoctemplateTxtContent());
			this.renderText(content);
		}
		else{
			this.renderText("");
		}
		return null;
	}
	
	/**
	 * author:lanlc
	 * description:符合条件的模板类别下的模板项列表
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String list() throws Exception {
		page = manager.getItemByTempType(docgroupId, page, model);
		docgroupType = typemanager.getTemplateType(docgroupId).getDocgroupType();
		list=page.getResult();
		List<ToaDoctemplate> docList=new ArrayList<ToaDoctemplate>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				ToaDoctemplate doctemplate=list.get(i);
				if(doctemplate.getLogo()!=null&&!doctemplate.getLogo().equals("")){
					String imageName=doctemplate.getDoctemplateId().toString()+"."+doctemplate.getLogo();
					String result=IsHasDoctempItemImage(imageName);
					if(result.equals("true")){
						doctemplate.setIsHasImage("1");
					}else {
						doctemplate.setIsHasImage("0");
					}
				}else {
					doctemplate.setIsHasImage("0");
				}
				docList.add(doctemplate);
			}
			list=docList;
		}
		page.setResult(list);
		doctempTypeName = typemanager.doctempTypeName(docgroupId);
		//if(path==null||"".equals(path)){
			HttpServletRequest request=this.getRequest();		//图片所在磁盘路径
			path=request.getContextPath()+POSITON;
		//}
		return SUCCESS;
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 3, 2010 6:42:37 PM
	 */
	public String getDocTempItemList()throws Exception {
		if(docgroupId!=null&&!"".equals(docgroupId)){
			list=manager.getTypeByItem(docgroupId);
		}else if(type!=null&&!"".equals(type)){
			list=manager.getAllTxtTemplate(type);
		}else{
			list=manager.getAllTemplate();
		}
		List<ToaDoctemplate> docList=new ArrayList<ToaDoctemplate>();
		if(list.size()>0){											// * 判断模板类型所显示的图标 是否存在	
			for(int i=0;i<list.size();i++){
				ToaDoctemplate doctemplate=list.get(i);
				if(doctemplate.getLogo()!=null&&!doctemplate.getLogo().equals("")){
					
					String imageName=doctemplate.getDoctemplateId().toString()+"."+doctemplate.getLogo();
					String result=IsHasDoctempItemImage(imageName);
					if(result.equals("true")){
						doctemplate.setIsHasImage("1");
					}else {
						doctemplate.setIsHasImage("0");
					}
				}else {
					doctemplate.setIsHasImage("0");
				}
				docList.add(doctemplate);
			}
			list=docList;
		}
		HttpServletRequest request=this.getRequest();		//图片所在磁盘路径
		path=request.getContextPath()+POSITON;
		return "list";
	}
	/**
	 * 判断模板类型所显示的图标 是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-7-29 上午11:44:41 
	 * @return
	 * @throws Exception
	 */
	public String IsHasDoctempItemImage(String imageName) throws Exception{
		templateImagename=imageName;
		HttpServletRequest request=this.getRequest();
		String POSITON="oa"+File.separator +"image"+File.separator +"doctemplate"+File.separator ;
		path=request.getSession().getServletContext().getRealPath("/");
		if(!path.endsWith(File.separator))
			path = path + File.separator;
		path = path + POSITON;
		System.out.println(path);
		String result="false";
		if(path!=null&&!path.equals("")){
			
			File file=new File(path);
			File [] fileArrFiles=file.listFiles();
			if(fileArrFiles!=null&&fileArrFiles.length>0){				
				for(int i = 0; i < fileArrFiles.length; i++){
					String image= fileArrFiles[i].getName();
					if(templateImagename!=null&&!templateImagename.equals("")&&!image.equals("")&&templateImagename.equals(image)){
						result="true";
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * author:lanlc
	 * description:获取公文模板实体
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(doctemplateId!=null&&!doctemplateId.equals("")){
			model = manager.getDoctemplate(doctemplateId);
//			if(model.getDoctemplateTxtContent()!=null&&!model.getDoctemplateTxtContent().equals("")){
//				model.setDoctemplateTxtContent(HtmlUtils.htmlUnescape(model.getDoctemplateTxtContent()));
//			}
			docgroupId=model.getToaDoctemplateGroup().getDocgroupId();
			doctempTypeName=typemanager.getTemplateType(docgroupId).getDocgroupName();
//			doctempTypeName=model.getToaDoctemplateGroup().getDocgroupName();
			if(model.getLogo()!=null&&!"".equals(model.getLogo())){	//获取图片所在磁盘路径
				HttpServletRequest request=this.getRequest();
				path=request.getContextPath()+POSITON+doctemplateId+"."+model.getLogo();
			}
		}else{
			model = new ToaDoctemplate();
		}
	}
	
	/**
	 * author:lanlc
	 * description:公文模板管理初始化提示界面
	 * modifyer:
	 * @return
	 */
	public String init()throws Exception{
		return "init";
	}
	
	/** 
	 * 对公文模板类型进行编辑时，判断该模板类型是否添加了模板项
	 * @author zhengzb
	 * @desc 
	 * 2010-6-13 下午05:34:24 
	 * @return
	 * @throws Exception
	 */
	public String IsAddItem() throws Exception{
		List list=null;
		list=manager.getTypeByItem(docgroupId);//获取模板类型下的模板项表
		List typeList=typemanager.getTypeByParentId(docgroupId);//获取模板类型下的子节点模板
		if(list.size()>0||typeList.size()>0){			
			groupModel=typemanager.getTemplateType(docgroupId);
			return renderHtml(groupModel.getDocgroupName());				
		}
		else {
			return renderHtml("1");
		}
	}
	
	/**
	 * 在添加模板项的时候，根据模块类型显示不同的操作界面
	 * @author zhengzb
	 * @desc 
	 * 2010-6-17 上午09:32:34 
	 * @return
	 * @throws Exception
	 */
	public String docTempType() throws Exception{
		groupModel=typemanager.getTemplateType(docgroupId);
		if(groupModel.getDocgroupType()==null){
			return renderHtml("0");
		}else{
			System.out.println(groupModel.getDocgroupType());
			return renderHtml(groupModel.getDocgroupType());
		}
	}
	
	
	/**
	 * author:lanlc
	 * description:保存公文模板
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String save() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String newdate = sdf.format(new Date());
		String fileType="";	//文件类型
		String oldType=model.getLogo();
		StringBuffer message = new StringBuffer("");
		groupModel=typemanager.getTemplateType(model.getToaDoctemplateGroup().getDocgroupId());
		type=groupModel.getDocgroupType();
		if(file!=null&&file[0]!=null&&file[0].length()>GlobalBaseData.IMANGE_SIZE){
			message.append("{")
			.append("fail: '").append("图片大小超出最大范围。'")
			.append("}");
			if(type!=null&&type.equals("1")){//编辑文本类型    郑志斌修改 2010-06-12 17：35
				addActionMessage("error上传的图片大小超出最大范围。");
				docgroupId=model.getToaDoctemplateGroup().getDocgroupId();
//				doctemplateId=model.getDoctemplateId();
//				prepareModel();//重新初始化界面
				return "txtinput";
			}else{
				
				this.renderText(message.toString());						//word类型
				return null;
			}
		}
		if("".equals(model.getDoctemplateId())){
			model.setDoctemplateId(null);
		}
		model.setDoctemplateCreateTime(sdf.parse(newdate));
		if(fileName!=null&&!"".equals(fileName)){		//获取图片类型
			fileType=fileName.substring(fileName.lastIndexOf(".")+1);
			if(IsHasImage(fileType)){
				model.setLogo(fileType);				
			}
			else{
				message.append("{")
				.append("fail: '").append("上传的文件中包含非图片文件，请选择图片文件。'")
				.append("}");
				if(type!=null&&type.equals("1")){//编辑文本类型    郑志斌修改 2010-06-12 17：35
					addActionMessage("error上传的文件中包含非图片文件，请选择图片文件。");
					docgroupId=model.getToaDoctemplateGroup().getDocgroupId();
//					doctemplateId=model.getDoctemplateId();
//					prepareModel();//重新初始化界面
					return "txtinput";
				}else{
					
					this.renderText(message.toString());						//word类型
					return null;
				}
			}
		}
		try{
			if(type==null||!type.equals("1")){//模块为word类型时，				
				FileInputStream fs = new FileInputStream(wordDoc);
				byte[] fileBuffer = new byte[(int)wordDoc.length()];
				fs.read(fileBuffer);
				model.setDoctemplateContent(fileBuffer);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}	
		model.setDoctemplateTxtContent(HtmlUtils.htmlEscape(model.getDoctemplateTxtContent()));
		manager.saveDoctemplate(model);				//保存信息
		if(fileName!=null&&!"".equals(fileName)){	//上传图片
			HttpServletRequest request=this.getRequest();
			String webPath=request.getSession().getServletContext().getRealPath("/");	//工程所在磁盘路径
			if(!webPath.endsWith(File.separator))
				webPath = webPath + File.separator;
			String position="oa"+File.separator +"image"+File.separator +"doctemplate"+File.separator ;
			System.out.println(webPath);
			System.out.println(position);
			if(oldType!=null&&!"".equals(oldType)){		//编辑时
				path=webPath+position+model.getDoctemplateId()+"."+oldType;
				File exsitFile=new File(path);		
				exsitFile.delete();					//删除之前上传的图片
			}
			path=webPath+position+model.getDoctemplateId()+"."+fileType;
			File newFile=new File(path);
			newFile.createNewFile();
			file[0].renameTo(newFile);
			IOUtils.copy(new FileInputStream(file[0]), new FileOutputStream(newFile));
		}
		message.append("{")
		.append("id: '").append(model.getDoctemplateId()).append("',")
		.append("title: '").append(model.getDoctemplateTitle()).append("',")
		.append("success: '").append("yes'")
		.append("}");
		if(type!=null&&type.equals("1")){//编辑文本类型   郑志斌修改 2010-06-12 17：35
			addActionMessage(message.toString());
			doctemplateId=model.getDoctemplateId();
			prepareModel();   						//重新初始化界面
			return "txtinput";
		}else{			
			this.renderText(message.toString());						//word类型
			return null;
		}
		
	}
	
	/**
	 * 判断上传的文件，是否是图片
	 * @author zhengzb
	 * @desc 
	 * 2010-6-22 下午07:34:35 
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public Boolean IsHasImage(String fileType) throws Exception{
		Boolean flag=false;
		String arrImage[]={ "jpg","JPG","jpeg","JPEG","bmp","BMP","gif","GIF","png","PNG","psd","PSD","dxf","DXF","cdr","CDR" };
		for(int i=0;i<arrImage.length;i++){
			if(arrImage[i].equals(fileType))
			{
				flag=true;
			}
		}
		return flag;		
	}
	
	/*
	 * 
	 * Description:读取模板内容
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 3, 2010 10:37:54 PM
	 */
	public String readTemplate()throws Exception{
		ToaDoctemplate doctemplate=manager.getDoctemplate(doctemplateId);
		byte[] annexContent = doctemplate.getDoctemplateContent();
		String tempContent = new String(annexContent);
		String content = HtmlUtils.htmlEscape(tempContent);
		this.renderText(content);
		return null;
	}
	
	/**
	* @method getCharset
	* @author 申仪玲
	* @created 2011-10-29 下午03:49:37
	* @description 获取TXT文件的编码
	* @return String 返回类型
	*/
	public String getCharset(File file ) {   
		String charset = "gb2312";   
        byte[] head = new byte[3];   
        try {      
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );  
            bis.mark( 0 );   
            int read = bis.read( head, 0, 3 );   
            if (read == -1){
            	return charset;
            }else{
	            if (head[0] == (byte) 0xFF && head[1] == (byte)0xFE ) {  
	                charset = "UTF-16";   	        
	            }   
	            else if (head[0] == (byte)0xFE && head[1] == (byte)0xFF ) {  
	                charset = "unicode";   
 
	            }   
	            else if (head[0] == (byte)0xEF && head[1] == (byte)0xBB && head[2] == (byte)0xBF ) {  
	                charset = "UTF-8";    
	            } 
	            bis.reset();
            }    
            bis.close();   
        } catch ( Exception e ) {   
            e.printStackTrace();   
        }   
  
        return charset;    
    }   

	/**
	 * author:胡丽丽
	 * description:上传文件模版
	 * modifyer:
	 * description:
	 * @return
	 */
	public String fileSave() throws Exception {
		String[] names=fileName.split(",");
		String titlename="";//模版名称
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String newdate = sdf.format(new Date());
		String htmlStr="";
		if(file!=null&&file.length>0){
			ToaDoctemplateGroup group=null;
			for(int i=0;i<file.length;i++){
				model=new ToaDoctemplate();
				titlename=names[i].substring(0,names[i].lastIndexOf("."));
				model.setDoctemplateTitle(titlename);
				model.setDoctemplateCreateTime(sdf.parse(newdate));
				group=typemanager.getTemplateType(docgroupId);
				if(group!=null){//模版类型是否为空
					model.setToaDoctemplateGroup(group);
					type=group.getDocgroupType();
				}
				if(type!=null&&type.equals("1")){
					if(file[i].length()>0){
						String charset = this.getCharset(file[i]);
						//System.out.println(charset);
						BufferedReader fReader = new BufferedReader(new InputStreamReader(new FileInputStream(file[i]),charset));
						
						String string = null;
						StringBuilder bufferBuilder = new StringBuilder();
						while((string =fReader.readLine()) != null){
							string ="<p>"+string+"</p>";
							bufferBuilder.append(HtmlUtils.htmlEscape(string));
						}
						logger.info(bufferBuilder);
						model.setDoctemplateTxtContent(bufferBuilder.toString());
						
					}else {
						htmlStr="<script>alert('上传的【"+titlename+"】txt文件为空，请重新确认。');  window.parent.location.reload();window.close();</script>";
						break;
					}
				}
				else{
					FileInputStream fis = null;
					
					try {
						fis = new FileInputStream(file[i]);
						byte[] buf = new byte[(int)file[i].length()];
						fis.read(buf);
						model.setDoctemplateContent(buf);
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				manager.saveDoctemplate(model);
			}
		}
		if(htmlStr.length()>0){
			return renderHtml(htmlStr);
		}else {
			
			return renderHtml("<script>window.parent.location.reload();window.close();</script>");
		}
	}
	/**
	 * author:胡丽丽
	 * description:上传文件模版
	 * modifyer:
	 * description:
	 * @return
	 */
	public String inputFile() throws Exception {
		ToaDoctemplateGroup group=typemanager.getTemplateType(docgroupId);
		if(group!=null){
			type=group.getDocgroupType();			
		}
		return "fileinput";
	}

	/**
	 * 兼容2003 - 2007 打开空文档。
	 * @author:邓志城
	 * @date:2010-4-13 上午11:49:00
	 */
	public synchronized void openEmptyDocFromUrl() {
		HttpServletResponse response = this.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		String docType = getRequest().getParameter("docType");
		logger.info("文档类型：" + docType);
		try{
			if("1".equals(docType)){
				type = ".doc";
			}else if("2".equals(docType)){
				type = ".xls";
			}else if("3".equals(docType)){
				type = ".ppt";
			}else if("4".equals(docType)){
				type = ".vsd";
			}else if("5".equals(docType)){
				type = ".mpp";
			}else if("6".equals(docType)){
				type = ".wps";
			}
			String rootPath = getRequest().getSession().getServletContext().getRealPath("/empty"+type);
			output = response.getOutputStream();
			File file = new File(rootPath);
			if(!file.exists()){
				rootPath = getRequest().getSession().getServletContext().getRealPath("/empty.doc");
				file = new File(rootPath);
			}
			logger.info("打开文档:"+rootPath);
			byte[] buf = FileUtil.inputstream2ByteArray(new FileInputStream(file));
			output.write(buf);
		}catch(Exception e){
			logger.error("打开文档出错了。",e);
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	public ToaDoctemplate getModel() {
		return model;
	}

	public void setModel(ToaDoctemplate model) {
		this.model = model;
	}
	@Autowired
	public void setManager(DocTempItemManager manager) {
		this.manager = manager;
	}

	public String getDoctemplateId() {
		return doctemplateId;
	}

	public void setDoctemplateId(String doctemplateId) {
		this.doctemplateId = doctemplateId;
	}

	public Page<ToaDoctemplate> getPage() {
		return page;
	}

	public String getDocgroupId() {
		return docgroupId;
	}

	public void setDocgroupId(String docgroupId) {
		this.docgroupId = docgroupId;
	}
	
	@Autowired
	public void setTypemanager(DocTempTypeManager typemanager) {
		this.typemanager = typemanager;
	}

	public String getDoctempTypeName() {
		return doctempTypeName;
	}

	public void setDoctempTypeName(String doctempTypeName) {
		this.doctempTypeName = doctempTypeName;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<ToaDoctemplate> getList() {
		return list;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTemplateImagename() {
		return templateImagename;
	}
	public void setTemplateImagename(String templateImagename) {
		this.templateImagename = templateImagename;
	}
	public String getDocgroupType() {
		return docgroupType;
	}
	public void setDocgroupType(String docgroupType) {
		this.docgroupType = docgroupType;
	}
}
