package com.strongit.oa.personnel.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaStructure;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
/**
 * 机构编制manager
 * @author 胡丽丽
 * date:2009-10-19 16:34:00
 *
 */
@Service
@Transactional
public class PersonStructureManager {

	private GenericDAOHibernate<ToaStructure, String> structureDAO;
	
	/** 字典接口 */
	private IDictService dictService;
	/** 机构manager*/
	private PersonOrgManager personOrgManager;
	/** 人员manager*/
	private PersonManager personManager;
	/** 机构map*/
	private Map<String, ToaBaseOrg> orgmap=new HashMap<String, ToaBaseOrg>();
	/** 编制Map*/
	private Map<String,StructureStatistic> strucMap=new HashMap<String, StructureStatistic>();
	public Map<String, ToaBaseOrg> getOrgmap() {
		return orgmap;
	}
	public void setOrgmap(Map<String, ToaBaseOrg> orgmap) {
		this.orgmap = orgmap;
	}
	public Map<String, StructureStatistic> getStrucMap() {
		return strucMap;
	}
	public void setStrucMap(Map<String, StructureStatistic> strucMap) {
		this.strucMap = strucMap;
	}
	@Autowired
	public void setPersonOrgManager(PersonOrgManager personOrgManager) {
		this.personOrgManager = personOrgManager;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		structureDAO = new GenericDAOHibernate<ToaStructure, String>(
					sessionFactory, ToaStructure.class);
		}
	/**
	 * 保存编制
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaStructure model) throws SystemException,ServiceException{
		try {
			structureDAO.save(model);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存编制时出错！" });
		}
	}
	
	/**
	 * 根据ID删除编制
	 * @param id
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String id)throws SystemException,ServiceException{
		try {
			String[] ids=id.split(",");
			String hql="delete ToaStructure t where t.strucId in (";
			if(ids.length>0){
				for(int i=0;i<ids.length;i++){
					if(i==ids.length-1){
						hql=hql+"?)";
					}else{
					hql=hql+"?,";
					}
				}
			}
			Query query=structureDAO.createQuery(hql,ids);
			query.executeUpdate();
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除编制时出错！" });
		}
	}
	/**
	 * 根据机构删除编制
	 * @param id
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByOrg(String id)throws SystemException,ServiceException{
		try {
			String hql="delete  ToaStructure t where t.toaBaseOrg.orgid=?";
			Query query=structureDAO.createQuery(hql,id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除编制时出错！" });
		} 
	}
	/**
	 * 根据ID查询编制
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaStructure getStructureByID(String id) throws SystemException,ServiceException{
		try {
			return structureDAO.get(id);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据ID查编制时出错！" });
		}
	}
	/**
	 * 根据机构ID查询编制
	 * @param orgId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaStructure> getStructureByOrg(String orgId)throws SystemException,ServiceException{
		List<ToaStructure> list=new ArrayList<ToaStructure>();
		try {
			String hql ="from ToaStructure t where t.toaBaseOrg.orgid=?";
			list=  structureDAO.find(hql,orgId);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据机构ID查询编制出错！"});
		}
		return list;
	}
	
	/*
	 * 
	 * Description:根据机构ID，编制ID查询编制出错
	 * param: 
	 * @author 	 彭小青
	 * @date 	 Oct 8, 2009 5:42:59 PM
	 */
	public ToaStructure getStructureByOrgStruct(String orgId,String strucId)throws SystemException,ServiceException{
		ToaStructure struct=null;
		try{
			String hql ="from ToaStructure t where t.toaBaseOrg.orgid=? and t.strucType=?";
			List list=  structureDAO.find(hql,orgId,strucId);
			if(list!=null&&list.size()>0){
				struct=(ToaStructure) list.get(0);
			}
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据机构ID,编制ID查询编制出错！"});
		}
		return struct;
	}
	/**
	 * 根据机构ID和编制类型查询编制
	 * @param orgId
	 * @param type
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaStructure> getStructureByOrgType(String orgId,String type)throws SystemException,ServiceException{
		try {
			String hql="from ToaStructure t where t.toaBaseOrg.orgid=? and t.strucType=?";
			return structureDAO.find(hql, orgId,type);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据机构ID得和编制类型查询编制出错！"});
		}
	}
	
	/**
	 * 根据机构ID查询编制
	 * @param orgId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaStructure> getStructureByOrg(Page<ToaStructure> page,String orgId)throws SystemException,ServiceException{
		try {
			String hql ="from ToaStructure t where t.toaBaseOrg.orgid=?";
			return structureDAO.find(page,hql,orgId);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据机构ID查询编制出错！"});
		}
	}
	/**
	 * 根据机构ID和编制类型查询编制
	 * @param orgId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaStructure> getStructureByOrg(Page<ToaStructure> page,String orgId,String strucId)throws SystemException,ServiceException{
		try {
			String hql ="from ToaStructure t where t.toaBaseOrg.orgid=? ";
			if(strucId!=null&&!"".equals(strucId)){
				hql=hql+" and t.strucType=?";
				return structureDAO.find(page,hql,orgId,strucId);
			}else{
			return structureDAO.find(page,hql,orgId);
			}
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据机构ID查询编制出错！"});
		}
	}
	
   /**
    * 查询所有编制
    * @param page
    * @return
    * @throws SystemException
    * @throws ServiceException
    */
	public Page<ToaStructure> getAllStructure(Page<ToaStructure> page)throws SystemException,ServiceException{
		try {
			return structureDAO.findAll(page);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询所有编制出错！"});
		}
	}

	/**
	 * 根据条件搜索编制
	 * @param page
	 * @param structure
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaStructure> search(Page<ToaStructure> page,
			ToaStructure structure) throws SystemException, ServiceException {
		List<Object> objlist = new ArrayList<Object>();
		String hql = "from ToaStructure t where 1=1";
		if(structure.getStrucType()!=null&&!"".equals(structure.getStrucType())){
			hql=hql+" and  t.strucType=?";
			objlist.add(structure.getStrucType());
		}
		//机构
		if (structure.getToaBaseOrg().getOrgid() != null&&!"".equals(structure.getToaBaseOrg().getOrgid())) {
			hql = hql + " and t.toaBaseOrg.orgid=?";
			objlist.add(structure.getToaBaseOrg().getOrgid());
		}
		//编内人数
		if (structure.getStrucNumber() != null
				&& !"".equals(structure.getStrucNumber())) {
			hql = hql + " and t.strucNumber=?";
			objlist.add(structure.getStrucNumber());
		}
		//编辑时间
		if (structure.getStrucEdittime() != null) {
			hql = hql + " and t.strucEdittime>=?";
			objlist.add(structure.getStrucEdittime());
		}
		//编制状态
		if(structure.getStrucStatus()!=null&&!"".equals(structure.getStrucStatus())){
			hql=hql+" and t.strucStatus=?";
			objlist.add(structure.getStrucStatus());
		}
		Object[] objs = new Object[objlist.size()];
		for (int i = 0; i < objlist.size(); i++) {
			objs[i] = objlist.get(i);
		}
		return structureDAO.find(page, hql, objs);
	}
	/**
	 * 查看一共有多少条数据
	 * @param orgsyscode
	 * @param temp
	 * @return
	 */
    public int getCountByOrg(String orgsyscode,String temp){
    	String hql="select t.toaBaseOrg.orgid,sum(t.strucNumber) from ToaStructure t  ";
		if(orgsyscode==null||"".equals(orgsyscode)){
		hql=hql+" where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1')  group by t.toaBaseOrg.orgid";
		}else{
			hql=hql+" where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1'" +
			"and (t1.orgSyscode like '"+orgsyscode+"___' or t1.orgSyscode like '"+orgsyscode+"____'))  group by t.toaBaseOrg.orgid";
		}
		Query query=null;
		query=structureDAO.getSession().createQuery(hql);
		List list=query.list();
		if(list!=null){
		return list.size();
		}else{
			return 0;
		}
    }
	/**
	 * 统计机构下编制人员情况
	 * @author 胡丽丽
	 * @date2009-10-13
	 * @param page
	 * @return
	 */
	public Page<StructureStatistic> getStatisticByOrg(Page<StructureStatistic> page,String orgsyscode,String temp){
		String hql="select t.toaBaseOrg.orgid,sum(t.strucNumber) from ToaStructure t  ";
		if(orgsyscode==null||"".equals(orgsyscode)){
		hql=hql+" where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1')" +
				"  group by t.toaBaseOrg.orgid";
		}else{
			hql=hql+" where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1'" +
			"and (t1.orgSyscode like '"+orgsyscode+"%' or t1.orgSyscode like '"+orgsyscode+"%'))"+
			"   group by t.toaBaseOrg.orgid";
		}
		Query query=null;
		query=structureDAO.getSession().createQuery(hql);
		int pagesize=page.getPageSize();//每页行数
		int pageno=page.getPageNo();//当前页
		int begin=(pageno-1)*pagesize;//开始行
		int end=begin+pagesize-1;//结束行
		List<Object> list=query.list();
		page.setTotalCount(this.getCountByOrg(orgsyscode,temp));
		List<StructureStatistic> strList=new ArrayList<StructureStatistic>();
		this.getOrgMap();
		this.getRealityPerson();
		StructureStatistic sta=null;
		for(int i=0;i<list.size();i++){
			if(i>=begin &&i<=end){
			sta=new StructureStatistic();
			Object[] ob=(Object[])list.get(i);
			sta.setToabaseorg(orgmap.get(ob[0].toString()));
			sta.setProperPerson(Integer.parseInt(ob[1].toString()));
			if(strucMap.get(ob[0].toString())!=null){
				int reality=strucMap.get(ob[0].toString()).getRealityPerson();
				int proper=Integer.parseInt(ob[1].toString());
				sta.setRealityPerson(reality);
				if(reality>proper){
					sta.setOutPerson(reality-proper);
				}else{
				    sta.setOutPerson(0);
				}
			}
			strList.add(sta);
			}
		}
		page.setResult(strList);
		return page;
	}
	/**
	 * 统计机构下编制人员情况数量
	 * @author 胡丽丽
	 * @date2009-10-13
	 * @param page
	 * @return
	 */
	public int getStatisticCountByOrg(String orgid){
		String hql="select t.toaBaseOrg.orgid from ToaStructure t where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1' and t1.orgid = ?)  group by t.toaBaseOrg.orgid";
		List list=structureDAO.find(hql,orgid);
		if(list==null){
			return 0;
		}
		return list.size();
	}
	/**
	 * 统计机构下编制人员情况
	 * @author 胡丽丽
	 * @date2009-10-13
	 * @param page
	 * @return
	 */
	public Page<StructureStatistic> getStatisticByOrg(Page<StructureStatistic> page,String orgid){
		String hql="select t.toaBaseOrg.orgid,sum(t.strucNumber) from ToaStructure t where  t.toaBaseOrg.orgid in (select t1.orgid from ToaBaseOrg t1 where t1.orgIsdel<>'1' and t1.orgid = ?)   group by t.toaBaseOrg.orgid";
		Query query=structureDAO.createQuery(hql,orgid);
		List<Object> list=query.list();
		page.setTotalCount(this.getStatisticCountByOrg(orgid));
		List<StructureStatistic> strList=new ArrayList<StructureStatistic>();
		int pagesize=page.getPageSize();//每页行数
		int pageno=page.getPageNo();//当前页
		int begin=(pageno-1)*pagesize;//开始行
		int end=begin+pagesize-1;//结束行
		this.getOrgMap();
		this.getRealityPerson();
		StructureStatistic sta=null;
		for(int i=0;i<list.size();i++){
			if(i>=begin &&i<=end){
			sta=new StructureStatistic();
			Object[] ob=(Object[])list.get(i);
			sta.setToabaseorg(orgmap.get(ob[0].toString()));
			sta.setProperPerson(Integer.parseInt(ob[1].toString()));
			if(strucMap.get(ob[0].toString())!=null){
				int reality=strucMap.get(ob[0].toString()).getRealityPerson();
				int proper=Integer.parseInt(ob[1].toString());
				sta.setRealityPerson(reality);
				if(reality>proper){
					sta.setOutPerson(reality-proper);
				}else{
				    sta.setOutPerson(0);
				}
			}
			strList.add(sta);
			}
		}
		page.setResult(strList);
		return page;
	}
	/**
	 * 实际人数
	 */
	public void getRealityPerson(){
		List<Object> list=personManager.getSumPersonPerOrg();
		StructureStatistic sta=null;
		for(Object obj:list){
			sta=new StructureStatistic();
			Object[] objs=(Object[])obj;
			sta.setToabaseorg(orgmap.get(objs[0].toString()));
			sta.setRealityPerson(Integer.parseInt(objs[1].toString()));
			strucMap.put(objs[0].toString(),sta);
		}
	}
	/**
	 * Description:填充机构map
	 * param: 
	 * @author 	胡丽丽
	 * @date 	 2009-10-13 11：52
	 */
	public void getOrgMap(){
		List<ToaBaseOrg> orglist=personOrgManager.getOrgsByIsdel("0");
		if(orglist!=null){
		for(ToaBaseOrg org:orglist){
			orgmap.put(org.getOrgid(),org);
		}
		}
	}
	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}
	@Autowired
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}
}
