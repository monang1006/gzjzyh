<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<base target="_self">
		<title>办理纪要</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
			<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script language="javascript">
		var numtest = /^\d+$/;
		function getDemoLen(demo){
			var demoLen=demo.length;
			var demoLen2=0;
		    for (var i=0;i<demoLen;i++) {
		        if ((demo.charCodeAt(i) < 0) || (demo.charCodeAt(i) > 255))
		            demoLen2 +=2;
		        else
		            demoLen2 += 1;
		    }
		    return demoLen2;
		}
		
		$(document).ready(function(){
				$("#btn_save").click(function(){					
					$("#managetDemo").val($.trim($("#managetDemo").val()));
					if($("#managetDemo").val()==""){
						alert("请填写纪要内容!");
						$("#managetDemo").focus();
						return false;
					}
					if(getDemoLen($("#managetDemo").val())>1000){
						alert("纪要内容不能超过500个汉字!");
						$("#managetDemo").focus();
						return false;
					}
					if($("#managetProgress").val()==""){
						alert("请填写工作进度!");
						$("#managetProgress").focus();
						return false;
					}
					if(!numtest.test($("#managetProgress").val())){
			        	alert('工作进度必须为数字！');
			        	$("#managetProgress").focus();
			        	return false;
			        }
					if($("#managetProgress").val()>100){
			        	alert('工作进度输入值必须在0-100之间！');
			        	$("#managetProgress").focus();
			        	return false;
			        }
					if($(":radio:checked").length==0){
						alert("请选择工作状态!");
						$(":radio").focus();
						return false;
					}
					if(($(":radio:checked").val()=="1"&&$("#managetProgress").val()!="100")||
						($(":radio:checked").val()!="1"&&$("#managetProgress").val()=="100")){
						alert("工作状态与工作进度不一致!");
						$(":radio").focus();
						return false;
					}
					$("#btn_save").attr({'disabled':true});
					$("#myForm").submit();
				});
			});
			function showSummary(){
				$("#summary").css("display","block");
				$("#div1").css("display","none");
			}
			function delAttach(id){
				if(confirm("删除选定的附件，确定？")) { 
			 	if(id!=null&&id!=""){
				 	var delattIds = $("#delAttIds").val();
				 	delattIds += id+",";
				 	$("#summAtt").hide();
				 	$("#delAttIds").val(delattIds);
			 	}
		}
			}
			function getState(){
				if($(":radio:checked").val()=="1"){
					$("#managetProgress").val("100");
				}
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
				<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="19%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												任务信息
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="85%">
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务标题：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.worktaskTitle}
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">承办者：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.worktaskSender}
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">办理期限：</span>
									</td>
									<td width="32%" class="td1" align="left">
										${model.worktaskStime} 至 ${model.worktaskEtime}
									</td>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">紧急程度：</span>
									</td>
									<td width="30%" class="td1" align="left">
										<c:if test="${model.worktaskEmerlevel == '0' }">普通</c:if>
										<c:if test="${model.worktaskEmerlevel == '1' }">快速</c:if>
										<c:if test="${model.worktaskEmerlevel == '2' }"><font color="red">紧急</font></c:if>
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务编号：</span>
									</td>
									<td width="32%" class="td1" align="left">
										${model.worktaskNo}
									</td>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务分类：</span>
									</td>
									<td width="30%" class="td1" align="left">
										${model.worktaskType}
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起人：</span>
									</td>
									<td width="32%" class="td1" align="left">
										${model.worktaskUserName}
									</td>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起单位：</span>
									</td>
									<td width="30%" class="td1" align="left">
										${model.worktaskUnitName}
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务内容：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="orgDescription" name="model.orgDescription"
											rows="5" cols="68" readonly>${model.worktaskContent}</textarea>
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<div style="OVERFLOW-y:auto;HEIGHT:100px;">
										<s:if
											test="workattachList!=null&&workattachList.size()>0">
											<s:iterator id="vo" value="workattachList">
												<div id="${vo.attachId}">
		                                              <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;color:#03C;'>${vo.attachFileName}</a>
													<br>
												
												</div>
											</s:iterator>
										</s:if>
									</div>
									</td>
								</tr>
								<tr>
									<td height="21" colspan="4" class="biao_bg1" align="center">
										 办理纪要
									</td>
								</tr>
								<s:if test="summaryList!=null&&summaryList.size()>0">
									<s:iterator id="sumvo" value="summaryList">
										<tr>
											<td width="19%" height="21" rowspan="2" class="biao_bg1" align="center">
												处理人:${sumvo.TOsWorktaskSend.taskRecvName}<br/><br/>
												进度:(${sumvo.managetProgress}%)<br/><br/>
												时间:<s:date name="#sumvo.managetTime" format="yyyy-MM-dd"/><br/>
											</td>
											<td class="td1" colspan="3" align="left">
												${sumvo.managetDemo}
											</td>
										</tr>
										<tr>
											<td class="td1" colspan="3" align="left">
												<div style="OVERFLOW-y:auto;HEIGHT:100px;">
													<s:if
														test="#sumvo.attachList!=null&&#sumvo.attachList.size()>0">
														<s:iterator id="attach" value="#sumvo.attachList">
															<div id="${attach.attachId}">			                                              
					                                              <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;color:#03C;'>${attach.attachFileName}</a>
																<br>													
															</div>
														</s:iterator>
													</s:if>
												</div>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:if test="model.managetDemo!=null">
								<tr>
									<td width="19%" height="21" rowspan="2" class="biao_bg1" align="center">
										处理人:${model.worktaskUserName}<br/><br/>
										进度:(${model.managetProgress}%)<br/><br/>
										时间:<s:date name="model.managetTime" format="yyyy-MM-dd"/><br/>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.managetDemo}
									</td>
								</tr>
								<tr>
									<td class="td1" colspan="3" align="left">
										<div style="OVERFLOW-y:auto;HEIGHT:100px;">
											<s:if
												test="attachList!=null&&attachList.size()>0">
												<s:iterator id="sendattach" value="attachList">
													<div id="${sendattach.attachId}">			                                              
			                                              <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;color:#03C;'>${sendattach.attachFileName}</a>
														<br>													
													</div>
												</s:iterator>
											</s:if>
										</div>
									</td>
								</tr>
								</s:if>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<div id="div1" style="display: block;">
						 <table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<input name="btn_showSummary" type="button" class="input_bg"
													value="填写办理纪要" onclick="javascript:showSummary();">
								</td>
							</tr>
						</table>
						</div>
						<div id="summary" style="display: none;">
						<s:form id="myForm"
							action="/workinspect/worksend/workSend!saveTaskSummary.action" theme="simple"
							enctype="multipart/form-data" target="hideFrame">
							<iframe id="hideFrame" name="hideFrame"
								style="width: 0; height: 0; display: none;"></iframe>
							<s:hidden name="model.worktaskId"></s:hidden>
							<input type="hidden" id="delAttIds" name="delAttIds" value="${delAttIds}">
						<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td height="21" colspan="4" class="biao_bg1" align="center">
										 发表或回复意见
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">内容(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="managetDemo" name="model.managetDemo"
											rows="5" cols="68">${model.managetDemo}</textarea>
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">工作进度(%)(<font color="red">*</font>)：</span>
									</td>
									<td width="32%" class="td1" align="left">
										<input id="managetProgress"											
											name="model.managetProgress" type="text" size="5" maxlength="3"
											value="${model.managetProgress}">%
									</td>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">工作状态(<font color="red">*</font>)：</span>
									</td>
									<td width="30%" class="td1" align="left">
										<s:radio id="managetState" name="model.managetState" list="#{'1':'完成' , '0':'处理中' }" listKey="key" listValue="value" onclick="getState();" value="0"/>
									</td>
								</tr>
								<tr>
									<td width="19%" height="21" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="file" id="file" name="file" size="60" class="multi" />
										<div style="OVERFLOW-y:auto;HEIGHT:100px;">
											<s:if
												test="attachList!=null&&attachList.size()>0">
												<s:iterator id="vo" value="attachList">
													<div id="summAtt">
			                                              <a  href="#" onclick="delAttach('${vo.attachId}');"  style='cursor: hand;'>[删除]</a>
			                                              <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;'>${vo.attachFileName}</a>
														<br>
													
													</div>
												</s:iterator>
											</s:if>
										</div>
									</td>
								</tr>
							</table>
							 <table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="34%">
												<input id="btn_save" type="submit" class="input_bg"
													value="提 交">
											</td>
											<td width="33%">
												<input id="btn_reset" type="reset" class="input_bg"
													value="重 写">
											</td>
											<td width="33%">
												<input id="btn_close" type="button" class="input_bg"
													value="关闭" onclick="javascript:window.close();">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</s:form>
						</div>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>