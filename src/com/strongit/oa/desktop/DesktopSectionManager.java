package com.strongit.oa.desktop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopSection;
import com.strongit.oa.bo.ToaDesktopSectionsel;
import com.strongit.oa.bo.ToaDesktopWhole;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
/**
 * 对数据库的桌面模块进行操作
 * @author yuhz
 * @version 1.0
 */
@Service
@Transactional
public class DesktopSectionManager {
	private GenericDAOHibernate<ToaDesktopSection,String> desktopSectionDao;
	
	private DesktopWholeManager wholeManager;
	
	@Autowired
	public void setWholeManager(DesktopWholeManager wholeManager) {
		this.wholeManager = wholeManager;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		desktopSectionDao = new GenericDAOHibernate<ToaDesktopSection,String>(sessionFactory,ToaDesktopSection.class);
	}
	
	public ToaDesktopSection getObjById(String id){
		return desktopSectionDao.get(id);
	}
	
	/**
	 * 注：Map  key值注释
	 *    showNum		显示条数
	 *    subLength		主题长度
	 *    showCreator	是否显示作者
	 *    showDate		是否显示日期
	 *    sectionFontSize		是否显示日期
	 * @param id
	 * @modify zhanglei
	 * @return
	 */
	
	public Map<String,String> getParam(String id){
		ToaDesktopSection section=desktopSectionDao.get(id);
		if(section!=null){
			Map<String,String> map=new HashMap<String,String>();
			map.put("showNum", String.valueOf(section.getSectionRow()));
			map.put("subLength", String.valueOf(section.getSectionWidth()));
			map.put("showCreator", section.getSectionCreater());
			map.put("showDate", section.getSectionDate());
			map.put("showType", section.getShowType());
			String sectionFontSize = ""+section.getSectionFontSize();
			if("0".equals(sectionFontSize)||sectionFontSize == null || "".equals(sectionFontSize)
					|| "null".equals(sectionFontSize)){
				map.put("sectionFontSize", "12");
			}else{
				map.put("sectionFontSize", section.getSectionFontSize()+"");
			}
			return map;
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:23:03 PM
	 * @desc: 根据ID进行删除模块操作
	 * @param id 
	 */
	public void delete(String id){
		desktopSectionDao.delete(id);
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:23:22 PM
	 * @desc: 对桌面模块添加的保存操作
	 * @param blockType  模块类型
	 * @param blockTitle 模块名称呢个
	 * @param wholeId    方案ID
	 * @param blockurl   模块对应的处理Action的url
	 * @return ToaDesktopSection
	 */
	public ToaDesktopSection saveObj(String blockType,String blockTitle,String wholeId,String blockurl){
		ToaDesktopSection desktopSection =uniqueObj(blockType,blockTitle,blockurl,wholeId);
		if("".equals(desktopSection.getSectionId())||desktopSection.getSectionId()==null){
		ToaDesktopWhole whole=wholeManager.getDesttopWholeById(wholeId);
		desktopSection.setSectionRow(5);
		desktopSection.setSectionWidth(16);
		desktopSection.setSectionType(blockType);
		desktopSection.setSectionName(blockTitle);
		desktopSection.setSectionCreater("0");
		desktopSection.setSectionDate("0");
		desktopSection.setSectionColor("cccccc");
		desktopSection.setSectionFontSize(14);
		desktopSection.setToaDesktopWhole(whole);
		desktopSection.setSectionurl(blockurl);
		desktopSection.setSectionImg("box.gif");
		desktopSectionDao.save(desktopSection);
		}else{
			return null;
		}
		return desktopSection;
	}
	/**
	 * 
	 * @author：xush
	 * @time：Feb 17, 20093:23:22 PM
	 * @desc: 对桌面模块添加的保存前做唯一性操作
	 * @param blockType  模块类型
	 * @param blockTitle 模块名称呢个
	 * @param wholeId    方案ID
	 * @param blockurl   模块对应的处理Action的url
	 * @return ToaDesktopSection
	 */
	public ToaDesktopSection uniqueObj(String blockType,String blockTitle,String blockurl,String wholeId){
		StringBuffer sql=new StringBuffer();
		String param[]={blockType,blockTitle,blockurl,wholeId};
		List<ToaDesktopSection> list=desktopSectionDao.find("from ToaDesktopSection as section where section.sectionType=? and section.sectionName = ? and section.sectionurl = ? and section.toaDesktopWhole.desktopId = ?", param);
		if(list.size()==0){
			return new ToaDesktopSection();
		}else{
			return list.get(0);
		}
	}
	
	
	public ToaDesktopSection saveObj(String blockType,String blockTitle,String wholeId,String blockurl,String showType){
		ToaDesktopSection desktopSection =uniqueObj(blockType,blockTitle,blockurl,wholeId);
		if("".equals(desktopSection.getSectionId())||desktopSection.getSectionId()==null){
		ToaDesktopWhole whole=wholeManager.getDesttopWholeById(wholeId);
		desktopSection.setSectionRow(5);
		desktopSection.setSectionWidth(16);
		desktopSection.setSectionType(blockType);
		desktopSection.setSectionName(blockTitle);
		desktopSection.setSectionCreater("0");
		desktopSection.setSectionDate("0");
		desktopSection.setSectionColor("cccccc");
		desktopSection.setSectionFontSize(14);
		desktopSection.setToaDesktopWhole(whole);
		desktopSection.setSectionurl(blockurl);
		desktopSection.setSectionImg("box.gif");
		desktopSection.setShowType(showType);
		desktopSectionDao.save(desktopSection);
		}else{
			return null;
		}
		return desktopSection;
	}	
	
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:24:15 PM
	 * @desc: 将对象持久化
	 * @param obj void
	 */
	public void saveObj(ToaDesktopSection obj){
		desktopSectionDao.save(obj);
	}
	
	public void flush(){
		desktopSectionDao.flush();
	}
	
	public ToaDesktopSection saveObjR(ToaDesktopSection obj){
		desktopSectionDao.save(obj);
		desktopSectionDao.flush();
		return obj;
	}

}
