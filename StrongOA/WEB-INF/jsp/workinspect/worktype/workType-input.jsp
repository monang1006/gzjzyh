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
				if($.trim($("#worktypeName").val())==""){
					alert("名称不能为空！");
					$("#worktypeName").focus();
					return false;
				}
				var worktypeSequence = $("#worktypeSequence").val();
				if($.trim($("#worktypeSequence").val())==""){
					alert("排序不能为空！");
					$("#worktypeSequence").focus();
					return false;
				}else if(!forcheck(worktypeSequence)){
					alert( "请输入大于零的整数!");
					$("#worktypeSequence").focus();
					return false;
					
				}else{}
				
				var id=$("#workTypeId").val();
				if(id==""){
					$("#dutyForm").attr("action","<%=root%>/workinspect/worktype/workType!save.action");
				}else{
					$("#dutyForm").attr("action","<%=root%>/workinspect/worktype/workType!save.action?worktypeId="+id);
				}
				document.dutyForm.submit();
			}
			function forcheck(ss){ 
			 var   type="^[0-9]*[1-9][0-9]*$"; 
			        var   re   =   new   RegExp(type); 
			       if(ss.match(re)==null) 
			        { 
			        // alert( "请输入大于零的整数!"); 
			        return false;
			        } 
			        return true;
			} 
	</script>
	</head>
	<base target="_self" />
	<body>
		<DIV id=contentborder align=center>
			<s:form action="workType!save.action" method="post" id="dutyForm"  name="dutyForm">
			<input type="hidden" name="model.workTypeId" id="workTypeId" value="${model.worktypeId}" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td width="5%" align="center"><img src="<%=path%>/common/images/ico.gif"></td>
            <td width="50%">新增任务分类</td>
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
							<table border="0" cellpadding="0" cellspacing="1" class="table1" width="100%">
								<tr>
									<td class="biao_bg1" width="20%" align="right">
										<span class="wz">名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1">
										<input id="worktypeName" name="model.worktypeName" type="text" size="40" 
										value="${model.worktypeName}"  >
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
									<td  class="td1" colspan="3" align="left">
										<input id="worktypeDemo" name="model.worktypeDemo" 
											value="${model.worktypeDemo}" type="text" size="40" maxlength="60">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">排序：</span>
									</td>
									<td  class="td1" colspan="3" align="left">
										<input id="worktypeSequence" name="model.worktypeSequence"  
											value="${model.worktypeSequence}" type="text" size="40" maxlength="60">
									</td>
								</tr>
							</table>
							<br>
							<table border="0" cellpadding="0" cellspacing="0" class="table1"
								width="100%" style="font: bold,x-large;">
								<tr align="center" class=td1>
									<td colspan="4" nowrap>
										<input type="button" value="保存" class="input_bg" onclick="save();">
									<span>
										<input type="button" value="取消" class="input_bg" onclick="javasctipr:window.close();">
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
