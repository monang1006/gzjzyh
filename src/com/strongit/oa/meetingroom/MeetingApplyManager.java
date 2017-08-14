package com.strongit.oa.meetingroom;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingroom;
import com.strongit.oa.bo.ToaMeetingroomApply;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class MeetingApplyManager {
private GenericDAOHibernate<ToaMeetingroomApply, java.lang.String> meetingApplyDao;
	
	private IUserService userService;
	private MeetingroomManager roomManager;
	

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setRoomManager(MeetingroomManager roomManager) {
		this.roomManager = roomManager;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		meetingApplyDao = new GenericDAOHibernate<ToaMeetingroomApply, java.lang.String>(
				session, ToaMeetingroomApply.class);
	}
	
	/**
	 * author:luosy
	 * description:添加修改会议室申请单
	 * modifyer:
	 * description:
	 * @param model
	 */
	@Transactional
	public void saveMeetingApply(ToaMeetingroomApply model, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			if("".equals(model.getMaId())){
				model.setMaId(null);
			}
			String userId = userService.getCurrentUser().getUserId();
	       TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
			model.setMaCreater(userId);//当前用户
			model.setMaDepartment(userService.getUserDepartmentByUserId(userId).getOrgName());//部门
			model.setMaCreaterName(userService.getUserNameByUserId(userId));
			model.setMaSubmittime(new Date());//申请时间
			
			model.setTopOrgcode(org.getOrgSyscode());
			model.setDepartmentId(org.getOrgId());
			
			//更新申请单状态
			if("".equals(model.getMaState())|null==model.getMaState()){
				model.setMaState(ToaMeetingroomApply.APPLY_NEW);
			}
			
			meetingApplyDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"修改会议室申请单"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 保存申请单对象
	 * modifyer:
	 * description:
	 * @param model
	 */
	@Transactional
	public void save(ToaMeetingroomApply model, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			model.setTopOrgcode(userService.getCurrentUser().getSupOrgCode());
			model.setDepartmentId(userService.getCurrentUser().getOrgId());
			meetingApplyDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存会议室申请单"});
		}
	}
	
	/**
	 * author:luosy
	 * description:删除会议室申请单
	 * modifyer:
	 * description:
	 * @param maId
	 */
	@Transactional
	public void delete(String maId, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			meetingApplyDao.delete(maId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除会议室申请单"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取申请单
	 * modifyer:
	 * description:
	 * @param maId 申请单ID
	 * @return
	 */
	@Transactional(readOnly=true)
	public ToaMeetingroomApply getMeetingApplyByMaId(String maId)throws SystemException,ServiceException{
		try{
			return meetingApplyDao.get(maId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取会议室申请单"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 查找 会议室申请单 分页对象
	 * modifyer:
	 * description:
	 * @param page
	 * @param model
	 * @param type 是否是查看当前用户 all - 所有申请单  self - 我的申请单 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaMeetingroomApply> getApplyByPage(Page<ToaMeetingroomApply> page,ToaMeetingroomApply model,String type) throws SystemException,ServiceException{
		try{
			String currentUserId = userService.getCurrentUser().getUserId();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaMeetingroomApply as t where 1=1 ");
			if("self".equals(type)){
				hql.append(" and t.maCreater = '").append(currentUserId).append("'");
			}
			
			//指定的会议室id
			if(null!=model.getToaMeetingroom()){
				String mrId = model.getToaMeetingroom().getMrId();
				if(!"".equals(mrId)&&null!=mrId){
					hql.append(" and t.toaMeetingroom.mrId like '%").append(FiltrateContent.getNewContent(mrId.trim())).append("%'");
				}
			}
			
			//主持人
			String maEmcee = model.getMaEmcee();
			if(!"".equals(maEmcee)&&null!=maEmcee){
				hql.append(" and t.maEmcee like '%").append(FiltrateContent.getNewContent(maEmcee.trim())).append("%'");
			}
			
			//申请人
			String maCreaterName = model.getMaCreaterName();
			if(!"".equals(maCreaterName)&&null!=maCreaterName){
				hql.append(" and t.maCreaterName like '%").append(FiltrateContent.getNewContent(maCreaterName.trim())).append("%'");
			}
			
			//会议室名称
			if(null!=model.getToaMeetingroom()){
				String mrName = model.getToaMeetingroom().getMrName();
				if(!"".equals(mrName)&&null!=mrName){
					hql.append(" and t.toaMeetingroom.mrName like '%").append(FiltrateContent.getNewContent(mrName.trim())).append("%'");
				}
			}
			
			//发起部门
			String maDepartment = model.getMaDepartment();
			if(!"".equals(maDepartment)&&null!=maDepartment){
				hql.append(" and t.maDepartment like '%").append(FiltrateContent.getNewContent(maDepartment.trim())).append("%'");
			}
			
			//议题描述
			String maMeetingdec = model.getMaMeetingdec();
			if(!"".equals(maMeetingdec)&&null!=maMeetingdec){
				hql.append(" and t.maMeetingdec like '%").append(FiltrateContent.getNewContent(maMeetingdec.trim())).append("%'");
			}
			
			//申请单状态
			String maState = model.getMaState();
			if(!"".equals(maState)&&null!=maState){
				hql.append(" and t.maState like '%").append(FiltrateContent.getNewContent(maState.trim())).append("%'");
			}
			
			 TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
				String orgCode=org.getOrgSyscode();
				String orgid=org.getOrgId();
			  if(userService.isViewChildOrganizationEnabeld()){
				  hql.append(" and t.topOrgcode like'"
						+ orgCode + "%'");
			}
			  else{
				  hql.append(" and t.departmentId ='"
							+ orgid + "'");
			  }
			
			//在某个时间段内出现的日程
			if(model.getMaAppstarttime()!=null&&model.getMaAppendtime()!=null){
				Calendar ts = Calendar.getInstance();
				ts.setTime(model.getMaAppstarttime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				Calendar te = Calendar.getInstance();
				te.setTime(model.getMaAppendtime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				Object[] obj = new Object[6];
				obj[0] = ts.getTime();
				obj[1] = te.getTime();
				obj[2] = ts.getTime();
				obj[3] = ts.getTime();
				obj[4] = te.getTime();
				obj[5] = te.getTime();
				hql.append("and (( t.maAppstarttime>? and t.maAppendtime<? ) or ( t.maAppstarttime<? and t.maAppendtime>?) " +
				" or ( t.maAppstarttime<? and t.maAppendtime>?) ) ");
				return meetingApplyDao.find(page,hql.toString()+" order by t.maSubmittime desc ",obj);
			}else if(null!=model.getMaSubmittime()){//申请日期
				Calendar ts = Calendar.getInstance();
				ts.setTime(model.getMaSubmittime());
				ts.set(Calendar.HOUR_OF_DAY, 0);
				ts.set(Calendar.MINUTE, 0);
				Calendar te = Calendar.getInstance();
				te.setTime(model.getMaSubmittime());
				te.set(Calendar.HOUR_OF_DAY, 23);
				te.set(Calendar.MINUTE, 59);
				
				Object[] obj = new Object[2];
				obj[0] = ts.getTime();
				obj[1] = te.getTime();
				hql.append("and ( t.maSubmittime>? and t.maSubmittime<?  ) ");
				return meetingApplyDao.find(page,hql.toString()+" order by t.maSubmittime desc ",obj);
			}else{
				return meetingApplyDao.find(page, hql.toString()+" order by t.maSubmittime desc ");
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"会议室申请单列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:	选择会议室到申请单
	 * modifyer:
	 * description:
	 * @param stime
	 * @param etime
	 * @return
	 */
	@Transactional(readOnly=true)
	public List selectRoom(Date stime,Date etime) throws SystemException,ServiceException{
		try{
			Calendar ts = Calendar.getInstance();
			ts.setTime(stime);
			Calendar te = Calendar.getInstance();
			te.setTime(etime);
			
			Object[] obj = new Object[6];
			obj[0] = ts.getTime();
			obj[1] = te.getTime();
			obj[2] = ts.getTime();
			obj[3] = ts.getTime();
			obj[4] = te.getTime();
			obj[5] = te.getTime();
			
			//在给定时段内 没有 申请记录 的会议室
			List list = meetingApplyDao.find("select distinct t.mrId, t.mrName, t.mrType, t.mrState , '该会议室空闲' " +
					" from  ToaMeetingroom as t where t.mrState like "+ToaMeetingroom.ROOM_OPEN+"  ");
			
			//在给定时段内 有 已通过的申请 的会议室
			List allowList = meetingApplyDao.find("select distinct t.toaMeetingroom.mrId, t.toaMeetingroom.mrName, t.toaMeetingroom.mrType,'==' ,'该时段有冲突' " +
					" from ToaMeetingroomApply as t where 1=1 and t.maState="+ToaMeetingroomApply.APPLY_ALLOW+
					"and (( t.maAppstarttime>? and t.maAppendtime<? ) or ( t.maAppstarttime<? and t.maAppendtime>?) " +
					" or ( t.maAppstarttime<? and t.maAppendtime>?) )",obj);
			
			//在给定时段内 有 未通过的申请 的会议室
			List newList = meetingApplyDao.find("select distinct t.toaMeetingroom.mrId, t.toaMeetingroom.mrName, t.toaMeetingroom.mrType, '!=' ,'该时段已被申请' " +
					" from ToaMeetingroomApply as t where 1=1 and t.maState like "+ToaMeetingroomApply.APPLY_NEW+
					"and (( t.maAppstarttime>? and t.maAppendtime<? ) or ( t.maAppstarttime<? and t.maAppendtime>?) " +
					" or ( t.maAppstarttime<? and t.maAppendtime>?) )",obj);
			
			//去处重复的记录
			for(int i=0;i<allowList.size();i++){
				Object[] obj1 = (Object[])allowList.get(i);
				for(int j=0;j<newList.size();j++){
					Object[] obj2 = (Object[])newList.get(j);
					if(obj1[0].equals(obj2[0])){
						newList.remove(j);
						j--;
						break;
					}
				}
			}
			newList.addAll(allowList);
			
			for(int i=0;i<list.size();i++){
				Object[] obj1 = (Object[])list.get(i);
				for(int j=0;j<newList.size();j++){
					Object[] obj2 = (Object[])newList.get(j);
					if(obj1[0].equals(obj2[0])){
						list.remove(i);
						i--;
						break;
					}
				}
			}
			list.addAll(newList);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"选择会议室-查询会议室列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:根据时间段得到对应的会议申请信息列表
	 * modifyer:
	 * description:
	 * @param startCal
	 * @param endCal
	 * @param mrId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List getListByTime(Calendar startCal, Calendar endCal,String mrId,String maId)throws SystemException,ServiceException{
		try{
			StringBuffer addhql = new StringBuffer();
			addhql.append("1=1 ");
			if (!"".equals(mrId) && mrId != null) {
				addhql.append(" and t.toaMeetingroom.mrId = '" + mrId + "' ");
			}
			if (!"".equals(maId) && maId != null) {
				addhql.append(" and t.maId not like '" + maId + "' ");
			}
			
			Object[] obj = new Object[6];
			obj[0] = startCal.getTime();
			obj[1] = endCal.getTime();
			obj[2] = startCal.getTime();
			obj[3] = startCal.getTime();
			obj[4] = endCal.getTime();
			obj[5] = endCal.getTime();
			
			return meetingApplyDao.find("select t.maId,t.maMeetingdec, t.maCreaterName, t.maAppstarttime,t.maAppendtime,t.maState,t.toaMeetingroom.mrId,t.toaMeetingroom.mrName, t.maSubmittime " +
					"from ToaMeetingroomApply as t where " + addhql.toString()+
					" and ( ( t.maAppstarttime>? and t.maAppendtime<? ) or ( t.maAppstarttime<? and t.maAppendtime>?) " +
					" or ( t.maAppstarttime<? and t.maAppendtime>?) )  ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据时间段得到对应的会议申请信息列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:将日程事务列表转化为jsonArray
	 * modifyer:
	 * description:
	 * @param calList<Object[]> 日程列表 maId,maMeetingdec,maCreaterName,maAppstarttime,maAppendtime,maState,mrId, mrName
	 * @return 	日程组件的jsonArray
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public JSONArray makeListToJSONArray(List applyList, boolean canEdit)throws SystemException,ServiceException{
		try{
			JSONArray array = new JSONArray();
			if(null==applyList){
				return array;
			}
			String title="" ;
			for (int i = 0; i < applyList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				JSONObject start = new JSONObject();
				JSONObject finish = new JSONObject();
				
				Object[] objs = (Object[])applyList.get(i);
				
				//title格式->  会议室名（用户：议题*状态）
				title = objs[7]+"（"+objs[2]+"："+objs[1]+"）";
				if(ToaMeetingroomApply.APPLY_ALLOW.equals(objs[5])){
					title += "[审批通过]";
				}else if(ToaMeetingroomApply.APPLY_DISALLOW.equals(objs[5])){
					title += "[审批驳回]";
				}else if(ToaMeetingroomApply.APPLY_END.equals(objs[5])){
					title += "[使用结束]";
				}else{
					title += "[未审批]";
				}

				
				boolean publicity = false;
				if(ToaMeetingroomApply.APPLY_ALLOW.equals(objs[5].toString())){
					publicity = true;
				}
				
				//设置活动开始结束时间
				Calendar s = Calendar.getInstance(); //calStartTime
				s.setTime((Date)objs[3]);
				Calendar e = Calendar.getInstance(); //calEndTime
				e.setTime((Date)objs[4]);
				
				start.put("year", s.get(Calendar.YEAR));
				start.put("month", s.get(Calendar.MONTH));
				start.put("day", s.get(Calendar.DATE));
				start.put("hour", s.get(Calendar.HOUR_OF_DAY));
				start.put("min", s.get(Calendar.MINUTE));
				finish.put("year", e.get(Calendar.YEAR));
				finish.put("month", e.get(Calendar.MONTH));
				finish.put("day", e.get(Calendar.DATE));
				finish.put("hour", e.get(Calendar.HOUR_OF_DAY));
				finish.put("min", e.get(Calendar.MINUTE));
				
				jsonObj.put("id", objs[0]);//calendarId
				jsonObj.put("description",title);//calTitle
				jsonObj.put("publicity", publicity);
				jsonObj.put("start", start);
				jsonObj.put("finish", finish);
				jsonObj.put("edit",canEdit);
				jsonObj.put("state", objs[5]);
				array.add(jsonObj);
			}
			
			return array;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"将会议申请事务列表转化为jsonArray"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取全部状态下的会议室
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaMeetingroom> getAllRoom()throws SystemException,ServiceException{
		try{
			List<ToaMeetingroom> list = roomManager.getAllStateRoom();
			if(list==null){
				return new ArrayList();
			}else{
				return list;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取全部状态的会议室"});
		}
	}
	/**
	 * author:luosy
	 * description:获取全部申请了会议室的部门
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getAllDepa()throws SystemException,ServiceException{
		try{
			List list = meetingApplyDao.find("select distinct t.maDepartment as depaName " +
			" from  ToaMeetingroomApply as t where t.maDepartment is not null ");
			if(list==null){
				return new ArrayList();
			}else{
				return list;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取申请过会议室的部门信息"});
		}
	}

	/**
	 * author:luosy
	 * description:创建3D饼状图(pie chart)数据集
	 * modifyer:
	 * description:
	 * @param realPath 绝对路径
	 * @param mrId	会议室id
	 * @param depa	部门名称
	 */
	@Transactional(readOnly=true)
	public void printPieChart(String realPath,String mrId,String depa) throws SystemException,ServiceException{
		try{
			FileOutputStream pieJpg = null; 	// 生成图片是用到的输出流
			
			DefaultPieDataset piedataset = new DefaultPieDataset();
			List<Object[]> dataList = this.getDataForChart(mrId,depa);
			for(Object[] obj:dataList){
				piedataset.setValue(obj[0].toString(), new Double(new Double(obj[1].toString()).doubleValue()));
			}
			
			String pieTit = "会议室申请统计图";
			if(!"".equals(mrId)&&null!=mrId){
				String mrName = roomManager.getRoomInfoByRoomId(mrId).getMrName();
				pieTit = "会议室["+mrName+"]被各部门使用的情况";
			}
			if(!"".equals(depa)&&null!=depa){
				pieTit = "部门["+depa+"]对各会议室使用的情况";
			}
			
			JFreeChart chart = ChartFactory.createPieChart3D("会议室申请统计图"
					, piedataset
					, true
					, true
					, false
			);
			
			// 使下说明标签字体清晰
			// chart.setTextAntiAlias(false);
			// 图片背景色
			chart.setBackgroundPaint(Color.white);
			// 设置图标题的字体重新设置title
			Font font = new Font("隶书", Font.BOLD, 25);
			TextTitle title = new TextTitle(pieTit);//("会议室统计");
			title.setFont(font);
			chart.setTitle(title);
			
			PiePlot3D plot = (PiePlot3D) chart.getPlot();
			// 图片中显示百分比:默认方式
			
			// 指定饼图轮廓线的颜色
			// plot.setBaseSectionOutlinePaint(Color.BLACK);
			// plot.setBaseSectionPaint(Color.BLACK);
			
			// 设置无数据时的信息
			plot.setNoDataMessage("无对应的数据，请重新查询。");
			
			// 设置无数据时的信息显示颜色
			plot.setNoDataMessagePaint(Color.red);
			
			// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
					"{0}={1}({2})", NumberFormat.getNumberInstance(),
					new DecimalFormat("0.00%")));
			// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
			plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
			"{0}={1}({2})"));
			
			plot.setLabelFont(new Font("SansSerif", Font.TRUETYPE_FONT, 12));
			
			// 指定图片的透明度(0.0-1.0)
			plot.setForegroundAlpha(0.65f);
			// 指定显示的饼图上圆形(false)还椭圆形(true)
			plot.setCircular(false, true);
			
			// 设置第一个 饼块section 的开始位置，默认是12点钟方向
			plot.setStartAngle(90);
			
			String path = realPath+"/oa/image/meetingroom/DrawPie.jpg";
			
			try {
				pieJpg = new FileOutputStream(path);
				ChartUtilities.writeChartAsJPEG(pieJpg, 100, chart, 700, 450, null);
			} catch (Exception e) {
			} finally {
				try {
					if(pieJpg!=null){
						pieJpg.close();
					}
				} catch (Exception e) {
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"创建饼图"});
		}

	}
	
	/**
	 * author:luosy
	 * description:创建3D柱状图(Bar chart)数据集
	 * modifyer:
	 * description:
	 * @param realPath 绝对路径
	 * @param mrId	会议室id
	 * @param depa	部门名称
	 */
	@Transactional(readOnly=true)
	public void printBarChart(String realPath,String mrId,String depa) throws SystemException,ServiceException{
		try{
			FileOutputStream barJpg = null;
			
			DefaultCategoryDataset barData = new DefaultCategoryDataset();
			List<Object[]> dataList = this.getDataForChart(mrId,depa);
			for(Object[] obj:dataList){
				barData.addValue(new Double(new Double(obj[1].toString()).doubleValue()),obj[0].toString(), obj[0].toString());
			}
			
			String pieTit = "会议室申请统计图";
			String rangeAxisTitle = "会议室";
			if(!"".equals(mrId)&&null!=mrId){
				String mrName = roomManager.getRoomInfoByRoomId(mrId).getMrName();
				pieTit = "会议室["+mrName+"]被各部门使用的情况";
				rangeAxisTitle = "发起部门";
			}
			if(!"".equals(depa)&&null!=depa){
				pieTit = "部门["+depa+"]对各会议室使用的情况";
			}
			
			JFreeChart chart = ChartFactory.createBarChart3D(pieTit, rangeAxisTitle, "使用次数", barData, PlotOrientation.VERTICAL, true, true, false);
			
			chart.getTitle().setFont(new Font("隶书", Font.BOLD, 25));
			chart.setBackgroundPaint(Color.white);//new Color(0xECF4F9));   //设定背景色
			CategoryPlot plot = chart.getCategoryPlot();    ////获得 plot：CategoryPlot			
			NumberAxis numberAxis=(NumberAxis)plot.getRangeAxis();//取得纵轴   
			//numberAxis.setAutoRangeIncludesZero(false);
			
			//设置柱图曲线图纵坐标间距
			numberAxis.setAutoTickUnitSelection(false);	
			numberAxis.setTickUnit(new NumberTickUnit(10));	
			BarRenderer renderer = (BarRenderer) plot.getRenderer();//获得renderer 注意这里是下嗍造型到BarRenderer
			renderer.setDrawBarOutline(false);  // Bar的外轮廓线不画
			//设置柱子上显示值
			//renderer.setIncludeBaseInRange(true); 
			renderer.setMaximumBarWidth(0.08); 
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator()); 
			renderer.setBaseItemLabelsVisible(true);
			plot.setRenderer(renderer); 
			
			String path = realPath+"/oa/image/meetingroom/DrawBar.jpg";
			
			try {
				barJpg = new FileOutputStream(path);
				ChartUtilities.writeChartAsJPEG(barJpg, 100, chart, 700, 450, null);
			} catch (Exception e) {
			} finally {
				try {
					if(barJpg!=null){
						barJpg.close();
					}
				} catch (Exception e) {
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"创建柱状图"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取 统计数据
	 * modifyer:
	 * description:
	 * @param mrId
	 * @param depa
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<Object[]> getDataForChart(String mrId, String depa)throws SystemException,ServiceException{
		try{
			List<Object[]> list = new ArrayList<Object[]>();
			if(!"".equals(mrId)&&null!=mrId){//根据会议室进行统计
				List depaList = this.getAllDepa();
				for(Object obj:depaList){
					List count = meetingApplyDao.find("select count(*)  from  ToaMeetingroomApply as t " +
							"where t.maState like "+ToaMeetingroomApply.APPLY_END+" and t.toaMeetingroom.mrId=? and t.maDepartment=? ",mrId,obj.toString());
					String c = count.get(0).toString();
					Object[] o = {obj,c};
					list.add(o);
				}
				return list;
			}
			if(!"".equals(depa)&&null!=depa){//根据部门进行统计
				List<ToaMeetingroom> roomList = this.getAllRoom();
				for(ToaMeetingroom obj:roomList){
					List count = meetingApplyDao.find("select count(*)  from  ToaMeetingroomApply as t " +
							"where t.maState like "+ToaMeetingroomApply.APPLY_END+" and t.toaMeetingroom.mrId=? and t.maDepartment=? ",obj.getMrId(),depa);
					String c = count.get(0).toString();
					Object[] o = {obj.getMrName(),c};
					list.add(o);
				}
				return list;
			}
			if(("".equals(mrId)|null==mrId)&&("".equals(depa)|null==depa)){//统计所有信息
				List<ToaMeetingroom> roomList = this.getAllRoom();
				for(ToaMeetingroom obj:roomList){
					List count = meetingApplyDao.find("select count(*)  from  ToaMeetingroomApply as t " +
							"where t.maState like "+ToaMeetingroomApply.APPLY_END+" and t.toaMeetingroom.mrId=? ",obj.getMrId());
					String c = count.get(0).toString();
					Object[] o = {obj.getMrName(),c};
					list.add(o);
				}
				return list;
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取统计数据"});
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
