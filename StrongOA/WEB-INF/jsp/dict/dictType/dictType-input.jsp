<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>保存字典类</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--页面样式  -->
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/service.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/workservice.js"
			type="text/javascript" language="javascript"></script>
		<script type="text/javascript">
		function test(){
			 var msg="";
			 var dictCode=$("#infoSetCode").val();
			 var dictValue=$("#dictValue").val();
			 var dictName=$("#dictName").val();
			 var msg1="";
			 //必填项判断
			 
			 if($.trim(dictValue)==""){
					
					//alert("系统链接不能为空");
					if(msg1==""){
					 document.getElementById("dictValue").focus();
					}
					msg1+="字典类值不能为空。\n";
					//return false;
				}
			 if($.trim(dictName)==""){
					
					//alert("系统链接不能为空");
					if(msg1==""){
					  document.getElementById("dictName").focus();
					}
					msg1+="字典类名称不能为空。\n";
					//return false;
				}
			 if(msg1==""){
			 $.ajax({
              	 	type:"post",
              	 	url:"<%=path%>/dict/dictType/dictType!yanzheng.action",
              	 	data:{
						"dictCode1":dictCode,"dictValue1":dictValue	
                        },
					async:false,
					success:function(data){
						//alert(data.indexOf("成功")==-1);
						if(data.indexOf("成功")==-1){
			                   msg="0";
			                   alert(data);
			            }else{
			                   msg="1";
			                 
			            }
			            
						},
				   error:function(data){
						alert("对不起，操作异常"+data);
						}
              	 	});
			 }
			 if(msg1!="" && msg1!="0" ){
			    	 alert(msg1);
			       return false;
			   }else if(msg=="0"){
			        return false;
			   }else{
			      document.getElementById("DictTypeForm").submit();
			     }
			}
		</script>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="DictTypeForm" theme="simple"
							action="/dict/dictType/dictType!save.action">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <s:if test="dictCode==null">
										<strong>新建字典类</strong>
									</s:if>
									<s:else>
										<strong>编辑字典类</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 <s:if test="model.dictIsSystem==\"1\"">
												
							         </s:if>
							         <s:else>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="test();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	 </s:else>
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
							
							<input id="infoSetCode" name="model.dictCode" type="hidden"
								size="32" value="${model.dictCode}">
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;字典类值：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="dictValue" name="model.dictValue" type="text" size="32"
											value="${model.dictValue}" class="required" maxlength="10">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;字典类名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="dictName" name="model.dictName" type="text" size="32"
											value="${model.dictName}" class="required"  maxlength="20">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">字典类类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.dictType"
											list="#{'A':'国标码','B':'地方码','C':'自定义码','D':'其他'}"
											listKey="key" listValue="value" />
									</td>
								</tr>
								<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
