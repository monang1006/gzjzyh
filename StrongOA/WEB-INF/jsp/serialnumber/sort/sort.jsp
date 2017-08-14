<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<!-- 	<script language="javascript" src="<%=path%>/common/js/common/search.js"></script> -->
		<SCRIPT language="javascript" type="text/javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">

         var addsort=function(){
               window.location.href="<%=path%>/serialnumber/sort/sort!input.action";
         }
         var editsort=function(){
               var id=getValue();
               if(id==null||id==""){
                  alert("请选择要修改的项！");   
               }else if(id.length>32){
                  alert("只可以修改一项内容！");
               }else{
               		 
               		
                  window.location.href="<%=path%>/serialnumber/sort/sort!input.action?sortId="+id;
               }
         }
         var delsort=function(){
          var id=getValue();
               if(id==null||id==""){
                  alert("请选择要删除的项！");   
               }else{
               	  if(confirm("确定要删除吗?")) {
                 	 window.location.href="<%=path%>/serialnumber/sort/sort!delete.action?sortId="+id;
                  }
               }
         }
</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<s:form theme="simple" id="myTableForm"
							action="">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>文号类型</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addsort();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editsort();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delsort();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="sortId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
									<td>
										&nbsp;&nbsp;类型名称：&nbsp;<input name="sortName" id="sortName" type="text" class="search" title="请输入文号类型名称" value="${model.sortName }">
							       		&nbsp;&nbsp;代字：&nbsp;<input name="sortAbbreviation" id="sortAbbreviation" type="text" class="search" title="请输入包含代字" value="${model.sortAbbreviation }">
							       		&nbsp;&nbsp;创建时间：&nbsp;<strong:newdate  name="model.sortTime" id="sortTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入创建时间" dateform="yyyy-MM-dd" dateobj="${model.sortTime}"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="getListBySta()" type="button" />
										<!-- <td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="getListBySta()" style="cursor: hand;">
										</td>
										
										<td width="30%" align="center" class="biao_bg1">
											<input id="sortName" name="sortName" type="text"
												style="width:100%" maxlength="25" value="${sortName}" class="search" title="请输入文号类型名称">
										</td>
										<td width="30%" align="center" class="biao_bg1">
											<input id="sortAbbreviation" name="sortAbbreviation"
												type="text" style="width:100%"
												value="${sortAbbreviation}" class="search" maxlength="10" title="请输入包含代字">
										</td>
								
										<td width="30%" align="center" class="biao_bg1">
										<strong:newdate id="sortTime" name="sortTime"
												dateform="yy-MM-dd HH:mm" isicon="true" width="100%"
												dateobj="${sortTime}" classtyle="search" title="编辑时间"/>
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td> -->
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="sortId"
									showValue="sortName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="类型名称" property="sortName"
									showValue="sortName" width="30%" isCanDrag="true"  showsize="15"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="包含代字" property="sortAbbreviation"
									showValue="sortAbbreviation" width="30%" isCanDrag="true" showsize="30"
									isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="创建时间" property="sortTime"
									showValue="sortTime" width="30%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
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
	        <security:authorize ifAllGranted="001-0002000700010003">
		        item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","addsort",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		    </security:authorize>
		    <security:authorize ifAllGranted="001-0002000700010003">
		        item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editsort",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
			</security:authorize>
			<security:authorize ifAllGranted="001-0002000700010003">
		        item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delsort",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
			</security:authorize>
	
	        sMenu.addShowType("ChangeWidthTable");
            registerMenu(sMenu);
         }
function getListBySta(){	//根据属性查询
document.getElementById("myTableForm").action="<%=path%>/serialnumber/sort/sort.action";
	document.getElementById("myTableForm").submit();
}
</script>
	</BODY>
</HTML>
