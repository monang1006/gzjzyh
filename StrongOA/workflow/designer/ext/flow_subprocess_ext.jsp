<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<!-- 新增tab页对应内容区 -->
<div id="tabs-8" height="100%" width="100%">
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id=tabpage2_1>子流程设置</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">

												<TR valign=top>
													<TD></TD>
													<TD>
														<span id=tabpage1_3>部门名称</span>&nbsp;&nbsp;
														<input type="hidden" name="plugins_NodeOrgId" value="">
														<INPUT TYPE="text" NAME="plugins_nodeDePartName" value="" class=txtput
															disabled>
														&nbsp;&nbsp;<a href="#" id="selOrg" onclick="selOrg()">选择部门</a>
													</TD>
													<TD></TD>
												</TR>


												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>共用父流程表单数据（不要数据映射,在异步子流程中不可用,主子流程必须是同一张业务表）</span>&nbsp;&nbsp;
														<input type="checkbox" name="chkReturnSame" onclick="doCheckSame(this)" id="chkReturnSame">
														<input type="hidden" name="plugins_doReturnSame" id="plugins_doReturnSame" value="0">
													</TD>
													<TD></TD>
												</TR>
										
												

												
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>子流程返回父流程时不选择人员（类似任务节点返回经办人功能）</span>&nbsp;&nbsp;
														<input type="checkbox" name="chkReturn" onclick="doCheck(this)" id="chkReturn">
														<input type="hidden" name="plugins_doReturn" id="plugins_doReturn" value="0">
													</TD>
													<TD></TD>
												</TR>
												<TR valign=top style="display:none">
													<TD width=5></TD>
													<TD>
														<span id=tabpage2_7>科室子流程</span>&nbsp;&nbsp;
														<input type="checkbox" id="isNotAllowContinueToDept" onclick="doNotAllowContinueToDept(this)" />
														<input type="hidden" name="plugins_isNotAllowContinueToDept" value="0">
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


//选择部门
function selOrg(){
	//第一个返回的orgId以及部门名称
	window.showModalDialog("<%=path%>/usermanage/usermanage!getOrgTree.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:420px');
}
function setAreaCodeId(sid, name){
	document.getElementsByName("plugins_nodeDePartName")[0].value=name;	
	document.getElementsByName("plugins_NodeOrgId")[0].value=sid;			
}


	/**
	 * 处理节点插件信息方法，用户需重载此方法，主要处理插件信息由展现信息到具体后台值
	 */
	function handlePlugins(){
		if($("#useSuperStartor_1")[0].checked){
			$("#plugins_useSuperStartor").val("1");
		}else if($("#useSuperStartor_2")[0].checked){
			$("#plugins_useSuperStartor").val("2");
		}else if($("#useSuperStartor_3")[0].checked){
			$("#plugins_useSuperStartor").val("3");
		}
		if($("#setStartor")[0].checked){
			$("#plugins_setStartor").val("1");
		}else{
			$("#plugins_setStartor").val("0");
		}
		if($("#setSuperActor")[0].checked){
			$("#plugins_setSuperActor").val("1");
		}else{
			$("#plugins_setSuperActor").val("0");
		}
	}
	
	function dealSelect(obj){
		if(obj.value == "1"){
			document.getElementById("chkReturnSame").disabled = false;
		} else {
			document.getElementById("chkReturnSame").disabled = true;
			document.getElementById("chkReturnSame").checked = false;
			document.getElementsByName("plugins_doReturn")[0].value = "0";
		}
	}
	
	function doCheck(chkObj){
		if(chkObj.checked){
			document.getElementsByName("plugins_doReturn")[0].value = "1";
		}else{
			document.getElementsByName("plugins_doReturn")[0].value = "0";
		}
	}
	
	function doCheckSame(chkObj){
		/*var type = $("input:radio[name='synchronize']:checked").val();
		if(type == "0"){
			chkObj.checked = false;
			return ;
		}*/
		if(chkObj.checked){
			document.getElementsByName("plugins_doReturnSame")[0].value = "1";
		}else{
			document.getElementsByName("plugins_doReturnSame")[0].value = "0";
		}
	}
	function doNotAllowContinueToDept(chkObj){
		if(chkObj.checked){
			document.getElementsByName("plugins_isNotAllowContinueToDept")[0].value = "1";
		}else{
			document.getElementsByName("plugins_isNotAllowContinueToDept")[0].value = "0";
		}
	}

	
	/**
	 * 用户需要在页面重载的方法，初始化节点插件信息，主要是一些插件属性的展现方式
	 */
	function initPlugins(){
		if($("#plugins_useSuperStartor").val() == "1"){
			$("#useSuperStartor_1")[0].checked = true;
		}else if($("#plugins_useSuperStartor").val() == "2"){
			$("#useSuperStartor_2")[0].checked = true;
		}else if($("#plugins_useSuperStartor").val() == "3"){
			$("#useSuperStartor_3")[0].checked = true;
		}
		if($("#plugins_setStartor").val() == "1"){
			$("#setStartor")[0].checked = true;
		}
		if($("#plugins_setSuperActor").val() == "1"){
			$("#setSuperActor")[0].checked = true;
		}
		
		if($("#plugins_doReturn").val() == "1"){
			$("#chkReturn")[0].checked = true;
		}
		var chkConcurrencySet = document.getElementById("plugins_isNotAllowContinueToDept").value;
		  if(chkConcurrencySet){
		  	if(chkConcurrencySet == "1"){
			  	document.getElementById("isNotAllowContinueToDept").checked = true;
		  	}
		  }
		
		if($("#plugins_doReturnSame").val() == "1"){
			$("#chkReturnSame")[0].checked = true;
		}
	}

</script>
