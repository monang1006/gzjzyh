<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<title>会议类型管理</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
			<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.easyui.min.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script language="javascript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="javascript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<style>
		.add {line-height:16px;font-size:14px;width: 100%;float:left}
		</style>
		<script type="text/javascript">
		$(document).ready(function(){
			if("${isAdd}"=="yes"){
				$("#doIt").html("确认添加&nbsp;");
				$("#addEditTable").show();
			}
	        $("#img_sousuo").click(function(){
	        	$("form").submit();
	        });
	    });
		function addSort(){
			$("#addEditTable").show();
			$("#doIt").html("确认添加&nbsp;");
			$("#addconfersortId").val("");
			$("#addconfersortName").val("");
			$("#addconfersortDemo").val("");
			$("#addconfersortName").focus();
		}

		function doAddSort(){
			var id = document.getElementById("addconfersortId").value;
			var name = document.getElementById("addconfersortName").value;
			var demo = document.getElementById("addconfersortDemo").value;
			if(name==null||name==""){
				alert("请输入议题类型名称！");
				return;
			}
			if(name.length>50){
				alert("类型名称长度不能超过50！");
				return false;
			}
			if(demo.length>500){
				alert("描述过长！");
				return false;
			}
			if(id==""){//标示是添加还是编辑，如果是添加那submit后仍然展现添加框，方便用户继续添加。
				$("#isAdd").val("yes");
			}else{
				$("#isAdd").val("no");
			}
		 
			$.post(
					"<%=root%>/noticeconference/noticeConferenceType!save.action",
					{"model.contypeId":id,"model.contypeName":name,"model.contypeDemo":demo},
					function(data){
						if(data=="nameConflict"){
							alert("操作失败，已存在该会议类型！");
							return false;
						}else if(data=="notExit"){
							alert("议题类型已被删除！");
							$("form").submit();
						}
						$("form").submit();
					});
		}
		
		function goBack(){
			$("#addconfersortName").val("");
			$("#addconfersortDemo").val("");
			$("#addEditTable").hide();
		}
		
		function deleteSort(){
			var id = getValue();
			if(id==null || id==""){
				alert("请选择需要删除的会议类型！");
				return;
			}
			if(confirm("您确定要删除吗？")){
				$.post(
						"<%=root%>/noticeconference/noticeConferenceType!delete.action",
						{"model.contypeId":id},
						function(data){
							if(data=="nosuccess"){
								alert("删除会议类型失败，类型下存在议题！");
								$("form").submit();
							}else{
								$("form").submit();
							}
						});
			}
		}

		function editSort(){
			var id = getValue();
			if(id==null || id==""){
				alert("请选择需要编辑的会议类型！");
				return;
			}
			if(id.split(",").length >1){
				alert("一次只能查编辑一条记录！");
				return;
			}
			$("#addconfersortId").val(id);
			var editName = $("input:checked").parent().parent().children().eq(2).html();
			var editDemo = $("input:checked").parent().parent().children().eq(3).html();
			editName = editName.replace("&nbsp;","");
			editDemo = editDemo.replace("&nbsp;","");
			$("#addconfersortName").val(editName);
			$("#addconfersortDemo").val(editDemo);
			$("#addEditTable").show();
			$("#doIt").html("保存修改&nbsp;");
			$("#addconfersortName").focus();
			var t=$("#addconfersortName").val();
			$("#addconfersortName").val("").focus().val(t);
		}
		</script> 
	</head>

<style>
.btn1_mouseout {
	BORDER-RIGHT: #7b9ebd 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #7b9ebd 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (        
		       GradientType =     0, StartColorStr =      #ffffff, EndColorStr
		=   
		 #cecfde );
	BORDER-LEFT: #7b9ebd 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #7b9ebd 1px solid
}

.btn1_mouseover {
	BORDER-RIGHT: #FF0000 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #FF0000 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (     
		            GradientType =     0, StartColorStr =      #ffffff,
		EndColorStr =      #CAE4B6 );
	BORDER-LEFT: #FF0000 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #FF0000 1px solid
}
</style>
	<body class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();">
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/noticeconference/noticeConferenceType.action" method="get">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
									startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="45%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9"  align="center">&nbsp;
												会议类型列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="70%"><table align="right"><tr>
											<td >
												<a class="Operation" href="#" onclick="addSort()"> <img
														src="<%=root%>/images/ico/tb-add.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">添加&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="editSort()"> <img
														src="<%=root%>/images/ico/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="deleteSort()"> <img
														src="<%=root%>/images/ico/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											</tr></table></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						 
						<table id="addEditTable" style="display: none" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
								<td style="text-overflow:ellipsis;white-space:nowrap;" class="biao_bg1" align="right" width="110" >
									<span class="add">类型名称(<font color=red>必填</font>)：</span>
								</td>
								<td class="biao_bg1" align="left" width="20%">
									<input id="addconfersortId" name="model.contypeId" type="hidden" />
									<input id="isAdd" name="isAdd" type="hidden" />
									<input id="addconfersortName" name="model.contypeName" type="text"
										 title="会议类型名称" style="width:100%;border-width:1px;"/>
								</td>
								<td class="biao_bg1" align="right" width="75">
									<span class="add">类型描述：</span>
								</td>
								<td class="biao_bg1" align="left" width="40%">
									<textarea id="addconfersortDemo" name="model.contypeDemo" rows="3"
										title="会议类型描述" maxlength="250" style="width:100%;border-width:1px;"></textarea>
								</td>
								<td class="biao_bg1">
									<table align="left"><tr>
											<td width="5"></td>
											<td><a class="Operation" href="#" onclick="doAddSort()"> <img
														src="<%=root%>/images/ico/baocun.gif"
														width="15" height="15" class="img_s"><span id="doIt"
													style="cursor:hand">确认&nbsp;</span> </a></td>
											<td width="5"></td>
											<td><a class="Operation" href="#" onclick="goBack()"> <img
														src="<%=root%>/images/ico/guanbi.gif"
														width="15" height="15" class="img_s"><span id="back"
													style="cursor:hand">返回&nbsp;</span> </a></td></tr>
									</table>
								</td>
								<td></td>
							</tr>
						</table>
					 
						
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="24%" class="biao_bg1">
									<s:textfield name="search.contypeName" cssClass="search" title="类型名称"></s:textfield> 
									</td>
									<td width="71%" class="biao_bg1">
									<s:textfield name="search.contypeDemo" cssClass="search" title="类型描述"></s:textfield>
									</td>
									<td width="5%" class="biao_bg1">
											<input type='button' id="img_sousuo" class=btn1_mouseout
												onmouseover="this.className='btn1_mouseover'"
												onmouseout="this.className='btn1_mouseout'"value='搜 索' />
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="contypeId"
								showValue="contypeId" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="类型名称" property="page.contypeName"
								showValue="contypeName" width="20%" isCanDrag="true" isCanSort="false" showsize="10"></webflex:flexTextCol>
							<webflex:flexTextCol caption="类型描述" property="page.contypeDemo"
								showValue="contypeDemo" width="75%" isCanDrag="true" isCanSort="false" showsize="40"></webflex:flexTextCol>
						</webflex:flexTable>
					
					</td>
				</tr>
			</table>
				</s:form>
		</DIV>
		<script TYPE="text/javascript">
		var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			item = new MenuItem("<%=root%>/images/ico/tb-add.gif","添 加","addSort",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/bianji.gif","编 辑","editSort",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","deleteSort",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
	</body>
</html>
