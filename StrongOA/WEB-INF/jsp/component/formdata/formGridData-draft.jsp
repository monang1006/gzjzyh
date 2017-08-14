<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>公文草稿</title>
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
	<script src="<%=path%>/oa/js/eform/grid.js" type="text/javascript"></script>
    <script type="text/javascript">
    		
    	  //右键菜单实现
    	  function loadContentMenu(gridControl){
    	  	if(gridControl != null){
    	  		gridControl.PopupMenuCallbackCommand = "GridPopupMenuCallbackCommand";
    	  		gridControl.PopupMenuFontSize=18;
    	  		gridControl.AddPopupMenuItem("查阅","openDoc");
                gridControl.AddPopupMenuItem("办理状态", "workflowView");
                gridControl.AddPopupMenuSeparator();
                gridControl.AddPopupMenuItem("催办", "sendReminder");
    	  	}
    	  }
    	  
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
       		 	alert("请选择要修改的草稿！");
       		 	return ;
       		 }else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能修改一份草稿！");
       		 		return ;
       		 	}
       		 }
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
          	  if(confirm("删除选定的草稿，确定？")){
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
              alert("请选择要删除的草稿！");
          }
      }
      
      //流程送审
      function sendDoc() {
       	  var height=473;//screen.availHeight-50;
       	  var width=480;//screen.availWidth/2;
          var bussinessId = getValue();
          if(bussinessId == ""){
   		 	alert("请选择要送审的流程！");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("一次只能送审一个流程！");
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
        		alert("发送失败！");
        	}
        }
      }
      
      function reloadPage() {
      	var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&tableName="+$("#tableName").val();
			
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
	  //草拟
	  function doCreate(){
	  		getSysConsole().refreshWorkByTitle('<%= path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2','公文草拟');
	  }
	  //登记
	  function doDengJi(){
	  		getSysConsole().refreshWorkByTitle('<%= path%>/senddoc/sendDoc!createWorkflow.action?workflowType=370020,413460','登记分发');
	  }
    </script>
  </head>
  <body class="contentbodymargin" scroll="no">
  <form id="form" name="form" method="post">
			<s:hidden name="formId"></s:hidden>
			<input id="formAction" type="hidden" name="formAction" />
			<s:hidden name="tableName"></s:hidden>
			<s:hidden name="type"></s:hidden>
		</form>
    <div id="contentborder"  align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="35">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>
				  <table align="right">
					<tr>
									<s:if test="formId == 'T_OARECVDOC'">
									<td >
					                  <a class="Operation" href="javascript:doDengJi();"><img
					                                    src="<%=root%>/images/ico/tianjia.gif" width="15"
					                                    height="15" class="img_s">登记&nbsp;</a>
					                </td>
					                <td width="5"></td>
					                </s:if>
									<s:if test="formId == 'T_OA_SENDDOC'">
									<td >
					                  <a class="Operation" href="javascript:doCreate();"><img
					                                    src="<%=root%>/images/ico/tianjia.gif" width="15"
					                                    height="15" class="img_s">草拟&nbsp;</a>
					                </td>
					                <td width="5"></td>
					                </s:if>
	                <td >
	                <s:if test="tableName != 'T_OARECVDOC' && tableName != 'T_OA_SENDDOC'">
	                  <a class="Operation" href="javascript:newDoc();"><img
	                                    src="<%=root%>/images/ico/tianjia.gif" width="15"
	                                    height="15" class="img_s">新建流程&nbsp;</a>
	                </td>
	                <td width="5"></td>
	                </s:if>
	                <td >
	                  <a class="Operation" href="javascript:sendDoc();"><img
	                                    src="<%=root%>/images/ico/tijiao.gif" width="15"
	                                    height="15" class="img_s">提交&nbsp;</a>
	                </td>
	                <td width="5">
                	</td>
	                <td >
										<a class="Operation" href="javascript:editDoc();"><img
												src="<%=root%>/images/ico/bianji.gif" width="15"
												height="15" class="img_s">修改&nbsp;</a>
									</td>
	                <td width="5"></td>
	                <td >
	                  <a class="Operation" href="javascript:deleteDoc();"><img
	                                    src="<%=root%>/images/ico/shanchu.gif" width="15"
	                                    height="15" class="img_s">删除&nbsp;</a>
	                </td>
	                <td width="5"></td>
	                </tr>
					           </table>   
					           </td>  
              </tr>
									</table>
									</td>
              </tr>
            </table>
          </td>
        </tr>
        </table>
        <iframe id="iframe_search" style="width: 100%;border: none;overflow: hidden;display: none;" src="<%=root%>/fileNameRedirectAction.action?toPage=component/formdata/formGridData-search.jsp"></iframe>
          <div id="silverlightControlHost"
			style="position:relative;height: 93%">
			<object data="data:application/x-silverlight-2,"
				type="application/x-silverlight-2" width="100%" height="100%"
				id="plugin">
				<param name="source"
					value="<%=path%>/GridFormReader/StrongFormReader.xap" />
				<param name="onError" value="onSilverlightError" />
				<param name="onLoad" value="onSilverlightLoad" />
				<param name="background" value="white" />
				<param name="minRuntimeVersion" value="4.0.50401.0" />
				<param name="autoUpgrade" value="true" />
				<a href="<%=path%>/detection/lib/Silverlight.exe"
					style="text-decoration:none"> <img
						src="<%=path%>/detection/images/SLMedallion_CHS.png"
						alt="Get Microsoft Silverlight" style="border-style:none" /> </a>
			</object>
			<iframe id="_sl_historyFrame"
				style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
		</div>
      
    </div>
    <iframe name="myiframe" src="" style="display:none;"> </iframe>
  </body>
</html>
