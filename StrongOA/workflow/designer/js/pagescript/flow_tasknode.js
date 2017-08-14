var currentStatus = window.dialogArguments.currentStatus;
var opener = window.dialogArguments;

var selectedSection = "task";

var aa = opener.fSelectedObj;
   
function getInitData(){
   	var returnValue;
   	if(selectedSection == "task"){
		returnValue = document.getElementsByName("handleactor")[0].value;
	}else if(selectedSection == "reassign"){
		returnValue = document.getElementsByName("reassignActor")[0].value;
	}
	return (returnValue == null || returnValue == "") ? [] : returnValue.split("|");
}
   
function setSelectedData(selectedData){
   	var returnValue = selectedData.join("|");

	if(selectedSection == "task"){
		document.getElementsByName("handleactor")[0].value = returnValue;
		tempOwner = returnValue;
		selectObj = document.getElementById("realActors");
	}else if(selectedSection == "reassign"){
		document.getElementsByName("reassignActor")[0].value = returnValue;
		tempDesigner = returnValue;
		selectObj = document.getElementById("reassignSelect");
	}
	
	while(selectObj.options.length > 0){
		selectObj.options[0] = null;
	}
	
	/**
	 * 不选择人的情况 
	 */
	if(returnValue != ""){
		for(var i = 0; i < selectedData.length; i++){
			var name = selectedData[i].split(",")[1];
					var op = new Option(name, name);
					selectObj.options.add(op, selectObj.options.length);
		}
	}
}

// 指定任务处理人
function setTaskActors(flag) {
	selectedSection = flag;
	var dispatch = "ag";
	if(flag == "task"){
		dispatch = "ag";
	}else if(flag == "reassign"){
		dispatch = "ra";
	}
	var vPageLink = scriptroot
			+ "/workflowDesign/action/userSelect!input.action?dispatch=" + dispatch;
	// var returnValue = OpenWindow(vPageLink,400,450,window);
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:700px;dialogHeight:540px;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
}

// 指定表单
function setForms() {
	if (document.getElementsByName("setform")[0].disabled != "") {
		return false;
	}
	var vPageLink = scriptroot
			+ "/workflowDesign/action/processDesign!allForms.action?formId="
			+ document.getElementsByName("formid")[0].value;
	// var returnValue = OpenWindow(vPageLink,190,260,window);
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:250pt;dialogHeight:300pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if (returnValue != null) {
		var returnits = returnValue.split(",");
		var obj = document.getElementsByName("togetherform")[0];
		obj.value = returnits[1];
		var obj = document.getElementsByName("formid")[0];
		obj.value = returnits[0];
		var obj = document.getElementsByName("formpriv")[0];
		obj.value = "superadmin";
	}
}

// 设置权限
function setPrivs() {
	if (document.getElementsByName("setpriv")[0].disabled != "") {
		return false;
	}
	if (document.getElementsByName("formid")[0].value == ""
			|| document.getElementsByName("formid")[0] == null) {
		alert("请先选择要挂接的表单!");
		return false;
	}
	var obj = document.getElementsByName("formpriv")[0];
	obj.value = "superadmin";
}

function iniAttributeDialog() {
	if (opener.fSelectedObj != null) {
		document.getElementsByName("nodeId")[0].value = opener.fSelectedObj.id;
		document.getElementsByName("nodeLabel")[0].value = opener.fSelectedObj.nodeclass.thisCaption;
		document.getElementsByName("leftlocate")[0].value = opener.fSelectedObj.style.posLeft;
		document.getElementsByName("toplocate")[0].value = opener.fSelectedObj.style.posTop;
		document.getElementsByName("taskname")[0].value = opener.fSelectedObj.taskName;
		document.getElementsByName("taskstartaction")[0].value = opener.fSelectedObj.taskStart;
		document.getElementsByName("taskendaction")[0].value = opener.fSelectedObj.taskEnd;
		document.getElementsByName("nodeleaveaction")[0].value = opener.fSelectedObj.nodeLeave;
		document.getElementsByName("nodeenteraction")[0].value = opener.fSelectedObj.nodeEnter;
		
		//zw	modify by caidw
		if(opener.fSelectedObj.eaNodeEnterName == null && opener.fSelectedObj.eaNodeEnterName == undefined){
			document.getElementsByName("node-enter-name")[0].value ="";
		}else{
			document.getElementsByName("node-enter-name")[0].value = opener.fSelectedObj.eaNodeEnterName;
		}
		
		if(opener.fSelectedObj.eaNodeLeaveName == null && opener.fSelectedObj.eaNodeLeaveName == undefined){
			document.getElementsByName("node-leave-name")[0].value = "";
		}else{
			document.getElementsByName("node-leave-name")[0].value = opener.fSelectedObj.eaNodeLeaveName;
		}
		
		if(opener.fSelectedObj.eaTaskStartName == null && opener.fSelectedObj.eaTaskStartName == undefined){
			document.getElementsByName("task-start-name")[0].value = "";
		}else{
			document.getElementsByName("task-start-name")[0].value = opener.fSelectedObj.eaTaskStartName;
		}
		
		if(opener.fSelectedObj.eaTaskEndName == null && opener.fSelectedObj.eaTaskEndName == undefined){
			document.getElementsByName("task-end-name")[0].value ="";
		}else{
			document.getElementsByName("task-end-name")[0].value = opener.fSelectedObj.eaTaskEndName;
		}
		document.getElementsByName("handleactor")[0].value = opener.fSelectedObj.handleActor;
		// document.getElementsByName("realActors")[0].value=opener.fSelectedObj.realActors;
		document.getElementsByName("togetherform")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[0];
		document.getElementsByName("formid")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[1];
		document.getElementsByName("formpriv")[0].value = opener.fSelectedObj.formPriv;
		document.getElementsByName("actionSet")[0].value = opener.fSelectedObj.actionSet;
		document.getElementsByName("noApprove")[0].value = opener.fSelectedObj.noApprove;
		document.getElementsByName("maxActors")[0].value = opener.fSelectedObj.maxActors;
		document.getElementsByName("reassignActor")[0].value = opener.fSelectedObj.reassignActor;

		
		
		// word控件权限设置
		if (opener.fSelectedObj.wordPrivil != "") {
			var privils = opener.fSelectedObj.wordPrivil.split(",");
			for (var i = 0; i < privils.length; i++) {
				if (privils[i] == "1") {
					if(document.getElementsByName("wordPrivil")[i] != null)
					document.getElementsByName("wordPrivil")[i].checked = true;
				}
			}
		}

		// 前置迁移类型
		if (opener.fSelectedObj.preTranType == "1") {
			document.getElementsByName("preTranType")[1].checked = "true";
		} else {
			document.getElementsByName("preTranType")[0].checked = "true";
		}
		// 后置迁移类型
		if (opener.fSelectedObj.nextTranType == "1") {
			document.getElementsByName("nextTranType")[1].checked = "true";
		} else {
			document.getElementsByName("nextTranType")[0].checked = "true";
		}

		// 会签类型
		if (opener.fSelectedObj.counterSignType == "1") {
			document.getElementsByName("counterSignType")[1].checked = "true";
		} else {
			document.getElementsByName("counterSignType")[0].checked = "true";
		}

		if (opener.fSelectedObj.isActiveactor == "1") {
			document.getElementsByName("isActiveactor")[0].checked = "true";
			if (opener.fSelectedObj.canSelectOther == "1") {
				document.getElementsByName("canSelectOther")[0].checked = "true";
			}
		} else {
			document.getElementsByName("canSelectOther")[0].disabled = "true";
		}

		//TODO
		if (opener.fSelectedObj.reAssign == "1") {
			document.getElementsByName("reAssign")[0].checked = "true";
			if (opener.fSelectedObj.reAssignmore == "1") {
				document.getElementsByName("reAssignmore")[0].checked = "true";
			}
		} else {
			document.getElementsByName("reAssignmore")[0].disabled = "true";
			document.getElementsByName("reassignChoose")[0].disabled = "true";
		}

		if (opener.fSelectedObj.isCounterSign == "1") {
			document.getElementsByName("iscountersign")[0].checked = "true";
			document.getElementsByName("noApprove")[0].disabled = "";
			document.getElementsByName("counterSignType")[0].disabled = "";
			document.getElementsByName("counterSignType")[1].disabled = "";
			document.getElementsByName("isDocEdit")[0].disabled = "";
		} else if (opener.fSelectedObj.isCounterSign == "0") {
			document.getElementsByName("noApprove")[0].disabled = "true";
			document.getElementsByName("counterSignType")[0].disabled = "true";
			document.getElementsByName("counterSignType")[1].disabled = "true";
			document.getElementsByName("isDocEdit")[0].disabled = "true";
		}

		if (opener.fSelectedObj.taskStartMail == "1") {
			document.getElementsByName("taskStartMail")[0].checked = "true";
		}

		if (opener.fSelectedObj.taskEndMail == "1") {
			document.getElementsByName("taskEndMail")[0].checked = "true";
		}

		if (opener.fSelectedObj.taskStartMes == "1") {
			document.getElementsByName("taskStartMes")[0].checked = "true";
		}

		if (opener.fSelectedObj.taskEndMes == "1") {
			document.getElementsByName("taskEndMes")[0].checked = "true";
		}

		if (opener.fSelectedObj.isDocEdit == "1") {
			document.getElementsByName("isDocEdit")[0].checked = "true";
		} else if (opener.fSelectedObj.isDocEdit == "0") {
			document.getElementsByName("isDocEdit")[0].checked = "";
		}

		if (opener.fSelectedObj.isSetAction == "1") {
			document.getElementsByName("isSetAction")[0].checked = "true";
			isSetAction();
		} else {
			document.getElementsByName("isSetAction")[0].checked = "";
			isSetAction();
		}
		document.getElementsByName("nodeType")[0].value = "任务节点";
		// 重新指派
		var reassignSelect = document.getElementById("reassignSelect");
		if (opener.fSelectedObj.reassignActor != "") {
			var designerDetails;
			var designers = opener.fSelectedObj.reassignActor.split("|");
			for (var i = 0; i < designers.length; i++) {
				designerDetails = designers[i].split(",");
				var objOption = new Option(designerDetails[1],
						designerDetails[1]);
				reassignSelect.options.add(objOption,
						reassignSelect.options.length);
			}
		}

		// 任务处理人
		var realActors = document.getElementById("realActors");
		if (opener.fSelectedObj.handleActor != "") {
			var designerDetails;
			var designers = opener.fSelectedObj.handleActor.split("|");
			for (var i = 0; i < designers.length; i++) {
				designerDetails = designers[i].split(",");
				var objOption = new Option(designerDetails[1],
						designerDetails[1]);
				realActors.options
						.add(objOption, reassignSelect.options.length);
			}
		}

		// 任务定时器设置
		var timerSet = opener.fSelectedObj.doc_timerSet.split(",");
		document.getElementsByName("doc_timer_data")[0].value = timerSet[0];
		document.getElementsByName("doc_timer_init")[0].value = timerSet[1];
		document.getElementsByName("pre_timer_data")[0].value = timerSet[2];
		document.getElementsByName("pre_timer_init")[0].value = timerSet[3];
		document.getElementsByName("re_timer_data")[0].value = timerSet[4];
		document.getElementsByName("re_timer_init")[0].value = timerSet[5];
		if (timerSet[6] == '1') {
			document.getElementsByName("mail_timer")[0].checked = true;
		} else {
			document.getElementsByName("mail_timer")[0].checked = false;
		}
		if (timerSet[7] == '1') {
			document.getElementsByName("notice_timer")[0].checked = true;
		} else {
			document.getElementsByName("notice_timer")[0].checked = false;
		}
		if (timerSet[8] == '1') {
			document.getElementsByName("mes_timer")[0].checked = true;
		} else {
			document.getElementsByName("mes_timer")[0].checked = false;
		}
		if (timerSet[9] == '1') {
			document.getElementsByName("rtx_timer")[0].checked = true;
		} else {
			document.getElementsByName("rtx_timer")[0].checked = false;
		}
		if (timerSet[10] == '1') {
			document.getElementsByName("handler_notice")[0].checked = true;
		} else {
			document.getElementsByName("handler_notice")[0].checked = false;
		}
		if (timerSet[11] == '1') {
			document.getElementsByName("start_notice")[0].checked = true;
		} else {
			document.getElementsByName("start_notice")[0].checked = false;
		}
		if (timerSet[12] == '1') {
			document.getElementsByName("owner_notice")[0].checked = true;
		} else {
			document.getElementsByName("owner_notice")[0].checked = false;
		}
		if (opener.fSelectedObj.doc_isTimer == "1") {
			document.getElementsByName("doc_isTimer")[0].checked = true;
		} else {
			document.getElementsByName("doc_isTimer")[0].checked = false;
			setIsTimer();
		}

		// 初始化节点插件信息
		var $plugins = $("[name^=" + pluginNamePrefix + "]");
		$.each($plugins, function(index, domElement) {
			$(domElement).val(opener.fSelectedObj.plugins[domElement.name]);
		});
		
		if (typeof(initPlugins) != "undefined") {// 用户需要在页面重载的方法，初始化节点插件信息，主要是一些插件属性的展现方式
			initPlugins();
		}
	}
}

function setIsTimer() {
	if (document.getElementsByName("doc_isTimer")[0].checked == true
			&& currentStatus != "edit") {
		document.getElementsByName("doc_timer_data")[0].disabled = false;
		document.getElementsByName("doc_timer_init")[0].disabled = false;
		document.getElementsByName("pre_timer_data")[0].disabled = false;
		document.getElementsByName("pre_timer_init")[0].disabled = false;
		document.getElementsByName("re_timer_data")[0].disabled = false;
		document.getElementsByName("re_timer_init")[0].disabled = false;
		document.getElementsByName("mail_timer")[0].disabled = false;
		document.getElementsByName("notice_timer")[0].disabled = false;
		document.getElementsByName("mes_timer")[0].disabled = false;
		document.getElementsByName("rtx_timer")[0].disabled = false;
		document.getElementsByName("owner_notice")[0].disabled = false;
		document.getElementsByName("start_notice")[0].disabled = false;
		document.getElementsByName("handler_notice")[0].disabled = false;
	} else {
		document.getElementsByName("doc_timer_data")[0].disabled = true;
		document.getElementsByName("doc_timer_init")[0].disabled = true;
		document.getElementsByName("pre_timer_data")[0].disabled = true;
		document.getElementsByName("pre_timer_init")[0].disabled = true;
		document.getElementsByName("re_timer_data")[0].disabled = true;
		document.getElementsByName("re_timer_init")[0].disabled = true;
		document.getElementsByName("mail_timer")[0].disabled = true;
		document.getElementsByName("notice_timer")[0].disabled = true;
		document.getElementsByName("mes_timer")[0].disabled = true;
		document.getElementsByName("rtx_timer")[0].disabled = true;
		document.getElementsByName("owner_notice")[0].disabled = true;
		document.getElementsByName("start_notice")[0].disabled = true;
		document.getElementsByName("handler_notice")[0].disabled = true;
	}
}

function okOnClick() {
	// 验证录入正确性
	var isValidated = validate();
	// 如果验证不通过，则返回，不继续执行以下逻辑
	if(!isValidated){
		return ;
	}
	// 如果验证通过，则先执行自定义逻辑，再执行本地逻辑
	var isSaved = true;
	if (typeof(onSave) != "undefined") {
		isSaved = onSave.call(this);
	}
	// 如果自定义逻辑执行不通过，则返回，不继续执行本地逻辑
	if(!isSaved){
		return ;
	}
	
	var opener = window.dialogArguments;
	var nodeEnterEvent = null;
	var nodeLeaveEvent = null;
	var timerCreateEvent = null;
	var taskStartEvent = null;
	var taskEndEvent = null;
	var task = null;
	var timer = null;
	// try{
	var nodename = document.getElementsByName("nodeLabel")[0].value;
	var thisnode = handNodeNameOnSave(nodename);
	
	// 预先查找该任务下存在的事件
	var events = thisnode.getElementsByTagName("event");
	for (var i = 0; i < events.length; i++) {
		if (events[i].getAttributeNode("type") != null
				&& events[i].getAttributeNode("type").value == "node-enter") {
			nodeEnterEvent = events[i];
		} else if (events[i].getAttributeNode("type") != null
				&& events[i].getAttributeNode("type").value == "node-leave") {
			nodeLeaveEvent = events[i];
		} else if (events[i].getAttributeNode("type") != null
				&& events[i].getAttributeNode("type").value == "task-start") {
			taskStartEvent = events[i];
		} else if (events[i].getAttributeNode("type") != null
				&& events[i].getAttributeNode("type").value == "task-end") {
			taskEndEvent = events[i];
		} else if (events[i].getAttributeNode("type") != null
				&& events[i].getAttributeNode("type").value == "timer-create") {
			timerCreateEvent = events[i];
		}
	}

	task = thisnode.getElementsByTagName("task")[0];
	task.getAttributeNode("name").value = document
			.getElementsByName("taskname")[0].value;

	// 预先查找该任务下的定时器信息
	var timers = task.getElementsByTagName("timer");
	for (var i = 0; i < timers.length; i++) {
		if (timers[i].getAttributeNode("name") != null
				&& timers[i].getAttributeNode("name").value == "com.strongit.timer") {
			timer = timers[i];
			break;
		}

	}

	// 会签属性设置
	if (document.getElementsByName("iscountersign")[0].checked) {
		opener.fSelectedObj.isCounterSign = "1";
		if (thisnode.getAttributeNode("signal") != null) {
			thisnode.getAttributeNode("signal").value = "last-wait";
		} else {
			var r = opener.doc.createAttribute("signal");
			r.value = "last-wait";
			thisnode.setAttributeNode(r);
		}
		if (thisnode.getAttributeNode("create-tasks") != null) {
			thisnode.getAttributeNode("create-tasks").value = "false";
		} else {
			var r = opener.doc.createAttribute("create-tasks");
			r.value = "false";
			thisnode.setAttributeNode(r);
		}
		// 会签任务信息
		var assigns = task.getElementsByTagName("assignment");
		for (var i = 0; i < assigns.length; i++) {
			assigns[i].parentNode.removeChild(assigns[i]);
		}
		if (taskEndEvent != null) {
			var k = 1;
			var actions = taskEndEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.counterSignName) {
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n2 = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;
				// 添加属性
				n2.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.counterSignName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = opener.counterSignClass;
				n2.setAttributeNode(r);
				taskEndEvent.appendChild(n2);
			}
		} else {
			taskEndEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			taskEndEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "task-end";
			taskEndEvent.setAttributeNode(r);
			var n2 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n2.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.counterSignName;
			n2.setAttributeNode(r);
			var r = opener.doc.createAttribute("class");
			r.value = opener.counterSignClass;
			n2.setAttributeNode(r);
			taskEndEvent.appendChild(n2);
			task.appendChild(taskEndEvent);
		}

		if (nodeEnterEvent != null) {
			var k = 1;
			var actions = nodeEnterEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.initCounterSignName) {
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n2 = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;
				// 添加属性
				n2.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.initCounterSignName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = opener.initCounterSignClass;
				n2.setAttributeNode(r);
				nodeEnterEvent.appendChild(n2);//会签节点进入事件永远放在最后面
			}
		} else {
			nodeEnterEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			nodeEnterEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "node-enter";
			nodeEnterEvent.setAttributeNode(r);
			var n2 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n2.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.initCounterSignName;
			n2.setAttributeNode(r);
			var r = opener.doc.createAttribute("class");
			r.value = opener.initCounterSignClass;
			n2.setAttributeNode(r);
			nodeEnterEvent.appendChild(n2);
			thisnode.appendChild(nodeEnterEvent);
		}
	} else {// 非会签
		if (thisnode.getAttributeNode("signal") != null) {
			thisnode.removeAttribute("signal");
		}
		if (thisnode.getAttributeNode("create-tasks") != null) {
			thisnode.removeAttribute("create-tasks");
		}
		if (taskEndEvent != null) {
			var actions = taskEndEvent.getElementsByTagName("action");
			for (var k = 0; k < actions.length; k++) {
				if (actions[k].getAttributeNode("name").value == opener.counterSignName) {
					actions[k].parentNode.removeChild(actions[k]);
					break;
				}
			}
		}
		if (nodeEnterEvent != null) {
			var actions = nodeEnterEvent.getElementsByTagName("action");
			for (var k = 0; k < actions.length; k++) {
				if (actions[k].getAttributeNode("name").value == opener.initCounterSignName) {
					actions[k].parentNode.removeChild(actions[k]);
					break;
				}
			}
		}
		opener.fSelectedObj.isCounterSign = "0";
		// 非会签任务信息
		var assigns = task.getElementsByTagName("assignment");
		if (assigns.length < 1) {
			var n1 = opener.doc.createNode(1, "assignment", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("class");
			r.value = opener.taskAssignClass;
			n1.setAttributeNode(r);
			task.appendChild(n1);
		}
	}
	
	//zw
	//设置节点进入的事件动作名称
	var eaNodeEnterName = document.getElementsByName("node-enter-name")[0].value;
	opener.fSelectedObj.eaNodeEnterName = "";
	if(eaNodeEnterName != "" && eaNodeEnterName != null && eaNodeEnterName != undefined)
		opener.fSelectedObj.eaNodeEnterName = eaNodeEnterName;
	
	//设置节点离开的事件动作名称
	var eaNodeLeaveName = document.getElementsByName("node-leave-name")[0].value;
	opener.fSelectedObj.eaNodeLeaveName = "";
	if(eaNodeLeaveName != "" && eaNodeLeaveName != null && eaNodeLeaveName != undefined)
		opener.fSelectedObj.eaNodeLeaveName = eaNodeLeaveName;
	
	//设置任务开始的事件动作名称
	var eaTaskStartName = document.getElementsByName("task-start-name")[0].value;
	opener.fSelectedObj.eaTaskStartName = "";
	if(eaTaskStartName != "" && eaTaskStartName != null && eaTaskStartName != undefined)
		opener.fSelectedObj.eaTaskStartName = eaTaskStartName;
	
	//设置任务结束的事件动作名称
	var eaTaskEndName = document.getElementsByName("task-end-name")[0].value;
	opener.fSelectedObj.eaTaskEndName = "";
	if(eaTaskEndName != "" && eaTaskEndName != null && eaTaskEndName != undefined)
		opener.fSelectedObj.eaTaskEndName = eaTaskEndName;
	
	// 设置节点进入操作
	var nodeenter = document.getElementsByName("nodeenteraction")[0].value;
	if (nodeenter != null && "" != nodeenter) {
		opener.fSelectedObj.nodeEnter = nodeenter;
		if (nodeEnterEvent != null) {
			var k = 1;
			var actions = nodeEnterEvent.getElementsByTagName("action");
			var action = null;//会签节点进入事件存储变量
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeEnterName) {
					actions[i].parentNode.removeChild(actions[i]);
					continue;
				}
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.initCounterSignName) {//查找会签节点进入事件
					action = actions[i];
				}
			}
			var n2 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n2.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.nodeEnterName;
			n2.setAttributeNode(r);
			var r = opener.doc.createAttribute("class");
			r.value = nodeenter;
			n2.setAttributeNode(r);
			nodeEnterEvent.insertBefore(n2, action);//该节点为会签节点，则需要将节点进入事件加到会签节点进入事件之前
		} else {
			nodeEnterEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			nodeEnterEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "node-enter";
			nodeEnterEvent.setAttributeNode(r);
			var n1 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.nodeEnterName;
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("class");
			r.value = nodeenter;
			n1.setAttributeNode(r);
			nodeEnterEvent.appendChild(n1);
			thisnode.appendChild(nodeEnterEvent);
		}
	} else {
		opener.fSelectedObj.nodeEnter = "";
		if (nodeEnterEvent != null) {
			var actions = nodeEnterEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeEnterName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if (nodeEnterEvent != null && nodeEnterEvent.childNodes.length == 0) {
			nodeEnterEvent.parentNode.removeChild(nodeEnterEvent);
		}
	}

	// 设置节点离开操作
	var nodeleave = document.getElementsByName("nodeleaveaction")[0].value;
	if (nodeleave != null && "" != nodeleave) {
		opener.fSelectedObj.nodeLeave = nodeleave;
		if (nodeLeaveEvent != null) {
			var k = 1;
			var actions = nodeLeaveEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeLeaveName) {
					actions[i].getAttributeNode("class").value = nodeleave;
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n2 = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;
				// 添加属性
				n2.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.nodeLeaveName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = nodeleave;
				n2.setAttributeNode(r);
				nodeLeaveEvent.appendChild(n2);
			}
		} else {
			nodeLeaveEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			nodeLeaveEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "node-leave";
			nodeLeaveEvent.setAttributeNode(r);
			var n1 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.nodeLeaveName;
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("class");
			r.value = nodeleave;
			n1.setAttributeNode(r);
			nodeLeaveEvent.appendChild(n1);
			thisnode.appendChild(nodeLeaveEvent);
		}
	} else {
		opener.fSelectedObj.nodeLeave = "";
		if (nodeLeaveEvent != null) {
			var actions = nodeLeaveEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeLeaveName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if (nodeLeaveEvent != null && nodeLeaveEvent.childNodes.length == 0) {
			nodeLeaveEvent.parentNode.removeChild(nodeLeaveEvent);
		}
	}

	// 任务开始动作设置
	var taskstart = document.getElementsByName("taskstartaction")[0].value;
	if (taskstart != null && "" != taskstart) {
		opener.fSelectedObj.taskStart = taskstart;
		if (taskStartEvent != null) {
			var k = 1;
			var actions = taskStartEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.taskStartCustomName) {
					actions[i].getAttributeNode("class").value = taskstart;
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n2 = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;
				// 添加属性
				n2.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.taskStartCustomName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = taskstart;
				n2.setAttributeNode(r);
				taskStartEvent.appendChild(n2);
			}
		} else {
			taskStartEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			taskStartEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "task-start";
			taskStartEvent.setAttributeNode(r);
			var n1 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.taskStartCustomName;
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("class");
			r.value = taskstart;
			n1.setAttributeNode(r);
			taskStartEvent.appendChild(n1);
			task.appendChild(taskStartEvent);
		}
	} else {
		opener.fSelectedObj.taskStart = "";
		if (taskStartEvent != null) {
			var actions = taskStartEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.taskStartCustomName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if (taskStartEvent != null && taskStartEvent.childNodes.length == 0) {
			taskStartEvent.parentNode.removeChild(taskStartEvent);
		}
	}
	// 任务结束动作设置
	var taskend = document.getElementsByName("taskendaction")[0].value;
	if (taskend != null && "" != taskend) {
		opener.fSelectedObj.taskEnd = taskend;
		if (taskEndEvent != null) {
			var k = 1;
			var actions = taskEndEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.taskEndCustomName) {
					actions[i].getAttributeNode("class").value = taskend;
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n2 = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;
				// 添加属性
				n2.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.taskEndCustomName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = taskend;
				n2.setAttributeNode(r);
				taskEndEvent.appendChild(n2);
			}
		} else {
			taskEndEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			taskEndEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "task-end";
			taskEndEvent.setAttributeNode(r);
			var n1 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.taskEndCustomName;
			n1.setAttributeNode(r);

			var r = opener.doc.createAttribute("class");
			r.value = taskend;
			n1.setAttributeNode(r);
			taskEndEvent.appendChild(n1);
			task.appendChild(taskEndEvent);
		}
	} else {
		opener.fSelectedObj.taskEnd = "";
		if (taskEndEvent != null) {
			var actions = taskEndEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.taskEndCustomName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if (taskEndEvent != null && taskEndEvent.childNodes.length == 0) {
			taskEndEvent.parentNode.removeChild(taskEndEvent);
		}
	}

	opener.fSelectedObj.nodeForm = document.getElementsByName("togetherform")[0].value
			+ "," + document.getElementsByName("formid")[0].value;
	opener.fSelectedObj.formPriv = document.getElementsByName("formpriv")[0].value;
	opener.fSelectedObj.taskName = document.getElementsByName("taskname")[0].value;
	opener.fSelectedObj.handleActor = document.getElementsByName("handleactor")[0].value;
	// opener.fSelectedObj.realActors=document.getElementsByName("realActors")[0].value;
	opener.fSelectedObj.taskStart = document
			.getElementsByName("taskstartaction")[0].value;
	opener.fSelectedObj.taskEnd = document.getElementsByName("taskendaction")[0].value;
	opener.fSelectedObj.maxActors = document.getElementsByName("maxActors")[0].value;
	opener.fSelectedObj.reassignActor = document
			.getElementsByName("reassignActor")[0].value;
	if (document.getElementsByName("isSetAction")[0].checked) {
		opener.fSelectedObj.isSetAction = "1";
	} else {
		opener.fSelectedObj.isSetAction = "0";
	}
	opener.fSelectedObj.actionSet = document.getElementsByName("actionSet")[0].value;
	opener.fSelectedObj.noApprove = document.getElementsByName("noApprove")[0].value;

	if (document.getElementsByName("preTranType")[1].checked) {
		opener.fSelectedObj.preTranType = "1";
	} else {
		opener.fSelectedObj.preTranType = "0";
	}

	if (document.getElementsByName("nextTranType")[1].checked) {
		opener.fSelectedObj.nextTranType = "1";
	} else {
		opener.fSelectedObj.nextTranType = "0";
	}

	if (document.getElementsByName("counterSignType")[1].checked) {
		opener.fSelectedObj.counterSignType = "1";
	} else {
		opener.fSelectedObj.counterSignType = "0";
	}

	if (document.getElementsByName("isActiveactor")[0].checked) {
		opener.fSelectedObj.isActiveactor = "1";
	} else {
		opener.fSelectedObj.isActiveactor = "0";
	}

	if (document.getElementsByName("reAssign")[0].checked) {
		opener.fSelectedObj.reAssign = "1";
	} else {
		opener.fSelectedObj.reAssign = "0";
	}

	if (document.getElementsByName("reAssignmore")[0].checked) {
		opener.fSelectedObj.reAssignmore = "1";
	} else {
		opener.fSelectedObj.reAssignmore = "0";
	}

	if (document.getElementsByName("canSelectOther")[0].checked) {
		opener.fSelectedObj.canSelectOther = "1";
	} else {
		opener.fSelectedObj.canSelectOther = "0";
	}

	if (document.getElementsByName("taskStartMail")[0].checked) {
		opener.fSelectedObj.taskStartMail = "1";
	} else {
		opener.fSelectedObj.taskStartMail = "0";
	}

	if (document.getElementsByName("taskEndMail")[0].checked) {
		opener.fSelectedObj.taskEndMail = "1";
	} else {
		opener.fSelectedObj.taskEndMail = "0";
	}

	if (document.getElementsByName("taskStartMes")[0].checked) {
		opener.fSelectedObj.taskStartMes = "1";
	} else {
		opener.fSelectedObj.taskStartMes = "0";
	}

	if (document.getElementsByName("taskEndMes")[0].checked) {
		opener.fSelectedObj.taskEndMes = "1";
	} else {
		opener.fSelectedObj.taskEndMes = "0";
	}

	if (document.getElementsByName("isDocEdit")[0].checked) {
		opener.fSelectedObj.isDocEdit = "1";
	} else {
		opener.fSelectedObj.isDocEdit = "0";
	}

	// 定时器功能设置
	var timerSet = document.getElementsByName("doc_timer_data")[0].value + ","
			+ document.getElementsByName("doc_timer_init")[0].value + ","
			+ document.getElementsByName("pre_timer_data")[0].value + ","
			+ document.getElementsByName("pre_timer_init")[0].value + ","
			+ document.getElementsByName("re_timer_data")[0].value + ","
			+ document.getElementsByName("re_timer_init")[0].value + ",";
	if (document.getElementsByName("mail_timer")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("notice_timer")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("mes_timer")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("rtx_timer")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("handler_notice")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("start_notice")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}
	if (document.getElementsByName("owner_notice")[0].checked == true) {
		timerSet = timerSet + "1" + ",";
	} else {
		timerSet = timerSet + "0" + ",";
	}

	opener.fSelectedObj.doc_timerSet = timerSet;

	if (document.getElementsByName("doc_isTimer")[0].checked == true) {
		opener.fSelectedObj.doc_isTimer = "1";
	} else {
		opener.fSelectedObj.doc_isTimer = "0";
	}

	// 增加任务定时器功能
	if (opener.fSelectedObj.doc_isTimer == "1") {
		var timerSet = opener.fSelectedObj.doc_timerSet.split(",");
		var wholeLast;
		var preLast;
		// 流程持续时间换算成分钟
		if (timerSet[1] == "day") {
			wholeLast = parseInt(timerSet[0]) * 24 * 60;
		} else if (timerSet[1] == "hour") {
			wholeLast = parseInt(timerSet[0]) * 60;
		} else if (timerSet[1] == "minute") {
			wholeLast = parseInt(timerSet[0]);
		}
		// 提前催办时间换算成分钟
		if (timerSet[3] == "day") {
			preLast = parseInt(timerSet[2]) * 24 * 60;
		} else if (timerSet[3] == "hour") {
			preLast = parseInt(timerSet[2]) * 60;
		} else if (timerSet[3] == "minute") {
			preLast = parseInt(timerSet[2]);
		}
		var duration = wholeLast - preLast;
		// 起始点进入事件已存在
		if (timer != null) {
			timer.getAttributeNode("duedate").value = duration + " minute";
			// 判断是否需要重复催办
			if (timerSet[4] != "0") {
				
				//判断repeat节点是否存在，若不存在，则新增repeat节点
				var r = timer.getAttributeNode("repeat");
				if(r == null){
					r = opener.doc.createAttribute("repeat");
					timer.setAttributeNode(r);
				}
				timer.getAttributeNode("repeat").value = timerSet[4] + " "
						+ timerSet[5];
			} else {
				timer.removeAttribute("repeat");
			}

			var action = timer.getElementsByTagName("action")[0];
			action.getAttributeNode("class").value = opener.timerAction;
		} else {
			timer = opener.doc.createNode(1, "timer", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			timer.setAttributeNode(r);

			r = opener.doc.createAttribute("name");
			r.value = "com.strongit.timer";
			timer.setAttributeNode(r);
			r = opener.doc.createAttribute("duedate");
			r.value = duration + " minute";
			timer.setAttributeNode(r);
			// 判断是否需要重复催办
			if (timerSet[4] != "0") {
				r = opener.doc.createAttribute("repeat");
				r.value = timerSet[4] + " " + timerSet[5];
				timer.setAttributeNode(r);
			}
			var n2 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n2.setAttributeNode(r);

			r = opener.doc.createAttribute("class");
			r.value = opener.timerAction;
			n2.setAttributeNode(r);
			timer.appendChild(n2);
			task.appendChild(timer);
		}

		// Timer-Create事件已存在
		if (timerCreateEvent != null) {
			var k = 1;
			var createActions = timerCreateEvent
					.getElementsByTagName("action");
			for (var i = 0; i < createActions.length; i++) {
				if (createActions[i].getAttributeNode("name") != null
						&& createActions[i].getAttributeNode("name").value == opener.createTimerName) {
					createActions[i].getAttributeNode("class").value = opener.createTimerClass;
					k = 0;
					break;
				}
			}
			if (k == 1) {
				var n = opener.doc.createNode(1, "action", "");

				// 添加xml节点标识属性
				var r = opener.doc.createAttribute("xmlflag");
				r.value = opener.nCtlNum++;

				// 添加属性
				n.setAttributeNode(r);

				var r = opener.doc.createAttribute("name");
				r.value = opener.createTimerName;
				n.setAttributeNode(r);
				r = opener.doc.createAttribute("class");
				r.value = opener.createTimerClass;
				n.setAttributeNode(r);
				timerCreateEvent.appendChild(n);
			}
		} else {
			timerCreateEvent = opener.doc.createNode(1, "event", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			timerCreateEvent.setAttributeNode(r);

			var r = opener.doc.createAttribute("type");
			r.value = "timer-create";
			timerCreateEvent.setAttributeNode(r);
			var n2 = opener.doc.createNode(1, "action", "");

			// 添加xml节点标识属性
			var r = opener.doc.createAttribute("xmlflag");
			r.value = opener.nCtlNum++;
			// 添加属性
			n2.setAttributeNode(r);

			var r = opener.doc.createAttribute("name");
			r.value = opener.createTimerName;
			n2.setAttributeNode(r);

			r = opener.doc.createAttribute("class");
			r.value = opener.createTimerClass;
			n2.setAttributeNode(r);
			timerCreateEvent.appendChild(n2);
			task.appendChild(timerCreateEvent);
		}
	} else {
		if (timer != null) {
			timer.parentNode.removeChild(timer);
		}
		// 删除该事件
		if (timerCreateEvent != null) {
			var actions = timerCreateEvent.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.createTimerName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		if (timerCreateEvent != null && timerCreateEvent.childNodes.length == 0) {
			timerCreateEvent.parentNode.removeChild(timerCreateEvent);
		}
	}

	// word控件权限设置
	var privil = "";
	var privils = document.getElementsByName("wordPrivil");
	for (var i = 0; i < privils.length; i++) {
		if (privils[i].checked == true) {
			privil = privil + ",1";
		} else {
			privil = privil + ",0";
		}
	}
	opener.fSelectedObj.wordPrivil = privil.substring(1);
	
	if(typeof(handlePlugins) != "undefined"){//处理节点插件信息方法，用户需重载此方法，主要处理插件信息由展现信息到具体后台值
		handlePlugins();
	}

	// 节点插件信息
	var $plugins = $("[name^=" + pluginNamePrefix + "]");
	var plugins = [];
	$.each($plugins, function(index, domElement) {
		plugins[domElement.name] = $(domElement).val();
	});
	opener.fSelectedObj.plugins = plugins;
//debugger;
	window.close();
	opener.movecontrolSubject.notifyObservers();
}

function cancelOnClick() {
	// 先执行自定义逻辑，再执行本地逻辑
	var isCanceled = true;
	if (typeof(onCancel) != "undefined") {
		isCanceled = onCancel.call(this);
	}
	// 如果自定义逻辑执行不通过，则返回，不继续执行本地逻辑
	if(!isCanceled){
		return ;
	}
	
	window.close();
}

function counterSign() {
	var cs = document.getElementsByName("iscountersign")[0];
	if (cs.checked) {
		//document.getElementById("huiqianattr").style.display = "inline";
		document.getElementsByName("noApprove")[0].disabled = "";
		document.getElementsByName("counterSignType")[0].disabled = "";
		document.getElementsByName("counterSignType")[1].disabled = "";
		document.getElementsByName("isDocEdit")[0].disabled = "";
	} else if (!cs.checked) {
		//document.getElementById("huiqianattr").style.display = "none";
		document.getElementsByName("noApprove")[0].disabled = "true";
		document.getElementsByName("counterSignType")[0].disabled = "true";
		document.getElementsByName("counterSignType")[1].disabled = "true";
		document.getElementsByName("isDocEdit")[0].disabled = "true";
	}
}

function activeActor() {
	var cs = document.getElementsByName("isActiveactor")[0];
	if (cs.checked) {
		// document.getElementsByName("maxActors")[0].disabled="";
		document.getElementsByName("canSelectOther")[0].disabled = "";
	} else if (!cs.checked) {
		// document.getElementsByName("maxActors")[0].disabled="true";
		document.getElementsByName("canSelectOther")[0].disabled = "true";
	}
}

function reAssign() {
	var cs = document.getElementsByName("reAssign")[0];
	if (cs.checked) {
		document.getElementsByName("reAssignmore")[0].disabled = "";
		document.getElementsByName("reassignChoose")[0].disabled = "";
		//document.getElementById("zhipai").style.display = "inline";
	} else if (!cs.checked) {
		document.getElementsByName("reAssignmore")[0].disabled = "true";
		document.getElementsByName("reassignChoose")[0].disabled = "true";
		//清除已选择指派的用户
		document.getElementsByName("reassignActor")[0].value = "";
		//document.getElementById("zhipai").style.display = "none";
	}
}

function isSetAction() {
	var sa = document.getElementsByName("isSetAction")[0];
	if (sa.checked) {
		document.getElementsByName("actionSet")[0].disabled = "";
	} else if (!sa.checked) {
		document.getElementsByName("actionSet")[0].disabled = "true";
	}
}

var all1Select = false;
var all2Select = false;
var all3Select = false;
function selectAll1() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all1Select = !all1Select;
	for (var i = 0; i < 6; i++) {
		wordPrivil[i].checked = all1Select;
	}
}
function selectAll2() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all2Select = !all2Select;
	for (var i = 6; i < 13; i++) {
		wordPrivil[i].checked = all2Select;
	}
}
function selectAll3() {
	var wordPrivil = document.getElementsByName("wordPrivil");
	all3Select = !all3Select;
	for (var i = 13; i < wordPrivil.length; i++) {
		wordPrivil[i].checked = all3Select;
	}
}

$(document).ready(function(){
	iniAttributeDialog();
});

function noApproveChoose() {
	var noApprove = document.getElementById("noApprove");
	var objid = opener.fSelectedObj.id;
	var aLines = opener.fSelectedObj.line.split(";");
	if(aLines != null){
		for( var j = 0; j < aLines.length - 1; j ++ ){
			var lineData = aLines[j].split(":");
			if(objid == lineData[1].split("TO")[0]){
				var lineId = lineData[0];
		
				var lineName = opener.document.getElementById(lineId).getAttribute("ctlName");
				var objOption = new Option(lineName,lineName);
				//document.getElementById("elseSelect").value;
				noApprove.options.add(objOption, noApprove.options.length);
			}
			
		}
	}
	
}

function checkInteger(data){
	if(data != null && data != ""){
		var numtest = /^[1-9]\d*|[0]$/;
		return numtest.test(data);	
	}else{
		return true;
	}
}
/**
 * 页面录入验证
 * 1.如果验证正确，则接口返回true
 * 2.如果验证失败，则接口直接alert错误信息，并返回false
 */
function validate(){
	var isValidated = true;
	// 对页面录入的验证
	var opener = window.dialogArguments;
	var thisnode;
	var nodename = document.getElementsByName("nodeLabel")[0].value;
	// 验证节点名称是否合法
	isValidated = validateNodeName(nodename, opener);
	
	if (isValidated && (document.getElementsByName("taskname")[0].value == null
			|| "" == document.getElementsByName("taskname")[0].value)) {
		alert("请输入任务名称。");
		isValidated = false;
	}else if(isValidated){
		var value = document.getElementsByName("taskname")[0].value;
		isValidated = checkIllegalCharacters(value);
	}
	
	var maxActors = document.getElementsByName("maxActors")[0].value;
	if(isValidated && maxActors != null && maxActors != ""){
		var numtest = /^[1-9]\d*$/;
		if(!numtest.test(maxActors)){
			alert("“最大参与人数”只能为正整数。");
			isValidated = false;
		}
	}
	
	var doc_timer_data = document.getElementsByName("doc_timer_data")[0].value;
	var pre_timer_data = document.getElementsByName("pre_timer_data")[0].value;
	var re_timer_data = document.getElementsByName("re_timer_data")[0].value;
	if(isValidated){
		if(!checkInteger(doc_timer_data)){
			alert("“任务持续时间”只能为整数。");
			isValidated = false;
		}
	}
	if(isValidated){
		if(!checkInteger(pre_timer_data)){
			alert("“第一次催办”只能为整数。");
			isValidated = false;
		}
	}
	if(isValidated){
		if(!checkInteger(re_timer_data)){
			alert("“重复催办间隔”只能为整数。");
			isValidated = false;
		}
	}
	
	if (isValidated && (document.getElementsByName("handleactor")[0].value == null
			|| "" == document.getElementsByName("handleactor")[0].value)) {
		alert("请为任务设置处理人。");
		isValidated = false;
	}

	if (isValidated && !document.getElementsByName("isSetAction")[0].checked
			&& (document.getElementsByName("togetherform")[0].value != null && document
					.getElementsByName("togetherform")[0].value != "")
			&& ("" == document.getElementsByName("formpriv")[0].value || null == document
					.getElementsByName("formpriv")[0].value)) {
		alert("请为挂接的表单设置权限。");
		isValidated = false;
	}
	
	if (isValidated && document.getElementsByName("isSetAction")[0].checked
			&& (null == document.getElementsByName("actionSet")[0].value || "" == document
					.getElementsByName("actionSet")[0].value)) {
		alert("请指定要挂接的动作。");
		isValidated = false;
	}
	
	// 会签属性设置
	if (isValidated && document.getElementsByName("iscountersign")[0].checked
			&& document.getElementsByName("noApprove")[0].value == "") {
		alert("请输入会签不通过时所选路径，若没有请输入\"无\"。");
		isValidated = false;
	}
	
	// 执行自定义验证
	if (isValidated && typeof(customValidate) != "undefined") {
		isValidated = customValidate.call(this);
	}
	return isValidated;
}