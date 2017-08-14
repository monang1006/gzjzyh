package com.strongit.oa.meetingroom;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

import com.strongit.oa.bo.ToaMeetingroom;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "meetingroomDesktop.action", type = ServletActionRedirectResult.class)
			,@Result(name = "show", value = "/meetingroomDesktop.jsp", type = ServletDispatcherResult.class)
		})
public class MeetingroomDesktopAction extends BaseActionSupport {

	private Page<ToaMeetingroom> page = new Page<ToaMeetingroom>(FlexTableTag.MAX_ROWS, true);
	private String mrId;
	ToaMeetingroom model = new ToaMeetingroom();
	private Meetingroom manager;
	private HashMap<String, String> statemap = new HashMap<String, String>();
	private List clashList;
	public MeetingroomDesktopAction(){
		statemap.put(null, "正常使用");
		statemap.put("0", "正常使用");
		statemap.put("1", "停止使用");
		statemap.put("2", "已删除");
	}
	//用于桌面显示
	public String desktopShow() throws Exception{
		List<Object[]> l = new ArrayList<Object[]>();
		page = manager.getAllRooms(page, model);
		if(page!=null){
			if(page.getResult().size()>0){
				for(ToaMeetingroom o:page.getResult()){
					Object[] objects = new Object[10];
					objects[0]=o.getMrId();
					objects[1]=o.getMrName();
					objects[2]=o.getMrPeople();
					objects[3]=o.getMrLocation();
					objects[4]=o.getMrType();
					objects[5]=o.getMrRemark();
					objects[6]=o.getMrState();
					//判断会议室是否正在使用
					String t = manager.listMeetingRoom(o.getMrId());
					if("true".equals(t)){
						objects[7]="(会议室正在使用！)";
						objects[8]="ncbgt/images/hysShow.gif";
					}else{
						objects[8]="ncbgt/images/hysClose.gif";
					}
					l.add(objects);
				}
			}
		}
		if(l!=null&&l.size()>0){
			getRequest().setAttribute("l", l);
		}
		return "show";
	}
	/**
	 * 
	 * author  taoji
	 * @return
	 * @date 2013-1-29 下午08:17:10
	 */
	public String getMeetingRoom(){
		clashList = manager.getListByTimeAllow( mrId,null);
		String info = "";
		if(clashList!=null&&clashList.size()>0){
			for(int i=0;i<clashList.size();i++){
				Object[] o = (Object[]) clashList.get(i);
				info = info + "会议标题："+o[1].toString()+"<br/>   申请人："+o[2].toString()+"<br/>";
//				info = info + "<div style=\"height: 10px;\"></div>";
				info = info + "开始时间："+o[3].toString()+"<br/>   结束时间："+o[4].toString()+"<br/>";
//				info = info + "<div style=\"height: 20px;\"></div>";
			}
		}
		return renderText(info);
	}
	
/*以下为get set 方法*/
	public void setModel(ToaMeetingroom model) {
		this.model = model;
	}

	public ToaMeetingroom getModel() {
		return model;
	}

	public Page<ToaMeetingroom> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingroom> page) {
		this.page = page;
	}

	@Autowired
	public void setManager(Meetingroom manager) {
		this.manager = manager;
	}
	public String getMrId() {
		return mrId;
	}

	public void setMrId(String mrId) {
		this.mrId = mrId;
	}

	public HashMap<String, String> getStatemap() {
		return statemap;
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

}
