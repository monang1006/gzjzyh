<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>设置投票</TITLE>
<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<script src="<%=path%>/oa/js/survey/drag.js" type="text/javascript"></script>

<style>   
td{
  word-break:keep-all
}
select{position:relative;left:2px;top:1px;font-size:12px;width:40px;line-height:10px;border:0px;color:#909993;} 

#col_1{position:absolute;width:100%;z-index:1;left:203px; top:53px;padding:8px;font-family:verdana,tahoma;font-size:12px ;line-height:17px;overflow-y:scroll;}
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
  var vid="${vid}";
  var no_question=true; //手机短信投票只允许创建一个问题
  var tmp_value ;
  var tmp_ques_sort ;//存储拖动前的问题顺序。[drag_qid,drag_qid]
  function recordValue(element){
    tmp_value=element.value
  }
  
  function saveValue(element,qid,name){
  //更新Question的属性
  //name是Question的属性名
  if(element.value==null||element.value.length<1){
   	return ;
  }
  if(/[^\d]/.test(element.value)){
    alert("必须是大于零的数字！");
    element.focus();
    return ;
  }else if(element.value<1){
    alert("必须大于零！");
    element.focus();
    return ;
  }
   if(element.value==tmp_value){
     return ;
   }else{
   //保存
   	var url = "<%=path%>/vote/question!editQuestion.action";
	var par = "question.qid="+qid+"&question."+name+"="+element.value;
	sendByAjax(url,par);
   }
  }
  
  var pic_size;
  function recordPicsize(element,qid){
  	//记录输入框的原来的值
    pic_size=element.value ;
  }
  
 function savePicsize(element){
  //设置图片尺寸
  if(element.value==null||element.value.length<1){
   	return ;
  }
  if(element.value.indexOf("*")<0||element.value.indexOf("*")==element.value.length-1){
    alert("图片尺寸值设置错误！样例(200*300)");
    element.focus();
    return ;
  }
 
  var value=element.value;
  var tmp;
  tmp=value.substring(0,element.value.indexOf("*"));
  if(/[^\d]/.test(tmp)){
    alert("图片尺寸值必须是数字！样例(200*300)");
    element.focus();
    return ;
  }
  tmp=value.substring(element.value.indexOf("*")+1);
  if(/[^\d]/.test(tmp)){
    alert("图片尺寸值必须是数字！样例(200*300)");
    element.focus();
    return ;
  }
    
  if(element.value==pic_size){
     return ;
  }else{
    //如果输入框的值改变，保存到数据库
   	var url = "<%=path%>/vote/question!editQuestion.action";
	var par = "question.qid="+qid+"&question.picSize="+element.value;
	sendByAjax(url,par);
  }
 } 
 
 function uploadpic(aid){
  //配置图片时用来上传图片
 window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/survey/survey-pic-upload.jsp?aid="+aid,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:140px');
 }
 function seturl(aid){
 //为答案选项设置详情链接
 window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/survey/survey-set-url.jsp?aid="+aid,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:140px');
 }
 
 function add_row(qid){
  //表格型问题的加入表格行
  var mt=$("table_"+qid);
  var mr=mt.insertRow();
  var table_content=new Array();
  var cell_aid=mr.insertCell();
  
  for(var i=1;i<mt.rows[0].cells.length;i++)
   {
   	mr.insertCell().innerHTML="<input maxlength=30 type=text name='' value='新建数据项'>";
   	table_content.push("新建数据项");
   	table_content.push("=");
   }
   table_content.pop();
   
   var url = "<%=path%>/vote/answer!saveAnswer.action";
   var queryString = "answer.question.qid="+qid+"&answer.content="+table_content.join("");

	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							setAidCheckbox(resp.responseText,cell_aid);
						},
				onFailure : function()
							{
								alert("操作出错，请联系管理员！");
							},
				parameters : queryString
			}
		);
 }
  function setAidCheckbox(aid,cell_aid){
	cell_aid.innerHTML="<input ondblclick=del_row(this) type=checkbox  value='"+aid+"'>";
  }
  
 function del_row(element){
 //删除表格型问题的表格行
   var mr=element.parentNode.parentNode;
   var aid=element.value ;
  if(confirm("删除行?")){
    var url="<%=path%>/vote/answer!delAnswer.action";
  	var queryString="answer.aid="+aid ;
  	sendByAjax(url,queryString);
  	
  	mr.parentNode.deleteRow(mr.rowIndex);
  }
 }


function updateQusSort(str)
{   str=str.replace(new RegExp("drag_","g"),"");
	if(tmp_ques_sort==str){
	  //如果没有改变问题的顺序号，则返回
	  return ;
	}
	tmp_ques_sort=str;
	//alert(str);
    //拖动结束后，重新计算问题的顺序
    //使用英文逗号分隔，每个元素：drag_qid
    url = "<%=path%>/vote/question!updateSort.action";    
	par ="ques_sort="+str;
	sendByAjax(url,par);
}
/* 
 *====================================================================================================================
*/
var oPopup = window.createPopup();

function showTip(msg,element)
{
//存在基准坐标element
with (oPopup.document.body)
{
 style.backgroundColor="lightyellow";
 style.border="solid black 1px";
 style.fontSize = 12;
 innerHTML=msg;
}
 
 oPopup.show(20, -20, (msg.length+2)*10, 15, element);
}

function hideTip()
{
 if(oPopup!=undefined)
  oPopup.hide();
}
//==============================格式化字符串函数(删除前后空格)========================================================
function trim(str)
{
 var tmpStr = new String(str);
 var startIndex = 0,endIndex = 0;
 for(var i=0;i<tmpStr.length;i++)
 {
  if(tmpStr.charAt(i)==" ")
  {
   continue;
  }
  else
  {
   startIndex = i;
   break;
  }
 }
 for(var i=tmpStr.length;i>=0;i--) //注意开始最大下标必须减1
 {
  if(tmpStr.charAt(i-1)==" ")
  {
   
   continue;
  }
  else
  {
   endIndex = i;
   break;
  }
 }

  tmpStr = tmpStr.substring(startIndex,endIndex);
  return tmpStr;
}
//====================================================================================================================
function sendByAjax(url,par){
	queryString = par;
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
						
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

<!--addDragDiv(qid,type,title,isRequired,maxRow,maxSelected,picSize,tableheader,isnew)-->
function addBlock(type,title){
    if(!no_question){
       alert("手机短信投票只允许创建一个问题！");
       return ;
    }
    //新增一个调查问题，block=调查问题
	url = "<%=path%>/vote/question!saveQuestion.action";
	queryString = "question.vote.vid="+vid+"&question.type="+type+"&question.title="+title+"&question.isRequired=N";
   
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							var qid = resp.responseText;
							addDragDiv(qid,type,title,'N',1,1,"","",true);
						},
				onFailure : function()
						{
							alert("操作出错，请联系管理员！");
						},
				parameters : queryString
			}
		);
	
}

function addDragDiv(qid,type,title,isRequired,maxRow,maxSelected,picSize,tableheader,isnew){
	//问题可以拖动
	//isnew:标识是否新建的问题
	var mvAry = [];
	mvAry[mvAry.length]  =' <div id="drag_'+qid+'" class="drag_div" style="background:#FFF;">';
	
	mvAry[mvAry.length]  ='		<div style="width:100%;background:url();height:25px;">';
	mvAry[mvAry.length]  ='		<table cellpadding="0" cellspacing="0" border="0">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span id="title_'+qid+'"  ondblclick=editTitle("'+qid+'","'+title+'")>'+title+'</span>';
//	mvAry[mvAry.length]  ='				</td>';
//	mvAry[mvAry.length]  ='				<td align="left" style="padding-left:20px;padding-right:20px;">';
	if(type=='radio'||type=='checkbox'||type=='select'){
		mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/survey/add_vote.gif" class="imglinkgray" onclick=addAnswer("'+qid+'","'+type+'") title="添加" style="display:"> ';
	}else if(type=="table"){
		mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/survey/add_vote.gif" class="imglinkgray" onclick=add_row("'+qid+'") title="添加" style="display:"> ';
	}else{
	 //text,textarea不处理
	}
	mvAry[mvAry.length]  ='<img src="<%=path%>/oa/image/survey/edit.gif" class="imglinkgray"  onclick=editTitle("'+qid+'","'+title+'") title="编辑问题标题" style="display:">' ;
	
//	if(type=="checkbox"||type=="radio"){
//	 mvAry[mvAry.length]  = "&nbsp;<input value='"+picSize+"' onclick='recordPicsize(this)' onblur=savePicsize(this,'"+qid+"') type=text style='width:60px'  maxlength=7 onmouseout='hideTip()' onmouseover='showTip(\"如果配置了图片,应该指定图片的显示尺寸。(200*300)\",this)'>";
//	 mvAry[mvAry.length]  = "&nbsp;&nbsp;<input value='"+maxRow+"'  onclick='recordValue(this)' onblur=saveValue(this,'"+qid+"','maxRow') type=text maxlength=1 style='width:10px' onmouseout='hideTip()' onmouseover='showTip(\" 每行最大的答案项数\",this)'>" ;
//    if(type=="checkbox"){
//     	mvAry[mvAry.length]  = "&nbsp;&nbsp;<input value='"+maxSelected+"' onclick='recordValue(this)' onblur=saveValue(this,'"+qid+"','maxSelected') type=text maxlength=1 style='width:10px' onmouseout='hideTip()' onmouseover='showTip(\" 最多选中的答案项数\",this)'>" ;
//     }
//    }
//	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='				<td>';
	mvAry[mvAry.length]  ='					<div id="drag_'+qid+'_h" class="drag_header" style="width:100%;height:25px"><img src="<%=path%>/oa/image/survey/move.gif"></div>';
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
	
	objCol.insertBefore(newdiv,objPos);//使用插入法家在问题。后加载的问题，显示在前面。
	
	initDrag();//初始化拖拽问题

    loadDragContent(qid,type);
    
    no_question=false;//创建成功，不能再创建问题了
}


function delDragDiv(qid){
	//删除问题
	delQuestion(qid);
	no_question=true;
}

function loadDragContent(qid,type){
    //从服务器加载问题的所有答案
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

	url = "<%=path%>/vote/question!load_answer.action";
	queryString = "type=sms&question.qid="+qid+"&question.type="+type;

	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							objDivID.innerHTML = resp.responseText;			
							//alert( resp.responseText);					
						},
			onFailure : function()
						{
							alert("操作出错，请联系管理员！");
						},
			parameters : queryString
		}
	);
}

function savetable(qid){
  var mt=document.getElementById("table_"+qid);
  var mhead=mt.rows[0];
  var tmp_table=new Array() ;
  
  for(var i=1;i<mhead.cells.length;i++){
    if(mhead.cells[i].firstChild.value.indexOf("=")>0){
      alert("表格内容不允许出现=号！");
      mt.focus();
      return ;
    }else if(mhead.cells[i].firstChild.value.length<1||mhead.cells[i].firstChild.value.length>50){
    	   alert("输入字数限制在0-50！");
    	   mt.focus();
    	   return ;
    }else{
      tmp_table.push(mhead.cells[i].firstChild.value);
      tmp_table.push("=");
    }
  }
  tmp_table.pop();
  saveTablehead(qid,tmp_table.join(""));
  
  for(var i=1;i<mt.rows.length;i++){
    tmp_table=new Array() ;
    var aid=mt.rows[i].cells[0].firstChild.value ;//获取aid
   
    for(var j=1;j<mt.rows[i].cells.length;j++){
    	//alert(mt.rows[i].cells[j].firstChild.value);//输出值
    	if(mt.rows[i].cells[j].firstChild.value.indexOf("=")>0){
      		alert("表格内容不允许出现=号！");
      		return ;
    	}else if(mt.rows[i].cells[j].firstChild.value.length<1||mt.rows[i].cells[j].firstChild.value.length>50){
    	   alert("输入字数限制在0-50！");
    	   return ;
    	}else{
      		tmp_table.push(mt.rows[i].cells[j].firstChild.value);
      		tmp_table.push("=");
    	}
    }
    tmp_table.pop();
  	saveTableContent(aid,tmp_table.join(""));
  }

}
function saveTableContent(aid,content){
	//保存table型问题的行
 	var url="<%=path%>/vote/answer!editAnswer.action";
  	var queryString="answer.aid="+aid+"&answer.content="+content ;
  	sendByAjax(url,queryString);
}
function saveTablehead(qid,tableHeader){
	//报错table型问题的表头
    var url = "<%=path%>/vote/question!editQuestion.action";
	var queryString = "question.qid="+qid+"&question.tableHeader="+tableHeader;
	sendByAjax(url,queryString);
}

function createAnswer(qid,type,aid){
	//创建页面控件
   var oNewNode = document.createElement("DIV");
   var nowNode = document.getElementById("drag_content_"+qid);
   
   oNewNode.setAttribute("id",'answer_'+aid);
   nowNode.appendChild(oNewNode);
    
  if(type=='radio'||type=='checkbox'){
	var in_content = "<table><tr><td><input class="+type+" name="+type+"_"+aid+" type="+type+">&nbsp;<span id='content_"+aid+"' ondblClick=editContent('"+aid+"','新增选项')>新增选项</span></td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('"+aid+"',"+this.value+")\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delAnswer('"+aid+"')\">"
	//in_content+=" <a href='#' onclick=uploadpic('"+aid+"')>[配置图片]</a><a href='#' onclick=seturl('"+aid+"')>[设置详情链接]</a>";
	in_content+="</td></tr></table>";
  }else if(type=='select'){
	var in_content = "<table><tr><td>--<span id='content_"+aid+"' onClick=\"editContent('"+aid+"',"+this.vlaue+")\">新增选项</span>--</td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('"+aid+"',"+this.value+")\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'  onClick=\"delAnswer('"+aid+"')\"></td></tr></table>";
  }else if(type=='text'||type=='textarea'){
	var in_content ="";
  }
   oNewNode.innerHTML = in_content;
}

function addAnswer(qid,type){
  //非table类型问题,加入一条答案选项到数据库
  var content; 
  if(type=='radio'||type=='checkbox'||type=='select'){
	content="新增选项";
  }else{
	content=""
  }
  var url="<%=path%>/vote/answer!saveAnswer.action";
  var queryString ;
  if(content.length>0){
	  queryString="answer.question.qid="+qid+"&answer.content="+content;
  }
  new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							createAnswer(qid,type,resp.responseText);
							//无法将aid传出
						},
			onFailure : function()
							{
								alert("操作出错，请联系管理员！");
							},
			parameters : queryString
			}
		);
 
}

function delAnswer(aid){
	var logicv = window.confirm("是否删除答案？");
	if(logicv){
		var url = "<%=path%>/vote/answer!delAnswer.action";
		var queryString = "answer.aid="+aid;
		new Ajax.Request(
			url,
			{
				method: "post",	
				onSuccess : function(resp)
						{
							var rid = document.getElementById("answer_"+aid);
							rid.parentNode.removeChild(rid); 
						},
				onFailure : function()
						{
							alert("操作出错，请联系管理员！");
							//alert(url);
							
						},
				parameters : queryString
		});
	}
}

function delQuestion(qid){
	var logicv = window.confirm("是否删除问题？");
	if(logicv){
		url = "<%=path%>/vote/question!delQuestion.action";
		queryString = "question.qid="+qid;
		new Ajax.Request(
			url,
			{
				method: "post",	
				onSuccess : function(resp)
						{
							var rid = document.getElementById("drag_"+qid);
							rid.parentNode.removeChild(rid); 
						},
				onFailure : function()
						{
							alert("操作出错，请联系管理员！");
							//alert(url);
							
						},
				parameters : queryString
		});
	}
}

var curObj = null;
function editTitle(qid,old_str){
 //编辑问题的标题
 var obj = document.getElementById('title_'+qid);
  if (curObj == null){
    curObj = obj;
	  var inputleng = eval(obj.innerText.replace(/[^\x00-\xff]/g,"**").length);
	  var text_content = "<input type='text' id='ed_"+qid+"' size="+inputleng+" value='"+obj.innerText+"' onblur=saveTitle(this,'"+qid+"','"+old_str+"');>";
	  obj.innerHTML = text_content;
	  document.getElementById("ed_"+qid).focus(); 
	  obj.firstChild.select();
  }
}
function saveTitle(obj,qid,old_str){
	//保存问题标题
    var str =  obj.value;
	if(str==''){
		alert("您编辑的内容为空不能保存！请检查！");
		return false;
	}
	if(str.length>100){
		alert("您编辑的内容太长不能保存！请检查！");
		return false;
	}
	curObj.innerHTML = str;
	if(str!=old_str){
		var url = "<%=path%>/vote/question!editQuestion.action";
		var par = "question.qid="+qid+"&question.title="+str;
		sendByAjax(url,par);
	}
	curObj = null;
}

function editContent(aid){
	 //编辑答案内容
	 var obj = document.getElementById('content_'+aid);
	 tmp_value=obj.innerText; //记录编辑前的值
  if (curObj == null){
      curObj = obj;
	  var inputleng = eval(obj.innerText.replace(/[^\x00-\xff]/g,"**").length);
	  var text_content = "<input type='text' id='ed_"+aid+"' size="+inputleng+" value=\""+obj.innerText+"\" onblur=saveContent(this,'"+aid+"');>";
	  obj.innerHTML = text_content;
	  document.getElementById("ed_"+aid).focus(); 
	  obj.firstChild.select();
  }	
}
function saveContent(obj,aid){
	//保存答案选项
    var str =  obj.value;
	if(str==''){
		alert("您编辑的内容为空不能保存！请检查！");
		return false;
	}
	if(str.length>50){
		alert("您编辑的内容太长不能保存！请检查！");
		return false;
	}
	curObj.innerHTML = str;
	if(str!=tmp_value){
		//如果值被改变
		var url = "<%=path%>/vote/answer!editAnswer.action";
		var par = "answer.aid="+aid+"&answer.content="+str;
		sendByAjax(url,par);
	}
	curObj = null;
}

function setRequire(qid){
	//设置问题是否必答题
	var state_str = document.getElementById('required_'+qid).checked;
	var required ;
	if(state_str==true){
		required ='Y';
	}else{
		required ='N';
	}
	var url = "<%=path%>/vote/question!editQuestion.action";
	var par = "question.qid="+qid+"&question.isRequired="+required;
	sendByAjax(url,par);
}

</SCRIPT>
</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="" >
<div id="contentborder" align="center">
<br>
<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
  <TBODY>
  <TR>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp; 设置问卷内容</TD></TR>
</TBODY>
</TABLE>
     
       
<TABLE width="100%">
  <TBODY>
  <TR height="100%">
  
    <TD vAlign=top width="200px" height="100%">
    			 
    </TD>
    
     
   <td valign='top' >
   
			<div id="col_1" class="col_div" style="height:380;width:100%;">
			                  
							<div id="col_1_hidden_div" class="drag_div no_drag"><div id="col_1_hidden_div_h"></div></div>
						
			<div style="float: right;width: 60%" >	
			      <input name="button" align="left" type="button" class="input_bg" value="返 回" onclick="history.back()">
			</div>
			</div>	
	<script>
		var h = document.getElementById("col_1").style.height;
		document.getElementById("col_1").style.height = document.body.clientHeight-80;
	</script>
	</td>
	

</TR>

  </TBODY>
  </TABLE>
  

  
  
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<div style="display:none">
<!-- function addDragDiv(qid,type,title,isRequired,maxRow,maxSelected,picSize,tableheader){ -->

   	<script language="javascript">
   	    var tmp_aaa=new Array();
		<c:forEach items="${list_qt}" var="qt">   	
   			addDragDiv("${qt.qid}","${qt.type}","${qt.title}","${qt.isRequired}","${qt.maxRow}","Y","${qt.picSize}","${qt.tableHeader}",false);
			tmp_aaa.splice(0,0,"${qt.qid}");
			tmp_aaa.splice(1,0,",");
		</c:forEach>
		tmp_aaa.pop();
		tmp_ques_sort=tmp_aaa.join("");//存储问题的顺序
		
		if(tmp_aaa.length<1){
		  addBlock('radio','新建单选框问题元素');
		}
		//alert(tmp_ques_sort);
   	</script>
        

</div>

</div>
</BODY>
</HTML>
  



