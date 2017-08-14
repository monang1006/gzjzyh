<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>查阅文件</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/fckeditor2/fckeditor.js" type="text/javascript" ></script>
		<script type="text/javascript">
		
		
		$(document).ready(function(){
			var isTrue = '${isTrue}';
				if(isTrue==1){
					returnValue ="reload";
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
		
			$(document).ready(function(){
				//新建文件
				$("#btnOK_newLocalFile").click(function(){
					 var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
				     var title = $.trim($("#fileTitle").val());
				     $("#content").val(acontent);
				     if($.trim($("#fileTitle").val()) == ""){
				     	alert("标题不能为空！");
				     	$("#fileTitle").focus();
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
				            		alert("对不起，发生异常！");
				            		return ;
				            	}else{//不可能发生的异常
				            		alert("对不起，数据提交异常！");
				            		return ;
				            	}
				            });
				});
			});
		});
		</script>
		<style type="text/css">
		input{
		border:1px solid #b3bcc3;background-color:#ffffff;
		}
		</style>
		<script type="text/javascript">
			function save(){
					 var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
				     var title = $.trim($("#fileTitle").val());
				     $("#content").val(acontent);
				     if($.trim($("#fileTitle").val()) == ""){
				     	alert("标题不能为空！");
				     	$("#fileTitle").focus();
				     	return ;
				     }
				     $("#btnOK_newLocalFile1").attr("disabled","disabled");
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
				            		alert("对不起，发生异常！");
				            		return ;
				            	}else{//不可能发生的异常
				            		alert("对不起，数据提交异常！");
				            		return ;
				            	}
				            });
				
		
			}
			
			
			
		</script>
		
		<script type="text/javascript">
	    $(document).ready(function(){
		$(".biao_bg1").css("width","85px");
		});
  
  </script>
		
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;" >
	<div style="height:100%;overflow: auto;">
		<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
		<form id="fileForm" name="fileForm" action="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!editFile.action" method="post">
			<s:hidden name="folderId"></s:hidden>
			<s:hidden name="model.fileId"></s:hidden>
			<s:hidden id="content" name="content"></s:hidden>
			<%--<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="4" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">&nbsp;&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;查阅文件</td>
				</tr>
				--%>
				
				<table border="0" width="100%" cellpadding="1" cellspacing="0"
					align="center">
					
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查阅文件</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td id="btnOK_newLocalFile1" class="Operation_input" onclick="save();">&nbsp;确&nbsp;定&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
									                  		</tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
				</table>
				
				
				<tr>
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
				<td width="12%" height="21" class="biao_bg1" align="right"><span class="wz"><font color='red'>*</font> 标题：</span></td>
				<td  class="td1" colspan="3">
					<s:textfield id="fileTitle" cssStyle="width:300px" name="model.fileTitle" maxlength="20"></s:textfield>
				</td>
			
			<tr>
			
				<td width="12%" height="21" class="biao_bg1" align="right"><span class="wz">附件：</span></td>
				<td  class="td1" colspan="3">
					<A target="myiframe" href="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!download.action?id=<s:property value='model.fileId' />"><s:text name="model.fileName"></s:text>&nbsp;<span style="color:#3366cc">下载</span></A>
					<iframe name="myiframe" style="display: none;"></iframe>
				</td>
			</tr>
			<tr>
				<td width="12%" height="21" class="biao_bg1" align="right" valign="top">
				<span class="wz">备注：</span></td>
				<td id="tdcontent" class="td1" colspan="3">
					<script type="text/javascript">
							var oFCKeditor = new FCKeditor( 'content' );
							oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
							oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
							oFCKeditor.Width = '95%' ;
	                        oFCKeditor.Height = '200' ;
							oFCKeditor.Value = document.forms[0].content.value;
							oFCKeditor.Create() ;
						
					</script>
				</td>
			</tr>
			<tr>
				<td width="12%" height="21" class="biao_bg1" align="right"><span class="wz">添加人：</span></td>
				<td  class="td1">
					<s:text name="userName"></s:text>
				</td>
				<tr>
				<td width="12%" height="21" class="biao_bg1" align="right"><span class="wz">添加日期：</span></td>
				<td  class="td1">
					<s:date name="optDate" format="yyyy-MM-dd"/>
				</td>
				</tr>
				
				<tr>
				<td class="table_td"></td>
				<td></td>
				</tr>
				</table>
			</tr>
		  </table>
		</table>
	</form>
	</div>
</body>
</html>