<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
            <table width="100%">
    <s:iterator value="pubDate">
              <tr>
                <td> <img src="<%=themePath%>/images/icon02.gif" /> <a href="#" onclick="pubReort('<s:property value="pubSubmitDate"/>')"><s:property value="pubSubmitDate"/></a></td>
              </tr>
	</s:iterator>
            </table>
	