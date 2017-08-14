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
		function showValue(obj){
			if(obj==null||obj=="null"||obj==""||obj=="0")
			return "多者规则";
			else if(obj!=null&&obj=="/senddoc/sendDoc")
				return "发文规则";
			else if(obj!=null&&obj=="/recvdoc/recvDoc")
				return "收文规则";
		}
	
         var addregulation=function(){
               window.location.href="<%=path%>/serialnumber/regulation/regulation!input.action";
         }
         var editregulation=function(){
             var id=getValue();
             if(id==""){
             alert("请先选择规则！");
             reutrn;
             }else if(id.length>32){
              alert("不可以同时编辑多条规则！");
             return;
             }
               window.location.href="<%=path%>/serialnumber/regulation/regulation!input.action?regulationId="+id;
         }
         var delregulation=function(){
          var id=getValue();
               if(id==null||id==""){
                   alert("请选择要删除的项！");   
               }else{
	               if(confirm("确定要删除吗?")) {
	                  window.location.href="<%=path%>/serialnumber/regulation/regulation!delete.action?regulationId="+id;
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
							action="" >
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>文号规则</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addregulation();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editregulation();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delregulation();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								wholeCss="table1" property="regulationId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
											&nbsp;&nbsp;规则名称：&nbsp;<input name="regulationName" id="regulationName" type="text" class="search" title="请输入规则名称" value="${model.regulationName }">
								       		&nbsp;&nbsp;规则段落：&nbsp;<input name="regulationParagraph" id="regulationParagraph" type="text" class="search" title="请输入段落" value="${model.regulationParagraph }">
								       		&nbsp;&nbsp;创建人：&nbsp;<input name="regulationUser" id="regulationUser" type="text" class="search" title="请您输入创建人" value="${model.regulationUser }">
								       		&nbsp;&nbsp;创建时间：&nbsp;<strong:newdate  name="model.regulationTime" id="regulationTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${model.regulationTime}"/>
								       	</td>
								       	</tr>
								       	<tr>
								       	<td>	
								       		&nbsp;&nbsp;规则类型：&nbsp;<s:select name="model.regulationSort" list="#{'':'全部','0':'多者规则','/senddoc/sendDoc':'发文规则','/recvdoc/recvDoc':'收文规则'}" listKey="key" listValue="value" onchange='getListBySta()'/>
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="getListBySta()" type="button" />
										<!--  <td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="getListBySta()" style="cursor: hand;">
										</td>
										
										<td width="30%" align="center" class="biao_bg1">
											<input id="regulationName" name="regulationName" type="text"
												style="width:100%" value="${regulationName}" maxlength="50" class="search" title="请输入规则名称">
										</td>
										
										<td width="10%" align="center" class="biao_bg1">
											<input id="regulationParagraph" name="regulationParagraph"
												type="text" style="width:100%"
												value="${regulationParagraph}" class="search" maxlength="10" title="请输入段落">
										</td>
								
								       <td width="20%" align="center" class="biao_bg1">
											<input id="regulationUser" name="regulationUser"
												type="text" style="width:100%"
												value="${regulationUser}" class="search" maxlength="25" title="创建人">
										</td>
										<td width="20%" align="center" class="biao_bg1">
										<strong:newdate id="regulationTime" name="regulationTime"
												dateform="yyyy-MM-dd HH:mm" isicon="true" width="100%"
												dateobj="${regulationTime}" classtyle="search" title="创建时间"/>
										</td>
										 <td width="15%" align="center" class="biao_bg1"><s:select name="regulationSort" list="#{'':'全部','0':'多者规则','/senddoc/sendDoc':'发文规则','/recvdoc/recvDoc':'收文规则'}" listKey="key" listValue="value" style="width:100%" onchange='getListBySta()'/></td>
               
										<td class="biao_bg1">
											&nbsp;-->
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="regulationId"
									showValue="regulationName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="规则名称" property="regulationName"
									showValue="regulationName" width="30%" isCanDrag="true"  showsize="15"
									isCanSort="true"></webflex:flexTextCol>
								
									<webflex:flexTextCol caption="规则段落" property="regulationParagraph"
									showValue="regulationParagraph" width="10%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="创建人" property="regulationUser"
									showValue="regulationUser" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="创建时间" property="regulationTime"
									showValue="regulationTime" width="20%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
							<webflex:flexTextCol caption="规则类型" property="regulationSort"
									showValue="javascript:showValue(regulationSort);" width="15%" isCanDrag="true"
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
			<security:authorize ifAllGranted="001-0002000700020001">
		        item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","addregulation",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
	        </security:authorize>
	        <security:authorize ifAllGranted="001-0002000700020002">
		         item = new MenuItem("<%=root%>/images/ico/tianjia.gif","编辑","editregulation",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
			</security:authorize>
			<security:authorize ifAllGranted="001-0002000700020003">
		        item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delregulation",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
			</security:authorize>
	        sMenu.addShowType("ChangeWidthTable");
            registerMenu(sMenu);
         }
function getListBySta(){	//根据属性查询
    document.getElementById("myTableForm").action="<%=path %>/serialnumber/regulation/regulation.action";
	document.getElementById("myTableForm").submit();
}
</script>
	</BODY>
</HTML>
