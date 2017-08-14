package com.strongit.oa.prsnfldr.departmentprsnfldr;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.ToaDepartmentPrsnfldrFolder;
import com.strongit.oa.bo.ToaPublicPrsnfldrFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class DepartmentPrsnfldrFolderManager {
	
	private GenericDAOHibernate<ToaDepartmentPrsnfldrFolder, String> folderDao;
	
	private IUserService userService;
	
	public DepartmentPrsnfldrFolderManager(){
		
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		folderDao = new GenericDAOHibernate<ToaDepartmentPrsnfldrFolder, String>(sessionFactory,ToaDepartmentPrsnfldrFolder.class);
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	/**
	 * @return List<DepartmentPrsnfldrFolder>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaDepartmentPrsnfldrFolder> getAllFolders() throws SystemException,ServiceException {
		try {
			String departId = getCurrentUser().getOrgId();
			String hql = "from ToaDepartmentPrsnfldrFolder as folder where folder.orgId=? and folder.folderParentId=? order by folder.folderSort asc";
			Object[] values = {departId,"0"};
			return folderDao.find(hql, values);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * 获取文件夹下的子文件夹
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaDepartmentPrsnfldrFolder> getAllSubFolders(String folderId) throws SystemException,ServiceException{
		try {
			String hql = "from ToaDepartmentPrsnfldrFolder folder where folder.folderParentId=? order by folder.folderSort asc";
			Object[] values = {folderId};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		} 
	}
	
	/**
	 * @param id
	 * @return com.strongit.oa.prsnfldr.departmentprsnfldr.ToaDepartmentPrsnfldrFolder
	 * @roseuid 493CE7600171
	 */
	@Transactional(readOnly=true)
	public ToaDepartmentPrsnfldrFolder getFolderById(String id) throws SystemException,ServiceException {
		try {
			return folderDao.get(id);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		} 
	}
	
	/**
	 * 验证部门文件柜下文件夹重名情况
	 * @author:邓志城
	 * @date:2009-10-14 下午04:29:05
	 * @param folderName
	 * @return
	 */
	public boolean isFolderNameUsed(String parentFolder,String folderName) throws ServiceException{
		try{
			Assert.hasText(folderName, "folderName不能为空。");
			if(parentFolder == null || "".equals(parentFolder)){
				parentFolder = "0";
			}
			String hql = "select count(*) from ToaDepartmentPrsnfldrFolder t where t.folderParentId=? and t.folderName=?";
			Long count = folderDao.findLong(hql, parentFolder,folderName);
			if(count > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			LogPrintStackUtil.logException(e);
			LogPrintStackUtil.error("校验文件夹名称失败。");
			throw new ServiceException("校验文件夹名称失败。");
		}
	}
	
	/**
	 * author:dengzc
	 * description:获取当前用户姓名
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public User getCurrentUser() throws SystemException,ServiceException{
		try {
			return userService.getCurrentUser();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"当前用户信息"});
		} 
	}
	
	/**
	 * 生成部门文件柜中文件夹的最大排序号
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String maxDepartmentFolderSort(String folderParentId)throws SystemException{
		StringBuffer hql = new StringBuffer("select max(t.folderSort)  from ToaDepartmentPrsnfldrFolder t where t.folderParentId=? ");
		List<String> lst = null;
		String folderSort = "500";
		lst = folderDao.find(hql.toString(),new Object[]{folderParentId});
		if(lst!=null && lst.size()>0){//找到记录
			folderSort = lst.get(0);
			if(!"".equals(folderSort)&& null!=folderSort){//记录不为空
				folderSort = String.valueOf(Integer.parseInt(folderSort)+1);
			}else{//记录为空
				folderSort = "500";
			}
		}else{
			folderSort = "500";
		}
		if(Integer.parseInt(folderSort) >= 999){
			throw new SystemException("排序号不能超过999!");
		}
		return folderSort;
	}
	
	/**
	 * 生成部门文件柜中文件夹的最小排序号
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String minDepartmentFolderSort(String folderParentId)throws SystemException{
		StringBuffer hql = new StringBuffer("select min(t.folderSort) from ToaDepartmentPrsnfldrFolder t where t.folderParentId=?");
		List<String> lst = null;
		String folderSort = "500";
		lst = folderDao.find(hql.toString(),new Object[]{folderParentId});
		if(lst!=null && lst.size()>0){//找到记录
			folderSort = lst.get(0);
			if(!"".equals(folderSort) && null!=folderSort){//记录不为空
				folderSort = String.valueOf(Integer.parseInt(folderSort)-1);
			}else{//记录为空
				folderSort = "500";
			}
		}else{
			folderSort = "500";
		}
		if(Integer.parseInt(folderSort) >= 999){
			throw new SystemException("排序号不能超过999!");
		}
		return folderSort;
	}
	
	/**
	 * 新创建一个文件夹
	 * @param folder
	 * @return com.strongit.oa.prsnfldr.departmentprsnfldr.ToaDepartmentPrsnfldrFolder
	 * @roseuid 493CE6F9025C
	 */
	public ToaDepartmentPrsnfldrFolder addFolder(ToaDepartmentPrsnfldrFolder folder) throws SystemException,ServiceException {
		try {
			folderDao.save(folder);
			return null;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件夹"});
		} 
	}
	
	/**
	 * 修改文件夹
	 */
	public void updateFolder(ToaDepartmentPrsnfldrFolder folder) throws SystemException,ServiceException{
		try {
			folderDao.update(folder);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件夹"});
		} 
	}
	/******************************* 框内为webservice 借口调用方法  begin**********************************************/
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaDepartmentPrsnfldrFolder> getDepartmentFolders(Page<ToaDepartmentPrsnfldrFolder> page,
			String departId,String folderName) throws SystemException,ServiceException {
		try {
			String hql = "from ToaDepartmentPrsnfldrFolder as folder where folder.folderParentId='0' and  folder.orgId=? ";
			Object[] values = new Object[10];
			int i=0;
			values[i++]=departId;
			if(folderName!=null&&!"".equals(folderName)){
				hql = hql + " and folder.folderName like ? ";
				values[i++]="%"+folderName+"%";
			}
			if("1".equals(page.getOrderBy())){
				hql = hql + " order by folder.folderCreateDatetime "+ page.getOrder();
			}else{
				hql = hql + " order by folder.folderSort asc ";
			}
			Object[] obj = new Object[i];
			for(int j=0;j<i;j++){
				obj[j]=values[j];
			}
			return folderDao.find(page,hql,obj);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	/******************************* 框内为webservice 借口调用方法  end**********************************************/

}
