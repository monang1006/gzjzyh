package com.strongit.oa.vote.util;

import java.util.HashMap;

/**
 * @author piyu Jun 10, 2010 调查问卷使用的常量类
 */
public class VoteConst {
	/**
	 * 问卷类型:页面投票，手机短信投票
	 */
	public static String vote_type_page="1";//页面投票
	public static String vote_type_sms="2";//短信和页面投票
	public static HashMap<String, String> map_survey_type = getVote_type();
	
	public static HashMap<String, String> getVote_type() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(vote_type_page,"页面参与");
		map.put(vote_type_sms,"短信和页面参与");
		return map;
	}
	
	public static String vote_wjh="0";//未激活
	public static String vote_yjh="1";//激活
	public static String vote_gq="2";//过期
	
	public static String table_separator="-";//表格型问题的行内容分隔符
	
	public static String prefix_picPath="/images/vote";//图片的路径前缀
	public static int BUFFER_SIZE=16 * 1024 ; //上传图片使用
	
	public static int page_row_size=40 ;//单位px，客观题选项描述的宽度，超过换行
}
