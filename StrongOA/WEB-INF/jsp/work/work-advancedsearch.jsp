<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>工作流高级查询</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<style>
			html { overflow-y: scroll; }
			body {font-family: Tahoma,Arial,Helvetica,Sans-Serif; background: #E7F1F8; margin: 0px; padding: 0px;}
			ul {
				margin: 0;
				margin-left: 3px;
				padding: 0;
				line-height:20px;
			}
			li {
				margin: 0 0 0 15px;
				padding: 0px 0 0px 0;
				list-style-type: disc;
				line-height:20px;
			}
			.module {border: 1px #dedcd8 solid; background: white; padding: 1px;margin-bottom:10px;margin-left:10px;}
			.moduleHeader {border-bottom: 1px #dedcd8 dotted; padding: 3px 1%; margin: 0; font-size: 12px;  overflow: hidden; font-weight: normal; line-height: 14px; }
			.moduleHeader .icon {float: left; padding: 5px 0px 0px 0px;}
			.listColor {border: 1px #b8d1e2 solid; color: #003366;}
			.listColor .moduleHeader {background: #e3eef2 url("list_hd_bg.png") repeat-x scroll left top; border-bottom: 1px #b8d1e2 dotted;}
		</style>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script>
			function _resize(module_id){
				 var body_i=$("#module_"+module_id+"_body").get(0);
				 var img_i=$("#img_resize_"+module_id).get(0);
				 if(body_i.style.display=="none"){
				    body_i.style.display="block";
				    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"expand_arrow.png";
				    img_i.title="折叠";
				 }
				 else{
				    body_i.style.display="none";
				    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"collapse_arrow.png";
				    img_i.title="展开";
				 }
			}
			//加载数据
			function loadContent(workflowTypeId){
				$.post("<%=root%>/work/work!getWorkFlowInfoByWorkFlowTypeId.action",
						{workflowType:workflowTypeId},
						function(data){
							$("#module_"+workflowTypeId+"_ul").html(data);
						});
			}
		</script>
	</head>

	<body oncontextmenu="return false;" style="margin-top:5px;" >
	<div id="contentborder" >
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=frameroot%>/images/ico/ico.gif" align="absmiddle">
					<span class="big3"> 工作流高级查询 - 请选择流程</span> &nbsp;&nbsp; 
					<input type="button" value="返回" class="input_bg"
						onClick="location='<%=root %>/work/work!query.action?workflowType='+${workflowType };">
				</td>
			</tr>
		</table>
		<table width="99%" border="0" cellpadding="0" cellspacing="0"
			style="margin-top:5px;margin-right:5px">
			<tr>
				<td width="50%" valign="top">
					<div id="div_l">
						<s:iterator value="#request.workFlowLst" status="index">
							<s:if test="#index.odd == true">
								<div id="module_<s:property value='workFlowTypeId' />" class="module listColor">
									<div class="head" onclick="_resize(<s:property value='workFlowTypeId' />);"
										style="cursor:pointer">
										<h4 id="module_<s:property value='workFlowTypeId' />_head" class="moduleHeader">
											<img class="icon" id="img_resize_<s:property value='workFlowTypeId' />"
												src="<%=root%>/oa/image/work/expand_arrow.png" title="折叠" />
											<span id="module_<s:property value='workFlowTypeId' />_text" class="text"><s:property
													value="workFlowTypeName" /> </span>
										</h4>
									</div>
									<div id="module_<s:property value='workFlowTypeId' />_body" class="module_body">
										<div id="module_<s:property value='workFlowTypeId' />_ul" class="module_div">
											<ul>
												<li>
													<img src="<%=path%>/oa/image/desktop/loading.gif">
												</li>
											</ul>
										</div>
									</div>
								</div>
								<script language="JavaScript">loadContent('<s:property value="workFlowTypeId"/>');</script>
							</s:if>
						</s:iterator>
						<div class="shadow"></div>
					</div>
				</td>
				<td width="50%" valign="top">
					<div id="div_r">
						<s:iterator value="#request.workFlowLst" status="index">
							<s:if test="#index.odd == false">
								<div id="module_<s:property value='workFlowTypeId' />" class="module listColor">
									<div class="head" onclick="_resize(<s:property value='workFlowTypeId' />);"
										style="cursor:pointer">
										<h4 id="module_<s:property value='workFlowTypeId' />_head" class="moduleHeader">
											<img class="icon" id="img_resize_<s:property value='workFlowTypeId' />"
												src="<%=root%>/oa/image/work/expand_arrow.png" title="折叠" />
											<span id="module_<s:property value='workFlowTypeId' />_text" class="text"><s:property
													value="workFlowTypeName" /> </span>
										</h4>
									</div>
									<div id="module_<s:property value='workFlowTypeId' />_body" class="module_body">
										<div id="module_<s:property value='workFlowTypeId' />_ul" class="module_div">
											<ul>
												<li>
													<img src="<%=path%>/oa/image/desktop/loading.gif">
												</li>
											</ul>
										</div>
									</div>
								</div>
								<script language="JavaScript">loadContent('<s:property value="workFlowTypeId"/>');</script>
							</s:if>
						</s:iterator>
						<div class="shadow"></div>
					</div>
				</td>
		</table>
	</div>	
	</body>
</html>
