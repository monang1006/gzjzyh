var currentStatus = window.dialogArguments.currentStatus;
var opener = window.dialogArguments;
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
		document.getElementsByName("nodeleaveaction")[0].value = opener.fSelectedObj.nodeLeave;
		document.getElementsByName("nodeenteraction")[0].value = opener.fSelectedObj.nodeEnter;
		document.getElementsByName("nodeDelAction")[0].value = opener.fSelectedObj.nodeDelegation;
		document.getElementsByName("togetherform")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[0];
		document.getElementsByName("formid")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[1];
		document.getElementsByName("formpriv")[0].value = opener.fSelectedObj.formPriv;
		document.getElementsByName("nodeType")[0].value = "自动节点";
		
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
		
		if(opener.fSelectedObj.eaNodeDelegateName == null && opener.fSelectedObj.eaNodeDelegateName == undefined){
			document.getElementsByName("node-delegate-name")[0].value = "";
		}else{
			document.getElementsByName("node-delegate-name")[0].value = opener.fSelectedObj.eaNodeDelegateName;
		}
		
		
		// 后置迁移类型
		if (opener.fSelectedObj.preTranType == "1") {
			document.getElementsByName("preTranType")[1].checked = "true";
		} else {
			document.getElementsByName("preTranType")[0].checked = "true";
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
	try {
		var nodename = document.getElementsByName("nodeLabel")[0].value;
		var thisnode = handNodeNameOnSave(nodename);
		
		var nodeEnterEvent = null;
		var nodeLeaveEvent = null;

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

		// 设置节点代理Action
		var nodeDelAction = document.getElementsByName("nodeDelAction")[0].value;
		if (nodeDelAction != null && "" != nodeDelAction) {
			opener.fSelectedObj.nodeDelegation = nodeDelAction;
			var k = 1;
			var actions = thisnode.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeDelegationName) {
					actions[i].getAttributeNode("class").value = nodeDelAction;
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
				r.value = opener.nodeDelegationName;
				n2.setAttributeNode(r);
				var r = opener.doc.createAttribute("class");
				r.value = nodeDelAction;
				n2.setAttributeNode(r);
				thisnode.appendChild(n2);
			}
		} else {
			opener.fSelectedObj.nodeDelegation = "";
			var actions = thisnode.getElementsByTagName("action");
			for (var i = 0; i < actions.length; i++) {
				if (actions[i].getAttributeNode("name") != null
						&& actions[i].getAttributeNode("name").value == opener.nodeDelegationName) {
					actions[i].parentNode.removeChild(actions[i]);
					break;
				}
			}
		}
		

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
		
		//设置节点代理的事件动作名称
		var eaNodeDelegateName = document.getElementsByName("node-delegate-name")[0].value;
		opener.fSelectedObj.eaNodeDelegateName = "";
		if(eaNodeDelegateName != "")
			opener.fSelectedObj.eaNodeDelegateName = eaNodeDelegateName;

		
		// 设置节点进入操作
		var nodeenter = document.getElementsByName("nodeenteraction")[0].value;
		if (nodeenter != null && "" != nodeenter) {
			opener.fSelectedObj.nodeEnter = nodeenter;
			if (nodeEnterEvent != null) {
				var k = 1;
				var actions = nodeEnterEvent.getElementsByTagName("action");
				for (var i = 0; i < actions.length; i++) {
					if (actions[i].getAttributeNode("name") != null
							&& actions[i].getAttributeNode("name").value == opener.nodeEnterName) {
						actions[i].getAttributeNode("class").value = nodeenter;
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
					r.value = opener.nodeEnterName;
					n2.setAttributeNode(r);
					var r = opener.doc.createAttribute("class");
					r.value = nodeenter;
					n2.setAttributeNode(r);
					nodeEnterEvent.appendChild(n2);
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
		
		if (document.getElementsByName("preTranType")[1].checked) {
			opener.fSelectedObj.preTranType = "1";
		} else {
			opener.fSelectedObj.preTranType = "0";
		}

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
	
	// 执行自定义验证
	if (isValidated && typeof(customValidate) != "undefined") {
		isValidated = customValidate.call(this);
	}
	return isValidated;
}