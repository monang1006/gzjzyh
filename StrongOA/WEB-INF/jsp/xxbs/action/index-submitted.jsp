<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="submitted">
              <tr>
                <td width="100"><img src="<%=themePath%>/images/icon02.gif" /> <span title="<s:property value="orgName"/>">
                 <s:if test="orgName.length()>4">
                <s:property value="orgName.substring(0,4)"/>..
                </s:if>
                <s:else>
				<s:property value="orgName"/>
				</s:else>
                </span>
                </td><td align="left">
                <a href="#" title="标题：<s:property value="pubTitle"/>&#10;上报单位：<s:property value="orgName"/>&#10;上报时间：<s:property value="pubDate"/>" onclick="publish('<s:property value="pubId"/>')">
                <s:if test="pubTitle.length()>24">
				<s:property value="pubTitle.substring(0,24)" />...
				</s:if>
				<s:else>
				<s:property value="pubTitle"/>
				</s:else>
                </a></td>
                <td width="128" align="right" valign="top"><s:property value="pubDate"/></td>
              </tr>
	</s:iterator>
            </table>
	