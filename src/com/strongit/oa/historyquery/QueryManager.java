package com.strongit.oa.historyquery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.Recivedoc;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.infotable.IInfoTableService;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class QueryManager{
	
	@Autowired private InfoItemManager itemManager;					//信息项manager
	
	/** 用户管理接口*/
	private IUserService userservice;
	
	@Autowired private IInfoTableService infoTable;		
	GenericDAOHibernate<Recivedoc, String> docDao ;	//DAO处理类//信息集服务接口
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {	
	   docDao = new GenericDAOHibernate<Recivedoc, java.lang.String>(session, Recivedoc.class);
	}
	
	
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 11, 2010 10:45:13 AM
	 */
	public List<ToaSysmanageProperty> generateColumnList(HttpServletRequest request,ToaSysmanageStructure struct,String groupByFiled){
		List<ToaSysmanageProperty> columnList=new ArrayList<ToaSysmanageProperty>();
		String itemCodes="";	
		String groupFiledCode="";	//分组字段对应的编码
		String infoSetValue=struct.getInfoSetValue();
		String infoSetCode=struct.getInfoSetCode();
		Cookie sCookie = null;
		Cookie[] cookies = request.getCookies();
		for (int i=0;cookies!=null&&i<cookies.length; i++){
			sCookie = cookies[i];
			if (sCookie.getName().startsWith(infoSetValue)&&sCookie.getValue()!= null&&sCookie.getValue().length() > 0) {
				itemCodes=sCookie.getValue();
			}
		}		
		if(groupByFiled!=null&&!"".equals(groupByFiled)&&!"0".equals(groupByFiled)){//分组字段不为空
			ToaSysmanageProperty infoItem=itemManager.getInfoItemByValue(groupByFiled, infoSetCode);
			groupFiledCode=infoItem.getInfoItemCode();	//获取分组字段的编码
		}		
		if(!"".equals(itemCodes)){		//如果cookie中有值
			if(!"".equals(groupFiledCode)&&itemCodes.indexOf(groupFiledCode)==-1){	//分组字段编码不为空，并且不在cookie中
				itemCodes=itemCodes+","+groupFiledCode;
			}
			columnList=itemManager.getInfoItemsByCodeArray(itemCodes.split(","));//获取信息项列表
		}else{
			columnList=itemManager.getCreatedItemsByValue(infoSetValue);	//过滤掉照片、备注、文件类型和隐藏的字段，并且condtion为空的。
		}	
		return columnList;
	}
	
	/*
	 * 
	 * Description:获取报表数据列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 11, 2010 10:45:28 AM
	 */
	public List<ToaReportBean> generateReportData(List<ToaSysmanageProperty> columnList,ToaSysmanageStructure struct,
				String condition,String groupByFiled)throws SystemException,ServiceException{
		try {
			String orderStr="";			//排序	
			if(groupByFiled!=null&&!"".equals(groupByFiled)&&!"0".equals(groupByFiled)){//分组字段不为空
				orderStr=struct.getInfoSetValue()+"."+groupByFiled;
			}		
			List<ToaReportBean> reportDateList=infoTable.getRecordListByCondition(columnList, struct.getInfoSetCode(), condition, orderStr, groupByFiled);//获取展现报表的数据列表			
			return reportDateList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取报表数据列表"});
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 29, 2010 5:35:32 PM
	 */
	public JasperPrint getJasperPrint(HttpServletRequest request,List<ToaSysmanageProperty> clumnList,List<ToaReportBean> reportDateList,ToaSysmanageStructure struct,String groupByFiled)throws Exception{
		try{
			Map parameters = new HashMap();
			parameters.put("ReportTitle", struct.getInfoSetName()); //如果报表中有用到变量，在这里给它赋值．
			JasperReport jasperReport = null;	//编译后的报表文件	
			if(groupByFiled!=null&&!"".equals(groupByFiled)){	//不分组报表
				jasperReport=getreportBygroup(request,clumnList,groupByFiled);	//组装分组报表
			}else{
				jasperReport=getreport(request,clumnList);			//组装不分组报表
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,new DataSource(reportDateList));
			return jasperPrint;	
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 画报表信息
	 * @author 胡丽丽
	 * @date 2010-01-28
	 * @return
	 */
	public JasperReport getreport(HttpServletRequest request,List<ToaSysmanageProperty> columnList)throws Exception{
		
		try {
			String filePath ="/WEB-INF/jsp/historyquery/report/report_test.jrxml"; // 报表文件地址  
			File file = new File(request.getRealPath(filePath));	 //读取报表信息
            JasperDesign jasperDesign = new JasperDesign();
			jasperDesign = JRXmlLoader.load(file);
			int columnNum = columnList.size();
			int columnWidth = 110;
			jasperDesign.setPageWidth(columnNum*columnWidth+130);	//设置页面宽度
			jasperDesign.setColumnWidth(columnNum*columnWidth+70);	//设置column宽度
			jasperDesign.setPageHeight(1100);
			int xwidth=0;//左边距
			
			//报表对象
			JRDesignBand columnHeader = (JRDesignBand)jasperDesign.getColumnHeader();
			JRDesignBand detail = (JRDesignBand)jasperDesign.getDetail();
			JRDesignBand titleheader=(JRDesignBand)jasperDesign.getTitle();
			JRDesignBand pagefooter=(JRDesignBand)jasperDesign.getPageFooter();

			//表头staticText-3
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
				ToaSysmanageProperty property=columnList.get(i);
				//生成列头
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
				textField.setText(property.getInfoItemSeconddisplay());
                columnHeader.addElement(textField);
                
				//生成表内容
				String field = "$F{text"+(i+1)+"}";
                JRDesignTextField textField1 = (JRDesignTextField)(((JRDesignBand)jasperDesign.getDetail()).getElementByKey("textField-1")).clone();
				textField1.setKey("textField"+(i+1));
				textField1.setY(0);
				if(i==0){
					textField1.setWidth(columnWidth+20);
					textField1.setX(xwidth+50+columnWidth*(i));
				}else{
					textField1.setWidth(columnWidth);
					textField1.setX(xwidth+50+columnWidth*(i)+20);
				}
				textField1.setHeight(35);
				JRDesignExpression expression1 = new JRDesignExpression();
				expression1.setValueClass(java.lang.String.class);
				expression1.setText(field);
				textField1.setExpression(expression1);
				detail.addElement(textField1);
			}
			
			//删除原有的内容
			JRDesignTextField textFieldDel = (JRDesignTextField)detail.getElementByKey("textField-1");
			JRDesignStaticText  statictextDel = (JRDesignStaticText)columnHeader.getElementByKey("staticText-2"); 
			columnHeader.removeElement(statictextDel);
			detail.removeElement(textFieldDel);
			return  JasperCompileManager.compileReport(jasperDesign);
		}catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 画报分组表信息
	 * @author 胡丽丽
	 * @date 2010-01-28
	 * @return
	 */
	public JasperReport getreportBygroup(HttpServletRequest request,List<ToaSysmanageProperty> columnList,String groupByFiled)throws Exception{
		try {
			String filePath ="/WEB-INF/jsp/historyquery/report/report_group1.jrxml"; // 报表文件地址  
			//读取报表信息
			File file = new File(request.getRealPath(filePath));
			JasperDesign jasperDesign = new JasperDesign();
			jasperDesign = JRXmlLoader.load(file);
			int columnNum = columnList.size();
			int columnWidth = 110;
			
			jasperDesign.setPageWidth((columnNum-1)*columnWidth+170);	//设置页面宽度
			jasperDesign.setColumnWidth((columnNum-1)*columnWidth+110);	//设置内容宽度
			jasperDesign.setPageHeight(1100);
			int xwidth=0;//左边距
			
			//获取报表属性
			JRDesignBand columnHeader = (JRDesignBand)jasperDesign.getColumnHeader();
			JRDesignBand detail = (JRDesignBand)jasperDesign.getDetail();
			JRDesignBand groupfoot=(JRDesignBand)jasperDesign.getGroups()[0].getGroupFooter();
            JRDesignBand titleheader=(JRDesignBand)jasperDesign.getTitle();
            JRDesignBand pagefooter=(JRDesignBand)jasperDesign.getPageFooter();

            //表头staticText-3
            JRDesignTextField pagetext=(JRDesignTextField)titleheader.getElementByKey("textField");
            pagetext.setX(((columnNum-1)*columnWidth+110)/2-pagetext.getWidth()/2);
            JRDesignTextField footertext=(JRDesignTextField)pagefooter.getElementByKey("textField");
			footertext.setX(((columnNum-1)*columnWidth+110)/2-footertext.getWidth()/2);
			
			//移动列头
			JRDesignStaticText  statictext1 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-2"); 
			statictext1.setX(xwidth);
			
			//移动分组列头
			JRDesignTextField  grouptext1 = (JRDesignTextField)groupfoot.getElementByKey("textField"); 
			grouptext1.setX(xwidth);
			
			//移动列表staticText-4
			JRDesignStaticText  statictext4 = (JRDesignStaticText)detail.getElementByKey("staticText-4"); 
			statictext4.setX(xwidth);
			statictext4.setText("  ");
			
			//获取模版单元格
			JRDesignStaticText  statictext = (JRDesignStaticText)columnHeader.getElementByKey("staticText-3"); 
			JRDesignStaticText  grouptext = (JRDesignStaticText)groupfoot.getElementByKey("staticText-1"); 
			JRDesignStaticText  statictext5 = (JRDesignStaticText)columnHeader.getElementByKey("staticText-5"); 
			JRDesignTextField textfield=(JRDesignTextField)detail.getElementByKey("textField-1");
			
			//获取分组名称
			for (int i=0;i<columnList.size();i++){
				ToaSysmanageProperty property=columnList.get(i);
				if(groupByFiled.equals(property.getInfoItemField())){
					statictext1.setText(property.getInfoItemSeconddisplay());
					columnList.remove(i);
				}
			}
			
			for (int i=0,j=1;i<columnList.size();i++,j++){
				ToaSysmanageProperty property=columnList.get(i);
				
				//生成列头
				JRDesignStaticText textField = (JRDesignStaticText)statictext.clone();
				if(i==0){
					textField.setX(xwidth+90+columnWidth*(i));
					textField.setWidth(columnWidth+20);
				}else{
					textField.setX(xwidth+90+columnWidth*(i)+20);
					textField.setWidth(columnWidth);
				}
				textField.setY(0);
				textField.setHeight(35);
				textField.setKey("staticText-3"+i);
				textField.setText(property.getInfoItemSeconddisplay());
				columnHeader.addElement(textField);

				//生成分组列头
				JRDesignStaticText groupfield=null;
				if(i>=(columnList.size()-1)){//判断是否是最后一个单元格
					
					groupfield = (JRDesignStaticText)statictext5.clone();
				}else{
					groupfield=(JRDesignStaticText)grouptext.clone();
				}
				if(i==0){
					groupfield.setX(xwidth+90+columnWidth*(i));
					groupfield.setWidth(columnWidth+20);
				}else{
					groupfield.setX(xwidth+90+columnWidth*(i)+20);
					groupfield.setWidth(columnWidth);
				}
				groupfield.setY(0);
				groupfield.setHeight(35);
				groupfield.setKey("staticText-3"+i);
				groupfield.setText("  ");
				groupfoot.addElement(groupfield);

				//生成表内容
				String field = "$F{text"+(j+1)+"}";
				JRDesignTextField textField1 = (JRDesignTextField)textfield.clone();
				if(i==0){
					textField1.setX(xwidth+90+columnWidth*(i));
					textField1.setWidth(columnWidth+20);
				}else{
					textField1.setX(xwidth+90+columnWidth*(i)+20);
					textField1.setWidth(columnWidth);
				}
				textField1.setKey("textField"+(i+1));
				textField1.setY(0);
				textField1.setHeight(35);
				JRDesignExpression expression1 = new JRDesignExpression();
				expression1.setValueClass(java.lang.String.class);
				expression1.setText(field);
				textField1.setExpression(expression1);
				detail.addElement(textField1);
			}
			//删除原有的内容
			columnHeader.removeElement(statictext);
			columnHeader.removeElement(statictext5);
			detail.removeElement(textfield);
			groupfoot.removeElement(grouptext);
			return  JasperCompileManager.compileReport(jasperDesign);
		}catch (Exception e) {
			throw e;
		}
	}

	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 29, 2010 4:48:09 PM
	 */
	public void printToPrinter(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			ServletOutputStream ouputStream = response.getOutputStream();	
		    //JasperPrintManager.printReport(jasperPrint, true);	//直接打印,不用预览PDF直接打印  true为弹出打印机选择.false为直接打印
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);	//将JasperPrint对象写入对象输出流中 
			oos.flush();
			oos.close();  
			ouputStream.flush();
			ouputStream.close();
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }


    /*
     * 
     * Description:将报表导出为Excel文件
     * param: 
     * @author 	    彭小青
     * @date 	    Jan 29, 2010 4:48:17 PM
     */
    public void printToExcel(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,ToaSysmanageStructure struct) throws Exception {
    	try{
    		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
    		JRXlsExporter exporter = new JRXlsExporter();
    		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
    		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
    		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
    		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
    		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
    		exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,Boolean.TRUE);
    		exporter.exportReport();
    		byte[] bytes = oStream.toByteArray();
    		if (bytes != null && bytes.length > 0) {
    			response.reset();
    			response.setContentType("application/vnd.ms-excel");
    			response.setHeader("Content-Disposition","attachment; filename="+ java.net.URLEncoder.encode(struct.getInfoSetName()+".xls","utf-8"));
    			response.setContentLength(bytes.length);
    			ServletOutputStream outputStream = response.getOutputStream();
    			outputStream.write(bytes, 0, bytes.length);
    			outputStream.flush();
    			outputStream.close();
    		}
    		oStream.flush();
    		oStream.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /*
     * 
     * Description:导出成PDF
     * param: 
     * @author 	    彭小青
     * @date 	    Jan 29, 2010 7:27:27 PM
     */
    public void printToPDF(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,ToaSysmanageStructure struct){
    	ServletOutputStream ouputStream;
		try{
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct.getInfoSetName()+".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    
    
    public List<ToaReportBean> getDJCXObjList(String qszt,String recvNum,String workTitle,String docNumber,String departSigned,String zbcs,String csqs,String recvstartTime,String recvendTime,String recvStartNum,String recvEndNum)throws SystemException,ServiceException, UnsupportedEncodingException, ParseException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list=new ArrayList<ToaReportBean>();
			ToaReportBean report;
			StringBuffer sql = new StringBuffer("");
			sql.append("select a.RECV_NUM,a.WORKFLOWTITLE,a.ISSUE_DEPART_SIGNED,a.DOC_NUMBER,a.RECV_TIME,a.ZBCS,a.CSQS from(select t.RECV_NUM,t.WORKFLOWTITLE,t.ISSUE_DEPART_SIGNED,t.DOC_NUMBER,t.RECV_TIME,t.ZBCS,t.CSQS,t.FCZT,"
					  +"Row_Number() Over(Partition By t.recv_num, t.workflowtitle, t.issue_depart_signed, t.DOC_NUMBER, t.RECV_TIME order by t.recv_num) rm from t_oarecvdoc t where 1=1 and t.qszt is not null and t.Workflowstate!='2' and t.Workflowstate!='-1' and (t.fczt != '1' or t.fczt is null) ");
			
			String userId=userservice.getCurrentUser().getUserId();
			
			if(userId!=null&&!"".equals(userId)){
				sql.append("and t.workflowauthor='").append(userId).append("'");
			}
			
			if(qszt!=null&&!"".equals(qszt)){
				sql.append("and t.QSZT='").append(qszt).append("'");
			}
			if(recvNum != null &&!"null".equals(recvNum) && !"".equals(recvNum)){
				sql.append(" and t.RECV_NUM like '%" + recvNum
						+ "%'");
			}
			if(workTitle != null &&!"null".equals(workTitle) && !"".equals(workTitle)){
				sql.append(" and t.WORKFLOWTITLE like '%" + workTitle
						+ "%'");
			}
			if(docNumber != null &&!"null".equals(docNumber) && !"".equals(docNumber)){
				sql.append(" and t.DOC_NUMBER like '%" +docNumber
						+ "%'");
			}
			if(departSigned != null &&!"null".equals(departSigned) && !"".equals(departSigned)){
				sql.append(" and t.ISSUE_DEPART_SIGNED like '%" + departSigned
						+ "%'");
			}
			if(zbcs != null &&!"null".equals(zbcs) && !"".equals(zbcs)){
				sql.append(" and t.ZBCS like '%" + zbcs
						+ "%'");
			}
			if(csqs != null &&!"null".equals(csqs) && !"".equals(csqs)){
				sql.append(" and t.CSQS like '%" + csqs
						+ "%'");
			}
			if(recvstartTime != null &&!"null".equals(recvstartTime) && !"".equals(recvstartTime)){
				sql.append(" and t.RECV_TIME >=to_date('"+ recvstartTime+"','yyyy-MM-dd')");
			}
			if(recvendTime != null &&!"null".equals(recvendTime) && !"".equals(recvendTime)){
				sql.append(" and t.RECV_TIME <=to_date('"+ recvendTime+"','yyyy-MM-dd')");
			}
			
			if(recvStartNum != null &&!"null".equals(recvStartNum) && !"".equals(recvStartNum)){
				sql.append(" and t.RECV_NUM >='"+ recvStartNum+"'");
			}
			if(recvEndNum != null &&!"null".equals(recvEndNum) && !"".equals(recvEndNum)){
				sql.append(" and t.RECV_NUM <='"+ recvEndNum+"'");
			}
			sql.append(") a where a.rm = 1 order by a.RECV_NUM");
			ResultSet rs=docDao.executeJdbcQuery(sql.toString());
			if(rs!=null){
				while(rs.next()){
					report=new ToaReportBean();
					if(rs.getString(1)!=null){
					report.setText1(rs.getString(1));
					}else{
						report.setText1(" ");
					}
					if(rs.getString(2)!=null){
						report.setText2(rs.getString(2));
						}else{
							report.setText2(" ");
						}
					if(rs.getString(3)!=null){
						report.setText3(rs.getString(3));
						}else{
							report.setText3(" ");
						}
					if(rs.getString(4)!=null){
						report.setText4(rs.getString(4));
						}else{
							report.setText4(" ");
						}
					
					Date begin=(Date)rs.getDate(5);
					if(begin!=null){                  
					  report.setText5(df.format(begin));
					}else
						report.setText5(" ");
					if(rs.getString(6)!=null){
						report.setText6(rs.getString(6));
						}else{
							report.setText6(" ");
						}
					if(rs.getString(7)!=null){
						report.setText7(rs.getString(7));
						}else{
							report.setText7(" ");
						}
					
					
					list.add(report);
					
				}
			}
			return list;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
    
    public List<ToaReportBean> getBorrowReport(String borrowMonth,String end,String docTitle) throws SystemException, ServiceException, UnsupportedEncodingException, ParseException{
    	
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	List<ToaReportBean> reportList=new ArrayList<ToaReportBean>();//报表列表
		ToaReportBean bean=null;//报表对象
		List<Object> list;
		
	     list = this.getObjList(borrowMonth, end, docTitle);
	
		for(Object obj:list){
			bean=new ToaReportBean();
			Object[] strobj=(Object[])obj;
			if(strobj[0]!=null){
				String yy=strobj[0].toString().substring(0,1);
				bean.setText1(yy);
				String xx=strobj[0].toString();
				StringBuffer x=new StringBuffer();
				if(xx.length()>10){
					x.append(xx.substring(0,xx.length()/2)).append("\n").append(xx.substring(xx.length()/2+1));	
					xx=x.toString();
				}
				bean.setText3(xx);
			}else{
				bean.setText1("  ");
				bean.setText3("  ");
			}
			
			Date begin=(Date)strobj[1];//收文日期
			if(begin!=null){
				bean.setText2(df.format(begin));
			}else{
				bean.setText2("  ");
			}
		
			if(strobj[2]!=null){
				bean.setText4(strobj[2].toString());
			}else{
				bean.setText4("  ");
			}
			if(strobj[3]!=null){
				bean.setText5(strobj[3].toString());
			}else{
				bean.setText5("  ");
			}
			if(strobj[4]!=null){
				bean.setText6(strobj[4].toString());
			}else{
				bean.setText6("  ");
			}
			
			reportList.add(bean);
		}
		return reportList;
    }
    
    public List<Object> getObjList(String borrowMonth,String end,String docTitle)throws SystemException,ServiceException, UnsupportedEncodingException, ParseException{
		try {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String hql="";
			Object[] values = new Object[2];
			StringBuffer queryStr = new StringBuffer(
					"select t.recvnum, t.recvTime, t.workflowTitle,t.departsigned, t.docNumber from Recivedoc t where 1=1 and (t.workflowState!='2' or t.workflowState is null)");
			
			if (docTitle != null && !"".equals(docTitle)) {
				queryStr.append(" and t.recvnum like '%" + docTitle + "%'");
			}
			if (borrowMonth != null && !"".equals(borrowMonth)) {
				Date start=df.parse(borrowMonth);
				queryStr.append(" and t.recvTime>=?");
				values[0] = start;
			} else {
				queryStr.append(" and 1=?");
				values[0] = 1;
			}
			if (end != null && !"".equals(end)) {
				Date endTime=df.parse(end);
				endTime.setHours(23);
				endTime.setMinutes(59);
				endTime.setSeconds(59);
				queryStr.append(" and t.recvTime<=?");
				values[1]=endTime;
			} else {
				queryStr.append(" and 1=?");
				values[1] = 1;
			}
			queryStr.append(" order by t.recvnum");
			hql=queryStr.toString();
			List<Object> list=docDao.find(hql,values);
			
			return list;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
    
    /**
	 * 日期转换成字符串
	 * @param date
	 * @param temp
	 * @return
	 */
	public String getDateToString(Date date,String temp){
		SimpleDateFormat df=new SimpleDateFormat(temp);
		return df.format(date);
	}


	public IUserService getUserservice() {
		return userservice;
	}

	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}
    
}
