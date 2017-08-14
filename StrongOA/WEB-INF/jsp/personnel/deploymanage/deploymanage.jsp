<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>调配类别列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		 <script type="text/javascript">
		
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
	 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/personnel/deploymanage/deploymanage.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="45%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9"  align="center">&nbsp;
												调配类别列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="55%"><table align="right"><tr>
											<td>
												<a class="Operation" href="#" onclick="addTitle()"> <img
														src="<%=root%>/images/ico/tianjia.gif"
														width="15" height="15"  class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onclick="editTitle()"> <img
														src="<%=root%>/images/ico/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onclick="deleteTitle()"> <img
														src="<%=root%>/images/ico/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											
											<td width="5"></td>
											</tr></table></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%"  align="center" class="biao_bg1">
										          <img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="17" height="16" style="cursor: hand;" title="单击搜索">
									</td>
									
									<td width="55%" class="biao_bg1">		
									<s:textfield name="pName" cssClass="search" title="请输入调配名称"></s:textfield>		
									</td>
									
									<td width="20%" class="biao_bg1">
								     <s:select name="pisveteran"  list="#{'':'是否转入','0':'未转入','1':'转入'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();' />
									</td>
									<td width="20%" class="biao_bg1">
								     <s:select name="pIsactiv"  list="#{'':'是否启用','0':'未启用','1':'启用'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();' />
									</td>
								<td width="5%" class="biao_bg1">
								 &nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="pdepId"
								showValue="pdepName" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
						
							<webflex:flexTextCol caption="调配名称" property="pdepName"
								showValue="pdepName" width="55%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
							<webflex:flexEnumCol caption="是否转入老干部" mapobj="${veternMap}" property="pdepIsveteran" showValue="pdepIsveteran" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>	
							<webflex:flexEnumCol caption="是否启用" mapobj="${activeMap}" property="pdepIsactiv" showValue="pdepIsactiv" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>	

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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添 加","addTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编 辑","editTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","deleteTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
	var result=window.showModalDialog("<%=path%>/personnel/deploymanage/deploymanage!input.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:400px');
}
function editTitle(){
	var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要编辑的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能编辑一条记录!');
		return;
	}	
	
	var result=window.showModalDialog("<%=path%>/personnel/deploymanage/deploymanage!input.action?deployId="+id,window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:400px');

}
function deleteTitle(){
var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
			return;
		}
	if(confirm("您确定要删除吗？")){
	$.post(
		"<%=path%>/personnel/deploymanage/deploymanage!delete.action",
		{deployId:id},
		function(data){
	
			if(data=='deletefalsedeletetrue'){	
		      alert('删除失败，请先删除与之关联的调配记录。')    
		}
		else if(data=='deletetrue'){
		   location="<%=path%>/personnel/deploymanage/deploymanage.action"; 
		}
		}
	)
	}
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });


</script>
	</BODY>
</HTML>
		