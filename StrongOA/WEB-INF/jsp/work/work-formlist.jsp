<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>新建工作-选择表单</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=path%>/common/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("input:checkbox").parent().next().next().hide();
		$("#img_search").click(function(){
			$("form").submit();
		});
		
	});
</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
           <td>&nbsp;</td>
            <td width="20%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            <label>${workflowTypeName }表单列表</label>
            </td>
            <td>&nbsp;</td>
            <td width="60%">
            	<table border="0" align="right" cellpadding="00" cellspacing="0">
                <tr>
                	  <td ><a class="Operation" href="JavaScript:listForm();"><img class="img_s" src="<%=root%>/images/ico/chakanlishi.gif" width="15" height="15">显示所有表单&nbsp;</a></td>
	                  <td width="5"></td>
                	<security:authorize ifAnyGranted="001-0002000100020001, 001-0001000100020001">
	                  <td ><a class="Operation" href="JavaScript:gotoNew();"><img class="img_s" src="<%=root%>/images/ico/tianjia.gif" width="15" height="15">新建&nbsp;</a></td>
	                  <td width="5"></td>
	                </security:authorize>  
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
	<s:form id="myTableForm" action="/work/work!formList.action">
		<s:hidden id="workflowType" name="workflowType"></s:hidden>
		<s:hidden name="workflowTypeName"></s:hidden>
	     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="id" 
	     isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
	     footShow="showCheck" getValueType="getValueByProperty" 
	     collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
		 <tr>
          <td width="4%" align="center"  class="biao_bg1"><img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17" height="16"></td>
          <td width="96%" align="center" class="biao_bg1"><s:textfield name="formName" cssClass="search" title="输入表单名称"></s:textfield></td>
          </td>
          <td class="biao_bg1">&nbsp;</td>
         </tr>
		 </table>
		<webflex:flexCheckBoxCol caption="选择" property="id" 
			showValue="title" width="4%" isCheckAll="true" isCanDrag="false"
			isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="表单名称" property="title"
			showValue="title" width="96%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="所属流程类型" property="flowType"
			showValue="flowType" width="96%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>	
	  </webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<security:authorize ifAnyGranted="001-0002000100020001, 001-0001000100020001">
		item = new MenuItem("<%=root%>/images/ico/tianjia.gif","新建","gotoNew",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>  	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//显示所有表单
function listForm(){
	parent.project_work_content.location = "<%=root%>/work/work!formList.action";
}
//新建
function gotoNew(){
	var formId = getValue();
	if(formId == ""){
		alert("请先选择表单！");
		return ;
	}else{
		if(formId.split(",").length>1){
			alert("一次只能选择一张表单！");
			return ;
		}
	}
	var workflowType = $("input:checkbox:checked").parent().next().next().attr("value");//选中表单所属流程类型。用于控制页面流转
	var hrefLink = "";
	switch (workflowType){
		case '1'://栏目类型
			break;
		case '2'://发文
			hrefLink = "<%=root%>/senddoc/sendDoc!input.action?formId="+formId;
			break;
		case '3'://收文
			hrefLink = "<%=root%>/recvdoc/recvDoc!input.action?formId="+formId;	
			break;
		case '4'://督察督办
			hrefLink = "<%=root %>/inspect/inspect!input.action?formId="+formId;	
			break;
		case '5'://会议管理
			break;
		case '6'://提案建议
			break;
		case '7'://信访管理
			break;
		case '8'://值班管理
			break;
		case '9'://信息处理
			break;
		case '10'://新闻管理
			break;
		case '11'://签报管理
			break;
		case '12'://档案管理
			break;	
		default:
			hrefLink = "<%=root%>/work/work!input.action?formId="+formId;	
			break;										
	}
	parent.location = hrefLink;
}
</script>
</BODY></HTML>
