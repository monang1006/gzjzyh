<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
共&nbsp;<s:property value="splitPage.totalPage" />&nbsp;页&nbsp;&nbsp;&nbsp;&nbsp;
共&nbsp;<s:property value="splitPage.totalRecords" />&nbsp;条记录&nbsp;&nbsp;&nbsp;&nbsp;
当前页:&nbsp;<s:property value="splitPage.currentPage" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<s:if test="splitPage.currentPage>1">
	<A href="javascript:gotoCurrentPage('1')">首页</A>&nbsp;
</s:if>
<s:if test="splitPage.currentPage<1">
	首页&nbsp;
</s:if>
<s:if test="splitPage.currentPage>1">
	<A href="javascript:gotoCurrentPage('<s:property value="splitPage.currentPage-1"/>')">上一页</A>&nbsp;
</s:if>
<s:if test="splitPage.currentPage<1">
	上一页&nbsp;
</s:if>
第
<select class="conditionSelectBox" name='pageNo'
	onChange='javascript:gotoCurrentPage(this.value)'>

	<s:if test="splitPage.totalPage >1">
		<%--<s:iterator value="splitPage.totalPage" status="status" var="">
			<s:if test="splitPage.currentPage==#status.index">
				<option value='<s:property value="#status.index"/>'
					selected="selected">
					<s:property value="i"></s:property>
				</option>
			</s:if>
			<s:if test="${splitPage.currentPage!=i}">
				<option value='<s:property value="${i}"></s:property>'>
					<s:property value="${i}"></s:property>
				</option>
			</s:if>
		</s:iterator>
	--%></s:if>
	<s:else>
		<option value="1">
			1
		</option>
	</s:else>
	</c:choose>
</select>
页

<s:if test="splitPage.currentPage< splitPage.totalPage">
	<A href="javascript:gotoCurrentPage('<s:property value="splitPage.currentPage+1"/>')">下一页</A>&nbsp;
</s:if>
<s:if test="splitPage.currentPage>=splitPage.totalPage">
	下一页&nbsp;
</s:if>
<s:if test="splitPage.currentPage< splitPage.totalPage">
	<A href="javascript:gotoCurrentPage('<s:property value="splitPage.totalPage"/>')">尾页</A>
</s:if>
<s:if test="splitPage.currentPage>=splitPage.totalPage">
	尾页
</s:if>

<input type="hidden" name="pageInfo" value="<s:property value="splitPage.currentPage"/>">

<script language="javascript" charset="GBK">
	function gotoCurrentPage(pageNo) {
		var pageE = document.getElementById("currentPage");
                pageE.value = pageNo;
        	document.forms[0].submit();
	}
</script>


