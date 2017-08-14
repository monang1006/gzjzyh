<%@ page language="java" import="java.util.*" autoFlush="true" pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
	<head>
	
		<TITLE>信息统计</TITLE>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
		<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">


		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css" >
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<LINK href="<%=path %>/common/frame/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
     
      <script type="text/javascript">
      
      //发布统计
      function showpub(){
       getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!statisticPub.action","发布量统计");
      }
      //点击量统计
      function showHits(){
        getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!statisticHits.action","点击量统计");
      }
      //评论统计
      function showComment(){
        getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!statisticComment.action","评论量统计");
      }
      
      //人员统计
      function showUser(){
         getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!statisticUser.action","人员编辑量统计");
      }
      //查看栏目下详细统计信息
      function showinfo(value,name){
             var begintime= $("#beginTime").val();
             var endtime=$("#endTime").val();
		      getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!columnState.action?columnId="+value+"&beginTime="+begintime+"&endTime="+endtime,name);
		}
	function showbar(begin,end){
	$("#bar").attr("src","<%=root%>/infopub/statistic/statistic.action?beginTime="+begin+"&endTime="+end+"&jpgtype=bar"); 
	
	}
	function selectdate(){
	var begintime=$("#beginTime").val();
	var endtime=$("#endTime").val();
	if(endtime!=""&&begintime>endtime){
	alert("开始时间不能大于结束时间!");
	return;
	}
	$("#datesele").submit();
	}
  </script>
  <style>
    .tabl2{
	
		border-left:1px #E1E1E1 solid;
		border-top:1px #E1E1E1 solid;

		}
	.tabl2 td{
		
		border-bottom:1px #E1E1E1 solid;
		border-right:1px #E1E1E1 solid;
		}	
  </style>
  
  </head>
  
  <body  class=contentbodymargin oncontextmenu="return false;" >
  <script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
  <DIV id=contentborder align=center>
  <table width="100%"><tr><td>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    	
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
          		<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>信息量统计</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onClick="showpub();"><img src="<%=root%>/images/operationbtn/The_release.png"/>&nbsp;发&nbsp;布&nbsp;量&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onClick="showHits();"><img src="<%=root%>/images/operationbtn/Click_a_quantity.png"/>&nbsp;点&nbsp;击&nbsp;量&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onClick="showComment();"><img src="<%=root%>/images/operationbtn/comments.png"/>&nbsp;评&nbsp;论&nbsp;量</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onClick="showUser();"><img src="<%=root%>/images/operationbtn/Edit_statistics.png"/>&nbsp;编&nbsp;辑&nbsp;人&nbsp;统&nbsp;计&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="2%"></td>
					                 	</tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
          </tr>
        </table>
        </td>
      </tr>
    </table>							
  <hr></td></tr><tr><td>
  <form action="<%=root%>/infopub/statistic/statistic.action" id="datesele" method="post">
  <table width="100%" border="0" cellpadding="0" cellspacing="0" >
							     <tr>
							       <td>
							       		&nbsp;&nbsp; 开始时间：&nbsp;<strong:newdate  name="beginTime" id="beginTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${beginTime}"/>
							       		&nbsp;&nbsp; 结束时间：&nbsp;<strong:newdate  name="endTime" id="endTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${endTime}"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"  onclick="selectdate();"/>
							       	</td>
							     </tr>
 </table> 
 </form>
	<br>  
   <table width="100%">
   <tr><td width="50%" valign="top" >
    <img src="<%=root %>/img/${temp1}.jpg">
   </td>
   <td valign="top" width="50%">
   <div style="font-weight:bolder; font-size:18px; text-align:center; width:80%;height:28px;">信息量统计表</div>
    <table cellpadding="1" cellspacing="1" width='80%' class="tabl2" >
    <tr height="25px;">
   <td align="center"  bgcolor="#E1E1E1">序号</td>
   <td align="center" bgcolor="#E1E1E1">栏目名称</td>
   <td align="center" bgcolor="#E1E1E1">文章数量</td>
   </tr>
    <c:forEach items="${list}" var="vo" varStatus="index">
   <tr height="25px;">
   <td align="center" width="10%">${index.index+1 }</td>
   <td align="left" style="padding-left:10px;" width="70%"><a href="javascript:showinfo('${vo.columnId }','${vo.columnName }')"><font color="#3366cc">
   ${vo.columnName }</font></a>
   </td>
   <td align="center" width="20%">${vo.pubCount }</td></tr>
   </c:forEach>
   </table>
    
   </td>
   
   </tr>
   <tr>
   <td width="50%" style="padding-left:20px;"> 
   
   <c:if test="${not empty list}">
   <img src="<%=root %>/img/${temp}.jpg"></c:if>
   </td>
   <td width="50%" ></td>
   
   </tr>
   </table>
   
  </td></tr></table></DIV>
  </body>
  <script>
 /* function getimg(){
  var time = new Date();
   var imgso = document.getElementById("imgs");
   imghtml = "<img src='<%=root %>/images/DrawBar.jpg?time="+time.getTime()+"' /><br>";
  <c:if test="${not empty list}">
     imghtml = imghtml+"<img src='<%=root %>/images/DrawPie.jpg?time="+time.getTime()+"' />"
  </c:if>
   imgso.innerHTML = imghtml;
  }
  window.setTimeout('getimg()',2000);*/
  //搜索
  </script>
</html>
  