<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr height="20px"  valign="top">
	<td>
	<tr height="20px">
		<td align="left">
			<div style="padding:10px; border-bottom:1px dashed #dadada; ">
			  <div style="font-size:12px; color:#666;"><s:property value="dp[0]"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="dp[1]"/>
			  </div>
              <div style="font-size:14px; margin-top:8px; padding:8px; background-color:#f7f7f7; color:#333;word-break:break-all;">
			  &nbsp;&nbsp;&nbsp;&nbsp;<font size="4" ><s:property value="dp[2]"/></font>
			  </div>
            </div>
		</td>
	</tr>
	<s:iterator value="#request.comm" var="comm">
	<tr height="20px">
		<td align="left">
			<div style="padding:10px; border-bottom:1px dashed #dadada; ">
			  <div style="font-size:12px; color:#666;"><s:property value="#comm.commentName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="#comm.commentDate"/>
			  <s:if test="%{#comm.commentIstrue==1}">
			  &nbsp;&nbsp;&nbsp;<a href="#" onclick="javascript:del('<s:property value="#comm.commentId"/>')"><font color="blue">[删除]</font></a>
			  </s:if>
			  </div>
              <div style="font-size:14px; margin-top:8px; padding:8px; background-color:#f7f7f7; color:#333; word-break:break-all;">
			  &nbsp;&nbsp;&nbsp;&nbsp;<font size="4" ><s:property value="#comm.commentInfo"/></font>
			  </div>
            </div>
		</td>
	</tr>
	</s:iterator>
	</table>