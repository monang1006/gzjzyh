<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>

	<HEAD>
		<TITLE>未上栏稿件列表</TITLE>
		<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
	<!--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		-->
		<script type="text/javascript">
		//设置标题的颜色和字体是否是粗体
		function showTitle(articlesTitle,articlesTitlecolor,articlesTitlefont){
			return "<font color="+articlesTitlecolor+">"+articlesTitle+"</font>";
		}
		function showimg(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
			var rv = '' ;
			/**if(articlesGuidetype == '1'){
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
		function showTitle1(articlesTitle,articlesTitlecolor,articlesTitlefont){
			return articlesTitle;
		}
		function showimg1(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment){
			var rv = '' ;
			/**if(articlesGuidetype == '1'){
				rv += "<font color='#CC0000'>导</font>&nbsp&nbsp";
			}
			if(articlesIshot == '1'){
				rv += "<font color='#FF0000'>热</font>&nbsp&nbsp";
			}**/
			if(articlesIsstandtop == '1'){
				rv += "固顶";
			}
			if(articlesIscancomment == '1'){
				rv +="评论";
			}
			return rv;
		}
		</script>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%"  >
						<table width="100%" border="0" cellspacing="0" cellpadding="0"  >
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
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
															<strong>未上栏稿件列表</strong>
														</td>
														<td align="right">
															<table border="0" align="right" cellpadding="00"
																cellspacing="0">
																<tr>
																    <security:authorize ifAllGranted="001-0004000200010003">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="addArtclesToColumn();"><img src="<%=root%>/images/operationbtn/On_the_bar.png" />&nbsp;上&nbsp;栏&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	 </security:authorize>
																	 <security:authorize ifAllGranted="001-0004000200010004">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="addArtcles();"><img src="<%=root%>/images/operationbtn/add.png" />&nbsp;新&nbsp;建&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	</security:authorize>
																	<security:authorize ifAllGranted="001-0004000200010001">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="editArtcles();"><img src="<%=root%>/images/operationbtn/edit.png" />&nbsp;编&nbsp;辑&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	 </security:authorize>
																	<security:authorize ifAllGranted="001-0004000200010005">
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
																	<td class="Operation_list" onclick="show();"><img src="<%=root%>/images/operationbtn/view.png" />&nbsp;查&nbsp;看&nbsp;</td>
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td width="5"></td>
																	</security:authorize>
																    <security:authorize ifAllGranted="001-0004000200010002">
																	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
																	<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png" />&nbsp;删&nbsp;除&nbsp;</td>
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
											<tr>
												<td>
													<s:form theme="simple" id="myTableForm"
														action="/infopub/articles/articles.action" method="post">
														<webflex:flexTable name="myTable" width="100%"
															height="370px" wholeCss="table1" property="articlesId"
															isCanDrag="true" isCanFixUpCol="true"
															clickColor="#A9B2CA" footShow="showCheck"
															getValueType="getValueByProperty"
															collection="${page.result}" page="${page}">
															<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;稿件名称：&nbsp;<input style="width:140px;"  name="articlesTitle" id="articlesTitle" type="text" class="search" title="请您输入稿件名称" value="${model.articlesTitle}">
							       		</div>
							       		<div style="float: left;width: 200px;">&nbsp;&nbsp;作者：&nbsp;<input style="width:120px;" name="articlesAuthor" id="articlesAuthor" type="text" class="search" title="请您输入作者" value="${model.articlesAuthor }">
							       		</div>
							       		<div style="float: left;width: 200px;">&nbsp;&nbsp;编辑：&nbsp;<input  style="width:120px;" name="articlesLatestuser" id="articlesLatestuser" type="text" class="search" title="请您输入编辑" value="${model.articlesLatestuser }">
							       	   </div>
							       	    <div style="float: left;width: 200px;"> &nbsp;&nbsp;编辑日期：&nbsp;<strong:newdate width="100px" name="articlesLatestchangtime" id="articlesLatestchangtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入编辑日期" dateform="yyyy-MM-dd" dateobj="${model.articlesLatestchangtime}"/>
							       		</div>
							       		<div style="float: left;padding-top:2px;">&nbsp;&nbsp;属性: &nbsp;<s:select  name="disLogo"  list="#{'全部':'0','固顶':'3','评论':'4'}" listKey="value" listValue="key"  onchange='$("#img_sousuo").click();' />
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
							</table> 
															<webflex:flexCheckBoxCol caption="选择"
																property="articlesId" showValue="articlesTitle"
																width="40px" isCheckAll="true" isCanDrag="false"
																isCanSort="false"></webflex:flexCheckBoxCol>
															<webflex:flexTextCol caption="稿件名称"
																property="articlesId" showValue="javascript:showTitle(articlesTitle,articlesTitlecolor,articlesTitlefont)"
																width="25%" isCanDrag="true" isCanSort="false"
																showsize="20"></webflex:flexTextCol>
															<webflex:flexTextCol caption="作者" align="center"
																property="articlesAuthor" showValue="articlesAuthor"
																width="15%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
															<webflex:flexTextCol caption="属性" property="articlesId" align="center"
																showValue="javascript:showimg(articlesGuidetype,articlesIshot,articlesIsstandtop,articlesIscancomment)"
																width="15%" isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
															<webflex:flexTextCol caption="编辑" align="center"
																property="articlesLatestuser"
																showValue="articlesLatestuser" width="15%" align="center"
																isCanDrag="true" isCanSort="false"></webflex:flexTextCol>
															<webflex:flexDateCol caption="编辑时间"
																property="articlesLatestchangtime"
																showValue="articlesLatestchangtime" showsize="20"
																dateFormat="yyyy-MM-dd HH:mm:ss" width="25%"
																isCanDrag="true" isCanSort="false"></webflex:flexDateCol>

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
	<security:authorize ifAllGranted="001-0004000200010003">
	item = new MenuItem("<%=root%>/images/operationbtn/On_the_bar.png","上栏","addArtclesToColumn",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0004000200010004">
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addArtcles",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0004000200010001">
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editArtcles",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0004000200010005">
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	
	<security:authorize ifAllGranted="001-0004000200010002">
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function commentShow(){
var id=getValue();
	if(id == null||id == ''){
		alert('请选择记录。');
		return;
	}
window.location.href="<%=root%>/infopub/articles/articles!getComments.action?articlesId="+id;
}
function addArtclesToColumn(){//上栏
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要上栏的记录。');
		return;
	}
	var audit= window.showModalDialog("<%=root%>/infopub/articles/articles!tree.action?treeType=1&articlesId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
}

function addArtcles(){//增加稿件
	var audit= window.showModalDialog("<%=root%>/infopub/articles/articles!input.action",window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:600px');
}
function editArtcles(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以编辑一条记录。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!input.action?articlesId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:768px');
	
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的记录。');
		return;
	}
	if(confirm("确定要删除吗？")) 
	{ 
	location = '<%=path%>/infopub/articles/articles!delete.action?articlesId='+id;
	} 
	
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
	var audit = window.showModalDialog("<%=path%>/infopub/articles/articles!show.action?articlesId="+id,window,'help:no;status:no;scroll:no;dialogWidth:860px; dialogHeight:600px');
}

<%--function getListBySta(){	//根据属性查询--%>
<%--	--%>
<%--	document.getElementById("myTableForm").submit();--%>
<%--}--%>

$(document).ready(function(){
	$("#img_sousuo").click(function(){
		$("form").submit();
	});
}); 

</script>
</BODY></HTML>
