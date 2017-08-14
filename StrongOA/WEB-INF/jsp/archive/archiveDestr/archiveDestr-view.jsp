<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML>
<HEAD>
<TITLE>操作内容</TITLE>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
<script type="text/javascript">
function showType(value){
  if(value==null){
  return "全部";
  }
  if(value=='0'){
  return "待审核";
  }
  if(value=='1'){
  return "审核中";
  }
  if(value=='2'){
  return "已销毁";
  }
  if(value=='3'){
  return "不通过";
  }
  
}

$(document).ready(function(){
  var val=showType(${model.destroyAuditingType});
  $("#destroyAuditingType").val(val);
});
</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<script language="javascript" type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  		<tr>
   	 		<td height="100%">
    			<table width="100%" border="0" cellspacing="0" cellpadding="0">
      				<tr>
        				<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        					<table width="100%" border="0" cellspacing="0" cellpadding="00">
         						<tr>
            						<td width="5%" align="center"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"></td>
            						<td width="50%">销毁案卷文件查看</td>
            						<td width="10%">&nbsp;</td>
           	 						<td width="35%">
            							<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                							<tr>
                								<td width="*%">&nbsp;</td>
                 			 					<td><a class="Operation" href="javascript:cancel();"><img src="<%=root%>/images/ico/ht.gif" width="15" height="15" class="img_s">返回&nbsp;</a></td>
				  								<td width="5"></td>
                							</tr>
            							</table>
            						</td>
          						</tr>
        					</table>
        				</td>
      				</tr>
					<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
						<tr>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">案卷编号：</span>
							</td>
							<td class="td1"  align="left">
								<input id="destroyFolderNo" name="model.destroyFolderNo" type="text" size="30" value="${model.destroyFolderNo}">
							</td>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">案卷题名：</span>
							</td>
							<td class="td1"  align="left">
								<input id="destroyFolderName" name="model.destroyFolderName" type="text" size="30" value="${model.destroyFolderName}">
							</td>
						</tr>
						<tr>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">全宗名称：</span>
							</td>
							<td class="td1" align="left">
								<input id="destroyFolderOrgname" name="model.destroyFolderOrgname" type="text" size="30" value="${model.destroyFolderOrgname }">
							</td>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">案卷创建日期：</span>
							</td>	
							<td class="td1"  align="left">
								<input id="destroyFolderDate" name="model.destroyFolderDate" type="text" size="30" value="${folderDate}">
							</td>
						</tr>		
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz">所属部门：</span>
							</td>
							<td class="td1"  align="left">
								<input id="departmentName" name="model.departmentName"  onkeydown="return false;"  type="text" size="30" value="${model.departmentName}">
							</td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz">归档号：</span>
							</td>
							<td class="td1"  align="left">
								<input id="destroyFolderArchiveNo" name="model.destroyFolderArchiveNo"  onkeydown="return false;"  type="text" size="30" value="${model.destroyFolderArchiveNo}">
							</td>						
						</tr>
						<tr>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">案卷创建者：</span>
							</td>	
							<td class="td1"  align="left">
								<input id="destroyFolderCreaterName" name="model.destroyFolderCreaterName" type="text" size="30" value="${model.destroyFolderCreaterName}">
							</td>		
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">审核状态：</span>
							</td>	
							<td class="td1"  align="left">
							
								<input id="destroyAuditingType"  onkeydown="return false;"  name="model.destroyAuditingType" type="text" size="30" value="">
							</td>					
						</tr>
						
						<tr>
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">销毁审核时间：</span>
							</td>	
							<td class="td1"  align="left">
							
								<input id="auditingTime" name="auditingTime"  onkeydown="return false;"   type="text" size="30" value="${auditingTime}">
							</td>		
							<td width="18%" height="21" class="biao_bg1" align="right">
								<span class="wz">销毁审核者：</span>
							</td>	
							<td class="td1"  align="left">
								<input id="destroyAuditingName" name="model.destroyAuditingName"  onkeydown="return false;"  type="text" size="30" value="${model.destroyAuditingName}">
							</td>					
						</tr>
						
						<tr >
							<td height="21" class="biao_bg1" align="right">
								<span class="wz">销毁原因：</span>
<%--								<s:hidden id="desc" name="model.destroyApplyDesc"></s:hidden>--%>
							</td>
							<td class="td1" colspan="3" align="left" >
							
								<textarea id="destroyAuditingDesc" name="destroyAuditingDesc" onkeydown="return false;" rows="5" cols="70">${model.destroyAuditingDesc }</textarea>
							</td>
						</tr>
					</table>
    		</table>
    		<webflex:flexTable name="myTable" width="100%" height="180px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" showSearch="false" collection="${destroyFileList}">
<%--		 		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">--%>
<%--        			<tr>--%>
<%--          				<td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16" onclick="search();"></td>--%>
<%--          				<td width="20%" align="center" class="biao_bg1"><input id="destroyFileNo" name="model.destroyFileNo" type="text" style="width:100%" value="${modle.destroyFolderNo}"></td>--%>
<%--          				<td width="20%" align="center"  class="biao_bg1"><input id="destroyFileName"  name="model.destroyFileName" type="text" style="width:100%"  value="${modle.destroyFolderName}"></td>--%>
<%--          				<td width="20%" align="center" class="biao_bg1"><strong:newdate  id="destroyFileDate" name="model.destroyFileDate" dateform="yy-MM-dd" isicon="true" width="100%" dateobj="${modle.destroyApplyTime}"/></td>--%>
<%--          				<td width="15%" align="center" class="biao_bg1"><input id="destroyFileAuthor" name="model.destroyFileAuthor" style="width:100%" value="${model.destroyFileAuthor}"></td>   --%>
<%--         				<td width="20%" align="center" class="biao_bg1"><input id="destroyFilePage" name="model.destroyFilePage" style="width:100%" value="${modle.destroyFolderDate}"/></td>--%>
<%--       				</tr>--%>
<%--      			</table> --%>
				<webflex:flexCheckBoxCol caption="选择" property="destroyFileId" showValue="destroyFileName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
				<webflex:flexTextCol caption="文件页码" property="destroyFilePage" showValue="destroyFilePage" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
				<webflex:flexTextCol caption="文件号" property="destroyFileNo" showValue="destroyFileNo" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
				<webflex:flexTextCol caption="文件名称" property="destroyFileName" showValue="destroyFileName" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
				<webflex:flexDateCol caption="文件创建日期" property="destroyFileDate" showValue="destroyFileDate" width="20%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
				<webflex:flexNumricCol caption="作者名" property="destroyFileAuthor" showValue="destroyFileAuthor" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>			
			</webflex:flexTable>
      	</td>
  	</tr>
	</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/ht.gif","返回","cancel",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function cancel(){
	location="<%=path%>/archive/archiveDestr/archiveDestr.action";
}


<%--var value=document.getElementById("desc").value;--%>
<%--document.getElementById("destroyApplyDesc").value=value;--%>
	

</script>
</BODY>
</HTML>
