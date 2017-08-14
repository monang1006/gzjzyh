<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>待审信息列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>-->
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">      
      function processDoc() {
          if(checkSelectedOneDis()){
              var taskId = getValue();
			  var title = $("input[type=checkbox]:checked").attr("showValue");
			  var info = getInfo();
         	  var instanceId = info[0];
			  var workflowType = $("#workflowType").val();
			 // alert(info);
              //location = "<%=root%>/recvdoc/recvDoc!form.action?taskId="+taskId+"&recvtitle="+title+"&isStartWorkflow=0"+"&instanceId="+instanceId+"&workflowType="+workflowType;
             var contextPath="<%=root%>/meetingmanage/meetingaudit/meetingaudit";
            
              var returnValue = OpenWindow("<%=root%>/meetingmanage/meetingaudit/meetingaudit!nextstep.action?taskId="+taskId+"&instanceId="+instanceId+"&timestampt="+new Date().getTime()+"&fromPath="+contextPath, 
		                                   550, 500, window);
		      if(returnValue=='OK'){
		      	location="<%=root%>/meetingmanage/meetingaudit/meetingaudit!todo.action?workflowType=5&timestampt="+new Date().getTime();
		      }
          }
      }
      $(document).ready(function(){
      	$("input:checkbox").parent().next().hide();//隐藏第二列
      	
      	//搜索
	       $("#img_sousuo").click(function(){
	       	$("form").submit();
	       });     
      })
      
      function backDoc() {
         if(checkSelectedOneDis()){
          var taskId = getValue();
		  var title = $("input[type=checkbox]:checked").attr("showValue");
		//var instanceId = $(":checked").parent().next().attr("value");
	//	var workflowType = $("#workflowType").val();
       var width=screen.availWidth-10;;
        var height=screen.availHeight-30;
        var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return", 
                                   width, height, window);
             //  alert(ReturnStr);
        if(ReturnStr!=undefined && ReturnStr!=""){
          
          $.post(
		"<%=path%>//meetingmanage/meetingaudit/meetingaudit!back.action",
		{taskId:taskId,returnNodeId:ReturnStr},
		function(data){
			 if(data=='0'){	
		      alert('回退成功！');
		    location="<%=path%>/meetingmanage/meetingaudit/meetingaudit!todo.action?workflowType=5&timestampt="+new Date().getTime(); 
		}
		else{
		   alert('回退失败！');
		    location="<%=path%>/meetingmanage/meetingaudit/meetingaudit!todo.action?workflowType=5&timestampt="+new Date().getTime(); 
		}
		}
	)
        // location = "<%=root%>/meetingmanage/meetingaudit/meetingaudit!back.action?taskId="+taskId+"&returnNodeId="+ReturnStr;
        }
      }
     }
  function viewTitle(){
	var taskId=getValue();
		if(taskId==null || taskId==""){
		alert("请选择需要查看的记录！");
			return;
		}
		if(taskId.length >32){
		alert('一次只能查看一条记录!');
		return;
	}		
   var result=window.showModalDialog("<%=path%>/meetingmanage/meetingaudit/meetingaudit!displayview.action?taskId="+taskId,window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:580px');
  
}  

 //得到列表信息
      function getInfo(){
      	var info = new Array();
      	var id = getValue();
      	<s:iterator value="pageWorkflow.result" var="obj">
      		if(id == '${obj[0]}'){
      			info[0] = '${obj[3]}';//任务实例id
      			info[1] = '${obj[6]}';//业务数据标题
      		}
      	</s:iterator>
      	return info;
      }  
</script>
	</HEAD>
	<script type="text/javascript">
	function recviewform(){
		//location="recvdoc_viewform.jsp"
	}
</script>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<s:hidden id="workflowType" name="workflowType"></s:hidden>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>待审议题列表</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
							            <tr>
							            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="processDoc();"><img src="<%=root%>/images/operationbtn/audit.png"/>&nbsp;审&nbsp;批&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="backDoc();"><img src="<%=root%>/images/operationbtn/Return.png"/>&nbsp;退&nbsp;回&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="viewTitle();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
								</tr></table></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form name="myTableForm" action="/meetingmanage/meetingaudit/meetingaudit!todo.action">
						<s:hidden name="workflowType"></s:hidden>
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray"
								collection="${pageWorkflow.result}" page="${pageWorkflow}" pagename="pageWorkflow">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
                class="table1_search">
                <tr>
                	<td>
			       		&nbsp;&nbsp;议题主题：&nbsp;<input name="businessName" id="businessName" type="text" class="search" title="请您输入议题主题" value="${businessName }">
			       		&nbsp;&nbsp;经办人：&nbsp;<input name="userName" id="userName" type="text" class="search" title="请您输入经办人" value="${userName }">
			       		&nbsp;&nbsp;开始时间：&nbsp;<strong:newdate  name="startDate" id="searchDraftDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入开始时间" dateform="yyyy-MM-dd" dateobj="${startDate}" />
						&nbsp;&nbsp;结束时间：&nbsp;<strong:newdate  name="endDate" id="searchDraftDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束时间" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
			       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo"  type="button" />
			       	</td>
                 <!--  <td width="5%" align="center" class="biao_bg1">
                    <img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="50%" class="biao_bg1">
                    <s:textfield name="businessName" title="请输入议题主题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" class="biao_bg1">
                   <s:textfield name="userName" title="请输入经办人" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" id="searchDraftDate" width="95%" classtyle="search"  title="开始日期"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                   <strong:newdate name="endDate" id="searchDraftDate" width="95%" classtyle="search"  title="截止日期"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
                  </td>
                  <td width="5%" class="biao_bg1">
								 &nbsp;
				</td> -->
								
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="6"
                width="5%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
                <webflex:flexTextCol valuepos="3" valueshowpos="3" caption=""></webflex:flexTextCol>
              <webflex:flexTextCol caption="议题主题" valuepos="6" valueshowpos="6" isCanDrag="true"
                width="50%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol caption="经办人" valuepos="7" valueshowpos="7" isCanDrag="true"
                width="15%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="接收时间"
                 valuepos="1" valueshowpos="1"
                width="30%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexDateCol>
            </webflex:flexTable>
						</s:form>	
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        item = new MenuItem("<%=root%>/images/ico/shenhe.gif","审批","processDoc",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        item = new MenuItem("<%=root%>/images/ico/guanbi.gif","退回","backDoc",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
         item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewTitle",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
	</body>
</HTML>
