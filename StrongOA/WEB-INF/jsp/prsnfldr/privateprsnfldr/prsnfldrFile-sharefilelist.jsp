<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>共享文件列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/js/autocomplete/css/jquery.autocomplete.css" />
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script type='text/javascript'
			src='<%=path%>/common/js/autocomplete/js/jquery.autocomplete.min.js'></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js"
			type="text/javascript"></script>
			
			<style type="text/css">
		#search1{
		border:1px solid #b3bcc3;background-color:#ffffff;
		}
		</style>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="00">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            
            
            
             <td class="table_headtd_img" >
			<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
											
											<td width="25%">
												&nbsp;
												<label id="l_prsnfldr_folderName">
													${folderName }
												</label>
											</td>
											<td>
												&nbsp;
											</td>
											<%--<td width="70%">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<security:authorize ifAnyGranted="001-0001000700020001">
															<td>
																<a class="Operation" href="javascript:gotoView();"><img
																		class="img_s" src="<%=root%>/images/ico/page.gif"
																		width="15" height="15" />查阅&nbsp;</a>
															</td>
															<td width="5">
																&nbsp;
															</td>
														</security:authorize>
													</tr>
												</table>
											</td>--%>
											
											
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                
								                 	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	 </tr>
								            </table>
											</td>
											
											
											
										</tr>
									</table>
								</td>
							</tr>
							<s:form id="myTableForm"
								action="/prsnfldr/privateprsnfldr/prsnfldrFile!shareFileList.action">
								<input id="folderId" type="hidden" name="folderId"
									value="${folderId }" />
								<input id="folderName" type="hidden" name="folderName"
									value="${folderName }" />
								<!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="fileId" isCanDrag="true"
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
									getValueType="getValueByArray" collection="${page.result}"
									page="${page}">
									<%--<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img onclick="javascript:gotoSearch();"
													style="cursor: hand;" title="单击搜索"
													src="<%=root%>/images/ico/sousuo.gif" width="17"
													height="16">
											</td>
											<td width="40%" class="biao_bg1">
												<input id="search1" maxlength="32"
													url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!autoComplete.action"
													name="searchFileName" value="${searchFileName }"
													class="search" title="输入文件标题" />
											</td>
											<td width="*%" align="center" class="biao_bg1">
												&nbsp;
											</td>
											</td>
										</tr>
									</table>
									--%>
									
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;标题：&nbsp;<input id="search1" maxlength="32" 
													url="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!autoComplete.action"
													name="searchFileName" value="${searchFileName }"
													class="search" title="输入文件标题" />
						
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="javascript:gotoSearch();" id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							     
							</table>
									
									
									
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="标题" valuepos="0" valueshowpos="1"
										width="40%" onclick="javascript:gotoViewByTitle(this.value);"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexDateCol caption="添加日期" valuepos="2"
										valueshowpos="2" dateFormat="yyyy-MM-dd" width="20%"
										isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
									<webflex:flexTextCol caption="文件大小" valuepos="3"
										valueshowpos="3" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="查阅数" valuepos="4"
										valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<security:authorize ifAnyGranted="001-0001000700020001">
		item = new MenuItem("<%=root%>/images/operationbtn/Consult_the_reply.png","查阅","gotoView",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>	
//	item = new MenuItem("<%=path%>/personal_office/prsnfldr/images/copy.gif","复制","gotoCopy",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
//	item = new MenuItem("<%=path%>/personal_office/prsnfldr/images/cut.gif","剪切","gotoCut",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
//	item = new MenuItem("<%=path%>/personal_office/prsnfldr/images/paste.gif","粘贴","gotoParse",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//查看
function gotoView(){
	var url = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!download.action";
	var id = getValue();
	if(id == ""){
		alert("请选择要查阅的记录。");
		return;
	}else{
		var ids = id.split(",");
		if(ids.length>1){
			alert("只可以查阅一条记录。");
			return ;
		}
	}
	var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!initEditFile.action?folderId="
						+ $("#folderId").val() + "&id=" + id, "500", "300",
				window);
		if (ret != undefined && ret != "None") {
			parent.project_work_content.document.location.reload();
		} else {
			var readCount = $(":checked").parent().next().next().next().next()
					.text();
			$(":checked").parent().next().next().next().next().text(
					parseInt(readCount) + 1)
		}
	}
	//搜索
	function gotoSearch() {
		if ($("#folderName").val() == "/") {
			//alert("请先添加文件夹！");
			return;
		}
		$("form").submit();
	}
	//直接点标题进去查看
	function gotoViewByTitle(id) {
		var ret = OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!initEditFile.action?folderId="
		+$("#folderId").val()+"&id="+id,"500","300",window);
	if(ret!=undefined && ret!="None"){
		parent.project_work_content.document.location.reload() ;
	}else{
		var readCount = $(event.srcElement).next().next().next().text();
		$(event.srcElement).next().next().next().text(parseInt(readCount)+1);
	}
}
//复制
function gotoCopy(){
	copyFile($("#hrfCopy").get(0).url);
}
//剪切
function gotoCut(){
	cutFile($("#hrfCut").get(0).url);
}
//粘贴
function gotoParse(){
	parseFile($("#hrfParse").get(0).url);
}
</script>
	</BODY>
</HTML>
