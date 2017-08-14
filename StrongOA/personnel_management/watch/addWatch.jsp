<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>保存值班记录</title>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript">
			function setDate(){
<%--				var df=new DateFormat();--%>
<%--				df.applyPattern("yyyy-MM-dd HH:mm");--%>
				var nowDate=new Date();
<%--				var str=df.format(nowDate);--%>
<%--				alert(str)--%>
				document.getElementById("time").value=nowDate;
				
			}
		 	function selectPerson(){
		 	}
		 	function selectWatchType(){
		 		var ReturnStr=window.showModalDialog("selectType.jsp","selectTypeWindow","dialogWidth:600pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		 	}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						添加值班记录
					</td>
					<td width="*">
						&nbsp;
					</td>	
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">来访人姓名：</span>
					</td>
					<td class="td1" align="left">
						<input id="interviewer" name="interviewer" type="text" size="30">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访人电话：</span>
					</td>
					<td class="td1" align="left">
						<input id="phone" name="phone" type="text" size="30">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访时间：</span>
					</td>
					<td class="td1" align="left">

						<input id="time" name="time" type="text" size="30" value="当天"
							readonly="readonly">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访人地址：</span>
					</td>
					<td class="td1" align="left">
						<input id="place" name="place" type="text" size="30">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">被访人员：</span>
					</td>
					<td class="td1" align="left">
						<input id="interviewed" name="interviewed" type="text" size="30"
							readonly="readonly" value="人员列表中选择">
						<input type="button" value="选择" onclick="selectPerson()">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">接待人：</span>
					</td>
					<td class="td1" align="left">
						<input id="reception" name="reception" type="text" size="30"
							value="用户" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">访问类型：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="type" name="type" type="text" size="30">
						<input type="button" value="选择" onclick="selectWatchType()">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">访问内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<script type="text/javascript"
							src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
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
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="34%" height="34">
									&nbsp;
								</td>
								<td width="29%">
									<input name="Submit" type="submit" class="input_bg" value="保 存">
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
		<%--		<script type="text/javascript">--%>
		<%--			setDate();--%>
		<%--		</script>--%>
	</body>

</html>
