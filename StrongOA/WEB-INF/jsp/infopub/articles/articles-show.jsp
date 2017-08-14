<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
	<html>
	<head>
	
		<title>查看稿件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<style type="text/css">
		.Wdate{
		border:1px solid #b3bcc3;background-color:#ffffff;
		}
		</style>
		<script type="text/javascript">
			function onsubmitform(){
				document.getElementById("artsave").submit();
			}
			
			function show(){
				if(document.getElementById("articlesRedirecturl").disabled){
					document.getElementById("articlesRedirecturl").disabled ="";
				}else{
					document.getElementById("articlesRedirecturl").disabled ="disabled";
				}
				
			}
			function sohwtime() { //显示隐藏组选项
				var objDiv = document.getElementById ("sohwtime"); 
				if(document.getElementById("gudingBoolean").checked){
					objDiv.style.display = "block"; 
				}else 
					objDiv.style.display = "none"; 
			}
			
			function selectCheckbox(){
				var objDiv = document.getElementById ("sohwtime"); 
				if(document.getElementById("likeBoolean").checked){
					document.getElementById("articlesRedirecturl").disabled ="";
				}
				if(document.getElementById("gudingBoolean").checked){
					objDiv.style.display = "block"; 
				}
			} 
		</script>
		
		<script type="text/javascript">
	function addmykm(id){
      var url="/infopub/articles/articles!showColumnArticl.action?columnArticleId="+id;
      var audit= window.showModalDialog("<%=root%>/knowledge/mykm/mykm!input.action?mykmUrl="+url+"&articleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');

}
function showgo(){
window.close();
}
function commentShow(id){
	if(id == null||id == ''){
		alert('请选择稿件！');
		return;
	}
window.location.href="<%=root%>/infopub/articles/articles!getComments.action?articlesId="+id;
}
	</script>
	</head>
	<body class="contentbodymargin" >

		<DIV id=contentborder align=center >
			<form name="form1">
				<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看稿件</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                 <tr>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
				                  						</tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="00" >
										<tr  valign="top">
										<td valign="top">
										<DIV class=tab-pane id=tabPane1>
										<SCRIPT type="text/javascript">
												tp1 = new WebFXTabPane( document.getElementById("tabPane1") ,false );
										</SCRIPT>
									<DIV class=tab-page id=tabPage1 style="border: 0px"  >
									<H2 class=tab>
										正文
									</H2>
									
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
									<tr><td class="td1" style="word-break:break-all;line-height: 1.4">
												${model.articlesArticlecontent}
									</td>
									</tr>
									</table>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage2   style="border: 0px"  >
									<H2 class=tab>
										文章属性
									</H2>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >责任编辑：&nbsp;</span>
											</td>
											<td class="td1" >
											${model.articlesEditor}
											</td>
										</tr>
										<tr >
											<td  class="biao_bg1" align="right" style="word-break:break-all;line-height: 1.4">
												<span id="addressspan" class="wz" >摘要信息：&nbsp;</span>
											</td>
											<td  class="td1">
											${model.articlesDesc}
											</td>
										</tr>
										<tr style="display:none">
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >内容分页：&nbsp;</span>
											</td>
											<td  class="td1">
												 <s:select name="model.articlesPagination"  list="#{'1':'自动分页','0':'不分页'}" listKey="key" listValue="value" style="width:100px;" disabled="true"/>
												<span class="wz"><font id=colortext color="gray" >
											 自动分页大小${model.articlesPaginationnum}
											</font></span> 
											</td>
										</tr>
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >上线时间：&nbsp;</span>
											</td>
											<td class="td1" >
											<strong:newdate id="articlesAutopublishtime" name="model.articlesAutopublishtime"
												dateform="yyyy-MM-dd HH:mm:ss" width="200"
												dateobj="${model.articlesAutopublishtime}" isicon="true" disabled="true"/>
											</td>
										<tr>
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >下线时间：&nbsp;</span>
											</td>
											<td class="td1" >
											<strong:newdate id="articlesAutopublishtime" name="model.articlesAutopublishtime"
												dateform="yyyy-MM-dd HH:mm:ss" width="200"
												dateobj="${model.articlesAutocancletime}" isicon="true" disabled="true"/>
											</td>
										</tr>
										<tr >
											<td  class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >初始点击数：&nbsp;</span>
											</td>
											<td  class="td1">
											${model.articlesHits}
											</td>
										</tr>
								    </table>
								
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
							</DIV>
						</td>
					</tr>
			</table>
			</td></tr></table>
		</DIV>
	</body>
</html>