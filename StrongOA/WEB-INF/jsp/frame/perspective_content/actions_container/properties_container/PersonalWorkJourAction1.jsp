<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/web/js/grid/ChangeWidthTable.js'></script>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="../../../css/properties_windows.css" type=text/css
			rel=stylesheet>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>

		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=path%>/common/frame/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												信息显示列表
											</td>
											<td width="70%">
												<table width="283" border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="19">
															<img src="<%=path%>/common/images/quanxuan.gif"
																width="15" height="15">
														</td>
														<td width="33">
															全选
														</td>
														<td width="15">
															<img src="<%=path%>/common/images/tianjia.gif" width="10"
																height="10">
														</td>
														<td width="34">
															添加
														</td>
														<td width="20">
															<img src="<%=path%>/common/images/bianji.gif" width="14"
																height="15">
														</td>
														<td width="34">
															修改
														</td>
														<td width="17">
															<img src="<%=path%>/common/images/shanchu.gif" width="12"
																height="12">
														</td>
														<td width="34">
															删除
														</td>
														<td width="20">
															<img src="<%=path%>/common/images/baocun.gif" width="14"
																height="14">
														</td>
														<td width="34">
															保存
														</td>
														<td width="23">
															&nbsp;
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<div>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table1">
								<tr>
									<td>
										<table width="100%" border="0" cellpadding="0" cellspacing="1"
											class="table1">
											<tr>
												<td width="4%" align="center" class="biao_bg1">
													<img
														src="<%=path%>/common/frame/images/perspective_leftside/sousuo.gif"
														width="17" height="16">
												</td>
												<td width="60%" class="biao_bg1">
													<input name="textfield" type="text" size="60">
												</td>
												<td width="14%" align="center" class="biao_bg1">
													<input name="textfield2" type="text" size="18">
												</td>
												<td width="14%" align="center" class="biao_bg1">
													<input name="textfield22" type="text" size="18">
												</td>
												<td width="8%" class="biao_bg1">
													&nbsp;
													<select name="menu1"
														onChange="MM_jumpMenu('parent',this,0)">
														<option>
															已读
														</option>
														<option>
															未读
														</option>
													</select>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<TABLE width="100%" border="0" cellpadding="0" cellspacing="1"
											class="table1">
											<tr>
												<td align="center" class="biao_bg2">
													#
												</td>
												<td class="biao_bg2">
													<table width="100%" border="0" cellspacing="0"
														cellpadding="00">
														<tr>
															<td width="95%" align="center">
																标题
															</td>
															<td width="5%">
																<img
																	src="<%=path%>/common/frame/images/perspective_leftside/shang.gif"
																	width="11" height="10">
															</td>
														</tr>
													</table>
												</td>
												<td class="biao_bg2">
													<table width="100%" border="0" cellspacing="0"
														cellpadding="00">
														<tr>
															<td width="95%" align="center">
																发件人
															</td>
															<td width="5%">
																<img
																	src="<%=path%>/common/frame/images/perspective_leftside/xia.gif"
																	width="11" height="10">
															</td>
														</tr>
													</table>
												</td>
												<td class="biao_bg2">
													&nbsp;时间
												</td>
												<td class="biao_bg2">
													&nbsp;阅读状态
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
											<tr>
												<td height="21" align="center" class="td1">
													<input type="checkbox" name="checkbox" value="checkbox">
												</td>
												<td class="td1">
													<span class="wz">fdfdfdfdfdf</span>
												</td>
												<td class="td1">
													<span class="wz">dfdfdfd</span>
												</td>
												<td class="td1">
													<span class="wz">2008-09-21 14:52</span>
												</td>
												<td class="td1">
													<span class="wz">未读</span>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="biao_bg3">
													<table width="100%" border="0" cellspacing="0"
														cellpadding="00">
														<tr>
															<td width="2%">
																&nbsp;
															</td>
															<td width="5%">
																当前
															</td>
															<td width="4%">
																1/2
															</td>
															<td width="2%">
																页
															</td>
															<td width="3%">
																每页
															</td>
															<td width="5%">
																<input name="textfield3" type="text" size="2">
															</td>
															<td width="6%">
																<input name="Submit" type="submit" class="input_bg"
																	value="设置">
															</td>
															<td width="7%">
																数据总量
															</td>
															<td width="4%">
																55
															</td>
															<td width="21%">
																&nbsp;
															</td>
															<td width="6%">
																<input name="Submit2" type="submit" class="input_bg"
																	value="首页">
															</td>
															<td width="7%">
																<input name="Submit22" type="submit" class="input_bg"
																	value="上一页">
															</td>
															<td width="7%">
																<input name="Submit23" type="submit" class="input_bg"
																	value="上一页">
															</td>
															<td width="6%">
																<input name="Submit24" type="submit" class="input_bg"
																	value="尾页">
															</td>
															<td width="3%">
																转到
															</td>
															<td width="3%">
																<input name="textfield32" type="text" size="4">
															</td>
															<td width="2%">
																页
															</td>
															<td width="4%">
																<input name="Submit242" type="submit" class="input_bg"
																	value="转">
															</td>
															<td width="3%">
																&nbsp;
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
										</div>
									</td>
								</tr>
							</table>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>
