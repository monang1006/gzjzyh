<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
	<head>
		<title>个人办理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="kiben" content="no-cache">


		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'>
</script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'>
</script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js">
</script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js">
</script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js">
</script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js">
</script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;" 
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js">
		</script>
		<div class="gd_name"><div class="gd_name_left">
			<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
					&nbsp;个人办理</div>
			<div class="gd_name_right" style="margin-bottom: 5px">
			<input name="" type="button" class="gd_qs" value="签收" onclick="siGn();"/>
			<input name="" type="button" class="gd_gz" value="任务评语" onclick="viewPost();"/>
			</div>
		<br style="clear:both;"/>
		<DIV id=contentborder align=center>
			<s:form action="/workinspect/worktodo/workTodo!list.action"
				method="POST" theme="simple" id="myTableForm"
				onsubmit="return false;">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="100%">
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="sendtaskId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=frameroot%>/images/sousuo.gif" id="img_sousuo"
												width="17" height="16" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="10%" align="left" class="biao_bg1">
											<s:select name="selectTaskState" id="selectTaskState"
												list="#{'':'全部','0':'待签收','1':'办理中','2':'已办结'}"
												listKey="key" listValue="value" cssStyle="width:100%" onchange='$("#img_sousuo").click();'/>
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<s:textfield id="selectTaskTitle" name="selectTaskTitle"
												cssClass="search" title="标题" maxlength="50 "></s:textfield>
										</td>
										<td width="32%" align="center" class="biao_bg1">
											<s:textfield id="selectTaskUser" name="selectTaskUser"
												cssClass="search" title="任务来源" maxlength="50"></s:textfield>
										</td>
										<td width="30%" align="left" class="biao_bg1">
											<strong:newdate name="selectTaskSendTime"
												id="selectTaskSendTime" width="125" skin="whyGreen"
												isicon="true" dateform="yyyy-MM-dd" title="发送时间"
												dateobj="${selectTaskSendTime}"></strong:newdate>
											&nbsp;&nbsp;至&nbsp;&nbsp;
											<strong:newdate name="selectTaskSendTime2"
												id="selectTaskSendTime2" width="125" skin="whyGreen"
												isicon="true" dateform="yyyy-MM-dd" title="发送时间"
												dateobj="${selectTaskSendTime2}"></strong:newdate>
										</td>

										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>


								<webflex:flexCheckBoxCol caption="选择" property="sendtaskId"
									showValue="sendtaskId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexTextCol caption="" property="restImg"
									showValue="restImg" width="5%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>
								<%-- <webflex:flexTextCol caption="列号" property="restNum"
									showsize="5" showValue="restNum" isCanDrag="true" width="5%"
									isCanSort="true"></webflex:flexTextCol>--%>
								<webflex:flexTextCol caption="任务标题" property="sendtaskId"
									showValue="TOsWorktask.worktaskTitle" width="20%"
									isCanDrag="true" isCanSort="false"
									onclick="getinfo(this.value);" showsize="11"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="办理状态" mapobj="${typeMap}"
									property="taskState" showValue="taskState" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="任务来源" property="worktaskUserName"
									showsize="2000" showValue="TOsWorktask.worktaskUserName"
									isCanDrag="true" width="23%" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexDateCol caption="发送时间"
									property="TOsWorktask.worktaskEntryTime"
									dateFormat="yyyy-MM-dd HH:mm:ss"
									showValue="TOsWorktask.worktaskEntryTime" isCanDrag="true"
									width="18%" isCanSort="false"></webflex:flexDateCol>
								<webflex:flexDateCol caption="办理期限"
									property="TOsWorktask.worktaskEtime" showsize="16"
									dateFormat="yyyy-MM-dd" showValue="TOsWorktask.worktaskEtime"
									isCanDrag="true" width="15%" isCanSort="false"></webflex:flexDateCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td align="left">
							<div style="padding:10px; line-height:1.5;">
                            <h3 style="font-size:14px;">状态说明：</h3>
                            <p><span><img src="<%=frameroot%>/images/red.gif"/></span> -->已经超过办理期限的工作</p>
                            <p><span><img src="<%=frameroot%>/images/blue.gif"/></span> -->当前日期为办理期限最后一天</p>
                            <p><span><img src="<%=frameroot%>/images/green.gif"/></span> -->在办理期限之内的工作</p>
                            <p><span><img src="<%=frameroot%>/images/dgray.gif"/></span> -->办结件</p>
                            </div>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		</div>
		<script language="javascript">
var sMenu = new Menu();
/**
 * 初始化右键菜单
 * @param 
 */
function initMenuT() {
	var yangyon='${selectTaskState}';
	$("#selectTaskState").val(yangyon);
	
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/gd_qs.gif", "签收", "siGn", 1,
			"ChangeWidthTable", "checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_rwpy.gif", "任务评语", "viewPost",
			1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
	registerMenu(sMenu);
}

/**
 * 双击查看办理纪要
 * @param val
 */
function view(val) {
	window
			.showModalDialog(
					'<%=path%>/workinspect/worktodo/workTodo!view.action?sendtaskId=' + val,
					window,
					'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:570px');
}

/**
 * 查看工作评语
 * @param 
 */
function viewPost() {
	var id = getValue();
	if (id == null || id == "") {
		alert("请选择需要查看的记录！");
		return;
	}
	var ifind = id.indexOf(",");
	if (ifind != -1) {
		alert("只能查看一条记录!");
		return;
	}
	window
			.showModalDialog(
					'<%=path%>/workinspect/worktodo/workTodo!viewDev.action?sendtaskId=' + id,
					window,
					'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:300px');
}
/**
 * 签收工作
 * @param 
 */
function siGn() {
	var id = getValue();
	if (id == null || id == "") {
		alert("请选择需要签收的记录！");
		return;
	}
	var ifind=id.indexOf(",");
	if (ifind!=-1) {
		alert("只能签收一条记录!");
		return;
	}
	if (confirm("您确定要签收吗?")) {
		location = "<%=path%>/workinspect/worktodo/workTodo!siGn.action?sendtaskId="
				+ id;
	}

}
/**
 * 点击标题弹出页面
 * @param 
 */
function getinfo(id) {
	var audit = window
			.showModalDialog(
					"<%=path%>/workinspect/worktodo/workTodo!view.action?sendtaskId="
							+ id, window,
					'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
	if(audit){
		location = "<%=path%>/workinspect/worktodo/workTodo!list.action";
	}
}
$(document).ready(function() {
	$("#img_sousuo").click(function() {
		$("form").submit();
	});
});
</script>
	</BODY>
</HTML>
