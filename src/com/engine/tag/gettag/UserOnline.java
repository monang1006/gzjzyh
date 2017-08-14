package com.engine.tag.gettag;

import com.engine.tag.Tag;
import com.strongit.oa.common.user.util.Const;
/**
 *  获取在线人数
 * 
 * @author ganyao
 * @date 7/310/2013 9:06 AM
 * @return  在线人数
 */
public class UserOnline extends Tag { 
	//<GET UserOnline()>@人数@</GET>
	public String parse() {
		//获取在线人数
		int counts = Const.userMap.size();
		String count= String.valueOf(counts);
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		temp = this.htmlContent;
		temp = temp.replaceAll(quote("@人数@"),quoteReplacement(count));
			returnHtml.append(temp);
		return returnHtml.toString();
	}
}
