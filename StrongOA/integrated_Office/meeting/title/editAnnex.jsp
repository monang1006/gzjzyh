<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
 <%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看附件</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
	</head>
	<script type="text/javascript">
		function selectTitle(){
			var ReturnStr=window.showModalDialog("selectTitle.jsp","selectTitleWindow","dialogWidth:600pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		}
	</script>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="90%"  style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
			        <td><img src="<%=frameroot%>/images/ico.gif" width="7" height="9"></td>
			        <td>查看附件</td>			    
				</tr>
			</table>	
			<table width="90%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">附件名称：</span>
					</td>
					<td class="td1"  align="left">
						<input id="annexName" name="annexName" type="text" size="22">
					</td>
				</tr>	
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">附件内容：</span>
					</td>
        			<td class="td1" colspan="3" align="left">
 		 					<script type="text/javascript" src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
													<script type="text/javascript">
													 
													var oFCKeditor = new FCKeditor( 'content' );
													oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '100%' ;
                                                    oFCKeditor.Height = '400' ;
													
													oFCKeditor.Value	= '\n          \t\t' ;
													oFCKeditor.Create() ;
													 
                                                    </script>                              
     				</td>
 				 </tr>
			</table>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="20%" height="34">
									&nbsp;
								</td>
								<td width="37%">
									<input name="Submit2" type="submit" class="input_bg"
										value="返 回" onclick="history.go(-1)">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
