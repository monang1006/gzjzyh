<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>调查管理</TITLE>
<%@include file="/common/include/meta.jsp" %>

        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
	
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	</HEAD>
<script language="javascript">
  <s:if test="msg!=null">
    alert("${msg}");
  </s:if>
   var array_style=new Array();//样式名集合
  
  function addStyle(styleid,stylename){
  	array_style[styleid]=stylename;
  	return stylename ;
  }
</script>

<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						
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
													<strong>调查样式列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="styleAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="styleEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="styleDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="styleRest();"><img src="<%=root%>/images/operationbtn/Reset.png"/>重置为默认样式</td>
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
							<tr>
							<td>
  
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="voteId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}" showSearch="false">
		<webflex:flexCheckBoxCol caption="选择" property="styleId" showValue="styleName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="样式名称" property="styleName" showValue="javascript:addStyle(styleId,styleName)" width="95%" isCanDrag="true" isCanSort="true" showsize="70" ></webflex:flexTextCol>
	</webflex:flexTable>
	
	

</td></tr></table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","添加","styleAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","styleEdit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","styleDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/Reset.png","重置为默认样式","styleRest",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function sendByAjax(url,par){
    //ajax异步提交，三个参数分别为：处理页面，参数，返回显示结果
	queryString = par;
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
						   alert("成功重置为默认样式！");
						},
			onFailure : function()
						{
							alert("操作出错，请联系管理员！");
							//alert(url);
							
						},
			parameters : queryString
		}
	);
}


function styleAdd(){//增加样式
	var audit= window.showModalDialog("<%=root%>/vote/voteStyle!input.action",window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:600px');
}
function styleEdit(){
	var id=getValue();
	
	if(id == null||id == ''){
		alert('请选择要编辑的样式！');
		return;
	}
	if(id.length >32){
		alert('每次只能编辑一个样式！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/vote/voteStyle!input.action?styleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:600px');
    //window.open("<%=path%>/vote/voteStyle!input.action?styleId="+id);
}
function styleDel(){//删除样式
	var id=getValue();
	var styleName = array_style[id];
	
	if(id == null||id == ''){
		alert('请选择样式！');
		return;
	}	
		
	if(styleName == '默认样式'){
		alert('默认样式不能删除！');
		return;
	}	
	if(confirm("确定删除样式吗?")) 
	{ 
	  location = '<%=path%>/vote/voteStyle!delete.action?styleId='+id;
	} 	
}

function styleRest(){
    var id=getValue();
    	if(id == null||id == ''){
		alert('请选择要重置的样式！');
		return;
	}
	if(id.length >32){
		alert('每次只能重置一个样式！');
		return;
	}
    url="<%=path%>/vote/voteStyle!reseat.action";
	par="styleId="+id;
    sendByAjax(url,par);
}


</script>


</BODY></HTML>
