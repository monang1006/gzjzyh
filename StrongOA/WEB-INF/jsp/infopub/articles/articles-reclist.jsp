<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>回收站稿件列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<%--		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>--%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script type="text/javascript">
		function showimg(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
			var rv = '' ;
		    /**
			if(articlesGuidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}
			if(articlesIshot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}
			**/
			if(articlesIsstandtop == '1'){
				rv += "<font color='#0000FF'>固顶</font>&nbsp&nbsp";
			}
			if(articlesIscancomment == '1'){
				rv += "<font color='#FF00FF'>评论</font>&nbsp&nbsp";
			}
			return rv;
		}
		</script>
		
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
           <td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>回收站稿件列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="revert();"><img src="<%=root%>/images/operationbtn/Reduction.png"/>&nbsp;还&nbsp;原&nbsp;稿&nbsp;件</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="truedel();"><img src="<%=root%>/images/operationbtn/completely_del.png"/>&nbsp;彻&nbsp;底&nbsp;删&nbsp;除</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="clears();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;清&nbsp;空&nbsp;信&nbsp;息 </td>
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
												<s:form theme="simple" id="myTableForm"
														action="/infopub/articles/articles!reclist.action" method="post">
														<webflex:flexTable name="myTable" width="100%"
															height="370px" wholeCss="table1" property="articlesId"
															isCanDrag="true" isCanFixUpCol="true"
															clickColor="#A9B2CA" footShow="showCheck"
															getValueType="getValueByProperty"
															collection="${page.result}" page="${page}">
															<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
												
							   			  <tr>
							       <td>
							       	   <div style="float: left; ">
							       		&nbsp;&nbsp;稿件名称：&nbsp;<input  name="articlesTitle" style="width:140px;"  id="articlesTitle" type="text" class="search" title="请您输入稿件名称" value="${model.articlesTitle}">
							       		</div>
							       		 <div style="float: left; width: 220px;">
							       		&nbsp;&nbsp;作者：&nbsp;<input  name="articlesAuthor" style="width:140px;"  id="articlesAuthor" type="text" class="search" title="请您输入作者" value="${model.articlesAuthor }">
							       		</div>
							       		 <div style="float: left; ">
							       		&nbsp;&nbsp;栏目名称：&nbsp;<input  name="clumnName" style="width:140px;"  id="clumnName" type="text" class="search" title="请您输入栏目名称" value="${clumnName }">
							       		</div>
							       		 <div style="float: left; width: 220px; ">
							       		&nbsp;&nbsp;编辑：&nbsp;<input  name="articlesLatestuser"  style="width:140px;"  id="articlesLatestuser" type="text" class="search" title="请您输入编辑" value="${model.articlesLatestuser }">
							       	   </div>
							       		 <div style="float: left;width: 180px"> 
							       	    &nbsp;&nbsp;编辑日期：&nbsp;<strong:newdate width="90px" name="articlesLatestchangtime"    id="articlesLatestchangtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入编辑日期" dateform="yyyy-MM-dd" dateobj="${model.articlesLatestchangtime}"/>
							       		</div>
							       		 <div style="float: left; width: 315px;padding-top: 3px;">
							       		&nbsp;&nbsp;属性：&nbsp;<s:select name="disLogo"  list="#{'全部':'0','固顶':'3','评论':'4'}" listKey="value" listValue="key" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
        </table>
		<webflex:flexCheckBoxCol caption="选择"  property="columnArticleId" showValue="articlesTitle"  width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="稿件名称"  property="columnArticleId" showValue="toaInfopublishArticle.articlesTitle" onclick="show(this.value)" width="30%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
		<webflex:flexTextCol caption="作者"   align="center" property="articlesAuthor" showValue="toaInfopublishArticle.articlesAuthor"  width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="所属栏目"  align="center" property="clumnName" showValue="toaInfopublishColumn.clumnName"  width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="属性"  align="center"
									property="articlesId"
									showValue="javascript:showimg(columnArticleGuidetype,toaInfopublishArticle.articlesIshot,columnArticleIsstandtop,toaInfopublishArticle.articlesIscancomment)"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="编辑"  align="center" property="columnArticleLatestuser" showValue="columnArticleLatestuser" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol caption="编辑时间"   property="columnArticleLatestchangtime" showsize="20" showValue="columnArticleLatestchangtime" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	</webflex:flexTable>
  </s:form>
											</tr>
									</table>
</DIV>
<script language="javascript">
function show(value){
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!show.action?columnArticleId="+value,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:600px');
}
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;

	item = new MenuItem("<%=root%>/images/operationbtn/Reduction.png","还原稿件","revert",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);

	 

	item = new MenuItem("<%=root%>/images/operationbtn/completely_del.png","彻底删除","truedel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);

	 

	//item = new MenuItem("<%=root%>/images/operationbtn/del.png","清空信息","clear",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);

	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function revert(){//还原
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要还原的记录。');
		return;
	}
   location = '<%=path%>/infopub/articles/articles!revert.action?columnArticleId='+id;
}

function truedel(){//彻底删除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要彻底删除的记录。');
		return;
	}
	if(confirm("确定要彻底删除吗？")) 
	{ 
	location = '<%=path%>/infopub/articles/articles!truedelete.action?articlesId='+id;
	} 
	
}

function clears(){
	if(confirm("确定要清空所有记录吗？")) 
	{ 
	location = '<%=path%>/infopub/articles/articles!clear.action';
	} 
}

function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}


$(document).ready(function(){
	$("#img_sousuo").click(function(){
		$("form").submit();
	});
}); 

</script>
</BODY></HTML>
