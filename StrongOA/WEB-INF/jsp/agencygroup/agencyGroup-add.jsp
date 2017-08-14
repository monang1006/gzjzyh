<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>机构-新建组</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
					//添加分组
					$("#btnOK").click(function(){
						var groupName = $.trim($("#groupName").val());
						var groupDesc = $.trim($("#groupDesc").html());
					
						if(groupName == ""){
							alert("分组名称不能为空！");
							$("#groupName").focus();
							return false;
						}
						if( groupDesc != ""){
							if(groupDesc.length>256){
								$("#groupDesc").html($("#groupDesc").html().substring(0,256))
								alert("描述内容过长，请控制在256个字以内！");
								$("#groupDesc").focus();
								return false;
							}
						}
						
						var url = $("form").attr("action");
						var id= $("#groupId").val();
						if(id != ""){
							url += "?model.groupAgencyId="+id;
						}
						$.ajax({
							type:"post",
							url:url,
							data:{
								"model.groupAgencyName":groupName,
								"model.groupAgencyRemark":groupDesc
							},
							success:function(data){
								window.returnValue="suc";
								window.close();
							},
							error:function(){
								alert("对不起，操作出错！");
								window.returnValue="fail";
								window.close();
							}
						});
						
					});
					//关闭窗口
					$("#btnCancel").click(function(){window.close();});
			
				
			});
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;">
	<div align="center" style="height:100%;width:100%;overflow: auto;">
		<form action="<%=root %>/agencygroup/agencyGroup!save.action" method="post">
			<input type=hidden id="groupId" value="${groupId}"/>
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9">&nbsp;&nbsp;<label id="title">
			        	<script type="text/javascript">
			        		var id = '${groupId}';
			        		if(id == ""){
			        			document.write("新建组");
			        		}else{
			        			document.write("机构组重命名");
			        		}
			        	</script>
			        	
			        		</label>
			        </td>
				</tr>
			<tr>
				<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz">发文组名称(<font color='red'>*</font>)：</span></td>
				<td  class="td1">
					<input type="text" id="groupName" value="${model.groupAgencyName}" maxlength="12" size="32"/>
					<input type="text" style="display: none;"/><!-- 此隐藏域只是为了防止上个文本框按回车后提交了 -->
				</td>
			</tr>
			<tr>
				<td width="20%" height="21" class="biao_bg1" align="right"><span class="wz">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：</span></td>
				<td  class="td1">
					<textarea id="groupDesc" rows="5" cols="32">${model.groupAgencyRemark }</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2"  class="td1" align="center">
        			<input id="btnOK" type="button" class="input_bg" icoPath="<%=frameroot%>/images/queding.gif" value="  保存" />&nbsp;&nbsp;&nbsp;&nbsp;
        			<input id="btnCancel" type="button" icoPath="<%=frameroot%>/images/quxiao.gif" class="input_bg" value="  取消" />
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>