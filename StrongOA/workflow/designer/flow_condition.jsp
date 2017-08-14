<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<HTML>
<HEAD>
<TITLE> 模型文件编辑 </TITLE>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="js/webTab/webtab.css">
<script language=jscript src="js/webTab/webTab.js"></script>

<style>
body {
	background-color: buttonface;
	scroll: no;
	margin: 7px, 0px, 0px, 7px;
	border: none;
	overflow: hidden;	

}
</style>

<SCRIPT LANGUAGE="JavaScript">
<!--
function iniWindow(){
   try{
	   iniAttributeDialog();
   }catch(e){
     alert('打开编辑对话框时出错！!');
	 window.close();
   }   
}
function iniAttributeDialog(){
   var opener = window.dialogArguments;
   document.getElementById("xmlvalue").innerText=opener.root.xml.replace(/></g,">\n<");
}

function checkElement(str){
   	var opener = window.dialogArguments;
	if(str!=""){
		var strLength=str.length;
		var tempLength=0;
		for(;tempLength<strLength;tempLength++){
			if(str.charAt(tempLength)=='<' && str.charAt(++tempLength)!='/'){
				var i=tempLength;
				tempLength++;
				while(str.charAt(tempLength)!=' ' && str.charAt(tempLength)!='>')
					tempLength++;
				var temp=str.substring(i,tempLength);
				if(opener.testArray[temp]!="1"){
					alert(temp);
					alert("模型元素错误,请检查!");
					return false;
				}								
			}
			if(str.charAt(tempLength)=='<' && str.charAt(++tempLength)=='/'){
				var i=tempLength;
				tempLength++;
				while(str.charAt(tempLength)!='>')
					tempLength++;
				var temp=str.substring(i,tempLength);
				if(opener.testArray[temp]!="1"){
					alert(temp);
					alert("模型元素错误,请检查!");
					return false;
				}								
			}			
		}
		return true;
	}
}

function okOnClick(){
   var opener = window.dialogArguments;
   try{
		if(!checkElement(document.getElementById("xmlvalue").innerText))
			return false;		
		var tempdoc=new ActiveXObject("Msxml2.DOMDocument");
		tempdoc.loadXML(document.getElementById("xmlvalue").innerText);
		var nParseError = tempdoc.parseError.errorCode ;
		if(0!=nParseError){
			alert("模型XML文档结构错误!");
			return false;
		}
		var namearray=new Array();
		var warray=new Array();
		var linearray=new Array();
		var temproot=tempdoc.getElementsByTagName("process-definition")[0];
		for(var i=0;i<temproot.childNodes.length;i++){
			var node=temproot.childNodes[i];
//			if(node.getAttributeNode("name")==null || node.getAttributeNode("name").value==""){
//				alert("模型错误!请为每个节点设置\"name\"属性!");
//				return false;
//			}
			if(namearray[node.getAttributeNode("name").value]!=undefined){
				alert("模型错误!"+node.getAttributeNode("name").value+"节点名称与其它节点相同!");
				return false;
			}else{
				namearray[node.getAttributeNode("name").value]=i;
			}
			if(node.getAttributeNode("atnode")!=null){
				if(warray[node.getAttributeNode("atnode").value]!=undefined){
					alert("模型错误!"+node.getAttributeNode("name").value+"节点\"atnode\"属性与其它节点相同!");
					return false;
				}else{
					warray[node.getAttributeNode("atnode").value]=i;
				}
			}
		}
		var lines=tempdoc.getElementsByTagName("transition");
		for(var i=0;i<lines.length;i++){
			if(lines[i].getAttributeNode("atnode")!=null){
				if(warray[lines[i].getAttributeNode("atnode").value]!=undefined){
					alert("模型错误!连接\"atnode\"属性与其它元素相同!");
					return false;
				}else{
					warray[lines[i].getAttributeNode("atnode").value]=i;
				}
			}
			if(lines[i].getAttributeNode("to")==null){
				alert("模型错误!请为每个连接设置\"to\"属性!");
				return false;
			}else if(namearray[lines[i].getAttributeNode("to").value]==undefined){
				alert("模型错误!请确认每个连接设置的\"to\"属性指向的节点存在!");
				return false;
			}else if(linearray[lines[i].parentNode.getAttributeNode("name").value+lines[i].getAttributeNode("to").value]!=undefined){
				alert("模型错误!请确认两个节点间没有重复连接存在!");
				return false;				
			}else{
				linearray[lines[i].parentNode.getAttributeNode("name").value+lines[i].getAttributeNode("to").value]=i;
			}
		}
		temproot=null;
		tempdoc=null;
		//var addStr=document.getElementById("xmlvalue").innerText.replace(/>[^<]+</g,"><");
		var addStr=document.getElementById("xmlvalue").innerText.replace(/>[\r|\n|\s]+</g,"><");
		opener.repaintElements(addStr,0);
		window.close();
   }catch(e){
     alert('关闭编辑对话框时出错！');
	 window.close();
   }   
}
function cancelOnClick(){
   window.close();
}

//-->
</SCRIPT> 

</HEAD>

<BODY onload='iniWindow()'>
<table border="0" cellpadding="0" cellspacing="0" height=385px>
<thead>
  <tr id="WebTab">
	<td class="selectedtab" id="tab1" onmouseover='hoverTab("tab1")' onclick="switchTab('tab1','contents1');"><span id=tabpage1>模型文件</span></td>
	
  </tr>
</thead>
<tbody>
  <tr>
	<td id="contentscell" colspan="5">
<!-- Tab Page 1 Content Begin -->
<div class="selectedcontents" id="contents1">
<TABLE border=0 width="100%" height="100%">
<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage1_1>模型XML</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><TEXTAREA cols=73 rows=24 id="xmlvalue" readonly="readonly"></TEXTAREA></TD>
		<TD></TD>
	</TR>
	<TR height="3">
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
	</TABLE>
	</Fieldset>
	</TD>
	<TD>&nbsp;</TD>
</TR>

<TR height="100%">
	<TD></TD><TD></TD><TD></TD>
</TR>
</TABLE>
</div>
<!-- Tab Page 1 Content End -->

	</td>
  </tr>
</tbody>
</table>

<table cellspacing="1" cellpadding="0" border="0" style="position: absolute; top: auto; left: 0px;">
	<tr height="10"><td colspan=3></td></tr>
	<tr>
		<td width="100%"></td>
		<td><input type=button id="btnOk" class=btn value="关 闭" onclick="jscript: cancelOnClick();">&nbsp;&nbsp;&nbsp;</td>
	</tr>
</table>
</BODY>
</HTML>
