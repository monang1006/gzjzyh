<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>南昌市人民政府办公厅办公自动化系统</title>
		<link href="ncbgt/css.css" rel="stylesheet" type="text/css" />

		<script language="javascript" src="<%=path%>/resource/jquery.js"></script>
		<SCRIPT type=text/javascript src="<%=path%>/resource/officecontrol.js"></SCRIPT>
		<script type="text/javascript">
		
		/**
		 * 加载OFFICE控件
		 */
	var OfficeTabContent = "";
	OfficeTabContent += "<object id=\"TANGER_OCX_OBJ\" width=\"900px\" height=\"780px\" classid=\"clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404\" "; 
		OfficeTabContent += "codebase=' resource/common/OfficeControl/OfficeControl.cab#Version=5,0,1,8'>";
	OfficeTabContent += "<param name='MakerCaption' value=\"思创数码科技股份有限公司\">";
	OfficeTabContent += "<param name='MakerKey' value='5C1FF1F1177246B272DB34DD8ADA318222D19F65'>";
	OfficeTabContent += "<param name='ProductCaption' value=\"南昌市政府办公厅\">";
	OfficeTabContent += "<param name='ProductKey' value='FD6357E9840E880F0B72EAC5357E7303379848AB'>";
	OfficeTabContent += "<param name='TitleBar' value='false'>";
	OfficeTabContent += "<param name='ToolBars' value='false'>";
	OfficeTabContent += "<param name='Menubar' value='false'>";
	OfficeTabContent += "<param name='BorderStyle' value='1'>";
	OfficeTabContent += "<param name='TitlebarColor' value='42768'>";
	OfficeTabContent += "<param name='TitlebarTextColor' value='0'>";
	OfficeTabContent += "<param name='IsResetToolbarsOnOpen' value='true'>";
	OfficeTabContent += "<param name='IsUseUTF8URL' value='true'>";
	OfficeTabContent += "<param name='IsUseUTF8Data' value='true'>";
	OfficeTabContent += "<span style=\"color: red\">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span></object>";

	/**
	 * 打开文档
	 */
	function openFromlocURL(newPath) {
		TANGER_OCX_OBJ.activate(true);
		/**
		 * OFFICE控件读取路径
		 */
		TANGER_OCX_OBJ.OpenFromURL(newPath);
		/**
		 * 隐藏标尺
		 */
		TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.ActivePane.DisplayRulers = false;
		/**
		 * 比例更改为 125%。
		 */
		TANGER_OCX_OBJ.ActiveDocument.ActiveWindow.View.Zoom.Percentage = 100;

	}
	/**
	 * 默认一开始加载OFFICE文档
	 */
	$(document).ready(
			function() {
				if ($("#offcieTd").attr("height") != "undefined"
						&& $("#offcieTd").attr("height") != "") {
					$("#offcieTd").html(OfficeTabContent);
					var newPath = $("#newPath").val();
					openFromlocURL(newPath);
				}
			});
	/**
	 * 保存要情
	 */
	function saveYQ(){
		/**
		 * 获得领导批示对象
		 */
		var remark = $("#remark").val();
		
		/**
		 * 判断领导批示内容是否为空
		 */
		if(remark==""){
			alert("批示内容不能为空!");
			return false;
		}
		/**
		 * 判断领导批示内容是否过长
		 */
		if(remark.length>500){
			alert("批示内容过长!");
			return false;
		}
		/**
		 * 提交表单
		 */
		$("#myform").submit();
		/**
		 * 关闭窗口
		 */
		window.close();
	}
	
</script>
	</head>
	<base target="_self" />
	<body>



		<div class="center md">
			<table width="983" border="0" cellpadding="0" cellspacing="0"
				class="center">

				<tr>

					<td
						style="border-left: 1px solid #d1ca7a; border-right: 1px solid #d1ca7a;"
						height="500" valign="top">
						<div class="news_titile">
							<a name="1" id="1"></a>

							<INPUT style="WIDTH: 708px; HEIGHT: 20px" id="newPath"
								name="newPath"
								value="${path1}"
								size="3" type="hidden" />
							<TABLE width='100%' height='100%'>
								<TBODY height='100%' width='100%'>
									<TR height='100%' width='100%'>
										<TD id=offcieTd height='100%' align='center' vAlign=top
											colSpan=4>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
							</FONT></FONT>

						</div>
					</td>
					<td valign="top">
					<s:form id="myform" action="/senddoc/sendDoc!saveYQ.action" theme="simple">
						<input type="hidden" name="issId" id="issId"  value="${issId}"/>
						<span style="font-family: 黑体; font-size: 20px;">领导批示：</span>
						
						<br />
						<s:if test="flag!='success'">
							<s:if test="%{pub.articlesInstructionContent==null}">
						<textarea rows="5" cols="20" style="width: 400px; height: 250px;" id="remark" name="remark">第一条：&#13;第二条：&#13;第三条：&#13;第四条：&#13;第五条：&#13;第六条：&#13;第七条：&#13;第八条：&#13;第九条：&#13;第十条：&#13;第十一条：&#13;第十二条：&#13;第十三条：&#13;第十四条：&#13;第十五条：</textarea>
							</s:if>
							<s:else>
							<textarea rows="5" cols="20" style="width: 400px; height: 250px;" id="remark" name="remark">${pub.articlesInstructionContent}</textarea>	
							</s:else>
						<input type="button" value="提交" onclick="saveYQ()"/>
						</s:if>
						<s:else>
						<textarea readonly="readonly" rows="5" cols="20" style="width: 400px; height: 250px;" id="remark" name="remark">${pub.articlesInstructionContent}</textarea>	
						</s:else>
						<br />
					</s:form>
					</td>


				</tr>

			</table>

		</div>

	</body>

</html>
