<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="invitation">
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" />
				<a href="#" title="<s:property value="aptTitle"/>" onclick="invitation('<s:property value="aptId"/>')">
				<s:if test="aptTitle.length()>15">
				<s:property value="aptTitle.substring(0,15)" />...
				</s:if>
				<s:else>
				<s:property value="aptTitle"/>
				</s:else>
                </a></td>
              </tr>
	</s:iterator>
            </table>
