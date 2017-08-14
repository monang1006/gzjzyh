<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link type=text/css rel=stylesheet href="<%=frameroot%>/css/strongitmenu.css">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT type="text/javascript">
			function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=='0')
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;'>";
			}
			
			function showAuName(value){
			     if(value==null||value=="null")
			     {
			        return "";
			     }else{
                    return value;			     
			     }
			}
			function showfilePage(value){
			 if(value==null||value=="null")
			     {
			        return "";
			     }else{
                    return value;			     
			     }
			}
		</SCRIPT>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm" action="/archive/archivefolder/archiveFolder!input.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
													档案卷(盒)查看
												</td>
												<td width="70%">
													<table width="20%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
														<!-- 	<td width="50">
																<a class="Operation" href="#"><img src="<%=root%>/images/ico/quanxankongzhi.gif" width="15" height="15" class="img_s">权限</a>
															</td>
															<td width="5"></td> -->
															<td >
																<a class="Operation" href="javascript:cancel();"><img src="<%=root%>/images/ico/ht.gif" width="15" height="15" class="img_s">返回</a>
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
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<input id="folderId" name="folderId" type="hidden" size="30" value="${model.folderId}">
								<input id="forward" name="forward" type="hidden" size="30" value="archiveFile">
								<input id="moduletype" name="moduletype" type="hidden" size="30" value="${moduletype}">
								<input id="archiveSortId" name="archiveSortId" type="hidden" size="30" value="${archiveSortId}">
<%--								<input id="archiveSortId" name="model.toaArchiveSort.sortId" type="hidden" size="30" value="${model.toaArchiveSort.sortId}">--%>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">所属类目：</span>
										<br>
									</td>
									<td class="td1" align="left">
										
										<input id="archiveSortName" name="model.toaArchiveSort.sortName" type="text"
											size="24" value="${archiveSortName}" readonly="readonly">
										<br>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">书名：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderFormName" name="model.folderFormName" type="text"
											size="24" value="${model.folderFormName}" readonly="readonly">
										<br>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">卷(盒)号：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderNo" name="model.folderNo" type="text" size="24" value="${model.folderNo}" readonly="readonly">
										<br>
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">题名：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderName" name="model.folderName" type="text" size="24" value="${model.folderName}" readonly="readonly">
										<br>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">全宗号：</span>
										<%--						<input id="folderOrgId" name="model.folderOrgId" type="hidden" value="${model.folderOrgId}">--%>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderOrgName" name="model.folderOrgName" type="text" size="24" readonly="readonly" value="${model.folderOrgName}">
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">保管期限：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderLimitName" name="model.folderLimitName" type="text" value="${model.folderLimitName}" size="24" readonly="readonly">
										<br>
									</td>
								</tr>
								
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">开始年度自：</span>
									</td>
									<td class="td1" align="left">
										<input id="folderFromDate" name="model.folderFromDate" type="text" size="24" value="${model.folderFromDate}" readonly="readonly">
									</td>
								
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">结束年度至：</span>
									</td>
									<td class="td1" align="left">
										<input id="folderToDate" name="model.folderToDate" type="text" size="24" value="${model.folderToDate}" readonly="readonly">
									</td>
								</tr>
								
								
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">日期：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="title" name="name" type="text" value=${model.folderDate } size="24" readonly="readonly">
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">归档号：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderArchiveNo" name="model.folderArchiveNo" type="text" size="24" readonly="readonly" value="${model.folderArchiveNo}">
										<br>
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">年度：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<strong:newdate id="folderDate" name="model.folderDate" cantinput="false" disabled="true" dateform="yyyy" width="174" dateobj="${model.folderDate}" classtyle="required"   /> 
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">件(册)数：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderFileNum" name="model.folderPage"
											type="text" size="24" readonly="readonly"
											value="${model.folderPage}">
										<br>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">机构(处室)：</span>
										<input id="folderDepartment" name="model.folderDepartment" type="hidden" value="${model.folderDepartment}">
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderDepartmentName" type="text" size="24" readonly="readonly" value="${folderDepartmentName}">
										<%--						<input name="gorscelet" type="button" class="input_bg" value="选 择" onclick="departscelet()">--%>
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">责任者：</span>
										<input id="folderCreaterId" name="model.folderCreaterId" type="hidden" value="${model.folderCreaterId}" >
										<br>
									</td>
									<td class="td1" align="left">
										<input id="folderCreaterName" name="model.folderCreaterName" type="text" size="24" readonly="readonly" value="${model.folderCreaterName}">
										<br>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">卷(盒)状态：</span>
									</td>
									<td class="td1" align="left">
										<s:select name="model.folderAuditing" list="#{'0':'未归档','1':'已归档','2':'待审核','3':'驳回','4':'销毁审核中','5':'销毁驳回'}" listKey="key" listValue="value" disabled="true" />

									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz"> 归档审核者：</span>
									</td>
									<td class="td1" align="left">
										<input id="folderAuditingName" name="model.folderAuditingName" type="text" value="${model.folderAuditingName}" size="24" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1"  align="left">
										<textarea rows="4" cols="20" id="folderAuditingContent"
											name="model.folderAuditingContent">${model.folderAuditingContent}</textarea>
									</td>
								
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">卷(盒)描述：</span>
									</td>
									<td class="td1"  align="left">
										<textarea rows="4" cols="20" id="folderDesc"
											name="model.folderDesc">${model.folderDesc}</textarea>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" showSearch="false" footShow="showCheck" getValueType="getValueByProperty" collection="${fileList}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" onclick="search();" style="cursor: hand;">
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<input id="filePieceNo" name="filemodel.filePieceNo" type="text" style="width:100%" value="${filemodel.filePieceNo}"  class="search" title="件号">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<input name="filemodel.fileNo" type="text" style="width:100%" value="${filemodel.fileNo}"  class="search" title="文号">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<input name="filemodel.fileTitle" type="text" style="width:100%" value="${filemodel.fileTitle}"  class="search" title="题名">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="fileDate" name="filemodel.fileDate" dateform="yyyy-MM-dd" width="100%" dateobj="${filemodel.fileDate}" isicon="true"  classtyle="search" title="文件日期"/>
										</td>
										<td width="15%" align="center" class="biao_bg1" >
											<input name="filemodel.fileAuthor" type="text" style="width:100%" value="${filemodel.fileAuthor}"  class="search" title="责任者">
										</td>
										<td width="10%" align="center" class="biao_bg1">
											<input id="filePage" name="filemodel.filePage" type="text" style="width:100%" value="${filemodel.filePage}"  class="search" title="页数">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input name="filemodel.fileDesc" type="text" style="width:100%" value="${filemodel.fileDesc}" class="search" title="备注">
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
						
								<webflex:flexCheckBoxCol caption="选择" property="fileId" showValue="fileTitle" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexNumricCol caption="件号" property="filePieceNo" showValue="javascript:showfilePage(filePieceNo)" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
								<webflex:flexTextCol caption="文号" property="fileNo" showValue="fileNo" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="题名" property="fileId" showValue="fileTitle" width="15%" isCanDrag="true" isCanSort="true" onclick="viewFileInfo(this.value);"></webflex:flexTextCol>
								<webflex:flexDateCol caption="日期" property="fileDate" showValue="fileDate" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexNumricCol caption="责任者" property="fileAuthor" showValue="javascript:showAuName(fileAuthor)" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
<%--								<webflex:flexTextCol caption="正文" property="fileId" showValue="javascript:showimg(appendsize)" width="5%" isCanDrag="true" isCanSort="true" ></webflex:flexTextCol>--%>
								<webflex:flexTextCol caption="页数"   property="filePage" showValue="filePage" width="10%" isCanDrag="true" isCanSort="true" ></webflex:flexTextCol>
								<webflex:flexTextCol caption="备注" property="fileDesc" showValue="fileDesc" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<div align="right"><script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	//item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","权限","",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回","cancel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function viewFileInfo(id){ //查看信息
	location="<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileIds="+id;
}

function cancel(){	 //返回
	var folderId=document.getElementById("folderId").value;
	var archiveSortId=document.getElementById("archiveSortId").value;
	var moduletype=document.getElementById("moduletype").value;
	location="<%=path%>/archive/archivefolder/archiveFolder.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId;
}

function submitForm(){//表单提交
	document.getElementById("myTableForm").submit();
}

function search(){	 //查询
	if(isNaN(document.getElementById("filePage").value)){
		alert("文件页码必须为数字！");
		return;
	}
	
	if(isNaN(document.getElementById("filePieceNo").value))
	{
		alert("件号必须为数字");
		return false;
	}
	submitForm();
}

function viewAnnex(value){
	location="<%=path%>/archive/archivefile/achiveFile!download.action?fileId="+value;
}
</script> 
	</div></BODY>
</HTML>
