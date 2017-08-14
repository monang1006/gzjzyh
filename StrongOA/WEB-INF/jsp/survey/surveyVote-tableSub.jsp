<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.sql.*"pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
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

<% 

   String titles=(String)request.getAttribute("titles");
   String queName=(String)request.getAttribute("queName");
   String options=(String)request.getAttribute("options");
      String answNumber=(String)request.getAttribute("answNumber");

%>

<SCRIPT>
function createNew(){
   var titles=document.getElementById("titles").value;
   var queName=document.getElementById("queName").value;
      var answNumber=document.getElementById("answNumber").value;
   var head=titles.split(',');
   var que=queName.split(';');
    var answer=answNumber.split(',');
  
   //alert(que);
   col=head.length-1;
   row=que.length-1;
   
      for(var k=0;k<row;k++){
        answers.push(answer[k]);   
   }
     
       
   for(var i=0;i<head.length-1;i++){
	  headers.push(head[i]);	 
	}
	
    var names=new Array();
   for(var e=0;e<que.length-1;e++){
	   names.push(que[e]);
	}
	
	var xx=names.toString();
	var yy=xx.split(',');
	
   for(var i=0;i<yy.length;i++){
      if(yy[i]!=''&& yy[i]!=null){
       dataArray.push(yy[i]);
      }
   }
   
   var options=document.getElementById("options").value;
   checkType=options;
 CreateTable('specForm','1','','tableId',col,row);
}

/*
 *====================================================================================================================
*/


//定义全局的表格行列值
var col =3;
var row = 3;
var titles='';
var checkType='radio';

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

 var answers = new Array();


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
var dataArray = new Array();
var m=0,n=0;
for(m=0;m<row;m++)
{
 for(n=0;n<col;n++)
 {
 // dataArray[m*col + n] ="array(" + m + "," + n + ")";  //注意这里代表2维数组
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
   document.getElementById("tableTitle").innerHTML=startStr + colStr + endStr;
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
  colStr += "<td width='5%' onClick=orderTb1(this,1); onMouseOver=\"showTip('单击对本列排序');\" onMouseOut=\"hideTip();\"><center>"
  colStr += "序号";
  colStr += "</center></td>";
  
  colStr += "<td width='5%'><center>"
  colStr += "选择";
  colStr += "</center></td>";
 for(i=1;i<=colNum;i++)
 {
  colStr += "<td align=center width='28%' onClick=orderTb1(this,1); onMouseOver=\"showTip('单击对本列排序');\" onMouseOut=\"hideTip();\" >";
  colStr += headers[i-1];
  colStr += "</td>";
 }

 //创建表体行
 for(i=0;i<rowNum;i++)
 {
  colStr += "<tr ";
  colStr += " ";  //表格行中的事件支持部分
  colStr += "onBlur=\"ResetTR(this);\" >";
  
  //创建每行的所有单元格
   colStr += "<td  onClick=SelectRow(this);>";
   colStr += i+1;
   colStr +="</td>";
   
   colStr += "<td align='center' width='5%'>";
   colStr += "<input type=\"" + checkType +"\" name='astate' id='astate"+i+"' value=\""+answers[i]+"\" style='WIDTH: 25px; font-size:15pt; color:#00000F'/>&nbsp;";
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
 // if(colDefaultValues[i-1]=="j") //j代表该列不能装入默认值
   str += "value=\"" + objTR.cells[i].innerText + "\"";
//  else//否则装入默认值
 //  str += "value=\"" + colDefaultValues[i-1] + "\"";
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



//选定指定行
function SelectRow(objTD)
{
 var objTR =objTD.parentElement;
 var objTable = objTR.parentElement;
 
 for(var i=1;i<objTable.rows.length;i++)
  objTable.rows[i].cells[0].bgColor = "#ffffff";
  
 objTD.bgColor="#ff0000";
 currRowIndex = objTR.rowIndex;

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
// ChangeInput(this);// 切换到输入状态
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

//提交投票
function sub_mit()
{     
    var str_id="";
    var survye_id="";
       var va = document.getElementsByName('astate');
       for(var k=0;k<va.length;k++)
       {
          
          if(va[k].checked)
          {         
            str_id+=va[k].value+",";
          }
         
       }
    
   
     if(""==str_id)
     {
       alert("请选择答案!");
        return false;
     }
    str_id= str_id.substr(0,str_id.length-1);
    survye_id=document.getElementById("sruveyId").value;

    window.location ="<%=root %>/survey/surveyAnswer!submitTable.action?answerNumber="+str_id+"&surveyId="+survye_id;

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
						     	objDivID.innerHTML="<input id='s_u'  type='submit' class='input_bg'  value='提 交' disabled='disabled' onclick='' >";						     
						     }else{
						     	objDivID.innerHTML="<input id='s_u'  type='submit' class='input_bg'  value='提 交' disabled='disabled' onclick='' > <input name='button' type='button' class='input_bg' value='查看结果' onclick='surveySee()'>";
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

function surveySee(){//查看投票结果
    survye_id=document.getElementById("sruveyId").value;
	var audit= window.showModalDialog("<%=path%>/survey/surveyVote!view.action?viewType=see&surveyId="+survye_id,window,'help:no;status:no;scroll:yes;dialogWidth:860px; dialogHeight:680px');
}

//====================================================================================================================

</SCRIPT>
</HEAD>
<base target="_self"/>

<BODY class=contentbodymargin oncontextmenu="return false;" >
<DIV id=contentborder align=center>

<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0>

  <TBODY>
  <TR>
  <td>&nbsp;</td>
    <TD class=pagehead1><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp; 接受调查</TD></TR>
  <TR>
    <TD class=pagehead2>
<input type="hidden" id="titles" name="title" value="<%=titles %>"/>
<input type="hidden" id="queName" name="queNam" value="<%=queName %>"/>
<input type="hidden" id="options" name="options" value="<%=options %>"/>
<input type="hidden" id="answNumber" name="answNumber" value="<%=answNumber %>"/>
 <input type="hidden" id="sruveyId" value=${surveyId}>
    <input type="hidden" id="isPublic" value=${isPublic}>

     ${topHtml}
       
<TABLE width="100%">
  <TBODY>
  <TR height="100%">
 
   <td valign='top' align="center" class='tablecol1' >
			   <div id="tableTitle" style="overFlow-x: scroll;">
   
                     </div>
                     <table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="30%">
									<div id="sub_mit"><input id=s_u  type="button" class="input_bg"  value="提 交" onclick="sub_mit();">
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
	</td>	

</TR>
  </TBODY>
</TABLE>      
${bottomHtml}

</DIV>
<script type="text/javascript">
			window.onload = function (){
			   createNew();
				init();
			}
		</script>
</BODY>
</HTML>
  

