<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/tags/web-remind" prefix="stron"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
	<head>

		<title>任务发送</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
			<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
     <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script language="javascript">
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
		/**
		 * 获取提醒方式
		 * @param 
		 */
		function getRemindValue(){
				var returnValue = "";
				$("#StrRem").find("input:checkbox:checked").each(function(){
					returnValue = returnValue + $(this).val() + ",";
				});
				if(returnValue!=""){
					returnValue = returnValue.substring(0,returnValue.length-1);
				}
				return returnValue;
			}	
		$(document).ready(function(){
				$("#save").click(function(){
					$("#worktaskTitle").val($.trim($("#worktaskTitle").val()));
					if($("#worktaskTitle").val()==""){
						alert("请填写任务标题!");
						$("#worktaskTitle").focus();
						return false;
					}
					if($("#recvNames").val()==""){
						alert("请选择承办者!");
						$("#recvNames").focus();
						return false;
					}
					if($("#worktaskStime").val()==""){
						alert("请选择办理期限开始时间!");
						$("#worktaskStime").focus();
						return false;
					}
					if($("#worktaskEtime").val()==""){
						alert("请选择办理期限结束时间!");
						$("#worktaskEtime").focus();
						return false;
					}
					if($("#worktaskStime").val()!="" && $("#worktaskEtime").val()!="" && $("#worktaskEtime").val()<$("#worktaskStime").val()){
						alert("办理期限的结束时间不能早于开始时间!");
						$("#worktaskEtime").focus();
						return false;
					}
					if($(":radio:checked").length==0){
						alert("请选择紧急程度!");
						$(":radio").focus();
						return false;
					}
					if($("#worktaskType").val()==""){
						alert("请选择任务分类!");
						$("#worktaskType").focus();
						return false;
					}
					$("#worktaskContent").val($.trim($("#worktaskContent").val()));
					if(getDemoLen($("#worktaskContent").val())>1000){
						alert("任务内容不能超过500个汉字!");
						$("#worktaskContent").focus();
						return false;
					}
					var remindType=getRemindValue();
					$("#remindType").val(remindType);
					$("#save").attr({'disabled':true});
					$("#myForm").submit();
					//alert('操作成功');
					//window.location = "<%=root%>/workinspect/worksend/workSend!input.action";
				});
			});
		/**
		 * 发送工作
		 * @param 
		 */
		function dosend(){
				$("#worktaskTitle").val($.trim($("#worktaskTitle").val()));
				if($("#worktaskTitle").val()==""){
					alert("请填写任务标题!");
					$("#worktaskTitle").focus();
					return false;
				}
				if($("#recvNames").val()==""){
						alert("请选择承办者!");
						$("#recvNames").focus();
						return false;
				}
				if($("#worktaskStime").val()==""){
					alert("请选择办理期限开始时间!");
					$("#worktaskStime").focus();
					return false;
				}
				if($("#worktaskEtime").val()==""){
					alert("请选择办理期限结束时间!");
					$("#worktaskEtime").focus();
					return false;
				}
				if($("#worktaskStime").val()!="" && $("#worktaskEtime").val()!="" && $("#worktaskEtime").val()<$("#worktaskStime").val()){
					alert("办理期限的结束时间不能早于开始时间!");
					$("#worktaskEtime").focus();
					return false;
				}
				if($(":radio:checked").length==0){
					alert("请选择紧急程度!");
					$(":radio").focus();
					return false;
				}
				if($("#worktaskType").val()==""){
					alert("请选择任务分类!");
					$("#worktaskType").focus();
					return false;
				}
				$("#worktaskContent").val($.trim($("#worktaskContent").val()));
				if(getDemoLen($("#worktaskContent").val())>1000){
					alert("任务内容不能超过500个汉字!");
					$("#worktaskContent").focus();
					return false;
				}
				var remindType=getRemindValue();
				$("#remindType").val(remindType);
				$("#save").attr({'disabled':true});
				$("#send").attr({'disabled':true});
				$("#myForm").attr("action","<%=root%>/workinspect/worksend/workSend!save.action?drafsend=1");
				document.myForm.submit();
		}

		//选择承办者
		function selectUser(){
			var url = "<%=path%>/workflowDesign/action/userSelect!assignUserList.action";
			window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:500px');
		}
		
		function delAttach(id){
			if(confirm("删除选定的附件，确定？")) { 
			 	if(id!=null&&id!=""){
				 	var delattIds = $("#delAttIds").val();
				 	delattIds += id+",";
				 	$("#"+id).hide();
				 	$("#delAttIds").val(delattIds);
			 	}
			}
		}
		//人员选择界面调用的方法，返回人员信息
		function setSelectedData(selectedData){

			
		   	var returnValue = selectedData.join("|");
		   	//returnValue:返回的人员信息
			//格式:off808081335428e7013354ca2cc00042,省政府|u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042,鹿心社
			//说明:以"|"分割，'off808081335428e7013354ca2cc00042,省政府'表示一条数据  ,off808081335428e7013354ca2cc00042以'o'开头，表示发给部门的
			//u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042以'u'开头，表示发给个人的，'$'后面的表示用户所在部门ID
		  	
		  	//初始化已选人员使用
			document.getElementsByName("handleactor")[0].value = returnValue;
			var temp = "";
			var temp_id = "";
			if(returnValue != ""){
				for(var i = 0; i < selectedData.length; i++){
					if(i>0){
						temp += ",";
						temp_id +=",";
					}
					var name = selectedData[i].split(",")[1];
					var id = selectedData[i].split(",")[0];
					temp += name;
					if(id.indexOf('$')>0){
						var uid = id.split("$")[0];
						temp_id += "4|"+uid.substring(1);
					}else{
						temp_id += "1|"+id.substring(1);
					}
				}				
			}
			$("#recvNames").val(temp);
			$("#recvIds").val(temp_id);
		}
		//人员选择界面调用的方法,初始化已选人员
		function getInitData(){
		   	var returnValue;
		   	//初始化已选人员使用
			returnValue = document.getElementsByName("handleactor")[0].value;
			return (returnValue == null || returnValue == "") ? [] : returnValue.split("|");
		}
		
	</script>
	</head>
	<body>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div class="gd_name"><div class="gd_name_left"><img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;任务发送</div>
		  <br style="clear:both;"/>
		</div>
		<div style="border-top:1px solid #17628c; padding-top:10px;">
			<s:form id="myForm" name="myForm"
				action="/workinspect/worksend/workSend!saveDraft.action"  method="post"
				enctype="multipart/form-data">
				<input id="remindType" name="remindType" type = "hidden" value="${remindType}" />
				<input id="inputFrom" name="inputFrom" type = "hidden" value="${inputFrom}" />
				<input id="worktaskId" name="worktaskId" type = "hidden" value="${model.worktaskId}" />
				<input type="hidden" id="delAttIds" name="delAttIds" value="${delAttIds}">
				<iframe id="hideFrame" name="hideFrame" 
					style="width: 0; height: 0; display: none;"></iframe>
				<table class="gd_fslist" width="900" border="0" cellspacing="0" cellpadding="0">
				  <tr>
				    <td width="120" height="43" align="right"><span class="red">*</span>任务标题：</td>
				    <td colspan="3">
				      <input id="worktaskTitle"	class="input_big_title"										
							name="model.worktaskTitle" type="text"  maxlength="30"
							value="${model.worktaskTitle}"/>
				    	
				    </td>
				   </tr>
				  <tr>
				    <td width="120" height="43" align="right">承办者：</td>
				    <td colspan="3">
					    <input id="recvNames" name="recvNames" type="text" class="input_big_too" readonly="readonly"  value="${recvNames}"/>
					    <input  type="button" class="gd_add" value="" onclick="selectUser();"/>
						<input id="recvIds" name="recvIds" type="hidden" value="${recvIds}" />
						<input id="selectedData" name="selectedData" type="hidden"  value="${selectedData}"/>
						<!-- 初始化已选人员使用 -->
						<input id="handleactor" name="handleactor" type="hidden"  value="${selectedData}"/>
				    </td>
				    </tr>
				  <tr>
				    <td width="120" height="76" align="right">任务内容：</td>
				    <td colspan="3">
				      <textarea id="worktaskContent" name="model.worktaskContent" cols="45" rows="5" class="gd_textarea">${model.worktaskContent}</textarea>
				    </td>
				    </tr>
				  <tr>
				    <td width="120" height="43" align="right">提醒设置：</td>
				    <td colspan="3" id="StrRem">
					    <div style="margin-left:20px;">
					    <stron:remind msgChecked="checked" includeRemind="RTX,SMS" isOnlyRemindInfo="true"
												code="<%=GlobalBaseData.SMSCODE_DCDB %>" />
						</div>
				    </td>
				    </tr>
				  <tr>
				    <td height="43" align="right"><span class="red">*</span>办理期限：</td>
				    <td><strong:newdate name="model.worktaskStime" id="worktaskStime" classtyle="input_md" width="105"
	                      skin="whyGreen" isicon="true" dateobj="${model.worktaskStime}" dateform="yyyy-MM-dd"></strong:newdate>						
						&nbsp;至&nbsp;<strong:newdate name="model.worktaskEtime" id="worktaskEtime" classtyle="input_md" width="105"
	                      skin="whyGreen" isicon="true" dateobj="${model.worktaskEtime}" dateform="yyyy-MM-dd"></strong:newdate></td>
				    <td width="120" align="right">紧急程度：</td>
				    <td><div style="margin-left:20px;">
				    <s:radio id="worktaskEmerlevel" name="model.worktaskEmerlevel" list="#{'0':'普通' , '1':'快速','2':'<font color=red>紧急</font>' }" 
				    listKey="key" listValue="value" value="model.worktaskEmerlevel"/></div></td>
				    </tr>
				    <tr>
				    <td height="43" align="right">任务编号：</td>
				    <td><input id="worktaskNo" name="model.worktaskNo" type="text" class="input_big"  value="${model.worktaskNo}" readonly="readonly"/>
				    </td>
				    <td align="right"><span class="red">*</span>任务分类：</td>
				    <td>
				      <s:select name="model.worktaskType" id="worktaskType" cssClass="select_big" list="workTypeList" 
					      listKey="worktypeName" listValue="worktypeName" headerKey="" headerValue="--请选择--" 
					      value="model.worktaskType">
				      </s:select>
				      </td>
				  </tr>
				  <tr>
				    <td height="43" align="right">发起人：</td>
				    <td><input id="worktaskUserName" name="model.worktaskUserName" type="text" class="input_big" value="${model.worktaskUserName}" readonly="readonly"/></td>
				    <td align="right">发起单位：</td>
				    <td><input id="worktaskUnitName" name="model.worktaskUnitName" type="text" class="input_big" value="${model.worktaskUnitName}" readonly="readonly"/></td>
				  </tr>
				  <tr>
				    <td height="43" align="right">附&nbsp;&nbsp;件：</td>
				    <td colspan="3">
				    	<input type="file" id="file" name="file" size="60" class="input_big_too" />
						<div style="OVERFLOW-y:auto;">
							<s:if
								test="attachList!=null&&attachList.size()>0">
								<s:iterator id="vo" value="attachList">
									<div id="${vo.attachId}">
                                        &nbsp;&nbsp;&nbsp;&nbsp;     <a href="#" onclick="delAttach('${vo.attachId}');"  style='cursor: hand;'>[删除]</a>
                                             <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;'><font color="blue">${vo.attachFileName}</font></a>
										<br>
									
									</div>
								</s:iterator>
							</s:if>
						</div>
					</td>
				   </tr>
				</table>
		<div class="gd_icon"><div class="gd_search_icon">
		  <input id="send" name="send" type="button" class="gd_icon_send" value=" " onclick="dosend();"/>
		 <input id="reset" name="reset" type="reset" class="gd_icon_reslt" value=" " />
		</div>
			</s:form>
		</div>
		</div>
</body>
</html>