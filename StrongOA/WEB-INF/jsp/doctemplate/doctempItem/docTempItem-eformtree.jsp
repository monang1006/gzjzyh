<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<title>挂接表单</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			
			$(document).ready(function(){
				var forms = "${forms}";
				$("input:checkbox").each(function(){
					var id = $(this).val();
					if(forms.indexOf(","+id+",")!=-1){
						$(this).attr("checked",true);
					}
				});
			});
		
			//确定
			function doSelect(){
				var formId = "";
				$("input:checkbox:checked").each(function(){
					formId += $(this).val() + ",";
				});
				var forms = "${forms}";
				if(forms == "," && formId == ""){
					alert("请选择表单。");
					return ;
				}
				$.post("<%=root%>/doctemplate/doctempItem/docTempItem!doSelectEForm.action",
					{formId:formId,doctemplateId:$("#doctemplateId").val()},function(ret){
						if(ret == "0"){
							alert("操作成功。");
							window.close();
						}else if(ret == "-1"){
							alert("对不起，操作失败，请与管理员联系。");
							return ;
						}
					});
			}
			//取消
			function doCancel(){
				window.close();
			}
			
			function select(nodeId,type){
				var chk = $("input[value='"+nodeId+"']");
				var flag = chk.attr("checked");
				if(flag == true){
					chk.attr("checked",false);
				}else{
					chk.attr("checked",true);
				}
			}
		</script>
	</head>
<base target="_self"/>	  
<body class=contentbodymargin scroll="auto">
<div id=contentborder style="overflow:hidden;">
	<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>挂接表单</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSelect();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="doCancel();">&nbsp;取&nbsp;消&nbsp;</td>
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
	 <div style="width:100%; height:425px; overflow-y:auto;">
	<s:hidden id="doctemplateId" name="doctemplateId"></s:hidden>
	<tree:strongtree title="电子表单" check="true"  dealclass="com.strongit.oa.doctemplate.eform.DocRedTypeTreeDeal" data="${typeList}" iconpath="frame/theme_gray/images/"/>
    </div>
</div>
</body>
</html>
