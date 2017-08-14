<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>查看投票记录</TITLE>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

        	
        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		
		
<SCRIPT>
</SCRIPT>

</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" >
<DIV id=contentborder align=center>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form theme="simple" id="myTableForm" action="/vote/voteLog!list.action"  >	
							<input type="hidden" name="vid" value="${vid}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>投票记录列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	                <td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	                <td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		                <td width="6"></td>
														</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
							
	<webflex:flexTable name="myTable" width="100%"  showSearch="false" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">   
	  	<webflex:flexTextCol caption="用户名"  property="username" showValue="username" width="30%" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>
	        <webflex:flexTextCol caption="IP" property="IP" showValue="IP" width="20%" isCanDrag="true" isCanSort="true"  ></webflex:flexTextCol>
	<webflex:flexTextCol caption="手机号"  property="mobile" showValue="mobile" width="30%" isCanDrag="true"  isCanSort="true"></webflex:flexTextCol>		
		<webflex:flexDateCol caption="参与时间" property="vote_date" showValue="vote_date" dateFormat="yyyy-MM-dd  HH:mm" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	</webflex:flexTable>
</form>
</td></tr></table>
</DIV>

</BODY></HTML>
