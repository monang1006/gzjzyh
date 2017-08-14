<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存信息项分类</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		
		<script type="text/javascript">
		$(document).ready(function(){
		    var propertypeName1=$("#propertypeName").val();//原来的信息项分类名称-编辑
		    $("#propertypeName1").val(propertypeName1);
		});
			//校验信息项分类名称是否已经使用
			function checkInfoTypeNameIsUsed(){
			    var propertypeId=$("#propertypeId").val();
				var propertypeName =$("#propertypeName").val();//信息项分类名称
				 var propertypeName1=$("#propertypeName1").val();//原来的信息项分类名称-编辑
				var infoSetCode = $("#infoSetCode").val();//当前信息项分类的信息集
				if($.trim(propertypeName) == ""){
					alert("请输入信息项分类名称。");
					return ;
				}else{
					if(propertypeName.indexOf("<")!=-1 || propertypeName.indexOf(">")!=-1){
						alert("信息项分类名称中含特殊字符，拒绝输入。");
						return ;
					}
					if(propertypeName.length>64){
						alert("信息项分类名称输入过长。");
						return ;
					}
					$("#propertypeName").val($.trim(propertypeName));
				}
				if(propertypeId!=null){
					   if(propertypeName1==propertypeName){//编辑的时候，信息项分类名称未改变,不验证重名
					      $("#propertypeName").val(propertypeName);
					       $("form").submit();
					   }else{//编辑的时候，信息项分类名称改变，验证重名
					      $.post("<%=root%>/infotable/infotype/infoType!checkInfoTypeName.action",
							  {"model.propertypeName":$.trim(propertypeName),"model.toaSysmanageStructure.infoSetCode":infoSetCode},
							  function(ret){
							  	if(ret == "1"){
							  		alert("信息项分类名称已存在,请重新输入。");
							  		return ;
							  	}else if(ret == "0"){
							  		$("#propertypeName").val(propertypeName);
							  		$("form").submit();
							  	}else{
							  		alert(ret);//异常
							  	}
						  });
					   }
				}
				else
				{//新增的时候,验证信息项分类名称是否重名
					$.post("<%=root%>/infotable/infotype/infoType!checkInfoTypeName.action",
						  {"model.propertypeName":propertypeName,"model.toaSysmanageStructure.infoSetCode":infoSetCode},
						  function(ret){
						  	if(ret == "1"){
						  		alert("信息项分类名称已存在,请重新输入。");
						  		return ;
						  	}else if(ret == "0"){
						  		$("#propertypeName").val(propertypeName);
						  		$("form").submit();
						  	}else{
						  		alert(ret);//异常
						  	}
					  });
				}
			}
		</script>
		
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="infotypeForm" theme="simple" action="/infotable/infotype/infoType!save.action" method="post">
				<input id="propertypeId" name="model.propertypeId" type="hidden" size="32" value="${model.propertypeId}">
				<input id="infoSetCode" name="infoSetCode" type="hidden" size="32" value="${infoSetCode}">
				<input id="propertypeName1" name="propertypeName1" type="hidden" size="32" value="">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <s:if test="propertypeId==null">
										<strong>新建信息项分类</strong>
									</s:if>
									<s:else>
										<strong>编辑信息项分类</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="checkInfoTypeNameIsUsed();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
				
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">对应信息集：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input name="model.toaSysmanageStructure.infoSetCode" type="hidden" size="32" value="${infoSetCode}">
										<span class="wz">${infoSetName}</span>
									</td>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;信息项分类名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="propertypeName" name="model.propertypeName" type="text" size="32" class="required" value="${model.propertypeName}" maxlength="25">
									</td>
								</tr>
								<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
