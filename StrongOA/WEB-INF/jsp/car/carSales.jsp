<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="/WEB-INF/tlds/runqianReport.tld" prefix="report" %>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="org.apache.log4j.*" %>
<%@ include file="/common/include/rootPath.jsp"%>

<%@ page import="java.sql.*"%>
<%@ page import="java.util.Hashtable.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.runqian.base.util.*"%>
<%@ page import="com.runqian.report.usermodel.*,com.runqian.report.pager.*"%>
<%@ page import="com.runqian.report.cellset.*"%>
<%@ page import="com.runqian.report.view.*"%>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">

	
<html>

<%
 // *******************************************************
 // 本页面作为对	普通报表	处理的公共入口，如有需要，可另行增加相应参数。
//DSJ201001081500001  ZSJ201001101100002 DSJ201001081500001
   String params="";
  //  String postcode=(String) request.getParameter("postcode");
      String startTime=(String) request.getParameter("startTime");
        String endTime=(String) request.getParameter("endTime");
        
         String reportId = (String)request.getParameter("reportId"); 
  String reportFileName = reportId+".raq"; 	//--> 对应的具体报表文件名称，含路径。
   //params="postcode="+postcode;
   
  // params="postcode="+postcode+
      //  			";startTime="+startTime+
       	//		";endTime="+endTime;
       			
      params="startTime="+startTime+
       			";endTime="+endTime;
 


%>

<BODY class=contentbodymargin>
	<DIV id=contentborder align=center>
	
		<report:html name="<%=reportId%>" reportFileName="<%=reportFileName%>"
			 funcBarLocation="bottom"
			 needPageMark="no"
			 scrollWidth="100%"
	         scrollHeight="86%" 
              needScroll="yes"
			 functionBarColor="#fff5ee"
			 funcBarFontSize="9pt"
			 funcBarFontColor="#0000FF"
			 separator="&nbsp;&nbsp;"
			 needSaveAsExcel="yes"
			 needSaveAsPdf="yes"
			 needDirectPrint="no"
			 needSelectPrinter="yes"
			 needPrint="yes"
			 savePrintSetup="yes"
			 pageMarkLabel="页号{currpage}/{totalPage}"
		     printLabel="打印"
			 displayNoLinkPageMark="yes"
             printedRaq="<%=reportFileName%>"
			 saveAsName="车辆使用情况表"
			 params="<%=params %>"
			/>	
			</DIV>
</body>
</html>