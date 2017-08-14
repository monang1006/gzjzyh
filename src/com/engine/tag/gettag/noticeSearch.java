package com.engine.tag.gettag;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;

public class noticeSearch extends Tag { 
	//<GET noticeSearch(10,yyyy-MM-dd,20)>@数据ID@,@数据标题@,@数据部门@,@数据时间@</GET>
	public String parse() {
		//获取配置参数
		int pageNo = Integer.valueOf((String)this.para.get(0)).intValue();//每页条数
		String timeFormat = (String)this.para.get(1);//时间格式
		int titleLength = Integer.valueOf((String)this.para.get(2)).intValue();//标题长度     	
		
		String newTitle = (String)requestMap.get("newTitle");          //搜索条件
		if(newTitle== null) newTitle="";
		
		//获取页码
		int curpage = 1; 
		if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
			curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
		}
		
		//获取数据
		Page page = new Page(pageNo, true);
		page.setPageNo(curpage);
		page.setPageSize(pageNo);
		
		newTitle = newTitle.replaceAll(quote("@"),quoteReplacement("%"));
		try {
			newTitle = java.net.URLDecoder.decode(newTitle,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "select t.toaAffiche.afficheId,t.toaAffiche.afficheTitle,t.toaAffiche.afficheGov,t.toaAffiche.afficheTime,t.toaAffiche.afficheUsefulLife " +
				"from ToaAfficheReceiver t where t.afficheReceiverId = 'alluser' and t.toaAffiche.afficheState = '1' and t.toaAffiche.afficheTitle " +
				"like ? and ( ( t.toaAffiche.afficheUsefulLife > ? and t.toaAffiche.afficheTime < ?) or t.toaAffiche.afficheUsefulLife is null ) " +
				"order by t.toaAffiche.afficheTime DESC";
		Date date = new Date();
		page = this.engineDao.find(page,sql,new Object[]{"%"+newTitle+"%",date,date});
		List list = page.getResult();

		//过滤时间失效的通知公告	
		if(list != null && list.size()> 0){
			for (int i = 0; i < list.size(); i++) {				
				String startTime = ((Object[])list.get(i))[3] == null ?"":TimeKit.formatDate((Date)((Object[])list.get(i))[3], timeFormat);
				String endTime = ((Object[])list.get(i))[4] == null ?"":TimeKit.formatDate((Date)((Object[])list.get(i))[4], timeFormat);
				String nowTime = TimeKit.formatDate(date, timeFormat);
				String[] startTimes = startTime.split("-");
				String[] endTimes = endTime.split("-");
				String[] nowTimes = nowTime.split("-");
				String sTime =startTimes[0]+""+startTimes[1]+""+startTimes[2];
				String eTime = endTimes[0]+""+endTimes[1]+""+endTimes[2];
				String nTime =nowTimes[0]+""+nowTimes[1]+""+nowTimes[2];
				if(sTime != null && !"".equals(sTime)){
					if(Integer.parseInt(sTime) > Integer.parseInt(nTime)){
						list.remove(i);
					}
				}
				
				if(eTime != null && !"".equals(eTime)){
					if(Integer.parseInt(eTime) < Integer.parseInt(nTime)){
						list.remove(i);
					}
				}
				
			}
			
		}
		
		
		//替换占位符
		String dataId="";//@数据ID@
		String dataTitle="";//@数据标题@
		String dataGov="";  //@数据部门@
		String dataTime = "";//@数据时间@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
	
		if(list != null && list.size()> 0){
			//数据处理
			for(int i=0;i<list.size();i++){
				dataId = ((Object[])list.get(i))[0] == null ?"":((Object[])list.get(i))[0].toString();
				dataTitle = ((Object[])list.get(i))[1] == null ?"":((Object[])list.get(i))[1].toString();
				if(dataTitle.length()> titleLength){
					dataTitle = dataTitle.substring(0, titleLength)+"...";
				}
				dataGov = ((Object[])list.get(i))[2] == null ?"":((Object[])list.get(i))[2].toString();
				dataTime = ((Object[])list.get(i))[3] == null ?"":TimeKit.formatDate((Date)((Object[])list.get(i))[3], timeFormat);
				
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
				temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
				temp = temp.replaceAll(quote("@数据部门@"),quoteReplacement(dataGov));
				temp = temp.replaceAll(quote("@数据时间@"),quoteReplacement(dataTime));
				returnHtml.append(temp);
			}
			//翻页处理
			temp = this.pageHtml;
			if(temp!=null && !"".equals(temp)){
				PageNextHandler pageNextHandler = new PageNextHandler();
				temp = temp.replaceAll(quote("#PAGE_HTML#"),quoteReplacement(pageNextHandler.getHTML( new int[]{page.getTotalPages(), page.getTotalCount(),curpage}, null, (String)requestMap.get(Define.WEB_PAGE_TYPE)+".shtml")));
				returnHtml.append(temp);
			}
			
		}
		return returnHtml.toString();
	}
}
