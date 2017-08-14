<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js" language="javascript"></script>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
			function viewState(state){
				if(state=="1")
					return "<img src='<%=root%>/images/ico/read.gif'>";
				else 
					return "<img src='<%=root%>/images/ico/unread.gif'>";
			}
			function viewtime(stime,etime){
				return stime+":"+etime;
			}
			function borrowView(borrowId){
	if(borrowId=="null"||borrowId==""){
		alert("请至少选择一条借阅信息!");
		return;
	}else if(borrowId.indexOf(",")!=-1){
		alert("只能选择一条借阅信息！");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==borrowId){
			var borrowAuditing = chkButtons[i].parentElement.parentElement.cells[5].value;
			var showValue = chkButtons[i].showValue;
			var borrowFromtime = showValue.substring(showValue.indexOf("'")+1,showValue.indexOf(',')-1);
			var borrowEndtime = showValue.substring(showValue.indexOf(',')+2,showValue.lastIndexOf("'"));
			borrowFromtime = borrowFromtime.substring(0,borrowFromtime.indexOf('.'));
			borrowEndtime = borrowEndtime.substring(0,borrowEndtime.indexOf('.'));
			var d1 = new Date(borrowFromtime.replace(/-/,"/"));
       		var d2 = new Date(borrowEndtime.replace(/-/,"/"));	
<%--       		alert(d1);--%>
<%--       		alert(d2);		--%>
			var nowtime = new Date();
			var totime=new Date();
			totime.setDate(totime.getDate()-1);  
			if(borrowAuditing=="0"){
				alert("该申请还未审批或未通过，不能查看！");
				return 
			}else if(Date.parse(nowtime) - Date.parse(d1)<0){
				alert("该文件还未到可查看时间！");
				return 
			}else if(Date.parse(totime) - Date.parse(d2)>0){
			
				alert("该文件已经超过归还时间，不可以查看！");
				return 
			}
			break;
		}
	}
	location="<%=path%>/archive/archiveborrow/archiveBorrow!viewFile.action?borrowId="+borrowId;
}

      function showFileTilte(title,value){
           var rv="";
           rv="<a href=javascript:borrowView('"+value+"')><font color='blue'>"+title+"</font></a>"
           return rv;
      }
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin  oncontextmenu="return false;" onload="initMenuT()">
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple" action="/archive/archiveborrow/archiveBorrow.action">
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
													<strong>我的借阅文件列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="show();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查看申请单&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="2%"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="borrowId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;文件编号：&nbsp;<input name="model.toaArchiveFile.fileNo" type="text"  value="${model.toaArchiveFile.fileNo}" class="search" title="文件编号">
							       		&nbsp;&nbsp;文件名称：&nbsp;<input name="model.toaArchiveFile.fileTitle" type="text"  value="${model.toaArchiveFile.fileTitle}" class="search" title="文件名称">
							       		&nbsp;&nbsp;借阅时间：&nbsp;<strong:newdate id="borrowFromtime" name="model.borrowFromtime" dateform="yyyy-MM-dd"
									  	dateobj="${model.borrowFromtime}" isicon="true" classtyle="search" title="借阅时间" width='146px'/></br>
							       		&nbsp;&nbsp;归还时间：&nbsp;<strong:newdate id="borrowEndtime" name="model.borrowEndtime" dateform="yyyy-MM-dd"  dateobj="${model.borrowEndtime}" isicon="true" classtyle="search" title="归还时间" width='146px'/>
							       		&nbsp;&nbsp;申请状态：&nbsp;<s:select name="model.borrowAuditing" list="#{'0':'待审','1':'已审','3':'驳回'}" listKey="key" listValue="value"  onchange="search()"  style='width:146px'/>
							       		&nbsp;&nbsp;查看状态：&nbsp;<s:select name="model.borrowViewState" list="#{'':'查看状态','0':'未查看','1':'已查看'}" listKey="key" listValue="value"  onchange="search()" style='width:146px'/>
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
<%--							<%--<webflex:flexCheckBoxCol caption="选择" property="borrowId" showValue="javascript:viewtime(borrowFromtime,borrowEndtime)" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>--%>
								<webflex:flexCheckBoxCol caption="选择" property="borrowId" showValue="toaArchiveFile.fileTitle" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="文件编号" property="toaArchiveFile.fileNo" showValue="toaArchiveFile.fileNo" width="16%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="文件名称" property="toaArchiveFile.fileTitle" showValue="javascript:showFileTilte(toaArchiveFile.fileTitle,borrowId)" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="借阅时间" property="borrowFromtime" showValue="borrowFromtime" width="16%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexDateCol caption="归还时间" property="borrowEndtime" showValue="borrowEndtime" width="16%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexEnumCol caption="申请状态" mapobj="${statemap}" property="borrowAuditing" showValue="borrowAuditing" width="11%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="查看状态" property="borrowViewState" showValue="javascript:viewState(borrowViewState)" width="11%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
			item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看申请单","show",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		
		function search(){
			myTableForm.submit();
		}
		function submitForm(){
		 	myTableForm.submit();
		}
		function show(){
		var id=getValue();
		if(id==""){
		alert("请选择一份借阅文件！");
		return;
		}
		if(id.length>32){
		alert("每次只可以查看一份借阅信息！");
		return;
		}
		location="<%=path%>/archive/archiveborrow/archiveBorrow!show.action?borrowId="+id;
		}
		</script>
	</BODY>
</HTML>