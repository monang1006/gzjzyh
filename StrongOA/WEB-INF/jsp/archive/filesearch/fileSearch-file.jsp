<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' type="text/javascript"
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" type="text/javascript"
			src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=='0')
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;'>";
			}
	function showFolderName(obj,value){
		if(obj==null||obj=="null"||obj==""||obj=="[]")
			return "";
		else{
			if(value==null||value=="null"||value==""){
				return "";
			}else{
				return value;
			}
		}
	}
	
</script>
	</HEAD>
	<BODY class=contentbodymargin 
		onload="initMenuT()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm"
							action="/archive/filesearch/fileSearch!filelist.action">
							<input id="tempfileDeadline" name="tempfileDeadline" type="hidden" value="${tempfileDeadline}">
							<input type="hidden" id="month" name="month" value="${month }">
							<input type="hidden" id="year" name="year" value="${year}">
							<input type="hidden" id="fileNo" name="fileNo" value="${fileNo }">
							<input type="hidden" id="fileTitle" name="fileTitle" value="${fileTitle }">
							<input type="hidden" id="fileAuthor" name="fileAuthor" value="${fileAuthor }">
							<input type="hidden" id="fileDate" name="fileDate" value="${fileDate }">
							<input type="hidden" id="fileFolder" name="fileFolder" value="${fileFolder }">
							<input type="hidden" id="filepage" name="filepage" value="${filepage }">
							<input type="hidden" id="fileType" name="fileType" value="${fileType }">
							<input type="hidden" id="orgId" name="orgId" value="${orgId }">
							<input type="hidden" id="disLogo" name="disLogo" value="${disLogo }">
							<input type="hidden" id="groupType" name="groupType" value="${groupType }">
							<input type="hidden" id="treeValue" name="treeValue" value="${treeValue }">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>已归档文件列表</strong>
												</td>			
												<td align="right">
													<table  border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="editTempFile();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="goto();"><img src="<%=root%>/images/operationbtn/Return.png"/>&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="excelport();"><img src="<%=root%>/images/operationbtn/derive.png"/>&nbsp;导&nbsp;出&nbsp;</td>
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
								wholeCss="table1" property="fileId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<webflex:flexCheckBoxCol caption="选择" property="fileId"
									showValue="fileTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="件号" property="filePieceNo" showsize="10"
									showValue="filePieceNo" width="7%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="责任者" property="fileAuthor" showsize="18"
									showValue="fileAuthor" width="9%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="文号" property="fileNo" showsize="10"
									showValue="fileNo" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="题名" property="fileId"
									showValue="fileTitle" width="28%" isCanDrag="true" showsize="28"
									isCanSort="true" onclick="viewFileInfo(this.value);"></webflex:flexTextCol>
								<webflex:flexDateCol caption="时间" property="tempfileDate"
									showValue="fileDate" width="13%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
							    <webflex:flexTextCol caption="保管期限" property="fileDeadlineId" showValue="fileDeadline"  width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="卷（盒）题名"
									property="toaArchiveFolder.folderId" showsize="18"
									showValue="javascript:showFolderName(toaArchiveFolder,toaArchiveFolder.folderName)"
									width="15%" isCanDrag="true" isCanSort="true" showsize="8"></webflex:flexTextCol>
								<webflex:flexTextCol caption="页数" property="filePage" showValue="filePage"
									width="5%" isCanDrag="true" isCanSort="true" showsize="10"></webflex:flexTextCol>
<%--								<%--<webflex:flexTextCol caption="备注" property="fileDesc"
<%--									showValue="fileDesc" showsize="11"
<%--									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="附件" property="tempfileId"
									showValue="javascript:showimg(appendsize)"
									width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>--%>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<iframe id="annexFrame" style="display:none"></iframe>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/view.png"," 查看","editTempFile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function editTempFile(){	//编辑文件
	var id=getValue();
	if(id=="")
		alert("请选择要查看的文件！");
	else if(id.indexOf(",")!=-1)
		alert("请选择一个文件！");
	else{
<%--	    $.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",--%>
<%--           {"tempfileId":id},--%>
<%--           function(data){--%>
<%--             if(data=="1"){--%>
<%--		      alert('你无权访问！');--%>
<%--		      return;--%>
<%--		    }--%>
			   getSysConsole().refreshWorkByTitle("<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileIds="+id+"&searchType=file","查看文件");
<%--		--%>
<%--		  });--%>
	  }
	  
}
function getListBySta(){	//根据是否入卷查询
	document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}
function excelport(){
    var disLogo=document.getElementById("disLogo").value;
    var groupType=document.getElementById("groupType").value;
     var fileNo=$("#fileNo").val();
     var fileTitle=$("#fileTitle").val();
     var  tempfileDeadline=$("#tempfileDeadline").val();
     var  year=$("#year").val();
     var  month=$("#month").val();
     var fileFolder=document.getElementById("fileFolder").value;
     var  orgId=$("#orgId").val();
     if(!fileNo){
      fileNo="";
     }
     if(!fileTitle){
      fileTitle="";
     }
     if(!fileFolder){
     fileFolder="";
     }
     if(!tempfileDeadline){
      tempfileDeadline="";
     }
     if(!year){
      year="";
     }
     if(!month){
      month="";
     }
     if(!orgId){
      orgId="";
     }
    // alert(tempfileDeadline);
    var exportExcelUrl = '<%=path%>/archive/filesearch/fileSearch!exportExcel.action?disLogo='+disLogo+'&groupType='+groupType+'&fileNo='+encodeURI(encodeURI(fileNo))+'&fileTitle='+encodeURI(encodeURI(fileTitle))+'&tempfileDeadline='+encodeURI(encodeURI(tempfileDeadline))+'&year='+year+'&month='+month+'&orgId='+orgId+'&fileFolder='+fileFolder;
    $('#annexFrame').attr('src',exportExcelUrl);
}
function viewFileInfo(value){//查看文件
<%-- $.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",--%>
<%--           {"tempfileId":value},--%>
<%--           function(data){--%>
<%--         --%>
<%--	    if(data=="1"){--%>
<%--	      alert('你无权访问！');--%>
<%--	      return;--%>
	$.post('<%=path%>/viewmodel/modelPrival!isNotDoor.action',
				 function(data){			
			            if(data=="sucess"){
			              	  //getSysConsole().refreshWorkByTitle("<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileNo=${fileNo}&disLogo1=${disLogo}&groupType=${groupType}&fileTitle=${fileTitle}&fileFolder=${fileFolder}&tempfileDeadline=${tempfileDeadline}&orgId1=${orgId}&year1=${year}&month1=${month}&fileIds="+value+"&searchType=file","查看文件");
			              	  location="<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileNo=${fileNo}&disLogo1=${disLogo}&groupType=${groupType}&fileTitle=${fileTitle}&fileFolder=${fileFolder}&tempfileDeadline=${tempfileDeadline}&orgId1=${orgId}&year1=${year}&month1=${month}&fileIds="+value+"&searchType=file";
			              	}else {
							  //getSysConsole().refreshWorkByTitle("<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileNo=${fileNo}&disLogo1=${disLogo}&groupType=${groupType}&fileTitle=${fileTitle}&fileFolder=${fileFolder}&tempfileDeadline=${tempfileDeadline}&orgId1=${orgId}&year1=${year}&month1=${month}&fileIds="+value+"&searchType=file","查看文件");
							  location="<%=path%>/archive/archivefolder/archiveFolder!viewFile.action?fileNo=${fileNo}&disLogo1=${disLogo}&groupType=${groupType}&fileTitle=${fileTitle}&fileFolder=${fileFolder}&tempfileDeadline=${tempfileDeadline}&orgId1=${orgId}&year1=${year}&month1=${month}&fileIds="+value+"&searchType=file";
							 }
						});
	 
	
<%--		}--%>
<%--		  });--%>
}

function viewAnnex(value){	//查看附件
$.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",
           {"tempfileId":value},
           function(data){
         
	    if(data=="1"){
	      alert('你无权访问！');
	      return;
	    }else{
		var frame=document.getElementById("annexFrame");
	frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tempfileId="+value;}
		  });
}
function goto(){
window.location="<<%=path%>/fileNameRedirectAction.action?toPage=archive/filesearch/fileSearch-search.jsp";
}
</script>
	</BODY>
</HTML>
