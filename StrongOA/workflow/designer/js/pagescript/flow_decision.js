var currentStatus = window.dialogArguments.currentStatus;
var opener = window.dialogArguments;
var logicSet = null;
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
		initOptions();
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
		document.getElementsByName("togetherform")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[0];
		document.getElementsByName("formid")[0].value = opener.fSelectedObj.nodeForm
				.split(",")[1];
		document.getElementsByName("formpriv")[0].value = opener.fSelectedObj.formPriv;
		
		
		//设置默认的处理类路径
		if(opener.fSelectedObj.decideHandleClass == "" || opener.fSelectedObj.decideHandleClass == null || opener.fSelectedObj.decideHandleClass == undefined){
			document.getElementsByName("decide-handle-class")[0].value = decideHandleClass;
		}else{
			//add by caidw
			//增加判断处理类路径信息
			document.getElementsByName("decide-handle-class")[0].value = opener.fSelectedObj.decideHandleClass;
		}
		//设置处理类名称
		if(opener.fSelectedObj.decideHandleName == "" || opener.fSelectedObj.decideHandleName == null || opener.fSelectedObj.decideHandleName == undefined){
			document.getElementsByName("decide-handle-name")[0].value = "";
		}else{
			document.getElementsByName("decide-handle-name")[0].value = opener.fSelectedObj.decideHandleName;
		}

		logicSet = opener.fSelectedObj.logicSet;

		document.getElementsByName("nodeType")[0].value = "条件节点";
		var obj = document.getElementsByName("logicSets")[0];
		var sets = logicSet.split("$");
		if (sets != "") {
			for (var i = 0; i < sets.length - 1; i = i + 2) {
				var op = new Option(sets[i + 1], sets[i]);
				obj.options.add(op);
			}
			document.getElementById("elseSelect").value = sets[sets.length - 1];
		}
		initOptions();
		
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

function initOptions() {
	var conditionItem = document.getElementById("conditionItem");
	// 清空原有表单域选项
	for (var k = 0; k < conditionItem.options.length;) {
		if (conditionItem.options[k].value.indexOf("[挂接表单.") != -1) {
			conditionItem.options[k] = null;
		} else {
			k++;
		}
	}
	var formId = document.getElementsByName("formid")[0].value;
	if (formId == "0") {
		formId = opener.processForm.split(",")[1];
	}
	// jquery获取表单所有域
	$.ajax({
		url : scriptroot
				+ "/workflowDesign/action/processDesign!getFormDomains.action",
		type : "post",
		dataType : "text",
		data : "formId=" + formId,
		success : function(msg) {
			if (msg != "") {
				var domain = msg.split("|");
				for (var j = 0; j < domain.length; j++) {
					var objOption = new Option("[挂接表单."
							+ domain[j].split(",")[1] + "]", "[挂接表单."
							+ domain[j].split(",")[0] + "]");
					conditionItem.options.add(objOption,
							conditionItem.options.length);
				}
			}
		}
	});
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
	var beforeClass = null;
	var script = null;
	var handle = null;
	var nodename = document.getElementsByName("nodeLabel")[0].value;
	
	var thisnode = handNodeNameOnSave(nodename);
	
	var handles = thisnode.getElementsByTagName("handler");
	for (var i = 0; i < handles.length; i++) {
		if (handles[i].getAttributeNode("name") != null
				&& handles[i].getAttributeNode("name").value == opener.decideHandleName) {
			handle = handles[i];
			break;
		}
	}

	//add by caidw
	//设置处理类名称
	var decideHandleName = document.getElementsByName("decide-handle-name")[0].value;
	opener.fSelectedObj.decideHandleName = "";
	if(decideHandleName != ""){
		opener.fSelectedObj.decideHandleName = decideHandleName;
	}
	
	//add by caidw
	//增加判断处理类路径信息
	var decideHandle = document.getElementsByName("decide-handle-class")[0].value;
	if(decideHandle == null || decideHandle == ""){
		opener.fSelectedObj.decideHandleClass = decideHandleClass;
	}else{
		opener.fSelectedObj.decideHandleClass = decideHandle;
	}
	var handles = thisnode.getElementsByTagName("handler");
	for (var i = 0; i < handles.length; i++) {
		if (handles[i].getAttributeNode("name") != null
				&& handles[i].getAttributeNode("name").value == opener.decideHandleName) {
			handle = handles[i];
			break;
		}
	}

	
	//add by caidw 添加处理类
	if (handle == null) {
		handle = opener.doc.createNode(1, "handler", "");

		// 添加xml节点标识属性
		var r = opener.doc.createAttribute("xmlflag");
		r.value = opener.nCtlNum++;
		// 添加属性
		handle.setAttributeNode(r);

		var r = opener.doc.createAttribute("name");
		r.value = opener.decideHandleName;
		handle.setAttributeNode(r);
		var r = opener.doc.createAttribute("class");
		
		//增加条件节点中条件处理类的路径设置
		//r.value = opener.decideHandleClass;
		r.value = opener.fSelectedObj.decideHandleClass;
		
		handle.setAttributeNode(r);
		thisnode.appendChild(handle);
	}else{
		//增加条件节点中条件处理类的路径设置
		handle.getAttributeNode("class").value = opener.fSelectedObj.decideHandleClass;
	}
	opener.fSelectedObj.nodeForm = document.getElementsByName("togetherform")[0].value
			+ "," + document.getElementsByName("formid")[0].value;
	opener.fSelectedObj.formPriv = document.getElementsByName("formpriv")[0].value;

	logicSet = "";
	var obj = document.getElementsByName("logicSets")[0];
	if (obj.options.length != 0) {
		for (var i = 0; i < obj.options.length; i++) {
			logicSet = logicSet + obj.options[i].value + "$"
					+ obj.options[i].text + "$";
		}
	}
	logicSet = logicSet + document.getElementById("elseSelect").value;
	opener.fSelectedObj.logicSet = logicSet;
	
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

// 保存focus的输入框
var selectInput = null;
// 保存选择的条件组
var selectIndex = null;

// 增加一行TR
function addNewTR() {
	var table = document.getElementById("logicTable");
	var tr = table.insertRow(-1);
	var td1 = tr.insertCell(-1);
	td1.setAttribute("width", "40%");
	td1.innerHTML = "<input type=\"text\" style=\"width:95%\" onfocus=\"focusInput(this)\" class=txtput>";
	var td2 = tr.insertCell(-1);
	td2.setAttribute("width", "10%");
	td2.innerHTML = "<select class=txtput><option value=\"small\">&lt;</option><option value=\"smallE\">&lt;=</option><option value=\"big\">&gt;</option><option value=\"bigE\">&gt;=</option><option value=\"NumEqu\">数值相等</option><option value=\"NumNEqu\">数值不等</option><option value=\"StrEqu\">字符相等</option><option value=\"StrNEqu\">字符不等</option><option value=\"isNull\">为空</option><option value=\"notNull\">不为空</option><option value=\"isGroupOf\">岗位为</option></select>";
	var td3 = tr.insertCell(-1);
	td3.setAttribute("width", "20%");
	td3.innerHTML = "<input type=\"text\" style=\"width:95%\" class=txtput>";
	var td4 = tr.insertCell(-1);
	td4.setAttribute("width", "10%");
	td4.innerHTML = "<select class=txtput><option value=\"并且\">并且</option><option value=\"或者\">或者</option></select>";
	var td5 = tr.insertCell(-1);
	td5.setAttribute("width", "20%");
	td5.innerHTML = "<a href='#' title='增加' onclick='addNewTR()'><img src='"
			+ opener.systemroot
			+ "/images/workflow/drop-add.gif' border='0' align='absmiddle'/></a>"
			+ "<a href='#' title='删除' onclick='delTR(this)'><img src='"
			+ opener.systemroot
			+ "/images/workflow/delete.gif' border='0' align='absmiddle'/></a>";
}

// 增加一行已经存在的TR
function addEditTR(str1, str2, str3, str4) {
	var table = document.getElementById("logicTable");
	var tr = table.insertRow(-1);
	var td1 = tr.insertCell(-1);
	td1.setAttribute("width", "40%");
	td1.innerHTML = "<input type=\"text\" style=\"width:95%\" onfocus=\"focusInput(this)\" value=\""
			+ str1 + "\" class=txtput>";
	var td2 = tr.insertCell(-1);
	td2.setAttribute("width", "10%");
	td2.innerHTML = "<select class=txtput><option value=\"small\">&lt;</option><option value=\"smallE\">&lt;=</option><option value=\"big\">&gt;</option><option value=\"bigE\">&gt;=</option><option value=\"NumEqu\">数值相等</option><option value=\"NumNEqu\">数值不等</option><option value=\"StrEqu\">字符相等</option><option value=\"StrNEqu\">字符不等</option><option value=\"isNull\">为空</option><option value=\"notNull\">不为空</option><option value=\"isGroupOf\">岗位为</option></select>";
	td2.childNodes[0].value = str2;
	var td3 = tr.insertCell(-1);
	td3.setAttribute("width", "20%");
	td3.innerHTML = "<input type=\"text\" style=\"width:95%\" value=\"" + str3
			+ "\" class=txtput>";
	var td4 = tr.insertCell(-1);
	td4.setAttribute("width", "10%");
	td4.innerHTML = "<select class=txtput><option value=\"并且\" selected>并且</option><option value=\"或者\">或者</option></select>";
	if (str4 != "") {
		td4.childNodes[0].value = str4;
	}
	var td5 = tr.insertCell(-1);
	td5.setAttribute("width", "20%");
	td5.innerHTML = "<a href='#' title='增加' onclick='addNewTR()'><img src='"
			+ opener.systemroot
			+ "/images/workflow/drop-add.gif' border='0' align='absmiddle'/></a>"
			+ "<a href='#' title='删除' onclick='delTR(this)'><img src='"
			+ opener.systemroot
			+ "/images/workflow/delete.gif' border='0' align='absmiddle'/></a>";
}

// 删除一行TR
function delTR(obj) {
	var tr = obj.parentNode.parentNode;
	tr.parentNode.removeChild(tr);
}

// 保存选定的INPUT
function focusInput(obj) {
	selectInput = obj;
}

// 将选中的内容拷贝到已保存的选定的input中
function defaultInput(obj) {
	if (selectInput != null) {
		selectInput.focus();
		//zw
		try{
			var str = window.clipboardData.getData("Text"); // 获得剪贴版的文字
			window.clipboardData.setData('Text', obj.value);
			document.execCommand('Paste');
			window.clipboardData.setData('Text', str); // 将原剪贴板里的文字贴还回去
		}
		catch(e){
			
		}
	}
	// 选择第一个
	document.getElementById("conditionItem").options[0].selected = true;
}

// 新增加一个转移条件组合
function newTransition() {
	var table = document.getElementById("logicTable");
	while (table.rows.length > 0) {
		table.rows[0].parentNode.removeChild(table.rows[0]);
	}
	selectInput = null;
	selectIndex = null;
	document.getElementById("transitionName").value = "";
	addNewTR();
}

// 确定一个转移条件组合
function addTransition() {
	var table = document.getElementById("logicTable");
	var rows = table.rows;
	var str = "";
	var textStr = "";
	for (var i = 0; i < rows.length; i++) {
		if (rows[i].cells[0].childNodes[0].value == "") {
			alert("请输入相应的表单域条件");
			return false;
		}else if(rows[i].cells[1].childNodes[0].value != "isNull"
			&& rows[i].cells[1].childNodes[0].value != "notNull"
				&& rows[i].cells[2].childNodes[0].value == "") {
			alert("请输入相应的判断条件");
			return false;
		}else {

			str = str + rows[i].cells[0].childNodes[0].value + ",";
			textStr = textStr + rows[i].cells[0].childNodes[0].value + " ";
			str = str + rows[i].cells[1].childNodes[0].value + ",";
			var tempValue = "";
			switch (rows[i].cells[1].childNodes[0].value) {
				case "small" :
					tempValue = "<";
					break;
				case "smallE" :
					tempValue = "<=";
					break;
				case "big" :
					tempValue = ">";
					break;
				case "bigE" :
					tempValue = ">=";
					break;
				case "NumEqu" :
					tempValue = "数值相等";
					break;
				case "NumNEqu" :
					tempValue = "数值不等";
					break;
				case "isGroupOf" :
					tempValue = "岗位为";
					break;
				case "StrEqu" :
					tempValue = "字符相等";
					break;
				case "StrNEqu" :
					tempValue = "字符不等";
					break;
				case "isNull" :
					tempValue = "为空";
					break;
				case "notNull" :
					tempValue = "不为空";
					break;
				default :
					tempValue = "<";

			}
			textStr = textStr + tempValue + " ";
			str = str + rows[i].cells[2].childNodes[0].value + ",";
			textStr = textStr + rows[i].cells[2].childNodes[0].value + " ";
			str = str + rows[i].cells[3].childNodes[0].value + "|";
			textStr = textStr + rows[i].cells[3].childNodes[0].value + " ";
		}
	}
	if (str == "") {
		return false;
	} else if (document.getElementById("transitionName").value == "") {
		alert("请输入该条件组要转移的转移名称！");
		return false;
	} else {
		if(!checkIllegalCharacters(document.getElementById("transitionName").value)){
			return false;
		}
		
		str = str.substring(0, str.length - 4) + "|";
		str = str + document.getElementById("transitionName").value;
		textStr = textStr.substring(0, textStr.length - 3) + " ";
		textStr = textStr + "转移路径为："
				+ document.getElementById("transitionName").value;
	}

	if (selectIndex == null) {
		var op = new Option(textStr, str);
		document.getElementsByName("logicSets")[0].options.add(op);
	} else {
		document.getElementsByName("logicSets")[0].options[selectIndex].value = str;
		document.getElementsByName("logicSets")[0].options[selectIndex].text = textStr;
	}
	newTransition();
}

// 双击条件组修改条件组
function initEditTransition(obj) {
	if (obj.options.length != 0) {
		selectInput = null;
		selectIndex = obj.selectedIndex;
		if(selectIndex != -1){
			var table = document.getElementById("logicTable");
			while (table.rows.length > 0) {
				table.rows[0].parentNode.removeChild(table.rows[0]);
			}
			var selectValue = obj.options[obj.selectedIndex];
			var values = selectValue.value.split("|");
			for (var i = 0; i < values.length - 1; i++) {
				var tds = values[i].split(",");
				if (tds.length == 4) {
					addEditTR(tds[0], tds[1], tds[2], tds[3]);
				} else {
					addEditTR(tds[0], tds[1], tds[2], "");
				}
			}
			document.getElementById("transitionName").value = values[values.length
					- 1];
		}
	}
}

// 删除一个条件组
function delTransition() {
	var obj = document.getElementsByName("logicSets")[0];
	var index = obj.selectedIndex;
	if (index == selectIndex) {
		obj.options[index] = null;
		newTransition();
	} else if (index == -1) {
		alert("请选择要删除的条件！");
		return false;
	} else {
		obj.options[index] = null;
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
	// 判断表单权限是否设置
	if (isValidated && (document.getElementsByName("togetherform")[0].value != null && document
			.getElementsByName("togetherform")[0].value != "")
			&& ("" == document.getElementsByName("formpriv")[0].value || null == document
					.getElementsByName("formpriv")[0].value)) {
		alert("请为挂接的表单设置权限。");
		isValidated = false;
	}
	// 判断默认路由是否设置
	if (isValidated && (document.getElementById("elseSelect").value == null
			|| document.getElementById("elseSelect").value == "")) {
		alert("请输入默认选择路由。");
		isValidated = false;
	}
	//校验默认路由字符是否合法
	if (isValidated){
		isValidated = checkIllegalCharacters(document.getElementById("elseSelect").value);
	}
	// 执行自定义验证
	if (isValidated && typeof(customValidate) != "undefined") {
		isValidated = customValidate.call(this);
	}
	return isValidated;
}