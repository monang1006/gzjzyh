<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaRule" />
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!-- 新增tab页对应内容区 -->
<div id="tabs-8" height="100%" width="100%">

							<TABLE border=0 width="100%" height="100%">
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span>意见排序方式设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD valign="middle">
														<span>意见排序方式</span>
													</TD>
													<TD>
														<select id="plugins_suggestion" name="plugins_suggestion">
															<option value="date_desc">
																按日期降序
															</option>
															<option value="date_asc">
																按日期升序
															</option>
															<option value="personnumber_asc" selected="selected">
																按人员排序号升序
															</option>
															<option value="personnumber_desc">
																按人员排序号降序
															</option>
															<option value="taskstart_asc">
																按任务开始顺序升序
															</option>
															<option value="taskstart_desc">
																按任务开始顺序降序
															</option>
															<option
																value="departmentnumber_asc_and_personnumber_desc">
																按部门升序，人员排序号降序
															</option>
															<option value="departmentnumber_asc_and_personnumber_asc">
																按部门升序，人员排序号升序
															</option>
															<option value="departmentnumber_asc_and_date_desc">
																按部门升序，日期降序
															</option>
															<option value="departmentnumber_asc_and_date_asc">
																按部门升序，日期升序
															</option>
<%--															<option value="departmentnumber_asc_and_taskstart_asc">--%>
<%--																按部门升序，任务开始顺序升序--%>
<%--															</option>--%>
<%--															<option value="departmentnumber_asc_and_taskstart_desc">--%>
<%--																按部门升序，任务开始顺序降序--%>
<%--															</option>--%>
														</select>
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span>办理意见不可见选择范围</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">

												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<select name="plugins_reassignSelect"
															id="plugins_reassignSelect" size="4" style="width:200px"></select>
														<input type="hidden" name="plugins_reassignActor"
															id="plugins_reassignActor">
														&nbsp;&nbsp;
														<input type="button" value="选择" class="button"
															onclick="setTaskActors('reassign')">
													</TD>
													<TD></TD>
												</TR>

												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top style="display:none">
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span>办理记录显示控制</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">

												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<input type="checkbox"
															onclick="doChkIsShowSimplePDImage(this)"
															name="isShowSimplePDImage" id="isShowSimplePDImage">
														<label for="isShowSimplePDImage" />
															显示流程简图tab页
														</label>
														<input type="hidden" name="plugins_isShowSimplePDImage"
															id="plugins_isShowSimplePDImage" value="0" />
														<input type="checkbox" onclick="doChkIsShowPDImage(this)"
															name="isShowPDImage" id="isShowPDImage">
														<label for="isShowPDImage" />
															不显示流程图tab页
														</label>
														<input type="hidden" name="plugins_isShowPDImage"
															id="plugins_isShowPDImage" value="0" />
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span>编号规则设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<table>
															<tr>
																<td nowrap>
																	选择规则：
																</td>
																<td>
																	<%--<select onchange="onSelectChange()" style="width: 110px;" id="ruletype">
																<s:iterator id="vo" value="list">
																	<option value="${vo.id }">
																		${vo.ruleName }
																	</option>
																</s:iterator>
															</select>
															--%>
																	<%
																		List<ToaRule> list = (List<ToaRule>) request.getAttribute("list");
																		StringBuilder select = new StringBuilder();
																		select
																				.append("<select id=\"ruletype\" name=\"plugins_rulecode\" onchange=\"onSelectChange()\">");
																		select.append("<option>请选择规则</option>");
																		if (list != null && !list.isEmpty()) {
																			for (ToaRule rule : list) {
																				String ruleName = rule.getRuleName();
																				if (ruleName.length() > 10) {
																			ruleName = ruleName.substring(0, 10) + "...";
																				}
																				select.append(
																				"<option title=\"" + rule.getRuleName()
																				+ "\" value=\"").append(rule.getId())
																				.append("\">").append(ruleName).append("</option>");
																			}
																		}
																		select.append("</select>");
																		out.println(select.toString());
																	%>
																	<%--<s:select listKey="id" headerKey="" id="ruletype" name="plugins_rulecode" onchange="onSelectChange()" headerValue="请选择规则" listValue="ruleName" list="list"></s:select>
														--%>
																</td>
															</tr>
															<tr id="exampleblank" style="display:none">
																<td></td>
																<td></td>
															</tr>
															<tr id="exampletr" style="display:none">
																<td nowrap>
																	规则内容：
																</td>
																<td id="ruleExample">
																</td>
																<td>
																	<input type="button" id="preview" class="btn"
																		value="预览文号">
																</td>
															</tr>
															<tr>
																<td></td>
																<td></td>
															</tr>
															<tr id="ecode" style="display: none">
																<td>
																	当前文号：
																</td>
																<td>
																	<span id="result"></span>
																</td>
															</tr>
														</table>
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>
								<TR height="100%">
									<TD></TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TABLE>

</div>
<script>
/**
 * 动态添加业务扩展Tab页
 */
$(function() {
	$("#tabs").tabs('add', '#tabs-8', '扩展属性');
	var pf = $("select[name=processForm]");
	pf.attr("disabled", true);
	var node = "&nbsp;<input type='button' value='选择' class='button' onclick='setForms();'>&nbsp;<a href='#' onclick='JavaScript:formPrew();'>&nbsp;预览</a>";

	pf.after(node);
	//pf.replaceWith("<input type=hidden id=processForm  name=processform ><input type=text id=startForm  style=width:60%; >");
	//pf.append("<input type=text id=startForm  style=width:60%; >");
	//alert($("#processForm").val());
	
	
	//显示已经设置的插件项信息
	<s:iterator value="plugins">
		var pluginName = '<s:property value="definitionPluginName"/>';
		var pluginValue = '<s:property value="definitionPluginValue"/>';
		if(pluginName == 'plugins_suggestion'){//意见插件项
			$("option[value='"+pluginValue+"']").attr("selected","selected");
		}
		if(pluginName == 'plugins_rulecode'){//意见插件项
			$("option[value='"+pluginValue+"']").attr("selected","selected");
		}
		if(pluginName == 'plugins_attachpath'){
			$("#plugins_attachpath").val(pluginValue);
		}
		if(pluginName == 'plugins_reassignSelect'){//办理记录不可见人员的姓名
			$("#plugins_reassignSelect").val(pluginValue);
		}
		if(pluginName == 'plugins_reassignActor'){//办理记录不可见人员的用户id
			$("#plugins_reassignActor").val(pluginValue);
		}
		if(pluginName == 'plugins_isShowSimplePDImage'){//办理记录不可见人员的用户id
			$("#plugins_isShowSimplePDImage").val(pluginValue);
		}
		if(pluginName == 'plugins_isShowPDImage'){//办理记录不可见人员的用户id
			$("#plugins_isShowPDImage").val(pluginValue);
		}
	</s:iterator>
		opener.fSelectedObj.reassignActor = document.getElementsByName("plugins_reassignActor")[0].value;
	// 重新指派
		var reassignSelect = document.getElementById("plugins_reassignSelect");
		if (opener.fSelectedObj.reassignActor != "") {
			var designerDetails;
			var designers = opener.fSelectedObj.reassignActor.split("|");
			for (var i = 0; i < designers.length; i++) {
				designerDetails = designers[i].split(",");
				var objOption = new Option(designerDetails[1],
						designerDetails[1]);
				reassignSelect.options.add(objOption,
						reassignSelect.options.length);
			}
		}
	if($("#plugins_isShowSimplePDImage").val()=="1"){
		document.getElementById("isShowSimplePDImage").checked = true;
	}
	if($("#plugins_isShowPDImage").val()=="1"){
		document.getElementById("isShowPDImage").checked = true;
	}
	
});

/**
 * 自定义验证接口，在保存时对扩展页面中扩展的业务属性进行正确性验证
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
function customValidate(){
	// TODO：扩展属性验证逻辑
	return true;
}

/**
 * 配置页面保存时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onSave(){
	// TODO：配置页面保存时执行的业务逻辑
		//添加保存流程定义插件的代码片段 dengzc 2011年2月9日8:23:36
		// 节点插件信息
	var $plugins = $("[name^='plugins_']");
	var pfId = opener.document.getElementById("pfId").value;
	var workflowName = opener.processName;
	var retJson = "[";
		$.each($plugins, function(index, domElement) {
    	retJson += "{'workflowId':'"+pfId+"','workflowName':'"+workflowName+"'";
    	retJson += ",'pluginName':'"+domElement.name+"','pluginValue':'"+domElement.value+"'";
    	retJson += "},";
    });
    if(retJson != "["){
    	retJson = retJson.substring(0,retJson.length - 1);
    }
    retJson += "]";
    $.ajax({
  		type:"post",
  		dataType:"json",
  		async:false,
  		url:"<%=path%>/workflowDesign/action/processDesign!savePlugin.action",
  		data:{pluginParam:retJson},
  		success:function(retCode){
  			 opener.processType = document.getElementsByName("processType")[0].value;
  	      	  opener.root.getAttributeNode("name").value = document.getElementsByName("processName")[0].value;
  		}
  	});

	return true;
}

/**
 * 配置页面取消时执行的业务接口
 * 1.如果执行正确，则接口返回true
 * 2.如果执行失败，则接口直接alert错误信息，并返回false
 */
function onCancel(){
	// TODO：配置页面取消时执行的业务逻辑
	return true;
}
</script>

<script>
// 指定表单
function setForms() {
	var vPageLink = scriptroot
			+ "/workflowDesign/action/processDesign!getAllForms.action?formId="+opener.processForm.split(",")[1];
	// var returnValue = OpenWindow(vPageLink,190,260,window);
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:280pt;dialogHeight:350pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if($.trim(returnValue)!=""){
		var a = returnValue.split(",");
		var sel = $("select[name=processForm]");		
		sel.attr("disabled", false);
		sel.val(a[1]+","+a[0]);
		sel.attr("disabled", true);
		opener.processForm = a[1]+","+a[0];
	}
}

var opener = window.dialogArguments;
//var tempOwner = "";
//var tempDesigner = "";
//var tempStartor = "";
//add 严建 2012-05-17 10:06  添加数据验证方法
function checkData(){
		//<--当启用流程催办时，流程持续时间不能为 0 分/小时/天
		if(document.getElementsByName("doc_isTimer")[0].checked == true){
			if(document.getElementsByName("doc_timer_data")[0].value=="0"){
				alert("当启用流程催办时，流程持续时间不能为 0 分钟/小时/天");
				$("#tab3").click();
				document.getElementsByName("doc_timer_data")[0].focus();
				return false;
			}
		}
		//-->
		return true;
}
//增加表单预览功能 邓志城 2010年6月8日15:26:46
function formPrew(){
	//var objSelect = document.getElementById("startForm");
	//var formId = objSelect.value.split(",")[1];
	var formId = opener.processForm.split(",")[1];
	if(formId == "0"){
		alert("请选择表单。");
		return ;
	}
	var Url = "<%=root%>/fileNameRedirectAction.action?toPage=eform/eform-prew.jsp&formId="+formId;
	var Width=screen.availWidth-10;
    var Height=screen.availHeight-30;
    var ReturnStr = showModalDialog(Url, 
	                                window, 
	                                "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
	                                "status:no;help:no;scroll:no;");
}

function onSelectChange(){
			var myoption=getSelectOption();
			if(myoption.value==0){
				$("#exampleblank").hide();
				$("#exampletr").hide();
				$("#ruleExample").html("");
				//$("#result").html("");
			}else{
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=root%>/autocode/autoCode!getXmlInfo.action",
					data:"id="+myoption.value,
					success:function(xml){
						createHtml(xml);
						$("#exampleblank").show();
						$("#exampletr").show();
					//	$("#result").show();
					},
					error:function(){
						$("#result").html("");
					}
				});
			}
		}
		
		function createHtml(xml){
	        var xmlDoc=null;
	        //判断浏览器的类型
	        //支持IE浏览器 
	       	var rHtml="";
	        if(!window.DOMParser && window.ActiveXObject){   //window.DOMParser 判断是否是非ie浏览器
	            var xmlDomVersions = ['MSXML.2.DOMDocument.6.0','MSXML.2.DOMDocument.3.0','Microsoft.XMLDOM'];
	            for(var i=0;i<xmlDomVersions.length;i++){
	                try{
	                    xmlDoc = new ActiveXObject(xmlDomVersions[i]);
	                    xmlDoc.async = false;
	                    xmlDoc.loadXML(xml); //loadXML方法载入xml字符串
	                    break;
	                }catch(e){
	                }
	            }
	           	var root = xmlDoc.childNodes[0];
	           	for(var i=0;i<root.childNodes.length;i++){
	           		if(root.childNodes[i].nodeName=="String"){
	           			rHtml = rHtml + root.childNodes[i].getAttribute("Value");
	           		}else if(root.childNodes[i].nodeName=="Sequence"){
	           			rHtml = rHtml + " $序号$ ";
	           		}else if(root.childNodes[i].nodeName=="Variant"){
	           			var type = root.childNodes[i].getAttribute("Type");
	           			if(type=="Data LongYear"){
	           				rHtml = rHtml + " $年份$ ";
	           			}else if(type=="Data ShortYear"){
	           				rHtml = rHtml + " $短年份$";
	           			}else if(type=="Data Month"){
	           				rHtml = rHtml + " $月份$ ";
	           			}else if(type=="Data Day"){
	           				rHtml = rHtml + " $日期$ ";
	           			}else if(type=="Array"){
	           				var myArrays = root.childNodes[i].childNodes;
	           				if(myArrays.length>0){
	           					rHtml = rHtml + "<select id=\"mySel\""+i+" name=\"oasel\" ctype=\"oahave\">";
	           				}
	           				for(var t=0;t<myArrays.length;t++){
	           					rHtml = rHtml + "<option value="+myArrays[t].getAttribute("Id")+">"+myArrays[t].getAttribute("Value")+"</option>";
	           				}
	           				rHtml = rHtml + "</select>";
	           			}
	           		}
	           	}
	           	$("#ruleExample").html(rHtml);
	        }
		}
		
		function getSelectOption(){
			var myoption = document.getElementById("ruletype");
			for(var i=0;i<myoption.length;i++){
				if(myoption[i].selected){
					return myoption[i];
				}
			}
		}
		$(document).ready(function(){
			onSelectChange();
			var list = '${forms}';
			$("#startForm").val(opener.processForm.split(",")[0]); //设置文本框的初始值
			$("#preview").click(function(){
				var ruleId = $("#ruletype").val();
				var sels = $("select[name='oasel']").attr("ctype","oahava");
				if(ruleId==""||ruleId=="null"){
				}else{
					var selIds="";
					if(sels.length==0){

					}else{
						for(var i=0;i<sels.length;i++){
							if(i==0){
								selIds=sels[i].value;
							}else{
								selIds=selIds+","+sels[i].value;
							}
						}					
					}
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!getPreview.action",
						data:"id="+ruleId+"&selIds="+selIds,
						success:function(message){
							$("#ecode").show();
							$("#result").html("<span id=presult>"+message+"</span>");
						}
					});
				}
			});
			$("#recBtn").click(function(){
				var codename = $("#recyclecode").val();
				if(codename==""){
					alert("请您填写想要回收的文号！");
					return ;
				}
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=root%>/autocode/autoCode!createRecCode.action",
					data:"codeName="+codename,
					success:function(msg){
						if(msg=="no"){
							alert("文号："+codename+"在数据库中不存在，不能进行回收操作");
						}else if(msg=="res"){
							alert("文号:"+codename+"已经预留，不需要再进行回收操作");
						}else if(msg=="rec"){
							alert("文号:"+codename+"已经回收，不需要再进行回收操作");
						}else if(msg=="true"){
							alert("文号:"+codename+"回收成功");
							window.parent.document.frames["used"].refreshIframe();
						}else{
							alert("文号:"+codename+"回收失败，未知异常，请与管理员联系");
						}
					}
				});
			});
		});
		
		function saveReservedCode(){
			var ruleId = $("#ruletype").val();
			var sels = $("select[name='oasel']").attr("ctype","oahava");
			var prevCode = $("#presult").html();
			if(ruleId==""||ruleId=="null"){
			}else{
				var selIds="";
				if(sels.length==0){

				}else{
					for(var i=0;i<sels.length;i++){
						if(i==0){
							selIds=sels[i].value;
						}else{
							selIds=selIds+","+sels[i].value;
						}
					}					
				}
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=root%>/autocode/autoCode!createCode.action",
					data:"id="+ruleId+"&selIds="+selIds+"&prevCode="+prevCode+"&type=reserved",
					success:function(message){
						if(message=="error"){
							alert("生成xml信息异常");
						}else if(message=="noeq"){
							alert("预览信息与生成信息不符，请您核对，预留文号失败");
						}else if(message=="true"){
							window.close();
						}else{
							alert("未知异常信息，请您与系统管理员联系");
						}
					}
				});
			}
		}
		
		function createCode(){
			var ruleId = $("#ruletype").val();
			var sels = $("select[name='oasel']").attr("ctype","oahava");
			var prevCode = $("#presult").html();
			if(ruleId==""||ruleId=="null"){
			}else{
				var selIds="";
				if(sels.length==0){

				}else{
					for(var i=0;i<sels.length;i++){
						if(i==0){
							selIds=sels[i].value;
						}else{
							selIds=selIds+","+sels[i].value;
						}
					}					
				}
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=root%>/autocode/autoCode!createCode.action",
					data:"id="+ruleId+"&selIds="+selIds+"&prevCode="+prevCode,
					success:function(message){
						if(message=="error"){
							alert("生成xml信息异常");
						}else if(message=="noeq"){
							alert("预览信息与生成信息不符，请您核对");
						}else if(message=="true"){
							window.returnValue=prevCode;
							window.close();
						}else{
							alert("未知异常信息，请您与系统管理员联系");
						}
					}
				});
			}
		}

	var selectedSection = "task";

	// 指定任务处理人
	function setTaskActors(flag) {
		selectedSection = flag;
		var dispatch = "ag";
		if(flag == "task"){
			dispatch = "ag";
		}else if(flag == "reassign"){
			dispatch = "ra";
		}
		var pfId = opener.document.getElementById("pfId").value;
		var isActiveactor = "1";
		/*
		var isActiveactor = document.getElementsByName("isActiveactor")[0].checked;
		if(isActiveactor){
			isActiveactor = "1";
		} else {
			isActiveactor = "0";
		}
		*/
		var vPageLink = scriptroot
				+ "/workflowDesign/action/userSelect!input.action?prefix=u&dispatch=" + dispatch + "&pfId=" + pfId+"&isActiveactor="+isActiveactor;
		// var returnValue = OpenWindow(vPageLink,400,450,window);
		var returnValue = window
				.showModalDialog(
						vPageLink,
						window,
						"dialogWidth:720px;dialogHeight:700px;status:no;help:no;scroll:auto;status:0;help:0;");
	}
	
	function getInitData(){
	   	var returnValue;
	   	if(selectedSection == "task"){
			returnValue = document.getElementsByName("handleactor")[0].value;
		}else if(selectedSection == "reassign"){
			returnValue = document.getElementsByName("plugins_reassignActor")[0].value;
		}
		return (returnValue == null || returnValue == "") ? [] : returnValue.split("|");
	}
	
function setSelectedData(selectedData){
   	var returnValue = selectedData.join("|");

	if(selectedSection == "task"){
		document.getElementsByName("handleactor")[0].value = returnValue;
		tempOwner = returnValue;
		selectObj = document.getElementById("realActors");
	}else if(selectedSection == "reassign"){
		document.getElementsByName("plugins_reassignActor")[0].value = returnValue;
		tempDesigner = returnValue;
		selectObj = document.getElementById("plugins_reassignSelect");
	}
	
	while(selectObj.options.length > 0){
		selectObj.options[0] = null;
	}
	/**
	 * 不选择人的情况 
	 */
	if(returnValue != ""){
		for(var i = 0; i < selectedData.length; i++){
			var name = selectedData[i].split(",")[1];
					var op = new Option(name, name);
					selectObj.options.add(op, selectObj.options.length);
		}
	}
}

function doChkIsShowSimplePDImage(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isShowSimplePDImage")[0].value = "1";
	}else{
		document.getElementsByName("plugins_isShowSimplePDImage")[0].value = "0";
	}
}
function doChkIsShowPDImage(chkObj){
	if(chkObj.checked){
		document.getElementsByName("plugins_isShowPDImage")[0].value = "1";
	}else{
		document.getElementsByName("plugins_isShowPDImage")[0].value = "0";
	}
}
</script>