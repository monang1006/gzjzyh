<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>查看文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			function view(value){
					var Width=screen.availWidth-10;
              	 	var Height=screen.availHeight-30;
              	 	var tempfileId=document.getElementById("tempfileId").value;
              	 	$.ajax({
              	 		type:"post",
              	 		url:"<%=path%>/archive/tempfile/tempFile!getTempFileExt.action",
              	 		data:{
							tempfileId:tempfileId,
							tfileAppedId:value			
						},
						success:function(data){
							if(data!=null&&data!=""&&data!="null"){	
								if(data=="doc"||data=="docx"){
									var ReturnStr=OpenWindow("<%=root%>/archive/tempfile/tempFile!readAnnex.action?tempfileId="+tempfileId+"&tfileAppedId="+value, 
                                   		Width, Height, window);
								}else{
									if(confirm("对不起，该附件不是word文档，如果需要查看，请点击下载！")){
										//var frame=document.getElementById("annexFrame");	
										// frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
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
function cancel(){	 //返回
	
	//var archiveSearch="<%=request.getParameter("archiveSearch")%>";
	var archiveSearch=document.getElementById("archiveSearch").value;
	 //alert(archiveSearch);
	if(!archiveSearch||"null"== archiveSearch){
	
	    var depLogo=document.getElementById("depLogo").value;
	    var treeValue=document.getElementById("treeValue").value;
	    var treeType=document.getElementById("treeType").value;
	    //alert(treeValue);
	   location="<%=path%>/archive/tempfile/tempFile.action?treeValue="+treeValue+"&depLogo="+depLogo+"&treeType="+treeType;}
	else{
	    var fileNo=document.getElementById("fileNo").value;
	    
	    var disLogo=document.getElementById("disLogo").value;
	    //alert(disLogo);
	    var groupType=document.getElementById("groupType").value;
	    var fileTitle=document.getElementById("fileTitle").value;
	    var fileFolder=document.getElementById("fileFolder").value;
	    var tempfileDeadline=document.getElementById("tempfileDeadline").value;
	    var orgId=document.getElementById("orgId").value;
	    var year=document.getElementById("year").value;
	    var month=document.getElementById("month").value;
	   location="<%=path%>/archive/filesearch/fileSearch.action?groupType="+groupType+"&disLogo="+disLogo+"&fileNo="+fileNo+"&fileTitle="+fileTitle+"&tempfileDeadline="+tempfileDeadline+"&year="+year+"&month="+month+"&orgId="+orgId+"&fileFolder="+fileFolder;
	}
}
			function viewAnnex(value){	//查看附件
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
            }
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="mytable"
							action="/archive/tempfile/tempFile!save.action" theme="simple">
							<input type="hidden" id="forwardStr" name="forwardStr"
								value="${forwardStr}">
								<input type="hidden" id="depLogo" name="depLogo" value="${depLogo }">
							<input type="hidden" id="folderId" name="folderId"
								value="${folderId}">
								<!-- 在添加和编辑档案中心文件时，设置返回参数 -->
							<input type="hidden" id="treeValue" name="treeValue" value="${treeValue }">
							<input type="hidden" id="treeType" name="treeType" value="${treeType }">
							<input type="hidden" id="fileNo" name="fileNo" value="${fileNo }">
							<input id="tempfileDeadline" name="tempfileDeadline" type="hidden" value="${tempfileDeadline}">
							<input type="hidden" id="fileTitle" name="fileTitle" value="${fileTitle }">
							<input type="hidden" id="fileFolder" name="fileFolder" value="${fileFolder }">
							<input type="hidden" id="orgId" name="orgId" value="${orgId }">
							<input type="hidden" id="month" name="month1" value="${month1 }">
							<input type="hidden" id="year" name="year1" value="${year1}">
							<input type="hidden" id="disLogo" name="disLogo1" value="${disLogo1 }">
							<input type="hidden" id="groupType" name="groupType" value="${groupType }">
							<input type="hidden" id="archiveSearch" name="archiveSearch" value="${archiveSearch}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看档案中心文件</strong>
												</td>
												<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
															
															<s:if test="searchType==null||searchType==''">
															<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="history.go(-1);">&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
													</s:if>
													<s:else>
															<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
															</s:else>
														</tr>
													</table> 
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input type="hidden" id="tempfileId" name="model.tempfileId" value="${model.tempfileId}">
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">题名：</span>
									</td>
									<td class="td1">
										<span class="wz">${model.tempfileTitle}</span>
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">文号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfileNo}</span>
									</td>								
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">保管期限：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.tempfileDeadline}</span>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span>日期：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									   <s:date name="model.tempfileDate" format="yyyy年MM月dd日 HH点mm分"/>
										
									</td>
								</tr>
<%--										<input id="tempfileDate" name="model.tempfileDate" type="text"--%>
<%--											size="30" value="${model.tempfileDate }" readonly="readonly">--%>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">所属部门：</span>
										<input id="tempfileDepartment" name="model.tempfileDepartment"
											type="hidden" size="30" value="${model.tempfileDepartment}"
											readonly="readonly">
									</td>
									<td class="td1" align="left">
										<span class="wz">${tempfileDepartmentName}</span>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">页数：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfilePage}</span>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">顺序号：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.tempfile_sortorder}</span>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">件号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfilePieceNo}</span>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">责任者：</span>
									</td>
									<td class="td1" class="td1" align="left">
										<span class="wz">${model.tempfileAuthor}</span>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">地点：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfilePlace}</span>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">人（物）：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.tempfileFigure}</span>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">事由：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfileReasons}</span>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">颁奖单位：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${tempfileAwardsOrgLevel}</span>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">获奖内容：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.tempfileAwardsContent}</span>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">文件备注：</span>
									</td>
									<td class="td1" colspan="5" align="left">
										<span class="wz">${model.tempfileDesc}</span>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="5" align="left">
											<s:if
											test="model.toaArchiveTfileAppends!=null&&model.toaArchiveTfileAppends.size()>0">
											<s:iterator id="vo" value="model.toaArchiveTfileAppends">
                                         	  <a id="fujian" href="#" onclick="view('${vo.tempappendId}');"  style='cursor: hand;'><font color="blue">${vo.tempappendName}</font></a>
                                              <a href="#" onclick="viewAnnex('${vo.tempappendId}');"  style='cursor: hand;'>下载</a>
											<br>
											</s:iterator>
										</s:if>
									</td>
								</tr>
							</table>
							<iframe id="annexFrame" style="display:none"></iframe>
							
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
