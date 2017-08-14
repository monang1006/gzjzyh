package com.strongit.oa.desktop;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopSection;
import com.strongit.oa.bo.ToaDesktopSectionsel;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 对权限可选项进行管理的类
 * @author yhz
 * @version 1.0
 */
@Service
@Transactional
public class DesktopSectionselManager {
	private GenericDAOHibernate<ToaDesktopSectionsel,String> desktopSectionselDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		desktopSectionselDao = new GenericDAOHibernate<ToaDesktopSectionsel,String>(sessionFactory,ToaDesktopSectionsel.class);
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:30:16 PM
	 * @desc: 查询出当前素有可选菜单
	 * @return List<ToaDesktopSectionsel>
	 */
	@Transactional(readOnly=true)
	public List<ToaDesktopSectionsel> findAll(){
		return desktopSectionselDao.find("from ToaDesktopSectionsel as sectionsel order by sectionsel.sectionselOrderby", null);
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:30:16 PM
	 * @desc: 查询出当前素有可选菜单
	 * @return List<ToaDesktopSectionsel>
	 */
	@Transactional(readOnly=true)
	public List<ToaDesktopSectionsel> deskMenuShow(){
		return desktopSectionselDao.find("from ToaDesktopSectionsel as sectionsel order by sectionsel.sectionselOrderby", null);
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:31:18 PM
	 * @desc: 根据权限Code去寻找桌面可选项对象
	 * @param code
	 * @return ToaDesktopSectionsel
	 */
	@Transactional(readOnly=true)
	public ToaDesktopSectionsel findByCode(String code){
		String param[]={code};
		List<ToaDesktopSectionsel> list=desktopSectionselDao.find("from ToaDesktopSectionsel as sectionsel where sectionsel.privilId=?", param);
		if(list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public void saveObj(ToaDesktopSectionsel obj){
		desktopSectionselDao.save(obj);
	}
	
	public void delObj(ToaDesktopSectionsel obj){
		desktopSectionselDao.delete(obj);
	}
}
