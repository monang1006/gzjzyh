<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>工作草稿</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	<div class="gd_name"><div class="gd_name_left"><img src="<%=frameroot%>/images/ico/ico.gif" width="14" height="12" >
												&nbsp;任务草稿列表</div>
		<div class="gd_name_right" style="margin-bottom: 5px">
			<input name="" type="button" class="gd_add2" value="新增" onclick="tianjia();"/>
			<input name="" type="button" class="gd_ck" value="查看" onclick="chakan();"/>
			<input name="" type="button" class="gd_qs" value="编辑" onclick="bianji();"/>
			<input name="" type="button" class="gd_delete" value="删除" onclick="del();"/>
			<input name="" type="button" class="gd_send" value="发送" onclick="banli();"/>
		</div>
		<br style="clear:both;"/>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm"
			action="/workinspect/worksend/workSend!getPersonalDraft.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" 
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<webflex:flexTable name="myTable" width="100%" height="370px"
							wholeCss="table1" property="userId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}"
							page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img src="<%=root%>/images/ico/sousuo.gif" width="17"
											id="img_sousuo" height="16" style="cursor: hand;"
											title="单击搜索">
									</td>
									
									<%--<td width="12%" align="left" class="biao_bg1">
										<s:select name="selectTaskType"
											list="#{'1':'个人工作','0':'部门工作'}" listKey="key"
											listValue="value" cssStyle="width:100%" />
									</td>
									--%><td width="22%" align="center" class="biao_bg1">
										<s:textfield name="selectTaskTitle" cssClass="search"
											title="请输入标题" maxlength="50"></s:textfield>
									</td>
									<td width="23%" align="center" class="biao_bg1">
										<s:textfield name="selectTaskNo" cssClass="search"
											title="请输入编号" maxlength="50"></s:textfield>
									</td>
									<td width="*%" align="center" class="biao_bg1">
										&nbsp;
									</td>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="worktaskId"
								showValue="worktaskNo" width="5%" isCheckAll="true"
								isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="任务类型" property="worktaskType"
								showValue="worktaskType" width="12%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
							<webflex:flexTextCol caption="任务标题" property="worktaskId"
								showValue="worktaskTitle" width="22%" isCanDrag="true"
								isCanSort="false" showsize="12" onclick="getinfo(this.value)"></webflex:flexTextCol>
							<webflex:flexTextCol caption="承办情况" property="worktaskSender" showValue="worktaskSender" 
								width="25%" isCanDrag="true" isCanSort="false" showsize="200"></webflex:flexTextCol>
							<webflex:flexDateCol caption="发送日期" property="worktaskEntryTime"
								showValue="worktaskEntryTime" width="13%" isCanDrag="true"
								isCanSort="false" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
							<webflex:flexDateCol caption="办理期限" property="worktaskEtime"
								showValue="worktaskEtime" width="13%" isCanDrag="true"
								isCanSort="false" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
							<webflex:flexTextCol caption="发起人" property="worktaskUserName"
								showValue="worktaskUserName" width="12%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</s:form>
	</DIV>
	</div>
	<script language="javascript">
var sMenu = new Menu();
var orgid = '${extOrgId}';
function initMenuT() {
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/gd_tj.gif","新增","tianjia",1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_ck.gif","查看","chakan",1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_bj.gif","编辑","bianji",1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_sc.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_fs.gif","发送","banli",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

//新增
function tianjia(){
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!input.action?inputFrom=1",window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:560px');
	 if( audit && audit=="OK"){
		 window.location.reload();
	  }
}

//查看
function chakan(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的任务！');
		return;
	}
	if(id.split(",").length >1){
		alert('只能选择一条记录操作！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!viewDraft.action?taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:560px');
}

//编辑
function bianji(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的任务！');
		return;
	}
	if(id.split(",").length >1){
		alert('只能选择一条记录操作！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!inputDraft.action?temp=1&taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:600px');
	if( audit && audit=="OK"){
		 window.location.reload();
	 }
}

//删除
function del(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的任务！');
		return;
	}
	if(confirm("删除选定的任务，确定？")) { 
		location = '<%=path%>/workinspect/worksend/workSend!deleteDraft.action?taskId='+id;
	} 
}
// 办理
function banli(){
	var id=getValue();
	
	if(id == null||id == ''){
		alert('请选择要发送的任务！');
		return;
	}
	if(id.split(",").length >1){
		alert('不能同时发送多条任务！');
		return;
	}
		location = '<%=path%>/workinspect/worksend/workSend!inputDraft.action?taskId='+id;
}
//查看工作信息
function getinfo(id){
	//var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
//	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,"工作内容");
    getSysConsole().refreshWorkByTitle("<%=path%>/workinspect/worksend/workSend!viewDraft.action?taskId="+id,"工作内容");
}

 $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });

</script>
	</BODY>
</HTML>
