<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择流程处理人</title>
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <style media="screen" type="text/css">
    .tabletitle {
      FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    }
    
    .hand {
      cursor:pointer;
    }
    </style>
    <script type="text/javascript">
      function submit() {
        var strTemp = "";
        var strNodeId = $("#nodeId").val();
        var option = document.getElementById("taskActors").options;        
        for (var i=0; i<option.length; i++) {
          if (option[i].selected) {
            var str = option[i].value;
            var strUser = str.split(",");
            strTemp += strUser[0]+"|"+strNodeId+",";
          }
        }
        
        var strRet = strTemp.substring(0, strTemp.length-1);
        if (strRet.split(",").length > $("#maxTaskActors").val()) {
          alert("只能选择"+$("#maxTaskActors").val()+"个处理人!");
          return;
        }
        
        window.returnValue = strRet;
        window.close();
      }
      
      function cancel() {
        window.returnValue = "";
        window.close();
      }
    </script>
	</head>
	<body  class="contentbodymargin" oncontextmenu="return false;">
	<s:hidden name="isSelectOtherActors"></s:hidden>
	<s:hidden name="maxTaskActors"></s:hidden>
	<s:hidden name="nodeId"></s:hidden>
	<s:hidden name="nodeName"></s:hidden>
	<s:hidden name="taskId"></s:hidden>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td>
											<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;
									选择流程处理人
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center">
					  请选择处理人(<s:property value="maxTaskActors" />):<br>
						<select id="taskActors" multiple="multiple">
							<s:iterator value="taskActors">
								<option value="<s:property />"><s:property /></option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr>
          <td align="center">
            <input type="button" id="last" value="确定"
            onclick="submit();" class="input_bg">
            <input type="button" id="last" value="取消"
            onclick="cancel();;" class="input_bg">
          </td>
        </tr>
			</table>
		</div>
	</body>
</html>