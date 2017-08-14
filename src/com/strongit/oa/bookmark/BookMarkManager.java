package com.strongit.oa.bookmark;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBookMark;
import com.strongit.oa.bo.ToaBookMarkEForm;
import com.strongit.oa.util.FiltrateContent;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * 标签管理服务类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午04:52:44
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bookmark.BookMarkManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class BookMarkManager {

	IGenericDAO<ToaBookMark, java.lang.String> bookMarkDao = null;		//定义DAO操作类.
	
	IGenericDAO<ToaBookMarkEForm, String> markEFormDao = null;			//标签与电子表单映射DAO操作类.
	
	IGenericDAO<Object, String> DAO = null ;							//通用DAO操作类
	
	/**
	 * 注入SESSION工厂
	 * @author:邓志城
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		bookMarkDao = new GenericDAOHibernate<ToaBookMark, java.lang.String>(
				sessionFactory, ToaBookMark.class);
		markEFormDao = new GenericDAOHibernate<ToaBookMarkEForm, java.lang.String>(
				sessionFactory, ToaBookMarkEForm.class);
		DAO = new GenericDAOHibernate<Object, String>(sessionFactory,Object.class);
	}

	/**
	 * 得到标签分页列表.
	 * @author:邓志城
	 * @date:2010-7-7 下午05:37:39
	 * @param page			分页对象
	 * @param model			BO对象,用于传输查询参数.
	 * @return				分页对象
	 */
	Page<ToaBookMark> getBookMarkList(Page<ToaBookMark> page,ToaBookMark model) {
		StringBuilder hql = new StringBuilder("from ToaBookMark t where 1=1 ");
		if(model != null){
			if(model.getName() != null && !"".equals(model.getName())){
				model.setName(model.getName().trim());
				hql.append(" and t.name like '%"+FiltrateContent.getNewContent(model.getName())+"%'");
			}
			if(model.getDesc() != null && !"".equals(model.getDesc())){
				model.setDesc(FiltrateContent.getNewContent(model.getDesc()).trim());
				hql.append(" and t.desc like '%"+model.getDesc()+"%'");
			}			
		}
		hql.append(" order by t.name");
		return bookMarkDao.find(page, hql.toString());
	}

	/**
	 * 根据id得到标签对象.
	 * @author:邓志城
	 * @date:2010-7-7 下午06:09:02
	 * @param id			主键
	 * @return				标签对象.
	 */
	ToaBookMark getBookMarkById(String id){
		return bookMarkDao.get(id);
	}

	/**
	 * 根据标签名称查找标签对象.
	 * @author:邓志城
	 * @date:2010-7-7 下午07:45:59
	 * @param model		标签对象
	 * @return			标签对象
	 */
	ToaBookMark getBookMarkByName(ToaBookMark model){
		StringBuilder hql = new StringBuilder("from ToaBookMark t where 1=1 ");
		List<String> params = new ArrayList<String>(1);
		if(model != null){
			if(model.getId()!=null && !"".equals(model.getId())){
				hql.append(" and t.id <> ?");
				params.add(model.getId());
			}
			if(model.getName() != null && !"".equals(model.getName())){
				hql.append(" and t.name like ?");
				params.add(model.getName());
			}
		}
		List<ToaBookMark> bookMark = bookMarkDao.find(hql.toString(), params.toArray());
		if(bookMark.size() > 0){
			return bookMark.get(0);
		}
		return null;
	}
	
	/**
	 * 根据表单id得到映射的标签列表.
	 * @author:邓志城
	 * @date:2010-7-7 下午06:00:26
	 * @param formId		表单模板id
	 * @return				
	 * 		List<Object[]>{
	 * 			书签id，书签名称，控件id，控件绑定的字段名，控件绑定的表名
	 * 		}
	 */
	public List getBookMarkList(String formId){
		StringBuilder hql = new StringBuilder("select t1.id,t1.name, t2.rest1,t2.fieldName,t2.tableName from ToaBookMark t1,");
					  hql.append(" ToaBookMarkEForm t2 where t1.id=t2.bookMarkId ");
					  hql.append(" and t2.formId=?");
		return DAO.find(hql.toString(), formId);			  
	}

	/**
	 * 查找指定的标签对应的所有表单的映射关系表.
	 * @author:邓志城
	 * @date:2010-7-15 上午10:28:49
	 * @param id				标签id
	 * @return					标签与电子表单的映射关系列表
	 */
	List<ToaBookMarkEForm> getBookMarkEFormList(String id) {
		StringBuilder hql = new StringBuilder("from ToaBookMarkEForm t where t.bookMarkId = ?");
		return markEFormDao.find(hql.toString(), id);
	}
	
	/**
	 * 根据书签id查找映射列表,用于校验是否书签已经被映射过了.
	 * @author:邓志城
	 * @date:2010-7-8 下午02:52:52
	 * @param formId			电子表单模板id
	 * @param id				书签id
	 * @return
	 * 		List<Object[]>{
	 * 			书签id，书签名称，控件id，控件绑定的字段，控件绑定的表，关联表的主键值
	 * 		}
	 */
	List getBookMarkList(String formId,String id){
		StringBuilder hql = new StringBuilder("select t1.id,t1.name, t2.rest1,t2.fieldName,t2.tableName,t2.id from ToaBookMark t1,");
		  hql.append(" ToaBookMarkEForm t2 where t1.id=t2.bookMarkId ");
		  hql.append(" and t2.formId=?");
		  hql.append(" and t2.bookMarkId = ?");
		  return DAO.find(hql.toString(), formId,id);
	}
	
	/**
	 * 得到标签列表
	 * @author:邓志城
	 * @date:2010-7-7 下午06:05:22
	 * @return	返回标签列表
	 */
	List<ToaBookMark> getBookMarkList() {
		return bookMarkDao.findAll();
	}
	
	/**
	 * 保存标签
	 * @author:邓志城
	 * @date:2010-7-7 下午05:38:56
	 * @param model		标签对象
	 */
	void save(ToaBookMark model) {
		bookMarkDao.save(model);
	}

	/**
	 * 批量删除标签
	 * 需要同时删除映射关系.
	 * @author:邓志城
	 * @date:2010-7-7 下午05:40:50
	 * @param ids	标签id
	 */
	void delete(String[] ids){
		if(ids != null && ids.length > 0){
			for(String id : ids){
				List<ToaBookMarkEForm> list = this.getBookMarkEFormList(id);
				if(list != null && !list.isEmpty()){
					for(ToaBookMarkEForm markEForm : list){
						this.deleteRelate(markEForm.getId());
					}
				}
				bookMarkDao.delete(id);
			}
		}
	}
	
	/**
	 * 保存映射关系
	 * @author:邓志城
	 * @date:2010-7-7 下午05:46:02
	 * @param model				映射关系对象.
	 */
	void saveRelate(ToaBookMarkEForm model){
		markEFormDao.save(model); 
		markEFormDao.flush();
	}

	/**
	 * 删除映射关系
	 * @author:邓志城
	 * @date:2010-7-7 下午05:47:51
	 * @param ids			映射关系ID
	 */
	void deleteRelate(String[] ids){
		markEFormDao.delete(ids);
		markEFormDao.flush();
	}

	/**
	 * 删除映射关系
	 * @author:邓志城
	 * @date:2010-7-7 下午05:47:51
	 * @param id			映射关系ID
	 */
	void deleteRelate(String id){
		markEFormDao.delete(id);
		markEFormDao.flush();
	}
}
