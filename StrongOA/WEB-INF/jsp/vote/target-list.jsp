<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>调查对象管理</TITLE>
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
  var vid="${vid}";
  function addtarget(){
    window.location="<%=root%>/vote/target!init_setTarget.action?vid="+vid;
  }
  
  function deltarget(){
    var tids=getValue();
	if(tids == null||tids == ''){
		alert('请选择要删除的调查对象！');
		return;
	}
   window.location="<%=root%>/vote/target!delTarget.action?page.pageNo=${page.pageNo}&vid=${vid}&tids="+tids;	
  }
  
  function goback(){
    window.location="<%=root%>/vote/vote.action";
  }
</SCRIPT>

</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">

     <form theme="simple" id="myTableForm" action="/vote/target!list.action"  >	
     <input type="hidden" name="vid" value="${vid}">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  > 
		 <tr>
		   <td height="40"style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>
					&nbsp;
					</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					调查对象列表
					</td>
					<td width="*">&nbsp;</td>
					<td >
						<a class="Operation" href="#" onclick="goback()">
						   <img src="<%=root%>/images/ico/ht.gif" width="15" height="15" class="img_s">
						   <span id="test" style="cursor: hand">返回&nbsp;</span>
						</a>
					</td>
					<td width="10"></td>
					<td >
						<a class="Operation" href="#" onclick="addtarget()">
						   <img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
						   <span id="test" style="cursor: hand">添加&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="deltarget()"> 
						<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">删除&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>	
				
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">   
      
		<webflex:flexCheckBoxCol caption="选择" property="tid" showValue="username" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	  	<webflex:flexTextCol caption="用户名"  property="username" showValue="username" width="45%" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="手机号" property="mobile" showValue="mobile" width="50%" isCanDrag="true" isCanSort="true"  ></webflex:flexTextCol>
	</webflex:flexTable>
	
	
		</form>

</td></tr></table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	//$("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deltarget",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}


</script>


</BODY></HTML>
