<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>调查问卷管理</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<%
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragrma", "no-cache");
			response.setDateHeader("Expires", 0);
		%>


		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>


		<SCRIPT>
  var vote_state=new Array();//问卷的状态
  var vote_type=new Array();//问卷的类型
  var vote_isprivate=new Array();//问卷是否限制查看结果
  
  var canViewResult=false;//有权限查看调查结果
  
<security:authorize ifAnyGranted="001-0004000900030001"> 
	canViewResult=true;
</security:authorize>

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

function showState(state,vid,endDate){	
			//设置问卷状态
			vote_state[vid]=state;//存入问卷的状态
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
				  rv = "<font color='red'>未激活</font>&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定激活调查吗?\")){location = \"<%=path%>/vote/vote!enableVote.action?vids="+vid+"\";}'>[激活]</a>";
			   }
			   if(state == '1'){
				  rv = "<font color='green'>激活</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='if(confirm(\"确定终止调查吗?\")){location = \"<%=path%>/vote/vote!disableVote.action?vids="+vid+"\";}'>[终止]</a>";
			   }	
	        }		
			return rv;
}

function showType(vid,type,isprivate){
	//设置问卷的类型
  	vote_type[vid]=type;
	vote_isprivate[vid]=isprivate ;//是否限制查看结果
	
  	if(type==1){
   		return "页面参与";
  	}else if(type==2){
   		return "短信参与";
  	}
}
//function showOperate(id){	
           
//			var rv ;
//		rv= "<a href='<=path%>/survey/surveyVote!input.action?surveyId="+id+"'>[设置问卷调查]</a> <a href=\"javascript:window.showModalDialog('<=path%>/survey/surveyVote!view.action?viewType=view&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[预览]</a> <a href=\"javascript:window.showModalDialog('<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+id+"',window,'help:no;status:no;scroll:yes;dialogWidth:800px; dialogHeight:600px');\">[查看结果]</a>";
			
				
			
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

						<s:form theme="simple" id="myTableForm"
							action="/vote/vote!list_enabled.action">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid :     DXImageTransform.Microsoft.Gradient (     gradientType =     0, startColorStr =     #ededed, endColorStr =     #ffffff );">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="20%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													调查列表
												</td>
												<td>
													&nbsp;
												</td>
												<td width="75%">
													<table border="0" align="right" cellpadding="00"
														cellspacing="0">
														<tr>

															<td>
																<a class="Operation" href="#" onclick="viewvote()">
																	<img src="<%=root%>/images/ico/chakan.gif" width="15"
																		height="15" class="img_s"> <span id="test"
																	style="cursor: hand">查看投票&nbsp;</span> </a>
															</td>
															<td width="5"></td>
															<td>
																<a class="Operation" href="#" onclick="viewresult()">
																	<img src="<%=root%>/images/ico/chakan.gif" width="15"
																		height="15" class="img_s"> <span id="test"
																	style="cursor: hand">查看结果&nbsp;</span> </a>
															</td>

															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>

							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="surveyId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<input id="search" type="hidden" name="search" />
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" style="cursor: hand;" title="单击搜索"
												onclick="getListBySta();">
										</td>
										<td width="30%" align="center" class="biao_bg1">
											<input id="surveyName" name="vote.title" type="text"
												style="width: 100%" value="${vote.title}" class="search"
												title="请输入调查名称">
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<strong:newdate id="surveyStartTime" name="vote.startDate"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${vote.startDate}" classtyle="search" title="开始时间" />
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<strong:newdate id="surveyEndTime" name="vote.endDate"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${vote.endDate}" classtyle="search" title="结束时间" />
										</td>
										<td width="*%" align="center" class="biao_bg1">
											<input name="asdf" type="text" style="width: 100%"
												readonly="readonly" disabled="disabled">
										</td>
									</tr>
								</table>

								<webflex:flexCheckBoxCol caption="选择" property="vid"
									showValue="title" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="状态操作" property="state"
									showValue="state" width="0" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="调查名称" property="title"
									showValue="title" width="30%" isCanDrag="true" isCanSort="true"
									showsize="15"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="startDate"
									showValue="startDate" dateFormat="yyyy-MM-dd" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="endDate"
									showValue="endDate" dateFormat="yyyy-MM-dd" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="问卷类型" property="type"
									showValue="javascript:showType(vid,type,isPrivate)" width="9%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="限制重复" property="isRepeated"
									showValue="javascript:showCheckBox(isRepeated)" width="8%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="说明" property="memo"
									showValue="memo" width="*%" isCanDrag="true" showsize="8"
									isCanSort="true"></webflex:flexTextCol>
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
	
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看投票","viewvote",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看结果","viewresult",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
 
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
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
	
   //window.open("<%=root%>/vote/vote!viewVote.action?admin=N&vote.vid="+vid);
   //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/vote/vote!viewVote.action?admin=Y&vote.vid="+vid,"查看投票");
   var audit = window.showModalDialog("<%=root%>/vote/vote!viewVote.action?admin=N&vote.vid="+vid,window,'help:no;status:no;scroll:yes;dialogWidth:950px; dialogHeight:680px');
}

function viewresult(){
    //查看问卷结果
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的调查结果！');
		return;
	}
	if(id.length >32){
		alert('每次只能查看一个调查结果！');
		return;
	}
	var flag=false;	
	if(vote_isprivate[id]=="N"){
	    //不限制查看结果
	    flag=true;
	}else if(canViewResult){
	    //限制查看，但是有权限
	    flag=true;
	}
	if(flag){
    	window.showModalDialog("<%=path%>/vote/vote!viewResult.action?vote.vid="+id,window,'help:no;status:no;scroll:yes;dialogWidth:950px; dialogHeight:680px');
    }else{
      alert("对不起，您无权查看调查结果！");
    }
   //window.open("<%=path%>/vote/vote!viewResult.action?vote.vid="+id);
   //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/vote/vote!viewResult.action?vote.vid="+id,"查看投票结果");
}

function getListBySta(){	//根据属性查询

    document.getElementById("search").value="true";
	document.getElementById("myTableForm").submit();
}
</script>


	</BODY>
</HTML>
