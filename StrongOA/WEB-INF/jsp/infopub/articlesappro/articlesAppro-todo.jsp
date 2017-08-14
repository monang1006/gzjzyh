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
<%--		<script src="<%=path%>/common/js/common/search.js"--%>
<%--			type="text/javascript"></script>--%>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">      
			/**
		* 查看流程图
		* @param contextPath 上下文路径 返回值如：/StrongOA/work/work
		* @param instanceId 流程实例id
		*/
		function viewPDImage(){
		   var id = getValue();
		   if(id==""){
			   alert("请选择要查看的流程！");
			   return;
		   }
		   var ids=id.split(",");
		   if(ids.length>1){
		       alert("不可以同时查看多个流程！");
			   return;
		   }
		   var info = getInfo();
		   if(info.length<=0){
		       alert("请选择要查看的流程！");
		       return;
		   }
	       var instanceId = info[0];
		   var width=screen.availWidth-10;
		   var height=screen.availHeight-30;
		   var ReturnStr=OpenWindow("<%=root%>/infopub/articlesappro/articlesAppro!PDImageView.action?instanceId="+instanceId,width, height, window);
		} 
		//由eform.js中定义的doNext()回调
		function goBack(){
			alert("发送成功！");
	   		window.document.location.reload();
	   		window.close();
		}
       function processDoc() {
          if(checkSelectedOneDis()){
              var taskId = getValue();
			  var title = $("input[type=checkbox]:checked").attr("showValue");
			  var info = getInfo();
         	  var instanceId = info[0];
			  var workflowType = $("#workflowType").val();
			  var contextpath = "<%=root%>/infopub/articlesappro/articlesAppro";
              //location = "<%=root%>/recvdoc/recvDoc!form.action?taskId="+taskId+"&recvtitle="+title+"&isStartWorkflow=0"+"&instanceId="+instanceId+"&workflowType="+workflowType;
              var returnValue = OpenWindow("<%=root%>/infopub/articlesappro/articlesAppro!nextstep.action?taskId="+taskId+"&instanceId="+instanceId+"&fromPath="+contextpath+"&timestampt="+new Date().getTime(), 
		                                   550, 500, window);
		      if(returnValue=='OK'){
		      	location="<%=root%>/infopub/articlesappro/articlesAppro!todo.action?workflowType=1";
		      }
          }
      }
      $(document).ready(function(){
      	$("input:checkbox").parent().next().hide();//隐藏第二列
      	
      	//搜索
	       $("#img_search").click(function(){
	       	$("form").submit();
	       });     
      })
      
      //退回
      function backDoc() {
         if(checkSelectedOneDis()){
         var taskId = getValue();
		
		 var info = getInfo();
         var instanceId = info[0];
      	 var width=screen.availWidth-10;;
         var height=screen.availHeight-30;
         var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return", 
                                   width, height, window);
       	 if(ReturnStr){
		 var ret = OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/initback.jsp", 
		                                   400, 300, window);
	     if(ret){
		    $.post("<%=root%>/infopub/articlesappro/articlesAppro!back.action",{taskId:taskId,formId:"0",returnNodeId:ReturnStr,businessId:instanceId,suggestion:encodeURI(ret)},
				   function(retCode){
				   		if(retCode == "0"){
				   			goBack();//调用页面上提供的函数
				   		}else if(retCode == "-1"){
				   			alert("任务实例不存在或已删除！");
				   		}else if(retCode == "-2"){
				   			alert("任务退回过程中出现异常！");
				   		}
				   }			
			);
		}                                   
	}	                               
      }
     }
  function viewTitle(){
	var taskId=getValue();
	var taskIds=taskId.split(",");
		if(taskId==null || taskId==""){
		alert("请选择需要查看的记录！");
			return;
		}
		if(taskIds.length >1){
		alert('一次只能查看一条记录!');
		return;
	}
   $.post("<%=root%>/infopub/articlesappro/articlesAppro!displayview.action",
          {taskId:taskId},
		   function(retCode){
			   if(retCode!=""){
			   		var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!show.action?columnArticleId="+retCode,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
			   }else{
			   		alert("该流程没有绑定业务数据!");
			   }
		   }
   );
   
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
								<strong>待审信息列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="processDoc();"><img src="<%=root%>/images/operationbtn/audit.png"/>&nbsp;审&nbsp;批&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="viewPDImage();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;审&nbsp;批&nbsp;状&nbsp;态</td>
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
				                  		<td width="2%"></td>
					                 </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
					<s:form id="myTableForm" action="/infopub/articlesappro/articlesAppro!todo.action" method="post">
					<s:hidden name="workflowType"></s:hidden>
					<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray"
								collection="${pageWorkflow.result}" page="${pageWorkflow}" pagename="pageWorkflow">
					        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;标题：&nbsp;<input name="businessName" id="businessName" type="text" class="search" title="请您输入标题" value="${model.businessName }">
							       		&nbsp;&nbsp;经办人：&nbsp;<input name="userName" id="userName" type="text" class="search" title="请您输入经办人" value="${model.userName }">
							       		&nbsp;&nbsp;接收起始时间：&nbsp;<strong:newdate  name="startDate" id="searchDraftDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${startDate}"/>
							       		--&nbsp;<strong:newdate  name="endDate" id="searchDraftDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table> 
							<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="6"
				                width="5%" isCheckAll="true"
				                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
				                <webflex:flexTextCol valuepos="3" valueshowpos="3" caption=""></webflex:flexTextCol>
				              <webflex:flexTextCol caption="标题" valuepos="6" valueshowpos="6" isCanDrag="true"
				                width="50%" isCanSort="true"></webflex:flexTextCol>
				              <webflex:flexTextCol caption="经办人"  align="center" valuepos="7" valueshowpos="7" isCanDrag="true"
				                width="15%" isCanSort="true"></webflex:flexTextCol>
				              <webflex:flexDateCol caption="接收时间" 
				                 valuepos="1" valueshowpos="1"
				                width="30%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexDateCol>
				            </webflex:flexTable>
					</s:form>
				</tr>
			</table>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        item = new MenuItem("<%=root%>/images/operationbtn/audit.png","审批","processDoc",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","审批状态","viewPDImage",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        item = new MenuItem("<%=root%>/images/operationbtn/Return.png","退回","backDoc",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewTitle",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      $(document).ready(function(){
    		$("#img_sousuo").click(function(){
    			$("form").submit();
    		});
    	}); 
    </script>
	</body>
</HTML>
