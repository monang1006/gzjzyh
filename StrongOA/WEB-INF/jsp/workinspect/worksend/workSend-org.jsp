<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<HTML>
	<HEAD>
		<TITLE>已发任务列表(分承办个人查看)</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
			<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<div class="gd_name"><div class="gd_name_left">
		<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
												&nbsp;已发任务列表(分承办个人查看)</div>
		<div class="gd_name_right" style="margin-bottom: 5px">
			<input name="" type="button" class="gd_gz" value="任务评语" onclick="review();"/>
			<input name="" type="button" class="gd_cb" value="催办" onclick="sendsms();"/>
		</div>
		<br style="clear:both;"/>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm" action="/workinspect/worksend/workSender!getOrg.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="sendtaskId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="3%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17" id="img_sousuo"
												height="16" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="11%" align="center" class="biao_bg1">
											 <s:select name="selectTaskState"  list="#{'':'全部','0':'待签收','1':'办理中','2':'已办结'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' cssStyle="width:100%"/>
										</td>
										<td width="22%" align="center" class="biao_bg1">
											 <s:textfield name="selectTaskTitle2" cssClass="search" title="请输入标题"  maxlength="50"></s:textfield>  
										</td>
										<td width="23%" align="center" class="biao_bg1">
											 <s:textfield name="selectRecvOrgName" cssClass="search" title="请输入承办单位" maxlength="50"></s:textfield> 
										</td>
										<td width="45%" align="left" class="biao_bg1">
											<strong:newdate name="selectTaskBSendTime" id="selectTaskBSendTime"  width="120"  title="请选择发送日期"
						                      skin="whyGreen" isicon="true" dateobj="${selectTaskBSendTime}" dateform="yyyy-MM-dd"></strong:newdate>						
											至<strong:newdate name="selectTaskESendTime" id="selectTaskESendTime"  width="120"  title="请选择发送日期"
						                      skin="whyGreen" isicon="true" dateobj="${selectTaskESendTime}" dateform="yyyy-MM-dd"></strong:newdate>						                    
										</td>
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexTextCol caption="" property="sendtaskId" showValue="restImg" width="3%" 
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexCheckBoxCol caption="选择" property="sendtaskId" showValue="sendtaskId" width="3%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol><%--
								<webflex:flexTextCol caption="" property="taskRecvId" showValue="taskRecvId" width="0%" 
									isCanDrag="true" isCanSort="false" onclick="sendsms(this.value)"></webflex:flexTextCol>
								--%><%--<webflex:flexTextCol caption="序号" property="sendtaskId" showValue="restNum" width="0%" isCanDrag="true"
									isCanSort="false"></webflex:flexTextCol>--%>
								<webflex:flexTextCol caption="任务标题" property="TOsWorktask.worktaskId" showValue="TOsWorktask.worktaskTitle" width="22%" isCanDrag="true"
									isCanSort="false" onclick="getinfo(this.value)" showsize="12"></webflex:flexTextCol>
								<webflex:flexTextCol caption="承办情况" property="taskRecvName" showValue="taskRecvName" 
									width="33%" isCanDrag="true" isCanSort="false" showsize="200"></webflex:flexTextCol>
								<webflex:flexDateCol caption="发送时间" property="taskSendTime" showValue="taskSendTime" width="17%" 
									isCanDrag="true" isCanSort="false" dateFormat="yyyy-MM-dd "></webflex:flexDateCol>	
								<webflex:flexTextCol caption="办理期限" property="TOsWorktask.worktaskEtime" showValue="TOsWorktask.worktaskEtime" 
									width="12%" isCanDrag="true" isCanSort="false" showsize="7"></webflex:flexTextCol><%--
								<webflex:flexTextCol caption="发起人" property="TOsWorktask.worktaskUserName" showValue="TOsWorktask.worktaskUserName" width="10%" 
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
							--%></webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;height:100px;">
				<td align="left">
					<div style="padding:10px; line-height:1.5;">
                            <h3 style="font-size:14px;">状态说明：</h3>
                            <p><span><img src="<%=frameroot%>/images/red.gif"/></span> -->已经超过办理期限的任务</p>
                            <p><span><img src="<%=frameroot%>/images/blue.gif"/></span> -->当前日期为办理期限最后一天</p>
                            <p><span><img src="<%=frameroot%>/images/green.gif"/></span> -->在办理期限之内的任务</p>
                            <p><span><img src="<%=frameroot%>/images/dgray.gif"/></span> -->办结件</p>
                            </div>
				</td>
			</table>
		</DIV>
		</div>
<script language="javascript">
var sMenu = new Menu();
/**
 * 初始化右键菜单
 * @param 
 */
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/gd_rwpy.gif","任务评语","review",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/gd_cb.gif","催办","sendsms2",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

/**
 * 添加/查看评语
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
	var audit = window.showModalDialog("<%=path%>/workinspect/worksend/workSend!sendReview.action?sendTaskId="+id,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:260px');
}

/**
 * 查看工作详情
 * @param obj
 */
function getinfo(id){
	//top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,"工作内容");
	getSysConsole().refreshWorkByTitle("<%=path%>/workinspect/worksend/workSend!view.action?taskId="+id,"任务内容");
}

 $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });

/**
 * 催办
 * @param 
 */
function sendsms2(){
	var id="";
	var check = $(":checked");  //得到所有被选中的checkbox
 		check.each(function(i){        //循环拼装被选中项的值
 			id+=$(this).parent().next().next().val()+","; 			
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
	var ss=$(":checked").parent().next().next().next().next().attr("value");
	if(ss.indexOf("办结")!=-1){
		alert('该任务已办结，不需要再提醒！');
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
  			id+=$(this).parent().next().next().val()+","; 			
   		});
		if(id.indexOf(",")==0){
			id=id.substr(1);
		}
	}else{
		var id=obj;
	}
	if(id.lastIndexOf(",")>0){
		id=id.substr(0,id.length-1);
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
	if(ss.indexOf("办结")!=-1){
		alert('该任务已办结，不需要再提醒！');
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
	</BODY>
</HTML>
