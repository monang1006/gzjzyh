/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板套红类别manager
 */
package com.strongit.oa.docredtemplate.docredtype;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDocred;
import com.strongit.oa.bo.ToaDocredGroup;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.docredtemplate.docreditem.DocRedItemManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class DocRedTypeManager {
	private GenericDAOHibernate<ToaDocredGroup,java.lang.String> typeDao;
	
	@Autowired DocRedItemManager itemmanager ;
	
	/**
	 * author:lanlc
	 * description:无参构造方法
	 * modifyer:
	 */
	public DocRedTypeManager(){}
	
	/**
	 * author:lanlc
	 * description:注入sessionFactory
	 * modifyer:
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		typeDao = new GenericDAOHibernate<ToaDocredGroup,java.lang.String>(sessionFactory,ToaDocredGroup.class);
	}
	
	/**
	 * author:lanlc
	 * description:获取指定的公文套红类别
	 * modifyer:
	 * @param redtempGroupId
	 * @return
	 */
	public ToaDocredGroup getDocredType(String redtempGroupId)throws SystemException,ServiceException{
		try{
			return typeDao.get(redtempGroupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取套红类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:删除公文套红类别
	 * modifyer:
	 * @param redtempGroupId
	 */
	public void delDocredType(String redtempGroupId)throws SystemException,ServiceException{
		try{
			typeDao.delete(redtempGroupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除套红类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:保存公文套红类别
	 * modifyer:
	 * @param model
	 */
	public void saveDocredType(ToaDocredGroup model)throws SystemException,ServiceException{
		try{
			typeDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存套红类别"});
		}
	}
	
	
	/**
	 * author:lanlc
	 * description:获取所有父节点为0的公文套红类别
	 * modifyer:
	 * @return
	 */
	public List docDocredTypeList()throws SystemException,ServiceException{
		try{
			String hql="from ToaDocredGroup t where t.redtempGroupParentId=?";
			Object[] values={"0"}; 
			List docDocredTypeList = typeDao.find(hql, values);
			return docDocredTypeList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"套红类别集"});
		}
	}

	/**
	 * 构造套红树：插入套红功能用到.
	 * @author:邓志城
	 * @date:2009-10-26 下午05:55:43
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TempPo> docRedTree()throws SystemException,ServiceException {
		try{
			List<ToaDocredGroup> redGroupList = typeDao.findAll();//得到所有套红类别
			List<TempPo> tempPoList = new ArrayList<TempPo>();//树节点列表
			if(redGroupList != null && !redGroupList.isEmpty()){
				Map<String, List<ToaDocred>> redMap = itemmanager.getTypeMap();//得到套红集合列表
				for(ToaDocredGroup group : redGroupList) {
					String groupName = group.getRedtempGroupName();
					if(groupName != null && !"".equals(groupName)){
						groupName = groupName.trim();
							TempPo po = new TempPo();
							po.setId(group.getRedtempGroupId());
							po.setName(groupName);
							po.setParentId(group.getRedtempGroupParentId());
							po.setType("group");//树节点为套红类别
							tempPoList.add(po);
							//获取类别下的套红数据,并作为树节点挂接在类别下.
							List<ToaDocred> itemList = redMap.get(group.getRedtempGroupId());
							if(itemList != null && !itemList.isEmpty()){
								for(ToaDocred docRed:itemList){
									TempPo itemPo = new TempPo();
									itemPo.setId(docRed.getRedstempId());
									itemPo.setName(docRed.getRedstempTitle());
									itemPo.setParentId(po.getId());
									itemPo.setType("item");//树节点为套红数据
									tempPoList.add(itemPo);
								}
								
							}
							
					}
				}
			}
			
			//这里需要处理只有子节点但是找不到父节点的情况。
			/*for(int i=0;i<tempPoList.size();i++){
				TempPo cpo = tempPoList.get(i);
				if(!isPoExist(cpo, tempPoList)){//父节点子啊当前列表中不存在,展现树时会有问题.需要自动加上此节点的所哟父节点.
					tempPoList.addAll(this.getFatherNode(cpo, redGroupList, null));
				}
			}*/
			
			return tempPoList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"套红类别"});
		}
	}

/*	*//**
	 * 验证某个PO是否存在父节点
	 * @author:邓志城
	 * @date:2009-10-30 下午02:50:09
	 * @param child
	 * @param list
	 * @return
	 *//*
	private boolean isPoExist(TempPo child,List<TempPo> list){
		if(child.getParentId().equals("0")){//根节点情况
			return true;
		}
		for(TempPo po : list){
			if(child.getParentId().equals(po.getId())){
				return true;
			}
		}
		return false;
	}

	*//**
	 * 递归查找某节点的所有父节点
	 * @author:邓志城
	 * @date:2009-10-30 下午03:04:36
	 * @param po
	 * @param redGroupList
	 * @return
	 *//*
	private List<TempPo> getFatherNode(TempPo po ,List<ToaDocredGroup> redGroupList,List<TempPo> fatherList){
		if(fatherList == null){
			fatherList = new ArrayList<TempPo>();
		}
		for(ToaDocredGroup group:redGroupList){
			if(po.getParentId().equals(group.getRedtempGroupId())){
				TempPo parent = new TempPo();
				parent.setId(group.getRedtempGroupId());
				parent.setName(group.getRedtempGroupName());
				parent.setParentId(group.getRedtempGroupParentId());
				parent.setType("group");
				if(!fatherList.contains(parent)){
					fatherList.add(parent);
					getFatherNode(parent, redGroupList, fatherList);
				}
			}
		}
		return fatherList;
			
	}*/
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别下的子类别
	 * modifyer:
	 * @param ParentId
	 * @return
	 */
	public List docredSubTypeList(String parentId)throws SystemException,ServiceException{
		try{
			String hql="from ToaDocredGroup t where t.redtempGroupParentId=?";
			Object[] values={parentId};
			return typeDao.find(hql, values);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"套红类别的子集"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别名称
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public String redtempGroupName(String redtempGroupId)throws SystemException,ServiceException{
		try{
			ToaDocredGroup docredtype=getDocredType(redtempGroupId);
			return docredtype.getRedtempGroupName();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取套红类别名称"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文套红类别
	 * modifyer:
	 * @return
	 */
	public List<ToaDocredGroup> getAllDocredType(){
		return typeDao.findAll();
	}
}
