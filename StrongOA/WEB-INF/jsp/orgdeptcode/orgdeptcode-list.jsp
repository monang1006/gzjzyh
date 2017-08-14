<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>

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
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
		<div id="contentborder" align="center">
						<s:form id="myTableForm" theme="simple" action="/orgdeptcode/orgdeptcode.action">
							
							<input type="hidden" name="extOrgId" id="extOrgId" value="${extOrgId}"/>
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
									                 	<td class="Operation_list" onclick="onCreateItem();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;规&nbsp;则&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="onDeleteItem();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;规&nbsp;则&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  	</tr>
								                  </table>
								                 </td>
								              </tr>
								            </table>
								        </td> 
								<tr>
									<td>
										<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="code" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection='${page.result}' page="${page}">
											<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
												<tr>
													<td>
											       		&nbsp;&nbsp;规则名称：&nbsp;<input name="ruleName" id="ruleName" type="text"  class="search" title="请您输入规则名称" value="${ruleName }">
											       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="search();" type="button" />
											       	</td>
												</tr>
											</table>
											<webflex:flexCheckBoxCol caption="选择" property="id" showValue="ruleName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
											<webflex:flexTextCol caption="规则ID" property="id" showValue="id" showsize="32" width="40%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="规则名称" property="ruleName" showValue="ruleName" width="55%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										</webflex:flexTable>
									</td>
								</tr>
							</table>
						</s:form>
		</div>
		<script language="javascript">
			var sMenu = new Menu();
			 var orgid = '${extOrgId}';
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/Number_generator.png","生成器","testCreate",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建规则","onCreateItem",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
			
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除规则","onDeleteItem",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			
			// 给该部门添加一条文号规则
			function onCreateItem(){
			if(orgid == null || orgid == ""){
			  alert("请选择机构部门。");
			  return ;
			  }else{
			     var rule = window.showModalDialog("<%=path%>/orgdeptcode/orgdeptcode!ruleList.action?extOrgId="+orgid, window, 'help:no;status:no;scroll:no;dialogWidth:1000px; dialogHeight:610px');
			}
			}
			
			// 更新一条规则
			function onUpdateItem(){
				var id = getValue();
				if(id == ""||id == "null"){
					alert("请先选择要编辑的规则。");
					return;
				}
				else if(id.indexOf(",") != -1){
					alert("只能选择一条编辑规则。");
					return;
				}
				var rule = window.showModalDialog("<%=path%>/autoencoder/rule!input.action?id=" + id, window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
			}
			
			
			//
			function testCreate(){
			  if(orgid == null || orgid == ""){
				  alert("请选择机构部门。");
				  return ;
			  }else{
			  	var id = getValue();
			  	//alert("id:"+id+"&orgId="+orgid);
				var rule = window.showModalDialog("<%=path%>/autocode/autoCode!input.action?id="+id+"&orgId="+orgid, window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:250px');
			  }
			}
			
			// 删除一条规则
			function onDeleteItem(){
				var id = getValue();
				if(id == "" || id == "null"){
					alert("请先选择要删除的规则。");
					return;
				}
				if(!confirm("确定要删除吗？")){
			        return;
			    }
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/orgdeptcode/orgdeptcode!delete.action",
	      				data:{
								"ruleIds":id,
								"extOrgId":orgid
								},	
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
				window.location = "<%=path%>/autoencoder/codemanage.action";
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
