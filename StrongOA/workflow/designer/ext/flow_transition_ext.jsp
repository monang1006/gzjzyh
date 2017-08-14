<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.oa.dict.dictItem.DictItemManager"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageDictitem"/>
<%@include file="/common/include/rootPath.jsp"%>
<%
	DictItemManager itemManager = (DictItemManager)ServiceLocator.getService("dictItemManager");
	String code = "40288f2931842af601318451d879002b";
	List<ToaSysmanageDictitem> items = itemManager.getAllDictItems(code);
%>
<!-- 新增tab页对应内容区 -->
<div id="tabs-8" height="100%" width="100%">
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>属性设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_2>迁移线优先级</span>&nbsp;&nbsp;
														<INPUT TYPE="text" onkeypress="KeyPress(this)" NAME="plugins_transitionpri" id="plugins_transitionpri" value=""
															class=txtput>
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_2>迁移线所属分组</span>&nbsp;&nbsp;
														<select id="plugins_transitiongroup" name="plugins_transitiongroup">
															<option value="">&lt;选择分组&gt;</option>
															<%
																if(items != null && !items.isEmpty()){
																	for(ToaSysmanageDictitem item : items){
																		out.println("<option value="+item.getDictItemCode()+">");
																		out.println(item.getDictItemName());
																		out.println("</option>");
																	}
																}
															%>
														</select>
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top style="display: none">
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_2>协办处室</span>&nbsp;&nbsp;
														<input type="checkbox" name="chkdept" id="chkdept" onclick="doCheckDept(this)" value="0">
														<input type="hidden" name="plugins_dept" id="plugins_dept" value="0"/>	
													</TD>
													<TD></TD>
												</TR>
												
												<TR height="3">
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>处理人&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;
														<INPUT TYPE="hidden" NAME="plugins_handleactor" class=txtput
															disabled>
														<select name="realActors" id="realActors" size="3"
															style="width:200px"></select>
														&nbsp;&nbsp;&nbsp;
														<input type="button" value="选择"
															onclick="setTaskActors('task')">
													</TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
										
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>扩展属性</span>&nbsp;
											</LEGEND>
												<TABLE width="100%" height="100%" style="font-size:10pt;">
													<TR valign=top>
														<TD width=5></TD>
														<TD>
															<table>
																<tr>
																	<td><span id=tabpage2_2>迁移线类型</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
																	<td><label for="defaultType">默认</label></td>
																	<td><input type="radio" name="tranlineType" id="defaultType" onclick="doTranlineType(this)" value="defaultType"></td>
																	<td><label for="checkboxType">复选</label></td>
																	<td><input type="radio" name="tranlineType" id="checkboxType" onclick="doTranlineType(this)" value="checkboxType"></td>
																	<td><label for="radioType">单选</label></td>
																	<td><input type="radio" name="tranlineType" id="radioType" onclick="doTranlineType(this)" value="radioType"></td>
																	<td><input type="hidden" name="plugins_tranlineType" id="plugins_tranlineType" value="defaultType"/></td>
																</tr>
															</table>
														</TD>
														<TD></TD>
													</TR>
													<TR>
													<TR valign=top>
														<TD width=5></TD>
														<TD>
															<table>
																<tr>
																	<td><span id=tabpage2_2>人员选择模式</span>&nbsp;&nbsp;</td>
																	<td><label for="treeType">树型</label></td>
																	<td><input type="radio" name="selectPersonType" id="treeType" onclick="doSelectPersonType(this)" value="treeType"></td>
																	<td><label for="buttonType">按钮型</label></td>
																	<td><input type="radio" name="selectPersonType" id="buttonType" onclick="doSelectPersonType(this)" value="buttonType"></td>
																	<td><input type="hidden" name="plugins_selectPersonType" id="plugins_selectPersonType" value="treeType"/></td>
																</tr>
															</table>
														</TD>
														<TD></TD>
													</TR>
													<TR>
														<TD width=5></TD>
														<TD>
															<table>
																<tr>
																	<td align="right"><span id=tabpage2_2>办理必填验证</span></td>
																	<td colspan="6" align="left">
																	<input type="text" size="50" name="plugins_tranlineValidate" id="plugins_tranlineValidate" value="" width="100%" disabled="disabled"/>
																	<input type="checkbox" name="chkTranlineValidate" id="chkTranlineValidatet" onclick="doCheckTranlineValidate(this)" value="0">
																	</td>
																</tr>
																<tr>
																	<td colspan="7">(<span style="color: blue;">办理必填验证</span>,填写html元素的id，存在多个时用英文字符"，"隔开。<%--，<span style="color: red;">省办公厅需求</span>--%>)</td>
																</tr>
															</table>
														</TD>
														<TD></TD>
													</TR>
												</TABLE>
									</Fieldset>

</div>
<script>
/**
 * 动态添加业务扩展Tab页
 */
$(function() {
	$("#tabs").tabs('add', '#tabs-8', '扩展属性');
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

		<script type="text/javascript">
			function KeyPress(objTR){
        		//只允许录入数据字符 0-9 和小数点 
           		//var objTR = element.document.activeElement; 
           		var txtval=objTR.value; 
           		var key = event.keyCode;
           		if((key < 48||key > 57)){ //&&key != 46
            		event.keyCode = 0;
           		} else {
            		/*if(key == 46){
             			if(txtval.indexOf(".") != -1||txtval.length == 0)
              				event.keyCode = 0;
            		}*/
           		}
        	}
        	
        	function doCheckSign(chkObj){
				if(chkObj.checked){
					document.getElementsByName("plugins_submitformdataset")[0].value = "1";
				}else{
					document.getElementsByName("plugins_submitformdataset")[0].value = "0";
				}
			}
			function setSelectedData(selectedData){
   				var returnValue = selectedData.join("|");

				if(selectedSection == "task"){
					document.getElementsByName("plugins_handleactor")[0].value = returnValue;
					tempOwner = returnValue;
					selectObj = document.getElementById("realActors");
				}else if(selectedSection == "reassign"){
					document.getElementsByName("reassignActor")[0].value = returnValue;
					tempDesigner = returnValue;
					selectObj = document.getElementById("reassignSelect");
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
					}//end if
				}//end for
			}
			// 指定任务处理人
			function setTaskActors(flag) {
				var opener = window.dialogArguments;
				selectedSection = flag;
				var dispatch = "ag";
				if(flag == "task"){
					dispatch = "ag";
				}else if(flag == "reassign"){
					dispatch = "ra";
				}
				var pfId = opener.document.getElementById("pfId").value;
				var vPageLink = scriptroot
						+ "/workflowDesign/action/userSelect!input.action?dispatch=" + dispatch + "&pfId=" + pfId;
				// var returnValue = OpenWindow(vPageLink,400,450,window);
				var returnValue = window
					.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:720px;dialogHeight:700px;status:no;help:no;scroll:auto;status:0;help:0;");
			}
			//当userSelect-input.jsp启动时，调用函数
			function getInitData(){
   				var returnValue;
   				if(selectedSection == "task"){
					returnValue = document.getElementsByName("plugins_handleactor")[0].value;
				}else if(selectedSection == "reassign"){
					returnValue = document.getElementsByName("reassignActor")[0].value;
				}
				return (returnValue == null || returnValue == "") ? [] : returnValue.split("|");
			}
		</script>
<script type="text/javascript">
<!--
	/**
	 * 处理节点插件信息方法，用户需重载此方法，主要处理插件信息由展现信息到具体后台值
	 */
	function handlePlugins(){
		var value = document.getElementById("plugins_transitionpri").value;
		if(isNaN(value)){
			alert("请输入合法的数字。");
			return false;
		}
		return true;
	}
	
	/**
	 * 用户需要在页面重载的方法，初始化节点插件信息，主要是一些插件属性的展现方式
	 */
	function initPlugins(){
		var value = document.getElementById("plugins_transitiongroup").value;
		if(value != ""){
			$("#plugins_transitiongroup").find("option[value='"+value+"']").attr("selected","selected");
		}
		
		var pluginDept = document.getElementById("plugins_dept").value;
		if(pluginDept == "1"){
			document.getElementById("chkdept").checked = true;
		}
		var pluginsTranlineValidate = document.getElementById("plugins_tranlineValidate").value;
		if(pluginsTranlineValidate && pluginsTranlineValidate != ""){
			document.getElementById("chkTranlineValidate").checked = true;
		}
		var pluginsTranlineType = document.getElementById("plugins_tranlineType").value;
		if(pluginsTranlineType=="checkboxType"){
			document.getElementById("checkboxType").checked = true;
		}else if(pluginsTranlineType=="radioType"){
			document.getElementById("radioType").checked = true;
		}else{
			document.getElementById("defaultType").checked = true;
		}
		var pluginsSelectPersonType = document.getElementById("plugins_selectPersonType").value;
		if(pluginsSelectPersonType=="buttonType"){
			document.getElementById("buttonType").checked = true;
		}else{
			document.getElementById("treeType").checked = true;
		}
		
		
		//初始化领导批示意见验证信息
		//var plugins_tranlineValidate = document.getElementById("plugins_tranlineType").value;
		
		var pluginActor=document.getElementById("plugins_handleactor").value;
		var returnValue = pluginActor.split("|");
		selectObj = document.getElementById("realActors");
		for(var i = 0; i < returnValue.length; i++){
			var name = returnValue[i].split(",")[1];
			var op = new Option(name, name);
			selectObj.options.add(op, selectObj.options.length);
		}//end if
	}

	function doCheckDept(chkObj){
		if(chkObj.checked){
			document.getElementById("plugins_dept").value = "1";
		}else{
			document.getElementById("plugins_dept").value = "0";
		}
	}
	function doCheckTranlineValidate(chkObj){
		if(chkObj.checked){
			document.getElementById("plugins_tranlineValidate").value = "doneSuggestion,attachName";
			document.getElementById("plugins_tranlineValidate").disabled="";
		}else{
			document.getElementById("plugins_tranlineValidate").value = "";
			document.getElementById("plugins_tranlineValidate").disabled="disabled";
		}
	}
	
	function doTranlineType(Obj){
		document.getElementById("plugins_tranlineType").value = Obj.value;
	}
	function doSelectPersonType(Obj){
		document.getElementById("plugins_selectPersonType").value = Obj.value;
	}
//-->
</script>