<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>设置投票</TITLE>


<!-- <LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet> -->
<link href="<%=frameroot%>/css/properties_windows_add.css" rel="stylesheet" type="text/css">
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<script src="<%=path%>/oa/js/survey/drag.js" type="text/javascript"></script>


<style>  
select{position:relative;left:2px;top:1px;font-size:12px;width:40px;line-height:10px;border:0px;color:#909993;} 

#col_1{position:absolute;width:100%;z-index:1;left:200px; top:53px;padding:8px;font-family:verdana,tahoma;font-size:12px ;line-height:17px;overflow-y:scroll;}
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
  var vote_type="${vote_type}";
  var has_tableqt=false ;//是否存在table型问题
  var table_separator="-" ;//表格型问题的分隔符
  var tmp_value ;
  var array_picsize=new Array();//图片尺寸数组
  var tmppicSize="200*300";//默认的图片尺寸
  var tmp_ques_sort ;//存储拖动前的问题顺序。[drag_qid,drag_qid]
  var tmp_ques_table_type=new Array();//table型问题的类型：单选，多选
  
  function fanhui(){
    //返回
    if(has_tableqt){
      //存在表格型问题
      if(!confirm("表格型问题是否保存？")){
         return ;
      }else{
      	var objImg = document.getElementsByName("tableSaveImg");
      	for(var i=0;i<objImg.length;i++){
      		objImg[i].onclick();
      	}
      }
    }
  	history.back()
  }  
  
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
  
 function savePicsize(element,qid){
  //设置图片尺寸
  if(element.value==pic_size){
   	//没有改变，返回
   	return ;
  }
  if(element.value!=null&&element.value.length>0){
  
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
  //alert(element.value);///////////////////////////////
 } 
    //如果输入框的值改变，保存到数据库
   	var url = "<%=path%>/vote/question!editQuestion.action";
	var par = "question.qid="+qid+"&question.picSize="+element.value;
	sendByAjax(url,par);
	pic_size=element.value;//保存值
    array_picsize[qid]=pic_size;//更新图片尺寸数组
 } 
 
 function uploadpic(aid,oldvalue,qid){
  var uuid="&uuid="+new Date();
  //配置图片时用来上传图片
  //window.open("<%=path%>/vote/answer!init_set_pic.action?pic_size="+array_picsize[qid]+"&answer.aid="+aid);
  window.showModalDialog("<%=path%>/vote/answer!init_set_pic.action?pic_size="+array_picsize[qid]+"&answer.aid="+aid+uuid,window,'help:no;status:no;scroll:auto;dialogWidth:400px; dialogHeight:440px');
 }
 function seturl(aid,oldvalue){
 var uuid="&uuid="+new Date();
 //为答案选项设置详情链接
 //window.open("<%=path%>/vote/answer!init_set_url.action?answer.aid="+aid);
 window.showModalDialog("<%=path%>/vote/answer!init_set_url.action?answer.aid="+aid+uuid,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:170px');
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
   	table_content.push(table_separator);
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
							setAidCheckbox(qid,resp.responseText,cell_aid);
						},
				onFailure : function()
							{
								alert("操作出错，请联系管理员！");
							},
				parameters : queryString
			}
		);
 }
  function setAidCheckbox(qid,aid,cell_aid){
    //cell_aid是第一个格子
	var cell_html="<input onmouseout='hideTip()' onmouseover='showTip(\"双击删除行\",this)' ondblclick=del_row(this) type=";
	//alert(tmp_ques_table_type[qid]);
	if(tmp_ques_table_type[qid]=='N'){
		cell_html=cell_html+"'checkbox'" ;
	}else{
		cell_html=cell_html+"'radio'" ;
	}
	cell_html=cell_html+" value='"+aid+"'>";
	
	cell_aid.innerHTML=cell_html ;
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

<!--addDragDiv(qid,type,title,isRequired,maxRow,tableisonly,picSize,tableheader,isnew)-->
function addBlock(type,title){
    //新增一个调查问题，block=调查问题
	var url = "<%=path%>/vote/question!saveQuestion.action";
	var queryString = "question.vote.vid="+vid+"&question.type="+type+"&question.title="+title+"&question.isRequired=N";
    var picSize="";
    if(type=="pcheckbox"||type=="pradio"){
    	queryString=queryString+"&question.picSize="+tmppicSize;
    	picSize=tmppicSize;
    }
	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							var qid = resp.responseText;
							addDragDiv(qid,type,title,'N',1,'Y',picSize,"",true);
							},
				onFailure : function()
							{
							alert("操作出错，请联系管理员！");
							},
				parameters : queryString
			}
		);
}

function addDragDiv(qid,type,title,isRequired,maxRow,tableisonly,picSize,tableheader,isnew){
	if(picSize!=null&&picSize.length>0){
		array_picsize[qid]=picSize;
	}
	if(type=="table"){
		has_tableqt=true;
	}
	
	//问题可以拖动
	//isnew:标识是否新建的问题
	var mvAry = [];
	mvAry[mvAry.length]  =' <div id="drag_'+qid+'" class="drag_div" style="background:#FFF;">';
	
	mvAry[mvAry.length]  ='		<div style="width:100%;background:url();height:25px;">';
	mvAry[mvAry.length]  ='		<table cellpadding="0" cellspacing="0" border="0">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span id="title_'+qid+'"  ondblclick=editTitle("'+qid+'","'+title+'")>'+title+'</span>';
	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='				<td>';
	mvAry[mvAry.length]  ='					<div id="drag_'+qid+'_h" class="drag_header" style="width:100%;height:25px"><img src="<%=path%>/oa/image/survey/move.gif"></div>';
	mvAry[mvAry.length]  ='				</td></tr><tr>';
	mvAry[mvAry.length]  ='				<td align="left" style="padding-left:10px;padding-right:10px;">';
	if(type=='radio'||type=='checkbox'||type=='select'||type=="pcheckbox"||type=="pradio"){
		mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/survey/add_vote.gif" class="imglinkgray" onclick=addAnswer("'+qid+'","'+type+'") title="添加" style="display:"> ';
	}else if(type=="table"){
		mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/survey/add_vote.gif" class="imglinkgray" onclick=add_row("'+qid+'") title="添加" style="display:"> ';
	}else{
	 //text,textarea不处理
	}
	mvAry[mvAry.length]  ='<img src="<%=path%>/oa/image/survey/edit.gif" class="imglinkgray"  onclick=editTitle("'+qid+'","'+title+'") title="编辑问题标题" style="display:"> <img src="<%=path%>/oa/image/survey/delete_s.gif" class="imglinkgray" title="删除问题" onclick=delDragDiv("'+qid+'") style="display:">&nbsp;&nbsp;必答题:<input type="checkbox" id="required_'+qid+'" class="checkbox" ';
	if('Y'==isRequired){
		mvAry[mvAry.length]  =' checked ';
	}
	mvAry[mvAry.length]  ='title="是否必填、选中表示必填！" onclick=setRequire("'+qid+'")>';
	
	if(type=="checkbox"||type=="radio"||type=="pcheckbox"||type=="pradio"){
	 	mvAry[mvAry.length]  = "&nbsp;&nbsp;每行答案项数:<input value='"+maxRow+"'  onclick='recordValue(this)' onblur=saveValue(this,'"+qid+"','maxRow') type=text maxlength=1 style='width:10px' onmouseout='hideTip()' onmouseover='showTip(\"每行答案项数\",this)'>" ;
    }
    if(type=="pcheckbox"||type=="pradio"){
    	mvAry[mvAry.length]  = "&nbsp;&nbsp;&nbsp;图片尺寸:<input value='"+picSize+"' onclick='recordPicsize(this)' onblur=savePicsize(this,'"+qid+"') type=text style='width:60px'  maxlength=7 onmouseout='hideTip()' onmouseover='showTip(\"如果配置了图片,必须指定图片的显示尺寸，否则不显示图片。(宽度*高度:200*300)\",this)'>";
    }
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

    if(type=="table"){
    if(isnew){
    //为表格型问题创建表格和表格头
    var col_num ;
    
    while(true){
   
       var rtvalue=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/vote/vote-tableinit.jsp",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:170px');
     //  var rtvalue="";
     //  window.open("<%=path%>/fileNameRedirectAction.action?toPage=/vote/vote-table-init.jsp");	
     
    	if(rtvalue==null||rtvalue.length<1)
    	{ //返回空,不创建table型问题
    	  delQuestion2(qid);
    	  return ;
    	}else if(rtvalue.indexOf("-")<0){
    	  continue;
    	}
    	col_num=rtvalue.substring(0,rtvalue.indexOf("-"));
    	tmp_ques_table_type[qid]=rtvalue.substring(rtvalue.indexOf("-")+1); //全局数组
    	
    	//alert(tmp_ques_table_type[qid]);
    	
    	 if(/[^\d]/.test(col_num)){
    	   continue;
    	 }else{
    	   if(col_num>0&&col_num<7)
    	   {
    	     break ;
    	   }
    	 }
    }
    var mt=document.createElement("table");
    var head_content=new Array();
    mt.setAttribute("id","table_"+qid);
    
    var mthead=mt.insertRow();
    mthead.insertCell().innerHTML= "<img src='<%=path%>/oa/image/survey/save.gif' name='tableSaveImg' class='imglinkgray' onclick=savetable('"+qid+"') title='保存表格数据' style='display:'>";; 
    for(var i=1;i<=col_num;i++){
      mthead.insertCell().innerHTML="<input type=text value='表头_"+i+"'>";
      head_content.push("表头_"+i);
      head_content.push(table_separator);
    }
    head_content.pop();
  	saveTablehead(qid,head_content.join(""));//保存表头
   
    var parent_node=document.getElementById('drag_content_'+qid);
    parent_node.appendChild(mt);
    Element.hide('loadcontentid_'+qid);
    return ;
   	}else{
   	   tmp_ques_table_type[qid]=tableisonly;
   	}
   }
    loadDragContent(qid,type,tableisonly);
}


function delDragDiv(qid){
	//删除问题
	delQuestion(qid);
}

function loadDragContent(qid,type,tableisonly){
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
	queryString = "vote_type=1&question.qid="+qid+"&question.type="+type+"&question.tableisonly="+tableisonly;

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
}

function savetable(qid){
  var mt=document.getElementById("table_"+qid);
  var mhead=mt.rows[0];
  var tmp_table=new Array() ;
  
  for(var i=1;i<mhead.cells.length;i++){
    if(mhead.cells[i].firstChild.value.indexOf(table_separator)>-1){
      alert("表格内容不允许出现"+table_separator+"号！");
      mt.focus();
      return ;
    }else if(mhead.cells[i].firstChild.value.length<1||mhead.cells[i].firstChild.value.length>30){
    	   alert("输入字数限制在1-30！");
    	   mt.focus();
    	   return ;
    }else{
      tmp_table.push(mhead.cells[i].firstChild.value);
      tmp_table.push(table_separator);
    }
  }
  tmp_table.pop();
  saveTablehead(qid,tmp_table.join(""));
  
  for(var i=1;i<mt.rows.length;i++){
    tmp_table=new Array() ;
    var aid=mt.rows[i].cells[0].firstChild.value ;//获取aid
   
    for(var j=1;j<mt.rows[i].cells.length;j++){
    	//alert(mt.rows[i].cells[j].firstChild.value.indexOf("="));//输出值
    	if(mt.rows[i].cells[j].firstChild.value.indexOf(table_separator)>-1){
      		alert("表格内容不允许出现"+table_separator+"号！");
      		return ;
    	}else if(mt.rows[i].cells[j].firstChild.value.length<1||mt.rows[i].cells[j].firstChild.value.length>50){
    	   alert("输入字数限制在1-50！");
    	   return ;
    	}else{
      		tmp_table.push(mt.rows[i].cells[j].firstChild.value);
      		tmp_table.push(table_separator);
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
	//table型问题的表头
//					alert(tableHeader);
	var url = "<%=path%>/vote/question!editQuestion.action";
	var queryString = "question.qid="+qid+"&question.tableHeader="+tableHeader+"&question.tableisonly="+tmp_ques_table_type[qid];
	sendByAjax(url,queryString);
}

function createAnswer(qid,type,aid){
	//创建页面控件
   var oNewNode = document.createElement("DIV");
   var nowNode = document.getElementById("drag_content_"+qid);
   
   oNewNode.setAttribute("id",'answer_'+aid);
   nowNode.appendChild(oNewNode);
   
  var in_content ; 
  if(type=='pradio'||type=='pcheckbox'){
    type=type.substring(1);// 截取类型
	in_content = "<table><tr><td><input class="+type+" name="+type+"_"+aid+" type="+type+">&nbsp;<span id='content_"+aid+"' ondblClick=editContent('"+aid+"','新增选项')>新增选项</span></td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('"+aid+"',"+this.value+")\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delAnswer('"+aid+"')\">"
	in_content+="<a href='#' onclick=seturl('"+aid+"','')>[设置详情链接]</a><a href='#' onclick=uploadpic('"+aid+"','','"+qid+"')>[配置图片]</a>";
	in_content+="</td></tr></table>";
  }else if(type=='radio'||type=='checkbox'){
  	in_content = "<table><tr><td><input class="+type+" name="+type+"_"+aid+" type="+type+">&nbsp;<span id='content_"+aid+"' ondblClick=editContent('"+aid+"','新增选项')>新增选项</span></td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('"+aid+"',"+this.value+")\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delAnswer('"+aid+"')\">"
  	if(vote_type=="1"){
  	//网页参与的调查问卷
  		in_content+="<a href='#' onclick=seturl('"+aid+"','')>[设置详情链接]</a>";
  	}
  	in_content+="</td></tr></table>";
  }else if(type=='select'){
	in_content = "<table><tr><td>--<span id='content_"+aid+"' onClick=\"editContent('"+aid+"',"+this.vlaue+")\">新增选项</span>--</td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('"+aid+"',"+this.value+")\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'  onClick=\"delAnswer('"+aid+"')\"></td></tr></table>";
  }else if(type=='text'||type=='textarea'){
	in_content ="";
  }
   oNewNode.innerHTML = in_content;
}

function addAnswer(qid,type){
  //非table类型问题,加入一条答案选项到数据库
  var content; 
  if(type=='radio'||type=='checkbox'||type=='select'||type=='pradio'||type=='pcheckbox'){
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

function delQuestion2(qid){
//直接删除问题，不提示
//table型问题使用
		var rid = document.getElementById("drag_"+qid);
		rid.parentNode.removeChild(rid); 
		url = "<%=path%>/vote/question!delQuestion.action";
		par = "question.qid="+qid;
		sendByAjax(url,par);
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
<div id="contentborder" align="center" >
<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						
							<input name="dictCode" value="${dictCode}" type="hidden">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>设置问卷内容</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												 
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
							　

       
<TABLE width="100%" >
  <TBODY>
  <TR height="100%">
  
    <TD vAlign=top width="150px" height="100%">
    
						<DIV style="PADDING-RIGHT: 5px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
							
							<DIV style="COLOR: red">
								点击'<IMG class=paneladdimg src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>'添加元素
							</DIV>
						<hr>
						<div id="putong">
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/checkbox.gif" align=absMiddle>
								&nbsp;复选框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('checkbox','新建复选框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/radio.gif"
									align=absMiddle>
								&nbsp;单选框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('radio','新建单选框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<br>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/select.gif"
									align=absMiddle>
								&nbsp;下拉菜单&nbsp;
								<IMG class=paneladdimg onclick="addBlock('select','新建下拉菜单问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/text.gif"
									align=absMiddle>
								&nbsp;文本框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('text','新建文本框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/textarea.gif" align=absMiddle>
								&nbsp;多行文本框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('textarea','新建多行文本框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
			                 </DIV>
							<BR>
							<hr>
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/mt.gif" align=absMiddle>
								&nbsp;表格&nbsp;
								<IMG class=paneladdimg onclick="addBlock('table','新建表格问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
			                 </DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/pcheckbox.gif" align=absMiddle>
								&nbsp;复选图片框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('pcheckbox','新建复选图片框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/pr.gif"
									align=absMiddle>
								&nbsp;单选图片框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('pradio','新建单选图片框问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							
				</div>
						<br>
					<div id="col_title_div" ></div> 
					<div id="col_1__div"></div>
						</DIV>
							 
    </TD>
        
   <td valign='top' >
   			<div id="col_1" class="col_div" style="height:380;width:100%;">
				<div id="col_1_hidden_div" class="drag_div no_drag">
					<div id="col_1_hidden_div_h"></div></div>
						
			<div style="float: right;width: 70%" >	
			    <a id="addPerson"  href="#" class="button" onclick="fanhui()" style="align:left" >返回</a>
			     
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
  

  
  
<div style="display:none">
<!-- function addDragDiv(qid,type,title,isRequired,maxRow,maxSelected,picSize,tableheader){ -->

   	<script language="javascript">
   	    var tmp_aaa=new Array();
		<c:forEach items="${list_qt}" var="qt">   	
   			addDragDiv("${qt.qid}","${qt.type}","${qt.title}","${qt.isRequired}","${qt.maxRow}","${qt.tableisonly}","${qt.picSize}","${qt.tableHeader}",false);
			tmp_aaa.splice(0,0,"${qt.qid}");
			tmp_aaa.splice(1,0,",");
		</c:forEach>
		tmp_aaa.pop();
		tmp_ques_sort=tmp_aaa.join("");//存储问题的顺序
		//alert(tmp_ques_sort);
   	</script>
        

</div>

</div>
</BODY>
</HTML>
  



