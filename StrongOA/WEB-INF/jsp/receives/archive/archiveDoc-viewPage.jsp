<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/eformOCX/version.jsp"%>
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
		var TANGER_OCX_Username = '${userName}';
    
    	function downloadArchiveFile(){		//下载正文
			var id=$("#archiveId").val();
				var frame=document.getElementById("annexFrame");
				frame.src="<%=path%>/receives/archive/archiveDoc!download.action?archiveId="+id;	
			
		}
    	
		function windowClose(){
			window.close();
			//var data=$("#docTitle").val();
			//top.perspective_content.actions_container.personal_properties_toolbar.closeWorkByNames(data);
		}
		
		
		function viewAnnex(value){	//下载附件
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/receives/archive/archiveDoc!downloadAttachFile.action?archiveAttachId="+value;	
            }
		
		function view(value){		//查看附件
					var Width=screen.availWidth-10;
              	 	var Height=screen.availHeight-30;
              	 	var archiveId=$("#archiveId").val();
              	 	$.ajax({
              	 		type:"post",
              	 		url:"<%=path%>/receives/archive/archiveDoc!getArchiveFileExt.action",
              	 		data:{
							archiveId:archiveId,
							archiveAttachId:value			
						},
						success:function(data){
							if(data!=null&&data!=""&&data!="null"){	
								if(data=="doc"){
									var ReturnStr=OpenWindow("<%=root%>/receives/archive/archiveDoc!readAnnex.action?archiveId="+archiveId+"&archiveAttachId="+value, 
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
				 
		  function TANGER_OCX_PrintDoc(booValue)
     　　{ 		
			  //打印密码
			 $.ajax({
				       type:"post",
				       url:"<%=root%>/sends/docSend!printPasswords.action",
				       data:{},
				       success:function(info){
				       		if(info=="false"){
				       			if(confirm("设置了打印密码才能进行打印，是否现在去设置？")){
				       				OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-newPrint.jsp","250", "180", window);
				       				return;
				       			}
				       		}else{
				       			var reValue = OpenWindow("<%=root%>/sends/docSend!printPasswords.action?password=password","250", "180", window);
				       			if(reValue!="true") return;
				       			else{
				       				 
<%--     		打印总数--%>
     		var docHavePrintSum;	
<%--     		需要打印数		--%>
     		var needprint;
<%--     			已打印数--%>
     		var docHavePrintNum;
     		
     		var ret=0;
     		var archiveId=$("#archiveId").val();
   			var reValue = OpenWindow("<%=root%>/receives/archive/archiveDoc!gotoPrintConfig.action?archiveId="+archiveId,"300", "250", window);
       		
       		if(reValue == null || reValue == undefined){
       			return ;
       		}
       		
       		if(reValue=="false"){
       			return;
       		}else{
				var para=reValue.split(",");
				docHavePrintSum=para[0];
				needprint=para[1];
				docHavePrintNum=para[2];
       		}
       		
       		var TANGER_OCX_OBJ =window.frames[0].document.getElementById("TANGER_OCX_OBJ");		//  window.frames(0).document.getElementById("TANGER_OCX_OBJ");
       		if(TANGER_OCX_OBJ == ""){
				document.frames(0).openDoc(); 
			}
		    TANGER_OCX_OBJ.FilePrint=true;
	       		if((parseInt(needprint)+parseInt(docHavePrintNum))<=parseInt(docHavePrintSum)){
	       			var sucprint="0";
	       			for(i=0;i<parseInt(needprint);i++){
		       			try{      				
					      	TANGER_OCX_OBJ.PrintOut();  					      
					      	sucprint=i+1;
					      		
		       			}catch(e){
		       				alert("您在打印第"+(i+1)+"份时出现异常！");
		       				break;
		       			}
	       			}
			       $.ajax({
				       type:"post",
				       url:"<%=root%>/receives/archive/archiveDoc!changePrintedNum.action",
				       data:{
							archiveId:archiveId,
							docHavePrintNum:(parseInt(docHavePrintNum)+parseInt(sucprint))
				       },
				       success:function(info){
				       		if(info=="true"){
							    /*if(confirm("是否提交下一处理人？")){
							      submitNext();
							    }*/
				       		}else{
				       			alert("对不起出现错误");
				       		}
				       }
			      });
	       		}else{
	       			alert("允许打印总份数为"+docHavePrintSum+",已打印份数为"+docHavePrintNum+",您已经不能打印"+needprint+"份了");
	       			return;
	       		}
       		
       	
     		
     		//document.frames(0).printit(booValue);
     		
     		
				       			}
				       				}	
				       					}
							 });
     　　} 
				 
		 function doFullScreen(){
		 	var TANGER_OCX_OBJ =window.frames[0].document.getElementById("TANGER_OCX_OBJ");		//  window.frames(0).document.getElementById("TANGER_OCX_OBJ");
       		if(TANGER_OCX_OBJ == ""){
				document.frames(0).openDoc(); 
			}
			TANGER_OCX_OBJ.IsShowFullScreenButton(true);
		 }
		
		
	</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" scroll="no">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<form id="form" action="<%=root %>/sends/transDoc!save.action" method="post">
				<table width="100%" height="5%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="5%" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									</td>
									<td width="10%">
										查看公文
									</td>
									<td>
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td id="fullScreen" style="display: none;" width="80">
													<a class="Operation" href="javascript:doFullScreen();">
														<img src="<%=frameroot%>/images/songshen.gif" width="15"
															height="15" class="img_s">全屏模式</a>
												</td>
												<td width="5">
													&nbsp;
												</td>
												
												<td width="86">
													<a class="Operation" href="javascript:TANGER_OCX_PrintDoc(true);">
														<img src="<%=frameroot%>/images/tb-print16.gif" width="15"
															height="15" class="img_s">打印公文</a>
												</td>
											  <td width="5"></td>
												<td width="86">
													<a class="Operation" href="javascript:downloadArchiveFile();">
														<img src="<%=frameroot%>/images/xia.gif" width="15"
															height="15" class="img_s">下载公文</a>
												</td>
											  <td width="5"></td>
												<td>
													<a class="Operation" href="javascript:windowClose();">
														<img src="<%=frameroot%>/images/songshen.gif" width="15"
															height="15" class="img_s">关闭</a>
												</td>
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
				</table>
				<table width="100%" height="92%">
					<tr>
						<td width="100%">
							<input type="hidden" id="archiveId" name="archiveId" value="<%=request.getParameter("archiveId")%>">
							<iframe id='view' name='view'  scrolling="no" align="top" width="100%" height="100%"  frameborder="1" src="<%=path%>/receives/archive/archiveDoc!input.action?archiveId=<%=request.getParameter("archiveId")%>&forwardStr=view&newDate="+new Date()"">
							</iframe>
						</td>
					</tr>
					<iframe id="annexFrame" style="display:none"></iframe>
				</table>
			</form>
	</body>
</html>
