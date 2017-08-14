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
<%--		<style>--%>
<%--		/*-= UserCard =-*/--%>
<%--		div.UserCard {--%>
<%--			position: absolute;--%>
<%--			z-index: 999;--%>
<%--			width: 357px;--%>
<%--			height: 194px;--%>
<%--			background: url('<%=path%>/workflow/images/bgUserCard.gif') no-repeat;--%>
<%--		}--%>
<%--		</style>--%>
<%--		<script type="text/javascript">--%>
<%--    var flag=0;  //显示为节点的id--%>
<%--     	--%>
<%--    function change(i,obj){--%>
<%--		var ob=document.getElementById(obj);--%>
<%--		if(i==1){--%>
<%--			ob.style.border="1px solid blue";--%>
<%--		}else if(i==2){--%>
<%--			ob.style.border="1px solid gray";--%>
<%--		}--%>
<%--	}--%>
<%--	--%>
<%--	//避免html元素中子元素对鼠标进出事件的影响（兼容FireFox）--%>
<%--	function contains(obj2,obj) {--%>
<%--		while(obj != null && typeof(obj.tagName) != "undefind"){--%>
<%--			if(obj == obj2)--%>
<%--				return true;--%>
<%--			obj = obj.parentNode;--%>
<%--		}    --%>
<%--		return false;--%>
<%--	}--%>
<%----%>
<%--	//总体处理鼠标进入节点事件--%>
<%--	function mouseInNode(obj,nodeId,i,myevent,flag){--%>
<%--		var obj2;--%>
<%--		if(typeof(event)!='undefined'){//IE情况下--%>
<%--			obj2 = event.fromElement;--%>
<%--			if(!contains(obj,obj2)){--%>
<%--				viewCurrentHandle(obj,nodeId,i,event,flag);--%>
<%--			}--%>
<%--		}else{ //FireFox情况下--%>
<%--			obj2 = myevent.relatedTarget;--%>
<%--			if(!contains(obj,obj2)){--%>
<%--				viewCurrentHandle(obj,nodeId,i,myevent,flag);--%>
<%--			}--%>
<%--		}--%>
<%--	}--%>
<%--	--%>
<%--	//总体处理鼠标移出事件--%>
<%--	function mouseOutNode(obj,myevent){--%>
<%--/**		alert("out event");--%>
<%--		var obj2;--%>
<%--		if(typeof(event)!='undefined'){//IE情况下--%>
<%--			obj2 = event.toElement;--%>
<%--			alert(obj.id);--%>
<%--			alert(obj.tagName);--%>
<%--			alert(obj2.name);--%>
<%--			alert(obj2.tagName);--%>
<%--			alert(obj2.id);--%>
<%--			if(obj2.id != 'processImage'){--%>
<%--				if(!contains(obj,obj2)){--%>
<%--					changeDisplay();--%>
<%--				}--%>
<%--			}--%>
<%--		}else{ //FireFox情况下--%>
<%--			obj2 = myevent.target;--%>
<%--			if(obj2.id != 'processImage'){--%>
<%--				if(!contains(obj,obj2)){--%>
<%--					changeDisplay();--%>
<%--				}--%>
<%--			}--%>
<%--		}**/		--%>
<%--	}	--%>
<%--	--%>
<%--/**	--%>
<%--	function changeProcess(obj,i,event){--%>
<%--		if(event.srcElement.tagName=="roundrect" && flag!=event.srcElement.id){--%>
<%--			var divx=document.getElementById("info");--%>
<%--			//alert(parseInt(event.srcElement.style.left)+parseInt(event.srcElement.style.width)/2+document.getElementById("contentborder").scrollLeft);--%>
<%--			divx.style.left=parseInt(event.srcElement.style.left)+parseInt(event.srcElement.style.width)/2+document.getElementById("processImage").offsetLeft;--%>
<%--			divx.style.top=parseInt(event.srcElement.style.top)+parseInt(event.srcElement.style.height)/2+document.getElementById("processImage").offsetTop;--%>
<%--			var processId=document.getElementsByName("processId")[0].value;--%>
<%--			var framex=document.frames['nodeinfo'];--%>
<%--			framex.location = scriptroot+"/workflowRun/action/processStatus!handle.action?processId="+processId+"&nodeId="+obj;--%>
<%--			--%>
<%--			divx.style.display="block";		--%>
<%--			flag=event.srcElement.id;--%>
<%--		}--%>
<%--	}--%>
<%--**/--%>
<%--	--%>
<%--	function viewCurrentHandle(obj,nodeId,i,event,flag){--%>
<%--//		if(event.srcElement.tagName=="roundrect" && flag!=event.srcElement.id){--%>
<%--			var divx=document.getElementById("info");--%>
<%--			//alert(parseInt(event.srcElement.style.left)+parseInt(event.srcElement.style.width)/2+document.getElementById("contentborder").scrollLeft);--%>
<%--			divx.style.left=parseInt(obj.style.left)+parseInt(obj.style.width)/2+document.getElementById("processImage").offsetLeft;--%>
<%--			divx.style.top=parseInt(obj.style.top)+parseInt(obj.style.height)/2+document.getElementById("processImage").offsetTop;--%>
<%--			var processId=document.getElementsByName("processId")[0].value;--%>
<%--			var framex=document.frames['nodeinfo'];--%>
<%--			if(flag == 'handle'){--%>
<%--				framex.location = scriptroot+"/workflowRun/action/processStatus!handle.action?processId="+processId+"&nodeId="+nodeId;--%>
<%--			}else if(flag == 'current'){--%>
<%--				framex.location = scriptroot+"/workflowRun/action/processStatus!current.action?processId="+processId+"&nodeId="+nodeId;--%>
<%--			}--%>
<%--			--%>
<%--			divx.style.display="block";		--%>
<%--			flag=event.srcElement.id;--%>
<%--//		}--%>
<%--	}	--%>
<%--	--%>
<%--	--%>
<%--	function changeDisplay(){--%>
<%--		var divx=document.getElementById("info");--%>
<%--		divx.style.display="none";--%>
<%--		flag=0;--%>
<%--	}	--%>
<%--		--%>
<%--		// 改变流程状态，挂起、恢复、结束--%>
<%--        function changeStatus(processId,flag) {--%>
<%--			//判断流程名称是否冲突--%>
<%--		  $.ajax({ url: scriptroot + "/workflowDesign/action/processMonitor!status.action",--%>
<%--             type:"post",--%>
<%--             dataType:"text",--%>
<%--             data: "processId=" + processId + "&flag=" + flag,--%>
<%--             success:function(msg){ --%>
<%--                alert(msg);--%>
<%--                location = scriptroot+ "/workflowDesign/action/processMonitor!input.action?instanceId=" + processId;--%>
<%--             } --%>
<%--          });            --%>
<%--        }	--%>
<%--        --%>
<%--        function changeInstance(processId,nodeId) {--%>
<%--            var strDid = "";--%>
<%--            var p = new Array();--%>
<%--            strDid += processId;--%>
<%--            p[0] = strDid;--%>
<%--            p[1] = nodeId;--%>
<%--            var buffalo = new Buffalo(scriptroot+"/ajaxAction.do");--%>
<%--            buffalo.remoteCall("processManagerService.changeProInstance", p,--%>
<%--            function(reply){--%>
<%--               var result = reply.getResult();--%>
<%--               alert(result);--%>
<%--               location=scriptroot+ "/workflowmanager/viewMonitorData.do?instanceId="+strDid;--%>
<%--             });  --%>
<%--        }--%>
<%--                	--%>
<%--		//挂起流程--%>
<%--		function suspendPro(){--%>
<%--			var status=document.getElementsByName("statustd")[0].innerHTML;--%>
<%--			if(status=="&nbsp;暂停"){--%>
<%--				alert("流程已经处于挂起状态！");--%>
<%--			}else if(status=="&nbsp;结束"){--%>
<%--				alert("该流程已经结束！");--%>
<%--			}else{--%>
<%--				var processId=document.getElementsByName("processId")[0].value;--%>
<%--				changeStatus(processId,"1");--%>
<%--			}--%>
<%--		}--%>
<%--		//恢复流程--%>
<%--		function resumePro(){--%>
<%--			var status=document.getElementsByName("statustd")[0].innerHTML;--%>
<%--			if(status=="&nbsp;执行"){--%>
<%--				alert("流程已经处于运行状态！");--%>
<%--			}else if(status=="&nbsp;结束"){--%>
<%--				alert("该流程已经结束！");--%>
<%--			}else{--%>
<%--				var processId=document.getElementsByName("processId")[0].value;--%>
<%--				changeStatus(processId,"2");--%>
<%--			}			--%>
<%--		}--%>
<%--		//结束流程--%>
<%--		function endPro(){--%>
<%--			var status=document.getElementsByName("statustd")[0].innerHTML;--%>
<%--			if(status=="&nbsp;结束"){--%>
<%--				alert("流程已经被结束！");--%>
<%--			}else{--%>
<%--				var processId=document.getElementsByName("processId")[0].value;--%>
<%--				changeStatus(processId,"3");				--%>
<%--			}			--%>
<%--		}--%>
<%--</script>--%>

	</head>
	<body class="contentbodymargin">
		<!--  oncontextmenu="return false;"> -->
		<div id="contentborder" align="center">

<%--			<div id="info" name="info" class="UserCard" style="display: none">--%>
<%--				<table width="100%" height="100%">--%>
<%--					<tr>--%>
<%--						<td height="10"></td>--%>
<%--						<td width="5"></td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td align="right" height="160">--%>
<%--							<iframe name="nodeinfo" id="nodeinfo" width="83%" height="100%"--%>
<%--								border="0"></iframe>--%>
<%--						</td>--%>
<%--						<td></td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td align="right" height="10">--%>
<%--							<a href="#" onclick="changeDisplay()">关闭</a>--%>
<%--						</td>--%>
<%--						<td></td>--%>
<%--					</tr>--%>
<%--				</table>--%>
<%--			</div>--%>

			<table width="98%">
				<tr>
					<td height="5"></td>
				</tr>
			</table>
			     <table width="98%">
        <tr>
          <td align="center">
            <%
            String tokenInstanceId = (String) request.getAttribute("token");
            System.out.println(tokenInstanceId);
            %>
            <strongit:processImagePopToken
              token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" />

          </td>
        </tr>

      </table>
			<input type="hidden" name="processId"
							value="${model[0].processInstanceId }">
		</div>
	</body>
</html>
<%--<script type="text/javascript">--%>
<%--	//显示层隐藏--%>
<%--	document.getElementById("processImage").onmouseover = function(myevent){--%>
<%--		var obj;--%>
<%--		if(typeof(event) != 'undefined'){--%>
<%--			obj = event.srcElement;--%>
<%--		}else{--%>
<%--			obj = myevent.target;--%>
<%--		}--%>
<%--		if(this == obj){--%>
<%--			changeDisplay();--%>
<%--		}--%>
<%--	}--%>
<%--</script>--%>
