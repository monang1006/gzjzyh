<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文监控与统计</title>
		<link href="<%=path%>theme_red/css/windows.css" type='text/css'
			rel="stylesheet">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
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
		
			border-right: 1px solid #C1DAD7; 
			border-bottom: 1px solid #C1DAD7; 
			background: #fff; 
			font-size:11px; 
			padding: 6px 6px 6px 12px; 
			color: #4f6b72; 
			vertical-align: middle;
			margin:0 auto;
			background: #CAE8EA no-repeat; 
			} 
			
		td { 
		/*	border-right: 1px solid #C1DAD7; */
		/*	border-bottom: 1px solid #C1DAD7; */
			background: #fff; 
			font-size:11px; 
			padding: 6px 6px 6px 12px; 
			color: #4f6b72; 
			vertical-align: middle;
			margin:0 auto;
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
	<base target="_self"/>
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
		//alert($("#chart").attr("class")+"\n"+$("#table").attr("class"));
	}
	
	
	function onclickChar(orgId){
		parent.scrollIntoView(orgId);
		//document.getElementById("tempDiv")scrollIntoView(true);
		parent.setFramebyOrg(orgId);
	}
	
	function detail(param){
    		parent.location = "<%=path%>/fileNameRedirectAction.action?toPage=senddocRegistSta/sendDocRegistSta-Frame.jsp?selectType="+param;
    	}
	</script>
	<table style="width: 100%;height: 100%;">
	<tr>
		<td style="width: 100%;height:30px;">
			<table width="100%">
				<tr>
					<td width="50%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;&nbsp;&nbsp;<font size="3">公文监控与统计</font>
					</td>
					<td width="50%">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr>
							<s:if test="selectType!=0">
								<td>
									<a class="Operation" href="javascript:detail(0);">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													本日发文数&nbsp;</a>
								</td>
							</s:if>
							<s:if test="selectType!=1">
								<td>
									<a class="Operation" href="javascript:detail(1);">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													本周发文数&nbsp;</a>
								</td>
							</s:if>
							<s:if test="selectType!=2">
								<td>
									<a class="Operation" href="javascript:detail(2);">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													本月发文数&nbsp;</a>
								</td>
							</s:if>
							<s:if test="selectType!=3">
								<td>
									<a class="Operation" href="javascript:detail(3);">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													本年发文数&nbsp;</a>
								</td>
							</s:if>
							</tr>
						</table>
					</td>
				</tr>
			</table>
        </td>
    </tr>
	<tr>
	<td style="width:100%;height: 100%;vertical-align: middle;">
		<fieldset style="width: 100%;height: 100%; overflow: auto; ">
		<s:if test="#request.charXML == 0">
				<div id="message"  align="center"  style="height:100%;width: 100%;font-size: 30;color: red;">
					当前功能为公文监控与统计功能，要使用该功能，<br/>必须由管理员设置【分管权限】。
				</div>
			</s:if>
			<s:else>
			<div id="chartdiv"  align="center" style="height:100%;width: 100%;">
			</div>
			<script type="text/javascript">
				   var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");
				  // chart.setDataURL("<%=path%>/common/js/FusionCharts/Data/Combi2DDY.xml");		   
				  
				  //alert("charXML=\n"+"${charXML}")
				   chart.setDataXML("${charXML}");
				   /*
				   	var testXml ="<chart palette='2' caption='公文监控与统计' subCaption='' showValues='0' baseFontSize='12' divLineDecimalPrecision='1' limitsDecimalPrecision='1' PYAxisName='' SYAxisName='' numberPrefix='' formatNumberScale='0'>"
								   +"<categories ><category label='综合处' /><category label='政策法规处' /><category label='秘书一处' /><category label='秘书二处'  /><category label='秘书三处' /><category label='秘书四处' /><category label='秘书五处' /><category label='信息处' /></categories >"
								   +"<dataset seriesName='办理总数' color='1328c9'><set value='4' /><set value='3' /><set value='2' /><set value='21' /><set value='2' link='javascript:onclickChar(\"402882a03248c89901324bc7e7bd000e\")' /><set value='1' /><set value='21' /><set value='12' link='javascript:onclickChar()'/></dataset>"
								   +"</chart>";
				   chart.setDataXML(testXml);
				   */
				   chart.render("chartdiv");
				</script>
			</s:else>
		</fieldset>
	</td>
	</tr>
	<tr><td >
		<div id="tempDiv" style="height:21px;display: none">
		</div>
	</td></tr>
	</table>
	
	
	
	
	</body>
</html>
