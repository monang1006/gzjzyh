<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>班次管理</TITLE>
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
		<SCRIPT type="text/javascript">
			function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=="[]")
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;' title='该班次是否已设置轮班规则'>";
			}	
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center style="BORDER-TOP:0px">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
												&nbsp;
											</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												【${scheGroup.groupName}】的班次信息列表
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
																	<td width="45" align="right"><%--
																		<a class="Operation" href="javascript:add();"> <img
																				src="<%=root%>/images/ico/tianjia.gif" width="15"
																				height="15" class="img_s">新增</a>
																	    --%>
																	    <input type="button" class="input_bg" value="新 增"
																				onclick="add()">
																	</td>
																	<td width="5"></td>
																	<td width="45" align="right">
																		<%--<a class="Operation" href="javascript:edit();"> <img
																				src="<%=root%>/images/ico/bianji.gif" width="15"
																				height="15" class="img_s">修改</a>
																		   --%><input type="button" class="input_bg" value="修 改"
																				onclick="edit()">
																	</td>
																	<td width="5"></td>
																	<td width="45" align="right">
																		<%--<a class="Operation" href="javascript:del();"> <img
																				src="<%=root%>/images/ico/shanchu.gif" width="15"
																				height="15" class="img_s">删除</a>
																		--%><input type="button" class="input_bg" value="删 除"
																				onclick="del()">
																	</td>
																	<td width="5"></td>
																	<td width="80" align="right">
																		<%--<a class="Operation" href="javascript:setReg();">
																			<img src="<%=root%>/images/ico/set_config.gif"
																				width="15" height="15" class="img_s">设置轮班规则</a>
																			--%><input type="button" class="input_bg" value="设置轮班规则"
																				onclick="setReg()">
																	</td>
																	<td width="5"></td>
																	<%--<td width="58" align="right">
																		<a class="Operation" href="javascript:goback();">
																			<img src="<%=root%>/images/ico/fankui.gif"
																				width="15" height="15" class="img_s">返回</a>
																	</td>
																	<td width="5"></td>
																--%></tr>
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
							<s:form id="myTableForm"
								action="/attendance/arrange/schedules.action">
								<input id="groupId" type="hidden" name="groupId"
									value="${groupId }" />
								<input id="schedulesId" type="hidden" name="schedulesId"
									value="${schedulesId }" />
								<!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="groupId" isCanDrag="true"
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
									getValueType="getValueByProperty" collection="${page.result}"
									page="${page}">
									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img src="<%=root%>/images/ico/sousuo.gif" width="17"
													id="img_sousuo" height="16" style="cursor: hand;"
													title="单击搜索">
											</td>
											<td width="18%" align="center" class="biao_bg1">
												<input type="text" name="model.schedulesName"
													value="${model.schedulesName}" class="search" title="请输入班次名称" />
											</td>
											<%--<td width="14%" align="center" class="biao_bg1">
												<input type="text" name="scheGroup.groupName"
													value="${scheGroup.groupName}"  style="width:100%" readonly="readonly" />
											</td>
											--%><td width="18%" align="center" class="biao_bg1">
												<strong:newdate id="workStime" name="model.workStime"
													dateform="HH:mm" isicon="true" width="100%"
													nowvalue="${model.workStime}" classtyle="search"
													title="请选择上班时间" />
											</td>
											<td width="18%" align="center" class="biao_bg1">
												<strong:newdate id="workEtime" name="model.workEtime"
													dateform="HH:mm" isicon="true" width="100%"
													nowvalue="${model.workEtime}" classtyle="search"
													title="请选择下班时间" />
											</td>
											<td width="44%" colspan="3" align="center" class="biao_bg1">
												<input name="" type="text" style="width:100%" readonly="readonly">
											</td>
											<td class="biao_bg1">
												&nbsp;
											</td>
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" property="schedulesId"
										showValue="schedulesName" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="班次名称" property="schedulesId"
										showValue="schedulesName" onclick="viewInfo(this.value);" width="18%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<%--<webflex:flexTextCol caption="班组名称" property="toaAttendSchedGroup.groupName"
										showValue="toaAttendSchedGroup.groupName" width="14%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									--%><webflex:flexTextCol caption="上班时间" property="workStime"
										showValue="workStime" width="18%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="下班时间" property="workEtime"
										showValue="workEtime" width="18%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>		   
									<webflex:flexNumricCol caption="迟到（分）" property="laterTime"
										showValue="laterTime" width="11%" isCanDrag="true"
										isCanSort="true"></webflex:flexNumricCol>
									<webflex:flexNumricCol caption="早退（分）" property="earlyTime"
										showValue="earlyTime" width="11%"
										isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
									<webflex:flexNumricCol caption="旷工（分）" property="skipTime"
										showValue="skipTime" width="11%"
										isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
									<webflex:flexTextCol caption="轮班规则" property="schedulesId"
										showValue="javascript:showimg(toaAttendRegulations)" onclick="viewReg(this.value)" width="11%"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
				item = new MenuItem("<%=root%>/images/ico/tianjia.gif","新增","add",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/bianji.gif","修改","edit",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/set_config.gif","设置轮班规则","setReg",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				//item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回","goback",1,"ChangeWidthTable","checkOneDis");
				//sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		
		function add(){
		    var groupId=document.getElementById("groupId").value;
		 	OpenWindow("<%=path%>/attendance/arrange/schedules!input.action?groupId="+groupId,390,300,window);	
		}
		
		function edit(){
			var id=getValue();
			var groupId=document.getElementById("groupId").value;
			if(id==""){
				alert("请选择需编辑的班次信息！");
			}else if(id.indexOf(",")!=-1){
				alert("只能选择一条班次信息！");
			}else{
				OpenWindow("<%=path%>/attendance/arrange/schedules!input.action?schedulesId="+id+"&groupId="+groupId,390,300,window);	
			    <%--$.ajax({
					type:"post",
					url:"<%=path%>/attendance/arrange/schedules!isEffective.action",
					data:{
						groupId:groupId			
					},
					success:function(data){
						if(data == "0"){
							 if(confirm("该班次已生效，修改可能会影响月末的考勤统计，是否继续？")){
							 	OpenWindow("<%=path%>/attendance/arrange/schedules!input.action?schedulesId="+id+"&groupId="+groupId,390,300,window);	
							 }
						}else{
							OpenWindow("<%=path%>/attendance/arrange/schedules!input.action?schedulesId="+id+"&groupId="+groupId,390,300,window);	
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				});--%>
			}
		}
		
		function del(){
			var id=getValue();
			var groupId=document.getElementById("groupId").value;
			if(id==""){
				alert("请选择要删除的班次信息！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条班次信息！");
			}else{
			    if(confirm("确定要删除吗？")){
			    	location="<%=path%>/attendance/arrange/schedules!delete.action?schedulesId="+id+"&groupId="+groupId;	
			    	<%--$.ajax({
						type:"post",
						url:"<%=path%>/attendance/arrange/schedules!isEffective.action",
						data:{
							groupId:groupId			
						},
						success:function(data){
							if(data == "0"){
								 if(confirm("该班次已生效，删除可能会影响月末的考勤统计，是否继续？")){
								 	location="<%=path%>/attendance/arrange/schedules!delete.action?schedulesId="+id+"&groupId="+groupId;		
								 }
							}else{
								location="<%=path%>/attendance/arrange/schedules!delete.action?schedulesId="+id+"&groupId="+groupId;	
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});--%>
			    }	
			}
		}
		
		function setReg(){
			var id=getValue();
			if(id==""){
				alert("请选择要删除的班次信息！");
			}else if(id.indexOf(",")!=-1){
				alert("有且只能选择一条班次信息！");
			}else{
			    OpenWindow("<%=path%>/attendance/arrange/reg!input.action?scheId="+id,345,300,window);	
			}
		}
		
		
		function viewReg(id){
			  OpenWindow("<%=path%>/attendance/arrange/reg!input.action?scheId="+id,345,300,window);	
		}
		
		function submitForm(){
			document.getElementById("myTableForm").submit();
		}
		
		function goback(){
			window.location="<%=path%>/attendance/arrange/scheGroup.action";
		}
		
		function viewInfo(id){
			var groupId=document.getElementById("groupId").value;
			OpenWindow("<%=path%>/attendance/arrange/schedules!view.action?schedulesId="+id+"&groupId="+groupId,390,285,window);	
			
		}
		
		 $(document).ready(function(){
		    $("#img_sousuo").click(function(){
		    	$("Form").submit();
		    });     
		  });
      
	</script>
	</BODY>
</HTML>
