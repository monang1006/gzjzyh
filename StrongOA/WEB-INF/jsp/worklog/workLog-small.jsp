<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>日程组件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<SCRIPT type="text/javascript">
				var curMonth="${currentMonth}";   
				
				function initPage(){ //初始化
					document.getElementById("m"+curMonth).style.background="#DBEABD";
				}
				 
				function setStyle(currentYear,current){	//设置点击样式 
					showID.innerHTML =currentYear+"年"+current+"月";  
					document.getElementById("m"+curMonth).style.background="white";
					document.getElementById("m"+current).style.background="#DBEABD";
					curMonth=current; 
				}
				
				function setMon(current,isNow){	//点击月份
				    if(isNow=="now"){	//如果为本月
				    	var myDate=new Date();
				    	document.getElementById("currentYear").value=myDate.getYear();
				    }
					var currentYear=document.getElementById("currentYear").value;
					setStyle(currentYear,current);
					var sdate = currentYear+"-"+current+"-01";
					var daycon = parent.document.getElementById("daycontent");
					daycon.src = "<%=path%>/worklog/workLog!viewpage.action?setDate="+sdate;
				}
				
				function getCurrentMonth(){		//获取当前时间
					var currentYear=document.getElementById("currentYear").value;
					var sdate = currentYear+"-"+curMonth+"-01";
					return sdate;
				} 
				
				function  gotoPre(){	//前一个月
					var currentYear=document.getElementById("currentYear").value;
					var current;
					if(curMonth==1){
						currentYear=currentYear*1-1;
						current=12;
					}else{
						current=curMonth*1-1;
					}
					document.getElementById("currentYear").value=currentYear;
					setMon(current)
				}
				
				function  gotoNext(){	//后一个月
					var currentYear=document.getElementById("currentYear").value;
					var current;
					if(curMonth==12){
						currentYear=currentYear*1+1;
						current=1;
					}else{
						current=curMonth*1+1;
					}
					document.getElementById("currentYear").value=currentYear;
					setMon(current)
				}
		</SCRIPT>
	</head>
	<body style="margin:0; padding:0;" onload="initPage()"  class=contentbodymargin oncontextmenu="return false;" onselectstart ='return false' >
		<input type="hidden" id="currentYear" name="currentYear" value="${currentYear}">
		<table align=center width="100%"
						bordercolor="threeddarkshadow"
						style="border-collapse: collapse; margin-top: 0px;"
						bgcolor="#DBEABD">
			<tr>
				<td align=center id="showID">${currentYear}年${currentMonth}月
				</td>
			</tr>
			<tr> 
				<td>
					<table align=center cellpadding="3" cellspacing="3"
						bordercolor="threeddarkshadow"
						style="border-collapse: collapse; margin-top: 0px; border-color: #CCCCCC;"
						 bgcolor="#ffffff">
						<TR valign=middle>
							<TD align=center height=18>
								<font id="m1" onclick="setMon(1)" style="cursor: hand;">01月</font>
							</TD>
							<TD align=center>
								<font id="m2" onclick="setMon(2)" style="cursor: hand;">02月</font>
							</TD>
							<TD align=center>
								<font id="m3" onclick="setMon(3)" style="cursor: hand;">03月</font>
							</TD>
						</TR>
						<TR valign=middle>
							<TD align=center height=18>
								<font id="m4" onclick="setMon(4)" style="cursor: hand;">04月</font>
							</TD>
							<TD align=center>
								<font id="m5" onclick="setMon(5)" style="cursor: hand;">05月</font>
							</TD>
							<TD align=center>
								<font id="m6" onclick="setMon(6)" style="cursor: hand;">06月</font>
							</TD>
						</TR>
						<TR valign=middle>
							<TD align=center height=18>
								<font id="m7" onclick="setMon(7)" style="cursor: hand;">07月</font>
							</TD>
							<TD align=center>
								<font id="m8" onclick="setMon(8)" style="cursor: hand;">08月</font>
							</TD>
							<TD align=center>
								<font id="m9" onclick="setMon(9)" style="cursor: hand;">09月</font>
							</TD>
						</TR>
						<TR valign=middle>
							<TD align=center height=18>
								<font id="m10" onclick="setMon(10)" style="cursor: hand;">10月</font>
							</TD>
							<TD align=center>
								<font id="m11" onclick="setMon(11)" style="cursor: hand;">11月</font>
							</TD>
							<TD align=center>
								<font id="m12" onclick="setMon(12)" style="cursor: hand;">12月</font>
							</TD>
						</TR>
						<tr valign=middle>
							<td align=center height=18>
								<input type="button" value="  ◄  " class="input_bg"
									onclick="gotoPre()">
							</td>
							<td align=center height=18>
								<input type="button" value=" 本月 " class="input_bg"
									onclick="setMon('${currentMonth}','now')">
							</td>
							<td align=center height=18>
								<input type="button" value="  ►  " class="input_bg"
									onclick="gotoNext()">

							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
