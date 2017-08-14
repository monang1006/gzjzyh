<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>任务分类</title>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
					src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		
	<script type="text/javascript">
		 function save(){
		 $.post("<%=path%>/worktype/workType!.action",
				{userId:$("#personId").val(),searchDate:$("#attDate").val()},
		           function(data){
				    	if(data=="shas"){
				    		alert("您今日上午已签到，无需再次签到！");
			    		}else if(data=="xhas"){
				    		alert("您今日下午已签到，无需再次签到！");
			    		}else if(data=="no"){
						 	dutyForm.submit();
			    		}else{
			    			alert("对不起，获取签到信息出错。请与管理员联系！");
			    		}
			    });	
		 }
		
	</script>
	</head>
	<base target="_self" />
	<body>
		<DIV id=contentborder align=center>
			<s:form action="personattendance!save.action" method="post" id="dutyForm">
			<input name="results" type="hidden">
			<input type="hidden" id="worktypeId" name="model.worktypeId" value="${model.worktypeId}">
			<input type="hidden" id="worktypeValue" name="model.worktypeValue" value="${model.worktypeValue}">
			<input type="hidden" id="worktypeName" name="model.worktypeName" value="${model.worktypeName}">
			<input type="hidden" id="worktypeDemo" name="worktypeDemo" value="${model.worktypeDemo}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td width="5%" align="center"><img src="<%=path%>/common/images/ico.gif"></td>
            <td width="50%">编辑任务分类</td>
            <td width="10%">&nbsp;</td>
            <td width="35%">
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
				<table border="0" cellpadding="0" cellspacing="1" width="100%">
					<tr>
						<td valign="top" align="left" width="70%">
							<table border="0" cellpadding="0" cellspacing="1" class="table1"width="100%">
							 
								<tr>
									<td class="biao_bg1" width="20%" align="right">
										<span class="wz">值内容(<font color="red">*</font>)：</span>
									</td>
									<td class="td1">
										<input id="worktypeValue" name="model.worktypeValue" type="text" size="40" 
										value="领导批示" readonly="readonly" >
									</td>
								</tr>
								
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">类别(<font color="red">*</font>)：</span>
									</td>
									<td  class="td1" colspan="3" align="left">
										<input id="worktypeName" name="model.worktypeName" readonly="readonly"
											value="分类工作" type="text" size="40" maxlength="60">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
									<td  class="td1" colspan="3" align="left">
										<input id="worktypeDemo" name="model.worktypeDemo" readonly="readonly"
											value="${model.worktypeDemo}" type="text" size="40" maxlength="60">
									</td>
								</tr>
							</table>
							<br>
							<table border="0" cellpadding="0" cellspacing="0" class="table1"
								width="100%" style="font: bold,x-large;">
								<tr align="center" class=td1>
									<td colspan="4" nowrap>
										<input type="button" value="确认" class="input_bg" onclick="save();">
									<span>
										<input type="button" value="取消" class="input_bg" onclick="abort();">
									</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
