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
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.dto.IssueDto;
import com.strongit.xxbs.dto.OrgDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.dto.UnsaveMailDto;
import com.strongit.xxbs.dto.UsedReportDto;
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
public class JdbcInfoReportService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	
	
	public Page<UsedReportDto> getReports(final Page<UsedReportDto> pagePl,String retitle)
	{
		String selectCount = "select count(*) ";
		String select = "select t.rp_id,t.rp_title ,t.rp_date  ";
		String from = "from T_INFO_BASE_REPORT t ";
		
		String  where = "";
		if(!retitle.equals("")&&!retitle.equals(null)){
			where = "where RP_TITLE like '%" + retitle + "%' ";
		}
		String order = "order by t.rp_date desc limit ? offset ? ";
		
		String sqlCount = selectCount + from +where;
		List<?> nums = template.query(sqlCount, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		
		String sql = select + from + where+order;
		@SuppressWarnings("unchecked")
		List<UsedReportDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(UsedReportDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		return pagePl;
	}
	
	public List<UsedReportDto> lastReports() throws ServiceException,
	SystemException, DAOException
	{ 
		String sql = "select t.rp_id,t.rp_title from T_INFO_BASE_REPORT t " +
				"order by t.rp_date  desc limit ?" ;
		@SuppressWarnings("unchecked")
		List<UsedReportDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, 7);
			}
		}, new BeanPropertyRowMapper(UsedReportDto.class));
		
		return rets;
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
