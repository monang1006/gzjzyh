<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/include/rootPath.jsp"%>
<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/tlds/runqianReport.tld" prefix="report" %>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="org.apache.log4j.*" %>

<%@ page import="java.sql.*"%>
<%@ page import="java.util.Hashtable.*"%>
<%@ page import="javax.sql.*"%>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.runqian.base.util.*"%>
<%@ page import="com.runqian.report.usermodel.*,com.runqian.report.pager.*"%>
<%@ page import="com.runqian.report.cellset.*"%>
<%@ page import="com.runqian.report.view.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<html>
<script language="javascript">
    
	</script>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>报表查询</title>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
	
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>

		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
											<td width="5">&nbsp;</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												车辆使用情况表
											</td>
							
							
											<td width="80%">&nbsp;</td>
											
										</tr>
										<tr>
											<td width="5">&nbsp;</td>
										</tr>
									</table>
									<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								 
								<tr>
									<td width="10%" align="center" class="biao_bg1">
											统计月份:
									</td>
									<td width="10%" class="biao_bg1">
										<strong:newdate name="searchdate" id="searchdate" 
                      skin="whyGreen" isicon="true" dateobj="${searchdate}" dateform="yyyy-MM" width="100%" classtyle="search" title="日期" ></strong:newdate>
                     </td>
                     
									<td width="10%" align="center" class="biao_bg1">
											使用时间:
									</td>
									<td width="15%" class="biao_bg1">
										<strong:newdate name="startTime" id="startTime" 
                      skin="whyGreen" isicon="true" dateobj="${startTime}" dateform="yyyy-MM-dd HH:mm:ss" width="100%" classtyle="search" title="开始日期"></strong:newdate>
                     </td>
                     
                     <td width="3%" align="center" class="biao_bg1">
									--		
						</td>
									<td width="15%" class="biao_bg1">
									
                      <strong:newdate name="endTime" id="endTime" 
                      skin="whyGreen" isicon="true" dateobj="${endTime}" dateform="yyyy-MM-dd HH:mm:ss" width="100%" classtyle="search" title="截止日期"></strong:newdate>
									</td>
									
										<td width="5%" class="biao_bg1">
										<input type="button" value="开始查询" onclick="showReport();"/>
									</td>	
									
								</tr>
								
							</table>
							
	
	</table>

	
								</td>
								
							</tr>
						</table>
					
						
					</td>
				</tr>
			</table>
			 <div id="reportContent" style="display:none">
             <iframe id="printdiv" height="100%" width="100%" frameborder="no" border="0" marginwidth="0" marginheight="0"  scrolling="no"></iframe>	
			</div>
		</DIV>
	
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
   
 //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	var arrsDate2=stime.split(' ');
         	stime=arrsDate2[0]+""+arrsDate2[1];
         	var arrsDate3=stime.split(':');
         	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
       
   //  function choiceOrg(){	
	// var result=window.showModalDialog("<%=path%>/reportManage/reportManage!choiceOrg2.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
//} 

	function showReport(){
				var searchdate=$("#searchdate").val();
				
			if(searchdate!='' && searchdate!=null){
				   var ss=searchdate.toString();
			       var s=ss.substring(5,7);
			       var y=ss.substring(0,4);
					var d=new Date()
					d.setFullYear(y)	
                   d.setMonth(s-1)
                  var MonthFirstDay=new Date(d.getYear(),d.getMonth(),1);   
                  var  MonthNextFirstDay=new  Date(d.getYear(),d.getMonth()+1,1);   
                    var  MonthLastDay=new Date(MonthNextFirstDay-86400000); 
                    mm=MonthFirstDay.getMonth()+1;
                    dd=MonthFirstDay.getDate();
                      mm=mm<10?"0"+mm:mm;
                      dd=dd<10?"0"+dd:dd;
                      ml=MonthLastDay.getMonth()+1;  
                      dl=MonthLastDay.getDate(); 
                      ml=ml<10?"0"+ml:ml;
                      dl=dl<10?"0"+dl:dl;
                   var ff=MonthFirstDay.getFullYear()+'-'+mm+'-'+dd+' '+'00:'+'00:'+'01';
                //   alert(ff);   
                  var last=MonthLastDay.getFullYear()+'-'+ml+'-'+dl+' '+'23:'+'59:'+'59';
                //   alert(last);        
				 document.getElementById("startTime").value=ff;	
				 document.getElementById("endTime").value=last;
				}
				else{
				    var startTime=$("#startTime").val();
				    var endTime=$("#endTime").val();        
			    if(startTime=='' || startTime==null){
					alert("请选择开始时间！");
					return;
				 }
				if(endTime=='' || endTime==null){
					alert("请选择结束时间！");
					return ;
				}
		 if(startTime!='' && endTime!==''){
            if(date2string(startTime)>date2string(endTime)){
			alert("开始时间不能比结束时间晚！");
		      return;
	     }
	   }
	}
	
	               var startTime=$("#startTime").val();
				  var endTime=$("#endTime").val();        
	 document.getElementById("reportContent").style.display="";//显示 
	 var reportId="";
	
	 reportId="paichefeiyongbiao";
	 
	 document.getElementById("printdiv").src="<%=path%>/fileNameRedirectAction.action?toPage=/car/carSales.jsp?startTime="+startTime+"&endTime="+endTime+"&reportId="+reportId;		
					}

</script>
	</BODY>
</HTML>
