<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat"/>
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager"/>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>
			<%
				String title = "我委派的流程";
				String assignType = (String)request.getAttribute("assignType");
				if("0".equals(assignType)){
					title = "我委派的流程";
				}else if("1".equals(assignType)){
					title = "我指派的流程";
				}
				out.println(title);
				System.out.println("assignType:"+assignType);
			%>
		</title>
    <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
      rel="stylesheet">
   <LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css 
   	  rel=stylesheet>
    <script language='javascript'
      src='<%=path%>/common/js/grid/ChangeWidthTable.js'
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
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
    
      //重定向到此页面
      function reloadPage() {
      	window.location = "<%=root%>/senddoc/sendDoc!entrust.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val();
      }	
     
      //处理  
      function processDoc() {
      /**/
      	var TaskIdAndInstanceId = getValue();
          if(TaskIdAndInstanceId == ""){
          	alert("请选择要查看的流程！");
          	return ;
          }else{
          	var taskIds = TaskIdAndInstanceId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一个流程！");
          		return ;
          	}
          }  
      	TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&taskId="+taskId,'entrust',width, height);
      
      /* 
          var bussinessId = getValue();
              if(bussinessId == ""){
              	alert("请选择要查看的流程！");
              	return ;
              }else{
              	var bids = bussinessId.split(",");
              	if(bids.length>1){
              		alert("一次只能查看一个流程！");
              		return ;
              	}
              }
              var result = bussinessId.split("$");
			  var instanceId = result[0];
              var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=OpenWindow("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+result[1],width, height, window);
   			  var ret=OpenWindow("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+result[1],width, height, window);
     */
      }
       //处理状态（查看流程图）
      function workflowView(){
	      var TaskIdAndInstanceId = getValue();
          if(TaskIdAndInstanceId == ""){
          	alert("请选择要查看的流程！");
          	return ;
          }else{
          	var taskIds = TaskIdAndInstanceId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一个流程！");
          		return ;
          	}
          }  
          TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		  var taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId, 
                                   width, height, window);
      }
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
  <script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
         <td colspan="3" class="table_headtd">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
								<strong>${workflowName }</strong>
								</td>
								<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
									<tr>
										<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="processDoc();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
										</tr>
								</table>
								</td>
							</tr>
						</table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/senddoc/sendDoc!entrust.action">
                       <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="senddocId" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByProperty" collection="${page.result}"
              page="${page}">
              	<!-- 流程名称 -->
          		<s:hidden id="workflowName" name="workflowName"></s:hidden>
          		<!-- 表单id -->
          		<s:hidden id="formId" name="formId"></s:hidden>
          		<!-- 表名称 -->
          		<s:hidden id="tableName" name="tableName"></s:hidden>
          		<!-- 流程类型 -->
          		<s:hidden name="workflowType"></s:hidden>
          		<!-- 委托类型 -->
          		<s:hidden name="assignType"></s:hidden>
              <strong:query queryColumnList="${queryColumnList}"/>
              <%
              	List showColumnList = (List)request.getAttribute("showColumnList");
                double size = showColumnList.size()-1;//减掉第一列（主键列）
                String width = "20%";
                NumberFormat nf = NumberFormat.getPercentInstance();
                if(size > 0) {
                	size = 96/size;
                	size = size/100;
                	width = nf.format(size);
                }
                System.out.println(width);
                String pkFieldName = "";
              	for(int i=0;i<showColumnList.size();i++){
              		if(i==showColumnList.size()-1){
              			width="100%";
          			}
              		
              		width = "10%";
              		
              		Object[] column = (Object[])showColumnList.get(i);
              		String columnName = (String)column[0];
              		String showColumn = (String)column[3];
              		String caption = (String)column[1];
              		String type = (String)column[2];//字段类型
              		if(i == 0){
              			pkFieldName = columnName;
              %>
              <webflex:flexCheckBoxCol caption="选择" property="<%=columnName %>"
	                showValue="<%=showColumn %>" width="4%" isCheckAll="true"
	                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <%			
              		} else if("5".equals(type) || "6".equals(type)){//日期类型
             %>
             <webflex:flexDateCol caption="<%=caption %>"
                 property="<%=columnName %>" showValue="<%=showColumn %>"
                width="<%=width %>" isCanDrag="true" dateFormat="yyyy-MM-dd" isCanSort="true"></webflex:flexDateCol>
             <%			
              		} else {
              			if(BaseWorkflowManager.WORKFLOW_TITLE.equals(columnName)){//标题字段
            %> 
            <webflex:flexTextCol caption="<%=caption %>" property="<%=pkFieldName %>"
          	                showValue="<%=showColumn %>" width="66%"
          	                isCanDrag="false" onclick="ViewFormAndWorkflow(this.value);" isCanSort="false"></webflex:flexTextCol>
             <%					
              			}else{
              				
          %> 
          				<webflex:flexTextCol caption="<%=caption %>" property="<%=columnName %>"
          	                showValue="<%=showColumn %>" width="<%=width %>"
          	                isCanDrag="false" isCanSort="false"></webflex:flexTextCol>
          <%				
              			}
              		}
              	}
              %>
              
            </webflex:flexTable>
           </s:form>
          </td>
        </tr>
      </table>
      <%--
      
      <div align="center" height="50%">
			<iframe id="blank" name="frame_query" width="100%" src="<%=path %>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0"  scrolling="no"></iframe>
		</div>	
    --%>
    </div>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","processDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理状态","workflowView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }

	/**
		@param TaskIdAndInstanceId 任务id$流程实例id
	*/
	function ViewFormAndWorkflow(TaskIdAndInstanceId) {
		TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&taskId="+taskId,'entrust',width, height);
	}
	/**
		@param TaskIdAndInstanceId 任务id$流程实例id
	
	function ViewFormAndWorkflow(TaskIdAndInstanceId) {
		TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
		var fullContextPath = $("form").attr("action");
  		var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  		document.getElementById('blank').contentWindow.setWorkId(taskId,instanceId,contextPath); 
	}
      */
    </script>
  </body>
</html>
