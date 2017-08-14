/**  
* @title: MobileLogService.java
* @package com.strongit.oa.webservice.verificationService
* @description: TODO
* @author  hecj
* @date Apr 3, 2014 8:28:15 PM
*/


package com.strongit.oa.webservice.verificationService;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TUumsUserandip;
import com.strongit.oa.bo.ToaPrintSet;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @classname: MobileLogService	
 * @author hecj
 * @date Apr 3, 2014 8:28:15 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.verificationService
 * @update
 */
@Service
@Transactional
public class MobileLogService {
	private GenericDAOHibernate<TUumsUserandip, String> mobileLogDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.mobileLogDao = new GenericDAOHibernate<TUumsUserandip, String>(sessionFactory,TUumsUserandip.class);
	}
	/**
	 * 根据
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 3, 2014 8:32:53 PM
	 * @param   loginState
	 * 				登录状态 1成功 0失败
	 * 			userId
	 * 				用户id
	 * @return  Page<TUumsUserandip>
	 * @throws
	 */
	public Page<TUumsUserandip> queryMobileLog(Page page,String userId,String loginState){
		StringBuilder hql=new StringBuilder("from TUumsUserandip t where 1=1 ");
		List<String> paramList=new ArrayList<String>();
		if(userId!=null&&!"".equals(userId)){
			hql.append(" and t.userid=? ");
			paramList.add(userId);
		}
		if(loginState!=null&&!"".equals(loginState)&&!"1".equals(loginState)){
			hql.append(" and t.loginState!='1' ");
			//paramList.add(loginState);
		}else{
			hql.append(" and t.loginState=? ");
			paramList.add(loginState);
		}
		hql.append(" order by t.logintime desc ");
		return mobileLogDao.find(page, hql.toString(), paramList.toArray());
	}
}
