<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="usedReports">
              <tr>
                <td><img src="<%=themePath%>/images/icon02.gif" /> 
                <a href="#" title="<s:property value="rpTitle"/>" onclick="report('<s:property value="rpId"/>')">
                 <s:if test="rpTitle.length()>8">
				<s:property value="rpTitle.substring(0,8)" />...
				</s:if>
				<s:else>
				<s:property value="rpTitle"/>
				</s:else>
                </a></td>
              </tr>
	</s:iterator>
            </table>
