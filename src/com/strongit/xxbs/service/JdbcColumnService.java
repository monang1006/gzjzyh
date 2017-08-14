package com.strongit.xxbs.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.utils.DateTimes;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.BulletinDto;
import com.strongit.xxbs.dto.BulletinListDto;
import com.strongit.xxbs.dto.ColumnDto;
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.dto.IssueDto;
import com.strongit.xxbs.dto.OrgDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.dto.roleDto;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBaseBulletinMarked;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class JdbcColumnService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	
	
	public List<IssueDto> lastIssues(String issId) throws ServiceException,
	SystemException, DAOException
	{ 
		String sql = "select u.iss_id,t.jour_name,u.iss_number " +
				"from t_info_base_journal t ,t_info_base_issue u " ;
		String where= "where t.jour_id=u.jour_id and u.iss_is_publish=1 " ;
		if(!issId.equals("")&&!issId.equals(null)){
			where +=  "and u.iss_id in("+issId+")";
		}
		String order = " order by u.iss_id , u.iss_number desc limit ?";
		String sqlnum = sql+where+order;
		@SuppressWarnings("unchecked")
		List<IssueDto> rets = template.query(sqlnum, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, 10);
			}
		}, new BeanPropertyRowMapper(IssueDto.class));
		
		return rets;
	}
	
	
	public List<IssueDto> findIssus(final String issId, final String orgId) throws ServiceException,
	SystemException, DAOException
	{ 
		String sql = "select t1.iss_id " +
		"from t_info_base_publish t1, t_info_base_column t2 " +
		" where  t1.col_id = t2.col_id " +
		"and t1.pub_use_status=1 " +
		"and t1.org_id='"+orgId+"' "+
		"and t1.iss_id='"+issId+"' "+
		"order by t2.col_sort";

		@SuppressWarnings("unchecked")
		List<?> nums = template.queryForList(sql);
		String ids = "";
		if(nums.size()>0){
		for(int i=0;i<nums.size();i++){
			String id =  nums.get(i).toString();
			id = id.substring(8, id.length()-1);
			ids += "'"+id+"'"+",";
		}
		ids = ids.substring(0, ids.length()-1);
		}
		List<IssueDto> ret = new ArrayList<IssueDto>();
		if(!ids.equals("")){
		ret = this.lastIssues(ids);
		}
		return ret;
	}
	
	
	public List<roleDto> findRole(final String userId,final String isRoleactiv) throws ServiceException,
	SystemException, DAOException
	{ 
		String sql = "select t.role_id from t_uums_base_role_user t where t.user_id=?";

		@SuppressWarnings("unchecked")
		List<roleDto> nums = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, userId);
			}
		}, new BeanPropertyRowMapper(roleDto.class));
		String ids = "";
		if(nums.size()>0){
		for(int i=0;i<nums.size();i++){
			String id =  nums.get(i).getRoleId();
			ids += "'"+id+"'"+",";
		}
		ids = ids.substring(0, ids.length()-1);
		}
		String sql2 ="select t.role_syscode from t_uums_base_role t ";
		String where="where t.role_isactive=? ";
		if(!ids.equals("")){
			where +="and t.role_id in("+ids+")";
		}		
		@SuppressWarnings("unchecked")
		List<roleDto> role = template.query(sql2+where, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setString(1, isRoleactiv);
			}
		}, new BeanPropertyRowMapper(roleDto.class));
		return role;
	}
	
	public Page<ColumnDto> getColumns(final Page<ColumnDto> pagePl,String colName,String jourName) throws ServiceException, SystemException,
			DAOException
	{
		String selectCount = "select count(*) ";
		String select = "select u.col_id , t.jour_name , u.col_name ,u.col_code ";
		String from = "from t_info_base_journal t ,t_info_base_column u ";
		String where ="where t.jour_id=u.jour_id ";
		if(!jourName.equals("all")&&!jourName.equals("")){
			where += " and t.jour_id = '" + jourName + "' ";
		}		
		if(!colName.equals("")&&!colName.equals(null)){
			where += " and u.col_name like '%" + colName + "%' ";
		}	
		
		String order = "order by u.col_sort asc limit ? offset ? ";
		String sqlCount = selectCount + from +where;
		List<?> nums = template.query(sqlCount, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		
		String sql = select + from +where+ order;
		@SuppressWarnings("unchecked")
		List<ColumnDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(ColumnDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		
		
		return pagePl;
	}
	
	
	/*
	 * 
	 */

	public JdbcTemplate getTemplate()
	{
		return template;
	}

	@Autowired
	public void setTemplate(JdbcTemplate template)
	{
		this.template = template;
	}

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	
}
