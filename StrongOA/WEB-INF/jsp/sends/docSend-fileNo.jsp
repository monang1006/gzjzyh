<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<HTML>
	<HEAD>
		<TITLE>公文归档</TITLE>
		<%@include file="/common/include/meta.jsp"%>
			<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows.css">

		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
	</HEAD>
	<BODY >			
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="34" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									</td>
									<td width="227">
										档案中心
									</td>
									<td>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>  
				</table>

				<table border="0" cellpadding="0" cellspacing="1" width="100%" class=biao_bg1
					height="100%">
					<tr>
						<td valign="top" align="left" width="70%">
							<table border="0" cellpadding="0" cellspacing="1" class="table1"
								width="100%">
								<tr>
									<td colspan="2" class="biao_bg1" >
										&nbsp;
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" width="50%">
										<font color='red'>&nbsp;&nbsp;档案文号：</font>
									</td>
									<td class="td1" width="50%"> 
										<input type="text" id="fileNo" name="fileNo" maxlength="30" size="16" width="100%" >
									</td>
								</tr>
								<tr>
									<td colspan="2" class="biao_bg1" >
										&nbsp;
									</td>
								</tr>
								<tr align="center" class=biao_bg1>
									<td colspan="2" nowrap>
										<input type="button" value="确定" class="input_bg" 
											onclick="save();"> 
										&nbsp;&nbsp;
										<input type="button" value="关闭" class="input_bg"
											onclick="windocClose();">
									</td>
								</tr>
								
							</table>
						</td>
					</tr>
				</table>
				
		<script type="text/javascript">
			function checkFileNo(){
				var fileNo=document.getElementById("fileNo").value;
				if(fileNo!=null&&fileNo!="")
				{								
				  
				}
			}
			
			function save(){
				var fileNo=document.getElementById("fileNo").value;	
				for(var i=0;i<fileNo.length;i++){
					var subStr=fileNo.substring(i,i+1);
					if(subStr!=""&&isNaN(subStr)){  //为空或为数字不处理
						var partarn=/^[%_']+$/;
						if(partarn.test(subStr)){ 
							 alert("请不要输入“%”,“_”和“ ’”特殊字符！");
							 document.getElementById("fileNo").value=fileNo.substring(0,i);
							 return;
						}
					}
 				}
							
				if(fileNo!=null&&fileNo!=""){
					 $.post("<%=path%>/receives/archive/archiveDoc!isHasSendFileNo.action",
                 	{"fileNo":fileNo},
                   function(data){
                   	
                      if(data=="1"){
                      		alert("已存在该档案文号，不能进行保存！");
                      		document.getElementById("fileNo").value="";
                      		//document.getElementById("fileNo").focus();
                      		return ;                     	
                      }else{
                      	
						window.returnValue=fileNo;
						window.close();				
                      }
                   });
				}else{
					alert("档案文号不能为空！");
				}
			}
			
			function windocClose(){
<%--				window.returnValue="";--%>
				window.close();	
			}
		</script>
	</BODY>
</HTML>
