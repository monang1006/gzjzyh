<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>增加文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
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
		<style type="text/css">
		.number{
		  font-size: 3;
		  font-family: 宋体;
		}
		
		</style>
		<script type="text/javascript">
			//设置选择文件保管期限值
			function changelimitName(obj){
 				var objvalue = obj.value;
 				for(var i=0;i<obj.options.length;i++){
 					if(obj.options[i].value==objvalue){
 						document.getElementById("folderLimitName").value=obj.options[i].text;
 					}
 				}
 				//notifyChange(true);
 			}
		//设置颁奖单位
		function changeAwardsOrgLevel(obj){
			var objvalue = obj.value;
 				for(var i=0;i<obj.options.length;i++){
 					if(obj.options[i].value==objvalue){
 						document.getElementById("tempfileAwardsOrgLevel").value=obj.options[i].text;
 					}
 				}
		}
		
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
								if(data=="doc"){
									var ReturnStr=OpenWindow("<%=root%>/archive/tempfile/tempFile!readAnnex.action?tempfileId="+tempfileId+"&tfileAppedId="+value, 
                                   		Width, Height, window);
								}else{
									if(confirm("对不起，该附件不是word文档，如果需要查看，请点击下载。")){
										//var frame=document.getElementById("annexFrame");	
										// frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
									}				
								}			
							}else{
								alert("对不起，该附件格式被破环。");
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
              	 	});
				 }
		
		function viewAnnex(value){	//查看附件
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
            }
		
		//颁奖单位与级别组合
		   function awardsOrg(){
		   		var orgName=document.getElementById("tempfileAwardsOrgLevel").value;
		   		var orgLevel=document.getElementById("tempfileAwardsOrg").value;
		   		document.getElementById("tempfileAwardsOrg").value=orgName+orgLevel;
		   }
		
			$(document).ready(function(){		
				$("#tempfileNo").blur(function(){
					var tempfileNo=document.getElementById("tempfileNo").value;
					var tempfileId=document.getElementById("tempfileId").value;
					if((tempfileId!=null&&tempfileId!="")||tempfileNo==""){
					}else{
						$.ajax({
							type:"post",
							url:"<%=path%>/archive/tempfile/tempFile!isExist.action",
							data:{
								tempfileNo:tempfileNo				
								},
								
							success:function(data){	
								if(data!=null&&data=="0"){//无后缀，说明是用户新建的而不是上传的文件
						 			alert("该文件编号已经存在，请重新输入。");
						 			document.getElementById("tempfileNo").value="";
						 			document.getElementById("tempfileNo").focus();
								}
							},
							error:function(data){
								alert("对不起，操作异常"+data);
							}
						});
					}
				});		
				
			});
			var filename="";
			function show(obj){
				if(obj.id=="bianji"){
					document.getElementById("view").style.display="none";
					document.getElementById("edit").style.display="";
					filename=document.getElementById("fileFileName").value;
					document.getElementById("fileFileName").value="";
				}else{
					document.getElementById("view").style.display="";
					document.getElementById("edit").style.display="none";
					document.getElementById("fileFileName").value=filename;
				}		
			}
			function chkNN(obj){ //控制卷内件数为数字
			  	str=obj.value;  	
		      	if(str!=""&&str.search(/^[0-9]*$/)==-1){
		      		alert("文件页数只能为整数。");
		      	}   	      
   			}
   			
   			function chkPN(obj){ //控制卷内件号为数字
			  	str=obj.value;  	
		      	if(isNaN(str)){
				alert("件号必须为数字。");
		      	}   	      
   			}
   			var i=0;
   			function gogo(){
   				i++;
   				if(i>1){
   					alert("请不要重复点击提交按钮。");
   					return false;
   				}
   			    var msg="";
   			    var messg = "";
   				var content = document.getElementById("tempfileDesc").value;
   				//var tempfileNo=document.getElementById("tempfileNo").value;
   				var tempfileTitle=document.getElementById("tempfileTitle").value;
   				var tempfilePage=document.getElementById("tempfilePage").value;
   				var tempfilePieceNo=document.getElementById("tempfilePieceNo").value;
   				var folderLimitName=$("#tempfileDeadlineId").val()
   				var department=document.getElementById("tempfileDepartmentName").value;
   				var tempfile_sortorder=document.getElementById("tempfile_sortorder").value;
<%--   				 if(tempfileNo==""||tempfileNo==null||tempfileNo=="null"){--%>
<%--   				    alert("文件编号不可以为空。");--%>
<%--   				    return false;--%>
<%--   				}else--%> 

 				if($.trim(tempfileTitle) == ""||$.trim(tempfileTitle)==null||$.trim(tempfileTitle)=="null"){
   				    messg = messg+"题名不可以为空。\n";
   				    //return false;
   				} if(department==""||department==null||department=="null"){
   				    messg = messg+"所属部门不可以为空。\n";
   				    //return false;
   				} if(content.length > 1000){
   					messg = messg+"文件备注请不要超过1000字。\n";
   					//document.getElementById("tempfileDesc").value="";
   					//return false;
   				}
   				 if(tempfile_sortorder!=""&&tempfile_sortorder.search(/^[1-9]*$/)==-1){
		      		messg = messg+"顺序号只能为正整数。\n";
		      		//return false;
              } 
   				 if(folderLimitName==""||folderLimitName=="null"){
   					messg = messg+"保管期限不可以为空。\n";
   				    //return false;
   				} if(tempfilePage!=""&&tempfilePage.search(/^[0-9]*$/)==-1){
		      		messg = messg+"文件页数只能为整数。\n";
		      		//return false;
		      	}  	if(isNaN(tempfilePieceNo)){
					messg = messg+"件号必须为数字。\n";
					//return false;
		      	}  
		      	//var file=document.getElementById("file").value;
		      	//alert(file);
		      	if(messg!=null&&messg!=""){
		      		alert(messg);
		      		messg="";
		      		i--;
		      		return;
		      	}
		      	var file=document.getElementsByName("file");
		      	//alert(file.length);
		      	if(0!=file.length-1){
              	 	for(i=0;i<file.length-1;i++){
              	 	      file1=file[i].value;
              	 	      //alert(file1);
              	 	     $.ajax({
              	 		type:"post",
              	 		url:"<%=path%>/archive/tempfile/tempFile!getTempFileExt.action",
              	 		data:{
							file1:file1		
                         },
						async:false,
						success:function(data){
								if(data!="doc"&&data!="docx"){
								     msg="1";
									return;
									
								}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
              	 	});
              	 	     }
              	 	}
				 	
<%--   				awardsOrg();--%>
<%--   				alert($("#tempfileAwardsOrg").val());--%>
            //alert(msg);
           if(msg!="1"){
   				document.getElementById("mytable").submit();
   			}else{
        	   i=0;
   			  alert("其中含有不是word格式的文档，请删除。");
   			}
   			}
   		
   		var createNumber=function(){
   			var width=screen.availWidth-10;;
			var height=screen.availHeight-30;
   		  // var ret=  OpenWindow("<%=root%>/serialnumber/number/number!show.action", 400, 300, window);
   		   var ret = OpenWindow(scriptroot + "/autocode/autoCode!input.action?id=4028828b40438053014043955a1e000c",width,height,window);
                                   		if(ret==undefined){
                                   		}else{
<%--         	               document.getElementById("tempfileNo").value= ret;--%>
         	               if(ret!=null&&ret!=""){
				                  $("#tempfileNo").val(ret);				
							}            
         	}
   		}
   		//删除附件
        function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 
		 function gonext(){
		 	var forwardStr="${forwardStr}";
		 	if(forwardStr!=""&&forwardStr!=null){
		 		window.location='<%=path%>/archive/archivefolder/archiveFolder!input.action?depLogo=${depLogo}&forward=viewFile&folderId=${folderId}&archiveSortId=${archiveSortId}&moduletype=${moduletype}';
		 	}
		 	else{
		     	window.location='<%=path%>/archive/tempfile/tempFile.action?depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}';
		 	}
		 }
		 
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="mytable" action="/archive/tempfile/tempFile!save.action" enctype="multipart/form-data" theme="simple">
							<input type="hidden" id="forwardStr" name="forwardStr" value="${forwardStr}">
							<input type="hidden" id="status" name="status" value="${status}">
							<input type="hidden" id="depLogo" name="depLogo" value="${depLogo }">	
							<input type="hidden" id="folderId" name="folderId" value="${folderId}">
							<input type="hidden" name="model.toaArchiveFolder.folderId" value="${model.toaArchiveFolder.folderId}">
							<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
							<s:hidden id="workflow" name="model.workflow"></s:hidden>
							<s:hidden id="tempfileFormId" name="model.tempfileFormId"></s:hidden>
							<s:hidden id="tempfileDocType" name="model.tempfileDocType"></s:hidden>
							<s:hidden id="tempfileDocId" name="model.tempfileDocId"></s:hidden>
							<!-- 在添加和编辑档案中心文件时，设置返回参数 -->
							<input type="hidden" id="treeValue" name="treeValue" value="${treeValue }">
							<input type="hidden" id="treeType" name="treeType" value="${treeType }">
							<!-- //在查看案卷中，编辑末归档的文件，返回操作，传（案卷类目ID和模块）参数 -->
							<s:hidden id="archiveSortId" name="archiveSortId"></s:hidden>
							<s:hidden id="moduletype" name="moduletype"></s:hidden>
							<!-- 判断当前操作是不否是修改档案中心文件，如果是修改，则更新该文件所属附件全文检索索引 -->
							<input type="hidden" id="IsEditTempFile" name="IsEditTempFile" value="${IsEditTempFile}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<%--<strong>保存资料中心文件</strong>--%>
													<strong>保存档案中心文件</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="gogo();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="gonext();">&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input type="hidden" id="tempfileId" name="model.tempfileId"
								value="${model.tempfileId}">
							<input type="hidden" id="tempfileIds" name="tempfileId"
								value="${model.tempfileId}">
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
										<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;题名：</span>
									</td>
									<td class="td1" align="left" >
										<input id="tempfileTitle" name="model.tempfileTitle"
											type="text" size="27" value="${model.tempfileTitle}"
											class="required"  maxlength="80">
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">文号：</span>
									</td>
								 <!-- 	<td class="td1" colspan="3" align="left">   -->
								 	<td class="td1" align="left">
								<!-- 	<serial:serialnumber id="tempfileNo" name="model.tempfileNo" nowvalue="${model.tempfileNo}"/> -->
									<input  id="tempfileNo" onkeydown="return false;" onclick="createNumber()" name="model.tempfileNo" type="text"
											size="27" value="${model.tempfileNo}" class="required"   maxlength="25">
											 <a  href="#" style="padding-bottom: 3px;" class="button" onclick="createNumber()">生成</a>
											
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;保管期限：</span>
										<input id="folderLimitName" name="model.tempfileDeadline" type="hidden" value="${model.tempfileDeadline}">
									</td>
									<td class="td1"  align="left" >
										<s:select style="width:13.7em" list="limitdictList" listKey="dictItemCode" listValue="dictItemName" headerKey="" headerValue="请选择保管期限" id="tempfileDeadlineId" name="model.tempfileDeadlineId" onchange="changelimitName(this)"  
										/>
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">时间：</span>
									</td>
									<td class="td1" align="left"  style="width:13.7em" >
										<strong:newdate id="tempfileDate" name="model.tempfileDate"
											dateform="yyyy-MM-dd HH:mm:ss" width="100%"
											dateobj="${model.tempfileDate}" isicon="true"/>
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;所属部门：</span>
										<input id="tempfileDepartment" name="model.tempfileDepartment" type="hidden" value="${model.tempfileDepartment}">
									</td>
									<td class="td1" align="left">
										<input id="tempfileDepartmentName" name="tempfileDepartmentName" type="text" size="27" readonly="readonly" value="${tempfileDepartmentName}" class="required"  >
										<a  href="#" style="padding-bottom: 3px;" class="button" onclick="departscelet()">选择</a>
									</td>
									<td  height="21" class="biao_bg1" align="right">

										<span class="wz">页数：</span>

									</td>
									<td class="td1" align="left">
										<input id="tempfilePage" name="model.tempfilePage" type="text"
											size="27" value="${model.tempfilePage}"  onblur="chkNN(this)"
											  maxlength="10">
									</td>
								</tr>														
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">顺序号：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfile_sortorder" name="model.tempfile_sortorder"
											type="text" size="27" value="${model.tempfile_sortorder}" maxlength="30">
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">件号：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfilePieceNo" name="model.tempfilePieceNo"
											type="text" size="27" value="${model.tempfilePieceNo}" onblur="chkPN(this)" maxlength="30">
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">责任者：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfileAuthor" name="model.tempfileAuthor"
											type="text" size="27" value="${model.tempfileAuthor}" maxlength="25">
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">地点：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfilePlace" name="model.tempfilePlace"
											type="text" size="27" value="${model.tempfilePlace}" maxlength="100">
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">人（物）：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfileFigure" name="model.tempfileFigure"
											type="text" size="27" value="${model.tempfileFigure}" maxlength="100">
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">事由：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfileReasons" name="model.tempfileReasons"
											type="text" size="27" value="${model.tempfileReasons}" maxlength="250">
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">颁奖单位：</span>
										<input id="tempfileAwardsOrgLevel" name="tempfileAwardsOrgLevel" type="hidden" value="${tempfileAwardsOrgLevel}">
									</td>
									<td class="td1" align="left" style="width:222px" >
										<s:select list="orgdictList" listKey="dictItemCode" listValue="dictItemName" headerKey="" headerValue="请选择获奖单位" id="tempfileAwardsOrgId" name="model.tempfileAwardsOrgId" onchange="changeAwardsOrgLevel(this)" cssStyle="width:13.7em"  />
										<input id="tempfileAwardsOrg" name="model.tempfileAwardsOrg" type="hidden" size="27" value="${model.tempfileAwardsOrg} "maxlength="20">
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">获奖内容：</span>
									</td>
									<td class="td1" align="left">
										<input id="tempfileAwardsContent" name="model.tempfileAwardsContent"
											type="text" size="27" value="${model.tempfileAwardsContent}" maxlength="100">
									</td>
								</tr>
								<tr>
									<td height="21"  class="biao_bg1" align="right" valign="top">
										<span class="wz">文件备注：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="8" id="tempfileDesc"
											name="model.tempfileDesc" 
											style="width:657px;overflow: auto; font-size: 14px;">${model.tempfileDesc}</textarea>
											<br><font color="#999999">请不要超过1000字</font>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right" >
										<span class="wz" style="width:120px">请选择word附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
<%--										<s:if test="tempfileId!=null">
											<input type="file" id="file" name="file" size="60"
												class="multi" />
												${fileFileName }
										</s:if>
										
										<s:else>
											<input type="file" id="file" name="file" size="60"
												class="multi" />
										</s:else>--%>
										<input type="file" id="file" name="file" size="50"
												class="multi" />									
										<s:if
											test="model.toaArchiveTfileAppends!=null&&model.toaArchiveTfileAppends.size()>0">
											<s:iterator id="vo" value="model.toaArchiveTfileAppends">
												<div id="${vo.tempappendId}">
		                                         <a  href="#" onclick="delAttach('${vo.tempappendId}');"  style='cursor: hand;'>[<SPAN style="color: blue">删除</SPAN>]</a>
		                                              <a href="#" onclick="viewAnnex('${vo.tempappendId}');"  style='cursor: hand;'>${vo.tempappendName}</a>
													<br>
												
												</div>
											</s:iterator>
										</s:if>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
							<iframe id="annexFrame" style="display:none"></iframe>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
									</td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
	<script type="text/javascript">
			var deptbudget_basedata_width = 300;
			var deptbudget_basedata_height = 360;
			function departscelet(){			
 				var objId = "tempfileDepartment";
 				var objName = "tempfileDepartmentName";
 				var URLStr = scriptroot+"/archive/archivefolder/archiveFolder!orgtree.action?objId="+objId+"&objName="+objName+"&moduletype=pige";
	 			var sty="dialogWidth:"+deptbudget_basedata_width*1.5+"px;dialogHeight:"+deptbudget_basedata_height+"px;status:no";
				showModalDialog(URLStr, window, sty);
 			}
	</script>
</html>
