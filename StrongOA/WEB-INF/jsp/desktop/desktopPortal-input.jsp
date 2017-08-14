<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>
				<%--<s:if test="model.portalId!=null">
					编辑门户
				</s:if>
				<s:else>
					新建门户
				</s:else>--%>
				保存门户
		</title>
		
				
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		

		<script>
      //   var x=document.getElementById("ismorenm").value;
       //  alert(x);
		// $("option[value='${model.isMoren}']").attr("selected","selected");
		 
			function save(){
			    var numtest = /^\d+$/; 
			    var portalId=document.getElementById("portalId").value;
			    var portalName=document.getElementById("portalName").value;
			    portalName=portalName.replace(/[ ]/g,""); 
				if($.trim($("#portalName").val())==""){
					alert("请输入门户名称。");
					$("#portalName").focus();
					return;
				}
				
				if($.trim($("#isMoren").val())==""){
					alert("请选择是否为默认。");
					$("#isMoren").focus();
					return;
				}
				
				if($.trim($("#isEditorno").val())==""){
					alert("请选择是否可编辑。");
					$("#isEditorno").focus();
					return;
				}
				
			var sequence=document.getElementById("secrec").value;
			if(''==sequence){
				    alert('请输入排序序号。');
			       document.getElementById("secrec").focus();
			        return;
			}else{if(!numtest.test(sequence)){
			       alert('排序必须为数字。');
				      document.getElementById("secrec").focus();
				        return;
			        }
			}
			
			 
				$.ajax({
					type:"post",
					url:"<%=path%>/desktop/desktopPortal!isExistName.action",
					data:{
						portalId:portalId,
						portalName:portalName			
					},
					success:function(data){	
						if(data!=null&&data=="0"){//无后缀，说明是用户新建的而不是上传的文件
				 			alert("该门户名称已经存在，请重新输入。");
				 			document.getElementById("portalName").value="";
				 			document.getElementById("portalName").focus();
						}else{
							form.submit();
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
				});			
			}
		</script>
	</head>
		<base target="_self">
	<body oncontextmenu="return false;" >
		<DIV id=contentborder align=center>
		<s:form name="form" action="/desktop/desktopPortal!save.action" method="post" >
		<label id="actionMessage" style="display:none;"><s:actionmessage/></label>
			<input type="hidden" id="portalId" name="model.portalId" value="${model.portalId}">
			<input type="hidden" id="ismorenm" name="ismorenm" value="${model.isMoren}">
			<input type="hidden" id="isEdit" name="isEdit" value="${model.isEdit}">
			<input type="hidden" id="portalDesktopId" name="model.portalDesktopId" value="${model.portalDesktopId}">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<s:if test="model.portalId!=null">
							编辑门户
						    </s:if>
						   <s:else>
							新建门户
						   </s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td class="biao_bg1" align="right">
						<span class="wz"><font color=red>*</font>&nbsp;名  称：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input id="portalName" name="model.portalName" type="text" value="${model.portalName}"
									 size="36" maxlength="25">
									 
						&nbsp;
					</td>
				</tr>
				
				<tr>
					<td  class="biao_bg1" align="right">
						<span class="wz"><font color="red">*</font>&nbsp;是否为默认门户：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<select id="isMoren" name="model.isMoren" style="width:244px">
	                    <s:if test="model.isMoren==1">
						 <option value='1' selected="selected">是</option>
						 <option value='0'>否</option>
					   </s:if>
						<s:else>
						<s:if test="model.isMoren==0">
		                     <option value='1'>是</option>
							 <option value='0' selected="selected">否</option>
						</s:if> 
						<s:else> 
							 <option value=''>---请选择---</option>
		                     <option value='1'>是</option>
							 <option value='0'>否</option>
						</s:else>
						</s:else>    
                         </select>
					</td>
				</tr>
				<tr>
					<td  class="biao_bg1" align="right">
						<span class="wz"><font color="red">*</font>&nbsp;是否可编辑：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<select id="isEditorno" name="model.isEdit" style="width:244px">
		                      <s:if test="model.isEdit==1">
							   <option value='1' selected="selected">是</option>
							   <option value='0'>否</option>
						      </s:if>
		                     <s:else>
								 <s:if test="model.isEdit==0">
				                     <option value='1'>是</option>
									 <option value='0' selected="selected">否</option>
								</s:if> 
								<s:else> 
									 <option value=''>---请选择---</option>
				                     <option value='1'>是</option>
									 <option value='0'>否</option>
								</s:else>
						    </s:else>    
				
                         </select>
					</td>
				</tr>
				<tr>
					<td  class="biao_bg1" align="right">
						<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
					</td>
					<td  class="td1" style="padding-left:5px;">
						<input id="secrec" name="model.secrec" value="${model.secrec}" type="text" maxlength="4" size="36">
					</td>
				</tr>
				 <tr>
					<td class="table1_td"></td>
					<td></td>
				</tr>
			</table>
		</td>
	</tr>
 </table>
		</s:form>
		<script>
		// $("option[value='${model.isMoren}']").attr("selected","selected");
		// $("option[value='${model.isEdit}']").attr("selected","selected");
		 
		 </script>
		</DIV>
	</body>
</html>
