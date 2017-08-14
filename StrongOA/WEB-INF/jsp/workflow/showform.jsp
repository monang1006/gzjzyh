<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>选择表单</title>
   <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
      <style>
     .pluginInfo1{
        height:30px;
        padding-left:15px;
        padding-top:5px;
     }
      .pluginInfo1 input{
       padding-right:5px;
       }
      </style>
    <script type="text/javascript">      
      		/*
             * 获得催办类型
             */
            function getRemindValue() {
            	var obj = $("input[@type=radio][@checked]");
            	var id = obj.val();
            	if(id!=null && id!=''){
            		var workflowName = obj.attr("processName");
            		var formId = obj.val();
            		 window.returnValue=id + "," + workflowName + ","+obj.attr("processId");
                	 window.close();
            	}else{
            		alert("请选择表单！");
            	}
               
            }     
      
	      function cancel() {
	          window.returnValue="";
	          window.close();
	      }
      
     
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin">
  	<DIV id=contentborder align=center style="overflow:hidden;">
  		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="rendForm" theme="simple"
							action="" method="POST">
							<table height="30" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
								<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>请选择表单</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="getRemindValue();">&nbsp;确&nbsp;定&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="cancel();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
				
								</tr>
							</table>
							
							
								<div style="width:100%; overflow-y:auto; height:230px;">
								<script>
									var parentWin = window.dialogArguments;
									var pluginInfo = parentWin.document.getElementById("pluginInfo");
									
									var info = eval('('+pluginInfo.value+')');
									$.each(info,function(i,json){
										var formId = json.formId;
										var formName = json.formName;
										var flowName = json.flowName;
										var flowId   = json.flowId;
										if(formId!="" && flowName!=""){
											document.write("<div class='pluginInfo1'>");
											document.write("<input type=\"radio\" name=\"processName\" processId=\""+flowId+"\" processName=\""+flowName+"\" value=\""+formId+"\"/>");
											document.write(formName);
											document.write("[");
											document.write(flowName);
											document.write("]");
											document.write("</div>");
										}
									});
								</script>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
                      </div>
							
						</form>
					</td>
				</tr>
			</table>
  	</DIV>
  </body>
</html>