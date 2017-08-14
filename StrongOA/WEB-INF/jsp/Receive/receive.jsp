<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文获取列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css" rel="stylesheet">   
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>    
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
		.tabletitle {
		  FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                        gradientType = 0, 
		                        startColorStr = #ededed, 
		                        endColorStr = #ffffff);
		}
		
		.hand {
		  cursor:pointer;
		}
		</style>
		<script type="text/javascript">
			var subing = false;
			function chageone(id){
	    		if(id==null||id==""){
	    			alert("请您选择要操作的文档");
	    			return false;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("一次仅能操作一个文档，请您重新选择");
	    				return false;
	    			}else{
	    				return true;
	    			}
	    		}
	    	}	    	
			function test(){
	    		location="<%=root%>/Receive/receive!query.action";
	    	}
		</script>
		<base target="_self" />
	</head>
	<body class="contentbodymargin">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
		      <tr>
		        <td height="40" class="tabletitle">
		          <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
		            <tr>
						  <td>&nbsp;</td>
						<td width="30%">
						  <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"alt="">&nbsp;
						  	公文获取选择
						</td>
					
						<td width="70">
						  <a class="Operation" href="#" onclick="test()"><img
						                    src="<%=root%>/images/ico/daoru.gif" width="15"
						     				height="20" class="img_s">获取&nbsp;</a>
						</td>
						
		            </tr>
		          </table>
		        </td>
		      </tr>
		      <tr>
		        <td height="100%">
		        <form id="myTableForm" action="<%=root%>/Receive/receive!query.action" method="get" >
		          <webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1" property="recvDocId" isCanDrag="true"
		            isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
		            getValueType="getValueByProperty" collection="${page.result}"
		            page="${page}">		           
		            <webflex:flexCheckBoxCol caption="选择" property="recvDocId"
		              showValue="recvDocId" width="3%" isCheckAll="true"
		              isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		            <webflex:flexTextCol caption="标题" property="recvdocTitle"
		              showValue="recvdocTitle"  isCanDrag="true"
		              isCanSort="true" width="40%"></webflex:flexTextCol>
		            <webflex:flexTextCol caption="文号" property="recvdocCode"
		              showValue="recvdocCode"  isCanDrag="true"
		              isCanSort="true" width="10%"></webflex:flexTextCol>
		            <webflex:flexTextCol caption="缓急" property="recvdocEmergency"
		              showValue="recvdocEmergency"  isCanDrag="true"
		              isCanSort="true" width="7%"></webflex:flexTextCol>
		            <webflex:flexTextCol caption="发文单位" property="recvdocSubmittoDepart"
		              showValue="recvdocSubmittoDepart" isCanDrag="true"
		              isCanSort="true" width="30%"></webflex:flexTextCol>           
		              
		            <webflex:flexDateCol caption="发送时间"
		              property="recvdocOfficialTime" showValue="recvdocOfficialTime"
		              isCanDrag="true" dateFormat="yyyy-MM-dd" isCanSort="true" width="10%"></webflex:flexDateCol>
		          </webflex:flexTable>
		          </form>
		        </td>
		      </tr>
			</table>
		</div>
	</body>
</html>
