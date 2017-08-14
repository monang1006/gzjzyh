package com.engine.tag.gettag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.engine.tag.Tag;
import com.engine.util.Define;

public class LeadList extends Tag { 
	public String parse() {
		/*
		 * 搜索时间
		 */
		String time = (String)requestMap.get("date");	
		SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		if(time ==null){
			time = pim.format(date);
		}
		/*
		 * 获取数据
		 */
		String sql = "select user_id,user_name from t_Uums_Base_User u where u.org_id='ff8080813b88ec33013b9330a1200295' order by u.user_sequence";
		Query query =  this.engineDao.getSession().createSQLQuery(sql);
		List list = query.list();
		
		
		
		//替换占位符
		/*
		 * @文章标题@
		 */
		String userId="";
		/*
		 * @文章副标题@
		 */
		String userName ="";
		/*
		 * @上午@
		 */
		String morning = "";
		/*@下午@
		 * 
		 */
		String afternoon = "";
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		/*
		 * 循环所有领导
		 */
		if(list != null && list.size()> 0){
			for(int i=0;i<list.size();i++){
				
				
				
			/*
			 * 数据处理
			 */
			Object[] datas = (Object[])list.get(i);
			userId = datas[0] == null?"":datas[0].toString();
			userName = datas[1] == null?"":datas[1].toString();
			
			/*
			 * 查询上午的领导活动
			 */
			String sql2 = "select cal_title from T_OA_CALENDAR c where c.user_id=? and  c.cal_start_time  between  to_date('"+time+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss') "+
	        " and  to_date('"+time+" 12:00:00', 'yyyy-mm-dd hh24:mi:ss')";
			Query query2 =  this.engineDao.getSession().createSQLQuery(sql2).setString(0, userId);
			List list2 = query2.list();
			if(list2.size()>0){
				for(int j=0;j<list2.size();j++){
					String str = list2.get(j).toString() == null?"":list2.get(j).toString();
					morning = morning + str +",";
				}
			}
			
			if(!"".equals(morning)){
				morning = morning.substring(0, morning.length()-1);
			}
			/*
			 * 查询下午的领导活动
			 */
			String sql3 = "select cal_title from T_OA_CALENDAR c where c.user_id=? and  c.cal_start_time  between to_date('"+time+" 12:00:01', 'yyyy-mm-dd hh24:mi:ss') "+
	        " and  to_date('"+time+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss')";
			Query query3 =  this.engineDao.getSession().createSQLQuery(sql3).setString(0, userId);
			List list3 = query3.list();
			if(list3.size()>0){
				for(int j=0;j<list3.size();j++){
					String str = list3.get(j).toString() == null?"":list3.get(j).toString();
					afternoon = afternoon + str +",";
				}
			}
			/*
			 * 去除逗号
			 */
			if(!"".equals(afternoon)){
				afternoon = afternoon.substring(0, afternoon.length()-1);
			}
			/*
			 * 返回HTML
			 */
			temp = this.htmlContent;
		    temp = temp.replaceAll(quote("@用户ID@"),quoteReplacement(userId));
		    temp = temp.replaceAll(quote("@用户名称@"),quoteReplacement(userName));
		    temp = temp.replaceAll(quote("@时间@"),quoteReplacement(time));
		    temp = temp.replaceAll(quote("@上午@"),quoteReplacement(morning));
		    temp = temp.replaceAll(quote("@下午@"),quoteReplacement(afternoon));
			returnHtml.append(temp);	
			morning = "";
			afternoon = "";
			}
		}
		return returnHtml.toString();
	}
}
