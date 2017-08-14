<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML><HEAD><TITLE>设置投票</TITLE>

<%@include file="/common/include/meta.jsp" %>


<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>

<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>

<style>   
#fra{height:100%;width:100%;z-index:1;padding:8px;font-family:verdana,tahoma;font-size:12px ;line-height:17px;overflow-y:auto;}
</style>

<STYLE type=text/css>BODY {
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


var bId = new Array();
var i = 0;

function addDragDiv(blockid,col,pos,isnotinit,blocktpl,imgurl,blocktitle,blocktype){
    bId[i++]=blockid;
	var col,blocktpl,imgurl,blocktitle;
	if(typeof(blocktpl) == "undefined" || blocktpl == ""){
		var blocktpl = "gray";
	}
	if(typeof(blocktitle) == "undefined" || blocktitle == ""){
		var blocktitle = "";
	}
	var mvAry = [];

	mvAry[mvAry.length]  =' <div id="drag_'+blockid+'" class="drag_div" style="background:#FFF;">';
	

	mvAry[mvAry.length]  ='		<div style="width:100%;background:url();height:25px;" id="drag_title_'+blockid+'">';
	mvAry[mvAry.length]  ='		<table cellpadding="0" cellspacing="0" border="0">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span id="drag_text_'+blockid+'" >'+blocktitle+'</span>';
	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='			</tr>';
	mvAry[mvAry.length]  ='		</table>';
	mvAry[mvAry.length]  ='		</div>';


	mvAry[mvAry.length]  =' 	<div id="drag_switch_'+blockid+'">';
	mvAry[mvAry.length]  =' 		<div class="drag_editor" id="drag_editor_'+blockid+'" style="display:none">';
	mvAry[mvAry.length]  ='			<div id="loadeditorid_'+blockid+'" style="width:100px"><img src="<%=path%>/oa/image/survey/loading.gif"><span id="loadeditortext_'+blockid+'" style="color:#333"></span></div>';

	mvAry[mvAry.length]  =' 		</div>';
	mvAry[mvAry.length]  =' 		<div class="drag_content" id="drag_content_'+blockid+'"><div id="loadcontentid_'+blockid+'" style="width:100px"><img src="<%=path%>/oa/image/survey/loading.gif"><span id="loadcontenttext_'+blockid+'" style="color:#333"></span></div>';
	mvAry[mvAry.length]  =' 		</div>';
	mvAry[mvAry.length]  ='		</div>';
	mvAry[mvAry.length]  =' </div>';
	
	var objCol = document.getElementById("col_1");
	var objColChilds = objCol.getElementsByTagName("div");
	var objPos = objColChilds[0];
	var newdiv = document.createElement("div");	
		newdiv.innerHTML = mvAry.join("");	

	
	objCol.insertBefore(newdiv,objPos);
	loadDragContent(blockid,blocktype);    
}



//加载元素内容
function loadDragContent(id,type){


	var objDivID = document.getElementById('drag_content_'+id);
	var objOtext = document.getElementById('loadcontenttext_'+id);

	var objLoadcontentid = document.getElementById('loadcontentid_'+id);
	
	objOtext.innerHTML = "";
	
	var saveGimgContent = {
		onCreate: function(){
			Element.show('loadcontentid_'+id);
		},
		onComplete: function() {
			if(Ajax.activeRequestCount == 0){
				Element.hide('loadcontentid_'+id);
			}
		}
	};
	Ajax.Responders.register(saveGimgContent);	

	url = "<%=path%>/survey/surveyAnswer!initView.action";
	queryString = "answerQueNumber="+id+"&type="+type;

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

function init()
{
	var flag = "${viewType}"
	if("sub_sucess"==flag){
		if(confirm("投票成功，是否继续投票？\n\n点击【确定】继续投票\n点击【取消】查看投票结果")==true){
			
		}else{
			surveySee()
		}
	}
	
   var isP = "${isPublic}";
   var objDivID = document.getElementById('sub_mit');
   var survye_id=document.getElementById("sruveyId").value;
    url = "<%=path%>/survey/surveyAnswer!surveyUnrepeatCheck.action";
	queryString = "surveyId="+survye_id;
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
						  var temp = resp.responseText;	
						  if("true"==temp)
						  {
						     alert("你已经参与过投票,不能重复投票！");	
						     if("1"==isP){
						     	objDivID.innerHTML="<input id=s_u  type='submit' class='input_bg'  value='提 交' disabled='disabled' onclick='' >";						     
						     }else{
						     	objDivID.innerHTML="<input id=s_u  type='submit' class='input_bg'  value='提 交' disabled='disabled' onclick='' > <input name='button' type='button' class='input_bg' value='查看结果' onclick='surveySee()'>";
						     }
						  }
						},
			onFailure : function()
						{
							alert("操作出错，请联系管理员！");
							//alert(url);
							return false;
						},
			parameters : queryString
		}
	);
   
}

//提交投票
function sub_mit()
{     
    var str_id="";
    var text_id = "";
    var survye_id="";
    for(var j=0;j<bId.length;j++)
    {
       var va = document.getElementsByName(bId[j]);

       for(var k=0;k<va.length;k++)
       {
          if(va[k].type=="text")
          {
            //alert(va[k].value);
            if(va[k].value=="")
            {
               alert("请回复文本问题！");
               return false;
            }
            if(va[k].value.length>500)
            {
               alert("回复的文本太长,请重新输入！");
               return false;
            }
            text_id+= bId[j]+",";
          }
          if(va[k].type=="textarea")
          {
          //  alert(va[k].value);
            if(va[k].value=="")
            {
               alert("请回复文本问题！");
               return false;
            }
            if(va[k].value.length>500)
            {
               alert("回复的文本太长,请重新输入！");
               return false;
            }
            text_id+= bId[j]+",";
          }
          if(va[k].type=="select-one")
          {
           // alert(va[k].value);
             str_id+=va[k].value+",";
          }
          if(va[k].checked)
          {         
           // alert(va[k].value);
            str_id+=va[k].value+",";
          }
         //alert(va[k].type);
       }
    }
     //alert(str_id);
     //alert(text_id);
     if(""==str_id)
     {
       alert("请选择答案!");
        return false;
     }
    str_id= str_id.substr(0,str_id.length-1);
    text_id = text_id.substr(0,text_id.length-1);
    survye_id=document.getElementById("sruveyId").value;
   document.form1.action ="<%=root %>/survey/surveyAnswer!submit.action?answerNumber="+str_id+"&strQnum="+text_id+"&surveyId="+survye_id;

}


function surveySee(){//查看投票结果
    survye_id=document.getElementById("sruveyId").value;
	var audit= window.showModalDialog("<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+survye_id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}


</SCRIPT>
</HEAD>
<base target="_self"/>

<BODY class=contentbodymargin oncontextmenu="return false;"  style="overflow:scroll"  >
<div id='fra'>
<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0>

  <TBODY>
  <TR>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp; 接受调查 
    
    <input type="hidden" id="sruveyId" value=${surveyId}>
    <input type="hidden" id="isPublic" value=${isPublic}>
   
    </TD></TR>
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
  
    ${topHtml}   
     
<form  name="form1"  method="post" onsubmit="javascript:return sub_mit();"  >
<TABLE width="100%">
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

<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="30%">
									<div id="sub_mit">
									<input id=s_u  type="submit" class="input_bg"  value="提 交" >
									<s:if test="isPublic!=1">
										<input name="button" type="button" class="input_bg" value="查看结果" onclick="surveySee()">
									</s:if>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
</table>
</form>
${bottomHtml}
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<div style="display:none">
    <webflex:flexTable name="myTable"  property="questionId"   getValueType="getValueByProperty"  isCanSplit="false"
      collection="${page.result}" page="${page}">
		<webflex:flexTextCol  property="questionName" showValue="javascript:addDragDiv(questionNumber,0,0,0,gray,null,questionName,questionType)" ></webflex:flexTextCol>
		<webflex:flexTextCol  property="questionTrue" showValue="questionTrue" ></webflex:flexTextCol>
	    <webflex:flexTextCol   property="questionType" showValue="questionType" ></webflex:flexTextCol>
	    <webflex:flexTextCol  property="questionNumber" showValue="questionNumber" ></webflex:flexTextCol>
	</webflex:flexTable>

</div>
</div>
<script type="text/javascript">
			window.onload = function (){
				init();
			}
		</script>
</BODY>
</HTML>




