package com.strongit.oa.desktop;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopSection;
import com.strongit.oa.bo.ToaDesktopWhole;
import com.strongit.oa.desktop.util.Mycomparator;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 对桌面方案的管理类
 * @author yuhz
 * @version 1.0
 */
@Service
@Transactional
public class DesktopWholeManager {
	
	private GenericDAOHibernate<ToaDesktopWhole,String> desktopWholeDao;
	private DesktopSectionManager sectionManager;
	
	
	

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		desktopWholeDao = new GenericDAOHibernate<ToaDesktopWhole,String>(sessionFactory,ToaDesktopWhole.class);
	}
	
	public ToaDesktopWhole getDesttopWholeById(String id){
		return desktopWholeDao.get(id);
	}
	
	public List<ToaDesktopWhole> getDesktopWholeListById(String id){	
	     List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.portalId=?",id);
	         if(list!=null && list.size()!=0){
	        	 return list;
	         }
	         else 
	        	 return null;
	}
	
	public List<ToaDesktopWhole> getDesktopWholeListNomoren(String id){	
		String[] whole={"0",id};	
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=?",whole);
	         if(list!=null && list.size()!=0){
	        	 return list;
	         }
	         else 
	        	 return null;
	}
	
	public List<ToaDesktopWhole> getWholesNomorenAndUser(String id){	
		String[] whole={"0",id};	
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=? and whole.userId is not null",whole);
	         if(list!=null && list.size()!=0){
	        	 return list;
	         }
	         else 
	        	 return null;
	}
	
	public ToaDesktopWhole getDefaultDesktopWholeisMor(String id){
		String[] whole={"1",id};	
	List<ToaDesktopWhole> list2=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=?",whole);
	return list2.get(0);
		
		
	}
	
	public ToaDesktopWhole getDefaultDesktopWholenoMor(String id,String userid){
		String[] whole={"0",id,userid};
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=? and whole.userId=?", whole);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else
		{
			String[] hole={"1",id};
			List<ToaDesktopWhole> list2=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=?",hole);
			return list2.get(0);
		}
		
	}
	
	public ToaDesktopWhole getDefaultDesktopWholenoMorById(String id,String userid){
		String[] whole={"0",id,userid};
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.isMoren=? and whole.portalId=? and whole.userId=?", whole);
		if(list!=null && list.size()>0){
			return list.get(0);
	}else 
		return null;
	}	
	/**
	 * 根据人员ID获得当前人员的所对应的桌面
	 * @param userId
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public ToaDesktopWhole getDesktopWhole(String userId,String role) throws IllegalAccessException, InvocationTargetException{
		String[] param={userId,"0"};
		
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.userId=? and whole.desktopIsdefault=?", param);
		if(list.size()==0){
			if(role==null)
				role = "0";
			
			ToaDesktopWhole dd=this.getDefaultDesktop(role);
			String testOrder=dd.getDesktopLayout();
			if(testOrder==null||testOrder.equals("")){
				testOrder="";
			}
			ToaDesktopWhole newDesk = new ToaDesktopWhole();
			newDesk.setDesktopId(null);
			newDesk.setUserId(userId);
			newDesk.setDesktopIsdefault("0");
			newDesk.setDesktopCenter(dd.getDesktopCenter());
			newDesk.setDesktopColumn(dd.getDesktopColumn());
			newDesk.setDesktopCtime(dd.getDesktopCtime());
			newDesk.setDesktopLayout(dd.getDesktopLayout());
			newDesk.setDesktopLeft(dd.getDesktopLeft());
			newDesk.setDesktopRight(dd.getDesktopRight());
			newDesk.setDesktopRole(dd.getDesktopRole());
			//saveObjN(newDesk);
			Set sets=dd.getToaDesktopSections();
			Set saveSet=new HashSet();
			Iterator it=sets.iterator();
			while(it.hasNext()){
				ToaDesktopSection section=(ToaDesktopSection)it.next();
				ToaDesktopSection deskSection=new ToaDesktopSection();
				BeanUtils.copyProperties(deskSection, section);
				deskSection.setSectionId(null);
				deskSection.setToaDesktopWhole(newDesk);
				//sectionManager.saveObjR(deskSection);
//				testOrder=testOrder.replace(section.getSectionId(), deskSection.getSectionId());
				saveSet.add(deskSection);
				//sectionManager.flush();
			}
			
			
//			obj.setDesktopLayout(testOrder);
			//ToaDesktopWhole nowDesk=getDesttopWholeById(obj.getDesktopId());
			newDesk.setDesktopLayout(testOrder);
			newDesk.setToaDesktopSections(saveSet);
			saveObjN(newDesk);
			List newList=new ArrayList();
			List oldList=new ArrayList();
			newList.addAll(newDesk.getToaDesktopSections());
			oldList.addAll(dd.getToaDesktopSections());
			Mycomparator comparator=new Mycomparator();
			Collections.sort(newList,comparator);
			Collections.sort(oldList,comparator);
			for(int i=0;i<newList.size();i++){
				ToaDesktopSection  newSection=(ToaDesktopSection)newList.get(i);
				ToaDesktopSection  oldSection=(ToaDesktopSection)oldList.get(i);
				testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
			}
//			Iterator newit=newDesk.getToaDesktopSections().iterator();
//			Iterator oldit=dd.getToaDesktopSections().iterator();
//			while(newit.hasNext()){
//				ToaDesktopSection  newSection=(ToaDesktopSection)newit.next();
//				ToaDesktopSection oldSection=(ToaDesktopSection)oldit.next();
//				testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
//			}
			newDesk.setDesktopLayout(testOrder);
			saveObjN(newDesk);
			return newDesk;
		}else{
			return list.get(0);
		}
	}
	/**
	 *  门户初始为系统默认方案
	 * @param role
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ToaDesktopWhole getDesktopWholeByPortal(String role) throws IllegalAccessException, InvocationTargetException{
		
			if(role==null)
				role = "0";
			
			ToaDesktopWhole dd=this.getDefaultDesktop(role);
			String testOrder=dd.getDesktopLayout();
			if(testOrder==null||testOrder.equals("")){
				testOrder="";
			}
			ToaDesktopWhole newDesk = new ToaDesktopWhole();
			newDesk.setDesktopId(null);
			newDesk.setUserId("null");
			newDesk.setDesktopIsdefault("2");
			newDesk.setDesktopCenter(dd.getDesktopCenter());
			newDesk.setDesktopColumn(dd.getDesktopColumn());
			newDesk.setDesktopCtime(dd.getDesktopCtime());
			newDesk.setDesktopLayout(dd.getDesktopLayout());
			newDesk.setDesktopLeft(dd.getDesktopLeft());
			newDesk.setDesktopRight(dd.getDesktopRight());
			newDesk.setDesktopRole(dd.getDesktopRole());
			//saveObjN(newDesk);
			Set sets=dd.getToaDesktopSections();
			Set saveSet=new HashSet();
			Iterator it=sets.iterator();
			while(it.hasNext()){
				ToaDesktopSection section=(ToaDesktopSection)it.next();
				ToaDesktopSection deskSection=new ToaDesktopSection();
				BeanUtils.copyProperties(deskSection, section);
				deskSection.setSectionId(null);
				deskSection.setToaDesktopWhole(newDesk);
				//sectionManager.saveObjR(deskSection);
			//	testOrder=testOrder.replace(section.getSectionId(), deskSection.getSectionId());
				saveSet.add(deskSection);
				//sectionManager.flush();
			}
			
			
//			obj.setDesktopLayout(testOrder);
			//ToaDesktopWhole nowDesk=getDesttopWholeById(obj.getDesktopId());
			newDesk.setDesktopLayout(testOrder);
			newDesk.setToaDesktopSections(saveSet);
			saveObjN(newDesk);
			List newList=new ArrayList();
			List oldList=new ArrayList();
			newList.addAll(newDesk.getToaDesktopSections());
			oldList.addAll(dd.getToaDesktopSections());
			Mycomparator comparator=new Mycomparator();
			Collections.sort(newList,comparator);
			Collections.sort(oldList,comparator);
			for(int i=0;i<newList.size();i++){
				ToaDesktopSection  newSection=(ToaDesktopSection)newList.get(i);
				ToaDesktopSection  oldSection=(ToaDesktopSection)oldList.get(i);
				testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
			}
//			Iterator newit=newDesk.getToaDesktopSections().iterator();
//			Iterator oldit=dd.getToaDesktopSections().iterator();
//			while(newit.hasNext()){
//				ToaDesktopSection  newSection=(ToaDesktopSection)newit.next();
//				ToaDesktopSection oldSection=(ToaDesktopSection)oldit.next();
//				testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
//			}
			newDesk.setDesktopLayout(testOrder);
			saveObjN(newDesk);
			return newDesk;
		
	}
	
	public ToaDesktopWhole getDesktopWholeByPortalId(String role,String portalId) throws IllegalAccessException, InvocationTargetException{
		
		if(role==null)
			role = "0";
		
		ToaDesktopWhole dd=this.getDefaultDesktop(role);
		String testOrder=dd.getDesktopLayout();
		if(testOrder==null||testOrder.equals("")){
			testOrder="";
		}
		ToaDesktopWhole newDesk = new ToaDesktopWhole();
		newDesk.setDesktopId(null);
		newDesk.setUserId("null");
		newDesk.setDesktopIsdefault("2");
		newDesk.setDesktopCenter(dd.getDesktopCenter());
		newDesk.setDesktopColumn(dd.getDesktopColumn());
		newDesk.setDesktopCtime(dd.getDesktopCtime());
		newDesk.setDesktopLayout(dd.getDesktopLayout());
		newDesk.setDesktopLeft(dd.getDesktopLeft());
		newDesk.setDesktopRight(dd.getDesktopRight());
		newDesk.setDesktopRole(dd.getDesktopRole());
		//saveObjN(newDesk);
		newDesk.setIsMoren("1");
		newDesk.setPortalId(portalId);
		
		Set sets=dd.getToaDesktopSections();
		Set saveSet=new HashSet();
		Iterator it=sets.iterator();
		while(it.hasNext()){
			ToaDesktopSection section=(ToaDesktopSection)it.next();
			ToaDesktopSection deskSection=new ToaDesktopSection();
			BeanUtils.copyProperties(deskSection, section);
			deskSection.setSectionId(null);
			deskSection.setToaDesktopWhole(newDesk);
			//sectionManager.saveObjR(deskSection);
//			testOrder=testOrder.replace(section.getSectionId(), deskSection.getSectionId());
			saveSet.add(deskSection);
			//sectionManager.flush();
		}
		
		
//		obj.setDesktopLayout(testOrder);
		//ToaDesktopWhole nowDesk=getDesttopWholeById(obj.getDesktopId());
		newDesk.setDesktopLayout(testOrder);
		newDesk.setToaDesktopSections(saveSet);
		saveObjN(newDesk);
		List newList=new ArrayList();
		List oldList=new ArrayList();
		newList.addAll(newDesk.getToaDesktopSections());
		oldList.addAll(dd.getToaDesktopSections());
		Mycomparator comparator=new Mycomparator();
		Collections.sort(newList,comparator);
		Collections.sort(oldList,comparator);
		for(int i=0;i<newList.size();i++){
			ToaDesktopSection  newSection=(ToaDesktopSection)newList.get(i);
			ToaDesktopSection  oldSection=(ToaDesktopSection)oldList.get(i);
			testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
		}
//		Iterator newit=newDesk.getToaDesktopSections().iterator();
//		Iterator oldit=dd.getToaDesktopSections().iterator();
//		while(newit.hasNext()){
//			ToaDesktopSection  newSection=(ToaDesktopSection)newit.next();
//			ToaDesktopSection oldSection=(ToaDesktopSection)oldit.next();
//			testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
//		}
		newDesk.setDesktopLayout(testOrder);
		saveObjN(newDesk);
		return newDesk;
	
}
	
	/**
	 * 获得默认桌面
	 * @return
	 */
	public ToaDesktopWhole getDefaultDesktop(String role){
		String[] whole={"1",role};
		List<ToaDesktopWhole> list=desktopWholeDao.find("from ToaDesktopWhole as whole where whole.desktopIsdefault=? and whole.desktopRole=?", whole);
		return list.get(0);
	}
	
	public boolean deleteDesktop(String wholeid){
		try{
			desktopWholeDao.delete(wholeid);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public ToaDesktopWhole saveObjN(ToaDesktopWhole deskWhole){
		try{
			desktopWholeDao.save(deskWhole);
			desktopWholeDao.flush();
			return deskWhole;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将不被缓存管理的内容持久化
	 * @param deskWhole 
	 */
	public ToaDesktopWhole saveObj(ToaDesktopWhole deskWhole){
		try{
			desktopWholeDao.getSession().merge(deskWhole);				//将不归缓存管理的对象进行持久化
			desktopWholeDao.getSession().flush();
			return deskWhole;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 17, 20093:46:00 PM
	 * @desc: 对顺序字符串的解析操作
	 * @param orderStr
	 * @return String[]
	 */
	public  String[] createOrder(String orderStr){
		String[] order=null;
		if(orderStr==null||"".equals(orderStr)){
			return null;
		}else{
			String temp[]=orderStr.split(";");
			order=new String[temp.length];
			for(int i=0;i<temp.length;i++){
				if((temp[i].indexOf(":"))==(temp[i].length()-1)){
					order[i]="null";
				}else{
					String idcore=temp[i].substring(temp[i].indexOf(":")+1, temp[i].length());
					order[i]=idcore;
				}
			}
			return order;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:46:30 PM
	 * @desc: 根据某一列字符串获取相应的List对象
	 * @param col
	 * @return List
	 */
	public  List getColumn(String col){
		List list=new ArrayList();
		if(col==null||"null".equals(col)||"".equals(col)){
			return null;
		}else{
			String id[]=col.split(",");
			for(int i=0;i<id.length;i++){
				String idnum=id[i].substring(id[i].indexOf("_")+1,id[i].length());
				ToaDesktopSection temp=sectionManager.getObjById(idnum);
				list.add(temp);
			}
			return list;
		}
	}

	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

}
