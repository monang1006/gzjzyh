<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="com.strongit.bo.ListTest" %>
<%@ page import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %> 
<HTML><HEAD><TITLE>操作内容</TITLE>
<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>


</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/systemlog/systemLog.action">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td>
            </td>
            <td width="50%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
           	 日志列表
            </td>
            <td width="10%">&nbsp;</td>
            <td width="35%">
            <table border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="*">&nbsp;</td>
                  <td width="5"></td>
                  <td ><a class="Operation" href="javascript:exportTempFile();"><img src="<%=root%>/images/ico/daochu.gif" width="15" height="15" class="img_s">导出数据&nbsp;</a></td>
<%--                  <td width="7%"><img src="<%=path%>/common/images/bianji.gif" width="14" height="15"></td>--%>
<%--                  <td width="10%"><a href="javascript:selectTempFile();">查找</a></td>--%>
                  <td width="5"></td>
                  <td ><a class="Operation" href="javascript:deleteTempFile();"><img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">删除&nbsp;</a></td>                                    
				
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="logId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">    
    	 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="3%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="17" height="16" style="cursor: hand;" title="单击搜索"></td>&nbsp;
          <td width="10%" align="center"  class="biao_bg1">
          	<s:textfield name="logOpname" cssClass="search" title="请输入操作用户"></s:textfield>
          </td>
          <td width="25%" align="center"  class="biao_bg1">
          	<s:textfield name="logOpcontent" cssClass="search" title="请输入操作内容"></s:textfield>
          </td>
          <td width="14%" align="center"  class="biao_bg1">
          	<strong:newdate name="logOpStartdate" id="logOpStartdate" dateobj="${logOpStartdate}" 
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
          </td>
          <td width="14%" align="center"  class="biao_bg1">
          	<strong:newdate name="logOpEnddate" id="logOpEnddate" dateobj="${logOpEnddate}" 
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
          </td>
          <td width="12%" align="center" class="biao_bg1">
          	<s:textfield name="logOpip" cssClass="search" title="请输入操作用户IP"></s:textfield>
          </td>
          <td width="10%" align="center" class="biao_bg1">
          	<s:select name="logGrade"  list="#{'':'日志级别','0':'操作日志','1':'系统日志','2':'安全日志'}" listKey="key" listValue="value" style="width:100%"/>
          </td>
          <td width="10%" align="center" class="biao_bg1">
          	<s:select name="logOpresult"  list="#{'':'操作结果','1':'成功','0':'失败'}" listKey="key" listValue="value" style="width:100%"/>
          </td>
          <td width="*%" align="center" class="biao_bg1"></td>
      </table> 
		<webflex:flexCheckBoxCol caption="选择" property="logId" showValue="logId" width="3%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="操作用户" property="logOpname" showValue="logOpname" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="操作内容" property="logOpcontent" showValue="logOpcontent" width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol caption="操作时间" property="logOpdate" showValue="logOpdate" dateFormat="yyyy-MM-dd" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
		<webflex:flexTextCol caption="操作IP" property="logOpip" showValue="logOpip" width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="日志级别" mapobj="${LogGradeMap}" property="logGrade" showValue="logGrade" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
		<webflex:flexEnumCol caption="是否成功" mapobj="${logresultMap}" property="logOpresult" showValue="logOpresult" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>

	</webflex:flexTable>
      </td>
  </tr>
</table>
</s:form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	//item = new MenuItem("<%=root%>/images/ico/shanchu.gif","导出数据","exportTempFile",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addTactic(){
	window.showModalDialog("tacticAdd.jsp",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:450px');
}
function exportTempFile(){
	window.showModalDialog("<%=path%>/systemlog/systemLog!input.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:450px');
}
function deleteTempFile(){
	var id=getValue();
	if(id==null||id==""){
		alert("请选择需要删除的记录！");
		return;
	}
	location="<%=path%>/systemlog/systemLog!delete.action?logId="+id+",";
	window.alert("删除成功！");	
}
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
</script>
</BODY></HTML>
