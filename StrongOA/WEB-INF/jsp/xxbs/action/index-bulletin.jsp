<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="bulletins">
              <tr>
                <td><img src="<%=themePath%>/images/dx12.gif" /> <a href="#" title="<s:property value="blTitle"/>" onclick="bulletin('<s:property value="blId"/>')">
				<s:if test="blTitle.length()>5">
				<s:property value="blTitle.substring(0,5)" />...
				</s:if>
				<s:else>
				<s:property value="blTitle"/>
				</s:else>
                </a> 
                <s:if test="isRead==false">
                <img src="<%=themePath%>/images/dxnew.gif" />
                </s:if>
                 (<s:date name="blDate" format="yyyy-MM-dd" />)</td>
              </tr>
	</s:iterator>
            </table>
