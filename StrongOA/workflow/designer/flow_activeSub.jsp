<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
<HEAD>
<html:base/>
<TITLE> 模型属性设置 </TITLE>
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
	var currentStatus = window.dialogArguments.currentStatus;
	//指定任务处理人
	function setTaskActors(){
			
			var vPageLink = scriptroot + "/workflow/chosse_person.jsp";
			var returnValue = OpenWindow(vPageLink,400,450,window);
			if(returnValue != null && returnValue != "None"){
				var name = '';
				var value = '';
				
				var returnValues = returnValue.split("#");
				
				var person = returnValues[0];
				var post = returnValues[1];
				
				if(person.length != 0){
					var persons = person.split(",");
					for(var i = 0; i < persons.length; i++){
						name = name + persons[i].split("|")[1] + ",";
						value = value + persons[i].split("|")[0] + ",";
					}
				}
				
				if(post.length != 0){
					var posts = post.split(",");
					if(name != ''){
						name = name.substring(0, name.length - 1) + "|";
						value = value.substring(0, value.length - 1) + "|";
					}else{
						name = name + "|";
						value = value + "|";
					}
					for(var i = 0; i < posts.length; i++){
						name = name + posts[i].split("|")[1] + ",";
						value = value + posts[i].split("|")[0] + ",";						
					}
				}

				var obj = document.getElementsByName("handleactor")[0];
				obj.value = value.substring(0, value.length - 1);
				var obj = document.getElementsByName("realActors")[0];
				obj.value = name.substring(0, name.length - 1);				
			}	
	}
	
	//指定表单
	function setForms(){
			if(document.getElementsByName("setform")[0].disabled!=""){
				return false;
			}
			var vPageLink = scriptroot +"/workflowmanager/processfile/initSetActiveForm.do";
			var returnValue = OpenWindow(vPageLink,190,260,window);
			if(returnValue!=null){
		        var returnits= returnValue.split(",");
				var obj=document.getElementsByName("togetherform")[0];
				obj.value=returnits[1];	
				var obj=document.getElementsByName("formid")[0];
				obj.value=returnits[0];
				var obj=document.getElementsByName("formpriv")[0];
				obj.value="superadmin";							        
			}					
	}
	
	//设置权限
	function setPrivs(){
			if(document.getElementsByName("setpriv")[0].disabled!=""){
				return false;
			}	
		if(document.getElementsByName("formid")[0].value=="" || document.getElementsByName("formid")[0]==null){
			alert("请先选择要挂接的表单!");
			return false;
		}
		var obj=document.getElementsByName("formpriv")[0];
		obj.value="superadmin";
	}


function iniWindow(){
   try{
	   iniAttributeDialog();
   }catch(e){
     alert('打开属性设置对话框时出错！');
	 window.close();
  }   
}
function iniAttributeDialog(){
   var opener = window.dialogArguments;
   if(opener.fSelectedObj!=null){
		document.getElementsByName("nodeId")[0].value=opener.fSelectedObj.id;
		document.getElementsByName("nodeName")[0].value=opener.fSelectedObj.nodeclass.thisCaption;
		document.getElementsByName("leftlocate")[0].value=opener.fSelectedObj.style.posLeft;
		document.getElementsByName("toplocate")[0].value=opener.fSelectedObj.style.posTop;
		document.getElementsByName("taskname")[0].value=opener.fSelectedObj.taskName;		
		document.getElementsByName("taskstartaction")[0].value=opener.fSelectedObj.taskStart;
		document.getElementsByName("taskendaction")[0].value=opener.fSelectedObj.taskEnd;
		document.getElementsByName("nodeleaveaction")[0].value=opener.fSelectedObj.nodeLeave;
		document.getElementsByName("nodeenteraction")[0].value=opener.fSelectedObj.nodeEnter;
		document.getElementsByName("handleactor")[0].value=opener.fSelectedObj.handleActor;
		document.getElementsByName("realActors")[0].value=opener.fSelectedObj.realActors;		
		document.getElementsByName("togetherform")[0].value=opener.fSelectedObj.nodeForm.split(",")[0];
		document.getElementsByName("formid")[0].value=opener.fSelectedObj.nodeForm.split(",")[1];
		document.getElementsByName("formpriv")[0].value=opener.fSelectedObj.formPriv;
		document.getElementsByName("actionSet")[0].value=opener.fSelectedObj.actionSet;
		document.getElementsByName("maxActors")[0].value=opener.fSelectedObj.maxActors;		
		
		if(opener.fSelectedObj.synchronize != null && opener.fSelectedObj.synchronize == "0"){
			document.getElementsByName("synchronize")[1].checked = true;
		}else{
			document.getElementsByName("synchronize")[0].checked = true;
		}
		
		if(opener.fSelectedObj.isActiveactor == "1"){
			document.getElementsByName("isActiveactor")[0].checked="true";
			if(opener.fSelectedObj.canSelectOther == "1"){			
				document.getElementsByName("canSelectOther")[0].checked="true";
			}
		}else{
			document.getElementsByName("canSelectOther")[0].disabled="true";
		}
		
		if(opener.fSelectedObj.fromParent=="1"){
			document.getElementsByName("fromParent")[0].checked="true";
		}
		
		if(opener.fSelectedObj.toParent=="1"){
			document.getElementsByName("toParent")[0].checked="true";
		}							

		if(opener.fSelectedObj.taskStartMail=="1"){
			document.getElementsByName("taskStartMail")[0].checked="true";
		}
		
		if(opener.fSelectedObj.taskEndMail=="1"){
			document.getElementsByName("taskEndMail")[0].checked="true";
		}

		if(opener.fSelectedObj.taskStartMes=="1"){
			document.getElementsByName("taskStartMes")[0].checked="true";
		}
		
		if(opener.fSelectedObj.taskEndMes=="1"){
			document.getElementsByName("taskEndMes")[0].checked="true";
		}
		
		if(opener.fSelectedObj.isSetAction=="1"){
			document.getElementsByName("isSetAction")[0].checked="true";
			isSetAction();
		}else{
			document.getElementsByName("isSetAction")[0].checked="";
			isSetAction();			
		}
		document.getElementsByName("nodeType")[0].value="动态子流程节点";
		
		//任务定时器设置
		var timerSet = opener.fSelectedObj.doc_timerSet.split(",");
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
			document.getElementsByName("owner_notice")[0].checked = true;		
		}else{
			document.getElementsByName("owner_notice")[0].checked = false;		
		}
		if(timerSet[10] == '1'){
			document.getElementsByName("start_notice")[0].checked = true;		
		}else{
			document.getElementsByName("start_notice")[0].checked = false;		
		}
		if(timerSet[11] == '1'){
			document.getElementsByName("handler_notice")[0].checked = true;		
		}else{
			document.getElementsByName("handler_notice")[0].checked = false;		
		}	
		if(opener.fSelectedObj.doc_isTimer == "1"){
			document.getElementsByName("doc_isTimer")[0].checked = true;
		}else{
			document.getElementsByName("doc_isTimer")[0].checked = false;
			setIsTimer();
		}		
   }
}

function setIsTimer(){
	if(document.getElementsByName("doc_isTimer")[0].checked == true && currentStatus != "edit"){
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

function okOnClick(){
   var opener = window.dialogArguments;
   var thisnode;
   var nodeEnterEvent=null;
   var nodeLeaveEvent=null;
   var taskStartEvent=null;
   var taskEndEvent=null;
   var task=null;
   var timer = null;   
   try{
		var nodename=document.getElementsByName("nodeName")[0].value;
		if(nodename!=null && nodename!="" && nodename!=opener.fSelectedObj.nodeclass.thisCaption){
			for(var i=0;i<opener.root.childNodes.length;i++){
				var node=opener.root.childNodes[i];
				if(node.getAttributeNode("atnode").value!=opener.fSelectedObj.id && node.getAttributeNode("name").value==nodename){
					alert("节点名称与模型中其他节点相同,请重新输入!")
					return false;
				}
				if(node.getAttributeNode("atnode").value==opener.fSelectedObj.id)
					thisnode=node;
			}
			var transitions=opener.doc.getElementsByTagName("transition");
			for(var i=0;i<transitions.length;i++){
				if(transitions[i].getAttributeNode("to").value==opener.fSelectedObj.nodeclass.thisCaption){
					transitions[i].getAttributeNode("to").value=nodename;
				}
			}
			opener.fSelectedObj.nodeclass.thisCaption=nodename;
			opener.fSelectedObj.nodeclass.textobj.innerText=nodename;
			opener.fSelectedObj.title=nodename;			
			thisnode.getAttributeNode("name").value=nodename;

		}else if(nodename==opener.fSelectedObj.nodeclass.thisCaption){
			for(var i=0;i<opener.root.childNodes.length;i++){
				var node=opener.root.childNodes[i];
				if(node.getAttributeNode("atnode").value==opener.fSelectedObj.id)
					thisnode=node;
			}						
		}else{
			alert("请输入节点名称(name属性)!");
			return false;
		}
		if(document.getElementsByName("taskname")[0].value==null || ""==document.getElementsByName("taskname")[0].value){
			alert("请输入任务名称!");
			return false;
		}
		if(document.getElementsByName("handleactor")[0].value==null || ""==document.getElementsByName("handleactor")[0].value){
			alert("请为任务设置处理人!");
			return false;
		}
		
		if(!document.getElementsByName("isSetAction")[0].checked && (document.getElementsByName("togetherform")[0].value!=null && document.getElementsByName("togetherform")[0].value!="") && (""==document.getElementsByName("formpriv")[0].value || null==document.getElementsByName("formpriv")[0].value)){
			alert("请为挂接的表单设置权限!")
			return false;
		}
		if(document.getElementsByName("isSetAction")[0].checked && (null==document.getElementsByName("actionSet")[0].value || ""==document.getElementsByName("actionSet")[0].value)){
			alert("请指定要挂接的动作！");
			return false;
		}
		
		var events=thisnode.getElementsByTagName("event");
		for(var i=0;i<events.length;i++){
			if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="node-enter"){
				nodeEnterEvent=events[i];
			}else if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="node-leave"){
				nodeLeaveEvent=events[i];
			}else if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="task-start"){
				taskStartEvent=events[i];
			}else if(events[i].getAttributeNode("type")!=null && events[i].getAttributeNode("type").value=="task-end"){
				taskEndEvent=events[i];
			}
		}
		
		task=thisnode.getElementsByTagName("task")[0];
		task.getAttributeNode("name").value=document.getElementsByName("taskname")[0].value;

		// 预先查找该任务下的定时器信息
		var timers = task.getElementsByTagName("timer");
		for(var i=0; i<timers.length; i++){
			if(timers[i].getAttributeNode("name") != null && timers[i].getAttributeNode("name").value == "com.strongit.timer"){
				timer = timers[i];
				break;
			}
			
		}

		//设置节点进入操作
		var nodeenter=document.getElementsByName("nodeenteraction")[0].value;
		if(nodeenter!=null && ""!=nodeenter){
			opener.fSelectedObj.nodeEnter=nodeenter;
			if(nodeEnterEvent!=null){
					var k=1;
					var actions=nodeEnterEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.nodeEnterAction"){
							actions[i].getAttributeNode("class").value=nodeenter;
							k=0;
							break;
						}
					}
					if(k==1){
						var n2 = opener.doc.createNode(1,"action","");
						
						//添加xml节点标识属性
						var r = opener.doc.createAttribute("xmlflag");
						r.value = opener.nCtlNum ++; 
						//添加属性 
						n2.setAttributeNode(r);						
						
						var r = opener.doc.createAttribute("name"); 
							r.value="com.strongit.nodeEnterAction";
							n2.setAttributeNode(r);												 
						var r = opener.doc.createAttribute("class"); 
							r.value=nodeenter;
							n2.setAttributeNode(r);
						nodeEnterEvent.appendChild(n2);
					}	
			}else{
				nodeEnterEvent = opener.doc.createNode(1,"event","");
				
					//添加xml节点标识属性
					var r = opener.doc.createAttribute("xmlflag");
					r.value = opener.nCtlNum ++; 
					//添加属性 
					nodeEnterEvent.setAttributeNode(r);				
				
					var r = opener.doc.createAttribute("type"); 
						r.value="node-enter";
						nodeEnterEvent.setAttributeNode(r);
				var n1 = opener.doc.createNode(1,"action","");
				
					//添加xml节点标识属性
					var r = opener.doc.createAttribute("xmlflag");
					r.value = opener.nCtlNum ++; 
					//添加属性 
					n1.setAttributeNode(r);					
				
					var r = opener.doc.createAttribute("name"); 
						r.value="com.strongit.nodeEnterAction";
						n1.setAttributeNode(r);						 

					var r = opener.doc.createAttribute("class"); 
						r.value=nodeenter;
						n1.setAttributeNode(r);
				nodeEnterEvent.appendChild(n1);
				thisnode.appendChild(nodeEnterEvent);				
			}
		}else{
			opener.fSelectedObj.nodeEnter="";
			if(nodeEnterEvent!=null){
					var actions=nodeEnterEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.nodeEnterAction"){
							actions[i].parentNode.removeChild(actions[i]);
							break;
						}
					}
			}
			if(nodeEnterEvent!=null && nodeEnterEvent.childNodes.length==0){
				nodeEnterEvent.parentNode.removeChild(nodeEnterEvent);
			}
		}
		
		//设置节点离开操作
		var nodeleave=document.getElementsByName("nodeleaveaction")[0].value;
		if(nodeleave!=null && ""!=nodeleave){
			opener.fSelectedObj.nodeLeave=nodeleave;
			if(nodeLeaveEvent!=null){
					var k=1;
					var actions=nodeLeaveEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.nodeLeaveAction"){
							actions[i].getAttributeNode("class").value=nodeleave;
							k=0;
							break;
						}
					}
					if(k==1){
						var n2 = opener.doc.createNode(1,"action","");
						
						//添加xml节点标识属性
						var r = opener.doc.createAttribute("xmlflag");
						r.value = opener.nCtlNum ++; 
						//添加属性 
						n2.setAttributeNode(r);							
						
						var r = opener.doc.createAttribute("name"); 
							r.value="com.strongit.nodeLeaveAction";
							n2.setAttributeNode(r);												 
						var r = opener.doc.createAttribute("class"); 
							r.value=nodeleave;
							n2.setAttributeNode(r);
						nodeLeaveEvent.appendChild(n2);
					}	
			}else{
				nodeLeaveEvent = opener.doc.createNode(1,"event","");
				
					//添加xml节点标识属性
					var r = opener.doc.createAttribute("xmlflag");
					r.value = opener.nCtlNum ++; 
					//添加属性 
					nodeLeaveEvent.setAttributeNode(r);					
				
					var r = opener.doc.createAttribute("type"); 
						r.value="node-leave";
						nodeLeaveEvent.setAttributeNode(r);
				var n1 = opener.doc.createNode(1,"action","");
				
					//添加xml节点标识属性
					var r = opener.doc.createAttribute("xmlflag");
					r.value = opener.nCtlNum ++; 
					//添加属性 
					n1.setAttributeNode(r);				
				
					var r = opener.doc.createAttribute("name"); 
						r.value="com.strongit.nodeLeaveAction";
						n1.setAttributeNode(r);						 

					var r = opener.doc.createAttribute("class"); 
						r.value=nodeleave;
						n1.setAttributeNode(r);
				nodeLeaveEvent.appendChild(n1);
				thisnode.appendChild(nodeLeaveEvent);				
			}
		}else{	
			opener.fSelectedObj.nodeleave="";
			if(nodeLeaveEvent!=null){
					var actions=nodeLeaveEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.nodeLeaveAction"){
							actions[i].parentNode.removeChild(actions[i]);
							break;
						}
					}
			}
			if(nodeLeaveEvent!=null && nodeLeaveEvent.childNodes.length==0){
				nodeLeaveEvent.parentNode.removeChild(nodeLeaveEvent);
			}
		}
		
		
				//任务开始动作设置				
				var taskstart=document.getElementsByName("taskstartaction")[0].value;
				if(taskstart!=null && ""!=taskstart){
					opener.fSelectedObj.taskStart=taskstart;
					if(taskStartEvent!=null){
							var k=1;
							var actions=taskStartEvent.getElementsByTagName("action");
							for(var i=0;i<actions.length;i++){
								if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.taskStartAction"){
									actions[i].getAttributeNode("class").value=taskstart;
									k=0;
									break;
								}
							}
							if(k==1){
								var n2 = opener.doc.createNode(1,"action","");
								
								//添加xml节点标识属性
								var r = opener.doc.createAttribute("xmlflag");
								r.value = opener.nCtlNum ++; 
								//添加属性 
								n2.setAttributeNode(r);									
								
								var r = opener.doc.createAttribute("name"); 
									r.value="com.strongit.taskStartAction";
									n2.setAttributeNode(r);												 
								var r = opener.doc.createAttribute("class"); 
									r.value=taskstart;
									n2.setAttributeNode(r);
								taskStartEvent.appendChild(n2);
							}	
					}else{
						taskStartEvent = opener.doc.createNode(1,"event","");
						
							//添加xml节点标识属性
							var r = opener.doc.createAttribute("xmlflag");
							r.value = opener.nCtlNum ++; 
							//添加属性 
							taskStartEvent.setAttributeNode(r);							
						
							var r = opener.doc.createAttribute("type"); 
								r.value="task-start";
								taskStartEvent.setAttributeNode(r);
						var n1 = opener.doc.createNode(1,"action","");
						
							//添加xml节点标识属性
							var r = opener.doc.createAttribute("xmlflag");
							r.value = opener.nCtlNum ++; 
							//添加属性 
							n1.setAttributeNode(r);						
						
							var r = opener.doc.createAttribute("name"); 
								r.value="com.strongit.taskStartAction";
								n1.setAttributeNode(r);						 
		
							var r = opener.doc.createAttribute("class"); 
								r.value=taskstart;
								n1.setAttributeNode(r);
						taskStartEvent.appendChild(n1);
						task.appendChild(taskStartEvent);				
					}
		}else{	
			opener.fSelectedObj.taskStart="";
			if(taskStartEvent!=null){
					var actions=taskStartEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.taskStartAction"){
							actions[i].parentNode.removeChild(actions[i]);
							break;
						}
					}
			}
			if(taskStartEvent!=null && taskStartEvent.childNodes.length==0){
				taskStartEvent.parentNode.removeChild(taskStartEvent);
			}
		}
				//任务结束动作设置
				var taskend=document.getElementsByName("taskendaction")[0].value;
				if(taskend!=null && ""!=taskend){
					opener.fSelectedObj.taskEnd=taskend;
					if(taskEndEvent!=null){
							var k=1;
							var actions=taskEndEvent.getElementsByTagName("action");
							for(var i=0;i<actions.length;i++){
								if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.taskEndAction"){
									actions[i].getAttributeNode("class").value=taskend;
									k=0;
									break;
								}
							}
							if(k==1){
								var n2 = opener.doc.createNode(1,"action","");
								
								//添加xml节点标识属性
								var r = opener.doc.createAttribute("xmlflag");
								r.value = opener.nCtlNum ++; 
								//添加属性 
								n2.setAttributeNode(r);									
								
								var r = opener.doc.createAttribute("name"); 
									r.value="com.strongit.taskEndAction";
									n2.setAttributeNode(r);												 
								var r = opener.doc.createAttribute("class"); 
									r.value=taskend;
									n2.setAttributeNode(r);
								taskEndEvent.appendChild(n2);
							}	
					}else{
						taskEndEvent = opener.doc.createNode(1,"event","");
						
							//添加xml节点标识属性
							var r = opener.doc.createAttribute("xmlflag");
							r.value = opener.nCtlNum ++; 
							//添加属性 
							taskEndEvent.setAttributeNode(r);						
						
							var r = opener.doc.createAttribute("type"); 
								r.value="task-end";
								taskEndEvent.setAttributeNode(r);
						var n1 = opener.doc.createNode(1,"action","");
						
							//添加xml节点标识属性
							var r = opener.doc.createAttribute("xmlflag");
							r.value = opener.nCtlNum ++; 
							//添加属性 
							n1.setAttributeNode(r);						
						
							var r = opener.doc.createAttribute("name"); 
								r.value="com.strongit.taskEndAction";
								n1.setAttributeNode(r);						 
		
							var r = opener.doc.createAttribute("class"); 
								r.value=taskend;
								n1.setAttributeNode(r);
						taskEndEvent.appendChild(n1);
						task.appendChild(taskEndEvent);				
					}
		}else{	
			opener.fSelectedObj.taskEnd="";
			if(taskEndEvent!=null){
					var actions=taskEndEvent.getElementsByTagName("action");
					for(var i=0;i<actions.length;i++){
						if(actions[i].getAttributeNode("name")!=null && actions[i].getAttributeNode("name").value=="com.strongit.taskEndAction"){
							actions[i].parentNode.removeChild(actions[i]);
							break;
						}
					}
			}
			if(taskEndEvent!=null && taskEndEvent.childNodes.length==0){
				taskEndEvent.parentNode.removeChild(taskEndEvent);
			}
		}					
												
				opener.fSelectedObj.nodeForm=document.getElementsByName("togetherform")[0].value+","+document.getElementsByName("formid")[0].value;
				opener.fSelectedObj.formPriv=document.getElementsByName("formpriv")[0].value;				
				opener.fSelectedObj.taskName=document.getElementsByName("taskname")[0].value;
				opener.fSelectedObj.handleActor=document.getElementsByName("handleactor")[0].value;
				opener.fSelectedObj.realActors=document.getElementsByName("realActors")[0].value;
				opener.fSelectedObj.taskStart=document.getElementsByName("taskstartaction")[0].value;
				opener.fSelectedObj.taskEnd=document.getElementsByName("taskendaction")[0].value;
				opener.fSelectedObj.maxActors=document.getElementsByName("maxActors")[0].value;
				if(document.getElementsByName("isSetAction")[0].checked){
					opener.fSelectedObj.isSetAction="1";
				}else{
					opener.fSelectedObj.isSetAction="0";
				}
				opener.fSelectedObj.actionSet=document.getElementsByName("actionSet")[0].value;
				
				if(document.getElementsByName("isActiveactor")[0].checked){
					opener.fSelectedObj.isActiveactor="1";
				}else{
					opener.fSelectedObj.isActiveactor="0";
				}
				
				if(document.getElementsByName("canSelectOther")[0].checked){
					opener.fSelectedObj.canSelectOther="1";
				}else{
					opener.fSelectedObj.canSelectOther="0";
				}					
				
				if(document.getElementsByName("taskStartMail")[0].checked){
					opener.fSelectedObj.taskStartMail="1";
				}else{
					opener.fSelectedObj.taskStartMail="0";
				}
				
				if(document.getElementsByName("taskEndMail")[0].checked){
					opener.fSelectedObj.taskEndMail="1";
				}else{
					opener.fSelectedObj.taskEndMail="0";
				}
				
				if(document.getElementsByName("taskStartMes")[0].checked){
					opener.fSelectedObj.taskStartMes="1";
				}else{
					opener.fSelectedObj.taskStartMes="0";
				}
				
				if(document.getElementsByName("taskEndMes")[0].checked){
					opener.fSelectedObj.taskEndMes="1";
				}else{
					opener.fSelectedObj.taskEndMes="0";
				}
				
				if(document.getElementsByName("fromParent")[0].checked){
					opener.fSelectedObj.fromParent="1";
				}else{
					opener.fSelectedObj.fromParent="0";
				}
				
				if(document.getElementsByName("toParent")[0].checked){
					opener.fSelectedObj.toParent="1";
				}else{
					opener.fSelectedObj.toParent="0";
				}
				
		  //定时器功能设置	
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
		  if(document.getElementsByName("owner_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";	
		  }else{
			timerSet = timerSet + "0" + ",";	
		  }
		  if(document.getElementsByName("start_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";		
		  }else{
			timerSet = timerSet + "0" + ",";		
		  }
		  if(document.getElementsByName("handler_notice")[0].checked == true){
			timerSet = timerSet + "1" + ",";		
		  }else{
			timerSet = timerSet + "0" + ",";		
		  }
		  
		  opener.fSelectedObj.doc_timerSet = timerSet;
		         	  
       	  if(document.getElementsByName("doc_isTimer")[0].checked == true){
       	  	opener.fSelectedObj.doc_isTimer = "1";
       	  }else{
       	  	opener.fSelectedObj.doc_isTimer = "0";
       	  }
       	  
       	  
		//增加任务定时器功能
	if(opener.fSelectedObj.doc_isTimer == "1"){
		var timerSet = opener.fSelectedObj.doc_timerSet.split(",");
		var wholeLast;
		var preLast;
		//流程持续时间换算成分钟
		if(timerSet[1] == "day"){
			wholeLast = parseInt(timerSet[0])*12*60;
		}else if(timerSet[1] == "hour"){
			wholeLast = parseInt(timerSet[0])*60;
		}else if(timerSet[1] == "minute"){
			wholeLast = parseInt(timerSet[0]);
		}
		//提前催办时间换算成分钟
		if(timerSet[3] == "day"){
			preLast = parseInt(timerSet[2])*12*60;
		}else if(timerSet[3] == "hour"){
			preLast = parseInt(timerSet[2])*60;
		}else if(timerSet[3] == "minute"){
			preLast = parseInt(timerSet[2]);
		}
		var duration = wholeLast - preLast;		
		// 起始点进入事件已存在
		if(timer != null){
			timer.getAttributeNode("duedate").value = duration + " minute";
			timer.getAttributeNode("repeat").value = timerSet[4] + " " + timerSet[5];
			var action = timer.getElementsByTagName("action")[0];
			action.getAttributeNode("class").value = opener.timerAction;
		}else{
			timer = opener.doc.createNode(1,"timer","");
			
			//添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum ++; 
			//添加属性 
			timer.setAttributeNode(r);			
			
			r = opener.doc.createAttribute("name");
			r.value = "com.strongit.timer";
			timer.setAttributeNode(r);
			r = opener.doc.createAttribute("duedate");
			r.value = duration + " minute";;
			timer.setAttributeNode(r);
			r = opener.doc.createAttribute("repeat");
			r.value = timerSet[4] + " " + timerSet[5];;
			timer.setAttributeNode(r);
			var n2 = opener.doc.createNode(1, "action", "");
			
			//添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum ++; 
			//添加属性 
			n2.setAttributeNode(r);			
														 
			r = opener.doc.createAttribute("class"); 
			r.value = opener.timerAction;
			n2.setAttributeNode(r);					
			timer.appendChild(n2);
			task.appendChild(timer);
		}
	}else{
		if(timer!=null){
			timer.parentNode.removeChild(timer);
		}
	} 				
				
																																								
													
		window.close();
		opener.movecontrolSubject.notifyObservers();
  }catch(e){
     alert('关闭属性设置对话框时出错！');
	 window.close();
  }   
}
function cancelOnClick(){
   window.close();
}

function isSetAction(){
	var sa=document.getElementsByName("isSetAction")[0];
	if(sa.checked){
		document.getElementsByName("actionSet")[0].disabled="";
	}else if(!sa.checked){
		document.getElementsByName("actionSet")[0].disabled="true";			
	}
}

function activeActor(){
	var cs=document.getElementsByName("isActiveactor")[0];
	if(cs.checked){
		document.getElementsByName("maxActors")[0].disabled="";
		document.getElementsByName("canSelectOther")[0].disabled = "";
	}else if(!cs.checked){
		document.getElementsByName("maxActors")[0].disabled="true";	
		document.getElementsByName("canSelectOther")[0].disabled = "true";
	}
}
</SCRIPT> 

</HEAD>

<BODY onload='iniWindow()'>
<table border="0" cellpadding="0" cellspacing="0" height=385px>
<thead>
  <tr id="WebTab">
	<td class="selectedtab" id="tab1" onmouseover='hoverTab("tab1")' onclick="switchTab('tab1','contents1');"><span id=tabpage1>基本属性</span></td>
	<td class="tab" id="tab2" onmouseover='hoverTab("tab2")' onclick="switchTab('tab2','contents2');"><span id=tabpage2>工作流设置</span></td>
    <td class="tab" id="tab3" onmouseover='hoverTab("tab3")' onclick="switchTab('tab3','contents3');"><span id=tabpage3>表单设置</span></td>
	<td class="tab" id="tab4" onmouseover='hoverTab("tab4")' onclick="switchTab('tab4','contents4');"><span id=tabpage3>子流程设置</span></td>
	<td class="tab" id="tab5" onmouseover='hoverTab("tab5")' onclick="switchTab('tab5','contents5');"><span id=tabpage5>时间设置</span></td>
	<td class="tabspacer" width=1>&nbsp;</td>	
  </tr>
</thead>
<tbody>
  <tr>
	<td id="contentscell" colspan="6">
<!-- Tab Page 1 Content Begin -->
<div class="selectedcontents" id="contents1">
<TABLE border=0 width="100%" height="100%">
<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage1_1>基本属性</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage1_2>节点编号</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="nodeId" value="" class=txtput disabled></TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD></TD>
		<TD><span id=tabpage1_3>节点名称</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" readOnly=\"true\" NAME=\"nodeName\" value=\"\" class=txtput>");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"nodeName\" value=\"\" class=txtput>");
				}
			</script>		
		</TD>
		<TD></TD>
    </TR>

	<TR valign=top>
		<TD></TD>
		<TD><span id=tabpage1_3>节点类型</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="nodeType" value="" class=txtput disabled></TD>
		<TD></TD>
    </TR>

	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage1_7>Left 位置</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="leftlocate" value="" class=txtput disabled></TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD></TD>
		<TD><span id=tabpage1_8>Top 位置</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="toplocate" value="" class=txtput disabled></TD>
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

<!-- Tab Page 2 Content Begin -->
<div class="contents" id="contents2">
<TABLE border=0 width="100%" height="100%">

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>节点设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_2>节点进入</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" NAME=\"nodeenteraction\" readOnly=\"true\" value=\"\" class=txtput >");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"nodeenteraction\" value=\"\" class=txtput >");
				}
			</script>			
		</TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>节点离开</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" NAME=\"nodeleaveaction\" readOnly=\"true\" value=\"\" class=txtput >");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"nodeleaveaction\" value=\"\" class=txtput >");
				}
			</script>		
		</TD>
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

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>任务设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_2>任务名称</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" NAME=\"taskname\" value=\"\" class=txtput readOnly=\"true\">");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"taskname\" value=\"\" class=txtput>");
				}
			</script>		
		</TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>处理人&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;&nbsp;<INPUT TYPE="hidden" NAME="handleactor" class=txtput disabled><INPUT TYPE="text" NAME="realActors" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a href="#" onclick="setTaskActors()">选择处理人</a></TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>动态指定处理人</span>&nbsp;&nbsp;<input type="checkbox" name="isActiveactor" value="1" onclick="activeActor()"></TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>允许选择其他人</span>&nbsp;&nbsp;<input type="checkbox" name="canSelectOther" value="1"></TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>最大参与人数</span>&nbsp;&nbsp;<input type="text" name="maxActors" class=txtput></TD>
		<TD></TD>
	</TR>		
	<TR valign=top>
		<TD></TD>
		<TD><span id=tabpage2_3>任务开始</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" NAME=\"taskstartaction\" value=\"\" class=txtput readOnly=\"true\">");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"taskstartaction\" value=\"\" class=txtput >");
				}
			</script>		
		&nbsp;&nbsp;&nbsp;短信<INPUT TYPE="checkbox" NAME="taskStartMes" value="">&nbsp;&nbsp;通知<INPUT TYPE="checkbox" NAME="taskStartMail" value=""></TD>
		<TD></TD>
    </TR>
	<TR valign=top>
		<TD></TD>
		<TD><span id=tabpage2_4>任务结束</span>&nbsp;&nbsp;
			<script>
				if(currentStatus == "edit"){
					document.write("<INPUT TYPE=\"text\" NAME=\"taskendaction\" value=\"\" class=txtput readOnly=\"true\">");
				}else{
					document.write("<INPUT TYPE=\"text\" NAME=\"taskendaction\" value=\"\" class=txtput >");
				}
			</script>		
		&nbsp;&nbsp;&nbsp;短信<INPUT TYPE="checkbox" NAME="taskEndMes" value="">&nbsp;&nbsp;通知<INPUT TYPE="checkbox" NAME="taskEndMail" value=""></TD>
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
<!-- Tab Page 2 Content End -->

<!-- Tab Page 3 Content Begin -->
<div class="contents" id="contents3">
<TABLE border=0 width="100%" height="100%">

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>表单设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>挂接表单</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="togetherform" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a id="setform" href="#" onclick="setForms()">选择表单</a><INPUT TYPE="hidden" NAME="formid" value=""></TD>
		
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

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>权限设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>表单权限</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="formpriv" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;<a id="setpriv" href="#" onclick="setPrivs()">设置权限</a></TD>
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

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage2_1>挂接动作</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>挂接动作</span>&nbsp;&nbsp;<INPUT TYPE="checkbox" NAME="isSetAction" value="" onclick="isSetAction();">&nbsp;&nbsp;&nbsp;</TD>
		<TD></TD>
	</TR>
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>设置动作</span>&nbsp;&nbsp;<INPUT TYPE="text" NAME="actionSet" value="" class=txtput disabled>&nbsp;&nbsp;&nbsp;</TD>
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
<!-- Tab Page 3 Content End -->

<!-- Tab Page 4 Content Begin -->
<div class="contents" id="contents4">
<TABLE border=0 width="100%" height="100%">

<TR valign=top>
	<TD></TD>
	<TD width="100%" valign=top>
	<Fieldset style="border: 1px solid #C0C0C0;">
	<LEGEND align=left style="font-size:9pt;">&nbsp;<span id=tabpage4_1>子流程设置</span>&nbsp;</LEGEND>
	<TABLE border=0 width="100%" height="100%" style="font-size:9pt;">	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage4_7>子流程类型</span>
					<script>
				if(currentStatus == "edit"){
					document.write("<input type=\"radio\" name=\"synchronize\" value=\"1\" disabled>同步</input>&nbsp;&nbsp;<input type=\"radio\" name=\"synchronize\" value=\"0\" disabled>异步</input>");
				}else{
					document.write("<input type=\"radio\" name=\"synchronize\" value=\"1\">同步</input>&nbsp;&nbsp;<input type=\"radio\" name=\"synchronize\" value=\"0\">异步</input>");
				}
			</script>
		</TD>
		<TD></TD>
	</TR>
	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>父流程表单数据可见</span>&nbsp;&nbsp;<input type="checkbox" name="fromParent" value="1"></TD>
		<TD></TD>
	</TR>
	
	<TR valign=top>
		<TD width=5></TD>
		<TD><span id=tabpage2_7>结束时数据返回父流程</span>&nbsp;&nbsp;<input type="checkbox" name="toParent" value="1"></TD>
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
<!-- Tab Page 4 Content End -->

<!-- Tab Page 5 Content Begin -->
<div class="contents" id="contents5">
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
		<span id=tabpage2_7>启用定时催办
			<script>
				if(currentStatus == "edit"){
					document.write("<input type=\"checkbox\" name=\"doc_isTimer\" value=\"1\" disabled>");
				}else{
					document.write("<input type=\"checkbox\" name=\"doc_isTimer\" value=\"1\" onclick=\"setIsTimer()\">");
				}
			</script>			
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
		<span id=tabpage2_7>通知办理人<input type="checkbox" name="handler_notice" value="1">
		</span>		
		&nbsp;
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
<!-- Tab Page 5 Content End -->

	</td>
  </tr>
</tbody>
</table>

<table cellspacing="1" cellpadding="0" border="0" style="position: absolute; top: 400px; left: 0px;">
	<tr>
		<td width="100%"></td>
		<td><input type=button id="btnOk" class=btn value="确 定" onclick="jscript: okOnClick();">&nbsp;&nbsp;&nbsp;</td>
		<td><input type=button id="btnCancel" class=btn value="取 消" onclick="jscript: cancelOnClick();">&nbsp;&nbsp;&nbsp;</td>

	</tr>
</table>
</BODY>
</HTML>
