<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<table width="100%" style="font-size: 14px" cellpadding="0"
	cellspacing="0">
	<s:iterator value="article">
	<tr height="25px">
		<td width="75%">
			<!-- 每日要情标题 -->
			<a onclick="open1('<s:property value="ToaInfopublishArticle.articlesIssId"/>','${flag1}')" href="#">
			<s:if test="ToaInfopublishArticle.articlesInstructionContent!=null">
				<img src="<%=root%>/oa/image/desktop/littlegif/green.png" title="已批示"/>
			</s:if>
			<s:else>
				<img src="<%=root%>/oa/image/desktop/littlegif/red.png" title="未批示"/>
			</s:else>
			<s:property value="ToaInfopublishArticle.articlesTitle"/>
			</a>
		</td>
		<td width="25%">
		<!-- 每日要情创建时间 -->
			<s:date name="ToaInfopublishArticle.articlesCreatedate" format="yyyy-MM-dd"/>
		</td>
	</tr>
	</s:iterator>
</table>

