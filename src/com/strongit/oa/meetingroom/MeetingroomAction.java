package com.strongit.oa.meetingroom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.bo.ListTest;
import com.strongit.oa.bo.ToaMeetingroom;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "meetingroom.action", type = ServletActionRedirectResult.class) })
public class MeetingroomAction extends BaseActionSupport {

	private Page<ToaMeetingroom> page = new Page<ToaMeetingroom>(FlexTableTag.MAX_ROWS, true);
	private String mrId;
	ToaMeetingroom model = new ToaMeetingroom();
	private MeetingroomManager manager;
	private IDictService dictService;
	private List mrTypeList;
	private List<ToaMeetingroom> roomlist;
	private File[] file;
	private HashMap<String, String> statemap = new HashMap<String, String>();
	private String canEdit;
	
	//日程组件的显示样式
	private String displayType;
	//日程组件的初始化日期
	private String setDate;
	
	public MeetingroomAction(){
		statemap.put(null, "正常使用");
		statemap.put("0", "正常使用");
		statemap.put("1", "停止使用");
		statemap.put("2", "已删除");
	}
	
	/**
	 * author:luosy
	 * description:删除会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@Override
	public String delete() throws Exception {
		ToaMeetingroom room = manager.getRoomInfoByRoomId(mrId);
		manager.deleRoomByMrId(room, new OALogInfo("会议室管理-『删除』："+room.getMrName()));
		renderText("reload");
		return null;
	}

	/**
	 * author:luosy
	 * description:跳转到 会议室 编辑页面
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String input() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		mrTypeList = dictService.getItemsByDictValue("MRTYPE");
		if(!"".equals(mrId)&&null!=mrId){
			model = manager.getRoomInfoByRoomId(mrId);
		}
		return INPUT;
	}

	/**
	 * author:luosy
	 * description:查看会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		if(!"".equals(mrId)&&null!=mrId){
			model = manager.getRoomInfoByRoomId(mrId);
		}
		return "view";
	}

	protected void prepareModel() throws Exception {

	}

	/**
	 * author:luosy
	 * description:保存对会议室信息的编辑
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		if(file!=null){
			for(int i=0;i<file.length;i++){
				FileInputStream fils = null;
				try{
					fils = new FileInputStream(file[i]);
					byte[] buf = new byte[(int)file[i].length()];
					fils.read(buf);	
					model.setMrImg(buf);
				}catch (Exception e) {
					addActionMessage("error");
					return input();
				}finally{
					try {
						if(null!=fils){
							fils.close();
						}
					} catch (IOException e) {
						addActionMessage("error");
						return input();
					}
				}
			}
		}
		manager.saveRoomInfo(model, new OALogInfo("会议室管理-『保存』："+model.getMrName()));
		addActionMessage("success");
		return input();
	}
	
	/**
	 * author:luosy
	 * description:会议室列表
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		if(model.getMrName()!=null&&!"".equals(model.getMrName())){
			model.setMrName(URLDecoder.decode(model.getMrName(), "utf-8"));
		}
		if(model.getMrLocation()!=null&&!"".equals(model.getMrLocation())){
			model.setMrLocation(URLDecoder.decode(model.getMrLocation(), "utf-8"));
		}
		if(model.getMrPeople()!=null&&!"".equals(model.getMrPeople())){
			model.setMrPeople(URLDecoder.decode(model.getMrPeople(), "utf-8"));
		}
		if(model.getMrType()!=null&&!"".equals(model.getMrType())){
			model.setMrType(URLDecoder.decode(model.getMrType(), "utf-8"));
		}
		
		manager.getAllRoom(page, model);
		return SUCCESS;
	}
	
	/**
	 * author:luosy
	 * description:跳转到申请会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String selecttree() throws Exception {
		roomlist = manager.getAllRoomList();
		return "selecttree";
	}
	
	/**
	 * author:luosy
	 * description:跳转到安排会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String managertree() throws Exception {
		roomlist = manager.getAllRoomList();
		canEdit = "yes";
		return "selecttree";
	}
	
	/**
	 * author:luosy
	 * description: 跳转到会议室申请日程组件页面
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String selectCalender() throws Exception {
		if(!"".equals(mrId)&&null!=mrId){
			model = manager.getRoomInfoByRoomId(mrId);
		}
		if("".equals(displayType)|null==displayType){
			displayType="week";
		}
		if("".equals(setDate)|null==setDate){
			Calendar now = Calendar.getInstance();
			setDate=String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}
		return "selectcalendar";
	}
	
	
	/**
	 * author:luosy
	 * description:获取会议室信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String getRoomInfo() throws Exception {
		model = manager.getRoomInfoByRoomId(mrId);
		String hasImg="yes";
		if("".equals(model.getMrImg())|null==model.getMrImg()){
			hasImg="";
		}
		renderText(model.getMrName()+","+model.getMrLocation()+","+model.getMrPeople()+","+model.getMrRemark()+","+model.getMrType()+","+hasImg);
		return null;
	}
	
	/**
	 * author:luosy
	 * description: 图片查看
	 * modifyer:
	 * description:
	 * @return
	 */
	public String viewImg() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaMeetingroom room = manager.getRoomInfoByRoomId(mrId);
		response.setContentType("application/x-download");         //windows
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		if(room.getMrImg()!=null){
			response.reset();
			OutputStream output = null;
			try{
				response.setContentType("application/x-download");         //windows
				response.setHeader("Cache-Control","no-store");
				response.setHeader("Pragrma","no-cache");
				response.setDateHeader("Expires",0);
				response.addHeader("Content-Disposition", "attachment;filename=" +
				         new String(room.getMrName().getBytes("gb2312"),"iso8859-1"));
				
			    output = response.getOutputStream();
			    
			    output.write(room.getMrImg());
			    output.flush();
			} catch(Exception e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			} finally {		 
			    if(output != null){
			      try {
					output.close();
				} catch (IOException e) {
					if(logger.isErrorEnabled()){
						logger.error(e.getMessage(),e);
					}
				}
			      output = null;
			    }
			}
			
		}
		return null;
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
	public void setManager(MeetingroomManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public List getMrTypeList() {
		return mrTypeList;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
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

	public List<ToaMeetingroom> getRoomlist() {
		return roomlist;
	}

	public String getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(String canEdit) {
		this.canEdit = canEdit;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

}
