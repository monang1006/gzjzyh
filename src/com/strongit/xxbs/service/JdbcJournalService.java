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
import com.strongit.xxbs.dto.BulletinListDto;
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.dto.JournalListDto;
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
public class JdbcJournalService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	public Page<JournalListDto> getJourals(final Page<JournalListDto> pagePl,String jTitle)
	{
		String selectCount = "select count(*) ";
		String select = "select JOUR_ID,JOUR_NAME,JOUR_DATE,JOUR_CODE ";
		String from = "from t_info_base_journal ";
		
		String  where = "";
		if(!jTitle.equals("")&&!jTitle.equals(null)){
			where = "where JOUR_NAME like '%" + jTitle + "%' ";
		}
		String order = "order by JOUR_DATE desc limit ? offset ? ";
		
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
		List<JournalListDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(JournalListDto.class));
		
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
