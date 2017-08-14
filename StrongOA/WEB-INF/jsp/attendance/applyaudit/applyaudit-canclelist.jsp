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
		<title>待审销假单列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
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
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
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
		   var ReturnStr=OpenWindow("<%=root%>/attendance/applyaudit/applyaudit!PDImageView.action?instanceId="+instanceId,width, height, window);
		} 
		 //得到列表信息
	    function getInfo(){
	       var info = new Array();
	       var id = getValue();
     	   <s:iterator value="pageflow.result" var="obj">
	     		if(id == '${obj[0]}'){
	     			info[0] = '${obj[3]}';//任务实例id
	     			info[1] = '${obj[6]}';//业务数据标题
	     		}
     	   </s:iterator>
	       return info;
	    }    
        $(document).ready(function(){
	      	$("input:checkbox").parent().next().hide();//隐藏第二列
	      	//搜索
	        $("#img_search").click(function(){
	       		$("form").submit();
	        });     
        })
        //审核
		function processDoc(){
		  var taskId=getValue();
		  var taskIds=taskId.split(",");
		  if(taskId==null || taskId==""){
		  alert("请选择要审核的记录！");
			 return;
		  }
		  if(taskIds.length >1){
		  alert('一次只能审核一条记录!');
		 	 return;
	      }		
	      var width=screen.availWidth-10;;
	      var height=screen.availHeight-30;
	      var info = getInfo();
	      var instanceId = info[0];
	      var businessName = info[1];
	      var ReturnStr=OpenWindow("<%=root%>/attendance/applyaudit/applyaudit!candocView.action?taskId="+taskId+"&instanceId="+instanceId+"&businessName="+encodeURI(encodeURI(businessName)), 
		                                   width, height, window);
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
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								&nbsp;
								</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									待审销假单列表
								</td>
								<td>
									&nbsp;
								</td>
								<td width="50">
									<a class="Operation" href="javascript:processDoc();"> <img
											src="<%=root%>/images/ico/shenhe.gif" width="15" height="15"
											alt="办理" class="img_s"> 审批</a>
								</td>
								<td width="5"></td>
								<td width="80">
									<a class="Operation" href="javascript:viewPDImage();"> <img
											src="<%=root%>/images/ico/shenhe.gif" width="15" height="15"
											alt="状态" class="img_s"> 审批状态</a>
								</td>
								<td width="5"></td>
											<td width="55">
												<a class="Operation" href="#" onclick="goback()">
													<img src="<%=root%>/images/ico/ht.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">返回</span> </a>
											</td>
								<td width="5">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form name="myTableForm" action="/attendance/applyaudit/applyaudit!canclelist.action">
						<s:hidden name="workflowType" value="15"></s:hidden>
			<webflex:flexTable name="myTable" width="100%" height="200px"
				wholeCss="table1" property="0" isCanDrag="true"
				isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
				getValueType="getValueByArray"
				collection="${pageflow.result}" page="${pageflow}" pagename="pageflow">
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="5%" align="center" class="biao_bg1">
                    <img id="img_search" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="50%" class="biao_bg1">
                    <s:textfield name="businessName" title="请输入标题" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" class="biao_bg1">
                   <s:textfield name="userName" title="请输入经办人" cssClass="search"></s:textfield> 
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                    <strong:newdate name="startDate" title="搜索开始时间" id="searchDraftDate" width="96%"
                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"  classtyle="search" dateobj="${startDate}"></strong:newdate>
                  </td>
                  <td width="15%" align="center" class="biao_bg1">
                   <strong:newdate name="endDate"  title="搜索结束时间"  id="searchDraftDate" width="97%"
                      skin="whyGreen" isicon="true"  classtyle="search" dateform="yyyy-MM-dd" dateobj="${endDate}"></strong:newdate>
                  </td>
                  <td width="5%" class="biao_bg1">
								 &nbsp;
				</td>
								
                </tr>
              </table>
              <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="6"
                width="5%" isCheckAll="true"
                isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
                <webflex:flexTextCol valuepos="3" valueshowpos="3" caption=""></webflex:flexTextCol>
              <webflex:flexTextCol caption="标题" valuepos="6" valueshowpos="6" isCanDrag="true"
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
        item = new MenuItem("<%=root%>/images/ico/guanbi.gif","审批状态","viewPDImage",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
         item = new MenuItem("<%=root%>/images/ico/kaiguan.gif","返回","goback",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      
  function goback(){
 	window.location="<%=path%>/attendance/applyaudit/applyaudit!todo.action?workflowType=14";
 }
    </script>
	</body>
</HTML>
