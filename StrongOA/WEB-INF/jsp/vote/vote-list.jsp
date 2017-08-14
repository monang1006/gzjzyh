<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>调查管理</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<%
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragrma", "no-cache");
			response.setDateHeader("Expires", 0);
		%>


		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>

		<SCRIPT>
 <s:if test="msg!=null">
   alert("${msg}");
 </s:if>

  var vote_state=new Array();//调查的状态
  var vote_type=new Array();//调查的类型,1页面参与，2短信和页面参与

function sendByAjax(url,par){  
	//ajax异步提交，三个参数分别为：处理页面，参数，返回显示结果
	$.post(url,par,
		function(data){
		}
	);
	return false;
}

function showCheckBox(va){		  		    
			var rv ;
			if(va == 'Y'){
				rv = "不可重复";
			}else
			{
				rv = "可重复";
			}			
			return rv;
}

function getdate()
{   
  var now=new Date();
  y=now.getFullYear();
  m=now.getMonth()+1;
  d=now.getDate();
  m=m<10?"0"+m:m;
  d=d<10?"0"+d:d;
  return y+"-"+m+"-"+d;
}

function showPrivate(isprivate){
	//是否限制查看调查结果
  if(isprivate=="Y"){
    return "限制";
  }else{
    return "不限制";
  }
}

function showState(state,vid,endDate){	
			//设置调查状态
			vote_state[vid]=state;//存入调查的状态
			var rv ;
	        var NOW_DATE = getdate(); 
	        var END_DATE = endDate; 
	
	        if(NOW_DATE>END_DATE||state=='2')
	        {
	             rv = "<font color='#999999'>过期</font>";		
	             // url="<%=path%>/survey/survey!setState.action";
	             // par="surveyId="+surveyId+"&state=2";
	             // sendByAjax(url,par);
	             
	        }else
	        {
	           if(state == '0'){
				  rv = "<font color='red'>未激活</font>&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定激活调查吗?\")){location = \"<%=path%>/vote/vote!enableVote.action?vote.vid="+vid+"\";}'>[激活]</a>";
			   }
			   if(state == '1'){
				  rv = "<font color='green'>激活</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定终止调查吗?\")){location = \"<%=path%>/vote/vote!disableVote.action?vote.vid="+vid+"\";}'>[终止]</a>";
			   }	
	        }		
			return rv;
}

function showType(vid,type){
	//设置调查的类型
  	vote_type[vid]=type;
	
  	if(type==1){
   		return "页面";
  	}else if(type==2){
   		return "短信和页面";
  	}
}
//function showOperate(id){	
           
//			var rv ;
//		rv= "<a href='<=path%>/survey/surveyVote!input.action?surveyId="+id+"'>[设置调查调查]</a> <a href=\"javascript:window.showModalDialog('<=path%>/survey/surveyVote!view.action?viewType=view&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[预览]</a> <a href=\"javascript:window.showModalDialog('<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[查看结果]</a>";
			
				
			
//			return rv;
//		}
</SCRIPT>

	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm" action="/vote/vote.action">
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>调查列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="surveyAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="surveyEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="surveyDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="surveyVote();"><img src="<%=root%>/images/operationbtn/Set_the_content_of_investigation.png"/>&nbsp;设置调查内容&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="votetarget();"><img src="<%=root%>/images/operationbtn/Set_the_object_of_investigation.png"/>&nbsp;设置调查对象&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="viewvote();"><img src="<%=root%>/images/operationbtn/View_the_survey.png"/>&nbsp;查看调查&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="resultview();"><img src="<%=root%>/images/operationbtn/To_view_the_results.png"/>&nbsp;查看结果&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="viewvotelog();"><img src="<%=root%>/images/operationbtn/Check_the_records.png"/>&nbsp;查看记录&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="2%"></td>
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
			
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="surveyId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<input id="search" type="hidden" name="search" />
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;调查名称：&nbsp;<input id="surveyName" name="vote.title" type="text"
												 value="${vote.title}" class="search"
												title="请输入调查名称">
							       		&nbsp;&nbsp;开始时间：&nbsp;<strong:newdate id="surveyStartTime" name="vote.startDate"
												dateform="yyyy-MM-dd HH:mm" isicon="true" 
												dateobj="${vote.startDate}" classtyle="search" title="开始时间" />
							       		&nbsp;&nbsp;结束时间：&nbsp;<strong:newdate id="surveyEndTime" name="vote.endDate"
												dateform="yyyy-MM-dd HH:mm" isicon="true" 
												dateobj="${vote.endDate}" classtyle="search" title="结束时间" />
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="getListBySta()"/>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="vid"
									showValue="title" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="状态操作" property="state"
									showValue="state" width="0" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="调查名称" property="title"
									showValue="title" width="15%" isCanDrag="true" isCanSort="false"
									showsize="15"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="startDate"
									showValue="startDate" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="endDate"
									showValue="endDate" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
								<webflex:flexTextCol caption="调查类型" property="type"
									showValue="javascript:showType(vid,type)" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="限制重复" property="isRepeated"
									showValue="javascript:showCheckBox(isRepeated)" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="查看结果" property="isPrivate"
									showValue="javascript:showPrivate(isPrivate)" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="状态操作" property="state"
									showValue="javascript:showState(state,vid,endDate)" width="10%"
									isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
								<webflex:flexTextCol caption="说明" property="memo"
									showValue="memo" width="12%" isCanDrag="true" showsize="2"
									isCanSort="false"></webflex:flexTextCol>
							</webflex:flexTable>


						</s:form>

					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
    $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","添加","surveyAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","surveyEdit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","surveyDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/Set_the_content_of_investigation.png","设置调查内容","surveyVote",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/Set_the_object_of_investigation.png","设置调查对象","votetarget",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/View_the_survey.png","查看调查","viewvote",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/To_view_the_results.png","查看结果","resultview",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/Check_the_records.png","查看记录","viewvotelog",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function viewvotelog(){
	//查看那投票记录
	var vid=getValue();
	if(vid == null||vid == ''){
		alert('请选择要查看的调查！');
		return;
	}
	if(vid.length >32){
		alert('每次只能查看一个调查！');
		return;
	}
var audit = window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/vote/voteLogbox.jsp?vid="+vid,window,'help:no;status:no;scroll:auto;dialogWidth:750px; dialogHeight:580px');
 //  window.open("<%=path%>/fileNameRedirectAction.action?toPage=/vote/voteLogbox.jsp?vid="+vid);
}

function viewvote(){
  //进入投票页面
  var vid=getValue();
	if(vid == null||vid == ''){
		alert('请选择要查看的调查！');
		return;
	}
	if(vid.length >32){
		alert('每次只能查看一个调查！');
		return;
	}
   //window.open("<%=root%>/vote/vote!viewVote.action?admin=Y&vote.vid="+vid);
   //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/vote/vote!viewVote.action?admin=Y&vote.vid="+vid,"查看投票");
  var audit = window.showModalDialog("<%=root%>/vote/vote!viewVote.action?admin=Y&vote.vid="+vid,window,'help:no;status:no;scroll:auto;dialogWidth:950px; dialogHeight:680px');
}

function resultview(){
    //查看调查结果
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的调查结果！');
		return;
	}
	if(id.length >32){
		alert('每次只能查看一个调查结果！');
		return;
	}

   var audit = window.showModalDialog("<%=path%>/vote/vote!viewResult.action?vote.vid="+id,window,'help:no;status:no;scroll:yes;dialogWidth:950px; dialogHeight:680px');
   //window.open("<%=path%>/vote/vote!viewResult.action?vote.vid="+id);
   //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/vote/vote!viewResult.action?vote.vid="+id,"查看投票结果");
}

function votetarget(){
  //设置调查对象
  var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能编辑一个调查！');
		return;
	}
	var state=vote_state[id];
	
 	if(state=="1"&&vote_type[id]=="2"){
 		//激活的短信和页面投票，在激活时发送短信给问题
      	alert("如果添加了调查对象，必须重新激活调查！");
  	}
   //var audit= window.showModalDialog("<%=root%>/vote/target!init_setTarget.action?vid="+id,window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:450px');
   //location="<%=root%>/vote/target!init_setTarget.action?vid="+id;
   //window.open("<%=root%>/vote/target!init_setTarget.action?vid="+id);
   var uuid="&md="+new Date();
   window.showModalDialog("<%=root%>/vote/target!init_setTarget.action?vid="+id+uuid,window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:400px');
}


function surveyAdd(){
//增加调查
	var audit= window.showModalDialog("<%=root%>/vote/vote!init_add.action",window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:450px');
	//window.open("<%=root%>/vote/vote!init_add.action");
}
function surveyEdit(){
//编辑调查
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能编辑一个调查！');
		return;
	}
	var state=vote_state[id];
   if(state=='1'){
   		 alert("激活的投票不允许编辑！");
   }else{
        var audit =window.showModalDialog("<%=path%>/vote/vote!init_edit.action?vote.vid="+id,window,'help:no;status:no;scroll:no;dialogWidth:610px; dialogHeight:450px'); 
	    //window.open("<%=path%>/vote/vote!init_edit.action?vote.vid="+id);
   }
	
	//
}
function surveyDel(){//删除调查
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择调查！');
		return;
	}
	if(confirm("确定删除调查吗?")) 
	{ 
	 location = '<%=path%>/vote/vote!delVotes.action?vids='+id;
	} 	
}

function surveyVote(){//设置调查内容
	var vid=getValue();
	if(vid == null||vid == ''){
		alert('请选择要设置的调查！');
		return;
	}
	if(vid.length >32){
		alert('每次只能设置一个调查！');
		return;
	}
	var state=vote_state[vid];
	var type=vote_type[vid];
	
   if(state=='1'){
   		alert('该调查已经在投票当中，不允许设置了！');
   		return ;
   }else if(state=='2'){
     	 alert('该调查过期，不允许设置了！');
   		return ;
   }else{
	 location = "<%=path%>/vote/question!list.action?vid="+vid+"&vote_type="+type;
	 }
}

function surveyView(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要预览的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能预览一个调查！');
		return;
	}
	var audit = window.open("<%=path%>/vote/vote!previewVote.action?vote.vid="+id);
	// window.showModalDialog("<%=path%>/vote/vote!previewVote.action?vote.vid="+id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}

function getListBySta(){	//根据属性查询

    document.getElementById("search").value="true";
	document.getElementById("myTableForm").submit();
}
</script>


	</BODY>
</HTML>
