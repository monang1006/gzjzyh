<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>参与投票</TITLE>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<STYLE type=text/css>
#content_div{
  padding-top:5px;
}
table.mt { 
	border:1px solid #A9B3CD; 
	border-collapse:collapse; 
} 
tr.mt_row  { 
	padding-top: 2px; 
	padding-bottom: 2px;
} 
tr.mt_row td{	
    text-align:center;
	border: solid #A9B3CD; /* 设置边框属性；样式(solid=实线)、颜色(#999=灰) */ 
	border-width: 1px 1px 1px 1px; /* 设置边框状粗细：上 右 下 左 = 对应：1px 0 0 1px */ 
}

table.rc { 
	border:0px solid #A9B3CD; 
	border-collapse:collapse; 
} 
tr.rc_row  { 
	padding-top: 1px; 
	padding-bottom: 1px;
} 
tr.rct_row td{	
	vertical-align:top;
    text-align:left;
	border:2px solid #A9B3CD; /* 设置边框属性；样式(solid=实线)、颜色(#999=灰) */ 
	/* border-width: 0px 0px 0px 0px;  设置边框状粗细：上 右 下 左 = 对应：1px 0 0 1px */ 
}

table.rc_content { 
	border:0px solid #A9B3CD; 
	border-collapse:collapse; 
} 

BODY {
	MARGIN-TOP: 2px; MARGIN-LEFT: 0px; MARGIN-RIGHT: 5px
}
#title {
	PADDING-RIGHT: 0px; PADDING-LEFT: 0px; FONT-SIZE: 12px; PADDING-BOTTOM: 2px; PADDING-TOP: 0px; BORDER-BOTTOM: #bbb 1px solid
}
#title H1 {
	FONT-SIZE: 16px
}
#title #intro {
	
}
.col_div {
	FLOAT: left; MARGIN: 0px; TEXT-ALIGN: left
}
.drag_div {
	BORDER-RIGHT: #e1e1e1 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #e1e1e1 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px 1px 1px; BORDER-LEFT: #e1e1e1 1px solid; WIDTH: 100%; PADDING-TOP: 0px; BORDER-BOTTOM: #e1e1e1 1px solid
}
.modbox {
	BORDER-RIGHT: #e1e1e1 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #e1e1e1 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px 1px 1px; BORDER-LEFT: #e1e1e1 1px solid; WIDTH: 100%; PADDING-TOP: 0px; BORDER-BOTTOM: #e1e1e1 1px solid
}
.drag_header {
	HEIGHT: 22px
}
.drag_content {
	PADDING-RIGHT: 1px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; PADDING-TOP: 1px; HEIGHT: 30px
}
.drag_editor {
	PADDING-RIGHT: 10px; PADDING-LEFT: 10px; BACKGROUND: #ebebeb; PADDING-BOTTOM: 10px; COLOR: #333; PADDING-TOP: 10px
}
.no_drag {
	BORDER-RIGHT: 0px; PADDING-RIGHT: 0px; BORDER-TOP: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; OVERFLOW: hidden; BORDER-LEFT: 0px; PADDING-TOP: 0px; BORDER-BOTTOM: 0px; HEIGHT: 0px
}
.btitle {
	DISPLAY: inline
}
.imglink {
	BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; CURSOR: hand; MARGIN-RIGHT: 2px; BORDER-BOTTOM: 0px
}
.imglinkgray {
	FILTER: alpha(opacity=80); CURSOR: hand; MARGIN-RIGHT: 2px
}
</STYLE>

<STYLE type=text/css>.block_editor_a {
	PADDING-RIGHT: 2px; DISPLAY: inline; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; WIDTH: 30%; PADDING-TOP: 2px
}
.block_editor_b {
	PADDING-RIGHT: 2px; DISPLAY: inline; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; WIDTH: 70%; PADDING-TOP: 2px; TEXT-ALIGN: left
}
.colorblock {
	BORDER-RIGHT: #666 1px solid; BORDER-TOP: #666 1px solid; DISPLAY: inline; BORDER-LEFT: #666 1px solid; WIDTH: 15px; MARGIN-RIGHT: 5px; BORDER-BOTTOM: #666 1px solid; HEIGHT: 15px
}
.block_button {
	BORDER-RIGHT: #333 1px solid; BORDER-TOP: #333 1px solid; MARGIN-LEFT: 10px; BORDER-LEFT: #333 1px solid; WIDTH: 50px; BORDER-BOTTOM: #333 1px solid; HEIGHT: 18px
}
.block_input {
	BORDER-RIGHT: #666 1px solid; BORDER-TOP: #666 1px solid; BORDER-LEFT: #666 1px solid; BORDER-BOTTOM: #666 1px solid; HEIGHT: 18px
}
UL LI {
	PADDING-RIGHT: 2px; PADDING-LEFT: 2px; LIST-STYLE-IMAGE: url(/images/index_i/dot.gif); PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
</STYLE>

<STYLE>.linkDiv {
	PADDING-RIGHT: 3px; PADDING-LEFT: 3px; PADDING-BOTTOM: 3px; PADDING-TOP: 3px; BORDER-BOTTOM: #ddddee 1px dotted
}
.linktextOp {
	CURSOR: hand; COLOR: #0033cc; BORDER-BOTTOM: #0033cc 1px double
}
.linkgray {
	COLOR: #999
}
.linkgray10 {
	FONT-SIZE: 10px; COLOR: #999
}
.linktext:link {
	COLOR: #0033cc; TEXT-DECORATION: underline
}
.linktext:visited {
	COLOR: #0033cc; TEXT-DECORATION: underline
}
.linktext:active {
	COLOR: #0033cc; TEXT-DECORATION: underline
}
.linktext:hover {
	COLOR: #ff0000; TEXT-DECORATION: underline
}
A:link {
	COLOR: #344456; TEXT-DECORATION: none
}
A:visited {
	COLOR: #344456; TEXT-DECORATION: none
}
A:active {
	COLOR: #3333ff; TEXT-DECORATION: underline
}
A:hover {
	COLOR: #ff0000; TEXT-DECORATION: underline
}
</STYLE>

<SCRIPT>
  function viewresult(){
    //查看结果
    window.showModalDialog("<%=path%>/vote/vote!viewResult.action?vote.vid=${vote.vid}",window,'help:no;status:no;scroll:yes;dialogWidth:950px; dialogHeight:680px');
  }
//qid,title,startDate,endDate,memo,type
function addDragDiv(qid,title,type,isRequired){
	var mvAry = [];

	mvAry[mvAry.length]  =' <div id="drag_'+qid+'" class="drag_div" style="background:#FFF;">';
	

	mvAry[mvAry.length]  ='		<div style="width:100%;background:url();height:25px;" id="drag_title_'+qid+'">';
	mvAry[mvAry.length]  ='		<table cellpadding="0" cellspacing="0" border="0">';
	//mvAry[mvAry.length]  ='			<input type="hidden" name="typevalue_'+qid+'" id="typevalue_'+qid+'" value="'+type+'">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span>'+title ;
	if(isRequired=='Y'){
		mvAry[mvAry.length]  = "(<font color=red>*</font>)";
	}
	mvAry[mvAry.length]  = '</span>';
	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='			</tr>';
	mvAry[mvAry.length]  ='		</table>';
	mvAry[mvAry.length]  ='		</div>';


	mvAry[mvAry.length]  =' 	<div id="drag_switch_'+qid+'">';
	mvAry[mvAry.length]  =' 		<div class="drag_editor" id="drag_editor_'+qid+'" style="display:none">';
	mvAry[mvAry.length]  ='			<div id="loadeditorid_'+qid+'" style="width:100px"><img src="<%=path%>/oa/image/survey/loading.gif"><span id="loadeditortext_'+qid+'" style="color:#333"></span></div>';

	mvAry[mvAry.length]  =' 		</div>';
	mvAry[mvAry.length]  =' 		<div class="drag_content" id="drag_content_'+qid+'"><div id="loadcontentid_'+qid+'" style="width:100px"><img src="<%=path%>/oa/image/survey/loading.gif"><span id="loadcontenttext_'+qid+'" style="color:#333"></span></div>';
	mvAry[mvAry.length]  =' 		</div>';
	mvAry[mvAry.length]  ='		</div>';
	mvAry[mvAry.length]  =' </div>';
	
	var objCol = document.getElementById("col_1");
	var objColChilds = objCol.getElementsByTagName("div");
	var objPos = objColChilds[0];
	var newdiv = document.createElement("div");	
		newdiv.innerHTML = mvAry.join("");	

	
	objCol.insertBefore(newdiv,objPos);
	
	loadDragContent(qid,type);    
}



//加载元素内容
function loadDragContent(qid,type){
	var objDivID = document.getElementById('drag_content_'+qid);
	var objOtext = document.getElementById('loadcontenttext_'+qid);

	var objLoadcontentid = document.getElementById('loadcontentid_'+qid);
	
	objOtext.innerHTML = "";
	
	var saveGimgContent = {
		onCreate: function(){
			Element.show('loadcontentid_'+qid);
		},
		onComplete: function() {
			if(Ajax.activeRequestCount == 0){
				Element.hide('loadcontentid_'+qid);
			}
		}
	};
	Ajax.Responders.register(saveGimgContent);	

	url = "<%=path%>/vote/question!preview_ques.action";
	queryString = "question.qid="+qid+"&question.type="+type;

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
}

function showType(type){
	//设置问卷的类型
  	if(type==1){
   		return "页面投票";
  	}else if(type==2){
   		return "手机短信投票";
  	}
}

 function check(){
  //必填问题的检查
  var isrequired ;
  var type ;
  var ele ;
  var isOk=false;
    <c:forEach items="${list_qt}" var="qt"> 
	isrequired="${qt.isRequired}";
	type="${qt.type}";
	if(isrequired=='Y'){
	   if(type=="text")
	   {
         ele=document.getElementById("text_"+"${qt.qid}");
	  	 if(ele.value==null||ele.value.length<1){
	  	 	alert("必答题[${qt.title}]不能为空！");
	   		return false;
	     }else if(ele.value.length>100)	{
	       alert("[${qt.title}]超过字数限制！");
	   	   return false;
	     }	
	 }else if(type=="textarea"){
	    ele=document.getElementById("textarea_"+"${qt.qid}");
	  	if(ele.value==null||ele.value.length<1){
	  	 	alert("必答题[${qt.title}]不能为空！");
	   		return false;
	     }else if(ele.value.length>200)	{
	       alert("[${qt.title}]超过字数限制！");
	   	   return false;
	     }	
	 }else if(type=="select"){
	 	//do nothing	 
	 }else{
	   //选择题radio,checkbox,pradio,pcheckbox
	   ele=document.getElementsByName("${qt.qid}");
	   isOk=false;
	   for(i=0;i<ele.length;i++) {
       	if(ele[i].checked) {
           isOk=true;
           break;
       	 }       	 
	   }
	   if(!isOk){
	     alert("必答题[${qt.title}]不能为空！");
	     return false;
	   }	
	 }
	 
	}
    </c:forEach>
	document.getElementById("sbvote").style.display="none";
	document.getElementById("sbvote1").style.display="none";
	document.getElementById("sbvote2").style.display="none";
  	document.getElementById("img_loading").style.display="block";    
  	return true;         
	}
	
  function success(){
    alert("感谢您的参与！");
    document.getElementById("img_loading").style.display="none";  
  }
  function failure(){
  	alert("提交失败！");
  	document.getElementById("img_loading").style.display="none";  
    document.getElementById("sbvote").style.display="block";
  }
</SCRIPT>
</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" >
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
								<strong>参与调查</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                	<c:choose>
					                 <c:when test="${canSubmitVote}">
					                 <td ><div style="display:none" id="img_loading"><font color=red>&nbsp;&nbsp;处理中...</font><img src="<%=path%>/oa/image/survey/loading.gif"></div>
				                  		</td>
					                 	<td width="7" id="sbvote1"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="check();" id="sbvote">&nbsp;提交投票&nbsp;</td>
					                 	<td width="7" id="sbvote2"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		
				                  		 </c:when>
				                     </c:choose>
				                     <c:choose>
					                    <c:when test="${limitViewResult}">
						                <security:authorize ifAnyGranted="001-0004000900030001">
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="viewresult();">&nbsp;查看结果&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		</security:authorize>
					                </c:when>	
					               <c:otherwise>
				                     <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="viewresult();">&nbsp;查看结果&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		</c:otherwise>
				                 </c:choose>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp; 闭&nbsp;</td>
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


  ${vote_header}       
  <iframe name="votesbumit" style="display:none"></iframe>     
  <form action="<%=root%>/vote/vote!submitVote.action" onsubmit="return check();" target="votesbumit" method="post"> 
   <input type="hidden" name="vote.vid" value="${vote.vid}">
   
<TABLE width="100%" border=0>
  <TBODY>
  <TR height="100%">
    <td valign='top' class='tablecol1'>&nbsp;
			<div id="col_1" class="col_div">
					<div id="col_1_hidden_div" class="drag_div no_drag"><div id="col_1_hidden_div_h"></div></div>
			</div>		
	</td>	
</TR>
<tr><td><br>
<div>
 <c:out value="${smsVote_memo}" escapeXml="false"/>
 </div>
</td></tr>
  </TBODY>
</TABLE>

<div style="display:none">
   	<script language="javascript">
		<c:forEach items="${list_qt}" var="qt"> 
			addDragDiv("${qt.qid}","${qt.title}","${qt.type}","${qt.isRequired}");
         </c:forEach>
   	</script>

</div>


</form>
 ${vote_foot}
 </td>
 </tr>
 </table>
 </DIV>
</BODY>
</HTML>
  



