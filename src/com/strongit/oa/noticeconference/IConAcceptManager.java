package com.strongit.oa.noticeconference;

import java.util.Date;
import java.util.List;

import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongmvc.orm.hibernate.Page;

public interface IConAcceptManager {
	/***
	 * 
	* 方法简要描述：分页查找已签收单位数据集
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 17, 2013
	* @Author 万俊龙
	* @param page
	* @param entity
	* @param beginTime
	* @param endTime
	* @param regendtime
	* @return
	* @version	1.0
	* @see
	 */
	public Page<TOmConferenceSend> queryForReceived(Page<TOmConferenceSend> page,
			TOmConferenceSend entity, Date beginTime, Date endTime,
			Date regendtime) ;
	/***
	 * 
	* 方法简要描述：根据主键，获取上报人员通讯录实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 16, 2013
	* @Author 万俊龙
	* @param personId
	* @return
	* @version	1.0
	* @see
	 */
	public TOmConPerson getPersonById(String personId);
	
	/***
	 * 
	* 方法简要描述:更新下发通知实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 7, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	public void update(TOmConferenceSend entity);
	/***
	 * 
	* 方法简要描述：根据主键，获取实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 7, 2013
	* @Author 万俊龙
	* @param id
	* @return
	* @version	1.0
	* @see
	 */
	public TOmConferenceSend getModel(String id);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：分页查找下发通知表
	 * 
	 * 
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @param state
	 * @param receState
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConferenceSend> queryForConsend(Page<TOmConferenceSend> page,
			TOmConferenceSend entity, Date beginTime, Date endTime,
			Date regendtime, String state);
	/**
	 * 
	 * 方法简要描述：根据会议下发签收主键ID,删除实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param id
	 * @version 1.0
	 * @see
	 */
	public void delete(String id);
	
	/**
	 * 
	 * 方法简要描述：删除会议签发实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void delete(TOmConferenceSend entity) ;
	
	/**
	 * 
	 * 方法简要描述：批量删除会议签发实体对象集
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void delete(List<TOmConferenceSend> list);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议下发实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void save(TOmConferenceSend entity);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：判断在指定会议下，签发单位是否接收
	 * 
	 * @return true:表示签发单位已经签发 false:表示签发单位没有签发
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @version 1.0
	 * @see
	 */
	public boolean checkSeatsetDepIsAccept(String meetid, String depId);
	
	/***************************************************************************
	 * 
	 * 方法简要描述:获取指定会议下签发单位集合
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param meetingId
	 * @param fromOrgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TOmConferenceSend> findByconNoticeId(String conNoticeId);
	
	/***
	 * 
	* 方法简要描述: * 保存签发单位
	 * <br>在保存签发单位前，根据会议编号删除之前的签发单位。再保存最新的签发单位。
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 2, 2013
	* @Author 万俊龙
	* @param conId
	* @param depIds
	* @version	1.0
	* @see
	 */
	public void save(String conId, String depIds,String state) ;
	
	/***
	 * 
	* 方法简要描述: * 保存签发单位
	 * <br>在保存签发单位前，根据会议编号删除之前的签发单位。再保存最新的签发单位。
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 2, 2013
	* @Author 万俊龙
	* @param conId
	* @param depIds
	* @version	1.0
	* @see
	 */
	public void save(String conId, String depIds);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：删除会议下的所有签发单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param meetingId
	 * @version 1.0
	 * @see
	 */
	public void deleteByConId(String conId) ;
	
	/***
	 * 
	* 方法简要描述：更加会议通知编号，获取会议通知实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 2, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public TOmConference getConNoticeByConId(String conId);
	
	/***
	 * 
	* 方法简要描述：获取已发会议通知中所有下派部门信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 10, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmConferenceSend> findIssureConferenceDepts(String conId);
}
