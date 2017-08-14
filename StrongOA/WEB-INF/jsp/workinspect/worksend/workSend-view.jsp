<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>

		<title>任务信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>		
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<script language="javascript">

//获得工作状态  add by niwy
function getworkState(){
       var succ = false;//允许修改
       $.ajax({
   		type : "post",
   		url : scriptroot + "/workinspect/worksend/workSend!getworkSate.action",
   		data : "taskId=" + $("#taskId").val(),
   		async : false,
   		success : function(data) {
   			if (data == "1") {
   				succ = true; //不允许修改
   				return succ;
   			}
   		}
   	});	
       return succ;
	}
//设置承办者,可选择多个
function selectUser(){
	var url = "<%=path%>/workflowDesign/action/userSelect!assignUserList.action";
	window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:500px');
}

//人员选择界面调用的方法，返回人员信息
function setSelectedData(selectedData){
   	var returnValue = selectedData.join("|");
   	//returnValue:返回的人员信息
	//格式:off808081335428e7013354ca2cc00042,省政府|u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042,鹿心社
	//说明:以"|"分割，'off808081335428e7013354ca2cc00042,省政府'表示一条数据  ,off808081335428e7013354ca2cc00042以'o'开头，表示发给部门的
	//u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042以'u'开头，表示发给个人的，'$'后面的表示用户所在部门ID
  	
  	//初始化已选人员使用
	document.getElementsByName("handleactor")[0].value = returnValue;
	var temp = "";
	var temp_id = "";
	if(returnValue != ""){
		for(var i = 0; i < selectedData.length; i++){
			if(i>0){
				temp += ",";
				temp_id +=",";
			}
			var name = selectedData[i].split(",")[1];
			var id = selectedData[i].split(",")[0];
			temp += name;
			if(id.indexOf('$')>0){
				var uid = id.split("$")[0];
				temp_id += "4|"+uid.substring(1);
			}else{
				temp_id += "1|"+id.substring(1);
			}
		}
	}
	$("#recvNames").val(temp);
	$("#recvIds").val(temp_id);
}
//人员选择界面调用的方法,初始化已选人员
function getInitData(){
   	var returnValue;
   	//初始化已选人员使用
	returnValue = document.getElementsByName("handleactor")[0].value;
	return (returnValue == null || returnValue == "") ? [] : returnValue.split("|");
}
		
//编辑工作
function edit(){
	//var ss=$(":checked").parent().next().next().next().next().next().attr("value");
	var state= getworkState();
	 if(state){
		 alert("该任务已被签收，不允许修改！");
	 }else{
			var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!edit.action?taskId="+$("#taskId").val(),window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:600px');
		    location = '<%=path%>/workinspect/worksend/workSend!view.action?taskId='+$("#taskId").val();
	 }
}

//添加承办者
function addRecver(){
	var ids = $("#selectedData2").val();//老
	var ids2 = $("#recvIds").val();//新
	if(ids2.length==0){
		alert('请选择承办者！');
		return;
	}
	if(ids!=""){
		var ida = ids.split(",");
		var ida2 = ids2.split(",");
		if(ida.length>=ida2.length){
			for(var i = 0; i<ida2.length; i++){
		   		if(ids.indexOf(ida2[i])>=0){
		   			alert('该承办者已经办结该任务，不允许添加！');
					return;
		   		}			
	    	}
		}else{
			for(var i = 0; i<ida.length; i++){
		   		if(ids2.indexOf(ida[i])>=0){
		   			alert('选择的承办者中含有已存在的承办者,请勿重复添加！');
					return;
		   		}			
	    	}
		}
	}
	

	$("#mytable").attr("action","<%=path%>/workinspect/worksend/workSend!addRecver.action");
	$("#mytable").submit();
}

//删除承办者
function delRecver(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的承办者！');
		return;
	}
	/*
	var ida = id.split(",");
	if(ida.length>100){
		alert("一次删除承办者操作请勿超过100个。");
		return ;
	}
	*/
	/*
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行删除操作!");
		return;
	}
	*/
	if(confirm("删除选定的承办者，确定？")) {
		$("#recvIds").val(id);
		$("#mytable").attr("action","<%=path%>/workinspect/worksend/workSend!deleteRecver.action");
		$("#mytable").submit();
		//location = "< %=path%>/workinspect/worksend/workSend!deleteRecver.action?taskId="+$("#taskId").val()+"&recvIds="+id;
	} 
}
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												任务信息
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="85%">
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务标题：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.worktaskTitle}
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">承办者：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${recvNames}
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务内容：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.worktaskContent}
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">办理期限：</span>
									</td>
									<td class="td1" width="280px" align="left">
										${model.worktaskStime}	&nbsp;至&nbsp;${model.worktaskEtime}
									</td>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">紧急程度：</span>
									</td>
									<td class="td1" align="left">
										<c:if test="${model.worktaskEmerlevel == '0' }">普通</c:if>
										<c:if test="${model.worktaskEmerlevel == '1' }">快速</c:if>
										<c:if test="${model.worktaskEmerlevel == '2' }"><font color="red">紧急</font></c:if>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务编号：</span>
									</td>
									<td class="td1" align="left">
										${model.worktaskNo}
									</td>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务分类：</span>
									</td>
									<td class="td1" align="left">
										${model.worktaskType}
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起人：</span>
									</td>
									<td class="td1" align="left">
										${model.worktaskUserName}
									</td>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起单位：</span>
									</td>
									<td class="td1" align="left">
										${model.worktaskUnitName}
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<div style="OVERFLOW-y:auto;HEIGHT:30px;">
										<s:if
											test="attachList!=null&&attachList.size()>0">
											<s:iterator id="vo" value="attachList">
												<div id="${vo.attachId}">
		                                              <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;'><font color="blue">${vo.attachFileName}</font></a>
													<br>
												
												</div>
											</s:iterator>
										</s:if>
									</div>
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<%-- 
						<table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table align="center" width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td align="center" width="100%">
												<input name="" type="button" class="gd_qs" value="修 改" onclick="edit();"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>--%>						
						<s:form id="mytable"
							action="/workinspect/worksend/workSend!addRecver.action" theme="simple">
							<s:hidden id="taskId" name="taskId"></s:hidden>
						<div style="overflow-y:scroll; width:100%; height:125px;">
						<webflex:flexTable name="myTable" width="100%" height="125px"
							wholeCss="table1" property="sendtaskId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA"
							getValueType="getValueByProperty" collection="${workSendList}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="30"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="40%" align="left">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
													承办者列表
												</td>
												<td width="60%" align="right">
													<input name="" type="button" class="gd_cb" value="催办" onclick="sendsms();"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="sendtaskId" showValue="taskRecvName" width="4%" isCheckAll="true"
								isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<%--<webflex:flexTextCol caption="" property="taskRecvId" showValue="restMobileImg" width="3%"
								isCanDrag="false" isCanSort="false" onclick="sendsms(this.value)"></webflex:flexTextCol>--%>
							<webflex:flexTextCol caption="办理单位/人" property="taskRecvId" showValue="taskRecvName" width="12%" isCanDrag="true"
								isCanSort="true" showsize="6"></webflex:flexTextCol>
							<webflex:flexTextCol caption="所属单位" property="taskRecvOrgid" showValue="taskRecvOrgName" width="26%" isCanDrag="true"
								isCanSort="true" showsize="17"></webflex:flexTextCol>
							<webflex:flexEnumCol caption="办理类型" mapobj="${taskTypeMap}" property="taskType" showValue="taskType" 
								width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							<webflex:flexEnumCol caption="办理状态" mapobj="${taskStateMap}" property="taskState" showValue="taskState" width="10%" 
								isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							<webflex:flexDateCol caption="签收时间" property="taskRecvTime" showValue="taskRecvTime" width="18%" 
								isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
							<webflex:flexDateCol caption="办结时间" property="taskcompleteTime" showValue="taskcompleteTime" width="18%" 
								isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
						</webflex:flexTable>
						</div>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">承办者：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" cols="58" readonly="true" id="recvNames" name="recvNames"></textarea>
										<input name="" type="button" class="gd_add2" value="选择" onclick="selectUser();"/>
										<input id="recvIds" name="recvIds" type="hidden"/>
										<input id="selectedData" name="selectedData" type="hidden"/>
										<input id="selectedData2" name="selectedData2" value="${selectedData}" type="hidden"/>
										<!-- 初始化已选人员使用 -->
										<input id="handleactor" name="handleactor" type="hidden"/>
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						 <table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table align="center" width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr align="center">
											<td width="50%">
												<input name="add" type="button" class="gd_add2" value="添 加" onclick="addRecver();"/>
											</td>
											<td width="50%">
												<input name="del" type="button" class="gd_delete" value="删 除" onclick="delRecver();"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=frameroot%>/images/mobile.gif","催办","sendsms2",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function sendsms2(){
	var id="";
	var check = $(":checked");  //得到所有被选中的checkbox
 		check.each(function(i){        //循环拼装被选中项的值
 			id+=$(this).parent().next().val()+","; 			
  		});
	if(id.indexOf(",")==0){
		id=id.substr(1);
	}
	if(id.lastIndexOf(",")>0){
		id=id.substr(0,id.length-1);
	}
	if(id == null||id == ''){
		alert('请选择要催办的承办者！');
		return;
	}
	if(id.split(",").length >1){
		alert('只能选择一位承办者操作！');
		return;
	}
	var ss=$(":checked").parent().next().next().next().next().next().attr("value");
	if(ss=='2'){
		alert('该工作已办结，不需要再提醒！');
		return;
	}
	//alert("这里有一个验证承办者是否已经办结!");
	var url = "<%=path%>/sms/sms!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_DCDB %>&recvUserIds="+id;
	var a = window.showModalDialog(url,window,'dialogWidth:400pt;dialogHeight:350pt;help:no;status:no;scroll:no');
	if("reload"==a){
		//alert("已提交服务器发送");
	}
}
function sendsms(obj){
	if(obj==null){
		var id="";
		var check = $(":checked");  //得到所有被选中的checkbox
  		check.each(function(i){        //循环拼装被选中项的值
  			id+=$(this).parent().next().val()+","; 			
   		});
		if(id.indexOf(",")==0){
			id=id.substr(1);
		}
	}else{
		var id=obj;
	}
	if(id == null||id == ''){
		alert('请选择要催办的承办者！');
		return;
	}
	var ida = id.split(",");
	if(ida.length>100){
		alert("一次催办操作请勿超过100个承办者。");
		return ;
	}
	var ss=$(":checked").parent().next().next().next().next().attr("value");
	if(ss=='2'){
		alert('该工作已办结，不需要再提醒！');
		return;
	}
	//alert("这里有一个验证承办者是否已经办结!");
	var url = "<%=path%>/sms/sms!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_DCDB %>&recvUserIds="+id;
	var a = window.showModalDialog(url,window,'dialogWidth:400pt;dialogHeight:350pt;help:no;status:no;scroll:no');
	if("reload"==a){
		//alert("已提交服务器发送");
	}
}
</script>