<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>列表控件映射列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		 <script type="text/javascript">
		 
		 function showInfo(privilName,privilSyscode){
		 	var info = "";
		 	if(privilName != null ){
		 		info += privilName;
		 	}
		 	if(privilSyscode != null){
		 		info += "(" + privilSyscode + ")";
		 	}
		 	return info;
		 	
		 }
		 
		 function showType(mapType){
		 	var type = ""
		 	if(mapType == "0"){
		 		type = "<font color='#90036'>展现列表</font>&nbsp&nbsp"; 
		 	}
		 	if(mapType == "1"){
		 		type = "<font color='#90036'>搜索列表</font>&nbsp&nbsp"; 
		 	}
		 	return type;
		 }
		 
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
	 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/componentMap/componentMap.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="70%">
												<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9"  align="center">&nbsp;
												映射列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td >
												<a class="Operation" href="#" onclick="addMap()"> <img
														src="<%=root%>/images/ico/tianjia.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">添加&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="editMap()"> <img
														src="<%=root%>/images/ico/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="delMap()"> <img
														src="<%=root%>/images/ico/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
		
											<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
 									<tr>
					                  <td width="3%" align="center" class="biao_bg1">
					                    <img id="img_sousuo" onclick="doSubmit();" style="cursor: hand;" src="<%=root%>/images/ico/sousuo.gif" width="17"
					                      height="16">
					                  </td>
					                  <td width="30%" class="biao_bg1">
											<s:textfield name="model.mapPrivil.privilName" cssClass="search"
												title="请输入资源编码名称"></s:textfield>
									  </td>
									  
									  <td width="25%" class="biao_bg1">
											<s:textfield name="model.mapProcess.pfName" cssClass="search"
												title="请输入流程名称"></s:textfield>
									  </td>

	
					                  <td width="30%" class="biao_bg1">
					                  	<s:textfield name="model.mapForm.title" cssClass="search"
												title="请输入列表控件名称"></s:textfield>
					                  </td>
					                  					                  
					                  <td width="15%" align="center" class="biao_bg1">
					                  		<s:select onchange= "doSelect();" name="model.mapType" list="#{'':'全部','0':'展现列表','1':'搜索列表'}" 
					                  			listKey="key" listValue="value" style="width:100%"/>
					                  </td>
					                  <td width="5%" align="center" class="biao_bg1">
					                    &nbsp;
					                  </td>
					                </tr>
							</table>
								<webflex:flexCheckBoxCol caption="选择" property="mapId"
									showValue="mapPrivil.privilName" width="3%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="资源编码" property="mapPrivil.privilId"
									showValue="javascript:showInfo(mapPrivil.privilName,mapPrivil.privilSyscode)" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="流程名称" property="mapProcess.pfId"
									showValue="mapProcess.pfName" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="列表控件" showsize="25" property="mapForm.id"
									width="30%" showValue="mapForm.title" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>	
								<webflex:flexTextCol caption="用途类型" property="mapType"
									width="15%" showValue="javascript:showType(mapType)" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
					</td>
				</tr>
			</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
  //$("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添 加","addMap",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编 辑","editMap",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","delMap",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addMap(){
   	var width=screen.availWidth/4;
    var height=screen.availHeight/3;
	var result=window.showModalDialog("<%=path%>/componentMap/componentMap!input.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	//alert(result);
	window.location.href="<%=path %>/componentMap/componentMap.action";
}
function editMap(){

	var id=getValue();	
		if(id==null || id==""){
		alert("请选择需要编辑的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能编辑一条记录!');
		return;
	}	
	var width=screen.availWidth/4;
    var height=screen.availHeight/3;
	var result=window.showModalDialog("<%=path%>/componentMap/componentMap!input.action?mapId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");		
	//window.location.href="<%=path %>/componentMap/componentMap.action";
}
function delMap(){

	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
		return;
	}
		
	if(confirm("您确定要删除吗？")){
		$.post(
			"<%=path%>/componentMap/componentMap!delete.action",
			{mapId:id},
			function(data){		
				if(data=="false"){	
			      alert("删除失败");
			          
				}else if(data=="true"){
			   		location="<%=path%>/componentMap/componentMap.action"; 
				}
			}
		);
	}	
}


$(document).ready(function(){
	$("#img_sousuo").click(doSelect);     
});

function doSelect(){
	$("form").submit();
}


</script>
	</BODY>
</HTML>
		