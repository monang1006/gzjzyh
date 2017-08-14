package com.strongit.oa.work;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      工作处理Manager
 */
@Service
@Transactional
public class WorkManager extends BaseManager{

	/**
	 * 提交工作流【外部系统调用OA的工作流审批】
	 * 流程启动者为OA系统专门提供一个匿名用户
	 * @author:邓志城
	 * @date:2009-7-8 下午05:59:39
	 * @param workflowName
	 * @param businessId
	 * @param businessName
	 * @param tansitionName
	 * @param concurrentTrans
	 * @param sugguestion
	 * @throws SystemException
	 */
	public void handleWorkflowWithUser(String workflowName,
									   String businessId,
									   String businessName,
									   String tansitionName, 
									   String concurrentTrans,
									   String sugguestion)throws SystemException{
		//这里的userId为初始化数据
		String userId = "402882282262726001226289c8cb0001";
		workflow.startWorkflow("0", workflowName, userId,
				businessId, businessName, null, tansitionName, concurrentTrans,
				sugguestion);
	}

	/**
	 * 获取工作处理审批意见字典项
	 * @author 邓志城
	 * @Date 2009年5月4日14:07:03
	 * @return 工作处理意见列表
	 */
	public List getIdeas() throws SystemException,ServiceException{
		return super.getItemsByDictValue(WorkFlowTypeConst.WORKIDEA);
	}

	/**
	 * 保存业务数据【暂时用于IPP请求OA】
	 * @author:邓志城
	 * @date:2009-7-7 上午10:54:00
	 * @return
	 * @throws SystemException
	 */
	public String saveBusinessData(String info,
								   DataHandler[] attachments,
								   String[] fileNames)throws SystemException{
		StringBuffer sql = new StringBuffer("insert into T_OA_IPP_WORKFLOW(WORKFLOWID,MANNAME,MANCARDTYPE,MANCARDNUMBER,MANBIRTHDAY,MANCOUNTRY,MANNATION,MANJOB,MANEDUCATION,MANPERMENTTYPE,MANMARRIAGE,MANPROVINCE,MANCITY,MANMOBILE,NAME,CARDTYPE,CARDNUMBER,BIRTHDAY,NATION,JOB,EDUCATION,PERMENTTYPE,MARRIAGE,PROVINCE,CITY,MOBILE,COUNTRY,FEEDBACKCODE) ");
		//产生UUID
		UUIDGenerator gen = new UUIDGenerator();
		String uuid = gen.generate().toString();
		JSONObject objInfo = JSONObject.fromObject(info);
		/**           男方信息                      **/
		String manName = objInfo.getString("manName");//姓名
		String manCardType = objInfo.getString("manCardType");//证件类型
		String manCardNumber = objInfo.getString("manCardNumber");//证件编号
		String manBirthday = objInfo.getString("manBirthday");//出生日期
		String manCountry = objInfo.getString("manCountry");//国籍
		String manNation = objInfo.getString("manNation");//民族
		String manJob = objInfo.getString("manJob");//职业
		String manEducation = objInfo.getString("manEducation");//文化程度
		String manPermantType = objInfo.getString("manPermantType");//户籍类别
		String manMarriage = objInfo.getString("manMarriage");//婚姻状况
		String manProvince = objInfo.getString("manProvince");//户口所在地
		String manCity = objInfo.getString("manCity");//户口所在市
		String manMobile = objInfo.getString("manMobile");//电话
		/**     女方信息                      **/
		String name = objInfo.getString("name");
		String cardType = objInfo.getString("cardType");
		String cardNumber = objInfo.getString("cardNumber");
		String birthday = objInfo.getString("birthday");
		String country = objInfo.getString("country");
		String nation = objInfo.getString("nation");
		String job = objInfo.getString("job");
		String education = objInfo.getString("education");
		String permantType = objInfo.getString("permantType");
		String marriage = objInfo.getString("marriage");
		String province = objInfo.getString("province");
		String city = objInfo.getString("city");
		String mobile = objInfo.getString("mobile");
		//咨询反馈号
		String feedBackCode = objInfo.getString("feedBackCode");
		
		sql.append("values('"+uuid+"','"+manName+"','"+manCardType+"','"+manCardNumber+"','"+manBirthday+"','"+manCountry+"','"+manNation+"','"+manJob+"','"+manEducation+"','"+manPermantType+"','"+manMarriage+"','"+manProvince+"','"+manCity+"','"+manMobile+"','"+name+"','"+cardType+"','"+cardNumber+"','"+birthday+"','"+nation+"','"+job+"','"+education+"','"+permantType+"','"+marriage+"','"+province+"','"+city+"','"+mobile+"','"+country+"','"+feedBackCode+"')");
		//执行插入操作
		super.executeJdbcUpdate(sql.toString());
		saveAttachments(uuid, attachments, fileNames);
		//返回业务数据标示
		String ret = "T_OA_IPP_WORKFLOW;WORKFLOWID;"+uuid;
		return ret;
	}

	/**
	 * 根据ID获取咨询反馈号、手机号码
	 * @author:邓志城
	 * @date:2009-7-15 上午11:04:55
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public String[] getFeedBackCode(String id)throws SystemException{
		String[] ret = new String[2];
		try {
			String sql = "select FEEDBACKCODE,MOBILE from T_OA_IPP_WORKFLOW where WORKFLOWID='"+id+"'";
			ResultSet rs = super.executeJdbcQuery(sql);
			if(rs.next()){
				ret[0] = rs.getString("FEEDBACKCODE");
				ret[1] = rs.getString("MOBILE");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		return ret;
	}
	
	/**
	 * 保存附件
	 * @author:邓志城
	 * @date:2009-7-9 下午04:05:20
	 * @param fkId
	 * @param attachments
	 * @param fileNames
	 */
	@SuppressWarnings("deprecation")
	private void saveAttachments(String fkId,DataHandler[] attachments,String[] fileNames)throws SystemException{
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try{
			//transfer(attachments, fileNames);
			con = super.getConnection();
			//产生UUID
			UUIDGenerator gen = new UUIDGenerator();
			if(attachments!=null && attachments.length>0){
				con.setAutoCommit(false);
				for(int i=0;i<attachments.length;i++){
					String sql = "insert into T_DOCATTACH(DOCATTACHID,DOC_ID,ATTACH_NAME,ATTACH_CONTENT)values(?,?,?,empty_blob())";
					String id = gen.generate().toString();
					psmt = con.prepareStatement(sql);
					psmt.setString(1, id);
					psmt.setString(2, fkId);
					psmt.setString(3, fileNames[i]);
					psmt.executeUpdate();
					sql = "select * from T_DOCATTACH where DOCATTACHID='"+id+"' for update";
					psmt = con.prepareStatement(sql);
					rs = psmt.executeQuery();
					if(rs.next()){
						sql = "update T_DOCATTACH set ATTACH_CONTENT=? where DOCATTACHID=?";
						psmt = con.prepareStatement(sql);
						is = attachments[i].getInputStream();
						psmt.setBinaryStream(1, is, is.available());
						psmt.setString(2, id);
						psmt.executeUpdate();
					}
				}
				con.commit();
			}
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				
			}
			throw new SystemException("保存附件异常！",e);
		}finally{
			try{
				if(is!=null){
					is.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(psmt!=null){
					psmt.close();
				}
				if(con!=null && !con.isClosed()){
					con.close();
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new SystemException("关闭记录集异常！",e);
			}
		}
	}
	
	/*
	 * 
	 * Description:获取所有流程类型
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 11, 2010 2:45:13 PM
	 */
	public Map<String,String> getProccessTypeIdAndName(Page pageTodo)throws SystemException,ServiceException{
		Map<String,String> typeMap=new HashMap<String,String>();
		List<Object[]> list=pageTodo.getResult();
		try{
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			Object[] typeObj;
			Object[] obj;
			int k;
			for(int i=0;i<typeList.size();i++){
				typeObj=typeList.get(i);
				k=0;
				for(int j=0;list!=null&&j<list.size();j++){
					obj=list.get(j);
					if(typeObj[0].equals(obj[8])){
						k+=1;
					}
				}
				typeMap.put(typeObj[0].toString(), typeObj[1].toString()+"(<font color='red'>"+k+"</font>)");
			}
			return typeMap;
		}catch(Exception e){
			e.printStackTrace();
			throw new SystemException("获取流程类型类表异常！",e);
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 22, 2010 3:47:57 PM
	 */
	public List<Object[]> getProccessTypeIdAndNameList(Page pageTodo)throws SystemException,ServiceException{
		List<Object[]> list=pageTodo.getResult();
		String tempValue;
		String tempValue1;
		List<Object[]> typeAndNameList=new ArrayList<Object[]>();
		Object[] workFlow;
		try{
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			Object[] typeObj;
			Object[] obj;
			int k;
			for(int i=0;i<typeList.size();i++){
				typeObj=typeList.get(i);
				k=0;
				for(int j=0;list!=null&&j<list.size();j++){
					obj=list.get(j);
					tempValue=typeObj[0].toString();	
					tempValue1=obj[8]==null?null:obj[8].toString();
					if(tempValue1!=null&&tempValue.equals(tempValue1)){
						k+=1;
					}
				}
				if(k!=0){
					workFlow=new Object[2];
					workFlow[0]=typeObj[0].toString();
					workFlow[1]=typeObj[1].toString()+"(<font color='red'>"+k+"</font>)";
					typeAndNameList.add(workFlow);
				}
			}
			return typeAndNameList;
		}catch(Exception e){
			e.printStackTrace();
			throw new SystemException("获取流程类型类表异常！",e);
		}
	}

	/*
	 * 
	 * Description:获取当前用户所有代办和在办任务
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 11, 2010 10:48:41 AM
	 */
	@Transactional(readOnly = true)
	public Page getAllTodoWorks(Page page,String workflowType,String businessName,String userName,Date startDate,Date endDate,String isBackSpace) throws SystemException,ServiceException{
		try {
			User curUser = userService.getCurrentUser();//获取当前用户
			String searchType = "all";
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate,isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取待办工作"});
		}
	}
	
}
