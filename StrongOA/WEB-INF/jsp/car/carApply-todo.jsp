<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <title>待审批车辆申请列表</title>
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
      //审批  
      function processCarApply() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择车辆申请！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能审批一份车辆申请！");
          		return ;
          	}
          }
          $.post("<%=root%>/car/carApply!judgeTaskIsDone.action",
         		{taskId:taskId},
         		function(data){
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
         				 var instanceId = $(":checked").parent().next().next().attr("value");
          				location = "<%=root%>/car/carApply!form.action?taskId="+taskId+
                     				"&instanceId="+instanceId;
         			}
         		});
              
              
          
      }
       //审批状态（查看流程图）
      function workflowView(){
	      var taskId = getValue();
          if(taskId == ""){
          	alert("请选择车辆申请！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一份车辆申请的审批状态！");
          		return ;
          	}
          }  
          var instanceId = $(":checked").parent().next().next().attr("value");    
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/car/carApply!PDImageView.action?instanceId="+instanceId, 
                                   width, height, window);
      }
      //指派
      function assignCarApply(){
      	  var taskId = getValue();
          if(taskId == ""){
          	alert("请选择车辆申请！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能指派一份车辆申请！");
          		return ;
          	}
          } 
          alert("待实现！");
      }
      
      $(document).ready(function(){
        $("input:checkbox").parent().next().next().hide();//隐藏第二列    
        
         //搜索
	     $("#img_search").click(function(){
	       	$("form").submit();
	     });     
      });
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
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									待审批车辆申请列表
								</td>
									<td width="70%"><table align="right"><tr>
									<td >
										<a class="Operation" href="javascript:processCarApply();"><img
												src="<%=root%>/images/ico/shenhe.gif" width="15" height="15"
												class="img_s">审批&nbsp;</a> 
									</td>
									<td width="5">
									</td>
									<td >
										<a class="Operation" href="javascript:workflowView();"><img
												src="<%=root%>/images/ico/chakan.gif" width="15" height="15"
												class="img_s">审批状态&nbsp;</a> 
									</td>
									<td width="5">
									</td>
									<td  style="display:none">
										<a class="Operation" href="javascript:assignCarApply();"><img
												src="<%=root%>/images/ico/weituo.gif" width="15" height="15"
												class="img_s">代办&nbsp;</a> 
									</td>
									<td width="5" style="display:none">
					                  &nbsp;
					                </td>
					                </tr></table></td>
							</tr>
						</table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/car/carApply!todo.action">
          <s:hidden name="workflowType"></s:hidden>
            <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="0" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByArray" collection="${pageWorkflow.result}"
              page="${pageWorkflow}" pagename="pageWorkflow">
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="5%" align="center" class="biao_bg1">
                    <img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="57%" class="biao_bg1">
                    <s:textfield name="businessTitle" title="请输入标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="12%" class="biao_bg1">
                   <s:textfield name="userName" title="请输入申请人" cssClass="search"></s:textfield> 
                  </td>
                  <td width="13%" align="center" class="biao_bg1">
                    <strong:newdate name="starttime" id="starttime" width="45%" classtyle="search" title="起始日期"
                       isicon="true" dateform="yyyy-MM-dd HH:mm" dateobj="${starttime}"></strong:newdate>
                      </td>
                       <td width="13%" align="center" class="biao_bg1">
                   <strong:newdate name="endtime" id="endtime" width="45%" classtyle="search" title="结束日期"
                       isicon="true" dateform="yyyy-MM-dd HH:mm" dateobj="${endtime}"></strong:newdate>
                  </td>
                  <td align="center" class="biao_bg1">
                    &nbsp;
                  </td>
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="6"
                width="5%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
              <webflex:flexTextCol caption="标题" valuepos="6" valueshowpos="6" isCanDrag="true" showsize="33"
                width="57%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexTextCol valuepos="3" valueshowpos="3" caption=""></webflex:flexTextCol>
              <webflex:flexTextCol caption="申请人" valuepos="7" valueshowpos="7" isCanDrag="true"
                width="12%" isCanSort="true"></webflex:flexTextCol>
              <webflex:flexDateCol caption="接收时间"
                valuepos="1" valueshowpos="1" showsize="20"
                width="26%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>  
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
	        item = new MenuItem("<%=root%>/images/ico/banli.gif","审批","processCarApply",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/chakan.gif","审批状态","workflowView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);    
	//        item = new MenuItem("<%=root%>/images/ico/weituo.gif","代办","assignCarApply",1,"ChangeWidthTable","checkMoreDis");
	 //       sMenu.addItem(item); 
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
    </script>
  </body>
</html>
