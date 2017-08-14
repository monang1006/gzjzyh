package com.strongit.oa.meetingroom;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMeetingroomApply;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "meetingApply.action", type = ServletActionRedirectResult.class) })
public class MeetingApplyAction extends BaseActionSupport {
	
	private Page<ToaMeetingroomApply> page = new Page<ToaMeetingroomApply>(FlexTableTag.MAX_ROWS, true);
	private String maId;
	private String mrId;
	private String depa;
	ToaMeetingroomApply model = new ToaMeetingroomApply();
	
	private MeetingApplyManager manager;
	private MeetingroomManager roomManager;
	private IUserService userService;
	
	private String termStart;
	private String termEnd;
	private String inputType;
	
	private List roomList;
	private List clashList;
	private List depaList;

	private HashMap<String, String> statemap = new HashMap<String, String>();
	
	public MeetingApplyAction(){
		statemap.put(null, "新申请");
		statemap.put("0", "新申请");
		statemap.put("1", "审批通过");
		statemap.put("2", "审批驳回");
		statemap.put("3", "结束使用");
	}
	
	/**
	 * author:luosy
	 * description: 删除会议室申请记录
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String delete() throws Exception {
		if("".equals(maId)|null==maId){
			renderText("请选择要删除的会议申请！");
			return null;
		}
		ToaMeetingroomApply app = manager.getMeetingApplyByMaId(maId);
		if(ToaMeetingroomApply.APPLY_NEW.equals(app.getMaState())|ToaMeetingroomApply.APPLY_DISALLOW.equals(app.getMaState())){
			manager.delete(maId, new OALogInfo("会议室申请-『删除』："+app.getMaAppstarttime()+"("+app.getToaMeetingroom().getMrName()+")"));
			renderText("reload");
		}else{
			renderText("不能删除已经通过审批的会议申请！");
		}
		return null;
	}

	/**
	 * author:luosy
	 * description:添加 会议室申请单
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String input() throws Exception {
		if(!"".equals(mrId)&&null!=mrId){
			model.setToaMeetingroom(roomManager.getRoomInfoByRoomId(mrId));
		}
		if("".equals(maId)|null==maId){
			String userId = userService.getCurrentUser().getUserId();
			model.setMaDepartment(userService.getUserDepartmentByUserId(userId).getOrgName());
		}
		return "input";
	}
	
	
	/**
	 * author:luosy
	 * description: 提供给会议管理的接口
	 * modifyer:
	 * description:
	 * @return		会议室申请页面
	 * @throws Exception
	 */
	public String applyRoom()throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		
		if(model.getMaMeetingdec()!=null&&!"".equals(model.getMaMeetingdec())){
			model.setMaMeetingdec(URLDecoder.decode(model.getMaMeetingdec(), "utf-8"));
		}
		if(model.getMaEmcee()!=null&&!"".equals(model.getMaEmcee())){
			model.setMaEmcee(URLDecoder.decode(model.getMaEmcee(), "utf-8"));
		}
		String userId = userService.getCurrentUser().getUserId();
		model.setMaDepartment(userService.getUserDepartmentByUserId(userId).getOrgName());
		return "input";
	}
	
	/**
	 * author:luosy
	 * description:查看处理 会议室申请单
	 * modifyer:
	 * description:
	 * @return 查看页面
	 */
	public String view() throws Exception {
		if(!"".equals(maId)&&null!=maId){
			model = manager.getMeetingApplyByMaId(maId);
		}
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(model.getMaAppstarttime());
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(model.getMaAppendtime());
		clashList = manager.getListByTime(startCal, endCal, model.getToaMeetingroom().getMrId(), model.getMaId());
		return "view";
	}

	/**
	 * author:luosy
	 * description:会议室申请单列表
	 * modifyer:
	 * description:
	 * @return 
	 */
	@Override
	public String list() throws Exception {
		if(model.getMaCreaterName()!=null&&!"".equals(model.getMaCreaterName())){
			model.setMaCreaterName(URLDecoder.decode(model.getMaCreaterName(), "utf-8"));
		}
		if(model.getMaMeetingdec()!=null&&!"".equals(model.getMaMeetingdec())){
			model.setMaMeetingdec(URLDecoder.decode(model.getMaMeetingdec(), "utf-8"));
		}
		if(null!=model.getToaMeetingroom()){
			if(model.getToaMeetingroom().getMrName()!=null&&!"".equals(model.getToaMeetingroom().getMrName())){
				model.getToaMeetingroom().setMrName(URLDecoder.decode(model.getToaMeetingroom().getMrName(), "utf-8"));
			}
		}
		page = manager.getApplyByPage(page, model, "");
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	/**
	 * author:luosy
	 * description:保存会议室申请单
	 * modifyer:
	 * description:
	 * @return 查看页面
	 */
	@Override
	public String save() throws Exception {
		manager.saveMeetingApply(model, new OALogInfo("会议室申请-『保存』："+model.getMaMeetingdec()+"("+model.getToaMeetingroom().getMrName()+")-"+statemap.get(model.getMaState())));
		addActionMessage("success");
		return "input";
	}
	
	/**
	 * author:luosy
	 * description:申请单中选择会议室  根据时间段找到各会议室申请状态
	 * modifyer:
	 * description:
	 * @return
	 */
	public String selectRoom() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		roomList = manager.selectRoom(model.getMaAppstarttime(), model.getMaAppendtime());
		return "selectroom";
	}
	
	/**
	 * author:luosy
	 * description:修改调整会议申请单的时间
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String moveApplySchedules() throws Exception {
		model = manager.getMeetingApplyByMaId(maId);
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar cals = Calendar.getInstance();
				cals.setTime(format.parse(termStart));
			model.setMaAppstarttime(cals.getTime());// 开始时间

			Calendar cale = Calendar.getInstance();
			cale.setTime(format.parse(termEnd));
			model.setMaAppendtime(cale.getTime());// 结束时间
		} catch (ParseException e) {
			e.printStackTrace();
		}
		manager.save(model, new OALogInfo("会议室申请-『调整时间』："+model.getMaMeetingdec()+"("+model.getToaMeetingroom().getMrName()+")"));
		renderText("success");
		return null; 
	}

	
	/**
	 * author:luosy
	 * description: 初始化 日程组件的 申请单数据
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String setSchedules() throws Exception {
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(termStart)&&null!=termStart){
			String[] toDate = termStart.split("-"); 
			startCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 00, 00);
		}
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(termEnd)&&null!=termEnd){
			String[] toDate = termEnd.split("-"); 
			endCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 23, 59);
		}
		List applyList = null;
		boolean edit = false;
		if("yes".equals(inputType)){
			edit = true;
		}
		applyList = manager.getListByTime(startCal, endCal,mrId,maId);
		JSONArray array = manager.makeListToJSONArray(applyList, edit);
		return renderText(array.toString());
	}
	
	
	/**
	 * author:luosy
	 * description: 统计会议室使用情况
	 * modifyer:
	 * description:
	 * @return
	 */
	public String applychart() throws Exception {
		String realPath = this.getRequest().getSession().getServletContext().getRealPath("/");
		roomList = manager.getAllRoom();
		depaList = manager.getAllDepa();
		manager.printBarChart(realPath,mrId,depa);
		manager.printPieChart(realPath,mrId,depa);
		return "jfcharts";
	}
	
	/**
	 * author:luosy
	 * description: ajax 重画统计图片
	 * modifyer:
	 * description:
	 * @return
	 */
	public String rePrintChart() throws Exception {
		String realPath = this.getRequest().getSession().getServletContext().getRealPath("/");
//		String realPath = getSession().getServletContext().getResource("/").getPath();
	    
	    if(depa!=null&&!"".equals(depa)){
			try {
				depa = URLDecoder.decode(depa, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		manager.printBarChart(realPath,mrId,depa);
		manager.printPieChart(realPath,mrId,depa);
		renderText("reload");
		return null;
	}
	/**********************以下为 get set 方法***/

	@Autowired
	public void setManager(MeetingApplyManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setRoomManager(MeetingroomManager roomManager) {
		this.roomManager = roomManager;
	}
	
	public String getMaId() {
		return maId;
	}

	public void setMaId(String maId) {
		this.maId = maId;
	}

	public Page<ToaMeetingroomApply> getPage() {
		return page;
	}

	

	public void setModel(ToaMeetingroomApply model) {
		this.model = model;
	}

	public ToaMeetingroomApply getModel() {
		return model;
	}

	public String getMrId() {
		return mrId;
	}

	public void setMrId(String mrId) {
		this.mrId = mrId;
	}

	public List getRoomList() {
		return roomList;
	}

	public String getTermEnd() {
		return termEnd;
	}

	public void setTermEnd(String termEnd) {
		this.termEnd = termEnd;
	}

	public String getTermStart() {
		return termStart;
	}

	public void setTermStart(String termStart) {
		this.termStart = termStart;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public List getClashList() {
		return clashList;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public List getDepaList() {
		return depaList;
	}

	public void setDepa(String depa) {
		this.depa = depa;
	}

	public HashMap<String, String> getStatemap() {
		return statemap;
	}

}
