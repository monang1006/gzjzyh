<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>调查管理</TITLE>
<%@include file="/common/include/meta.jsp" %>

        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	   



</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">

         <s:form theme="simple" id="myTableForm" action=""  >	
		<table width="100%" border="0" cellspacing="0" cellpadding="0"  > 
		 <tr>
		   <td height="40"style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
			   <table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td>&nbsp;</td>
					<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					调查样式列表
					</td>
					<td width="*">&nbsp;</td>
					<td >
						<a class="Operation" href="#" onclick="styleAdd()">
						   <img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
						   <span id="test" style="cursor: hand">添加&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="styleEdit()">
						 <img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">
						 <span id="test" style="cursor: hand">编辑&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="styleDel()"> 
						<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">删除&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>	
			        <td >
						<a class="Operation" href="#" onclick="styleRest()"> 
						<img src="<%=root%>/images/ico/set.gif" width="15" height="15" class="img_s">
						<span id="test" style="cursor: hand">重置为默认样式&nbsp;</span>
						</a>
					</td>
					<td width="5"></td>	
				
					
						</tr>
					</table>
				</td>
			</tr>
		</table>
	
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}" showSearch="false">
		<webflex:flexCheckBoxCol caption="选择" property="styleId" showValue="styleName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="样式名称" property="styleName" showValue="styleName" width="30%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
	</webflex:flexTable>
	
	
		</s:form>

</td></tr></table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","styleAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","styleEdit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","styleDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","重置为默认样式","styleRest",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function sendByAjax(url,par){  
	//ajax异步提交，三个参数分别为：处理页面，参数，返回显示结果
	$.post(url,par,
		function(data){
			alert("默认样式设置成功！");
		}
		
	);

	return false;
}

function styleAdd(){//增加样式
	var audit= window.showModalDialog("<%=root%>/survey/surveyStyle!input.action",window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
}
function styleEdit(){
	var id=getValue();
	
	if(id == null||id == ''){
		alert('请选择要编辑的样式！');
		return;
	}
	if(id.length >32){
		alert('每次只能编辑一个样式！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/survey/surveyStyle!input.action?styleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
}
function styleDel(){//删除样式
	var id=getValue();
	var styleName = $(":checked").parent().next().attr("value");
	
	if(id == null||id == ''){
		alert('请选择样式！');
		return;
	}	
		
	if(styleName == '默认样式'){
		alert('默认样式不能删除！');
		return;
	}	
	if(confirm("确定删除样式吗?")) 
	{ 
	location = '<%=path%>/survey/surveyStyle!delete.action?styleId='+id;
	} 	
}

function styleRest(){
    var id=getValue();
    	if(id == null||id == ''){
		alert('请选择要重置的样式！');
		return;
	}
	if(id.length >32){
		alert('每次只能重置一个样式！');
		return;
	}
    url="<%=path%>/survey/surveyStyle!reseat.action";
	par="styleId="+id;
    sendByAjax(url,par);

}


</script>


</BODY></HTML>
