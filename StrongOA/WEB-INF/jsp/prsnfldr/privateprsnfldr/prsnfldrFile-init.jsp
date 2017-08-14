<html>
	<head>
		<title>操作内容</title>
		<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<%@ taglib uri="/struts-tags" prefix="s"%>
		<jsp:directive.page import="com.strongit.oa.prsnfldr.util.FileUtil" />
		<%@include file="/common/include/rootPath.jsp"%>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/fckeditor2/fckeditor.js" type="text/javascript"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var isTrue='${isTrue}';
			if(isTrue==1){
				returnValue="reload";
				window.close();
			}
		});
		
		$(document).ready(function(){
			    $("#fileTitle").keypress(
			        function(event){
			            if(event.keyCode == 13){
			                return false;
			            }
			        }
			    );
				//新建文件
				$("#btnOK_newLocalFile").click(function(){
					 var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
				     var title = $.trim($("#fileTitle").val());
				     $("#content").val(acontent);
				     if( title == ""){
				     	alert("标题不能为空。");
				     	$("#fileTitle").focus();
				     	return ;
				     }
				     var isSelectFile = false;
				     $("input:file.multi").each(function(){
				     	if($(this).val()!=""){
				     		isSelectFile = true;
				     	}
				     });
				     if(!isSelectFile){
				     	alert("对不起，请选择附件。");
				     	return ;
				     }
				     $("#btnOK_newLocalFile").attr("disabled","disabled");
				     //验证标题是否已存在
				     $.post("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!checkTitle.action",
				            {fileTitle:title,folderId:$("#folderId").val()},
				            function(ret){
				            	if(ret == "0"){//不存在
				            		 $("#fileTitle").val(title)
				            		 $("form").submit();
				            	}else if(ret == "1"){//存在
				            		alert("您输入的标题已存在,请重新输入.");
				            		return ;
				            	}else if(ret == "-1"){//异常
				            		alert("对不起，发生异常。");
				            		return ;
				            	}else{//不可能发生的异常
				            		alert("对不起，数据提交异常。");
				            		return ;
				            	}
				            });
				});
			});
		</script>
	</head>
	<base target="_self" />
	<body oncontextmenu="return false;" >
		<div style="height:100%;overflow: auto;">
			<label id="l_actionMessage" style="display: none;">
				<s:actionmessage />
			</label>
			<form id="fileForm" name="fileForm"
				action="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!save.action"
				method="post" enctype="multipart/form-data">
				<s:hidden id="folderId" name="folderId"></s:hidden>
				<input type="hidden" id="content" name="content">
			    <table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr align="center" >
				<td colspan="3" class="table_headtd">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>保存文件</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td >
											<table align="right" border="0"  cellpadding="0" cellspacing="0">
								                <tr>
								                	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
								                 	<td id="btnOK_newLocalFile2" class="Operation_input" onClick="saveFile();">&nbsp;保&nbsp;存&nbsp;</td>
								                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
							                  		<td width="5"></td>
				                                       <td>&nbsp;
													</td>
								                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
								                 	<td class="Operation_input1" onClick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
				</td>
				</tr>
				<td>
			<table border="0" cellpadding="0" cellspacing="0" class="table1" width="100%">
				    <tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font> 标题：</span>						
							</td>
						<td width="88%" colspan="3" class="td1">
							<s:textfield id="fileTitle" cssStyle="width:300px" name="fileTitle" maxlength="20"></s:textfield>
					  </td>
					</tr>
                     <tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font> 附件：</span>						
						</td>
						<td class="td1">
							<s:file id="upload" onkeydown="return false;" name="upload" cssClass="multi"></s:file>
						</td>
				 	 </tr>
						<tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz"></span>						
						</td>
						<td class="td1" colspan="2">
							<font color="#999999" size="2.5px">
						     说明：可以批量上传文件.不限制文件类型。每次上传文件大小不能超过${prsnfldAttSize/1024/1024 }M。
							</font>
						</td>
					</tr>
					<tr>
						<td width="12%" height="21" class="biao_bg1" align="right" valign="top">
							<span class="wz">备注：</span>						
						</td>
						<td class="td1" colspan="3">
							<script type="text/javascript">
								var oFCKeditor = new FCKeditor( 'content' );
								oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
								oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
								oFCKeditor.Width = '95%' ;
		                        oFCKeditor.Height = '200' ;
							//	oFCKeditor.Value = document.forms[0].content.value;
								oFCKeditor.Create() ;
			             	</script>
						</td>
					</tr>
					<tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz">添加人：</span>						
						</td>
						<td class="td1">
							<s:text name="userName"></s:text>
						</td>
				 	</tr>
						<tr>
						<td width="12%" height="21" class="biao_bg1" align="right">
							<span class="wz">添加日期：</span>						
						</td>
						<td class="td1">
							<s:date name="optDate" format="yyyy-MM-dd" />
						</td>
					</tr>
					<tr>
				<td class="table_td"></td>
				<td></td>
				</tr>
					</table>
					</td>
				</table>
				</table>
			</form>
		</div>
	</body>
<script type="text/javascript">
function saveFile(){
	 var oEditor = FCKeditorAPI.GetInstance('content');
     var acontent = oEditor.GetXHTML();
     var title = $.trim($("#fileTitle").val());
     var file = $.trim($("#upload").val());
     var fileIndex= file.lastIndexOf("\\");
     file = file.substring(fileIndex+1,file.length);
    // alert(file.length);
     
   // var fileLength = fileStr.substring(index+1);
    
   // alert(file);
     $("#content").val(acontent);
     if( title == ""){
     	alert("标题不能为空。");
     	$("#fileTitle").focus();
     	return ;
     }
     var isSelectFile = false;
     $("input:file.multi").each(function(){
     	if($(this).val()!=""){
     		isSelectFile = true;
     	}
     });
     if(!isSelectFile){
     	alert("对不起，请选择附件。");
     	return ;
     }
     if(file.length>64 ){
    	 alert("对不起，附件名称长度超过限制。");
    	 return;
     }
     $("#btnOK_newLocalFile2").attr("disabled","disabled");
     //验证标题是否已存在
     $.post("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!checkTitle.action",
            {fileTitle:title,folderId:$("#folderId").val()},
            function(ret){
            	if(ret == "0"){//不存在
            		 $("#fileTitle").val(title)
            		 $("form").submit();
            	}else if(ret == "1"){//存在
            		alert("您输入的标题已存在,请重新输入.");
            		return ;
            	}else if(ret == "-1"){//异常
            		alert("对不起，发生异常。");
            		return ;
            	}else{//不可能发生的异常
            		alert("对不起，数据提交异常。");
            		return ;
            	}
            });
}
</script>
</html>