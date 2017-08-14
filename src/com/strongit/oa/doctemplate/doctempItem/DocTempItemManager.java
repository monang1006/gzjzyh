/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板manager类
 */
package com.strongit.oa.doctemplate.doctempItem;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaDoctemplate;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@Service
@Transactional
public class DocTempItemManager {
	
	private final String POSITON="oa\\js\\worklog\\";
	
	private GenericDAOHibernate<ToaDoctemplate, java.lang.String> itemDao;

	
	private DocTempTypeManager typemanager;
	
	private IUserService userService;


	/**
	 * author:lanlc 
	 * description:无参构造方法 
	 * modifyer:
	 */
	public DocTempItemManager() {
	}

	/**
	 * author:lanlc 
	 * description:注入sessionFactory 
	 * modifyer:
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new GenericDAOHibernate<ToaDoctemplate, String>(
				sessionFactory, ToaDoctemplate.class);

	}

	
	/**
	 * author:lanlc 
	 * description:返回所有符合条件的公文模板类别下的模板项列表 
	 * modifyer:
	 * @param docgroupId 类型ID
	 * @param page  分页对象
	 * @param model  模板类别对象
	 * @return
	 */
	public Page<ToaDoctemplate> getItemByTempType(String docgroupId,
			Page<ToaDoctemplate> page, ToaDoctemplate model) throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[3]; //创建数组,接收搜索参数
			StringBuffer hql = new StringBuffer(
					"from ToaDoctemplate t where t.toaDoctemplateGroup.docgroupId=?");
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				
				if(isView){
					hql.append(" and t.doctemplateOrgCode like '"+org.getSupOrgCode()+"%'");		
				}else {
					hql.append(" and t.doctemplateOrgId  = '"+org.getOrgId()+"'");
				}
			}
			
			int i = 0;  //统计接收参数数量，调用具体的查询方法
			if(model.getDoctemplateTitle()!=null && !"".equals(model.getDoctemplateTitle())){
				hql.append(" and t.doctemplateTitle like ?");
				model.setDoctemplateTitle(model.getDoctemplateTitle().trim());
				obj[i]="%"+model.getDoctemplateTitle()+"%";
				i++;
			}
			if(model.getDoctemplateCreateTime()!=null && !"".equals(model.getDoctemplateCreateTime())){
				hql.append(" and t.doctemplateCreateTime = ?");
				obj[i] = model.getDoctemplateCreateTime();
				i++;
			}
			
			if(model.getDocType()!=null && !"".equals(model.getDocType())){
				hql.append(" and t.docType = ?");
				obj[i] = model.getDocType();
				i++;
			}
			//增加按日期排序 dengzc 2010年7月12日14:37:20
			//hql.append(" order by t.doctemplateCreateTime asc");
			//增加按排序号排序,相同的序号按时间排序 liss
			hql.append(" order by t.docNumber asc,t.doctemplateCreateTime desc");
			//end
			if (i == 0) {
				page = itemDao.find(page, hql.toString(), docgroupId);
			}
			else if(i==1){
				page = itemDao.find(page, hql.toString(), docgroupId,obj[0]);
			}
			else if(i==2){
				page = itemDao.find(page, hql.toString(), docgroupId,obj[0],obj[1]);
			}else if(i==3){
				page = itemDao.find(page, hql.toString(), docgroupId,obj[0],obj[1],obj[2]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文模板项页及查询"});
		}
	}

	/**
	 * author:lanlc 
	 * description:获取指定的公文模板 
	 * modifyer:
	 * @param doctemplateId
	 * @return
	 */
	public ToaDoctemplate getDoctemplate(String doctemplateId) throws SystemException,ServiceException{
		try{
			return itemDao.get(doctemplateId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文模板"});
		}
	}

	/**
	 * author:lanlc 
	 * description:保存公文模板 
	 * modifyer:
	 * 
	 * @param model
	 */
	public void saveDoctemplate(ToaDoctemplate model) throws SystemException,ServiceException{
		try{
			User user=userService.getCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			if(org!=null){
				model.setDoctemplateOrgId(org.getOrgId());
				model.setDoctemplateOrgCode(org.getSupOrgCode());
			}
			itemDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存公文模板"});
		}
	}

	/**
	 * author:lanlc 
	 * description:删除公文模板 
	 * modifyer:
	 * 
	 * @param doctemplateId
	 */
	public void delDoctemplate(ToaDoctemplate toaDoctemplate) throws SystemException,ServiceException{
		try{
			itemDao.delete(toaDoctemplate);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除公文模板"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:编辑时打开文档，文档流直接写入HttpServletResponse请求
	 * modifyer:
	 * @param response
	 * @param doctemplateId
	 */
	public void setContentToHttpResponse(HttpServletResponse response, String doctemplateId) throws SystemException,ServiceException{
		try{
			ToaDoctemplate model = getDoctemplate(doctemplateId);
			response.reset();
			response.setContentType("application/octet-stream");
			/*response.setHeader("Content-Disposition", "attachment; filename="
					+ model.getDoctemplateTitle());*/
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(model.getDoctemplateContent());
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"打开公文模板"});
		}
	}
	
	/**
	 * 根据模板类型，获取模板项列表
	 * @author zhengzb
	 * @desc 
	 * 2010-6-21 上午10:57:48 
	 * @param docgroupType 模板类型
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaDoctemplate> getAllTxtTemplate(String docgroupType) throws SystemException,ServiceException{
		try {
			List list=null;
			List txtList=typemanager.getTemplateByType(docgroupType);
			String groupIds="";
			for(int i=0;i<txtList.size();i++){
				ToaDoctemplateGroup groupTemplate=(ToaDoctemplateGroup)txtList.get(i);
				groupIds+=",'"+groupTemplate.getDocgroupId()+"'";
			}
			if(null!=groupIds&&!"".equals(groupIds)){
				groupIds=groupIds.substring(1);
				String hql="select t.doctemplateId,t.doctemplateTitle,t.doctemplateContent,t.doctemplateCreateTime,t.doctemplateRemark,t.logo,t.doctemplateTxtContent ,t.toaDoctemplateGroup.docgroupId  from ToaDoctemplate t where t.toaDoctemplateGroup.docgroupId in ("+groupIds+") ";
				if(userService.isViewSetOpen()) {
					User user=userService.getCurrentUser();
					TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
					boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
					String orgStr="";
					
					if(isView){
						orgStr=" and t.doctemplateOrgCode like '"+org.getSupOrgCode()+"%'";		
					}else {
						orgStr=" and t.doctemplateOrgId  = '"+org.getOrgId()+"'";
					}
					hql=hql+orgStr;
					
				}
				
				list=itemDao.find(hql);
				return pakagingList(list);
			}else{
				return new ArrayList<ToaDoctemplate>();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"打开公文模板"});
		}
	}
	
	/**
	 * 把list组装成List<ToaDoctemplate> 列表
	 * @author zhengzb
	 * @desc 
	 * 2010-6-21 下午12:02:02 
	 * @param list
	 * @return
	 */
	public List<ToaDoctemplate> pakagingList(List list){
		List<ToaDoctemplate> templateList=new ArrayList<ToaDoctemplate>();
		for(Object temp: list){
			Object[] obj=(Object[])temp;
			ToaDoctemplate template=new ToaDoctemplate();
			template.setDoctemplateId(obj[0].toString());
			if(obj[1]!=null){
				template.setDoctemplateTitle(obj[1].toString());
			}
			if(obj[2]!=null){
				template.setDoctemplateContent((byte[])obj[2]);
			}
			if(obj[3]!=null){
				template.setDoctemplateCreateTime((Date)obj[3]);
			}
			if(obj[4]!=null){
				template.setDoctemplateRemark(obj[4].toString());
			}
			if(obj[5]!=null){
				template.setLogo(obj[5].toString());
			}
			if(obj[6]!=null){
				template.setDoctemplateTxtContent(obj[6].toString());
			}
			Map<String , ToaDoctemplateGroup>map=getMapGroup();
			if(obj[7]!=null){
				if(map.get(obj[7].toString())!=null){
					template.setToaDoctemplateGroup(map.get(obj[7].toString()));
				}
			}
			templateList.add(template);
		}
		return templateList;
	}
	
	public Map<String, ToaDoctemplateGroup> getMapGroup(){
		List<ToaDoctemplateGroup> list=typemanager.getAllTypeTemplate();
		Map<String, ToaDoctemplateGroup> map=new HashMap<String, ToaDoctemplateGroup>();
		if(list!=null){
			for(ToaDoctemplateGroup group:list){
				map.put(group.getDocgroupId(),group);
			}
		}
		return map;
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板
	 * modifyer:
	 * @return
	 */
	public List<ToaDoctemplate> getAllTemplate()throws SystemException,ServiceException{
		try{
			return itemDao.findAll();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文模板"});
		}
	}

	/**
	 * 得到每个类别下的模板列表
	 * @author:邓志城
	 * @date:2010-7-9 下午07:45:10
	 * @return
	 * 	Map<Object,List>
	 * 		key:类别id
	 * 		value：List<Object[]>{模板id，模板名称}
	 */
	public Map<Object, List<Object[]>> getTemplateMap() {
		String hql = "select t.toaDoctemplateGroup.docgroupId,t.doctemplateId,t.doctemplateTitle from ToaDoctemplate t where 1=1";
		if(userService.isViewSetOpen()) {
			User user=userService.getCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			String orgStr="";
			boolean isSetOpen = userService.isViewSetOpen();
			if(isSetOpen) {
				if(isView){
					orgStr=" and t.doctemplateOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.doctemplateOrgId  = '"+org.getOrgId()+"'";
				}			
			}
			hql=hql+orgStr;
			
		}
		
		List list = itemDao.find(hql);
		Map<Object, List<Object[]>> map = new HashMap<Object, List<Object[]>>();
		if(list != null && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				if(!map.containsKey(objs[0])){
					List<Object[]> templateLst = new ArrayList<Object[]>();
					templateLst.add(new Object[]{objs[1],objs[2]});
					map.put(objs[0], templateLst);
				}else{
					map.get(objs[0]).add(new Object[]{objs[1],objs[2]});
				}
			}
		}
		return map;
	}
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别下的公文模板项
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public List getTypeByItem(String docgroupId)throws SystemException,ServiceException{
		try{
			String hql="from ToaDoctemplate t where t.toaDoctemplateGroup.docgroupId=?";
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.doctemplateOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.doctemplateOrgId  = '"+org.getOrgId()+"'";
				}
				hql=hql+orgStr;
				
			}
			
			Object[] values={docgroupId};
			List typeByItemList = itemDao.find(hql.toString(), values);
			return typeByItemList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取类别下的公文模板"});
		}
	}
	
	

	/*
	 * 
	 * Description:上传图片至服务器中
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 2, 2010 7:56:18 PM
	 */
	public void uploadFile(HttpServletRequest request,String id){
		try {
			String path=request.getSession().getServletContext().getRealPath("/")+POSITON;
			DiskFileUpload fu = new DiskFileUpload();      
			fu.setSizeMax(4194304);    		// 设置最大文件尺寸，这里是4MB  
			fu.setSizeThreshold(4096);   	// 设置缓冲区大小，这里是4kb  
			fu.setRepositoryPath(path);  	// 设置临时目录：   			   
			List fileItems = fu.parseRequest(request);	// 得到所有的文件：
			Iterator i = fileItems.iterator();    
			// 依次处理每一个文件：    
			while(i.hasNext()) {    
				FileItem fi = (FileItem)i.next();    		  
				String fileName=fi.getName();    		// 获得文件名，这个文件名包括路径：    
				String fileType=fileName.substring(fileName.lastIndexOf(".")+1);//文件类型
				fi.write(new File(path + "\\"+id+"."+fileType));	// 写入文件，暂定文件名为a.txt，可以从fileName中提取文件名：  
			}
		}catch (FileUploadException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}   
	}

	/**  
     * 生成缩略图  
     * @param srcImageFile 		源图片文件的File实例.  
     * @param dstImageFileName 	待生成的缩略图片文件名  
     * @throws Exception  
     */  
    public static void makeSmallImage(File srcImageFile,String dstImageFileName) throws Exception {   
        FileOutputStream fileOutputStream = null;   
        JPEGImageEncoder encoder = null;   
        BufferedImage tagImage = null;   
        Image srcImage = null;   
        try{   
            srcImage = ImageIO.read(srcImageFile);   
            int srcWidth = srcImage.getWidth(null);		//原图片宽度   
            int srcHeight = srcImage.getHeight(null);	//原图片高度   
            int dstMaxSize = 120;		//目标缩略图的最大宽度/高度，宽度与高度将按比例缩写   
            int dstWidth = srcWidth;	//缩略图宽度   
            int dstHeight = srcHeight;	//缩略图高度   
            float scale = 0;   
            if(srcWidth>dstMaxSize){  	//计算缩略图的宽和高  
                dstWidth = dstMaxSize;   
                scale = (float)srcWidth/(float)dstMaxSize;   
                dstHeight = Math.round((float)srcHeight/scale);   
            }   
            srcHeight = dstHeight;   
            if(srcHeight>dstMaxSize){   
                dstHeight = dstMaxSize;   
                scale = (float)srcHeight/(float)dstMaxSize;   
                dstWidth = Math.round((float)dstWidth/scale);   
            }   
            tagImage = new BufferedImage(dstWidth,dstHeight,BufferedImage.TYPE_INT_RGB); //生成缩略图   
            tagImage.getGraphics().drawImage(srcImage,0,0,dstWidth,dstHeight,null);   
            fileOutputStream = new FileOutputStream(dstImageFileName);   
            encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);   
            encoder.encode(tagImage);   
            fileOutputStream.close();   
            fileOutputStream = null;   
        }finally{   
            if(fileOutputStream!=null){   
                try{   
                    fileOutputStream.close();   
                }catch(Exception e){   
                }   
                fileOutputStream = null;   
            }   
            encoder = null;   
            tagImage = null;   
            srcImage = null;   
            System.gc();   
        }   
    }
    
    @Autowired
	public void setTypemanager(DocTempTypeManager typemanager) {
		this.typemanager = typemanager;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}  

}
