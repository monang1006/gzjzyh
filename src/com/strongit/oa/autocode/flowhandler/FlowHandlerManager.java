/**
 * @copyright  : CopyRright (c) 2012 Jiang Xi Strong Co. Ltd. All right reserved.
 * @description: FlowHandlerManager.java
 * @project    : 抚州人社局人才市场系统 ——人才市场招聘网站子系统
 * @jdk        : jdk5.0 
 * @comments   : 描述类功能
 * @create     :徐淑华&xush xush@strongit.com.cn
 * @modify     : new
 * @version    : V1.0 
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 15, 2013 2:52:48 PM  
 */
package com.strongit.oa.autocode.flowhandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.oa.bo.ToaReportBean1;

import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.util.annotation.OALogger;

import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @description: 收文办件退文流程处理类
 * @author     :徐淑华&xush xush@strongit.com.cn
 * @date       : Apr 15, 2013 2:52:48 PM
 * @version    : v1.0
 * @since  
 */
@Service
@Transactional
@OALogger
public class FlowHandlerManager extends BaseManager {
    private static Map<String, String> liyousMap = new HashMap<String, String>();//定义退文原因map
    private GenericDAOHibernate<ToaReportBean1, String> toaReportBean1DAO = null;
    /**
     * 注入sessionFactory
     * 
     * @param sessionFactory
     */
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        toaReportBean1DAO = new GenericDAOHibernate<ToaReportBean1, String>(
                sessionFactory, ToaReportBean1.class);
    }
    public ToaReportBean1 getToaReportBeanById(String bussinessId) throws Exception{
        final String hql = "from ToaReportBean1 as t where t.businessId =?"; 
        List<ToaReportBean1> list = toaReportBean1DAO.find(hql,bussinessId);
        if(list!=null&& list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    private Map getLiyousMap() {
        liyousMap.put("11", "请示文件只能一文一事，不能主送多个机关;");
        liyousMap.put("12", "请示文件要有签发人、发文号、印章、联系人、联系电话，联合上报公文要注明会签人姓名;");
        liyousMap.put("13", "公文文种要使用得当，“报告”和“意见”不能夹带请示事项;");
        liyousMap.put("14", "公文中附件资料要齐全;");
        liyousMap.put("15", "有主管部门的单位、企业，应由主管部门行文;");
        liyousMap.put("16", "无主管部门的企业按属地管理原则，由县区政府、开发区（新区）管委会行文;");
        liyousMap.put("21", "属于部门职权范围内的事项应报部门办理;");
        liyousMap.put("22", "公文内容涉及其他地区或部门职权范围内的事项，主办单位应事先征求相关部门意见，并附上征求意见情况说明、相关单位盖章同意的正式意见;");
        liyousMap.put("23", "公文反映的问题要清晰，提出的要求要明确;");
        liyousMap.put("24", "应由部门发文的不能报请市政府转发（上级有明确要求的除外）;");
        liyousMap.put("25", "除上级明文规定和涉及全市的、综合性的重大工作外，设立和调整（除非常设机构正副职调整外）市政府非常设机构不要请报市政府印发;");
        liyousMap.put("26", "非常设机构确需报请市政府设立和调整的，该机构办公室应事先组织研究、协调并明确提出意见;");
        liyousMap.put("33","" );
        return liyousMap;
    }
    /**
     * 通过流程名称得到流程类型ID
     * 使登记时自动插入一条数据进入toareportbean表
     * @param workflowname
     *              流程名称
     */
    @SuppressWarnings("unchecked")
     public void addReport(String bussinessId, String instanceId) throws Exception{
        if (bussinessId != null && !"".equals(bussinessId)) {
            String[] bussinessIds = bussinessId.split(";");
            Connection con = this.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psmt = null;
            ResultSet rs = null;
            try {
                String sql = "";
                sql = "select WORKFLOWTITLE,RECV_TIME,RECV_NUM,ISSUE_DEPART_SIGNED from " + bussinessIds[0]
                        + " where  " + bussinessIds[1] + "='" + bussinessIds[2]
                        + "' ";
                psmt = con.prepareStatement(sql);
                rs = psmt.executeQuery();
                if (rs.next()) {
                    String title = rs.getString("WORKFLOWTITLE");
                    String units = rs.getString("ISSUE_DEPART_SIGNED");
                    //String department = rs.getString("SENDDOC_SUBMITTO_DEPART");
                    String receiveTime = rs.getString("RECV_TIME");
                    String sendCode=rs.getString("RECV_NUM");
                   ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                    //psmt = con.prepareStatement(sql.toString());
                    if(model==null){
                        model=new ToaReportBean1();
                      if(title != null){ 
                          model.setTitle(title);
                      }
                      if(sendCode != null){ 
                          model.setSendCode(sendCode);
                      }
                        if(units != null){
                            model.setUnits(units);
                        }
                        if(title != null){
                            model.setTitle(title);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if(receiveTime != null){
                            model.setReceiveTime(sdf.parse(receiveTime));
                        }
    
                        model.setFinishflg("0");
                        model.setReturnFlg("0");
                        model.setBusinessId(bussinessIds[2]);
                        toaReportBean1DAO.save(model);}
                    else{
                        toaReportBean1DAO.update(model); 
                    }
                }
            } catch (SQLException e) {
	            e.printStackTrace();
	        }finally{
	            try{
	                if(rs != null){
	                    rs.close();
	                }
	                if (psmt != null) {
	                    psmt.close();
	                }
	            }catch (Exception e){
	                e.printStackTrace();
	            }
	}
        }
    } 
    
    /**
     * 通过流程名称得到流程类型ID
     * 使办结状态更新
     * @param workflowname
     *              流程名称
     */
    @SuppressWarnings("unchecked")
    public void updateRturnFlg(String bussinessId, String instanceId) throws Exception{
        if (bussinessId != null && !"".equals(bussinessId)) {
            String[] bussinessIds = bussinessId.split(";");
            Connection con = this.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psmt = null;
            ResultSet rs = null;
           try {
               ToaReportBean1 model = this.getToaReportBeanById(bussinessIds[2]);
                String sql = "";
                sql = "select QITA,LIYOUS,DOC_RECV_REMARK from T_DOC_SENDS t where t.senddoc_id in (select t.doc_id from T_OARECVDOC t where t.oarecvdocid='"
                        + bussinessIds[2] + "')";
                psmt = con.prepareStatement(sql);
                rs = psmt.executeQuery();
                StringBuffer buffer=new StringBuffer();
                if (rs.next()) {
                    String qita = rs.getString("QITA");
                    String liyous = rs.getString("LIYOUS");
                    String remark = rs.getString("DOC_RECV_REMARK");
                    if(liyous!=null){
                       String[] liyou=liyous.split(",");
                       for(String reason:liyou){
                           if("33".equals(reason)){
                               buffer.append(qita+";");
                           }else{
                               buffer.append(getLiyousMap().get(reason)); 
                           }
                        }
                    }else{
                        buffer.append(qita);
                        if(qita!=null){
                        	buffer.append(";");}
                        }
                    if(null!=remark&&!"".equals(remark)){
                        buffer.append(remark+";");
                    }
                    if(model!=null&&buffer!=null){
                        model.setReturnReason(buffer.toString());
                    }
                }
                model.setReturnFlg("1");
                toaReportBean1DAO.update(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
    }
    
    public void saveToaReportBean1(TtransDoc model,TdocSend modelSend){
            try {
                ToaReportBean1 toaReportBean1 = new ToaReportBean1();
                toaReportBean1.setTitle(model.getDocTitle());
                toaReportBean1.setUnits(model.getDocIssueDepartSigned());
                toaReportBean1.setSendCode(model.getDocCode());
                toaReportBean1.setReceiveTime(model.getDocEntryTime());
                toaReportBean1.setReturnFlg("1");
                if(modelSend!=null){
                String qita = modelSend.getQita();
                String liyous = modelSend.getLiyous();
                String remark = modelSend.getDocRecvRemark();
                StringBuffer buffer = new StringBuffer();
                if (liyous != null) {
                    String[] liyou = liyous.split(",");
                    for (String reason : liyou) {
                        if ("33".equals(reason)) {
                            buffer.append(qita + ";");
                        } else {
                            buffer.append(getLiyousMap().get(reason));
                        }
                    }
                } else {
                    buffer.append(qita);
                    if (qita != null) {
                        buffer.append(";");
                    }
                }
                if (null != remark && !"".equals(remark)) {
                    buffer.append(remark + ";");
                }
                if (buffer != null) {
                    toaReportBean1.setReturnReason(buffer.toString());
                }}
                toaReportBean1DAO.save(toaReportBean1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //return toaReportBean1;
    }
    
    /**
     * 通过流程名称得到流程类型ID
     * 使办结状态更新
     * @param workflowname
     *              流程名称
     */
    @SuppressWarnings("unchecked")
    public void updateInfo(String bussinessId, String instanceId) throws Exception{
        if (bussinessId != null && !"".equals(bussinessId)) {
            String[] bussinessIds = bussinessId.split(";");
            Connection con = this.getConnection();
            con.setAutoCommit(false);
            PreparedStatement psmt = null;
            try {
                String sql = "";
                sql = "update  t_oa_report_bean set on_time_flg='1'"
                        + " where business_Id "
                        + "='"
                        + bussinessIds[2] + "' ";
               psmt = con.prepareStatement(sql.toString());
               psmt.executeUpdate();
                con.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }  
    }
}