var currentStatus = window.dialogArguments.currentStatus;
var opener = window.dialogArguments;
// 指定子流程
function setProcess() {
	var vPageLink = scriptroot +"/workflowDesign/action/processDesign!subProcess.action?subProcessId=" + document.getElementsByName("subProcessId")[0].value;
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:250pt;dialogHeight:300pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnValue!=null){
		var returnits= returnValue.split(",");
		var obj=document.getElementsByName("subProcessName")[0];
		obj.value=returnits[1];	
		var obj=document.getElementsByName("subProcessId")[0];
		obj.value=returnits[0];
	}	
}

// 指定表单
function setForms(flag) {
	if (document.getElementsByName("setform")[0].disabled != "") {
		return false;
	}
	if(flag=="main"){
		vPageLink = scriptroot + "/workflowDesign/action/processDesign!allForms.action?formId=" + document.getElementsByName("formid")[0].value;
	}else if(flag=="sub"){
		vPageLink = scriptroot + "/workflowDesign/action/processDesign!allForms.action?formId=" + document.getElementsByName("subFormid")[0].value;
	}else{
		vPageLink = null;
	}
	// var returnValue = OpenWindow(vPageLink,190,260,window);
	var returnValue = window
			.showModalDialog(
					vPageLink,
					window,
					"dialogWidth:250pt;dialogHeight:300pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if (returnValue != null) {
		if (flag == "main") {
			var returnits = returnValue.split(",");
			var obj = document.getElementsByName("togetherform")[0];
			obj.value = returnits[1];
			var obj = document.getElementsByName("formid")[0];
			obj.value = returnits[0];
			var obj = document.getElementsByName("formpriv")[0];
			obj.value = "superadmin";
		} else if (flag == "sub") {
			var returnits = returnValue.split(",");
			var obj = document.getElementsByName("subProcessForm")[0];
			obj.value = returnits[1];
			var obj = document.getElementsByName("subFormid")[0];
			obj.value = returnits[0];
		}
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
		document.getElementsByName("subProcessForm")[0].value = opener.fSelectedObj.subProcessForm
				.split(",")[0];
		document.getElementsByName("subFormid")[0].value = opener.fSelectedObj.subProcessForm
				.split(",")[1];
		document.getElementsByName("subProcessName")[0].value = opener.fSelectedObj.subProcessName;
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
		
		document.getElementsByName("togetherform")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[0];
		document.getElementsByName("formid")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[1];
		document.getElementsByName("formpriv")[0].value = opener.fSelectedObj.formPriv;
		
		if(opener.fSelectedObj.subProcessName != null && opener.fSelectedObj.subProcessName != ""){//增加子流程Id号保存功能
			var subProcesses = opener.fSelectedObj.subProcessName.split(",");
			if(subProcesses.length > 1){
				document.getElementsByName("subProcessId")[0].value = subProcesses[0];
				document.getElementsByName("subProcessName")[0].value = subProcesses[1];	
			}else{//兼容旧的流程模板格式
				document.getElementsByName("subProcessName")[0].value = subProcesses[0];
			}
		}

		if (opener.fSelectedObj.synchronize != null
				&& opener.fSelectedObj.synchronize == "0") {
			document.getElementsByName("synchronize")[1].checked = true;
		} else {
			document.getElementsByName("synchronize")[0].checked = true;
		}

		if (opener.fSelectedObj.fromParent == "1") {
			document.getElementsByName("fromParent")[0].checked = "true";
		}

		if (opener.fSelectedObj.toParent == "1") {
			document.getElementsByName("toParent")[0].checked = "true";
		}

		document.getElementsByName("nodeType")[0].value = "子流程节点";
		
		// 初始化节点插件信息
		var $plugins = $("[name^=" + pluginNamePrefix + "]");
		$.each($plugins, function(index, domElement) {
			$(domElement).val(opener.fSelectedObj.plugins[domElement.name]);
		});
		
		ownedInitPlugins();
		
		if (typeof(initPlugins) != "undefined") {// 用户需要在页面重载的方法，初始化节点插件信息，主要是一些插件属性的展现方式
			initPlugins();
		}
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
	try {
		var nodename = document.getElementsByName("nodeLabel")[0].value;
		var thisnode = handNodeNameOnSave(nodename);
		
		var events = thisnode.getElementsByTagName("event");
		for (var i = 0; i < events.length; i++) {
			if (events[i].getAttributeNode("type") != null
					&& events[i].getAttributeNode("type").value == "node-enter") {
				nodeEnterEvent = events[i];
			} else if (events[i].getAttributeNode("type") != null
					&& events[i].getAttributeNode("type").value == "node-leave") {
				nodeLeaveEvent = events[i];
			}
		}

		// 子流程属性设置
		
		
		
		//zw
		//设置节点进入的事件动作名称
		var eaNodeEnterName = document.getElementsByName("node-enter-name")[0].value;
		opener.fSelectedObj.eaNodeEnterName = "";
		if(eaNodeEnterName != "")
			opener.fSelectedObj.eaNodeEnterName = eaNodeEnterName;
		
		//设置节点离开的事件动作名称
		var eaNodeLeaveName = document.getElementsByName("node-leave-name")[0].value;
		opener.fSelectedObj.eaNodeLeaveName = "";
		if(eaNodeLeaveName != "")
			opener.fSelectedObj.eaNodeLeaveName = eaNodeLeaveName;


		// 设置节点进入操作
		var nodeenter = document.getElementsByName("nodeenteraction")[0].value;
		if (nodeenter != null && "" != nodeenter) {
			opener.fSelectedObj.nodeEnter = nodeenter;
			if (nodeEnterEvent != null) {
				var actions = nodeEnterEvent.getElementsByTagName("action");
				var action = null;//子流程节点进入事件记录变量
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].getAttributeNode("name") != null
							&& actions[i].getAttributeNode("name").value == opener.nodeEnterName) {//查找自定义节点进入事件
						actions[i].parentNode.removeChild(actions[i]);
						continue;
					}
					if (actions[i].getAttributeNode("name") != null
							&& actions[i].getAttributeNode("name").value == opener.subProcessStartName) {//查找子流程节点进入事件
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
				
				nodeEnterEvent.insertBefore(n2, action);//子流程节点进入事件不为空，则需要将节点进入事件加到子流程节点进入事件之前
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
		opener.fSelectedObj.nodeForm = document
				.getElementsByName("togetherform")[0].value
				+ "," + document.getElementsByName("formid")[0].value;
		opener.fSelectedObj.formPriv = document.getElementsByName("formpriv")[0].value;
		if(document.getElementsByName("subProcessId")[0].value != null && document.getElementsByName("subProcessId")[0].value != ""){//增加子流程Id保存功能
			opener.fSelectedObj.subProcessName = document.getElementsByName("subProcessId")[0].value + "," + document.getElementsByName("subProcessName")[0].value;
		}else{//兼容旧的流程模板文件格式
			opener.fSelectedObj.subProcessName = document.getElementsByName("subProcessName")[0].value;
		}
		//opener.fSelectedObj.subProcessName = document
				//.getElementsByName("subProcessName")[0].value;
		opener.fSelectedObj.subProcessForm = document
				.getElementsByName("subProcessForm")[0].value
				+ "," + document.getElementsByName("subFormid")[0].value;
		if (document.getElementsByName("synchronize")[0].checked) {
			opener.fSelectedObj.synchronize = "1";
		} else {
			opener.fSelectedObj.synchronize = "0";
		}

		if (document.getElementsByName("fromParent")[0].checked) {
			opener.fSelectedObj.fromParent = "1";
		} else {
			opener.fSelectedObj.fromParent = "0";
		}

		if (document.getElementsByName("toParent")[0].checked) {
			opener.fSelectedObj.toParent = "1";
		} else {
			opener.fSelectedObj.toParent = "0";
		}
		
		ownedHandlePlugins();
		
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
		
		window.close();
		opener.movecontrolSubject.notifyObservers();
	} catch (e) {
		alert('关闭属性设置对话框时出错！!');
		window.close();
	}
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

function isSetAction() {
	var sa = document.getElementsByName("isSetAction")[0];
	if (sa.checked) {
		document.getElementById("setform").disabled = "true";
		document.getElementById("setpriv").disabled = "true";
		document.getElementsByName("actionSet")[0].disabled = "";
	} else if (!sa.checked) {
		document.getElementById("setform").disabled = "";
		document.getElementById("setpriv").disabled = "";
		document.getElementsByName("actionSet")[0].disabled = "true";
	}
}

$(document).ready(function(){
	iniAttributeDialog();
});

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
	
	if (isValidated && (document.getElementsByName("subProcessName")[0].value == null
			|| "" == document.getElementsByName("subProcessName")[0].value)) {
		alert("请指定子流程。");
		isValidated = false;
	}
	if (isValidated && (document.getElementsByName("subProcessForm")[0].value == null
			|| "" == document.getElementsByName("subProcessForm")[0].value)) {
		alert("请为子流程设置表单。");
		isValidated = false;
	}
	if (isValidated){
		isValidated=checkIllegalCharacters(document.getElementsByName("plugins_subBusiName")[0].value);
	}
	
	
	// 执行自定义验证
	if (isValidated && typeof(customValidate) != "undefined") {
		isValidated = customValidate.call(this);
	}
	return isValidated;
}