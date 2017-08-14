<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>查看流程图</title>
    <link href="<%=frameroot%>/css/windows.css" type="text/css"
      rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script> 
      <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
      <script>
      	function selectNode(nodeid, nodeName){
      	 var taskId= $("#taskId").val();
      	 var type=$("#type").val();
      	 if(type=="return"){//退回
      		if(confirm("确定要退回到节点["+nodeName+"]吗？")){
      		$.ajax({
						type:"post",
						url:"<%=path%>/senddoc/sendDocWorkflow!preNodeIsplugins_notbackspace.action",
						data:{
							nodeid:nodeid,
							taskId:taskId,
							nodeName:nodeName			
						},
						success:function(data){	
							if(data!=null&&data=="0"){
								alert("该节点["+nodeName+"]是不可退回的节点，不能退回此节点。");
							}
							else{
						       		window.returnValue=nodeid;
      		                        window.close();
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
				}

      		}
      	if(type=="bohui"){//驳回
      		if(confirm("确定要驳回到节点["+nodeName+"]吗？")){
      		$.ajax({
						type:"post",
						url:"<%=path%>/senddoc/sendDocWorkflow!preNodeIsplugins_notoverrule.action",
						data:{
							nodeid:nodeid,
							taskId:taskId,
							nodeName:nodeName			
						},
						success:function(data){	
							if(data!=null&&data=="0"){
								alert("该节点["+nodeName+"]是不可驳回的节点，不能驳回此节点。");
							}
							else{
						       		window.returnValue=nodeid;
      		                        window.close();
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
				}

      		}
      		/*else{
      		window.returnValue="";
      		window.close();
      	    }*/
      	 }
      </script>
  </head>
  <body class="contentbodymargin">
    <div id="contentborder" align="center">
    <input type="hidden" id="taskId" name="taskId" value="<%=request.getParameter("taskId")%>">
   <input type="hidden" id="type" name="type" value="<%=request.getParameter("type")%>">
      <table width="98%">
        <tr>
          <td height="5"></td>
        </tr>
      </table>
      <table width="98%">
        <tr>
          <td align="center">
            <%
            String task = (String) request.getParameter("taskId");//任务ID
            String type = (String) request.getParameter("type");//回退还是驳回;return|bohui
            %>
             <strongit:processReturn task="<%=Long.parseLong(task)%>" type="<%=type%>" />
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>