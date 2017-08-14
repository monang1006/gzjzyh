<HTML>
	<HEAD>
	<TITLE> 表单域字段设置 </TITLE>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib uri="/struts-tags" prefix="s"%>
	<%@include file="/common/include/rootPath.jsp"%>
	<%@include file="/common/include/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=root %>/workflow/designer/css/style.css">
	<link rel="stylesheet" type="text/css" href="<%=root %>/workflow/designer/js/webTab/webtab.css">
	<script src="<%=root %>/workflow/designer/js/webTab/webTab.js"></script>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script type="text/javascript">
		//文档设置勾选只读时，其他的文档设置复选框设置为不可用
		function  otherDis(index,obj){
				if(obj.checked){//只读选中
					$("input[id^='fieldList'][id$="+index+"]").each(function(i){
						if((this.id).indexOf("ReadOnly")!=-1){//判断是否是只读复选框
						}else{
							$(this).attr("checked",false);
							$(this).attr("disabled","true");
						}
				});
			}else{
				//未勾选只读
				$("input[id^='fieldList'][id$="+index+"]").each(function(i){
						$(this).attr("disabled","");
				});
			}
		}
			function doSubmit(){
				var ret = "[";
				var checkbox_yj = $(".yj_field:checked");
				var yj_fieldName = "";
				var yj_fieldValue = "";
				if(checkbox_yj.size()>0){
					yj_fieldName = checkbox_yj.attr("fieldText");
					yj_fieldValue = checkbox_yj.attr("value");
					ret = ret + "{fieldName:'"+yj_fieldValue+"'#type:'yj_field'}#"; 
				}
				//得到表单域设置
				$(".tr_FormField").each(function(){
					ret += "{fieldName:'"+$(this).attr("fieldName")+"'";
					$(this).find("input:checkbox").each(function(){
						ret += "#";
						if($(this).attr("checked")){
							ret += $(this).attr("name")+":'"+$(this).attr("checkedValue")+"'";
						}else{
							ret += $(this).attr("name")+":'"+$(this).attr("disCheckedValue")+"'";
						}
					});
					ret += "}#";	
				});
				ret = ret.substring(0,ret.length-1);
				ret = ret + "]";
				//alert(ret);
				window.returnValue = encodeURI(ret) + "," + yj_fieldName;
				window.close();
			}
			
			$(document).ready(function(){
				var params = $("#flagId").val();
				if(params != ""){
					params = decodeURI(params);
					var jsonParams = eval('('+params+')');
					$.each(jsonParams,function(i,json){
						if(json.type!=null && json.type == "yj_field"){//意见绑定对象
							validateYJCheckbox(json.fieldName);
						}else{
							//vaildateCheckbox(json.fieldName,json.ReadOnly,json.Visible);
							vaildateCheckbox(json);
						}
					});
				}
			});
			
			//验证意见字段设置
			function validateYJCheckbox(fieldName){
				/*$(".yj_field").each(function(){
					if($(this).attr("name") == fieldName){
						$(this).attr("checked",true);
					}
				});*/
				
				/*$("input:checkbox").filter(".yj_field").each(function(){
					if($(this).attr("name") == fieldName){
						$(this).attr("checked",true);
					}
				});*/
				$("input:checkbox").filter(".yj_field[name='"+fieldName+"']").attr("checked",true);
			}
			
			//验证其他字段设置
			function vaildateCheckbox(json){
				$("input:checkbox").filter(".other").each(function(i){
					if($(this).attr("fieldName") == json.fieldName){
						if($(this).attr("name") == "ReadOnly"){
							if(json.ReadOnly == "True"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						if($(this).attr("name") == "Visible"){
							if(json.Visible == "false"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						if($(this).attr("name") == "Enabled"){
							if(json.Enabled == "True"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						if($(this).attr("name") == "Required"){
							if(json.Required == "Y" || json.Required == "True"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						
						if($(this).attr("name") == "IsAllowAdd"){
							if(json.IsAllowAdd == "False"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						
						if($(this).attr("name") == "IsAllowDelete"){
							if(json.IsAllowDelete == "False"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						
						if($(this).attr("name") == "IsReadOnly"){
							if(json.IsReadOnly == "True"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						
						if($(this).attr("name") == "SetFormTabVisibility"){
							if(json.SetFormTabVisibility == "False"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
						
						//OFFICE 可为多个
						if($(this).attr("name") == "FileNew" && json.FileNew != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FileNew == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "FileSave" && json.FileSave != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FileSave == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "FilePrint" && json.FilePrint != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FilePrint == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "FileOpen" && json.FileOpen != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FileOpen == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "FileClose" && json.FileClose != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FileClose == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "FileSaveAs" && json.FileSaveAs != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.FileSaveAs == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						if($(this).attr("name") == "SetReadOnly" && json.SetReadOnly != null){
							if($(this).attr("fieldName") == json.fieldName){
								if(json.SetReadOnly == "true"){
									$(this).attr("checked",true);
								}else{
									$(this).attr("checked",false);
								}
							}
						}
						//HTML页面设置
						if($(this).attr("name") == "visible"){
							if(json.visible == "none"){
								$(this).attr("checked",true);
							}else{
								$(this).attr("checked",false);
							}
						}
					}
				});
			}
			
			function doCheck(cur){
				if(cur.checked){
					$("input:checkbox").filter(".yj_field").each(function(){
						if(cur.name != $(this).attr("name")){
							$(this).attr("checked",false);
						}
					});
				}
			}

			function doCheckAll(){  
				$("input:checkbox").filter(".other").each(function(){
					if($(this).attr("name") == "ReadOnly"){
						$(this).attr("checked",true);
					}
				});
			}
			
			function doCheckOther(){  
				$("input:checkbox").filter(".other").each(function(){
					if($(this).attr("name") == "ReadOnly"){
						if($(this).attr("checked") == false){
							$(this).attr("checked",true);
						}else $(this).attr("checked",false);
					}
				});
			}
			
			function doCheckNon(){  
				$("input:checkbox").filter(".other").each(function(){
					if($(this).attr("name") == "ReadOnly"){
							$(this).attr("checked",false);
					}
				});
			}
			
			function checkOne(){
		  	     var toChangeMainActor = $("#toChangeMainActor").attr("checked");
			     if (!toChangeMainActor){
			        var permitReassigned=document.getElementById("permitReassigned").value;
			         if(permitReassigned==""){
			           alert("【主办变更】配合【指派功能】使用，请确认【指派功能】已配置。");
			            $("#toChangeMainActor").attr("checked",true);
			           return false;
			         }
			     }
			}
			
	function showTemp(value,object){
		if(value==0){
			if(object.checked){
				var temp = "#"+"Required"+object.id.replace(/[^0-9]/ig,"");
				$(temp).attr("disabled",true);
			}else{
				var temp = "#"+"Required"+object.id.replace(/[^0-9]/ig,"");
				$(temp).attr("disabled",false);
			}
		}else{
			if(object.checked){
				var temp = "#"+"IsAllowAdd"+object.id.replace(/[^0-9]/ig,"");
				$(temp).attr("disabled",true);
			}else{
				var temp = "#"+"IsAllowAdd"+object.id.replace(/[^0-9]/ig,"");
				$(temp).attr("disabled",false);
			}
		}
	}
	</script>
	<style>
		body {
			background-color: buttonface;
			scroll: auto;
			margin: 7px, 0px, 0px, 7px;
			border: none;
			overflow: auto;	
		}
	</style>
	</HEAD>
<BODY scroll="auto">
<s:hidden id="flagId" name="flagId"></s:hidden>
<s:hidden id="permitReassigned" name="permitReassigned"></s:hidden>
<table border="0" cellpadding="0" cellspacing="0" height=385px>
<thead>
  <tr id="WebTab">
	<td class="selectedtab" id="tab1" onmouseout='outTab(this)' onmouseover='hoverTab("tab1")' onclick="switchTab('tab1','contents1');"><span id="tabpage1">意见域设置</span></td>
	<td class="tab" id="tab2" onmouseout='outTab(this)' onmouseover='hoverTab("tab2")' onclick="switchTab('tab2','contents2');"><span id="tabpage2">表单域设置</span></td>
	<td class="tab" id="tab6" onmouseout='outTab(this)' onmouseover='hoverTab("tab6")' onclick="switchTab('tab6','contents6');"><span id="tabpage6">按钮设置</span></td>
	<td class="tab" id="tab5" onmouseout='outTab(this)' onmouseover='hoverTab("tab5")' onclick="switchTab('tab5','contents5');"><span id="tabpage5">标签设置</span></td>
    <td class="tab" id="tab3" onmouseout='outTab(this)' onmouseover='hoverTab("tab3")' onclick="switchTab('tab3','contents3');"><span id="tabpage3">文档设置</span></td>
	<td class="tab" id="tab4" onmouseout='outTab(this)' onmouseover='hoverTab("tab4")' onclick="switchTab('tab4','contents4');"><span id="tabpage3">页面设置</span></td>
	<td class="tab" id="tab7" onmouseout='outTab(this)' onmouseover='hoverTab("tab7")' onclick="switchTab('tab7','contents7');"><span id="tabpage7">表单卡设置</span></td>
	<td class="tab" id="tab8" onmouseout='outTab(this)' onmouseover='hoverTab("tab8")' onclick="switchTab('tab8','contents8');"><span id="tabpage8">附件设置</span></td>
	<td class="tabspacer" width=50>&nbsp;</td>	
  </tr>
</thead>
<tbody>
  <tr>
	<td id="contentscell" colspan="8">
		<div class="selectedcontents" id="contents1">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
						<Fieldset style="border: 1px solid #C0C0C0;">
							<LEGEND align=left style="font-size:9pt;">&nbsp;<span id="tabpage1_1">审批意见域</span>&nbsp;</LEGEND>
							<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
								<div id="spyj">
								<s:iterator value="fieldList" status="index">
									<s:if test="type.equals('Strong.Form.WorkControls.AuditOpinion')&&caption!=null&&caption.length()>0">
										<TR valign=top>
											<TD width="75%"><span id="tabpage1_2">${caption }</span>&nbsp;&nbsp;</TD>
											<TD><input class="yj_field" name="${name }" onclick="doCheck(this);" type="checkbox" fieldText="${caption }" value="${name }"/></TD>
										</TR>
									</s:if>	
								</s:iterator>
								</div>
							</TABLE>
						</Fieldset>
					</TD>
				</TR>
			</TABLE>
		</div>
		<%--<div class="contents" id="contents2">
			<TABLE border=0 width="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
						<Fieldset style="border: 1px solid #C0C0C0;">
							<LEGEND align="left" style="font-size:9pt;">&nbsp;<span id="tabpage2_1">表单域</span>&nbsp;</LEGEND>
							<TABLE border="0" style="font-size:9pt">	
								<s:iterator value="fieldList" status="index">
									<s:if test="caption!=null&&caption.length()>0&&!type.equals('TEFLine')&&!type.equals('TEFLabel')&&!type.equals('TEFButton')&&!type.equals('TEFOffice')">
										<TR valign=top class="tr_FormField" fieldName="${name }">
											<TD width="60%"><span id="tabpage2_2">${caption }</span>&nbsp;&nbsp;</TD>
											<TD><input class="other" type="checkbox" name="ReadOnly" fieldName="${name }" disCheckedValue="false" checkedValue="True"/>只读</TD>
											<TD><input class="other" name="Visible" type="checkbox" fieldName="${name }" disCheckedValue="True" checkedValue="false" value="false"/>不显示</TD>
											<TD><input class="other" name="Required" type="checkbox" fieldName="${name }" disCheckedValue="N" checkedValue="Y" value="Y"/>必填</TD>
										</TR>
									</s:if>	
								</s:iterator>
							</TABLE>
						</Fieldset>
					</TD>
				</TR>
			</TABLE>  
		</div>
		--%><div class="contents" id="contents2">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align="left" style="font-size:9pt;">&nbsp;<span id="tabpage2_1" onclick="doCheckAll();">表</span><span id="tabpage2_1" onclick="doCheckOther();">单</span><span id="tabpage2_1" onclick="doCheckNon();">域</span>&nbsp;</LEGEND>
					<TABLE border="0" width="100%" height="100%" style="font-size:9pt;">
						<s:iterator value="fieldList" status="index">
							<s:if test="caption!=null&&caption.length()>0&&!type.equals('Strong.Form.WorkControls.AuditOpinion')&&!type.equals('Strong.Form.Controls.Line')&&!type.equals('Strong.Form.Controls.Label')&&!type.equals('Strong.Form.Controls.Button')&&!type.equals('Strong.Form.Controls.SingleAccessory')&&!type.equals('Strong.Form.Controls.AccessoryControl')&&!type.equals('Office')&&!type.equals('Blank')&&!type.equals('Iframe')">
								<TR valign=top class="tr_FormField" fieldName="${name }">
									<TD width="60%"><span id="tabpage2_2">${caption }</span>&nbsp;&nbsp;</TD>
									<TD><input class="other" type="checkbox" name="ReadOnly" id="ReadOnly<s:property value='#index.index' />" fieldName="${name }" disCheckedValue="false" checkedValue="True"/><label for="ReadOnly<s:property value='#index.index' />">只读</label></TD>
									<TD><input class="other" name="Visible" type="checkbox" id="Visible<s:property value='#index.index' />" fieldName="${name }" disCheckedValue="True" checkedValue="false" value="false"/><label for="Visible<s:property value='#index.index' />">不显示</label></TD>
									<TD><input class="other" name="Required" type="checkbox" id="Required<s:property value='#index.index' />" fieldName="${name }" disCheckedValue="N" checkedValue="Y" value="Y"/><label for="Required<s:property value='#index.index' />">必填</label></TD>
								</TR>
							</s:if>	
						</s:iterator>
					</TABLE>
					</Fieldset>
					</TD>
				</TR>
			</TABLE>	  
		</div>
		<div class="contents" id="contents5">
		<TABLE border=0 width="100%" height="100%">
			<TR valign=top>
				<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align=left style="font-size:9pt;">&nbsp;<span id="tabpage2_1">标签设置</span>&nbsp;</LEGEND>
					<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
						<s:iterator value="fieldList" status="index">
							<s:if test="type.equals('Strong.Form.Controls.Label')&&caption!=null&&caption.length()>0">
								<TR valign=top class="tr_FormField" fieldName="${name }">
									<TD width="65%"><span id="tabpage1_2">${caption }</span>&nbsp;&nbsp;</TD>
									<TD><input class="other" name="Visible" id="<s:property value='#index.index' />" type="checkbox" fieldName="${name }" disCheckedValue="True" checkedValue="false" value="false"/><label for="<s:property value='#index.index' />">不显示</label></TD>
								</TR>
							</s:if>	
						</s:iterator>
					</TABLE>
					</Fieldset>
				</TD>
			</TR>
		</TABLE>  
		</div>
		<div class="contents" id="contents3">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align=left style="font-size:9pt;">&nbsp;<span id="tabpage2_1">文档设置</span>&nbsp;</LEGEND>
					<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
						<s:iterator value="fieldList" status="index">
							<s:if test="type.equals('Office')">
								<tr>
									<td>&nbsp;&nbsp;${caption}</td>
								</tr>
								<TR valign=top class="tr_FormField" fieldName="${name}">
									<!-- <TD width="5%"><span>${caption }</span>&nbsp;&nbsp;</TD>-->
									<TD><input class="other" type="checkbox" name="FileNew" fieldName="${name}" id="fieldListFileNew${index.index}" disCheckedValue="false" checkedValue="true"/>允许新建</TD>
									<TD><input class="other" type="checkbox" name="FileSave" fieldName="${name}" id="fieldListFileSave${index.index}" disCheckedValue="false" checkedValue="true"/>允许保存</TD>
									<TD><input class="other" type="checkbox" name="FilePrint" fieldName="${name}" id="fieldListFileOpen${index.index}" disCheckedValue="false" checkedValue="true"/>允许打印</TD>
									<TD><input class="other" type="checkbox" name="FileOpen" fieldName="${name}" id="fieldListFileOpen${index.index}" disCheckedValue="false" checkedValue="true"/>允许打开</TD>
									<TD><input class="other" type="checkbox" name="FileClose" fieldName="${name}" id="fieldListFileClose${index.index}" disCheckedValue="false" checkedValue="true"/>允许关闭</TD>
									<TD><input class="other" type="checkbox" name="FileSaveAs" fieldName="${name}" id="fieldListFileSaveAs${index.index}" disCheckedValue="false" checkedValue="true"/>允许另存为</TD>
									<TD><input class="other" type="checkbox" name="SetReadOnly" fieldName="${name}" id="fieldListSetReadOnly${index.index}" disCheckedValue="false" checkedValue="true"  onclick="otherDis('${index.index}',this);"/>只读</TD>
								</TR>
							</s:if>	
						</s:iterator>
					</TABLE>
					</Fieldset>
					</TD>
				</TR>
			</TABLE>	  
		</div>	              
		<div class="contents" id="contents4">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD></TD>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align=left style="font-size:9pt;">&nbsp;<span id="tabpage2_1">按钮</span>&nbsp;</LEGEND>
					<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
						<TR valign=top class="tr_FormField" fieldName="toViewState">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">处理状态</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toViewState" fieldName="toViewState" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none"/><label for="toViewState">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toNext">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">提交下一处理人</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toNext" fieldName="toNext" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none"/><label for="toNext">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toPrev">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">退回上一处理人</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toPrev" fieldName="toPrev" name="visible" type="checkbox" disCheckedValue="block" checked="checked" checkedValue="none" value="none"/><label for="toPrev">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toBack">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">退回</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toBack" fieldName="toBack" name="visible" type="checkbox" disCheckedValue="block" checked="checked" checkedValue="none" value="none"/><label for="toBack">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toSave">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">保存并关闭</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toSave" fieldName="toSave" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none"/><label for="toSave">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toSaveOrToNext">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">保存或提交</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toSaveOrToNext" fieldName="toSaveOrToNext" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none"  checked="checked"/><label for="toSaveOrToNext">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toPrint">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">打印处理单</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toPrint" fieldName="toPrint" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none"/><label for="toPrint">不显示</label></TD>
						</TR>	
						<TR valign=top class="tr_FormField" fieldName="toReturnBack">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">驳回（子流程适用）</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toReturnBack" fieldName="toReturnBack" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none" checked="checked"/><label for="toReturnBack">不显示</label></TD>
						</TR>	
						<% 
						 String flag=(String)request.getAttribute("biaozhi"); //主办权限 是否启用
						  if("1".equals(flag)){
						%>
						<TR valign=top class="tr_FormField" fieldName="toChangeMainActor">
							<TD width=5></TD>
							<TD><span id="tabpage2_7">主办变更（当前处理人需为主办人员且开启指派功能）</span>&nbsp;&nbsp;</TD>
							<TD><input class="other" id="toChangeMainActor" fieldName="toChangeMainActor" name="visible" type="checkbox" disCheckedValue="block" checkedValue="none" value="none" checked="checked" onclick="checkOne();"/><label for="toChangeMainActor">不显示</label></TD>
						</TR>
						<%
						  }
						 %>
						<TR height="3">
							<TD></TD>
							<TD></TD>
							<TD></TD>
						</TR>
					</TABLE>
					</Fieldset>
					</TD>
					<TD>&nbsp;</TD>
				</TR>
				<TR height="100%">
					<TD></TD><TD></TD><TD></TD>
				</TR>
			</TABLE>
		</div>	             
		<div class="contents" id="contents6">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align="left" style="font-size:9pt;">&nbsp;<span id="tabpage2_1">按钮</span>&nbsp;</LEGEND>
					<TABLE border="0" width="100%" height="100%" style="font-size:9pt;">
						<s:iterator value="fieldList" status="index">
							<s:if test="type.equals('Strong.Form.Controls.Button')&&caption!=null&&caption.length()>0">
								<TR valign=top class="tr_FormField" fieldName="${name}">
									<TD><span id="tabpage1_2">${caption}</span>&nbsp;&nbsp;</TD>
									<TD><input class="other" type="checkbox" name="Enabled" fieldName="${name}" disCheckedValue="false" checkedValue="True"/>可用</TD>
									<TD><input class="other" name="Visible" type="checkbox" fieldName="${name}" disCheckedValue="True" checkedValue="false" value="false"/>不显示</TD>
								</TR>
							</s:if>	
						</s:iterator>
					</TABLE>
					</Fieldset>
					</TD>
				</TR>
			</TABLE>	  
		</div>	
		<div class="contents" id="contents7">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align="left" style="font-size:9pt;">&nbsp;<span id="tabpage2_1">表单卡</span>&nbsp;</LEGEND>
					<TABLE border="0" width="100%" height="50%" style="font-size:9pt;">
						<s:iterator value="fieldList" status="index">							
							<s:if test="caption!=null&&caption.length()>0&&(type.equals('Blank')||type.equals('Office')||type.equals('Iframe'))">
								<TR valign=top class="tr_FormField" fieldName="${caption}">
									<TD width="60%"><span id="tabpage2_2">${caption}</span>&nbsp;&nbsp;</TD>
									<TD><input class="other" type="checkbox" name="SetFormTabVisibility" fieldName="${caption}" disCheckedValue="True" checkedValue="False" value="False"/>隐藏</TD>
								</TR>
							</s:if>								
						</s:iterator>
					</TABLE>
					</Fieldset>
					</TD>
				</TR>
			</TABLE>	  
		</div>
		<div class="contents" id="contents8">
			<TABLE border=0 width="100%" height="100%">
				<TR valign=top>
					<TD width="100%" valign=top>
					<Fieldset style="border: 1px solid #C0C0C0;">
					<LEGEND align="left" style="font-size:9pt;">&nbsp;<span id="tabpage2_1">附件</span>&nbsp;</LEGEND>
					<TABLE border="0" width="100%" height="50%" style="font-size:9pt;">
						<s:iterator value="fieldList" status="index">
							<s:if test="caption!=null&&caption.length()>0&&(type.equals('Strong.Form.Controls.SingleAccessory')||type.equals('Strong.Form.Controls.AccessoryControl'))">
								<TR valign=bottom class="tr_FormField" fieldName="${name}">
									<TD width="50%"><span id="tabpage2_2">${caption}</span>&nbsp;&nbsp;</TD>
									<TD><input class="other" type="checkbox" name="IsAllowAdd" id="IsAllowAdd<s:property value='#index.index' />" fieldName="${name}" disCheckedValue="True" checkedValue="False" value="False" onclick="showTemp(0,this);"/><label for="IsAllowAdd<s:property value='#index.index' />">不可添加</label></TD>
									<TD><input class="other" type="checkbox" name="IsAllowDelete" id="IsAllowDelete<s:property value='#index.index' />" fieldName="${name}" disCheckedValue="True" checkedValue="False" value="False"/><label for="IsAllowDelete<s:property value='#index.index' />">不可删除</label></TD>
									<TD><input class="other" type="checkbox" name="IsReadOnly" id="IsReadOnly<s:property value='#index.index' />" fieldName="${name}" disCheckedValue="False" checkedValue="True" value="True"/><label for="IsReadOnly<s:property value='#index.index' />">只读</label></TD>
									<TD><input class="other" type="checkbox" name="Required" id="Required<s:property value='#index.index' />" fieldName="${name}" disCheckedValue="N" checkedValue="Y" value="Y" onclick="showTemp(1,this);"/><label for="Required<s:property value='#index.index' />">必填</label></TD>
								</TR>
							</s:if>															
						</s:iterator>
					</TABLE>
					</Fieldset>
					</TD>
				</TR>
			</TABLE>	  
		</div>
	</td>
  </tr>
</tbody>
</table>
<table cellspacing="1" cellpadding="0" border="0" style="position: absolute; left: 0px;">
	<tr>
		<td width="100%"></td>
		<td><input type=button id="btnOk" class=btn value="确 定" onclick="doSubmit();">&nbsp;&nbsp;&nbsp;</td>
		<td><input type=button id="btnCancel" class=btn value="取 消" onclick="window.close();">&nbsp;&nbsp;&nbsp;</td>
	</tr>
</table>
</BODY>
  <script type="text/javascript">
  $(function(){
	  $("input[id^='fieldListSetReadOnly']").each(function(i){
			if($(this).attr("checked")){
				$(this).attr("checked","");
				$(this).click();
			}
		});
  })
  </script>
</HTML>
