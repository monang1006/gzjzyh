<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>在办工作列表</title>
    <%@include file="/common/include/meta.jsp" %>
    <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/search.css" type="text/css" rel="stylesheet">
    <script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script> 
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
    	$(document).ready(function(){
    		//搜索
	       $("#img_search").click(function(){
	       	$("form").submit();
	       });
    	});
    function showtype(isback, assignType){
    		if("1"==isback){
    			return "退回";
    		}else if("0"==assignType){
    			return "委托";
    		}else if("1"==assignType){
    			return "指派";
    		}else{
    			return "普通";
    		}
    	}
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
    <script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5%" align="center">
                  <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
                    alt="">
                </td>
                <td width="20%">
                  在办工作列表
                </td>
                <td>&nbsp;</td>
                <td width="55%">
                  <table border="0" align="right" cellpadding="00" cellspacing="0">
                  	<tr>
                  		<td ><a class="Operation" href="JavaScript:pendWork();"><img src="<%=root%>/images/ico/banli.gif" width="15" height="15" alt=""  class="img_s">办理&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
		            <td >
						<a class="Operation" href="javascript:workflowView();"><img
								src="<%=root%>/images/ico/chakan.gif" width="15" height="15"
								class="img_s">&nbsp;办理记录&nbsp;</a> 
					</td>
					<td width="5">
		                  &nbsp;
		                </td>
		                <td ><a class="Operation" href="JavaScript:zp();"><img src="<%=root%>/images/ico/weituo.gif" width="15" height="15" alt=""  class="img_s">指派&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
                  	</tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/work/work!workinglist.action">
           <input id="listMode" type="hidden" value="${listMode }"/>
           <s:hidden name="workflowType"></s:hidden>
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="0" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${pageWorkflow.result}"
              page="${pageWorkflow}" pagename="pageWorkflow">
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="3%" align="center" class="biao_bg1">
                    <img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="42%" class="biao_bg1">
                    <s:textfield name="businessName" title="请输入标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" class="biao_bg1">
                    <s:textfield name="userName" title="请输入主办人" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="startDate" width="98%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                    <strong:newdate name="endDate" id="endDate" width="98%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
                  </td>
                  <td width="10%" align="center" class="biao_bg1">
                  		<s:select name="isBackSpace" list="#{'4':'全部','0':'退回','1':'委托','2':'指派','3':'普通'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_search").click();'/>
                  </td>
                  <td width="5%" align="center" class="biao_bg1">
                    &nbsp;
                  </td>
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="6"
                width="3%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <webflex:flexTextCol caption="标题" showsize="45"  onclick="getinfo(this.value)"valuepos="0" valueshowpos="6" isCanDrag="true"
                width="42%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="主办人" valuepos="7" valueshowpos="7" isCanDrag="true"
                width="15%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="接收时间"
                 valuepos="1" valueshowpos="1"
                width="30%" isCanDrag="true" showsize="30" isCanSort="true"></webflex:flexDateCol>
              <webflex:flexTextCol caption="任务类型" valuepos="9" valueshowpos="javascript:showtype(9,10)" isCanDrag="true"
                width="10%" isCanSort="true"></webflex:flexTextCol>
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
	        item = new MenuItem("<%=root%>/images/ico/banli.gif","办理","pendWork",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	    item = new MenuItem("<%=root%>/images/ico/chakan.gif","办理状态 ","workflowView",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item);       
	        item = new MenuItem("<%=root%>/images/ico/weituo.gif","指派","zp",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      //获取选中的信息
      function getInfo(id){
          var info = new Array();
          <c:forEach items="${pageWorkflow.result}" var="obj" varStatus="status">
         	var bid = '<c:out value="${obj[0]}"/>';//任务实例ID
         	if(bid == id){
         	   info[0] = '<c:out value="${obj[3]}"/>';//流程实例ID
         	   info[1] = '<c:out value="${obj[8]}"/>';//流程类型ID
         	   info[2] = '<c:out value="${obj[6]}"/>';//标题
         	 }
          </c:forEach>
          return info;
       }
       //指派
       function zp(){
       	var taskId = getValue();
       	if(taskId == ""){
       		alert("请选择要指派的工作！");
       		return ;
       	}else{
       		if(taskId.split(",").length>1){
       			alert("一次只能指派一份工作！");
       			return;
       		}
       	}
       	$.post("<%=root%>/work/work!checkCanReturn.action",
       			{taskId:taskId},
       			function(data){
       				if(data == "-1"){
       					alert("此任务不存在或已删除，操作失败！");
       				}else if(data == "-2"){
       					alert("操作失败，请与管理员联系！");
       				}else{
       					var flags = data.split("|");
       					var flagzp = flags[2];//指派
       					if(flagzp == "1"){//允许指派
       						var taskActors = "";
							var url = scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=reassign&nodeId=0&taskId=" + taskId + "&taskActors=" + taskActors;
							var userstr = OpenWindow(url, 420, 450, window);          
							//用户ID|用户名称,指派是否需要返回（0：否、1：是）
							if(userstr!=null && userstr!=''){
								var info = getInfo(taskId);
             					var instanceId   = info[0];
             					var workflowType = info[1];
             					var bussinessTitle = info[2];
								$.post("<%=root%>/work/work!reAssign.action",{taskId:taskId,suggestion:encodeURI(userstr),businessName:encodeURI(bussinessTitle)},
									   function(retCode){
									   		if(retCode == "0"){
									   			alert("任务指派成功！");
									   			parent.location = "<%=root%>/fileNameRedirectAction.action?toPage=/work/work-doingmain.jsp";
									   		}else if(retCode == "-1"){
									   			alert("任务实例不存在或已删除！");
									   		}else if(retCode == "-2"){
									   			alert("指派过程中出现异常！");
									   		}else if(retCode == "-3"){
									   			alert("参数传输错误！");
									   		}
									   }			
								);
							}
       					}else if(flagzp == "0"){//不允许指派
       						alert("对不起，此任务不允许指派！");
       					}else{
       						alert("对不起，出现未知错误！请与管理员联系！");
       					}
       				}
       			});
       }
       //办理状态
 	   function workflowView(){
 	   	var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要查看的工作！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一项工作！");
          		return ;
          	}
          } 
          var info = getInfo(taskId);
          var instanceId   = info[0]; 
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          //var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+instanceId, 
          //                         width, height, window);
           var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+instanceId+"&taskId="+taskIds, 
                                   width, height, window);
 	   }	
       //办理
       	/**
		 * 判断目前任务是否可被当前用户处理
		 * @author:邓志城
		 * @date:2009-12-17 下午04:00:25
		 * @return
		 * 	1、f1|该任务正在被其他人处理或被挂起
		 * 	2、f2|该任务已被取消
		 * 	3、f3|该任务已被其他人处理
		 * 	4、f4|可被处理
		 * 	5、任务实例不存在返回-1
		 * 	6、抛出异常返回-2
		 * @throws Exception
		 */		
       function pendWork() {
             var taskId = getValue();
             if(taskId == ""){
             	alert("请选择要办理的工作！");
             	return;
             }else{
             	if(taskId.split(",").length>1){
             		alert("一次只能办理一项工作！");
             		return ;
             	}
             }
             $.post("<%=root%>/work/work!judgeTaskIsDone.action",
             		{taskId:taskId},
             		function(data){
             			if(data == "-1"){
             				alert("此任务不存在或已删除！");
             				return ;
             			}else if(data == "-2"){
             				alert("操作异常！");
             				return ;
             			}
             			var info = data.split("|");
             			var ch = info[0];
             			var infomation = info[1];
             			if(ch == 'f1'){
             				alert(infomation);//需求待定
             			}else if(ch == 'f2'){
             				alert(infomation);
             			}else if(ch == 'f3'){
             				alert(infomation);
             			}else if(ch == 'f4'){
             				var info = getInfo(taskId);
             				var instanceId   = info[0];
             				var workflowType = info[1];
             				var bussinessTitle = info[2];
							switch (workflowType){
								case '1'://栏目类型
									break;
								case '2'://发文
									parent.location = "<%=root%>/senddoc/sendDoc!form.action?taskId="+taskId+"&instanceId="+instanceId;
									break;
								case '3'://收文
									parent.location = "<%=root%>/recvdoc/recvDoc!form.action?taskId="+taskId+"&isStartWorkflow=0"+"&instanceId="+instanceId+"&workflowType="+workflowType;
									break;
								case '4'://督察督办
									parent.location = "<%=root%>/inspect/inspect!input.action?taskId="+taskId+"&listMode="+$("#listMode").val()+"&instanceId="+instanceId;
									break;
								case '5'://会议管理
									OpenWindow("<%=root%>/meetingmanage/meetingaudit/meetingaudit!nextstep.action?taskId="+taskId+"&instanceId="+instanceId+"&timestampt="+new Date().getTime(), 
		                                   400, 300, window);
									break;
								case '6'://提案建议
									break;
								case '7'://信访管理
									break;
								case '8'://值班管理
									break;
								case '9'://信息处理
									break;
								case '10'://新闻管理
									break;
								case '11'://签报管理
									break;
								case '12'://档案管理
									break;	
								default:
									parent.location = "<%=root%>/work/work!input.action?taskId="+taskId+"&listMode=4"+"&instanceId="+instanceId+"&businessName="+encodeURI(encodeURI(bussinessTitle));
									break;										
							}
							 
             			}
             		});
            
       }
       
    function getInstanceId(taskId){
      	var info = new Array();
      	<s:iterator value="pageWorkflow.result" var="obj">
      		if(taskId == '${obj[0]}'){
      			info[0] = '${obj[3]}';//流程实例id
      			info[1] = taskId;
      		}
      	</s:iterator>
      	return info ;
      }
   //得到相关信息【通过任务ID和流程ID链接】
function getinfo(workId){
	var info = getInstanceId(workId);
	var instanceId = info[0];
	 var fullContextPath = $("form").attr("action");
  	var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
	//alert(instanceId);
	document.getElementById('blank').contentWindow.setWorkId(workId,instanceId,contextPath); 
		//document.getElementById("blank").setWorkId(value);
}
    </script>
  </body>
</html>
