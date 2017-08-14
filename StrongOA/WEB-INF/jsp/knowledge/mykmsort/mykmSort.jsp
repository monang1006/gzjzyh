<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>知识类型管理</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="initMenuT()">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/knowledge/mykmsort/mykmSort!input.action">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    	
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
            <td>&nbsp;</td>
            <td width="15%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            	知识类型列表
            	</td>
            <td width="5%">&nbsp;</td>
            <td width="80%">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="35%">
                  
                  <td width="20%">&nbsp;</td>
               <td width="*"></td>
                  <td >
                  <a class="Operation" href="javascript:addMykmSort();">
                  	<img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">添加分类&nbsp;</a>                  </td>
                  <td width="5"></td>
                  <td ><a class="Operation" href="javascript:editMykmSort();">
                  	<img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">编辑分类&nbsp;</a>                  </td>
                  <td width="5"></td>
                  <td ><a class="Operation" href="javascript:del();">
                  	<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">删除分类&nbsp;</a>                  </td>
                 
                   <td width="5">&nbsp;</td>
                </tr>
            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="mykmSortId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
        <tr>
          <td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16" style="cursor: hand;" title="单击搜索" onClick="getListBySta();"></td>
          <td width="35%" align="center"  class="biao_bg1">
			<input id="mykmSortName" name="mykmSortName"
			type="text" style="width:100%" value="${mykmSortName}" class="search" title="请输入收藏名称">
          </td>
          
         
          <td width="*%" align="center" class="biao_bg1">
          	<input name="asdf" type="text" style="width:100%" readonly="readonly">
          </td>
      </table> 
		<webflex:flexCheckBoxCol caption="选择" property="mykmSortId" showValue="mykmSortName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="类型名称" onclick="click(mykmSortId)" property="mykmSortName" showValue="mykmSortName" width="35%" isCanDrag="true" isCanSort="true" showsize="15" ></webflex:flexTextCol>
		<webflex:flexTextCol caption="类型描述" property="mykmSortDesc" showValue="mykmSortDesc" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	    
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加分类","addMykmSort",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑分类","editMykmSort",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除分类","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);

	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}


//单元格点击事件
function click(id){

window.location.href="<%=root%>/knowledge/mykmsort/mykmSort!input.action?mykmId="+id;
}
function addMykmSort(){//增加知识分类
	var audit= window.showModalDialog("<%=root%>/knowledge/mykmsort/mykmSort!input.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
}
function editMykmSort(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要修改的知识！');
		return;
	}
	if(id.length >32){
		alert('不能同时修改多个知识点！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/knowledge/mykmsort/mykmSort!input.action?mykmSortId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
	
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要废除的知识！');
		return;
	}
	if(confirm("确定废除这个收藏的知识吗?")) 
	{ 
	location = '<%=path%>/knowledge/mykm/mykm!delete.action?mykmId='+id;
	} 
	
}

function show(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择知识点！');
		return;
	}
	if(id.length >32){
		alert('只能同时编辑一个知识点！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/knowledge/mykm/mykm!input.action?mykmSortId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
}

function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}



</script>
</BODY></HTML>
