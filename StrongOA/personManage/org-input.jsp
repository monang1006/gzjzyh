<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>增加机构</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>

		<script language="javascript">

	function hebing(){
	 window.showModalDialog("org-merge.jsp",window,'help:no;status:no;scroll:no;dialogWidth:260px; dialogHeight:300px');
	 
	}
	
	function yidong(){
	 window.showModalDialog("org-mobile.jsp",window,'help:no;status:no;scroll:no;dialogWidth:260px; dialogHeight:300px');
	
	}
	function exit(){
	$("#div1").attr("dispaly","none");
	$("#div2").attr("dispaly","block");
	}

	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form action="" id="veteranform" name="veteranform" method="post" enctype="multipart/form-data">
									<table width="100%" border="0" cellspacing="0" cellpadding="00"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<br>
									      <tr>
											<td width="5%" align="center">
												<img src="<%=frameroot%>/images/ico.gif" width="9"
													height="9">
											</td>
											<td width="12%">
												人事机构管理
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
																	
																	<td width="50">
																		<a class="Operation"
																			href="org-input.jsp">
																			<img src="<%=frameroot%>/images/tianjia.gif"
																				width="14" height="14" class="img_s">添加</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="org-input.jsp"> <img
																				src="<%=frameroot%>/images/bianji.gif" width="15"
																				height="15" class="img_s">编辑</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation" href="#"> <img
																				src="<%=frameroot%>/images/shanchu.gif" width="15"
																				height="15" class="img_s">删除</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">移动</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="javascript:hebing();">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">合并</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="#">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">导入</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="#">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">导出</a>
																	</td>
																	
																	
																	
																	<td width="5"></td>
																
																	</tr>
																	<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		
																	</td>
																	<td width="5"></td>
																	<td width="50">
																	
																	</td>
																	<td width="80" colspan="4" align="right"><br>
																	<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">编制情况</a>
																	</td>
																	<td width="5"></td>
																	<td width="80" colspan="4" align="left"><br>
																	<a class="Operation"
																			href="javascript:yidong();">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">人员信息</a>
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
						 <table width="100%">
			            	<tr>
				        	<td height="10">	
					  
				         	</td>
			              	</tr>
			           </table>
							<input type="hidden" id="" name=""
								value="">
							<input type="hidden" id="" name=""
								value="">
							
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构编号(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id=""
											
											name="" type="text" size="22"
											value="">
										
									</td>
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="">
									</td>
								</tr>
								
							
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name=""
											type="text" size="22" value=""
											onclick="">
										
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
								</tr>
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="orgTel" name="" type="text" size="22"
											value="">
									</td>
								</tr>
								<tr>
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
											value="" onclick="">
										
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否删除：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="22"
											value="" onclick="">
										
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">地址：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="" name="" type="text" size="60"
											value="">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">机构描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="" name=""
											rows="4" cols="38"></textarea>
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
				
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												<input name="Submit2" type="button" class="input_bg"
													value="保 存" onclick="">
											</td>
											<td width="30%">
												<input name="Submit2" type="button" class="input_bg"
													value="取 消">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
				
		</s:form>
		</DIV>
	</body>
</html>
