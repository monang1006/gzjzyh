package com.strongit.xxbs.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
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
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.dto.OrgDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class JdbcInvitationService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	public Page<InvitationDto> getInvitations(final Page<InvitationDto> pagePl)
	{
		String selectCount = "select count(*) ";
		String select = "select APT_ID , APT_TITLE , APT_DATE , APT_DUEDATE ";
		String from = "from T_INFO_BASE_APPOINT ";
		
		String order = "order by APT_DATE desc limit ? offset ? ";
		
		String sqlCount = selectCount + from;
		List<?> nums = template.query(sqlCount, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		
		String sql = select + from + order;
		@SuppressWarnings("unchecked")
		List<InvitationDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(InvitationDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		return pagePl;
	}
	
	
	public Page<InvitationDto> findAppoints(final Page<InvitationDto> pagePl,Map<String, String> search)
	{
		String selectCount = "select count(*) ";
		String select = "select APT_ID , APT_TITLE , APT_DATE , APT_DUEDATE ";
		String from = "from T_INFO_BASE_APPOINT ";
		
		String where = "where 1=1 ";
		if(search != null && !search.isEmpty())
		{
			where += "and APT_TITLE like '%" + search.get("aptTitle") + "%' ";
			
			String startDate = search.get("aptDate");
			String endDate = search.get("aptDuedate");
			if(!"".equals(startDate))
			{
				startDate += " 0:0:0";
				where += "and APT_DATE >= '" + startDate + "' ";
			}
			if(!"".equals(endDate)){
				endDate += " 23:59:59";
				where += "and APT_DUEDATE <= '" + endDate + "' ";
			}
		}
		
		String order = "order by APT_DATE desc limit ? offset ? ";
		
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
		List<InvitationDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(InvitationDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		return pagePl;
	}
	
	
	public Page<InvitationDto> getAppoints(final Page<InvitationDto> pagePl,
			String orgId, Boolean isSinceToday,Map<String, String> search) throws ServiceException, SystemException,
			DAOException
	{
		String selectCount = "select count(*) ";
		String select = "select APT_ID , APT_TITLE , APT_DATE , APT_DUEDATE ";
		String from = "from T_INFO_BASE_APPOINT ";
		
		String where = "where 1=1 ";
		if(search != null && !search.isEmpty())
		{
			where += "and APT_TITLE like '%" + search.get("aptTitle") + "%' ";
			
			String startDate = search.get("aptDate");
			String endDate = search.get("aptDuedate");
			if(!"".equals(startDate))
			{
				startDate += " 0:0:0";
				where += "and APT_DATE >= '" + startDate + "' ";
			}
			if(!"".equals(endDate)){
				endDate += " 23:59:59";
				where += "and APT_DUEDATE <= '" + endDate + "' ";
			}
		}
		
		
		String sql1 = "select apt_id from t_info_base_appoint_to where org_id='"+orgId+"' " ;
		List<?> aptIds = template.queryForList(sql1);
		String ids = "";
		if(aptIds.size()>0){
		for(int i=0;i<aptIds.size();i++){
			String aptId = aptIds.get(i).toString();
			aptId = aptId.substring(8, aptId.length()-1);
			ids+="'"+aptId+"',";
		}
		ids = ids.substring(0, ids.length()-1);
		}
		where += "and apt_id in ("+ids+") or apt_is_all_org=0";
		
		
		String order = "order by APT_DATE desc limit ? offset ? ";
		
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
		List<InvitationDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(InvitationDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		
		
		return pagePl;
	}
	
	public Page<InvitationDto> getLaseAppoints(final Page<InvitationDto> pagePl,
			String orgId, Boolean isSinceToday,Map<String, String> search) throws ServiceException, SystemException,
			DAOException
	{
		String selectCount = "select count(*) ";
		String select = "select APT_ID , APT_TITLE , APT_DATE , APT_DUEDATE ";
		String from = "from T_INFO_BASE_APPOINT ";
		DateTimes time = new DateTimes();
		Date sdate = new Date();
		sdate = time.date2DateTime(sdate, false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stdate = sdf.format(sdate);
		
		String where = "where 1=1 ";
		if(search != null && !search.isEmpty())
		{
			where += "and APT_TITLE like '%" + search.get("aptTitle") + "%' ";
			
			String startDate = search.get("aptDate");
			String endDate = search.get("aptDuedate");
			if(!"".equals(startDate))
			{
				startDate += " 0:0:0";
				where += "and APT_DATE >= '" + startDate + "' ";
			}
			if(!"".equals(endDate)){
				endDate += " 23:59:59";
				where += "and APT_DUEDATE <= '" + endDate + "' ";
			}
		}
		
		String sql1 = "select apt_id from t_info_base_appoint_to where org_id='"+orgId+"' " ;
		List<?> aptIds = template.queryForList(sql1);
		String ids = "";
		if(aptIds.size()>0){
		for(int i=0;i<aptIds.size();i++){
			String aptId = aptIds.get(i).toString();
			aptId = aptId.substring(8, aptId.length()-1);
			ids+="'"+aptId+"',";
		}
		ids = ids.substring(0, ids.length()-1);
		}
		if(ids.equals("")){
			where+="and apt_is_all_org=0 ";
		}
		else{
			where += "and  (apt_is_all_org=0 or apt_id in ("+ids+")) ";
		}
		
		where += "and APT_DATE<= '"+stdate+"' and APT_DUEDATE>='"+stdate+"' ";
		
		String order = "order by APT_DATE desc limit ? offset ? ";
		
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
		List<InvitationDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(InvitationDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		
		
		return pagePl;
	}
	
	public List<InvitationDto> lastAppoints(String orgId)
	throws ServiceException, SystemException, DAOException
	{
		Page<InvitationDto> pagePl = new Page<InvitationDto>(1, true);
		pagePl.setPageNo(1);
		pagePl.setPageSize(8);
		pagePl.setOrder("desc");
		pagePl.setOrderBy("aptDate");
		pagePl = getLaseAppoints(pagePl, orgId, true, null);
		return pagePl.getResult();
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
