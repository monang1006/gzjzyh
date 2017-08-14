<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%> 
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>活动分类</title>
		<%@include file="/common/include/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows_add.css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
			<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
		 function ssave(){
		 	var activityName = document.getElementById("activityName");
		 	if(activityName.value==null|activityName.value==''){
		 		alert('请输入分类名称！');return false;
		 	}
		 	form.submit();
		 	returnValue ="reload";
			window.close();
		 }
		 
		 //新建活动分类
         function save(){
         	var activityId = document.getElementById("activityId");
         	var activityName = document.getElementById("activityName");
         	var activityRemark = document.getElementById("activityRemark");
         	if(activityName.value.trim()==null|activityName.value.trim()==""){
         		alert("请输入活动分类名称！");
         		return false;
         	}else{
	         	var myAjax = new Ajax.Request(
	                 '<%=path%>/calendar/calendarActivity!save.action', // 请求的URL
	                {
	                    //参数    
	                   //修改输入#号不显示的bug niwy encodeURIComponent该方法不会对 ASCII 字母和数字进行编码，也不会对这些 ASCII 标点符号进行编码： - _ . ! ~ * ' ( ) 。
	                    
	                    parameters : 'activityId='+activityId.value+'&model.activityName='+encodeURIComponent(encodeURIComponent(activityName.value))+'&model.activityRemark='+encodeURIComponent(encodeURIComponent(activityRemark.value)),
	                   
	                    // 使用GET方式发送HTTP请求
	                    method:  'post', 
	                    // 指定请求成功完成时需要执行的js方法
	                    onComplete: function(response){
		                    	var activityId = response.responseText||"no response text";
		                    	if(activityId!="no response text"){
		                    		if(activityId=="exist"){
		                    			alert("该活动分类已存在！");
		                    		}else{
			                    		returnValue = "reload"+activityId;
										window.close();
		                    		}
		                    	}else{alert("不能添加活动分类！");}
	                    	}
	                }
	            );
         	}
         }
         
		 function del(){
		 	var activityId = document.getElementById("activityId").value;
		 	if(activityId==""|activityId==null){
		 		document.getElementById("activityName").value="";
		 		document.getElementById("activityRemark").value="";
		 		return false;
		 	}
		 	var reload = document.getElementById("reload");
		 	reload.href = "<%=path%>/calendar/calendarActivity!delete.action?activityId="+activityId;
		 	reload.click();
		 	returnValue ="reload";
			window.close();
		 }
		</script>
	</head>
	<base target="_self"/>
	<body>
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
	
	<DIV align=center>
	<a id="reload" style="display: none" href=""></a>
	<s:form action="calendarActivity!saveForm.action" name="form">
	<input type="hidden" id="activityId" name="model.activityId" value="${activityId}">
	<table width="100%" border="0" cellspacing="0" cellpadding="00">
		<tr>
			<td height="40"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<script>
								var id = "${model.activityId}";
								if(id==null|id==""){
									window.document.write("<strong>新建活动分类</strong>");
								}else{
									window.document.write("<strong>修改活动分类</strong>");
								}
								</script>
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
				</table>
			</td>
		</tr>
	</table>
		
	<table border="0" cellpadding="0" cellspacing="1" width="100%"  class="table1"  height="100%">
		<tr>
		<td valign="top" align="left">
			<table  border="0" cellpadding="0" cellspacing="0"  width="100%" >
				<tr>
					<td class="biao_bg1"  align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;名称 ：&nbsp;</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						&nbsp;<input id="activityName" name="model.activityName" type="text" value="${model.activityName}"   
							 size="45" maxlength="15">
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">备注 ：&nbsp;</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						&nbsp;<input id="activityRemark" name="model.activityRemark" value="${model.activityRemark}"  type="text"  
							 size="45" maxlength="45">
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
	</DIV>
	</body>
</html>
