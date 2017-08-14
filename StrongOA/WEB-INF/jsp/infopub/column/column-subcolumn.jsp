<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存栏目</title>
		<LINK href="<%=frameroot%>/css/properties_windows_special.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<SCRIPT type="text/javascript">
		function validatesubmit(){
			var form=document.getElementById("columnForm");
			var columnname=$("#clumnName").val();
			columnname=columnname.replace(/(^\s*)|(\s*$)/g,"");
		    if(columnname==null||columnname=="")
			 {
			    alert("栏目名称不可以为空！");
			    return  ;
			 }
			 if(columnname=="<\%%>"){
			 alert("栏目名称不可以输入“<\%%>”！");
			    return  ;
			 }
			form.submit();
		}
		function cancel(){
			parent.location.reload();
		}
		//新建子栏目
		function newSubColumn(){
			var folderId = '${clumnId}';
			parent.project_work_content.document.location="<%=root%>/infopub/column/column!newSubColumn.action?clumnId="+folderId;
		}
		//新建顶级栏目
		function newColumn(){
			parent.project_work_content.document.location="<%=root%>/infopub/column/column!newSubColumn.action";
		}
		
		function isShowChoose(){
			//alert(($("#clumnIsprivate"):checked).val())
			if($("input[name=model\.clumnIsprivate]:checked").val()=='0'){
				$("#user").css("display","");
				$("#chakan").text("查看权限：");
				$("#user1").css("display","");
				
			}else{
				$("#user").css("display","none");
				$("#chakan").text("");
				$("#user1").css("display","none");
			} 
		}
		
		function isShowChoose1(){
			//alert(($("#clumnIsprivate"):checked).val())
			if($("input[name=isadmin]:checked").val()=='0'){
				$("#user1").css("display","");
				
			}else{
				$("#user1").css("display","none");
			} 
		}
		
		$(document).ready(function(){
			var userName='<%=session.getAttribute("userName")%>';
			var userId=$("#userid").val();
			if($("#orgusername1").val()==""){
				$("#orgusername1").val(userName);
				$("#orguserid1").val(userId);
			}
			if($("input[name=model\.clumnIsprivate]:checked").val()=='0'){
				//var clumnId = $("#clumnId").val();
				//alert(clumnId);
			}else{
	        	$("#user").css("display","none");
	        	$("#user1").css("display","none");
	        }
	        
	        //共享文件时选择接收人
			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空接收人
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
	        
      	}); 
		
		
		//选择人员
		function addPerson1(){
<%--			var ret=OpenWindow("<%=root%>/infopub/column/column!persontree.action","400","350",window);--%>
var ret=OpenWindow("<%=root%>/address/addressOrg!tree.action?elementId=orguserid1&elementName=orgusername1" +
		"&selectId=orguserid&selectName=orgusername","600","400",window);
		}
		function clearPerson1(){
			$("#orgusername1").val("");
			$("#orguserid1").val("");
		}
		
		
		setPageListenerEnabled(true);
	</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="columnForm" theme="simple"
							action="<%=root%>/infopub/column/column!save.action" method="POST">
								<input type="hidden" name="userid" id="userid" value="${userid }"/>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<s:if test="null==clumnViewName">
														<strong>新建顶级栏目</strong> 
													</s:if>
													<s:else>
														<strong><s:text name="clumnViewName"/></strong> &nbsp; 栏目信息
													</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="validatesubmit();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="newColumn();">&nbsp;新&nbsp;建&nbsp;顶&nbsp;级&nbsp;栏&nbsp;目&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="clumnParent" name="model.clumnParent" type="hidden"
								size="32" value="${model.clumnId}">												
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">所属栏目：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="null==clumnViewName">
											顶级栏目
										</s:if>
										<s:else>
											<s:text name="clumnViewName"></s:text>
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;栏目名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnName" name="model.clumnName" style="border:1px solid #b3bcc3;background-color:#ffffff;" type="text"  size="32" value="${model.clumnName}" class="required" maxlength="20"> 
									</td>
								</tr>
								<!-- 
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目英文名(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnDir" name="model.clumnDir" type="text" size="32" value="${model.clumnDir}" maxlength="30">
									</td>
								</tr>
								 -->
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目提示：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnTips" name="model.clumnTips" style="border:1px solid #b3bcc3;background-color:#ffffff;" type="text" size="32" value="${model.clumnTips}" maxlength="30">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目说明：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnMemo" name="model.clumnMemo" style="border:1px solid #b3bcc3;background-color:#ffffff;" type="text" size="32" value="${model.clumnMemo}" maxlength="30">
									</td>
								</tr>
								<!-- 
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">打开方式：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:radio id="clumnIsnewopen" name="model.clumnIsnewopen" list="#{'0':'是原窗口打开','1':'在新窗口打开'}" onselect="0"/>
									</td>
								</tr>
								<tr>
									<td width="20%" align="right" height="21" class="biao_bg1">
										<span class="wz">是否启用RSS功能: </span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:radio id="clumnIsuserss" name="model.clumnIsuserss" list="#{'0':'是','1':'否'}" onselect="0"/>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目是否生成html：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:radio id="clumnIsarticle" name="model.clumnIsarticle" list="#{'0':'是','1':'否'}" onchange="notifyChange(true);" onselect="0"/>
									</td>
								</tr>
								 -->
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否加权：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<span  id="chakan" class="wz"></span>
										<s:radio id="clumnIsprivate" name="model.clumnIsprivate" list="#{'0':'是','1':'否'}" onclick="isShowChoose();" onchange="notifyChange(true);" onselect="0"/>
										<div id="user">
											<table>
											<td>
											<s:textarea cols="30" id="orgusername" name="userName" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid" name="userId" value="<s:property value='userId'/>" />
											</td>
											<td><a class="button" id="addPerson" url="<%=root%>/address/addressOrg!tree.action?selectId=orguserid1&selectName=orgusername1" href="#">添加用户</a></td>
											<td></td>
											<td><a class="button" id="clearPerson" href="#">清空</a></td>
											</table>
											
											
											
											<br><span class="wz">管理权限：</span>
											<font color="#ADADAD">如果不选则默认只有当前操作用户有权限管理</font>
											<table>
											<div id="user1">
											<td>
											<s:textarea cols="30" id="orgusername1" name="userName" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid1" name="adminUser" value="${adminUser }" />
											</td>
											<td><a class="button" href="JavaScript:addPerson1();">添加用户</a></td>
											<td></td>
											<td><a class="button" href="JavaScript:clearPerson1();">清空</a></td>
											</div>
											</table>
											
											
											
										</div>
									</td>
								</tr>
<%--								<tr>--%>
<%--									<td width="20%" height="21" class="biao_bg1" align="right">--%>
<%--										<span class="wz">流程选项：</span>--%>
<%--									</td>--%>
<%--									<td class="td1" colspan="3" align="left">--%>
<%--										<s:select list="processList" headerKey="" headerValue=" 请选择流程" id="processName" name="model.processName" onchange="notifyChange(true);" />--%>
<%--									</td>--%>
<%--								</tr>--%>
								<!--
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">文章打开是否新窗口：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:radio id="clumnIsarticlenewwin" name="model.clumnIsarticlenewwin" list="#{'0':'是','1':'否'}" onchange="notifyChange(true);" onselect="0"/>
									</td>
								</tr>
								-->
							
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							<br>
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
