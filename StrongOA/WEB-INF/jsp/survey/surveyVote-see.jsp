<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>调查结果</TITLE>
<%@include file="/common/include/meta.jsp" %>


<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>

<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>


<SCRIPT>

function loadingImg(qid,pie_bar,divid){
	
	var objDivID = document.getElementById(divid);
	if(objDivID.style.display == ''){
		objDivID.style.display = 'none';
		return;
	}
	//alert(pie_bar);
    objDivID.style.display = '';  

	document.getElementById(divid).src="<%=path%>/survey/answerImage.action?answerQueNumber="+qid+"&imageType="+pie_bar;

}



function createQue(type,qid,name)
{    
     var oNewNode = document.createElement("DIV");
     var nowNode = document.getElementById("create");
     nowNode.appendChild(oNewNode);

     
     if(type=='textfield'||type=='textarea'){
         var in_content = "<table class=pubtable cellSpacing=1 cellPadding=3 width='100%' border=0><TR class=TableLine1>"+
	         "<TD width='30%'><SPAN title="+name+" style='CURSOR: hand'>"+name+"</SPAN></TD>"+
	         "<TD><DIV id=statusdivflag><IMG style='CURSOR: hand' onclick=\"loadingAnswer("+qid+",'answer_"+qid+"')\""+
	         " src='<%=path%>/oa/image/survey/see_answer.gif'></DIV>"+
	         "<div id='answer_"+qid+"' style='display:none'  height='320'  ></div></TD></TR></table>";
     }
     
    
     if(type=='radio'||type=='checkbox'||type=='select' || type=='tableRadio'|| type=='tableCheckbox'){
	    var in_content = "<table class=pubtable cellSpacing=1 cellPadding=3 width='100%' border=0><TR class=TableLine2>"+
	         "<TD width='30%'><SPAN title="+name+" style='CURSOR: hand'>"+name+"</SPAN></TD>"+
	         "<TD><DIV id=statusdivflag><IMG style='CURSOR: hand' onclick=\"loadingImg("+qid+",'pie','statusdiv_"+qid+"')\""+
	         " src='<%=path%>/oa/image/survey/chart_pie.gif'><IMG style='CURSOR: hand'  onclick=\"loadingImg("+qid+",'bar','statusbardiv_"+qid+"')\""+     
	         "src='<%=path%>/oa/image/survey/chart_bar_down.gif'></DIV><iframe id='statusdiv_"+qid+"' style='display:none' name='SearchContent'  frameborder=0 scrolling=no width='100%' height='320'></iframe>"+
	         " <iframe id='statusbardiv_"+qid+"' style='display:none' name='SearchContent'  frameborder=0 scrolling=no width='100%' height='320'></iframe></TD></TR></table>";
	         
     }
   
     oNewNode.innerHTML = in_content;
   
    
}



function loadingAnswer(qid,divid){


   var objDivID = document.getElementById(divid);
	if(objDivID.style.display == ''){
		objDivID.style.display = 'none';
		return;
	}
    objDivID.style.display = '';  


	url = "<%=path%>/survey/surveyAnswer.action";
	queryString = "answerQueNumber="+qid;
	
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
							//alert(url);
							
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




</SCRIPT>

</HEAD>
<BODY class=bodycolor >
<TABLE cellSpacing=0 cellPadding=3 width="100%" border=0>
  <TBODY>
  <TR>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
      调查结果 </TD></TR>
  <TR>
    <TD class=pagehead2>
  </TD></TR></TBODY></TABLE><BR>
   ${topHtml}
<FORM name=reportform>

    <div>
      <table class=pubtable cellSpacing=1 cellPadding=3 width="100%" border=0>
       <TR>
       <TD width="30%"><strong>调查项目</strong></TD>
       <TD width="70%"><strong>数据统计</strong></TD></TR></table>
    </div>

    <div id="create">

    

    
    
    </div>
    
      
    

</FORM>   
 ${bottomHtml}
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<div style="display:none">
    <webflex:flexTable name="myTable"  property="questionId"   getValueType="getValueByProperty"  isCanSplit="false"
      collection="${page.result}" page="${page}">
		<webflex:flexTextCol  property="questionName" showValue="javascript:createQue(questionType,questionNumber,questionName)" ></webflex:flexTextCol>
		<webflex:flexTextCol  property="questionTrue" showValue="questionTrue" ></webflex:flexTextCol>
	    <webflex:flexTextCol   property="questionType" showValue="questionType" ></webflex:flexTextCol>
	    <webflex:flexTextCol  property="questionNumber" showValue="questionNumber" ></webflex:flexTextCol>
	</webflex:flexTable>
</div>
 
 
    <table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="30%">
									<input name="button" type="button" class="input_bg" value="关 闭" onclick="window.close()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
    </table>

    </BODY></HTML>
