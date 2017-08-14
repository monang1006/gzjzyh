/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：界面设置实现类
*/

package com.strongit.oa.dataprivil;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPostProperty;
import com.strongit.oa.bo.ToaPostStructure;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Position;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class PostDataPrivilManager extends BaseManager{
   
   private GenericDAOHibernate<ToaPostStructure, java.lang.String> structureDao;
   
   private GenericDAOHibernate<ToaPostProperty, java.lang.String> propertyDao;
   
   @Autowired private InfoSetManager infoSetManager;
   
   @Autowired private InfoItemManager infoItemManger;
   
   @Autowired private IUserService userService;
   
   private final static String PERSON_TABLE_NAME="T_OA_BASE_PERSON";
   
   private final static String ZERO="0";
   
   private final static String ONE="1";
   
   public static final String hidden = "1"; //隐藏
   
   public static final String hiddenN = "0"; //非隐藏
   
   public static final String readonly = "1"; //只读
   
   public static final String readonlyN = "0"; //非只读
   
   public static final String readwrite = "1";//读写
   
   public static final String readwriteN = "0";//非读写
   
   public PostDataPrivilManager(){
	  
   }
   
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   structureDao=new GenericDAOHibernate<ToaPostStructure, java.lang.String>(sessionFactory,ToaPostStructure.class);
	   propertyDao=new GenericDAOHibernate<ToaPostProperty, java.lang.String>(sessionFactory,ToaPostProperty.class);
   }
   
   /*
    * 
    * Description: 获取分页的岗位信息集权限列表
    * param: 
    * @author 	    彭小青
    * @date 	   Apr 1, 2010 4:13:50 PM
    */
    @Transactional(readOnly = true)
	public Page<ToaPostStructure> getPagePostStructure(Page<ToaPostStructure> page,String postId,String infoSetCode,String status) throws SystemException,ServiceException
	{
		try{
			String hql="from ToaPostStructure t where t.infoSet.infoSetState='"+ONE+"' and t.postId=?";
			if(infoSetCode!=null&&!"".equals(infoSetCode)){
				hql+=" and t.infoSet.infoSetCode='"+infoSetCode+"'";
			}
			if(status!=null&&!"".equals(status)){//权限
				if(ZERO.equals(status)){		//只读
					hql+=" and t.postStructureReadonly='"+ONE+"'";
				}else if(ONE.equals(status)){	//读写
					hql+=" and t.postStructureHide='"+ONE+"'";
				}else{							//隐藏
					hql+=" and t.postStructureReadwrite='"+ONE+"'";
				}
			}
			hql+=" order by t.infoSet.infoSetOrderno";
			page=structureDao.find(page, hql, postId);
			return page;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分页岗位信息集权限列表"}); 
		}
	}
    
    /*
     * 
     * Description:获取岗位信息集权限列表
     * param: 
     * @author 	    彭小青
     * @date 	    Apr 1, 2010 4:49:00 PM
     */
	public List<ToaPostStructure> getPostStructureList(String postId) throws SystemException,ServiceException
	{
		try{
			List<ToaPostStructure> list=this.getToaPostStructureList(postId, ""); //查找该岗位的信息集权限列表
			List<ToaSysmanageStructure> structureList=infoSetManager.getChildCreatedInfoSet2(PERSON_TABLE_NAME, true);
			ToaPostStructure postStructure=null;	
			if(structureList!=null&&structureList.size()>0){//存在信息集
				if(list==null||list.size()==0){				//如果是第一次设置
					list=new ArrayList<ToaPostStructure>();
					for(ToaSysmanageStructure structure:structureList){
						postStructure=new ToaPostStructure();
						postStructure.setInfoSet(structure);
						postStructure.setPostId(postId);
						postStructure.setPostStructureHide("0");
						postStructure.setPostStructureReadonly("0");
						postStructure.setPostStructureReadwrite("1");
						structureDao.save(postStructure);
						list.add(postStructure);
					}
				}else if(structureList.size()!=list.size()){//如果添加了信息集
					for(ToaSysmanageStructure structure:structureList){
						postStructure=this.getToaPostStructure(postId,structure.getInfoSetCode());
						if(postStructure==null){	
							postStructure=new ToaPostStructure();
							postStructure.setInfoSet(structure);
							postStructure.setPostId(postId);
							postStructure.setPostStructureHide("0");
							postStructure.setPostStructureReadonly("0");
							postStructure.setPostStructureReadwrite("1");
							structureDao.save(postStructure);
							list.add(postStructure);
						}	
					}
				}
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getPostStructureList+岗位信息集权限列表"}); 
		}
	}
	
	/*
	 * 
	 * Description:获取岗位的信息项权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 10:59:29 PM
	 */
	public List<ToaPostProperty> getPostPropertyList(String postId,String infoSetCode) throws SystemException,ServiceException{
		try{
			List<ToaPostProperty> list=this.getToaPostPropertyList(postId,infoSetCode,""); 
			List<ToaSysmanageProperty> itemList=infoItemManger.getCreatedAndReadItems(infoSetCode);
			List<ToaPostProperty> tempList=null;
			ToaPostProperty postProperty=null;
			if(itemList!=null&&itemList.size()>0){	//信息项列表不为null
				if(list==null||list.size()==0){		//第一次设置岗位的信息项权限
					list=new ArrayList<ToaPostProperty>();
					for(ToaSysmanageProperty item:itemList){
						postProperty=new ToaPostProperty();
						postProperty.setInfoItem(item);
						postProperty.setPostId(postId);
						postProperty.setInfoSetCode(infoSetCode);
						if(item.getInfoItemDatatype().equals("15")){
							postProperty.setPostPropertyReadonly("1");
							postProperty.setPostPropertyReadwrite("0");
							postProperty.setPostPropertyHide("0");
						}else{
							postProperty.setPostPropertyReadonly("0");
							postProperty.setPostPropertyReadwrite("1");
							postProperty.setPostPropertyHide("0");
						}
						propertyDao.save(postProperty);
						list.add(postProperty);
					}
				}else if(itemList.size()!=list.size()){//添加了信息项
					for(ToaSysmanageProperty item:itemList){	//循环信息项
						tempList=this.getToaPostPropertyList(postId,"",item.getInfoItemCode());	//根据岗位ID和信息项编码查找是否已设置
						if(tempList!=null&&tempList.size()>0){	//设置了
						}else{			//没设置则进行设置		
							postProperty=new ToaPostProperty();
							postProperty.setInfoItem(item);
							postProperty.setPostId(postId);
							postProperty.setInfoSetCode(infoSetCode);
							if(item.getInfoItemDatatype().equals("15")){
								postProperty.setPostPropertyReadonly("1");
								postProperty.setPostPropertyReadwrite("0");
								postProperty.setPostPropertyHide("0");
							}else{
								postProperty.setPostPropertyReadonly("0");
								postProperty.setPostPropertyReadwrite("1");
								postProperty.setPostPropertyHide("0");
							}
							propertyDao.save(postProperty);	//保存
							list.add(postProperty);			//放到list里
						}	
					}
				}
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getPostPropertyList+获取岗位的信息项权限"}); 
		}
	}
	
	/*
	 * 
	 * Description:根据岗位和信息集查找敢为信息集权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 5:20:50 PM
	 */
	public ToaPostStructure getToaPostStructure(String postId,String infoSetCode)throws SystemException,ServiceException{
		try{
			List<ToaPostStructure> list=structureDao.find(" from ToaPostStructure t where t.postId=? and t.infoSet.infoSetCode=?",postId,infoSetCode);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getToaPostStructure+根据岗位和信息集查找岗位信息集权限"}); 
		}
	}
	
	/*
	 * 
	 * Description:查找岗位的信息项权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 10:29:06 PM
	 */
	public List<ToaPostProperty> getToaPostPropertyList(String postId,String infoSetCode,String infoItemCode)throws SystemException,ServiceException{
		try{
			String hql="from ToaPostProperty t where t.infoItem.infoItemState='"+ONE+"' and t.postId=? ";
			if(infoSetCode!=null&&!"".equals(infoSetCode)){
				hql+=" and t.infoSetCode='"+infoSetCode+"' ";
			}
			if(infoItemCode!=null&&!"".equals(infoItemCode)){
				String infoItemArr[]=infoItemCode.split(",");
				String infoItemCodes="";
				for(int i=0;i<infoItemArr.length;i++){
					infoItemCodes+=",'"+infoItemArr[i]+"'";
				}
				infoItemCodes=infoItemCodes.substring(1);
				hql+=" and t.infoItem.infoItemCode in ("+infoItemCodes+")";
			}
			hql+=" order by t.infoItem.infoItemOrderby";
			List<ToaPostProperty> list=propertyDao.find(hql,postId);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"getToaPostPropertyList+查找岗位的信息项权限"}); 
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 10:29:06 PM
	 */
	public List<ToaPostStructure> getToaPostStructureList(String postId,String infoSetCode)throws SystemException,ServiceException{
		try{
			String hql="from ToaPostStructure t where t.infoSet.infoSetState='"+ONE+"' and t.postId=? ";
			if(infoSetCode!=null&&!"".equals(infoSetCode)){
				String infoSetArr[]=infoSetCode.split(",");
				String infoSetCodes="";
				for(int i=0;i<infoSetArr.length;i++){
					infoSetCodes+=",'"+infoSetArr[i]+"'";
				}
				infoSetCodes=infoSetCodes.substring(1);
				hql+=" and t.infoSet.infoSetCode in ("+infoSetCodes+")";
			}
			hql+=" order by t.infoSet.infoSetOrderno";
			List<ToaPostStructure> list=structureDao.find(hql,postId);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"getToaPostStructureList+查找岗位信息集权限"}); 
		}
	}
	
	/*
	 * 
	 * Description:设置信息集为只读
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String readSet(String postId,String infoSetCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostStructure> list=this.getToaPostStructureList(postId, infoSetCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostStructure structrue=list.get(i);
					structrue.setPostStructureReadonly(readonly);
					structrue.setPostStructureReadwrite(readwriteN);
					structrue.setPostStructureHide(hiddenN);
				}
				structureDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"readSet+设置信息集为只读"}); 
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:设置信息集为读写
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String readWriteSet(String postId,String infoSetCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostStructure> list=this.getToaPostStructureList(postId, infoSetCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostStructure structrue=list.get(i);
					structrue.setPostStructureReadonly(readonlyN);
					structrue.setPostStructureReadwrite(readwrite);
					structrue.setPostStructureHide(hiddenN);
				}
				structureDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"readWriteSet+设置信息集为读写"}); 
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:设置信息集为隐藏
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String hiddenSet(String postId,String infoSetCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostStructure> list=this.getToaPostStructureList(postId, infoSetCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostStructure structrue=list.get(i);
					structrue.setPostStructureReadonly(readonlyN);
					structrue.setPostStructureReadwrite(readwriteN);
					structrue.setPostStructureHide(hidden);
				}
				structureDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"hiddenSet+设置信息集为隐藏"}); 
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:设置信息项为只读
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String itemReadSet(String postId,String infoItemCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostProperty> list=this.getToaPostPropertyList(postId,"", infoItemCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostProperty property=list.get(i);
					property.setPostPropertyReadonly(readonly);
					property.setPostPropertyReadwrite(readwriteN);
					property.setPostPropertyHide(hiddenN);
				}
				propertyDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"itemReadSet+设置信息项为只读"}); 
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:设置信息项为读写
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String itemReadWriteSet(String postId,String infoItemCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostProperty> list=this.getToaPostPropertyList(postId,"", infoItemCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostProperty property=list.get(i);
					property.setPostPropertyReadonly(readonlyN);
					property.setPostPropertyReadwrite(readwrite);
					property.setPostPropertyHide(hiddenN);
				}
				propertyDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"itemReadWriteSet+设置信息项为读写"}); 
		}
		return msg;
	}
	
	/*
	 * 
	 * Description:设置信息项为隐藏
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 1, 2010 8:25:24 PM
	 */
	public String itemHiddenSet(String postId,String infoItemCode)throws SystemException,ServiceException{
		String msg="failed";
		try{
			List<ToaPostProperty> list=this.getToaPostPropertyList(postId,"", infoItemCode);
			if(list!=null){
				for(int i=0;i<list.size();i++){
					ToaPostProperty property=list.get(i);
					property.setPostPropertyReadonly(readonlyN);
					property.setPostPropertyReadwrite(readwriteN);
					property.setPostPropertyHide(hidden);
				}
				propertyDao.save(list);
			}
			msg="success";
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"itemHiddenSet+设置信息项为隐藏"}); 
		}
		return msg;
	}
	
	
	
	/*
	 * 
	 * Description:根据岗位ID和信息项code查找信息项权限列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 3:09:25 PM
	 */
	public List<ToaPostProperty> getToaPostPropertyList(String postId,String infoItemCode)throws SystemException,ServiceException{
		try{
			String hql=" from ToaPostProperty t where t.postId in ("+postId+") and t.infoItem.infoItemCode=?";
			return propertyDao.find(hql,infoItemCode);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getToaPostPropertyList+根据岗位ID和信息项code查找信息项权限列表"}); 
		}
	}

	/*
	 * 
	 * Description:判断当前用户是否有该信息项的读写或只读权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 3:05:14 PM
	 */
	public boolean isHasPrivilOfPerson(ToaSysmanageProperty item,String userid)throws SystemException,ServiceException{
		try{
			List<Position> positionList= userService.getUserPositionsByUserId(userid);
			Position position=null;
			ToaPostProperty property=null;
			String postId="";
			if(positionList==null||positionList.size()==0){
				return false;
			}
			for(int i=0;i<positionList.size();i++){
				position=positionList.get(i);
				postId+=",'"+position.getPostId()+"'";
			}
			postId=postId.substring(1);
			List<ToaPostProperty> list=this.getToaPostPropertyList(postId, item.getInfoItemCode());
			if(list==null||list.size()==0){
				return false;
			}
			for(int j=0;j<list.size();j++){
				property=list.get(j);
				if(property.getPostPropertyReadwrite().equals(readwrite)||property.getPostPropertyReadonly().equals(readonly)){
					return true;
				}
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"isHasPrivilOfPerson+判断当前用户是否有该信息项的读写或只读权限"}); 
		}
	}
	
	
	
	/*
	 * 
	 * Description:获取当前用户的拥有权限的信息项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 2:47:51 PM
	 */
	public List<ToaSysmanageProperty> getUserInfoPros(String infoSetCode)throws SystemException,ServiceException{
		try{
			String userid=userService.getCurrentUser().getUserId();
			if(!this.isHasStructurePrivilOfPerson(infoSetCode, userid)){
				return null;
			}
			List<ToaSysmanageProperty> itemList=infoItemManger.getCreatedAndReadItems(infoSetCode);
			ToaSysmanageProperty item=null;
			List<ToaSysmanageProperty> infoitemlist = new ArrayList<ToaSysmanageProperty>();
			for(int i=0;itemList!=null&&i<itemList.size();i++){
				item=itemList.get(i);
				if(isHasPrivilOfPerson(item, userid)){
					infoitemlist.add(item);
				}
			}
			return infoitemlist;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getUserInfoPros+获取当前用户的拥有权限的信息项"}); 
		}
	}
	
	/*
	 * 
	 * Description:根据岗位ID和信息项code查找信息项权限列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 3:09:25 PM
	 */
	public List<ToaPostStructure> getToaPostStructureList2(String postId,String infoSetCode)throws SystemException,ServiceException{
		try{
			String hql=" from ToaPostStructure t where t.postId in ("+ postId+") and t.infoSet.infoSetCode=?";
			return structureDao.find(hql,infoSetCode);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getToaPostStructureList2+根据岗位ID和信息项code查找信息集权限列表"}); 
		}
	}
	
	/*
	 * 
	 * Description:判断当前用户是否有该信息集的读写或只读权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 3:05:14 PM
	 */
	public boolean isHasStructurePrivilOfPerson(String infoSetCode,String userid)throws SystemException,ServiceException{
		try{
			List<Position> positionList= userService.getUserPositionsByUserId(userid);	//获取当前用户的岗位信息列表
			Position position=null;	
			String postId="";
			ToaPostStructure structure=null;
			if(positionList==null||positionList.size()==0){	//如果该人员没有岗位，则直接返回
				return false;
			}
			for(int i=0;i<positionList.size();i++){	//循环岗位，取出岗位ID
				position=positionList.get(i);
				postId+=",'"+position.getPostId()+"'";
			}
			postId=postId.substring(1);
			List<ToaPostStructure> list=this.getToaPostStructureList2(postId, infoSetCode);	//根据岗位ID和信息集查找信息集权限列表
			if(list==null||list.size()==0){	//如果这些岗位没有设置信息集权限，则直接返回
				return false;
			}
			for(int j=0;j<list.size();j++){
				structure=list.get(j);
				if(structure.getPostStructureReadonly().equals(readwrite)||structure.getPostStructureReadwrite().equals(readonly)){
					return true;
				}
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"isHasStructurePrivilOfPerson+判断当前用户是否有该信息集的读写或只读权限"}); 
		}
	}
	
	/*
	 * 
	 * Description:获取当前用户的拥有权限的信息集
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 2, 2010 2:47:51 PM
	 */
	public List<ToaSysmanageStructure> getUserInfoStru()throws SystemException,ServiceException{
		try{
			String userid=userService.getCurrentUser().getUserId();
			List<ToaSysmanageStructure> infoSetList=infoSetManager.getChildCreatedInfoSet2(PERSON_TABLE_NAME, false);//获取已构建的人员信息集子集	
			List<ToaSysmanageStructure> infoSets = new ArrayList<ToaSysmanageStructure>();
			ToaSysmanageStructure structure=null;
			for(int i=0;infoSetList!=null&&i<infoSetList.size();i++){	//循环子集
				structure=infoSetList.get(i);
				if(isHasStructurePrivilOfPerson(structure.getInfoSetCode(), userid)){//判断当前用户是否有对应信息集的权限
					infoSets.add(structure);
				}
			}
			return infoSets;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getUserInfoStru+获取当前用户的拥有权限的信息集"}); 
		}
	}
	
	/*
	 * 
	 * Description:删除岗位后，将岗位的信息集与信息项权限删除
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 7, 2010 2:36:02 PM
	 */
	public void deletePostDataPrivil(String postId){
		String[] postArr=postId.split(",");
		String postIds="";
		for(int i=0;i<postArr.length;i++){
			postIds+=",'"+postArr[i]+"'";
		}
		postIds=postIds.substring(1);
		
		String hql="  from ToaPostStructure t where t.postId in ("+postIds+")";
		List list=structureDao.find(hql);
		if(list!=null&&list.size()>0)
			structureDao.delete(list);
		
		String hql2=" from ToaPostProperty t where t.postId in ("+postIds+")";
		list=structureDao.find(hql2);
		if(list!=null&&list.size()>0)
			propertyDao.delete(list);
	}
	
}
