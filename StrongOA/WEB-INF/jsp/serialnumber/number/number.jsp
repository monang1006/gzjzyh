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
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<!-- 	<script language="javascript" src="<%=path%>/common/js/common/search.js"></script> -->
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		//按回车搜索
		function keydown(){
			if(event.keyCode==13){
				search();
			}
		}
		
         var addNumber=function(){
               window.location.href="<%=path%>/serialnumber/number/number!show.action?doinput=add";
         }
         
         var delNumber=function(){
          var id=getValue();
               if(id==null||id==""){
                   alert("请选择要删除的项！");   
               }else{
	               if(confirm("确定要删除吗?")) {
	                  window.location.href="<%=path%>/serialnumber/number/number!delete.action?numberId="+id;
	               }
               }
         }
         var show=function(value){
           if(value=='0'||value==''||value=='null'){
               return "已使用";
           }else if(value=='1'){
               return "预留中";
           }else if(value=="2"){
               return "已回收";
           }
         }
</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
			<td colspan="3" class="table_headtd">
						<s:form theme="simple" id="myTableForm"
							action="/serialnumber/number/number.action">
							<input type="hidden" name="disLogo" id="disLogo" value="seacsh">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>文号列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addNumber();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delNumber();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								wholeCss="table1" property="numberId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
											&nbsp;&nbsp;文号：&nbsp;<input name="numberNo" id="numberNo" type="text" class="search" title="请输入文号" value="${numberNo }">
								       		&nbsp;&nbsp;所属机构：&nbsp;<input name="orgName" id="orgId" type="text" class="search" title="请您输入所属机构" value="${orgName }">
								       		&nbsp;&nbsp;生成人：&nbsp;<input name="userName" id="userName" type="text" class="search" title="请您输入用户名" value="${userName }">
								       		&nbsp;&nbsp;文号状态：&nbsp;<s:select name="model.numberState" list="#{'全部':'','已使用':'0','预留中':'1','已回收':'2'}" listKey="value" listValue="key" onchange="search()"/>
								       		&nbsp;&nbsp;生成时间：&nbsp;<strong:newdate  name="model.numberTime" id="numberTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入生成时间" dateform="yyyy-MM-dd" dateobj="${numberTime}"/>
								       		
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="search()" type="button" />
							       <!-- 	</td>
										<td width="5%" align="center" class="biao_bg1">
											
											<img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15"  onclick="search()" style="cursor: hand;">
										</td>
										
										<td width="25%" align="center" class="biao_bg1">
											<input id="numberNo" name="model.numberNo" type="text"
												style="width:100%" value="${model.numberNo}" maxlength="50" class="search" title="请输入文号" onkeydown="keydown()">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input id="orgId" name="model.orgName" type="text"
												style="width:100%" value="${model.orgName}" maxlength="50" class="search" title="所属机构" onkeydown="keydown()">
										</td><td width="15%" align="center" class="biao_bg1">
											<input id="userName" name="userName" type="text"
												style="width:100%" value="${model.userName}" maxlength="50" class="search" title="用户名" onkeydown="keydown()">
										</td>
								      <td width="15%" align="center" class="biao_bg1">
								      		<s:select  name="model.numberState" list="#{'全部':'','已使用':'0','预留中':'1','已回收':'2'}" listKey="value" listValue="key" cssStyle="width:100%" onchange="search()" ></s:select>
											
										</td>
										<td width="15%" align="center" class="biao_bg1">
										<strong:newdate id="numberTime" name="model.numberTime"
												dateform="yyyy-MM-dd HH:mm" isicon="true" width="100%"
												dateobj="${model.numberTime}" classtyle="search" title="生成时间"/>
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td> -->
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="numberId"
									showValue="numberNo" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="文号" property="numberNo"
									showValue="numberNo" width="25%" isCanDrag="true"  isCanDrag="true" 
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="所属机构" property="orgId"
									showValue="orgName" width="25%" isCanDrag="true"  isCanDrag="true" 
									isCanSort="true" showsize="15"></webflex:flexTextCol>
									<webflex:flexTextCol caption="生成人" property="userId"
									showValue="userName" width="15%" isCanDrag="true"  isCanDrag="true" 
									isCanSort="true" showsize="15"></webflex:flexTextCol>
									<webflex:flexTextCol caption="文号状态" property="numberState"
									showValue="javascript:show(numberState)" width="15%" isCanDrag="true"  isCanDrag="true" 
									isCanSort="true" showsize="15"></webflex:flexTextCol>
							<webflex:flexDateCol caption="生成时间" property="numberTime"
									showValue="numberTime" width="15%" isCanDrag="true"
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
	 		<security:authorize ifAllGranted="001-0002000700030001">
		        item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","addNumber",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
			</security:authorize>
			<security:authorize ifAllGranted="001-0002000700030002">
		        item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delNumber",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		    </security:authorize>
	
	        sMenu.addShowType("ChangeWidthTable");
            registerMenu(sMenu);
         }
function getListBySta(){	//根据属性查询
<%--    document.getElementById("myTableForm").action="<%=path %>/serialnumber/number/number.action";--%>
<%--	document.getElementById("myTableForm").submit();--%>
		myTableForm.submit();
}
function search(){
	//cument.getElementById("pageNo").value="1";
	myTableForm.submit();
}
</script>
	</BODY>
</HTML>
