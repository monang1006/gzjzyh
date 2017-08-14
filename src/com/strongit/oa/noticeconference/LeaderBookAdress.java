/*package com.strongit.oa.noticeconference;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.ToaSeatsetDep;
import com.strongit.oa.bo.ToaSeatsetPerson;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

*//***
 * 领导名册接口
 * 
 * @Description:  LeaderBookAdress.java
 * @Date:Apr 23, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 *//*
@Service
@Transactional(readOnly = true)
@OALogger
public class LeaderBookAdress extends BaseManager implements ILeaderBookAdress {
	private GenericDAOHibernate<ToaSeatsetPerson, String> personDao;
	private GenericDAOHibernate<ToaSeatsetDep, java.lang.String> depDao;

	@Autowired
	private IDictService dictService;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		personDao = new GenericDAOHibernate<ToaSeatsetPerson, String>(
				sessionFactory, ToaSeatsetPerson.class);

		depDao = new GenericDAOHibernate<ToaSeatsetDep, String>(sessionFactory,
				ToaSeatsetDep.class);
	}

	*//***************************************************************************
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
	 *//*
	@SuppressWarnings("unchecked")
	public List<ToaSeatsetDep> getAllDepInfos() {
		StringBuffer hql = new StringBuffer();
		hql
				.append("from ToaSeatsetDep t where t.depIsDeleted = 0 order by t.code");
		List<ToaSeatsetDep> list = depDao.find(hql.toString());
		return list;
	}
	
	*//***
	 * 
	* 方法简要描述:获取当前机构及子机构
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 17, 2013
	* @Author 万俊龙
	* @param pid
	* @return
	* @version	1.0
	* @see
	 *//*
	@Transactional(readOnly = true)
	public   List<ToaSeatsetDep> getSelfAndChildrenDeptsByPid(String id) {
		Assert.notNull(id);
		List<ToaSeatsetDep> deptList=new ArrayList();
		ToaSeatsetDep currentDetp=this.depDao.get(id);
		if(StringUtil.isNotEmpty(currentDetp)){
			//currentDetp.setParentId("0");
			deptList.add(currentDetp);
			List<ToaSeatsetDep> childrenLst=this.findChildrenDepts(currentDetp.getCode());
			if(StringUtil.isNotEmpty(childrenLst)){
				deptList.addAll(childrenLst);				
			}
		}
		return deptList;
	}
	
	*//***
	 * 
	* 方法简要描述：根据机构编码，获取指定编码的所有子节点
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 17, 2013
	* @Author 万俊龙
	* @param code
	* @return
	* @version	1.0
	* @see
	 *//*
	private List<ToaSeatsetDep>  findChildrenDepts(String code){
		List paramList=new ArrayList();
		StringBuffer hql = new StringBuffer();
		hql
				.append("from ToaSeatsetDep t where t.depIsDeleted = 0 ");
		if(StringUtil.isNotEmpty(code)){
			hql.append(" and  t.code like ?");
			paramList.add(code+"%");
		}		
		hql.append("order by t.code");
		List<ToaSeatsetDep> list = depDao.find(hql.toString(),paramList.toArray());
		return list;
	}
	
	
	 

	*//***************************************************************************
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
	 *//*
	public Page<Object[]> getAddressList(Page<Object[]> page, String sysCode,
			String userName) {
		StringBuffer hql = new StringBuffer();
		hql
				.append("select t.personId,t.personName,t.personPost,t.personSax,t.personNation,t.personMobile1,t.personHomephone,t.personPhone,t.baseDep.depFullName");
		hql.append(" from ToaSeatsetPerson t,ToaSeatsetDep o");
		hql
				.append(" where t.baseDep.depId=o.depId and (t.personIsDeleted is null or t.personIsDeleted=0)");
		hql.append(" and o.depIsDeleted=0");

		if (sysCode != null && !"".equals(sysCode)) {
			hql.append(" and o.code like '" + sysCode + "%'");
		}
		if (userName != null && !"".equals(userName)) {
			hql.append(" and t.personName like '%");
			hql.append(userName);
			hql.append("%'");
		}

		hql.append(" order by o.code ");

		 
		List<Object[]> result = personDao.find(hql.toString());
		if(StringUtil.isNotEmpty(result)){
			for(Object []objs:result){
				String 	nation=this.dictService.getDictItemName(objs[4].toString()); 
				if(StringUtil.isEmpty(nation)){
					nation="汉族";
				}
				objs[4]=nation;
				
				if("1".equals(objs[3].toString())){
					objs[3]="女";
				}else{
					objs[3]="男";
				}
				
				
			}
		}

		page = ListUtils.splitList2Page(page, result);
		return page;
	}

	*//***************************************************************************
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
	 *//*
	public Page<ToaSeatsetPerson> getPersonByOrg(Page<ToaSeatsetPerson> page,
			 String sysCode,String userName) throws SystemException,
			ServiceException {
		try {
			List paramLst = new ArrayList();
			StringBuffer hql = new StringBuffer();
			hql
					.append("select t.personId,t.personName,t.personPost,t.personSax,t.personNation,t.personMobile1,t.personHomephone,t.personPhone");
			hql
					.append(" from ToaSeatsetPerson t,ToaSeatsetDep o");
			hql
					.append(" where t.baseDep.depId=o.depId and (t.personIsDeleted is null or t.personIsDeleted=?)");
			paramLst.add("0");
			
			if(StringUtil.isNotEmpty(sysCode)){
				hql.append(" and o.code like ?");
				paramLst.add("%"+sysCode+"%");
				
			}
			if(StringUtil.isNotEmpty(userName)){
				hql.append(" and t.personName like ? ");
				paramLst.add("%"+userName+"%");
			}
			hql.append("order by t.personNum");
			page = personDao.find(page, hql.toString(), paramLst.toArray());

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return page;
	}
}
*/