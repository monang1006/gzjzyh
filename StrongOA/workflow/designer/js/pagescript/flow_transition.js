var currentStatus = window.dialogArguments.currentStatus;

function iniAttributeDialog() {
	var opener = window.dialogArguments;
	if (opener.fSelectedObj != null) {
		document.getElementsByName("lineId")[0].value = opener.fSelectedObj.id;
		document.getElementsByName("lineName")[0].value = opener.fSelectedObj.nodeclass.thisCaption;
		document.getElementsByName("startNode")[0].value = opener.fSelectedObj.fromelement.nodeclass.thisCaption;
		document.getElementsByName("endNode")[0].value = opener.fSelectedObj.toelement.nodeclass.thisCaption;
		if (opener.fSelectedObj.isStraint == 0)
			document.getElementsByName("lineType")[1].checked = true;
		else
			document.getElementsByName("lineType")[0].checked = true;
		
		// 初始化迁移线插件信息
		var $plugins = $("[name^=" + pluginNamePrefix + "]");
		$.each($plugins, function(index, domElement) {
			$(domElement).val(opener.fSelectedObj.plugins[domElement.name]);
		});
		
		if (typeof(initPlugins) != "undefined") {// 用户需要在页面重载的方法，初始化迁移线插件信息，主要是一些插件属性的展现方式
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
	var thisline;
	try {
		var linename = document.getElementsByName("lineName")[0].value;
		//zw
		linename = linename.replace(/[ 　]/g, "");
		
		if (linename != null
				&& linename != opener.fSelectedObj.nodeclass.thisCaption) {
			var lines = opener.doc.getElementsByTagName("transition");
			for (var i = 0; i < lines.length; i++) {
				if (lines[i].getAttributeNode("atnode").value == opener.fSelectedObj.id) {
					lines[i].getAttributeNode("name").value = linename;
					opener.fSelectedObj.nodeclass.thisCaption = linename;
					opener.fSelectedObj.nodeclass.textobj.innerText = linename;
					break;
				}
			}
			if (document.getElementsByName("lineType")[0].checked) {
				opener.fSelectedObj.isStraint = 1;
				opener.fSelectedObj.nodeclass.movepoint1.style.display = "none";
				opener.fSelectedObj.nodeclass.movepoint2.style.display = "none";
			} else {
				opener.fSelectedObj.isStraint = 0;
				opener.fSelectedObj.nodeclass.movepoint1.style.display = "block";
				opener.fSelectedObj.nodeclass.movepoint2.style.display = "block";
			}

		} else if (linename == opener.fSelectedObj.nodeclass.thisCaption) {
			if (document.getElementsByName("lineType")[0].checked) {
				opener.fSelectedObj.isStraint = 1;
				opener.fSelectedObj.nodeclass.movepoint1.style.display = "none";
				opener.fSelectedObj.nodeclass.movepoint2.style.display = "none";
			} else {
				opener.fSelectedObj.isStraint = 0;
				opener.fSelectedObj.nodeclass.movepoint1.style.display = "block";
				opener.fSelectedObj.nodeclass.movepoint2.style.display = "block";
			}
		} else {
		}
		
		if(typeof(handlePlugins) != "undefined"){//处理迁移线插件信息方法，用户需重载此方法，主要处理插件信息由展现信息到具体后台值
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
		alert(e);
		alert('关闭属性设置对话框时出错！');
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

$(document).ready(function() {
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
	var linename = document.getElementsByName("lineName")[0].value;
	//zw
	linename = linename.replace(/[ 　]/g, "");
	if(linename == ""){
		alert("请输入路由名称。");
		isValidated = false;
		return false;
	}
	if(isValidated){
		isValidated = checkIllegalCharacters(linename);
	}
	// 执行自定义验证
	if (isValidated && typeof(customValidate) != "undefined") {
		isValidated = customValidate.call(this);
	}
	return isValidated;
}
