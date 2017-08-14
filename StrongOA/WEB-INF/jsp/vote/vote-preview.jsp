<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>设置投票</TITLE>

<%@include file="/common/include/meta.jsp" %>


<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>

<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>


<STYLE type=text/css>
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



//qid,title,startDate,endDate,memo,type
function addDragDiv(qid,title,type){
	var mvAry = [];

	mvAry[mvAry.length]  =' <div id="drag_'+qid+'" class="drag_div" style="background:#FFF;">';
	

	mvAry[mvAry.length]  ='		<div style="width:100%;background:url();height:25px;" id="drag_title_'+qid+'">';
	mvAry[mvAry.length]  ='		<table cellpadding="0" cellspacing="0" border="0">';
	mvAry[mvAry.length]  ='			<input type="hidden" name="typevalue_'+qid+'" id="typevalue_'+qid+'" value="'+type+'">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span id="drag_text_'+qid+'" >'+title+'</span>';
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

</SCRIPT>
</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;"  >

<center>
<TABLE cellSpacing=1 cellPadding=1 width="70%" border=0>

  <TBODY>
  <TR>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"> 预览调查</TD></TR>
  <TR>
    <TD class=pagehead2>

      <TABLE style="FLOAT: right; WIDTH: 150px" cellSpacing=0 cellPadding=0>
        <TBODY>
        <TR>
        <td></td>
       </TR></TBODY>
       </TABLE>
       </TD></TR></TBODY>
</TABLE>
 问卷标题：${vote.title}<br>
     问卷类型：
     <script language="javascript">
       document.write(showType("${vote.type}"));
     </script>
    
    <br> 有效期：[<s:date name="vote.startDate" format="yyyy-MM-dd" />]--[<s:date name="vote.endDate" format="yyyy-MM-dd" />]
       
<TABLE width="70%" border=0>
  <TBODY>
  <TR height="100%">
 
   <td valign='top' class='tablecol1' >
			<div id="col_1" class="col_div">
			
							<div id="col_1_hidden_div" class="drag_div no_drag"><div id="col_1_hidden_div_h"></div></div>
			</div>		
	</td>	

</TR>
  </TBODY>
</TABLE>

<!--${bottomHtml} -->

<div style="display:none">
   	<script language="javascript">
		<c:forEach items="${list_qt}" var="qt"> 
			addDragDiv("${qt.qid}","${qt.title}","${qt.type}");
         </c:forEach>
   	</script>

</div>

<table width="90%" border="0" cellspacing="0" cellpadding="00">
               <br>
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
</center>
</BODY>
</HTML>
  



