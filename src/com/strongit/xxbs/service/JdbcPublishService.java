package com.strongit.xxbs.service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import com.strongit.xxbs.dto.ScoreRankDto;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class JdbcPublishService
{
	private JdbcTemplate template;
	private SessionFactory sessionFactory;
	
	public Page<PublishListDto> getPublishs(final Page<PublishListDto> page, 
			final String orgName, final String submitStatus, final String useStatus, Map<String, String> search)
	{
		String selectCount = "select count(*) ";
		String select = "select P.PUB_ID, P.ORG_ID, P.PUB_DATE, P.PUB_IS_INSTRUCTION, P.PUB_IS_COMMENT, P.PUB_IS_SHARE, P.PUB_TITLE, P.PUB_USE_STATUS,P.PUB_MERGE_ORG,P.PUB_MERGE_NAME,P.ORG_NAME ";
		String from = "from T_INFO_BASE_PUBLISH P ";
		
		String where = "where P.PUB_SUBMIT_STATUS=? AND (P.PUB_IS_MERGE!=0 OR P.PUB_IS_MERGE IS NULL) ";
		if(!Publish.ALL.equals(orgName))
		{
		where += "and (P.ORG_NAME LIKE '%" + orgName +"%' or P.PUB_MERGE_NAME LIKE '%" + orgName +"%') ";
		}
		if(!Publish.ALL.equals(useStatus) && !Publish.COMMENT.equals(useStatus))
		{
			where += "and P.pub_use_status='" + useStatus + "' ";
		}
		if(Publish.COMMENT.equals(useStatus))
		{
			where += "and P.pub_is_comment='" + Publish.COMMENTED + "' ";
		}
		if(search != null && !search.isEmpty())
		{
			where += "and P.pub_title like '%" + search.get("pubTitle") + "%' ";
			
			String startDate = search.get("startDate");
			String endDate = search.get("endDate");
			if(!"".equals(startDate) && !"".equals(endDate))
			{
				startDate += " 0:0:0";
				endDate += " 23:59:59";
				where += "and P.pub_date between '" + startDate + "' and '" + endDate + "' ";
			}
		}
		
		String order = "order by P.PUB_DATE desc limit ? offset ? ";
		
		String sqlCount = selectCount + from + where;
		List<?> nums = template.query(sqlCount, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, submitStatus);
			}
			
		}, new RowMapper(){

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
		List<PublishListDto> rets = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps)
					throws SQLException
			{
				int i = 1;
				ps.setString(i++, submitStatus);
				ps.setLong(i++, page.getPageSize());
				ps.setLong(i++, (curpage-1) * page.getPageSize());
			}
		}, new BeanPropertyRowMapper(PublishListDto.class));
		
		page.setPageNo(curpage);
		page.setTotalCount(totalCount);
		page.setResult(rets);
		}
		return page;
	}
	
	public List<PublishDto> lastUsed(String orgId)
	{
		String select = "select t.PUB_ID, t.ORG_ID, t.PUB_SUBMIT_DATE, t.PUB_TITLE,u.ORG_NAME,t.PUB_MERGE_ORG ,t.ISS_ID ";
		String from = "from T_INFO_BASE_PUBLISH t , t_uums_base_org u ";
		
		String where = "where pub_use_status=1 and t.org_id = u.org_id and (t.PUB_IS_MERGE!=0 OR t.PUB_IS_MERGE IS NULL)  ";
		if(!Publish.ALL.equals(orgId))
		{
			where += "and t.org_id='" + orgId +"' ";
		}
		String order = "order by t.PUB_SUBMIT_DATE desc limit 6 ";

		String sql = select + from + where + order;
		@SuppressWarnings("unchecked")
		List<PublishDto> lists = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
			}
			
		}, new BeanPropertyRowMapper(PublishDto.class));
		return lists;
	}
	
	public List<PublishDto> lastSubmitted(String orgId)
	{
		String select = "select t.PUB_ID, t.ORG_ID, t.PUB_DATE, t.PUB_TITLE,u.ORG_NAME ";
		String from = "from T_INFO_BASE_PUBLISH t , t_uums_base_org u ";
		
		String where = "where t.pub_submit_status=1 and t.pub_use_status!=1 and t.org_id = u.org_id ";
		if(!Publish.ALL.equals(orgId))
		{
			where += "and t.org_id='" + orgId +"' ";
		}
		String order = "order by t.PUB_DATE desc limit 8 ";

		String sql = select + from + where + order;
		@SuppressWarnings("unchecked")
		List<PublishDto> lists = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
			}
			
		}, new BeanPropertyRowMapper(PublishDto.class));
		return lists;
	}
	
	
	public List<PublishDto> lastShared()
	{
		String select = "select t.PUB_ID, t.ORG_ID, t.PUB_DATE, t.PUB_TITLE,u.ORG_NAME , t.PUB_MERGE_ORG ";
		String from = "from T_INFO_BASE_PUBLISH t , t_uums_base_org u ";
		
		String where = "where pub_is_share=1 and t.org_id = u.org_id ";
		String order = "order by t.PUB_DATE desc limit 7 ";

		String sql = select + from + where + order;
		@SuppressWarnings("unchecked")
		List<PublishDto> lists = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
			}
			
		}, new BeanPropertyRowMapper(PublishDto.class));
		return lists;
	}

	
	public Map<String, String> getOrgNames()
	{
		Map<String, String> ret = new HashMap<String, String>();
		
		String sql = "select org_id, org_name from t_uums_base_org";
		@SuppressWarnings("unchecked")
		List<OrgDto> lists = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
			}
			
		}, new BeanPropertyRowMapper(OrgDto.class));
		
		if(lists != null)
		{
			for(OrgDto org : lists)
			{
				ret.put(org.getOrgId(), org.getOrgName());
			}
		}
		return ret;
	}
	
	public List<ScoreRankDto> scoreRank()
	{
		
		Calendar now = Calendar.getInstance();
		SimpleDateFormat   sdf   =   new   SimpleDateFormat( "yyyy-MM-dd "); 
		Date date = new Date();
		
		//本年1月1日
		now.setTime(date);
		now.set(Calendar.MONTH, 0);
		now.set(Calendar.DATE, 1);
		final String sDate = "'"+sdf.format(now.getTime())+" 0:0:0'";
		//本年12月末日
		now.setTime(date);
		now.set(Calendar.MONTH, 11);
		now.add(Calendar.MONTH, 1);
		now.set(Calendar.DATE, 1);
		now.add(Calendar.DATE, -1);
		final String eDate = "'"+sdf.format(now.getTime())+" 23:59:59'";
		String sql = "select t.org_id, o.org_name,sum(score) as totalScore " +
				"from t_info_base_total_score t , t_uums_base_org o " +
				"where t.pub_date >= " +sDate+
				" and t.pub_date <= "+eDate+
				" and t.org_id=o.org_id " +
				"group by t.org_Id ,o.org_name " +
				"order by sum(score) desc limit 5";
		
		@SuppressWarnings("unchecked")
		List<ScoreRankDto> lists = template.query(sql, new PreparedStatementSetter(){

			public void setValues(PreparedStatement ps) throws SQLException
			{
			}
			
		}, new BeanPropertyRowMapper(ScoreRankDto.class));
		for(int i=0;i<lists.size();i++){
			lists.get(i).setIndex(String.valueOf(i+1));
		}
		return lists;
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
