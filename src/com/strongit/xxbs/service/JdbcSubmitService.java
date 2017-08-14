package com.strongit.xxbs.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.OrgDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.PublishListDto;
import com.strongit.xxbs.dto.SubmitDto;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class JdbcSubmitService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	public Page<SubmitDto> getPublishs(final Page<SubmitDto> page, 
			final String orgId, final String submitStatus,  Map<String, String> search ,String isShared)
	{
		String selectCount = "select count(*) ";
		String select = "select pub_id , pub_title , PUB_IS_INSTRUCTION , pub_date , pub_submit_status , apt_id , pub_use_status , pub_is_comment , pub_is_share ";
		String from = "from T_INFO_BASE_PUBLISH ";
		
		String where = "where 1=1  AND (PUB_IS_MERGE!=0 OR PUB_IS_MERGE IS NULL) ";
		if(!Publish.ALL.equals(orgId)&&!Publish.ISSHARED.equals(submitStatus))
		{
		 	where += "and org_id='" + orgId +"' ";
		}
		if(!Publish.ALL.equals(submitStatus) && !Publish.COMMENT.equals(submitStatus)&&!Publish.ISSHARED.equals(submitStatus))
		{
			where += "and pub_submit_status='" + submitStatus + "' ";
		}
		if(Publish.COMMENT.equals(submitStatus))
		{
			where += "and pub_is_comment='" + Publish.COMMENTED + "' ";
		}
		if(Publish.ISSHARED.equals(submitStatus))
		{
			where += "and pub_is_share='" + Publish.SHARED + "' ";
		}
		if(search != null && !search.isEmpty())
		{
			where += "and pub_title like '%" + search.get("pubTitle") + "%' ";
			
			String startDate = search.get("startDate");
			String endDate = search.get("endDate");
			if(!"".equals(startDate) && !"".equals(endDate))
			{
				startDate += " 0:0:0";
				endDate += " 23:59:59";
				where += "and pub_date between '" + startDate + "' and '" + endDate + "' ";
			}
		}
		
		String order = "order by PUB_DATE desc limit ? offset ? ";
		
		String sqlCount = selectCount + from + where;
		List<?> nums = template.query(sqlCount, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		Double dTotalCount = (double) totalCount;
		Double totPages = Math.ceil(dTotalCount/page.getPageSize());
		int tPages = totPages.intValue();
		
		final int curpage = (page.getPageNo() > tPages)? tPages : page.getPageNo();
		
		String sql = select + from + where + order;
		if(totalCount!=0){
		@SuppressWarnings("unchecked")
		List<SubmitDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, page.getPageSize());
				ps.setLong(2, (curpage-1) * page.getPageSize());
			}
		}, new BeanPropertyRowMapper(SubmitDto.class));
		page.setPageNo(curpage);
		page.setTotalCount(totalCount);
		page.setResult(rets);
		}
		return page;
	}
	
	public Page<SubmitDto> getPublishsIsShared(final Page<SubmitDto> page)
	{
		String selectCount = "select count(*) ";
		String select = "select pub_id , pub_title , pub_info_type , pub_date , pub_submit_status , apt_id , pub_use_status , pub_is_comment , pub_is_share ";
		String from = "from T_INFO_BASE_PUBLISH ";
		
		String where = "where pub_is_share='1' ";
		String order = "order by PUB_DATE desc limit ? offset ? ";
		
		String sqlCount = selectCount + from + where;
		List<?> nums = template.query(sqlCount, new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				return rs.getInt(1);
			}
			
		});
		
		int totalCount = (Integer) nums.get(0);
		
		String sql = select + from + where + order;
		@SuppressWarnings("unchecked")
		List<SubmitDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				ps.setLong(1, page.getPageSize());
				ps.setLong(2, (page.getPageNo()-1) * page.getPageSize());
			}
		}, new BeanPropertyRowMapper(SubmitDto.class));
		
		page.setTotalCount(totalCount);
		page.setResult(rets);
		return page;
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
