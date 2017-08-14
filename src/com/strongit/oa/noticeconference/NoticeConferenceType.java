package com.strongit.oa.noticeconference;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class NoticeConferenceType extends BaseManager implements
		INoticeConferenceType {
	private GenericDAOHibernate<TOmConType, String> dao;

	private GenericDAOHibernate<TOmConference, String> conferDao;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MyLogManager logService;
	@Autowired
	private IUserService userService;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new GenericDAOHibernate<TOmConType, String>(sessionFactory,
				TOmConType.class);

		conferDao = new GenericDAOHibernate<TOmConference, String>(
				sessionFactory, TOmConference.class);

	}

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
			String demo) {
		try {
			StringBuilder hql = new StringBuilder();
			List<Object> parmList = new ArrayList<Object>();
			hql.append("from TOmConType t where 1=1 ");
			if (null != name && !("").equals(name)) {
				if(name.indexOf("%")!=-1){
					name =name.replaceAll("%","/%");
				hql.append("and t.contypeName like '%" + name + "%' ESCAPE '/' ");
				}
				else{
					hql.append("and t.contypeName like ? ");
					parmList.add("%" + name + "%");
				}
			}
			if (null != demo && !("").equals(demo)) {
				hql.append("and t.contypeDemo like ? ");
				parmList.add("%" + demo + "%");
			}
			hql.append("order by contypeName asc");
			page = dao.find(page, hql.toString(), parmList.toArray());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return page;
	}

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
	public List<TOmConType> getListByName(String name) {
		List<TOmConType> list = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from TOmConType t where 1=1 ");
			if (null != name && !("").equals(name)) {
				hql.append("and t.contypeName = ?");
			}
			hql.append("order by contypeName asc");
			list = dao.find(hql.toString(),name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

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
	public List<TOmConType> getList() {
		List<TOmConType> list = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from TOmConType order by contypeName asc");
			list = dao.find(hql.toString());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return list;
	}

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
	public void add(TOmConType model) {
		Assert.notNull(model);
		try {
			dao.save(model);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

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
	public String del(String[] ids){
		String flag ="success";
		int len = 0;
		for(int i =0;i<ids.length;i++){
			String hql = "from TOmConference t where t.tsConfersort.contypeId = ?";
			List<TOmConference> list = dao.find(hql, new Object[]{ids[i]});
			if(list.size()>0)
			{
				flag = "nosuccess";
				return flag;
			}
			else
			{
				len = len +1;
			}
			
		}
		if(len == ids.length){
			dao.delete(ids);
		}
		
		return null;
	}

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
	public void edit(TOmConType model) {
		Assert.notNull(model);
		Assert.notNull(model.getContypeId());
		try {
			TOmConType entity=this.dao.get(model.getContypeId());
			entity.setContypeDemo(model.getContypeDemo());
			entity.setContypeName(model.getContypeName());
			entity.setContypeSequence(model.getContypeSequence());
			entity.setRest1(model.getRest1());
			entity.setRest2(model.getRest2());
			entity.setRest3(model.getRest3());
			dao.update(entity);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

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
	public boolean isExit(String id) {
		Assert.notNull(id);
		TOmConType cs = dao.get(id);
		if (cs == null) {
			return false;
		} else {
			return true;
		}
	}

}
