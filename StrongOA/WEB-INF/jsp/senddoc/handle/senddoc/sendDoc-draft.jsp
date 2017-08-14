<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程草稿</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css"
			type=text/css rel=stylesheet>
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
      //新建流程
      function newDoc(){
      	var width=screen.availWidth-10;
		var height=screen.availHeight-30;
		var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
		var ret = OpenWin("<%=root%>/senddoc/sendDoc!input.action"+param,
			width, height, window);
      }
      
      //编辑草稿
      function editDoc() {         
       		 var bussinessId = getValue();
       		 if(bussinessId == ""){
       		 	alert("请选择要编辑的记录。");
       		 	return ;
       		 }else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("只可以编辑一条记录。");
       		 		return ;
       		 	}
       		 }
       		var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val();
			var ret = WindowOpen("<%=root%>/senddoc/sendDoc!input.action"+param,'draft',width, height);
            
      }
      
      function processDoc1() {
			var bussinessId =$(window.event.srcElement).siblings().eq(1).find(":checkbox").val();
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val();
			var ret = WindowOpen("<%=root%>/senddoc/sendDoc!input.action"+param,'draft',width, height);
		}
      
      //删除草稿
      function deleteDoc() {
          var bussinessId = getValue();
          if (bussinessId != "") {
          	  if(confirm("确定要删除吗？")){
          	  	$.post("<%=root%>/senddoc/sendDoc!delete.action",
          	  			{pkFieldValue:bussinessId,tableName:$("#tableName").val()},function(ret){
          	  				if(ret == "0"){
          	  					reloadPage() ;
          	  					//window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;    
          	  				}else if(ret == "-1"){
          	  					alert("对不起，操作出错，请与管理员联系。");
          	  					return ;
          	  				}
          	  			});
          	  }
          } else {
              alert("请选择要删除的记录。");
          }
      }
      
      //流程送审
      function sendDoc() {
       	  var height=473;//screen.availHeight-50;
       	  var width=480;//screen.availWidth/2;
          var bussinessId = getValue();
          if(bussinessId == ""){
   		 	alert("请选择要送审的记录。");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("只可以送审一条记录。");
   		 		return ;
   		 	}
   		 }
         var ret = OpenWindow("<%=root%>/senddoc/sendDoc!nextstep.action?tableName="+$("#tableName").val()
         			+"&pkFieldValue="+bussinessId+"&formId="+$("#formId").val()
         			+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())), 
                               width, height,window);
        if(ret){
        	if(ret == "OK"){
        		//alert("发送成功！");
        		reloadPage() ;
        	//	window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
        	}else if(ret == "NO"){
        		alert("发送失败。");
        	}
        }
      }
      
      function reloadPage() {
      	var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&tableName="+$("#tableName").val()+"&handleKind="+$("#handleKind").val()+"&workflowType="+$("#workflowType").val();
			
		if("${page.pageSize}"!=""){
         	param += "&page.pageSize=${page.pageSize}";
	    }
	    var privilName = "";
	    if("${privilName}" !=""){
	    	privilName = encodeURI(encodeURI("${privilName}"));
	    	param += "&privilName="+privilName;
	    }else{
		    if($("#privilName").val() != ""){
		    	privilName = encodeURI(encodeURI($("#privilName").val()));
		    	param += "&privilName="+privilName;
		    }
	    }
      	window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
      }
      function doHide(){
	      	var d = $("#searchtable").css("display");
	      	if(d == "none"){
		      	$("#searchtable").show();
		      	$("#label_search").text("隐藏查询条件");
	      	} else {
	      		$("#searchtable").hide();
	      		$("#label_search").text("显示查询条件");
	      	}
	      }
    //转发邮件
		function sendMail(){
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要转发邮件的记录。");
				return ;
			}else{
				var docIds = bussinessId.split(",");
				if(docIds.length>1){
					alert("只可以转发邮件一条记录。");
					return ;
				}
			}
			var iWidth=700; //弹出窗口的宽度;
			var iHeight=480; //弹出窗口的高度;
			var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
			var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
			var param = "?workflowType="+$("#workflowType").val()+"&gwcs=gwcs&formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val();
			 WindowOpen("<%=root%>/senddoc/sendDoc!sendMail.action"+param,'sendMail',700, 480,iTop,iLeft);  
		}
	  //草拟
	  function doCreate(){
	  		//window.parent.refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2','公文草拟');
	  		//getSysConsole().refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2','草拟');
	  		window.location.href = '<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2';
	  }
	  
	  
    </script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
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
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName%>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else></strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00"cellspacing="0">
										<tr>
										     <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	          <td class="Operation_list" onclick="sendMail();"><img src="<%=root%>/images/operationbtn/Send_email.png"/>&nbsp;转发邮件&nbsp;</td>
						                 	         <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	          <td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="newDoc();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="editDoc();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<%-- 
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="doHide();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;显&nbsp;示&nbsp;查&nbsp;询&nbsp;条&nbsp;件&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	--%>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="deleteDoc();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
						<s:form id="myTableForm" action="/senddoc/sendDoc!draft.action">
							<!-- 流程名称 -->
							<s:hidden id="workflowName" name="workflowName"></s:hidden>
							<!-- 表单id -->
							<s:hidden id="formId" name="formId"></s:hidden>
							<!-- 表名称 -->
							<s:hidden id="tableName" name="tableName"></s:hidden>
							<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
							<s:hidden id="handleKind" name="handleKind"></s:hidden>
							<s:hidden id="workflowType" name="workflowType"></s:hidden>
							<!-- 资源名称 -->
							<input type="hidden" name="privilName" id="privilName"
								value="<%=privilName%>" />
							<!--发文处理草稿显示列 【文件标题	  发文类别	发文单位】	  -->
							<%
								List showColumnList = (List) request.getAttribute("showColumnList");
								showColumnList = new ArrayList(showColumnList);
								List<Object[]> showList = new LinkedList<Object[]>();
								String[] column1 = (String[]) showColumnList.get(0);
								String[] column2 = null;
								String[] column3 = null;
								String[] column4 = null;
								for (int i = 0; i < showColumnList.size(); i++) {
									Object[] column = (Object[]) showColumnList.get(i);
									String columnName = (String) column[0];
									if (columnName.toUpperCase().equals(
									"WORKFLOWTITLE".toUpperCase())) {
										column2 = (String[]) column;
										showColumnList.remove(i);
										i--;
										continue;
									}
									if (columnName.toUpperCase().equals(
									"WORKFLOWNAME".toUpperCase())) {
										column3 = (String[]) column;
										showColumnList.remove(i);
										i--;
										continue;
									}
									if (columnName.toUpperCase().equals(
									"SENDDOC_ISSUE_DEPART_SIGNED".toUpperCase())) {
										column4 = (String[]) column;
										showColumnList.remove(i);
										i--;
										continue;
									}
								}
							%>
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<strong:query queryColumnList="${queryColumnList}" />
								<%
									if(column1 != null){
										%>
										<webflex:flexCheckBoxCol caption="选择" property="<%=column1[0]%>"
											showValue="<%=column1[3]%>" width="4%" isCheckAll="true"
											isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
										<%
									}
								%>
								<%
									if(column2 != null){
										%>
										<webflex:flexTextCol caption="<%=column2[1]%>"
											property="<%=column2[3]%>" showValue="<%=column2[0]%>" 
											onclick="processDoc1();"
											width="71%" isCanDrag="false" isCanSort="false" showsize="35">
											</webflex:flexTextCol>
										<%
									}
								%>
								<%
									if(column3 != null){
										%>
										<webflex:flexTextCol caption="<%=column3[1]%>"
											property="<%=column3[3]%>" showValue="<%=column3[0]%>"
											width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
										<%
									}
								%>
								<%
									if(column4 != null){
										%>
										<webflex:flexTextCol caption="<%=column4[1]%>"
											property="<%=column4[3]%>" showValue="<%=column4[0]%>"
											width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
										<%
									}
								%>
								
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
    
      var workflowName =$("#workflowName").val();
      var formId =$("#formId").val();
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
            item = new MenuItem("<%=root%>/images/operationbtn/Send_email.png","转发邮件","sendMail",1,"ChangeWidthTable","checkOneDis");
            sMenu.addItem(item);
          	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","newDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	       // item = new MenuItem("<%=root%>/images/ico/songshen.gif","提交","sendDoc",1,"ChangeWidthTable","checkOneDis");
	        //sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","deleteDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
         var privilName = "<%=privilName%>";
       if(privilName ==""){
       		privilName = "${privilName}";
       }
       $("#privilName").val(privilName);
      }
    </script>
	</body>
</html>
