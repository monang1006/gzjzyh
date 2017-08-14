package com.strongit.oa.report.query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.TgwReportBean;
import com.strongit.oa.bo.TgwReportBean1;
import com.strongit.oa.bo.TgwReportBean2;
import com.strongit.oa.bo.ToaFeedback;
import com.strongit.oa.bo.ToaFeedbackBean;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaReportBean1;
import com.strongit.oa.bo.ToaUnitsBean;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * 生成报表服务类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-17 下午01:49:28
 * @version  3.0
 * @classpath com.strongit.oa.report.query.ReportServiceImpl
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
public class ReportServiceImpl implements IReportService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String jrxmlPath = null;	//模板文件路径
	
	@Autowired JdbcTemplate jdbcTemplate;	//jdbc操作辅助类
	private IGenericDAO<ToaReportBean, String> toaReportBeanDAO = null;
	private IGenericDAO<ToaReportBean1, String> toaReportBean1DAO = null;
	private GenericDAOHibernate<ToaFeedback, String> toaFeedbackDAO = null;
	@Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.toaReportBeanDAO = new GenericDAOHibernate<ToaReportBean, String>(
                sessionFactory, ToaReportBean.class);
        this.toaReportBean1DAO = new GenericDAOHibernate<ToaReportBean1, String>(
                sessionFactory, ToaReportBean1.class);
        toaFeedbackDAO = new GenericDAOHibernate<ToaFeedback, String>(
                sessionFactory, ToaFeedback.class);
    }
	public ReportServiceImpl(){
		jrxmlPath = "/WEB-INF/jsp/report/jrxml/report_workflow.jrxml"; 
	}
	
	public ReportServiceImpl(String jrxmlPath) {
		this.jrxmlPath = jrxmlPath;
	}

	/**
	 * 根据表名称得到表中所有字段名称
	 * @author:邓志城
	 * @date:2010-12-23 下午06:57:11
	 * @param tableName		表名称
	 * @return				表字段列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getColumnField(String tableName) {
		try {
			StringBuilder sql = new StringBuilder("select * from ").append(tableName).append(" where 1=0");
			logger.info(sql.toString());
			final Map<String, Integer> columnMap = new LinkedHashMap<String, Integer>();//Map<字段名称,字段对应的Java类型>
			return (Map<String, Integer>)jdbcTemplate.query(sql.toString(), new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					ResultSetMetaData rsmd = rs.getMetaData();
					int count = rsmd.getColumnCount();
					for(int i=1;i<=count ;i++){
						columnMap.put(rsmd.getColumnName(i), rsmd.getColumnType(i));//存储数据库字段对应的SQL类型
					}
					return columnMap;
				}
				
			});
		} catch (DataAccessException e) {
			logger.error("查询表中字段时发生异常", e);
			throw new SystemException(e);
		}
	}

	/**
	 * 将生成的对象输出到打印机
	 * @author:邓志城
	 * @date:2010-12-24 下午02:39:56
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（留待以后扩展）
	 */
	public void exportPrinter(JasperPrint jasperPrint,Object...objects) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream ouputStream = response.getOutputStream();
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);//将JasperPrint对象写入对象输出流中 
			oos.flush();
			oos.close();  
			ouputStream.flush();
			ouputStream.close();
			//JasperPrintManager.printReport(jasperPrint, false);
		} catch (Exception e) {
			logger.error("打印报表时发生异常", e);
			throw new SystemException(e);
		}	
	}
	/**
     * 填充退文中子报表的数据
     * @author:xush
     * @date:4/19/2013 11:12 AM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
	public List<ToaUnitsBean> getUnitsList(String units,String year,String startTime){
	    try {
            List<ToaUnitsBean> unitsList=new ArrayList<ToaUnitsBean>();
            ToaUnitsBean toaUnitsBean = null;
            ResultSet result1 = null;
            StringBuffer sqlUnit = new StringBuffer();
            sqlUnit.append( "select title,return_reason from t_oa_report_bean t where t.units='")
                   .append(units+"'")
                   .append(" and return_flg='1'  and RECEIVE_TIME between to_date('")
                   .append(year+"','YYYY-MM-DD HH24:MI:SS') and to_date('")
                   .append(startTime+"','YYYY-MM-DD HH24:MI:SS')");
            result1=toaReportBeanDAO.getConnection().createStatement().executeQuery( sqlUnit.toString());
             while(result1.next()){
                toaUnitsBean=new ToaUnitsBean();
                String title = result1.getString("title");
                String returnReason = result1.getString("return_reason");
                toaUnitsBean = new ToaUnitsBean();
                toaUnitsBean.setTitle(title);
                if(returnReason==null){
                toaUnitsBean.setReturnReason(" ");}
                else{
                    toaUnitsBean.setReturnReason(returnReason);
                }
                unitsList.add(toaUnitsBean);
                                   }
             return unitsList;
        } catch (DAOException e) {
            
            e.printStackTrace();
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return null;                        
	}
	public String getFeedbackReturn(String businessId){
	    try {
            List<ToaFeedback> feedbackList=new ArrayList<ToaFeedback>();
            StringBuffer buffer =new StringBuffer();
            //ToaFeedback toaFeedback=new ToaFeedback();
            feedbackList=toaFeedbackDAO.find(" from ToaFeedback where bussinessId=?",new Object[]{businessId});
            if(feedbackList!=null&&feedbackList.size()>0){
            for(ToaFeedback toaFeedback: feedbackList){
               buffer.append(toaFeedback.getUnitName()+":"+toaFeedback.getActualPrior()+";");
            }
            return buffer.toString();
            }
        } catch (DAOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return null;
	}
	public List<TgwReportBean2> getNum(List<Integer> list){
	    TgwReportBean2 tgwReportBean2 =null;
        List<TgwReportBean2> numberList=new ArrayList<TgwReportBean2>();
	    if(list.size()==1){
	        for(int i=1;i<=list.get(0);i++){
	            tgwReportBean2 = new TgwReportBean2();
	        tgwReportBean2.setNumber(i);
	        numberList.add(tgwReportBean2);
	    }
	        }else{
            for(int i=list.get(list.size()-2)+1;i<=list.get(list.size()-1);i++){
                tgwReportBean2 = new TgwReportBean2();
                tgwReportBean2.setNumber(i);
                numberList.add(tgwReportBean2);
            }
        }
	    return numberList;
	}
	public List<TgwReportBean2> getNumberList(String department,String year,String selectDept) throws Exception{
	    TgwReportBean2 tgwReportBean2 = null;
	    List<TgwReportBean2> numberList=new ArrayList<TgwReportBean2>();
        ResultSet result1 = null;
        StringBuffer sqlUnit = new StringBuffer();
        SimpleDateFormat st = new SimpleDateFormat(
        "yyyy-MM-dd");
        int sum=1;
        int total=0;
        sqlUnit.append( "select count(*) from t_oa_report_bean t where t.department is not null")
               .append(" and to_char(RECEIVE_TIME,'YYYY-MM') like '")
               .append(year+"'");
       
        result1=toaReportBean1DAO.getConnection().createStatement().executeQuery( sqlUnit.toString());
         while(result1.next()){
             total=result1.getInt("total");
          }
         for(int i=1;i<=total;i++){
             tgwReportBean2.setNumber(i);
             numberList.add(tgwReportBean2);
         }
         return null;
	}
	/**
     * 填充公文中子报表的数据
     * @author:xush
     * @date:5/12/2013 5:37 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<TgwReportBean1> getGwList(String department,String year,String selectDept) throws Exception{
        try {
            List<TgwReportBean1> gwList=new ArrayList<TgwReportBean1>();
            TgwReportBean1 tgwReportBean1 = null;
            ResultSet result1 = null;
            StringBuffer sqlUnit = new StringBuffer();
            SimpleDateFormat st = new SimpleDateFormat(
            "yyyy-MM-dd");
            
            sqlUnit.append( "select business_Id, title,units, receive_time,GU_FINISH_PRIOR,GL_FINISH_PRIOR,FIX_TIME,SEND_CODE,REMARKS,FU_FINISH_PRIOR,FL_FINISH_PRIOR,FINISH_PRIOR,TURN_TIME,FEEDBACK_TIME from t_oa_report_bean t where t.department='")
                   .append(department+"'")
                   .append(" and to_char(RECEIVE_TIME,'YYYY-MM') like '")
                   .append(year+"'");
           
            result1=toaReportBean1DAO.getConnection().createStatement().executeQuery( sqlUnit.toString());
             while(result1.next()){
                 tgwReportBean1=new TgwReportBean1();
                String title = result1.getString("title");
                String units = result1.getString("units");
                String receiveTime = result1.getString("receive_time");
                String fixTime = result1.getString("fix_time");
                String guFinishPrior = result1.getString("GU_FINISH_PRIOR");
                String glFinishPrior = result1.getString("GL_FINISH_PRIOR");
                String flFinishPrior = result1.getString("FL_FINISH_PRIOR");
                String fuFinishPrior = result1.getString("FU_FINISH_PRIOR");
                String sendCode = result1.getString("SEND_CODE");
                String remarks = result1.getString("REMARKS");
                String finishPrior = result1.getString("FINISH_PRIOR");
                String feedbackTime = result1.getString("FEEDBACK_TIME");
                String turnTime = result1.getString("TURN_TIME");
                String businessId = result1.getString("business_Id");
                
                    String returnPrior = this.getFeedbackReturn(businessId);
                    if(returnPrior==null||"0".equals(returnPrior)){
                        tgwReportBean1.setReturnPrior(" ");}
                        else{
                            tgwReportBean1.setReturnPrior(returnPrior);
                        }
                    
                
                if(feedbackTime==null){
                    tgwReportBean1.setFeedBackTime(" ");}
                    else{
                        tgwReportBean1.setFeedBackTime(st.format(st.parse(feedbackTime)));
                    }
                //tgwReportBean1.setFeedBackTime(feedbackTime);
                if(finishPrior==null||"0".equals(finishPrior)){
                    tgwReportBean1.setFinishPrior(" ");}
                    else{
                        tgwReportBean1.setFinishPrior(finishPrior);
                    }
                //tgwReportBean1.setFinishPrior(finishPrior);
                if(fixTime==null||"0".equals(fixTime)){
                    tgwReportBean1.setFixTime(" ");}
                    else{
                        tgwReportBean1.setFixTime(fixTime);
                    }
                //tgwReportBean1.setFixTime(fixTime);
                if(flFinishPrior==null){
                    tgwReportBean1.setFlFinishPrior(" ");}
                  else{
                      tgwReportBean1.setFlFinishPrior(flFinishPrior);
                  }
               // tgwReportBean1.setFlFinishPrior(flFinishPrior);
                if(fuFinishPrior==null){
                    tgwReportBean1.setFuFinishPrior(" ");}
                  else{
                      tgwReportBean1.setFuFinishPrior(fuFinishPrior);
                  }
                //tgwReportBean1.setFuFinishPrior(fuFinishPrior);
                if(glFinishPrior==null){
                    tgwReportBean1.setGlFinishPrior(" ");}
                  else{
                      tgwReportBean1.setGlFinishPrior(glFinishPrior);
                  }
                //tgwReportBean1.setGlFinishPrior(glFinishPrior);
                if(guFinishPrior==null){
                    tgwReportBean1.setGuFinishPrior(" ");}
                  else{
                      tgwReportBean1.setGuFinishPrior(guFinishPrior);
                  }
                //tgwReportBean1.setGuFinishPrior(guFinishPrior);
                if(receiveTime==null){
                    tgwReportBean1.setReceiveTime(" ");}
                  else{
                      tgwReportBean1.setReceiveTime(st.format(st.parse(receiveTime)));
                  }
                //tgwReportBean1.setReceiveTime(receiveTime);
                if(remarks==null){
                    tgwReportBean1.setRemarks(" ");}
                  else{
                      tgwReportBean1.setRemarks(remarks);
                  }
                //tgwReportBean1.setRemarks(remarks);
                if(sendCode==null){
                    tgwReportBean1.setSendCode(" ");}
                  else{
                      tgwReportBean1.setSendCode(sendCode);
                  }
                //tgwReportBean1.setSendCode(sendCode);
                if(units!=null&&!"".equals(units)){
                    if(title!=null&&!"".equals(title)){
                        tgwReportBean1.setTitle("单位:"+units+"  "+"标题:"+title);
                        }
                    else{
                        tgwReportBean1.setTitle("单位:"+units);
                    }
                    
                }else{
                    if(title!=null&&!"".equals(title)){ 
                      tgwReportBean1.setTitle("标题:"+title);
                      }
                    else{
                        tgwReportBean1.setTitle(" "); 
                    }
                }
                
                if(turnTime==null){
                    tgwReportBean1.setTurnTime(" ");}
                  else{
                      tgwReportBean1.setTurnTime(st.format(st.parse(turnTime)));
                  }
                
                //tgwReportBean1.setTurnTime(turnTime);
                //tgwReportBean1.setUnits(units);
                gwList.add(tgwReportBean1);
                                   }
             return gwList;
        } catch (DAOException e) {
            
            e.printStackTrace();
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return null;                        
    }
	/**
     * 获取符合查询条件数据条数
     * @author:xush
     * @date:4/19/2013 11:12 AM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public int getTotalGw(String units,String year,String startTime){
        try {
            int totalGw=0;
            ResultSet result1 = null;
            StringBuffer sqlUnit = new StringBuffer();
            sqlUnit.append( "select count(*) TOTALGW from t_oa_report_bean t where t.units='")
                   .append(units+"'")
                   .append(" and RECEIVE_TIME between to_date('")
                   .append(year+"','YYYY-MM-DD HH24:MI:SS') and to_date('")
                   .append(startTime+"','YYYY-MM-DD HH24:MI:SS')");
            result1=toaReportBeanDAO.getConnection().createStatement().executeQuery( sqlUnit.toString());
             while(result1.next()){
                 totalGw = result1.getInt("TOTALGW");
                                 }
             return totalGw;
        } catch (DAOException e) {
            
            e.printStackTrace();
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return 0;                        
    }
	/**
     * 填充退文数据
     * @author:xush
     * @date:4/19/2013 11:12 AM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<ToaReportBean1> getReturnData(String year,String startTime){
        try {
            List<ToaUnitsBean> unitsList=new ArrayList<ToaUnitsBean>();
            String yearTime=null;
            String yearTime1=null;
           if("1".equals(startTime)){
               yearTime=year+"-01-01 00:00:00";
               yearTime1=year+"-04-01 00:00:00";
           }else if("2".equals(startTime)){
               yearTime=year+"-04-01 00:00:00";
               yearTime1=year+"-07-01 00:00:00";
           }else if("3".equals(startTime)){
               yearTime=year+"-07-01 00:00:00";
               yearTime1=year+"-10-01 00:00:00";
           }else if("4".equals(startTime)){
               yearTime=year+"-10-01 00:00:00";
               yearTime1=year+"-01-01 00:00:00";
           }
           String sql = "";
           if(startTime!=null&&year!=null){
           sql="select units,count(*) totalGw, sum(case when return_flg='1' then 1 else 0 end) percentageReturn1 " +
           		"from T_OA_REPORT_BEAN where return_flg='1'  and RECEIVE_TIME between to_date('"
               +yearTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"
               +yearTime1+"','YYYY-MM-DD HH24:MI:SS') group by units";
            //sql.append("select units,count(*) totalGw, sum(case when return_flg='1' then 1 else 0 end)/count(*)*100 percentageReturn");
            //sql.append(" from T_OA_REPORT_BEAN where RECEIVE_TIME between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') group by units");
            //sql.append(+yearTime+",'YYYY-MM-DD HH24:MI:SS') and to_date(");
            
            //sql.append(" LEFT JOIN (select units,sum(case when return_flg='1'then 1 else 0 end ) percentageReturn1 from t_oa_report_bean group by units) h ON h.units = b.units");
            //sql.append(" t_oa_report_bean k where  t.units=k.units and k.units=h.units group by k.units");
            //PreparedStatement statement= null;
            ResultSet result = null;
            
            result = toaReportBean1DAO.getConnection().createStatement().executeQuery(sql.toString());
            ToaReportBean1 toaReportBean1 = null;
            ToaUnitsBean toaUnitsBean = null;
            List<ToaReportBean1> list = new ArrayList<ToaReportBean1>();
            while(result.next()){
               String units = result.getString("units");
               //StringBuffer sqlUnit = new StringBuffer();
               unitsList = this.getUnitsList(units,yearTime,yearTime1);//获取子报表的数据
               int totalGw = this.getTotalGw(units,yearTime,yearTime1);//获取符合查询条件数据条数
               int percentageReturn1 = result.getInt("percentageReturn1");
               BigDecimal percentageReturn =new BigDecimal(((double)percentageReturn1/(double)totalGw)*100);
               toaReportBean1 = new ToaReportBean1();
               toaReportBean1.setUnits(units);
               toaReportBean1.setPercentageReturn(percentageReturn);
               toaReportBean1.setUnitsList(unitsList);
               toaReportBean1.setTotalGw(totalGw);
               list.add(toaReportBean1);
               
             }
            return list;
           }
//            List<ToaReportBean> list=toaReportBeanDAO.createQuery(sql.toString()).list();
           // return null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
       return new ArrayList<ToaReportBean1>();
    }
    /**
     * 退文总数
     * @author:xush
     * @date:6/6/2013 2:56 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              退文总数
     * @return                  报表数据
     */
    public int getReturnTotal(String year,String startTime){
        try {
           
            String yearTime=null;
            String yearTime1=null;
            //设置季度范围
           if("1".equals(startTime)){
               yearTime=year+"-01-01 00:00:00";
               yearTime1=year+"-04-01 00:00:00";
           }else if("2".equals(startTime)){
               yearTime=year+"-04-01 00:00:00";
               yearTime1=year+"-07-01 00:00:00";
           }else if("3".equals(startTime)){
               yearTime=year+"-07-01 00:00:00";
               yearTime1=year+"-10-01 00:00:00";
           }else if("4".equals(startTime)){
               yearTime=year+"-10-01 00:00:00";
               yearTime1=year+"-01-01 00:00:00";
           }
           String sql = "";
           sql="select count(*) total " +
                "from T_OA_REPORT_BEAN where return_flg='1'  and RECEIVE_TIME between to_date('"
               +yearTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"
               +yearTime1+"','YYYY-MM-DD HH24:MI:SS')";
            //sql.append("select units,count(*) totalGw, sum(case when return_flg='1' then 1 else 0 end)/count(*)*100 percentageReturn");
            //sql.append(" from T_OA_REPORT_BEAN where RECEIVE_TIME between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') group by units");
            //sql.append(+yearTime+",'YYYY-MM-DD HH24:MI:SS') and to_date(");
            
            //sql.append(" LEFT JOIN (select units,sum(case when return_flg='1'then 1 else 0 end ) percentageReturn1 from t_oa_report_bean group by units) h ON h.units = b.units");
            //sql.append(" t_oa_report_bean k where  t.units=k.units and k.units=h.units group by k.units");
            //PreparedStatement statement= null;
            ResultSet result = null;
            
            result = toaReportBean1DAO.getConnection().createStatement().executeQuery(sql.toString());
            ToaReportBean1 toaReportBean1 = null;
            
            int total=0;
            while(result.next()){
                total = result.getInt("total");//获取此季度退文总数
               }
//            List<ToaReportBean> list=toaReportBeanDAO.createQuery(sql.toString()).list();
            return total;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
       return 0;
    }
   public String getDeptList(){
       try {
        String sql = "";
           sql="select department from T_oa_report_bean";
           ResultSet res = null;
           List list = new ArrayList();
           res = toaReportBeanDAO.getConnection().createStatement().executeQuery(sql.toString());
           while(res.next()){
               String department = res.getString("department");
               if(department!=null){
               list.add(department);}
             }
           return list.toString();
    } catch (DAOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       return null;
   }
   
   
   public List<ToaReportBean1> getDeptList2() throws DAOException{
       //String sql = "";
       //sql="select * from T_oa_report_bean where department is not null";
       List list = toaReportBean1DAO.findAll();
       return list;
   }
    /**
     * 填充反馈意见数据
     * @author:xush
     * @date:5/6/2013 2:47 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<ToaFeedbackBean> getFeedbackData(String year,String startTime){
        try {
            List<ToaUnitsBean> unitsList=new ArrayList<ToaUnitsBean>();
            String yearTime=null;
            String yearTime1=null;
           if("1".equals(startTime)){
               yearTime=year+"-01-01 00:00:00";
               yearTime1=year+"-04-01 00:00:00";
           }else if("2".equals(startTime)){
               yearTime=year+"-04-01 00:00:00";
               yearTime1=year+"-07-01 00:00:00";
           }else if("3".equals(startTime)){
               yearTime=year+"-07-01 00:00:00";
               yearTime1=year+"-10-01 00:00:00";
           }else if("4".equals(startTime)){
               yearTime=year+"-10-01 00:00:00";
               yearTime1=year+"-01-01 00:00:00";
           }
           String sql = "";
           if(year!=null&startTime!=null){
           sql="select units_name,count(*) total, sum(case when on_time_flg='0' then 1 else 0 end) onTimeTotal,sum(case when on_time_flg='1' then 1 else 0 end) laterTotal,avg(feedback_fix_prior) fixRavPrior,avg(actual_Prior) actualRavPrior,avg(later_Prior) laterRavPrior" +
                " from T_OA_FEEDBACK where FEEDBACK_START_TIME between to_date('"
               +yearTime+"','YYYY-MM-DD HH24:MI:SS') and to_date('"
               +yearTime1+"','YYYY-MM-DD HH24:MI:SS') group by units_name";
            //sql.append("select units,count(*) totalGw, sum(case when return_flg='1' then 1 else 0 end)/count(*)*100 percentageReturn");
            //sql.append(" from T_OA_REPORT_BEAN where RECEIVE_TIME between to_date(?,'YYYY-MM-DD HH24:MI:SS') and to_date(?,'YYYY-MM-DD HH24:MI:SS') group by units");
            //sql.append(+yearTime+",'YYYY-MM-DD HH24:MI:SS') and to_date(");
            
            //sql.append(" LEFT JOIN (select units,sum(case when return_flg='1'then 1 else 0 end ) percentageReturn1 from t_oa_report_bean group by units) h ON h.units = b.units");
            //sql.append(" t_oa_report_bean k where  t.units=k.units and k.units=h.units group by k.units");
            //PreparedStatement statement= null;
            ResultSet result = null;
            
            result = toaFeedbackDAO.getConnection().createStatement().executeQuery(sql.toString());
            ToaFeedbackBean toaFeedbackBean = null;
            List<ToaFeedbackBean> list = new ArrayList<ToaFeedbackBean>();
            while(result.next()){
               String unitsName = result.getString("units_name");
               
               int total = result.getInt("total");//此部门征询意见总数
               int onTimeTotal = result.getInt("onTimeTotal");
               int laterTotal = result.getInt("laterTotal");
               BigDecimal onTimePercentage =new BigDecimal(((double)onTimeTotal/(double)total)*100);
               BigDecimal laterPercentage =new BigDecimal(((double)laterTotal/(double)total)*100);
               BigDecimal fixRavPrior = result.getBigDecimal("fixRavPrior");
               BigDecimal actualRavPrior = result.getBigDecimal("actualRavPrior");
               BigDecimal laterRavPrior = result.getBigDecimal("laterRavPrior");
               toaFeedbackBean = new ToaFeedbackBean(); 
               toaFeedbackBean.setActualRavPrior(actualRavPrior);
               toaFeedbackBean.setLaterPercentage(laterPercentage);
               toaFeedbackBean.setLaterRavPrior(laterRavPrior);
               toaFeedbackBean.setLaterTotal(laterTotal);
               toaFeedbackBean.setFixRavPrior(fixRavPrior);
               toaFeedbackBean.setUnitName(unitsName);
               toaFeedbackBean.setTotal(total);
               toaFeedbackBean.setLaterTotal(laterTotal);
               toaFeedbackBean.setOnTimePercentage(onTimePercentage);
               toaFeedbackBean.setOnTimeTotal(onTimeTotal);
               list.add(toaFeedbackBean);
               
             }
//            List<ToaReportBean> list=toaReportBeanDAO.createQuery(sql.toString()).list();
            return list;}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
       return new ArrayList<ToaFeedbackBean>();
    }
    /**
     * 填充公文统计数据
     * @author:xush
     * @date:5/6/2013 2:47 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public List<TgwReportBean> getgW(String yearGw,String selectDept){
        try {
            List<TgwReportBean1> gwList=new ArrayList<TgwReportBean1>();
            List<TgwReportBean2> numberList=new ArrayList<TgwReportBean2>();
            List<TgwReportBean2> numberList1=new ArrayList<TgwReportBean2>();
            List<Integer> list1=new ArrayList<Integer>();
           String sql = "";
           int sum;
           if(selectDept==null||"".equals(selectDept)||"null".equals(selectDept)){
           sql="select department" +
                " from T_OA_REPORT_BEAN where to_char(RECEIVE_TIME,'YYYY-MM') like '"
               +yearGw+"'"+" group by department";}
           else{
               sql="select department" +
                " from T_OA_REPORT_BEAN where to_char(RECEIVE_TIME,'YYYY-MM') like '"
               +yearGw+"'"+" and department ='"+selectDept+"'"+" group by department";
           }
            ResultSet result = null;
            
            result = toaReportBean1DAO.getConnection().createStatement().executeQuery(sql.toString());
            TgwReportBean tgwReportBean = null;
            List<TgwReportBean> list = new ArrayList<TgwReportBean>();
            while(result.next()){
               String department = result.getString("department");
               tgwReportBean = new TgwReportBean(); 
               if(department!=null){
               tgwReportBean.setDepartment(department);
               gwList = this.getGwList(department,yearGw,selectDept);//获取子报表的数据
               tgwReportBean.setGwList(gwList);
               //numberList =this.getNumberList(department,yearGw,selectDept);
               if(list1.size()==0){
                 list1.add(gwList.size());}
               else{
                  list1.add(list1.get(list1.size()-1)+gwList.size()); 
               }
               numberList1=this.getNum(list1);
               tgwReportBean.setNumberList(numberList1); 
               list.add(tgwReportBean);
               }
               
             }
//            List<ToaReportBean> list=toaReportBeanDAO.createQuery(sql.toString()).list();
            return list;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
       return null;
    }
    /**
     * 填充公文统计数据的总数
     * @author:xush
     * @date:5/6/2013 2:47 PM
     * @param parameters        报表需要的参数
     * @param jasperReport      报表
     * @param data              填充报表的数据
     * @return                  报表数据
     */
    public int getTotalNum(String yearGw,String selectDept){
        try {
            List<TgwReportBean1> gwList=new ArrayList<TgwReportBean1>();
            
           String sql = "";
           if(selectDept==null||"".equals(selectDept)||"null".equals(selectDept)){
           sql="select count(*) total" +
                " from T_OA_REPORT_BEAN where to_char(RECEIVE_TIME,'YYYY-MM') like '"
               +yearGw+"'"+" and department is not null";}
           else{
               sql="select count(*) total" +
                " from T_OA_REPORT_BEAN where to_char(RECEIVE_TIME,'YYYY-MM') like '"
               +yearGw+"'"+" and department ='"+selectDept+"'";
           }
            ResultSet result = null;
            int total=0;
            result = toaReportBean1DAO.getConnection().createStatement().executeQuery(sql.toString());
            TgwReportBean tgwReportBean = null;
            List<TgwReportBean> list = new ArrayList<TgwReportBean>();
            while(result.next()){
                total = result.getInt("total");
               }
               
             
//            List<ToaReportBean> list=toaReportBeanDAO.createQuery(sql.toString()).list();
            return total;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0; 
        
    }
	/**
	 * 报表导出Excel格式
	 * @author:邓志城
	 * @date:2010-12-24 下午03:04:16
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（第一个参数为输出标题）
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(JasperPrint jasperPrint,Object...objects) {
		try {
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			HttpServletResponse response = ServletActionContext.getResponse();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,Boolean.TRUE);
			exporter.exportReport();
			byte[] bytes = oStream.toByteArray();
			if (bytes != null && bytes.length > 0) {
				response.reset();
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","attachment; filename="+ java.net.URLEncoder.encode(objects[0]+".xls","utf-8"));
				response.setContentLength(bytes.length);
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(bytes, 0, bytes.length);
				outputStream.flush();
				outputStream.close();
			}
			oStream.close();
		} catch (Exception e) {
			logger.error("报表导出EXCEL时发生异常", e);
			throw new SystemException(e);
		}
	}
	
	/**
	 * 报表导出PDF格式
	 * @author:邓志城
	 * @date:2010-12-24 下午03:04:16
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（第一个参数为输出标题）
	 */
	public void exportPdf(JasperPrint jasperPrint,Object...objects) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(objects[0]+".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			logger.error("报表导出PDF时发生异常", e);
			throw new SystemException(e);
		}
	}
	
	/**
	 * 将报表对象生成HTML格式输出
	 * @author:邓志城
	 * @date:2010-12-23 下午04:57:32
	 * @param jasperPrint		报表对象
	 * @param objects			扩展参数（留待以后扩展）,第一个为页码
	 * @return					生成的HTML内容
	 */
	public String exportHtml(JasperPrint jasperPrint,Object...objects) {
		JRHtmlExporter exporter = new JRHtmlExporter();	
		StringBuffer content = new StringBuffer();
		try {
			/*Integer pageIndex = (Integer)objects[0];
			if(pageIndex < 0) {
				pageIndex = 0;
			}
			pageIndex = pageIndex - 1;*/
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
			exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,content);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			//exporter.setParameter(JRExporterParameter.PAGE_INDEX, pageIndex);  
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
			exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表HTML数据时发生异常", e);
			throw new SystemException(e);
		}
		return content.toString();
	}
	
	/**
	 * 根据报表模板生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:26:12
	 * @param columnList	List<Map<列头名,列头值>>	//列头
	 * 	例如Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","标题")
	 * 		map.put("COLUMNVALUE","测试标题");
	 * @return
	 */
	public JasperReport generateReport(List columnList,int columnWidth) {
		File file = new File(ServletActionContext.getServletContext().getRealPath(jrxmlPath));	 //读取报表信息
		if(file == null || !file.exists()){
			logger.error("文件" + jrxmlPath + "不存在！");
			throw new SystemException("文件" + jrxmlPath + "不存在！");
		}
		if(columnList == null) {
			throw new SystemException("参数columnList属于非法参数。");
		}
		try {
			JasperDesign jasperDesign = JRXmlLoader.load(file);
			//List l = jasperDesign.getFieldsList();
			int columnNum = columnList.size();
			//int columnWidth = 110;
			jasperDesign.setPageWidth(columnNum*columnWidth+130);	//设置页面宽度
			jasperDesign.setColumnWidth(columnNum*columnWidth+70);	//设置column宽度
			jasperDesign.setPageHeight(1100);
			int xwidth=0;//左边距
			//报表对象
			JRDesignBand columnHeader = (JRDesignBand)jasperDesign.getColumnHeader();
			JRDesignBand detail = (JRDesignBand)jasperDesign.getDetail();
			JRDesignBand titleheader=(JRDesignBand)jasperDesign.getTitle();
			JRDesignBand pagefooter=(JRDesignBand)jasperDesign.getPageFooter();
			JRDesignTextField pagetext=(JRDesignTextField)titleheader.getElementByKey("textField");
			pagetext.setX((columnNum*columnWidth+70)/2-pagetext.getWidth()/2);
			JRDesignTextField footertext=(JRDesignTextField)pagefooter.getElementByKey("textField");
			footertext.setX((columnNum*columnWidth+70)/2-footertext.getWidth()/2);

			//列头 staticText-1 位置移动 和 分组textField 位置移动
			JRDesignStaticText  statictext1 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-1");
			JRDesignTextField field1=(JRDesignTextField)detail.getElementByKey("textField");
			statictext1.setX(xwidth);
			field1.setX(xwidth);
			for (int i=0;i<columnList.size();i++){
				Map map = (Map)columnList.get(i);
				JRDesignField jrField = new JRDesignField();
				jrField.setDescription(map.get(COLUMN_TEXT).toString());
				jrField.setName(map.get(COLUMN_VALUE).toString());
				jrField.setValueClass(String.class);
				jasperDesign.addField(jrField);
				JRDesignStaticText  statictext = (JRDesignStaticText)columnHeader.getElementByKey("staticText-2"); 
				JRDesignStaticText textField = (JRDesignStaticText)statictext.clone();
				if(i==0){
					textField.setX(xwidth+50+columnWidth*(i));
					textField.setWidth(columnWidth+20);
				}else{
					textField.setX(xwidth+50+columnWidth*(i)+20);
					textField.setWidth(columnWidth);
				}
				textField.setY(0);
				textField.setHeight(35);
				textField.setKey("staticText-2"+i);
				textField.setText(map.get(COLUMN_TEXT).toString());
				//处理标题字段,标题设定宽度比其他字段长
				/*if(map.get(COLUMN_VALUE).toString().equals(BaseWorkflowManager.WORKFLOW_TITLE)) {
					textField.setWidth(textField.getWidth()+1);
					textField.setX(textField.getX()+1);
				}*/
                columnHeader.addElement(textField);
                
                String field = "$F{"+map.get(COLUMN_VALUE).toString()+"}";
                JRDesignTextField textField1 = (JRDesignTextField)detail.getElementByKey("textField-1").clone();
				textField1.setKey("textField"+(i+1));
				textField1.setY(textField.getY());
				textField1.setWidth(textField.getWidth());
				textField1.setX(textField.getX());
				textField1.setHeight(textField.getHeight());
				JRDesignExpression expression1 = new JRDesignExpression();
				expression1.setValueClass(java.lang.String.class);
				expression1.setText(field);
				textField1.setExpression(expression1);
				detail.addElement(textField1);
			}
			JRDesignTextField textFieldDel = (JRDesignTextField)detail.getElementByKey("textField-1");
			JRDesignStaticText  statictextDel = (JRDesignStaticText)columnHeader.getElementByKey("staticText-2"); 
			columnHeader.removeElement(statictextDel);
			detail.removeElement(textFieldDel);
			return  JasperCompileManager.compileReport(jasperDesign);
		} catch (JRException e) {
			logger.error("报表引擎生成报表对象时发生异常", e);
			throw new SystemException(e);
		}
	}

	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:34:24
	 * @param parameters		报表需要的参数
	 * @param jasperReport		报表
	 * @param data				填充报表的数据
	 * @return					报表数据
	 */
	public JasperPrint generateJaserPrint(Map parameters,JasperReport jasperReport,List data) {
		try {
			if(data == null) {
				throw new SystemException("参数data属于非法参数。");
			}
			return JasperFillManager.fillReport(jasperReport, parameters, new JavaBeanDataSource(data));
		} catch (JRException e) {
			logger.error("填充报表数据时发生异常", e);
			throw new SystemException(e);
		}
	}

	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午01:38:54
	 * @param parameters	报表参数
	 * @param columnList	报表列头列表
	 * 	例如:
	 * 		List columnList = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","流程标题")
	 * 		map.put("COLUMNVALUE","SENDDOC_TITME"); 
	 * 		columnList.add(map);
	 * @param data			报表列头对应的数据列表
	 * 	例如：
	 * 		List data = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("SENDDOC_TITME","这是测试标题");
	 * 		data.add(map);
	 * @return				报表
	 */
	public JasperPrint generateJaserPrint(Map parameters,List columnList,List data,String key,int columnWidth) {
		HttpSession session = ServletActionContext.getRequest().getSession();
		JasperReport jasperReport = (JasperReport)session.getAttribute("jasperReport_workflow"+key);
		if(jasperReport == null) {
			jasperReport = generateReport(columnList,columnWidth);
			session.setAttribute("jasperReport_workflow"+key, jasperReport);
			logger.info("将生成的报表写入用户SESSION中,key=" + "jasperReport_workflow"+key);
		} else {
			logger.info("在用户SESSION中找到曾访问过的报表.");
		}
		return generateJaserPrint(parameters, jasperReport, data);
	}

	/**
	 * 生成报表
	 * @author:邓志城
	 * @date:2010-12-17 下午03:21:49
	 * @param parameters	报表参数
	 * @param columnList	报表列头列表
	 * 	例如:
	 * 		List columnList = new ArrayList();
	 * 		Map map = new HashMap();
	 * 		map.put("COLUMNTEXT","流程标题")
	 * 		map.put("COLUMNVALUE","SENDDOC_TITLE"); 
	 * 		columnList.add(map);
	 * @param sql			由调用方传入的SQL语句
	 * @param page			分页对象(支持跨数据库查询)
	 * @return				报表
	 */
	public JasperPrint generateJasperPrint(Map parameters,List<Map<String, String>> columnList,final String sql,Page page,String key,int columnWidth) {
		/*List<String> columnNameList = new LinkedList<String>();
		for(int i=0;i<columnList.size();i++) {
			Map<String, String> map = columnList.get(i);
			String columnName = map.get(COLUMN_VALUE);
			if(columnName != null) {
				columnNameList.add(columnName);
			}
		}*/
		try{
			/*String columnNameStr = StringUtils.join(columnNameList, ',');
			StringBuilder SqlQuery = new StringBuilder("select ").append(columnNameStr);
			SqlQuery.append(" from ");
			String sql = "";*/
			String countSql = "select count(*) from " + StringUtils.substringAfter(sql, "from");
			logger.info(countSql);
			int totalCount = jdbcTemplate.queryForInt(countSql);
			page.setTotalCount(totalCount);
			final int biginIndex = (page.getPageNo() - 1) * page.getPageSize();
			int endIndex = biginIndex + page.getPageSize();//查询截止索引位置
			if(endIndex > page.getTotalCount()) {
				endIndex = page.getTotalCount();
			}
			if(!page.isAutoCount()) {
				jdbcTemplate.setMaxRows(endIndex);				
			}
			List data = (List)jdbcTemplate.query(new PreparedStatementCreator(){

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					return con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				}
				
			},new ResultSetExtractor(){
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(biginIndex > 0) {
						rs.absolute(biginIndex);    //直接移动游标为当前页起始记录处
					}
					return new RowMapperResultSetExtractor(new ColumnMapRowMapper()).extractData(rs);
				}
			});
			jdbcTemplate.setMaxRows(0);//重置，因为jdbcTemplate是单例的，这里需要重新设置为默认值0
			return generateJaserPrint(parameters,columnList,data,key,columnWidth);
		}catch (Exception ex) {
			logger.error("查询报表数据发生异常", ex);
			throw new SystemException(ex);
		}
	}
	
	public String getJrxmlPath() {
		return jrxmlPath;
	}

	public void setJrxmlPath(String jrxmlPath) {
		this.jrxmlPath = jrxmlPath;
	}
	
}
