<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%> 
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公文模板项列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
		//alert(window.location);
			$(document).ready(function(){
				$("#sousuo").submit();
			});
		
			function showLogo(type,id,IsHasImage){
				var path=document.getElementById("path").value;	
				var url=path+"view.jpg";
				var	imageUrl= "<img src='"+url+"' width='100px' height='28px' style='border:1px solid #404040;'>";
				if(type!=null&&type!=""&&type!="null"&&IsHasImage!=null&&IsHasImage!=""&&IsHasImage=="1"){
					
			    	url=path+id+"."+type;
					imageUrl= "<img src='"+url+"' width='100px' height='28px' style='border:1px solid #404040;'>";  								
				}
				return imageUrl;
			}
			
			//挂接电子表单
			function handlEForm() {
				var id = getValue();
				if(id == ""){
					alert("请选择要挂接的模板。");
					return ;
				}
				if(id.split(",").length > 1) {
					alert("一次只能选择一张模板。");
					return ;
				}
				var ReturnStr = OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!eformTree.action?doctemplateId="+id,"400", "350", window);
			}
			
			function showType(docType){      
				var rv = '' ;
				if(docType == '1'){
					rv = "<font color='#90036'>Word</font>&nbsp&nbsp";
				}
				if(docType == '2'){
					rv = "<font color='#ff1119'>Excel</font>&nbsp&nbsp";
				}
				if(docType == '3'){
					rv = "<font color='#63ad00'>PowerPoint</font>&nbsp&nbsp";
				}
				if(docType == '4'){
					rv = "<font color='#90036'>Visio</font>&nbsp&nbsp";
				}
				if(docType == '5'){
					rv = "<font color='#ff1119'>Project</font>&nbsp&nbsp";
				}
				if(docType == '6'){
					rv = "<font color='#63ad00'>Wps</font>&nbsp&nbsp";
				}						
				return rv;
			}
			
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<s:form id="myTableForm" 
							action="/doctemplate/doctempItem/docTempItem.action" method="post">
							<s:hidden id="docgroupId" name="docgroupId"></s:hidden>
							<s:hidden id="path" name="path"></s:hidden>
							<s:hidden id="doctempTypeName" name="doctempTypeName"></s:hidden>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>${doctempTypeName}</strong>
							</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="infile();"><img src="<%=root%>/images/operationbtn/Upload.png"/>&nbsp;上&nbsp;传&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="templateItemAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="templateItemEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="templateItemDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="newsubtype();"><img src="<%=root%>/images/operationbtn/The_new_sub_categories.png"/>&nbsp;新&nbsp;建&nbsp;子&nbsp;类&nbsp;别&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="newtype();"><img src="<%=root%>/images/operationbtn/The_new_Root_Category.png"/>&nbsp;新&nbsp;建&nbsp;根&nbsp;类&nbsp;别&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<s:if test="docgroupType !=\"1\"">
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="handlEForm();"><img src="<%=root%>/images/operationbtn/Hanging_form.png"/>&nbsp;挂&nbsp;接&nbsp;表&nbsp;单&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		</s:if>
				                  		
				                  		
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<tr>
						<td>
						
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="doctemplateId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection='${page.result}'
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										 <td>
										 	<div style="float: left; ">
								       		&nbsp;&nbsp;模板名称：&nbsp;<input name="model.doctemplateTitle"  id="doctemplateTitle" type="text" class="search" title="请您输入模板名称" value="${model.doctemplateTitle }">
								       		</div>
								       		<div style="float: left;width: 200px; ">
								       		&nbsp;&nbsp;更新时间：&nbsp;<strong:newdate  name="model.doctemplateCreateTime" id="doctemplateCreateTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入v" dateform="yyyy-MM-dd" dateobj="${model.doctemplateCreateTime}"/>
								       		</div>
								       		<div style="float: left;width:305px; ">
								       		&nbsp;&nbsp;模板类型：&nbsp;<s:select name="model.docType" list="#{'':'请选择类型','1':'Word','2':'Excel','3':'PowerPoint','4':'Visio','5':'Project','6':'Wps'}" listKey="key" listValue="value" style="width:7em;" onchange='$("#img_sousuo").click();'/>
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" onclick="search();" type="button" />
								       		</div>
								       	</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="doctemplateId"
									showValue="doctemplateTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="模板名称" property="doctemplateTitle"
									showValue="doctemplateTitle" width="20%" isCanDrag="true" showsize="30"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="模板简介" property="doctemplateRemark"
									showValue="doctemplateRemark" width="25%" isCanDrag="true" showsize="100"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="模板类型" property="docType"
									showValue="javascript:showType(docType)" width="15%" isCanDrag="true" showsize="100"
									isCanSort="true"></webflex:flexTextCol>
								<%-- <webflex:flexTextCol caption="图标" property="logo"
									showValue="javascript:showLogo(logo,doctemplateId,isHasImage)" width="20%" isCanDrag="true" showsize="30"
									isCanSort="true"></webflex:flexTextCol>--%>
								<webflex:flexDateCol caption="更新时间"
									property="doctemplateCreateTime"
									showValue="doctemplateCreateTime" dateFormat="yyyy-MM-dd"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>

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
	item = new MenuItem("<%=root%>/images/operationbtn/Upload.png","上传","infile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","templateItemAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","templateItemEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","templateItemDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/The_new_sub_categories.png","新建子类别","newsubtype",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/The_new_Root_Category.png","新建根类别","newtype",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	<s:if test="docgroupType !=\"1\"">
	item = new MenuItem("<%=path%>/images/operationbtn/Hanging_form.png","挂接表单","handlEForm",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</s:if>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function templateItemAdd(){
	var Width=screen.availWidth-10;
    var Height=screen.availHeight-30;
    var id=document.getElementById("docgroupId").value;
    
    $.post("<%=path%>/doctemplate/doctempItem/docTempItem!docTempType.action",
	           {"docgroupId":id},
	           function(data){	
<%--	           		当前data为“1”时，为文本编辑--%>
	           		if(data!=null&&data=="1"){  
	           			var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!input.action?docgroupId="+id, Width, Height, window);
	           		}else{
	           			
					    var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!input.action?docgroupId="+id, Width, Height, window);
	           		}
				    if (ReturnStr == "Save") {
				    	window.location="<%=root%>/doctemplate/doctempItem/docTempItem.action?docgroupId="+id;
				    }
	           });
    
}
function templateItemEdit(){
	var doctemplateId = getValue();
	var Width=screen.availWidth-10;
    var Height=screen.availHeight-30;
    var id=document.getElementById("docgroupId").value;
	if(checkSelectedOneDis()){
<%--		var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!input.action?doctemplateId="+doctemplateId, 
                                   Width, Height, window);
	    if (ReturnStr == "OK") {
	         location.reload();
	    }--%>
			 $.post("<%=path%>/doctemplate/doctempItem/docTempItem!docTempType.action",
	           {"docgroupId":id},
	           function(data){	
<%--	           		当前data为“1”时，为文本编辑--%>
	           		if(data!=null&&data=="1"){  
	           			var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!input.action?doctemplateId="+doctemplateId, Width, Height, window);
	           		}else{
	           			
					    var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempItem/docTempItem!input.action?doctemplateId="+doctemplateId, Width, Height, window);
	           		}
				    if (ReturnStr == "Save") {
				        window.location="<%=root%>/doctemplate/doctempItem/docTempItem.action?docgroupId="+id;
				    }
	           });
	}
}
function templateItemDel(){
	var doctemplateId=getValue();
	var id=document.getElementById("docgroupId").value;
	if(checkSelectedOneDis()){
		if(confirm('确定要删除吗？')){
			location="<%=path%>/doctemplate/doctempItem/docTempItem!delete.action?doctemplateId="+doctemplateId+"&docgroupId="+id;
		}
	}
}

function newsubtype(){
	var id=document.getElementById("docgroupId").value;
	//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
	var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!init.action?docgroupId="+id, 
                                   "400px", "250px", window);
    if(ReturnStr=="OK"){
    	parent.project_work_tree.document.location.reload() 
    	//parent.project_work_tree.reload();
    }
}

function newtype(){
	//window.showModalDialog("<%=path%>/doctemplate/doctempType/docTempType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
	var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!init.action", 
                                   "400px", "250px", window);
    if(ReturnStr=="OK"){
    	parent.project_work_tree.document.location.reload() 
    	//parent.project_work_tree.reload();
    }
}

function search(){
	submitForm();
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}
function   UpLoadForm_Validator()   
			  {   
			      var valuefile=$("#UpLoadFile").val();
			     
				  if(valuefile=="")   
				  {   
				  window.confirm("请选择上传的文档。");    
				  return;   
				  }   
				  var   strFileFormat=valuefile.match(/^(.*)(\.)(.{1,8})$/)[3];//检查上传文件格式   
				  strFileFormat=strFileFormat.toUpperCase();   
				  if(strFileFormat=="DOC"||strFileFormat=="doc")   
				  {   
				  $.post("<%=path %>/doctemplate/doctempItem/docTempItem!saveFile.action",
				  {"file":valuefile},
				  function(data){
				  alert(data);
				  });
				   // document.getElementById("myTableForm").action="";
	              //  submitForm();
				  }   
				  else   
				  {   
				  window.confirm("只能上传word文件,请重新选择。");   
				  return ;   
				  }     
			  }   
			function infile(){
				var Width=screen.availWidth/2;
				var Height=screen.availHeight/5;
				//alert(Width);
				//alert(Height);
				window.showModalDialog("<%=path %>/doctemplate/doctempItem/docTempItem!inputFile.action?docgroupId=${docgroupId}",window, "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
		                                "status:no;help:no;scroll:no;");
				location.reload();
			}
</script>
	</body>
</html>
