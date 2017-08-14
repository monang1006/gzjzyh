<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构编制列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
			
		<LINK type=text/css rel=stylesheet	href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		
			<style type="text/css">
			.titlebottom {
	        cursor:default;
			padding-left:5;
			padding-top:2;
			padding-right:5;
			padding-bottom:0;
			text-align:center;
			width:100%;
			background-image:url("<%=path%>/image/personnel/tab-sprite6.gif");
          }
			</style>
		<script type="text/javascript">
		function onSub(){
		$("#isgoogle").val("search");
		    $("#myTableForm").submit();
		}
		function show(){
		 var id=getValue();
		 if(id==""){
			 alert("请先选择机构！");
			 return;
		 }else if(id.length>32){
			 alert("不可以同时查看多个机构");
			 return;
		 }else{
	    	window.location="<%=root%>/personnel/structure/personStructure!statisticInfo.action?orgId="+id;
	    	}
		}
		function showinfo(value){
		  window.location="<%=root%>/personnel/structure/personStructure!statisticInfo.action?orgId="+value;
		}
		  function radioOrg(){
		    var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!radiotree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return false;
		    }else{
              $("#baseorgName").val(audit[1]);	
              $("#baseorgid").val(audit[0]);    
		    }
		}
		function remove(){
		    $("#baseorgName").val("");	
              $("#baseorgid").val("");   
		}
			</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();">
		<DIV id=contentborder align=center>
		
			<s:form theme="simple" id="myTableForm"
				action="/personnel/structure/personStructure!statistic.action">
				<input type="hidden" name="org.orgid" value="${org.orgid }" id="baseorgid">
				<input type="hidden" name="isgoogle" id="isgoogle" value="">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid :   DXImageTransform .   Microsoft .   Gradient(gradientType =   0, startColorStr =   #ededed, endColorStr =   #ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													机构人员统计列表
												</td>
												<td>
													&nbsp;
												</td>
												<td width="70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table border="0" align="right"
																	cellpadding="0" cellspacing="0">

																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td align="right">
																			<a class="Operation"
																				href="javascript:show();"><img
																					src="<%=root%>/images/ico/chakan.gif" width="14"
																					height="14" class="img_s">查看详细</a>
																		</td>
																		
																		<td width="5"></td>




																	</tr>
																</table>
															</td>

														</tr>

														<tr higth="20"></tr>

														<tr>


														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="orgid" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" pageSize="10"
								getValueType="getValueByProperty" collection="${statpage.result}"
								page="${statpage}" pagename="statpage">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												id="img_sousuo" height="16" style="cursor: hand;"
												onclick="onSub();" title="单击搜索">
										</td>
										<td width="35%" align="center" class="biao_bg1" nowrap>
										<input type="button" value="清空" style="width:25%;"  class="input_bg" onclick="remove();">
										<s:textfield id="baseorgName" name="org.orgName" onclick="radioOrg();"
												cssClass=""  style="width:75%; color: #999999;" value="请选择所属机构"  onkeydown="return false;"  title="请选择所属机构"></s:textfield>
												
										</td>
										<td width="60%" align="center" class="biao_bg1">
											
											<input name="" type="text" style="width:100%" readonly="readonly">
										</td>

									    <td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="toabaseorg.orgid"
									showValue="toabaseorg.orgName" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="所属机构" 
									property="toabaseorg.orgid" showValue="toabaseorg.orgName" width="35%"
									isCanDrag="true" onclick="showinfo(this.value)" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol showsize="20" caption="应有编内人数" 
									property="properPerson" showValue="properPerson" width="33%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="实际人数" property="realityPerson"
									showValue="realityPerson" width="32%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								
                      	</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		
			<div id="bar">
			<img src="<%=path %>/img/${temp1}.jpg">
			</div>
			
		</DIV>
		
		<script language="javascript">
           var sMenu = new Menu();
          function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看详细","show",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			    sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
  
          }




</script>
	</BODY>
</HTML>
