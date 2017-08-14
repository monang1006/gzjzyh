<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.common.workflow.model.TaskBean"/>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>办理记录查看</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<style  media="screen" type="text/css">
		.text-overflow {  
			    display:block;/*内联对象需加*/  
			    width:31em;  
			    word-break:keep-all;/* 不换行 */  
			    white-space:nowrap;/* 不换行 */  
			    overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */  
			    text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/  
			} 
			a:link {text-decoration: underline overline;color: blue;}     /* 未被访问的链接     蓝色 */
			a:visited {color: blue}  /* 已被访问过的链接   蓝色 */
			a:hover {TEXT-DECORATION: underline overline;color: red;}    /* 鼠标悬浮在上的链接 蓝色 */
			a:active {color: blue}   /* 鼠标点中激活链接   蓝色 */ 
		</style>
		
		<script>
	      	function selectNode(nodeid, nodeName, userId, userName){
	      		if(confirm("确定要退回到节点["+nodeName+"]吗？")){
	      			window.returnValue=nodeid+","+userId;//节点id,用户id
	      			window.close();
	      		}/*else{
	      			window.returnValue="";
	      			window.close();
	      	    }*/
	      	 }
      </script>
	</head>

	<body>
		<div id="contentborder">
			${preNodeListHtml }
			<table align="center">
				<tr height="80">
					<td align="center">
						<input type="button" id="btnNo" class="input_bg"
							icoPath="<%=root%>/images/ico/quxiao.gif"
							onclick="window.close()" value="关 闭" />
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
