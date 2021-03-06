<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath3.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>图片上传</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.8.3.min.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>
<!-- webuploader -->
<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/webuploader/webuploader.css">
<script type="text/javascript" src="<%=path%>/common/js/webuploader/webuploader.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		var uploader = WebUploader.create({
			auto:true,
			//fileVal:fileName,
			//fileNumLimit:1,//最大允许文件数
		    // swf文件路径
		    swf: '<%=path%>/common/js/webuploader/Uploader.swf',
		    // 文件接收服务端。
		    server: "<%=path%>/servlet/upload",
		    // 选择文件的按钮。可选。
		    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
		    pick: "#filePicker",
		    accept: {
		        title: 'Images',
		        extensions: 'gif,jpg,jpeg,bmp,png',
		        mimeTypes: 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
		    },
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false,
		    duplicate: true
		});
		uploader.on('uploadSuccess', function(file,response) {
			//layer.close(logining);
			srcImagePath = response[0];
			
			document.getElementById("imgField").src = "<%=path%>" + response[0];
		});
		uploader.on('beforeFileQueued', function( file ) {
			//logining = layer.msg("服务器分析中...",{icon: 16,time:60000,shade: [0.8, '#393D49']});
			return true;
	    });
		uploader.on('error', function(type) {
			if (type == "Q_EXCEED_NUM_LIMIT") {
				alert("超出最大张数");
			}
		});
	});
	
</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<input type="hidden" id="imageX" name="imageX"/>
		<input type="hidden" id="imageY" name="imageY"/>
		<input type="hidden" id="imageW" name="imageW"/>
		<input type="hidden" id="imageH" name="imageH"/>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>图片上传</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="cutImage();">&nbsp;确&nbsp;定&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table> 
					<table width="100%" height="10%" border="0" cellpadding="0"
						cellspacing="0" align="center" class="table1">
						<tr>
							<td align="center">
								<div id="filePicker">选择图片</div>
							</td>
						</tr>
						<tr>
							<td align="center" style="height:10px;">
							</td>
						</tr>
						<tr>
							<td align="center">
								<img id="imgField" name="imgField"></img>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</DIV>
</body>
</html>