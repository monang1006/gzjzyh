<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="used">
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
                <a href="#" title="标题：<s:property value="pubTitle"/>&#10;采用期刊、期号：<s:property value="jourName"/>，第<s:property value="issNumber"/>期&#10;采用时间：<s:property value="pubSubmitDate"/>" onclick="publish('<s:property value="pubId"/>')">
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
	