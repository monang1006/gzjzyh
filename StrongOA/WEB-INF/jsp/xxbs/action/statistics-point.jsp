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
        机构类型：<s:select cssClass="formin" id="isOrg" name="orgType"
									list="#{'0':'市政府各有关部门','3':'驻外办','all':'县（市、区）'}" listKey="key" listValue="value" />
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
    <th style="background-color:#e4e9ee; text-align: right; width: 250px ; height: 51px; line-height:1;" ><img src="<%=themePath%>/images/title.jpg" /></th>
     <th>1月</th>
    <th>2月</th>
    <th>3月</th>
    <th>4月</th>
    <th>5月</th>
    <th>6月</th>
    <th>7月</th>
    <th>8月</th>
    <th>9月</th>
    <th>10月</th>
    <th>11月</th>
    <th>12月</th>
    <th>得分</th>
    <th>排名</th>
  </tr>
  <%
  	int i=1;
  %>
  <s:iterator value="list3" var="data">
  <tr>
    <td>
    <s:property value="#data[1]"/>
    </td>
    <td><a id="<s:property value="#data[0]"/>#${year }-01" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-01')" ></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-02" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-02')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-03" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-03')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-04" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-04')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-05" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-05')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-06" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-06')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-07" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-07')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-08" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-08')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-09" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-09')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-10" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-10')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-11" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-11')"></a></td>
    <td><a id="<s:property value="#data[0]"/>#${year }-12" href="javascript:findcode('<s:property value="#data[0]"/>','${year }-12')"></a></td>
    <td >
    <s:if test="#data[2]!=0">
    <a href="#" onclick="findcode('<s:property value="#data[0]"/>','${year }')"><s:property value="#data[2]"/></a>
    </s:if>
    </td>
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
	<s:iterator value="list2" var="data1">
	document.getElementById("<s:property value="#data1[0]"/>#<s:property value="#data1[1]"/>").innerHTML = '<s:property value="#data1[2]"/>';
	</s:iterator>
	
	
	$("#img_sousuo").click(function(){
		var flag = $("#isOrg option:selected").val();
		var year = $("#year option:selected").val();
		location = "${root}/xxbs/action/statistics!point.action?orgType="+flag+"&year="+year;
	});
	$("#findOrg").click(function(){
			location = "${root}/xxbs/action/statistics!findOrg.action";
		});
	
	$("#findSubmit").click(function(){
		location = "${root}/xxbs/action/statistics!findSubmit.action";
	});
	
	function excel(){
		var orgType = $("#isOrg").val();
		location = "${root}/xxbs/action/statistics!excel.action?orgType="+orgType;
	}
	
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
	$(function(){
		var mm = "${flag}";
		if (mm!="")
		{
			$("#select_id ").val(mm);
		}
		var year = "${year}";
		$("#year ").val(year);
		if($("#isOrg").val()==0){
		$("#top").html(year+"年市政府部门及相关单位信息采用情况");
		}
		if($("#isOrg").val()==1){
			$("#top").html(year+"年全市各设区市信息采用情况");
			}
		if($("#isOrg").val()==2){
			$("#top").html(year+"年1-12月全市县（市、区）信息采用情况");
			}
		if($("#isOrg").val()==3){
			$("#top").html(year+"年市各驻外办信息采用情况");
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
			else if(flag=="4")
			{
				location = "${root}/xxbs/action/statistics!findCode.action?flag=4";
			}
			
		});
	});
	
	</script>
	</body>
</html>
