<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link type=text/css rel=stylesheet
			href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<!--右键菜单样式 -->
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js"></script>
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<!--右键菜单脚本 -->
		<SCRIPT type="text/javascript">
			function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=='0')
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;'>";
			}
			function showtempflePage(value){
			    if(value==null||value=="null"){
			      return "";
			    }else{
			      return value;
			    }
			}
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"  onload="initMenuT()" >
		<script src="<%=path%>/common/js/newdate/WdatePicker.js"
			type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/archive/archivefolder/archiveFolder!input.action">
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
													<strong>卷（盒）查看</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 <s:if
																test="model.folderAuditing==null || model.folderAuditing==\"0\"">
																<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>	
										                 	<td class="Operation_list" onclick="fileAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="fileEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="fileDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </s:if>
				                                   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="cancel();"><img src="<%=root%>/images/operationbtn/Cancel.png"/>&nbsp;返&nbsp;回&nbsp;</td>
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
								<input id="moduletype" name="moduletype" type="hidden" size="30"
									value="${moduletype}">
								<input id="archiveSortId" name="archiveSortId" type="hidden"
									size="30" value="${archiveSortId}">
								<input id="folderId" name="folderId" type="hidden" size="30"
									value="${model.folderId}">
								<input id="forward" name="forward" type="hidden" size="30"
									value="viewFile">
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">所属类目：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${archiveSortName}</span>
										
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">书名：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderFormName}</span>
										<br>
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">卷（盒）号：</span>
										<br>
									</td>
									<td class="td1" align="left">
											<span class="wz">${model.folderNo}</span>
										<br>
									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">题名：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderName}</span>
										<br>
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">全宗号：</span>
										<%--						<input id="folderOrgId" name="model.folderOrgId" type="hidden" value="${model.folderOrgId}">--%>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderOrgName}</span>
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">保管期限：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderLimitName}</span>
										<br>
									</td>
								</tr>

								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">开始年度自：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderFromDate}</span>
									</td>

									<td height="21" class="biao_bg1" align="right">
										<span class="wz">结束年度至：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderToDate}</span>
									</td>
								</tr>

								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">日期：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<s:date name="model.folderDate" format="yyyy年MM月dd日 HH点mm分"/>
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">归档号：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderArchiveNo}</span>
										<br>
									</td>
								</tr>
								
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">年度：</span>
										<br>
									</td>
									<td class="td1" align="left">
									<s:date name="model.folderDate" format="yyyy"/>
										
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">件（册）数：</span>
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderPage}</span>
										<br>
									</td>
								</tr>
								
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">机构(处室)：</span>
										<input id="folderDepartment" name="model.folderDepartment"
											type="hidden" value="${model.folderDepartment}"
											readonly="readonly">
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${folderDepartmentName}</span>
										<%--						<input name="gorscelet" type="button" class="input_bg" value="选 择" onclick="departscelet()">--%>
										<br>
									</td>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">责任者：</span>
										<input id="folderCreaterId" name="model.folderCreaterId"
											type="hidden" value="${model.folderCreaterId}" size="26">
										<br>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderCreaterName}</span>
										<%--						<input name="userscelet" type="button" class="input_bg" value="选 择" onclick="userselect()">--%>
										<br>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">是否归档：</span>
									</td>
									<td class="td1" align="left">
										
											<div class="wz" id="status"> </div >
										<s:hidden id="folderAuditing" name="model.folderAuditing"></s:hidden>

									</td>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">审核者：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.folderAuditingName}</span>
									</td>
								</tr>
								<tr>
									<s:if test="model.folderAuditing==\"3\"||model.folderAuditing==\"1\"">
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1"  align="left" style="word-break:break-all;line-height: 1.4">
										<span class="wz">${model.folderAuditingContent} </span>
									</td>
								
									<td  class="biao_bg1" align="right">
										<span class="wz">卷(盒)描述：</span>
									</td>
									<td class="td1"  align="left" style="word-break:break-all;line-height: 1.4">
										<span class="wz">${model.folderDesc} </span>
									</td>
								</s:if>
								<s:else>
								<td height="21" class="biao_bg1" align="right">
										<span class="wz">卷(盒)描述：</span>
									</td>
									<td class="td1"  align="left" style="word-break:break-all;line-height: 1.4">
										<span class="wz">${model.folderDesc} </span>
									</td>
								</s:else>
								</tr>
							</table>
				<webflex:flexTable name="myTable" width="100%" wholeCss="table1"
					property="id" isCanDrag="true" isCanFixUpCol="true"
					clickColor="#A9B2CA" showSearch="false" footShow="showCheck"
					getValueType="getValueByProperty" collection="${fileList}">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;件号：&nbsp;<input id="tempfilePieceNo" name="tempfilemodel.tempfilePieceNo" type="text"
												 value="${tempfilemodel.tempfilePieceNo}" class="search"
												title="件号">
							       		&nbsp;&nbsp;责任者：&nbsp;<input name="tempfilemodel.tempfileAuthor" type="text"
									 value="${tempfilemodel.tempfileAuthor}"
									class="search" title="责任者">
							       		&nbsp;&nbsp;文号：&nbsp;<input name="tempfilemodel.tempfileNo" type="text"
									 value="${tempfilemodel.tempfileNo}"
									class="search" title="文号"><br>
							       		&nbsp;&nbsp;文件题名：&nbsp;<input name="tempfilemodel.tempfileTitle" type="text"
									style="width:  value="${tempfilemodel.tempfileTitle }"
									class="search" title="文件题名">
							       		&nbsp;&nbsp;文件日期：&nbsp;<strong:newdate id="tempfileDate"
									name="tempfilemodel.tempfileDate" dateform="yyyy-MM-dd"
									isicon="true" 
									dateobj="${tempfilemodel.tempfileDate}" classtyle="search"
									title="文件日期" />
							       		&nbsp;&nbsp;页数：&nbsp;<input id="filePage" name="tempfilemodel.tempfilePage"
									type="text" 
									value="${tempfilemodel.tempfilePage }" class="search"
									title="页数">
							       		
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
					
					<webflex:flexCheckBoxCol caption="选择" property="tempfileId"
						showValue="tempfileTitle" width="5%" isCheckAll="true"
						isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
					<webflex:flexTextCol caption="件号" property="tempfilePieceNo"
									showValue="tempfilePieceNo" width="8%" isCanDrag="true" showsize="5"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="责任者" property="tempfileAuthor"
									showsize="30" showValue="tempfileAuthor" width="20%" showsize="10"
									isCanDrag="true" isCanSort="true">
								</webflex:flexTextCol>
									
								<webflex:flexTextCol caption="文号" property="tempfileNo"
									showValue="tempfileNo" width="16%" isCanDrag="true" 
									showsize="9" isCanSort="true"></webflex:flexTextCol>
									
								<webflex:flexTextCol caption="题名" property="tempfileId"
									showsize="50" showValue="tempfileTitle" width="27%" showsize="14"
									isCanDrag="true" isCanSort="true"
									onclick="viewFileInfo(this.value);"></webflex:flexTextCol>

								
								<webflex:flexDateCol caption="日期" property="tempfileDate"
									showValue="tempfileDate" width="15%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								
								<webflex:flexTextCol caption="页数" showsize="50"
									property="tempfilePage" showValue="tempfilePage" width="8%"
									isCanDrag="true" isCanSort="true" showsize="8"></webflex:flexTextCol>
				</webflex:flexTable>
				</td>
				</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
var folderAuditing=document.getElementById("folderAuditing").value;
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	if(folderAuditing==""||folderAuditing=="0"){	//未归档的文件可进行增删改操作
		item = new MenuItem("<%=root%>/images/operationbtn/add.png","增加","fileAdd",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","fileEdit",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","fileDel",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	}
//	item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","权限","",1,"ChangeWidthTable","checkOneDis");
//	sMenu.addItem(item);
<%--	item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回",":cancel()",1,"ChangeWidthTable","checkOneDis");--%>
<%--	sMenu.addItem(item);--%>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

var folderAuditing=document.getElementById("folderAuditing").value;
var obj=document.getElementById("status");
if(folderAuditing==""){
	obj.innerText="未归档";
}else if(folderAuditing=="0"){
	obj.innerText="未归档";
}else if(folderAuditing=="1"){
	obj.innerText="已归档";
}else if(folderAuditing=="2"){
	obj.innerText="审核中";
}else if(folderAuditing=="3"){
	obj.innerText="驳回";
}else
	obj.innerText="销毁审核";

function fileAdd(){		//增加文件
	var folderId=document.getElementById("folderId").value;
	var archiveSortId=document.getElementById("archiveSortId").value;	//类目id
	var moduletype=document.getElementById("moduletype").value;			//模块标识
	location="<%=path%>/archive/tempfile/tempFile!input.action?forwardStr=addAtFolder&folderId="+folderId+"&archiveSortId="+archiveSortId+"&moduletype="+moduletype;
}

function fileEdit(){	//编辑文件
	var id=getValue();
	var archiveSortId=document.getElementById("archiveSortId").value;	//类目id
	var moduletype=document.getElementById("moduletype").value;			//模块标识
	var folderId=document.getElementById("folderId").value;
	if(id==""){
		alert("请选择文件进行编辑！");
	}
	else if(id.indexOf(",")!=-1){
		alert("只能编辑一条文件信息！")
	}
	else{
		location="<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+id+"&forwardStr=addAtFolder&folderId="+folderId+"&archiveSortId="+archiveSortId+"&moduletype="+moduletype;
	}
}
function fileDel(){		//删除文件
	var id=getValue();
	var archiveSortId=document.getElementById("archiveSortId").value;	//类目id
	var moduletype=document.getElementById("moduletype").value;			//模块标识
	var folderId=document.getElementById("folderId").value;
	if(id==""){
		alert("请选择需删除的文件！");
	}else if(confirm("您确定要删除吗？")){
		location="<%=path%>/archive/tempfile/tempFile!delete.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype+"&tempfileId="+id+"&forwardStr=deleteAtFolder&folderId="+folderId;
	}
}
function viewFileInfo(value){//查看信息
	location="<%=path%>/archive/tempfile/tempFile!input.action?forwardStr=view&tempfileId="+value;
}

function cancel(){		//返回
	//window.history.go(-1);
	var archiveSortId=document.getElementById("archiveSortId").value;	//类目id
	var moduletype=document.getElementById("moduletype").value;			//模块标识
	location="<%=path%>/archive/archivefolder/archiveFolder.action?archiveSortId="+archiveSortId+"&moduletype="+moduletype;
}

function submitForm(){	//表单提交
	document.getElementById("myTableForm").submit();
}

function search(){	  	//查询
	if(isNaN(document.getElementById("filePage").value)){
		alert("文件页码必须为数字！");
		return;
	}
	submitForm();
}

function viewAnnex(value){
	location="<%=path%>/archive/tempfile/tempFile!download.action?tempfileId="+value;
}
</script>
	</BODY>
</HTML>
