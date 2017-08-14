package com.strongit.oa.mylog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * <p>Title: MyLogManager.java</p>
 * <p>Description: 日志信息Manager类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-11-23 18:37:17
 * @version  1.0
 */
@Service
@Transactional
public class MyLogManager {
	
	private GenericDAOHibernate<ToaLog, java.lang.String> myLogDao;
	
	@Autowired
	IUserService userService;// 统一用户服务
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		myLogDao=new GenericDAOHibernate<ToaLog, java.lang.String>(sessionFactory,ToaLog.class);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-23 18:43:17
	 * @des     日志信息保存
	 * @return  boolean
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean saveObj(ToaLog log){
		try{
			myLogDao.save(log);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * @author 胡丽丽
	 * @date 2009-12-15
	 * @des  根据ID删除日志信息
	 * @param id
	 * @return
	 */
	public boolean deleteObj(String id){
		try {
			String hql="update ToaLog t set t.logState='0' where t.logId=?";
			Query query=myLogDao.createQuery(hql, id);
			query.executeUpdate();
			return true;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * @author 胡丽丽
	 * @date  2009-12-15
	 * @des 日志查询
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaLog> getAllToaLog(Page<ToaLog> page) throws SystemException,ServiceException{
		try {
			String hql="from ToaLog t where t.logState='1' order by t.opeTime desc";
			page=myLogDao.find(page, hql);
			return page;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"日志查询"});
		} 
	}
	
	/**
	 * 日志搜索
	 * @author 胡丽丽
	 * @date 2009-12-15
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaLog> search(Page<ToaLog> page,ToaLog model)throws SystemException,ServiceException{
		try {
			String hql="from ToaLog t where t.logState=?";
			List<Object> list=new ArrayList<Object>();
			list.add("1");
			//日志信息
			if(model.getLogInfo()!=null&&!"".equals(model.getLogInfo().trim())){
				hql=hql+" and t.logInfo like ?";
				list.add("%"+model.getLogInfo()+"%");
			}
			//用户ID
			if(model.getOpeIp()!=null&&!"".equals(model.getOpeIp().trim())){
				hql=hql+" and t.opeIp=?";
				list.add(model.getOpeIp());
			}
			//搜索开始时间
			if(model.getBeginTime()!=null){
				hql=hql+" and t.opeTime>=?";
				list.add(model.getBeginTime());
			}
			//搜索结束时间
			if(model.getOpeTime()!=null){
				hql=hql+" and t.opeTime<=?";
				list.add(model.getOpeTime());
			}
			//操作人
			if(model.getOpeUser()!=null&&!"".equals(model.getOpeUser().trim())){
				hql=hql+" and t.opeUser like ?";
				list.add("%"+model.getOpeUser()+"%");
			}
			hql=hql+" order by t.opeTime desc";
			Object[] obj=new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			page=myLogDao.find(page, hql, obj);
			return page;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"日志搜索"});
		}
	}
	/**
	 * 根据ID查询日志
	 * @author 胡丽丽
	 * @date 2009-12-15
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaLog getToaLogByID(String id)throws SystemException,ServiceException{
		try {
			ToaLog log=myLogDao.get(id);
			return log;
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据ID查询日志"});
		}
	}
	
	/**
	* @description 快捷添加一条日志信息
	* @method addLog
	* @author 申仪玲(shenyl)
	* @created 2012-4-12 下午05:10:32
	* @param logInfo
	* @return void
	* @throws Exception
	*/
	public void addLog(String logInfo,String ip){
		ToaLog log = new ToaLog();
		log.setOpeIp(ip); // 操作者IP地址
		String userName = userService.getCurrentUser().getUserName();
		log.setOpeUser(userName); // 操作姓名
		log.setLogState("1"); // 日志状态
		log.setOpeTime(new Date()); // 操作时间
		log.setLogInfo(logInfo.toString());// 日志信息
		this.saveObj(log);
	}
	
}
