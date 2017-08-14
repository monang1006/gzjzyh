package com.strongit.oa.docmonitor.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.docmonitor.service.IDocMonitorService;
import com.strongit.oa.docmonitor.vo.DocMonitorParamter;
import com.strongit.oa.docmonitor.vo.DocMonitorVo;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.work.manager.WorkExtendManager;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
@Service
public class DocMonitorManager implements IDocMonitorService{
	@Autowired
	private IUserService userService; // 工作流服务
	@Autowired
	private ICustomUserService customUserService;
	@Autowired
	private WorkExtendManager workExtendManager; 
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getDLeaderProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(userService.getCurrentUser().getOrgId());
			param.setUserIds(userIds);
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getDLeaderProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(userService.getCurrentUser().getOrgId());
			param.setUserIds(userIds);
			param.setState("1");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getDLeaderProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(userService.getCurrentUser().getOrgId());
			param.setUserIds(userIds);
			param.setState("0");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getDLeaderTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(userService.getCurrentUser().getOrgId());
			param.setUserIds(userIds);
			page = workExtendManager.getTodoWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getPersonalProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getPersonalProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			param.setState("1");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getPersonalProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			param.setState("0");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getPersonalTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			page = workExtendManager.getTodoWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		// TODO Auto-generated method stub
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTLeaderProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getManagerUserIds(userService.getCurrentUser().getUserId());
			param.setUserIds(userIds);
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTLeaderProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getManagerUserIds(userService.getCurrentUser().getUserId());
			param.setUserIds(userIds);
			param.setState("1");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTLeaderProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getManagerUserIds(userService.getCurrentUser().getUserId());
			param.setUserIds(userIds);
			param.setState("0");
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTLeaderTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getManagerUserIds(userService.getCurrentUser().getUserId());
			param.setUserIds(userIds);
			page = workExtendManager.getTodoWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return page;
	}

	/**
	 * 获取分管领导柱状图显示的待办数据
	 * 
	 * @author yanjian
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 4:22:19 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String[]> getManagerDeptTodoDocs(DocMonitorParamter param) throws ServiceException,DAOException, SystemException{
		try {
			List<Object[]> deptList = customUserService.getManageOrgInfo(userService.getCurrentUser().getUserId());
			if(deptList != null && !deptList.isEmpty()){
				LinkedHashMap<String,String[]> map =new LinkedHashMap<String,String[]>(deptList.size());
				if(param == null){
					param = new DocMonitorParamter();
				}
				int size = deptList.size();
				for (int i = size - 1; i >= 0; i--) {
					Object[] dept = deptList.get(i);
					String orgId = StringUtil.castString(dept[0]);
					String orgName = StringUtil.castString(dept[1]);
					List<String> userIds = customUserService.getAllUserIdByOrgId(orgId);
					param.setUserIds(userIds);
					Page<DocMonitorVo> page = new Page<DocMonitorVo>(1,true);
					page = workExtendManager.getTodoWorks(page, param);
					String[] strs = new String[]{orgName,orgId,(page.getTotalCount() < 1?0:page.getTotalCount())+""};
					map.put(orgName, strs);
				}
				return map;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String,String[]> getManagerDeptProcessDocs(DocMonitorParamter param) throws ServiceException,DAOException, SystemException{
		try {
			List<Object[]> deptList = customUserService.getManageOrgInfo(userService.getCurrentUser().getUserId());
			if(deptList != null && !deptList.isEmpty()){
				LinkedHashMap<String,String[]> map =new LinkedHashMap<String,String[]>(deptList.size());
				if(param == null){
					param = new DocMonitorParamter();
				}
				int size = deptList.size();
				for (int i = size - 1; i >= 0; i--) {
					Object[] dept = deptList.get(i);
					String orgId = StringUtil.castString(dept[0]);
					String orgName = StringUtil.castString(dept[1]);
					List<String> userIds = customUserService.getAllUserIdByOrgId(orgId);
					param.setUserIds(userIds);
					Page<DocMonitorVo> page = new Page<DocMonitorVo>(1,true);
					page = workExtendManager.getProcessedWorks(page, param);
					String[] strs = new String[]{orgName,orgId,(page.getTotalCount() < 1?0:page.getTotalCount())+""};
					map.put(orgName, strs);
				}
				return map;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTodoDeptLocaltion(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(param.getGrantOrgId());
			param.setUserIds(userIds);
			page = workExtendManager.getTodoWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		// TODO Auto-generated method stub
		return page;
	}
	
	public Page<DocMonitorVo> getProcessDeptLocaltion(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException {
		try {
			if(param == null){
				param = new DocMonitorParamter();
			}
			List<String> userIds = customUserService.getAllUserIdByOrgId(param.getGrantOrgId());
			param.setUserIds(userIds);
			page = workExtendManager.getProcessedWorks(page, param);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		// TODO Auto-generated method stub
		return page;
	}
	
}
