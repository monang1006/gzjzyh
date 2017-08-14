<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存栏目</title>
		<LINK href="<%=frameroot%>/css/properties_windows_special.css"
			type=text/css rel=stylesheet>
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
			    alert("栏目名称不可以为空。");
			    return  ;
			 }
			 if(columnname=="<\%%>"){
			 alert("栏目名称不可以输入“<\%%>”。");
			    return  ;
			 }
			 
<%--			if($("input[name=model.clumnIsprivate]:checked").val()=='0'){--%>
<%--				if($("#orgusername").val()=='' || $("#orgusername").val()==null){--%>
<%--					alert("请添加用户。");--%>
<%--					return false;--%>
<%--				}--%>
<%--			}--%>
			
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
			if($("input[name=isadmin]:checked").val()=='0'){
				$("#user1").css("display","");
				
			}else{
				$("#user1").css("display","none");
			} 
			
		}
		function isadminU(){
		  var temp= $("#orgusername1").text();
		  if(temp!=null&&temp!="")
		  {
		   $("#rdyes").attr("checked",true);
		  }
		}
		
		$(document).ready(function(){
			var userName='<%=session.getAttribute("userName")%>';
			var userId=$("#userid").val();
			if($("#orgusername1").val()==""){
				$("#orgusername1").val(userName);
				$("#orguserid1").val(userId);
			}
				
			if($("input[name=model.clumnIsprivate]:checked").val()=='0'){
				//var clumnId = $("#clumnId").val();
				//alert(clumnId);
				$("#chakan").text("查看权限：");
				isadminU();
			}else{
	        	$("#user").css("display","none");
	        	$("#user1").css("display","none");
	        	$("#chakan").text("");
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
			//var ret=OpenWindow("<%=root%>/infopub/column/column!persontree.action","400","350",window);
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
									<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<s:if test="null==clumnViewName">
										<strong>新建顶级栏目 </strong>
								</s:if>
								<s:else>
										<strong><s:text name="clumnViewName"/> &nbsp;</strong>
								</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            <s:if test="#request.viewPrivate==\"0\""></s:if>
						            <s:else>
						            	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="validatesubmit();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="newSubColumn();">&nbsp;新&nbsp;建&nbsp;子&nbsp;栏&nbsp;目</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="newColumn();">&nbsp;新&nbsp;建&nbsp;顶&nbsp;级&nbsp;栏&nbsp;目</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
						            </s:else>
						            </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
								</tr>
							</table>
							<input id="clumnId" name="model.clumnId" type="hidden"
								size="32" value="${model.clumnId}">	
							<input id="clumnParent" name="model.clumnParent" type="hidden"
								size="32" value="${model.clumnParent}">										
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">所属栏目：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="null==parentClumnName">
											顶级栏目
										</s:if>
										<s:else>
											<s:text name="parentClumnName"></s:text>
										</s:else>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="wz">本栏目ID：</span>${model.clumnId}
									</td>
								</tr>
								
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;栏目名称：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnName" name="model.clumnName" style="background-color:#ffffff;border:1px solid #b3bcc3;" type="text" size="32" value="${model.clumnName}" class="required" maxlength="20">
									</td>
								</tr>
								<!-- 
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目英文名(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnDir" name="model.clumnDir" type="text" size="32" value="${model.clumnDir}" class="required" maxlength="30">
									</td>
								</tr>
								 -->
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">栏目提示：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnTips" style="background-color:#ffffff;border:1px solid #b3bcc3;" name="model.clumnTips" type="text" size="32" value="${model.clumnTips}" maxlength="30">
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">栏目说明：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnMemo" style="background-color:#ffffff;border:1px solid #b3bcc3;" name="model.clumnMemo" type="text" size="32" value="${model.clumnMemo}" maxlength="30">
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
									<td class="biao_bg1" align="right">
										<span class="wz">是否加权：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<span  id="chakan" class="wz"></span>
										<s:radio id="clumnIsprivate" name="model.clumnIsprivate" list="#{'0':'是','1':'否'}" onclick="isShowChoose();" onchange="notifyChange(true);" onselect="0"/>
										<div id="user">
											<s:textarea cols="30" id="orgusername" name="userName" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid" name="userId" value="<s:property value='userId'/>" />
											<a id="addPerson"  href="#" class="button" url="<%=root%>/address/addressOrg!tree.action?selectId=orguserid1&selectName=orgusername1" href="#">添加用户</a>&nbsp;<a id="clearPerson" href="#" class="button">清空</a>
											
											
											
											
											
											
											<br><span class="wz">&nbsp;管理权限：</span>
											<font color="gray">(如果不选则默认只有当前操作用户有权限管理)</font>
											<div id="user1">
											<s:textarea cols="30" id="orgusername1" name="adminUserName" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid1" name="adminUser" value="${adminUser }" />
											<a href="JavaScript:addPerson1();" class="button" >添加用户</a>&nbsp;<a href="JavaScript:clearPerson1();" class="button">清空</a>
											</div>
										</div>
									</td>
								</tr>
<%--								<tr>--%>
<%--									<td  class="biao_bg1" align="right">--%>
<%--										<span class="wz">流程选项：&nbsp;</span>--%>
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
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
