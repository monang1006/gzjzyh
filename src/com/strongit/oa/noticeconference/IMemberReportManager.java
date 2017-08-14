package com.strongit.oa.noticeconference;

import java.util.List;


public interface IMemberReportManager {

	/***************************************************************************
	 * 
	 * 方法简要描述:根据单位编号，人员姓名，职位及性别，判断领导名册中是否存在相同的记录 <br>
	 * true:表示存在 false：表示不存在 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @param name
	 * @param personPost
	 * @param sax
	 * @return
	 * @version 1.0
	 * @see
	 */
	public boolean hasPerson(String deptId, String personName,
			String personPost, String personSax);
	
	/***************************************************************************
	 * 
	 * 方法简要描述:查找单位下上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 10, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 */
	/*public List<ToaSeatsetPerson> findByDeptid(String deptId) ;*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述：批量保存上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 9, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	/*public void save(List<ToaSeatsetPerson> list) ;*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述：查找人员上报信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param state
	 * @return
	 * @version 1.0
	 * @see
	 */
	/*public List<ToaSeatsetAttendMember> findDeptreport(String sendId,
			String state) ;*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述：查找单位待上报的人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 */
	/*public List<ToaSeatsetAttendMember> findWaitStateMembersByDeptId(
			String deptId) ;*/
	

	/***************************************************************************
	 * 
	 * 方法简要描述：根据下发单位编号he
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param conSendId
	 * @param personId
	 * @return
	 * @version 1.0
	 * @see
	 */
	/*public ToaSeatsetAttendMember getAttendMember(String deptId,
			String seatPersonId) ;*/
	
	/**
	 * 
	 * 方法简要描述：批量保存单位用户上报信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param list
	 * @version 1.0
	 * @see
	 */
	/*public void saveToReport(String sendId, List<ToaSeatsetPerson> list);*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述:新增单位上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	/*public void saveToReport(String sendId, ToaSeatsetPerson entity) ;*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述：保存上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 9, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	/*public void save(ToaSeatsetAttendMember entity);*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述: 获取领导名册用户信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */

	/*public List<ToaSeatsetPerson> findAll();*/
	
	/****
	 * 
	* 方法简要描述：根据领导名册用户信息编号主键，获取实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 24, 2013
	* @Author 万俊龙
	* @param personId
	* @return
	* @version	1.0
	* @see
	 */
	/*public ToaSeatsetPerson getSeatSetPerson(String personId);*/
	
	/***
	 * 
	* 方法简要描述：更新单位上报人员信息表
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 24, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	/*public void updateAttenMember(ToaSeatsetAttendMember entity);*/
	
	/***************************************************************************
	 * 
	 * 方法简要描述：获得领导名册组织机构实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 23, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 */
	/*public ToaSeatsetDep getSeatSetDept(String deptId);*/
	
}
