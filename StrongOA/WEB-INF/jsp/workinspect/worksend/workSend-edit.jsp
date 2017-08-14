<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>

		<title>任务信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
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
				$("#save").click(function(){
					$("#worktaskTitle").val($.trim($("#worktaskTitle").val()));
					if($("#worktaskTitle").val()==""){
						alert("请填写任务标题!");
						$("#worktaskTitle").focus();
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
					var ids = $("#selectedData2").val();//老
					var ids2 = $("#recvIds").val();//新
					if(ids2.length>0 && ids!=""){
						var ida = ids.split(",");
						var ida2 = ids2.split(",");
						if(ida.length>=ida2.length){
							for(var i = 0; i<ida2.length; i++){
						   		if(ids.indexOf(ida2[i])>=0){
						   			alert('选择的承办者中含有已发的承办者,请勿重复添加！');
									return;
						   		}
					    	}
						}else{
							for(var i = 0; i<ida.length; i++){
						   		if(ids2.indexOf(ida[i])>=0){
						   			alert('选择的承办者中含有已发的承办者,请勿重复添加！');
									return;
						   		}
					    	}
						}
					}
					$("#worktaskContent").val($.trim($("#worktaskContent").val()));
					if(getDemoLen($("#worktaskContent").val())>1000){
						alert("任务内容不能超过500个汉字!");
						$("#worktaskContent").focus();
						return false;
					}
					$("#save").attr({'disabled':true});
					$("#myForm").submit();
					//alert('操作成功');
				});
			});

		//设置部门负责人,可选择多个
		function selectUser(){
			var url = "<%=path%>/workflowDesign/action/userSelect!assignUserList.action";
			window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:500px');
		}

		function delAttach(id){
			alert("执行删除附件功能");
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
			$("#recvNames2").val(temp);
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
	<body class=contentbodymargin oncontextmenu="return false;">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div class="gd_name"><div class="gd_name_left"><img src="<%=frameroot%>/images/ico/ico.gif" width="9"
												height="9">&nbsp;任务信息</div>
	  	<br style="clear:both;"/>
		</div>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
				<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
						<s:form id="myForm"
							action="/workinspect/worksend/workSend!save.action" theme="simple"
							enctype="multipart/form-data" target="hideFrame">
							<s:hidden id="worktaskId" name="model.worktaskId"></s:hidden>
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
								    <td width="120" height="43" align="right">已发承办者：</td>
								    <td colspan="3">
									    <input id="recvNames" name="recvNames" title="此处承办者不做修改" type="text" class="input_big_too"/>
								    </td>
							   </tr>
							   <tr>
							    <td width="120" height="43" align="right">新增承办者：</td>
							    <td colspan="3">
								    <input id="recvNames2" name="recvNames2" title="此处为新增承办者" type="text" class="input_big_too"/>
								    <input  type="button" class="gd_add" value="" onclick="selectUser();"/>
									<input id="recvIds" name="recvIds" type="hidden"/>
									<input id="selectedData" name="selectedData" type="hidden"/>
									<input id="selectedData2" name="selectedData2" value="${selectedData}" type="hidden"/>
									<!-- 初始化已选人员使用 -->
									<input id="handleactor" name="handleactor" type="hidden"/>
							    </td>
							    </tr>
							    <tr>
								    <td width="120" height="76" align="right">任务内容：</td>
								    <td colspan="3" align="left">
								      <textarea id="worktaskContent" name="model.worktaskContent" cols="45" rows="5" class="gd_textarea">${model.worktaskContent}</textarea>
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
								    listKey="key" listValue="value" value="0" /></div></td>
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
				                                             <a href="#" onclick="delAttach('${vo.attachId}');"  style='cursor: hand;'>[删除]</a>
				                                             <a href="<%=root%>/workinspect/worksend/workSend!download.action?attachId=<s:property  value='attachId'/>" target="tempframe" style='cursor: hand;'>${vo.attachFileName}</a>
														<br>
													
													</div>
												</s:iterator>
											</s:if>
										</div>
									</td>
								   </tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table align="center" width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr align="center">
											<td width="34%">
												<input id="save" type="button" class="input_bg"
													value="保 存">
											</td>
											<td width="33%">
												<input id="reset" type="reset" class="input_bg"
													value="重 写">
											</td>
											<td width="33%">
												<input id="c" type="button" class="input_bg"
													value="关闭" onclick="javascript:window.close();">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>