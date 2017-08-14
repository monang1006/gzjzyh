package com.strongit.oa.outlink;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaOutlink;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/outLinkDesktop.action") })
public class OutLinkDesktopAction extends BaseActionSupport<ToaOutlink>{

	private static final long serialVersionUID = 5841830558896827794L;
	
	@Autowired private OutlinkManager outLinkManager;
	
	@Autowired private DesktopSectionManager dsmanager;
	
	private Page<ToaOutlink> page=new Page<ToaOutlink>(FlexTableTag.MAX_ROWS,true);
	
	private String blockid;
	
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		Map<String,String> map = dsmanager.getParam(blockid);		//通过blockid获取映射对象
		String showNum = map.get("showNum");						//显示条数
		String subLength = map.get("subLength");					//主题长度
		String showCreator = map.get("showCreator");				//是否显示作者
		String showDate = map.get("showDate");						//是否显示日期
		String sectionFontSize = map.get("sectionFontSize");		//是否显示字体大小
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		page.setPageSize(num);
		List<ToaOutlink> list=outLinkManager.getShowDeskInfo("0", page);
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){						//获取在条数范围内的列表
				ToaOutlink link = list.get(i);
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td >");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
			
				String title = link.getOutlinkDes();
				title=title==null?"无描述":title;
				if(title.length()>length)								//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span style=\"font-size:"+sectionFontSize+"px\" title=\"").append(link.getOutlinkDes()).append("\">")
				.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"window.open('"+link.getOutlinkAddress()+"')\"> ").append(title).append("</a></span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"100px\">");
				if("1".equals(showDate)){								//如果设置为显示日期，则显示日期信息
					String dateStr="";
					if(link.getOutlinkDate()==null){
						
					}else{
						dateStr=st.format(link.getOutlinkDate());
					}
					innerHtml.append("<span class =\"linkgray10\">").append(dateStr).append("</span>");
				}
				innerHtml.append("	</td>");
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		//链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/outlink/outLink.action")
			.append("', '外部链接'")
			.append(");");
		
		/*innerHtml.append("<div align=\"right\" style=\"padding:2px;font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
		 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");*/
		return renderHtml(innerHtml.toString());					//用renderHtml将设置好的html代码返回桌面显示
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ToaOutlink getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBlockid() {
		return blockid;
	}

	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}


}
