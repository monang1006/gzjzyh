package com.engine.tag.gettag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongmvc.orm.hibernate.Page;
/*
 * 门户首页领导活动列表
 */
public class distanceList extends Tag {
	public String parse() {

		StringBuffer returnHtml = new StringBuffer();
		try {
			/*
			 * 时间类型1
			 */
			SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
			/*
			 * 时间类型2
			 */
			SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			/*
			 * 标题
			 */
			String time = sim.format(date);
			/*
			 * 时间
			 */
			String time1 = pim.format(date);
			String temp = "";
			for (int i = 0; i < 9; i++) {
				/*
				 * 返回HTML
				 */
				temp = this.htmlContent;
				/*
				 * 设置标签名称
				 */
				temp = temp.replaceAll(quote("@活动标题@"), quoteReplacement(time));
				temp = temp.replaceAll(quote("@活动时间@"), quoteReplacement(time1));
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				/*
				 * 往前减少一天
				 */
				int day = c.get(Calendar.DATE);
				c.set(Calendar.DATE, day - 1);

				time = new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
				time1 = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
				date = sim.parse(time);

				returnHtml.append(temp);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return returnHtml.toString();
	}
}
