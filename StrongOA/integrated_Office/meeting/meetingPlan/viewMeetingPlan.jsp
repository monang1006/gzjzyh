<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加会议计划</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<!--右键菜单脚本 -->
			   <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	</head>
	<script type="text/javascript">
<%--		$(document).ready(function(){--%>
<%--			alert("111111111111")--%>
<%--				$("textarea").each(function(){--%>
<%--					var textcss={"style":"overflow:auto"};--%>
<%--					alert(textcss);--%>
<%--					${this}.css(textcss);--%>
<%--				});--%>
<%--		});--%>
		function selectTitle(){
			var ReturnStr=window.showModalDialog("selectTitle.jsp","selectTitleWindow","dialogWidth:600pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		}
	</script>
	<body class=contentbodymargin oncontextmenu="return false;">
	
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						会议通知
					</td>
					<td width="*">
												&nbsp;
							</td>
											<td width="80">
												<a class="Operation" href="#" onclick="addTitle()"> <img
														src="<%=frameroot%>/images/shenyue.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">发送通知</span> </a>
											</td>
											
											<td width="5">&nbsp;</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">通知标题：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="name" name="name" type="text" size="100%">
					</td>
				</tr>
				
			
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议时间：</span>
					</td>
					<td  colspan="3" class="td1" align="left">
					<strong:newdate name="startDate" id="startDate"  width="236"
                      skin="whyGreen" isicon="true" dateobj="${startDate}" dateform="yyyy-MM-dd HH:mm"></strong:newdate>
						
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;	<strong:newdate name="endDate" id="endDate"  width="236"
                      skin="whyGreen" isicon="true" dateobj="${endDate}" dateform="yyyy-MM-dd HH:mm"></strong:newdate>
					</td>

				</tr>
			
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议地点：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="place" name="place" type="text" size="100%">
						<input type="button" value="选择" onclick="">
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议议题：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="titleId" name="titleId" type="text" size="100%"
							readonly="readonly">
						<input type="button" value="选择" onclick="selectTitle()">
					</td>
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">主 持 人：</span>
					</td>
					<td class="td1" align="left">
						<input id="moderator" name="moderator" type="text" size="24">
						<input type="button" value="选择" onclick="">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">记 录 人：</span>
					</td>
					<td class="td1" align="left">
						<input id="moderator" name="moderator" type="text" size="37">
						<input type="button" value="选择" onclick="">
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">参会人员：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="63" id="participants" name="participants"
							style="overflow:auto"></textarea>
							<input type="button" value="选择" onclick="">
					</td>
				</tr>
				<!--  
				<tr>
					
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">申 请 人：</span>
					</td>
					<td class="td1" align="left">
						<input id="moderator" name="moderator" type="text" size="24">
						<input type="button" value="选择" onclick="">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">是否需要审核：</span>
					</td>
					<td class="td1" align="left">
						<input type="checkbox" name="fruit" value ="apple" >
					</td>
				</tr>
				-->
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="5" cols="63" id="content" name="content"
							style="overflow:auto"></textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">安排事宜：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="5" cols="63" id="things" name="things"
							style="overflow:auto"></textarea>
					</td>
				</tr>
			

				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">备   注：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="5" cols="63" id="remarks" name="remarks"
							style="overflow:auto"></textarea>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="10%" height="34">
									&nbsp;
								</td>
							
								<td width="25%" align="center">
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
