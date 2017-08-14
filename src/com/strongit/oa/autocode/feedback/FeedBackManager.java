/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: FeedBackManager.java
 * @project    : 抚州人社局人才市场系统 ——人才市场招聘网站子系统
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 22, 2013 8:00:50 PM  
 */
package com.strongit.oa.autocode.feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaFeedback;
import com.strongit.oa.bo.ToaReportBean1;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @description: 反馈意见报表处理类
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 22, 2013 8:00:50 PM
 * @version    : v1.0
 * @since  
 */
@Service
@Transactional
@OALogger
public class FeedBackManager
        extends BaseManager {
    private GenericDAOHibernate<ToaReportBean1, String> toaReportBean1DAO = null;
    private GenericDAOHibernate<ToaFeedback, String> toaFeedbackDAO = null;
    /**
     * 注入sessionFactory
     * 
     * @param sessionFactory
     */
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        toaReportBean1DAO = new GenericDAOHibernate<ToaReportBean1, String>(
                sessionFactory, ToaReportBean1.class);
        toaFeedbackDAO = new GenericDAOHibernate<ToaFeedback, String>(
                sessionFactory, ToaFeedback.class);
    }
    
    
	/**
	 * author:luosy 2013-5-5
	 * description: 根据业务表信息获取报表对象
	 * modifyer:
	 * description:
	 * @param bussinessId
	 * @return
	 * @throws Exception
	 */
	public ToaReportBean1 getToaReportBeanById(String bussinessId) throws Exception {
		final String hql = "from ToaReportBean1 as t where t.businessId =?";
		List<ToaReportBean1> list = toaReportBean1DAO.find(hql, bussinessId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * author:luosy 2013-5-5
	 * description:	开始意见征询 记录征询时间，规定反馈时限
	 * modifyer:
	 * description:
	 * @param bussinessId 	顶级父流程的业务表信息
	 * @param bussiness1	意见征询的业务表信息
	 * @throws Exception
	 */
	public void addBackTime(String bussinessId, String bussiness1 ) throws Exception {
		if (bussiness1 != null && !"".equals(bussiness1)) {
			String[] bussinessIds1 = bussiness1.split(";");

			String[] bussinessIds = bussinessId.split(";");

			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {

				String sql = "";
				sql = "select RECV_TIME,WORKFLOWTITLE,PERSON_OPERATE_DATE ,DOC_NUMBER,SENDDOC_DEPART,RESP_DEPART,REV_DEPART,RETURN_DATE from "
						+ bussinessIds1[0] + " where  " + bussinessIds1[1]
						+ "='" + bussinessIds1[2] + "' ";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();

				if (rs.next()) {
					String feedBackTime = rs.getString("PERSON_OPERATE_DATE");
					String returnDate = rs.getString("RETURN_DATE");
					String title=rs.getString("WORKFLOWTITLE");
					String department=rs.getString("RESP_DEPART");
					String sendCode=rs.getString("DOC_NUMBER");
					String units = rs.getString("SENDDOC_DEPART");
					Date receiveTime = rs.getDate("RECV_TIME");
					ToaReportBean1 model = this
							.getToaReportBeanById(bussinessIds[2]);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    
                    
					if (model != null) {
					  //设置开始意见征求的时间
	                    model.setFeedBackTime(sdf.parse(feedBackTime));
	                    //判断收文编号先前是否为空的情况
	                    if(model.getSendCode()==null||"".equals(model.getSendCode())){
	                        model.setSendCode(sendCode);
	                    }
	                    //设置意见征求规定的反馈用时（工作日）
	                    model.setFixTime(getWorkDaysBetweenStartDateAndEndDate(feedBackTime.split(" ")[0], returnDate.split(" ")[0]));
						toaReportBean1DAO.update(model);
					}else{
					    model=new ToaReportBean1();
					  //设置开始意见征求的时间
                        model.setFeedBackTime(sdf.parse(feedBackTime));
                        //设置意见征求规定的反馈用时（工作日）
                        model.setFixTime(getWorkDaysBetweenStartDateAndEndDate(feedBackTime.split(" ")[0], returnDate.split(" ")[0]));
                        model.setBusinessId(bussinessIds1[2]);
                        model.setTitle(title);
                        model.setDepartment(department);
                        model.setSendCode(sendCode);
                        model.setUnits(units);
                        model.setReceiveTime(receiveTime);
                        toaReportBean1DAO.save(model);
					}

				}
			} catch (SQLException e) {
				//
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * author:luosy 2013-5-5
	 * description:		结束意见征询 记录各个反馈单位名称，反馈用时，是否超期
	 * modifyer:
	 * description:
	 * @param bussinessId
	 * @param bussiness1
	 * @throws Exception
	 */
	public void addEndTime(String bussinessId, String bussiness1) throws Exception {
		if (bussiness1 != null && !"".equals(bussiness1)) {
			String[] bussinessIds1 = bussiness1.split(";");

			String[] bussinessIds = bussinessId.split(";");

			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {

				String sql = "";
				sql = "select SYSDATE from " + bussinessIds1[0] + " where  "
						+ bussinessIds1[1] + "='" + bussinessIds1[2] + "' ";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();

				if (rs.next()) {
					String feedBackEndTime = rs.getString("SYSDATE");
					ToaReportBean1 model = this
							.getToaReportBeanById(bussinessIds[2]);
					if (model != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
						long diff = sdf.parse(feedBackEndTime).getTime()
								- model.getFeedBackTime().getTime();
						long day = diff / nd;
						model.setReturnPrior((int) day);
						toaReportBean1DAO.update(model);
					}

				}
			} catch (SQLException e) {
				//
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
     * author:xush 2013-5-6
     * description:     结束意见征询 记录各个反馈单位名称，反馈用时，是否超期
     * modifyer:
     * description:
     * @param handledDept,wordDays,handleworkDays,overDays,bussiness
     * @throws Exception
     */
    public void insertData(String handledDept,int wordDays,int handleworkDays,int overDays, String bussiness) throws Exception {
        if (bussiness != null && !"".equals(bussiness)) {
            String[] bussinessIds = bussiness.split(";");
            ToaReportBean1 toaReportBean = this
            .getToaReportBeanById(bussinessIds[2]);
            String title = toaReportBean.getTitle();
            ToaFeedback model = new ToaFeedback();
            model.setTitle(title);
            model.setBussinessId(bussinessIds[2]);
            model.setUnitName(handledDept);
           model.setFeedBackFixPrior(wordDays);
            model.setActualPrior(handleworkDays);
            model.setLaterPrior(overDays);
            model.setFeedBackStartTime(toaReportBean.getFeedBackTime());
            if(overDays==0){
                model.setOnTimeFlg("0");
            }else{
                model.setOnTimeFlg("1"); 
            }
            toaFeedbackDAO.save(model);
        }
            

    }
    /**
     * author:luosy 2013-5-4
     * description:		得到意见征询的规定反馈时间
     * modifyer:
     * description:
     * @param bussiness1
     * @throws Exception
     */
    public Date getYjzxGdsj(String bussiness1) throws Exception{
		if (bussiness1 != null && !"".equals(bussiness1)) {
			String[] bussinessIds1 = bussiness1.split(";");

			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {

				String sql = "";
				sql = "select RETURN_DATE from " + bussinessIds1[0] + " where  "
						+ bussinessIds1[1] + "='" + bussinessIds1[2] + "' ";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();

				if (rs.next()) {
					return rs.getDate("RETURN_DATE");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
    }
    
    /**
     * author:luosy 2013-5-4
     * description:		得到意见征询的规定反馈用时（工作日）
     * modifyer:
     * description:
     * @param bussiness1
     * @throws Exception
     */
    public int getYjzxDays(String bussiness1) throws Exception{
		if (bussiness1 != null && !"".equals(bussiness1)) {
			String[] bussinessIds1 = bussiness1.split(";");

			Connection con = this.getConnection();
			con.setAutoCommit(false);
			PreparedStatement psmt = null;
			ResultSet rs = null;
			try {

				String sql = "";
				sql = "select FIX_TIME from T_oa_report_bean where business_Id "
						 + "='" + bussinessIds1[2] + "' ";
				psmt = con.prepareStatement(sql);
				rs = psmt.executeQuery();

				if (rs.next()) {
					return  rs.getInt("FIX_TIME");
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (psmt != null) {
						psmt.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return 0;
    }
    /**
     * author:xush 2013-5-7
     * description:     得到意见征询的开始反馈时间
     * modifyer:
     * description:
     * @param bussiness1
     * @throws Exception
     */
    public Date getFeedbackTime(String bussiness) throws Exception{
        if (bussiness != null && !"".equals(bussiness)) {
            String[] bussinessIds = bussiness.split(";");

            Connection con = this.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psmt = null;
            ResultSet rs = null;
            try {

                String sql = "";
                sql = "select FEEDBACK_TIME from T_oa_report_bean where business_Id "
                         + "='" + bussinessIds[2] + "' ";
                psmt = con.prepareStatement(sql);
                rs = psmt.executeQuery();

                if (rs.next()) {
                    return  rs.getDate("FEEDBACK_TIME");
                }
                
                
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (psmt != null) {
                        psmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    

    /**
     * author:luosy 2013-5-5
     * description:		计算某段时间的工作日
     * modifyer:
     * description:
     * @param StartDate 格式为 yyyy-MM-dd
     * @param endDate 格式为 yyyy-MM-dd
     * @return
     * @throws Exception
     */
    public int getWorkDaysBetweenStartDateAndEndDate(String StartDate, String endDate) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = sdf.parse(StartDate);
		Date d2 = sdf.parse(endDate);
		return getWorkDaysBetweenStartDateAndEndDate(d1,d2);
	}
    

    /**
     * author:luosy 2013-5-5
     * description:		计算某段时间的工作日
     * modifyer:
     * description:
     * @param StartDate 格式为 yyyy-MM-dd
     * @param endDate 格式为 yyyy-MM-dd
     * @return
     * @throws Exception
     */
    public int getWorkDaysBetweenStartDateAndEndDate(Date StartDate, Date endDate) throws Exception{
    	// 这里要判断第二个参数日期要比第一个参数日期大先继续运行
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 工作日
		int workDay = 0;
		
		try {
			gc.setTime(StartDate);
			long time = endDate.getTime() - StartDate.getTime();
			long day = time / 3600000 / 24 + 1;
			for (int i = 0; i < day; i++) {
				if (gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY			//排除周六
						&& gc.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {		//排除周日
					
					//如果不是法定节假日的话就算工作日
					if (!holidayList(sdf.format(gc.getTime()))){	
						workDay++;
					}
				}else if(unholidayList(sdf.format(gc.getTime()))){	
					//根据法定假日调整，需要上班的周六周日也算工作日
					workDay++;
				}
				// 天数加1
				gc.add(gc.DATE, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(StartDate+"至"+endDate+"期间，工作日为"+workDay+"天");
		
		return workDay;
    	
    }

	/**
	 * author:luosy 2013-5-5
	 * description:		比较某个日期是否是法定假日
	 * modifyer:
	 * description:
	 * @param ymd	年月日
	 * @return
	 */
	public boolean holidayList(String ymd) {
		List<ToaSysmanageDictitem> JJRList = dictService.getItemsByDictValue("JJR");
		for(ToaSysmanageDictitem dict:JJRList){
			if(ymd.equals(dict.getDictItemValue())){
				return true;
			}
		}
		return false;
	}

	/**
	 * author:luosy 2013-5-5
	 * description:		比较某个日期是否是因法定假日调整需要上班的周六周日
	 * modifyer:
	 * description:
	 * @param ymd	年月日
	 * @return
	 */
	public boolean unholidayList(String ymd) {
		List<ToaSysmanageDictitem> JJRList = dictService.getItemsByDictValue("JJRTX");
		for(ToaSysmanageDictitem dict:JJRList){
			if(ymd.equals(dict.getDictItemValue())){
				return true;
			}
		}
		return false;
	}
}

