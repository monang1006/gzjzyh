<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>报表类型</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script> -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			function search(){
				//var sortName1=$("#sortName").val();
				
				myTableForm.submit();
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" >
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
		<s:form action="/report/workflowReportSort.action" id="myTableForm" theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
				
						
							<input type="hidden" id="checkSort" name="checkSort" value="${checkSort}"/>
							<tr>
						<td height="100%">
						 <table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>报表类型</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="addReportSort();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="editReportSort();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="delReportSort();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="2%"></td>
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="sortId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;类型名称：&nbsp;<input name="model.sortName" type="text" id="sortName"  class="search"  title="类型名称" value="${model.sortName}" maxlength="30">
							       		&nbsp;&nbsp;类型描述：&nbsp;<input name="model.sortDesc" type="text" id="sortDesc"  class="search"  title="类型描述" value="${model.sortDesc}" maxlength="30">
							       		
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="sortId"
									showValue="sortName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexTextCol caption="类型名称" property="sortId"
									showValue="sortName" width="45%" isCanDrag="true" showsize="30"
									isCanSort="true" align="center"></webflex:flexTextCol>
								<webflex:flexTextCol caption="类型描述" property="sortDesc"
									showValue="sortDesc" width="50%" isCanDrag="true" showsize="30"
									isCanSort="true"  align="center"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
	 	var sMenu = new Menu();
	 	var checkSort = document.getElementById("checkSort").value;
       function initMenuT(){
	        sMenu.registerToDoc(sMenu);
	        var item = null;
	   
	     	
			<security:authorize ifAllGranted="001-0002000300010001">
			item = new MenuItem("<%=root%>/images/operationbtn/add.png","增加","addReportSort",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			</security:authorize>
			
			<security:authorize ifAllGranted="001-0002000300010002">
			item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editReportSort",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			</security:authorize>
			
			<security:authorize ifAllGranted="001-0002000300010003">
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delReportSort",1,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			</security:authorize>
	   
		
		
		
		sMenu.addShowType("ChangeWidthTable");
         registerMenu(sMenu);
	}
	//确定选择中的报表类型
	function sureReportSort(){
		var sortId = getValue();
		if(""==sortId|null==sortId){
			alert("请选择一个表单!");
			return;
		}
		if(sortId.indexOf(",")>0){
			alert("只能选择一张表单！");
			return;
		}
		
		var flag = false;
	var chkButtons = document.getElementsByName("chkButton");
	var sortName;
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==sortId){
			sortName = chkButtons[i].parentElement.parentElement.cells[2].value;
			flag=true;
			break;
		}
	}
	if(flag){
		var parWin = window.dialogArguments;
		parWin.document.getElementById("sortName").value=sortName;
		parWin.document.getElementById("sortId").value=sortId;
		window.close();
	}else{
		alert("请重新选择表单！");
		return;
	}
	}
	
	//选择报表类型时，关闭窗口
	function closeReportSort(){
		alert("关闭");
		window.close();
	}
	
	
	
	//添加报表类型
	function addReportSort(){		//添加报表类型
		window.showModalDialog("<%=path%>/report/workflowReportSort!input.action",window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:300px');
	}
	//编辑报表类型
	function editReportSort(){	//编辑文件
		var id=getValue();
		if(id=="")
			alert("请选择需编辑的报表类型！");
		else if(id.indexOf(",")!=-1)
			alert("请选择一个报表类型！");
		else{
	
			window.showModalDialog("<%=path%>/report/workflowReportSort!input.action?sortId="+id,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:300px');
		  }  
	}
	//删除
	function delReportSort(){
		var id=getValue();
		if(id==""){
			alert("请选择需编辑的报表类型！");
			return;

		}else{
			$.post('<%=path%>/report/workflowReportSort!isHasDelete.action',
		             { 'sortId':id},
		              function(data){
		              if(data=='1'){
		              	if(confirm("确定要删除吗?")) {
							location="<%=path%>/report/workflowReportSort!delete.action?sortId="+id;
						}
		              }else{	              
						alert("报表类型【"+data+"】已经应用，不能做删除操作!");
		              }
		       });
		
			
		}
	}
	
	function search(){
		myTableForm.submit();	
	}

</script>



	</BODY>
</HTML>
