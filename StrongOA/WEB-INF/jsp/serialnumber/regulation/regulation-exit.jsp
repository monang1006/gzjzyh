<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>

		<title>序列生成器</title>

		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		
	
		function numToYear(value){
		  if(value=="1"){
		  return "当年";
		  }else if(value=="2"){
		  return "去年";
		  }else if(value=="3"){
		  return "前年";
		  }
		}
		function numToNum(value){
		   if(value=="1"){
		   return "数字递增";
		   }else if(value=="2"){
		   return "字母递增";
		   }else if(value=="3"){
		   return '数字递增+"号"';
		   }else if(value=="4"){
		   return '字母递增+"号"';
		   }
		}
	


		var cancel=function(){
		    window.history.go(-1);
		}
		
	
		//获取年份
		var getyear=function(value){
		      var myDate = new Date();
		     var ye= myDate.getYear(); 
		  if(value=='1'){
		     var myDate = new Date();
		     return ye;
		  }else if(value=='2'){
		     ye=ye-1;
		     return ye;
		  }else{
		     ye=ye-2;
		     return ye;
		  }
		}
		
		var getNumber=function(value){
		    if(value=='1'){
		       return "1";
		    }else if(value=='2'){
		       return "ABTR";
		    }else if(value=='3'){
		    return "1号";
		    }else if(value=='4'){
		    return "ABTR号";
		    }
		}
		//测试
		var ceshi=function(){
		    
		}
		var getNum=function(value){
		     if(value=='当年'||value=='数字递增'){
		        return '1';
		     }else if(value=='去年'||value=='字母递增'){
		         return '2';
		     }else if(value=='前年'||value=='数字递增+"号"'){
		         return '3';
		         
		     }else if(value=='字母递增+"号"'){
		     return "4"
		     }
		}
	    var sortSelectChange=function(){
		     var id=$("#sortSelect").val();
		     $.post('<%=path%>/serialnumber/sort/sort!select.action',
		             { 'sortId':id},
		              function(data){
		              	$("#abbSelect").html(data); 
		       });
		}
		//添加
		var add=function(){
		    document.getElementById("div3").style.display="block";
		}
		var selectType=function(){
		     var type=$("#ziduanType").val();
		     
		     if(type=="1"){
		        $("#selecttype").empty();
		        $("#selecttype").html("<select id='sortSelect' onchange='sortSelectChange()'>"+
									"<option>选择文号类型</option>"+
									"<s:iterator id='vo' value='sortList'><option value='${vo.sortId }'>${vo.sortName }</option></s:iterator>"+
									"</select>——>>"+
									"<select id='abbSelect'	style='width: 80px;' class='search'>"+
									"<option>请选择代字</option></select>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='确  定' onclick='queding()' >");
		     }else if(type=="2"){
		        $("#selecttype").empty();
		        $("#selecttype").html("<input type='text' id='abbSelect' />&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='确  定' onclick='queding()' >");
		     }else if(type=="3"){
		        $("#selecttype").empty();
		        $("#selecttype").html("<select id='abbSelect' style='width: 110px;' style='width:80px;' class='search'>"+
									"<option value='当年'>当年</option>"+
									"<option value='去年'>去年</option>"+
									"<option value='前年'>前年</option></select>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='确  定' onclick='queding()' >");
		     }else{
		       $("#selecttype").empty();
		       $("#selecttype").html("<select id='abbSelect' style='width: 110px;'>"+
									"<option value='数字递增'>数字递增</option>"+
									"<option value='数字递增+\"号\"'>数字递增+\"号\"</option>"+
									"<option value='字母递增'>字母递增</option>"+
									"<option value='字母递增+\"号\"'>字母递增+\"号\"</option></select>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='保  存' onclick='queding()' >");
   }
		}
		var queding=function(){
		   var type=  $("#ziduanType").val();
		   var value=$("#abbSelect").val();
		   var jieguo=type+"|"+value;
		   if($("#sortSelect").val()!=null||$("#sortSelect").val()!=''){
		     jieguo=jieguo+"|"+$("#sortSelect").val();
		   }
		   $("#regulationValue").append("<option value='"+jieguo+"'>"+value+"</option>");
		    document.getElementById("div3").style.display="none";
		}
		</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin">
		<form id="regulationForm" theme="simple"
			action="<%=path%>/serialnumber/regulation/regulation!save.action"
			method="post">
			<input type="hidden" id="regulationId" name="regulationId" value="${model.regulationId }">
			<div id="contentborder">
				<div id="step1" style="padding: 15px 15px 15px 15px; font-size: 12px;width:600">
					<fieldset>
						<legend>
							序列生成规则
						</legend>
						<div id="step2"
							style="padding: 15px 15px 15px 15px; font-size: 12px;">
							<div>
								<span>规则名称：</span>
								<input type="text" id="regulationName" maxlength="50"
									value="${model. regulationName}" name="model.regulationName">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								例如:某某单位序列生成规则
							</div>
						</div>
						<div style="padding: 15px 15px 15px 15px; text-align: left;">
							<div><table><tr><td>
								<select size="7" id="regulationValue" style="width:200">
								
								</select>
								</td><td>
							&nbsp;&nbsp;&nbsp;
								<input type="button" value="添  加" onclick="add()"><br><br>&nbsp;&nbsp;&nbsp;
								<input type="button" value="修  改" onclick="exit()"><br><br>&nbsp;&nbsp;&nbsp;
								<input type="button" value="删  除">
							</td></tr></table>
                            <div id="div3" style="padding: 15px 15px 15px 15px; display: none;">
								<table>
									<tr>
										<td>
											<s:select list="#{'单位代字':'1','固定字符':'2','年份':'3','自动序号':'4'}" id="ziduanType"
												listKey="value" listValue="key" onchange="selectType()"></s:select>——>>
										</td>
									    <td id="selecttype">
											<select id="sortSelect" onchange="sortSelectChange()">
												<option>
													选择文号类型
												</option>
												<s:iterator id="vo" value="sortList">
													<option value="${vo.sortId }">
														${vo.sortName }
													</option>
												</s:iterator>
											</select>——>>
											<select id="abbSelect"	style="width: 80px;" class="search">
													<option>
														请选择代字
													</option>
											</select>&nbsp;&nbsp;&nbsp;&nbsp;
											<input type='button' value='确  定' onclick='queding()' >
										</td>
									</tr>
								</table>
							</div>
                            <div style="float: right; padding: 15px 15px 15px 15px;">
								<input type="button" value="测  试" onclick="ceshi()">
								<label id="label"></label>
							</div>
						</div><br><br>
						<div style="width: 100%;" align="center">
	                        <span>
	                        	<input type="button" id="finish" value="保  存" onclick="onSub()" class="input_bg">
							</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>
								<input type="button" value="返  回" class="input_bg" onclick="cancel();">
							</span>
						</div>
					</fieldset>
				</div>
        	</div>
		</form>
	</body>
</html>
