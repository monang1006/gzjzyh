<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>

<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title>档案文件查看</title>

		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		var isDoSave = false;//是否已经点击保存按钮
		var TANGER_OCX_Username = '${fileFileName}';
		var loaded = false;
		var tp1 ;
		function custom(tabpage){
			if(tabpage.index != 1){//处理正文Tab
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
		}	
		
		function openDoc(){
			if(!loaded){			
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			var width=screen.availWidth-65;
   			var height=screen.availHeight-140;
	  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
	  		var archiveId=$("#archiveId").val();
	  		var forwardStr=$("#forwardStr").val();
	  		var url=basePath + "receives/archive/archiveDoc!openEmptyDocFromUrl.action?docType="+type+"&archiveId="+archiveId;
	  		if(forwardStr=="doc"){
	  			url=basePath + "receives/recvTDoc!openEmptyDocFromUrl.action?docType="+type+"&archiveId="+archiveId;
	  		}
		  	TANGER_OCX_OBJ.OpenFromURL(url);
		  	TANGER_OCX_OBJ.WebFileName='新建文档';
		  	TANGER_OCX_OBJ.width = width;
		  	TANGER_OCX_OBJ.height = height;
		  	TANGER_OCX_OBJ.SetReadOnly(true);
		  	
		  	 //禁用新建功能
                TANGER_OCX_OBJ.FileNew=false;
                //禁用保存功能
                TANGER_OCX_OBJ.FileSave=false;
                //禁用打印功能
                TANGER_OCX_OBJ.FilePrint=false;
                //禁用关闭功能
                TANGER_OCX_OBJ.FileClose=false;
                //禁用另存为功能
                TANGER_OCX_OBJ.FileSaveAs=false;  
                //TANGER_OCX_OBJ.IsShowInsertMenu=false;  
                //TANGER_OCX_OBJ.IsShowEditMenu=false;  
                loaded = true;
           		tp1.setSelectedIndex(1);	
			}
		}
		
    	function downloadArchiveFile(){		//下载正文
			var id=$("#archiveId").val();
				var frame=document.getElementById("annexFrame");
				frame.src="<%=path%>/receives/archive/archiveDoc!download.action?archiveId="+id;	
			
		}
    	
		function windowClose(){
			window.close();
		}
		
		
		function viewAnnex(value){	//下载附件
	           var frame=document.getElementById("annexFrame");
	           var forwardStr=$("#forwardStr").val();
	           if(forwardStr!=null&&forwardStr=="doc"){
	           		frame.src="<%=path%>/receives/recvTDoc!downloadAttachFile.action?archiveAttachId="+value;	
	           		//window.location = window.location;	
	           }else{
		           frame.src="<%=path%>/receives/archive/archiveDoc!downloadAttachFile.action?archiveAttachId="+value;	
		           //window.location = window.location;	
	           }
            }
            
        function DeleteAnnex(value){	//删除附件
	           var forwardStr=$("#forwardStr").val();
	          var archiveId=$("#archiveId").val();
	           if(forwardStr!=null){
	           		 if(confirm("确定删除该附件吗?")) 
	                   {                                         
	                        $.get("<%=path%>/receives/recvTDoc!delete.action?archiveAttachId="+value, function(response){
		                  	if(response == "success"){		                  
			              	alert("删除成功!");
			              	window.location = window.location;
		                            	}
	                             	});	                       
	                    }   			
                       }
            }
            
		
		function view(value){		//查看附件
					var Width=screen.availWidth-10;
              	 	var Height=screen.availHeight-30;
              	 	var archiveId=$("#archiveId").val();
              	 	var forwardStr=$("#forwardStr").val();
              	 	var url= "<%=path%>/receives/archive/archiveDoc!getArchiveFileExt.action";
			  		var urlWin= "<%=root%>/receives/archive/archiveDoc!readAnnex.action?archiveId="+archiveId+"&archiveAttachId="+value;
			  		if(forwardStr=="doc"){
			  			url="<%=path%>/receives/recvTDoc!getArchiveFileExt.action";
			  			urlWin="<%=root%>/receives/recvTDoc!readAnnex.action?archiveId="+archiveId+"&archiveAttachId="+value;
			  		}

              	 	$.ajax({
              	 		type:"post",
              	 		url:url,
              	 		data:{
							archiveId:archiveId,
							archiveAttachId:value			
						},
						success:function(data){
							if(data!=null&&data!=""&&data!="null"){	
								if(data=="doc"){
									var ReturnStr=OpenWindow(urlWin, 
                                   		Width, Height, window);
                                   	
								}else{
									if(confirm("对不起，该附件不是word文档，如果需要查看，请点击下载！")){
										return;
									}				
								}			
							}else{
								alert("对不起，该附件格式被破环！");
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
              	 	});
				 }
			
			//打印公文	 
			function printit(booValue){

				if(confirm('确定打印吗？')){ 
	     　			 var FormInputOCX = document.getElementById("TANGER_OCX_OBJ");
				      try{
					      FormInputOCX.PrintOut(booValue);      
				      }catch(e){}
	     　		} 
			
			}
				 
		
	</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" scroll="no">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<form id="form" action="<%=root %>/sends/transDoc!save.action" method="post">
				
				<table width="100%" border="0" cellpadding="0"
					cellspacing="1" class="">
					<tr>
							
			            <td>
			            	<s:hidden id="archiveId" name="archiveId"></s:hidden>
							<input type="hidden" id="forwardStr" name="forwardStr"
											value="${forwardStr}">
							<input type="hidden" id="showType" name="showType" value="${showType}">
							<DIV style="height: 280;" class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
									tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										<font size=3>公文摘要</font>
									</H2>
									<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield id="docIssueDepartSigned" name="model.docIssueDepartSigned" cssStyle="width:83%" readonly="true" ></s:textfield>
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文文号：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield id="docCode" name="model.docCode" cssStyle="width:83%"  readonly="true"></s:textfield>
												&nbsp;
												
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文标题：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield id="docTitle" name="model.docTitle" cssStyle="width:83%"  readonly="true"></s:textfield>
											</td>
										</tr>
										
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文种类：</span>
											</td>
											<td class="td1" width="40%">
												<s:if test="model.docClass!=null&&model.docClass.equals(\"0\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="上行文"/>" id="docClass" style="width:85%" />
												</s:if>
												<s:if test="model.docClass!=null&&model.docClass.equals(\"1\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="平行文"/>" id="docClass" style="width:85%" />
												</s:if>
												<s:if test="model.docClass!=null&&model.docClass.equals(\"2\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="下行文"/>" id="docClass" style="width:85%" />
												</s:if>
											
<%--												<s:textfield id="docClass" name="model.docClass" cssStyle="width:85%"  readonly="true"></s:textfield>--%>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">秘密等级：</span>
											</td>
											<td class="td1">
												<s:textfield id="docSecretLvl" name="model.docSecretLvl" cssStyle="width:63%"  readonly="true"></s:textfield>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">紧急程度：</span>
											</td>
											<td class="td1">
												<s:textfield id="docEmergency" name="model.docEmergency" cssStyle="width:85%"  readonly="true"></s:textfield>
											</td>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">成文日期：</span>
											</td>
											<td class="td1">
												<strong:newdate id="docOfficialTime" name="model.docOfficialTime"  disabled="true" 
													dateform="yyyy-MM-dd"   dateobj="${model.docOfficialTime}"  isicon="true" width="63%"/>
<%--												<s:textfield id="docOfficialTime" name="model.docOfficialTime" cssStyle="width:63%"  readonly="true"></s:textfield>--%>
											</td>
										</tr>
										<%-- <s:if test="model.rest3=='001'">   --%>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">主送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea id="docSubmittoDepart" name="model.docSubmittoDepart" cssStyle="width:83%"  readonly="true"></s:textarea>
											</td>
										</tr>
										<%--  </s:if>   --%>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">抄送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea id="docCcDepart" name="model.docCcDepart" cssStyle="width:83%"  readonly="true"></s:textarea>
											</td>
										</tr>
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">会签人：</span>
											</td>
											<td class="td1">
												<s:textfield id="docCountersigner" name="model.docCountersigner" cssStyle="width:85%"  readonly="true"></s:textfield>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">签发人：</span>
											</td>
											<td class="td1">
												<s:textfield id="docIssuer" name="model.docIssuer" cssStyle="width:63%"  readonly="true"></s:textfield>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">创建日期：</span>
											</td>
											<td  width="38%" class="td1">
												<strong:newdate id="docEntryTime" name="model.docEntryTime"  disabled="true" 
													dateform="yyyy-MM-dd"   dateobj="${model.docEntryTime}"  isicon="true" width="85%"/>
<%--												<s:textfield id="docEntryTime" name="model.docEntryTime" cssStyle="width:85%"  readonly="true"></s:textfield>--%>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文状态：</span>
											</td>
											<td class="td1">
												<s:if test="model.docState!=null&&model.docState.equals(\"0\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="草拟"/>" id="docState" style="width:63%" />
												</s:if>
												<s:if test="model.docState!=null&&model.docState.equals(\"1\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="待签章"/>" id="docState" style="width:63%" />
												</s:if>
												<s:if test="model.docState!=null&&model.docState.equals(\"2\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="待分发"/>" id="docState" style="width:63%" />
												</s:if>
												<s:if test="model.docState!=null&&model.docState.equals(\"3\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="已分发"/>" id="docState" style="width:63%" />
												</s:if>
												<s:if test="model.docState!=null&&model.docState.equals(\"4\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="退回"/>" id="docState" style="width:63%" />
												</s:if>
												<s:if test="model.docState!=null&&model.docState.equals(\"5\")">
													<input title="公文状态" disabled="disabled" value="<s:text name="已回收"/>" id="docState" style="width:63%" />
												</s:if>
<%--													<s:textfield id="docState" name="model.docState" cssStyle="width:63%"  readonly="true" value=""></s:textfield>--%>
												
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">签章处理：</span>
											</td>
											<td class="td1">
												<s:if test="model.docSealIs!=null&&model.docSealIs.equals(\"1\")">
													<input id="docSealIs" disabled="disabled" checked="checked" value="1" name="model.docSealIs" type="checkbox" />
												</s:if>
												<s:else>
													<input id="docSealIs" disabled="disabled" value="1" name="model.docSealIs" type="checkbox" />
												</s:else>
											
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">印发日期：</span>
											</td>
											<td class="td1">
												<strong:newdate id="ddocPrintTime" name="model.docSendTime"  disabled="true" 
													dateform="yyyy-MM-dd"   dateobj="${model.docSendTime}"  isicon="true" width="63%"/>
											</td>
										</tr>
										<%--<tr>
											<td width="11%" height="28" class="biao_bg1" align="right">
												短信通知：
											</td>
											<td class="td1" colspan="3">
													<s:textfield title="短信通知"  disabled="true" maxlength="70" id="rest10" name="model.rest10" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>--%>
										<tr>
											<td height="21" class="biao_bg1" align="right">
												<span class="wz">附件：</span>
											</td>
											<td class="td1" colspan="3" align="left">
													<s:if
													     test="model.ttransArchiveAttaches!=null&&model.ttransArchiveAttaches.size()>0">
														<s:iterator id="vo" value="model.ttransArchiveAttaches">
															<div>
			                                         			<a  id="fujian" href="#"   style='cursor: hand;'>${vo.attachFileName}</a>
			                                         			<s:if test="showType=='view'">
			                                         			<input id="delete" name="delete" type="button" onclick="DeleteAnnex('${vo.archiveAttachId}');" class="input_bg" value="删 除">
			                                              		</s:if>
			                                              		<a href="#" onclick="viewAnnex('${vo.archiveAttachId}');"  style='cursor: hand;'><font color="blue">下载</font></a>
																<br>
															</div>	
														</s:iterator>
												</s:if>
												<s:if test="model.ttransDocAttaches!=null&&model.ttransDocAttaches.size()>0">
													<s:iterator id="vo" value="model.ttransDocAttaches">
														<div>
<%--														<a  id="fujian" href="#" onclick="view('${vo.docAttachId}');"  style='cursor: hand;'><font color="blue">${vo.attachFileName}</font></a>--%>
														<a href="#" onclick="viewAnnex('${vo.docAttachId}');"  style='cursor: hand;'><font color="blue">${vo.attachFileName}</font></a>
														<s:if test="showType=='view'">
														<input id="delete" name="delete" type="button" onclick="DeleteAnnex('${vo.docAttachId}');" class="input_bg" value="删 除">
														</s:if>
														<br>
														</div>
			                                        </s:iterator>
			                                       
												</s:if>
											</td>
										</tr>
									</table>
									<iframe id="annexFrame" style="display:none"></iframe>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>
							
									<DIV class=tab-page id=tabPage1 >
										<H2 class=tab >
											<font size=3>公文正文</font>
										</H2>
										<%--<object id="TANGER_OCX_OBJ" 
										classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
										codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=5,0,1,8"
										width="100%" height="100%">
										
										<param name='MakerCaption' value="思创数码科技股份有限公司">
										<param name='MakerKey' value='5C1FF1F1177246B272DB34DD8ADA318222D19F65'>
										<param name='ProductCaption' value='南昌市政府办公厅'>
										<param name='ProductKey' value='FD6357E9840E880F0B72EAC5357E7303379848AB'>
										<param name="BorderStyle" value="1">
										<param name="TitlebarColor" value="42768">
										<param name="TitlebarTextColor" value="0">
										<param name="TitleBar" value="false">
										<param name="MenuBar" value="false">
										<param name="Toolbars" value="ture">
										<param name="IsResetToolbarsOnOpen" value="true">
										<param name="IsUseUTF8URL" value="true">
										<param name="IsUseUTF8Data" value="true">
										<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
									</object>
									--%>
										<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
									</DIV>
								
							</td>
					</tr>
				</table>
			</form>
	</body>
</html>
