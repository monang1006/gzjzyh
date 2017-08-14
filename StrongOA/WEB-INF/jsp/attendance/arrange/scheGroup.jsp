<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>班组管理</TITLE>
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
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/address/address.js"
			type="text/javascript"></script>
		<script type="text/javascript">	
			function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=="[]")
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;' title='该班组下存在班次'>";
			}		
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center style="BORDER-BOTTOM:0px">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
												&nbsp;
											</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												班组信息列表
											</td>
											<td width="70%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">
																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	<td width="58" align="right">
																		<a class="Operation" href="javascript:add();">
																			<img src="<%=root%>/images/ico/tianjia.gif"
																				width="15" height="15" class="img_s">新增</a>
																	</td>
																	<td width="5"></td>
																	<td width="58" align="right">
																		<a class="Operation" href="javascript:edit();"> <img
																				src="<%=root%>/images/ico/bianji.gif" width="15"
																				height="15" class="img_s">修改</a>
																	</td>
																	<td width="5"></td>
																	<td width="58" align="right">
																		<a class="Operation" href="javascript:del();">
																			<img src="<%=root%>/images/ico/shanchu.gif"
																				width="15" height="15" class="img_s">删除</a>
																	</td>
																	<td width="5"></td>
																	<%--<td width="85" align="right">
																		<a class="Operation" href="javascript:manageSche();">
																			<img src="<%=root%>/images/ico/set_config.gif"
																				width="15" height="15" class="img_s">设置班次</a>
																	</td>
																	<td width="5"></td>
																	--%><td width="90" align="right">
																		<a class="Operation" href="javascript:arrange();">
																			<img src="<%=root%>/images/ico/planManage.gif"
																				width="15" height="15" class="img_s">人员排班</a>
																	</td>
																	<td width="5"></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr higth="20"></tr>
													<tr>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<s:form id="myTableForm" action="/attendance/arrange/scheGroup.action">
								<input id="groupId" type="hidden" name="groupId"
									value="${groupId }" />
								<!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="groupId" isCanDrag="true"
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" pageSize="10"
									getValueType="getValueByProperty" collection="${page.result}"
									page="${page}" onclick="viewScheGrouplist(this.value)">
									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img src="<%=root%>/images/ico/sousuo.gif" width="17"
													id="img_sousuo" height="16" style="cursor: hand;"
													title="单击搜索">
											</td>
											<td width="15%" align="center" class="biao_bg1">
												<input type="text" name="model.groupName"
													value="${model.groupName}" class="search" title="请输入班组名" />
											</td>
											<td width="15%" align="center" class="biao_bg1">
												<s:select list="groupTypeMap" id="logo" name="model.logo"
													listKey="key" listValue="value" headerKey="" headerValue="请选择班组类型"
													cssStyle="width:100%" onchange="$('#img_sousuo').click();" />
											</td>
											<td width="20%" align="center" class="biao_bg1">
												<strong:newdate id="groupStime" name="model.groupStime"
													dateform="yyyy-MM-dd" isicon="true" width="100%"
													dateobj="${model.groupStime}" classtyle="search"
													title="请选择有效日期" />
											</td>
											<td width="20%" align="center" class="biao_bg1">
												<strong:newdate id="groupEtime" name="model.groupEtime"
													dateform="yyyy-MM-dd" isicon="true" width="100%"
													dateobj="${model.groupEtime}" classtyle="search"
													title="请选择失效日期" />
											</td>
											<td width="25%" align="center" class="biao_bg1">
												<input type="text" name="model.groupDesc"
													value="${model.groupDesc}" class="search" title="请输入备注" />
											</td>
											<td class="biao_bg1">
												&nbsp;
											</td>
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" property="groupId"
										showValue="groupName" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="班组名称" property="groupId"
										showValue="groupName" onclick="viewInfo(this.value)" width="15%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexEnumCol caption="班组类型" property="logo"
										mapobj="${groupTypeMap}" showValue="logo" width="15%"
										isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
									<webflex:flexDateCol caption="有效时间" property="groupStime"
										showValue="groupStime" width="20%" isCanDrag="true"
										isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
									<webflex:flexDateCol caption="失效时间" property="groupEtime"
										showValue="groupEtime" width="20%" isCanDrag="true"
										isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
									<webflex:flexTextCol caption="备注" property="groupDesc"
										showValue="groupDesc" width="25%" showsize="20"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<%--<webflex:flexTextCol caption="班次" property="groupId"
										showValue="javascript:showimg(toaAttendSchedules)" width="5%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
								--%></webflex:flexTable>
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
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","新增","add",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/bianji.gif","修改","edit",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				//item = new MenuItem("<%=root%>/images/ico/set_config.gif","设置班次","manageSche",1,"ChangeWidthTable","checkOneDis");
				//sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/planManage.gif","人员排班","arrange",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		
		function add(){
		 	OpenWindow("<%=path%>/attendance/arrange/scheGroup!input.action",420,270,window);	
		}
		
		function edit(){
			var id=getValue();
			if(id==""){
				alert("请选择需编辑的班组信息！");
			}else if(id.indexOf(",")!=-1){
				alert("只能选择一条班组信息！");
			}else{
				OpenWindow("<%=path%>/attendance/arrange/scheGroup!input.action?groupId="+id,420,270,window);	
			}
		}
		
		function del(){
			var id=getValue();
			if(id==""){
				alert("请选择要删除的班组信息！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条班组信息！");
			}else{
			    if(confirm("确定要删除吗？")){
			    	$.ajax({
						type:"post",
						url:"<%=path%>/attendance/arrange/scheGroup!isHasSchedules.action",
						data:{
							groupId:id		
						},
						success:function(data){
							if(data == "0"){
								 if(confirm("该班组下有班次，是否继续？")){
								 	if(confirm("该班组已给人员排班，是否继续？")){
								 		location="<%=path%>/attendance/arrange/scheGroup!delete.action?groupId="+id;
								 	}
								 }
							}else if(data == "00"){
								 if(confirm("该班组已给人员排班，是否继续？")){
								 	location="<%=path%>/attendance/arrange/scheGroup!delete.action?groupId="+id;
								 }
							}else{
								location="<%=path%>/attendance/arrange/scheGroup!delete.action?groupId="+id;	
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
				}	
			}
		}
		
		function arrange(){
			var id=getValue();
			if(id==""){
				alert("请选择需编辑的班组信息！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条班组信息！");
			}else{
				$.ajax({
					type:"post",
					url:"<%=path%>/attendance/arrange/scheGroup!isCanArrange.action",
					data:{
						groupId:id		
					},
					success:function(data){
						//if(data == "0"){
						//	alert("该班组下没有班次，请设置班次！");
						//	return;
						//}else if(data == "00"){
						//	alert("该班组下的部分班次没有设置轮班规则，请先设置轮班规则！");
						//	return;
						//}else{
							//OpenWindow("<%=path%>/attendance/arrange/scheGroup!arrange.action?groupId="+id,440,355,window);
							OpenWindow("<%=path%>/attendance/arrange/scheGroup!arrange1.action?groupId="+id,350,400,window);		
						//}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				});
			}
		}
		
		function manageSche(){
			var id=getValue();
			if(id==""){
				alert("请选择一条班组信息！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条班组信息！");
			}else{
				window.parent.schedules.location="<%=path%>/attendance/arrange/schedules.action?groupId="+id;	
			}
		}

		function submitForm(){
			document.getElementById("myTableForm").submit();
		}
		
		 $(document).ready(function(){
		    $("#img_sousuo").click(function(){
		    	$("Form").submit();
		    });     
		  });
      
      	function viewInfo(id){
			OpenWindow("<%=path%>/attendance/arrange/scheGroup!view.action?groupId="+id,420,270,window);	
		}
		
		function viewScheGrouplist(value){
			window.parent.schedules.location="<%=path%>/attendance/arrange/schedules.action?groupId="+value;	
		}
		
	</script>
</BODY>
</HTML>
