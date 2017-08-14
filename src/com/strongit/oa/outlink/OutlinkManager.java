package com.strongit.oa.outlink;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMail;
import com.strongit.oa.bo.ToaOutlink;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * <p>Title: OutlinkManager.java</p>
 * <p>Description: 外部链接Manager类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-03-22 22:29:46
 * @version  1.0
 */

@Service
@Transactional
public class OutlinkManager {
	
	private GenericDAOHibernate<ToaOutlink,String> outlinkDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		outlinkDao = new GenericDAOHibernate<ToaOutlink,String>(sessionFactory,ToaOutlink.class);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2010-03-24 09:03:32
	 * @des     进行相关的存储操作
	 * @return  boolean
	 */
	public boolean saveObj(ToaOutlink outLink){
		try{
			outlinkDao.save(outLink);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean delObjects(String id){
		String[] ids = id.split(",");
		try{
			outlinkDao.delete(ids);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(readOnly=true)
	public ToaOutlink getObjectById(String id){
		return outlinkDao.get(id);
	}
	
	public  List<ToaOutlink> getShowDeskInfo(String type,Page<ToaOutlink> page){
		Page<ToaOutlink> list=outlinkDao.find(page ,"from ToaOutlink t where t.outlinkType = ? order by t.outlinkDate desc",type);
		return list.getResult();
	}
	
	/**
	 * @author  于宏洲
	 * @date    2010-03-24 09:21:43
	 * @des     根据描述和变动日期来查询记录集
	 * @return  Page<ToaOutlink>
	 */
	@Transactional(readOnly=true)
	public Page<ToaOutlink> getPageList(Page<ToaOutlink> page,String des,Date beginDate,Date endDate){
		
		Object[] values = new Object[3];
		
		String queryStr = "from ToaOutlink t where 1=1";
		
		if(des!=null&&!"".equals(des)){
			queryStr = queryStr + " and t.outlinkDes like ?";
			values[0] = "%"+des+"%";
		}else{
			queryStr = queryStr+" and 1=?";
			values[0] = 1;
		}
		
		if(beginDate!=null){
			queryStr = queryStr + " and t.outlinkDate>=?";
			values[1] = beginDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[1] = 1;
		}
		
		if(endDate!=null){
			queryStr = queryStr + " and t.outlinkDate<=?";
			values[2] = endDate;
		}else{
			queryStr = queryStr + " and 1=?";
			values[2] = 1;
		}
		
		queryStr = queryStr + " order by t.outlinkDate desc";
		
		return outlinkDao.find(page, queryStr, values);
		
	}

}
