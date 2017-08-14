package com.strongit.oa.attence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendanceRecord;
import com.strongit.oa.bo.ToaAttendanceTime;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author 汤世风
 * @company Strongit Ltd. (C) copyright
 * @date 2012年9月17日
 * @comment 考勤时间设定Manager
 */
@Service
@Transactional
public class AttenceRecordManager extends BaseManager {

	private GenericDAOHibernate<ToaAttendanceRecord, java.lang.String> attenceRecordDao = null;

	// 统一用户接口
	@Autowired
	private IUserService userService;

	public AttenceRecordManager() {
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attenceRecordDao = new GenericDAOHibernate<ToaAttendanceRecord, java.lang.String>(
				sessionFactory, ToaAttendanceRecord.class);
	}

	/**
	 * 根据条件查询考勤信息
	 * @param page
	 * @param userID 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Page<ToaAttendanceRecord> getAttenceList(
			Page<ToaAttendanceRecord> page, String userID, Date startTime,
			Date endTime) {
		
		String hql = "from ToaAttendanceRecord t where 1=1 ";
		List<Object> list = new ArrayList<Object>();

		if (userID != null && !"".equals(userID)) {
			hql = hql + " and t.userId like ? ";
			list.add("%" + userID + "%");
		}
		if (startTime != null && !"".equals(startTime)) {
			hql = hql + " and t.time>=? ";
			list.add(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			hql = hql + " and t.time<=? ";
			list.add(endTime);
		}
		hql = hql + " order by time desc ";
		Object[] obj = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			obj[i] = list.get(i);
		}

		return attenceRecordDao.find(page, hql, obj);

	}
	
	/**
	 * 保存考勤信息
	 * @param record
	 */
	@Transactional(readOnly=false)
	public void save(ToaAttendanceRecord record){
		attenceRecordDao.save(record);
	}
	
	/**
	 * 更新考勤信息
	 * @param record
	 */
	@Transactional(readOnly=false)
	public void update(ToaAttendanceRecord record){
		attenceRecordDao.update(record);
	}
	/**
	 * 保存或更新考勤信息
	 * @param record
	 */
	@Transactional(readOnly=false)
	public void updateOrSave(ToaAttendanceRecord record){
		try {
			if(record.getId()!=null&&!"".equals(record.getId())){
				attenceRecordDao.update(record);
			}else{
				attenceRecordDao.save(record);
			}
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据userID获取此人当天的考勤信息
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public ToaAttendanceRecord getTodayAttenceInfo(String userID) throws Exception{
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = null;
		currentDate = dateFormat.parse(dateFormat.format(new Date()));//util类型时间
		
		String hql = "from ToaAttendanceRecord t where t.time = ? ";
		List<Object> list = new ArrayList<Object>();
		list.add(currentDate);

		if (userID != null && !"".equals(userID)) {
			hql = hql + " and t.userId like ? ";
			list.add("%" + userID + "%");
		}
		
		hql = hql + " order by time desc ";
		Object[] obj = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			obj[i] = list.get(i);
		}
		List<ToaAttendanceRecord> attendanceRecords=attenceRecordDao.find(hql, obj);
		ToaAttendanceRecord record=null;
		if(attendanceRecords.size()>0){
			record=attendanceRecords.get(0);
		}
		
		return record;

	}

}
