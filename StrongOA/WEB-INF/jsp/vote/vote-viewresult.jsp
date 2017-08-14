<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>调查结果</TITLE>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<SCRIPT>
function loadingImg(qid,pie_bar,divid,question_type){
	//加载客观题的答案图表
	var objDivID = document.getElementById(divid);
	if(objDivID.style.display == ''){
		objDivID.style.display = 'none';
		return;
	}
	//alert(pie_bar);
    objDivID.style.display = '';  
	document.getElementById(divid).src="<%=path%>/vote/answer!answerImage.action?qid="+qid+"&imageType="+pie_bar+"&question_type="+question_type;
}

function loadingAnswer2(qid,divid){
	//分页查看文本回答
	var objDivID = document.getElementById(divid);
	if(objDivID.style.display == ''){
		objDivID.style.display = 'none';
		return;
	}
	
    objDivID.style.display = '';
    objDivID.innerHTML="<iframe width='620px' height='400px' frameborder=0 border=0 src='<%=path%>/vote/answer!answerTextPage.action?qid="+qid+"'></iframe>";
   
	//var audit = window.showModalDialog("<%=path%>/vote/answer!answerTextPage.action?qid="+qid,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
	//top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=path%>/vote/answer!answerTextPage.action?qid="+qid,"查看问题答案");
}

function loadingAnswer(qid,divid){
	loadingAnswer2(qid,divid);
	return ;
	//加载主观题的答案
    var objDivID = document.getElementById(divid);
	if(objDivID.style.display == ''){
		objDivID.style.display = 'none';
		return;
	}
	
    objDivID.style.display = '';  
	var url = "<%=path%>/vote/answer!answerText.action";
	var queryString = "qid="+qid;
	
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
						    objDivID.innerHTML = resp.responseText;		
						},
			onFailure : function()
						{
							alert("操作出错，请联系管理员！");
						},
			parameters : queryString
		}
	);
	//var ScreenWidth = screen.availWidth	;
   // var ScreenHeight = screen.availHeight ;
   // var StartX = (ScreenWidth - 400) / 2 ;
   // var StartY = (ScreenHeight - 400) / 2 ;
   //window.open(url, '', 'left='+ StartX + ', top='+ StartY + ', Width=700, height=500, toolbar=no, menubar=no,scrollbars=yes, resizable=yes,location=no, status=1') ;
}

//qid,title,type
function createQues(qid,title,type)
{    
     var oNewNode = document.createElement("DIV");
     var nowNode = document.getElementById("create");
     nowNode.appendChild(oNewNode);
     
     var in_content ;
     if(type=='text'||type=='textarea'){
        in_content = "<table class=pubtable cellSpacing=1 cellPadding=3 width='100%' border=0><TR class=TableLine1>"+
	         "<TD width='30%'><SPAN title='"+title+"' style='CURSOR: hand'>"+title+"</SPAN></TD>"+
	         "<TD><DIV id=statusdivflag><IMG style='CURSOR: hand' onclick=\"loadingAnswer('"+qid+"','answer_"+qid+"')\""+
	         " src='<%=path%>/oa/image/survey/see_answer.gif'></DIV>"+
	         "<div id='answer_"+qid+"' style='display:none'  height='320'  ></div></TD></TR></table>";
     }else if(type=='pradio'||type=='pcheckbox'||type=='radio'||type=='checkbox'||type=='select' || type=='table'){
	    in_content = "<table class=pubtable cellSpacing=1 cellPadding=3 width='100%' border=0><TR class=TableLine2>"+
	         "<TD width='30%'><SPAN title='"+title+"' style='CURSOR: hand'>"+title+"</SPAN></TD>"+
	         "<TD><DIV id=statusdivflag><IMG style='CURSOR: hand' onclick=\"loadingImg('"+qid+"','pie','statusdiv_"+qid+"','"+type+"')\""+
	         " src='<%=path%>/oa/image/survey/chart_pie.gif'><IMG style='CURSOR: hand'  onclick=\"loadingImg('"+qid+"','bar','statusbardiv_"+qid+"','"+type+"')\""+     
	         "src='<%=path%>/oa/image/survey/chart_bar_down.gif'></DIV><iframe id='statusdiv_"+qid+"' style='display:none' name='SearchContent'  frameborder=0 scrolling=no width='100%' height='320'></iframe>"+
	         " <iframe id='statusbardiv_"+qid+"' style='display:none' name='SearchContent'  frameborder=0 scrolling=no width='100%' height='320'></iframe></TD></TR></table>";
     }
     
     oNewNode.innerHTML = in_content;
     //alert(in_content);
}

</SCRIPT>

</HEAD>
<BODY class=bodycolor style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);" >
<DIV id=contentborder align=center>
<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>调查结果</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                	
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关 &nbsp;闭&nbsp;</td>
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

    <div>
      <table class=pubtable cellSpacing=1 cellPadding=3 width="100%" border=0>
       <TR>
       <TD width="30%"><strong>调查项目</strong></TD>
       <TD width="70%"><strong>数据统计</strong></TD></TR></table>
    </div>

    <div id="create"></div>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<div style="display:none">
    <script language="javascript">
		<c:forEach items="${list_qt}" var="qt">   	
			createQues("${qt.qid}","${qt.title}","${qt.type}");
		</c:forEach>
   	</script>
    
</div>
  
    
    </BODY>
   </HTML>
