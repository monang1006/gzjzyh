<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript' src="<%=path%>/common/js/common/common.js"></script><%--
		<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>

		--%><TITLE>工作日志事务活动列表</TITLE>

		<script type="text/javascript">
		
		function showimg(id,obj){
			if(obj!=null&&obj.length>2&&obj!="[]"){
				return "<img onclick='downLoad(\""+id+"\")' src='<%=root%>/images/ico/yes.gif' style='cursor: hand;'>";
			}else{
				return "";
			}	
		}
		
		function downLoad(id){
			var url="<%=path%>/worklog/workLog!downLoad.action?workLogId="+id;
			OpenWindow(url,300,100,window);
		}
		
		//添加新事务
		function addTask(){
			var url = "<%=path%>/worklog/workLog!input.action";
			var result=window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:565px;help:no;status:no;scroll:no');//   OpenWindow(url,650,500,window);
				if(result=="OK"){
						
						window.location.reload();
						//location="<%=path%>/worklog/workLog.action";
					}
		}
		
		//编辑
		function edit(){
			var id = getValue();
			view(id,"");
		}
		
		//查看
		function view(id,type){
			if(id==null||id==""||id=="null"){
				alert("请选择要编辑的记录。");
			}else if(id.indexOf(",")>0){
				alert("只可以编辑一条记录。");
			}else{
				var url = "<%=path%>/worklog/workLog!input.action?operateType="+type+"&workLogId="+id;
				url += "&timestamp="+new Date();
				var result=window.showModalDialog(url,window,'dialogWidth:850px;dialogHeight:650px;help:no;status:no;scroll:no');//   OpenWindow(url,650,500,window);
				if(result=="OK"){
					window.location.reload();
				}
			}
		}
		
		//删除
		function gotoRemove(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的记录。");
			}else{
				url = "<%=path%>/worklog/workLog!delete.action";
				if(confirm("确定要删除吗？")){
					$.ajax({
						type:"post",
						url:url,
						data:{
							workLogId:id,
							operateType:'list'
						},
						success:function(data){
								if(data!="" && data!=null){
									alert(data);					
								}else{
									submitForm();
								}
							},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				   });
				}
			}
		}
		
		//跳转回视图
		function gotoWorkLogView(){
			location = "<%=path%>/fileNameRedirectAction.action?toPage=worklog/workLog-list.jsp";
		}		
			
		function submitForm(){
			document.getElementById("myTableForm").submit();
		}

		$(document).ready(function(){
				$("#img_sousuo").click(function(){
				
					$("#wlogTitle").val(encodeURI($("#wlogTitleTEXT").val()));
					$("#wlogUserName").val(encodeURI($("#wlogUserNameTEXT").val()));
					
					submitForm();
				});
					
			}); 		

		
		$(document).ready(function(){
			$("#img_sousuo").click(function(){
			
			var st = $("#wlogStartTime").val()
			var se = $("#wlogEndTime").val()
			st = st.replace("-","").replace("-","");
			se = se.replace("-","").replace("-","");
			if((st!=""&&se!="")&&st>se){
				alert("结束日期不能早于开始日期。");
				return ;
			}
		}); 
		});
		
		
		
		</script>
	</HEAD>
	<s:if test="operateType==\"all\"">
		<BODY class=contentbodymargin oncontextmenu="return false;">
	</s:if>
	<s:else>
		<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
	</s:else>
	<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
<!--	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>-->
	<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									
											<table width="100%" border="0" cellspacing="0" cellpadding="00">
						                       <tr>
							                    <td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >
							                    </td>
							                     <td align="left">
											<s:if test="operateType==\"all\"">
													<strong>日志查询列表</strong>
												</s:if>
											<s:else>
													<strong>个人日志列表</strong>
												</s:else>
										   </td>
										   
										
											<s:if test="operateType==\"all\"">
											<td width="70%">
											<%--<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="view();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
							                  	</tr>
								            </table>
											--%></td>
											
											</s:if>
											<s:else>
												<%--<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td>
															<security:authorize ifAllGranted="001-0001001000010001">
																<a class="Operation" href="javascript:addTask();"><img
																		src="<%=root%>/images/ico/tb-add.gif" width="15" height="15" class="img_s">新建&nbsp;</a>

															</security:authorize>
														</td>
														<td width="5"></td>
														<td>
															<security:authorize ifAllGranted="001-0001001000010002">
																<a class="Operation" href="javascript:edit();"><img
																		src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">修改&nbsp;</a>
															</security:authorize>
														</td>
														<td width="5"></td>
														<td>
															<security:authorize ifAllGranted="001-0001001000010003">
																<a class="Operation" href="javascript:gotoRemove();"><img
																		src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">删除&nbsp;</a>
															</security:authorize>
														</td>
														<td width="5"></td>
														<td>
															<a class="Operation" href="javascript:gotoWorkLogView();"><img
																	src="<%=root%>/images/ico/tb-list16.gif" width="15" height="15" class="img_s">视图&nbsp;</a>
														</td>
														<td width="5"></td>
													</tr>
												</table>
		                                    --%>
		                                    
		                                    
		                                    <td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="addTask();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								                	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="edit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                 	
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoRemove();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
				                  		            
								                 	<%--<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoWorkLogView();"><img src="<%=root%>/images/operationbtn/Statistics_view.png"/>&nbsp;视&nbsp;图&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                --%></tr>
								            </table>
											</td>
											</s:else>
										
									</tr>
									
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<s:form id="myTableForm" action="/worklog/workLog.action" method="get">
						<input type="hidden" id="workLogId" name="workLogId" value="${model.workLogId}">
						<input type="hidden" id="operateType" name="operateType" value="${operateType}">
						<input type="hidden" name="wlogTitle" id="wlogTitle" value="${wlogTitle}" />
						<input type="hidden" name="wlogUserName" id="wlogUserName" value="${wlogUserName}" />
						<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="0"
							isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
								<tr>
									<%--<td width="40px" align="center" class="biao_bg1">
										<img id="img_sousuo" style="cursor: hand;"
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="17" height="16" />
									</td>
									--%>
								
									<s:if test="operateType==\"all\"" >
										
										<td >
											<%--<input id="wlogUserNameTEXT" name="wlogUserNameTEXT" type="text" style="width: 100%"
												class="search" title="请输入姓名" value="${wlogUserName}">
												--%>
											<div style="float: left;">&nbsp;&nbsp;姓名：&nbsp;<input name="wlogUserNameTEXT" id="wlogUserNameTEXT"  type="text" class="search" title="请您输入接收人姓名" value="${model.wlogUserName }">
										    </div>
										<%--<input id="wlogTitleTEXT" name="wlogTitleTEXT" type="text" style="width: 100%"
											class="search" title="请输入文档标题" value="${wlogTitle}">
											--%>
											<div style="float: left;">&nbsp;&nbsp;标题：&nbsp;<input name="wlogTitleTEXT" id="wlogTitleTEXT" type="text" class="search"  title="请您输入文档标题" value="${model.wlogTitle }">
									        </div>
									       <div style="float: left;"> &nbsp;&nbsp;开始日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogStartTime" id="wlogStartTime" dateobj="${model.wlogStartTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/>
									        </div>
									        <div style="float:left;width:300px;">&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogEndTime" id="wlogEndTime" dateobj="${model.wlogEndTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期"/>
									        &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
									        </div>
									</td>
									
									</s:if>
									
									<%--<s:if test="operateType==\"all\"">
										<td >
											<strong:newdate id="wlogStartTime" name="model.wlogStartTime" dateform="yyyy-MM-dd"
												width="100%" skin="whyGreen" isicon="true" dateobj="${model.wlogStartTime}"
												classtyle="search" title="请输入起始日期" />
												
											&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogStartTime" id="wlogStartTime" dateobj="${model.wlogStartTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/>
												
										</td>
										<td align="center" class="biao_bg1">
											<strong:newdate id="wlogEndTime" name="model.wlogEndTime" dateform="yyyy-MM-dd" 
												width="100%" skin="whyGreen" isicon="true" dateobj="${model.wlogEndTime}"
												classtyle="search" title="请输入结束日期" />
												
                                             &nbsp;&nbsp;结束日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogEndTime" id="wlogEndTime" dateobj="${model.wlogEndTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期"/>
										</td>
									</s:if>
									--%><s:else>
										<td  >
											<%--<strong:newdate id="wlogStartTime" name="model.wlogStartTime" dateform="yyyy-MM-dd"
												width="100%" skin="whyGreen" isicon="true" dateobj="${model.wlogStartTime}"
												classtyle="search" title="请输入起始日期" />
										--%>
										    <div style="float:left;">&nbsp;&nbsp;标题：&nbsp;<input name="wlogTitleTEXT" id="wlogTitleTEXT" type="text" class="search"  title="请您输入文档标题" value="${model.wlogTitle }">
									        </div>
									        <div style="float:left;">&nbsp;&nbsp;开始日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogStartTime" id="wlogStartTime" dateobj="${model.wlogStartTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期"/>
									        </div>
									        <div style="float:left;width:300px;">&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.wlogEndTime" id="wlogEndTime" dateobj="${model.wlogEndTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期"/>
										    &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										    </div>
										</td>
									</s:else>
									
									
							
								</tr>
							</table>

							<webflex:flexCheckBoxCol caption="选择" property="workLogId" showValue="wlogTitle" width="5%"
								isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<s:if test="operateType==\"all\"">
								<webflex:flexTextCol caption="姓名" property="wlogUserName" showValue="wlogUserName"
									width="15%" isCanDrag="true" showsize="50" isCanSort="true"></webflex:flexTextCol>
							</s:if>
							<webflex:flexTextCol caption="标题" property="workLogId" showValue="wlogTitle" width="45%"
								isCanDrag="true" showsize="45"  onclick="view(this.value,'view')" isCanSort="true"></webflex:flexTextCol>

							<%--<webflex:flexDateCol caption="结束日期" showsize="18" width="20%"
									isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"
									property="wlogEndTime" showValue="wlogEndTime"></webflex:flexDateCol>
								--%>
							<s:if test="operateType==\"all\"">
								<webflex:flexDateCol caption="时间" showsize="18" property="wlogStartTime"
									showValue="wlogStartTime" width="25%" isCanDrag="true" isCanSort="true"
									dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
								<webflex:flexTextCol caption="附件" property="toaWorkLogAttaches"
									showValue="javascript:showimg(workLogId,toaWorkLogAttaches)" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</s:if>
							<s:else>
								<webflex:flexDateCol caption="时间" showsize="18" property="wlogStartTime"
									showValue="wlogStartTime" width="25%" isCanDrag="true" isCanSort="true"
									dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
								<webflex:flexTextCol caption="附件" property="workLogId"
									showValue="javascript:showimg(workLogId,toaWorkLogAttaches)" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</s:else>
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
			<security:authorize ifAllGranted="001-0001001000010001">			
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTask",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			</security:authorize>
			<security:authorize ifAllGranted="001-0001001000010002">	
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			</security:authorize>
			<security:authorize ifAllGranted="001-0001001000010003">	
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			</security:authorize>
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
	</BODY>
</HTML>
