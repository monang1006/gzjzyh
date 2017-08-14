package com.strongit.oa.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public class ListUtils {
	/**
	 * 去除列表中重复的记录
	 * @author:邓志城
	 * @date:2009-5-7 下午04:48:28
	 * @param lst
	 * @return List lst 不存在重复记录的列表
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static List removeReduplicateItem(List lst)throws SystemException{
		if(lst!=null){
			for(int i=0;i<lst.size();i++){
				for(int j=i+1;j<lst.size();j++){
					if(lst.get(i).equals(lst.get(j))){ 
						lst.remove(j); 
						j--;
					} 
				}
			}
		}
		return lst;
	}
	
	/**
	 * 将List转换成分页对象
	 * @author:邓志城
	 * @param Page page 分页对象，此对象用于从前台获取参数。例如当前页，每页显示记录数
	 * @param List lst 数据列表
	 * @param int pageNo 当前页
	 * @param int pageSize 每页显示的记录条数
	 * @param List newLst 临时变量
	 * @date:2009-5-7 下午05:39:27
	 * @return 分页对象
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static Page splitList2Page(Page page,List lst)throws SystemException{
		
		List btLst = new ArrayList();
		int n = (page.getPageNo() - 1) * page.getPageSize();
		int pn = n + page.getPageSize();
		if(lst.size()<=page.getPageSize()){//解决当翻页之后再搜索时无法搜索出数据的问题，当搜索结果小于每页展示的数据量应该重新定位到第一页 jianggb 修改于2014-03-02
			n=0;
			page.setPageNo(1);
		}
		if (lst.size() < pn)
			pn = lst.size();
		for (int i = n; i < pn; i++)
			btLst.add(lst.get(i));

		page.setTotalCount(lst.size());
		page.setResult(btLst);
		lst = null;
		return page;
	}

	/**
	 * 将记录结果集转换成List
	 * @author:邓志城
	 * @date:2009-6-2 上午09:14:53
	 * @param rs
	 * @return
	 * @throws SystemException
	 */
	public static List result2List(ResultSet rs)throws SystemException{
		List<TempPo> beans = new ArrayList<TempPo>();
		try {
			while(rs.next()){
				TempPo bean = new TempPo();
				bean.setId(rs.getString(1));
				beans.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException("result2List异常!",e);
		}
		return beans;
	}

	/**
	 * 将一个bean中的所有属性值存放到一个List中
	 * @author:邓志城
	 * @date:2009-6-2 下午05:35:50
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static List<Object> bean2List(Object bean){
		List<Object> lst = new ArrayList<Object>();
		
		Field[] fields = bean.getClass().getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		for(Field field:fields){
			String fieldName = field.getName();
			Object fieldValue = null;
			try {
				fieldValue = field.get(bean);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(!fieldName.equals("serialVersionUID")){
				lst.add(fieldValue);
			}
		}
		return lst;
	}

	/**
	 * 将bean中的属性存储在一个Map中
	 * @author:邓志城
	 * @date:2009-7-6 下午02:25:23
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> bean2Map(Object bean)throws Exception{
		Map<String, Object> beanMap = new HashMap<String, Object>();
		Field[] fields = bean.getClass().getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		for(Field field:fields){
			String fieldName = field.getName();
			Object fieldValue = field.get(bean);
			if(!fieldName.equals("serialVersionUID")){
				beanMap.put(fieldName, fieldValue);
			}
		}
		return beanMap;
	}
	
	public static void main(String[] args)throws Exception{
		TempPo po = new TempPo();
		po.setId("id_123");
		po.setCodeId("code_123");
		po.setName("name_123");
		po.setType("type_123");
		po.setParentId("parentid_123");
		System.out.println(bean2List(po));
		System.out.println(bean2Map(po));
	}
}
