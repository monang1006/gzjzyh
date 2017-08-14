<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<HTML>
<HEAD>
<TITLE> 流程设置 </TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="<%=path%>/workflow/designer/css/style.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/workflow/designer/js/webTab/webtab.css">
<script language=jscript src="<%=path%>/workflow/designer/js/webTab/webTab.js"></script>

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

	   var opener = window.dialogArguments;
//	   var tempOwner = "";
//	   var tempDesigner = "";
//	   var tempStartor = "";
	   
       function  ok(){
/**       	  if(document.getElementsByName("processName")[0].value==null || ""==document.getElementsByName("processName")[0].value){
       	  	alert("流程名称不能为空!")
       	  	return false;
       	  }
**/
       	  opener.processType = document.getElementsByName("processType")[0].value;
//       	  opener.processName = document.getElementsByName("processName")[0].value;
       	  opener.root.getAttributeNode("name").value = document.getElementsByName("processName")[0].value;
       	  var processform = document.getElementsByName("processForm")[0].value;
       	  //启动表单节点更改
       	  if(processform != opener.processForm){
       	  	opener.processForm = processform;
       	  	for(var i = 0; i < opener.sltObjArray.options.length; i++) {
       	  	  var obj = opener.document.getElementById(opener.sltObjArray.options[i].value);
       	  	  if(obj.processForm == "流程启动表单,0" && obj.formPriv != "superadmin"){
       	  	  	obj.formPriv = "superadmin";
       	  	  }
       	  	}
       	  }
/**       	  
       	  if(tempOwner != ""){
       	  	opener.owner = tempOwner;
       	  }
       	  
       	  if(tempDesigner != ""){
       	  	opener.designer = tempDesigner;
       	  }
       	  
       	  if(tempStartor != ""){
       	  	opener.startor = tempStartor;
       	  }
**/       	  
       	  var timerSet = document.getElementsByName("doc_timer_data")[0].value + "," +  	  
						 document.getElementsByName("doc_timer_init")[0].value + "," +
						 document.getElementsByName("pre_timer_data")[0].value + "," +
						 document.getElementsByName("pre_timer_init")[0].value + "," +
						 document.getElementsByName("re_timer_data")[0].value + "," +
						 document.getElementsByName("re_timer_init")[0].value + ",";	
		  if(document.getElementsByName("mail_timer")[0].checked == true){
			timerSet = timerSet + "1" + ",";		
		  }else{
			timerSet = timerSet + "0" + ",";		
		  }
		  if(document.getElementsByName("notice_timer")[0].checked == true){
			timerSet = timerSet + "1" + ",";
		  }else{
			timerSet = timerSet + "0" + ",";	
		  }
		  if(document.getElementsByName("mes_timer")[0].checked == true){
		 	timerSet = timerSet + "1" + ",";	
		  }else{
			timerSet = timerSet + "0" + ",";
		  }
		  if(document.getElementsByName("handler_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";	
		  }else{
			timerSet = timerSet + "0" + ",";	
		  }
		  if(document.getElementsByName("start_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";		
		  }else{
			timerSet = timerSet + "0" + ",";		
		  }
		  if(document.getElementsByName("owner_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";		
		  }else{
			timerSet = timerSet + "0" + ",";		
		  }
		  
		  opener.doc_timerSet = timerSet;
		         	  
       	  if(document.getElementsByName("doc_isTimer")[0].checked == true){
       	  	opener.doc_isTimer = "1";
       	  }else{
       	  	opener.doc_isTimer = "0";
       	  }
       	  window.returnValue = "true";
       	  window.close();
       }
       
       function cancel(){
       	  window.close();
       }
/**       
		//指定人员
		function setTaskActors(flag){
			
			var vPageLink = scriptroot + "/workflow/workflow_processowner_select.jsp?pageType=" + flag;
			var returnValue = OpenWindow(vPageLink,400,450,window);
			if(returnValue != null && returnValue != ""){
			
				var returnValues = returnValue.split("|");
				
				var person = returnValues[2];
				var post = returnValues[1];
				var org = returnValues[0];
				
				var selectObj;
				if(flag == "owner"){
					tempOwner = returnValue;
					selectObj = document.getElementById("owner");
				}else if(flag == "designer"){
					tempDesigner = returnValue;
					selectObj = document.getElementById("designer");
				}else if(flag == "startor"){
					tempStartor = returnValue;
					selectObj = document.getElementById("startor");
				}
				
				while(selectObj.options.length > 0){
					selectObj.options[0] = null;
				}
				
				for(var i = 0; i < returnValues.length; i++){
					var name = returnValues[i].split(",")[1];
					var op = new Option(name, name);
					selectObj.options.add(op, selectObj.options.length);
				}
			}	
		}       
**/
function initWindow(){

//	tempOwner = opener.owner;
//	tempDesigner = opener.designer;
//	tempStartor = opener.startor;
/**
	var ownerSelect = document.getElementById("owner");
	var startorSelect = document.getElementById("startor");
	var designerSelect = document.getElementById("designer");
	if(opener.startor != ""){
		var startorDetails;
		var startors = opener.startor.split("|");
		for(var i=0; i<startors.length; i++){
			startorDetails = startors[i].split(",");
			var flag = startorDetails[0].substring(0,1);
			if(flag == "o"){
				var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
		      	startorSelect.options.add(objOption, startorSelect.options.length);	
			}else if(flag == "p"){
				var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
		      	startorSelect.options.add(objOption, startorSelect.options.length);					
			}else if(flag == "u"){
				var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
		      	startorSelect.options.add(objOption, startorSelect.options.length);					
			}
		}
	}    
	if(opener.designer != ""){
		var designerDetails;
		var designers = opener.designer.split("|");
		for(var i=0; i<designers.length; i++){
			designerDetails = designers[i].split(",");
			var flag = designerDetails[0].substring(0,1);
			if(flag == "o"){
				var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
		      	designerSelect.options.add(objOption, designerSelect.options.length);	
			}else if(flag == "p"){
				var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
		      	designerSelect.options.add(objOption, designerSelect.options.length);					
			}else if(flag == "u"){
				var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
		      	designerSelect.options.add(objOption, designerSelect.options.length);					
			}
		}		
	}
	if(opener.owner != ""){
		var ownerDetails;
		var owners = opener.owner.split("|");
		for(var i=0; i<owners.length; i++){
			ownerDetails = owners[i].split(",");
			var flag = ownerDetails[0].substring(0,1);
			if(flag == "o"){
				var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
		      	ownerSelect.options.add(objOption, ownerSelect.options.length);	
			}else if(flag == "p"){
				var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
		      	ownerSelect.options.add(objOption, ownerSelect.options.length);					
			}else if(flag == "u"){
				var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
		      	ownerSelect.options.add(objOption, ownerSelect.options.length);					
			}
		}		
	}
**/	
	var timerSet = opener.doc_timerSet.split(",");
		document.getElementsByName("doc_timer_data")[0].value = timerSet[0];		
		document.getElementsByName("doc_timer_init")[0].value = timerSet[1];		
		document.getElementsByName("pre_timer_data")[0].value = timerSet[2];		
		document.getElementsByName("pre_timer_init")[0].value = timerSet[3];			
		document.getElementsByName("re_timer_data")[0].value = timerSet[4];	
		document.getElementsByName("re_timer_init")[0].value = timerSet[5];
		if(timerSet[6] == '1'){
			document.getElementsByName("mail_timer")[0].checked = true;		
		}else{
			document.getElementsByName("mail_timer")[0].checked = false;		
		}
		if(timerSet[7] == '1'){
			document.getElementsByName("notice_timer")[0].checked = true;
		}else{
			document.getElementsByName("notice_timer")[0].checked = false;	
		}
		if(timerSet[8] == '1'){
			document.getElementsByName("mes_timer")[0].checked = true;		
		}else{
			document.getElementsByName("mes_timer")[0].checked = false;		
		}
		if(timerSet[9] == '1'){
			document.getElementsByName("handler_notice")[0].checked = true;		
		}else{
			document.getElementsByName("handler_notice")[0].checked = false;		
		}
		if(timerSet[10] == '1'){
			document.getElementsByName("start_notice")[0].checked = true;		
		}else{
			document.getElementsByName("start_notice")[0].checked = false;		
		}
		if(timerSet[11] == '1'){
			document.getElementsByName("owner_notice")[0].checked = true;		
		}else{
			document.getElementsByName("owner_notice")[0].checked = false;		
		}	
	if(opener.doc_isTimer == "1"){
		document.getElementsByName("doc_isTimer")[0].checked = true;
	}else{
		document.getElementsByName("doc_isTimer")[0].checked = false;
		setIsTimer();
	}
}

function setIsTimer(){
	if(document.getElementsByName("doc_isTimer")[0].checked == true){
		document.getElementsByName("doc_timer_data")[0].disabled = false;
		document.getElementsByName("doc_timer_init")[0].disabled = false;
		document.getElementsByName("pre_timer_data")[0].disabled = false;
		document.getElementsByName("pre_timer_init")[0].disabled = false;
		document.getElementsByName("re_timer_data")[0].disabled = false;
		document.getElementsByName("re_timer_init")[0].disabled = false;
		document.getElementsByName("mail_timer")[0].disabled = false;
		document.getElementsByName("notice_timer")[0].disabled = false;
		document.getElementsByName("mes_timer")[0].disabled = false;
		document.getElementsByName("owner_notice")[0].disabled = false;
		document.getElementsByName("start_notice")[0].disabled = false;
		document.getElementsByName("handler_notice")[0].disabled = false;
	}else{
		document.getElementsByName("doc_timer_data")[0].disabled = true;
		document.getElementsByName("doc_timer_init")[0].disabled = true;
		document.getElementsByName("pre_timer_data")[0].disabled = true;
		document.getElementsByName("pre_timer_init")[0].disabled = true;
		document.getElementsByName("re_timer_data")[0].disabled = true;
		document.getElementsByName("re_timer_init")[0].disabled = true;
		document.getElementsByName("mail_timer")[0].disabled = true;
		document.getElementsByName("notice_timer")[0].disabled = true;
		document.getElementsByName("mes_timer")[0].disabled = true;
		document.getElementsByName("owner_notice")[0].disabled = true;
		document.getElementsByName("start_notice")[0].disabled = true;
		document.getElementsByName("handler_notice")[0].disabled = true;		
	}	
}

</SCRIPT> 

</HEAD>

<BODY onload='initWindow()'>
<table border="0" cellpadding="0" cellspacing="0" height=385px>
<thead>
  <tr id="WebTab">
	<td class="selectedtab" id="tab1" onmouseout='outTab(this)' onmouseover='hoverTab("tab1")' onclick="switchTab('tab1','contents1');"><span id=tabpage1>基本设置</span></td>
	<td class="tab" id="tab3" onmouseout='outTab(this)' onmouseover='hoverTab("tab3")' onclick="switchTab('tab3','contents3');"><span id=tabpage3>时间设置</span></td>
	<td class="tabspacer" width=210>&nbsp;</td>	
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
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage1_1>基本设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD valign="middle"><span id=tabpage1_2>流程名称</span></TD>
		<TD><input type="text" name="processName" value="" disabled>
			<script>document.getElementsByName("processName")[0].value=opener.processName;</script>
		</TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD></TD>
		<TD valign="middle"><span id=tabpage1_3>流程类型</span></TD>
		<TD>
					<select name="processType">
						<c:forEach items="${types}" var="type">
							<script>
							if(opener.processType.split(",")[1]=="<c:out value="${type[0]}"/>"){
								document.write("<option selected value='"+"<c:out value="${type[1]}"/>"+","+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
							}else{
								document.write("<option value='"+"<c:out value="${type[1]}"/>"+","+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
							}
							</script>
						</c:forEach>
					</select>
		</TD>
		<TD></TD>
    </TR>

	<TR valign=top>
		<TD></TD>
		<TD valign="middle"><span id=tabpage1_3>流程启动表单</span></TD>
		<TD>
					<select name="processForm">
						<option selected value='未指定表单,0'>未指定表单</option>
						<c:forEach items="${forms}" var="form">
							<script>
							if(opener.processForm.split(",")[1]=="<c:out value="${form[0]}"/>"){
								document.write("<option selected value='"+"<c:out value="${form[1]}"/>"+","+"<c:out value="${form[0]}"/>"+"'>"+"<c:out value="${form[1]}"/>"+"</option>");
							}else{
								document.write("<option value='"+"<c:out value="${form[1]}"/>"+","+"<c:out value="${form[0]}"/>"+"'>"+"<c:out value="${form[1]}"/>"+"</option>");
							}
							</script>
						</c:forEach>
					</select></TD>
		<TD></TD>
    </TR>
	<TR height="3">
		<TD></TD>
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

<!-- Tab Page 3 Content Begin -->
<div class="contents" id="contents3">
<TABLE border=0 width="100%" height="100%">

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>定时器设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD colspan="2">
		<span id=tabpage2_7>启用定时催办<input type="checkbox" name="doc_isTimer" value="1" onclick="setIsTimer()"></span>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD valign = "middle"><span id=tabpage2_7>流程持续时间</span></TD>
		<TD align="left"><input type="text" name="doc_timer_data" style="width:40">&nbsp;
		<select name="doc_timer_init">
			<option value="day">天</option>
			<option value="hour">小时</option>
			<option value="minute">分钟</option>
		</select>
		</TD>
		<TD></TD>
	</TR>
	
	<TR valign=top>
		<TD width=5></TD>
		<TD valign = "middle"><span id=tabpage2_7>第一次催办</span></TD>
		<TD align="left"><input type="text" name="pre_timer_data" style="width:40">&nbsp;
		<select name="pre_timer_init">
			<option value="day">天</option>
			<option value="hour">小时</option>
			<option value="minute">分钟</option>
		</select>
		&nbsp;持续时间到达前
		</TD>
		<TD></TD>
	</TR>
	
	<TR valign=top>
		<TD width=5></TD>
		<TD valign = "middle"><span id=tabpage2_7>重复催办间隔</span></TD>
		<TD align="left"><input type="text" name="re_timer_data" style="width:40">&nbsp;
		<select name="re_timer_init">
			<option value="day">天</option>
			<option value="hour">小时</option>
			<option value="minute">分钟</option>
		</select>
		</TD>
		<TD></TD>
	</TR>				

	<TR height="3">
		<TD></TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
	</TABLE>
	</Fieldset>
	</TD>
	<TD>&nbsp;</TD>
</TR>

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>催办方式设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD colspan="2">
		<span id=tabpage2_7>邮件催办<input type="checkbox" name="mail_timer" value="1">
		</span>
		&nbsp;
		<span id=tabpage2_7>通知催办<input type="checkbox" name="notice_timer" value="1">
		</span>
		&nbsp;
		<span id=tabpage2_7>短信息催办<input type="checkbox" name="mes_timer" value="1">
		</span>
		<TD></TD>
	</TR>		

	<TR height="3">
		<TD></TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
	</TABLE>
	</Fieldset>
	</TD>
	<TD>&nbsp;</TD>
</TR>

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>催办人员设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD colspan="2">
		<input type="checkbox" style="display:none" name="handler_notice" value="1">	
		<span id=tabpage2_7>通知发起人<input type="checkbox" name="start_notice" value="1">
		</span>
		&nbsp;
		<span id=tabpage2_7>通知流程管理员<input type="checkbox" name="owner_notice" value="1">
		</span>
		<TD></TD>
	</TR>			

	<TR height="3">
		<TD></TD>
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
<!-- Tab Page 3 Content End -->



	</td>
  </tr>
</tbody>
</table>

<table cellspacing="1" cellpadding="0" border="0">
	<tr>
		<td width="100%"></td>
		<td><input type=button id="btnOk" class=btn value="确 定" onclick="jscript: ok();">&nbsp;&nbsp;&nbsp;</td>
		<td><input type=button id="btnCancel" class=btn value="取 消" onclick="jscript: cancel();">&nbsp;&nbsp;&nbsp;</td>

	</tr>
</table>
</BODY>
</HTML>