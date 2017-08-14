<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>组织机构树</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			$(document).ready(function() {
				var param = window.dialogArguments;
			});
			
			var imageRootPath='<%=path%>/common/frame';
			function doSubmit(){
				var name = new Array();
				var id = new Array();
				$(":checked").each(function(){
				    var checkid=this.id.substring(4);
				    var checknode=tree.node[checkid];
				    
				    //只收集叶子节点
				    if(!checknode.hasChild){
						 id.push($(this).val());
					     name.push($(this).next().text());
					}
				});			
				/*if(name.length == 0){
					alert("请选择数据(不含有子节点的数据)！");
					return ;
				}*/
				var param = window.dialogArguments;
				var zf = "，";
				if(param){
					if(typeof(param) == "string"){
						zf = param;
					}
				}
				window.returnValue =[name.join(zf),id.join(zf)];
				window.close();
			}
 
			
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">

		<DIV id=contentborder cellpadding="0">
			<div align="center">
				<input type="button" class="input_bg" onclick="doSubmit()"
					value="确 定" />
				&nbsp;&nbsp;
				<input type="button" class="input_bg" onclick="window.close()"
					value="取 消" />
			</div>
			<tree:strongtree title="组织结构" check="true"
				dealclass="com.strongit.oa.address.util.OrgTreeDeal"
				data="${orgList}" target="project_work_content" rootcheck="false" hascheckedvalues="${depIds}"/>
			<div align="center">
				<input type="button" class="input_bg" onclick="doSubmit()"
					value="确 定" />
				&nbsp;&nbsp;
				<input type="button" class="input_bg" onclick="window.close()"
					value="取 消" />
			</div>
		</DIV>
	</BODY>
</HTML>
