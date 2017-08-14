<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>已发任务列表</TITLE>
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
	<div class="gd_name"><div class="gd_name_left">
			<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
												&nbsp;已发任务列表</div>
		<div class="gd_name_right" style="margin-bottom: 5px">
			<input name="" type="button" class="gd_gz" value="任务评语" onclick="review();"/>
			<input name="" type="button" class="gd_bl" value="办理纪要" onclick="summary();"/>
			<input name="" type="button" class="gd_delete" value="删除" onclick="del();"/>
			<input name="" type="button" class="gd_cb" value="催办" onclick="sendsms();"/>
		</div>
	<br style="clear:both;"/>
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm"
			action="/workinspect/worksend/workSend!getPersonal.action">
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
								
									<td width="22%" align="center" class="biao_bg1">
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
							<%--<webflex:flexTextCol caption="" property="worktaskUser" showValue="worktaskUser" width="0%" 
									isCanDrag="true" isCanSort="false" ></webflex:flexTextCol>
							--%><webflex:flexTextCol caption="任务类型" property="worktaskType"
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
							<%--<webflex:flexTextCol caption="发起人" property="worktaskUserName"
								showValue="worktaskUserName" width="10%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
						--%></webflex:flexTable>
					</td>
				</tr>
			</table>
		</s:form>
	</DIV>
	</div>
	<script language="javascript">
var sMenu = new Menu();
var orgid = '${extOrgId}';
/**
 * 初始化右键菜单
 * @param 
 */
function initMenuT() {
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/gd_rwpy.gif", "任务评语", "review",
			1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_bljy.gif", "办理纪要", "summary",
			1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_sc.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_cb.gif","催办","sendsms",1,"ChangeWidthTable","checkOneDis")
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

/**
 * 添加评语
 * @param 
 */
function review(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要添加评语的任务！');
		return;
	}
	if(id.split(",").length >1){
		alert('只能选择一条记录操作！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!taskReview.action?taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:260px');
}

/**
 * 添加纪要
 * @param 
 */
function summary(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要添加纪要的任务！');
		return;
	}
	if(id.split(",").length >1){
		alert('只能选择一条记录操作！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!summary.action?taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
}
//获得工作状态  add by niwy
function getworkState(id){
       var succ = false;//允许删除
       $.ajax({
   		type : "post",
   		url : scriptroot + "/workinspect/worksend/workSend!getworkSate.action",
   		data : "taskId=" + id,
   		async : false,
   		success : function(data) {
   			if (data == "1") {
   				succ = true; //不允许删除
   				return succ;
   			}
   		}
   	});	
       return succ;
	}


/**
 * 删除
 * @param 
 */
function del(){
	var id=getValue();
	
	if(id == null||id == ''){
		alert('请选择要删除的记录！');
		return;
	}
	if(id.split(",").length >1){
		alert('一次只能删除一条记录！');
		return;
	}
	var state=getworkState(id);
	
		if(state){
			alert("该任务已被签收，不能删除！");	
		}else{
			if(confirm("删除选定的任务，确定？")) { 
			location = '<%=path%>/workinspect/worksend/workSend!delete.action?taskId='+id;
		}
 	 } 
	
	
}

/**
 * 查看工作信息
 * @param id
 */
function getinfo(id){
	//var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
//	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,"工作内容");
    getSysConsole().refreshWorkByTitle("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,"工作内容");
}

 $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });

/**
 * 催办
 * @param obj
 */
function sendsms(obj){
	var id=getValue();
	if(id.split(",").length >1){
		alert('只能选择一条记录操作！');
		return;
	}
	$.ajax({
			type:"post",
			url:"<%=path%>/workinspect/worksend/workSend!getTaskReceiver.action",
			data:'model.worktaskId='+id,
			success:function(data){
					if(data!="" && data!=null){
						if(data == null||data == ''){
							alert('请选择要催办的承办者！');
							return;
						}
						var ida = data.split(",");
						if(ida.length>100){
							alert("一次催办操作请勿超过100个承办者。");
							return ;
						}
						var ss=$(":checked").parent().next().next().next().attr("value");
						if(ss.indexOf("办结")!=-1){
							if(ss.indexOf("待签收")==-1&&ss.indexOf("办理中")==-1){
								alert('该工作已办结，不需要再提醒！');
								return;
							}
						}
						var url = "<%=path%>/sms/sms!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_DCDB %>&recvUserIds="+data;
						var a = window.showModalDialog(url,window,'dialogWidth:400pt;dialogHeight:350pt;help:no;status:no;scroll:no');
						if("reload"==a){
							//alert("已提交服务器发送");
						}
					}else{
						 alert("该工作已办结，不需要再提醒！");
					}
				},
			error:function(data){
				alert("对不起，获取栏目信息出错"+data);
			}
	   });
}
</script>
	</BODY>
</HTML>
