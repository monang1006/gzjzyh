package com.strongit.oa.noticeconference;

import java.util.List;

import com.strongit.oa.bo.TOmConType;
import com.strongmvc.orm.hibernate.Page;

public interface INoticeConferenceType {

	/***************************************************************************
	 * 
	 * 方法简要描述 获取所有的信息page
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param name
	 * @param demo
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConType> getPage(Page<TOmConType> page, String name,
			String demo);
	
	/***************************************************************************
	 * 
	 * 方法简要描述 通过名字查找 会议类型 返回 list
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public List<TOmConType> getListByName(String name);
	/***************************************************************************
	 * 
	 * 方法简要描述 获取所有的信息list
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public List<TOmConType> getList();
	
	/***************************************************************************
	 * 
	 * 方法简要描述 保存添加的会议通知类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @param model
	 * @version 1.0
	 * @see
	 */
	public void add(TOmConType model) ;
	
	/***
	 * 
	* 方法简要描述：删除一个会议通知类型
	*  <br>在删除会议通知类型前，需判断是否被使用了，如果没哟使用可以删除，若已经使用，不能删除。
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 25, 2013
	* @Author 万俊龙
	* @param ids
	* @return
	* @version	1.0
	* @see
	 */
	@SuppressWarnings("unchecked")
	public String del(String[] ids) ;
	/***************************************************************************
	 * 
	 * 方法简要描述 更新一个会议类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @param model
	 * @version 1.0
	 * @see
	 */
	public void edit(TOmConType model) ;
	
	/***************************************************************************
	 * 
	 * 方法简要描述:判断是否存在该议题类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 25, 2013
	 * @Author 万俊龙
	 * @param id
	 * @return
	 * @version 1.0
	 * @see
	 */
	public boolean isExit(String id);
}
