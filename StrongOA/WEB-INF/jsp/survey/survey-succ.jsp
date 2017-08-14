<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>调查表管理</TITLE>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		

		
		
<SCRIPT>


function sendByAjax(url,par){
	queryString = par;
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
						},
			onFailure : function()
						{
							alert("操作出错，请联系管理员！");
							//alert(url);							
						},
			parameters : queryString
		}
	);
}




function showState(sta){	
                             		    
		    var rv ;

	        if(sta == '0'){
				  rv = "<font color='red'>已投票</font>";
			}
			if(sta == '1'){
				rv = "<font color='green'>投票中</font>";
			}		      		
			return rv;
}

</SCRIPT>

</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">

<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">

        <s:form theme="simple" id="myTableForm" action="" >	
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  > 
		 <tr>
		   <td height="40"style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>&nbsp;</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					接受调查列表
					</td>
					<td width="*">&nbsp;</td>
					<td >
						<a class="Operation" href="#" onclick="surveySub()">
						   <img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
						   <span id="test" style="cursor: hand">投票&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="surveySee()">
						 <img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">
						 <span id="test" style="cursor: hand">查看结果&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
				
					<td width="5"></td>					
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}" showSearch="false">
		<webflex:flexCheckBoxCol caption="选择" property="surveyId" showValue="surveyName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="调查名称" property="surveyName" showValue="surveyName" width="30%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
		<webflex:flexDateCol caption="开始时间" property="surveyStartTime" showValue="surveyStartTime" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexDateCol caption="结束时间" property="surveyEndTime" showValue="surveyEndTime" dateFormat="yyyy-MM-dd" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	    <webflex:flexTextCol caption="状态操作"  property="state" showValue="javascript:showState(state)" width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="已提交(份)"  property="surveyCount" showValue="surveyCount" width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
    </s:form>
 	
</td></tr></table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","投票","surveySub",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看结果","surveySee",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}



function surveySee(){//查看投票结果
	var id=getValue();
		if(id == null||id == ''){
		alert('请选择要查看的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能查看一个调查！');
		return;
	}
	var audit= window.showModalDialog("<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}

function surveySub(){//投票
	var id=getValue();
		if(id == null||id == ''){
		alert('请选择要调查的调查！');
		return;
	}
	if(id.length >32){
		alert('每次只能选择一个调查！');
		return;
	}
	//location ="<=path%>/survey/surveyVote!view.action?viewType=sub&surveyId="+id;
   // var audit= window.showModalDialog("<%=path%>/survey/surveyVote!view.action?viewType=sub&surveyId="+id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
     var audit = window.parent.refreshWorkByUrl("<%=path%>/survey/surveyVote!view.action?viewType=sub&surveyId="+id, '参与投票调查');
}

</script>


</BODY></HTML>
