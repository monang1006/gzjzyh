<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
              	<s:iterator value="issue">
              <tr>
                <td><img src="<%=themePath%>/images/icon03.gif" /> 
                <a href="#" title='<s:property value="issTime1"/>')" onclick="journal('<s:property value="issId"/>')" >
                <s:property value="jourName"/> （第<s:property value="issNumber"/>期）
                </a>
              </td>
              </tr>
              	</s:iterator>
            </table>