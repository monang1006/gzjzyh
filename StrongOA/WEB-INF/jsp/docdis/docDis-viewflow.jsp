<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看各个机构接受情况</title>
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
	    	function changeContent(info){
	    		if(info=="0"){
	    			return "<font color=red>未签收</font>";
	    		}else if(info=="1"){
	    			return "已签收";
	    		}else if(info=="2"){
	    			return "已签收";
	    		}
	    		else{
	    			return "";
	    		}
	    	}
	    	
	    	function changeFont(state,content){
	    		if(state=="0"){
	    			return "<font color=red>"+content+"</font>"
	    		}else if(state=="1"){
	    			return content;
	    		}else{
	    			return content;
	    		}
	    	}
	    </script>
	</head>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<form id="meTableForm" action="<%=root %>/docdis/docDis!viewflow.action" method="get" >
				<input id="docId" name="docId" value="${docId}" type="hidden">
				<webflex:flexTable name="meTable" width="100%" height="200px" wholeCss="table1" property="docId" isCanDrag="true"
			            isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" pagename="fpage" 
			            getValueType="getValueByProperty" collection="${fpage.result}"
			            page="${fpage}"  isShowMenu="false">
			            <webflex:flexTextCol caption="下发单位" property="deptname"
			              showValue="javascript:changeFont(getOrnot,deptname)" width="40%" isCanDrag="true"
			              isCanSort="true"></webflex:flexTextCol>
			            <webflex:flexDateCol caption="收文日期"
			              property="getDocDate" showValue="getDocDate"
			              width="25%" isCanDrag="true" dateFormat="yyyy-MM-dd" isCanSort="true"></webflex:flexDateCol>
			            <webflex:flexTextCol caption="是否签收" property="getOrnot" 
			              showValue="javascript:changeContent(getOrnot)" width="10%" isCanDrag="true"
			              isCanSort="true"></webflex:flexTextCol> 
			            <webflex:flexDateCol caption="签收日期"
			              property="getDate" showValue="getDate"
			              width="25%" isCanDrag="true" dateFormat="yyyy-MM-dd" isCanSort="true"></webflex:flexDateCol>
			    </webflex:flexTable>
			</form>
		</div>
	</body>
</html>
