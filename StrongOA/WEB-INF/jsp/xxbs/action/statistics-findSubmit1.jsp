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
         机构类型：<s:select cssClass="formin" id="isOrg" name="orgType"
									list="#{'0':'市政府各有关部门','3':'驻外办','all':'县（市、区）','4':'所有'}" listKey="key" listValue="value" />
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
      
      <script>
		var orgid=0;
		var n=0;
		var m =0;
	  </script>
        	  <table width = "100%" border="0" cellpadding="0" cellspacing="0"  id="table1">
        	  <div id="top" align="center" colspan="16" style="height: 40px;font-size: 20pt; font-family: 宋体; font-weight: bold;">
        	  </div>
  <tr>
    <th style="background-color:#e4e9ee; text-align: right; width: 100px ; height: 51px; line-height:1;" colspan="2" ><img src="<%=themePath%>/images/title1.jpg" /></th>
    <th width="50">1月</th>
    <th  width="50">2月</th>
    <th  width="50">3月</th>
    <th  width="50">4月</th>
    <th width="50">5月</th>
    <th  width="50">6月</th>
    <th  width="50">7月</th>
    <th  width="50">8月</th>
    <th  width="50">9月</th>
    <th  width="50">10月</th>
    <th  width="50">11月</th>
    <th  width="50">12月</th>
    <th style="width: 100px" onclick="getSubmit()"><span style="cursor: pointer;">上报条数</span></th>
    <th style="width: 100px" onclick="getUse()"><span style="cursor: pointer;">采用条数</span></th>
    <!--  <th width="50">排名</th>-->
  </tr>
  <%
  	int i=1;
  %>
  <s:iterator value="list3" var="data">
  <tr>
    <script>
  if(orgid!='<s:property value="#data[3]"/>'){
	 if(orgid!=0) {
		 document.getElementById(orgid).rowSpan = n;
		 m=0;
	 }
    document.write('<td width="80px" id="<s:property value="#data[3]"/>">&nbsp;</td>');
	n=0;
  }
  m++;
  n++;
  orgid = '<s:property value="#data[3]"/>';
  </script> 
  	<td width="80px">
  	<s:property value="#data[1]"/>
  	</td>
    <td id="<s:property value="#data[0]"/>${year }-01SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-02SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-03SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-04SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-05SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-06SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-07SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-08SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-09SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-10SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-11SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>${year }-12SUBMIT" width="40"></td>
    <td id="<s:property value="#data[0]"/>#SUBMIT">
    <s:if test="#data[2]!=0">
    <s:property value="#data[2]"/>
    </s:if>
    </td>
    <td id="<s:property value="#data[0]"/>#USE">
    </td>
    <!--  <td id="paiming<%=i%>">11</td>
    <script>
    $("#paiming<%=i%>").html(m);
    //document.getElementById('paiming<%=i%>').innerHTML(m);
    </script>-->
  </tr>
  <%
  i++;
  %>
  </s:iterator>
</table>
<script>
	if(document.getElementById(orgid)){
  document.getElementById(orgid).rowSpan = n;
	}
 </script>
      </div>
    </div>
	<script>	
	/**
	 * 上报信息统计
	 */
	<s:iterator value="list2" var="data1">
	$("#<s:property value="#data1[0]"/><s:property value="#data1[1]"/>SUBMIT").append('<s:property value="#data1[2]"/>!');
	</s:iterator>
	/**
	 * 采用信息统计
	 */
	<s:iterator value="list5" var="data5">
	$("#<s:property value="#data5[0]"/><s:property value="#data5[1]"/>SUBMIT").append('/<s:property value="#data5[2]"/>');
	</s:iterator>
	/**
	 * 当前年采用数量
	 */
	<s:iterator value="list4" var="data4">
	document.getElementById("<s:property value="#data4[0]"/>#USE").innerHTML = '<s:property value="#data4[1]"/>';
	</s:iterator>
	
	<s:iterator value="list6" var="date6">
	if(document.getElementById('<s:property value="orgSyscode"/>'))
	document.getElementById('<s:property value="orgSyscode"/>').innerHTML = '<s:property value="orgName"/>';
	</s:iterator>
	$("#table1 td").each(function(){
		var text1 = $(this).text();
		if(text1.substring(0,1)=="/"){
			text1 = text1.replace("!","");
			$(this).text("0"+text1);
		}
		else if((text1.indexOf("!")>=0)&&(text1.indexOf("/")<0)){
			text1 = text1.replace("!","");
			var r2 = text1 +"/0";
			$(this).text(r2);
		}
		else{
			text1 = text1.replace("!","");
			$(this).text(text1);
		}
	});
	
	/**
	 * 搜索
	 */
	$("#img_sousuo").click(function(){
		var flag = $("#isOrg option:selected").val();
		var year = $("#year option:selected").val();
		location = "${root}/xxbs/action/statistics!findSubmit.action?orgType="+flag+"&year="+year+"&flag=3";
	});
	
	$("#findOrg").click(function(){
			location = "${root}/xxbs/action/statistics!findOrg.action";
		});
	/**
	 * 导出EXCEL
	 */
	function excel(){
		location = "${root}/xxbs/action/statistics!excelSubmit1.action";
	}
	/**
	 * 根据上报信息统计
	 */
	function getSubmit()
	{
		var flag = $("#isOrg option:selected").val();
		var year = $("#year option:selected").val();
		location = "${root}/xxbs/action/statistics!findSubmit.action?flag=3&orgType="+flag+"&year="+year;
	}
	/**
	 * 根据采用信息统计
	 */
	function getUse()
	{
		var flag = $("#isOrg option:selected").val();
		var year = $("#year option:selected").val();
		location = "${root}/xxbs/action/statistics!findUse.action?flag=3&orgType="+flag+"&year="+year;
	}
	/**
	 * 弹出信息具体得分
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
		
		$.get("<%=root%>/xxbs/action/statistics!orgcode.action?orgId="+orgId+"&year="+m[0]+"&month="+m[1], function(response){
			alert(response);
		});
	}
	/**
	 * 初始化报表表头
	 */
	$(function(){
		var mm = "${flag}";
		if (mm!="")
		{
			$("#nature ").val(mm);
		}
		var year = "${year}";
		$("#year ").val(year);
		if($("#isOrg").val()=="all"){
		$("#top").html(year+"年1-12月全市各县（市、区）在设区市信息上报情况");
		}
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
			
		});
		
	});
	</script>
	</body>
</html>
