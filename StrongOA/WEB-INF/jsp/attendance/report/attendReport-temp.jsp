<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
		<script language="javascript">
		 window.dialogArguments.parent.frames[1].onsub("","${beginTime}","${endTime}","");
		//window.dialogArguments.parent.SearchContent1.document.location="<%=path%>/attendance/report/attendReport!dateReport.action?beginTime=${beginTime}&endTime=${endTime}";
		  window.close();
		</script>
  </head>
</html>
