<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<html>
	<head>
		<title>请选择栏目</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css
			rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
		
			function onsubmitform(){
			 	var values = "";
				$("input:checked").each(function () {
					values += "," + $(this).val();
				});
				if (values.length > 1) {
					values = values.substring(1);
				}
				if(values==""){
				alert("请先选择栏目！");
				return;
				}
				document.getElementById("columnId").value = values;
				
				artsave.submit();
			}
			
			function onform(){
			     var parWin = window.dialogArguments;
				 var columnIds = parWin.document.getElementById("columnIds");
				 var columnNames=parWin.document.getElementById("columnNames");
				 var values="";
				 var valuesNames="";
				 $("input:checked").each(function () {
					 values += "," + $(this).val();
					 valuesNames+=","+$(this).next().text();
				 });	
				 if (values.length > 1) {
					columnIds.value = values.substring(1);
				 }else{
				    columnIds.value="";
				 }
				 if(valuesNames.length>1){
				    columnNames.value=valuesNames.substring(1);
				 }else{
				    columnNames.value="";
				 }
			
			    window.close();
					
					
			}
			function initChecked(value){
				if(value==""){
					return "no";
				}else{
					var parWin = window.dialogArguments;
					var columnIds = parWin.document.getElementById("columnIds");
					if(columnIds && value.length == 32 && columnIds.value.indexOf(value) != -1){
						return "true";
					}
				}
				return "false";
			}
			$(document).ready(function(){
			});
		</script>
	</head>
	<base target="_self" />
	<body>
		<DIV id=contentborder align=center>
			<s:form id="artsave"
						action="/infopub/articles/articles!setArticlesClumn.action"
						theme="simple">
						<input type="hidden" id="articlesId" name="articlesId"
							value="${articlesId}">
						<input type="hidden" id="columnId" name="columnId">
					</s:form>
			<div>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>	
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>请选择</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                <security:authorize ifAllGranted="001-0004000200010011">
											<s:if test="articlesId==null||articlesId==''">
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 			<td class="Operation_input" onclick="onform();" name="ok">&nbsp;确&nbsp;定&nbsp;</td>
					                 			<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  				<td width="5"></td>
											</s:if>
											<s:else>
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 			<td class="Operation_input"  name="Submit2" onclick="onsubmitform();" name="ok">&nbsp;保&nbsp;存&nbsp;</td>
					                 			<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  				<td width="5"></td>
											</s:else>
									</security:authorize>
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
					<tree:strongtree title="栏目名称" check="true" chooseType="chooseOne"
					dealclass="com.strongit.oa.infopub.articles.DealTreeNode"
					data="${columnList}" target="articlesList" 
					iconpath="frame/theme_gray/images/"/>
				</td>
				</tr>
				</table>
			</div>
		</DIV>

	</body>
</html>
