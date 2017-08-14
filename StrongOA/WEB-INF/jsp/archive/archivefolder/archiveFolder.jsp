<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
function search(){
	//cument.getElementById("pageNo").value="1";
	//var folderDate_str=$("#folderDate_str").val();
	//if(folderDate_str!=null&&folderDate_str!=""){
		//$("#folderDate_str").val(folderDate_str+"-01-01");//年度
	//}
	
	myTableForm.submit();
}
function folderEdit(){
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请先选择一条卷(盒)信息!");
		return;
	}else if(folderId.indexOf(",")!=-1){
		alert("只能选择一条卷(盒)信息！");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==folderId){
			var folderAuditing = chkButtons[i].parentElement.parentElement.cells[7].value;
			if(folderAuditing=="1"){
				alert("该卷(盒)已归档，不能编辑！");
				return;
			}else if(folderAuditing=="2"){
				alert("该卷(盒)已提交归档，不能编辑！");
				return;
			}
			break;
		}
	}
	location="<%=path%>/archive/archivefolder/archiveFolder!input.action?moduletype="+moduletype+"&folderId="+folderId+"&archiveSortId="+archiveSortId;
}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form action="/archive/archivefolder/archiveFolder.action" id="myTableForm" theme="simple">
							<input type="hidden" name="archiveSortId" value="${archiveSortId}">
							<input type="hidden" name="moduletype" value="${moduletype}">
							<input type="hidden" name="archiveIsEnable" value="${archiveIsEnable}">
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
													<strong>档案卷(盒)列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														<s:if test="moduletype==\"pige\"">
														  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderMove();"><img src="<%=root%>/images/operationbtn/Move_file.png"/>&nbsp;移&nbsp;动&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderPigeonhole();"><img src="<%=root%>/images/operationbtn/File.png"/>&nbsp;归&nbsp;档&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </s:if>
				                                  <s:elseif test="moduletype==\"manage\"">
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="CreatePieceNo();"><img src="<%=root%>/images/operationbtn/Formation_No.png"/>&nbsp;生成件号&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderMove();"><img src="<%=root%>/images/operationbtn/Move_file.png"/>&nbsp;移&nbsp;动&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <s:if test="archiveIsEnable==0">
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="folderAuditing();"><img src="<%=root%>/images/operationbtn/audit.png"/>&nbsp;审&nbsp;核&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </s:if>
				                                   <td width="2%"></td>
				                                   </s:elseif>
														</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
			
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="folderId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;卷(盒)号：&nbsp;<input name="model.folderNo" type="text"  class="search" value="${model.folderNo}" title="卷(盒)号" id="folderNo">
							       		&nbsp;&nbsp;卷(盒)题名：&nbsp;<input name="model.folderName" type="text" class="search" value="${model.folderName}" title="卷(盒)题名" id="folderName">
							       		&nbsp;&nbsp;年度：&nbsp;<strong:newdate id="folderDate_str" name="folderDateY"
												dateform="yyyy" isicon="true" 
												dateobj="${model.folderDate}" classtyle="search"
												title="年度" /></br>
							       		&nbsp;&nbsp;机构：&nbsp;<input name="model.folderDepartmentName" type="text" class="search" value="${model.folderDepartmentName}" title="机构" id="folderDepartmentName">
							       		&nbsp;&nbsp;保管期限：&nbsp;<s:select list="limitdictList" listKey="dictItemCode" listValue="dictItemName" headerKey="" headerValue="保管期限" id="folderLimitId" name="model.folderLimitId"  onchange="search()" />
							       		&nbsp;&nbsp;卷(盒)状态：&nbsp;<s:select name="model.folderAuditing" list="#{'':'请选择','0':'未归档','1':'已归档'}" listKey="key" listValue="value"  onchange="search()" id="folderAuditing"/>
							       		
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
								
								<webflex:flexCheckBoxCol caption="选择" property="folderId" showValue="folderName" width="3%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="卷(盒)号" property="folderArrayNo" showValue="folderNo" width="15%" isCanDrag="true" isCanSort="true" showsize="6"></webflex:flexTextCol>
								<webflex:flexTextCol caption="题名" property="folderId" showValue="folderName" width="37%" isCanDrag="true" isCanSort="true" onclick="viewFile(this.value);" showsize="8"></webflex:flexTextCol>
								<webflex:flexDateCol caption="年度" property="folderDate" showValue="folderDate" width="6%" isCanDrag="true" isCanSort="true" dateFormat="yyyy"></webflex:flexDateCol>
								<webflex:flexTextCol caption="机构" property="folderDepartment" showValue="folderDepartmentName"  width="16%" isCanDrag="true" isCanSort="true" showsize="6" ></webflex:flexTextCol>
								<webflex:flexTextCol caption="保管期限" property="folderLimitId" showValue="folderLimitName"  width="11%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="卷(盒)状态" mapobj="${statemap}" property="folderAuditing" showValue="folderAuditing" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<IFRAME style="display: none;" src="" id="helpframe"></IFRAME>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
var archiveSortId = document.getElementById("archiveSortId").value;
var archiveIsEnable="${archiveIsEnable}";
var moduletype = document.getElementById("moduletype").value;
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	if(moduletype=="pige"){
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","增加","folderAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","folderEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","folderDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Move_file.png","移动","folderMove",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/File.png","归档","folderPigeonhole",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
<%--	<security:authorize ifAllGranted="001-0003000300020006">--%>
<%--	item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","权限","",1,"ChangeWidthTable","checkOneDis");--%>
<%--	sMenu.addItem(item);--%>
<%--	</security:authorize>--%>
	}
	if(moduletype=="manage"){
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","增加","folderAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","folderEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","folderDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Move_file.png","移动","folderMove",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Formation_No.png","生成件号","CreatePieceNo",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	if(archiveIsEnable==0){
		item = new MenuItem("<%=root%>/images/operationbtn/audit.png","审核","folderAuditing",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);	
	}
	
<%--	<security:authorize ifAllGranted="001-0003000300030006">--%>
<%--	item = new MenuItem("<%=root%>/images/ico/xiaohui.gif"," 销毁","folderDestroy",1,"ChangeWidthTable","checkOneDis");--%>
<%--	sMenu.addItem(item);--%>
<%--	</security:authorize>--%>
<%--	<security:authorize ifAllGranted="001-0003000300030007">--%>
<%--	item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","权限","",1,"ChangeWidthTable","checkOneDis");--%>
<%--	sMenu.addItem(item);--%>
<%--	</security:authorize>--%>
	}
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function folderAdd(){
	if(archiveSortId=="null"||archiveSortId==""){
		alert("请先选择类目!");
		return;
	}	
	location="<%=path%>/archive/archivefolder/archiveFolder!input.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype;
}
//生成件号
function CreatePieceNo(){
	  if(archiveSortId=="null"||archiveSortId==""){
			alert("请先选择类目!");
			return;
		}	
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请至少选择一条卷(盒)信息!");
		return;
	}
	            $.ajax({
						type:"post",
						url:"<%=path%>/archive/archivefolder/archiveFolder!getfolderAuditing.action",
						data:{
							folderId:folderId				
						},
						success:function(data){	
							if(data!=null&&data=="0"){
								alert("选择的卷(盒)有已归档的，请重新选择未归档的卷(盒)!");
							}
							else{
							 url = "<%=path%>/archive/archivefolder/archiveFolder!getArchiveFile.action?folderId="+folderId;
							 window.showModalDialog(url,window,"help:no;status:no;scroll:no;dialogWidth:950px; dialogHeight:500px");
							  //window.showModalDialog("<%=path%>>/archive/archivefolder/archiveFolder!getArchiveFile.action?folderId="+folderId",window,"help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:500px");
							  //+"&archiveSortId="+archiveSortId+"&moduletype="+moduletype
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
}



function folderDel(){
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请至少选择一条卷(盒)信息!");
		return;
	}
	if(folderId.length>32){
		alert("不可以同时删除多条案卷!");
		return;
	}
	if(confirm("您确定要删除吗？")){
		location="<%=path%>/archive/archivefolder/archiveFolder!delete.action?moduletype="+moduletype+"&folderId="+folderId+"&archiveSortId="+archiveSortId;
	}
}
function viewFile(value){
	var archiveSortId=document.getElementById("archiveSortId").value;
	var moduletype = document.getElementById("moduletype").value;
	if(moduletype=="searchborrowFile"){
		location="<%=path%>/archive/archivefolder/archiveFolder!input.action?moduletype="+moduletype+"&archiveSortId="+archiveSortId+"&forward=searchborrowFile&folderId="+value;
	}
	else{
		location="<%=path%>/archive/archivefolder/archiveFolder!input.action?forward=viewFile&folderId="+value+"&archiveSortId="+archiveSortId+"&moduletype="+moduletype;
	}
}
//案卷归档
function folderPigeonhole(){
	//window.showModalDialog("fileList.jsp",window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:570px');
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请至少选择一条卷(盒)信息!");
		return;
	}
	else{
<%--		location="<%=path%>/archive/archivefolder/archiveFolder!appigeonhole.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId;--%>
	//判断案卷中 的文件是否生成件号，生成件号后才归档
	            $.ajax({
						type:"post",
						url:"<%=path%>/archive/archivefolder/archiveFolder!archiveIsCreatePieceNo.action",
						data:{
							folderId:folderId				
						},
						success:function(data){	
							if(data!=null&&data=="0"){
								alert("在操作归档“卷(盒)”之前，请在<<案卷管理>>中生成件号!!!!");
							}
							else{  
							if(confirm("是否确定将此案卷盒归档?")){
										//判断是否申请归档或直接归档
										$.post("<%=path%>/archive/archivefolder/archiveFolder!archiveIsEnable.action",
									        function(data){
											    if(data=="0"){
											    	show("案卷进行申请归档处理,请耐心等待...");
											      	location="<%=path%>/archive/archivefolder/archiveFolder!appigeonhole.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId;
											      	setTimeout("hidden()", 500);
											    }else if(data=="1"){
											    	show("案卷进行归档处理,请耐心等待...");
													location="<%=path%>/archive/archivefolder/archiveFolder!auditArchive.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId;
													setTimeout("hidden()", 2000);
											   }else{
											   		alert("在操作归档“卷(盒)”之前，请在“系统管理设置”中->>“系统全局配置”中->>设置“卷(盒)是否直接归档“初始化值！！！");
											   }
											   
										       });
										       }
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
					});
	}
}
function folderAuditing(){
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请先选择一条卷(盒)信息!");
		return;
	}else if(folderId.indexOf(",")!=-1){
		alert("只能选择一条卷(盒)信息!");
		return;
	}
	var flag = true;
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==folderId){
			var folderAuditing = chkButtons[i].parentElement.parentElement.cells[7].value;
			//alert("卷(盒)状态："+folderAuditing);
				//alert("folderId:"+folderId+"==folderAuditing:"+folderAuditing);
			if(folderAuditing=="1"){
				alert("该卷(盒)已归档，无需再归档！");
				flag = false;
			}else if(folderAuditing=="3"){
				alert("该卷(盒)归档申请未通过，请重新提交归档申请！");
				flag = false;
			}else if(folderAuditing=="0"){
				alert("该卷(盒)未提交归档申请，请先提交归档申请！");
				flag = false;
			}else if(folderAuditing=="4"){
			    alert("该卷(盒)销毁审核中，不可以再归档！");
				flag = false;
			}else if(folderAuditing=="5"){
			    alert("该卷(盒)销毁驳回，不可以归档！");
			    flag=false;
			}
			break;
		}
	}
	if(flag){
		window.showModalDialog("<%=path%>/archive/archivefolder/archiveFolder!initaudit.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:450px');	
	}
}
function folderMove(){
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请要移动的卷(盒)信息!");
		return;
	}else if(folderId.length>32){
	alert("不可以同时移动多条信息！");
	return;
	}
	window.showModalDialog("<%=path%>/archive/archivefolder/archiveFolder!initremove.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&folderId="+folderId,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:360px');
}
function folderExport(){
    //$('#folderLimitId1').val($('#folderLimitId').val()) ;
     var folderLimitId =  $('#folderLimitId').val();
     if(!folderLimitId){
     folderLimitId="";
     }
     var folderDepartmentName = $('folderDepartmentName').val();
     if(!folderDepartmentName){
      folderDepartmentName="";
     }
     var folderNo=$('#folderNo').val();
     if(!folderNo){
      folderNo="";
     }
     var folderName=$('#folderName').val();
     if(!folderName){
      folderName="";
     }
     var folderDate=$('#folderDate').val();
     if(!folderDate){
      folderDate="";
     }
     var folderAuditing=$('#folderAuditing').val();
     if(!folderAuditing){
      folderAuditing="";
     }
     if(archiveSortId=="null"||archiveSortId==""){
		alert("请先选择类目!");
		return;
	}	
	var exportExcelUrl = '<%=path%>/archive/archivefolder/archiveFolder!importExcel.action?archiveSortId='+archiveSortId+'&moduletype='+moduletype+'&folderLimitId='+folderLimitId+'&folderDepartmentName='+folderDepartmentName+'&folderNo='+folderNo+'&folderName='+folderName+'&folderDate='+folderDate+'&folderAuditing='+folderAuditing;
	//+'&folderLimitId='+folderLimitId+'&folderDepartmentName='+folderDepartmentName+'&folderNo='+folderNo+'&folderName='+folderName+'&folderDate='+folderDate+'&folderAuditing='+folderAuditing
	$('#helpframe').attr('src',exportExcelUrl);
	
	//window.showModalDialog("<%=path%>/archive/archivefolder/archiveFolder!importExcel.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype,window,'help:no;status:no;scroll:no;dialogWidth:300px; dialogHeight:360px');
}
function folderDestroy(){
	var folderId = getValue();
	if(folderId=="null"||folderId==""){
		alert("请先选择要申请销毁的卷(盒)!");
		return;
	}else if(folderId.indexOf(",")!=-1){
		alert("只能同时申请一份卷(盒)销毁!");
		return;
	}
	
	var chkButtons = document.getElementsByName("chkButton");
	var flag = true;
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==folderId){
			var folderAuditing = chkButtons[i].parentElement.parentElement.cells[7].value;
			if(folderAuditing==null||folderAuditing=="null"||folderAuditing=="0"){
				alert("该卷(盒)未归档，只有归档的卷(盒)才可以申请销毁！");
				flag = false;
			}else if(folderAuditing=="2"){
				alert("该案在归档审核中，只有归档的卷(盒)才可以申请销毁！");
				flag = false;
			}else if(folderAuditing=="3"){
				alert("该卷(盒)归档申请已被驳回，不可以销毁！");
				flag = false;
			}else if(folderAuditing=="4"){
				alert("该卷(盒)已经提交了销毁申请，请耐心等待！");
				flag = false;
			}else if(folderAuditing=="5"){
			    alert("该卷(盒)销毁申请已被驳回，不可以销毁！");
			    flag = false;
			}
			
			break;
		}
	}
	if(flag)
		window.showModalDialog("<%=path%>/archive/archiveDestr/archiveDestr!input.action?archiveSortId="+archiveSortId+"&folderId="+folderId,window,'help:no;status:no;scroll:no;dialogWidth:700px; dialogHeight:500px');
}

function submitForm(){
	myTableForm.submit();
}
</script>
	</BODY>
</HTML>
