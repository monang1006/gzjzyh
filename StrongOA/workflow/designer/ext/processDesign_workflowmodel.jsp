<HTML>
	<HEAD>
		<TITLE>流程模板列表</TITLE>
		<%@ page language="java" contentType="text/html; charset=UTF-8"
			pageEncoding="UTF-8"%>
		<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
		<%@ taglib uri="/struts-tags" prefix="s"%>
		<%@ taglib uri="/tags/security" prefix="security"%>
		<%@include file="/common/include/rootPath.jsp"%>
		<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
		<%@ taglib uri="/tags/c.tld" prefix="c" %>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=path%>/common/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script type="text/javascript">
		
	$(document).ready(function(){
		//搜索功能
		$("#img_sousuo").click(function(){
			$("form").submit();
		});
		
		//对选择的流程类型，显示流程模板
	function changePackageName(){
		
 		var processType=document.getElementById("processType").value;
 		if(processType!=null&&processType!=""){
 		
 		}else{
 			processType="<c:out value="${processType}"/>"
 		}
 		 $.post("<%=path%>/workflowDesign/action/processDesign!select.action",
		          {"processType":processType},
		          function(data){
	              	$("#modelId").html(data);
		 });	
 	}
	});
	
	
</script>
	</HEAD>
	<base target="_self" />
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="5%" align="center">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">
											</td>
											<td width="20%">
												<label>
													流程模板列表
												</label>
											</td>
											<td>
												&nbsp;
											</td>
											<td width="60%">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>

														
														<td width="50">
															<a class="Operation" href="javascript:del();"><img
																	src="<%=root%>/images/ico/shanchu.gif" width="14"
																	height="14" class="img_s">删除</a>
														</td>
														<td width="5"></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<s:form id="myTableForm" action="/workflowDesign/action/processDesign!workflowModelList.action" method="post">

								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="modelId" isCanDrag="true"
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
									getValueType="getValueByProperty"
									collection="${modelPage.result}" page="${modelPage}"
									>

									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img id="img_sousuo" style="cursor: hand;"
													src="<%=root%>/images/ico/perspective_leftside/sousuo.gif"
													width="17" height="16">
											</td>
											<td width="25%" align="center" class="biao_bg1">
												<select name="formModel.modelProcesstype"
													onchange='$("#img_sousuo").click();' 
													class="search" title="请选择流程类型">
														<option value="0">
															全部
														</option>
														
								       	
													<c:forEach items="${types}" var="type" varStatus="status">
														<script>										
														if("${processType}"=="<c:out value="${type[0]}"/>"){
															document.write("<option selected value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
														}else{
														
															document.write("<option value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
														}
														</script>
													</c:forEach>
												</select>
											</td>
											<td width="40%" class="biao_bg1">
												<input id="formModel.modelName" name="formModel.modelName"  type="text"
													style="width=100%" class="search" title="请您输入流程模板名称" maxlength="30"
													value="${formModel.modelName}">
											</td>
									      <td width="15%" align="center" class="biao_bg1">
											  <strong:newdate name="startDate" id="startDate" width="98%"
						                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"></strong:newdate>
										
										</td>
										<td width="15%" align="center" class="biao_bg1">
											  <strong:newdate name="endDate" id="endDate" width="98%"
						                     	 skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
										
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td>
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" property="modelId"
										showValue="modelName" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexEnumCol caption="流程类型名称" mapobj="${typeMap}"
									property="modelProcesstype" showValue="modelProcesstype" width="25%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
									
									<webflex:flexTextCol caption="流程模板名称" property="modelName"
										showValue="modelName" width="40%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexDateCol caption="添加时间" property="modelDate"
										showValue="modelDate" width="30%" isCanDrag="true"
										isCanSort="true" dateFormat="yyyy-MM-dd" showsize="16"></webflex:flexDateCol>

								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
	var sMenu = new Menu();
	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = null;
	
		item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
		sMenu.addShowType("ChangeWidthTable");
	    registerMenu(sMenu);
	    
	}

	function del(){
		 var id=getValue();
               if(id==null||id==""){
                  alert("请选择要删除的项！");   
               }else{
               	  if(confirm("确定要删除吗?")) {
                 	 window.location.href="<%=path%>/workflowDesign/action/processDesign!deleteWorkflowModel.action?modelId="+id;
                  }
               }
	}
	
	

</script>
	</BODY>
</HTML>
