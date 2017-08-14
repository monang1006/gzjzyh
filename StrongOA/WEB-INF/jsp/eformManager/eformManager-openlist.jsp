<HTML><HEAD><TITLE>电子表单列表</TITLE>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
<script type="text/javascript">
	function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
	
	$(document).ready(function(){
		//搜索功能
		$("#img_sousuo").click(function(){
			$("#searchTitle").val(encodeURI($("#title").val()));
			$("form").submit();
		});
	});
	
	function selectValue(value){
		window.returnValue=value;
		window.close();
	}
</script>
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" >
<DIV id=contentborder align=center>
<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>表单列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	
					                 </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				
				</tr>
              <tr>
			<td height="100%">
	<s:form id="myTableForm" action="/eformManager/eformManager.action" method="get">
		<input type="hidden" name="operating" id="operating" value="${operating}">
    	 <input type="hidden" name="model.title" id="searchTitle" value="${model.title}">
	     <webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" ondblclick="selectValue(this.value)" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
			     <tr>
			     	<td>
			       		&nbsp;&nbsp;表单名称：&nbsp;<input name="title" id="title" type="text" class="search" title="请您输入表单名称" value="${title}">
			       		&nbsp;&nbsp;表单类型：&nbsp;<s:select name="model.type" list="#{'':'全部','SF':'启动表单','QF':'查询表单','VF':'展现表单'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
			       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
			       	</td>
			     </tr>
			</table> 
			<webflex:flexTextCol caption="编号" valuepos="0" valueshowpos="0" width="5%" isCanDrag="true" isCanSort="true"  showsize="34"></webflex:flexTextCol>
			<webflex:flexEnumCol caption="类型" mapobj="${typemap}" valuepos="2" 
								valueshowpos="2" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
			<webflex:flexTextCol caption="标题" valuepos="1" valueshowpos="1" width="20%" isCanDrag="true" isCanSort="true"  showsize="34"></webflex:flexTextCol>
			<webflex:flexTextCol caption="创建人" valuepos="4" valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true"  showsize="34"></webflex:flexTextCol>
			<webflex:flexDateCol caption="创建时间" valuepos="3" valueshowpos="3" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>
			<webflex:flexTextCol caption="修改人" valuepos="5" valueshowpos="5" width="15%" isCanDrag="true" isCanSort="true"  showsize="34"></webflex:flexTextCol>
			<webflex:flexDateCol caption="修改时间" valuepos="6" valueshowpos="6" width="" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>
		</webflex:flexTable>
	</s:form>
					</td>
				</tr>
			</table>
		
<script language="javascript">
var sMenu = null;
</script>
</DIV>
</BODY></HTML>
