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
import com.strongit.xxbs.dto.OrgDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBaseBulletinMarked;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class JdbcBulletinService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	public Page<BulletinListDto> getBulletins(final Page<BulletinListDto> pagePl,String blTitle)
	{
		String selectCount = "select count(*) ";
		String select = "select BL_ID,BL_TITLE,BL_DATE ";
		String from = "from T_INFO_BASE_BULLETIN ";
		
		String  where = "";
		if(!blTitle.equals("")&&!blTitle.equals(null)){
			where = "where BL_TITLE like '%" + blTitle + "%' ";
		}
		String order = "order by BL_DATE desc limit ? offset ? ";
		
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
		List<BulletinListDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, pagePl.getPageSize());
				ps.setLong(2, (pagePl.getPageNo()-1) * pagePl.getPageSize());
			}
		}, new BeanPropertyRowMapper(BulletinListDto.class));
		
		pagePl.setTotalCount(totalCount);
		pagePl.setResult(rets);
		return pagePl;
	}
	
	public List<BulletinDto> lastBulletins() throws ServiceException,
	SystemException, DAOException
	{ 
		String sql = "select bl_id,bl_title,bl_date " +
				"from t_info_base_bulletin order by bl_date desc limit ? ";
		@SuppressWarnings("unchecked")
		List<BulletinDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setInt(1, 5);
			}
		}, new BeanPropertyRowMapper(BulletinDto.class));
		
		return rets;
	}
	
	public Boolean isRead(final String blId, final String userId) throws ServiceException,
	SystemException, DAOException
	{
		Boolean isRead = false;
		String sql = "select count(*) from t_info_base_bulletin_marked where bl_id = ? and user_id=? ";
		List<?> nums = template.query(sql,new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, blId);
				ps.setString(2, userId);
			}
			
		}, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		
		if(totalCount > 0)
		{
			isRead = true;
		}
		return isRead;
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
