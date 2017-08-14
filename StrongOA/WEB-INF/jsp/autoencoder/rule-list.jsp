<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);

	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>编码规则</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet" />
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet />
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet />
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/common/common.js"type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/autoencoder/rule!list.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>自动编号列表</strong>
									</td>
									<td align="right">
										<table border="0" align="right" cellpadding="00" cellspacing="0">
						            	<tr>
							            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="testCreate();"><img src="<%=root%>/images/operationbtn/Number_generator.png"/>&nbsp;生&nbsp;成&nbsp;器&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="usedList();"><img src="<%=root%>/images/operationbtn/Has_the_list.png"/>&nbsp;已&nbsp;用&nbsp;列&nbsp;表&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="onCreateItem();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="onUpdateItem();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="onDeleteItem();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
										</tr>
										</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
								<tr>
									<td>
										<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="code" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection='${page.result}' page="${page}">
											<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
												<tr>
													<td>
											       		&nbsp;&nbsp;规则名称：&nbsp;<input name="model.ruleName" id="model.ruleName" type="text"  class="search" title="请您输入规则名称" value="${model.ruleName }">
											       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="search();" type="button" />
											       	</td>
												</tr>
											</table>
											<webflex:flexCheckBoxCol caption="选择" property="id" showValue="ruleName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
											<webflex:flexTextCol caption="规则ID" property="id" showValue="id" width="25%" isCanDrag="true" isCanSort="true" showsize="32"></webflex:flexTextCol>
											<webflex:flexTextCol caption="规则名称" property="ruleName" showValue="ruleName" width="70%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
				item = new MenuItem("<%=root%>/images/operationbtn/Number_generator.png","生成器","testCreate",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Has_the_list.png","已用列表","usedList",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","onCreateItem",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","onUpdateItem",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","onDeleteItem",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			
			// 添加一条规则
			function onCreateItem(){
			
				var rule = window.showModalDialog("<%=path%>/autoencoder/rule!input.action", window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:400px');
			}
			
			// 更新一条规则
			function onUpdateItem(){
				var id = getValue();
				if(id == ""||id == "null"){
					alert("请选择要编辑的规则。");
					return;
				}
				else if(id.indexOf(",") != -1){
					alert("只能选择一条编辑规则。");
					return;
				}
				var rule = window.showModalDialog("<%=path%>/autoencoder/rule!input.action?id=" + id, window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:400px');
			}
			
			//测试生成文号
			function testCreate(){
				var id = getValue();
				var rule = window.showModalDialog("<%=path%>/autocode/autoCode!input.action?id=" + id, window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:250px');
			}
			
			// 删除一条规则
			function onDeleteItem(){
				var id = getValue();
				if(id == "" || id == "null"){
					alert("请选择要删除的规则。");
					return;
				}
				if(!confirm("确定要删除吗？")){
			        return;
			    }
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/autoencoder/rule!delete.action",
	      				data:"id=" + id,
	      				success:function(msg){
	      					if(msg=="deleted"){
	      						// 刷新规则列表
 								search();
	      					}else{
	      						alert("删除失败。");
	      					}	      						    				
						}
      			});
			}
			
			function usedList(){
				//window.location = "<%=path%>/autoencoder/codemanage.action";
				var rule = window.showModalDialog("<%=path%>/autoencoder/codemanage.action",window,'help:no;status:no;scroll:no;dialogWidth:950px; dialogHeight:610px');
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
