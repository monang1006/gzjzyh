/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: ToDoManager.java
 * @project    : 抚州人社局人才市场系统 ——人才市场招聘网站子系统
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : May 3, 2013 4:22:49 PM  
 */
package com.strongit.oa.autocode.gongwen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaReportBean1;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.service.BaseManager;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @description: 转办处理类
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : May 3, 2013 4:22:49 PM
 * @version    : v1.0
 * @since  
 */
@Service
public class ToDoManager extends BaseManager {
    private GenericDAOHibernate<ToaReportBean1, String> toaReportBean1DAO = null;
    private GenericDAOHibernate<ToaSenddoc, String> toaSenddocDAO = null;
    /**
     * 注入sessionFactory
     * 
     * @param sessionFactory
     */
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        toaReportBean1DAO = new GenericDAOHibernate<ToaReportBean1, String>(
                sessionFactory, ToaReportBean1.class);
        toaSenddocDAO = new GenericDAOHibernate<ToaSenddoc, String>(
                sessionFactory, ToaSenddoc.class);
    }
    public ToaReportBean1 getToaReportBeanById(String bussinessId) throws Exception{
        final String hql = "from ToaReportBean1 as t where t.businessId =?"; 
        List<ToaReportBean1> list = toaReportBean1DAO.find(hql,bussinessId);
        if(list!=null&& list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    public ToaSenddoc getToaSenddocById(String bussinessId) throws Exception{
        final String hql = "from ToaSenddoc as t where t.senddocId =?"; 
        List<ToaSenddoc> list = toaSenddocDAO.find(hql,bussinessId);
        if(list!=null&& list.size() > 0){
            return list.get(0);
        }
        return null;
    }
   public void toDo(String bussiness,String bussinessId) throws Exception{
       if (bussinessId != null && !"".equals(bussinessId)) {
           String[] bussinessIds = bussinessId.split(";");
           String[] bussinesss = bussiness.split(";");
           Connection con = this.getConnection();
           con.setAutoCommit(false);
           PreparedStatement psmt = null;
           ResultSet rs = null;
          try {
              String sql="";
              sql="select PERSON_OPERATE_DATE,WORKFLOWTITLE,DOC_NUMBER,SENDOC_DEPART,CBCS from "
                  + bussinesss[0] + " where  " + bussinesss[1]+ "='" + bussinesss[2] + "' ";
              psmt = con.prepareStatement(sql);
              rs = psmt.executeQuery();
              if(rs.next()) {
                  Date receiveTime = rs.getDate("PERSON_OPERATE_DATE");
                  String title=rs.getString("WORKFLOWTITLE");
                  String department=rs.getString("CBCS");
                  String sendCode=rs.getString("DOC_NUMBER");
                  String units = rs.getString("SENDOC_DEPART");
                  //Date receiveTime = rs.getDate("RECV_TIME");
                 ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                  //psmt = con.prepareStatement(sql.toString());
                  if(model!=null){
                      model.setTurnTime(new Date());
                      toaReportBean1DAO.update(model); 
                      toaReportBean1DAO.flush();
                  }else{
                      model=new ToaReportBean1();
                      model.setTitle(title);
                      model.setSendCode(sendCode);
                      model.setDepartment(department);
                      model.setTurnTime(new Date());
                      model.setUnits(units);
                      model.setReceiveTime(receiveTime);
                      toaReportBean1DAO.save(model);
                  }
              } 
          } catch (Exception e) {
          e.printStackTrace();
      }
}
}
   /**
    * author:xush 2013-5-9
    * description:     得到处室开始时间
    * modifyer:
    * description:
    * @param bussiness1
    * @throws Exception
    */
   public Date getDeptStartTime(String bussiness1) throws Exception{
       if (bussiness1 != null && !"".equals(bussiness1)) {
           String[] bussinessIds = bussiness1.split(";");
       final String hql = "from ToaReportBean1 as t where t.businessId =?"; 
       List<ToaReportBean1> list = toaReportBean1DAO.find(hql,bussinessIds[2]);
       if(list!=null&& list.size() > 0){
           return list.get(0).getGwDeptStartTime();
       }
       }
       return null;
   }    
        /**
         * author:xush 2013-5-8
         * description:     获取处室单位
         * modifyer:
         * description:
         * @param department,bussiness
         * @throws Exception
         */
        public void insertDept(String department,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
                //String[] bussinessIds = bussinessId.split(";"); 
               try {
                      ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setDepartment(department);
                           model.setGwDeptStartTime(new Date());
                           toaReportBean1DAO.update(model); 
                       }
               } catch (Exception e) {
               e.printStackTrace();
           }
     }  
        }
        /**
         * author:xush 2013-5-8
         * description:     获取处室公文处理
         * modifyer:
         * description:
         * @param department,bussiness
         * @throws Exception
         */
        public void insertDeptSign(String gwDeptStartTime,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
               try {
                      ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setGuFinishPrior(gwDeptStartTime);
                           toaReportBean1DAO.update(model); 
                           toaReportBean1DAO.flush();
                       }
               } catch (Exception e) {
               e.printStackTrace();
           }
     }  
        }
        /**
         * author:xush 2013-5-10
         * description:     获取处室发文处理
         * modifyer:
         * description:
         * @param department,bussiness
         * @throws Exception
         */
        public void insertDeptSend(String department,Date startTime,String fwDeptSendStartTime,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
                Connection con = this.getConnection();
                con.setAutoCommit(false);
                PreparedStatement psmt = null;
                ResultSet rs = null;
               try {
                   String sql = "";
                   sql = "select WORKFLOWTITLE from "
                           + bussinessIds[0] + " where  " + bussinessIds[1]
                           + "='" + bussinessIds[2] + "' ";
                   psmt = con.prepareStatement(sql);
                   rs = psmt.executeQuery();
                   if (rs.next()) {
                       String title=rs.getString("WORKFLOWTITLE");
                       ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setFuFinishPrior(fwDeptSendStartTime);
                           toaReportBean1DAO.update(model); 
                       }else{
                           model=new ToaReportBean1();
                           model.setDepartment(department);
                           model.setReceiveTime(startTime);
                           model.setFuFinishPrior(fwDeptSendStartTime);
                           model.setTitle(title);
                           model.setBusinessId(bussinessIds[2]);
                           toaReportBean1DAO.save(model);
                       }
                    }}catch (SQLException e) {
                        
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
         * author:xush 2013-5-8
         * description:     获取领导公文处理
         * modifyer:
         * description:
         * @param department,bussiness
         * @throws Exception
         */
        public void insertLeaderSign(String gwLeaderStartTime,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
                //String[] bussinessIds = bussinessId.split(";"); 
               try {
                      ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setGlFinishPrior(gwLeaderStartTime);
                           toaReportBean1DAO.update(model); 
                           toaReportBean1DAO.flush();
                       }
               } catch (Exception e) {
               e.printStackTrace();
           }
     }  
        }
        /**
         * author:xush 2013-5-8
         * description:     获取领导发文处理
         * modifyer:
         * description:
         * @param department,bussiness
         * @throws Exception
         */
        public void insertLeaderSend(String fwLeaderStartTime,int finishPrior,String bussiness0,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
                String[] bussinessIds0 = bussiness0.split(";");
                //String[] bussinessIds = bussinessId.split(";"); 
                String sendCode=null;
               try {
                   if(bussinessIds[2].equals(bussinessIds0[2])){
                       sendCode=this.getSendC(bussiness0);
                   }
                      ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setFlFinishPrior(fwLeaderStartTime);
                           model.setFinishPrior(finishPrior);
                           if(model.getSendCode()==null||"".equals(model.getSendCode())){
                               model.setSendCode(sendCode);
                           }
                           toaReportBean1DAO.update(model); 
                       }
               } catch (Exception e) {
               e.printStackTrace();
           }
            }
        }
        /**
         * author:luosy 2013-5-4
         * description:     得到意见征询的规定反馈时间
         * modifyer:
         * description:
         * @param bussiness1
         * @throws Exception
         */
        public String getSendC(String bussiness1) throws Exception{
            if (bussiness1 != null && !"".equals(bussiness1)) {
                String[] bussinessIds1 = bussiness1.split(";");

                Connection con = this.getConnection();
                con.setAutoCommit(false);
                PreparedStatement psmt = null;
                ResultSet rs = null;
                try {
                    String sql = "";
                    sql = "select SENDDOC_CODE from " + bussinessIds1[0] + " where  "
                            + bussinessIds1[1] + "='" + bussinessIds1[2] + "' ";
                    psmt = con.prepareStatement(sql);
                    rs = psmt.executeQuery();
                    if (rs.next()) {
                        return rs.getString("SENDDOC_CODE");
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
         * author:xush 2013-5-16
         * description:     获取成文时间
         * modifyer:
         * description:
         * @param endTime,bussiness
         * @throws Exception
         */
        public void insertTime(Date endTime,String bussiness) throws Exception {
            if (bussiness != null && !"".equals(bussiness)) {
                String[] bussinessIds = bussiness.split(";");
               try {
                   ToaSenddoc model = this.getToaSenddocById(bussinessIds[2]);
                       //psmt = con.prepareStatement(sql.toString());
                       if(model!=null){
                           model.setSenddocOfficialTime(endTime);
                           toaSenddocDAO.update(model); 
                       }
               } catch (Exception e) {
               e.printStackTrace();
           }
            }
        }
               /**
                * author:xsh 2013-5-14
                * description:     得到发文时间
                * modifyer:
                * description:
                * @param bussiness1
                * @throws Exception
                */
               public Date getReceive(String bussiness1) throws Exception{
                   if (bussiness1 != null && !"".equals(bussiness1)) {
                       String[] bussinessIds1 = bussiness1.split(";");

                       Connection con = this.getConnection();
                       con.setAutoCommit(false);
                       PreparedStatement psmt = null;
                       ResultSet rs = null;
                       try {
                           String sql = "";
                           sql = "select receive_time from  t_oa_report_bean"  + " where  business_Id"
                                   +  "='" + bussinessIds1[2] + "' ";
                           psmt = con.prepareStatement(sql);
                           rs = psmt.executeQuery();
                           if (rs.next()) {
                               return rs.getDate("receive_time");
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
        }