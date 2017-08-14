<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>查看表单</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
		    .tabletitle {
		      FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                            gradientType = 0, 
		                            startColorStr = #ededed, 
		                            endColorStr = #ffffff);
		    }
		    .hand {
		      cursor:pointer;
		    }
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
		          var eform = document.getElementById("FormInputOCX");
		          eform.InitialData('<%=basePath%>/services/EFormService?wsdl');
		          eform.SetFormTemplateID('${id}');
				  eform.width=eform.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
		  		  eform.height=eform.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
		          eform.SetFieldsReadOnly(true);
			});
		   function goback(){
		   		window.close();
		   }
			
			function initOrgName(){}
			function initUserName(){}
			function initForm(){}
		</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" oncontextmenu="return false;">	
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">				
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
										查看表单——${model.title}
								</td>
								<td>
									&nbsp;
									<iframe id="downloadframe" name="downloadframe" style="display: none;"></iframe>
								</td>
								<td width="55%">
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<td width="*">
												&nbsp;
											</td>	
											 <%--<td width="100">
							                  <a class="Operation" href="#" onclick="JavaScript:goToPrint();"><img
							                                    src="<%=root%>/images/ico/chakan.gif" width="15"
							                                    height="15" class="img_s">打印处理单</a>
							                </td>
							                --%>
							                <td >
												<a class="Operation" href="<%=root%>/eformManager/eformManager!down.action?id=${id}"  target="downloadframe">
													<img src="<%=root%>/images/ico/xia.gif" width="15"
														height="15" class="img_s">下载表单&nbsp;</a>
											</td>
							                <td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="goback();">
													<img src="<%=root%>/images/ico/guanbi.gif" width="15"
														height="15" class="img_s">关闭&nbsp;</a>
											</td>	
											<td width="5"></td>																																	
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
		          <td height="100%" align="center"><object height="450" width="100%"
		              classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
		              codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
		              id="FormInputOCX"><param name="Visible" value="0" />
		              <param name="AutoScroll" value="0" />
		              <param name="AutoSize" value="0" />
		              <param name="AxBorderStyle" value="1" />
		              <param name="Caption" value="FormInputOCX" />
		              <param name="Color" value="4278190095" />
		              <param name="Font" value="MS Sans Serif" />
		              <param name="KeyPreview" value="0" />
		              <param name="PixelsPerInch" value="96" />
		              <param name="PrintScale" value="1" />
		              <param name="Scaled" value="-1" />
		              <param name="DropTarget" value="0" />
		              <param name="HelpFile" value="" />
		              <param name="ScreenSnap" value="0" />
		              <param name="SnapBuffer" value="10" />
		              <param name="DoubleBuffered" value="0" />
		              <param name="Enabled" value="-1" />
		            </object>
		          </td>
        		</tr>
			</table>
		</div>
	</body>
</html>
