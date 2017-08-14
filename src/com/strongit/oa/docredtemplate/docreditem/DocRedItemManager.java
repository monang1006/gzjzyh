/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板套红项manager
 */
package com.strongit.oa.docredtemplate.docreditem;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDocred;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class DocRedItemManager {
	private GenericDAOHibernate<ToaDocred,java.lang.String> itemDao;
	
	/**
	 * author:lanlc 
	 * description:无参构造方法 
	 * modifyer:
	 */
	public DocRedItemManager(){};
	
	/**
	 * author:lanlc 
	 * description:注入sessionFactory 
	 * modifyer:
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		itemDao = new GenericDAOHibernate<ToaDocred,String>(sessionFactory,ToaDocred.class);
	}
	
	/**
	 * author:lanlc 
	 * description:返回所有符合条件的公文套红类别下的套红项列表 
	 * modifyer:
	 * @param redtempGroupId
	 * @param page
	 * @param model
	 * @return
	 */
	public Page<ToaDocred> getItemBydocredType(String redtempGroupId,Page<ToaDocred> page,ToaDocred model)throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[2];  //创建数组,接收搜索参数
			StringBuffer hql = new StringBuffer(
			"from ToaDocred t where t.toaDocredGroup.redtempGroupId=?");
			int i = 0;  //统计接收参数数量，调用具体的查询方法
			if(model.getRedstempTitle()!=null && !"".equals(model.getRedstempTitle())){
				hql.append(" and t.redstempTitle like ?");
				obj[i]="%"+model.getRedstempTitle()+"%";
				i++;
			}
			if(model.getRedstempCreateTime()!=null && !"".equals(model.getRedstempCreateTime())){
				hql.append(" and t.redstempCreateTime = ?");
				obj[i]=model.getRedstempCreateTime();
				i++;
			}
			if (i == 0) {
				page = itemDao.find(page, hql.toString(), redtempGroupId);
			}
			else if(i==1){
				page = itemDao.find(page, hql.toString(), redtempGroupId,obj[0]);
			}
			else if(i==2){
				page = itemDao.find(page, hql.toString(), redtempGroupId,obj[0],obj[1]);
			}
			
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文套红分页及查询"});
		}
	}
	
	/**
	 * author:lanlc 
	 * description:获取指定的公文套红 
	 * modifyer:
	 * @param redstempId
	 * @return
	 */
	public ToaDocred getDocRed(String redstempId)throws SystemException,ServiceException{
		try{	
			return itemDao.get(redstempId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文套红"});
		}
	}
	
	/**
	 * author:lanlc 
	 * description:保存公文套红
	 * modifyer:
	 * 
	 * @param model
	 */
	public void saveDocRed(ToaDocred model)throws SystemException,ServiceException{
		try{
			itemDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存公文套红"});
		}
	}
	
	/**
	 * author:lanlc 
	 * description:删除公文套红
	 * modifyer:
	 * 
	 * @param redstempId
	 */
	public void delDocRed(String redstempId)throws SystemException,ServiceException{
		try{
			itemDao.delete(redstempId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除公文套红"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:编辑时打开文档，文档流直接写入HttpServletResponse请求
	 * modifyer:
	 * @param response
	 * @param redstempId
	 */
	public void setContentToHttpResponse(HttpServletResponse response, String redstempId) throws SystemException,ServiceException{
		try{
			ToaDocred model = getDocRed(redstempId);
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ model.getRedstempTitle());
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(model.getRedstempContent());
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"打开公文套红"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文套红
	 * modifyer:
	 * @return
	 */
	public List<ToaDocred> getAllDocred()throws SystemException,ServiceException{
		try{
			return itemDao.findAll();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取所有的公文套红"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别下的公文套红项
	 * modifyer:
	 * @param redtempGroupId
	 * @return
	 */
	public List getTypeByItem(String redtempGroupId)throws SystemException,ServiceException{
		try{
			String hql="from ToaDocred t where t.toaDocredGroup.redtempGroupId=?";
			Object[] values={redtempGroupId};
			List typeByItemList = itemDao.find(hql.toString(), values);
			return typeByItemList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文套红类别下的公文套红项"});
		}
	}
	
	/**
	 * 得到所有套红集合。KEY:类别id；Value：类别下的套红列表
	 * @author:邓志城
	 * @date:2009-10-26 下午05:29:56
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<ToaDocred>> getTypeMap()throws SystemException,ServiceException{
		try{
			String hql = "from ToaDocred t ";
			Map<String, List<ToaDocred>> redMap = new HashMap<String, List<ToaDocred>>();
			List<ToaDocred> docRedList = itemDao.find(hql);
			if(docRedList != null && !docRedList.isEmpty()){
				for(ToaDocred docRed : docRedList){
					String groupId = docRed.getToaDocredGroup().getRedtempGroupId();
					if(!redMap.containsKey(groupId)){
						List<ToaDocred> tempList = new ArrayList<ToaDocred>();
						tempList.add(docRed);
						redMap.put(groupId, tempList);
					}else{
						redMap.get(groupId).add(docRed);
					}
				}
			}
			return redMap;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文套红类别下的公文套红项"});
		}
	}
	
}
