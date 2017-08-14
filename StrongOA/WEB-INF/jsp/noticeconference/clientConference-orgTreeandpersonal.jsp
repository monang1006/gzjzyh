<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>个人通讯录</title>
		<%@include file="/common/include/meta.jsp" %>
		<script type="text/javascript">
			function Select(objSelect){
				project_work_content.allSelect(objSelect);
			}
			function removeSelected(objSelectValue){
				project_work_content.removeSelected(objSelectValue);
			}
			
			//得到列表中当前选中的项
			function getCurrentSelectItem(){
				return project_work_content.getCurrentSelectItem();
			}
			
			//得到列表中当前页所有项
			function getCuurentAllItem(){
				return project_work_content.getCuurentAllItem();
			}
			
			function getCurrentSelItes(){
			  return project_work_content.getCheckedInfo();
			}
			
		</script>
	</head>
	<FRAMESET border="0" frameSpacing="0" cols="30%,*" frameBorder="0">
		<FRAME name="project_work_tree" marginWidth="0" marginHeight="0" style="weight:250% height: 100%" src="<%=root %>/noticeconference/clientConference!orgtree.action?isShowAllUser=<%=request.getParameter("isShowAllUser") %>" frameBorder="0" scrolling="no">

		<FRAME name="project_work_content" marginWidth="0" marginHeight="0"
			src="<%=root %>/noticeconference/clientConference!showlist.action"
			frameBorder="0" scrolling="auto">
	</FRAMESET>
	<noframes></noframes>
</html>