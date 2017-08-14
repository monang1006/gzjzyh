<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>
		<s:if test="flag=='guoban'"><s:if test="%{op==\"add\"}">新建呈国办</s:if><s:else>修改呈国办</s:else></s:if>
		<s:elseif test="flag=='shengji'"><s:if test="%{op==\"add\"}">新建省级加分</s:if><s:else>修改省级加分</s:else></s:elseif>
		<s:else><s:if test="%{op==\"add\"}">新建呈阅件</s:if><s:else>修改呈阅件</s:else></s:else>
		</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			请填写信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/piece!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="pieceId" value="${model.pieceId}"/>
			</s:if>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<s:if test="flag=='guoban'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈国办标题：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceTitle"
							name="pieceTitle" type="text" style="width: 400px"
							value="${model.pieceTitle}" />
					</td>
					</s:if>
					<s:elseif test="flag=='shengji'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 省级标题：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceTitle"
							name="pieceTitle" type="text" style="width: 400px"
							value="${model.pieceTitle}" />
					</td>
					</s:elseif>
					<s:else>
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈阅件标题：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceTitle"
							name="pieceTitle" type="text" style="width: 400px"
							value="${model.pieceTitle}" />
					</td>
					</s:else>
				</tr>
				<tr>
					<s:if test="flag=='guoban'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈国办类型：
					</td>
						<s:if test="nature=='true'">
						<td class="contentTd">
							<s:select cssClass="formin" id="pieceOpen" name="pieceOpen" value="%{model.pieceOpen}"
										list="#{\"0\":'报国办',\"1\":'报国办被采用',\"2\":'报国办被采用并批示',\"3\":'国办约稿',\"4\":'国办约稿被采用',\"5\":'国办约稿被采用并批示',\"6\":'核国办稿',\"7\":'核国办稿被采用',\"8\":'核国办稿被采用并批示',\"9\":'核国办稿约稿',\"10\":'核国办稿约稿被采用',\"11\":'核国办稿约稿被采用并批示'}"
										 listKey="key" listValue="value" />
						</td>
						</s:if>
					
						<s:else>
						<td class="contentTd">
						    <s:select cssClass="formin" id="pieceOpen" name="pieceOpen" value="%{model.pieceOpen}"
										list="#{\"0\":'报国办',\"12\":'国办综合采用',\"13\":'国办要情采用',\"14\":'国办专报采用',\"15\":'国办综合采用并批示',\"16\":'国办要情采用并批示',\"17\":'国办专报采用并批示',\"3\":'国办约稿',\"18\":'国办约稿综合采用',\"19\":'国办约稿要情采用',\"20\":'国办约稿专报采用',\"21\":'国办约稿综合采用并批示',\"22\":'国办约稿要情采用并批示',\"23\":'国办约稿专报采用并批示'}"
										 listKey="key" listValue="value" />
						</td>	
						</s:else>
					</s:if>
					<s:elseif test="flag=='shengji'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 省级类型：
					</td>
					<td class="contentTd">
						<s:select cssClass="formin" id="pieceOpen" name="pieceOpen" value="%{model.pieceOpen}"
									list="#{\"0\":'每日要情',\"1\":'江西政务'}"
									 listKey="key" listValue="value" />
					</td>
					</s:elseif>
					<s:else>
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈阅件类型：
					</td>
					<td class="contentTd">
					<s:select cssClass="formin" id="pieceOpen" name="pieceOpen" value="%{model.pieceOpen}"
									list="#{\"0\":'呈阅',\"1\":'呈批',\"2\":'批转',\"3\":'送阅',\"4\":'专报',\"5\":'专报批示'}"
									 listKey="key" listValue="value" />
					</td>
					</s:else>
				</tr>
				<s:if test="flag=='guoban'">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 是否批示：
					</td>
					<td class="contentTd">
						<s:radio name="isInstruction" list="#{\"0\":'否',\"1\":'是'}" value="%{model.isInstruction}"/>
					</td>
				</tr>
				</s:if>
				<s:if test="flag=='shengji'">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 是否批示：
					</td>
					<td class="contentTd">
						<s:radio name="isInstruction" list="#{\"0\":'否',\"1\":'是'}" value="%{model.isInstruction}"/>
					</td>
				</tr>
				</s:if>
				<tr>
					<s:if test="flag=='guoban'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈国办时间：
					</td>
					<td class="contentTd">
					${pieceTime}
						<input name="pieceTime" id="pieceTime" title="请输入呈国办时间" value="${pieceDate}" readonly="readonly">
						<web:datetime format="yyyy-MM-dd" readOnly="true" id="pieceTime" />
					</td>
					</s:if>
					<s:elseif test="flag=='shengji'">
					<td class="labelTd">
						<font color="#FF0000">*</font> 上报时间：
					</td>
					<td class="contentTd">
					${pieceTime}
						<input name="pieceTime" id="pieceTime" title="请输入上报省级时间" value="${pieceDate}" readonly="readonly">
						<web:datetime format="yyyy-MM-dd" readOnly="true" id="pieceTime" />
					</td>
					</s:elseif>
					<s:else>
					<td class="labelTd">
						<font color="#FF0000">*</font> 呈阅件时间：
					</td>
					<td class="contentTd">
					${pieceTime}
						<input name="pieceTime" id="pieceTime" title="请输入呈阅件时间" value="${pieceDate}" readonly="readonly">
						<web:datetime format="yyyy-MM-dd" readOnly="true" id="pieceTime" />
					</td>
					</s:else>
					
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 得分：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceCode" 
							name="pieceCode" type="text"
							value="${model.pieceCode}" required="true" digits="true" digitsMsg="getText(errors_digits, ['得分'])"
							requiredMsg="getText(errors_required, ['得分'])"/>
					</td>
				</tr>
			</table>
			<s:if test="flag=='guoban'">
			<input type="hidden" name="pieceFlags" value="1" name="pieceFlags"/>
			</s:if>
			<s:elseif test="flag=='shengji'">
			<input type="hidden" name="pieceFlags" value="3" name="pieceFlags"/>
			</s:elseif>
			<s:else>
			<input type="hidden" name="pieceFlags" value="2" name="pieceFlags"/>
			</s:else>
			<input type="hidden" value="${model.orgId}" name="orgId" id="orgId">
			</s:form>
		</div>
		
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="确定" name="save" id="save" />
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
			var flag= "${flag}";
			var pt = $("#pieceTitle").val().length;
			if($("#pieceTitle").val().trim()==""){
				if(flag=="guoban"){
					alert("呈国办标题不能为空！");
				}
				else if(flag=="shengji"){
					alert("省级标题不能为空！");
				}
				else{
					alert("呈阅件标题不能为空！");
				}
				return false;
			}
			if(pt>100){
				if(flag=="guoban"){
					alert("呈国办标题过长！");
				}
				else if(flag=="shengji"){
					alert("省级标题过长！");
				}
				else{
					alert("呈阅件标题过长！");
				}
				return false;
			}
			var pieceTime = $("#pieceTime").val();
			if(pieceTime==""){
				if(flag=="guoban"){
					alert("呈国办时间不能为空！");
				}
				else if(flag=="shengji"){
					alert("上报时间不能为空！");
				}
				else{
					alert("呈阅件时间不能为空！");
				}
				return false;
			}
			var pieceCode = $("#pieceCode").val();
			if(pieceCode.length>2){
				alert("得分过大！");
				return;
			}
			if(pieceCode==""){
				alert("得分不能为空！");
				return;
			}
			$("#save").attr("disabled","none");
			$("#myform").submit();
		}
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	
	//表单验证
	//$("#myform").validate({
	//	container:$(document.body)
	//});
});

</script>
