<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@page import="java.util.List"%>
<%
  //RTX用户信息
  String j_username=request.getParameter("j_username");
  String j_usersign=request.getParameter("j_usersign");
  if(j_usersign!=null&&j_usersign.length()>0)
  {
	  //不要删除，要不会有问题，
	  j_usersign=java.net.URLEncoder.encode(j_usersign);
  }
  //待办事宜
  Object todoList=request.getAttribute("todoDocList");
  List todoDocList=(todoList==null?null:(List)todoList);
%>
<html>
 <head>
  <title>列出待办事宜</title>
  <meta name="Generator" content="EditPlus">
  <meta name="Author" content="">
  <meta name="Keywords" content="">
  <meta name="Description" content="">
  <style type="text/css">
*{ padding:0; margin:0; }
img{ border:none; }
table{ border-collapse:collapse; border-spacing:0; width:100%; }
a{ color: #000; cursor:pointer; text-decoration:none; }
a:hover{ color:#000; text-decoration:underline; }
html{ color: #000; background:#fff; }
body{ font-family:"\5b8b\4f53", sans-serif; font-size:12px; }
.dcon{ background:url(images/drtx02.jpg) repeat-x center top; }
.dclog{ height:39px; background:url(images/drtx01.jpg) no-repeat center 8px; }
.dclogb{ width:124px; height:28px; padding-top:8px; margin:0 auto; }
.dclog input{ float:left; width:62px; height:23px; border:none; background:none; }
.dctit{ height:25px; padding:6px 0 2px 14px; }
.dctit span{ display:block; width:69px; height:25px; line-height:21px; text-align:center; background:url(images/drtx03.jpg) no-repeat center top; color:#fff; }
.dcmain{ background:#fff; padding:10px; }
.dcmain a{ color:#566777; }
.dcmain a:hover{ color:#000; }
.dcmain td{ height:19px; line-height:19px; border-bottom:1px dashed #c0d5e2; } 
</style>
 </head>
<script type="text/javascript">
<!--
  window.onload=function()
  {
	var contentDiv=document.getElementById("contentDiv");
	    contentDiv.style.height=document.body.clientHeight-20;
  }
  //查看待办表单
  function viewProcessed(taskId,instanceId,workflowName)
  {
	  var width=screen.availWidth-10;var height=screen.availHeight-30;
	  var url="<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+workflowName+"&j_flag=rtx&j_username=<%=j_username%>&j_usersign=<%=j_usersign%>";
	  //alert(url);
	  window.open(url,'processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');
  }
//-->
</script>
 <body  >
         <div class="dcmain">
    <table>
     <%
       if(todoDocList!=null)
       {
          //<Object[任务实例id,接收日期,发起人,流程名称,流程实例Id,流程标题,发起人ID,业务ID]
          Object[] obj=null;
		  String docJj="";//急件
          for(int i=0;i<todoDocList.size();i++)
          {
            obj=(Object[])todoDocList.get(i);
            if(obj==null) continue;
			/*if(i!=0)
			  { 
                 out.println("<br/>");
			  }
			
			if("jj_div".equals(listType))
			  {
				  docJj="<b>（急）&nbsp;</b>";
			  }else
			  {
				  docJj="";
			  }
			  
			*/
			 String workflowName=java.net.URLEncoder.encode(java.net.URLEncoder.encode(obj[3].toString(), "utf-8"), "utf-8");
			out.println("<tr><td>");
					
			
            out.println("<img src='"+root+"/oa/image/desktop/littlegif/drtxic.gif'><a href='javascript:void(0);'  onclick='viewProcessed("+obj[0]+","+obj[4]+",\""+workflowName+"\")' >"+obj[5]+"</a>");
         	out.println("</td></tr>");
          }
       }else
	   {
            out.println("<font color='red'>没有待办事宜</font>");
	   }
     %>
     
      </table>
  </div>
 </body>
</html>

