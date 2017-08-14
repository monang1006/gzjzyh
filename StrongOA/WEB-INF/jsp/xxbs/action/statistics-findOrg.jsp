<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>采用统计</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message-${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
        <style type="text/css">
		 *{ margin:0; padding:0; }
		 html,body{ background-color:#fff; }
         .dcytjm{ padding:22px; }
		 .dcytjm table{ border-collapse:collapse; border-spacing:0; }
		 .dcytjm th{ height:26px; padding:0 2px; border:1px solid #b7b7b7; font-size:16px; color:#333; font-family: 楷体_GB2312 ;}
		 .dcytjm td{ height:26px; padding:0 2px; border:1px solid #b7b7b7; font-size:14px; color:#333; font-family: 仿宋_GB2312 ;}
		 .dcytjm td{ color:#333; text-align:center; font-size:16px;}
		 .dcytjm th{ background-color:#f5f7f8; }
		 .dmenutop{ padding:8px 22px; background:#e5eef7; border-bottom:1px solid #b7b7b7; }
        </style>
        <style type="text/css" media=print>
.noprint{display : none }
</style>
	</head>
	
	<body>
	
	<script>
		$("#findOrg").click(function(){
			location = "${root}/xxbs/action/statistics!findOrg.action";
		});
	</script>
    <div class="main_up_out">
    <span class="noprint">
      <div class="dmenutop">
      统计年份：<select name="year" id="year">
        		<option value="<s:property value="years[0]"/>"><s:property value="years[0]"/></option>
        		<option value="<s:property value="years[1]"/>"><s:property value="years[1]"/></option>
        		<option value="<s:property value="years[2]"/>"><s:property value="years[2]"/></option>
        		<option value="<s:property value="years[3]"/>"><s:property value="years[3]"/></option>
        		<option value="<s:property value="years[4]"/>"><s:property value="years[4]"/></option>
        		<option value="<s:property value="years[5]"/>" selected="selected"><s:property value="years[5]"/></option>
        		<option value="<s:property value="years[6]"/>"><s:property value="years[6]"/></option>
        		<option value="<s:property value="years[7]"/>"><s:property value="years[7]"/></option>
        		<option value="<s:property value="years[8]"/>"><s:property value="years[8]"/></option>
        		<option value="<s:property value="years[9]"/>"><s:property value="years[9]"/></option>
        		<option value="<s:property value="years[10]"/>"><s:property value="years[10]"/></option>
       	   </select>
          统计月份：<select name="month" id="month">
        		<option value="01">1月</option>
        		<option value="02">2月</option>
        		<option value="03">3月</option>
        		<option value="04">4月</option>
        		<option value="05">5月</option>
        		<option value="06">6月</option>
        		<option value="07">7月</option>
        		<option value="08">8月</option>
        		<option value="09">9月</option>
        		<option value="10">10月</option>
        		<option value="11">11月</option>
        		<option value="12">12月</option>
       	   </select>
       	  <input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="preview()" value="打印" style="font-size:16px;"> 
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="excel()" value="导出" style="font-size:16px;"> 
      <s:select cssClass="formin" id="nature" name="nature" cssStyle=" float: right;"
									list="#{'1':'地市、部门采用情况','2':'处室采用情况通报','3':'上报信息统计','4':'月政务信息统计'}" listKey="key" listValue="value" />
									<span style=" float: right;">过滤：</span>
      </div>
      </span>
      <div class="dcytjm">
       <div id="top" align="center" colspan="16" style="height:30px;font-size: 20pt; font-family: 宋体; font-weight: bold;">
        	  </div>
        	  <table width = "100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <th width="5">序号</th>
    <th width="100">处室</th>
    <th width="90">领导活动及发文</th>
    <th width="150">其他信息</th>
    <th width="100">得分</th>
  </tr>
  <%
  	int i=1;
  %>
  <s:iterator value="list5" var="data">
  <tr id="<s:property value="orgId"/>">
    <td>
    <%=i %>
    </td>
    <td><s:property value="orgName"/></td>
    <td id="<s:property value="orgId"/>lingdao"></td>
    <td id="<s:property value="orgId"/>qita"></td>
    <td id="<s:property value="orgId"/>defen"></td>
  </tr>
  <%
  i++;
  %>
  </s:iterator>
</table>
      </div>
    </div>
    <div style="height:26px; padding:0 2px; font-size:14px; color:#333; font-family: 仿宋_GB2312 ;padding:22px;">
				1.标有“★”符号的为领导在《每日要情》上批示的信息；<br />
				2.标有“☆”符号的为领导对呈阅件批示的信息；<br />
				3.标有“□”符号的为领导对呈阅件批转的信息；<br />
				4.标有“●”符号的为国务院办公厅“政府情况交流”采用的信息；<br />
				5.标有“△”符号的为国务院办公厅约稿并采用为“专报”的信息；<br />
				6.标有“▽”符号的为国务院办公厅约稿并采用为“综合、要情”的信息；<br />
				7.标有“◣”符号的为国务院办公厅约稿采用并被国务院领导批示的信息；<br />
				8.标有“▲”符号的为国务院办公厅采用为“专报”并被国务院领导批示的信息；<br />
				9.标有“▼”符号的为国务院办公厅采用为“综合、要情”并被国务院领导批示的信息；<br />
				10.标有“○”符号的为国务院办公厅采用为“综合、要情”的信息；<br />
				11.标有“◇”符号的为领导在《江西政务》上批示的信息；<br />
				12.标有“◆”符号的为领导在《江西政务》增刊上批示的信息；<br />
				13.标有“要情”的为《每日要情》信息刊物采用信息；<br />
				14.标有“送阅”的为直接领导阅知的信息；<br />
				15.标有“专报”的为呈送给省政府主要领导的专报信息。
	</div>
	<script>
	
	$(function(){
		var mm = "${flag}";
		if (mm!="")
		{
			$("#nature").val(mm);
		}
		$("#month").val("${month}");
		var month = $("#month").val();
		var year = "${year}";
		$("#year ").val(year);
		$("#top").html(year+"年"+month+"月份各处室信息采用情况通报");
		
		$("#nature").change(function(){
			var flag = $("#nature").val();
			if(flag=="1")
			{
				location = "${root}/xxbs/action/statistics!point.action?flag=1";
			}
			else if(flag=="2")
			{
				location = "${root}/xxbs/action/statistics!findOrg.action?flag=2";
			}
			else if(flag=="3")
			{
				location = "${root}/xxbs/action/statistics!findSubmit.action?flag=3";
			}
			else if(flag=="4")
			{
				location = "${root}/xxbs/action/statistics!findCode.action?flag=4";
			}
		});
	});
	
	<s:iterator value="list3" var="data3">
	$("#<s:property value="#data3[0]"/>lingdao").html("<s:property value="#data3[1]"/>");
	</s:iterator>
	
	<s:iterator value="list2" var="data2">
	$("#<s:property value="#data2[0]"/>qita").append("<s:property value="#data2[2]"/>  <s:property value="#data2[3]"/>   ");
	</s:iterator>
	
	<s:iterator value="list6" var="data6">
	var ss = "<s:property value="#data6[1]"/>";
	var flag="";
	if(ss==1){
		flag="呈国办 ";
	}
	else if(ss==2){
		flag="呈阅件 ";
	}
	$("#<s:property value="#data6[0]"/>qita").append(flag+ " <s:property value="#data6[2]"/>   ");
	</s:iterator>
	
	<s:iterator value="list4" var="data4">
	<s:if test="#data4[1]!=0">
	$("#<s:property value="#data4[0]"/>defen").html("<s:property value="#data4[1]"/>");
	</s:if>
	</s:iterator>
	
	$("#img_sousuo").click(function(){
		var year = $("#year option:selected").val();
		var month = $("#month option:selected").val();
		location = "${root}/xxbs/action/statistics!findOrg.action?&year="+year+"&month="+month;
	});
	$("#findOrg").click(function(){
		location = "${root}/xxbs/action/statistics!point.action";
	});
	
	function excel(){
		var year = $("#year").val();
		var month = $("#month").val();
		location = "${root}/xxbs/action/statistics!excel2.action?year="+year+"&month="+month;
	}
	</script>
	</body>
</html>
