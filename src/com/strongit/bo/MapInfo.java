package com.strongit.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * 		case "navarat":

		case "orange":

		case "yellow":

		case "green":

		case "blue":

		case "gray":

		case "o_navarat":

		case "o_orange":

		case "o_yellow":

		case "o_green":

		case "o_blue":

		case "o_gray":
 *
 */
public class MapInfo {
	public static Map<String,String> map=null;
	public static List<Channel> col=new ArrayList<Channel>();
	public static List<Channel> leaderCol=new ArrayList<Channel>();
	public static String orderStr;
	public static String leaderOrderStr;
	static{
		map=new HashMap<String,String>();
		map.put("-1", "新闻中心");
		map.put("-2", "通知公告");			//公告中心
		map.put("3", "待办事宜");				//待办流程
		map.put("27", "日程安排");			//常务安排
//		map.put("13", "最近消息");			//最近短信
		map.put("13", "最近邮件");			//最近短信
		map.put("12", "最近邮件");
		map.put("37", "今日会议");
		map.put("40", "待批会议");
		map.put("260", "RSS资讯");
		map.put("45", "最新客户");
		map.put("46", "最新产品");
		map.put("231", "车辆维护");
		map.put("250", "投票调查");
		map.put("-3", "快捷运行");
		map.put("30", "最近计划");
		map.put("111", "我的协作");
		map.put("7", "个人文档");				//个人文档
		map.put("8", "公共文档");
		map.put("16", "人事工作");
		map.put("-4", "天气预报");
		map.put("-99", "奥运金牌");
		
		//----------------------------------------------------
		
		map.put("261", "图片新闻");
		map.put("262", "列车时刻");
		map.put("263", "预算执行");
		map.put("264", "公文审核");
		map.put("265", "指标审核");
		map.put("266", "拨款审核");
		
		
		
		Channel addC=new Channel();
		
		/**
		 * 添加列中的内容(个人桌面)
		 */
		addC.setBlockid("0");
		addC.setBlocktype("261");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("261"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("copy.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("1");
		addC.setBlocktype("-1");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("-1"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("action_paste.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("2");
		addC.setBlocktype("-2");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("-2"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("3");
		addC.setBlocktype("27");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("27"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("4");
		addC.setBlocktype("8");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("8"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("5");
		addC.setBlocktype("262");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("262"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("6");
		addC.setBlocktype("264");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("264"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		
		addC=new Channel();
		addC.setBlockid("7");
		addC.setBlocktype("12");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("12"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("8");
		addC.setBlocktype("3");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("3"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("9");
		addC.setBlocktype("13");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("13"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("calendar_view_day.gif");
		col.add(addC);
		addC=null;
		
		orderStr="col_1:drag_0,drag_1,drag_6,drag_7,drag_8,drag_9,;col_2:drag_2,drag_3,drag_4,drag_5,;";//"col_1:drag_6,drag_4,drag_7,drag_10,;col_2:drag_0,drag_1,drag_8,;col_3:drag_2,drag_5,drag_3,drag_9,;";//"col_1:drag_2,drag_4,drag_7,drag_10,;col_2:drag_0,drag_1,drag_8,;col_3:drag_5,drag_6,drag_3,drag_9,;";//"col_1:drag_2,drag_4,drag_7,drag_10,;col_2:drag_0,drag_1,drag_8,;col_3:drag_3,drag_6,drag_5,drag_9,;";//"col_1:drag_0,drag_1,drag_6,;col_2:drag_2,drag_8,drag_7,;col_3:drag_3,drag_4,drag_5,;";
		
		//领导办公
		
		addC=new Channel();
		addC.setBlockid("0");
		addC.setBlocktype("261");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("261"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;

		addC=new Channel();
		addC.setBlockid("1");
		addC.setBlocktype("-1");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("-1"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("2");
		addC.setBlocktype("-2");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("-2"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("3");
		addC.setBlocktype("27");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("27"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("4");
		addC.setBlocktype("262");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("262"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("5");
		addC.setBlocktype("-4");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("-4"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("6");
		addC.setBlocktype("7");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("7"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("7");
		addC.setBlocktype("8");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("8"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("8");
		addC.setBlocktype("264");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("264"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("9");
		addC.setBlocktype("265");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("265"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;

		addC=new Channel();
		addC.setBlockid("10");
		addC.setBlocktype("266");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("266"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		addC=null;
		
		addC=new Channel();
		addC.setBlockid("11");
		addC.setBlocktype("3");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("3"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);		
		
		addC=new Channel();
		addC.setBlockid("12");
		addC.setBlocktype("263");
		addC.setBlocklayout("3");
		addC.setBlocktitle(map.get("263"));
		addC.setBlocktpl("gray");
		addC.setBlockimg("page_favourites.gif");
		leaderCol.add(addC);
		
		leaderOrderStr="col_1:drag_0,drag_1,drag_8,drag_9,drag_10,drag_11,drag_12,;col_2:drag_2,drag_3,drag_4,drag_5,drag_6,drag_7,;";
		
	}
	
}
