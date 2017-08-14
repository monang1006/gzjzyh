//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\privateprsnfldr\\PrsnfldrFolderManager.java

package com.strongit.oa.prsnfldr.privateprsnfldr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.ToaPrivatePrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrConfig;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrShare;
import com.strongit.oa.bo.ToaPubliccontact;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.theme.IThemeManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      文件夹管理Manager
 */
@Service
@Transactional
public class PrsnfldrFolderManager {
	
	private GenericDAOHibernate<ToaPrivatePrsnfldrFolder, String> folderDao;
	private GenericDAOHibernate<ToaPrsnfldrShare, String> shareDao;
	private GenericDAOHibernate<ToaPrsnfldrConfig, String> configDao;
	private GenericDAOHibernate<ToaPrsnfldrFolder, String> commonFolderDao; 
	private IUserService userService;

	@Autowired ParamConfigService paramConfigService ;
	@Autowired PrsnfldrFileManager pffManager;
	
	/**
	 * @roseuid 493DDBAC00BB
	 */
	public PrsnfldrFolderManager() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 493C866A034B
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		folderDao = new GenericDAOHibernate<ToaPrivatePrsnfldrFolder, String>(sessionFactory,ToaPrivatePrsnfldrFolder.class);
		shareDao  = new GenericDAOHibernate<ToaPrsnfldrShare, String>(sessionFactory,ToaPrsnfldrShare.class);
		configDao  = new GenericDAOHibernate<ToaPrsnfldrConfig, String>(sessionFactory,ToaPrsnfldrConfig.class);
		commonFolderDao  = new GenericDAOHibernate<ToaPrsnfldrFolder, String>(sessionFactory,ToaPrsnfldrFolder.class);
	}

	@Transactional(readOnly=true)
	public User getCurrentUser() throws SystemException,ServiceException{
		try {
			return userService.getCurrentUser();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"当前用户信息"});
		}
	}
	
	@Transactional(readOnly=true)
	public String getUserName() throws SystemException,ServiceException{
		try {
			return userService.getUserNameByUserId(getCurrentUser().getUserId());
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"当前用户信息"});
		}
	}
	
	@Transactional(readOnly=true)
	public String getUserName(String userId) throws SystemException,ServiceException{
		try {
			return userService.getUserNameByUserId(userId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"当前用户信息"});
		}
	}
	
	/**
	 * 取出所有父节点为0的文件夹。
	 * @return List<ToaPrivatePrsnfldrFolder>
	 * @roseuid 493C874A0196
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrivatePrsnfldrFolder> getAllFolders() throws SystemException,ServiceException {
		try {
			String userId = getCurrentUser().getUserId();
			String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.userId=? and folder.folderParentId=? order by folder.folderSort asc";
			Object[] values = {userId,"0"};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * 生成个人文件柜中文件夹的最大排序号
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String maxFolderSort(String folderParentId)throws SystemException{
		StringBuffer hql = new StringBuffer("select max(t.folderSort)  from ToaPrivatePrsnfldrFolder t where t.folderParentId=?");
		List<String> lst = null;
		String folderSort = "500";
		lst = folderDao.find(hql.toString(),new Object[]{folderParentId});
		if(lst!=null && lst.size()>0){//找到记录
			folderSort = lst.get(0);
			if(!"".equals(folderSort) && null!=folderSort){//记录不为空
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
	 * 生成个人文件柜中文件夹的最大排序号
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String minFolderSort(String folderParentId)throws SystemException{
		StringBuffer hql = new StringBuffer("select min(t.folderSort)  from ToaPrivatePrsnfldrFolder t  where t.folderParentId=?");
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
	 * author:dengzc
	 * description:获取用户的文件夹列表,用于获取用户文件大小
	 * modifyer:
	 * description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrivatePrsnfldrFolder> getAllPrivateFolders() throws SystemException,ServiceException{
		try {
			String userId = getCurrentUser().getUserId();
			String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.userId=?";
			Object[] values = {userId};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	
	/**
	 * 获取个人文件夹下的子文件夹
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrivatePrsnfldrFolder> getAllSubFolders(String folderId) throws SystemException,ServiceException{
		try {
			String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.folderParentId=? order by folder.folderSort asc";
			Object[] values = {folderId};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	
	/**
	 * 获取所有共享的文件夹(isShare=YES)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrivatePrsnfldrFolder> getAllShareFolders() throws SystemException,ServiceException{
		try {
			String userId = userService.getCurrentUser().getUserId();//当前用户ID
			String hql = "from ToaPrsnfldrShare as t where t.userId=? or t.userId='allPeople'";
			Object[] values = {userId};
			List<ToaPrsnfldrShare> shareLst = folderDao.find(hql, values);
			List<ToaPrivatePrsnfldrFolder> newLst = new ArrayList<ToaPrivatePrsnfldrFolder>();
			for(Iterator<ToaPrsnfldrShare> it=shareLst.iterator();it.hasNext();){
				newLst.add(it.next().getToaPrsnfldrFolder());
			}
			return newLst;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * 获取共享文件夹下的子文件夹
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrivatePrsnfldrFolder> getAllShareSubFolders(String folderId) throws SystemException,ServiceException{
		try {
			String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.folderParentId=?  order by folder.folderSort asc";
			Object[] values = {folderId};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	
	/**
	 * @param id
	 * @roseuid 493C8B62030D
	 */
	public boolean deleteFolder(Object obj) throws SystemException,ServiceException {
		try {
			folderDao.getSession().delete(obj);
			return true;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * @param toaPrsnfldrPrivateFolder
	 * @return com.strongit.oa.prsnfldr.privateprsnfldr.ToaPrivatePrsnfldrFolder
	 * @roseuid 493C9112030D
	 */
	public ToaPrivatePrsnfldrFolder addFolder(ToaPrivatePrsnfldrFolder toaPrsnfldrPrivateFolder) throws SystemException,ServiceException {
		try {
			folderDao.save(toaPrsnfldrPrivateFolder);
			return null;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * author:dengzc
	 * description:更新文件夹信息
	 * modifyer:
	 * description:
	 * @param flag:标示操作是增加还是更新共享信息
	 * @param userId:共享接收人
	 * @param prop:共享属性
	 * @param toaPrsnfldrPrivateFolder
	 */
	public void updateFolder(ToaPrivatePrsnfldrFolder toaPrsnfldrPrivateFolder,String flag,String userId,String[] prop) throws SystemException,ServiceException{
		try {
			if(null!=flag && null!=userId && prop!=null){
				String[] userIds = userId.split(",");
				if("I".equals(flag)){
					/*if("allPeople".equals(userId)){//共享给所有人
						userIds = getAllOrgUser();
					}*/
					for(int i=0;i<userIds.length;i++){
						for(int j=0;j<prop.length;j++){
							ToaPrsnfldrShare share = new ToaPrsnfldrShare();
							share.setUserId(userIds[i]);
							share.setSharePrivilege(prop[j]);
							share.setToaPrsnfldrFolder(toaPrsnfldrPrivateFolder);
							shareDao.save(share);
						}
					}
				}else if("U".equals(flag)){//先删除后添加
					Set<ToaPrsnfldrShare> set = toaPrsnfldrPrivateFolder.getToaPrsnfldrShares();
					for(Iterator<ToaPrsnfldrShare> it=set.iterator();it.hasNext();){
						shareDao.delete(it.next());
					}
					if(!"".equals(userId)){
						for(int i=0;i<userIds.length;i++){
							for(int j=0;j<prop.length;j++){
								ToaPrsnfldrShare share = new ToaPrsnfldrShare();
								share.setUserId(userIds[i]);
								share.setSharePrivilege(prop[j]);
								share.setToaPrsnfldrFolder(toaPrsnfldrPrivateFolder);
								shareDao.save(share);
							}
						}				
					}
				}
			}else {
				if("D".equals(flag)){
					Set<ToaPrsnfldrShare> set = toaPrsnfldrPrivateFolder.getToaPrsnfldrShares();
					for(Iterator<ToaPrsnfldrShare> it=set.iterator();it.hasNext();){
						shareDao.delete(it.next());
					}
				}	
			}
			folderDao.update(toaPrsnfldrPrivateFolder);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件夹"});
		}
	}
	
	/**
	 * @param id
	 * @return com.strongit.oa.prsnfldr.privateprsnfldr.ToaPrivatePrsnfldrFolder
	 * @roseuid 493C932201A5
	 */
	@Transactional(readOnly=true)
	public ToaPrivatePrsnfldrFolder getFolderById(String id) throws SystemException,ServiceException {
		try {
			return folderDao.findById(id, true);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	
	/**
	 * 获取文件柜
	 * 包括个人文件柜和公共文件柜，现在此方法主要用于文件的粘贴
	 * 文件粘贴类似操作系统的粘贴。可以从共享区粘贴到本地。也可以
	 * 将文件复制到公共文件柜.
	 * 补充:在验证文件夹下是否有文件也用到此方法
	 * @see(com.strongit.oa.prsnfldr.privateprsnfldr.PrsnfldrFileAction.parse())
	 * @see(com.strongit.oa.prsnfldr.privateprsnfldr.PrsnfldrFolderAction.initDelete())
	 */
	@Transactional(readOnly=true)
	public ToaPrsnfldrFolder getWholeFolderById(String id) throws SystemException,ServiceException{
		try {
			return (ToaPrsnfldrFolder)folderDao.getSession().get(ToaPrsnfldrFolder.class, id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}

	/**
	 * author:dengzc
	 * description:获取所有用户ID
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 */
	@Transactional(readOnly=true)
	public String[] getAllOrgUser()throws SystemException{
		List<Organization> lstOrg = userService.getAllDeparments();
		List<String> newLst = new ArrayList<String>();
		for(Organization org:lstOrg){
			List<TUumsBaseUser> setUser = userService.getUserListByOrgId(org.getOrgId());
			for(TUumsBaseUser user:setUser){
				newLst.add(user.getUserId());
			}
		}
		return newLst.toArray(new String[newLst.size()]);
	}
	
	/**
	 * author:dengzc
	 * description:获取分配给用户的文件柜空间大小
	 * modifyer:
	 * description:
	 * @return
	 */
	public double getUserSpace() throws SystemException,ServiceException{
		try {
			String userId = userService.getCurrentUser().getUserId();
			String hql = "select t.configSize from ToaPrsnfldrConfig as t where t.userId=?";
			Object[] values = {userId};
			List lst = configDao.find(hql, values);
			if(lst.size()>0){
				String configSize = lst.get(0).toString();
				double temp = 0;
				if(configSize.endsWith("k")){//如果此文件单位为K
					temp = Double.parseDouble(configSize.substring(0,configSize.indexOf("k")))*1024; 
				}else if(configSize.endsWith("字节")){
					temp = Double.parseDouble(configSize.substring(0,configSize.indexOf("字节")));
				}else if(configSize.endsWith("MB")){
					temp = Double.parseDouble(configSize.substring(0,configSize.indexOf("MB")))*(1024*1024);
				}
				return temp;
			}else{
				return Double.parseDouble(getDefaultUserSpace()[0].toString());//未分配空间
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"用户文件柜信息"});
		} 
	}
	
	/**
	 * author:dengzc
	 * description:获取分配给用户的文件柜空间大小
	 * modifyer:
	 * description:
	 * @return
	 */
	public String getUserSpaceStr() throws SystemException,ServiceException{
		try {
			String userId = userService.getCurrentUser().getUserId();
			String hql = "select t.configSize from ToaPrsnfldrConfig as t where t.userId=?";
			Object[] values = {userId};
			List lst = configDao.find(hql, values);
			if(lst.size()>0){
				String configSize = lst.get(0).toString();
				return configSize;
			}else{
				return getDefaultUserSpace()[1].toString();//未分配空间
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"用户文件柜信息"});
		} 
	}
	
	/**
	 * 获取某文件夹下所有子文件夹(在删除文件夹时用到)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaPrsnfldrFolder> getAllSubFolder(String folderId) throws SystemException,ServiceException{
		try {
			String hql = "from ToaPrsnfldrFolder as folder where folder.folderParentId=?";
			Object[] values = {folderId};
			return folderDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		} 
	}

	/**
	 * author:dengzc
	 * description:获取默认的文件柜大小
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Object[] getDefaultUserSpace()throws SystemException,ServiceException{
		Object[] size = new Object[2];
		/*try{
			Integer folderSize = themeManager.getTheme().getBaseFolderSize();//默认的文件柜大小
			if(null!=folderSize && !"".equals(folderSize)){
				size[0] = folderSize.intValue()*(1024*1024);//字节单位
				size[1] = folderSize.intValue()+"MB";
			}else{
				size[0] = 0;
				size[1] = "0MB";
			}
			return size;
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"用户默认空间"});
		}*/
		String folderSize = paramConfigService.getConfigSize(ConfigModule.PRSNFLDR);
		if(folderSize != null && !"".equals(folderSize)){
			Integer intSize = Integer.parseInt(folderSize);
			size[0] = intSize.intValue()*(1024*1024);//字节单位
			size[1] = intSize.intValue()+"MB";
		}else{
			size[0] = 0;
			size[1] = "0MB";
		}
		return size;
	}

	/**
	 * 验证个人文件柜下文件夹重名情况
	 * @author:邓志城
	 * @date:2009-10-14 下午04:29:05
	 * @param folderName
	 * @return
	 */
	public boolean isFolderNameUsed(String parentFolder,String folderName) throws ServiceException{
		try{
			User user = getCurrentUser();
			Assert.notNull(user,"无法得到当前用户。");
			Assert.hasText(folderName, "folderName不能为空。");
			if(parentFolder == null || "".equals(parentFolder)){
				parentFolder = "0";
			}
			String userId = user.getUserId();
			String hql = "select count(*) from ToaPrivatePrsnfldrFolder t where t.folderParentId=? and t.folderName=? and t.userId=?";
			Long count = folderDao.findLong(hql, parentFolder,folderName,userId);
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
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	/******************************* 框内为webservice 借口调用方法  begin**********************************************/
	/**
	 * 获取个人文件柜 文件夹
	 * author  taoji
	 * @param page
	 * @param userId
	 * @return
	 * @date 2014-1-9 下午02:17:11
	 */
	public Page<ToaPrivatePrsnfldrFolder> getDocFolderList(Page<ToaPrivatePrsnfldrFolder> page,
			String userId,String folderName){
		String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.folderParentId='0' and folder.userId=? ";
		Object[] values = new Object[10];
		int i=0;
		values[i++]=userId;
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
		page = folderDao.find(page,hql, obj);
		return page;
	}
	/**
	 * 根据文件夹id获取子文件夹
	 * author  taoji
	 * @param page
	 * @param userId
	 * @return
	 * @date 2014-1-9 下午02:17:11
	 */
	public Page<ToaPrivatePrsnfldrFolder> getFolderListById(Page<ToaPrivatePrsnfldrFolder> page,
			String folderId,String folderName){
		String hql = "from ToaPrivatePrsnfldrFolder as folder where folder.folderParentId=? ";
		List paramList=new ArrayList();
		paramList.add(folderId);
		if(folderName!=null&&!"".equals(folderName)){
			hql = hql + " and folder.folderName like ? ";
			paramList.add("%"+folderName+"%");
		}
		if("1".equals(page.getOrderBy())){
			hql = hql + " order by folder.folderCreateDatetime "+ page.getOrder();
		}else{
			hql = hql + " order by folder.folderSort asc ";
		}
		page = folderDao.find(page,hql, paramList.toArray());
		return page;
	}
	
	/**
	 * 根据文件夹id获取子文件夹
	 * author  taoji
	 * @param page
	 * @param userId
	 * @return
	 * @date 2014-1-9 下午02:17:11
	 */
	public Page<ToaPrsnfldrFolder> getCommonFolderListById(Page<ToaPrsnfldrFolder> page,
			String folderId,String folderName){
		String hql = "from ToaPrsnfldrFolder as folder where folder.folderParentId=? ";
		List paramList=new ArrayList();
		paramList.add(folderId);
		if(folderName!=null&&!"".equals(folderName)){
			hql = hql + " and folder.folderName like ? ";
			paramList.add("%"+folderName+"%");
		}
		if("1".equals(page.getOrderBy())){
			hql = hql + " order by folder.folderCreateDatetime "+ page.getOrder();
		}else{
			hql = hql + " order by folder.folderSort asc ";
		}
		page = commonFolderDao.find(page,hql, paramList.toArray());
		return page;
	}
	/**
	 * 根据个人文件夹id  获取  文件夹下的文件数量
	 * author  taoji
	 * @param FolderId
	 * @return
	 * @date 2014-1-9 上午11:37:19
	 */
	public int getFolderFileNum(String FolderId){
		Page<ToaPrsnfldrFile> page = new Page<ToaPrsnfldrFile>();
		page = pffManager.getFiles(page,FolderId,null);
		if(page!=null){
			if(page.getResult()!=null){
				return page.getResult().size();
			}
		}
		return 0;
	}
	/**
	 * 根据个人文件夹id  获取文件夹下 所有文件
	 * author  taoji
	 * @param page
	 * @param FolderId  
	 * @param fileName
	 * @param beginDate
	 * @param endDate
	 * @param smallSize
	 * @param bigSize
	 * @return
	 * @date 2014-1-9 下午07:34:04
	 */
	public Page<ToaPrsnfldrFile> getFolderFileList(Page<ToaPrsnfldrFile> page,String folderId,String fileTitle){
		page = pffManager.getFilesByWeb(page,folderId,fileTitle);
		List<ToaPrsnfldrFile> sortList = new ArrayList<ToaPrsnfldrFile>();
		if(page!=null&&page.getTotalCount()>0){
			for(ToaPrsnfldrFile t :page.getResult()){
				String temp = t.getFileSize();
				if(temp.endsWith("MB")){
					if(temp.length()>2){
						t.setTmpFileSortNo(1024*1024*Double.parseDouble(temp.substring(0,temp.length()-2)));
					}else{
						t.setTmpFileSortNo(0.0);
					}
				}else if(temp.endsWith("字节")){
					if(temp.length()>2){
						t.setTmpFileSortNo(Double.parseDouble(temp.substring(0,temp.length()-2)));
					}else{
						t.setTmpFileSortNo(0.0);
					}
				}else if(temp.endsWith("k")){
					if(temp.length()>2){
						Double tmpD=1024*Double.parseDouble(temp.substring(0,temp.length()-2));
						t.setTmpFileSortNo(tmpD);
					}else{
						t.setTmpFileSortNo(0.0);
					}
				}
				sortList.add(t);
			}
			if("size".equals(page.getOrderBy())){
				if("desc".equals(page.getOrder())){
					Collections.sort(sortList,new Comparator<ToaPrsnfldrFile>(){

						public int compare(ToaPrsnfldrFile o1,
								ToaPrsnfldrFile o2) {
							// TODO Auto-generated method stub
							return o2.getTmpFileSortNo().compareTo(o1.getTmpFileSortNo());
						}
						
					});
				}else{
					Collections.sort(sortList,new Comparator<ToaPrsnfldrFile>(){

						public int compare(ToaPrsnfldrFile o1,
								ToaPrsnfldrFile o2) {
							// TODO Auto-generated method stub
							return o1.getTmpFileSortNo().compareTo(o2.getTmpFileSortNo());
						}
						
					});
				}
			}
			page.setResult(sortList);
		}
		/*List<ToaPrsnfldrFile> list1 = new ArrayList<ToaPrsnfldrFile>();
		List<ToaPrsnfldrFile> list2 = new ArrayList<ToaPrsnfldrFile>();
		List<ToaPrsnfldrFile> list3 = new ArrayList<ToaPrsnfldrFile>();
		if(page!=null&&page.getTotalCount()>0){
			for(ToaPrsnfldrFile t :page.getResult()){
				String temp = t.getFileSize();
				if("MB".equals(temp.substring(temp.length()-2, temp.length()))){
					list1.add(t);
				}else if("字节".equals(temp.substring(temp.length()-2, temp.length()))){
					list3.add(t);
				}else{
					list2.add(t);
				}
			}
			List<ToaPrsnfldrFile> tempList = new ArrayList<ToaPrsnfldrFile>();
			//排序
			if(list1!=null&&list1.size()>0){
				for(int i=0;i<list1.size();i++){
					for(int j=i+1;j<list1.size();j++){
						String temp = list1.get(i).getFileSize();
						String temp1 =  temp.substring(0,temp.length()-2);
						temp = list1.get(j).getFileSize();
						String temp2 = temp.substring(0,temp.length()-2);
						if("desc".equals(page.getOrder())){
							if(Double.parseDouble(temp1)<Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list1.get(i);
								list1.set(i, list1.get(j));
								list1.set(j, t);
							}
						}else{
							if(Double.parseDouble(temp1)>Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list1.get(i);
								list1.set(i, list1.get(j));
								list1.set(j, t);
							}
						}
					}
				}
			}
			//排序
			if(list2!=null&&list2.size()>0){
				for(int i=0;i<list2.size();i++){
					for(int j=i+1;j<list2.size();j++){
						String temp = list2.get(i).getFileSize();
						String temp1 =  temp.substring(0,temp.length()-1);
						temp = list2.get(j).getFileSize();
						String temp2 = temp.substring(0,temp.length()-1);
						if("desc".equals(page.getOrder())){
							if(Double.parseDouble(temp1)<Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list2.get(i);
								list2.set(i, list2.get(j));
								list2.set(j, t);
							}
						}else{
							if(Double.parseDouble(temp1)>Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list2.get(i);
								list2.set(i, list2.get(j));
								list2.set(j, t);
							}
						}
					}
				}
			}
			//排序
			if(list3!=null&&list3.size()>0){
				for(int i=0;i<list3.size();i++){
					for(int j=i+1;j<list3.size();j++){
						String temp = list3.get(i).getFileSize();
						String tempNext=list3.get(j).getFileSize();
						
						String temp1 = "0";
						if( temp.length()>2){
							temp1 =  temp.substring(0, temp.length()-2);
						}
						
						
						String temp2 = "0";
						if(tempNext.length()>2){
							temp2=tempNext.substring(0, tempNext.length()-2);
						}
						
						if("desc".equals(page.getOrder())){
							if(Double.parseDouble(temp1)<Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list3.get(i);
								list3.set(i, list3.get(j));
								list3.set(j, t);
							}
						}else{
							if(Double.parseDouble(temp1)>Double.parseDouble(temp2)){
								ToaPrsnfldrFile t = new ToaPrsnfldrFile();
								t = list3.get(i);
								list3.set(i, list3.get(j));
								list3.set(j, t);
							}
						}
					}
				}
			}
			if("desc".equals(page.getOrder())){
				for(ToaPrsnfldrFile t :list1){
					tempList.add(t);
				}
				for(ToaPrsnfldrFile t :list2){
					tempList.add(t);
				}
				for(ToaPrsnfldrFile t :list3){
					tempList.add(t);
				}
			}else{
				for(ToaPrsnfldrFile t :list3){
					tempList.add(t);
				}
				for(ToaPrsnfldrFile t :list2){
					tempList.add(t);
				}
				for(ToaPrsnfldrFile t :list1){
					tempList.add(t);
				}
			}
			page.setResult(sortList);
		}*/
		return page;
	}
	/**
	 * 根据文件id 删除文件
	 * author  taoji
	 * @param fileId
	 * @date 2014-1-9 下午08:02:45
	 */
	public void delFile(String fileId){
		pffManager.deleteFile(fileId);
	}
	/**
	 * 根据文件id 获取文件对象
	 * author  taoji
	 * @param fileId
	 * @return
	 * @date 2014-1-10 下午01:53:06
	 */
	public ToaPrsnfldrFile getFile(String fileId){
		return pffManager.getFileById(fileId);
	}
	/**
	 * 获取文件前一个文件id  或 后一个文件id
	 * author  taoji
	 * @param folderId  文件夹id
	 * @param fileId	文件id
	 * @param order		按什么排序   date  or  size
	 * @param mark		前一个id 还是后一个id   ago  or  After
	 * @return
	 * @date 2014-1-11 下午04:07:52
	 */
	public String getFileId(String folderId,String fileId,String order,String orderBy,String mark){
		Page<ToaPrsnfldrFile> page = new Page(1000,true);
		page.setOrder(order);
		page.setOrderBy(orderBy);
		page = getFolderFileList(page, folderId, "");
		int m=0;
		if(page!=null&&page.getResult().size()>0){
			List<ToaPrsnfldrFile> list = page.getResult();
			for(int k=0;k<list.size();k++){
				if(list.get(k).getFileId().equals(fileId)){
					m=k;
					break;
				}
			}
			if("ago".equals(mark)){
				if(m==0){
					return null;
				}else{
					return list.get(m-1).getFileId();
				}
			}else{
				if(m==list.size()-1){
					return null;
				}else{
					return list.get(m+1).getFileId();
				}
			}
		}
		return null;
	}
	/**
	 * 获取所有共享的文件夹(isShare=YES)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaPrsnfldrShare> getShareFolders(Page<ToaPrsnfldrShare> page,
			String userId,String folderName) throws SystemException,ServiceException{
		try {
			String hql = "from ToaPrsnfldrShare as t where t.toaPrsnfldrFolder.folderParentId='0' and t.userId=? or t.userId='allPeople'";
			Object[] values = new Object[10];
			int i=0;
			values[i++]=userId;
			if(folderName!=null&&!"".equals(folderName)){
				hql = hql + " and t.toaPrsnfldrFolder.folderName like ? ";
				values[i++]="%"+folderName+"%";
			}
			if("1".equals(page.getOrderBy())){
				hql = hql + " order by t.toaPrsnfldrFolder.folderCreateDatetime "+ page.getOrder();
			}else{
				hql = hql + " order by t.toaPrsnfldrFolder.folderSort asc ";
			}
			Object[] obj = new Object[i];
			for(int j=0;j<i;j++){
				obj[j]=values[j];
			}
			page = shareDao.find(page ,hql, obj);
			return page;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件夹"});
		}
	}
	/******************************* 框内为webservice 借口调用方法  end**********************************************/
}
