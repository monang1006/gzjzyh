package com.strongit.oa.noticeconference;

import java.util.List;

import org.omg.CORBA.SystemException;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;

/*******************************************************************************
 * 领导通讯录接口
 * 
 * @Description: ILeaderBookAdress.java
 * @Date:Apr 12, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
public interface ILeaderBookAdress {

	/***************************************************************************
	 * 
	 * 方法简要描述:获取当前机构及子机构
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 17, 2013
	 * @Author 万俊龙
	 * @param pid
	 * @return
	 * @version 1.0
	 * @see
	 */

	/***************************************************************************
	 * 
	 * 方法简要描述：获取所有的单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")

	/***************************************************************************
	 * 
	 * 方法简要描述 查询系统通讯,支持父级通讯查看子级通讯录人员. 注意：这里是直接操作统一用户BO.
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 12, 2013
	 * @Author 万俊龙
	 * @param page
	 *            分页对象
	 * @param sysCode
	 *            机构编码
	 * @return Object[]{人员id，人员姓名，性别，职务，级别,手机号码，邮件}
	 * @version 1.0
	 * @see
	 */
	public Page<Object[]> getAddressList(Page<Object[]> page, String sysCode,
			String userName);

	/***************************************************************************
	 * 
	 * 方法简要描述：获取人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 15, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param model
	 * @param depId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @version 1.0
	 * @see
	 */
	 
}
