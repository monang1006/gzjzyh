<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ include file="/common/OfficeControl/version.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<link type="text/css" rel="stylesheet" 
			href="<%=frameroot%>/css/properties_windows_list.css" >
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<!-- 
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		 -->
		<script language='javascript' type="text/javascript"
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" type="text/javascript"
			src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
			<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script type="text/javascript">
	function showimg(obj){
				if(obj==null||obj=="null"||obj==""||obj=='0')
					return "";
				else
					return "<img src='<%=root%>/images/ico/yes.gif' style='cursor: hand;'>";
			}
	function search(){			//按条件查询
	document.getElementById("disLogo").value="search";
	var  tempfilePieceNo=document.getElementById("tempfilePieceNo").value;
	if(isNaN($.trim(tempfilePieceNo))){
		alert("件号必须为数字。");
		return;
	}
	 $("#tempfilePieceNo").val($.trim(tempfilePieceNo));
	document.getElementById("myTableForm").submit();
}		
	function showFolderName(obj,value){
		if(obj==null||obj=="null"||obj==""||obj=="[]")
			return "";
		else{
			
			if(value==null||value==""||value=="null")
			{ 
				return "";
			}
			else{
				return value;
			}
		}
	}
<%--		$(document).ready(function(){--%>
<%--			//禁用全选--%>
<%--			$("input:checkbox[name='checkall']").attr("disabled",true);--%>
<%--			//处理CHECKBOX单击事件--%>
<%--			$("input:checkbox[name!='checkall']").click(function(){--%>
<%--				var dept = $(this).parent().next().next().next().next().attr("value");--%>
<%--				doClick(dept);--%>
<%--				if($("input:checkbox:checked").size() == 0){--%>
<%--					$("input:checkbox[name!='checkall']").attr("disabled",false);--%>
<%--				}--%>
<%--			});--%>
<%--		});	--%>
<%--		function doClick(dept){--%>
<%--			$("input:checkbox[name!='checkall']").each(function(){--%>
<%--				var org = $(this).parent().next().next().next().next().attr("value");--%>
<%--				if(org!=dept){--%>
<%--					$(this).attr("disabled",true);--%>
<%--				}--%>
<%--			});--%>
<%--		}--%>
	 </script>
	</HEAD>
	<BODY class=contentbodymargin onLoad="initMenuT()" style="min-width:800px;">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div style="display: none;">
			<%--<object id="TANGER_OCX_OBJ"
				classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
				codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=4,0,3,2"
				width="100%" height="850">
				<param name="ProductCaption" value="思创数码科技股份有限公司">
				<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
				<param name="BorderStyle" value="1">
				<param name="TitlebarColor" value="42768">
				<param name="TitlebarTextColor" value="0">
				<param name="TitleBar" value="false">
				<param name="MenuBar" value="false">
				<param name="Toolbars" value="true">
				<param name="IsResetToolbarsOnOpen" value="true">
				<param name="IsUseUTF8URL" value="true">
				<param name="IsUseUTF8Data" value="true">
				<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
			</object>
		--%>
		<script type="text/javascript">
				document.write(OfficeTabContent);
		</script>
		</div>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm"
							action="/archive/tempfile/tempFile.action">
							<input type="hidden" id="disLogo" name="disLogo"
								value="${disLogo }">
							<!-- 郑志斌  20110810 是否显示部门档案 -->	
							<input type="hidden" id="depLogo" name="depLogo" value="${depLogo }">	
							
							<input type="hidden" id="treeValue" name="treeValue"
								value="${treeValue }">
							<input type="hidden" id="treeType" name="treeType"
								value="${treeType }">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>资料中心--文件列表</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onClick="addTempFile();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onClick="editTempFile();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onClick="delTempFile();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<!-- OA5.0发版中不包含此功能
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onClick="filePortal();"><img src="<%=root%>/images/operationbtn/rujuan.png"/>&nbsp;入卷(盒)&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onClick="fileGrop();"><img src="<%=root%>/images/operationbtn/zujuan.png"/>&nbsp;组卷(盒)&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		 -->
															<!-- 
															<td width="5">
															</td>
															<td width="120">
																<a class="Operation" href="javascript:searchView();"><img src="<%=root%>/images/ico/rujuan.gif" width="15"
																	height="15" class="img_s">检索查看</a>
															</td>
															 <td width="5">
															</td>
															<td width="5">
															</td>
															<td width="80">
																<a class="Operation" href="javascript:privil();"><img src="<%=root%>/images/ico/quanxankongzhi.gif"
																	width="15" height="15" class="img_s">权限</a>
															</td> 
															-->
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px" 
								wholeCss="table1" property="tempfileId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
									<tr>
										<td>
								       		<div style="float: left;width:190px;padding-top:5px;">&nbsp;&nbsp;件号：&nbsp;<input name="tempfilePieceNo" id="tempfilePieceNo" type="text" style="width:120px;" title="请输入件号" value="${model.tempfilePieceNo}">
								       		</div>
								       		<div style="float: left;width:180px;">&nbsp;&nbsp;文号：&nbsp;<input name="tempfileNo" id="tempfileNo" type="text" style="width:120px;" class="search" title="请输入文号" value="${model.tempfileNo}">
								       		</div>
								       		<div style="float: left;width:200px;">&nbsp;&nbsp;责任者：&nbsp;<input name="model.tempfileAuthor" id="tempfileAuthor" type="text"  style="width:120px;" class="search" title="请输入责任者" value="${model.tempfileAuthor}">
								       		</div>
								       		<div style="float: left;width:240px;">&nbsp;&nbsp;责任处室：&nbsp;<input name="tempfileDepartmentName" id="tempfileDepartmentName" type="text"  class="search" title="请输入责任处室" value="${model.tempfileDepartmentName}">
								       		</div>
								       		<div style="float: left;width:190px;">&nbsp;&nbsp;题名：&nbsp;<input name="tempfileTitle" id="tempfileTitle" type="text"  style="width:120px;" class="search" title="请输入文件题名" value="${tempfileTitle}">
								       		</div>
								       		<div style="float: left;display:none;">&nbsp;&nbsp;卷（盒）号：&nbsp;<input name="model.toaArchiveFolder.folderNo" id="folderNo" type="text" style="width:120px" class="search" title="请输入卷（盒）号" value="${model.toaArchiveFolder.folderNo}">
								       		</div>
								       		<div style="float: left;width:365px;">&nbsp;&nbsp;时间：&nbsp;<strong:newdate  name="model.tempfileDate" id="tempfileDate" skin="whyGreen" isicon="true" width="120px;" classtyle="search" title="请输入时间" dateform="yyyy-MM-dd" dateobj="${model.tempfileDate}"/>
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"/></div>
								       		</div>
								       	</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="tempfileId"
									showValue="tempfileTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="件号" property="tempfilePieceNo"
									showValue="tempfilePieceNo" width="5%" isCanDrag="true" showsize="5"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="文号" property="tempfileNo"
									showValue="tempfileNo" width="16%" isCanDrag="true" 
									showsize="9" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="责任者" property="tempfileAuthor"
									showsize="30" showValue="tempfileAuthor" width="11%" showsize="10"
									isCanDrag="true" isCanSort="true">
								</webflex:flexTextCol>
								<webflex:flexTextCol caption="题名" property="tempfileId"
									showsize="50" showValue="tempfileTitle" width="28%" showsize="14"
									isCanDrag="true" isCanSort="true"
									onclick="viewFileInfo(this.value);"></webflex:flexTextCol>
								<webflex:flexDateCol caption="时间" property="tempfileDate"
									showValue="tempfileDate" width="16%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>
								<webflex:flexTextCol caption="责任处室" showsize="50"
									property="tempfileDepartmentName" showValue="tempfileDepartmentName" width="11%"
									isCanDrag="true" isCanSort="true" showsize="8"></webflex:flexTextCol>
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
	$("#img_sousuo").click(function(){
		search();
	});
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTempFile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	/*OA5.0发版中不包含此功能
	item = new MenuItem("<%=root%>/images/operationbtn/rujuan.png","入卷(盒)","filePortal",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/zujuan.png","组卷(盒)","fileGrop",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	*/
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addTempFile(){		//添加文件
	//var status=document.getElementById("status").value;
	//alert(status);
	//location="<%=path%>/archive/tempfile/tempFile!input.action?status="+status+"&treeType=${treeType}&treeValue=${treeValue}";
	location="<%=path%>/archive/tempfile/tempFile!input.action?depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}";
}
function editTempFile(){	//编辑文件
	var id=getValue();
	if(id=="")
		alert("请选择需编辑的文件。");
	else if(id.indexOf(",")!=-1)
		alert("请选择一个文件。");
	else{
<%--	    $.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",
           {"tempfileId":id},
           function(data){
         
	    if(data=="1"){
	      alert('你无权编辑文件！');
	      return;
	    }else{--%>
		location="<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+id+"&status="+status+"&searchType=tempfile&depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}&IsEditTempFile=True";
<%--		}
		  });--%>
	  }
}
//判断用户是否有权限
function isprivil(id){
   var revalue;
   $.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",
           {"tempfileId":id},
           function(data){
            revalue=data;
           });
         alert(revalue);
         
}
function privil(){
   var id=getValue();
   if(id==""){
   alert("请选择文件。");
   }else{
   window.showModalDialog("<%=path%>/archive/tempfile/tempPrivil!input.action?tempfileIds="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
   }
}
function delTempFile(){		//删除文件
	var id=getValue();
	if(id==""){
		alert("请选择需删除的文件。");
		return;
	}else if(id.length>32){
		alert("不可以同时删除多份文件。");
		return;
	}else{
<%--	 $.post("<%=path%>/archive/tempfile/tempFile!isprivil.action",
           {"tempfileId":id},
           function(data){
         
	    if(data=="1"){
	      alert('你无权删除！');
	      return;
	    }else{--%>
		if(confirm("确定要删除文件吗？")){
		    //top.perspective_content.actions_container.personal_properties_toolbar.closeWorkByNames(data);
			location="<%=path%>/archive/tempfile/tempFile!delete.action?depLogo=${depLogo}&tempfileId="+id+"&status="+status+"&searchType=tempfile&treeType=${treeType}&treeValue=${treeValue}";
	      }
<%--	   }
	   });--%>
	}			
}

function filePortal(){		//文件入卷
    var id=getValue();
	if(id==""){
		alert("请选择需入卷的文件。");
	}else{	
	   var orgId = $("input:checkbox:checked").eq(0).parent().next().next().next().next().attr("value");//getFirst();
       var returnValue=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=archive/tempfile/tempFilePortalContent.jsp?forward=rujuan&moduletype=pige",window,"help:no;status:no;scroll:no;dialogWidth:950px; dialogHeight:500px");
	   if(returnValue!=""&&returnValue!=null){
			location="<%=path%>/archive/tempfile/tempFile!portalFile.action?folderId="+returnValue+"&fileIds="+id+"&depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}";
	   }
	}
}

//入卷并归档
function filePortalpige(){
 var id=getValue();
	if(id==""){
		alert("请选择需入卷归档的文件。");
	}else{	
	    var orgId = $("input:checkbox:checked").eq(0).parent().next().next().next().next().attr("value");//getFirst();
	    var returnValue=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=archive/tempfile/tempFilePortalContent.jsp?forward=rujuan&moduletype=rujuanpige",window,"help:no;status:no;scroll:no;dialogWidth:850px; dialogHeight:500px");
		if(returnValue!=""&&returnValue!=null){
			location="<%=path%>/archive/tempfile/tempFile!portalFile.action?depLogo=${depLogo}&folderId="+returnValue+"&fileIds="+id;
		}
	}
}
function fileGrop(){		//文件组卷
	 var id=getValue();
	if(id==""){
		alert("请选择需组卷的文件。");
	}else{
	    var orgId = $("input:checkbox:checked").eq(0).parent().next().next().next().next().attr("value");//getFirst();
	    location="<%=path%>/archive/archivefolder/archiveFolder!input.action?forward=folderinput&fileIds="+id+"&depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}";
	}
}

function getListBySta(){	//根据是否入卷查询
	document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}

function searchView(){
<%--	window.location="<%=path%>/archive/tempfile/tempFile!searchViewAppend.action?tfileAppedId=402882e0288182180128818544980003";--%>
	var tfileAppedId='40288239288511640128854ffbe30019';
	$.post("<%=path%>/archive/tempfile/tempFile!searchViewAppend.action",
           {"tfileAppedId":tfileAppedId},
           function(data){
           var arr=data.split(",");
         
	    if(arr.length==1){
	      alert(data);
	      return;
	    }else{
            window.parent.parent.refreshWorkByTitle("<%=path%>/archive/tempfile/tempFile!searchView.action?tempfileId="+arr[0]+"&forwardStr=view",arr[1]);
	    }	
	    });
}

function viewFileInfo(value,title){//查看文件
 $.post("<%=path%>/archive/tempfile/tempFile!isView.action",
           {"tempfileId":value},
           function(data){
	    if(data=="1"){
	      alert('当前所查看的文件已不存在。');
	      return;
	    }else{
          //top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+value+"&forwardStr=view&searchType=tempfile",data);
	   	// location="<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+value+"&forwardStr=view&searchType=tempfile";	
	   	  if(window.parent.parent.parent && typeof(window.parent.parent.parent.refreshWorkByTitle) !="undefined"){
		   	  location="<%=path%>/archive/tempfile/tempFile!input.action?depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}&tempfileId="+value+"&forwardStr=view&searchType=tempfile";
	   	  	  return ;
	   	  }
	   	  location="<%=path%>/archive/tempfile/tempFile!input.action?depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}&tempfileId="+value+"&forwardStr=view&searchType=tempfile";
	   	 // window.parent.parent.refreshWorkByTitle("<%=path%>/archive/tempfile/tempFile!input.action?depLogo=${depLogo}&treeType=${treeType}&treeValue=${treeValue}&tempfileId="+value+"&forwardStr=view&searchType=tempfile", data);
	    }	
    });
	//location="<%=path%>/archive/tempfile/tempFile!input.action?tempfileId="+value+"&forwardStr=view";
}

function viewAnnex(value){	//查看附件
	var frame=document.getElementById("annexFrame");
	frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tempfileId="+value;
}
</script>
	</BODY>
</HTML>
