<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<link rel="stylesheet" href="<%=path%>/common/js/FusionCharts/Contents/Style.css" type="text/css" />
		<script language="JavaScript" src="<%=path%>/common/js/FusionCharts/JSClass/FusionCharts.js"></script>
		<style type="text/css"> 
		<!-- 
body,table, td, a {font:9pt;} 
/*重点：固定行头样式*/
.scrollRowThead{position: relative; 
left: expression(this.parentElement.parentElement
.parentElement.parentElement.scrollLeft);
z-index:0;}
/*重点：固定表头样式*/
.scrollColThead {position: relative;
top: expression(this.parentElement.parentElement
.parentElement.scrollTop-2);left: expression(this.parentElement.parentElement
.parentElement.parentElement.scrollLeft);
z-index:2;}
/*行列交叉的地方*/
.scrollCR { z-index:3;}
/*div外框*/
.scrollDiv {height:200px;clear: both; 
border: 1px solid #EEEEEE;
OVERFLOW: scroll;width: 320px; }
/*行头居中*/
.scrollColThead td,.scrollColThead th
{ text-align: center ;}
/*行头列头背景*/
.scrollRowThead,.scrollColThead td,.scrollColThead th
{background-color:EEEEEE;}
/*表格的线*/
.scrolltable{
border-bottom:1px solid #CCCCCC; 
border-right:1px solid #CCCCCC; }
/*单元格的线等*/
.scrolltable td,.scrollTable th{
border-left: 1px solid #CCCCCC; 
border-top: 1px solid #CCCCCC; 
padding: 5px; }
--> 
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
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	  <td colspan="3" class="table_headtd">
       <table width="100%" border="0" cellspacing="0"
											cellpadding="00">
        <tr>
          <td class="table_headtd_img" >
			<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
				</td>
				<td align="left">
				<strong>公文限时办理情况</strong>
				</td>
			
						</tr>
					</table>
				</td>
			</tr>
			
	
	<tr>
	<td >
		<fieldset style="overflow: auto; ">
		<s:if test="#request.charXML == 0">
				<div id="message"  align="center"  style="height:100%;width: 100%;font-size: 30;color: red;">
					当前功能为公文限时办理情况功能，要使用该功能，<br/>必须由管理员设置【分管权限】。
				</div>
			</s:if>
			<s:else>
			<div id="chartdiv"  align="center" style="height:100%;width: 100%;">
			</div>
			<script type="text/javascript">
				   var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");
				  // chart.setDataURL("<%=path%>/common/js/FusionCharts/Data/Combi2DDY.xml");		   
				   chart.setDataXML("${charXML}");
				   chart.render("chartdiv");
				</script>
			<div  id="tablediv" class="scrollDiv" align="center" style="height:100%;width: 100%;display: none;" >
			${table}
			</div>
			</s:else>
		</fieldset>
	</td>
	</tr>
	<tr><td >
	<s:if test="#request.charXML == 0">
	</s:if>
	<s:else>
		<div style="height:21px;" align="center">
			<label for="chart" style="font-size: 14;cursor:pointer" >柱状图显示</label>
			<input type="radio"  name = "totalview" id="chart" style="cursor:pointer;"  checked="checked" onclick="changeClass(this.id);onlyShow('chartdiv');">
			<label for="table" style="font-size: 14;cursor:pointer">表格显示</label>
			<input type="radio"  name = "totalview" id="table" style="cursor:pointer;"  onclick="changeClass(this.id);onlyShow('tablediv');">
		</div>
	</s:else>
	</td></tr>
	</table>
	
	
	
	
	</body>
</html>
