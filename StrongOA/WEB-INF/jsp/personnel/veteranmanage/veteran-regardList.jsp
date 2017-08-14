<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.*"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<base target="_self">
		<%@include file="/common/include/meta.jsp"%>
		<title>老干部基本信息列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	<body class=contentbodymargin oncontextmenu="return false;"
		style="overflow: auto;" onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
			<s:form id="myTableForm"
				action="/personnel/veteranmanage/veteran!regardList.action">
				<input type="hidden" id="personId" name="personId"
					value="${model.personId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td>
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="vereId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty"
								collection="${regardPage.result}" page="${regardPage}"
								pagename="regardPage">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img
												src="<%=frameroot%>/images/perspective_leftside/sousuo.gif"
												id="img_sousuo" style="cursor: hand;" title="单击搜索"
												width="15" height="15" >
										</td>
										<td width="35%" class="biao_bg1">
											<s:textfield name="vereTopic" cssClass="search"
												title="请输入慰问主题"></s:textfield>
										</td>
										<td width="35%" class="biao_bg1">
											<s:textfield name="verePersons" cssClass="search"
												title="请输入带队领导"></s:textfield>
										</td>

										<td width="25%" class="biao_bg1">
											<strong:newdate id="vereTime" name="vereTime" dateobj="${vereTime}"
												dateform="yyyy-MM-dd" width="100%" isicon="true" />
										</td>


										<td class="biao_bg1">
											&nbsp;

										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="vereId"
									showValue="vereTopic" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="慰问主题" property="vereTopic"
									showValue="vereTopic" isCanDrag="true" width="35%"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="带队领导" property="verePersons"
									showValue="verePersons" isCanDrag="true" width="35%"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="慰问日期" property="vereTime"
									showsize="25" dateFormat="yyyy-MM-dd" showValue="vereTime"
									isCanDrag="true" width="25%" isCanSort="true"></webflex:flexDateCol>

							</webflex:flexTable>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addVeteranRegard",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteRegards",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("Form").submit();
        });     
      });
      
 function addVeteranRegard(){
       var pid=document.getElementById("personId").value;
  window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!addRegard.action?personId="+pid,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
	
}
<!-- 编辑指定的慰问信息-->      
function editTraining(){
     var id=getValue();
    
		if(id==null || id==""){
		alert("请选择编辑记录！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能选择一条记录！");
   		 		return ;
   		 	}
   		 }
   		 var pid=document.getElementById("personId").value;
  window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!addRegard.action?regardId="+id+"&personId="+pid,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
      }
 
 function deleteRegards(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择老干部慰问信息！");
			return;
		}
   var pid=document.getElementById("personId").value;
	if(confirm("您确定要删除吗？")){
    parent.veteranPersonRegards.location="<%=path%>/personnel/veteranmanage/veteran!deleteRegard.action?regardId="+id+"&personId="+pid;	
    }	
}
</script>
	</body>
</html>
