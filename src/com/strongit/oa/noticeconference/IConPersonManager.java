package com.strongit.oa.noticeconference;

import java.util.List;

import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmDeptreport;

/***
 * 参会人员实体表
 * 
 * @Description:  IConPersonManager.java
 * @Date:Apr 7, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
public interface IConPersonManager {
	
	/**
	 * 
	* 方法简要描述：批量保存单位用户上报信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param sendId
	* @param list
	* @version	1.0
	* @see
	 */
	public void saveToReport(String sendId,List<TOmConPerson> list);
	
	/***
	 * 
	* 方法简要描述:新增单位上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param sendId
	* @param entity
	* @version	1.0
	* @see
	 */
	public void saveToReport(String sendId,TOmConPerson entity);
	/***
	 * 
	* 方法简要描述：删除指定单位下待上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param deptId
	* @version	1.0
	* @see
	 */
	public void deleteMembersReport(String deptId);
	
	/***
	 * 
	* 方法简要描述：查找单位待上报的人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param deptId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmDeptreport>  findWaitStateMembersByDeptId(String deptId);
	
	/***
	 * 
	* 方法简要描述：查找人员上报信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param sendId
	* @param state
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmDeptreport> findDeptreport(String sendId,String state);
	/***
	 * 
	* 方法简要描述:根据单位编号，人员姓名，职位及性别，判断用户公共表中是否存在相同的记录
	*	 <br>true:表示存在
	*      false：表示不存在
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 11, 2013
	* @Author 万俊龙
	* @param deptId
	* @param name
	* @param personPost
	* @param sax
	* @return
	* @version	1.0
	* @see
	 */
	public boolean hasPerson(String deptId,String personName,String personPost,String personSax);
	/***
	 * 
	* 方法简要描述:查找单位下上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 10, 2013
	* @Author 万俊龙
	* @param deptId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmConPerson> findByDeptid(String deptId);
	
	/***
	 * 
	* 方法简要描述：保存上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	public void save(TOmConPerson entity);
	
	/***
	 * 
	* 方法简要描述：批量保存上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	public void save(List<TOmConPerson> list);
	
	/***
	 * 
	* 方法简要描述：批量删除上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	public void delete(List<TOmConPerson> list);
	
	/***
	 * 
	* 方法简要描述：删除上报人员信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 */
	public void delete(TOmConPerson entity);
	
}
