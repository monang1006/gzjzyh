<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>下达编制</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
				<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
			<script type="text/javascript">
			
			function addStructure(){
	         var audit= window.showModalDialog("structure-input.jsp",window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
	
             }
      function editStructure(){
	

       }
			</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			
									<table width="100%" border="0" cellspacing="0" cellpadding="00"
									 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br><tr>
											<td width="5%" align="center">
												<img src="<%=frameroot%>/images/ico.gif" width="9"
													height="9">
											</td>
											<td width="12%">
												组织机构信息 
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="87%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">

																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																
																	<td width="90">
																		<a class="Operation" href="javascript:editStructure();"> <img
																				src="<%=frameroot%>/images/goujian.gif" width="15"
																				height="15" class="img_s">下达编制</a>
																	</td>
																	<td width="5"></td>
																	
																	</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<br>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">组织机构编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id=""
											
											name="" type="text" size="22"
											value="">
										
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">组织机构名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="">
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">组织机构类别：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="">
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name=""
											type="text" size="22" value=""
											onclick="" readonly="readonly">
										
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构性质：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name=""
											list="#{'0':'无','1':'行政','2':'事业','3':'企业','4':'社团'}"
											listKey="key" listValue="value" style="width:11em" />
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构邮编：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgTel" name="" type="text" size="22"
											value="">
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传真：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgFax" name="model.orgFax" type="text" size="22"
											value="">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">负责人：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="" onclick=""
											readonly="readonly">
										
									</td>
								
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="60"
											value="">
									</td>
								</tr>
								<tr >
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="7" align="left">
										<textarea id="" name=""
											rows="4" cols="50"></textarea>
									</td>
								</tr>

							</table>
						
						
					<br>
			<div>
				<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="userId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=frameroot%>/images/sousuo.gif" width="17" id="img_sousuo"
												height="16" style="cursor: hand;" title="单击搜索">
										</td>
									   <td width="25%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入编制类型"></s:textfield>
										</td>
										<td width="25%" align="center" class="biao_bg1">
											 <s:textfield name="selectloginname" cssClass="search" title="请输入编制数量"></s:textfield> 
										</td>
										<td width="25%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入修改时间"></s:textfield>
										</td>
										
										<td width="20%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入状态"></s:textfield>
										</td>
										
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="userId"
									showValue="userName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexEnumCol caption="编制类型" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="25%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="编制数量" property="userLoginname"
									showValue="userLoginname" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="修改时间" property="baseOrg.orgId"
									showValue="baseOrg.orgName" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								
								<webflex:flexEnumCol caption="状态" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								
							</webflex:flexTable>
			</div>
		</DIV>
	</body>
</html>
