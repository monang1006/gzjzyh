<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>主办工作列表</title>
    <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
      rel="stylesheet">
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/search.css" type="text/css"
      rel="stylesheet">    
    <script language='javascript'
      src='<%=path%>/common/js/grid/ChangeWidthTable.js'
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"
      type="text/javascript"></script>    
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js"
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
    	//查看
    	function openDoc() {
              var bd = getValue();
              if(bd == ""){
              	alert("请选择要查看的工作！");
              	return ;
              }else{
              	var bids = bd.split(",");
              	if(bids.length>1){
              		alert("一次只能查看一项工作！");
              		return ;
              	}
              }
              var info = getInfo(bd); 
              var instanceId = info[0];
              var bussinessId = info[2];
              var formId = info[3];
              parent.location = "<%=root%>/work/work!viewHostedBy.action?bussinessId="+bussinessId+
                         "&instanceId="+instanceId+"&formId="+formId;
	      }
	       //获取信息
           function getInfo(id){
           	var info = new Array();
         	 <c:forEach items="${pageWorkflow.result}" var="obj" varStatus="status">
         	 	var bid = '<c:out value="${obj[0]}"/>';
         	 	if(bid == id){
         	 		info[0] = '<c:out value="${obj[1]}"/>';//流程实例ID
         	 		info[1] = '<c:out value="${obj[8]}"/>';;//流程结束时间。如果为null说明流程未结束
         	 		info[2] = '<c:out value="${obj[9]}"/>';//业务数据ID
         	 		info[3] = '<c:out value="${obj[10]}"/>';//表单模板ID
         	 	}
         	 </c:forEach>
           	return info;
           }
 		//处理状态（查看流程图）
	      function workflowView(){   
	      	  var bussinessId = getValue();
	      	  if(bussinessId == ""){
              	alert("请选择要查看的工作！");
              	return ;
              }else{
              	var bids = bussinessId.split(",");
              	if(bids.length>1){
              		alert("一次只能查看一项工作！");
              		return ;
              	}
              }
	          var info = getInfo(bussinessId); 
	          var instanceId = info[0];	   
	          var width=screen.availWidth-10;;
	          var height=screen.availHeight-30;
	            var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+instanceId, 
                                   width, height, window);
	      }
    	$(document).ready(function(){
	       //搜索
	       $("#img_search").click(function(){
	       		var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				if(endDate != ""){
					if(date2Number(startDate) > date2Number(endDate)){
						alert("开始日期不能大于结束日期！");
						return ;
					}
				}
	       	$("form").submit();
	       });
    	});
         //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2Number(stime){
         	var arrsDate1 = stime.split('-');
         	stime = arrsDate1[0] + "" + arrsDate1[1] + "" + arrsDate1[2];
         	try{
         		stime = parseInt(stime);
         	}catch(e){}
         	return stime;
         }
      
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
  <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>&nbsp;</td>
                <td width="30%">
                  <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
                    alt="">&nbsp;
                  	主办工作列表
                </td>
                <td>&nbsp;</td>
                <td width="70%">
                  <table border="0" align="right" cellpadding="00" cellspacing="0">
                  	<tr>
                  	 <security:authorize ifAnyGranted="001-0002000100040001, 001-0001000100040001">
                  		<td ><a  class="Operation" href="javascript:openDoc();"><img class="img_s" src="<%=root%>/images/ico/chakan.gif" width="15" height="15" alt="">查看&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
		             </security:authorize> 
		             	<td ><a  class="Operation" href="javascript:workflowView();"><img class="img_s" src="<%=root%>/images/ico/chakanlishi.gif" width="15" height="15" alt="">办理记录&nbsp;</a></td>
		                <td width="5"> </td>
		             <security:authorize ifAnyGranted="001-0002000100040002, 001-0001000100040002">  
		                <td ><a  class="Operation" href="javascript:sendReminder();"><img class="img_s" src="<%=root%>/images/ico/cuiban.gif" width="15" height="15" alt="">催办&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
		             </security:authorize>   
                  	</tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/work/work!hostedby.action">
          <s:hidden id="workflowType" name="workflowType"></s:hidden>
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="aiPiId" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${pageWorkflow.result}"
              page="${pageWorkflow}" pagename="pageWorkflow">
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="4%" align="center" class="biao_bg1">
                    <img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="57%" class="biao_bg1">
                    <s:textfield name="businessName" title="请输入标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="20%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="startDate" width="98%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="20%" align="center" class="biao_bg1">
                   <strong:newdate name="endDate" id="endDate" width="98%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
                  </td>
                  <td align="center" class="biao_bg1">
                    &nbsp;
                  </td>
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="4" width="4%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <webflex:flexTextCol caption="标题" showsize="45"  onclick="getInstance(this.value)" valuepos="0" valueshowpos="4" width="57%" isCanDrag="true"
                isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="主办时间"
                valuepos="2" valueshowpos="2"
                width="40%" isCanDrag="true" showsize="30" isCanSort="true"></webflex:flexDateCol>
            </webflex:flexTable>
            </s:form>
          </td>
        </tr>
      </table>
      
      <div align="center" height="50%">
			<iframe id="blank" name="frame_query" width="100%" src="<%=path %>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0"  scrolling="no"></iframe>
		</div>	
      
    </div>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        <security:authorize ifAnyGranted="001-0002000100040001, 001-0001000100040001">
	        item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","openDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	    </security:authorize>  
        item = new MenuItem("<%=root%>/images/ico/chakan.gif","办理状态","workflowView",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item);
	    <security:authorize ifAnyGranted="001-0002000100040002, 001-0001000100040002">    
	        item = new MenuItem("<%=root%>/images/ico/cuiban.gif","催办","sendReminder",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
        </security:authorize>
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      
      function sendReminder(){
		     var id=getValue();
			 if(checkSelectedOneDis()){
			  var info = getInfo(id); 
			 	var overDate = info[1];
			 	var instanceId = info[0];
			 	if(overDate != "" && overDate!=null){
			 		alert("流程已结束,无需催办！");
			 		return;
			 	}
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=work/work-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
			 }
			}
			
	
  //得到相关信息【通过任务ID和流程ID链接】
function getInstance(workId){
	      var info = getInfo(workId); 
              var instanceId = info[0];
              var bussinessId = info[2];
              var formId = info[3];
               var fullContextPath = $("form").attr("action");
  	var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  	
	document.getElementById('blank').contentWindow.setWorkId("",instanceId,contextPath); 
		//document.getElementById("blank").setWorkId(value);
}
      
    </script>
  </body>
</html>

              <!--<webflex:flexTextCol caption="状态" property="biState"
                showValue="biState" width="10%" isCanDrag="true"
                isCanSort="true"></webflex:flexTextCol>-->