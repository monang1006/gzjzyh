<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>设置投票</TITLE>


<LINK href="<%=path%>/oa/css/survey/style.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
<script src="<%=path%>/oa/js/survey/prototype.js" type="text/javascript"></script>
<script src="<%=path%>/oa/js/survey/drag.js" type="text/javascript"></script>


<style>   
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

/*
 *====================================================================================================================
*/

//定义全局的表格行列值
var col = 10;
var row = 3;

//currRowIndex 选定行
var currRowIndex = 0;

//表单域数组(这部分作废)-----------------
var elementNames = new Array(col);
var k = 0;
for(k=0;k<col;k++)
 elementNames[k] = k;
//---------------------------------------

//标题头数组
var headers = new Array();
//headers[0] = "姓名";
//headers[1] = "性别";
//headers[2] = "职位";
//headers[3] = "电话";
//for(k=0;k<col;k++)
//{
// headers[k] = "第" + k + "列";
//}

//columnPropertys : 装载要创建的列读写属性
var colPropertys = new Array(col)
for(k=0;k<10;k++)
 colPropertys[k] = 1;

 colPropertys[0] = 0;

//colDefaultValues : 代表每次增加行的时候需要装入的默认值
var colDefaultValues = new Array(col);
for(k=0;k<col;k++)
{
 if(k%2==0)
  colDefaultValues[k] = "o";
 else
  colDefaultValues[k] = "j"; //代表这个不是默认值
}

//dataArray : 需要修改的数据集,修改之前预先装入,供用户参考
var dataArray = new Array(row*col);
var m=0,n=0;
for(m=0;m<row;m++)
{
 for(n=0;n<col;n++)
 {
  //dataArray[m*col + n] ="value(" + m + "," + n + ")"; 
   dataArray[m*col + n] ="0"; //注意这里代表2维数组
 }
}


/* 
 *====================================================================================================================
*/
var oPopup = window.createPopup();


//创建一个公共的可编辑的表格
//参数：  
//   formName : 表单名
//   action : 代表当前要执行的提交动作 : 1:add 2:del 3:modefy 4:query
//   formAction : 表格对应Form的Action
//   tableId ：表格ID
//   colNum：表单列数目
//   rowNum：初始表格的行数目
function CreateTable(formName,action,formAction,tableId,colNum,rowNum)
{ 
 var startStr = new String("");
 var endStr = new String("");
 var colStr = new String("");
 startStr = "<form name=\"" + formName + "\" method=post action=\"" + formAction + "\">"; 
 startStr += "<table id=\"" + tableId +"\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\">";
 
 colStr = createTrs(colNum,rowNum);
 
 endStr = "</table>";

 //创建用来获取最后表格输出数据的隐藏域
 var hiddens = CreateHiddens(colNum,action);
 endStr += hiddens;
 endStr += "</form>";
 //输出表格
 //document.forms(0).elements("t").value = (startStr + colStr + endStr);
// document.write(startStr + colStr + endStr);
 
 //var addStr="<input type='button' value='InsertRow' onclick='InsertRow(\""+tableId+"\")'/>";
   var addStr="<img src='<%=path%>/oa/image/survey/add_vote.gif' class='imglinkgray' onclick='InsertRow(\""+tableId+"\")' title='添加' style='display:'>&nbsp;&nbsp;";
   var delStr="<img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' onclick='DeleteRow(\""+tableId+"\")' title='删除' style='display:'>&nbsp;&nbsp;";
    var saveStr="<img src='<%=path%>/oa/image/survey/save.gif' class='imglinkgray' onclick='getData(\""+tableId+"\")' title='提交' style='display:'>";
   document.getElementById("col_1_hidden_div").innerHTML=addStr+delStr+saveStr+startStr + colStr + endStr;
   var rcStr="<input type='radio' name='rad' id='rado' value='radio' style='color:#00000F' checked/>单选框&nbsp;<input type='radio' name='rad' id='chbo' value='checkbox' style='color:#00000F'/>复选框&nbsp";
    document.getElementById("col_1__div").innerHTML=rcStr;
       var titStr="设置标题：<input type='text' name='title' id='topTitles' size='35'/>&nbsp";
        document.getElementById("col_title_div").innerHTML=titStr;
    
}

function CreateHiddens(cols,action)
{
 var str = new String("");
 var i=0;
 for(i=0;i<cols;i++)
 {
  str +="<input type=\"hidden\" name=\"col" + (i+1) + "\" >";
 }
 str+="<input type=hidden name=\"action\" value=\"" + action + "\">";
 return str;
}

function createTrs(colNum,rowNum)
{
 var colStr = new String("");
 var i = 0;
 var j = 0;

 colStr += "<tr bgColor='#E0E0E0'>"
 
 //创建表头行
  colStr += "<td onClick=orderTb1(this,1); onMouseOver=\"showTip('单击对本列排序');\" onMouseOut=\"hideTip();\"><center>"
  colStr += "序号";
  colStr += "</center></td>";
 for(i=1;i<=colNum;i++)
 {
  colStr += "<td align='center' width='15%' onClick=orderTb1(this,1); onMouseOver=\"showTip('单击对本列排序');\" onMouseOut=\"hideTip();\" >";
  colStr += headers[i-1];
  colStr += "</td>";
 }

 //创建表体行
 for(i=0;i<rowNum;i++)
 {
  colStr += "<tr ";
  colStr += "onDblClick=\"ChangeInput(this);\" ";  //表格行中的事件支持部分
  colStr += "onBlur=\"ResetTR(this);\" >";
  
  //创建每行的所有单元格
   colStr += "<td  onClick=SelectRow(this);>";
   colStr += i+1;
   colStr +="</td>";
  for(j=1;j<=colNum;j++)
  {
   colStr += "<td onMouseOut=\"this.bgColor='#FFFFFF';\" onMouseOver=\"this.bgColor='#E6CAFF';\" >";
 colStr += dataArray[i*colNum+j-1];
   colStr +="</td>";
  }
  colStr +="</tr>";
 }
 return colStr;
}

//把某行转变为输入状态
function ChangeInput(objTR)
{
 var str = new String("");
 var i = 0;
 
 for(i=1;i<objTR.cells.length;i++)
 {
  str = "<input type=text name=\"" + elementNames[i-1] + "\" style=\"width:" + objTR.cells[i].width + "\" "; 
  //装入默认值
  if(colDefaultValues[i-1]=="j") //j代表该列不能装入默认值
   str += "value=\"" + objTR.cells[i].innerText + "\"";
  else//否则装入默认值
   str += "value=\"" + colDefaultValues[i-1] + "\"";
  //控制控件的读写属性
  if(colPropertys[i] == 0)
  {
   str += " disabled ";
  }
  str += "/>";
  objTR.cells[i].innerHTML = str;
 }

 objTR.ondblclick=doNothing;  //使行保持原始状态
}

//把行恢复为非输入状态
function ResetTR(objTR)
{
 var str = new String("");
 var i = 0;
 for(i=0;i<objTR.cells.length;i++)
 {
  var objChild;
  var tmpStr = "";
  tmpStr = objTR.cells[i].innerHTML;

  //里面包含控件
  if(objTR.cells[i].firstChild!=undefined && objTR.cells[i].firstChild.value!=undefined)
  {
   tmpStr = objTR.cells[i].firstChild.value;
   if(tmpStr=="")
    tmpStr = " "
   objTR.cells[i].innerHTML = tmpStr + " ";
   continue;
  }
  
  //里面不包含控件
  if(tmpStr==" " || tmpStr=="")
  {
   if(objTR.cells[i].innerHTML!="")
    tmpStr += objTR.cells[i].innerHTML;
   else
    tmpStr += " ";
  }
  objTR.cells[i].innerHTML = tmpStr;

  if(objTR.cells[i].innerHTML=="")
   objTR.cells[i].innerHTML=" ";
 }
}

//对表格指定列进行排序
function orderTB(objTB,index,type){
 for(var i=1;i<(objTB.rows.length-1);i++){
  for(var j=i+1;j<objTB.rows.length;j++){
   var tmp1,tmp2;
   
   if(objTB.rows[j].cells[index].firstChild.value==undefined)
    tmp1 = objTB.rows[j].cells[index].innerText;
   else
    tmp1 = objTB.rows[j].cells[index].firstChild.value;
   
   if(objTB.rows[i].cells[index].firstChild.value==undefined)
    tmp2 = objTB.rows[i].cells[index].innerText;
   else
    tmp2 = objTB.rows[i].cells[index].firstChild.value;
    if(tmp1>tmp2)
    {
     objTB.moveRow(j,i);
    }
  }
 }
}

function orderTb1(objTD,type)
{
 var objTR =objTD.parentElement;
 var objTable = objTR.parentElement;
 var colIndex = objTD.cellIndex;
 orderTB(objTable,colIndex); 
}

//在表格末尾增加一行
function InsertRow(tableId)
{
 var objTable = document.getElementById(tableId);
 var col = objTable.cells.length/objTable.rows.length;
 var row = objTable.rows.length;
 var objRow = objTable.insertRow();  //增加行
 var i = 0;
 //alert(objTable);
 //取得最大序号
 var max1 = 1;
 for(i=1;i<row-1;i++)
 {
  var tmpMax = 1;
  if(parseFloat(objTable.rows[i].cells[0].innerText) > parseFloat(objTable.rows[i+1].cells[0].innerText))
  {
   tmpMax = parseFloat(objTable.rows[i].cells[0].innerText);
  }
  else
  {
   tmpMax = parseFloat(objTable.rows[i+1].cells[0].innerText);
  }
  if(tmpMax>=max1)
   max1= tmpMax;
 }
 for(i=1;i<=col;i++)
 { 
  var objCell = objRow.insertCell();
  objRow.cells[0].innerText = parseFloat(max1)+1;
  objCell.innerHTML = " ";
  objCell.onmouseover = Td_MouseOver_Handle;
  objCell.onmouseout = Td_MouseOut_Handle
  objRow.cells[0].onclick = TD_Click_Handle;
  objRow.cells[0].onmouseover = doNothing;
  objRow.cells[0].onmouseout = doNothing;
 }
 //为新增的行提供事件绑定支持
 //objRow.attachEvent('ondblclick', dblClick_Handle);
 objRow.ondblclick = dblClick_Handle;

objRow.onblur = click_Handle;
}

//删除最后1行
function DeleteRow(tableId)
{
 var objTable = document.getElementById(tableId);
 if(objTable.rows.length==1)
 {
  alert("对不起，你不能删除表格头!!!");
  return;
 }
 if(confirm("确定删除?"))
 {
  objTable.deleteRow();
 }
}

//选定指定行
function SelectRow(objTD)
{
 var objTR =objTD.parentElement;
 var objTable = objTR.parentElement;
 
 for(var i=1;i<objTable.rows.length;i++)
  objTable.rows[i].cells[0].bgColor = "#ffffff";
  
 objTD.bgColor="#ff0000";
 currRowIndex = objTR.rowIndex;
 if(confirm("确定删除?"))
 {
    
  objTable.deleteRow(currRowIndex);
  currRowIndex = 0;
 }
}

//删除指定的行
function DeleteOneRow(tableId,rowIndex)
{
 var objTable = document.getElementById(tableId);
 var objTable = document.getElementById(tableId);
 if(objTable.rows.length==1 || rowIndex==0)
 {
  alert("对不起，你首先必须选择要删除的行！！！");
  return;
 }
 if(confirm("确定删除?"))
 {
  objTable.deleteRow(rowIndex);
  currRowIndex = 0;
 }
 else
 {
  for(var i=1;i<objTable.rows.length;i++)
  {
   objTable.rows[i].cells[0].bgColor = "#ffffff";
   currRowIndex = 0;
  }
 }
}

//鼠标选择指定列
function  TD_Click_Handle()
{
 SelectRow(this)
}

//鼠标移入事件支持
function Td_MouseOver_Handle()
{
 this.bgColor = "#E0E0E0";
}

//鼠标移出事件支持
function Td_MouseOut_Handle()
{
 this.bgColor = "#ffffff";
}

//鼠标双击事件支持
function dblClick_Handle()
{
 ChangeInput(this);// 切换到输入状态
 this.ondblclick=doNothing;
}

//鼠标单击事件支持
function click_Handle()
{
 ResetTR(this);
}
//保持行的原始状态
function doNothing()
{
 return;
}

function showTip(msg)
{

with (oPopup.document.body)
{
 style.backgroundColor="lightyellow";
 style.border="solid black 1px";
 style.fontSize = 12;
 innerHTML=msg;
}
oPopup.show(event.x, event.y, 95, 16, document.body);
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

function addTable(tab,st){

 document.getElementById("putong").style.display="none";
	var result=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/survey/setTable.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:320px');
   if(result!=null && result!='undefined'){
    	var a=result.split(',');	
		var col=a[0];
	    headers=new Array();
        headers.push(a[1]);
	   for(var i=col;i>=1;i--){
		  headers.push(a[i]);	 
		}
     CreateTable('specForm','1','','tableId',col,row);
    }
}

//====================================================================================================================
//submit 方法
function getData(tableId)
{
 var objTable = document.getElementById(tableId);
 var cols = objTable.cells.length/objTable.rows.length-1;
 var rows = objTable.rows.length-1;
 
 //为每个隐藏域设置值
 var i=0,j=0;
 var str='';
 
 for(i=1;i<=rows;i++)
 {
  for(j=1;j<=cols;j++)
  {
   if(objTable.rows[i].cells[j].firstChild.value==undefined)
   {
  
    if(objTable.rows[i].cells[j].innerText=='' || objTable.rows[i].cells[j].innerText==null){
          objTable.rows[i].cells[j].innerText='0';
    }
     str+=trim(objTable.rows[i].cells[j].innerText);
   }
   else{
 
  
    if(objTable.rows[i].cells[j].firstChild.value=='' || objTable.rows[i].cells[j].firstChild.value==null){
          objTable.rows[i].cells[j].firstChild.value='0';
    }
     str+=trim(objTable.rows[i].cells[j].firstChild.value);
   
     }
      str +=  ",";

  } 
    str +=  ";";
    var titles=headers;
   // alert(titles);chbo rado
 }
   var options;
 if(document.getElementById('chbo').checked==true){
 		options=document.getElementById('chbo').value;
 }
 if(document.getElementById('rado').checked==true){
 		options=document.getElementById('rado').value;
 }
   var topTitles=document.getElementById('topTitles').value;
  
    url = "<%=path%>/survey/surveyVote!saveTable.action";
	queryString ="questionStr="+str+"&titles="+titles+"&options="+options+"&topTitles="+topTitles;

 
  
   new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							var blockid = resp.responseText;
							  if(blockid=='111'){
							  	alert('设置成功！');
							  }
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


function updateQusSort(str)
{
    url = "<%=path%>/survey/survey!updateQusSort.action";    
	par ="surveyQusSort="+str;
	sendByAjax(url,par);
}


//增加元素
function addBlock(blocktype,blocktitle){
	  document.getElementById("tables").style.display="none";
	 url = "<%=path%>/survey/surveyVote!save.action";
	queryString = "questionType="+blocktype+"&questionName="+blocktitle+"&questionTrue=false";

	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							var blockid = resp.responseText;
					
							addDragDiv(blockid,0,0,0,'gray','',blocktitle,blocktype);
							if("textfield"==blocktype||"textarea"==blocktype)
							{
							   getSortId(blockid,blocktype);
							}
						    upSortId();
							
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

function addDragDiv(blockid,col,pos,isnotinit,blocktpl,imgurl,blocktitle,blocktype){

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
	mvAry[mvAry.length]  ='			<input type="hidden" name="blocktypevalue_'+blockid+'" id="blocktypevalue_'+blockid+'" value="'+blocktype+'">';
	mvAry[mvAry.length]  ='			<tr>';
	mvAry[mvAry.length]  ='				<td style="font-weight:bold;padding:2px">';
	mvAry[mvAry.length]  ='				<span id="drag_text_'+blockid+'"  onclick="editAnswer('+blockid+')">'+blocktitle+'</span>';
	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='				<td align="left" style="padding-left:20px;padding-right:20px;">';
	if(blocktype!='textfield'&&blocktype!='textarea'){
	mvAry[mvAry.length]  ='					<img src="<%=path%>/oa/image/survey/add_vote.gif" class="imglinkgray" onclick="getSortId('+blockid+',\''+blocktype+'\')" title="添加" style="display:"> ';
	}
	mvAry[mvAry.length]  ='<img src="<%=path%>/oa/image/survey/edit.gif" class="imglinkgray"  onclick="editAnswer('+blockid+')" title="编辑" style="display:"> <img src="<%=path%>/oa/image/survey/delete_s.gif" class="imglinkgray" title="删除" onclick="delDragDiv('+blockid+')" style="display:"><input type="checkbox" id="req_'+blockid+'" class="checkbox" title="是否必填、选中表示必填！" onclick="setRequire('+blockid+')">';
	mvAry[mvAry.length]  ='				</td>';
	mvAry[mvAry.length]  ='				<td>';
	mvAry[mvAry.length]  ='					<div id="drag_'+blockid+'_h" class="drag_header" style="width:100%;height:25px"><img src="<%=path%>/oa/image/survey/move.gif"></div>';
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
	
	if(typeof(isnotinit) == "undefined" || isnotinit == 0){	 
		initDrag();
	}

	loadDragContent(blockid,blocktype);
    
}

//删除元素
function delDragDiv(blockid){
	var logicv = window.confirm("是否删除？");
	if(logicv){
		var rid = document.getElementById("drag_"+blockid);
		rid.parentNode.removeChild(rid); 
		url = "<%=path%>/survey/surveyVote!delete.action";
		queryString = "questionNumber="+blockid;
		new Ajax.Request(url,{method: "post",parameters : queryString});
         upSortId();
	}
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

	url = "<%=path%>/survey/surveyAnswer!init.action";
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



function addElement(this_id,this_type,sort_id){
  var oNewNode = document.createElement("DIV");
  var nowNode = document.getElementById("drag_content_"+this_id);
  var div_id = this_id+'_'+sort_id;
  oNewNode.setAttribute("id",''+div_id+'');
  if(this_type!='textarea'&&this_type!='textfield'){
	 var data_str = this_id+"||"+sort_id+"||新增选项||N||Y";
	 var str ="新增选项";     
  }else{
	 var data_str ="";	
  }
  sendAddData(data_str,this_id,div_id,str,sort_id)
  nowNode.appendChild(oNewNode);
  if(this_type=='radio'||this_type=='checkbox'){
	var in_content = "<table><tr><td><input class="+this_type+" name="+this_type+"_"+this_id+" type="+this_type+">&nbsp;<span id='title_"+div_id+"' onClick=\"editTitle('"+div_id+"','"+data_str+"')\">新增选项</span></td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editTitle('"+div_id+"','"+data_str+"')\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delQuestion('"+div_id+"')\"></td></tr></table>";
  }
  if(this_type=='select'){
	var in_content = "<table><tr><td>--<span id='title_"+div_id+"' onClick=\"editTitle('"+div_id+"','"+data_str+"')\">新增选项</span>--</td><td style='padding-left:20px'><img src='<%=path%>/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editTitle('"+div_id+"','"+data_str+"')\" title='编辑'> <img src='<%=path%>/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'  onClick=\"delQuestion('"+div_id+"')\"></td></tr></table>";
  }
  
  if(this_type=='textfield'||this_type=='textarea'){
	var in_content ="";
  }

  
   oNewNode.innerHTML = in_content;
}


function getSortId(this_id,this_type){

    url = "<%=path%>/survey/surveyAnswer!getSortid.action";
	queryString = "answerQueNumber="+this_id;

	new Ajax.Request
	(
		url,
		{
			method: "post",	
			onSuccess : function(resp)
						{
							 sort_id = resp.responseText;	
							 addElement(this_id,this_type,sort_id);						
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


function sendTableData(data_str,this_id,div_id,str,sort_id){

alert(data_str);
alert(this_id);
alert(str);
	url = "<%=path%>/survey/surveyAnswer!saveAnswers.action";    
	par = "answerValue="+data_str+"&answerNumber="+div_id+"&answerQueNumber="+this_id+"&answerName="+str+"&answerSortid="+sort_id;
	sendByAjax(url,par);
}


function sendAddData(data_str,this_id,div_id,str,sort_id){

	url = "<%=path%>/survey/surveyAnswer!save.action";    
	par = "answerValue="+data_str+"&answerNumber="+div_id+"&answerQueNumber="+this_id+"&answerName="+str+"&answerSortid="+sort_id;
	sendByAjax(url,par);
}

function sendTopStr(top_str,this_id){
    url = "<%=path%>/survey/surveyVote!update.action";
    par = "questionName="+top_str+"&questionNumber="+this_id;
	sendByAjax(url,par);
}
function sendChangeData(new_str,str,this_id){

    url = "<%=path%>/survey/surveyAnswer!update.action";
	par = "answerValue="+new_str+"&answerNumber="+this_id+"&answerName="+str;
	sendByAjax(url,par);
}


function delQuestion(index_id){
	var logicv = window.confirm("是否删除？");
	if(logicv){
		var rid = document.getElementById(index_id);
		rid.parentNode.removeChild(rid); 
		url = "<%=path%>/survey/surveyAnswer!delete.action";
		par = "answerNumber="+index_id;
		sendByAjax(url,par);
	}
}
var curObj = null;
function editTitle(this_id,old_str){
 var obj = document.getElementById('title_'+this_id);
  if (curObj == null){
    curObj = obj;
	  var inputleng = eval(obj.innerText.replace(/[^\x00-\xff]/g,"**").length);
	  var text_content = "<input type='text' id='ed_"+this_id+"' size="+inputleng+" value='"+obj.innerText+"' onblur=saveChange(this,'"+this_id+"','"+old_str+"');>";
	  obj.innerHTML = text_content;
	  document.getElementById("ed_"+this_id).focus(); 
	   obj.firstChild.select();
  }
}
function saveChange(obj,this_id,old_str){
    var str =  obj.value;
	if(str==''){
		alert("您编辑的内容为空不能保存！请检查！");
		return false;
	}
	if(str.length>128){
		alert("您编辑的内容太长不能保存！请检查！");
		return false;
	}
	curObj.innerHTML = str;
	var arr_str = old_str.split('||');
	var new_str = old_str.replace(arr_str[2],str); 
	sendChangeData(new_str,str,this_id);
	curObj = null;
}
var curObj_an = null;
function editAnswer(qid){
	 var obj = document.getElementById('drag_text_'+qid);
  if (curObj_an == null){
    curObj_an = obj;
	  var inputleng = eval(obj.innerText.replace(/[^\x00-\xff]/g,"**").length);
	  var text_content = "<input type='text' id='ed_"+qid+"' size="+inputleng+" value=\""+obj.innerText+"\" onblur=saveTopChange(this,'"+qid+"');>";
	  obj.innerHTML = text_content;
	  document.getElementById("ed_"+qid).focus(); 
	   obj.firstChild.select();
  }	
}
function saveTopChange(obj,this_id){
	var str =  obj.value;
	if(str==''){
		alert("您编辑的内容为空不能保存！请检查！");
		return false;
	}
	if(str.length>128){
		alert("您编辑的内容太长不能保存！请检查！");
		return false;
	}
	
	curObj_an.innerHTML = str;
	sendTopStr(str,this_id);
	curObj_an = null;
}
function setRequire(qid){
	var state_str = document.getElementById('req_'+qid).checked;
	if(state_str==true){
		var required ='true';
	}else{
		var required ='false';
	}
	url = "<%=path%>/survey/surveyVote!updateQuTrue.action";
	par = "&answerNumber="+qid+"&questionTrue="+required;
	sendByAjax(url,par)
}
function showTitle(){
   var xx=document.getElementById("col_1").innerText;
   if(xx!=null && xx !=" "){
       document.getElementById("tables").style.display="none";
   }
}

</SCRIPT>
</HEAD>

<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="showTitle();" >
<div id="contentborder" align="center">

<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0>

  <TBODY>
  <TR>
   	<td>&nbsp;</td>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp; 设置问卷内容</TD></TR>
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
     
       
<TABLE width="100%">
  <TBODY>
  <TR height="100%">
  
    <TD vAlign=top width=200 height="100%">
    
						<DIV
							style="PADDING-RIGHT: 5px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
							
							<DIV style="COLOR: red">
								点击'+'添加元素
							</DIV>
						<div id="putong">
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/checkbox.gif" align=absMiddle>
								&nbsp;复选框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('checkbox','新建问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/radio.gif"
									align=absMiddle>
								&nbsp;单选框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('radio','新建问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/select.gif"
									align=absMiddle>
								&nbsp;下拉菜单&nbsp;
								<IMG class=paneladdimg onclick="addBlock('select','新建问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon src="<%=path%>/oa/image/survey/text.gif"
									align=absMiddle>
								&nbsp;文本框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('textfield','新建问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
							<BR>
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/textarea.gif" align=absMiddle>
								&nbsp;多行文本框&nbsp;
								<IMG class=paneladdimg onclick="addBlock('textarea','新建问题元素');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
			                 </DIV>
							<BR>
							
				</div>
						<div id='tables'>
							<DIV class=panelcon>
								<IMG class=panelicon
									src="<%=path%>/oa/image/survey/textarea.gif" align=absMiddle>
								&nbsp;表 格&nbsp;
								<IMG class=paneladdimg onclick="addTable('table','新建表格');"
									src="<%=path%>/oa/image/survey/add2.gif" align=absMiddle>
							</DIV>
						</div>
						<br>
					<div id="col_title_div" ></div> 
					<div id="col_1__div"></div>
						</DIV>
							 
    </TD>
    
     
   <td valign='top' class='tablecol1'  >
   
			<div id="col_1" class="col_div" style="height:380;width:1000;">
			                  
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
    <webflex:flexTable name="myTable"  property="questionId"   getValueType="getValueByProperty"  isCanSplit="false"
      collection="${page.result}" page="${page}">
		<webflex:flexTextCol  property="questionName" showValue="javascript:addDragDiv(questionNumber,0,0,0,gray,null,questionName,questionType)" ></webflex:flexTextCol>
		<webflex:flexTextCol  property="questionTrue" showValue="questionTrue" ></webflex:flexTextCol>
	    <webflex:flexTextCol   property="questionType" showValue="questionType" ></webflex:flexTextCol>
	    <webflex:flexTextCol  property="questionNumber" showValue="questionNumber" ></webflex:flexTextCol>
	</webflex:flexTable>

</div>

</div>
</BODY>
</HTML>
  



