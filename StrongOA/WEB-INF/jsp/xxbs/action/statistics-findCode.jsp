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
		 .dcytjm table{ border-collapse:collapse; border-spacing:0;border:2px solid #b7b7b7;}
		 .dcytjm th,.dcytjm td{ height:26px; padding:0 2px; border:1px solid #b7b7b7; font-size:16px; color:#333;font-family: 黑体 ;}
		 .dcytjm td{ color:#333; text-align:center; font-size:16px; font-family: 仿宋_GB2312;}
		 .dcytjm th{ background-color:#f5f7f8; }
		 .dmenutop{ padding:8px 22px; background:#e5eef7; border-bottom:1px solid #b7b7b7; }
        </style>
        <style type="text/css" media=print>
.noprint{display : none }
</style>
	</head>
	
	<body>
	
	
	
    <div class="main_up_out" >
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
        机构类型：<s:select cssClass="formin" id="isOrg" name="orgType"
									list="#{'0':'市政府各有关部门','3':'驻外办','2':'县（市、区）'}" listKey="key" listValue="value" />
		   <input type="button" value="搜索"  style="width: 50px;" id="img_sousuo" >
		&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="preview()" value="打印" style="font-size:16px;"> 
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="excel()" value="导出" style="font-size:16px;"> 
		<s:select cssClass="formin" id="nature" name="nature" cssStyle=" float: right;"
									list="#{'1':'地市、部门采用情况','2':'处室采用情况通报','3':'上报信息统计','4':'月政务信息统计'}" listKey="key" listValue="value" />
									<span style=" float: right;">过滤：</span>
      </div>
      </span>
      <div class="dcytjm">
       <div id="top" align="center" colspan="16" style="height: 40px;font-size: 20pt; font-family: 宋体; font-weight: bold;">
        	  </div>
        	  <table width = "100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <th rowspan="2">单位</th>
    <th colspan="3">市级</th>
    
    <th colspan="3">省级</th>
    
    <th colspan="2">国办</th>
    <th rowspan="2">本月加分</th>
    <th rowspan="2">本月得分</th>
    <th rowspan="2">上月累计</th>
    <th rowspan="2">累计得分</th>
    <th rowspan="2">排名情况</th>
  </tr>
  <tr>
    <th>每日要情</th>
    <th>南昌政务</th>
    <th>领导批示</th>
    <th>每日要情</th>
    <th>江西政务</th>
    <th>领导批示</th>
    <th>信息</th>
    <th>领导批示</th>
  </tr>
  <%
  	int i=1;
  %>
  <s:iterator value="list13" var="data">
  <tr>
    <td>
    <s:property value="#data[1]"/>
    </td>
    <td id="<s:property value="#data[0]"/>#YQ"></td>
    <td id="<s:property value="#data[0]"/>#ZW"></td>
    <td id="<s:property value="#data[0]"/>#PS"></td>
    <td id="<s:property value="#data[0]"/>#SJYQ"></td>
    <td id="<s:property value="#data[0]"/>#SJZW"></td>
    <td id="<s:property value="#data[0]"/>#SJPS"></td>
    <td id="<s:property value="#data[0]"/>#GBXX"></td>
    <td id="<s:property value="#data[0]"/>#GBPS"></td>
    <td id="<s:property value="#data[0]"/>#JF"></td>
    <td id="<s:property value="#data[0]"/>#DF"></td>
    <td id="<s:property value="#data[0]"/>#SYLJ"></td>
    <td><s:property value="#data[2]"/></td>
    <td><%=i%></td>
  </tr>
  <%
  i++;
  %>
  </s:iterator>

</table>
      </div>
    </div>
	<script>
	/**
	 * 每日要情
	 */	
	<s:iterator value="list2" var="data2">
	document.getElementById("<s:property value="#data2[0]"/>#YQ").innerHTML = '<s:property value="#data2[1]"/>';
	</s:iterator>
	/**
	 * 南昌政务
	 */
	<s:iterator value="list3" var="data3">
	document.getElementById("<s:property value="#data3[0]"/>#ZW").innerHTML = '<s:property value="#data3[1]"/>';
	</s:iterator>
	/**
	 * 领导批示
	 */
	<s:iterator value="list4" var="data4">
	document.getElementById("<s:property value="#data4[0]"/>#PS").innerHTML = '<s:property value="#data4[1]"/>';
	</s:iterator>
	/**
	 * 省级要情
	 */
	<s:iterator value="list5" var="data5">
	document.getElementById("<s:property value="#data5[0]"/>#SJYQ").innerHTML = '<s:property value="#data5[1]"/>';
	</s:iterator>
	/**
	 * 江西政务
	 */
	<s:iterator value="list6" var="data6">
	document.getElementById("<s:property value="#data6[0]"/>#SJZW").innerHTML = '<s:property value="#data6[1]"/>';
	</s:iterator>
	/**
	 * 省级领导批示
	 */
	<s:iterator value="list7" var="data7">
	document.getElementById("<s:property value="#data7[0]"/>#SJPS").innerHTML = '<s:property value="#data7[1]"/>';
	</s:iterator>
	/**
	 * 国办信息
	 */
	<s:iterator value="list8" var="data8">
	document.getElementById("<s:property value="#data8[0]"/>#GBXX").innerHTML = '<s:property value="#data8[1]"/>';
	</s:iterator>
	/**
	 * 国办批示
	 */
	<s:iterator value="list9" var="data9">
	document.getElementById("<s:property value="#data9[0]"/>#GBPS").innerHTML = '<s:property value="#data9[1]"/>';
	</s:iterator>
	/**
	 * 本月加分
	 */
	<s:iterator value="list10" var="data10">
	document.getElementById("<s:property value="#data10[0]"/>#JF").innerHTML = '<s:property value="#data10[1]"/>';
	</s:iterator>
	/**
	 * 本月得分
	 */
	<s:iterator value="list11" var="data11">
	document.getElementById("<s:property value="#data11[0]"/>#DF").innerHTML = '<s:property value="#data11[1]"/>';
	</s:iterator>
	/**
	 * 上月累计
	 */
	<s:iterator value="list12" var="data12">
	document.getElementById("<s:property value="#data12[0]"/>#SYLJ").innerHTML = '<s:property value="#data12[1]"/>';
	</s:iterator>
	/**
	 * 搜索
	 */
	$("#img_sousuo").click(function(){
		var flag = $("#isOrg option:selected").val();
		var year = $("#year option:selected").val();
		var month = $("#month option:selected").val();
		location = "${root}/xxbs/action/statistics!findCode.action?orgType="+flag+"&year="+year+"&month="+month+"&flag=4";
	});
	/**
	 * 处室信息统计
	 */
	$("#findOrg").click(function(){
			location = "${root}/xxbs/action/statistics!findOrg.action";
		});
	/**
	 * 上报信息统计
	 */
	$("#findSubmit").click(function(){
		location = "${root}/xxbs/action/statistics!findSubmit.action";
	});
	/**
	 * 导出EXCEL
	 */
	function excel(){
		var orgType = $("#isOrg").val();
		location = "${root}/xxbs/action/statistics!excelCode.action?orgType="+orgType;
	}
	/**
	 * 点击分数，弹出具体得分
	 */
	function findcode(orgId,time){
		var m = new Array();
		if(time.length>4){
		m= time.split("-");
		}
		else{
			m[0]=time;
			m[1]="";
		}
		var orgType = "${orgType}";
		if(orgType==1){
			$.get("<%=root%>/xxbs/action/statistics!orgcode1.action?orgId="+orgId+"&year="+m[0]+"&month="+m[1], function(response){
				alert(response);
			});
		}
		else{
			$.get("<%=root%>/xxbs/action/statistics!orgcode.action?orgId="+orgId+"&year="+m[0]+"&month="+m[1], function(response){
				alert(response);
			});
		}
		
	}
	/**
	 * 初始化报表表头
	 */
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
		$("#top").html(year+"年"+month+"月份政务信息采用、得分和排名情况");
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
	
	</script>
	</body>
</html>
