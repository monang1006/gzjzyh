<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>采用报送信息</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.raty/jquery.raty.min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
			#nobr br{
				display:none;
			}
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			采用报送信息
		</div>
		<div class="information_out" id="information_out">
			<s:form name="myform" id="myform" action="/xxbs/action/adoption!save.action" theme="simple">
			<input type="hidden" name="pubId" value="${toId}"/>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 信息标题
					</td>
					<td class="contentTd">${publish.pubTitle}</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 刊物期号
					</td>
					<td class="contentTd" id="nobr">
						<s:doubleselect id="issId" name="menu" list="journals" listKey="jourId" listValue="jourName"
						 value="%{publish.TInfoBaseIssue.jourId}" doubleName="issId"
						 doubleList="top.TInfoBaseIssues" doubleListKey="issId" doubleListValue="issNumber" 
						 formName="myform" doubleValue="%{publish.TInfoBaseIssue.issId}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 栏目
					</td>
					<td class="contentTd">
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 采用评分
					</td>
					<td class="contentTd">
						<input class="information_out_input" type="text" digits="采用评分"
						id="pubUseScore" name="pubUseScore" value="${publish.pubUseScore}"/>					
					</td>
				</tr>
				<!--  <tr>
					<td class="labelTd">&nbsp;						
					</td>
					<td id="starCy">										
					</td>
				</tr>-->
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 批示评分
					</td>
					<td class="contentTd">
						<input class="information_out_input" type="text" digits="批示评分"
						id="pubInstructionScore" name="pubInstructionScore" value="${publish.pubInstructionScore}"/>					
					</td>
				</tr>
				<!-- 
				<tr>
					<td class="labelTd">&nbsp;						
					</td>
					<td id="starPs">										
					</td>
				</tr> -->
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 批示人
					</td>
					<td class="contentTd">
						<input class="information_out_input" type="text"
						id="pubInstructor" name="pubInstructor" value="${publish.pubInstructor}"/>					
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 批示内容
					</td>
					<td class="contentTd">
						<textarea class="information_out_input_words" style="width:250px"
							id="pubInstructionContent" name="pubInstructionContent">${publish.pubInstructionContent}</textarea>		
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="采用" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="不采用"  id="saveNoAdopt" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">


$(function(){

	//表单提交操作
	$("#save").click(function(){
		if(validator.form()){
			$("#myform").submit();
		}
	});
	
	$("#saveNoAdopt").click(function(){
		$("#myform").attr("action", "<%=root%>/xxbs/action/handling!noAdopt.action?toId=${toId}");
		$("#myform").submit();
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	
	//表单验证
	//$("#myform").validate({
	//	container:$(document.body)
	//});

	/*
	var hintList = [60,70,80,90,100];
	
	var calcScore = function(score){
		return score * 10 + 50;
	};
	
	var startScore = function(score){
		var sc = 0;
		if(score >= 100){
			sc = 5;
		}
		else if(score >=90){
			sc = 4;
		}
		else if(score >=80){
			sc = 3;
		}
		else if(score >=70){
			sc = 2;
		}
		else if(score >=60){
			sc = 1;
		}
		return sc;
	};
	
	var cyScore = startScore(${model.pubUseScore});
	var psScore = startScore(${model.pubInstructionScore});
	
	$("#starCy").raty({
		path: '<%=scriptPath%>/jquery.raty/img/',
		click: function(score){
			$("#pubUseScore").attr("value", calcScore(score));
		},
		hintList: hintList,
		start: cyScore
	});
	
	$("#starPs").raty({
		path: '<%=scriptPath%>/jquery.raty/img/',
		click: function(score){
			var sc = score * 10 + 50;
			$("#pubInstructionScore").attr("value", sc);
		},
		hintList: hintList,
		start: psScore
	});*/
});

$("#btnSelect").click(function(){
	var ret = showSubDialog("<%=root%>/xxbs/action/user!select.action",600,400);
	if(ret != undefined && ret.status == "success"){
		$("#pubAssigntoid").attr("value", ret.userId);
		$("#assigntoname").attr("value", ret.userName);
	}
});

</script>
