package com.engine.tag.gettag;

import java.util.Date;
import java.util.List;
import com.engine.tag.Tag;
import com.engine.util.Define;

public class noticeAttach extends Tag {
	// <GET noticeAttach()>@附件ID@,@附件标题@</GET>
	public String parse() {
		// 获取配置参数
		String noticeId = (String) requestMap.get(Define.WEB_KEY);
		String src = "notifyattach/notifyAttach!down.action?attachId="; //下载的action地址

		// 获取数据
		String sql = "select t.attachId,t.attachName from ToaAttachment t where t.attachId in (select tt.attachId from ToaAfficheAttach tt where tt.toaAffiche.afficheId = ?)";
		List list = this.engineDao.find(sql, new Object[] { noticeId });

		// 替换占位符
		String dataId = ""; // @附件ID@
		String dataTitle = "";// @附件标题@

		StringBuffer returnHtml = new StringBuffer();
		if(list != null && list.size()> 0) {
			// 数据处理
			//returnHtml.append("");
			for (int i = 0; i < list.size(); i++) {

				Object[] datas = (Object[]) list.get(i);
				dataId = datas[0] == null ? "" : datas[0].toString();
				dataTitle = datas[0] == null ? "" : datas[1].toString();
				if (!"".equals(dataId)) {
					returnHtml
							.append("<div><font size='4'>附件:</font><a href=\"")
							.append(src)
							.append(dataId)
							.append("\"/><font size=\"4\" color=\"blue\">")
							.append(dataTitle)
							.append("</font></a></div>");
				}
			}

		}
		return returnHtml.toString();
	}
}
