package com.strongit.oa.attendance.gather;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import oracle.jdbc.driver.OracleConnection;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendStatistic;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.util.FieldNameValue;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 考勤汇总manager
 * @author 胡丽丽
 * @date:2009-11-25
 * @version  2.0.4
 */
@Service
@Transactional
public class GatherManager extends BaseManager {

	private GenericDAOHibernate<ToaAttendStatistic, String> gatherDao;
	
	private InfoItemManager itemManager;					//信息项manager
	
	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		gatherDao = new GenericDAOHibernate<ToaAttendStatistic, String>(sessionFactory,ToaAttendStatistic.class);
	}
	public void save(ToaAttendStatistic model)throws SystemException,ServiceException{
		try {
			gatherDao.save(model);
		} catch (DAOException e) {
			 throw new ServiceException(MessagesConst.save_error,
					new Object[]{"考勤汇总保存出错！"});
		}
	}
	public void save(List<ToaAttendStatistic> statisticList)throws SystemException,ServiceException{
		try {
			gatherDao.save(statisticList);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"考勤汇总列表保存出错！"});
		}
	}
	/**
	 * 格式化时间
	 * @param time
	 * @return
	 */
	public String  gettime(Date time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		String time1=df.format(time);
		return time1;
	}

	/**
	 * 保存考勤汇总
	 * @author 胡丽丽
	 * @date 2009-12-24
	 * @param recordlist
	 * @param map
	 * @param beginTime
	 * @param endTime
	 * @param sizeuser 用户数量
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public void save(List<ToaAttendRecord> recordlist,Map<String, BigDecimal> map,Date beginTime,Date endTime,int sizeuser)throws SystemException,ServiceException{
		try {
			String userid="";//用户ID
			String username="";//用户名称
			UUIDGenerator uuidgenerate=new UUIDGenerator();		//uuid生成器生成主键值
			Map<String, String> userMap=new HashMap<String, String>();//正在计算中的用户
			Map<String, String> saveUserMap=new HashMap<String, String>();//已汇总用户
			List<ToaSysmanageProperty> columnList=new ArrayList<ToaSysmanageProperty>();
			columnList=itemManager.getAllCreatedItemsByValue("T_OA_ATTEND_STATISTICS");//信息项
			List<List<ToaSysmanageProperty>> list=new ArrayList<List<ToaSysmanageProperty>>();
			//插入的sql语句
			String sqlnew="insert into T_OA_ATTEND_STATISTICS (";
			for(ToaSysmanageProperty property:columnList){//构成sql语句
				sqlnew=sqlnew+property.getInfoItemField()+",";
			}
			sqlnew=sqlnew.substring(0,sqlnew.length()-1)+") values (";
			int i=0;//判断第一个用户是否计算完毕
			for(int j=0;j<sizeuser;j++){//循环要汇总的用户 
				String sql=sqlnew;
				for(ToaAttendRecord record:recordlist){//循环考勤信息
					if(userMap.get(record.getUserId())==null){
						i++;
						if(i==2){//是否是第二个人的考勤明细
							i=0;
							break;
						}
						if(saveUserMap.get(record.getUserId())!=null){
							continue;
						}
						userid=record.getUserId();//当前正在汇总的用户ID
						username=record.getUserName();
						for(ToaSysmanageProperty property:columnList)
						{

							String primaryKeyValue =(String) uuidgenerate.generate();	//获取对应表的主键字段
							if("STATISTICS_ID".equals(property.getInfoItemField())){
								property.setInfoItemValue(primaryKeyValue);
								continue;
							}
							if("USER_ID".equals(property.getInfoItemField())){//用户ID
								property.setInfoItemValue(record.getUserId());
								continue;
							}
							if("USER_NAME".equals(property.getInfoItemField())){//用户名
								property.setInfoItemValue(record.getUserName());
								continue;
							}
							if("ORG_NAME".equals(property.getInfoItemField())){//机构名称
								property.setInfoItemValue(record.getOrgName());
								continue;
							}
							if("ORG_ID".equals(property.getInfoItemField())){//机构ID
								property.setInfoItemValue(record.getOrgId());
								continue;
							}
							if("STATISTICS_STIME".equals(property.getInfoItemField())){//开始时间
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
								String time=df.format(beginTime);
								property.setInfoItemValue(gettime(beginTime));
								continue;
							}
							if("STATISTICS_ETIME".equals(property.getInfoItemField())){//结束时间
								property.setInfoItemValue(gettime(endTime));
								continue;
							}
							if("STATISTICS_TIME".equals(property.getInfoItemField())){//汇总时间
								property.setInfoItemValue(gettime(new Date()));
								continue;
							}
							if("SHOULD_ATTEND_DAYS".equals(property.getInfoItemField())){//应出勤时间
								if(record.getShouldAttendHours()==null){
									property.setInfoItemValue("0");
								}else{
									property.setInfoItemValue(record.getShouldAttendHours().toString());
								}
								continue;
							}
							if("LATER_TIMES".equals(property.getInfoItemField())){//迟到次数
									if(record.getAttendLaterTime()!=null&&record.getAttendLaterTime().intValue()!=0){
										property.setInfoItemValue(((Object)(Integer.parseInt(property.getInfoItemValue())+1)).toString());
									}else{
										property.setInfoItemValue("0");
									}

								continue;
							}
							if("LATER_SUM_TIME".equals(property.getInfoItemField())){
								if(record.getAttendLaterTime()!=null){
								property.setInfoItemValue(((Object)record.getAttendLaterTime()).toString());
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							//早退
							if("EARLY_TIMES".equals(property.getInfoItemField())){
								if(record.getAttendEarlyTime()!=null&&record.getAttendEarlyTime().intValue()!=0){
									property.setInfoItemValue(((Object)(Integer.parseInt(property.getInfoItemValue())+1)).toString());
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							if("EARLY_SUM_TIME".equals(property.getInfoItemField())){
								if(record.getAttendEarlyTime()!=null){
								if(record.getAttendEarlyTime()!=null&&record.getAttendEarlyTime().intValue()!=0){
									property.setInfoItemValue(((Object)record.getAttendEarlyTime()).toString());
								}
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							//应出勤
							if("SHOULD_ATTEND_DAYS".equals(property.getInfoItemField())){
									if(record.getShouldAttendHours()!=null){
									property.setInfoItemValue(((Object)record.getShouldAttendHours()).toString());
									}else{
										property.setInfoItemValue("0");
									}
								
								continue;
							}
							//缺勤
							if("ABSENCE_TIME".equals(property.getInfoItemField())){
								if("0".equals(record.getIsCalcuAbsence())){
									if(record.getAbsenceHours()!=null){
									property.setInfoItemValue(((Object)record.getAbsenceHours()).toString());
									}else{
										property.setInfoItemValue("0");
									}
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							if("OVERWORK_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/加班")!=null){
								property.setInfoItemValue(((Object)map.get(record.getUserId()+"/加班")).toString());
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							if("SKIP_SUM_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/旷工")!=null){
								property.setInfoItemValue(((Object)map.get(record.getUserId()+"/旷工")).toString());
								}else{
									property.setInfoItemValue("0");
								}
								continue;
							}
							if("ERRAND_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/出差")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/出差")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("OUT_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/外出")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/外出")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("REST_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/调休")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/调休")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("AFFAIR_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/事假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/事假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("ILL_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/病假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/病假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("INJURE_TIME".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/工伤假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/工伤假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("FUNERAL_LEAVE".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/丧假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/丧假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("ANNUAL_LEAVE".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/年假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/年假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("WEDDING_LEAVE".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/婚假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/婚假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("MATEMITY_LEAVE".equals(property.getInfoItemField())){
								if(map.get(record.getUserId()+"/产假")!=null)
								    property.setInfoItemValue(((Object)map.get(record.getUserId()+"/产假")).toString());
								else
									property.setInfoItemValue("0");
								continue;
							}
							if("AUTI_STATE".equals(property.getInfoItemField())){
								property.setInfoItemValue("0");
								continue;
							}
						}
						userMap.put(record.getUserId(),record.getUserName());
					}
					else{
						for(ToaSysmanageProperty property:columnList)
						{

							if("SHOULD_ATTEND_DAYS".equals(property.getInfoItemField())){//应出勤时间
								if(record.getShouldAttendHours()!=null){
									property.setInfoItemValue(((Object)(Float.parseFloat(property.getInfoItemValue())+record.getShouldAttendHours().floatValue())).toString());
								}
								continue;
							}
							if("LATER_TIMES".equals(property.getInfoItemField())){//迟到次数
								if(record.getAttendLaterTime()!=null&&record.getAttendLaterTime().intValue()!=0){
									property.setInfoItemValue(((Object)(Integer.parseInt(property.getInfoItemValue())+1)).toString());
								}
								continue;
							}
							if("LATER_SUM_TIME".equals(property.getInfoItemField())){
								if(record.getAttendLaterTime()!=null&&record.getAttendLaterTime().intValue()!=0){
									property.setInfoItemValue(((Object)(Float.parseFloat(property.getInfoItemValue().toString())+record.getAttendLaterTime().floatValue())).toString());
								}
								continue;
							}
							//早退
							if("EARLY_TIMES".equals(property.getInfoItemField())){
								if(record.getAttendEarlyTime()!=null&&record.getAttendEarlyTime().intValue()!=0){
									property.setInfoItemValue(((Object)(Integer.parseInt(property.getInfoItemValue())+1)).toString());
								}
								continue;
							}
							if("EARLY_SUM_TIME".equals(property.getInfoItemField())){
								if(record.getAttendEarlyTime()!=null&&record.getAttendEarlyTime().intValue()!=0){
									property.setInfoItemValue(((Object)(Float.parseFloat(property.getInfoItemValue().toString())+record.getAttendEarlyTime().floatValue())).toString());
								}
								continue;
							}
							if("SHOULD_ATTEND_DAYS".equals(property.getInfoItemField())){
								if(record.getAbsenceHours()!=null){
									property.setInfoItemValue(((Object)(Float.parseFloat(property.getInfoItemValue().toString())+record.getShouldAttendHours().floatValue())).toString());
								}
							
							continue;
						}
							//缺勤
							if("ABSENCE_TIME".equals(property.getInfoItemField())){
								if("0".equals(record.getIsCalcuAbsence())){
									if(record.getAbsenceHours()!=null){
										property.setInfoItemValue(((Object)(Float.parseFloat(property.getInfoItemValue().toString())+record.getAbsenceHours().floatValue())).toString());
									}
								}
								continue;
							}
						}
					}
				}
				for(ToaSysmanageProperty pro:columnList){
					if("0".equals(pro.getInfoItemDatatype())||"1".equals(pro.getInfoItemDatatype())||"3".equals(pro.getInfoItemDatatype())||"4".equals(pro.getInfoItemDatatype())||"5".equals(pro.getInfoItemDatatype())||"13".equals(pro.getInfoItemDatatype())||"14".equals(pro.getInfoItemDatatype())||"15".equals(pro.getInfoItemDatatype())){//字符串类型
						sql=sql+"'"+pro.getInfoItemValue()+"',";
					}else if("2".equals(pro.getInfoItemDatatype())){
						sql=sql+pro.getInfoItemValue()+",";
					}else if("6".equals(pro.getInfoItemDatatype())){
						sql=sql+" to_date('"+pro.getInfoItemValue()+"','yyyy-MM-dd'),";
					}
				}
				sql=sql.substring(0,sql.length()-1);
				sql=sql+")";
				//this.delete(userid, gettime(beginTime), gettime(endTime));
				if(saveUserMap.get(userid)==null){
					  int count=this.saveinfo(sql);
				if(count>0)
					saveUserMap.put(userid, username);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"考勤汇总列表保存出错！"});
		}
	}
	/**
	 * 增删改数据
	 */
	public int saveinfo(String sql){
		try {
			Connection conn=gatherDao.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			int i=stmt.executeUpdate(sql.toString());//执行插入语句
			conn.commit();   
			stmt.close();
			conn.close();
			return i;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return 0;
	}
	/**
	 * 根据用户ID和汇总时间段删除汇总信息
	 * @author  胡丽丽
	 * @date:2009-11-25
	 * @param userid  用户ID
	 * @param beginTime  开始时间
	 * @param endTime  结束时间
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String userid,String beginTime, String endTime)throws SystemException,ServiceException{
		try {
			  String sql="delete from t_oa_attend_statistics t where t.user_id='"+userid
			         +"' and t.STATISTICS_STIME= to_date('"+beginTime+"','yyyy-MM-dd')"
			         +" and t.STATISTICS_ETIME=to_date('"+endTime+"','yyyy-MM-dd')";
			  this.saveinfo(sql);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[]{"根据ID删除汇总信息出错！"});
		}
	}
	/**
	 * 根据用户ID和汇总时间段查看汇总信息
	 * @author  胡丽丽
	 * @date:2009-11-25
	 * @param userid  用户ID
	 * @param beginTime  开始时间
	 * @param endTime  结束时间
	 * @throws SystemException
	 * @throws ServiceException
	 * @return 记录数量
	 */
	public int selectCountByUserId(String userid,Date beginTime, Date endTime)throws SystemException,ServiceException{
		int count=0;	
		String begin=gettime(beginTime);
		String end=gettime(endTime);
		String sql="select count(t.statistics_id) from t_oa_attend_statistics t where t.user_id='"+userid
			         +"' and t.AUTI_STATE<>'0' and t.STATISTICS_STIME= to_date('"+begin+"','yyyy-MM-dd')"
			         +" and t.STATISTICS_ETIME=to_date('"+end+"','yyyy-MM-dd')";
			  try {
					Connection conn=gatherDao.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet set=stmt.executeQuery(sql);
					
					while(set.next()){
						count=set.getInt(1);
					}
					set.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return count;
		
	}
	/**
	 * 根据汇总ID获取用户汇总开始时间和结束时间
	 * @author 胡丽丽
	 * @date 2009-12-24
	 * @param id
	 * @return
	 */
	public Object[] getGatherById(String id){
		String sql="select t.user_id,t.statistics_stime,t.statistics_etime,t.user_name from t_oa_attend_statistics t where t.statistics_id='"+id+"'";
		
		
		Object[] obj=new Object[4];
		try {
			Connection conn=gatherDao.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet set=stmt.executeQuery(sql);
			while(set.next()){
				obj[0]=set.getString("user_id");
				obj[1]=set.getDate("statistics_stime");
				obj[2]=set.getDate("statistics_etime");
				obj[3]=set.getString("user_name");
			}
			set.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	/**
	 * 获取汇总报表信息
	 * @author 胡丽丽
	 * @date 2010-03-08
	 * @return
	 */
	public List<ToaReportBean> getReportBeanList(String orgId,String year){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		List<ToaReportBean> recordlist=new ArrayList<ToaReportBean>();
			StringBuffer consql=new StringBuffer(" select ORG_NAME,USER_NAME,STATISTICS_STIME,STATISTICS_ETIME")
			.append(",LATER_TIMES,LATER_SUM_TIME,EARLY_TIMES,EARLY_SUM_TIME,OVERWORK_TIME,ABSENCE_TIME")
			.append(",SKIP_SUM_TIME,ERRAND_TIME,OUT_TIME,REST_TIME,AUTI_STATE,AFFAIR_TIME,ILL_TIME")
			.append(",INJURE_TIME,FUNERAL_LEAVE,ANNUAL_LEAVE,WEDDING_LEAVE,MATEMITY_LEAVE")
			.append(" FROM T_OA_ATTEND_STATISTICS where 1=1 and AUTI_STATE='2'");
			if(orgId!=null&&!"".equals(orgId)){
				consql.append(" and ORG_ID='")
				.append(orgId)
				.append("'");
			}
			if(year!=null&!"".equals(year)){//汇总开始时间
				
				consql.append(" and to_char(STATISTICS_TIME,'yyyy') like ")
				.append("'"+year+"' ");
//				consql.append(" and to_char(STATISTICS_ETIME,'yyyy') <=")
//				.append(year);
			}
			consql.append( " order by ORG_ID desc,USER_ID desc");
			try {
				Connection conn=gatherDao.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet set=stmt.executeQuery(consql.toString());
				while(set.next()){
					String[] column=new String[45];
					column[0]=set.getString(1);
					column[1]=set.getString(2);
					column[2]=sdf.format(set.getDate(3));
					column[3]=sdf.format(set.getDate(4));
					column[4]=String.valueOf(set.getBigDecimal(5));
					column[5]=String.valueOf(set.getBigDecimal(6));
					column[6]=String.valueOf(set.getBigDecimal(7));
					column[7]=String.valueOf(set.getBigDecimal(8));
					column[8]=String.valueOf(set.getBigDecimal(9));
					column[9]=String.valueOf(set.getBigDecimal(10));
					column[10]=String.valueOf(set.getBigDecimal(11));
					column[11]=String.valueOf(set.getBigDecimal(12));
					column[12]=String.valueOf(set.getBigDecimal(13));
					column[13]=String.valueOf(set.getBigDecimal(14));
					column[14]=set.getString(15);
					column[15]=String.valueOf(set.getBigDecimal(16));
					column[16]=String.valueOf(set.getBigDecimal(17));
					column[17]=String.valueOf(set.getBigDecimal(18));
					column[18]=String.valueOf(set.getBigDecimal(19));
					column[19]=String.valueOf(set.getBigDecimal(20));
					column[20]=String.valueOf(set.getBigDecimal(21));
					column[21]=String.valueOf(set.getBigDecimal(22));
					
					ToaReportBean reportBean=new ToaReportBean(column);
					recordlist.add(reportBean);
				}
				set.close();
				stmt.close();
				conn.close();
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return recordlist;
	}
	/**
	 * 提交工作流处理
	 * 这里覆盖父类的方法，将保存电子表单数据和提交工作流处理一并处理
	 * @param formId 表单ID
	 * @param workflowName 流程名称
	 * @param businessId 业务数据id
	 * @param businessName 标题
	 * @param taskActors 下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param transitionName 迁移线名称
	 */
	public String handleWorkflow(String formId, String workflowName,
			String businessId, String businessName, String[] taskActors,
			String tansitionName, String concurrentTrans, String sugguestion,String formData)throws SystemException,ServiceException {
		try {
			if(businessId == null || "".equals(businessId)){//新建表单数据-->提交工作流
				/*if(formData != null && !"".equals(formData)){
					businessId = super.saveForm(formData);
				}else{
					if(formData == null){
						System.err.println("获取表单数据失败！");
						throw new SystemException("获取表单数据失败！");
					}
				}*/
				
			}else{//草稿箱中提交工作流
				//不保存电子表单数据
			}
			super.handleWorkflow(formId, workflowName, businessId, businessName, taskActors, tansitionName, concurrentTrans, sugguestion);
			return businessId;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"工作流处理"});
		}
	}
	/**
	 * author:胡丽丽
	 * description:根据流程类型Id得到该类型下用户有权限的流程信息
	 * modifyer:
	 * @param processTypeId -流程类型Id
	 * @return List<Object[]> 流程类型下有权限的流程信息集<br>
	 * 		<p>数据结构：</p>
	 * 		<p>Object[]{流程名称, 流程Id}</p>
	 * @return
	 */
	public List<Object[]> getProcessOwnedByProcessType(String processTypeId,OALogInfo ... loginfos) {
		return workflow.getProcessOwnedByProcessType(processTypeId);
	}
	/**
	 * 修改审核状态
	 * @author hull
	 * @date 2010-03-23
	 * @param id 业务数据ID
	 * @param state 状态位
	 * @param loginfos
	 */
	public void updateGatherAutistate(String id,String state,OALogInfo ... loginfos){
		try {
			String sql="update t_oa_attend_statistics t set t.auti_state='"+state+"' where t.statistics_id='"+id+"'";
			Connection conn=gatherDao.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 得到当前用户相应类别的任务列表,
	   修改成挂起任务及指派委托状态也查找出来，在展现层展现状态 
	   增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	   @param page 分页对象
	   @param workflowType 流程类型参数
	   		1)大于0的正整数字符串：流程类型Id 
	   		2)0:不需指定流程类型
	   		3)-1:非系统级流程类型
	   @param businessName 业务名称查询条件
	   @param userName 主办人名称
	   @param startDate 任务开始时间
	   @param endDate 任务结束时间
	   @param isBackspace  是否是回退任务
	   		“0”：非回退任务；“1”：回退任务；“2”：全部
	   @return
	   		Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起, 
				 (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型} 
	 */
	@Transactional(readOnly = true)
	@Override
	public Page getTodoWorks(Page page,String workflowType,String businessName, 
			 				 String userName,Date startDate,
			 				 Date endDate,String isBackSpace,OALogInfo...infos) throws SystemException,ServiceException{
		try {
			User curUser = userService.getCurrentUser();//		获取当前用户
			String searchType = "all";
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate,isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取待办工作"});
		}
	}	
	 /**
	 * 提交工作流转到下一步
	 * @param taskId 任务ID
	 * @param transitionName 流向名称
	 * @param returnNodeId 驳回节点ID
	 * @param isNewForm 当前节点是否是新的表单
	 * @param formId 表单ID
	 * @param businessId 业务ID
	 * @param suggestion 提交意见
	 * @param taskActors 下一步任务处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param concurrentTrans 并发流向
	 */
	public void handleWorkflowNextStep(String taskId, String transitionName,
			String returnNodeId, String isNewForm, String formId,
			String businessId, String suggestion, String[] taskActors,
			String concurrentTrans) throws SystemException,ServiceException{
		try{
			User curUser = userService.getCurrentUser();
			workflow.goToNextTransition(taskId, transitionName, returnNodeId,isNewForm, "0", businessId, suggestion, curUser.getUserId(),taskActors);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"提交工作流转到下一步"});
    	}
	}
	/**
	 * 获取表单ID和业务ID
	 * @param taskId
	 * @return String[] 数据格式:[业务ID,表单ID]
	 */
	@Transactional(readOnly = true)
	public String[] getFormIdAndBussinessIdByTaskId(String taskId) throws SystemException,ServiceException{
		try{
			String[] ret = new String[2];
			String strNodeInfo="";
			try{
				strNodeInfo = workflow.getNodeInfo(taskId);
			}catch(Exception e){
				e.printStackTrace();
			}
			String[] arrNodeInfo = strNodeInfo.split(",");
			if ("form".equals(arrNodeInfo[0])) {
				ret[0] = arrNodeInfo[2];
				ret[1] = arrNodeInfo[3];
			} else {
				//异常情况，抛出异常
			}
			
			return ret;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取表单ID和业务ID"});
    	}
	}	
	/**
	 * 任务退回
	 * 退回操作之前需要先保存电子表单数据
	 * @author:胡丽丽
	 * @date:2009-12-15 下午08:22:14
	 * @param taskId 任务ID
	 * @param formId 表单ID
	 * @param suggestion 退回意见
	 * @param formData 电子表单数据
	 * @param returnNodeId 需要退回到的目标节点
	 * @throws SystemException
	 */
	public void backSpace(String taskId,String returnNodeId,String formId,String suggestion,String businessId) throws SystemException {
		String curUserId = userService.getCurrentUser().getUserId();
		super.handleWorkflowNextStep(taskId,WorkflowConst.WORKFLOW_TRANSITION_HUITUI,returnNodeId,"0",formId,businessId,suggestion,curUserId,null);
	}
}
