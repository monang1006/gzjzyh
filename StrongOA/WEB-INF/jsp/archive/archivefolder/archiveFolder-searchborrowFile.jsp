<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link type=text/css rel=stylesheet href="<%=frameroot%>/css/strongitmenu.css">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		function showauthor(value){
			     if(value==null||value=="null")
			     {
			        return "";
			     }else{
                    return value;			     
			     }
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm" action="/archive/archivefolder/archiveFolder!input.action">
				<input type="hidden" name="forward" value="searchborrowFile" />
				<input id="moduletype" name="moduletype" type="hidden" size="30" value="${moduletype}">
				<input id="archiveSortId" name="archiveSortId" type="hidden" size="30" value="${archiveSortId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>档案案卷查看</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 <s:if test="model.folderAuditing==\"1\"">
																<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>	
										                 	<td class="Operation_list" onclick="fileBorrow();"><img src="<%=root%>/images/operationbtn/jieyue.png"/>&nbsp;借&nbsp;阅&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </s:if>
				                                  
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="cancel();"><img src="<%=root%>/images/operationbtn/back.png"/>&nbsp;返&nbsp;回&nbsp;</td>
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
							</table>
				
										<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
											<input id="folderId" name="folderId" type="hidden" size="30" value="${model.folderId}">
											<tr>
												<td  height="21" class="biao_bg1" align="right">
													<span class="wz">案卷编号：</span>
													<br>
												</td>
												<td class="td1" align="left">
												  <span class="wz">${model.folderNo}</span>
													
												</td>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">案卷题名：</span>
													<br>
												</td>
												<td class="td1" align="left">
												  <span class="wz">${model.folderName}</span>
													
												</td>
											</tr>
											<tr>
												<td  height="21" class="biao_bg1" align="right">
													<span class="wz">全宗名称：</span>
													<br>
												</td>
												<td class="td1" align="left">
												   <span class="wz">${model.folderOrgName}</span>
													
												</td>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">保管期限：</span>
													<br>
												</td>
												<td class="td1" align="left">
												   <span class="wz">${model.folderLimitName}</span>
													
												</td>
											</tr>
											
											<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">年度开始自：</span>
									</td>
									<td class="td1" align="left">
									     
										<s:date name="model.folderFromDate" format="yyyy年MM月dd日 HH点mm分"/>
									</td>
								
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">年度结束到：</span>
									</td>
									<td class="td1" align="left">
										<s:date name="model.folderToDate" format="yyyy年MM月dd日 HH点mm分"/>
									   
										
									</td>
								</tr>
									
											<tr>
												<td  height="21" class="biao_bg1" align="right">
													<span class="wz">创建时间：</span>
													<br>
												</td>

												<td class="td1" align="left">
												<s:date name="model.folderDate" format="yyyy年MM月dd日 HH点mm分"/>
												   
													
												</td>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">归档号：</span>
													<br>
												</td>
												<td class="td1" align="left">
												     <span class="wz">${model.folderArchiveNo}</span>
													
												</td>
											</tr>
											<tr>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">所属处室：</span>
													<input id="folderDepartment" name="model.folderDepartment" type="hidden" value="${model.folderDepartment}">
													<br>
												</td>
												<td class="td1" align="left">
												  <span class="wz">${folderDepartmentName}</span>
													
												</td>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">案卷创建者：</span>
													<input id="folderCreaterId" name="model.folderCreaterId" type="hidden" value="${model.folderCreaterId}" size="30">
													<br>
												</td>
												<td class="td1" align="left">
												  <span class="wz">${model.folderCreaterName}</span>
												
												</td>
											</tr>
											<tr>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">案卷状态：</span>
												</td>
												<td class="td1" align="left">
												   <span class="wz">${model.folderAuditing}</span>
										
												</td>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">审核者：</span>
												</td>
												<td class="td1" align="left">
												   <span class="wz">${model.folderAuditingName}</span>
													
												</td>
											</tr>
											<tr>
												<td height="21" class="biao_bg1" align="right">
													<span class="wz">案卷描述：</span>
												</td>
												<td class="td1" align="left" style="word-break:break-all;line-height: 1.4">
												   <span class="wz">${model.folderDesc}</span>
												</td>
											</tr>
										</table>
										<webflex:flexTable name="myTable" width="100%" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" showSearch="false" footShow="showCheck" getValueType="getValueByProperty" collection="${fileList}">
										    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;文件编号：&nbsp;<input name="filemodel.fileNo" type="text"  value="${filemodel.fileNo}" class="search" title="请输入文件编号">
							       		&nbsp;&nbsp;文件名称：&nbsp;<input name="filemodel.fileTitle" type="text"  value="${filemodel.fileTitle}" class="search" title="请输入文件名称">
							       		&nbsp;&nbsp;文件创建日期：&nbsp;<strong:newdate id="fileDate" name="filemodel.fileDate" dateform="yyyy-MM-dd"  dateobj="${filemodel.fileDate}" isicon="true" classtyle="search" title="请输入文件创建日期"/><br/>
							       		&nbsp;&nbsp;作者名：&nbsp;<input name="filemodel.fileAuthor" type="text"  value="${filemodel.fileAuthor}" class="search" title="作者名">
							       		&nbsp;&nbsp;文件页码：&nbsp;<input id="filePage" name="filemodel.filePage" type="text"  value="${filemodel.filePage}" class="search" title="请输入文件页码">
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
											<webflex:flexCheckBoxCol caption="选择" property="fileId" showValue="fileTitle" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
											<webflex:flexTextCol caption="文件号" property="fileNo" showValue="fileNo" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="文件名称" property="fileId" showValue="fileTitle" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexDateCol caption="文件创建日期" property="fileDate" showValue="fileDate" width="20%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
											<webflex:flexNumricCol caption="作者名" property="fileAuthor" showValue="javascript:showauthor(fileAuthor)" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
											<webflex:flexTextCol caption="文件页码" property="filePage" showValue="filePage" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										</webflex:flexTable>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<s:if test="model.folderAuditing==\"1\"">
	item = new MenuItem("<%=root%>/images/operationbtn/jieyue.png","借阅","fileBorrow",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</s:if>
	item = new MenuItem("<%=root%>/images/operationbtn/Cancel.png","返回","cancel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function fileBorrow(){
	var folderId=document.getElementById("folderId").value;
	var fileId = getValue();
	
	if(fileId==""){
		alert("请选择文件进行借阅！");
	}else if(fileId.length>32){
	   alert("每次只可以借阅一份文件！");
	}else{
		location="<%=path%>/archive/archiveborrow/archiveBorrow!input.action?folderId="+folderId+"&fileId="+fileId;
     }
}
function viewFileInfo(id){
	location="<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileIds="+id;
}
function search(){
	if(isNaN(document.getElementById("filePage").value)){
		alert("文件页码必须为数字！");
		return;
	}
	myTableForm.submit();
}
function cancel(){
	
<%--	history.go(-1);--%>
	var archiveSortId=document.getElementById("archiveSortId").value;	//类目id
	var moduletype=document.getElementById("moduletype").value;			//模块标识
	location="<%=path%>/archive/archivefolder/archiveFolder.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype;
}
</script>
	</BODY>
</HTML>
