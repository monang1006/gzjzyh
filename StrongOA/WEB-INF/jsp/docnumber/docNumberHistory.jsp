<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文文号记录列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		function showName(name,value,number){			
			return name+"〔"+value+"〕"+number+"号";
		}
		
		$(document).ready(function(){
			$("#sousuo").submit();
			
		});
		</script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/docnumber/docNumberHistory.action">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													信息显示列表
												</td>
												<td width="70%">
													<table border="0" align="right" cellpadding="00"
														cellspacing="0">
														<tr>
															<td width="215">
																&nbsp;
															</td>

															<td >
																<a class="Operation" href="javascript:del();" /> <img
																		src="<%=root%>/images/ico/shanchu.gif" width="15"
																		height="15" class="img_s"> 删除&nbsp;</a>
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
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="docnumberHistoryid" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img id="sousuo" style="cursor: hand;"
												src="<%=root%>/images/ico/sousuo.gif" width="15"
												height="15">
										</td>
										<td width="70%" class="biao_bg1">
											<input class="search" name="searchdocnhword" type="text"
												title="请输入公文文号">
										</td>
										<td width="25%" class="biao_bg1">
											<input class="search" name="model.docnumberHistoryflag"
												type="text" title="请输入公文文号记录标识">
										</td>

										<td align="center" class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择"
									property="docnumberHistoryid" showValue="docnumberHistoryword"
									width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="公文文号"
									property="docnumberHistoryword"
									showValue="javascript:showName(docnumberHistoryword,docnumberHistoryyear,docnumberHistorynumber)"
									width="70%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="标识"
									property="docnumberHistoryflag"
									showValue="docnumberHistoryflag" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>

							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function del(){
	var id=getValue();
	if(checkSelectedOneDis()){
		if(confirm('确定删除选中的公文文号记录吗？')){
			location="<%=root%>/docnumber/docNumberHistory!delete.action?docnumberHistoryid="+id;
		}
	}
}
</script>
	</BODY>
</HTML>
