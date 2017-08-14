<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>部门文件柜</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	<FRAMESET border="1" frameSpacing="1" cols="20%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="project_work_tree" marginWidth=0 marginHeight=0 style="height: 100%"
			src="<%=root%>/prsnfldr/departmentprsnfldr/departmentPrsnfldrFolder!tree.action" frameBorder="0" scrolling="no">
        <FRAME name="project_work_content" marginWidth="0" marginHeight="0"
			src="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!departmentFileList.action"
			frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
	</HEAD>
</HTML>