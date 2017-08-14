
/*
�ýű�����toolbar��menu�Ĳ��������ԭ�ű����η�ʽ��ͳһ���ű�����ά�������⡣
��Ҫ���˼���ǣ�
1�����еĲ������toolbar��ҳ���Ͻ��д��?
2��������Ҫ����Ĳ�����toolbar���������ر?��
3��ͳһ���η�ʽ����������action�����������жϡ�
*/
var _top = null;
var _left = null;
var _width = null;
var _height = null;
var _confirmMessage = "";
var _isBlank = "0";

//����:
//��targetStr��confirmMessage��isBlank��targetStr��λ��
//actionStr ����
//targetStr Ŀ���ܣ�null��ʹ��Ĭ�ϣ�
//isSingle0perate �Ƿ񵥲����0������Ƿ�Ϊ�գ���1������Ƿ����¼�Ƿ�Ϊ�գ�nullʱ�����ids����ʱ����
//_isBlank Ĭ��Ϊ��0���ύ�?�� ��1������ڣ���2������ģ̬����
//confirmMessage ȷ����Ϣ��
function complexOperate(actionStr, targetStr, isSingle0perate, confirmMessage, isBlank, width, height, top, left) {
	try {
		document.submitForm.action = actionStr;
		if (null != targetStr) {
			document.submitForm.target = targetStr;
		} else {
			if (null != document.all("defaultTarget")) {
				document.submitForm.target = document.all("defaultTarget").value;
			} else {
				document.submitForm.target = getDefaultTarget();
			}
		}
		if (null != isSingle0perate) {
			if ("" == document.all("ids").value) {
				alert("\ufffd\ufffd\u0461\ufffd\ufffd\u04aa\ufffd\ufffd\ufffd\ufffd\u013c\ufffd\xbc\ufffd\ufffd");
				return false;
			}
			if ("1" == isSingle0perate) {
				if (!isSingleRecord(document.all("ids").value)) {
					return false;
				}
			}
		}
		if (null != confirmMessage) {
			_confirmMessage = confirmMessage;
		}
		if (null != isBlank) {
			_isBlank = isBlank;
			if (null == top) {
				top = (screen.height - height) / 2;
				if (top < 0) {
					top = 0;
				}
			}
			if (null == left) {
				left = (screen.width - width) / 2;
				if (left < 0) {
					left = 0;
				}
			}
			_top = top;
			_left = left;
			_width = width;
			_height = height;
		}
		gotoSubmitForm();
		//document.all("submitButtonHidden").click();
	}
	catch (e) {
	}
	finally {
		_resetSubmitForm();
	}
}
function _resetSubmitForm() {
	_isBlank = "0";
	_confirmMessage = "";
	if (null != document.all("isReset") && "1" == document.all("isReset").value) {
		resetSubmitForm();
	}
}

//�����ģ̬ҳ��ʱ���ظ�ҳ���Ӧ�ã����򷵻�null
function runSubmitForm(paraStr) {
	if ("" != _confirmMessage && null != _confirmMessage) {
		if (!confirm(_confirmMessage)) {
			return null;
		}
	}
	if ("0" == _isBlank) {
		document.submitForm.submit();
		return null;
	} else {
		var urlStr = document.submitForm.action;
		if (-1 == urlStr.indexOf("?")) {
			urlStr += "?";
		} else {
			urlStr += "&";
		}
		if (null != paraStr && "" != paraStr) {
			urlStr += paraStr;
		} else {
			urlStr += getParamFromForm(document.submitForm);
		}
		if ("2" == _isBlank) {
			showModalDialog(urlStr, window, "dialogWidth:" + _width + "px;dialogHeight:" + _height + "px;dialogTop:" + _top + "px;dialogLeft:" + _left + "px;status:no");
			return null;
		}
		//'1' == _isBlank
		return window.open(urlStr, "causeshow", "scrollbars=yes,width=" + _width + ",height=" + _height + ",top=" + _top + ",left=" + _left);
	}
}

//��ȡ�?�Ĳ�����Ϊurl��ַ�Ĳ���
function getParamFromForm(formObj) {
	var paramStr = "";
	for (var i = 0; i < formObj.elements.length; i++) {
		paramStr += formObj.elements[i].name + "=" + formObj.elements[i].value + "&";
	}
	return paramStr;
}

//���ڸ�ģ����д���
function runSubmitForm_outBudget(paraStr) {
	var openWindow = runSubmitForm(paraStr);
	return openWindow;
}

//ȡ��ѡ���ֵ��ת��Ϊ�ַ��á������ָ�
function getCheckboxValue(chkName, frame) {
	if (null == frame) {
		frame = window;
	}
	if (null == frame.document.getElementsByName(chkName)) {
		return "";
	}
	var chkObj = frame.document.getElementsByName(chkName);
	var chkValueStr = "";
	for (i = 0; i < chkObj.length; i++) {
		if (chkObj[i].checked) {
			chkValueStr += chkObj[i].value + ",";
		}
	}
	chkValueStr = chkValueStr.substring(0, chkValueStr.lastIndexOf(","));
	return chkValueStr;
}

//ȡ��ѡ���ֵ
function getRadioValue(radioName, frame) {
	if (null == frame) {
		frame = window;
	}
	if (null == frame.document.getElementsByName(radioName)) {
		return "";
	}
	var radioObj = frame.document.getElementsByName(radioName);
	var radioValue = "";
	for (i = 0; i < radioObj.length; i++) {
		if (radioObj[i].checked) {
			radioValue = radioObj[i].value;
		}
	}
	return radioValue;
}

//ȡ��-�б���ֵ
function getListValue(listName, frame) {
	if (null == frame) {
		frame = window;
	}
	if (null == frame.document.all(listName)) {
		return "";
	}
	var listObj = frame.document.all(listName);
	var listValue = "";
	for (i = 0; i < listObj.length; i++) {
		if (listObj[i].selected) {
			listValue = listObj[i].value;
		}
	}
	return listValue;
}

//ȡ�ı����ֵ
function getTextValue(textName, frame) {
	if (null == frame) {
		frame = window;
	}
	if (null == frame.document.all(textName)) {
		return "";
	}
	return frame.document.all(textName).value;
}

//���ı���ֵ
function setTextValue(textValue, textName, frame) {
	if (null == frame) {
		frame = window;
	}
	if (null != frame.document.all(textName)) {
		frame.document.all(textName).value = textValue;
	}
}

//��TOOLBAR���ı���ֵ
function setToolBarTextValue(textValue, textName) {
	var frame = top.perspective_content.project_properties_container.project_properties_toolbar;
	setTextValue(textValue, textName, frame);
}

//��ȡTOOLBAR����
function getToolBarHiddenObj(hiddenName) {
	var frame = top.perspective_content.project_properties_container.project_properties_toolbar;
	return frame.document.all(hiddenName);
}

//�ύtoolbar�Ľű�
function runToolBarAction(actionName) {
	var frame = top.perspective_content.project_properties_container.project_properties_toolbar;
	frame.eval(actionName + "()");
}

//�ж��Ƿ����¼
function isSingleRecord(ids) {
	if (-1 == ids.indexOf(",")) {
		return true;
	} else {
		alert("\u05bb\ufffd\ufffd\u0461\ufffd\ufffd\ufffd\ufffd\ufffd\xbc\ufffd\ufffd");
		return false;
	}
}

//����ids�Ӹ�ѡ���ֵ
function setIdsForOperate(destName, origName, frame) {
	var ids = getCheckboxValue(origName, frame);
	setToolBarTextValue(ids, destName);
}

//�ı��ı����ֵ
//���Ϊ��0�����Ϊ��%��
//���ǡ�0����������%
function changeBaseDataForQuery(textName, frame) {
	var thisDocument;
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = document;
	}
	if (null == thisDocument.all(textName)) {
		return false;
	}
	if (thisDocument.all(textName).value == "0" || thisDocument.all(textName).value == "") {
		thisDocument.all(textName).value = "%";
	} else {
		if (thisDocument.all(textName).value.indexOf("%") != -1) {
		//���С�%�����봦��
		} else {
			thisDocument.all(textName).value += "%";
		}
	}
}

//ȡ���ı����'%'
function backChangeBaseDataForQuery(textName, frame) {
	var thisDocument;
	var textValue;
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = document;
	}
	if (null == thisDocument.all(textName)) {
		return false;
	} else {
		textValue = thisDocument.all(textName).value;
	}
	if (textValue == "0") {
		thisDocument.all(textName).value = "";
	}
	if (textValue.indexOf("%") == -1) {
		return false;
	} else {
		thisDocument.all(textName).value = textValue.substr(0, textValue.indexOf("%"));
	}
}

//�л���
//divArray  �������磺new Array(null,"finction1","finction1", frame) ��ʾ���ڵ�һλ�Ĳ�
function switchArrayDiv(divArray, frame) {
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = document;
	}
	if (null == divArray || divArray.length < 1) {
		return false;
	}
	var divItems = new Array();
	if (thisDocument.getElementById) {
		for (var i = 0; i < divArray.length; i++) {
			if (null != divArray[i]) {
				divItems[i] = thisDocument.getElementById(divArray[i]);
				if (0 == i) {
					divItems[0].style.display = "";
				} else {
					divItems[i].style.display = "none";
				}
			}
		}
	}
}
function autoSwitchDisplay(id1, id2, frame) {
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = document;
	}
	var itm1 = null;
	var itm2 = null;
	if (thisDocument.getElementById) {
		itm1 = thisDocument.getElementById(id1);
		itm2 = thisDocument.getElementById(id2);
	}
	if (!itm1 || !itm2) {
	 // do nothing
	} else {
		if (itm1.style && itm2.style) {
			if ("" == itm1.style.display) {
				itm1.style.display = "none";
				itm2.style.display = "";
			} else {
				itm1.style.display = "";
				itm2.style.display = "none";
			}
		}
	}
}


//��ȡ���
function getToolbarFrame() {
	return top.perspective_content.project_properties_container.project_properties_toolbar;
}
function getProjectContentFrame() {
	return top.perspective_content.project_properties_container.frames[1];
}
function getProjectMainFrame() {
	return top.perspective_content.project_properties_container.project_properties_content.frames[1];
}
function getProjectMainFirstFrame() {
	return top.perspective_content.project_properties_container.project_properties_content.project_main.frames[0];
}
function getProjectMainSecondFrame() {
	return top.perspective_content.project_properties_container.project_properties_content.project_main.frames[1];
}
function getProjectMainTopFrame() {
	return top.perspective_content.project_properties_container.project_properties_content.project_main.project_main_top;
}

//��ʾ����
var loadCompanyTree = 1;
function displayTree(divTree, divNoTree) {
	autoSwitchDisplay(divTree, divNoTree);
	var propertiesContentFrameset = getProjectContentFrame().document.getElementsByTagName("frameset").item(0);
	if (propertiesContentFrameset.cols == "0,*") {
		propertiesContentFrameset.cols = "25%,*";
		if (loadCompanyTree == 1) {
			parent.project_properties_content.project_tree.location = dispayTreeURL;
			loadCompanyTree = 2;
		}
	} else {
		propertiesContentFrameset.cols = "0,*";
	}
}

//
function checkAllButton(id1, id2, buttomName, frame) {
	autoSwitchDisplay(id1, id2);
	if (null == buttomName) {
		buttomName = "chkButton";
	}
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = getProjectMainFrame().document;
	}
	var el = thisDocument.getElementsByName(buttomName);
	for (i = 0; i < el.length; i++) {
		el[i].checked = true;
	}
}
function checkInsteadButton(id1, id2, buttomName, frame) {
	autoSwitchDisplay(id1, id2);
	if (null == buttomName) {
		buttomName = "chkButton";
	}
	if (null != frame) {
		thisDocument = frame.document;
	} else {
		thisDocument = getProjectMainFrame().document;
	}
	var el = thisDocument.getElementsByName(buttomName);
	for (i = 0; i < el.length; i++) {
		if (el[i].checked == true) {
			el[i].checked = false;
		} else {
			el[i].checked = true;
		}
	}
}

//�Ҽ�˵���5����
function checkAddDis(type, ids, names) {
	return true;
	if (type == "enabled") {
		return false;
	}
	return true;
}
function checkModifyDis(type, ids, names) {
	if (type == "display") {
		return true;
	} else {
		if (names.length != 1) {
			return false;
		}
		return true;
	}
}
function canSuspend(type, ids, names) {
	if (type == "display") {
		if (names.length != 1) {
			return false;
		}
		if (ids[0][2] == "true") {
			return false;
		}
		return true;
	} else {
		if (names.length != 1) {
			return false;
		}
		return true;
	}
}
function canRestore(type, ids, names) {
	if (type == "display") {
		if (names.length != 1) {
			return false;
		}
		if (ids[0][2] == "false") {
			return false;
		}
		return true;
	} else {
		if (names.length != 1) {
			return false;
		}
		return true;
	}
}

