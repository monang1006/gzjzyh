<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>模板信息</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path %>/common/js/upload/myjquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path %>/common/js/upload/jquery.blockUI.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
			function OpenWindow(Url, Width, Height, WindowObj) {
				var ReturnStr = showModalDialog(Url,WindowObj,"dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+"status:yes;help:no;scroll:yes;");
				return ReturnStr;
			}
			function chooseFilePath(){
				msg = OpenWindow('<%=path%>/viewmodel/viewModelPage!getServerPathTree.action', '200', '300', window);
				//alert(msg.substring(msg.lastIndexOf("\\")+1,msg.length));
				$("#path").val(msg);
			}
			function formSubmit(){
				if($("#dbobj").val()=="null"||$("#dbobj").length==0){
					 var isSelectFile = false;
				     $("input:file.multi").each(function(){
				     	if($(this).val()!=""){
				     		isSelectFile = true;
				     	}
				     });
				     if(!isSelectFile){
				     	alert("对不起，请您选择一个模板文件！");
				     	return ;
				     }
				     if($("input:file.multi").length>2){
					 	alert("对不起，您选择了多个模板文件，一个模板只能对应一个模板文件");
					 	return ;
					 }
				}
				if($.trim($("#valName").val())!=''){
					$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/viewmodel/viewModelPage!chargeVal.action",
				  		data:"modelId=${modelId}&parentId=${parentId }&valName="+$("#valName").val()+"&id=${model.pagemodelId}",
				  		success:function(msg){
				  			if(msg=="true"){
								$("#pagemodelParentid").val("${parentId}");
								$("#myform").submit();
							}else{
								alert("同级页面中父级页面变量不可以相同");
								return;
							}
				  		}
				  	});
				}else{
					$("#pagemodelParentid").val("${parentId}");
					$("#myform").submit();
				}

			}
			function deldbobj(id){
				$("#dbobj").val("null");
				$("#divattachinput").show();
				$("#divattach").hide();
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<div id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="myform" action="<%=path %>/viewmodel/viewModelPage!save.action" method="post"  enctype="multipart/form-data">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							 <strong>页面模板信息</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="formSubmit();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="parent.location.reload();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
		                	<input id="clumnId" name="model.pagemodelId" type="hidden" value="${model.pagemodelId}">	
							<input id ="modelName" name ="model.pagemodelName" type ="hidden" value="${model.pagemodelName }">
							<input id="modelId" name="modelId" type="hidden" value="${modelId }">
							<input id="parentId" name="parentId" type ="hidden" value="${parentId }">
							<input id="pagemodelParentid" name="model.pagemodelParentid" type="hidden" value="${model.pagemodelParentid }">
							<tr>
							 <td>
									<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz">所属页面模板：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<s:if test="parentId==0">
													顶级页面模板
												</s:if>
												<s:else>
													<s:text name="parentName"></s:text>
												</s:else>
											</td>
										</tr>
										
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;页面模板描述：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input id="pagemodelName" name="model.pagemodelDes" style="border:1px solid #b3bcc3;background-color:#ffffff;" type="text" size="32" value="${model.pagemodelDes}" class="required" maxlength="20">
											</td>
										</tr>
										<s:if test="parentId==0">
										</s:if>
										<s:else>
										<tr id="trValName">
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;父级页面变量名称：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input id="valName" name="model.pagemodelValname" type="text" class="required" size="32" value="${model.pagemodelValname}" maxlength="30">
											</td>
										</tr>
										</s:else>
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span id="spanName" class="wz"><FONT color="red">*</FONT>&nbsp;父级页面变量对应值：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input id="valValue" name="model.pagemodelVal" type="text" style="border:1px solid #b3bcc3;background-color:#ffffff;" value="${model.pagemodelVal}" size=32  class="required" maxlength="100">
											</td>
										</tr>
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span  class="wz">页面前置名(ACTION名)：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input id="pagemodelActionName" style="border:1px solid #b3bcc3;background-color:#ffffff;" name="model.pagemodelActionName" type="text" value="${model.pagemodelActionName}" size=32 maxlength="100">
											</td>
										</tr>
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;页面模板上传(附件)：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<s:if test="model.pagemodelName!=null">
													<div id="divattachinput" style="display:none"><input type="file" class="upFileBtn" onkeydown="return false;" id="file" name="file" class="multi max-1" accept="ftl"  style="width: 76%;" /></div>
													<div id=divattach>[<a href="#" onclick="javascript:deldbobj('${model.pagemodelId }')">删除</a>]${model.pagemodelName }.ftl</div><input type="hidden" name="dbobj" id="dbobj" value=${model.pagemodelName } />
												</s:if>
												<s:else>
													<input type="file" onkeydown="return false;" id="file" name="file" class="multi max-1" accept="ftl"  style="width: 76%;" />
												</s:else>
											</td>
										</tr>
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;页面存储路径：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input id="path" readonly="readonly" style="border:1px solid #b3bcc3;background-color:#ffffff;" ondblclick="chooseFilePath()"  name="model.pagemodelSavepath" type="text" size="32" value="${model.pagemodelSavepath}" class="required" maxlength="100">
												&nbsp;&nbsp;&nbsp;
												<a id="choosePath"  href="#" class="button" onclick="chooseFilePath()">存储路径</a>&nbsp;
												<!--<input type="button" id="choosePath" name="choosePath" value="存储路径" onclick="chooseFilePath();">-->
											</td>
										</tr>
										<s:if test="parentId==0">
										</s:if>
										<s:else>
										<tr>
											<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;所在框架名称：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<input type="text" id="framename" name="model.pagemodelFramename"  value="${model.pagemodelFramename }" size="32" class="required" maxlength="50" />
											</td>
										</tr>
										</s:else>
										<tr>
										<td style="line-height: 34px;height: 30px;color: #454953;width:160px;" align="right">
												<span class="wz"><FONT color="red">*</FONT>&nbsp;页面类型：</span>
											</td>
											<td class="td1" style="padding-left:5px;">
												<s:radio id="type" name="model.pagemodelPagetype" list="#{'0':'普通页面' , '1':'导航按钮','2':'工作区','3':'顶部区域' }" listKey="key" listValue="value"/>
											</td>
										</tr>
										<tr>
											<td class="table1_td"></td>
											<td></td>
						               </tr>
				        	</table>
		     	  </td>
		     	</tr>
			</form>
      </td>
  </tr>
</table>
		</DIV>
	</body>
	<script type="text/javascript">
		if($("#parentId").val()=="0"){
			//$("#trValName").hide();
			$("#spanName").html("页面跳转Action：");
		}
	</script>
</html>
