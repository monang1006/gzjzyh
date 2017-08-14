<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
		<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改审批信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel=stylesheet>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
		/**
		 * 过滤特殊字符
		 * 
		 * @author 严建
		 * @date 2011年11月22日 15:12
		 * @param {}
		 *            suggestionValue:待处理字符
		 * @return temp 处理之后的字符串
		 */
		function EE_Filter(suggestionValue) {
			var temp = "";
			if ("undefined" != (typeof(suggestionValue))) {
				if (suggestionValue.indexOf("\"") != -1) { // 处理英文形式的双引号
					suggestionValue = suggestionValue.replace(new RegExp("\"", "gm"),
							"“");
				}
				if (suggestionValue.indexOf("\'") != -1) { // 处理英文形式的单引号
					suggestionValue = suggestionValue.replace(new RegExp("\'", "gm"),
							"’");
				}
				if (suggestionValue.indexOf("<") != -1) { // 处理英文形式的单引号
					suggestionValue = suggestionValue.replace(new RegExp("<", "gm"),
							"＜");
				}
				if (suggestionValue.indexOf(">") != -1) { // 处理英文形式的单引号
					suggestionValue = suggestionValue.replace(new RegExp(">", "gm"),
							"＞");
				}
				if (suggestionValue.indexOf("\\") != -1) { // 处理英文形式的的\
					suggestionValue = suggestionValue.replace(/[\\]/gm, "＼");
				}
				if (suggestionValue.indexOf("%") != -1) { // 处理英文形式的的%
					suggestionValue = suggestionValue.replace(/[%]/gm, "％");
				}
				temp = suggestionValue;
			}
			return temp;
		}
		function doSubmitBL(){
	   		if($("#handleDate").val() == ""){
	   			alert("办理时间是必填项！");
	   			return;
	   		}
   			$("body").mask("正在提交流程,请稍后...");
         	$("#myform").hide();
	  	  	$("#suggestion").val(EE_Filter($("#suggestion").val()));// modify
	  	  	$("form").submit();//提交表单,在流程草稿中直接提交流程
	        
   		}
		</script>
	</HEAD>
	<body class="contentbodymargin" >
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="20%">
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="85%" align="right">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
							<td>
								<s:form id="myform" action="/docbacktracking/docBackTracking!saveapproveinfo.action" method="post" target="myIframe">
									<fieldset >
							          <legend>
							            	修改审批信息
							          </legend>
							          <input type="hidden" name="approveinfo.aiId" id="aiId" value="${approveinfo.aiId }">
							          &nbsp;&nbsp;当前环节处理人：<font color="red">${approveinfo.aiActor}</font>
							          <div style="padding: 10px 10px 10px 10px; text-align: left;font-size: 12px;">
							            <div>
							              <textarea style="width: 680px" id="suggestion" name="approveinfo.aiContent" rows="12" cols="2" wrap="on">${approveinfo.aiContent==null?"":approveinfo.aiContent}</textarea>
							            </div>
							            <div>
							           <font color="red">*</font>办理时间： <strong:newdate name="approveinfo.aiDate"
												id="handleDate" skin="whyGreen" isicon="true"
												dateobj="${approveinfo.aiDate}"
												dateform="yyyy-MM-dd HH:mm:ss" width="87%"></strong:newdate>
							            </div>
							           <div align="center" id="btn_second" style="display: block;">
											<span><input type="button" id="next" value="  完    成  " onclick="doSubmitBL();" class="input_bg"></span>
											&nbsp;&nbsp;
											<span><input type="button" value="  取    消  " class="input_bg" onclick="window.close();"></span>
										</div>
							          </div>
							        </fieldset>
									</s:form>
							</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
	<iframe name="myIframe" style="display: none"></iframe>
</html>
