<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>模板类别列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" >
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/doctemplate/doctempType/docTempType.action">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9" onclick="search()">&nbsp;
													模板类别列表
												</td>
												<td width="70%">
													<table border="0" align="right" cellpadding="0"
														cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td width="20">
																<img src="<%=root%>/images/ico/tianjia.gif"
																	width="15" height="15">
															</td>
															<td width="34">
																<a href="javascript:templatetypeAdd();">添加</a>
															</td>
															<td width="20">
																<img src="<%=root%>/images/ico/bianji.gif" width="14"
																	height="15">
															</td>
															<td width="34">
																<a href="javascript:templatetypeEdit();">修改</a>
															</td>
															<td width="20">
																<img src="<%=root%>/images/ico/shanchu.gif"
																	width="15" height="15">
															</td>
															<td width="34">
																<a href="javascript:templatetypeDel();">删除</a>
															</td>
															<td width="25">
																&nbsp;
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
								wholeCss="table1" property="docgroupId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection='${page.result}'
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="search()">
										</td>
										<td width="75%" align="center" class="biao_bg1">
											<input class="search" name="model.docgroupName" type="text"
												value="请输入模板类别名称">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input class="search" name="model.docgroupParentId"
												type="text" value="模板分类编号">
										</td>

										<td align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="docgroupId"
									showValue="docgroupName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="模板类别名称" property="docgroupName"
									showValue="docgroupName" width="75%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="模板类别编号"
									property="docgroupParentId" showValue="docgroupParentId"
									width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>

							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","templatetypeAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","templatetypeEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","templatetypeDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function templatetypeAdd(){
	//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
	var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!input.action", 
                                   "400px", "250px", window);
    if(ReturnStr=="OK"){
    	location.reload();
    }
}
function templatetypeEdit(){
	var docgroupId = getValue();
	if(checkSelectedOneDis()){
		//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action?docgroupId="+docgroupId,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
		var ReturnStr=OpenWindow("<%=path%>/doctemplate/doctempType/docTempType!input.action?docgroupId="+docgroupId, 
                                   "400px", "250px", window);
         if(ReturnStr=="OK"){
	    	location.reload();
	     }
	}
}
function templatetypeDel(){
	var docgroupId=getValue();
	if(checkSelectedOneDis()){
		if(confirm('确定删除选中的模板类别吗？')){
			$.post(
			 		"<%=path%>/doctemplate/doctempType/docTempType!delete.action",
			 		{docgroupId:docgroupId},
			 		function(data){
			 			if(data == "delfalse"){//此模板类别下已存在模板			 								 			
			 				alert("该模板类别下已存在模板项，不允许删除");
			 			}else{//此模板类别下不存在模板项
				 			alert("删除成功");
				 			location.reload();
			 			}
			 		}
			 	);
		}
	}
}
function search(){
	submitForm();
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}
</script>
	</body>
</html>
