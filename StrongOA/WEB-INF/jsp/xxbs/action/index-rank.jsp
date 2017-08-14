<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
              <tr>
                <th width="34">序号</th>
                <th>报送单位</th>
                <th width="34">总分</th>
              </tr>
              <%
			  	int i=1;
			  %>
    <s:iterator value="list4" var="data">
              <tr>
              	<td align="center"><%=i %></td>
                <td>
                <s:if test="#data[1].length()>6">
                <s:property value="#data[1].substring(0,4)"/>..
                </s:if>
                <s:else>
				<s:property value="#data[1]"/>
				</s:else>
                </td>
                <td align="center"><s:property value="#data[2]"/></td>
              </tr>
              <%
			  i++;
			  %>
 	</s:iterator>
            </table>
            