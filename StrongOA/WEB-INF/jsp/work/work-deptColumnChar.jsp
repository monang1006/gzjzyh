<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
		<link href="<%=frameroot%>/css/windows.css" type='text/css'
			rel="stylesheet">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<link rel="stylesheet" href="<%=path%>/common/js/FusionCharts/Contents/Style.css" type="text/css" />
		<script language="JavaScript" src="<%=path%>/common/js/FusionCharts/JSClass/FusionCharts.js"></script>
		<style type='text/css'>
		body
		{
			font: normal 12px arial, tahoma, helvetica, sans-serif;
			//margin:0;
			//padding:0px;
			height: 90%;
			font: normal 11px auto "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
			color: #4f6b72; 
			background: #E6EAE9; 
			text-align:center;
		}
		th { 
			font: bold 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif; 
			color: #4f6b72; 
			border-right: 1px solid #C1DAD7; 
			border-bottom: 1px solid #C1DAD7; 
			border-top: 1px solid #C1DAD7; 
			letter-spacing: 2px; 
			text-transform: uppercase; 
			text-align: left; 
			padding: 6px 6px 6px 12px; 
			background: #CAE8EA no-repeat; 
			vertical-align: middle;
			margin:0 auto;
			} 
			
		td { 
			border-right: 1px solid #C1DAD7; 
			border-bottom: 1px solid #C1DAD7; 
			background: #fff; 
			font-size:11px; 
			padding: 6px 6px 6px 12px; 
			color: #4f6b72; 
			vertical-align: middle;
			margin:0 auto;}
			} 
		.selected{
			float: left;
			width: 73px;
			height:21px;
			background-image:url('<%=path%>/oa/image/personnel/anniu-blue.jpg') ; 
			vertical-align: bottom;
			font-size: 18;
			cursor:pointer;
		}
		.noselected{
			float: left;
			width: 73px;
			height:21px;
			background-image:url('<%=path%>/oa/image/personnel/anniu-hui.jpg')  ; 
			vertical-align: bottom;
			font-size: 18;
			cursor:pointer;
		}
		</style>
	</head>
	<body class="contentbodymargin" >
	<script type="text/javascript">
	function onlyShow(value){
		$("#chartdiv").hide();
		$("#tablediv").hide();
		$("#"+value).show();
	}
	function changeClass(value){
		if(value=='chart'){
			if($("#chart").attr("class")=="noselected"){
				$("#chart").attr("class","selected");
			}
			$("#table").attr("class","noselected");
		}else{
			if($("#table").attr("class")=="noselected"){
				$("#table").attr("class","selected");
			}
			$("#chart").attr("class","noselected");
		}
		//alert($("#chart").attr("class")+","+$("#table").attr("class"));
	}
	</script>
	<table style="width: 100%;height: 100%;">
	<tr>
	<td style="width:100%;height:30px;">
	<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;&nbsp;&nbsp;&nbsp;<font size="3">处室公文办理情况</font></td>
    </tr>
	<tr>
	<td style="width:100%;height: 100%;vertical-align: middle;">
		<fieldset style="width: 100%;height: 100%; overflow: auto; ">
			<div id="chartdiv"  align="center" style="height:100%;width: 100%;">
			</div>
			<script type="text/javascript">
				   var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");
				   chart.setDataXML("${charXML}");
				   chart.render("chartdiv");
				   //单击柱状图时，调用该方法
				   function myJS(deptId,workflowType,state,processTimeout){
				   /*
				   		window.location.href="<%=path%>/work/work!personColumnChar.action"
				   							  +"?deptId="+deptId+"&workflowType="+workflowType
				   							  +"&state="+state+"&processTimeout="+processTimeout;
				   							  */
				   		window.location.href="<%=path%>/work/work!todoOfAppointDept.action"
				  							+"?state="+state+"&workflowType="+workflowType
				  							+"&appointDeptId="+deptId+"&processTimeout="+processTimeout;
				   }
				</script>
		</fieldset>
	</td>
	</tr>
	</table>
	
	
	
	
	</body>
</html>
