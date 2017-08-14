<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width = "100%" border="0" cellpadding="0" cellspacing="0">
            	<tr>
					    <th>信息标题</th>
					    <th>报送单位</th>
					    <th>上报时间</th>
					  </tr>
     				<s:iterator value="list1">
					  <tr>
					  	<td width="60%" align="left"><s:property value="pubTitle"/></td>
					  	<td align="left"><s:property value="orgName"/></td>
					  	<td align="center" width="15%"><s:property value="pubDate"/></td>
					  </tr>
					  </s:iterator>
            </table>
	