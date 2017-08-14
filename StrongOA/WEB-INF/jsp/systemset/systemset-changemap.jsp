<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>更改映射</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var parentWindow = window.dialogArguments
			var plaintext = parentWindow.document.getElementById("plaintext");
			$("#column").text(plaintext.value+"：");
			$("#btnOK").click(function(){
				var dataType="${dataType}";					//被映射字段的数据类型
				var dateTypes=$("#dateTypes").get(0);		//映射字段的数据类型及数据大小
				var sel_column = $("#sel_column").get(0);
				if(sel_column.selectedIndex<0){
					alert("请选择子流程启动表单字段!");
					return;
				}
				var dateType2=dateTypes.options[sel_column.selectedIndex].text;	//映射字段的数据类型
				if(dataType=="String"&&dataType!=dateType2){	//如果是字符串映射到其他数据类型，则提示用户
					if(!confirm("由【字符串】数据类型映射到【"+getName(dateType2)+"】数据类型，是否继续？"))
						return;
				}	
				if(sel_column.selectedIndex==-1){
					window.returnValue="|";
				}else{
					window.returnValue = sel_column.options[sel_column.selectedIndex].text+"|"+sel_column.options[sel_column.selectedIndex].value+"|"+dateTypes.options[sel_column.selectedIndex].text+"|"+dateTypes.options[sel_column.selectedIndex].value;
				}
				window.close();
			});
			$("#btnCancel").click(function(){window.returnValue = "";window.close();});
		});
		
		function getName(value){
			var tempvalue="";
			if(value=="String"){
				tempvalue="字符串";
			}else if(value=="Timestamp"){
				tempvalue="日期";
			}else if(value=="Integer"){
				tempvalue="整型";
			}else if(value=="Long"){
				tempvalue="长整型";
			}else if(value=="BigDecimal"){
				tempvalue="浮点型";
			}else{
				tempvalue="不确定数据类型";
			}
			return tempvalue;
		}
	
	</script>
	</head>
	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>更改映射</strong>
								<%--<strong>编辑通知公告</strong>--%>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td id="btnOK" class="Operation_input">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td id="btnCancel" class="Operation_input1">&nbsp;取&nbsp;消&nbsp;</td>
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
									<td>
										<br>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<span class="wz">选择父流程启动表单字段对应的子流程启动表单字段：</span>
										<br>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<span id="column" class="wz"></span>
										<br>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<s:select cssStyle="width: 220px" list="mapItemList" listKey="infoItemField"
											listValue="infoItemSeconddisplay" id="sel_column" name="subFormId"></s:select>
										<br>
										<br>
										<s:select size="20" cssStyle="width: 220px;visibility:hidden" list="mapItemList" listKey="infoItemLength"
											listValue="infoItemDatatype" id="dateTypes" name="dateTypes"></s:select>				
									</td>
								</tr>
							</table>
						</td>
					</tr>
					</table>
	</body>
</html>
