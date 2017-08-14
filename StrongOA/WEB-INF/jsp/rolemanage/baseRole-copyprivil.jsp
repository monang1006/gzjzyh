<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@	include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="java.util.*"%>
<HTML>
	<HEAD>
		<TITLE>资源复制</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<base target=_self>
	</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/rolemanage/baseRole!copyprivil.action">
  <input type="hidden" id="roleId" name="roleId" value="${roleId}"/>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
         <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td class="table_headtd_img" >
				<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
            <td align="left">
            	<strong>选择要复制资源的角色</strong>
            </td>
            <td align="right">
				<table border="0" align="right" cellpadding="00" cellspacing="0">
	                <tr>
						<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	                 	<td class="Operation_input" onclick="savecopyprivils();">&nbsp;确&nbsp;定&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                  		<td width="5"></td>
	                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
	                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                  		<td width="6"></td>	
	                </tr>
	            </table>
            </td>
          </tr>       
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="roleId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
     footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
         <tr>
         	<td>
	         	&nbsp;&nbsp;角色编码：&nbsp;<input name="selectroleId" id="selectroleId" type="text" class="search" style="width:110px;" title="请输入角色编码" value="${selectroleId}">
	       		&nbsp;&nbsp;角色名称：&nbsp;<input name="selectrolename" id="selectrolename" type="text" class="search"  style="width:110px;" title="请输入角色名称" value="${selectrolename}">
	       		&nbsp;&nbsp;状态：&nbsp;<s:select name="roleisact"  list="#{'':'是否启用','1':'是','0':'否'}" listKey="key" listValue="value" style="width:110px;" onchange='$("#img_sousuo").click();'/>
	       		&nbsp;&nbsp;角色描述：&nbsp;<input name="roledesc" id="roledesc" type="text" class="search"  style="width:110px;" title="请输入角色描述" value="${roledesc}">
	       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
         	</td>
         </tr>
      </table> 
		<webflex:flexCheckBoxCol caption="选择" property="roleId" showValue="roleSyscode" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="角色编码" property="roleSyscode" showValue="roleSyscode" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="角色名称" property="roleName" showValue="roleName" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="是否启用" mapobj="${useMap}" property="roleIsactive" showValue="roleIsactive" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
		<webflex:flexTextCol caption="角色描述" property="roleDescription" showValue="roleDescription" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
      </td>
  </tr>
</table>
  </s:form>
     <form action="<%=path %>/rolemanage/baseRole!savecopyprivils.action" method="post">
     <input type="hidden" id="roleIds" name="roleIds" value=""/>
     <input type="hidden" id="searchRoleId" name="searchRoleId" value="${roleId}"/>
     </form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
			
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
      function savecopyprivils(){
			var id = getValue();
			if(id == ""||id==null){
				alert('请选择角色。');
				return;
			}
			//var group=document.getElementById("roleId").value;
			
			document.getElementById("searchRoleId").value = document.getElementById("roleId").value;
			
			document.getElementById("roleIds").value =id;
			document.forms[1].submit();
		}
		
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("#myTableForm").submit();
        });     
      });
</script>
</BODY>
</HTML>
