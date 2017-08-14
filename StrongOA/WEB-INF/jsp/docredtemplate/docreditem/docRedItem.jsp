<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文套红项列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
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

		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#sousuo").submit();
			});
			
			//上传套红
			function uploadRedItem(){
				var ret=OpenWindow("<%=root%>/docredtemplate/docreditem/docRedItem!toUploadPage.action","400","270",window);
				var redtempGroupId="${redtempGroupId}";
				if(ret!=null && ret!=undefined && ret == "ok"){
					//parent.location="<%=root%>/fileNameRedirectAction.action?toPage=docredtemplate/docreditem/docredItemContent.jsp";
					window.location="<%=path%>/docredtemplate/docreditem/docRedItem.action?redtempGroupId="+redtempGroupId;
				}
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/docredtemplate/docreditem/docRedItem.action">
							<s:hidden id="redtempGroupId" name="redtempGroupId"></s:hidden>
							<s:hidden id="redtempGroupName" name="redtempGroupName"></s:hidden>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="5%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9" onclick="search()">&nbsp;
													${redtempGroupName}
												</td>
												<td width="*">
												</td>
												<td width="80%">
													<table border="0" cellpadding="0" align="right"
														cellspacing="0">
														<tr>
															<td >
															<a class="Operation" href="javascript:uploadRedItem();">
																<img src="<%=root%>/images/ico/shang.gif" width="14"
																	height="14" class="img_s">
															
																上传套红&nbsp;</a>
															</td>
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:docredItemAdd();">
																<img src="<%=root%>/images/ico/tianjia.gif" width="14"
																	height="14" class="img_s">
															
																添加&nbsp;</a>
															</td>
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:docredItemEdit();">
																<img src="<%=root%>/images/ico/bianji.gif" width="15"
																	height="15" class="img_s">
															
																修改&nbsp;</a>
															</td>
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:docredItemDel();">
																<img src="<%=root%>/images/ico/shanchu.gif" width="15"
																	height="15" class="img_s">
															删除&nbsp;</a>
															</td>
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:newsubtype();">
																<img src="<%=path%>/oa/image/prsnfldr/newfolder.gif"
																	width="15" height="15" class="img_s">
															
																新建子类别&nbsp;</a>
															</td>
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:newtype();">
																<img src="<%=path%>/oa/image/prsnfldr/newfolder.gif"
																	width="15" height="15" class="img_s">
															
																新建根类别&nbsp;</a>
															</td>
															<td width="5">
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
								wholeCss="table1" property="doctemplateId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection='${page.result}'
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img id="sousuo" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="15"
												height="15" onclick="search()">
										</td>
										<td width="70%" align="center" class="biao_bg1">
											<input class="search" name="model.redstempTitle"
												type="text" title="请输入套红名称">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<strong:newdate name="model.redstempCreateTime"
												id="search2" width="98%" skin="whyGreen" isicon="true"
												dateform="yyyy-MM-dd" classtyle="search" title="请选择套红创建时间"></strong:newdate>
										</td>

										<td align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="redstempId"
									showValue="redstempTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="套红名称" property="redstempTitle" showsize="40"
									showValue="redstempTitle" width="70%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="套红创建时间"
									property="redstempCreateTime"
									showValue="redstempCreateTime" dateFormat="yyyy-MM-dd"
									width="25%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>

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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","docredItemAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","docredItemEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","docredItemDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function docredItemAdd(){
	var Width=screen.availWidth-10;
    var Height=screen.availHeight-30;
    var id=document.getElementById("redtempGroupId").value;
    var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docreditem/docRedItem!input.action?redtempGroupId="+id, 
                                   Width, Height, window);
    if (ReturnStr == "OK") {
    	parent.project_work_content.document.location="<%=root%>/docredtemplate/docreditem/docRedItem.action?redtempGroupId="+$("#redtempGroupId").val();
    }
}
function docredItemEdit(){
	var redstempId = getValue();
	var id=document.getElementById("redtempGroupId").value;
	var Width=screen.availWidth-10;
    var Height=screen.availHeight-30;
	if(checkSelectedOneDis()){
	var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docreditem/docRedItem!input.action?redstempId="+redstempId+"&redtempGroupId="+id, 
                                   Width, Height, window);
	    if (ReturnStr == "OK") {
	         location.reload();
	    }
	}
}
function docredItemDel(){
	var redstempId=getValue();
	var id=document.getElementById("redtempGroupId").value;
	if(redstempId == ""){
		alert("请选择要删除的公文套红！");
		return ;
	}
	
	if(confirm('确定删除选中的公文套红吗？')){
		location="<%=path%>/docredtemplate/docreditem/docRedItem!delete.action?redstempId="+redstempId+"&redtempGroupId="+id;
	}
}

function newsubtype(){
	var id=document.getElementById("redtempGroupId").value;
	//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
	var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!init.action?redtempGroupId="+id, 
                                   "400px", "250px", window);
    if(ReturnStr=="OK"){
    	parent.project_work_tree.document.location.reload() 
    	//parent.project_work_tree.reload();
    }
}

function newtype(){
	//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
	var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!init.action", 
                                   "400px", "250px", window);
    if(ReturnStr=="OK"){
    	parent.project_work_tree.document.location.reload() 
    	//parent.project_work_tree.reload();
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
