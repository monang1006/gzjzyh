<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-statictree" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
<HTML><HEAD><TITLE>类目树</TITLE>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<LINK href="<%=path %>/common/css/windows.css" type=text/css rel=stylesheet>
	<LINK href="<%=path %>/common/css/treeview.css" type=text/css rel=stylesheet>
	<SCRIPT src="<%=path %>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
</HEAD>
<BODY>
<DIV id=treecontentborder>
<table border="0" cellpadding="0" cellspacing="0" width="95%" height="100%">
<tr>
<td valign=top>
	<s:tree iconpath="common/images/" title="档案类目" target="folderSortList">
		<s:node name="党委资料" id="11">
			<s:node name="党委会议" url="records_management/archives/folder/folderList.jsp" id="12"/>
			<s:node name="党委决定" url="records_management/archives/folder/folderList.jsp" id="13"/>
			<s:node name="党委决定" url="records_management/archives/folder/folderList.jsp" id="14"/>
			<s:node name="党委决定" url="records_management/archives/folder/folderList.jsp" id="15"/>
		</s:node>
		<s:node name="办公室资料" id="21">
			<s:node name="厅会议资料" url="records_management/archives/folder/folderList.jsp" id="22"/>
			<s:node name="内部会议资料" url="records_management/archives/folder/folderList.jsp" id="23"/>
		</s:node>
	</s:tree>
<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle" >
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="29%">
									<input name="Submit" type="submit"class="input_bg" value="确 定">
								</td>
								<td width="37%">
									<input name="Submit2" type="submit" class="input_bg" value="取 消" onclick="window.close();">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
</td>
</tr>
</table></DIV>
</BODY></HTML>
