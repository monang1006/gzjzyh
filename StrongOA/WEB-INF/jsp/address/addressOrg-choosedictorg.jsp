<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>组织机构树</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
			function doSubmit(){
				var ret = new Array();
				$(":checked").each(function(){
					ret.push($(this).next().text());
				});			
				if(ret.length == 0){
					alert("请选择数据！");
					return ;
				}
				var param = window.dialogArguments;
				var zf = "、";
				if(param){
					if(typeof(param) == "string"){
						zf = param;
					}
				}
				window.returnValue = ret.join(zf);
				window.close();
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		
		<DIV id=contentborder cellpadding="0">
			<div align="center">
				<input type="button" class="input_bg" onclick="doSubmit()" value="确 定"/>
				&nbsp;&nbsp;
				<input type="button" class="input_bg" onclick="window.close()" value="取 消"/>
			</div>
			<tree:strongtree title="${root}"  check="true" chooseType="chooseOne" dealclass="com.strongit.oa.address.util.OrgTreeDeal" data="${orgList}" target="project_work_content"  />	
			<div align="center">
				<input type="button" class="input_bg" onclick="doSubmit()" value="确 定"/>
				&nbsp;&nbsp;
				<input type="button" class="input_bg" onclick="window.close()" value="取 消"/>
			</div>
		</DIV>
	</BODY>
</HTML>
