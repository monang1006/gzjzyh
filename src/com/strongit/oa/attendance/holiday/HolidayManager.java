package com.strongit.oa.attendance.holiday;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendHoliday;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 法定假日类型manager
 * @author 胡丽丽
 * @createdate:2009-11-13
 *
 */
@Service
@Transactional
public class HolidayManager {

	private GenericDAOHibernate<ToaAttendHoliday, String> holidayDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		holidayDao = new GenericDAOHibernate<ToaAttendHoliday, java.lang.String>(sessionFactory,
				ToaAttendHoliday.class);
	}

	/**
	 * 法定假日保存
	 * @author 胡丽丽
	 * @createdate:2009-11-13
	 * @param model：假日类型
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaAttendHoliday model)throws SystemException,ServiceException{
		try {
			holidayDao.save(model);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] {"法定假日保存出错"});
		}
	}

	/**
	 * 根据ID删除法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-13
	 * @param holidayId： 假日类型ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String holidayId)throws SystemException,ServiceException{
		try {
			holidayDao.delete(holidayId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,
					new Object[]{"法定假日删除出错"});
		}
	}

	/**
	 * 根据ID查询法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-14
	 * @param holidayId： 假日类型ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttendHoliday getHolidayByID(String holidayId)throws SystemException,ServiceException{
		try {
			return holidayDao.get(holidayId);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据ID查询法定假日出错！"});
		}
	}

	/**
	 * 查询所有法定假日
	 * @author 胡丽丽
	 * @cereatedate:2009-11-14
	 * @param page:法定假日page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendHoliday> getAllHoliday(Page<ToaAttendHoliday> page) throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendHoliday t order by holidayStime desc";
			return holidayDao.find(page,hql);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询所有法定假日出错！"});
		}
	}
	/**
	 * 查询所有法定假日
	 * @author 胡丽丽
	 * @cereatedate:2009-11-14
	 * @param page:法定假日列表
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendHoliday> getAllHoliday() throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendHoliday t order by holidayStime desc";
			return holidayDao.find(hql);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询所有法定假日出错！"});
		}
	}
	/**
	 * 查询有效期内的法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-16
	 * @param page
	 * @param beginTime  生效时间
	 * @param endTime 失效时间
	 * @return 返回page对象
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendHoliday> getHolidayByHenableTime(Page<ToaAttendHoliday> page,Date beginTime,Date endTime)throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendHoliday t where t.henableTime>=? and t.hdisableTime<? order by holidayStime desc";
			return holidayDao.find(page,hql,beginTime,endTime);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询有限期内的法定假日出错！"});
		}
	}
	/**
	 * 启用或禁用法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-17
	 * @param isenable
	 * @param holidayids
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void isEnableType(String isenable,String[] holidayids)throws SystemException,ServiceException{
		try {
			String ids="";
			for(int i=0;i<holidayids.length;i++){
				ids=ids+",'"+holidayids[i]+"'";
			}
			ids=ids.substring(1);
			String hql="update ToaAttendHoliday t set t.isEnable=? where t.holidayId in ("+ids+")";
			Query query=holidayDao.createQuery(hql,isenable);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"启用或禁用修改出现"});
		}
	}
	/**
	 * 根据条件搜索法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-17
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendHoliday> search(Page<ToaAttendHoliday> page,ToaAttendHoliday model)throws SystemException,ServiceException{
		String hql="from ToaAttendHoliday t where t.isWholeDay='0'";
		List<Object> list=new ArrayList<Object>();
		if(model.getHolidayName()!=null&&!"".equals(model.getHolidayName())){//假日名城
			hql=hql+" and t.holidayName like ?";
			list.add("%"+model.getHolidayName()+"%");
		}
		if(model.getHolidayStime()!=null&&!"".equals(model.getHolidayStime())){//开始时间
			hql=hql+" and t.holidayStime>=?";
			list.add(model.getHolidayStime());
		}
		if(model.getHolidayEtime()!=null&&!"".equals(model.getHolidayEtime())){//结束时间
			hql=hql+" and t.holidayEtime<=?";
			list.add(model.getHolidayEtime());
		}
		if(model.getHenableTime()!=null){//生效时间
			hql=hql+" and t.henableTime>=?";
			list.add(model.getHenableTime());
		}
		if(model.getHdisableTime()!=null){//失效时间
			hql=hql+" and t.hdisableTime<=?";
			list.add(model.getHdisableTime());
		}
		if(model.getIsEnable()!=null&&!"".equals(model.getIsEnable())){//是否启用
			hql=hql+" and t.isEnable=?";
			list.add(model.getIsEnable());
		}
		hql=hql+" order by holidayStime desc";
		Object[] objs=new Object[list.size()];
		for(int i=0;i<list.size();i++){
			objs[i]=list.get(i);
		}
		return holidayDao.find(page, hql, objs);
	}
	
	/**
	 * 获取某个时间段内有效的法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-25
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendHoliday> getHolidayByStimeAndEtime(Date beginTime,Date endTime)throws SystemException,ServiceException{
		String hql=" from ToaAttendHoliday t where t.isWholeDay='0'";
		List<Object> list=new ArrayList<Object>();
		//有效时间
		hql=hql+" and (t.henableTime>?";
		list.add(endTime);
		//失效时间
		hql=hql+" or  t.hdisableTime<?)";
		list.add(beginTime);

		Object[] objs=new Object[list.size()];
		for(int i=0;i<list.size();i++){
			objs[i]=list.get(i);
		}
		List<ToaAttendHoliday> objlist= holidayDao.find( hql, objs);
		String ids="";
		for(ToaAttendHoliday obj:objlist){
			ids=ids+",'"+obj.getHolidayId()+"'";
		}
		if(ids.length()>0){
			ids=ids.substring(1);
		}
		String newhql="";
		if(!"".equals(ids)){
			newhql="from ToaAttendHoliday t where t.holidayId not in ("+ids+")  and t.isEnable='0'";
		}else{
			newhql="from ToaAttendHoliday t where t.isEnable='0'";
		}
		return holidayDao.find(newhql);
	}
	
	/**
	 * 判断当天是否法定假日
	 * @author 胡丽丽
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean islegalHoliday()throws SystemException,ServiceException{
		try{
			StringBuffer hql=new StringBuffer("select count(t.holidayId) from ToaAttendHoliday t where t.isEnable='0'");
			hql.append(" and  t.holidayStime<=?")
			.append(" and t.holidayEtime>=?")
			.append(" and t.henableTime<=?")
			.append(" and ( t.hdisableTime is null or  t.hdisableTime>=?)");
			Object[] objs=new Object[4];
			Date time=new Date();
			SimpleDateFormat df = new SimpleDateFormat("MM-dd");//设置日期格式  
			String time1=df.format(time);
			objs[0]=time1;//开始时间
			objs[1]=time1;//结束时间
			objs[2]=new Date();//有效时间
			objs[3]=new Date();//失效时间
			Object obj=holidayDao.findUnique(hql.toString(), objs);
			if(obj!=null){
				if(Integer.parseInt(obj.toString())>0){
					return true;
				}
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"判断当天是否为法定假日"});
		}
	}
	
	/**
	 * 获取某个时间段是否已经设置了法定假日
	 * @author 胡丽丽
	 * @createdate:2009-11-25
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getHolidayByTime(String holidayId,String beginTime,String endTime)throws SystemException,ServiceException{
		String hql="from ToaAttendHoliday t where t.isWholeDay='0'";
		List<Object> list=new ArrayList<Object>();
		if(beginTime!=null){//开始时间
			hql=hql+" and t.holidayStime=?";
			list.add(beginTime);
		}
		if(endTime!=null){//结束时间
			hql=hql+" and t.holidayEtime=?";
			list.add(endTime);
		}
		if(holidayId!=null&&!"".equals(holidayId)){
			hql=hql+" and t.holidayId!=?";
			list.add(holidayId);
		}
		Object[] objs=new Object[list.size()];
		for(int i=0;i<list.size();i++){
			objs[i]=list.get(i);
		}
		List<ToaAttendHoliday> holidaylist= holidayDao.find( hql, objs);
		if(holidaylist==null||holidaylist.size()==0){	//没有查询到数据
			return 0;
		}else{	
			ToaAttendHoliday holiday=holidaylist.get(0);
			if("1".equals(holiday.getIsEnable())){		//是否禁用
				return 1;
			}else if(holiday.getHdisableTime()==null||holiday.getHdisableTime().after(new Date())){
				return 3;
			}else{	
				return 2;
			}

		}
	}
}
