<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>稿件列表</TITLE>
		
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
				<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<%--		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>--%>
		
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
				<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
				<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		//设置标题的颜色和字体是否是粗体
		function showTitle(articlesTitle,articlesTitlecolor,articlesTitlefont){
			return "<font color="+articlesTitlecolor+">"+articlesTitle+"</font>";
		}
		function showTitle1(articlesTitle,articlesTitlecolor,articlesTitlefont){
			return articlesTitle;
		}
		function showimg(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
		
			var rv = '' ;
			/**
			if(articlesGuidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}
			if(articlesIshot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}**/
			if(articlesIsstandtop == '1'){
				rv += "<font color='#0000FF'>固顶</font>&nbsp&nbsp";
			}
			if(articlesIscancomment == '1'){
				rv += "<font color='#FF00FF'>评论</font>&nbsp&nbsp";
			}
			return rv;
		}
		function showimg1(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
		
			var rv = '' ;
			if(articlesIsstandtop == '1'){
			//	rv += "<font color='#0000FF'>固顶</font>&nbsp&nbsp";
			rv="固顶";
			}
			if(articlesIscancomment == '1'){
			//	rv += "<font color='#FF00FF'>评论</font>&nbsp&nbsp";
			rv += "   评论"
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
          	<table width="100%" border="0" cellspacing="0"
													cellpadding="00">
													<tr>
														<td class="table_headtd_img">
															<img src="<%=frameroot%>/images/ico/ico.gif">
															&nbsp;
														</td>
														<td align="left">
														<s:if test="column == null">
															<strong>稿件列表</strong>
														</s:if>
														<s:else>
															<strong>${column.clumnName}</strong>
														</s:else>
            											</td>
            											<td align="right">
															<table border="0" align="right" cellpadding="00"
																cellspacing="0">
																<tr>
																	<security:authorize ifAllGranted="001-0004000200010004">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="addArtcles();"><img src="<%=root%>/images/operationbtn/add.png" />&nbsp;新&nbsp;建&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	</security:authorize>
																	 <security:authorize ifAllGranted="001-0004000200010001">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="editUser();"><img src="<%=root%>/images/operationbtn/edit.png" />&nbsp;编&nbsp;辑&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	</security:authorize>
																	 <security:authorize ifAllGranted="001-0004000200010005">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="show();"><img src="<%=root%>/images/operationbtn/view.png" />&nbsp;查&nbsp;看&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	</security:authorize>
																	 <security:authorize ifAllGranted="001-0004000200010008">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="pubArticle();"><img src="<%=root%>/images/operationbtn/public.png" />&nbsp;发&nbsp;布&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	 </security:authorize>
																	 <security:authorize ifAllGranted="001-0004000200010009">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="delpubArticle();"><img src="<%=root%>/images/operationbtn/Cancel_Public.png" />&nbsp;取&nbsp;消发&nbsp;布&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	 </security:authorize>
																         <security:authorize ifAllGranted="001-0004000200010002">
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png" />&nbsp;废&nbsp;除&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	 </security:authorize>
																	<td width="2%"></td>
																	<%--<security:authorize ifAllGranted="001-0003000600020004">
					                 	<td width="50"><a class="Operation" href="javascript:searchNotify();"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" class="img_s">查找</a></td>
					                 	<td width="5"></td>
					                 	</security:authorize>
					                --%>
             					  </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
    </table>
    <tr>
					<td>
					<s:form id="myTableForm" action="/infopub/articles/columnArticles.action" method="post">
						<input type="hidden" name="columnId" id="columnId" value="${columnId}">
						<input type="hidden" name="columnArticleId" id="columnArticleId" value="${columnArticleId}">
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="articlesId" isCanDrag="true"  isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
					        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left; ">
							       		&nbsp;&nbsp;稿件名称：&nbsp;<input style="width: 140px;" name="articlesTitle" id="articlesTitle" type="text" class="search" title="请您输入稿件名称" value="${articlesTitle }">
							       		</div>
							       		 <div style="float: left;  width: 220px;">
							       		&nbsp;&nbsp;作者：&nbsp;<input style="width: 140px;"  name="articlesAuthor" id="articlesAuthor" type="text" class="search" title="请您输入作者" value="${articlesAuthor }">
							       		</div>
<%--							       		 <div style="float: left; ">--%>
<%--							       		&nbsp;&nbsp;栏目名称：&nbsp;<input  style="width: 100px" name="clumnName" id="clumnName" type="text" class="search" title="请您输入栏目名称" value="${clumnName }">--%>
<%--							       		</div>--%>
							       		 <div style="float: left; width: 200px;">
							       		 &nbsp;&nbsp;编辑日期：&nbsp;<strong:newdate  name="columnArticleLatestchangtime" id="columnArticleLatestchangtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入编辑日期" dateform="yyyy-MM-dd" dateobj="${columnArticleLatestchangtime}"/>
							       		</div>
							       		<div style="float: left;width: 170px;padding-top:5px;">
							       		&nbsp;&nbsp;发布状态：&nbsp;<s:select style="width: 70px;"  name="ArcticlesType" list="#{'全部':'0','未发布':'1','已发布':'3'}" listKey="value" listValue="key" onchange='$("#img_sousuo").click();'/>
							       		</div>
							       		 <div style="float: left;  width: 315px;padding-top:5px;">
							       		&nbsp;&nbsp;属性：&nbsp;<s:select style="width: 70px;"  name="disLogo"  list="#{'全部':'0','固顶':'3','评论':'4'}" listKey="value" listValue="key" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
							</table> 
		<webflex:flexCheckBoxCol caption="选择" property="columnArticleId" showValue="toaInfopublishArticle.articlesTitle" width="40px" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="稿件名称" property="toaInfopublishArticle.articlesTitle" showValue="javascript:showTitle(toaInfopublishArticle.articlesTitle,toaInfopublishArticle.articlesTitlecolor,articlesTitlefont)" width="36%" isCanDrag="true" isCanSort="false" showsize="20"></webflex:flexTextCol>
		<webflex:flexTextCol caption="作者" align="center" property="toaInfopublishArticle.articlesAuthor" showValue="toaInfopublishArticle.articlesAuthor" width="10%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
		<webflex:flexTextCol caption="所属栏目" align="center" property="toaInfopublishColumn.clumnName" showValue="toaInfopublishColumn.clumnName" width="15%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="稿件状态" align="center" mapobj="${ArcticlesTypeMap}" property="columnArticleState" showValue="columnArticleState" width="10%" isCanDrag="true" isCanSort="false"></webflex:flexEnumCol>
		<webflex:flexTextCol caption="属性" align="center" 
									property="articlesId"
									showValue="javascript:showimg(columnArticleGuidetype,toaInfopublishArticle.articlesIshot,toaInfopublishArticle.articlesIsstandtop,toaInfopublishArticle.articlesIscancomment)"
									width="8%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
		<webflex:flexDateCol caption="编辑时间" property="columnArticleLatestchangtime" showsize="20" showValue="columnArticleLatestchangtime" dateFormat="yyyy-MM-dd HH:mm:ss" width="21%" isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
	</webflex:flexTable>
	</s:form>
		</tr>
	</table>
</DIV>
<script language="javascript">

  var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	
    <security:authorize ifAllGranted="001-0004000200010004">
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addArtcles",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	    
	<security:authorize ifAllGranted="001-0004000200010001">
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>

	 
	<security:authorize ifAllGranted="001-0004000200010005">
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0004000200010001">
	item = new MenuItem("<%=root%>/images/operationbtn/public.png","发布","pubArticle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
    <security:authorize ifAllGranted="001-0004000200010009">
	item = new MenuItem("<%=root%>/images/operationbtn/Cancel_Public.png","取消发布","delpubArticle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0004000200010002">
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","废除","del",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
    </security:authorize>
	
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//查看评论
function commentShow(){
var id=getValue();
	if(id == null||id == ''){
		alert('请选择稿件！');
		return;
	}
window.location.href="<%=root%>/infopub/articles/articles!getComments.action?articlesId="+id;
}

var columnId = '${columnId}';

function addArtcles(){//增加稿件
	var audit= window.showModalDialog("<%=root%>/infopub/articles/articles!input.action?columnId="+columnId+"&isPromulgate=0",window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
}
function editUser(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以编辑一条记录。');
		return;
	}
	if('${column.processName}'==null||'${column.processName}'=='null'||'${column.processName}'==''){
	   var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!input.action?columnArticleId="+id+"&columnId="+columnId+"&isPromulgate=0",window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
	}else{
		$.post(
			"<%=path%>/infopub/articles/articles!getProcessName.action",
			{columnId:columnId,columnArticleId:id},
			function(data){
				if(data!='flagfalse'){
					var str = data;
					var p = str.split(",");
					if(p[2] != '3' && p[2]!='9'&&p[2]!='5'){
						var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!input.action?columnArticleId="+id+"&columnId="+columnId,window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:700px');
			        }else if(p[2]=='3'){
			        	alert("文章已提交送审中...");
			        }else{
			        	alert("文章已发布...")
			        }
				}else{
					location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=0';
				}
			}
		);
	}
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要废除的记录。');
		return;
	}
<%--	if(id.length>32){--%>
<%--	alert("只可以废除一条记录。")--%>
<%--	return;--%>
<%--	}--%>
	if('${column.processName}'==null||'${column.processName}'=='null'||'${column.processName}'==''){
		if(confirm("确定要废除吗？")){ 
			location = '<%=path%>/infopub/articles/articles!colAtrdelete.action?columnArticleId='+id+'&columnId='+columnId;
		} 
	}else{
		$.post(
			"<%=path%>/infopub/articles/articles!getProcessName.action",
			{columnId:columnId,columnArticleId:id},
			function(data){
				if(data!='flagfalse'){
					var str = data;
					var p = str.split(",");
					if(p[2] != '3' && p[2]!='9'&&p[2]!='5'){
						if(confirm("确定要废除吗？")){ 
							location = '<%=path%>/infopub/articles/articles!colAtrdelete.action?columnArticleId='+id+'&columnId='+columnId;
						}
			        }else if(p[2]=='3'||p[2]=='5'){
			        	alert("文章已提交送审...");
			        }else {
			        	alert("文章已发布...")
			        }
				}else{
					location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=0';
				}
			}
		);
	}
	
}
function notColumn(){
	location = '<%=path%>/infopub/articles/articles.action';
}

function show(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以查看一条记录。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!show.action?columnArticleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:680px');
	//location = '<%=path%>/infopub/articles/articles!showColumnArticl.action?columnArticleId='+id;
}

function pubArticle(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要发布的记录。');
		return;
	}else{
		if(confirm("确定要发布吗？")){
			location = '<%=path%>/infopub/articles/articles!pubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
			}
	}
}

function delpubArticle(){
    var id=getValue();
	if(id == null||id == ''){
		alert('请选择要取消发布的记录。');
		return;
	}else{
		if(confirm("确定要取消发布吗？")){
			$.post(
				"<%=path%>/infopub/articles/articles!getProcessName.action",
				{columnId:columnId,columnArticleId:id},
				function(data){
					if(data!='flagfalse'){
						var str = data;
						var p = str.split(",");
						if(p[2]=='3'){
				        	alert(p[3]+"文章已提交审核中...");
				        }else if(p[2]=='0'){
				            alert(p[3]+"文章已被删除。");
				        }else if(p[2]=='9'){
				        	alert(p[3]+"文章已发布...")
				        }else if(p[2]=='5'){
					    	location = '<%=path%>/infopub/articles/articles!delPubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
						}
					}else{
						 location = '<%=path%>/infopub/articles/articles!delPubArticl.action?columnArticleId='+id+'&columnId='+columnId+'&isPromulgate=1';
					}
				}
			)
		}
	}
}

$(document).ready(function(){
	$("#img_sousuo").click(function(){
		$("form").submit();
	});
}); 


function getListBySta(){	//根据属性查询
	//document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}

</script>
</BODY></HTML>
